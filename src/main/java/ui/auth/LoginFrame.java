package ui.auth;

import model.Admin;
import model.Voter;
import service.AuthService;
import ui.admin.AdminDashboardFrame;
import ui.shared.AppTheme;
import ui.shared.BaseFrame;
import ui.voter.VoterDashboardFrame;
import util.QRGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.prefs.Preferences;

public class LoginFrame extends BaseFrame {
    private static final String HERO_LINE = "Your vote, counted clearly and protected with confidence.";
    private static final String NATIONAL_PORTAL_URL = "https://www.nebe.org.et";

    private final AuthService authService = new AuthService();
    private final Preferences preferences = Preferences.userRoot().node(AppTheme.PREF_NODE);
    private final JTextField identityField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> loginMode = new JComboBox<>(new String[]{"Voter", "Admin"});
    private CircularLogoLabel logoLabel;
    private JLabel typingLabel;
    private Timer typingTimer;
    private boolean typingPlayed;

    public LoginFrame() {
        super("National Digital Voting System", 1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeFrame();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.getCanvasBackground());
        root.add(createNavbar(), BorderLayout.NORTH);
        root.add(createMainContent(), BorderLayout.CENTER);
        root.add(createPortalButtonPanel(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(AppTheme.getSurfaceBackground());
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.getBorderColor()));
        navbar.setPreferredSize(new Dimension(0, 70));

        JLabel brand = new JLabel("Ethiopian Voting System");
        brand.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 17));
        brand.setForeground(AppTheme.getTextColor());
        brand.setBorder(new EmptyBorder(0, 20, 0, 0));

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 16));
        navButtons.setBackground(AppTheme.getSurfaceBackground());

        JButton homeBtn = createNavButton("Home");
        JButton aboutBtn = createNavButton("About");
        JButton contactBtn = createNavButton("Contact");
        JButton themeBtn = AppTheme.themeIconButton();

        aboutBtn.addActionListener(e -> openAboutPage());
        contactBtn.addActionListener(e -> openContactPage());
        themeBtn.addActionListener(e -> toggleTheme());

        navButtons.add(homeBtn);
        navButtons.add(aboutBtn);
        navButtons.add(contactBtn);
        navButtons.add(themeBtn);

        navbar.add(brand, BorderLayout.WEST);
        navbar.add(navButtons, BorderLayout.EAST);
        return navbar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 12));
        btn.setBackground(AppTheme.getSurfaceBackground());
        btn.setForeground(AppTheme.getTextColor());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 10, 6, 10));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(AppTheme.getPrimaryAccent());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(AppTheme.getTextColor());
            }
        });
        return btn;
    }

    private JPanel createPortalButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 10));
        panel.setBackground(AppTheme.getCanvasBackground());
        JButton qrButton = AppTheme.primaryButton("Portal QR");
        qrButton.setToolTipText("Show the national voting portal QR code");
        qrButton.addActionListener(e -> showPortalQr());
        panel.add(qrButton);
        return panel;
    }

    private JPanel createMainContent() {
        return new ResponsiveMainPanel(createHeroSection(), createFormSection());
    }

    private static final class ResponsiveMainPanel extends JPanel {
        private final JPanel hero;
        private final JPanel form;
        private boolean stacked;

        private ResponsiveMainPanel(JPanel hero, JPanel form) {
            this.hero = hero;
            this.form = form;
            setBackground(AppTheme.getCanvasBackground());
            setLayout(new GridBagLayout());
            addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    updateLayoutMode();
                }
            });
            updateLayoutMode();
        }

        private void updateLayoutMode() {
            boolean shouldStack = getWidth() > 0 && getWidth() < 900;
            if (shouldStack == stacked && getComponentCount() > 0) {
                return;
            }
            stacked = shouldStack;
            removeAll();
            setLayout(new GridBagLayout());

            if (stacked) {
                addStacked();
            } else {
                addColumns();
            }

            revalidate();
            repaint();
        }

        private void addColumns() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.gridy = 0;

            gbc.gridx = 0;
            gbc.weightx = 0.56;
            add(hero, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.44;
            add(form, gbc);
        }

        private void addStacked() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 0;
            gbc.weightx = 1.0;

            gbc.gridy = 0;
            gbc.weighty = 0.35;
            add(hero, gbc);

            gbc.gridy = 1;
            gbc.weighty = 0.65;
            add(form, gbc);
        }
    }

    private JPanel legacyTwoColumnMainContent() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(AppTheme.getCanvasBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 0.56;
        main.add(createHeroSection(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.44;
        main.add(createFormSection(), gbc);
        return main;
    }

    private JPanel createHeroSection() {
        JPanel hero = AppTheme.heroPanel();
        hero.setLayout(new GridBagLayout());
        hero.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        logoLabel = new CircularLogoLabel();
        logoLabel.setText("NDVS");
        logoLabel.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 18));
        logoLabel.setForeground(AppTheme.getPrimaryAccent());
        logoLabel.setPreferredSize(new Dimension(240, 240));
        logoLabel.setBackground(AppTheme.GOLD_SOFT);
        loadHomeLogo(homeLogoPath());
        hero.add(logoLabel, gbc);

        gbc.gridy++;
        JLabel title = new JLabel("National Digital Voting System", JLabel.CENTER);
        title.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 30));
        title.setForeground(AppTheme.getTextColor());
        hero.add(title, gbc);

        gbc.gridy++;
        JLabel subtitle = new JLabel("<html><center>Register voters, manage ballots, and monitor live election results<br>from one focused national workspace.</center></html>", JLabel.CENTER);
        subtitle.setFont(new Font(AppTheme.FONT_FAMILY, Font.PLAIN, 16));
        subtitle.setForeground(AppTheme.getMutedTextColor());
        hero.add(subtitle, gbc);

        gbc.gridy++;
        typingLabel = new JLabel("", JLabel.CENTER);
        typingLabel.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 16));
        typingLabel.setForeground(AppTheme.getPrimaryAccent());
        hero.add(typingLabel, gbc);
        startTypingEffect();
        return hero;
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

        JPanel form = AppTheme.cardPanel();
        form.setLayout(new GridBagLayout());

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(9, 0, 9, 0);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.weightx = 1.0;

        JLabel formTitle = new JLabel("Login");
        formTitle.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 22));
        formTitle.setForeground(AppTheme.getTextColor());
        form.add(formTitle, formGbc);

        formGbc.gridy++;
        JLabel helper = AppTheme.mutedLabel("Choose your account type and sign in.");
        form.add(helper, formGbc);

        addFieldLabel(form, formGbc, "Account type");
        AppTheme.styleInput(loginMode);
        form.add(loginMode, formGbc);

        addFieldLabel(form, formGbc, "Fayda ID or admin username");
        AppTheme.styleInput(identityField);
        form.add(identityField, formGbc);

        addFieldLabel(form, formGbc, "Password");
        AppTheme.styleInput(passwordField);
        form.add(passwordField, formGbc);

        formGbc.gridy++;
        formGbc.insets = new Insets(18, 0, 0, 0);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setBackground(AppTheme.getSurfaceBackground());
        buttonPanel.setMinimumSize(new Dimension(260, 48));
        buttonPanel.setPreferredSize(new Dimension(320, 48));

        JButton loginBtn = AppTheme.primaryButton("Login");
        JButton registerBtn = AppTheme.outlineButton("Register");
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegisterFrame());
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        form.add(buttonPanel, formGbc);

        container.add(form, gbc);
        return container;
    }

    private void addFieldLabel(JPanel form, GridBagConstraints formGbc, String text) {
        formGbc.gridy++;
        formGbc.insets = new Insets(14, 0, 3, 0);
        JLabel label = new JLabel(text);
        label.setForeground(AppTheme.getTextColor());
        label.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 12));
        form.add(label, formGbc);
        formGbc.gridy++;
        formGbc.insets = new Insets(3, 0, 7, 0);
    }

    private void loadHomeLogo(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                BufferedImage image = ImageIO.read(imageFile);
                if (image == null) {
                    throw new IllegalArgumentException("Unsupported logo image.");
                }
                logoLabel.setImage(image);
                logoLabel.setText("");
                logoLabel.setBackground(AppTheme.getSurfaceBackground());
                logoLabel.repaint();
                return;
            }
        } catch (Exception ignored) {
            // Keep the built-in mark visible when no admin logo can be loaded.
        }
        logoLabel.setImage(null);
        logoLabel.setText("NDVS");
        logoLabel.setBackground(AppTheme.GOLD_SOFT);
        logoLabel.repaint();
    }

    private String homeLogoPath() {
        String path = preferences.get(AppTheme.HOME_LOGO_PATH_KEY, "");
        if (path == null || path.isBlank()) {
            path = Preferences.userNodeForPackage(AdminDashboardFrame.class).get("adminDashboardLogoPath", "");
            if (path != null && !path.isBlank()) {
                preferences.put(AppTheme.HOME_LOGO_PATH_KEY, path);
            }
        }
        return path;
    }

    private void startTypingEffect() {
        if (typingTimer != null) {
            typingTimer.stop();
        }
        if (typingPlayed) {
            typingLabel.setText(HERO_LINE);
            return;
        }
        final int[] index = {0};
        typingTimer = new Timer(65, e -> {
            if (typingLabel == null) {
                ((Timer) e.getSource()).stop();
                return;
            }
            if (index[0] >= HERO_LINE.length()) {
                typingLabel.setText(HERO_LINE);
                typingPlayed = true;
                ((Timer) e.getSource()).stop();
                return;
            }
            typingLabel.setText(HERO_LINE.substring(0, index[0]) + (index[0] % 2 == 0 ? "|" : ""));
            index[0]++;
        });
        typingTimer.start();
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
        aboutFrame.setSize(980, 680);
        aboutFrame.setLocationRelativeTo(this);
        aboutFrame.getContentPane().setBackground(AppTheme.getCanvasBackground());

        JPanel content = new JPanel(new BorderLayout(22, 22));
        content.setBackground(AppTheme.getCanvasBackground());
        content.setBorder(new EmptyBorder(34, 40, 34, 40));

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        textPane.setBackground(AppTheme.getSurfaceBackground());
        textPane.setBorder(new EmptyBorder(24, 30, 24, 30));
        textPane.setText("""
                <html><body style='font-family:%s; font-size:14px; color:%s; line-height:1.65;'>
                <div style='letter-spacing:1px; color:%s; font-size:11px; font-weight:bold;'>ABOUT THE PLATFORM</div>
                <h1 style='color:%s; margin:6px 0 10px 0; font-size:28px;'>A clearer way to protect every vote.</h1>
                <p style='font-size:15px;'>The National Digital Voting System gives voters a calm, secure place to participate and gives election teams a clean workspace for registration, ballots, candidates, and live monitoring.</p>
                <h2 style='color:%s; margin-top:22px;'>Built around trust</h2>
                <p>Good elections need participation, transparency, accountability, and a process citizens can understand. This system keeps those ideas visible from login to final result monitoring.</p>
                <h2 style='color:%s; margin-top:22px;'>Designed for real election work</h2>
                <p>Admins can manage elections, parties, candidates, officers, and live results while voters see a focused ballot experience with candidate photos and party symbols.</p>
                </body></html>
                """.formatted(AppTheme.FONT_FAMILY, hex(AppTheme.getTextColor()), hex(AppTheme.getPrimaryAccent()),
                hex(AppTheme.getPrimaryAccent()), hex(AppTheme.getPrimaryAccent()), hex(AppTheme.getPrimaryAccent())));

        content.add(AppTheme.scrollPane(textPane), BorderLayout.CENTER);

        JButton closeBtn = AppTheme.primaryButton("Close");
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
        contactFrame.setSize(900, 600);
        contactFrame.setLocationRelativeTo(this);
        contactFrame.getContentPane().setBackground(AppTheme.getCanvasBackground());

        JPanel content = AppTheme.cardPanel();
        content.setLayout(new GridBagLayout());
        content.setBorder(new EmptyBorder(36, 46, 36, 46));
        content.setPreferredSize(new Dimension(720, 470));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel title = AppTheme.title("Contact NEBE");
        title.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 30));
        content.add(title, gbc);

        gbc.gridy++;
        JLabel intro = new JLabel("<html><b>Election support contact information</b><br>Reach the support desk for voting, registration, and election operations assistance.</html>");
        intro.setFont(new Font(AppTheme.FONT_FAMILY, Font.PLAIN, 14));
        intro.setForeground(AppTheme.getMutedTextColor());
        content.add(intro, gbc);

        addReadOnlyField(content, gbc, "Email", "contact@nebe.gov.et");
        addReadOnlyField(content, gbc, "Phone", "+251 116 45 9000");
        addReadOnlyField(content, gbc, "Address", "National Election Board of Ethiopia, Addis Ababa, Ethiopia");
        addReadOnlyField(content, gbc, "Hours", "8:00 AM - 5:00 PM (Monday-Friday)");

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton closeBtn = AppTheme.primaryButton("Close");
        closeBtn.addActionListener(e -> contactFrame.dispose());
        content.add(closeBtn, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(AppTheme.getCanvasBackground());
        wrapper.setBorder(new EmptyBorder(26, 26, 26, 26));
        wrapper.add(content, new GridBagConstraints());
        contactFrame.setContentPane(wrapper);
        contactFrame.setVisible(true);
    }

    private void addReadOnlyField(JPanel content, GridBagConstraints gbc, String labelText, String value) {
        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 4, 0);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 12));
        label.setForeground(AppTheme.getTextColor());
        content.add(label, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        JTextField field = new JTextField(value);
        field.setEditable(false);
        AppTheme.styleInput(field);
        content.add(field, gbc);
    }

    private String hex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private void showPortalQr() {
        try {
            BufferedImage qr = QRGenerator.generate(NATIONAL_PORTAL_URL, 280);
            JLabel qrLabel = new JLabel(new ImageIcon(qr));
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel title = new JLabel("National voting portal", SwingConstants.CENTER);
            title.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 20));
            title.setForeground(AppTheme.getTextColor());

            JLabel url = new JLabel(NATIONAL_PORTAL_URL, SwingConstants.CENTER);
            url.setFont(new Font(AppTheme.FONT_FAMILY, Font.PLAIN, 13));
            url.setForeground(AppTheme.getMutedTextColor());

            JPanel panel = AppTheme.cardPanel();
            panel.setLayout(new BorderLayout(0, 14));
            panel.setBorder(new EmptyBorder(22, 26, 22, 26));
            panel.add(title, BorderLayout.NORTH);
            panel.add(qrLabel, BorderLayout.CENTER);
            panel.add(url, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(this, panel, "Portal QR", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private static final class CircularLogoLabel extends JLabel {
        private BufferedImage image;

        private CircularLogoLabel() {
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
        }

        private void setImage(BufferedImage image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int size = Math.min(getWidth(), getHeight()) - 6;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            Ellipse2D circle = new Ellipse2D.Double(x, y, size, size);
            g2.setColor(getBackground() == null ? AppTheme.GOLD_SOFT : getBackground());
            g2.fill(circle);
            if (image != null) {
                Shape oldClip = g2.getClip();
                g2.setClip(circle);
                double scale = Math.max(size / (double) image.getWidth(), size / (double) image.getHeight());
                int width = Math.max(1, (int) Math.round(image.getWidth() * scale));
                int height = Math.max(1, (int) Math.round(image.getHeight() * scale));
                g2.drawImage(image, x + (size - width) / 2, y + (size - height) / 2, width, height, null);
                g2.setClip(oldClip);
            } else {
                super.paintComponent(graphics);
            }
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(AppTheme.getPrimaryAccent());
            g2.draw(circle);
            g2.dispose();
        }
    }
}
