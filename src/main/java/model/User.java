package model;

public abstract class User extends BaseEntity {
    private String fullName;
    private String faydaId;
    private String passwordHash;

    protected User() {
    }

    protected User(String fullName, String faydaId, String passwordHash) {
        this.fullName = fullName;
        this.faydaId = faydaId;
        this.passwordHash = passwordHash;
    }

    public abstract String getDisplayRole();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFaydaId() {
        return faydaId;
    }

    public void setFaydaId(String faydaId) {
        this.faydaId = faydaId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
