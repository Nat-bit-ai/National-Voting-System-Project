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
import java.io.File;

public class LoginFrame extends BaseFrame {
    private final AuthService authService = new AuthService();
    private final JTextField identityField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> loginMode = new JComboBox<>(new String[]{"Voter", "Admin"});
    private JLabel logoLabel;
    private JLabel typingLabel;
    private Timer typingTimer;
    private int typingIndex = 0;
    private final String typingText = "┌─► Ethiopian National Digital Voting System ◄─┐";

    public LoginFrame() {
        super("National Digital Voting System", 1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeFrame();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.getCanvasBackground());

        // Navigation Bar
        JPanel navbar = createNavbar();
        root.add(navbar, BorderLayout.NORTH);

        // Main Content - Responsive Layout
        JPanel mainContent = createMainContent();
        root.add(mainContent, BorderLayout.CENTER);

        return root;
    }

    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(AppTheme.getSurfaceBackground());
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.getTextColor()));
        navbar.setPreferredSize(new Dimension(0, 70));

        // Logo/Brand with terminal style
        JLabel brand = new JLabel("▓▒░ Ethiopian Voting System ░▒▓");
        brand.setFont(new Font("Courier New", Font.BOLD, 16));
        brand.setForeground(AppTheme.getTextColor());
        brand.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Navigation buttons
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        navButtons.setBackground(AppTheme.getSurfaceBackground());

        JButton homeBtn = createNavButton("Home");
        JButton aboutBtn = createNavButton("About");
        JButton contactBtn = createNavButton("Contact");

        homeBtn.addActionListener(e -> {});
        aboutBtn.addActionListener(e -> openAboutPage());
        contactBtn.addActionListener(e -> openContactPage());

        navButtons.add(homeBtn);
        navButtons.add(aboutBtn);
        navButtons.add(contactBtn);

        navbar.add(brand, BorderLayout.WEST);
        navbar.add(navButtons, BorderLayout.EAST);

        return navbar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton("→ " + text);
        btn.setFont(new Font("Courier New", Font.BOLD, 12));
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setForeground(AppTheme.getTextColor());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private final javax.swing.border.Border underlineBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, AppTheme.getMutedTextColor());
            private final javax.swing.border.Border normalBorder = new EmptyBorder(5, 10, 5, 10);

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBorder(underlineBorder);
                btn.setForeground(AppTheme.getMutedTextColor());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBorder(normalBorder);
                btn.setForeground(AppTheme.getTextColor());
            }
        });

        return btn;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(AppTheme.getCanvasBackground());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;

        // Hero Section - Left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        main.add(createHeroSection(), gbc);

        // Form Section - Right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        main.add(createFormSection(), gbc);

        return main;
    }

    private JPanel createHeroSection() {
        JPanel hero = AppTheme.heroPanel();
        hero.setLayout(new GridBagLayout());
        hero.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Logo
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        hero.add(logoLabel, gbc);

        // Typing Effect Label
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 5, 0);
        typingLabel = new JLabel("■");
        typingLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        typingLabel.setForeground(AppTheme.getTextColor());
        typingLabel.setHorizontalAlignment(JLabel.CENTER);
        hero.add(typingLabel, gbc);

        // Start typing effect
        startTypingEffect();

        // Title with typing style
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 5, 0);
        JLabel mainTitle = new JLabel("$ system.init()");
        mainTitle.setFont(new Font("Courier New", Font.BOLD, 22));
        mainTitle.setForeground(AppTheme.getTextColor());
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        hero.add(mainTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 10, 0);
        JLabel titleCont = new JLabel("▓ VOTING PLATFORM INITIALIZED ▓");
        titleCont.setFont(new Font("Courier New", Font.BOLD, 20));
        titleCont.setForeground(AppTheme.getTextColor());
        titleCont.setHorizontalAlignment(JLabel.CENTER);
        hero.add(titleCont, gbc);

        // Subtitle with command style
        gbc.gridy++;
        gbc.insets = new Insets(20, 20, 10, 20);
        JLabel subtitle = new JLabel("<html><center>[INFO] Secure & Transparent<br>[INFO] Voting Platform v2.0<br>[INFO] Status: READY</center></html>");
        subtitle.setFont(new Font("Courier New", Font.PLAIN, 12));
        subtitle.setForeground(AppTheme.getMutedTextColor());
        subtitle.setHorizontalAlignment(JLabel.CENTER);
        hero.add(subtitle, gbc);

        return hero;
    }

    private void startTypingEffect() {
        typingIndex = 0;
        if (typingTimer != null) {
            typingTimer.stop();
        }

        typingTimer = new Timer(50, e -> {
            if (typingIndex <= typingText.length()) {
                String displayText = typingText.substring(0, typingIndex) + "█";
                typingLabel.setText(displayText);
                typingIndex++;
            } else {
                ((Timer) e.getSource()).stop();
                // Start blinking cursor after typing is done
                startBlinkingCursor();
            }
        });
        typingTimer.start();
    }

    private void startBlinkingCursor() {
        typingLabel.setText(typingText + " █");
        Timer blinkTimer = new Timer(500, e -> {
            String current = typingLabel.getText();
            if (current.endsWith(" █")) {
                typingLabel.setText(typingText);
            } else {
                typingLabel.setText(typingText + " █");
            }
        });
        blinkTimer.start();
    }

    private JPanel createFormSection() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(AppTheme.getCanvasBackground());
        container.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.getSurfaceBackground());
        form.setBorder(BorderFactory.createCompoundBorder(
                new AppTheme.TerminalBorder(AppTheme.getTextColor(), 8),
                new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 0, 10, 0);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.weightx = 1.0;

        // Form Title with command prompt style
        JLabel formTitle = new JLabel("user@voting-system:~$ login");
        formTitle.setFont(new Font("Courier New", Font.BOLD, 18));
        formTitle.setForeground(AppTheme.getTextColor());
        form.add(formTitle, formGbc);

        // Divider line
        formGbc.gridy++;
        formGbc.insets = new Insets(10, 0, 15, 0);
        JLabel divider = new JLabel("════════════════════════════════════════");
        divider.setFont(new Font("Courier New", Font.PLAIN, 12));
        divider.setForeground(AppTheme.getMutedTextColor());
        form.add(divider, formGbc);

        // Login Mode
        formGbc.gridy++;
        formGbc.insets = new Insets(10, 0, 5, 0);
        JLabel modeLabel = new JLabel("$ [MODE]");
        modeLabel.setForeground(AppTheme.getTextColor());
        modeLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        form.add(modeLabel, formGbc);

        formGbc.gridy++;
        formGbc.insets = new Insets(5, 0, 15, 0);
        AppTheme.styleInput(loginMode);
        form.add(loginMode, formGbc);

        // Identity Field
        formGbc.gridy++;
        formGbc.insets = new Insets(10, 0, 5, 0);
        JLabel identityLabel = new JLabel("$ [FAYDA_ID]");
        identityLabel.setForeground(AppTheme.getTextColor());
        identityLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        form.add(identityLabel, formGbc);

        formGbc.gridy++;
        formGbc.insets = new Insets(5, 0, 15, 0);
        AppTheme.styleInput(identityField);
        form.add(identityField, formGbc);

        // Password Field
        formGbc.gridy++;
        formGbc.insets = new Insets(10, 0, 5, 0);
        JLabel passwordLabel = new JLabel("$ [PASSWORD]");
        passwordLabel.setForeground(AppTheme.getTextColor());
        passwordLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        form.add(passwordLabel, formGbc);

        formGbc.gridy++;
        formGbc.insets = new Insets(5, 0, 25, 0);
        AppTheme.styleInput(passwordField);
        form.add(passwordField, formGbc);

        // Buttons Panel - Fixed Size
        formGbc.gridy++;
        formGbc.insets = new Insets(15, 0, 0, 0);
        formGbc.weightx = 1.0;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 12, 0));
        buttonPanel.setBackground(AppTheme.getSurfaceBackground());
        buttonPanel.setPreferredSize(new Dimension(0, 50));

        JButton loginBtn = AppTheme.primaryButton("EXECUTE");
        JButton registerBtn = AppTheme.outlineButton("REGISTER");

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegisterFrame());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        form.add(buttonPanel, formGbc);

        container.add(form, gbc);
        return container;
    }

    public void setLogoImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
                logoLabel.setText("");
            } else {
                logoLabel.setText("◆");
                logoLabel.setFont(new Font("Courier New", Font.BOLD, 60));
                logoLabel.setForeground(AppTheme.getTextColor());
            }
        } catch (Exception e) {
            logoLabel.setText("◆");
            logoLabel.setFont(new Font("Courier New", Font.BOLD, 60));
            logoLabel.setForeground(AppTheme.getTextColor());
        }
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

    private void openRegisterFrame() {
        dispose();
        new RegisterFrame().setVisible(true);
    }

    private void openAboutPage() {
        JFrame aboutFrame = new JFrame("About - Voting & Democracy");
        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aboutFrame.setSize(1000, 700);
        aboutFrame.setLocationRelativeTo(this);
        aboutFrame.getContentPane().setBackground(AppTheme.getCanvasBackground());

        JPanel content = new JPanel();
        content.setBackground(AppTheme.getCanvasBackground());
        content.setLayout(new BorderLayout(20, 20));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(AppTheme.getSurfaceBackground());
        textArea.setForeground(AppTheme.getTextColor());
        textArea.setText(
            "$ cat about.voting\n" +
            "════════════════════════════════════════════════════════════\n\n" +
            "[INFO] ABOUT VOTING & DEMOCRACY\n\n" +
            "Democracy is a system where power is vested in the people,\n" +
            "who exercise that power directly or through elected representatives.\n" +
            "The foundation of democracy is the right to vote.\n\n" +
            "[SECTION] KEY PRINCIPLES OF DEMOCRACY:\n" +
            "  ► Freedom of Speech and Expression\n" +
            "  ► Right to Vote and Participate\n" +
            "  ► Rule of Law\n" +
            "  ► Transparency and Accountability\n" +
            "  ► Separation of Powers\n\n" +
            "[SECTION] THE IMPORTANCE OF VOTING:\n" +
            "Voting enables citizens to have a voice in government decisions\n" +
            "and shape the future of their nation. Regular and fair elections\n" +
            "are essential for maintaining democratic values.\n\n" +
            "[SECTION] ELECTORAL INTEGRITY:\n" +
            "A secure voting system ensures that every vote is counted\n" +
            "accurately and fairly. This application provides transparency,\n" +
            "security, and efficiency in the voting process.\n\n" +
            "[STATUS] Ready for execution..."
        );

        JScrollPane scrollPane = AppTheme.scrollPane(textArea);
        content.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = AppTheme.primaryButton("CLOSE");
        closeBtn.addActionListener(e -> aboutFrame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(AppTheme.getCanvasBackground());
        buttonPanel.add(closeBtn);
        content.add(buttonPanel, BorderLayout.SOUTH);

        aboutFrame.setContentPane(content);
        aboutFrame.setVisible(true);
    }

    private void openContactPage() {
        JFrame contactFrame = new JFrame("Contact Us");
        contactFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactFrame.setSize(900, 650);
        contactFrame.setLocationRelativeTo(this);
        contactFrame.getContentPane().setBackground(AppTheme.getCanvasBackground());

        JPanel content = new JPanel();
        content.setBackground(AppTheme.getCanvasBackground());
        content.setLayout(new GridBagLayout());
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("$ contact -i nebe@voting.gov");
        title.setFont(new Font("Courier New", Font.BOLD, 20));
        title.setForeground(AppTheme.getTextColor());
        content.add(title, gbc);

        gbc.gridy++;
        JLabel divider = new JLabel("════════════════════════════════════════");
        divider.setFont(new Font("Courier New", Font.PLAIN, 12));
        divider.setForeground(AppTheme.getMutedTextColor());
        content.add(divider, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel emailTitle = new JLabel("[EMAIL]");
        emailTitle.setFont(new Font("Courier New", Font.BOLD, 12));
        emailTitle.setForeground(AppTheme.getTextColor());
        content.add(emailTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 15, 0);
        JTextField emailField = new JTextField("contact@nebe.gov.et");
        emailField.setEditable(false);
        emailField.setFont(new Font("Courier New", Font.PLAIN, 12));
        emailField.setBackground(AppTheme.getSurfaceBackground());
        emailField.setForeground(AppTheme.getTextColor());
        emailField.setBorder(new EmptyBorder(8, 12, 8, 12));
        content.add(emailField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel phoneTitle = new JLabel("[PHONE]");
        phoneTitle.setFont(new Font("Courier New", Font.BOLD, 12));
        phoneTitle.setForeground(AppTheme.getTextColor());
        content.add(phoneTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 15, 0);
        JTextField phoneField = new JTextField("+251 116 45 9000");
        phoneField.setEditable(false);
        phoneField.setFont(new Font("Courier New", Font.PLAIN, 12));
        phoneField.setBackground(AppTheme.getSurfaceBackground());
        phoneField.setForeground(AppTheme.getTextColor());
        phoneField.setBorder(new EmptyBorder(8, 12, 8, 12));
        content.add(phoneField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel addressTitle = new JLabel("[ADDRESS]");
        addressTitle.setFont(new Font("Courier New", Font.BOLD, 12));
        addressTitle.setForeground(AppTheme.getTextColor());
        content.add(addressTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 25, 0);
        JTextArea addressArea = new JTextArea("National Election Board of Ethiopia\nAddis Ababa, Ethiopia\n[HOURS] 8:00 AM - 5:00 PM (MON-FRI)");
        addressArea.setEditable(false);
        addressArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        addressArea.setBackground(AppTheme.getSurfaceBackground());
        addressArea.setForeground(AppTheme.getTextColor());
        addressArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        content.add(addressArea, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton closeBtn = AppTheme.primaryButton("CLOSE");
        closeBtn.addActionListener(e -> contactFrame.dispose());
        content.add(closeBtn, gbc);

        contactFrame.setContentPane(content);
        contactFrame.setVisible(true);
    }
}
