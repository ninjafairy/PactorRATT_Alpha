# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-08-26  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 3+4 TNC init **hardware-validated**; Phase 5 outbound **done**; Listen `PN`/`Pt` + OPMODE Status Monitor **in**; **long-uptime UI hang fixed** (Build 11+, instrumentation stripped in **Build 13**); portable I/O is **`config/` beside the jar**; linked-ARQ / PTSend OPMODE captures, grey→green, call-timeout still open  
**License:** AGPL-3.0  
**Support contact (compat popups):** KJ7RBS@gmail.com  
**Last packaged jar:** `target/PactorRATT_Alpha.jar` copied to `Builds/Most Recent Build/PactorRATT_Alpha.jar` — warning line `Build: N  {date time}` (sequential `N` in `build.number.properties`; last package was **build 16**)

---

## What this project is

A portable **Java 21 + Swing** desktop chat program that drives a **PK-232 with Pactor firmware** in **Host Mode**. The TNC owns ARQ; the app is a structured AIM 3.x–inspired terminal with chat, status, and control actions separated.

**Normative docs (read in this order when resuming):**

1. [`PtRa_specification.md`](PtRa_specification.md) — full product/program specification  
2. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — software architecture (includes `hostIoLock` / §4.3–§4.4 pacing)  
3. This file — detailed “where we left off” summary  
4. [`docs/OPmodeResponse.md`](docs/OPmodeResponse.md) — OPMODE table + **hardware Pactor `PN`/`Pt` captures** (not AMTOR)  
5. [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — ordered TNC connect steps (hardware-updated)  
6. Other refs under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4 hostmode, Compat map, Pactor chapter, hardware capture)

---

## What we have accomplished

### Design & documentation (locked / updated)

- Full requirements: modes (Idle / Listen / Unproto-as-UI-“FEC” / ARQ), windows, IRS/ISS send pipeline, control map, settings, non-goals.
- Spec: [`PtRa_specification.md`](PtRa_specification.md) — includes **§9.2 Command / response pacing** (Ch. 4 §4.3) and **§4.8 / §8.5 data block limits**.
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — Host I/O includes command-wait, data-ack wait, and single `hostIoLock`.
- Reference imports under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4, Hardware capture, Pactor chapter, Compat map, Alpha init, **OPmodeResponse**).

### Key product decisions (do not re-litigate casually)

