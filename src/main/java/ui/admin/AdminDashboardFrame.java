package ui.admin;

import model.Admin;
import ui.auth.LoginFrame;
import ui.shared.AppTheme;
import ui.shared.BaseFrame;
import util.SessionManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.prefs.Preferences;

public class AdminDashboardFrame extends BaseFrame {
    private static final String LOGO_PATH_KEY = "adminDashboardLogoPath";

    private final Admin admin;
    private final Preferences preferences = Preferences.userRoot().node(AppTheme.PREF_NODE);
    private CircularLogoLabel logoLabel;

    public AdminDashboardFrame(Admin admin) {
        super("Admin Dashboard", 1180, 760);
        this.admin = admin;
        initializeFrame();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(AppTheme.CANVAS);
        root.setBorder(new EmptyBorder(18, 22, 18, 22));
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(AppTheme.SURFACE);
        top.setBorder(AppTheme.cardPanel().getBorder());

        logoLabel = new CircularLogoLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(88, 88));
        loadLogo(savedLogoPath());

        JPanel titleCopy = new JPanel(new GridLayout(4, 1, 0, 2));
        titleCopy.setOpaque(false);
        titleCopy.add(AppTheme.eyebrow("Election administration"));
        titleCopy.add(AppTheme.title("Admin Control Center"));
        titleCopy.add(AppTheme.mutedLabel("Manage elections, parties, candidates, officers, and live result monitoring."));
        titleCopy.add(AppTheme.mutedLabel(admin.getFullName() + " | " + admin.getDisplayRole()));

        JPanel brand = new JPanel(new BorderLayout(16, 0));
        brand.setOpaque(false);
        brand.add(logoLabel, BorderLayout.WEST);
        brand.add(titleCopy, BorderLayout.CENTER);
        top.add(brand, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setOpaque(false);
        JButton theme = AppTheme.themeIconButton();
        JButton changeLogo = AppTheme.secondaryButton("Change logo");
        JButton logout = AppTheme.secondaryButton("Logout");
        theme.addActionListener(e -> toggleTheme());
        changeLogo.addActionListener(e -> chooseLogo());
        logout.addActionListener(e -> {
            SessionManager.getInstance().clear();
            dispose();
            new LoginFrame().setVisible(true);
        });
        actions.add(theme);
        actions.add(changeLogo);
        actions.add(logout);
        top.add(actions, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(8, 0, 0, 0));
        tabs.addTab("Elections", new ElectionPanel(admin));
        tabs.addTab("Parties", new PartyPanel());
        tabs.addTab("Candidates", new CandidatePanel());
        tabs.addTab("Monitoring", new MonitoringPanel());
        if (admin.canManageOfficers()) {
            tabs.addTab("Officers", new OfficerPanel());
        }
        tabs.addChangeListener(e -> {
            Component selected = tabs.getSelectedComponent();
            if (selected instanceof CandidatePanel candidatePanel) {
                candidatePanel.reloadLookups();
            } else if (selected instanceof MonitoringPanel monitoringPanel) {
                monitoringPanel.reloadElections();
            } else if (selected instanceof TablePanel tablePanel) {
                tablePanel.refresh();
            }
        });
        root.add(top, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private String savedLogoPath() {
        String path = preferences.get(LOGO_PATH_KEY, "");
        if (path == null || path.isBlank()) {
            path = Preferences.userNodeForPackage(AdminDashboardFrame.class).get(LOGO_PATH_KEY, "");
            if (path != null && !path.isBlank()) {
                preferences.put(LOGO_PATH_KEY, path);
                preferences.put(AppTheme.HOME_LOGO_PATH_KEY, path);
            }
        }
        return path;
    }

    private void chooseLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose dashboard logo photo");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif", "webp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            preferences.put(LOGO_PATH_KEY, file.getAbsolutePath());
            preferences.put(AppTheme.HOME_LOGO_PATH_KEY, file.getAbsolutePath());
            loadLogo(file.getAbsolutePath());
        }
    }

    private void loadLogo(String path) {
        try {
            if (path != null && !path.isBlank()) {
                BufferedImage image = ImageIO.read(new File(path));
                if (image != null) {
                    logoLabel.setText("");
                    logoLabel.setImage(image);
                    return;
                }
            }
        } catch (Exception ignored) {
            // Fall back to the built-in mark if the selected image is moved or cannot be read.
        }
        logoLabel.setImage(null);
        logoLabel.setText("ND");
        logoLabel.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBackground(AppTheme.GREEN_DARK);
    }

    private static final class CircularLogoLabel extends JLabel {
        private BufferedImage image;

        private CircularLogoLabel() {
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setBorder(new EmptyBorder(8, 8, 8, 8));
        }

        private void setImage(BufferedImage image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int size = Math.min(getWidth(), getHeight()) - 4;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            Ellipse2D circle = new Ellipse2D.Double(x, y, size, size);
            g2.setColor(getBackground() == null ? AppTheme.GREEN_DARK : getBackground());
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
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(AppTheme.PALETTE_MAGENTA);
            g2.draw(circle);
            g2.dispose();
        }
    }
}
