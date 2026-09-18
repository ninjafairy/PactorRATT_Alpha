# This program is AI generated using Cursor with Grok 4.5 High Fast, 4.5 High, 4.6 High.

# Try it out

Run Java_Troubleshooter.jar from the "stable" build page to make sure the program launches with the proper version of java.

Now with PactoRATT_Launcher-v1 you dont have to mess with java at all, it will tell you if you need to use the launcher,
And if so find and launch PactorRATT with the proper version of JAVA

## Builds > Most Recent Build for the latest 

# Latest "Stable" Build
https://github.com/ninjafairy/PactorRATT_Alpha/releases/tag/Experimental.v.1.0.0




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

Portable root is the folder **containing the running jar**. All program files live under `{jarDir}/config/` (`settings.json`, `buddies.json`, `heard.json`, `mentioned.json`, `config.ini`, optional `debug-YYYYMMDD-HHMMSS.log`). There is no `logs/` folder and no top-level `buddies.json`. Copying the jar (Downloads, `Builds/Most Recent Build/`, etc.) creates `config/` beside that copy, not beside the GitHub tree.

Typical launch (`Run.txt`):

```bash
java --enable-native-access=ALL-UNNAMED -jar "Builds/Most Recent Build/PactorRATT_Alpha.jar"
```

From the Maven output (config then appears next to `target/`):

```bash
java --enable-native-access=ALL-UNNAMED -jar target/PactorRATT_Alpha.jar
```

COM settings default to **1200 7N1**. Use **Listen** or **Connect** to exercise windows; TNC actions need **TNC → Connect**.
