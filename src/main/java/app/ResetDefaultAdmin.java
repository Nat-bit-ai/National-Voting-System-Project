package app;

import model.Admin;
import service.BootstrapService;

public final class ResetDefaultAdmin {
    private ResetDefaultAdmin() {
    }

    public static void main(String[] args) {
        try {
            Admin admin = new BootstrapService().resetDefaultAdmin();
            System.out.println("Default admin reset successfully.");
            System.out.println("Username: " + BootstrapService.DEFAULT_ADMIN_USERNAME);
            System.out.println("Password: " + BootstrapService.DEFAULT_ADMIN_PASSWORD);
            System.out.println("Admin ID: " + admin.getId());
        } catch (Exception ex) {
            System.err.println("Default admin reset failed.");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
