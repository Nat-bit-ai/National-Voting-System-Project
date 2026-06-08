package ui.shared;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    protected BaseFrame(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setMinimumSize(new Dimension(Math.min(width, 900), Math.min(height, 620)));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected abstract JComponent buildContent();

    protected void initializeFrame() {
        setContentPane(buildContent());
    }

    protected void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Action failed", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
