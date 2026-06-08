package ui.admin;

import dao.ElectionDAO;
import model.Admin;
import model.Election;
import model.ElectionStatus;
import service.ElectionService;
import ui.shared.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class ElectionPanel extends TablePanel {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Admin admin;
    private final ElectionService service = new ElectionService();
    private final ElectionDAO dao = new ElectionDAO();
    private final JTextField title = field("Title");
    private final JTextField description = field("Description");
    private final JTextField startTime = field("yyyy-MM-dd HH:mm");
    private final JTextField endTime = field("yyyy-MM-dd HH:mm");

    ElectionPanel(Admin admin) {
        super(new String[]{"ID", "Title", "Status", "Start", "End"});
        this.admin = admin;
        startTime.setText(DATE_TIME_FORMAT.format(LocalDateTime.now().plusMinutes(5)));
        endTime.setText(DATE_TIME_FORMAT.format(LocalDateTime.now().plusDays(1)));
        JPanel form = actionPanel(
                "Election setup",
                "Create election windows and control the active voting status from one place.",
                3,
                4
        );
        JButton create = AppTheme.primaryButton("Create");
        JButton start = AppTheme.secondaryButton("Start");
        JButton close = AppTheme.secondaryButton("Close");
        JButton delete = AppTheme.dangerButton("Delete");
        form.add(labeled("Election title", title));
        form.add(labeled("Short description", description));
        form.add(labeled("Start date and time", startTime));
        form.add(labeled("End date and time", endTime));
        form.add(create);
        form.add(start);
        form.add(close);
        form.add(delete);
        form.add(spacer());
        form.add(spacer());
        add(form, BorderLayout.NORTH);
        create.addActionListener(e -> create());
        start.addActionListener(e -> status(ElectionStatus.ACTIVE));
        close.addActionListener(e -> status(ElectionStatus.CLOSED));
        delete.addActionListener(e -> delete());
        refresh();
    }

    private void create() {
        try {
            service.createElection(title.getText(), description.getText(), parseDate(startTime.getText()), parseDate(endTime.getText()), admin.getId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private LocalDateTime parseDate(String value) {
        try {
            return LocalDateTime.parse(value.trim(), DATE_TIME_FORMAT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Use date format yyyy-MM-dd HH:mm.");
        }
    }

    private void status(ElectionStatus status) {
        try {
            Election election = dao.findById(selectedId()).orElseThrow();
            service.setStatus(election, status, admin.getId());
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
            for (Election e : service.list()) {
                model.addRow(new Object[]{e.getId(), e.getTitle(), e.getStatus(), e.getStartTime(), e.getEndTime()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
