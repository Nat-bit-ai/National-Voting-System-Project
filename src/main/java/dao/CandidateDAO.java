package dao;

import model.Candidate;
import model.Party;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CandidateDAO extends AbstractDAO<Candidate> {
    @Override
    public Candidate create(Candidate candidate) throws SQLException {
        String sql = """
                INSERT INTO candidate(first_name, mid_name, last_name, party_id, photo_url, approved)
                VALUES(?,?,?,?,?,?) RETURNING candidate_id
                """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String[] name = splitName(candidate.getFullName());
            ps.setString(1, name[0]);
            ps.setString(2, name[1]);
            ps.setString(3, name[2]);
            setParty(ps, 4, candidate);
            ps.setString(5, candidate.getPhotoPath());
            ps.setBoolean(6, candidate.isApproved());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        }
        return candidate;
    }

    public void assignToElection(int candidateId, int electionId) throws SQLException {
// String sql = """
//        INSERT INTO candidate_election(candidate_id, election_id, party_id, approval_status)
//        SELECT candidate_id, ?, party_id,
//               (CASE
//                    WHEN approved THEN 'Approved'
//                    ELSE 'Pending'
//                END)::approval_status
//        FROM candidate
//        WHERE candidate_id = ?
//        ON CONFLICT (candidate_id, election_id) DO UPDATE
//        SET party_id = EXCLUDED.party_id,
//            approval_status = EXCLUDED.approval_status
//        """;
String sql = """
        INSERT INTO candidate_election(candidate_id, election_id, party_id, approval_status)
        SELECT candidate_id, ?, party_id,
               (CASE
                    WHEN approved THEN 'Approved'
                    ELSE 'Pending'
                END)::approval_status
        FROM candidate
        WHERE candidate_id = ?
        ON CONFLICT (candidate_id, election_id) DO UPDATE
        SET party_id = EXCLUDED.party_id,
            approval_status = EXCLUDED.approval_status
        """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ps.setInt(2, candidateId);
            ps.executeUpdate();
        }
    }

    public List<Candidate> findApprovedByElection(int electionId) throws SQLException {
        String sql = """
                SELECT c.*, COALESCE(ce.party_id, c.party_id) AS effective_party_id,
                       p.party_name, p.headquarters_address, p.party_logo
                FROM candidate c
                JOIN candidate_election ce ON ce.candidate_id = c.candidate_id
                LEFT JOIN political_party p ON p.party_id = COALESCE(ce.party_id, c.party_id)
                WHERE ce.election_id = ? AND ce.approval_status = 'Approved'
                ORDER BY c.first_name, c.mid_name, c.last_name
                """;
        List<Candidate> candidates = new ArrayList<>();
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(mapJoined(rs));
                }
            }
        }
        return candidates;
    }

    @Override
    public Optional<Candidate> findById(int id) throws SQLException {
        String sql = """
                SELECT c.*, c.party_id AS effective_party_id,
                       p.party_name, p.headquarters_address, p.party_logo
                FROM candidate c LEFT JOIN political_party p ON p.party_id = c.party_id
                WHERE c.candidate_id=?
                """;
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapJoined(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Candidate> findAll() throws SQLException {
        String sql = """
                SELECT c.*, c.party_id AS effective_party_id,
                       p.party_name, p.headquarters_address, p.party_logo
                FROM candidate c LEFT JOIN political_party p ON p.party_id = c.party_id
                ORDER BY c.candidate_id DESC
                """;
        List<Candidate> candidates = new ArrayList<>();
        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                candidates.add(mapJoined(rs));
            }
        }
        return candidates;
    }

    @Override
    public boolean update(Candidate candidate) throws SQLException {
        String sql = "UPDATE candidate SET first_name=?, mid_name=?, last_name=?, party_id=?, photo_url=?, approved=? WHERE candidate_id=?";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String[] name = splitName(candidate.getFullName());
            ps.setString(1, name[0]);
            ps.setString(2, name[1]);
            ps.setString(3, name[2]);
            setParty(ps, 4, candidate);
            ps.setString(5, candidate.getPhotoPath());
            ps.setBoolean(6, candidate.isApproved());
            ps.setInt(7, candidate.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM candidate WHERE candidate_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Candidate mapJoined(ResultSet rs) throws SQLException {
        Integer partyId = (Integer) rs.getObject("effective_party_id");
        Party party = null;
        if (partyId != null) {
            party = new Party(rs.getString("party_name"), rs.getString("headquarters_address"), rs.getString("party_logo"));
            party.setId(partyId);
        }
        Candidate candidate = new Candidate(fullName(rs), party, rs.getString("photo_url"), rs.getBoolean("approved"));
        candidate.setId(rs.getInt("candidate_id"));
        return candidate;
    }

    private void setParty(PreparedStatement ps, int index, Candidate candidate) throws SQLException {
        if (candidate.getParty() == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, candidate.getParty().getId());
        }
    }

    private String[] splitName(String fullName) {
        String[] parts = (fullName == null ? "" : fullName.trim()).split("\\s+");
        String first = parts.length > 0 && !parts[0].isBlank() ? parts[0] : "Unnamed";
        String last = parts.length > 1 ? parts[parts.length - 1] : "Candidate";
        String middle = parts.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length - 1)) : null;
        return new String[]{first, middle, last};
    }

    private String fullName(ResultSet rs) throws SQLException {
        return String.join(" ",
                rs.getString("first_name"),
                rs.getString("mid_name") == null ? "" : rs.getString("mid_name"),
                rs.getString("last_name")).replaceAll("\\s+", " ").trim();
    }
}
