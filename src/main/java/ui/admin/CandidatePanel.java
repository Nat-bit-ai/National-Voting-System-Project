package ui.admin;

import dao.CandidateDAO;
import dao.ElectionDAO;
import dao.PartyDAO;
import model.Candidate;
import model.Party;
import ui.shared.AppTheme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

class CandidatePanel extends TablePanel {
    private final CandidateDAO dao = new CandidateDAO();
    private final PartyDAO partyDAO = new PartyDAO();
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final JTextField fullName = field("Candidate name");
    private final JTextField photoPath = field("Photo path");
    private final JComboBox<Party> partyBox = new JComboBox<>();
    private final JCheckBox independent = new JCheckBox("Independent candidate");
    private final JCheckBox approved = new JCheckBox("Approved");
    private final JComboBox<String> electionBox = new JComboBox<>();

    CandidatePanel() {
        super(new String[]{"ID", "Name", "Party", "Approved", "Photo"});
        JPanel form = actionPanel(
                "Candidate management",
                "Add candidates, approve their profile, and assign them to an election ballot.",
                4,
                4
        );
        JButton add = AppTheme.primaryButton("Add");
        JButton update = AppTheme.secondaryButton("Update");
        JButton delete = AppTheme.dangerButton("Delete");
        JButton assign = AppTheme.secondaryButton("Assign to election");
        JButton browsePhoto = AppTheme.secondaryButton("Browse photo");
        form.add(labeled("Candidate full name", fullName));
        form.add(labeled("Political party", partyBox));
        form.add(labeled("Candidate type", independent));
        form.add(browsePhoto);
        form.add(labeled("Candidate photo path", photoPath));
        form.add(labeled("Approval status", approved));
        form.add(add);
        form.add(update);
        form.add(delete);
        form.add(labeled("Election ballot", electionBox));
        form.add(assign);
        form.add(spacer());
        form.add(spacer());
        form.add(spacer());
        add(form, BorderLayout.NORTH);
        add.addActionListener(e -> create());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
        assign.addActionListener(e -> assign());
        browsePhoto.addActionListener(e -> choosePhoto());
        independent.addActionListener(e -> partyBox.setEnabled(!independent.isSelected()));
        loadParties();
        loadElections();
        refresh();
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose candidate photo");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif", "webp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            photoPath.setText(file.getAbsolutePath());
        }
    }

    private void loadParties() {
        try {
            Party selected = (Party) partyBox.getSelectedItem();
            partyBox.removeAllItems();
            for (Party p : partyDAO.findAll()) {
                partyBox.addItem(p);
                if (selected != null && selected.getId() == p.getId()) {
                    partyBox.setSelectedItem(p);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private Candidate formCandidate() {
        Party selectedParty = independent.isSelected() ? null : (Party) partyBox.getSelectedItem();
        if (!independent.isSelected() && selectedParty == null) {
            throw new IllegalArgumentException("Create or select a political party first.");
        }
        return new Candidate(fullName.getText(), selectedParty, photoPath.getText(), approved.isSelected());
    }

    private void loadElections() {
        try {
            String selected = (String) electionBox.getSelectedItem();
            electionBox.removeAllItems();
            electionDAO.findAll().forEach(e -> electionBox.addItem(e.getId() + " - " + e.getTitle()));
            if (selected != null) {
                electionBox.setSelectedItem(selected);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    void reloadLookups() {
        loadParties();
        loadElections();
        refresh();
    }

    private void assign() {
        try {
            String selected = (String) electionBox.getSelectedItem();
            if (selected == null) {
                throw new IllegalArgumentException("Create an election first.");
            }
            int electionId = Integer.parseInt(selected.split(" - ")[0]);
            dao.assignToElection(selectedId(), electionId);
            JOptionPane.showMessageDialog(this, "Candidate assigned to election.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void create() {
        try {
            dao.create(formCandidate());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void update() {
        try {
            Candidate c = formCandidate();
            c.setId(selectedId());
            dao.update(c);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void delete() {
        try {
            dao.delete(selectedId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    @Override
    protected void refresh() {
        try {
            model.setRowCount(0);
            List<Candidate> candidates = dao.findAll();
            for (Candidate c : candidates) {
                String partyName = c.getParty() == null ? "Independent" : c.getParty().getName();
                model.addRow(new Object[]{c.getId(), c.getFullName(), partyName, c.isApproved(), c.getPhotoPath()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
