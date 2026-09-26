package com.pactorratt.alpha.hostmode;

/**
 * Host {@code OP} (OPMODE) detect + decode per {@code docs/OPmodeResponse.md}.
 * Pactor uses its own tags ({@code PN}, {@code Pt}, {@code PG}, {@code PD}); AMTOR {@code AM}/{@code AC}/{@code AL}/{@code FE}
 * are not Pactor.
 */
public final class OpmodeParser {

    private OpmodeParser() {
    }

    /**
     * Host command/response whose payload starts with {@code OP} on CTL {@code $4F}.
     * Covers both the poll ({@code OP}) and mode-tagged replies ({@code OPPA}, {@code OPPN}…).
     */
    public static boolean isOpmodeFrame(HostFrameCodec.Frame frame) {
        if (frame == null || (frame.ctl & 0xFF) != HostFrameCodec.CTL_GLOBAL) {
            return false;
        }
        byte[] p = frame.payload;
        return p != null && p.length >= 2 && p[0] == 'O' && p[1] == 'P';
    }

    /**
     * True when {@code data} is exactly one complete Host block that {@link #isOpmodeFrame} accepts.
     * Used to hide/show whole serial writes (typical {@code sendCommand("OP")} TX).
     */
    public static boolean isCompleteOpmodeBlock(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        HostFrameCodec.FrameParser parser = new HostFrameCodec.FrameParser();
        HostFrameCodec.Frame found = null;
        int frames = 0;
        for (byte b : data) {
            HostFrameCodec.Frame frame = parser.feed(b);
            if (frame != null) {
                frames++;
                found = frame;
            }
        }
        return frames == 1 && parser.awaitingSoh() && isOpmodeFrame(found);
    }

    /**
     * Decode a §4.3.2 / hardware-capture reply. Returns {@code null} for the two-byte poll
     * payload {@code OP} or any non-OPMODE frame.
     */
    public static Decoded decode(HostFrameCodec.Frame frame) {
        if (!isOpmodeFrame(frame) || frame.payload.length < 4) {
            return null;
        }
        byte[] p = frame.payload;
        int tag0 = p[2] & 0xFF;
        int tag1 = p[3] & 0xFF;
        String tag = new String(new byte[]{(byte) tag0, (byte) tag1}, java.nio.charset.StandardCharsets.US_ASCII);

        return switch (tag) {
            case "PA" -> new Decoded("Packet", null, null, null, false, null);
            case "MO" -> decodeMorse(p);
            case "BA" -> decodeSimpleXr("Baudot", p, 4);
            case "AS" -> decodeSimpleXr("ASCII", p, 4);
            case "AM" -> decodeAmtorStandby(p);
            case "AC" -> decodeWx("ARQ", p);
            case "AL" -> decodeArqListen(p);
            case "FE" -> decodeWx("FEC", p);
            case "SE" -> decodeWx("SELFEC", p);
            case "FA" -> decodeFax(p);
            case "PN" -> decodePactorListen(p);
            case "Pt" -> decodePactorStandby(p);
            case "PG" -> decodePactorArq(p);
            case "PD" -> decodePactorFec(p);
            default -> new Decoded("Unknown (" + tag + ")", null, null, null, false, null);
        };
    }

    private static Decoded decodeMorse(byte[] p) {
        Boolean tx = p.length > 4 ? xr(p[4]) : null;
        Integer wpm = null;
        if (p.length >= 7) {
            wpm = morseWpm(p[5], p[6]);
        }
        return new Decoded("Morse", null, tx, wpm, false, null);
    }

    private static Decoded decodeSimpleXr(String mode, byte[] p, int xIndex) {
        Boolean tx = p.length > xIndex ? xr(p[xIndex]) : null;
        return new Decoded(mode, null, tx, null, false, null);
    }

    private static Decoded decodeAmtorStandby(byte[] p) {
        String w = p.length > 4 ? wLabel(p[4] & 0xFF) : "Standby";
        Boolean tx = p.length > 5 ? xr(p[5]) : null;
        return new Decoded("AMTOR Standby", w, tx, null, true, null);
    }

    /** ARQ / FEC / SELFEC: *w* then *x*. */
    private static Decoded decodeWx(String mode, byte[] p) {
        String w = p.length > 4 ? wLabel(p[4] & 0xFF) : null;
        Boolean tx = p.length > 5 ? xr(p[5]) : null;
        boolean standby = w != null && w.equals("Standby");
        return new Decoded(mode, w, tx, null, standby, null);
    }

