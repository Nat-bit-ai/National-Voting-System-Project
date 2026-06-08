package service;

import dao.AdminDAO;
import model.Admin;
import model.AdminRole;
import util.BCryptUtil;

import java.sql.Connection;
import java.sql.Statement;

import static config.DatabaseConfig.getVotingConnection;

public class BootstrapService {
    public static final String DEFAULT_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final AdminDAO adminDAO = new AdminDAO();

    public void ensureDefaultAdmin() {
        try {
            ensureSchemaCompatibility();
            resetDefaultAdmin();
        } catch (Exception ignored) {
            // The login screen still opens when the database is not configured yet.
        }
    }

    public Admin resetDefaultAdmin() throws java.sql.SQLException {
        return adminDAO.upsertDefaultAdmin(new Admin(
                "System Super Admin",
                DEFAULT_ADMIN_USERNAME,
                BCryptUtil.hash(DEFAULT_ADMIN_PASSWORD),
                AdminRole.SUPER_ADMIN
        ));
    }

    private void ensureSchemaCompatibility() throws java.sql.SQLException {
        try (Connection connection = getVotingConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    ALTER TABLE IF EXISTS candidate
                    ADD COLUMN IF NOT EXISTS party_id INTEGER
                    REFERENCES political_party(party_id) ON DELETE SET NULL
                    """);
            statement.executeUpdate("""
                    ALTER TABLE IF EXISTS candidate
                    ADD COLUMN IF NOT EXISTS photo_url VARCHAR(255)
                    """);
            statement.executeUpdate("""
                    ALTER TABLE IF EXISTS candidate
                    ADD COLUMN IF NOT EXISTS approved BOOLEAN NOT NULL DEFAULT FALSE
                    """);
            statement.executeUpdate("""
                    ALTER TABLE IF EXISTS candidate_election
                    ADD COLUMN IF NOT EXISTS party_id INTEGER
                    REFERENCES political_party(party_id) ON DELETE SET NULL
                    """);
            statement.executeUpdate("""
                    ALTER TABLE IF EXISTS candidate_election
                    ADD COLUMN IF NOT EXISTS approval_status VARCHAR(20) DEFAULT 'Pending'
                    """);
        }
    }
}