| Topic | Decision |
|---|---|
| Stack | Java 21, Swing, jSerialComm, Maven uberjar, one process |
| Air mode | Pactor only |
| Host I/O | `HPOLL OFF` (`HPN`) in steady state; `GG` for entry/recovery only; **wait for each command response** (Ch. 4 §4.3); **wait for each data-ack** (Ch. 4 §4.4); all round-trips on **`hostIoLock`** |
| UI “FEC” | Label only; command is `PTSend` / unproto (`PD` + `n,x`) |
| FEC params | Program settings: **FEC 200** checkbox → `n=2` else `n=1`; **Retries** spinner 1–5 → `x` (defaults `fec200=false`, `fecRetries=1` → Host `PD1,1`) |
| FEC end TX | Embedded CTRL-D (`$04`) in Host data after buffer — **not** Host CMD `RE` |
| After FEC / CTRL-D | TNC **always** returns to **Pactor standby (`Pt`)**, not Listen (`PN`). Documented in Ch. 11. **No PK-232 setting** restores Listen. App must re-issue `PN` if Listen should continue (not wired yet — wait for TX-empty). |
| Callsign | One config value → `ML` and `Mf` |
| Idle | `Pt` (case-sensitive) |
| Listen ON | Show Listen window first. Worker: query `OP`. If `Pt` → send `PN`. If already `PN` → leave it. **Any other OPMODE / Host fail → refuse** (close window, uncheck, warn). Offline / no TNC: UI only. |
| Listen OFF / Listen window closed | Query `OP`. If `PN` → send `Pt`. Otherwise leave TNC alone. |
| Listen vs ARQ | While an ARQ window is **active**, **never** send `PN` or `Pt` (inactive Listen window UI only). |
| Abort | Listen checkbox on → `PN`; else → `Pt`; then `markArqDead` |
| Clean disconnect / handover | Embed CTRL-D (`$04`) / CTRL-Z (`$1A`) in Host **ch0 data** (`$01 $20 … $17`). Do **not** send Host CMD `RE` / `PV` (those only *set* the characters). Same-block for with-text. |
| Disconnect now | Host `TC` (TClear), wait ACK, then ch0 `$04`. Not `RC`/`Rcve`. |
| Handover now | Same ch0 `$1A` as HO after TX clear (both kept). HO / HO after TX clear / HO with text **lock** until OPMODE IRS then ISS again. |
| Seize | `AG` (`AChg`) |
| Inbound text CTLs | Treat **all** `0x30`–`0x3F` the same (Pactor channel 0 only; hardware PTL uses `0x3F`) |
| EAS | OFF (`EAN`); grey→green later via TX-empty/idle (not EAS) |
| Defaults | `PBY` (PT200), `PH0` (PTHUFF off), `WON` (WORDOUT off) |
| Portable I/O | **All program files under `{jarDir}/config/`** — not `logs/`, not temp, not the GitHub tree. `PactorRattAlphaApp.resolvePortableRoot()` = folder containing the running jar (IDE fallback: `user.dir`). Settings `config/settings.json`, buddies `config/buddies.json`, optional debug log `config/debug-YYYYMMDD-HHMMSS.log`. Save-chat still uses a user-chosen path. |
| Serial I/O vs UI | `SerialPortService.isOpen()` is a **volatile flag** — never call jSerialComm from the EDT. Native `readBytes`/`writeBytes` **do not** hold `ioLock`. Do **not** re-introduce `synchronized` on `isOpen()` or hold the service lock across a blocking COM read. |
| Listen ON UI | Open the Listen window **immediately**, then query `OP` / send `PN` on a worker. If OPMODE is not `Pt`/`PN`, close the window, uncheck Listen, warn. |
| Main-window Connect UI | Open the ARQ window **immediately**, then send `PG`+callsign on a worker. On fail/timeout, close the window and show an error. |
| COM default | **1200 7N1** (user-selectable; no forced 8N1 on open) |
| Compat | Supported v7.x continue; listed pre-v7 **hard refuse**; HK/UDC/unknown **warn + email + continue** |
| UI Connect | **TNC → Connect/Disconnect** = serial/Host session; main-window **Connect** = ARQ `PG` only |
| Long path | User may type `!CALL`; no dedicated checkbox |
| Startup gate | Blocking experimental warning; shows Java + `Build: N  {date time}` |
| OPMODE identify | CTL `$4F` + payload starting `OP` |
| Pactor OPMODE | **Not AMTOR.** Hardware: Listen = `OP PN w x` + 4 trailer bytes; Standby = `OP Pt $30 x` + 4 trailer bytes. See [`docs/OPmodeResponse.md`](docs/OPmodeResponse.md). |
| Field *x* | `S` = Tx / ISS, `R` = Rx / IRS — applies to **every** mode that includes *x* (Status Monitor + ARQ ISS/IRS). |
| Field *w* | `$30` Standby … `$37` Sync. Used on AMTOR ARQ/Listen/FEC/SELFEC and **Pactor Listen**. **Not** FAX. |
| Field *v* | **FAX only**; meaning TBD. Do not decode FAX *v* with the *w* table. |
| `Pt` `$30` | Fixed Pactor-standby **marker**, not the *w* sequence. |
| Mystery trailer | Last 4 payload bytes before ETB on Pactor OPMODE; Status Monitor shows `mystery bytes: HH HH HH HH`. Do not treat as *w*. (Note in OPmodeResponse: byte 3 of trailer `$30`↔`$31` with longpath — not decoded in code yet.) |
| OPPOLL | Program setting **0–10** (default **0** = off). While TNC connected and ARQ window **linked and not dead**, send Host `OP` that many times/second (`hostIoLock`, skip-if-busy). Status Monitor is display-only and does **not** poll. |
| ARQ Standby → dead | After a **live** (non-standby) OPMODE has been seen, `Pt` or *w*=Standby marks the ARQ window dead, stops OPPOLL, freezes ISS/IRS. Early Standby right after `PG` does **not** kill the window. |
| Linked ARQ / PTSend OPMODE | **Not captured yet — do not invent tags.** Unknown tags decode as `Unknown (xx)` and do not drive ISS/IRS. |
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs |

