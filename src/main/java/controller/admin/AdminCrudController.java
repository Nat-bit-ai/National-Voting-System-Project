package controller.admin;

import dao.AdminDAO;
import dao.CandidateDAO;
import dao.PartyDAO;
import model.Admin;
import model.Candidate;
import model.Party;

import java.sql.SQLException;
import java.util.List;

public class AdminCrudController {
    private final AdminDAO adminDAO = new AdminDAO();
    private final PartyDAO partyDAO = new PartyDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    public List<Admin> officers() throws SQLException {
        return adminDAO.findAll();
    }

    public Admin saveOfficer(Admin admin) throws SQLException {
        return adminDAO.create(admin);
    }

    public List<Party> parties() throws SQLException {
        return partyDAO.findAll();
    }

    public Party saveParty(Party party) throws SQLException {
        return partyDAO.create(party);
    }

    public List<Candidate> candidates() throws SQLException {
        return candidateDAO.findAll();
    }

    public Candidate saveCandidate(Candidate candidate) throws SQLException {
        return candidateDAO.create(candidate);
    }
}
