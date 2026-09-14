package com.pactorratt.alpha.hostmode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scan a completed Listen inbound line for Heard ({@code de CALL}), Mentioned (bare CALL),
 * and connect frames ({@code ?>… <C>}).
 * Callsign: 1–2 letters, 1 digit, 1–3 letters, then space or end of line. {@code de} is a
 * whole word (not the end of {@code aside}/{@code made}).
 * {@code <C>} marks a connect/calling beacon, not a complete copy. Those tokens never go
 * to Heard or Mentioned. Promote via {@link #promoteConnect}.
 */
public final class CallsignLineParser {

    /** 1–2 letters, 1 digit, 1–3 letters; not glued to a longer alphanumeric token. */
    private static final String CALL_BODY = "[A-Za-z]{1,2}[0-9][A-Za-z]{1,3}";
    private static final Pattern CALLSIGN = Pattern.compile("(?i)^" + CALL_BODY + "$");
    private static final Pattern HEARD_DE = Pattern.compile(
            "(?i)(?<![A-Za-z0-9])de (" + CALL_BODY + ")(?=$| )");
    private static final Pattern BARE_CALL = Pattern.compile(
            "(?i)(?<![A-Za-z0-9])(" + CALL_BODY + ")(?=$| )");
    /** Entire line: {@code ?>} raw-token space {@code <C>}. Token may be truncated. */
    private static final Pattern CONNECT_FRAME = Pattern.compile("^\\?>(.*) <C>$");

    public static final int CONNECT_STREAK = 3;
    public static final int CONNECT_WINDOW = 5;

    private CallsignLineParser() {
    }

    public static final class Hits {
        public final List<String> heard;
        public final List<String> mentioned;
        /**
         * Raw text between {@code ?>} and {@code  <C>}, uppercased, or {@code null}
         * if this line is not a connect frame. Empty string if the wrappers were present
         * with nothing in the middle.
         */
        public final String connectToken;

        Hits(List<String> heard, List<String> mentioned, String connectToken) {
            this.heard = heard;
            this.mentioned = mentioned;
            this.connectToken = connectToken;
        }
    }

    public static Hits parse(String line) {
        String connect = connectToken(line);
        if (connect != null) {
            return new Hits(List.of(), List.of(), connect);
        }
        List<String> heard = new ArrayList<>();
        Set<Integer> heardStarts = new LinkedHashSet<>();
        if (line != null && !line.isEmpty()) {
            Matcher de = HEARD_DE.matcher(line);
            while (de.find()) {
                heard.add(de.group(1).toUpperCase(Locale.ROOT));
                heardStarts.add(de.start(1));
            }
            Matcher bare = BARE_CALL.matcher(line);
            List<String> mentioned = new ArrayList<>();
            while (bare.find()) {
                if (heardStarts.contains(bare.start(1))) {
                    continue;
                }
                mentioned.add(bare.group(1).toUpperCase(Locale.ROOT));
            }
            return new Hits(heard, mentioned, null);
        }
        return new Hits(List.of(), List.of(), null);
    }

    /**
     * Raw token inside a connect frame, or {@code null} if the line is not {@code ?>… <C>}.
     */
    public static String connectToken(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        String trimmed = line.replace("\r", "").trim();
        Matcher m = CONNECT_FRAME.matcher(trimmed);
        if (!m.matches()) {
            return null;
        }
        return m.group(1).trim().toUpperCase(Locale.ROOT);
    }

    public static boolean isCallsign(String token) {
        return token != null && !token.isEmpty() && CALLSIGN.matcher(token).matches();
    }

    /**
     * Either gate: three identical connect tokens in a row (end of {@code recent}), or
     * last five where the two longest strings are identical and every other token is a
     * leading prefix of that longest call. Result must be a valid callsign.
     * {@code recent} is oldest → newest; only the last {@link #CONNECT_WINDOW} are used
     * for the stem gate.
     */
    public static String promoteConnect(List<String> recent) {
        if (recent == null || recent.isEmpty()) {
            return null;
        }
        if (recent.size() >= CONNECT_STREAK) {
            int n = recent.size();
            String a = recent.get(n - 1);
            if (a.equals(recent.get(n - 2)) && a.equals(recent.get(n - 3)) && isCallsign(a)) {
                return a;
            }
        }
        if (recent.size() >= CONNECT_WINDOW) {
            List<String> window = recent.subList(recent.size() - CONNECT_WINDOW, recent.size());
            return stemPromote(window);
        }
        return null;
    }

    /**
     * Last-5 stem gate. Two longest tokens must be the same string; every token in the
     * window must be a prefix of it. Confirmed: {@code KE6O} among {@code WA6HVC} fails;
     * two {@code WA6HV} plus shorter stems pass as {@code WA6HV}.
     */
    static String stemPromote(List<String> five) {
        if (five == null || five.size() != CONNECT_WINDOW) {
            return null;
        }
        int maxLen = 0;
        for (String t : five) {
            if (t != null && t.length() > maxLen) {
                maxLen = t.length();
            }
        }
        if (maxLen == 0) {
            return null;
        }
        String longest = null;
        int copies = 0;
        for (String t : five) {
            if (t == null || t.length() != maxLen) {
                continue;
            }
            if (longest == null) {
                longest = t;
                copies = 1;
            } else if (t.equals(longest)) {
                copies++;
            } else {
                return null;
            }
        }
        if (longest == null || copies < 2) {
            return null;
        }
        for (String t : five) {
            if (t == null || !longest.startsWith(t)) {
                return null;
            }
        }
        return isCallsign(longest) ? longest : null;
    }
}
