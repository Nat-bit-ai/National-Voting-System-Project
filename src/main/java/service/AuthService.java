package service;

import dao.AdminDAO;
import dao.FaydaDAO;
import dao.VoterDAO;
import model.Admin;
import model.Voter;
import util.BCryptUtil;
import util.SessionManager;

import java.sql.SQLException;

public class AuthService {

    private final VoterDAO voterDAO = new VoterDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final FaydaDAO faydaDAO = new FaydaDAO();
    private final AuditService auditService = new AuditService();

    public Voter registerVoter(String faydaId, String password) throws SQLException {

        FaydaDAO.FaydaCitizen citizen = verifyFaydaForRegistration(faydaId);

        // ✅ FIX: split full name safely
        String[] names = citizen.fullName().split(" ");

        String firstName = names.length > 0 ? names[0] : "";
        String midName   = names.length > 2 ? names[1] : "";
        String lastName  = names.length > 1 ? names[names.length - 1] : "";

        Voter voter = new Voter(
                firstName,
                midName,
                lastName,
                citizen.faydaId(),
                BCryptUtil.hash(password),
                citizen.dateOfBirth()
        );
        voter.setRegion(blankFallback(citizen.region(), "Unknown"));
        voter.setEmail(blankFallback(citizen.email(), citizen.faydaId() + "@voter.local"));
        voter.setVerified(true);

        voterDAO.create(voter);

        auditService.log("VOTER", voter.getId(), "REGISTER", "Fayda verified voter registration");

        return voter;
    }

    public FaydaDAO.FaydaCitizen verifyFaydaForRegistration(String faydaId) throws SQLException {
        String normalizedFaydaId = faydaId == null ? "" : faydaId.trim();

        if (normalizedFaydaId.isBlank()) {
            throw new IllegalArgumentException("Enter your Fayda ID before generating an OTP.");
        }

        if (voterDAO.findByFaydaId(normalizedFaydaId).isPresent()) {
            throw new IllegalArgumentException("This Fayda ID is already registered.");
        }

        FaydaDAO.FaydaCitizen citizen = faydaDAO.verifyCitizen(normalizedFaydaId)
                .orElseThrow(() -> new IllegalArgumentException("Fayda citizen record was not found."));

        if (!citizen.isEligibleVoter()) {
            throw new IllegalArgumentException("Citizen must be alive and at least 18 years old.");
        }

        return citizen;
    }

    public Voter loginVoter(String faydaId, String password) throws SQLException {

        Voter voter = voterDAO.findByFaydaId(faydaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Fayda ID or password."));

        if (!voter.isActive() || !BCryptUtil.verify(password, voter.getPasswordHash())) {
            auditService.log("VOTER", voter.getId(), "FAILED_LOGIN", "Invalid voter login");
            throw new IllegalArgumentException("Invalid Fayda ID or password.");
        }

        SessionManager.getInstance().login(voter);
        auditService.log("VOTER", voter.getId(), "LOGIN", "Voter logged in");

        return voter;
    }

    public Admin loginAdmin(String username, String password) throws SQLException {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedPassword = password == null ? "" : password.trim();

        Admin admin = adminDAO.findByUsername(normalizedUsername)
                .orElseGet(() -> null);

        if (admin == null && isDefaultAdminCredentials(normalizedUsername, normalizedPassword)) {
            admin = resetDefaultAdmin();
        }

        if (admin != null && (!admin.isActive() || !BCryptUtil.verify(normalizedPassword, admin.getPasswordHash()))) {
            if (isDefaultAdminCredentials(normalizedUsername, normalizedPassword)) {
                admin = resetDefaultAdmin();
            }
        }

        if (admin == null || !admin.isActive() || !BCryptUtil.verify(normalizedPassword, admin.getPasswordHash())) {
            auditService.log("ADMIN", admin == null ? null : admin.getId(), "FAILED_LOGIN", "Invalid admin login");
            throw new IllegalArgumentException("Invalid admin username or password.");
        }

        SessionManager.getInstance().login(admin);
        auditService.log("ADMIN", admin.getId(), "LOGIN", admin.getDisplayRole() + " logged in");

        return admin;
    }

    private boolean isDefaultAdminCredentials(String username, String password) {
        return BootstrapService.DEFAULT_ADMIN_USERNAME.equalsIgnoreCase(username)
                && BootstrapService.DEFAULT_ADMIN_PASSWORD.equals(password);
    }

    private Admin resetDefaultAdmin() throws SQLException {
        return new BootstrapService().resetDefaultAdmin();
    }

    private String blankFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
