package ui.shared;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public final class AppTheme {
    // Color Hunt Palette - https://colorhunt.co/palette/6096b493bfcfbdcdd6eee9da
    // Color 1: #6096B4 - Muted Blue
    public static final Color COLOR_1 = new Color(0x6096B4);
    // Color 2: #93BFCF - Light Blue  
    public static final Color COLOR_2 = new Color(0x93BFCF);
    // Color 3: #BDCDD6 - Pale Blue
    public static final Color COLOR_3 = new Color(0xBDCDD6);
    // Color 4: #EEE9DA - Cream/Beige
    public static final Color COLOR_4 = new Color(0xEEE9DA);

    // Command Prompt Theme
    public static final Color TERMINAL_BG = new Color(12, 12, 12);           // Dark terminal background
    public static final Color TERMINAL_TEXT = new Color(0, 255, 0);          // Bright green text
    public static final Color TERMINAL_ACCENT = new Color(0, 200, 0);        // Darker green
    public static final Color TERMINAL_BORDER = new Color(50, 50, 50);       // Dark gray border

    // Light Mode - Using palette colors
    public static final Color LIGHT_BG = TERMINAL_BG;                        // Dark terminal background
    public static final Color LIGHT_SURFACE = new Color(20, 20, 20);         // Slightly lighter than BG

    // Text Colors
    public static final Color TEXT_DARK = TERMINAL_TEXT;                     // Green text
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color TEXT_MUTED = new Color(0, 150, 0);             // Darker green

    // Accent Colors from Palette
    public static final Color PRIMARY = COLOR_1;                             // #6096B4 - Main accent
    public static final Color PRIMARY_LIGHT = COLOR_2;                       // #93BFCF - Light variant
    public static final Color SECONDARY = COLOR_3;                           // #BDCDD6 - Secondary
    
    // Status Colors
    public static final Color SUCCESS = new Color(0, 255, 0);                // Bright green
    public static final Color ERROR = new Color(255, 100, 100);              // Red
    public static final Color WARNING = new Color(255, 200, 0);              // Yellow
    public static final Color INFO = TERMINAL_TEXT;                          // Green

    // Legacy color names for backward compatibility
    public static final Color GREEN = TERMINAL_TEXT;
    public static final Color GREEN_DARK = TERMINAL_ACCENT;
    public static final Color GOLD = TERMINAL_TEXT;
    public static final Color GOLD_SOFT = TERMINAL_BORDER;
    public static final Color RED = ERROR;
    public static final Color RED_SOFT = new Color(100, 50, 50);
    public static final Color INK = TERMINAL_TEXT;
    public static final Color MUTED = TEXT_MUTED;
    public static final Color CANVAS = TERMINAL_BG;
    public static final Color SURFACE = LIGHT_SURFACE;
    public static final Color SURFACE_ALT = new Color(30, 30, 30);
    public static final Color BORDER = TERMINAL_BORDER;
    public static final Color BORDER_STRONG = new Color(80, 80, 80);
    public static final Color SELECTED = TERMINAL_TEXT;

    private static final Font TERMINAL_FONT = new Font("Courier New", Font.BOLD, 13);
    private static final Font TERMINAL_BOLD = new Font("Courier New", Font.BOLD, 14);
    private static final Font TERMINAL_LARGE = new Font("Courier New", Font.BOLD, 24);

    private AppTheme() {
    }

    public static Color getCanvasBackground() {
        return TERMINAL_BG;
    }

    public static Color getSurfaceBackground() {
        return LIGHT_SURFACE;
    }

    public static Color getSurfaceAlt() {
        return new Color(30, 30, 30);
    }

    public static Color getTextColor() {
        return TERMINAL_TEXT;
    }

    public static Color getMutedTextColor() {
        return TEXT_MUTED;
    }

    public static Color getPrimaryAccent() {
        return PRIMARY;
    }

    public static Color getPrimaryAccentLight() {
        return PRIMARY_LIGHT;
    }

    public static Color getSecondaryAccent() {
        return SECONDARY;
    }

    public static Color getBorderColor() {
        return TERMINAL_BORDER;
    }

    public static Color getBorderStrongColor() {
        return new Color(80, 80, 80);
    }

    public static void install() {
        UIManager.put("Panel.background", getCanvasBackground());
        UIManager.put("OptionPane.background", getCanvasBackground());
        UIManager.put("OptionPane.messageFont", TERMINAL_FONT);
        UIManager.put("Button.font", TERMINAL_BOLD);
        UIManager.put("Button.background", getSurfaceBackground());
        UIManager.put("Button.foreground", getTextColor());
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Label.font", TERMINAL_FONT);
        UIManager.put("Label.foreground", getTextColor());
        UIManager.put("TextField.font", TERMINAL_FONT);
        UIManager.put("PasswordField.font", TERMINAL_FONT);
        UIManager.put("TextField.selectionBackground", PRIMARY);
        UIManager.put("PasswordField.selectionBackground", PRIMARY);
        UIManager.put("ComboBox.font", TERMINAL_FONT);
        UIManager.put("CheckBox.font", TERMINAL_FONT);
        UIManager.put("CheckBox.background", getSurfaceBackground());
        UIManager.put("CheckBox.foreground", getTextColor());
        UIManager.put("Table.font", TERMINAL_FONT);
        UIManager.put("Table.background", getSurfaceBackground());
        UIManager.put("Table.alternateRowColor", getSurfaceAlt());
        UIManager.put("Table.selectionBackground", PRIMARY);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TableHeader.background", getSurfaceAlt());
        UIManager.put("TableHeader.foreground", getMutedTextColor());
        UIManager.put("TabbedPane.font", TERMINAL_BOLD);
        UIManager.put("TabbedPane.background", getCanvasBackground());
        UIManager.put("TabbedPane.selected", getSurfaceBackground());
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());

        UIManager.put("TextField.background", getSurfaceBackground());
        UIManager.put("TextField.foreground", getTextColor());

        UIManager.put("PasswordField.background", getSurfaceBackground());
        UIManager.put("PasswordField.foreground", getTextColor());

        UIManager.put("ComboBox.background", getSurfaceBackground());
        UIManager.put("ComboBox.foreground", getTextColor());

        UIManager.put("Table.foreground", getTextColor());
        UIManager.put("TableHeader.background", getSurfaceBackground());
        UIManager.put("TableHeader.foreground", getTextColor());
        UIManager.put("TabbedPane.foreground", getTextColor());
        UIManager.put("OptionPane.foreground", getTextColor());
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton("▶ " + text);
        button.setFont(TERMINAL_BOLD);
        button.setBackground(TERMINAL_ACCENT);
        button.setForeground(Color.BLACK);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_TEXT, 4),
                new EmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(TERMINAL_TEXT);
                button.setForeground(TERMINAL_BG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(TERMINAL_ACCENT);
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }

    public static JButton outlineButton(String text) {
        JButton button = new JButton("$ " + text);
        button.setFont(TERMINAL_BOLD);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setForeground(TERMINAL_TEXT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_TEXT, 4),
                new EmptyBorder(8, 15, 8, 15)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 100, 0, 100));
                button.setOpaque(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 0, 0, 0));
                button.setOpaque(false);
            }
        });

        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(TERMINAL_BOLD);
        button.setBackground(getSurfaceBackground());
        button.setForeground(getTextColor());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_BORDER, 4),
                new EmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(getSurfaceBackground());
            }
        });

        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = new JButton("✗ " + text);
        button.setFont(TERMINAL_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(ERROR);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(ERROR, 4),
                new EmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(255, 150, 150));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(ERROR);
            }
        });

        return button;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel("┌─► " + text);
        label.setFont(new Font("Courier New", Font.BOLD, 24));
        label.setForeground(getTextColor());
        return label;
    }

    public static JLabel heroTitle(String text) {
        JLabel label = new JLabel("╔═══ " + text + " ═══╗");
        label.setFont(new Font("Courier New", Font.BOLD, 32));
        label.setForeground(TERMINAL_TEXT);
        return label;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel("└─► " + text);
        label.setFont(new Font("Courier New", Font.BOLD, 18));
        label.setForeground(getTextColor());
        return label;
    }

    public static JLabel bodyText(String text) {
        JLabel label = new JLabel("│ " + text);
        label.setFont(TERMINAL_FONT);
        label.setForeground(getMutedTextColor());
        return label;
    }

    public static JLabel mutedLabel(String text) {
        JLabel label = new JLabel("// " + text);
        label.setFont(new Font("Courier New", Font.PLAIN, 11));
        label.setForeground(getMutedTextColor());
        return label;
    }

    public static JLabel eyebrow(String text) {
        JLabel label = new JLabel("[" + text.toUpperCase() + "]");
        label.setFont(new Font("Courier New", Font.BOLD, 11));
        label.setForeground(TERMINAL_TEXT);
        return label;
    }

    public static JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getSurfaceBackground());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_BORDER, 8),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static JPanel heroPanel() {
        return new TerminalPanel();
    }

    public static void styleInput(JComponent input) {
        input.setFont(TERMINAL_FONT);
        input.setForeground(getTextColor());
        input.setBackground(getSurfaceBackground());

        if (input instanceof JCheckBox checkBox) {
            checkBox.setOpaque(false);
            checkBox.setForeground(getTextColor());
            checkBox.setBorder(new EmptyBorder(8, 0, 8, 0));
            return;
        }

        input.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_TEXT, 6),
                new EmptyBorder(8, 10, 8, 10)
        ));

        if (input instanceof JTextField textField) {
            if (textField.getColumns() == 0) {
                textField.setColumns(22);
            }
            textField.setCaretColor(TERMINAL_TEXT);
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setGridColor(TERMINAL_BORDER);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBackground(getSurfaceAlt());
        table.getTableHeader().setForeground(getTextColor());
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TERMINAL_TEXT));
    }

    public static JScrollPane scrollPane(Component content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new TerminalBorder(TERMINAL_BORDER, 8),
                new EmptyBorder(8, 8, 8, 8)
        ));
        scrollPane.getViewport().setBackground(getSurfaceBackground());
        
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = TERMINAL_TEXT;
                this.trackColor = TERMINAL_BG;
            }
        });
        
        return scrollPane;
    }

    private static final class TerminalPanel extends JPanel {
        private TerminalPanel() {
            setOpaque(true);
            setBackground(TERMINAL_BG);
            setBorder(new TerminalBorder(TERMINAL_TEXT, 8));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.dispose();
        }
    }

    public static final class TerminalBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public TerminalBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(new RoundRectangle2D.Double(x + 1, y + 1, width - 2, height - 2, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(3, 3, 3, 3);
        }

        @Override
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(3, 3, 3, 3);
            return insets;
        }
    }

    public static final class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedLineBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(x + 0.75, y + 0.75, width - 1.5, height - 1.5, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 2);
            return insets;
        }
    }
}
