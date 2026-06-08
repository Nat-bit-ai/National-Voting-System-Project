CREATE TABLE IF NOT EXISTS fayda_citizens (
                                              fayda_id VARCHAR(16) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    sex VARCHAR(6) NOT NULL CHECK (sex IN ('Male', 'Female')),
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(13) UNIQUE,
    email VARCHAR(100) UNIQUE,
    region VARCHAR(50),
    district VARCHAR(50),
    kebele VARCHAR(50),
    citizenship_status VARCHAR(20) DEFAULT 'ETHIOPIAN'
    CHECK (citizenship_status = 'ETHIOPIAN'),
    qr_code_data TEXT UNIQUE,
    photo_path VARCHAR(255),
    fingerprint_hash TEXT,
    face_template TEXT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS civil_registry (
                                              id SERIAL PRIMARY KEY,
                                              fayda_id VARCHAR(16) UNIQUE NOT NULL
    REFERENCES fayda_citizens(fayda_id) ON DELETE CASCADE,
    is_alive BOOLEAN DEFAULT TRUE,
    date_of_death DATE,
    is_eligible BOOLEAN DEFAULT TRUE,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT death_check CHECK (
(is_alive = TRUE AND date_of_death IS NULL)
    OR
(is_alive = FALSE AND date_of_death IS NOT NULL)
    )
    );

INSERT INTO fayda_citizens(
    fayda_id, first_name, middle_name, last_name, sex, date_of_birth,
    phone_number, email, region, district, kebele, qr_code_data
)
VALUES
    ('FAYDA100001', 'Abebe', 'Bekele', 'Kebede', 'Male', '1995-03-12',
     '+251911000001', 'abebe@example.com', 'Addis Ababa', 'Bole', '01',
     'faydaId=FAYDA100001'),
    ('FAYDA100002', 'Hana', 'Tesfaye', 'Mekonnen', 'Female', '2001-08-24',
     '+251911000002', 'hana@example.com', 'Oromia', 'Adama', '03',
     'faydaId=FAYDA100002')
    ON CONFLICT (fayda_id) DO NOTHING;

INSERT INTO civil_registry(fayda_id, is_alive, is_eligible)
VALUES
    ('FAYDA100001', TRUE, TRUE),
    ('FAYDA100002', TRUE, TRUE)
    ON CONFLICT (fayda_id) DO NOTHING;
