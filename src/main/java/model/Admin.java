package model;

public class Admin extends User {
    private String username;
    private AdminRole role;
    private boolean active = true;

    public Admin() {
    }

    public Admin(String fullName, String username, String passwordHash, AdminRole role) {
        super(fullName, null, passwordHash);
        this.username = username;
        this.role = role;
    }

    @Override
    public String getDisplayRole() {
        return role == AdminRole.SUPER_ADMIN ? "Super Admin" : "Election Officer";
    }

    public boolean canManageOfficers() {
        return role == AdminRole.SUPER_ADMIN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
