import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StyledButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
    // Define colors for the button
    private static final Color BUTTON_COLOR1 = new Color(154, 191, 227); // Light blue
    private static final Color BUTTON_COLOR2 = new Color(51, 133, 198); // Dark blue
    private static final Color BUTTON_ROLLOVER_COLOR1 = new Color(112, 163, 204); // Light blue on rollover
    private static final Color BUTTON_ROLLOVER_COLOR2 = new Color(30, 101, 156); // Dark blue on rollover
    private static final Color BUTTON_PRESSED_COLOR1 = new Color(30, 101, 156); // Light blue on press
    private static final Color BUTTON_PRESSED_COLOR2 = new Color(0, 71, 125); // Dark blue on press

    private static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE; // Button text color
    private static final int BUTTON_ARC = 20; // Button corner arc

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        // Set button paint based on button state
        if (model.isRollover()) {
            if (model.isPressed()) {
                g2.setPaint(new GradientPaint(0, 0, BUTTON_PRESSED_COLOR1, 0, c.getHeight(), BUTTON_PRESSED_COLOR2));
            } else {
                g2.setPaint(new GradientPaint(0, 0, BUTTON_ROLLOVER_COLOR1, 0, c.getHeight(), BUTTON_ROLLOVER_COLOR2));
            }
        } else {
            g2.setPaint(new GradientPaint(0, 0, BUTTON_COLOR1, 0, c.getHeight(), BUTTON_COLOR2));
        }

        // Fill the button background with a rounded rectangle shape
        g2.fill(new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), BUTTON_ARC, BUTTON_ARC));

        // Call the super class to paint additional button elements
        super.paint(g, c);

        // Set the font metrics and position the text in the center of the button
        FontMetrics metrics = g.getFontMetrics(b.getFont());
        Rectangle stringBounds = metrics.getStringBounds(b.getText(), g).getBounds();
        int textX = c.getWidth() / 2 - stringBounds.width / 2;
        int textY = c.getHeight() / 2 - stringBounds.height / 2 + metrics.getAscent();

        // Set the font color and draw the button text
        g2.setColor(BUTTON_FOREGROUND_COLOR);
        g2.setFont(b.getFont());
        g2.drawString(b.getText(), textX, textY);
    }
}