### Phase 1 — Offline UI shell (done)

| Package | Role |
|---|---|
| `app` | Entry, `AppController`, `AppMode`, TNC + ARQ Host actions, inbound listener |
| `ui` | Main / connection windows, settings, Debug + Status Monitor, startup warning, `WrapLayout` |
| `config` | Portable `config/settings.json` + `config/buddies.json` |
| `util` | Per-launch debug log under **`config/`** (when enabled in Program settings) |
| `hostmode` | Framing, demux, session, compat, init, data send, **OPMODE parse** |
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
- Coded init, then `tncConnected = true`. After success, if Listen is already checked, same Listen-ON Host rule (`OP` then `PN` if `Pt`).
- **TNC → Disconnect** closes session / aborts in-flight connect.
- If connect **fails** while **Debug or Status Monitor** is open, serial session is **kept** until both monitors close (or Disconnect).

**Hardware-verified Host encoding rules** (critical)

- **No space** between mnemonic and argument (`HPN`, `AE6`, `MLCALL`, `PGN7ML`, `PD2,3`, not `HP N` / `PG N7ML`).
- Boolean switches: prefer `Y`/`N` (`HPN`); `HPOFF` also works without space.
- Integers where required: PTHUFF is Host **`PH`** (not `pH`) with level `PH0` for off.
- `AE` uses **decimal** address digits: `$0006` → `AE6`.
- `MM` **read** response is `MM$hh` (ASCII hex after `$`), e.g. `MM$93` → `0x93` — not binary status+data.
- Command ack status `0x01` is DLE-escaped as `10 01` on the wire (SOH must be escaped).
- **Command pacing (Ch. 4 §4.3):** wait for each command response before another command (`sendCommand`; Debug Monitor F&F is probe-only). **OPMODE replies are not ACK `$00`** — never use `sendHostOk("OP")`; use `sendCommand("OP")` and parse the frame.
- **Data pacing (Ch. 4 §4.4):** wait for `$5F … $00` after each data block before more data.

**Coded init commands (current)**

`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML…` / `Mf…`, `AA…`, `Pt`.

**Build:** `target/PactorRATT_Alpha.jar` (shaded). Manifest + `build-info.properties` include `Build-Time` and `Build-Number`. Maven **initialize** increments `build.number.properties` then packages. Local Maven may be under `.tools/` if system `mvn` is missing.

```powershell
.\.tools\apache-maven-3.9.6\bin\mvn.cmd -q package
Copy-Item -Force target\PactorRATT_Alpha.jar "Builds\Most Recent Build\PactorRATT_Alpha.jar"
```

### Phase 5 — Pactor flows (outbound done; Listen Host now wired)

#### Host data channel + §4.4 / §4.8 (done)

- [`HostFrameCodec`](src/main/java/com/pactorratt/alpha/hostmode/HostFrameCodec.java): `encodeData` → CTL `0x2x`; `isDataAck`; `isDataStatusError`; `MAX_HOST_TO_TNC_PAYLOAD = 330`.
- [`HostSession.sendData`](src/main/java/com/pactorratt/alpha/hostmode/HostSession.java): chunks &gt;330; wait data-ack per block; fail-fast on `$5F…W/Y`; timeout clears `statusQueue` before unlock.
- **`hostIoLock`:** serializes `sendCommand`, `sendData`, `probeOgg`, `readMemoryByte`, Debug F&F writes, **OPPOLL**, Listen `OP`/`PN`/`Pt`.
- Text → Host bytes: `\n`→`\r`, trailing CR if missing (`AppController.toHostDataBytes`).

#### ARQ App TX → TNC / ISS flush (done; now also OPMODE-driven)

