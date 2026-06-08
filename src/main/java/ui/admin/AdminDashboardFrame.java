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
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.prefs.Preferences;

public class AdminDashboardFrame extends BaseFrame {
    private static final String LOGO_PATH_KEY = "adminDashboardLogoPath";

    private final Admin admin;
    private final Preferences preferences = Preferences.userNodeForPackage(AdminDashboardFrame.class);
    private JLabel logoLabel;

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

        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(64, 64));
        loadLogo(preferences.get(LOGO_PATH_KEY, ""));

        JPanel titleCopy = new JPanel(new GridLayout(4, 1, 0, 2));
        titleCopy.setOpaque(false);
        titleCopy.add(AppTheme.eyebrow("Election administration"));
        titleCopy.add(AppTheme.title("Admin Control Center"));
        titleCopy.add(AppTheme.mutedLabel("Manage elections, parties, candidates, officers, and live result monitoring."));
        titleCopy.add(AppTheme.mutedLabel(admin.getFullName() + " | " + admin.getDisplayRole()));

        JPanel brand = new JPanel(new BorderLayout(12, 0));
        brand.setOpaque(false);
        brand.add(logoLabel, BorderLayout.WEST);
        brand.add(titleCopy, BorderLayout.CENTER);
        top.add(brand, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setOpaque(false);
        JButton changeLogo = AppTheme.secondaryButton("Change logo");
        JButton logout = AppTheme.secondaryButton("Logout");
        changeLogo.addActionListener(e -> chooseLogo());
        logout.addActionListener(e -> {
            SessionManager.getInstance().clear();
            dispose();
            new LoginFrame().setVisible(true);
        });
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

    private void chooseLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose dashboard logo photo");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif", "webp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            preferences.put(LOGO_PATH_KEY, file.getAbsolutePath());
            loadLogo(file.getAbsolutePath());
        }
    }

    private void loadLogo(String path) {
        try {
            if (path != null && !path.isBlank()) {
                BufferedImage image = ImageIO.read(new File(path));
                if (image != null) {
                    logoLabel.setText("");
                    logoLabel.setOpaque(false);
                    logoLabel.setIcon(new ImageIcon(image.getScaledInstance(56, 56, Image.SCALE_SMOOTH)));
                    return;
                }
            }
        } catch (Exception ignored) {
            // Fall back to the built-in mark if the selected image is moved or cannot be read.
        }
        logoLabel.setIcon(null);
        logoLabel.setText("ND");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(AppTheme.GREEN_DARK);
    }
}
