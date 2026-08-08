# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-08-02  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 3+4 TNC init **hardware-validated**; Phase 5 **in progress** — outbound Connect/`PG`, ARQ controls, Host `sendData` **+ §4.8 330-char chunking**, ISS App TX→TNC flush (ARQ), Listen **FEC / End TX** (`PD` + data + CTRL-D), inbound CTL demux + transcript, Debug Monitor frame coalesce done; Listen Host cmds (`PN`/`Pt`) / status still open  
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
5. Other refs under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4 hostmode, Compat map, Pactor chapter, hardware capture)

---

## What we have accomplished

### Design & documentation (locked / updated)

- Full requirements: modes (Idle / Listen / Unproto-as-UI-“FEC” / ARQ), windows, IRS/ISS send pipeline, control map, settings, non-goals.
- Spec: [`PtRa_specification.md`](PtRa_specification.md) — includes **§9.2 Command / response pacing** (Ch. 4 §4.3).
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — Host I/O includes command-wait rule.
- Reference imports:
  - [`docs/PK232_HostMode_Reference.md`](docs/PK232_HostMode_Reference.md) — framing, CTLs, command/data pacing
  - [`docs/HostCommands - Trimmed.md`](docs/HostCommands%20-%20Trimmed.md) — case-sensitive Host mnemonics
  - [`docs/Ch. 4 hostmode`](docs/Ch.%204%20hostmode) — PK-232 Chapter 4 source text
  - [`docs/Hardware_Capture_Sample.md`](docs/Hardware_Capture_Sample.md) — live PTL/`0x3F` Debug Monitor samples (SOH often alone due to OS chunking)
  - [`docs/Pactor_Chapter.md`](docs/Pactor_Chapter.md)
  - [`docs/Compat_Memory_Map.md`](docs/Compat_Memory_Map.md)
  - [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — kept in sync with hardware findings

### Key product decisions (do not re-litigate casually)

| Topic | Decision |
|---|---|
| Stack | Java 21, Swing, jSerialComm, Maven uberjar, one process |
| Air mode | Pactor only |
| Host I/O | `HPOLL OFF` (`HPN`) in steady state; `GG` for entry/recovery only; **wait for each command response** before next command (Ch. 4 §4.3) |
| UI “FEC” | Label only; command is `PTSend` / unproto (`PD`) |
| Callsign | One config value → `ML` and `Mf` |
| Idle | `Pt` (case-sensitive) |
| Abort | Listen on → `PN`; else → `Pt` |
| Clean disconnect / handover (Alpha wiring) | For now: Host CMD `RE` / `PV` as separate mnemonics (not embedded CTRL-D/CTRL-Z). Spec still documents embed-char path; may revisit after hardware trials |
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
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs |

### Phase 1 — Offline UI shell (done)

| Package | Role |
|---|---|
| `app` | Entry, `AppController`, `AppMode`, TNC + ARQ Host actions, inbound listener |
| `ui` | Main / connection windows, settings, Debug Monitor, `WrapLayout` |
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

- **No space** between mnemonic and argument (`HPN`, `AE6`, `MLCALL`, `PGN7ML`, not `HP N` / `PG N7ML`).
- Boolean switches: prefer `Y`/`N` (`HPN`); `HPOFF` also works without space.
- Integers where required: PTHUFF is Host **`PH`** (not `pH`) with level `PH0` for off.
- `AE` uses **decimal** address digits: `$0006` → `AE6`.
- `MM` **read** response is `MM$hh` (ASCII hex after `$`), e.g. `MM$93` → `0x93` — not binary status+data.
- Command ack status `0x01` is DLE-escaped as `10 01` on the wire (SOH must be escaped).
- **Command pacing (Ch. 4 §4.3):** The PK-232 always issues a response to each command; **wait for that response before issuing another command** (use `sendCommand` on product/init paths; Debug Monitor fire-and-forget is probe-only).

**Coded init commands (current)**

`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML…` / `Mf…`, `AA…`, `Pt`.

**Build:** `target/PactorRATT_Alpha.jar` (shaded). Local Maven may be under `.tools/` if system `mvn` is missing.

### Phase 5 — Pactor flows (in progress)

#### Host data channel (foundation for chat / with-text)

- [`HostFrameCodec`](src/main/java/com/pactorratt/alpha/hostmode/HostFrameCodec.java): `encodeData(channel, payload)` → CTL `0x2x`; `isDataAck` for CTL `0x5F … $00`; `MAX_HOST_TO_TNC_PAYLOAD = 330` (Ch. 4 §4.8).
- [`HostSession.sendData`](src/main/java/com/pactorratt/alpha/hostmode/HostSession.java): frames ch0 data, **chunks payloads &gt;330 pre-escape bytes**, waits for data-ack per block (HPOLL already OFF; no `GG` required); concurrent sends serialized on `dataSendLock`.
- Compose → App TX buffer while IRS; ARQ **Flush ISS** drains buffer to Host `sendData` + grey transcript; further ISS commits send immediately. Listen **FEC / End TX**: `PD`+`n,x` from Program settings (`fec200` / `fecRetries`, default `PD1,1`) → buffer as ch0 data (same §4.8 chunking) → CTRL-D (`$04`) end. With-text buttons share the same chunked `sendData` path.

#### Inbound frame demux + transcript (done)

- Typed [`HostEvent`](src/main/java/com/pactorratt/alpha/hostmode/HostEvent.java): `COMMAND_RESPONSE`, `DATA_ACK_OR_STATUS`, `INBOUND_DATA`, `ECHO`, `LINK_STATUS`, `LINK_MESSAGE`, etc.
- [`HostFrameCodec.classifyCtl`](src/main/java/com/pactorratt/alpha/hostmode/HostFrameCodec.java) / `isInboundDataCtl`: **all** CTL `0x30`–`0x3F` → `INBOUND_DATA` (no separate monitor path).
- [`HostSession.dispatchFrame`](src/main/java/com/pactorratt/alpha/hostmode/HostSession.java): `0x4F` → `commandQueue`; `0x5F` → `statusQueue`; other types fire events only.
- `drainInbound()` clears **waiter queues only** — async inbound text is no longer discarded by command waits.
- [`AppController`](src/main/java/com/pactorratt/alpha/app/AppController.java) attaches `hostEventListener` on successful TNC connect; `INBOUND_DATA` → `ConnectionWindow.appendRemoteText` on **active ARQ else active Listen** (ARQ wins; no dual-append).
- Capture ground truth: PTL monitor blocks are `01 3F … 17` with payload like `>KO6IZ <C>\r` ([`docs/Hardware_Capture_Sample.md`](docs/Hardware_Capture_Sample.md)).

#### Debug Monitor (updated)

- Live TX/RX hex + ASCII; TX still per-write.
- **RX Host blocks coalesced** to one line per complete `SOH…ETB` (lone `01` was OS chunking, not a separate packet).
- Non-framed pre-Host ASCII/autobaud still shown as “loose” bytes; idle flush resets incomplete Host parse state.
- Manual **Cmd** + **Payload** Send → CTL `0x4F` **fire-and-forget** (probe-only exception to Ch. 4 command wait).
- Clear / Pause; 200k char cap; session retain-after-failure while monitor open unchanged.

#### ARQ connection-window controls (wired)

| UI control | Host action |
|---|---|
| Disc. after TX clear | CMD `RE` |
| HO after TX clear | CMD `PV` |
| Seize | CMD `AG` |
| Abort | Listen checkbox on → `PN`, else `Pt`; then `markArqDead` |
| HO with text | Canned handover → data ch0, then CMD `PV` |
| Disc. with text | Canned disconnect → data ch0, then CMD `RE` |
| Disconnect now | **Stubbed** (await `Rcve` / `RC` hardware confirm) |
| Handover now | **Stubbed** |

Canned strings in Program Settings (`cannedHandoverText` / `cannedDisconnectText`; defaults `KKK` / `SK`). Empty canned → skip data step, still send trailing `PV`/`RE`.

#### Main-window ARQ Connect (wired)

- Connect / buddy double-click → `requestConnect`.
- Host: `"PG" + callsign` (no space); leading `!` preserved (`PG!N7ML`).
- Off-EDT `sendCommand`; **wait for command ACK** (`status 0x00`).
- **Only on ACK OK** → open ARQ window.
- On fail/timeout: error dialog; **no** ARQ window.
- Call-timeout / “did not answer” after ACK: **stubbed** (TODO in code).
- Guard: `arqConnectBusy`; timeout `ARQ_HOST_TIMEOUT_MS` (user may have adjusted).

#### Buddies file location

- Path: **`config/buddies.json`** via `ConfigStore.buddiesFile()` / `ensureBuddiesFile()`.
- If missing: create defaults `N0CALL`, `KJ7RBS`. Does not overwrite existing.

#### Still UI-only / not Host-wired in Phase 5

- Main **Listen** toggle creates/destroys Listen window only — does **not** yet send `PN` / return `Pt` (needed for real PTL monitor traffic into transcript).
- Listen-window **FEC / End TX** wired (`PD` + chunked data + CTRL-D). Listen toggle still does **not** send `PN` / `Pt`.
- No OPMODE / link-status / TX-empty / Heard-Mentioned parsers yet (grey→green deferred).

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
- Revisit whether `RE`/`PV` Host CMDs vs embedded CTRL-D/CTRL-Z is correct on hardware

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

**Hardware debug tip:** Open **TNC → Debug Monitor…** first, then **TNC → Connect**. Host RX lines should show full `01 3F … 17` blocks (coalesced). With Listen window active and TNC in PTL, transcript should show `>CALL <C>`-style text. On TNC-init failure the port stays open while the monitor remains open.

---

## Recommended next steps (implementation order)

### Finish Phase 5 — Pactor flows

1. Wire **Listen** toggle: on → `PN` + Listen window; off → `Pt` + destroy Listen window (respect ARQ-active inactive Listen window). This unlocks real PTL inbound into the demuxed transcript path.
2. ~~Wire Listen **FEC / End TX**~~ **done** (`PD` + chunked ch0 data + CTRL-D `$04`).
3. ~~Wire **ISS chat flush**~~ **done** (ARQ Flush ISS / ISS commits → `sendData`; §4.8 chunking in `sendData`).
4. ~~**Implement Ch. 4 §4.8 max block size**~~ **done** (≤330 payload chars per block inside `sendData`).
5. Implement **call-timeout / did not answer** after `PG` ACK (close ARQ + popup) once status signals are known — do not invent OPMODE bytes.
6. Keep Disconnect now / Handover now stubbed until confirmed.
7. Hardware-validate Alpha choice of Host CMD `RE`/`PV` vs embedding CTRL-D/CTRL-Z; FEC end uses embedded CTRL-D per PTSend docs.

### Phase 6 — Status + confirmation

1. OPMODE / status / link-message parsing into status bar + ticker (`LINK_STATUS` / `LINK_MESSAGE` events already demuxed).  
2. TX-empty + idle → flip grey transcript to green.  
3. Incoming ARQ string → open ARQ window.  
4. Heard (` de`) / Mentioned parsers when samples exist (can scrape Listen transcript / monitor lines).

### Optional / polish

- Further Host entry robustness at odd COM settings if needed.  
- Git commit checkpoint when you want one (user-driven).

---

## Source map (quick)

```text
app/
  PactorRattAlphaApp.java   entry
  AppController.java        modes, windows, connectTnc/disconnectTnc,
                            requestConnect (PG), arq* Host actions,
                            sendOutboundChat (ISS), listenFecEndTx (PD+data+CTRL-D),
                            HostEvent INBOUND_DATA → transcript,
                            debug/compat/startup dialogs, session retain
hostmode/
  HostFrameCodec.java       SOH/CTL/ETB + DLE; encodeData; classifyCtl;
                            MAX_HOST_TO_TNC_PAYLOAD (330); FrameParser
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
  ConnectionWindow.java     chat UI + ARQ controls + appendRemoteText
  DebugMonitorWindow.java   coalesced RX Host lines + Cmd/Payload Send
  ComPortDialog.java        COM settings
config/
  AppConfig, ConfigStore    settings.json + buddies.json under config/
util/                       DebugLog
docs/Alpha_Init_Sequence.md canonical init + Host encoding + command pacing
docs/Ch. 4 hostmode         Chapter 4 source
docs/Hardware_Capture_Sample.md  PTL RX samples
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS per-char coloring, Morse-ID disconnect, auto-AAB, Spring/DI frameworks, generic TNC abstraction layers.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md` and `docs/Alpha_Init_Sequence.md`. Phase 1 UI and Phase 3+4 TNC init are done and hardware-validated. Phase 5: Host `sendData` with Ch. 4 §4.8 330-char chunking; ARQ controls (`RE`/`PV`/`AG`/`PN`/`Pt`, with-text); Connect (`PG`+callsign, ACK then ARQ window); ISS App TX flush (ARQ); Listen FEC/End TX (`PD` + data + CTRL-D); inbound demux (all CTL `0x30`–`0x3F` → `INBOUND_DATA` → active Listen/ARQ transcript); Debug Monitor coalesces RX Host blocks; command pacing Ch. 4 §4.3 (wait for each command response). Buddies in `config/buddies.json`. **Not done:** Listen toggle → `PN`/`Pt`, call-timeout/did-not-answer, grey→green. Do not invent OPMODE/TX-empty/Rcve; leave Disconnect now / Handover now stubbed.
