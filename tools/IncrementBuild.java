import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Bumps {@code buildNumber} in a properties file. Invoked from Maven initialize via
 * {@code java tools/IncrementBuild.java <file>}.
 */
public class IncrementBuild {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: IncrementBuild <build.number.properties>");
            System.exit(1);
        }
        Path file = Path.of(args[0]);
        int n = 0;
        if (Files.isRegularFile(file)) {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String t = line.trim();
                if (t.isEmpty() || t.startsWith("#")) {
                    continue;
                }
                int eq = t.indexOf('=');
                if (eq > 0 && "buildNumber".equals(t.substring(0, eq).trim())) {
                    n = Integer.parseInt(t.substring(eq + 1).trim());
                }
            }
        }
        n++;
        String body = "# Sequential build number. Incremented by Maven on each build.\n"
                + "buildNumber=" + n + "\n";
        Files.writeString(file, body, StandardCharsets.UTF_8);
        System.out.println("Build number: " + n);
    }
}
