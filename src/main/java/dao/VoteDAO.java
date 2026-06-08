package dao;

import model.Vote;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class VoteDAO extends AbstractDAO<Vote> {
    @Override
    public Vote create(Vote vote) throws SQLException {
        String sql = """
                INSERT INTO vote(candidate_election_id, anonymous_token)
                SELECT candidate_election_id, ?
                FROM candidate_election
                WHERE election_id = ? AND candidate_id = ?
                RETURNING vote_id
                """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vote.getAnonymousToken());
            ps.setInt(2, vote.getElectionId());
            ps.setInt(3, vote.getCandidateId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vote.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Candidate is not assigned to this election.");
                }
            }
        }
        return vote;
    }

    public boolean hasVoted(int voterId, int electionId) throws SQLException {
        String sql = "SELECT participation_status FROM voter_election WHERE voter_id=? AND election_id=?";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voterId);
            ps.setInt(2, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && "Voted".equals(rs.getString(1));
            }
        }
    }

    public void markVoted(int voterId, int electionId) throws SQLException {
        String sql = """
                INSERT INTO voter_election(voter_id, election_id, participation_status, vote_timestamp)
                VALUES(?,?, 'Voted', CURRENT_TIMESTAMP)
                ON CONFLICT(voter_id, election_id)
                DO UPDATE SET participation_status = 'Voted',
                              vote_timestamp = CURRENT_TIMESTAMP
                """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voterId);
            ps.setInt(2, electionId);
            ps.executeUpdate();
        }
    }

    public int totalVotes(int electionId) throws SQLException {
        String sql = """
                SELECT count(*)
                FROM vote v
                JOIN candidate_election ce ON ce.candidate_election_id = v.candidate_election_id
                WHERE ce.election_id=?
                """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public Optional<Vote> findById(int id) {
        throw new UnsupportedOperationException("Votes are anonymous and not browsed by ID.");
    }

    @Override
    public List<Vote> findAll() {
        throw new UnsupportedOperationException("Use the election_results view for reporting.");
    }

    @Override
    public boolean update(Vote entity) {
        throw new UnsupportedOperationException("Votes are immutable.");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Votes are immutable.");
    }
}