    private static Decoded decodeArqListen(byte[] p) {
        String w = p.length > 4 ? wLabel(p[4] & 0xFF) : null;
        Boolean tx = p.length > 5 ? xr(p[5]) : Boolean.FALSE;
        boolean standby = w != null && w.equals("Standby");
        return new Decoded("ARQ Listen", w, tx, null, standby, null);
    }

    /** FAX: *v* (undocumented) then *x*. Do not decode *v* with the *w* table. */
    private static Decoded decodeFax(byte[] p) {
        Boolean tx = p.length > 5 ? xr(p[5]) : null;
        return new Decoded("FAX", null, tx, null, false, null);
    }

    /** Hardware: {@code OP PN w x} *u* {@code ?} *s* {@code ?}. */
    private static Decoded decodePactorListen(byte[] p) {
        return decodePactorWxTrailer("Pactor Listen", p);
    }

    /**
     * Hardware: {@code OP PG w x} *u* {@code ?} *s* {@code ?}. Linked ARQ ({@code PTConn}); *w* is live link status.
     */
    private static Decoded decodePactorArq(byte[] p) {
        return decodePactorWxTrailer("Pactor ARQ", p);
    }

    /**
     * Hardware: {@code OP PD w x} *u* {@code ?} *s* {@code ?}. Unproto / PTSend (UI FEC).
     * Traffic ({@code $34}) while sending; Idle ({@code $33}) immediately before end TX.
     * Not AMTOR {@code FE}.
     */
    private static Decoded decodePactorFec(byte[] p) {
        return decodePactorWxTrailer("Pactor FEC", p);
    }

    /** Pactor listen / ARQ / FEC: *w* then *x* then four trailer bytes (u ? s ?). */
    private static Decoded decodePactorWxTrailer(String mode, byte[] p) {
        String w = p.length > 4 ? wLabel(p[4] & 0xFF) : null;
        Boolean tx = p.length > 5 ? xr(p[5]) : null;
        boolean standby = w != null && w.equals("Standby");
        PactorTrailer trailer = parsePactorTrailer(p);
        return new Decoded(mode, w, tx, null, standby, trailer.hex, trailer.baud, trailer.longpath);
    }

    /**
     * Hardware: {@code OP Pt $30 x} *u* {@code ?} *s* {@code ?}. {@code $30} is a fixed Pactor-standby marker,
     * not the *w* sequence.
     */
    private static Decoded decodePactorStandby(byte[] p) {
        Boolean tx = p.length > 5 ? xr(p[5]) : null;
        PactorTrailer trailer = parsePactorTrailer(p);
        return new Decoded("Pactor Standby", "Standby", tx, null, true,
                trailer.hex, trailer.baud, trailer.longpath);
    }

    /**
     * Last four payload bytes: *u* baud, unnamed, *s* longpath, unnamed.
     * {@code '1'}/{@code '2'} → 100/200 baud; {@code '0'}/{@code '1'} on *s* → normal/longpath.
     */
    private static PactorTrailer parsePactorTrailer(byte[] p) {
        String hex = mysteryBeforeEtb(p);
        Integer baud = null;
        Boolean longpath = null;
        if (p != null && p.length >= 4) {
            int u = p[p.length - 4] & 0xFF;
            int s = p[p.length - 2] & 0xFF;
            if (u == '1') {
                baud = 100;
            } else if (u == '2') {
                baud = 200;
            }
            if (s == '0') {
                longpath = Boolean.FALSE;
            } else if (s == '1') {
                longpath = Boolean.TRUE;
            }
        }
        return new PactorTrailer(hex, baud, longpath);
    }

    private static final class PactorTrailer {
        final String hex;
        final Integer baud;
        final Boolean longpath;

        PactorTrailer(String hex, Integer baud, Boolean longpath) {
            this.hex = hex;
            this.baud = baud;
            this.longpath = longpath;
        }
    }

    /** {@code x = S} transmit, {@code R} receive. Applies to every mode that includes *x*. */
    private static Boolean xr(byte b) {
        int c = b & 0xFF;
        if (c == 'S') {
            return Boolean.TRUE;
        }
        if (c == 'R') {
            return Boolean.FALSE;
        }
        return null;
    }

    /**
     * {@code w}: $30 Standby, $31 Phasing, $32 Change-over, $33 Idle,
     * $34 Traffic, $35 Error, $36 RQ, $37 Sync.
     */
    public static String wLabel(int w) {
        return switch (w) {
            case 0x30 -> "Standby";
            case 0x31 -> "Phasing";
            case 0x32 -> "Change-over";
            case 0x33 -> "Idle";
            case 0x34 -> "Traffic";
            case 0x35 -> "Error";
            case 0x36 -> "RQ";
            case 0x37 -> "Sync";
            default -> String.format("w=$%02X", w);
        };
    }

