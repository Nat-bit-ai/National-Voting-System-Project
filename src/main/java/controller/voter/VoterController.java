package controller.voter;

import model.Candidate;
import model.Election;
import model.Voter;
import service.VotingService;

import java.sql.SQLException;
import java.util.List;

public class VoterController {
    private final VotingService votingService = new VotingService();

    public Election activeElection() throws SQLException {
        return votingService.activeElection();
    }

    public List<Candidate> candidatesFor(Election election) throws SQLException {
        return votingService.candidatesFor(election);
    }

    public void castVote(Voter voter, Election election, Candidate candidate) throws SQLException {
        votingService.castVote(voter, election, candidate);
    }
}
