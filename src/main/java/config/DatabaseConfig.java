package config;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    private static final String DEFAULT_VOTING_URL = "jdbc:postgresql://localhost:5432/voting_db";
    private static final String DEFAULT_CITIZENS_URL = "jdbc:postgresql://localhost:5432/citizens";

    private DatabaseConfig() {
    }

    public static Connection getVotingConnection() throws SQLException {
        return DriverManager.getConnection(
                env("VOTING_DB_URL", DEFAULT_VOTING_URL),
                env("DB_USER", "postgres"),
                env("DB_PASSWORD", "226600")
        );
    }

    public static Connection getCitizensConnection() throws SQLException {
        return DriverManager.getConnection(
                env("CITIZENS_DB_URL", DEFAULT_CITIZENS_URL),
                env("DB_USER", "postgres"),
                env("DB_PASSWORD", "226600")
        );
    }

    public static void testStartupConnections() {
        try (Connection ignored = getVotingConnection()) {
            // Startup smoke test only. DAOs open their own short-lived connections.
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Voting database is not reachable yet.\n" + ex.getMessage(),
                    "Database warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
