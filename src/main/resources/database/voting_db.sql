CREATE TABLE IF NOT EXISTS administrator (
    admin_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(30) DEFAULT 'Admin',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'election_status') THEN
        CREATE TYPE election_status AS ENUM ('Draft', 'Upcoming', 'Active', 'Paused', 'Completed', 'Cancelled');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS election (
                                        election_id SERIAL PRIMARY KEY,
                                        admin_id INTEGER NOT NULL REFERENCES administrator(admin_id) ON DELETE RESTRICT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status election_status DEFAULT 'Upcoming',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_election_dates CHECK (start_date < end_date)
    );

CREATE TABLE IF NOT EXISTS political_party (
                                               party_id SERIAL PRIMARY KEY,
                                               party_name VARCHAR(150) NOT NULL UNIQUE,
    acronym VARCHAR(20) NOT NULL UNIQUE,
    party_logo VARCHAR(255),
    headquarters_address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS candidate (
                                         candidate_id SERIAL PRIMARY KEY,
                                         first_name VARCHAR(50) NOT NULL,
    mid_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    party_id INTEGER REFERENCES political_party(party_id) ON DELETE SET NULL,
    manifesto TEXT,
    photo_url VARCHAR(255),
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

ALTER TABLE IF EXISTS candidate
    ADD COLUMN IF NOT EXISTS party_id INTEGER REFERENCES political_party(party_id) ON DELETE SET NULL;
ALTER TABLE IF EXISTS candidate
    ADD COLUMN IF NOT EXISTS photo_url VARCHAR(255);
ALTER TABLE IF EXISTS candidate
    ADD COLUMN IF NOT EXISTS approved BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS candidate_election (
                                                  candidate_election_id SERIAL PRIMARY KEY,
                                                  candidate_id INTEGER NOT NULL REFERENCES candidate(candidate_id) ON DELETE CASCADE,
    election_id INTEGER NOT NULL REFERENCES election(election_id) ON DELETE CASCADE,
    party_id INTEGER REFERENCES political_party(party_id) ON DELETE SET NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approval_status VARCHAR(20) DEFAULT 'Pending'
    CHECK (approval_status IN ('Pending', 'Approved', 'Rejected')),
    CONSTRAINT uq_candidate_election UNIQUE (candidate_id, election_id)
    );

CREATE TABLE IF NOT EXISTS voter (
                                     voter_id SERIAL PRIMARY KEY,
                                     first_name VARCHAR(50) NOT NULL,
    mid_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    fayda_id VARCHAR(16) UNIQUE,
    date_of_birth DATE NOT NULL,
    region VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS voter_election (
                                              voter_election_id SERIAL PRIMARY KEY,
                                              voter_id INTEGER NOT NULL REFERENCES voter(voter_id) ON DELETE CASCADE,
    election_id INTEGER NOT NULL REFERENCES election(election_id) ON DELETE CASCADE,
    participation_status VARCHAR(20) DEFAULT 'Eligible'
    CHECK (participation_status IN ('Eligible', 'Voted', 'Revoked', 'Provisional', 'Challenged')),
    vote_timestamp TIMESTAMP,
    CONSTRAINT uq_voter_election UNIQUE (voter_id, election_id)
    );

CREATE TABLE IF NOT EXISTS vote (
                                    vote_id SERIAL PRIMARY KEY,
                                    candidate_election_id INTEGER NOT NULL
                                    REFERENCES candidate_election(candidate_election_id) ON DELETE CASCADE,
    anonymous_token VARCHAR(80) NOT NULL UNIQUE,
    vote_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS audit_log (
                                         log_id SERIAL PRIMARY KEY,
                                         actor_type VARCHAR(20) NOT NULL CHECK (actor_type IN ('VOTER', 'ADMIN', 'SYSTEM', 'Voter', 'Admin', 'System')),
    actor_id INTEGER,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ipaddress VARCHAR(45),
    device_info VARCHAR(255)
    );

CREATE INDEX IF NOT EXISTS idx_election_status ON election(status);
CREATE INDEX IF NOT EXISTS idx_vote_time ON vote(vote_time);
CREATE INDEX IF NOT EXISTS idx_candidate_election ON candidate_election(election_id);
CREATE INDEX IF NOT EXISTS idx_voter_region ON voter(region);
CREATE INDEX IF NOT EXISTS idx_audit_action_time ON audit_log(action_time);

CREATE OR REPLACE FUNCTION prevent_invalid_vote()
RETURNS TRIGGER AS $$
DECLARE
election_status_value election_status;
    election_start TIMESTAMP;
    election_end TIMESTAMP;
BEGIN
SELECT e.status, e.start_date, e.end_date
INTO election_status_value, election_start, election_end
FROM candidate_election ce
         JOIN election e ON ce.election_id = e.election_id
WHERE ce.candidate_election_id = NEW.candidate_election_id;

IF election_status_value <> 'Active' OR now() NOT BETWEEN election_start AND election_end THEN
        RAISE EXCEPTION 'Voting is only allowed in active elections';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_invalid_vote ON vote;
CREATE TRIGGER trg_prevent_invalid_vote
    BEFORE INSERT ON vote
    FOR EACH ROW EXECUTE FUNCTION prevent_invalid_vote();

CREATE OR REPLACE VIEW election_results AS
SELECT
    e.election_id,
    e.title AS election_title,
    c.candidate_id,
    trim(concat_ws(' ', c.first_name, c.mid_name, c.last_name)) AS candidate_name,
    COALESCE(pp.party_name, 'Independent') AS party_name,
    COUNT(v.vote_id)::INTEGER AS vote_count
FROM candidate_election ce
         JOIN election e ON ce.election_id = e.election_id
         JOIN candidate c ON ce.candidate_id = c.candidate_id
         LEFT JOIN political_party pp ON pp.party_id = COALESCE(ce.party_id, c.party_id)
         LEFT JOIN vote v ON v.candidate_election_id = ce.candidate_election_id
GROUP BY e.election_id, e.title, c.candidate_id, c.first_name, c.mid_name, c.last_name, pp.party_name;

INSERT INTO political_party(party_name, acronym, party_logo, headquarters_address)
VALUES ('Unity Democratic Party', 'UDP', 'src/main/resources/images/unity.png', 'Addis Ababa')
    ON CONFLICT (party_name) DO NOTHING;
