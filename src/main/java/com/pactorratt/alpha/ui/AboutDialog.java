package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.config.AppConfig;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public final class AboutDialog {

    private static final String WEBSITE = "https://pactorratt.weebly.com/";
    private static final String DISCORD = "http://discord.gg/TkTw2ZBWcc";
    private static final String ISSUES = "https://github.com/ninjafairy/PactorRATT_Alpha/issues";
    private static final Font TEXT_FONT = aboutFont();

    private AboutDialog() {
    }

    public static void show(Component parent) {
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);
        text.add(line("PactorRATT_Alpha"));
        text.add(line("Portable PK-232 Host Mode Pactor chat (Alpha)"));
        text.add(Box.createVerticalStrut(10));
        text.add(line("License: AGPL-3.0"));
        text.add(line("support contact: " + AppConfig.SUPPORT_EMAIL));
        text.add(Box.createVerticalStrut(10));
        text.add(linkLine("Website: ", WEBSITE));
        text.add(line(" "));
        text.add(linkLine("Discord: ", DISCORD));
        text.add(Box.createVerticalStrut(10));
        text.add(linkLine("Github: ", ISSUES));

        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setOpaque(false);
        content.add(text, BorderLayout.CENTER);
        JLabel icon = iconLabel();
        if (icon != null) {
            content.add(icon, BorderLayout.EAST);
        }

        JOptionPane.showMessageDialog(parent, content, "About PactorRATT_Alpha",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static Font aboutFont() {
        Font base = UIManager.getFont("Label.font");
        if (base == null) {
            base = new JLabel().getFont();
        }
        return base.deriveFont(base.getSize2D() + 4f);
    }

    private static JLabel iconLabel() {
        try (InputStream in = AboutDialog.class.getResourceAsStream("/icona_120x120.jpg")) {
            if (in == null) {
                return null;
            }
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                return null;
            }
            JLabel icon = new JLabel(new ImageIcon(image));
            Dimension size = new Dimension(image.getWidth(), image.getHeight());
            icon.setPreferredSize(size);
            icon.setMinimumSize(size);
            icon.setVerticalAlignment(SwingConstants.TOP);
            icon.setHorizontalAlignment(SwingConstants.RIGHT);
            return icon;
        } catch (IOException e) {
            return null;
        }
    }

    private static JPanel line(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TEXT_FONT);
        return row(label);
    }

    private static JPanel linkLine(String caption, String url) {
        JLabel prefix = new JLabel(caption);
        prefix.setFont(TEXT_FONT);
        int points = Math.round(TEXT_FONT.getSize2D());
        JLabel link = new JLabel("<html><span style=\"font-size:" + points
                + "pt\"><a href=\"" + url + "\">" + url + "</a></span></html>");
        link.setFont(TEXT_FONT);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.setToolTipText(url);
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                open(url);
            }
        });
        return row(prefix, link);
    }

    private static JPanel row(Component... parts) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Component part : parts) {
            row.add(part);
        }
        Dimension pref = row.getPreferredSize();
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        return row;
    }

    private static void open(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(url));
            }
        } catch (IOException | UnsupportedOperationException ignored) {
        }
    }
}
