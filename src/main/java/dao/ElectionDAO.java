package dao;

import model.Election;
import model.ElectionStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElectionDAO extends AbstractDAO<Election> {
    @Override
    public Election create(Election election) throws SQLException {
        if (election.getAdminId() == null) {
            throw new IllegalArgumentException("Election creator admin ID is required.");
        }
        String sql = "INSERT INTO election(admin_id, title, description, start_date, end_date, status) "
                + "VALUES(?,?,?,?,?,?::election_status) RETURNING election_id";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, election.getAdminId());
            fill(ps, election, 2);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    election.setId(rs.getInt(1));
                }
            }
        }
        return election;
    }

    @Override
    public Optional<Election> findById(int id) throws SQLException {
        String sql = "SELECT * FROM election WHERE election_id=?";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<Election> findActive() throws SQLException {
        String sql = "SELECT * FROM election WHERE status=?::election_status ORDER BY start_date DESC LIMIT 1";
        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ElectionStatus.ACTIVE.databaseValue());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Election> findAll() throws SQLException {
        List<Election> elections = new ArrayList<>();
        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM election ORDER BY election_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                elections.add(map(rs));
            }
        }
        return elections;
    }

    @Override
    public boolean update(Election election) throws SQLException {
        String sql = "UPDATE election SET title=?, description=?, start_date=?, end_date=?, status=?::election_status WHERE election_id=?";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, election, 1);
            ps.setInt(6, election.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM election WHERE election_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private void fill(PreparedStatement ps, Election election, int startIndex) throws SQLException {
        ps.setString(startIndex, election.getTitle());
        ps.setString(startIndex + 1, election.getDescription());
        ps.setTimestamp(startIndex + 2, Timestamp.valueOf(election.getStartTime()));
        ps.setTimestamp(startIndex + 3, Timestamp.valueOf(election.getEndTime()));
        ps.setString(startIndex + 4, election.getStatus().databaseValue());
    }

    private Election map(ResultSet rs) throws SQLException {
        Election election = new Election();
        election.setId(rs.getInt("election_id"));
        election.setTitle(rs.getString("title"));
        election.setDescription(rs.getString("description"));
        election.setStartTime(rs.getTimestamp("start_date").toLocalDateTime());
        election.setEndTime(rs.getTimestamp("end_date").toLocalDateTime());
        election.setStatus(ElectionStatus.fromDatabaseValue(rs.getString("status")));
        election.setAdminId((Integer) rs.getObject("admin_id"));
        return election;
    }
}