- While **IRS**: commits go to App TX buffer only (not transcript).
- ARQ **Flush ISS**: buffer → grey transcript + `sendData` ch0; flips local role to ISS.
- While **ISS**: further commits → grey transcript + immediate `sendData`.
- **OPMODE *x*:** `S` → ISS (auto-flush App TX on IRS→ISS); `R` → IRS (hold). Same *x* table for every mode that includes *x*.
- ARQ status slot that used to say `DEAD`/`ARQ` shows **last *w* word** when known (Idle/Traffic/Standby/…).
- Listen window has **no** ISS flush control (FEC is the Listen send path).

#### Listen FEC / End TX (done)

- Button **FEC / End TX** (Listen only): requires non-empty App TX buffer.
- Flow: grey transcript + clear buffer → Host `PD`+`n,x` → ch0 data + CTRL-D `$04` → UI mode FEC then back to Listen/Idle **label**.
- TNC air mode after CTRL-D is **`Pt`**, not `PN`. App does **not** re-send `PN` after FEC yet (waiting on TX-empty).
- Program Settings: **FEC 200** + **Retries** 1–5; `fec200` / `fecRetries` in `settings.json`.

#### Listen toggle Host `PN` / `Pt` (done 2026-08-20; window-first 2026-08-21)

- Main **Listen** checkbox ON: `applyListenUiOn()` **first** (window appears immediately), then worker `enterListenHostThenUi()` — `OP` then `PN` if `Pt`; already `PN` OK; other modes / Host timeout → `refuseListenOn` (dispose Listen window, uncheck, warn).
- Listen OFF or Listen window close: `leaveListenHostIfPn()` — `OP` then `Pt` if `PN`.
- After TNC Connect success with Listen already checked (incl. listen-on-start): same ON path (init always lands on `Pt`).
- No Host I/O while ARQ is active.

#### Inbound frame demux + transcript (done)

- All CTL `0x30`–`0x3F` → `INBOUND_DATA` → active **ARQ else Listen** transcript.
- `0x4F` → `commandQueue` **and** `HostEvent` (OPMODE decode for Status Monitor + ARQ).
- `0x5F` → `statusQueue`; other types event-only.
- PTL samples: `01 3F … 17` ([`docs/Hardware_Capture_Sample.md`](docs/Hardware_Capture_Sample.md)).

#### Debug Monitor + Status Monitor (done)

- **TNC → Debug Monitor…** — live TX/RX hex+ASCII; RX coalesced per `SOH…ETB`. **Hides** complete OPMODE frames (`$4F` + `OP…`).
- **TNC → Status Monitor…** — **only** OPMODE TX polls and RX replies, same hex/ASCII format; **Mode:** line from `OpmodeParser.Decoded.statusLine()` (mode, *w*, Tx/Rx, Morse WPM, `mystery bytes:` trailer).
- Manual Debug Cmd+Payload → `0x4F` fire-and-forget.
- Failed connect: keep serial while either monitor remains open.

#### OPMODE parser + OPPOLL (done 2026-08-19/20)

- [`OpmodeParser`](src/main/java/com/pactorratt/alpha/hostmode/OpmodeParser.java) — Ch.4 tags plus hardware **`PN` / `Pt`**. AMTOR `AM`/`AC`/`AL`/`FE` are **not** used as Pactor stand-ins.
- Program Settings **OPPOLL** 0–10; `opPoll` in `settings.json`.
- `AppController` scheduler: `OP` at OPPOLL Hz while ARQ linked; apply ISS/IRS + *w*; `Pt` / *w*=Standby after live OPMODE → `markArqDead`.

#### ARQ connection-window controls (wired)

| UI control | Host action |
|---|---|
| Disc. after TX clear | Flush App TX, then ch0 `$04` in the same block |
| Disconnect now | Host `TC`, wait ACK, then ch0 `$04` |
| Handover | ch0 data `$1A` (CTRL-Z); lock HO buttons until IRS then ISS again |
| HO after TX clear | Flush App TX, then ch0 `$1A` in the same block; same HO lock |
| Seize | CMD `AG` |
| Abort | Listen checkbox on → `PN`, else `Pt`; then `markArqDead` |
| HO with text | Canned handover + `$1A` in the same ch0 block; same HO lock |
| Disc. with text | Canned disconnect + `$04` in the same ch0 block |
| Flush ISS | App TX → `sendData` + grey; mark ISS |

