package ui.admin;

import dao.ElectionDAO;
import dao.ResultDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import ui.shared.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class MonitoringPanel extends JPanel {
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final ResultDAO resultDAO = new ResultDAO();
    private final JComboBox<String> electionBox = new JComboBox<>();
    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final Timer timer;

    MonitoringPanel() {
        super(new BorderLayout(14, 14));
        setBackground(AppTheme.CANVAS);
        setBorder(new EmptyBorder(16, 4, 4, 4));
        JPanel controls = new JPanel(new BorderLayout(12, 12));
        controls.setBackground(AppTheme.SURFACE);
        controls.setBorder(AppTheme.cardPanel().getBorder());
        JPanel copy = new JPanel(new GridLayout(3, 1, 0, 2));
        copy.setOpaque(false);
        copy.add(AppTheme.eyebrow("Live results"));
        copy.add(AppTheme.sectionTitle("Live election monitoring"));
        copy.add(AppTheme.mutedLabel("Track vote totals and refresh the live candidate ranking chart."));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        JButton refresh = AppTheme.secondaryButton("Refresh");
        AppTheme.styleInput(electionBox);
        actions.add(new JLabel("Election"));
        actions.add(electionBox);
        actions.add(refresh);
        controls.add(copy, BorderLayout.NORTH);
        controls.add(actions, BorderLayout.CENTER);
        JFreeChart chart = ChartFactory.createBarChart("Live Candidate Rankings", "Candidate", "Votes", dataset);
        chart.setBackgroundPaint(AppTheme.SURFACE);
        chart.getCategoryPlot().setBackgroundPaint(AppTheme.SURFACE_ALT);
        chart.getCategoryPlot().setRangeGridlinePaint(AppTheme.BORDER);
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, AppTheme.GREEN);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(AppTheme.SURFACE);
        chartPanel.setBorder(AppTheme.cardPanel().getBorder());
        add(controls, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        refresh.addActionListener(e -> refresh());
        loadElections();
        timer = new Timer(3000, e -> refresh());
        timer.start();
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

    void reloadElections() {
        loadElections();
        refresh();
    }

    private int selectedElectionId() {
        String item = (String) electionBox.getSelectedItem();
        if (item == null) {
            throw new IllegalArgumentException("No election selected.");
        }
        return Integer.parseInt(item.split(" - ")[0]);
    }

    private void refresh() {
        try {
            dataset.clear();
            for (ResultDAO.ResultRow row : resultDAO.findResults(selectedElectionId())) {
                dataset.addValue(row.voteCount(), "Votes", row.candidateName());
            }
        } catch (Exception ignored) {
            // Timer refresh stays quiet until the operator selects a valid election.
        }
    }
}
