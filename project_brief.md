# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-08-19  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 3+4 TNC init **hardware-validated**; Phase 5 **mostly complete** for outbound chat/FEC/ARQ data paths — Listen toggle Host cmds (`PN`/`Pt`), call-timeout, grey→green / OPMODE still open  
**License:** AGPL-3.0  
**Support contact (compat popups):** KJ7RBS@gmail.com

---

## What this project is

A portable **Java 21 + Swing** desktop chat program that drives a **PK-232 with Pactor firmware** in **Host Mode**. The TNC owns ARQ; the app is a structured AIM 3.x–inspired terminal with chat, status, and control actions separated.

**Normative docs (read in this order when resuming):**

1. [`PtRa_specification.md`](PtRa_specification.md) — full product/program specification  
2. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — software architecture (includes `hostIoLock` / §4.3–§4.4 pacing)  
3. This file — detailed “where we left off” summary  
4. [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — ordered TNC connect steps (hardware-updated)  
5. Other refs under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4 hostmode, Compat map, Pactor chapter, hardware capture)

---

## What we have accomplished

### Design & documentation (locked / updated)

- Full requirements: modes (Idle / Listen / Unproto-as-UI-“FEC” / ARQ), windows, IRS/ISS send pipeline, control map, settings, non-goals.
- Spec: [`PtRa_specification.md`](PtRa_specification.md) — includes **§9.2 Command / response pacing** (Ch. 4 §4.3) and **§4.8 / §8.5 data block limits**.
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — Host I/O includes command-wait, data-ack wait, and single `hostIoLock`.
- Reference imports under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4, Hardware capture, Pactor chapter, Compat map, Alpha init).

### Key product decisions (do not re-litigate casually)

| Topic | Decision |
|---|---|
| Stack | Java 21, Swing, jSerialComm, Maven uberjar, one process |
| Air mode | Pactor only |
| Host I/O | `HPOLL OFF` (`HPN`) in steady state; `GG` for entry/recovery only; **wait for each command response** (Ch. 4 §4.3); **wait for each data-ack** (Ch. 4 §4.4); all round-trips on **`hostIoLock`** |
| UI “FEC” | Label only; command is `PTSend` / unproto (`PD` + `n,x`) |
| FEC params | Program settings: **FEC 200** checkbox → `n=2` else `n=1`; **Retries** spinner 1–5 → `x` (defaults `fec200=false`, `fecRetries=1` → Host `PD1,1`) |
| Callsign | One config value → `ML` and `Mf` |
| Idle | `Pt` (case-sensitive) |
| Abort | Listen on → `PN`; else → `Pt` |
| Clean disconnect / handover (Alpha wiring) | For now: Host CMD `RE` / `PV` as separate mnemonics (not embedded CTRL-D/CTRL-Z). Spec still documents embed-char path; may revisit after hardware trials |
| FEC end TX | Embedded CTRL-D (`$04`) in Host data after buffer (per PTSend docs) — **not** Host CMD `RE` |
| Disconnect now / `Rcve` | **Deferred** — confirm on hardware before coding |
| Handover now | **Stubbed** for now |
| Seize | `AG` (`AChg`) |
| Inbound text CTLs | Treat **all** `0x30`–`0x3F` the same (Pactor channel 0 only; hardware PTL uses `0x3F`) |
| EAS | OFF (`EAN`); grey→green later via TX-empty/idle (not EAS) |
| Defaults | `PBY` (PT200), `PH0` (PTHUFF off), `WON` (WORDOUT off) |
| COM default | **1200 7N1** (user-selectable; no forced 8N1 on open) |
| Compat | Supported v7.x continue; listed pre-v7 **hard refuse**; HK/UDC/unknown **warn + email + continue** |
| UI Connect | **TNC → Connect/Disconnect** = serial/Host session; main-window **Connect** = ARQ `PG` only |
| Long path | User may type `!CALL`; no dedicated checkbox |
| Startup gate | Blocking experimental warning dialog before main UI |
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs |

### Phase 1 — Offline UI shell (done)

