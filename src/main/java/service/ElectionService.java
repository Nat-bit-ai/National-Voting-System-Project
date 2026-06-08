package service;

import dao.ElectionDAO;
import model.Election;
import model.ElectionStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ElectionService {
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final AuditService auditService = new AuditService();

    public Election createElection(String title, String description, LocalDateTime start, LocalDateTime end, int adminId) throws SQLException {
        if (start.isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new IllegalArgumentException("Election start time cannot be before the current time.");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Election end time must be after start time.");
        }
        Election election = electionDAO.create(new Election(title, description, start, end, adminId));
        auditService.log("ADMIN", adminId, "CREATE_ELECTION", title);
        return election;
    }

    public void setStatus(Election election, ElectionStatus status, int adminId) throws SQLException {
        election.setStatus(status);
        electionDAO.update(election);
        auditService.log("ADMIN", adminId, "ELECTION_STATUS", election.getTitle() + " -> " + status);
    }

    public List<Election> list() throws SQLException {
        return electionDAO.findAll();
    }
}
