package model;

import java.time.LocalDate;

public class Voter extends Admin {

    private int id;
    private String firstName;
    private String midName;
    private String lastName;
    private String faydaId;
    private String passwordHash;
    private LocalDate dateOfBirth;
    private String region;
    private String email;
    private boolean verified;

    public Voter() {}

    // ✅ ADDED constructor to match AuthService
    public Voter(String firstName, String midName, String lastName,
                 String faydaId, String passwordHash, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.faydaId = faydaId;
        this.passwordHash = passwordHash;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return String.join(" ",
                firstName == null ? "" : firstName,
                midName == null ? "" : midName,
                lastName == null ? "" : lastName).replaceAll("\\s+", " ").trim();
    }

    @Override
    public void setFullName(String fullName) {
        String[] names = fullName == null ? new String[0] : fullName.trim().split("\\s+");
        firstName = names.length > 0 ? names[0] : "";
        midName = names.length > 2 ? names[1] : "";
        lastName = names.length > 1 ? names[names.length - 1] : "";
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isActive() {
        return verified;
    }
}
