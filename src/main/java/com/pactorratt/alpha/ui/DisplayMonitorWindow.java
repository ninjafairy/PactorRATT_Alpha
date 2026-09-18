package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.hostmode.DigitalLedState;
import com.pactorratt.alpha.hostmode.DigitalLedState.Lamp;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * Debug view of PK-232 digital front-panel lamps. The faceplate photo is the
 * display area; overlay circles sit on the physical LED lenses. Analog DCD and
 * the tuning bargraph are not controlled.
 */
public final class DisplayMonitorWindow extends JFrame {

    private static final String FACEPLATE_RESOURCE = "/pk232-faceplate.jpg";

    private final AppController app;
    private final JButton readButton = new JButton("Read");
    private final JSlider autoSlider = new JSlider(0, 4, 0);
    private final JLabel autoLabel = new JLabel("Auto: 0 Hz");
    /** Offline lamp test: 4 overlays per second. */
    private static final int DEMO_MS = 250;

    private final Timer autoTimer;
    private final Timer demoTimer;
    private final FaceplatePanel faceplate = new FaceplatePanel();
    private final Lamp[] demoLamps = DigitalLedState.overlayWalkOrder();
    private boolean peekInFlight;
    private int demoIndex;
    private boolean demoTurningOff;

    public DisplayMonitorWindow(AppController app) {
        super("TNC Display");
        this.app = app;
        autoTimer = new Timer(1000, e -> requestPeek(false));
        autoTimer.setRepeats(true);
        demoTimer = new Timer(DEMO_MS, e -> demoTick());
        demoTimer.setRepeats(true);
        buildUi();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                stopLampDemo();
                autoTimer.stop();
                app.onDisplayMonitorClosed(DisplayMonitorWindow.this);
            }
        });
        pack();
        setMinimumSize(new Dimension(720, 260));
        setLocationByPlatform(true);
        if (!app.isTncConnected()) {
            startLampDemo();
        }
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(6, 6));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        north.setBackground(UiColors.PANEL_BG);
        north.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        readButton.addActionListener(e -> requestPeek(true));
        north.add(readButton);

        autoSlider.setMajorTickSpacing(1);
        autoSlider.setPaintTicks(true);
        autoSlider.setPaintLabels(true);
        autoSlider.setSnapToTicks(true);
        autoSlider.setBackground(UiColors.PANEL_BG);
        autoSlider.setPreferredSize(new Dimension(140, autoSlider.getPreferredSize().height));
        autoSlider.addChangeListener(e -> onAutoSliderChanged());
        north.add(autoSlider);
        autoLabel.setFont(autoLabel.getFont().deriveFont(Font.PLAIN));
        north.add(autoLabel);

        add(north, BorderLayout.NORTH);
        add(faceplate, BorderLayout.CENTER);
    }

    private void onAutoSliderChanged() {
        int hz = autoSlider.getValue();
        autoLabel.setText("Auto: " + hz + " Hz");
        if (autoSlider.getValueIsAdjusting()) {
            return;
        }
        autoTimer.stop();
        if (hz <= 0) {
            return;
        }
        autoTimer.setDelay(Math.max(1, 1000 / hz));
        autoTimer.start();
        requestPeek(false);
    }

    private void startLampDemo() {
        stopLampDemo();
        demoIndex = 0;
        demoTurningOff = false;
        faceplate.clearLamps();
        demoTimer.setInitialDelay(0);
        demoTimer.start();
    }

    private void stopLampDemo() {
        demoTimer.stop();
    }

    private void demoTick() {
        if (!demoTurningOff) {
            if (demoIndex < demoLamps.length) {
                faceplate.setLit(demoLamps[demoIndex], true);
                demoIndex++;
                return;
            }
            demoTurningOff = true;
            demoIndex = 0;
        }
        if (demoIndex < demoLamps.length) {
            faceplate.setLit(demoLamps[demoIndex], false);
            demoIndex++;
        }
        if (demoIndex >= demoLamps.length) {
            stopLampDemo();
        }
    }

    private void requestPeek(boolean manual) {
        stopLampDemo();
        if (peekInFlight) {
            return;
        }
        peekInFlight = true;
        readButton.setEnabled(false);
        app.peekDigitalLeds(result -> {
            peekInFlight = false;
            readButton.setEnabled(true);
            if (result.error != null) {
                if (manual) {
                    JOptionPane.showMessageDialog(this, result.error, "TNC Display",
                            JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            if (result.state != null) {
                faceplate.applyState(result.state);
            }
        });
    }

    static final class FaceplatePanel extends JPanel {
        private static final Color OFF = new Color(0x4A, 0x12, 0x10);
        private static final Color LIT_HOT = new Color(0xFF, 0xFB, 0xF0);
        private static final Color LIT_MID = new Color(0xFF, 0x3A, 0x28);
        private static final Color LIT_EDGE = new Color(0xB0, 0x00, 0x00);

        private final BufferedImage image;
        private final Map<Lamp, Boolean> lit = new EnumMap<>(Lamp.class);

        FaceplatePanel() {
            setBackground(Color.BLACK);
            setOpaque(true);
            BufferedImage loaded;
            try (InputStream in = DisplayMonitorWindow.class.getResourceAsStream(FACEPLATE_RESOURCE)) {
                loaded = in == null ? null : ImageIO.read(in);
            } catch (IOException e) {
                loaded = null;
            }
            image = loaded;
            if (image != null) {
                int w = Math.min(1024, image.getWidth());
                int h = Math.max(1, Math.round(w * (image.getHeight() / (float) image.getWidth())));
                setPreferredSize(new Dimension(w, h));
            } else {
                setPreferredSize(new Dimension(1024, 222));
            }
            for (Lamp lamp : Lamp.values()) {
                if (lamp.hasOverlay()) {
                    lit.put(lamp, Boolean.FALSE);
                }
            }
        }

        void clearLamps() {
            boolean changed = false;
            for (Lamp lamp : lit.keySet()) {
                if (Boolean.TRUE.equals(lit.put(lamp, Boolean.FALSE))) {
                    changed = true;
                }
            }
            if (changed) {
                repaint();
            }
        }

        void setLit(Lamp lamp, boolean on) {
            if (!lamp.hasOverlay()) {
                return;
            }
            Boolean prev = lit.put(lamp, on);
            if (prev == null || prev != on) {
                repaint();
            }
        }

        void applyState(DigitalLedState state) {
            boolean changed = false;
            for (Lamp lamp : lit.keySet()) {
                boolean on = state.isLit(lamp);
                if (lit.put(lamp, on) != on) {
                    changed = true;
                }
            }
            if (changed) {
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int pw = getWidth();
            int ph = getHeight();
            if (image == null) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Faceplate image missing", 16, 24);
                g2.dispose();
                return;
            }

            int iw = image.getWidth();
            int ih = image.getHeight();
            float scale = Math.min(pw / (float) iw, ph / (float) ih);
            int dw = Math.round(iw * scale);
            int dh = Math.round(ih * scale);
            int ox = (pw - dw) / 2;
            int oy = (ph - dh) / 2;
            g2.drawImage(image, ox, oy, dw, dh, this);

            float diameter = Math.max(8f, dw * 0.0185f);
            for (Map.Entry<Lamp, Boolean> e : lit.entrySet()) {
                Lamp lamp = e.getKey();
                float cx = ox + lamp.nx * dw;
                float cy = oy + lamp.ny * dh;
                Ellipse2D.Float oval = new Ellipse2D.Float(
                        cx - diameter / 2f, cy - diameter / 2f, diameter, diameter);
                if (Boolean.TRUE.equals(e.getValue())) {
                    float radius = Math.max(1f, diameter * 0.55f);
                    g2.setPaint(new RadialGradientPaint(
                            cx - diameter * 0.18f,
                            cy - diameter * 0.22f,
                            radius,
                            new float[] {0f, 0.32f, 1f},
                            new Color[] {LIT_HOT, LIT_MID, LIT_EDGE},
                            MultipleGradientPaint.CycleMethod.NO_CYCLE));
                } else {
                    g2.setPaint(OFF);
                }
                g2.fill(oval);
            }
            g2.dispose();
        }
    }
}
