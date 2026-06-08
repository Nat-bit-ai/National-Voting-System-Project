package controller.auth;

import model.Admin;
import model.Voter;
import service.AuthService;

import java.sql.SQLException;

public class AuthController {
    private final AuthService authService = new AuthService();

    public Voter registerVoter(String faydaId, String password) throws SQLException {
        return authService.registerVoter(faydaId, password);
    }

    public Voter loginVoter(String faydaId, String password) throws SQLException {
        return authService.loginVoter(faydaId, password);
    }

    public Admin loginAdmin(String username, String password) throws SQLException {
        return authService.loginAdmin(username, password);
    }
}