Canned strings: `cannedHandoverText` / `cannedDisconnectText` (defaults `KKK` / `SK`).

#### Main-window ARQ Connect (wired; window-first 2026-08-21)

- Connect / buddy double-click → open ARQ window **immediately**, then worker `PG`+callsign (no space; `!` preserved).
- Command ACK `0x00` keeps the window; fail/timeout → `abortFailedConnect` (close window, restore Listen if checked, error dialog).
- Call-timeout / “did not answer” after ACK: **stubbed** (needs linked-ARQ OPMODE capture).

#### Buddies

- Path: **`config/buddies.json`**. Missing → create defaults `N0CALL`, `KJ7RBS` with **CRLF** (does not overwrite existing).

#### Startup warning (done; build number added)

- Modal blocking dialog: flashing **!!WARNING!!**, experimental copy, Java runtime, **`Build: N  {date time}`**, **Dont break my stuff** / **Risk it for the Biscuit**.

---

## 2026-08-21 — Long-uptime freeze (fixed; Build 13 clean)

**Symptom:** After the app had been open several hours (Listen on, TNC connected), the UI became extremely slow. Later, Status/Debug stayed snappy but Listen/ARQ would not open again: Status Monitor showed `OP` TX, no Listen window; Main-window Connect logged a click but no ARQ window; Exit still worked.

**What it was not:** unbounded Host queues, transcript growth, FrameParser buffer, WrapLayout viewport storms, Debug/Status document trim, Java heap GC. Health snapshots stayed ~15–40 MB with empty queues and tiny transcripts.

**Root cause (runtime stacks + NDJSON):**

1. **EDT vs serial lock.** `hostmode-reader` held `SerialPortService`’s monitor for the entire blocking `readBytes` (50 ms timeout, or hung USB-serial after hours). Clicks, Abort, and Listen close called `isOpen()` on the EDT and blocked for tens to hundreds of seconds. `ConnectionWindow.dispose()` itself was ~8–10 ms.
2. **Window creation waited on Host I/O.** Listen ON and Connect created the Swing window only *after* `sendCommand(OP/PG)` returned. After hours, `write()` waited on the same lock as the hung read, so OP/PG never finished and the window never appeared. Debug/Status do not wait on that round-trip, so they still opened.

**Fixes (keep these):**

- `SerialPortService`: `volatile boolean opened`; `isOpen()` does not call jSerialComm; snapshot the `SerialPort` under `ioLock` then `readBytes`/`writeBytes` **outside** the lock so a hung read cannot block Host TX.
- `HostSession.readerLoop`: no `synchronized (serial)` around the read.
- Listen ON: show window, then `OP`/`PN` on a worker; refuse path closes the window.
- Connect: show ARQ window, then `PG` on a worker; fail → `abortFailedConnect`.
- `FrameParser`: reset payload/raw after a complete `ETB` frame (health leftover, not the hang).

**Debug session leftover:** `AgentDbg`, `EdtWatch`, 15 s health sampler, and `config/debug-737444.log` ingest were **removed** in Build 13. Do not re-add them. Product debug log remains `config/debug-YYYYMMDD-HHMMSS.log` when enabled in Program settings.

**Testbed notes:** jar often copied to Downloads; Java **23** was used there. Portable root is the folder **containing the jar**, so `config/` appears next to that copy, not next to the GitHub tree.

---

## What is intentionally not done yet

