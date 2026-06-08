package dao;

import model.Party;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartyDAO extends AbstractDAO<Party> {
    @Override
    public Party create(Party party) throws SQLException {
        String sql = "INSERT INTO political_party(party_name, acronym, party_logo, headquarters_address) VALUES(?,?,?,?) RETURNING party_id";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, party.getName());
            ps.setString(2, acronymFor(con, party, null));
            ps.setString(3, party.getSymbolPath());
            ps.setString(4, party.getLeaderName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    party.setId(rs.getInt(1));
                }
            }
        }
        return party;
    }

    @Override
    public Optional<Party> findById(int id) throws SQLException {
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM political_party WHERE party_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Party> findAll() throws SQLException {
        List<Party> parties = new ArrayList<>();
        try (Connection con = votingConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM political_party ORDER BY party_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                parties.add(map(rs));
            }
        }
        return parties;
    }

    @Override
    public boolean update(Party party) throws SQLException {
        String sql = "UPDATE political_party SET party_name=?, acronym=?, party_logo=?, headquarters_address=? WHERE party_id=?";
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, party.getName());
            ps.setString(2, acronymFor(con, party, party.getId()));
            ps.setString(3, party.getSymbolPath());
            ps.setString(4, party.getLeaderName());
            ps.setInt(5, party.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection con = votingConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM political_party WHERE party_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Party map(ResultSet rs) throws SQLException {
        Party party = new Party();
        party.setId(rs.getInt("party_id"));
        party.setName(rs.getString("party_name"));
        party.setLeaderName(rs.getString("headquarters_address"));
        party.setSymbolPath(rs.getString("party_logo"));
        return party;
    }

    private String acronymFor(Connection con, Party party, Integer excludedPartyId) throws SQLException {
        String base = acronymBase(party);
        String acronym = base;
        int suffix = 2;
        while (acronymExists(con, acronym, excludedPartyId)) {
            String number = String.valueOf(suffix++);
            int baseLength = Math.min(base.length(), 20 - number.length());
            acronym = base.substring(0, Math.max(1, baseLength)) + number;
        }
        return acronym;
    }

    private boolean acronymExists(Connection con, String acronym, Integer excludedPartyId) throws SQLException {
        String sql = excludedPartyId == null
                ? "SELECT 1 FROM political_party WHERE acronym=?"
                : "SELECT 1 FROM political_party WHERE acronym=? AND party_id<>?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, acronym);
            if (excludedPartyId != null) {
                ps.setInt(2, excludedPartyId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String acronymBase(Party party) {
        String name = party.getName() == null ? "" : party.getName().trim();
        if (name.isEmpty()) {
            return "PARTY" + Math.max(0, party.getId());
        }
        String cleaned = name.replaceAll("[^A-Za-z0-9 ]", " ").replaceAll("\\s+", " ").trim();
        String[] words = cleaned.split(" ");
        String acronym;
        if (words.length == 1) {
            acronym = words[0].substring(0, Math.min(words[0].length(), 4)).toUpperCase();
        } else {
            acronym = cleaned.replaceAll("\\B\\w+", "").replace(" ", "").toUpperCase();
        }
        if (acronym.isBlank()) {
            acronym = name.substring(0, Math.min(name.length(), 20)).toUpperCase();
        }
        return acronym.length() > 20 ? acronym.substring(0, 20) : acronym;
    }
}
