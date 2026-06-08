package controller.election;

import model.Election;
import model.ElectionStatus;
import service.ElectionService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ElectionController {
    private final ElectionService electionService = new ElectionService();

    public Election create(String title, String description, LocalDateTime start, LocalDateTime end, int adminId) throws SQLException {
        return electionService.createElection(title, description, start, end, adminId);
    }

    public void setStatus(Election election, ElectionStatus status, int adminId) throws SQLException {
        electionService.setStatus(election, status, adminId);
    }

    public List<Election> list() throws SQLException {
        return electionService.list();
    }
}