- Re-issue **`PN` after FEC** when Listen is still on (TNC is `Pt`; wait for TX-empty/idle first)
- Grey→green confirmation (TX-empty + idle)
- Call-timeout / “did not answer” after successful `PG` ACK
- **Linked ARQ** and **PTSend/unproto** OPMODE wire captures (do not invent tags)
- Incoming ARQ detect → open ARQ window
- Heard / Mentioned parsers (transcript already gets raw lines)
- Settings → TNC large parameter editor
- Hardware-validate ch0 `$04`/`$1A` (and Disconnect now `TC`+`$04`) on a live link
- Decode mystery-trailer byte 3 longpath `$30`/`$31` (noted in OPmodeResponse; not coded)

---

## How to build & run (resume checklist)

```powershell
cd c:\Users\Jadon\Documents\GitHub\PactorRATT_Alpha

# Local Maven (typical on this machine):
.\.tools\apache-maven-3.9.6\bin\mvn.cmd -q package

# Copy for the usual launch path:
Copy-Item -Force target\PactorRATT_Alpha.jar "Builds\Most Recent Build\PactorRATT_Alpha.jar"

# Run.txt:
java --enable-native-access=ALL-UNNAMED -jar "C:\Users\Jadon\Documents\GitHub\PactorRATT_Alpha\Builds\Most Recent Build\PactorRATT_Alpha.jar"
```

Each `mvn package` increments `build.number.properties`. **`config/` is created beside the running jar**, not beside `user.dir` if those differ. **JDK 21+** (testbed has used 23).

**Hardware debug tip:** Open **TNC → Debug Monitor…** and/or **Status Monitor…**, then **TNC → Connect**. OPMODE frames (`01 4F 4F 50 … 17`) appear **only** in Status Monitor. Listen ON should move OPMODE from `Pt` to `PN` (window appears first; Host follows). After Listen FEC, expect `Pt` until we add post-TX `PN`. To leave Host Mode: Debug Cmd `HO` Payload `N`.

---

## Recommended next steps (implementation order)

1. **Capture OPMODE while linked ARQ (`PG`)** and while **PTSend (`PD`)** — add rows to [`docs/OPmodeResponse.md`](docs/OPmodeResponse.md) and `OpmodeParser` (ISS/IRS + *w* for the ARQ window currently cannot follow a live Pactor ARQ tag).
2. **After FEC TX-empty:** if Listen is still on and OPMODE is `Pt`, send `PN` (same rule as Listen ON). Needs a TX-empty/idle signal (Phase 6).
3. Call-timeout / did not answer after `PG` ACK — only after linked-ARQ OPMODE is known. (Window now opens before ACK; this is about *keeping* vs closing it when the far station never answers.)
4. Grey→green (TX-empty + idle).
5. Incoming ARQ → open ARQ window; Heard / Mentioned parsers when samples exist.
6. Hardware-validate ch0 `$04`/`$1A` and Disconnect now `TC`+`$04` on a live ARQ link.
7. Optional: refresh [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) portable layout (`config/` vs `logs/`).
8. Git commit when you want one (user-driven).

**Do not:** re-lock `SerialPortService.isOpen()` / native read on the UI path; do not wait for `OP`/`PG` before showing Listen/ARQ windows.

---

## Source map (quick)