| Package | Role |
|---|---|
| `app` | Entry, `AppController`, `AppMode`, TNC + ARQ Host actions, inbound listener |
| `ui` | Main / connection windows, settings, Debug Monitor, startup warning, `WrapLayout` |
| `config` | Portable `config/settings.json` + `config/buddies.json` |
| `util` | Per-launch debug log under `logs/` |
| `hostmode` | Framing, demux, session, compat, init, data send |
| `serial` | jSerialComm + byte listeners |

Working offline: Stations `JTree`, Listen/ARQ preview windows, commit modes, buddies, menus, portable layout.

### Phase 3+4 — Serial + Host init (done, hardware-proven)

**Session lifecycle**

- **TNC → Connect** runs `TncInitializer` off the EDT.
- Open COM at configured baud/bits/parity/stop/flow.
- Autobaud: send `*` (`0x2A`, no CR); wait **2 s clear air** (max 15 s); capture sign-on; modal popup if non-empty.
- Host detect: `OGG` / double-SOH resync; else ASCII `AWLEN 8` → `PARITY 0` → `8BITCONV ON` → `RESTART` → `*` again → `HOST ON` → re-probe.
- Compat: `AE6` + four `MM` reads for `$0006..$0009`.
- Firmware/hardware info popup (date + all 8 bits of `$0009`; OK or **4 s auto-close**), then hard-refuse / warn / supported.
- Coded init, then `tncConnected = true`.
- **TNC → Disconnect** closes session / aborts in-flight connect.
- If connect **fails** while **Debug Monitor** is open, serial session is **kept** for manual probing until the monitor closes (or Disconnect).

**Hardware-verified Host encoding rules** (critical)

- **No space** between mnemonic and argument (`HPN`, `AE6`, `MLCALL`, `PGN7ML`, `PD2,3`, not `HP N` / `PG N7ML`).
- Boolean switches: prefer `Y`/`N` (`HPN`); `HPOFF` also works without space.
- Integers where required: PTHUFF is Host **`PH`** (not `pH`) with level `PH0` for off.
- `AE` uses **decimal** address digits: `$0006` → `AE6`.
- `MM` **read** response is `MM$hh` (ASCII hex after `$`), e.g. `MM$93` → `0x93` — not binary status+data.
- Command ack status `0x01` is DLE-escaped as `10 01` on the wire (SOH must be escaped).
- **Command pacing (Ch. 4 §4.3):** wait for each command response before another command (`sendCommand`; Debug Monitor F&F is probe-only).
- **Data pacing (Ch. 4 §4.4):** wait for `$5F … $00` after each data block before more data.

**Coded init commands (current)**

`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML…` / `Mf…`, `AA…`, `Pt`.

**Build:** `target/PactorRATT_Alpha.jar` (shaded). Manifest includes `Build-Time` (Maven package timestamp). Local Maven may be under `.tools/` if system `mvn` is missing.

### Phase 5 — Pactor flows (mostly done; Listen Host toggle still open)

#### Host data channel + §4.4 / §4.8 (done)

- [`HostFrameCodec`](src/main/java/com/pactorratt/alpha/hostmode/HostFrameCodec.java): `encodeData` → CTL `0x2x`; `isDataAck` (exact 3-byte payload ending `$00`); `isDataStatusError` (`W`/`Y`); `MAX_HOST_TO_TNC_PAYLOAD = 330`.
- [`HostSession.sendData`](src/main/java/com/pactorratt/alpha/hostmode/HostSession.java): chunks pre-escape payloads &gt;330; waits for data-ack **per block**; fail-fast on `$5F…W/Y`; on timeout clears `statusQueue` before unlock (no stale-ack reuse).
- **`hostIoLock`:** single Host round-trip lock serializes `sendCommand`, `sendData`, `probeOgg`, `readMemoryByte`, and Debug F&F *writes*. Fixes concurrent ISS/ARQ/FEC races that could clear each other’s waiter queues. (Replaces earlier data-only `dataSendLock`.)
- Text → Host bytes: `\n`→`\r`, trailing CR if missing (`AppController.toHostDataBytes`).

#### ARQ App TX → TNC / ISS flush (done)

- While **IRS**: commits go to App TX buffer only (not transcript).
- ARQ **Flush ISS**: buffer → grey transcript + `sendData` ch0; flips local role to ISS.
- While **ISS**: further commits → grey transcript + immediate `sendData`.
- Listen window has **no** ISS flush control (FEC is the Listen send path).

