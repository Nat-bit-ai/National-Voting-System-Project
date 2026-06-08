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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class VoterDashboardFrame extends BaseFrame {
    private final Voter voter;
    private final VotingService votingService = new VotingService();
    private final JPanel candidatePanel = new JPanel(new GridLayout(0, 2, 14, 14));
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
        JButton logout = AppTheme.secondaryButton("Logout");
        logout.addActionListener(e -> logout());
        top.add(logout, BorderLayout.EAST);

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

    private JPanel card(Candidate candidate) {
        JPanel card = AppTheme.cardPanel();
        card.setLayout(new BorderLayout(12, 12));
        JLabel name = new JLabel(candidate.getFullName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 19));
        name.setForeground(AppTheme.INK);
        JLabel party = new JLabel(candidate.getParty() == null ? "Independent" : candidate.getParty().getName());
        party.setForeground(AppTheme.MUTED);
        party.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JPanel copy = new JPanel(new GridLayout(2, 1, 0, 3));
        copy.setOpaque(false);
        copy.add(name);
        copy.add(party);
        JButton vote = AppTheme.primaryButton("Vote");
        vote.addActionListener(e -> vote(candidate));
        card.add(candidatePhoto(candidate), BorderLayout.WEST);
        card.add(copy, BorderLayout.CENTER);
        card.add(vote, BorderLayout.SOUTH);
        return card;
    }

    private JComponent candidatePhoto(Candidate candidate) {
        JLabel label = new JLabel(initials(candidate.getFullName()), SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(74, 74));
        label.setOpaque(true);
        label.setBackground(AppTheme.GOLD_SOFT);
        label.setForeground(AppTheme.GREEN_DARK);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        String path = candidate.getPhotoPath();
        if (path == null || path.isBlank()) {
            return label;
        }
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (image != null) {
                label.setText("");
                label.setIcon(new ImageIcon(image.getScaledInstance(74, 74, Image.SCALE_SMOOTH)));
            }
        } catch (Exception ignored) {
            // Keep the initials fallback if the configured photo is missing.
        }
        return label;
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
