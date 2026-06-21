package ui.voter;

import model.Candidate;
import model.Election;
import model.Voter;
import service.VotingService;
import ui.auth.LoginFrame;
import ui.shared.AppTheme;
import ui.shared.BaseFrame;
import util.SessionManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class VoterDashboardFrame extends BaseFrame {
    private final Voter voter;
    private final VotingService votingService = new VotingService();
    private final JPanel candidatePanel = new JPanel(new GridLayout(0, 2, 18, 18));
    private Election activeElection;

    public VoterDashboardFrame(Voter voter) {
        super("Voter Dashboard", 1040, 720);
        this.voter = voter;
        initializeFrame();
        loadCandidates();
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBackground(AppTheme.CANVAS);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(AppTheme.SURFACE);
        top.setBorder(AppTheme.cardPanel().getBorder());
        JPanel copy = new JPanel(new GridLayout(3, 1, 0, 2));
        copy.setOpaque(false);
        copy.add(AppTheme.eyebrow("Ballot workspace"));
        copy.add(AppTheme.title("Welcome, " + voter.getFullName()));
        copy.add(AppTheme.mutedLabel("Review the approved candidates and submit one secure vote."));
        top.add(copy, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton theme = AppTheme.themeIconButton();
        JButton logout = AppTheme.secondaryButton("Logout");
        theme.addActionListener(e -> toggleTheme());
        logout.addActionListener(e -> logout());
        actions.add(theme);
        actions.add(logout);
        top.add(actions, BorderLayout.EAST);

        candidatePanel.setOpaque(false);
        candidatePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        root.add(top, BorderLayout.NORTH);
        root.add(AppTheme.scrollPane(candidatePanel), BorderLayout.CENTER);
        return root;
    }

    private void loadCandidates() {
        if (candidatePanel == null) {
            return;
        }
        candidatePanel.removeAll();
        try {
            activeElection = votingService.activeElection();
            List<Candidate> candidates = votingService.candidatesFor(activeElection);
            for (Candidate candidate : candidates) {
                candidatePanel.add(card(candidate));
            }
            if (candidates.isEmpty()) {
                candidatePanel.add(new JLabel("No approved candidates are assigned to the active election."));
            }
        } catch (Exception ex) {
            candidatePanel.add(new JLabel(ex.getMessage()));
        }
        candidatePanel.revalidate();
        candidatePanel.repaint();
    }

    @Override
    protected void toggleTheme() {
        AppTheme.setDarkMode(!AppTheme.isDarkMode());
        setContentPane(buildContent());
        loadCandidates();
        revalidate();
        repaint();
        SwingUtilities.updateComponentTreeUI(this);
    }

    private JPanel card(Candidate candidate) {
        JPanel card = AppTheme.cardPanel();
        card.setLayout(new BorderLayout(16, 14));
        card.setBackground(AppTheme.SURFACE);
        card.setPreferredSize(new Dimension(440, 260));
        JLabel name = new JLabel(candidate.getFullName());
        name.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 22));
        name.setForeground(AppTheme.INK);
        JLabel party = new JLabel(candidate.getParty() == null ? "Independent" : candidate.getParty().getName());
        party.setForeground(AppTheme.MUTED);
        party.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 14));
        JLabel prompt = AppTheme.mutedLabel("Tap vote when this is your confirmed choice.");
        JPanel copy = new JPanel(new GridLayout(3, 1, 0, 5));
        copy.setOpaque(false);
        copy.add(name);
        copy.add(party);
        copy.add(prompt);
        JButton vote = AppTheme.primaryButton("Vote");
        vote.addActionListener(e -> vote(candidate));

        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setOpaque(false);
        footer.add(AppTheme.eyebrow("Verified candidate"), BorderLayout.WEST);
        footer.add(vote, BorderLayout.EAST);

        card.add(candidateMedia(candidate), BorderLayout.WEST);
        card.add(copy, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        installCardHover(card);
        return card;
    }

    private JComponent candidateMedia(Candidate candidate) {
        JPanel media = new JPanel(new BorderLayout(8, 8));
        media.setOpaque(false);
        media.setPreferredSize(new Dimension(190, 214));
        media.add(candidatePhoto(candidate), BorderLayout.CENTER);
        media.add(partyLogo(candidate), BorderLayout.SOUTH);
        return media;
    }

    private JComponent candidatePhoto(Candidate candidate) {
        JLabel label = new JLabel(initials(candidate.getFullName()), SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(186, 164));
        label.setOpaque(true);
        label.setBackground(AppTheme.GOLD_SOFT);
        label.setForeground(AppTheme.GREEN_DARK);
        label.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 34));
        label.setBorder(BorderFactory.createCompoundBorder(
                new AppTheme.RoundedLineBorder(AppTheme.PALETTE_MAGENTA, 8),
                new EmptyBorder(8, 8, 8, 8)
        ));
        String path = candidate.getPhotoPath();
        if (path == null || path.isBlank()) {
            return label;
        }
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (image != null) {
                label.setText("");
                label.setIcon(fitIcon(image, 168, 146));
            }
        } catch (Exception ignored) {
            // Keep the initials fallback if the configured photo is missing.
        }
        return label;
    }

    private JComponent partyLogo(Candidate candidate) {
        String partyName = candidate.getParty() == null ? "Independent" : candidate.getParty().getName();
        JLabel label = new JLabel(initials(partyName), SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(186, 52));
        label.setOpaque(true);
        label.setBackground(AppTheme.SURFACE_ALT);
        label.setForeground(AppTheme.PALETTE_MAGENTA);
        label.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 18));
        label.setBorder(BorderFactory.createCompoundBorder(
                new AppTheme.RoundedLineBorder(AppTheme.BORDER_STRONG, 8),
                new EmptyBorder(6, 8, 6, 8)
        ));

        String path = candidate.getParty() == null ? null : candidate.getParty().getSymbolPath();
        if (path == null || path.isBlank()) {
            return label;
        }
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (image != null) {
                label.setText("");
                label.setIcon(fitIcon(image, 168, 38));
            }
        } catch (Exception ignored) {
            // Keep the party initials fallback if the logo path is unavailable.
        }
        return label;
    }

    private ImageIcon fitIcon(BufferedImage image, int maxWidth, int maxHeight) {
        double scale = Math.min(maxWidth / (double) image.getWidth(), maxHeight / (double) image.getHeight());
        int width = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(image.getHeight() * scale));
        BufferedImage canvas = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, (maxWidth - width) / 2, (maxHeight - height) / 2, width, height, null);
        g2.dispose();
        return new ImageIcon(canvas);
    }

    private void installCardHover(JPanel card) {
        Border normalBorder = card.getBorder();
        Border hoverBorder = BorderFactory.createCompoundBorder(
                new AppTheme.RoundedLineBorder(AppTheme.PALETTE_MAGENTA, 8),
                new EmptyBorder(15, 15, 15, 15)
        );
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(AppTheme.SURFACE_ALT);
                card.setBorder(hoverBorder);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(AppTheme.SURFACE);
                card.setBorder(normalBorder);
            }
        });
    }

    private String initials(String name) {
        String[] parts = name == null ? new String[0] : name.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isBlank()) {
            return "C";
        }
        String first = parts[0].substring(0, 1);
        String second = parts.length > 1 ? parts[1].substring(0, 1) : "";
        return (first + second).toUpperCase();
    }

    private void vote(Candidate candidate) {
        try {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Confirm vote for " + candidate.getFullName() + "?",
                    "Confirm vote",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                votingService.castVote(voter, activeElection, candidate);
                showInfo("Vote submitted successfully. You will be logged out.");
                logout();
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void logout() {
        SessionManager.getInstance().clear();
        dispose();
        new LoginFrame().setVisible(true);
    }
}
