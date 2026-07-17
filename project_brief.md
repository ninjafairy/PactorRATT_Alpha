# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-07-17  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 3+4 TNC init path implemented — serial + Host framer + compat gate + coded init; Pactor air flows still stubbed  
**License:** AGPL-3.0  
**Support contact (compat popups):** KJ7RBS@gmail.com

---

## What this project is

A portable **Java 21 + Swing** desktop chat program that will drive a **PK-232 with Pactor firmware** in **Host Mode**. The TNC owns ARQ; the app is a structured AIM 3.x–inspired terminal with chat, status, and control actions separated.

**Normative docs (read in this order when resuming):**

1. [`PtRa_specification.md`](PtRa_specification.md) — full product/program specification  
2. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — software architecture (in-repo copy of Cursor plan)  
3. This file — short “where we left off” summary  
4. Technical refs under [`docs/`](docs/)
5. [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — ordered TNC connect steps

---

## What we have accomplished

### Design & documentation (locked)

- Full requirements discovery: modes (Idle / Listen / Unproto-as-UI-“FEC” / ARQ), windows, IRS/ISS send pipeline, control map, settings, non-goals.
- Spec written: [`PtRa_specification.md`](PtRa_specification.md).
- Architecture written: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).
- Host / Pactor reference markdown imported:
  - [`docs/PK232_HostMode_Reference.md`](docs/PK232_HostMode_Reference.md) — framing, CTL, `GG`/`HPOLL`, entry
  - [`docs/HostCommands - Trimmed.md`](docs/HostCommands%20-%20Trimmed.md) — commands (case-sensitive Host mnemonics; header added)
  - [`docs/Pactor_Chapter.md`](docs/Pactor_Chapter.md) — Ch.11 operator flows
  - [`docs/Compat_Memory_Map.md`](docs/Compat_Memory_Map.md) — `$0006..$0009` fingerprints + policy
  - [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — connect/init order
- Cursor architecture plan also lives at:  
  `C:\Users\Jadon\.cursor\plans\pactorratt_alpha_architecture_e30d23b0.plan.md`  
  (repo copy is preferred for day-to-day work)

### Key product decisions (do not re-litigate casually)

| Topic | Decision |
|---|---|
| Stack | Java 21, Swing, jSerialComm, Maven uberjar, one process |
| Air mode | Pactor only |
| Host I/O | `HPOLL OFF` (async push); `GG` for entry/recovery only |
| UI “FEC” | Label only; command is `PTSend` / unproto (`PD`) |
| Callsign | One config value → both `MYCALL` (`ML`) and `MYPTCALL` (`Mf`) |
| Idle | `Pt` (Pactor standby; case-sensitive) |
| Abort | Listen on → `PN` (`PTList`); else → `Pt` |
| Clean disconnect after TX | Embed `<CTRL-D>` (RECEIVE) |
| Disconnect now / `Rcve` | **Deferred** — confirm on hardware before coding |
| Seize | `ACHG` (`AG`) works in Pactor |
| Handover | `PTOver` char (default `<CTRL-Z>`); with-text = canned then PTOver |
| EAS | OFF in Alpha; grey→green via later TX-empty/idle detect (not EAS) |
| Defaults | `PT200 ON`, `PTHUFF OFF`, `WORDOUT OFF` |
| COM default | **1200 7N1** (user may change before TNC Connect; no forced 8N1) |
| Compat | Supported v7.x continue; listed pre-v7 **hard refuse**; HK/UDC/unknown **warn + email + allow continue** |
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs |

### Phase 1 code (UI shell)

Maven module `com.pactorratt:PactorRATT_Alpha` with packages:

| Package | Role today |
|---|---|
| `app` | Entry point, `AppController`, `AppMode`; `connectTnc` / `disconnectTnc` |
| `ui` | Main + connection windows, settings dialogs, `WrapLayout`; **TNC** menu |
| `config` | `AppConfig` / `ConfigStore` (portable `config/settings.json`) |
| `util` | Per-launch debug log |
| `hostmode` | Frame codec, `HostSession`, `CompatChecker`, `TncInitializer` |
| `serial` | `SerialPortService` (jSerialComm) |

**UI features:**

- Main window: mode/TNC labels, Stations `JTree`, callsign + **Connect (ARQ only)**, Listen toggle, menus (File / Settings / **TNC Connect·Disconnect** / Help).
- Connection window (Listen or ARQ preview): transcript, App TX buffer, compose, controls, split pane.
- Offline helpers: File → Preview ARQ window, Simulate ISS flush.
- Buddies from [`buddies.json`](buddies.json).
- Portable layout: settings under `config/`, logs under `logs/`.

**Build artifact:** `target/PactorRATT_Alpha.jar` (shaded).  
Local Maven may exist under `.tools/` (gitignored) if system `mvn` was missing.

### Phase 3+4 code (TNC init — implemented)

- Open COM at user settings via **TNC → Connect**.
- Detect Host (`OGG` / double-SOH) or ASCII entry (`AWLEN`/`PARITY`/`HOST ON`).
- Read `$0006..$0009`; hard-refuse / supported / warn dialogs.
- Coded init: `HPOLL OFF`, `EAS OFF`, `PT200 ON`, `PTHUFF OFF`, `WORDOUT OFF`, `ML`/`Mf`, `AA`, `Pt`.
- `tncConnected = true` only after full success.
- **TNC → Disconnect** closes session / aborts in-flight connect.
- Saving COM settings while connected disconnects first.

---

## What is intentionally not done yet

- Real Listen / Connect / PTSend / control Host commands (ARQ Connect button still opens preview window only)
- Status-bar decode (OPMODE / link blocks)
- Grey→green confirmation (TNC TX empty + idle)
- Incoming ARQ parse string
- Heard/Mentioned real parsing from monitor text
- Settings → TNC large parameter editor
- Disconnect-now via `RC` until manually confirmed  

---

## How to build & run (resume checklist)

```powershell
cd c:\Users\Jadon\Documents\GitHub\PactorRATT_Alpha

# If mvn is on PATH:
mvn -q package

# Or use local Maven from earlier setup:
.\.tools\apache-maven-3.9.6\bin\mvn.cmd -q package

java -jar target\PactorRATT_Alpha.jar
```

Run from the project (or portable) folder so `config/`, `buddies.json`, and `logs/` resolve correctly (`user.dir`).

Requires **JDK 21+**.

---

## Recommended next steps (implementation order)

### Phase 5 — Pactor flows

1. Listen (`PN`) / Idle (`Pt`) / Connect (`PG`) / Unproto FEC UI (`PD` + `<CTRL-D>`).  
2. Wire control buttons (handover, seize, abort, disconnect-after-clear, with-text).  
3. Leave disconnect-now stubbed until `Rcve` tested on hardware.

### Phase 6 — Status + confirmation

1. OPMODE / status parsing into status bar + ticker.  
2. TX-empty + idle → flip grey transcript to green.  
3. Incoming ARQ string → open ARQ window.  
4. Heard (` de`) / Mentioned parsers when samples exist.

### Optional soon

- Hardware-validate Host entry at 7N1 vs 8N1 and adjust ASCII entry if needed.  
- Commit git history when you want a checkpoint (user-driven).

---

## Source map (quick)

```text
PactorRattAlphaApp.java     entry
AppController.java          modes, windows, connectTnc/disconnectTnc
MainWindow.java             tree + ARQ Connect + TNC menu
ConnectionWindow.java       chat UI + controls + split
SerialPortService.java      jSerialComm open/read/write
HostFrameCodec.java         SOH/CTL/ETB + DLE
HostSession.java            reader thread, OGG, commands, AE/MM
CompatChecker.java          fingerprint policy
TncInitializer.java         full connect/init orchestration
AppConfig / ConfigStore     portable settings
DebugLog                    logs/debug-*.log
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS per-char coloring, Morse-ID disconnect, auto-AAB, Spring/DI, generic TNC abstraction.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md`. TNC Connect/Disconnect (Host entry, compat, coded init) is done. Implement Phase 5: Listen/Idle/ARQ Connect/`PTSend` and control Host commands on the live `HostSession`. Do not invent OPMODE/TX-empty/Rcve behavior; leave those stubbed.
