package service;

import dao.CandidateDAO;
import dao.ElectionDAO;
import dao.VoteDAO;
import model.Candidate;
import model.Election;
import model.Vote;
import model.Voter;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class VotingService {
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO voteDAO = new VoteDAO();
    private final AuditService auditService = new AuditService();

    public Election activeElection() throws SQLException {
        return electionDAO.findActive().orElseThrow(() -> new IllegalStateException("No active election is available."));
    }

    public List<Candidate> candidatesFor(Election election) throws SQLException {
        return candidateDAO.findApprovedByElection(election.getId());
    }

    public void castVote(Voter voter, Election election, Candidate candidate) throws SQLException {
        if (!election.isOpenForVoting()) {
            throw new IllegalStateException("Election is not active.");
        }
        if (voteDAO.hasVoted(voter.getId(), election.getId())) {
            throw new IllegalStateException("You have already voted in this election.");
        }
        Vote vote = new Vote(election.getId(), candidate.getId(), UUID.randomUUID().toString());
        voteDAO.create(vote);
        voteDAO.markVoted(voter.getId(), election.getId());
        auditService.log("VOTER", voter.getId(), "VOTE_SUBMITTED", "Vote token " + vote.getAnonymousToken());
    }
}
