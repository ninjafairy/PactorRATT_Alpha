package com.pactorratt.alpha.ui;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;

/**
 * Saved window bounds ({@code x,y,width,height}) and the Settings reset stack.
 */
public final class WindowPlacement {

    public static final int MAIN_WIDTH = 640;
    public static final int MAIN_HEIGHT = 560;
    public static final int CONNECTION_WIDTH = 640;
    public static final int CONNECTION_HEIGHT = 520;
    private static final int CASCADE = 24;

    private WindowPlacement() {
    }

    /**
     * Apply a saved {@code x,y,width,height} string when it meets a current screen.
     * Otherwise use the default size and the platform location.
     */
    public static void apply(Window window, String saved, int defaultWidth, int defaultHeight) {
        Rectangle bounds = parse(saved);
        if (bounds != null && intersectsScreen(bounds)) {
            window.setBounds(bounds);
            return;
        }
        window.setSize(defaultWidth, defaultHeight);
        window.setLocationByPlatform(true);
    }

    public static String capture(Window window) {
        Rectangle bounds = window.getBounds();
        return bounds.x + "," + bounds.y + "," + bounds.width + "," + bounds.height;
    }

    /**
     * Put {@code main} back to the default size, centered on its current screen, then
     * place each open connection window on that top-left, stepped so title bars stay visible.
     * Main stays behind those windows and in front of other programs.
     */
    public static void restack(Window main, List<? extends Window> onTop) {
        centerDefault(main, MAIN_WIDTH, MAIN_HEIGHT);
        int x = main.getX();
        int y = main.getY();
        int step = CASCADE;
        main.toFront();
        if (onTop == null) {
            return;
        }
        for (Window window : onTop) {
            if (window == null || !window.isDisplayable()) {
                continue;
            }
            window.setBounds(x + step, y + step, CONNECTION_WIDTH, CONNECTION_HEIGHT);
            step += CASCADE;
            window.toFront();
        }
    }

    private static void centerDefault(Window window, int width, int height) {
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        if (gc == null) {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
        }
        Rectangle screen = gc.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        int usableW = screen.width - insets.left - insets.right;
        int usableH = screen.height - insets.top - insets.bottom;
        int x = screen.x + insets.left + Math.max(0, (usableW - width) / 2);
        int y = screen.y + insets.top + Math.max(0, (usableH - height) / 2);
        window.setBounds(x, y, width, height);
    }

    private static Rectangle parse(String saved) {
        if (saved == null || saved.isBlank()) {
            return null;
        }
        String[] parts = saved.split(",");
        if (parts.length != 4) {
            return null;
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            int w = Integer.parseInt(parts[2].trim());
            int h = Integer.parseInt(parts[3].trim());
            if (w <= 0 || h <= 0) {
                return null;
            }
            return new Rectangle(x, y, w, h);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean intersectsScreen(Rectangle bounds) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice device : ge.getScreenDevices()) {
            Rectangle screen = device.getDefaultConfiguration().getBounds();
            if (screen.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }
}
