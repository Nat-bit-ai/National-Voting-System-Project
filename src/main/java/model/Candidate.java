package model;

public class Candidate extends BaseEntity {
    private String fullName;
    private Party party;
    private String photoPath;
    private boolean approved;

    public Candidate() {
    }

    public Candidate(String fullName, Party party, String photoPath, boolean approved) {
        this.fullName = fullName;
        this.party = party;
        this.photoPath = photoPath;
        this.approved = approved;
    }

    @Override
    public String toString() {
        return fullName == null ? "" : fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
