package dao;

import model.Voter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoterDAO extends AbstractDAO<Voter> {

    @Override
    public Voter create(Voter voter) throws SQLException {

        String sql =
                "INSERT INTO voter " +
                        "(first_name, mid_name, last_name, fayda_id, date_of_birth, region, email, password_hash, is_verified) " +
                        "VALUES (?,?,?,?,?,?,?,?,?) RETURNING voter_id";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, voter.getFirstName());
            ps.setString(2, voter.getMidName());
            ps.setString(3, voter.getLastName());
            ps.setString(4, voter.getFaydaId());
            ps.setDate(5, Date.valueOf(voter.getDateOfBirth()));
            ps.setString(6, blankFallback(voter.getRegion(), "Unknown"));
            ps.setString(7, blankFallback(voter.getEmail(), voter.getFaydaId() + "@voter.local"));
            ps.setString(8, voter.getPasswordHash());
            ps.setBoolean(9, voter.isVerified());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    voter.setId(rs.getInt(1));
                }
            }
        }
        return voter;
    }

    @Override
    public Optional<Voter> findById(int id) throws SQLException {

        String sql = "SELECT * FROM voter WHERE voter_id = ?";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<Voter> findByFaydaId(String faydaId) throws SQLException {

        String sql = "SELECT * FROM voter WHERE fayda_id = ?";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, faydaId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Voter> findAll() throws SQLException {

        List<Voter> list = new ArrayList<>();

        String sql = "SELECT * FROM voter ORDER BY voter_id";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    @Override
    public boolean update(Voter voter) throws SQLException {

        String sql =
                "UPDATE voter SET first_name=?, mid_name=?, last_name=?, is_verified=? WHERE voter_id=?";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, voter.getFirstName());
            ps.setString(2, voter.getMidName());
            ps.setString(3, voter.getLastName());
            ps.setBoolean(4, voter.isVerified());
            ps.setInt(5, voter.getId());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM voter WHERE voter_id=?";

        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Voter map(ResultSet rs) throws SQLException {

        Voter v = new Voter();

        v.setId(rs.getInt("voter_id"));
        v.setFirstName(rs.getString("first_name"));
        v.setMidName(rs.getString("mid_name"));
        v.setLastName(rs.getString("last_name"));
        v.setFaydaId(rs.getString("fayda_id"));
        v.setPasswordHash(rs.getString("password_hash"));
        v.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        v.setRegion(rs.getString("region"));
        v.setEmail(rs.getString("email"));
        v.setVerified(rs.getBoolean("is_verified"));

        return v;
    }

    private String blankFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
