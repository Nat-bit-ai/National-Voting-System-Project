package ui.shared;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;

public final class AppTheme {
    public static final Color PALETTE_BLACK = new Color(0x000000);
    public static final Color PALETTE_MAGENTA = new Color(0xCB2957);
    public static final Color PALETTE_GRAY = new Color(0xDDDDDD);
    public static final Color PALETTE_OFF_WHITE = new Color(0xEEEEEE);

    private static final Color LIGHT_CANVAS = PALETTE_OFF_WHITE;
    private static final Color LIGHT_SURFACE = Color.WHITE;
    private static final Color LIGHT_SURFACE_ALT = new Color(0xF7F7F7);
    private static final Color LIGHT_TEXT = new Color(0x151515);
    private static final Color LIGHT_MUTED = new Color(0x666666);
    private static final Color DARK_CANVAS = PALETTE_BLACK;
    private static final Color DARK_SURFACE = new Color(0x181818);
    private static final Color DARK_SURFACE_ALT = new Color(0x242424);
    private static final Color DARK_TEXT = PALETTE_OFF_WHITE;
    private static final Color DARK_MUTED = new Color(0xBBBBBB);

    public static Color SUCCESS = new Color(0x198754);
    public static Color ERROR = new Color(0xB3261E);
    public static Color WARNING = new Color(0xB8860B);
    public static Color INFO = PALETTE_MAGENTA;

    public static Color GREEN;
    public static Color GREEN_DARK;
    public static Color GOLD;
    public static Color GOLD_SOFT;
    public static Color RED;
    public static Color RED_SOFT;
    public static Color INK;
    public static Color MUTED;
    public static Color CANVAS;
    public static Color SURFACE;
    public static Color SURFACE_ALT;
    public static Color BORDER;
    public static Color BORDER_STRONG;
    public static Color SELECTED;

    private static boolean darkMode;

    public static final String FONT_FAMILY = "Bahnschrift";
    public static final String PREF_NODE = "/et/gov/voting/ui";
    public static final String HOME_LOGO_PATH_KEY = "homeElectionLogoPath";
    private static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    private static final Font BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
    private static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    private static final Font HERO_FONT = new Font(FONT_FAMILY, Font.BOLD, 32);
    private static final Font SECTION_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    private static final int THEME_ICON_SIZE = 22;

    static {
        updatePalette();
    }

    private AppTheme() {
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean enabled) {
        darkMode = enabled;
        updatePalette();
        install();
    }

    public static Color getCanvasBackground() {
        return CANVAS;
    }

    public static Color getSurfaceBackground() {
        return SURFACE;
    }

    public static Color getSurfaceAlt() {
        return SURFACE_ALT;
    }

    public static Color getTextColor() {
        return INK;
    }

    public static Color getMutedTextColor() {
        return MUTED;
    }

    public static Color getPrimaryAccent() {
        return PALETTE_MAGENTA;
    }

    public static Color getPrimaryAccentLight() {
        return new Color(0xE06B8B);
    }

    public static Color getSecondaryAccent() {
        return PALETTE_GRAY;
    }

    public static Color getBorderColor() {
        return BORDER;
    }

    public static Color getBorderStrongColor() {
        return BORDER_STRONG;
    }

    private static void updatePalette() {
        CANVAS = darkMode ? DARK_CANVAS : LIGHT_CANVAS;
        SURFACE = darkMode ? DARK_SURFACE : LIGHT_SURFACE;
        SURFACE_ALT = darkMode ? DARK_SURFACE_ALT : LIGHT_SURFACE_ALT;
        INK = darkMode ? DARK_TEXT : LIGHT_TEXT;
        MUTED = darkMode ? DARK_MUTED : LIGHT_MUTED;
        BORDER = darkMode ? new Color(0x3A3A3A) : PALETTE_GRAY;
        BORDER_STRONG = darkMode ? new Color(0x666666) : new Color(0xBEBEBE);
        SELECTED = PALETTE_MAGENTA;
        GREEN = SUCCESS;
        GREEN_DARK = PALETTE_MAGENTA;
        GOLD = PALETTE_MAGENTA;
        GOLD_SOFT = darkMode ? new Color(0x2D2025) : new Color(0xF8DDE6);
        RED = ERROR;
        RED_SOFT = darkMode ? new Color(0x3B1717) : new Color(0xF8D7DA);
    }

    public static void install() {
        UIManager.put("Panel.background", CANVAS);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", INK);
        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("Button.font", BODY_BOLD);
        UIManager.put("Button.background", SURFACE);
        UIManager.put("Button.foreground", INK);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("Label.foreground", INK);
        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("PasswordField.font", BODY_FONT);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextField.foreground", INK);
        UIManager.put("PasswordField.background", SURFACE);
        UIManager.put("PasswordField.foreground", INK);
        UIManager.put("TextField.selectionBackground", PALETTE_MAGENTA);
        UIManager.put("PasswordField.selectionBackground", PALETTE_MAGENTA);
        UIManager.put("ComboBox.font", BODY_FONT);
        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("ComboBox.foreground", INK);
        UIManager.put("CheckBox.font", BODY_FONT);
        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("CheckBox.foreground", INK);
        UIManager.put("Table.font", BODY_FONT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", INK);
        UIManager.put("Table.alternateRowColor", SURFACE_ALT);
        UIManager.put("Table.selectionBackground", PALETTE_MAGENTA);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TableHeader.background", SURFACE_ALT);
        UIManager.put("TableHeader.foreground", INK);
        UIManager.put("TabbedPane.font", BODY_BOLD);
        UIManager.put("TabbedPane.background", CANVAS);
        UIManager.put("TabbedPane.foreground", INK);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
    }

    public static JButton primaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(PALETTE_MAGENTA);
        button.setForeground(Color.WHITE);
        button.setBorder(buttonBorder(PALETTE_MAGENTA));
        button.addMouseListener(new HoverAdapter(button, PALETTE_MAGENTA, new Color(0xA71E45), Color.WHITE));
        return button;
    }

