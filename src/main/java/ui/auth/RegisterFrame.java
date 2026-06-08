package ui.auth;

import service.AuthService;
import service.QRService;
import ui.shared.AppTheme;
import ui.shared.BaseFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends BaseFrame {
    private final AuthService authService = new AuthService();
    private final QRService qrService = new QRService();
    private final JTextField faydaField = new JTextField();
    private final JTextField otpField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();
    private String otp;
    private String otpFaydaId;

    public RegisterFrame() {
        super("Voter Registration", 760, 700);
        initializeFrame();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(AppTheme.CANVAS);
        root.setBorder(new EmptyBorder(28, 52, 28, 52));

        JPanel form = AppTheme.cardPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(AppTheme.eyebrow("Voter onboarding"), gbc);
        gbc.gridy++;
        form.add(AppTheme.title("Fayda voter registration"), gbc);
        gbc.gridy++;
        form.add(AppTheme.mutedLabel("Verify your Fayda record first, then use the generated OTP to complete registration."), gbc);
        gbc.gridy++;
        JButton scan = AppTheme.secondaryButton("Scan QR / paste QR text");
        form.add(scan, gbc);
        gbc.gridy++;
        AppTheme.styleInput(faydaField);
        form.add(fieldGroup("Fayda ID", "Required. OTP is generated only after this ID is found in the citizens database.", faydaField), gbc);
        gbc.gridy++;
        JButton sendOtp = AppTheme.secondaryButton("Generate OTP");
        form.add(sendOtp, gbc);
        gbc.gridy++;
        AppTheme.styleInput(otpField);
        form.add(fieldGroup("OTP", "Enter the demo code generated after Fayda verification.", otpField), gbc);
        gbc.gridy++;
        AppTheme.styleInput(passwordField);
        form.add(fieldGroup("Password", "Choose the password you will use when voting.", passwordField), gbc);
        gbc.gridy++;
        AppTheme.styleInput(confirmField);
        form.add(fieldGroup("Confirm password", "Repeat the same password to avoid typing mistakes.", confirmField), gbc);
        gbc.gridy++;
        JButton register = AppTheme.primaryButton("Register");
        form.add(register, gbc);
        gbc.gridy++;
        JButton back = AppTheme.secondaryButton("Back to login");
        form.add(back, gbc);

        scan.addActionListener(e -> scanQr());
        sendOtp.addActionListener(e -> generateOtp());
        register.addActionListener(e -> register());
        back.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        root.add(form, new GridBagConstraints());
        return root;
    }

    private JPanel fieldGroup(String label, String description, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        JLabel caption = new JLabel(label);
        caption.setFont(new Font("Segoe UI", Font.BOLD, 12));
        caption.setForeground(AppTheme.INK);
        panel.add(caption, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.add(AppTheme.mutedLabel(description), BorderLayout.SOUTH);
        return panel;
    }

    private void scanQr() {
        String qrText = JOptionPane.showInputDialog(this, "Paste Fayda QR content");
        if (qrText != null) {
            faydaField.setText(qrService.extractFaydaId(qrText));
            clearOtp();
        }
    }

    private void generateOtp() {
        try {
            String faydaId = faydaField.getText().trim();
            authService.verifyFaydaForRegistration(faydaId);
            otp = String.valueOf((int) (100000 + Math.random() * 899999));
            otpFaydaId = faydaId;
            otpField.setText("");
            showInfo("Demo OTP: " + otp);
        } catch (Exception ex) {
            clearOtp();
            showError(ex);
        }
    }

    private void register() {
        try {
            String faydaId = faydaField.getText().trim();
            if (otp == null || otpFaydaId == null) {
                throw new IllegalArgumentException("Generate an OTP after entering a valid Fayda ID.");
            }
            if (!otpFaydaId.equals(faydaId)) {
                throw new IllegalArgumentException("Fayda ID changed after OTP generation. Generate a new OTP.");
            }
            String password = new String(passwordField.getPassword());
            if (!password.equals(new String(confirmField.getPassword()))) {
                throw new IllegalArgumentException("Passwords do not match.");
            }
            if (!otp.equals(otpField.getText().trim())) {
                throw new IllegalArgumentException("Invalid OTP.");
            }
            authService.registerVoter(faydaId, password);
            showInfo("Registration successful. Please login.");
            dispose();
            new LoginFrame().setVisible(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void clearOtp() {
        otp = null;
        otpFaydaId = null;
        otpField.setText("");
    }
}