```text
app/
  PactorRattAlphaApp.java   entry, portableRoot (jar folder), startup warning gate
  AppController.java        modes, windows, connectTnc/disconnectTnc,
                            Listen ON/OFF: window first then Host OP+PN / OP+Pt,
                            requestConnect: ARQ window first then PG,
                            abortFailedConnect, arq* Host actions (ch0 $04/$1A, TC+ $04, AG, Abort),
                            sendOutboundChat (ISS), listenFecEndTx (PD+data+CTRL-D),
                            OPPOLL scheduler, applyOpmodeDecoded,
                            HostEvent INBOUND_DATA + COMMAND_RESPONSE (OPMODE),
                            debug/status/compat/startup dialogs, session retain
hostmode/
  HostFrameCodec.java       SOH/CTL/ETB + DLE; encodeData; classifyCtl;
                            MAX_HOST_TO_TNC_PAYLOAD (330); isDataAck / isDataStatusError;
                            FrameParser (reset buffers on ETB)
  HostSession.java          demux, commandQueue/statusQueue, sendCommand,
                            sendData (chunk + data-ack), hostIoLock round-trips,
                            readerLoop (no lock across serial.read),
                            OGG, AE/MM$hh
  OpmodeParser.java         $4F+OP detect; Ch.4 + Pactor PN/Pt decode; *w*/*x*;
                            mystery trailer; statusLine()
  HostEvent.java            typed demux events
  TncInitializer.java       full connect/init orchestration
  CompatChecker.java        fingerprint policy
serial/
  SerialPortService.java    jSerialComm; volatile opened; native I/O off ioLock
config/
  AppConfig, ConfigStore    {jarDir}/config/settings.json (opPoll, fec200, fecRetries)
                            + config/buddies.json (CRLF defaults)
ui/
  MainWindow.java           tree, ARQ Connect, Listen toggle, TNC menu
                            (Debug Monitor + Status Monitor)
  ConnectionWindow.java     chat UI + ARQ controls + FEC/End TX + *w* status slot
                            + OPMODE-driven ISS/IRS
  DebugMonitorWindow.java   coalesced RX; hides OPMODE frames
  StatusMonitorWindow.java  OPMODE-only stream + Mode: line
  ProgramSettingsDialog.java  commit mode, canned text, FEC 200/Retries, OPPOLL
  StartupWarningDialog.java warning + Java + Build: N  datetime
util/
  DebugLog.java             optional config/debug-YYYYMMDD-HHMMSS.log
docs/OPmodeResponse.md      OPMODE table + Pactor hardware captures
docs/Alpha_Init_Sequence.md canonical init + Host encoding + pacing
docs/Ch. 4 hostmode         Chapter 4 source
docs/Hardware_Capture_Sample.md  PTL RX samples
build.number.properties     sequential build N (Maven initialize)
tools/IncrementBuild.java   bumps build.number.properties
Run.txt                     launch Most Recent Build jar
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS/per-char confirm coloring, Morse-ID disconnect, auto-`AAB`, Spring/DI frameworks, generic TNC abstraction layers.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md`, `docs/OPmodeResponse.md`, and `docs/Alpha_Init_Sequence.md`. Phase 1 UI and Phase 3+4 TNC init are hardware-validated. Phase 5 outbound is done (`sendData` 330-chunk, `hostIoLock`, ARQ ch0 `$04`/`$1A` / `AG` / Abort / with-text, ISS flush, Listen FEC `PD`+data+CTRL-D). **ARQ end/HO:** Disc. after TX clear = flush App TX then ch0 `$04`; HO after TX clear = flush App TX then ch0 `$1A`; Handover = ch0 `$1A` only; with-text appends the control byte in the same ch0 block; Disconnect now = `TC` ACK then ch0 `$04`. Do **not** send Host `RE`/`PV` to trigger those. HO / HO after TX clear / HO with text lock until OPMODE IRS then ISS again (2 Hz `OP` watch if OPPOLL is 0). **Portable I/O:** `{jarDir}/config/` only (`settings.json`, `buddies.json`, optional debug log). **Long-uptime hang is fixed (Build 13):** do not lock `SerialPortService.isOpen()` or native COM read on the EDT; Listen/ARQ windows open immediately, then `OP`/`PN` or `PG` on a worker (fail closes the window). **Listen Host:** ON is `Pt`→`PN` (already `PN` OK; other modes refuse); OFF / Listen close is `PN`→`Pt`; no `PN`/`Pt` while ARQ is active. **OPMODE:** Pactor is **not** AMTOR — hardware tags `PN` / `Pt` (see OPmodeResponse.md); Status Monitor shows only `$4F`+`OP…` plus `Mode:`; Debug Monitor hides those frames; OPPOLL 0–10 polls `OP` while ARQ is linked; *x* drives ISS/IRS. After Listen FEC the TNC returns to **`Pt`**; do **not** auto-`PN` until TX-empty is known. Last packaged **Build 16** in `Builds/Most Recent Build/`. **Next:** capture linked-ARQ and PTSend OPMODE bytes (do not invent); then post-FEC `PN` restore; call-timeout after `PG`; grey→green. Hardware-validate `$04`/`$1A` / `TC` on a live link.
