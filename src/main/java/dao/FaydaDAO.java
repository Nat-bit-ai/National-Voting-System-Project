package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class FaydaDAO extends AbstractDAO<FaydaDAO.FaydaCitizen> {
    public Optional<FaydaCitizen> verifyCitizen(String faydaId) throws SQLException {
        String sql = """
                SELECT
                    f.fayda_id,
                    trim(concat_ws(' ', f.first_name, f.middle_name, f.last_name)) AS full_name,
                    f.date_of_birth,
                    f.region,
                    f.email,
                    c.is_alive,
                    c.is_eligible
                FROM fayda_citizens f
                JOIN civil_registry c ON c.fayda_id = f.fayda_id
                WHERE f.fayda_id = ?
                """;
        try (Connection con = citizensConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, faydaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                FaydaCitizen citizen = new FaydaCitizen(
                        rs.getString("fayda_id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("region"),
                        rs.getString("email"),
                        rs.getBoolean("is_alive"),
                        rs.getBoolean("is_eligible")
                );
                return Optional.of(citizen);
            }
        }
    }

    @Override
    public FaydaCitizen create(FaydaCitizen entity) {
        throw new UnsupportedOperationException("Fayda data is managed by the citizens database authority.");
    }

    @Override
    public Optional<FaydaCitizen> findById(int id) {
        throw new UnsupportedOperationException("Use verifyCitizen with a Fayda ID.");
    }

    @Override
    public java.util.List<FaydaCitizen> findAll() {
        throw new UnsupportedOperationException("Bulk Fayda reads are disabled.");
    }

    @Override
    public boolean update(FaydaCitizen entity) {
        throw new UnsupportedOperationException("Fayda data is read-only.");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Fayda data is read-only.");
    }

    public record FaydaCitizen(String faydaId, String fullName, LocalDate dateOfBirth,
                               String region, String email,
                               boolean alive, boolean eligible) {
        public boolean isEligibleVoter() {
            return alive && eligible && Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
        }
    }
}
