package util;

import model.Admin;
import model.User;
import model.Voter;

public final class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void clear() {
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Voter getVoter() {
        return currentUser instanceof Voter voter ? voter : null;
    }

    public Admin getAdmin() {
        return currentUser instanceof Admin admin ? admin : null;
    }
}
