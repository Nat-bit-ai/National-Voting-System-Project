package ui.shared;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class AppTheme {
    // ColorHunt Dark Theme
public static final Color GREEN = new Color(0, 173, 181);       // #00ADB5
public static final Color GREEN_DARK = new Color(0, 140, 147);

public static final Color GOLD = new Color(0, 173, 181);
public static final Color GOLD_SOFT = new Color(57, 62, 70);

public static final Color RED = new Color(255, 107, 107);
public static final Color RED_SOFT = new Color(57, 62, 70);

public static final Color INK = new Color(238, 238, 238);       // #EEEEEE
public static final Color MUTED = new Color(180, 180, 180);

public static final Color CANVAS = new Color(34, 40, 49);       // #222831
public static final Color SURFACE = new Color(57, 62, 70);      // #393E46
public static final Color SURFACE_ALT = new Color(48, 53, 60);

public static final Color BORDER = new Color(80, 85, 95);
public static final Color BORDER_STRONG = new Color(100, 105, 115);

public static final Color SELECTED = new Color(0, 173, 181);

    private static final Font UI = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font UI_MEDIUM = new Font("Segoe UI Semibold", Font.PLAIN, 13);
    private static final Font UI_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private AppTheme() {
    }

    public static void install() {
        UIManager.put("Panel.background", CANVAS);
        UIManager.put("OptionPane.background", CANVAS);
        UIManager.put("OptionPane.messageFont", UI);
        UIManager.put("Button.font", UI_BOLD);
        UIManager.put("Button.background", SURFACE);
        UIManager.put("Button.foreground", INK);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Label.font", UI);
        UIManager.put("TextField.font", UI);
        UIManager.put("PasswordField.font", UI);
        UIManager.put("TextField.selectionBackground", SELECTED);
        UIManager.put("PasswordField.selectionBackground", SELECTED);
        UIManager.put("ComboBox.font", UI);
        UIManager.put("CheckBox.font", UI);
        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("Table.font", UI);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.alternateRowColor", SURFACE_ALT);
        UIManager.put("Table.selectionBackground", SELECTED);
        UIManager.put("Table.selectionForeground", INK);
        UIManager.put("TableHeader.background", SURFACE_ALT);
        UIManager.put("TableHeader.foreground", MUTED);
        UIManager.put("TabbedPane.font", UI_BOLD);
        UIManager.put("TabbedPane.background", CANVAS);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Label.foreground", INK);

UIManager.put("TextField.background", SURFACE);
UIManager.put("TextField.foreground", INK);

UIManager.put("PasswordField.background", SURFACE);
UIManager.put("PasswordField.foreground", INK);

UIManager.put("ComboBox.background", SURFACE);
UIManager.put("ComboBox.foreground", INK);

UIManager.put("Table.foreground", INK);

UIManager.put("TableHeader.background", SURFACE);
UIManager.put("TableHeader.foreground", INK);

UIManager.put("TabbedPane.foreground", INK);

UIManager.put("OptionPane.foreground", INK);
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(GREEN);
        button.setForeground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(buttonBorder(GREEN_DARK));
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(new Color(0, 190, 200));
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(GREEN);
    }
});
        button.setPreferredSize(new Dimension(140, 42));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SURFACE);
        button.setForeground(INK);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(buttonBorder(BORDER_STRONG));
        button.setPreferredSize(new Dimension(140, 42));
        button.addMouseListener(new java.awt.event.MouseAdapter() {

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(new Color(70, 75, 85));
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(SURFACE);
    }
});
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = secondaryButton(text);
        button.setForeground(RED);
        button.setBackground(RED_SOFT);
        button.setBorder(buttonBorder(new Color(244, 176, 178)));
        return button;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 25));
        label.setForeground(INK);
        return label;
    }

    public static JLabel heroTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 34));
        label.setForeground(Color.WHITE);
        return label;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        label.setForeground(INK);
        return label;
    }

    public static JLabel mutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(MUTED);
        return label;
    }

    public static JLabel eyebrow(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(GREEN);
        return label;
    }

    public static JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(cardBorder());
        return panel;
    }

    public static JPanel heroPanel() {
        return new GradientPanel(GREEN_DARK, GREEN);
    }

    public static void styleInput(JComponent input) {
        input.setFont(UI);
        input.setForeground(INK);
        input.setBackground(SURFACE);
        if (input instanceof JCheckBox checkBox) {
            checkBox.setOpaque(false);
            checkBox.setBorder(new EmptyBorder(8, 0, 8, 0));
            return;
        }
        input.setBorder(inputBorder());
        if (input instanceof JTextField textField) {
            if (textField.getColumns() == 0) {
                textField.setColumns(22);
            }
            textField.setCaretColor(GREEN);
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setGridColor(new Color(235, 240, 246));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(SELECTED);
        table.setSelectionForeground(INK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
    }

    public static JScrollPane scrollPane(Component content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(cardBorder());
        scrollPane.getViewport().setBackground(SURFACE);
        return scrollPane;
    }

    private static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER, 8),
                new EmptyBorder(22, 24, 22, 24)
        );
    }

    private static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER_STRONG, 8),
                new EmptyBorder(9, 11, 9, 11)
        );
    }

    private static Border buttonBorder(Color line) {
        return BorderFactory.createCompoundBorder(
                new RoundedLineBorder(line, 8),
                new EmptyBorder(10, 16, 10, 16)
        );
    }

    private static final class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        private GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillOval(getWidth() - 190, -80, 260, 260);
            g2.setColor(new Color(246, 181, 55, 45));
            g2.fillOval(-80, getHeight() - 110, 210, 210);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static final class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        private RoundedLineBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Double(x + 0.5, y + 0.5, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(1, 1, 1, 1);
            return insets;
        }
    }
}
