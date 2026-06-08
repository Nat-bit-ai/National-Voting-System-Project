package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO extends AbstractDAO<ResultDAO.ResultRow> {
    public List<ResultRow> findResults(int electionId) throws SQLException {
        String sql = """
                SELECT
                    ce.election_id,
                    trim(concat_ws(' ', c.first_name, c.mid_name, c.last_name)) AS candidate_name,
                    COALESCE(pp.party_name, 'Independent') AS party_name,
                    COUNT(v.vote_id)::INTEGER AS vote_count
                FROM candidate_election ce
                JOIN candidate c ON ce.candidate_id = c.candidate_id
                LEFT JOIN political_party pp ON pp.party_id = COALESCE(ce.party_id, c.party_id)
                LEFT JOIN vote v ON v.candidate_election_id = ce.candidate_election_id
                WHERE ce.election_id=?
                GROUP BY ce.election_id, c.candidate_id, c.first_name, c.mid_name, c.last_name, pp.party_name
                ORDER BY vote_count DESC, candidate_name
                """;
        List<ResultRow> rows = new ArrayList<>();
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new ResultRow(
                            rs.getInt("election_id"),
                            rs.getString("candidate_name"),
                            rs.getString("party_name"),
                            rs.getInt("vote_count")
                    ));
                }
            }
        }
        return rows;
    }

    @Override
    public ResultRow create(ResultRow entity) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Optional<ResultRow> findById(int id) { throw new UnsupportedOperationException(); }
    @Override
    public List<ResultRow> findAll() { throw new UnsupportedOperationException(); }
    @Override
    public boolean update(ResultRow entity) { throw new UnsupportedOperationException(); }
    @Override
    public boolean delete(int id) { throw new UnsupportedOperationException(); }

    public record ResultRow(int electionId, String candidateName, String partyName, int voteCount) {
    }
}
