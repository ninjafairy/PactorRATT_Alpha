package com.pactorratt.alpha.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Packaged build identity. Maven filters {@code build-info.properties}; the shaded jar
 * also carries {@code Build-Number} and {@code Build-Time}. An unpackaged run yields {@code ?}.
 */
public final class BuildInfo {

    private BuildInfo() {
    }

    /** Sequential build number, or {@code ?} when this is not a packaged jar. */
    public static String buildNumber() {
        String fromResource = readBuildInfoProperty("buildNumber");
        if (fromResource != null) {
            return fromResource;
        }
        String fromManifest = readManifestValue("Build-Number");
        if (fromManifest != null && !fromManifest.contains("${")) {
            return fromManifest;
        }
        return "?";
    }

    /** Package timestamp, or a note when this is not a packaged jar. */
    public static String buildTime() {
        String fromResource = readBuildInfoProperty("buildTime");
        if (fromResource != null) {
            return fromResource;
        }
        String fromManifest = readManifestValue("Build-Time");
        if (fromManifest != null && !fromManifest.contains("${")) {
            return fromManifest;
        }
        return "unknown (not a packaged jar)";
    }

    /** Title fragment, including the leading space: {@code " b105"} or {@code " b?"}. */
    public static String titleTag() {
        return " b" + buildNumber();
    }

    private static String readBuildInfoProperty(String key) {
        try (InputStream in = BuildInfo.class.getResourceAsStream("/build-info.properties")) {
            if (in == null) {
                return null;
            }
            java.util.Properties props = new java.util.Properties();
            props.load(in);
            String value = props.getProperty(key);
            if (value == null || value.isBlank() || value.contains("${")) {
                return null;
            }
            return value.trim();
        } catch (IOException ignored) {
            return null;
        }
    }

    private static String readManifestValue(String key) {
        try {
            Enumeration<URL> resources = BuildInfo.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream in = url.openStream()) {
                    Manifest mf = new Manifest(in);
                    Attributes attrs = mf.getMainAttributes();
                    // Prefer our shaded app jar (has Main-Class).
                    String mainClass = attrs.getValue(Attributes.Name.MAIN_CLASS);
                    String value = attrs.getValue(key);
                    if (value != null && !value.isBlank()
                            && mainClass != null
                            && mainClass.contains("PactorRattAlphaApp")) {
                        return value.trim();
                    }
                }
            }
            // Fallback: first manifest that has the key.
            resources = BuildInfo.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try (InputStream in = resources.nextElement().openStream()) {
                    String value = new Manifest(in).getMainAttributes().getValue(key);
                    if (value != null && !value.isBlank() && !value.contains("${")) {
                        return value.trim();
                    }
                }
            }
        } catch (IOException ignored) {
            // Fall through.
        }
        return null;
    }
}