#### Listen FEC / End TX (done)

- Button **FEC / End TX** (Listen only): requires non-empty App TX buffer.
- Flow: grey transcript + clear buffer → Host `PD`+`n,x` from config (`AppConfig.ptSendHostCommand()`) → ch0 data (chunked) with trailing CTRL-D `$04` → mode UI FEC then back to Listen/Idle.
- Program Settings row: **FEC 200** checkbox + **Retries** spinner (1–5); persisted as `fec200` / `fecRetries` in `settings.json`.

#### Inbound frame demux + transcript (done)

- All CTL `0x30`–`0x3F` → `INBOUND_DATA` → active **ARQ else Listen** transcript (`appendRemoteText`; ARQ wins).
- `0x4F` → `commandQueue`; `0x5F` → `statusQueue`; other types event-only.
- `drainInbound()` clears waiter queues only (does not drop UI inbound text).
- PTL samples: `01 3F … 17` ([`docs/Hardware_Capture_Sample.md`](docs/Hardware_Capture_Sample.md)).

#### Debug Monitor (done)

- Live TX/RX hex + ASCII; RX Host blocks coalesced per `SOH…ETB`.
- Manual Cmd+Payload → `0x4F` fire-and-forget (takes `hostIoLock` for write only).
- Retain serial session after failed connect while monitor open.

#### ARQ connection-window controls (wired)

| UI control | Host action |
|---|---|
| Disc. after TX clear | CMD `RE` |
| HO after TX clear | CMD `PV` |
| Seize | CMD `AG` |
| Abort | Listen checkbox on → `PN`, else `Pt`; then `markArqDead` |
| HO with text | Canned handover → data ch0, then CMD `PV` |
| Disc. with text | Canned disconnect → data ch0, then CMD `RE` |
| Flush ISS | App TX → `sendData` + grey; mark ISS |
| Disconnect now | **Stubbed** (await `Rcve` / `RC` hardware confirm) |
| Handover now | **Stubbed** |

Canned strings in Program Settings (`cannedHandoverText` / `cannedDisconnectText`; defaults `KKK` / `SK`).

#### Main-window ARQ Connect (wired)

- Connect / buddy double-click → `PG`+callsign (no space; `!` preserved).
- Wait command ACK `0x00` → open ARQ window; fail/timeout → error, no window.
- Call-timeout / “did not answer” after ACK: **stubbed**.

#### Buddies

- Path: **`config/buddies.json`**. Missing → create defaults `N0CALL`, `KJ7RBS` with **CRLF** line endings (does not overwrite existing).

#### Startup warning (done)

- Modal blocking dialog before main UI: flashing **!!WARNING!!**, experimental-risk copy, Java runtime + jar **Build-Time**, buttons **Dont break my stuff** (exit) / **Risk it for the Biscuit** (continue).

#### Still not Host-wired / Phase 5 leftovers

- Main **Listen** toggle: creates/destroys Listen window only — does **not** send `PN` / `Pt`.
- No OPMODE / link-status / TX-empty / Heard-Mentioned parsers (grey→green deferred to Phase 6).

---

## What is intentionally not done yet

- Real Listen on/off Host commands (`PN` / `Pt`) from the Listen toggle
- Grey→green confirmation (TX-empty + idle) — Phase 6
- Handover now / Disconnect now (`RC` deferred)
- Call-timeout / “did not answer” after successful `PG` ACK
- Status-bar / OPMODE / link-block decode
- Incoming ARQ detect → open ARQ window
- Heard / Mentioned parsers from monitor text (transcript already receives raw lines)
- Settings → TNC large parameter editor
- Hardware-validate Host CMD `RE`/`PV` vs embedded CTRL-D/CTRL-Z for ARQ clean end (FEC already uses embedded CTRL-D)

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

Run from the project (or portable) folder so `config/` (`settings.json`, `buddies.json`) and `logs/` resolve correctly (`user.dir`).

Requires **JDK 21+**.

