package com.pactorratt.alpha.hostmode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Decodes PK-232 front-panel digital lamps from 8536 ports B ({@code $BF0E}) and C ({@code $BF0F}).
 * Analog DCD and the 10-segment bargraph are not represented.
 *
 * <p>Mode and status groups are 7445 decoder inputs: at most one lamp in each group is selected.
 * Direct bits (SEND / STA / CON / MULT) are 7406-driven; bit 1 is treated as lit.
 */
public final class DigitalLedState {

    public enum Lamp {
        PKT("PKT", Group.MODE, 0.7208f, 0.6315f),
        BAUDOT("BAUDOT", Group.MODE, 0.7867f, 0.3738f),
        ASCII("ASCII", Group.MODE, 0.7441f, 0.3725f),
        MORSE("MORSE", Group.MODE, 0.7637f, 0.6320f),
        CHECK("SELFEC", Group.MODE, 0.6772f, 0.6293f),
        FEC("FEC", Group.MODE, 0.7004f, 0.3739f),
        ARQ("ARQ", Group.MODE, 0.6339f, 0.6275f),
        MODE_L("MODEL", Group.MODE, 0.6572f, 0.3725f),
        MODE_STBY("STBY", Group.MODE, 0.6139f, 0.3676f),

        STATUS_STBY("STBY", Group.STATUS, 0.5709f, 0.3671f),
        PHASE("PHASE", Group.STATUS, 0.5279f, 0.3683f),
        IDLE("IDLE", Group.STATUS, 0.4845f, 0.3654f),
        OVER("OVER", Group.STATUS, 0.5481f, 0.6248f),
        ERROR_CONV("CONV", Group.STATUS, 0.4422f, 0.3631f),
        TFC_TRANS("TRANS", Group.STATUS, 0.5049f, 0.6248f),
        RQ_CMD("CMD", Group.STATUS, 0.4625f, 0.6194f),
        CON("CON", Group.STATUS, 0.4189f, 0.6203f),
        STA("STA", Group.STATUS, 0.3762f, 0.6162f),
        MULT("MULT", Group.STATUS, 0.3555f, 0.3577f),
        SEND("SEND", Group.STATUS, 0.3985f, 0.3626f);

        public final String label;
        public final Group group;
        /** Normalized faceplate photo coordinates of the physical lamp. */
        public final float nx;
        public final float ny;

        Lamp(String label, Group group, float nx, float ny) {
            this.label = label;
            this.group = group;
            this.nx = nx;
            this.ny = ny;
        }

        public boolean hasOverlay() {
            return !Float.isNaN(nx) && !Float.isNaN(ny);
        }
    }

    public enum Group {
        MODE,
        STATUS
    }

    /** Front-panel order for the Mode column. */
    public static final Lamp[] MODE_COLUMN = {
            Lamp.PKT, Lamp.BAUDOT, Lamp.ASCII, Lamp.MORSE, Lamp.CHECK,
            Lamp.FEC, Lamp.ARQ, Lamp.MODE_L, Lamp.MODE_STBY
    };

    /** Front-panel order for the Status column (direct bits after the decoder group). */
    public static final Lamp[] STATUS_COLUMN = {
            Lamp.STATUS_STBY, Lamp.PHASE, Lamp.IDLE, Lamp.OVER,
            Lamp.ERROR_CONV, Lamp.TFC_TRANS, Lamp.RQ_CMD,
            Lamp.CON, Lamp.STA, Lamp.MULT, Lamp.SEND
    };

    /**
     * Overlay lamps in faceplate order: top row left-to-right, then bottom row.
     * Used by the offline lamp-test walk.
     */
    public static Lamp[] overlayWalkOrder() {
        List<Lamp> lamps = new ArrayList<>();
        for (Lamp lamp : Lamp.values()) {
            if (lamp.hasOverlay()) {
                lamps.add(lamp);
            }
        }
        lamps.sort(Comparator.comparingDouble((Lamp l) -> (double) l.ny).thenComparingDouble(l -> (double) l.nx));
        return lamps.toArray(Lamp[]::new);
    }

    private static final Lamp[] MODE_BY_CODE = {
            Lamp.PKT,       // 0000
            Lamp.MORSE,     // 0001
            Lamp.ASCII,     // 0010
            Lamp.BAUDOT,    // 0011
            Lamp.CHECK,     // 0100
            Lamp.FEC,       // 0101
            Lamp.MODE_L,    // 0110
            Lamp.ARQ,       // 0111
            Lamp.MODE_STBY  // 1000
            // 1001 FAX and 1010 SIAM are not listed as physical lamps in this window
    };

    private static final Lamp[] STATUS_BY_CODE = {
            Lamp.STATUS_STBY, // 000
            Lamp.PHASE,       // 001
            Lamp.OVER,        // 010
            Lamp.IDLE,        // 011
            Lamp.TFC_TRANS,   // 100
            Lamp.ERROR_CONV,  // 101
            Lamp.RQ_CMD       // 110
            // 111 unused
    };

    private final int portB;
    private final int portC;
    private final Set<Lamp> lit;

    private DigitalLedState(int portB, int portC, Set<Lamp> lit) {
        this.portB = portB;
        this.portC = portC;
        this.lit = lit;
    }

    public static DigitalLedState fromPorts(int portB, int portC) {
        int b = portB & 0xFF;
        int c = portC & 0xFF;
        EnumSet<Lamp> on = EnumSet.noneOf(Lamp.class);

        int modeCode = (b >> 4) & 0x0F;
        if (modeCode < MODE_BY_CODE.length) {
            on.add(MODE_BY_CODE[modeCode]);
        }

        int statusCode = b & 0x07;
        if (statusCode < STATUS_BY_CODE.length) {
            on.add(STATUS_BY_CODE[statusCode]);
        }

        if (((b >> 3) & 1) != 0) {
            on.add(Lamp.SEND);
        }
        if ((c & 1) != 0) {
            on.add(Lamp.STA);
        }
        if (((c >> 1) & 1) != 0) {
            on.add(Lamp.CON);
        }
        if (((c >> 2) & 1) != 0) {
            on.add(Lamp.MULT);
        }

        return new DigitalLedState(b, c, Collections.unmodifiableSet(on));
    }

    public boolean isLit(Lamp lamp) {
        return lit.contains(lamp);
    }

    public int portB() {
        return portB;
    }

    public int portC() {
        return portC;
    }
}
