package model;

import java.time.LocalDateTime;

public class Vote extends BaseEntity {
    private int electionId;
    private int candidateId;
    private String anonymousToken;
    private LocalDateTime castAt;

    public Vote() {
    }

    public Vote(int electionId, int candidateId, String anonymousToken) {
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.anonymousToken = anonymousToken;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getAnonymousToken() {
        return anonymousToken;
    }

    public void setAnonymousToken(String anonymousToken) {
        this.anonymousToken = anonymousToken;
    }

    public LocalDateTime getCastAt() {
        return castAt;
    }

    public void setCastAt(LocalDateTime castAt) {
        this.castAt = castAt;
    }
}