**Hardware debug tip:** Open **TNC → Debug Monitor…** first, then **TNC → Connect**. Host RX lines should show full `01 3F … 17` blocks (coalesced). After Listen Host `PN` is wired (still TODO), PTL monitor text should land in the Listen transcript. On TNC-init failure the port stays open while the monitor remains open. To leave Host Mode manually: Debug Monitor Cmd `HO` Payload `N`.

---

## Recommended next steps (implementation order)

### Finish Phase 5 — remaining Pactor flows

1. **Wire Listen toggle:** on → Host `PN` + Listen window; off → Host `Pt` + destroy Listen window (keep inactive Listen window when ARQ is active per spec). This unlocks real PTL inbound into the demuxed transcript path.
2. Implement **call-timeout / did not answer** after `PG` ACK once status signals are known — do not invent OPMODE bytes.
3. Keep Disconnect now / Handover now stubbed until `RC` / immediate path confirmed on hardware.
4. Hardware-validate Alpha choice of Host CMD `RE`/`PV` vs embedding CTRL-D/CTRL-Z for ARQ clean end.

### Phase 6 — Status + confirmation

1. OPMODE / status / link-message parsing into status bar + ticker (`LINK_STATUS` / `LINK_MESSAGE` events already demuxed).  
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
  PactorRattAlphaApp.java   entry + startup warning gate
  AppController.java        modes, windows, connectTnc/disconnectTnc,
                            requestConnect (PG), arq* Host actions,
                            sendOutboundChat (ISS), listenFecEndTx (PD+data+CTRL-D),
                            HostEvent INBOUND_DATA → transcript,
                            debug/compat/startup dialogs, session retain
hostmode/
  HostFrameCodec.java       SOH/CTL/ETB + DLE; encodeData; classifyCtl;
                            MAX_HOST_TO_TNC_PAYLOAD (330); isDataAck / isDataStatusError;
                            FrameParser
  HostSession.java          demux, commandQueue/statusQueue, sendCommand,
                            sendData (chunk + data-ack), hostIoLock round-trips,
                            OGG, AE/MM$hh
  HostEvent.java            typed demux events
  TncInitializer.java       full connect/init orchestration
  CompatChecker.java        fingerprint policy
  CompatResult.java         date + bit display helpers
  StartupMessageUi.java     sign-on popup callback
  CompatInfoUi.java         firmware/hardware popup callback
serial/
  SerialPortService.java    jSerialComm + SerialByteListener taps
ui/
  MainWindow.java           tree, ARQ Connect, Listen toggle, TNC menu
  ConnectionWindow.java     chat UI + ARQ controls + FEC/End TX + appendRemoteText
  DebugMonitorWindow.java   coalesced RX Host lines + Cmd/Payload Send
  ProgramSettingsDialog.java  commit mode, canned text, FEC 200 / Retries
  StartupWarningDialog.java blocking experimental warning + Java/build info
  ComPortDialog.java        COM settings
config/
  AppConfig, ConfigStore    settings.json + buddies.json (CRLF defaults) under config/
util/                       DebugLog
docs/Alpha_Init_Sequence.md canonical init + Host encoding + command pacing
docs/Ch. 4 hostmode         Chapter 4 source (§4.3 / §4.4 / §4.8)
docs/Hardware_Capture_Sample.md  PTL RX samples
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS per-char coloring, Morse-ID disconnect, auto-AAB, Spring/DI frameworks, generic TNC abstraction layers.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md` and `docs/Alpha_Init_Sequence.md`. Phase 1 UI and Phase 3+4 TNC init are done and hardware-validated. Phase 5 outbound is largely done: Host `sendData` with Ch. 4 §4.8 330-char chunking; single `hostIoLock` for command+data round-trips (§4.3/§4.4); ARQ controls (`RE`/`PV`/`AG`/Abort, with-text); Connect (`PG`+callsign, ACK then ARQ window); ISS App TX flush; Listen FEC/End TX (`PD`+n,x from Program settings + data + CTRL-D); inbound demux (`0x30`–`0x3F` → active Listen/ARQ transcript); Debug Monitor coalesced RX; startup warning dialog; buddies.json CRLF defaults. **Next:** Listen toggle → `PN`/`Pt`; then call-timeout/did-not-answer; Phase 6 grey→green / OPMODE. Do not invent OPMODE/TX-empty/Rcve; leave Disconnect now / Handover now stubbed.
