# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-07-17  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 3+4 TNC session init **hardware-validated** through Host entry, compat fingerprint, and coded init; Debug Monitor for live I/O; **Phase 5 Pactor air flows not started**  
**License:** AGPL-3.0  
**Support contact (compat popups):** KJ7RBS@gmail.com

---

## What this project is

A portable **Java 21 + Swing** desktop chat program that drives a **PK-232 with Pactor firmware** in **Host Mode**. The TNC owns ARQ; the app is a structured AIM 3.x–inspired terminal with chat, status, and control actions separated.

**Normative docs (read in this order when resuming):**

1. [`PtRa_specification.md`](PtRa_specification.md) — full product/program specification  
2. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — software architecture  
3. This file — detailed “where we left off” summary  
4. [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — ordered TNC connect steps (hardware-updated)  
5. Other refs under [`docs/`](docs/) (Host Mode, HostCommands, Compat map, Pactor chapter)

---

## What we have accomplished

### Design & documentation (locked earlier)

- Full requirements: modes (Idle / Listen / Unproto-as-UI-“FEC” / ARQ), windows, IRS/ISS send pipeline, control map, settings, non-goals.
- Spec: [`PtRa_specification.md`](PtRa_specification.md).
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).
- Reference imports:
  - [`docs/PK232_HostMode_Reference.md`](docs/PK232_HostMode_Reference.md)
  - [`docs/HostCommands - Trimmed.md`](docs/HostCommands%20-%20Trimmed.md) — case-sensitive Host mnemonics
  - [`docs/Pactor_Chapter.md`](docs/Pactor_Chapter.md)
  - [`docs/Compat_Memory_Map.md`](docs/Compat_Memory_Map.md)
  - [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — kept in sync with hardware findings

### Key product decisions (do not re-litigate casually)

| Topic | Decision |
|---|---|
| Stack | Java 21, Swing, jSerialComm, Maven uberjar, one process |
| Air mode | Pactor only |
| Host I/O | `HPOLL OFF` (`HPN`) in steady state; `GG` for entry/recovery only |
| UI “FEC” | Label only; command is `PTSend` / unproto (`PD`) |
| Callsign | One config value → `ML` and `Mf` |
| Idle | `Pt` (case-sensitive) |
| Abort | Listen on → `PN`; else → `Pt` |
| Clean disconnect after TX | Embed `<CTRL-D>` (RECEIVE) |
| Disconnect now / `Rcve` | **Deferred** — confirm on hardware before coding |
| Seize / Handover | `AG` / `PTOver` (default `<CTRL-Z>`) |
| EAS | OFF (`EAN`); grey→green later via TX-empty/idle (not EAS) |
| Defaults | `PBY` (PT200), `PH0` (PTHUFF off), `WON` (WORDOUT off) |
| COM default | **1200 7N1** (user-selectable; no forced 8N1 on open) |
| Compat | Supported v7.x continue; listed pre-v7 **hard refuse**; HK/UDC/unknown **warn + email + continue** |
| UI Connect | **TNC → Connect/Disconnect** = serial/Host session; main-window **Connect** = ARQ only |
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs |

### Phase 1 — Offline UI shell (done)

| Package | Role |
|---|---|
| `app` | Entry, `AppController`, `AppMode`, TNC connect lifecycle |
| `ui` | Main / connection windows, settings, Debug Monitor, `WrapLayout` |
| `config` | Portable `config/settings.json` |
| `util` | Per-launch debug log under `logs/` |
| `hostmode` | Framing, session, compat, init |
| `serial` | jSerialComm + byte listeners |

Working offline: Stations `JTree`, Listen/ARQ preview windows, commit modes, buddies, menus, portable layout.

### Phase 3+4 — Serial + Host init (done, hardware-proven)

**Session lifecycle**

- **TNC → Connect** runs `TncInitializer` off the EDT.
- Open COM at configured baud/bits/parity/stop/flow.
- Autobaud: send `*` (`0x2A`, no CR); wait **2 s clear air** (max 15 s); capture sign-on; modal popup if non-empty.
- Host detect: `OGG` / double-SOH resync; else ASCII `AWLEN 8` → `PARITY 0` → `8BITCONV ON` → `RESTART` → `*` again → `HOST ON` → re-probe.
- Compat: `AE6` + four `MM` reads for `$0006..$0009`.
- Firmware/hardware info popup (date + all 8 bits of `$0009`; OK or **4 s auto-close**, non-blocking), then hard-refuse / warn / supported.
- Coded init, then `tncConnected = true`.
- **TNC → Disconnect** closes session / aborts in-flight connect.
- If connect **fails** while **Debug Monitor** is open, serial session is **kept** for manual probing until the monitor closes (or Disconnect).

**Hardware-verified Host encoding rules** (critical)

- **No space** between mnemonic and argument (`HPN`, `AE6`, `MLCALL`, not `HP N`).
- Boolean switches: prefer `Y`/`N` (`HPN`); `HPOFF` also works without space.
- Integers where required: PTHUFF is Host **`PH`** (not `pH`) with level `PH0` for off.
- `AE` uses **decimal** address digits: `$0006` → `AE6`.
- `MM` **read** response is `MM$hh` (ASCII hex after `$`), e.g. `MM$93` → `0x93` — not binary status+data.
- Command ack status `0x01` is DLE-escaped as `10 01` on the wire (SOH must be escaped).

**Coded init commands (current)**

`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML…` / `Mf…`, `AA…`, `Pt`.

**Debug Monitor (TNC → Debug Monitor…)**

- Live TX/RX hex + ASCII of all serial bytes.
- Manual **Cmd** (2-letter) + **Payload** (ASCII) + **Send** → framed Host CTL `0x4F` fire-and-forget.
- Send always enabled in UI; needs an open session (connect or retained-after-failure).
- Clear / Pause; 200k char cap.

**Build:** `target/PactorRATT_Alpha.jar` (shaded). Local Maven may be under `.tools/` if system `mvn` is missing.

---

## What is intentionally not done yet

- Real Listen (`PN`) / Idle return (`Pt`) / ARQ Connect (`PG`) / Unproto FEC (`PD` + `<CTRL-D>`)
- Control buttons on connection window (handover, seize, abort, disconnect-after-clear, with-text)
- Status-bar / OPMODE / link-block decode
- Grey→green confirmation (TNC TX empty + idle)
- Incoming ARQ detect → open ARQ window
- Heard / Mentioned parsers from monitor text
- Settings → TNC large parameter editor
- Disconnect-now via `RC` / `Rcve` until confirmed on hardware

Main-window **Connect** still opens an ARQ UI shell only (no `PG` yet).

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

**Hardware debug tip:** Open **TNC → Debug Monitor…** first, then **TNC → Connect**, to watch `*`, Host frames, `AE`/`MM`, and coded init. On failure the port stays open while the monitor remains open.

---

## Recommended next steps (implementation order)

### Phase 5 — Pactor flows (next real work)

1. `PactorController` (or equivalent) on the live `HostSession`: Listen `PN` / Idle `Pt` / Connect `PG` / Unproto `PD` + `<CTRL-D>`.  
2. Wire connection-window controls (handover, seize, abort, disconnect-after-clear, with-text).  
3. Gate UI on `tncConnected`; keep main **Connect** = ARQ only.  
4. Leave disconnect-now stubbed until `Rcve` tested on hardware.  
5. Remember Host encoding: no spaces; `Y`/`N` or ints as appropriate; case-sensitive mnemonics.

### Phase 6 — Status + confirmation

1. OPMODE / status parsing into status bar + ticker.  
2. TX-empty + idle → flip grey transcript to green.  
3. Incoming ARQ string → open ARQ window.  
4. Heard (` de`) / Mentioned parsers when samples exist.

### Optional / polish

- Further Host entry robustness at odd COM settings if needed.  
- Git commit checkpoint when you want one (user-driven).

---

## Source map (quick)

```text
app/
  PactorRattAlphaApp.java   entry
  AppController.java        modes, windows, connectTnc/disconnectTnc,
                            debug/compat/startup dialogs, session retain
hostmode/
  HostFrameCodec.java       SOH/CTL/ETB + DLE
  HostSession.java          reader, OGG, commands, AE/MM$hh, fire-and-forget
  TncInitializer.java       full connect/init orchestration
  CompatChecker.java        fingerprint policy
  CompatResult.java         date + bit display helpers
  StartupMessageUi.java     sign-on popup callback
  CompatInfoUi.java         firmware/hardware popup callback
serial/
  SerialPortService.java    jSerialComm + SerialByteListener taps
ui/
  MainWindow.java           tree, ARQ Connect, TNC menu
  ConnectionWindow.java     chat UI + controls (Host wiring pending)
  DebugMonitorWindow.java   hex/ASCII monitor + manual Cmd/Payload Send
  ComPortDialog.java        COM settings
config/                     AppConfig, ConfigStore
util/                       DebugLog
docs/Alpha_Init_Sequence.md canonical init order + Host encoding notes
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS per-char coloring, Morse-ID disconnect, auto-AAB, Spring/DI, generic TNC abstraction.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md` and `docs/Alpha_Init_Sequence.md`. Phase 1 UI and Phase 3+4 TNC init (Host entry, autobaud `*`, compat AE/MM, coded init, Debug Monitor) are done and hardware-validated. Host encoding: no space after mnemonics; booleans Y/N; PTHUFF is `PH0`; MM reads return `MM$hh`. Implement Phase 5: Listen/Idle/ARQ Connect/`PTSend` and control Host commands on the live `HostSession`. Do not invent OPMODE/TX-empty/Rcve behavior; leave those stubbed.
