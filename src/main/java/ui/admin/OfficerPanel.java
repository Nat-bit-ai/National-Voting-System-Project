package ui.admin;

import dao.AdminDAO;
import model.Admin;
import model.AdminRole;
import ui.shared.AppTheme;
import util.BCryptUtil;

import javax.swing.*;
import java.awt.*;

class OfficerPanel extends TablePanel {
    private final AdminDAO dao = new AdminDAO();
    private final JTextField fullName = field("Full name");
    private final JTextField username = field("Username");
    private final JPasswordField password = new JPasswordField();
    private final JComboBox<AdminRole> role = new JComboBox<>(AdminRole.values());
    private final JCheckBox active = new JCheckBox("Active", true);

    OfficerPanel() {
        super(new String[]{"ID", "Name", "Username", "Role", "Active"});
        JPanel form = actionPanel(
                "Officer accounts",
                "Create admin users, set their role, and disable access when an account should be inactive.",
                2,
                5
        );
        JButton add = AppTheme.primaryButton("Add officer");
        JButton update = AppTheme.secondaryButton("Update");
        JButton delete = AppTheme.dangerButton("Delete");
        password.putClientProperty("JTextField.placeholderText", "Password");
        form.add(labeled("Full name", fullName));
        form.add(labeled("Username", username));
        form.add(labeled("Temporary password", password));
        form.add(labeled("Role", role));
        form.add(labeled("Account status", active));
        form.add(add);
        form.add(update);
        form.add(delete);
        form.add(spacer());
        form.add(spacer());
        add(form, BorderLayout.NORTH);
        add.addActionListener(e -> create());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
        refresh();
    }

    private void create() {
        try {
            dao.create(new Admin(fullName.getText(), username.getText(), BCryptUtil.hash(new String(password.getPassword())), (AdminRole) role.getSelectedItem()));
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void update() {
        try {
            Admin admin = new Admin(fullName.getText(), username.getText(), "", (AdminRole) role.getSelectedItem());
            admin.setId(selectedId());
            admin.setActive(active.isSelected());
            dao.update(admin);
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
            for (Admin a : dao.findAll()) {
                model.addRow(new Object[]{a.getId(), a.getFullName(), a.getUsername(), a.getRole(), a.isActive()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