    public static JButton outlineButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(SURFACE);
        button.setForeground(PALETTE_MAGENTA);
        button.setBorder(buttonBorder(PALETTE_MAGENTA));
        button.addMouseListener(new HoverAdapter(button, SURFACE, GOLD_SOFT, PALETTE_MAGENTA));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(SURFACE_ALT);
        button.setForeground(INK);
        button.setBorder(buttonBorder(BORDER_STRONG));
        button.addMouseListener(new HoverAdapter(button, SURFACE_ALT, darkMode ? new Color(0x303030) : PALETTE_GRAY, INK));
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(ERROR);
        button.setForeground(Color.WHITE);
        button.setBorder(buttonBorder(ERROR));
        button.addMouseListener(new HoverAdapter(button, ERROR, new Color(0x8E1D18), Color.WHITE));
        return button;
    }

    public static JButton themeToggleButton() {
        JButton button = themeIconButton();
        button.addActionListener(e -> {
            applyThemeIcon(button);
            button.setToolTipText(themeTooltip());
        });
        return button;
    }

    public static JButton themeIconButton() {
        JButton button = secondaryButton("");
        applyThemeIcon(button);
        button.setToolTipText(themeTooltip());
        button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        button.setMinimumSize(new Dimension(48, 42));
        button.setPreferredSize(new Dimension(52, 42));
        return button;
    }

    public static String themeIcon() {
        return darkMode ? "\u2600" : "\u25D0";
    }

    public static String themeTooltip() {
        return darkMode ? "Switch to light mode" : "Switch to dark mode";
    }

    private static void applyThemeIcon(JButton button) {
        Icon icon = themeIconImage();
        button.setIcon(icon);
        button.setText(icon == null ? themeIcon() : "");
    }

    private static Icon themeIconImage() {
        String resource = darkMode ? "/icons/sun.png" : "/icons/moon.png";
        URL url = AppTheme.class.getResource(resource);
        if (url == null) {
            return null;
        }

        try {
            BufferedImage source = ImageIO.read(url);
            if (source == null) {
                return null;
            }
            BufferedImage tinted = new BufferedImage(THEME_ICON_SIZE, THEME_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = tinted.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(source, 0, 0, THEME_ICON_SIZE, THEME_ICON_SIZE, null);
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.setColor(INK);
            g2.fillRect(0, 0, THEME_ICON_SIZE, THEME_ICON_SIZE);
            g2.dispose();
            return new ImageIcon(tinted);
        } catch (IOException ex) {
            return null;
        }
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(INK);
        return label;
    }

    public static JLabel heroTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HERO_FONT);
        label.setForeground(INK);
        return label;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION_FONT);
        label.setForeground(INK);
        return label;
    }

    public static JLabel bodyText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(MUTED);
        return label;
    }

    public static JLabel mutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        label.setForeground(MUTED);
        return label;
    }

    public static JLabel eyebrow(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 11));
        label.setForeground(PALETTE_MAGENTA);
        return label;
    }

    public static JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER, 8),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static JPanel heroPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(darkMode ? new Color(0x101010) : new Color(0xFAFAFA));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER, 8),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return panel;
    }

    public static void styleInput(JComponent input) {
        input.setFont(BODY_FONT);
        input.setForeground(INK);
        input.setBackground(SURFACE);

        if (input instanceof JCheckBox checkBox) {
            checkBox.setOpaque(false);
            checkBox.setForeground(INK);
            checkBox.setBorder(new EmptyBorder(8, 0, 8, 0));
            return;
        }

        input.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER_STRONG, 8),
                new EmptyBorder(8, 10, 8, 10)
        ));

        if (input instanceof JTextField textField) {
            if (textField.getColumns() == 0) {
                textField.setColumns(22);
            }
            textField.setCaretColor(PALETTE_MAGENTA);
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PALETTE_MAGENTA);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setBackground(SURFACE_ALT);
        table.getTableHeader().setForeground(INK);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
    }

    public static JScrollPane scrollPane(Component content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new RoundedLineBorder(BORDER, 8),
                new EmptyBorder(4, 4, 4, 4)
        ));
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private static JButton baseButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BODY_BOLD);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setMinimumSize(new Dimension(108, 42));
        button.setPreferredSize(new Dimension(132, 42));
        return button;
    }

    private static Border buttonBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                new RoundedLineBorder(color, 8),
                new EmptyBorder(9, 16, 9, 16)
        );
    }

    private static final class HoverAdapter extends java.awt.event.MouseAdapter {
        private final JButton button;
        private final Color normalBackground;
        private final Color hoverBackground;
        private final Color foreground;

        private HoverAdapter(JButton button, Color normalBackground, Color hoverBackground, Color foreground) {
            this.button = button;
            this.normalBackground = normalBackground;
            this.hoverBackground = hoverBackground;
            this.foreground = foreground;
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(hoverBackground);
            button.setForeground(foreground);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(normalBackground);
            button.setForeground(foreground);
        }
    }

    public static final class TerminalBorder extends RoundedLineBorder {
        public TerminalBorder(Color color, int radius) {
            super(color, radius);
        }
    }

    public static class RoundedLineBorder extends AbstractBorder {
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
            g2.setStroke(new BasicStroke(1.4f));
            g2.draw(new RoundRectangle2D.Double(x + 0.7, y + 0.7, width - 1.4, height - 1.4, radius, radius));
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
