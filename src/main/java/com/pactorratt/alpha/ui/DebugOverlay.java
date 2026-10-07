package com.pactorratt.alpha.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;

/**
 * Dev-tools overlay. Window dimensions draws a horizontal line labeled with the
 * frame width and a vertical line labeled with the frame height.
 */
public final class DebugOverlay {

    private static boolean windowDimensions;
    private static boolean watching;

    private DebugOverlay() {
    }

    public static boolean isWindowDimensions() {
        return windowDimensions;
    }

    /** Apply immediately to every open frame, and to frames opened later. */
    public static void setWindowDimensions(boolean on) {
        windowDimensions = on;
        ensureWatching();
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame frame) {
                apply(frame);
            }
        }
    }

    private static void ensureWatching() {
        if (watching) {
            return;
        }
        watching = true;
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event.getID() == WindowEvent.WINDOW_OPENED && event.getSource() instanceof JFrame frame) {
                SwingUtilities.invokeLater(() -> apply(frame));
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    private static void apply(JFrame frame) {
        if (!frame.isDisplayable()) {
            return;
        }
        JRootPane root = frame.getRootPane();
        if (root == null) {
            return;
        }
        if (root.getGlassPane() instanceof DimensionGlass overlay) {
            overlay.setVisible(windowDimensions);
            overlay.repaint();
            return;
        }
        if (!windowDimensions) {
            return;
        }
        DimensionGlass overlay = new DimensionGlass(frame);
        root.setGlassPane(overlay);
        overlay.setVisible(true);
    }

    /** Clicks pass through. The lines mark the outer frame size. */
    private static final class DimensionGlass extends JComponent {
        private final JFrame frame;

        DimensionGlass(JFrame frame) {
            this.frame = frame;
            setOpaque(false);
        }

        @Override
        public boolean contains(int x, int y) {
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            int width = frame.getWidth();
            int height = frame.getHeight();
            int midX = getWidth() / 2;
            int midY = getHeight() / 2;
            g.setColor(new Color(200, 0, 0));
            g.drawLine(0, midY, getWidth(), midY);
            g.drawLine(midX, 0, midX, getHeight());
            String widthLabel = Integer.toString(width);
            String heightLabel = Integer.toString(height);
            FontMetrics fm = g.getFontMetrics();
            int widthW = fm.stringWidth(widthLabel);
            int away = 72;
            int widthX = Math.max(4, midX - away - widthW);
            int widthY = Math.max(fm.getAscent() + 2, midY - 8);
            int heightX = Math.min(getWidth() - fm.stringWidth(heightLabel) - 4, midX + 10);
            int heightY = Math.min(getHeight() - 6, midY + away);
            paintLabel(g, widthLabel, widthX, widthY);
            paintLabel(g, heightLabel, heightX, heightY);
        }

        private static void paintLabel(Graphics g, String text, int x, int y) {
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(text);
            int top = y - fm.getAscent();
            g.setColor(new Color(255, 255, 220));
            g.fillRect(x - 2, top - 1, w + 4, fm.getHeight() + 2);
            g.setColor(Color.BLACK);
            g.drawString(text, x, y);
        }
    }
}
