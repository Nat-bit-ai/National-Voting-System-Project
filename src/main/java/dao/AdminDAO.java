package dao;

import model.Admin;
import model.AdminRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminDAO extends AbstractDAO<Admin> {
    @Override
    public Admin create(Admin admin) throws SQLException {
        String sql = """
                INSERT INTO administrator(username, password_hash, first_name, last_name, email, role, active)
                VALUES(?,?,?,?,?,?,?) RETURNING admin_id
                """;
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String[] name = splitName(admin.getFullName());
                ps.setString(1, admin.getUsername());
                ps.setString(2, admin.getPasswordHash());
                ps.setString(3, name[0]);
                ps.setString(4, name[1]);
                ps.setString(5, admin.getUsername() + "@evoting.local");
                ps.setString(6, databaseRole(admin.getRole()));
                ps.setBoolean(7, admin.isActive());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                    }
                }
            }
        }
        return admin;
    }

    public Optional<Admin> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM administrator WHERE username = ?";
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(map(rs)) : Optional.empty();
                }
            }
        }
    }

    public Admin upsertDefaultAdmin(Admin admin) throws SQLException {
        String sql = """
                INSERT INTO administrator(username, password_hash, first_name, last_name, email, role, active)
                VALUES(?,?,?,?,?,?,true)
                ON CONFLICT (username) DO UPDATE
                SET password_hash = EXCLUDED.password_hash,
                    first_name = EXCLUDED.first_name,
                    last_name = EXCLUDED.last_name,
                    email = EXCLUDED.email,
                    role = EXCLUDED.role,
                    active = true
                RETURNING admin_id
                """;
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String[] name = splitName(admin.getFullName());
                ps.setString(1, admin.getUsername());
                ps.setString(2, admin.getPasswordHash());
                ps.setString(3, name[0]);
                ps.setString(4, name[1]);
                ps.setString(5, admin.getUsername() + "@evoting.local");
                ps.setString(6, databaseRole(admin.getRole()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                    }
                }
            }
        }
        admin.setActive(true);
        return admin;
    }


    @Override
    public Optional<Admin> findById(int id) throws SQLException {
        String sql = "SELECT * FROM administrator WHERE admin_id = ?";
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(map(rs)) : Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Admin> findAll() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM administrator ORDER BY admin_id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    admins.add(map(rs));
                }
            }
        }
        return admins;
    }

    @Override
    public boolean update(Admin admin) throws SQLException {
        String sql = "UPDATE administrator SET first_name=?, last_name=?, username=?, email=?, role=?, active=? WHERE admin_id=?";
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String[] name = splitName(admin.getFullName());
                ps.setString(1, name[0]);
                ps.setString(2, name[1]);
                ps.setString(3, admin.getUsername());
                ps.setString(4, admin.getUsername() + "@evoting.local");
                ps.setString(5, databaseRole(admin.getRole()));
                ps.setBoolean(6, admin.isActive());
                ps.setInt(7, admin.getId());
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection con = votingConnection()) {
            ensureAdministratorColumns(con);
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM administrator WHERE admin_id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() == 1;
            }
        }
    }

    private void ensureAdministratorColumns(Connection con) throws SQLException {
        try (Statement statement = con.createStatement()) {
            statement.executeUpdate("ALTER TABLE administrator ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE");
        }
    }

    private Admin map(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("admin_id"));
        admin.setFullName((rs.getString("first_name") + " " + rs.getString("last_name")).trim());
        admin.setUsername(rs.getString("username"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setRole(mapRole(rs.getString("role")));
        admin.setActive(rs.getBoolean("active"));
        return admin;
    }

    private AdminRole mapRole(String role) {
        if (role == null) {
            return AdminRole.SUPER_ADMIN;
        }
        return switch (role.trim().toUpperCase()) {
            case "OFFICER", "ELECTION_OFFICER" -> AdminRole.OFFICER;
            default -> AdminRole.SUPER_ADMIN;
        };
    }

    private String databaseRole(AdminRole role) {
        return role == AdminRole.OFFICER ? "Officer" : "Admin";
    }

    private String[] splitName(String fullName) {
        String safeName = fullName == null || fullName.isBlank() ? "System Admin" : fullName.trim();
        int space = safeName.indexOf(' ');
        if (space < 0) {
            return new String[]{safeName, "Admin"};
        }
        return new String[]{safeName.substring(0, space), safeName.substring(space + 1).trim()};
    }
}
