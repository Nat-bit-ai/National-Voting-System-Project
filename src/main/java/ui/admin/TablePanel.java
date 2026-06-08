package ui.admin;

import ui.shared.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

abstract class TablePanel extends JPanel {
    protected final DefaultTableModel model;
    protected final JTable table;

    protected TablePanel(String[] columns) {
        super(new BorderLayout(14, 14));
        setBackground(AppTheme.CANVAS);
        setBorder(new EmptyBorder(16, 4, 4, 4));
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        AppTheme.styleTable(table);
        add(AppTheme.scrollPane(table), BorderLayout.CENTER);
    }

    protected JTextField field(String placeholder) {
        JTextField field = new JTextField();
        field.putClientProperty("JTextField.placeholderText", placeholder);
        AppTheme.styleInput(field);
        return field;
    }

    protected JPanel actionPanel(String title, String description, int rows, int columns) {
        FormSection panel = new FormSection();
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBackground(AppTheme.SURFACE);
        panel.setBorder(AppTheme.cardPanel().getBorder());

        JPanel copy = new JPanel(new GridLayout(2, 1, 0, 2));
        copy.setOpaque(false);
        copy.add(AppTheme.eyebrow("Management"));
        copy.add(AppTheme.sectionTitle(title));
        panel.add(copy, BorderLayout.NORTH);
        panel.add(AppTheme.mutedLabel(description), BorderLayout.SOUTH);

        JPanel grid = new JPanel(new GridLayout(rows, columns, 10, 8));
        grid.setOpaque(false);
        panel.add(grid, BorderLayout.CENTER);
        panel.setGrid(grid);
        return panel;
    }

    protected JPanel labeled(String label, JComponent input) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setOpaque(false);
        AppTheme.styleInput(input);
        JLabel caption = new JLabel(label);
        caption.setFont(new Font("Segoe UI", Font.BOLD, 12));
        caption.setForeground(AppTheme.INK);
        wrapper.add(caption, BorderLayout.NORTH);
        wrapper.add(input, BorderLayout.CENTER);
        return wrapper;
    }

    protected JPanel spacer() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    protected int selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            throw new IllegalArgumentException("Select a row first.");
        }
        return Integer.parseInt(model.getValueAt(row, 0).toString());
    }

    protected abstract void refresh();

    private static final class FormSection extends JPanel {
        private JPanel grid;

        void setGrid(JPanel grid) {
            this.grid = grid;
        }

        @Override
        protected void addImpl(Component component, Object constraints, int index) {
            if (grid != null && constraints == null) {
                grid.add(component, index);
                return;
            }
            super.addImpl(component, constraints, index);
        }
    }
}
