# This program is AI generated using Cursor with Grok 4.5 High Fast

# Try it out

## Pick a .jar out of the Builds folder and run it. It will create some folders so place it in an empty folder 




# PactorRATT_Alpha

Portable Java 21 Swing chat client for **PK-232 Host Mode Pactor**.

- Product spec: [`PtRa_specification.md`](PtRa_specification.md)
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
- License: AGPL-3.0

## Build

Requires **JDK 21+** and **Maven**.

```bash
mvn -q package
```

If `mvn` is not on your PATH, install Maven or use a local copy under `.tools/` (gitignored).

Uberjar: `target/PactorRATT_Alpha.jar`

## Run (portable)

From this folder (so `config/`, `buddies.json`, and `logs/` stay beside the app):

```bash
java -jar target/PactorRATT_Alpha.jar
```

Phase 1 is an **offline UI shell**. COM settings default to **1200 7N1**. Host Mode / TNC session comes in later phases. Use **File → Preview ARQ window** or **Listen** to exercise connection UI without a TNC.
