package app;

import config.DatabaseConfig;
import service.BootstrapService;
import service.OfflineSyncService;
import ui.auth.LoginFrame;
import ui.shared.AppTheme;
import util.LanguageManager;
import util.SessionManager;

import javax.swing.SwingUtilities;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppTheme.install();
            LanguageManager.getInstance().setLanguage("en");
            SessionManager.getInstance().clear();
            DatabaseConfig.testStartupConnections();
            new BootstrapService().ensureDefaultAdmin();
            new OfflineSyncService().start();
            new LoginFrame().setVisible(true);
        });
    }
}