    /**
     * Inverse of {@link #wLabel(int)} for {@code $30}–{@code $37}. {@code null} if {@code label}
     * is not one of those words.
     */
    public static Integer wByte(String label) {
        if (label == null) {
            return null;
        }
        return switch (label) {
            case "Standby" -> 0x30;
            case "Phasing" -> 0x31;
            case "Change-over" -> 0x32;
            case "Idle" -> 0x33;
            case "Traffic" -> 0x34;
            case "Error" -> 0x35;
            case "RQ" -> 0x36;
            case "Sync" -> 0x37;
            default -> null;
        };
    }

    /** Last four payload bytes (immediately before ETB). */
    static String mysteryBeforeEtb(byte[] payload) {
        if (payload == null || payload.length < 4) {
            return null;
        }
        int start = payload.length - 4;
        StringBuilder sb = new StringBuilder(11);
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", payload[start + i] & 0xFF));
        }
        return sb.toString();
    }

    private static Integer morseWpm(byte y, byte z) {
        int yi = y & 0xFF;
        int zi = z & 0xFF;
        if (yi >= '0' && yi <= '9' && zi >= '0' && zi <= '9') {
            return (yi - '0') * 10 + (zi - '0');
        }
        if (yi <= 9 && zi <= 9) {
            return yi * 10 + zi;
        }
        return null;
    }

    /**
     * Parsed OPMODE reply for Status Monitor {@code Mode:} and ARQ ISS/IRS / link-phase.
     *
     * @param transmit {@code true} ISS (x=S), {@code false} IRS (x=R), {@code null} if x absent
     * @param wLabel   *w* status word, or {@code Standby} for {@code Pt}; {@code null} if none
     */
    public static final class Decoded {
        public final String modeName;
        public final String wLabel;
        public final Boolean transmit;
        public final Integer morseWpm;
        public final boolean standby;
        /** Four bytes before ETB as {@code HH HH HH HH}, or {@code null}. */
        public final String mysteryBytesHex;
        /** Pactor trailer *u*: 100 or 200, or {@code null} if unknown. */
        public final Integer pactorBaud;
        /** Pactor trailer *s*: longpath connect, or {@code null} if unknown. */
        public final Boolean longpath;

        public Decoded(String modeName, String wLabel, Boolean transmit, Integer morseWpm,
                       boolean standby, String mysteryBytesHex) {
            this(modeName, wLabel, transmit, morseWpm, standby, mysteryBytesHex, null, null);
        }

        public Decoded(String modeName, String wLabel, Boolean transmit, Integer morseWpm,
                       boolean standby, String mysteryBytesHex, Integer pactorBaud, Boolean longpath) {
            this.modeName = modeName;
            this.wLabel = wLabel;
            this.transmit = transmit;
            this.morseWpm = morseWpm;
            this.standby = standby;
            this.mysteryBytesHex = mysteryBytesHex;
            this.pactorBaud = pactorBaud;
            this.longpath = longpath;
        }

        public boolean hasDirection() {
            return transmit != null;
        }

        public boolean isPactorStandby() {
            return "Pactor Standby".equals(modeName);
        }

        public boolean isPactorListen() {
            return "Pactor Listen".equals(modeName);
        }

        public boolean isPactorArq() {
            return "Pactor ARQ".equals(modeName);
        }

        public boolean isPactorFec() {
            return "Pactor FEC".equals(modeName);
        }

        /**
         * {@code Mode: Pactor ARQ  Traffic  Tx  200 baud}
         * (longpath when field s is {@code $31}; unnamed trailer bytes omitted when u/s parse).
         */
        public String statusLine() {
            StringBuilder sb = new StringBuilder("Mode: ");
            sb.append(modeName == null ? "—" : modeName);
            if (wLabel != null && !wLabel.isBlank()) {
                sb.append("  ").append(wLabel);
            }
            if (transmit != null) {
                sb.append("  ").append(transmit ? "Tx" : "Rx");
            }
            if (morseWpm != null) {
                sb.append("  ").append(morseWpm).append(" WPM");
            }
            if (pactorBaud != null) {
                sb.append("  ").append(pactorBaud).append(" baud");
            }
            if (Boolean.TRUE.equals(longpath)) {
                sb.append("  longpath");
            }
            if (pactorBaud == null && mysteryBytesHex != null && !mysteryBytesHex.isBlank()) {
                sb.append("  mystery bytes: ").append(mysteryBytesHex);
            }
            return sb.toString();
        }
    }
}
