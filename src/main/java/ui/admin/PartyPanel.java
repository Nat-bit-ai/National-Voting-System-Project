package ui.admin;

import dao.PartyDAO;
import model.Party;
import ui.shared.AppTheme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

class PartyPanel extends TablePanel {
    private final PartyDAO dao = new PartyDAO();
    private final JTextField name = field("Party name");
    private final JTextField leader = field("Leader");
    private final JTextField symbol = field("Symbol path");

    PartyPanel() {
        super(new String[]{"ID", "Name", "Leader", "Symbol"});
        JPanel form = actionPanel(
                "Political party records",
                "Create parties, keep headquarters details current, and attach the logo path used on ballots.",
                2,
                4
        );
        JButton add = AppTheme.primaryButton("Add");
        JButton update = AppTheme.secondaryButton("Update");
        JButton delete = AppTheme.dangerButton("Delete");
        JButton browseLogo = AppTheme.secondaryButton("Browse logo");
        form.add(labeled("Party name", name));
        form.add(labeled("Headquarters / leader", leader));
        form.add(labeled("Logo or symbol photo path", symbol));
        form.add(browseLogo);
        form.add(add);
        form.add(update);
        form.add(delete);
        form.add(spacer());
        add(form, BorderLayout.NORTH);
        add.addActionListener(e -> create());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
        browseLogo.addActionListener(e -> chooseLogo());
        table.getSelectionModel().addListSelectionListener(e -> fill());
        refresh();
    }

    private void chooseLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose party logo or symbol");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif", "webp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            symbol.setText(file.getAbsolutePath());
        }
    }

    private void create() {
        try {
            dao.create(new Party(name.getText(), leader.getText(), symbol.getText()));
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void update() {
        try {
            Party party = new Party(name.getText(), leader.getText(), symbol.getText());
            party.setId(selectedId());
            dao.update(party);
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

    private void fill() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            name.setText(model.getValueAt(row, 1).toString());
            leader.setText(model.getValueAt(row, 2).toString());
            symbol.setText(model.getValueAt(row, 3) == null ? "" : model.getValueAt(row, 3).toString());
        }
    }

    @Override
    protected void refresh() {
        try {
            model.setRowCount(0);
            for (Party p : dao.findAll()) {
                model.addRow(new Object[]{p.getId(), p.getName(), p.getLeaderName(), p.getSymbolPath()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
