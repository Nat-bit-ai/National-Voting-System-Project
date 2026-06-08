package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditService {
    public void log(String actorType, Integer actorId, String action, String details) {
        String sql = "INSERT INTO audit_log(actor_type, actor_id, action, details) VALUES(?,?,?,?)";
        try (Connection con = DatabaseConfig.getVotingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, actorType);
            if (actorId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, actorId);
            }
            ps.setString(3, action);
            ps.setString(4, details);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            // Auditing should not crash the user workflow; deployment logging can capture this.
        }
    }
}
