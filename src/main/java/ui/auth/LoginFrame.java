package ui.auth;

import model.Admin;
import model.Voter;
import service.AuthService;
import ui.admin.AdminDashboardFrame;
import ui.shared.AppTheme;
import ui.shared.BaseFrame;
import ui.voter.VoterDashboardFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends BaseFrame {
    private final AuthService authService = new AuthService();
    private final JTextField identityField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> loginMode = new JComboBox<>(new String[]{"Voter", "Admin"});

    public LoginFrame() {
        super("National Digital Voting System", 980, 680);
        initializeFrame();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        JPanel banner = new JPanel(new GridLayout(3, 1));
        banner.setBackground(AppTheme.GREEN);
        banner.setBorder(new EmptyBorder(60, 48, 60, 48));
        JLabel title = new JLabel("Ethiopian National Digital Voting System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        JLabel subtitle = new JLabel("Secure Fayda-based registration, voting, monitoring, and audit control");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel flagLine = new JLabel("Green  •  Gold  •  Red");
        flagLine.setForeground(AppTheme.GOLD);
        flagLine.setFont(new Font("Segoe UI", Font.BOLD, 18));
        banner.add(title);
        banner.add(subtitle);
        banner.add(flagLine);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(36, 54, 36, 54));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(AppTheme.title("Sign in"), gbc);
        gbc.gridy++;
        form.add(loginMode, gbc);
        gbc.gridy++;
        form.add(new JLabel("Fayda ID or admin username"), gbc);
        gbc.gridy++;
        form.add(identityField, gbc);
        gbc.gridy++;
        form.add(new JLabel("Password"), gbc);
        gbc.gridy++;
        form.add(passwordField, gbc);
        gbc.gridy++;
        JButton login = AppTheme.primaryButton("Login");
        form.add(login, gbc);
        gbc.gridy++;
        JButton register = new JButton("Register as voter");
        form.add(register, gbc);

        login.addActionListener(e -> login());
        register.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });

        root.add(banner, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        return root;
    }

    private void login() {
        try {
            String identity = identityField.getText().trim();
            String password = new String(passwordField.getPassword());
            if ("Admin".equals(loginMode.getSelectedItem())) {
                Admin admin = authService.loginAdmin(identity, password.trim());
                dispose();
                new AdminDashboardFrame(admin).setVisible(true);
            } else {
                Voter voter = authService.loginVoter(identity, password);
                dispose();
                new VoterDashboardFrame(voter).setVisible(true);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
}
