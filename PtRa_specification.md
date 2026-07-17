# PactorRATT_Alpha (PtRa) — Program Specification

**Document:** `PtRa_specification.md`  
**Product stylized name:** PactorRATT_Alpha  
**Short name (if needed):** PtR_Alpha / PtRa  
**Status:** Draft final (locked decisions + explicit stubs)  
**Audience:** Solo developer implementing and extending the Alpha release  
**Companion docs:** `docs/PK232_HostMode_Reference.md`, `docs/HostCommands - Trimmed.md`, `docs/Pactor_Chapter.md`, `docs/Compat_Memory_Map.md`

---

## 1. Purpose and mission

### 1.1 One-sentence mission

PactorRATT_Alpha provides a clean, AIM 3.x–inspired chat interface that drives a single PK-232 (Pactor firmware) in Host Mode so two amateur stations can exchange line-oriented text over a Pactor link, while keeping chat text, link status, and control actions visually and logically separated.

### 1.2 What this program is

- A **structured Host Mode terminal** specialized for Pactor chat.
- A **single-user desktop GUI** that controls connect, listen, unproto (“FEC” in the UI), handover, seize, and disconnect behaviors.
- Software that **interfaces** with the TNC; the TNC performs ARQ, retries, and RF framing.

### 1.3 What this program is not

The TNC is not replaced. The app does not implement Pactor on the wire. It does not add an in-app ACK/ID protocol on top of Pactor text for Alpha.

### 1.4 Success test (Alpha)

Station A and Station B each run PactorRATT_Alpha on a supported PK-232. A configures callsign and COM, connects to B (or accepts inbound). Typed lines flow according to IRS/ISS rules. Transcript shows remote text in black and local outbound as grey then green when confirmation is known. Listen/FEC paths work. Chat can be saved. Debug Host I/O can be logged. The uberjar runs portably on Windows 10+, macOS, and Linux with Java 21.

---

## 2. Scope

### 2.1 In scope (Alpha)

| Area | Requirement |
|---|---|
| Hardware | One PK-232-class TNC with Pactor firmware, user-selected COM port |
| Air mode | Pactor only (ARQ, Listen/`PTList`, Unproto/`PTSend`) |
| Host protocol | PK-232 Host Mode framing and commands |
| UI | Swing, AIM 3.x spirit, main + connection windows |
| Chat | Line-oriented commit; IRS App TX buffer; ISS flush; combined transcript |
| Identity | One configured callsign written to both `MYCALL` and `MYPTCALL` |
| Compat | Fingerprint check at `$0006..$0009` per `docs/Compat_Memory_Map.md` |
| Packaging | Maven uberjar in a portable folder |
| License | AGPL-3.0 |
| Offline use | Full GUI without TNC; TNC actions gated |

### 2.2 Explicitly out of scope

- File transfer
- Multi-user / conference beyond one ARQ pair
- Store-and-forward, BBS, MailDrop, Winlink
- Encryption / obfuscation of content
- Logging to a network service
- Mobile apps
- Non-PK-232 TNCs / generic TNC abstraction layer
- EAS-driven per-character color confirmation (Alpha uses EAS off)
- Morse-ID disconnect (`CTRL-F`)
- Automatic `AAB` programming
- Spring or other heavy DI frameworks
- Multi-module Maven reactor (single module Alpha)

### 2.3 Deferred (known stubs — do not invent protocol bytes)

| Stub | Reason |
|---|---|
| OPMODE / detailed status-bar field parsing | Docs deferred |
| Host link-block formats for incoming ARQ string | To be captured from hardware |
| TNC TX-empty + idle signal for grey→green | To be figured out later |
| `Rcve` (`RC`) as “disconnect immediately” | Must be confirmed manually on Pactor |
| Mentioned-list regex beyond planned patterns | Need monitor samples |
| Settings → TNC large parameter editor | Phase 2 growth |
| Long-path Connect UI toggle | User may type `!CALL` manually |

---

## 3. Users, regulatory, and identity

### 3.1 Intended users

Licensed amateur radio operators using PK-232 Pactor over HF (or compatible RF setup). Solo operator per computer.

### 3.2 Regulatory posture

- Amateur-only product assumption.
- No encryption.
- Station identification is the operator’s responsibility on air; the app sets callsign into the TNC and may display it.
- App does not enforce content rules beyond refusing encrypted-mode features.

### 3.3 Callsign rules

- **Local callsign:** single persistent Program/config setting (no SSID required in Alpha config UI).
- On TNC init, write that value to:
  - `MYCALL` — Host mnemonic `ML`
  - `MYPTCALL` — Host mnemonic `Mf`
- **Remote callsign:** typed in main window field, or chosen from Buddy / Heard / Mentioned lists; used with `PTConn` (`PG`).
- Long path: user may prefix remote call with `!` (e.g. `!N7ML`); no dedicated long-path checkbox in Alpha.

---

## 4. Technology stack and distribution

| Item | Choice |
|---|---|
| Language | Java 21 |
| UI | Swing |
| Serial | jSerialComm |
| Build | Maven, single module, shaded/uber JAR |
| Process model | One JVM process |
| Platforms | Windows 10+, macOS, Linux |
| Look | Late-90s AIM 3.x inspired (not a clone of AOL services) |

### 4.1 Portable release layout

```text
<portable-root>/
  PactorRATT_Alpha.jar
  config/                 # settings files
  buddies.json            # or under config/
  logs/
    debug-<timestamp>.log
  docs/                   # optional
```

All user-writable state lives beside the jar (portable). No mandatory install to Program Files / Applications.

### 4.2 License

AGPL-3.0 for the project. Replace the current LICENSE file content when implementing.

### 4.3 Support contact

Compat and unknown-fingerprint dialogs instruct the user to email: **KJ7RBS@gmail.com**

---

## 5. High-level architecture

### 5.1 Layered packages (single module)

```text
app        — lifecycle, mode coordinator, window wiring
ui         — Swing windows/dialogs/status (EDT only)
hostmode   — Host framing, commands, events, init, compat, outbound drain
serial     — jSerialComm wrapper (used only by hostmode)
config     — load/save portable settings, buddies, UI persistence
util       — debug logging helpers
```

Rules:

- `ui` must not open the serial port.
- `ui` talks to `app` / controller APIs, not raw Host bytes.
- `hostmode` owns the TNC session.
- No generic “any TNC” interface; PK-232 Host Mode only.

### 5.2 Threading

| Thread | Role |
|---|---|
| EDT (Swing) | All UI create/update |
| Serial reader | Read bytes, feed frame parser, emit Host events |
| Optional host worker | Command timeouts / sequenced init (if needed) |

All UI updates from Host events use `SwingUtilities.invokeLater`. Never block the EDT on serial I/O.

### 5.3 Core runtime objects (logical)

- `App` / mode coordinator — Idle, Listen, Unproto(FEC), ARQ
- `MainWindow`
- `ConnectionWindow` (Listen instance or ARQ instance)
- `HostSession` — port + parser + send API
- `PactorController` — Pactor-specific commands and actions
- `CompatChecker`
- `OutboundPipeline` — compose commit, App TX buffer, flush, grey/green transcript coordination
- `ConfigStore`, `BuddyStore`, `DebugLog`

---

## 6. Application modes

Exactly one **active air mode** at a time:

| Mode ID | UI label | TNC command / posture | Notes |
|---|---|---|---|
| `IDLE` | Idle | `Pt` (Pactor standby) | Accepts inbound ARQ; does **not** monitor third-party traffic |
| `LISTEN` | Listen | `PN` (`PTList`) | Monitor ARQ/unproto traffic into Listen window |
| `UNPROTO` | **FEC** (UI) | `PD` (`PTSend`) | Simplex TX; cannot receive ARQ while transmitting |
| `ARQ` | ARQ | Linked via `PG` (`PTConn`) or inbound | One active link max |

**Important naming rule:** The UI may say **FEC**; the implementation must use **`PTSend` / unproto**, not AMTOR `FEC` (`FE`).

### 6.1 Mode transitions

1. App start, no TNC → UI-only; mode display may show disconnected.
2. TNC open + init success → enter `IDLE` (`Pt`).
3. Listen on → `LISTEN`; create Listen window.
4. Listen off → destroy Listen window; return `IDLE` (`Pt`).
5. Send from Listen window → `UNPROTO` (`PTSend`); when TX complete and returned to receive/listen, back to `LISTEN`.
6. Outbound Connect or inbound ARQ → `ARQ`; create/focus ARQ window.
7. If Listen was on during ARQ: Listen window remains open but **inactive** (no data pipe, compose disabled).
8. ARQ ends:
   - If Listen still enabled → return `LISTEN`, reactivate Listen window.
   - Else → `IDLE` (`Pt`).
9. Abort from ARQ:
   - If Listen enabled → `PN` (`PTList`).
   - Else → `Pt`.

---

## 7. Windows and UI specification

### 7.1 Visual direction

- AIM 3.x classic desktop feel (simple steel/gray era chat aesthetic is fine; not mandatory pixel-perfect clone).
- Not required: away messages, profiles, buddy-list servers, rich presence, sound packs beyond optional simple beep later.
- Separate **information/status** from **chat text** from **control buttons**.

### 7.2 Main window

**Required elements**

1. Menu bar  
   - **File → Exit**  
   - **Settings → COM Port…**  
   - **Settings → Program…**  
   - **Settings → TNC…** (Alpha: stub or minimal; large param UI later)  
   - **Help → About** (include contact email)
2. Mode / TNC status indicator (Idle / Listen / FEC / ARQ + connected flag)
3. Three collapsible list sections (user expand/collapse; **persist** state across launches):
   - **Buddies** — saved stations from local file
   - **Active callsigns heard** — parse monitored text for callsign after ` de` (raw; keep rare `-N` if seen)
   - **Callsigns mentioned** — “being called” patterns (exact patterns TBD from monitor samples)
4. Remote callsign text field
5. **Connect** button
6. **Listen** toggle (on/off)
7. Optional small area for messages/notices

**Connect behaviors**

- Enter callsign + Connect, **or** double-click a list entry → open new ARQ connection window and start `PTConn`.
- If TNC not connected: disable Connect **or** show error on click (prefer disable if simple).
- Refuse starting a second **active** ARQ; allow Connect while dead ARQ windows remain open.

### 7.3 Connection window (Listen or ARQ)

**Layout (top → bottom, approximate)**

1. Title showing remote callsign or “Listen”
2. **Transcript** — combined sent + received scrollback
3. **App TX buffer** panel — queued outbound while IRS (or waiting to flush)
4. **Compose** area + **Send** button
5. Control button row (ARQ; Listen may show subset)
6. **Status / information bar**

**Transcript**

- Keep **all** lines for the life of the window.
- Select All and Copy supported.
- On window close: **discard** unless user previously used Save chat.
- Save chat: dump transcript to a user-chosen file (end of session or anytime).
- Colors (transcript only):
  - **Grey** — local text accepted by TNC, not yet confirmed as fully sent/idle
  - **Green** — local text confirmed
  - **Black** — remote station text and all other transcript text

**Compose**

- Only exists on connection windows (not on main).
- Disabled when ARQ link has ended (dead window).
- Listen compose disabled while ARQ is active; re-enabled when back in Listen.
- On link loss (active → dead): compose becomes **read-only** (not editable) but still selectable/copyable; show non-modal notice + status update.

**App TX buffer**

- Holds committed lines waiting because local station is **IRS**.
- Not freely editable.
- **Right-click → Edit** (IRS queued lines only):
  1. Flush current compose into App TX buffer (append).
  2. Clear compose.
  3. Move entire App TX buffer contents back into compose.
  4. Clear App TX buffer.
- Once ISS flush begins, buffer contents are sent to TNC and appear in transcript (grey); Edit does not apply to in-flight/grey text.

### 7.4 Status bar fields (connection window)

Display when available from TNC status/events:

| Field | Notes |
|---|---|
| ISS / IRS | Local information sending/receiving role |
| TX ON / OFF | Transmitter keyed / not |
| FEC / ARQ / IDLE | App/TNC mode summary (UI may say FEC for unproto) |
| Link speed | 100 / 200 when known |
| Link quality | Derived later from speed + good/error counts when known |
| Retries | When known |
| Connected callsign | ARQ peer |
| Packet-type ticker | Last N reported types: connect, data, ack, idle, error, etc. |

Until OPMODE/link parsing exists, show placeholders or last-known raw status text without inventing decode logic.

### 7.5 Close / exit guards

**Closing an active ARQ window**

Modal choices:

- **Abort** — dirty leave link (Listen on → `PN`, else `Pt`)
- **Disconnect** — prefer clean path (after TX clear / `<CTRL-D>` as applicable); exact “immediate disconnect” deferred
- **Cancel** — do not close

**Closing Main while ARQ active**

Same Abort / Disconnect / Cancel dialog. On confirmed exit: tear down link per choice, close windows, quit.

**Closing Main with no active ARQ**

Close all connection windows (dead ones included) and exit. Unsaved transcripts are discarded unless previously saved.

---

## 8. Chat and outbound pipeline (normative)

### 8.1 Definitions

- **Compose:** text currently being edited by the user.
- **Commit:** move text from compose into the outbound pipeline (per commit mode).
- **App TX buffer:** UI/queue of committed lines waiting because local side is IRS.
- **TNC TX buffer:** bytes/characters held inside the PK-232 awaiting RF transmission.
- **Transcript:** durable (for window lifetime) display of confirmed/received and in-flight-grey local text.

### 8.2 Commit modes (Settings → Program)

**Line mode**

- Enter commits the current compose line into the App TX buffer (or straight to flush path if ISS).
- Clears that line from compose.

**Message mode**

- Enter inserts a newline in compose.
- **Send** commits the entire compose contents into the App TX buffer as lines, then clears compose.

### 8.3 IRS vs ISS behavior

**While local station is IRS**

- Committed text goes to **App TX buffer only**.
- Must **not** appear in the transcript yet.

**When becoming ISS / while ISS**

- Auto-drain: flush **entire** App TX buffer to the TNC as Host data blocks on channel 0.
- Immediately move that text into the transcript as **grey**.
- Additional commits while still sending append into the **same open grey outbound block** (not separate pending blocks).
- When the app later detects **TNC TX buffer empty** and **TNC idle** (mechanism TBD): recolor that grey block to **green**.
- Until that detector exists: leave text grey (acceptable Alpha stub).

**EAS**

- Alpha forces **EAS OFF**.
- Do not implement char-by-char EAS coloring in Alpha.
- Future: optional EAS path may refine confirmation; not required now.

### 8.4 Listen / FEC send path

1. User commits text in Listen window (same commit modes).
2. App enters unproto via `PTSend` (`PD`) — UI shows FEC.
3. Same grey→(later) green pipeline against TNC TX.
4. End unproto TX with RECEIVE character `<CTRL-D>` (default `RE` mapping) so TNC returns to receive / Listen posture.
5. Because PK-232 is simplex in this state, inbound ARQ cannot occur during FEC/unproto TX.

### 8.5 Data pacing to TNC

- Host data blocks use CTL `0x20` (channel 0).
- After each data block, wait for data acknowledgment `01 5F … 00 17` (per Host Mode reference) before sending the next data block.
- Respect Host max payload sizing (330 chars host→TNC excluding framing/escapes).

---

## 9. Host Mode protocol requirements

Normative reference: `docs/PK232_HostMode_Reference.md`.

### 9.1 Framing

- `SOH (0x01) | CTL | payload… | ETB (0x17)`
- Escape: if payload contains `SOH`, `DLE (0x10)`, or `ETB`, prefix that byte with `DLE`.
- No CR required inside Host blocks; `ETB` ends the block.

### 9.2 Polling policy

- **`HPOLL OFF`** for Alpha normal operation.
- Continuous serial reader consumes pushed blocks.
- Use `GG` primarily for Host-entry verification / recovery, not as the steady-state data pump.

### 9.3 Host entry (conceptual sequence)

Before Host:

- Ensure 8-bit / no parity path appropriate for Host (`AWLEN 8`, `PARITY 0`, etc., then `RESTART` as required by manuals).
- `HOST ON` / Host enable bits as documented.
- Verify with `OGG` probe block `01 4F 47 47 17` expecting success response with trailing `00`.

Exact ordered init list should be maintained as implementation proceeds against hardware; see also command encyclopedia.

### 9.4 Case sensitivity

Host two-letter mnemonics are **case-sensitive**.  
Example: Pactor standby is `Pt`, not `PT`.  
See header in `docs/HostCommands - Trimmed.md`.

### 9.5 CTL demux (receiver)

| CTL class | Meaning |
|---|---|
| `0x2F` | Echoed data |
| `0x3x` | Channel data |
| `0x3F` | Monitored data |
| `0x4x` | Link status |
| `0x5x` | Link messages |
| `0x5F` | Status / errors / data-ack |
| `0x4F` | Command response |

Parse command response code `c` for errors (`0x00` ack, `0x0A` need MYCALL, etc.).

### 9.6 Debug logging

- Toggle in Settings → Program.
- New log file each program launch.
- No rotation / no size cap in Alpha (may change later).
- Log raw hex + decoded interpretation: timestamp, direction, CTL, payload, status codes.
- Path: `logs/` under portable root.

---

## 10. Pactor control map (UI → Host)

Command encyclopedia: `docs/HostCommands - Trimmed.md`  
Operator flows: `docs/Pactor_Chapter.md`

| UI control | Behavior |
|---|---|
| Connect | `PG` / `PTConn` with remote callsign (optional `!` prefix) |
| Listen on | `PN` / `PTList`; create Listen window |
| Listen off | Destroy Listen window; `Pt` standby |
| FEC send (Listen) | `PD` / `PTSend`; data; end with `<CTRL-D>` |
| Handover now | Append `PTOver` char (default `<CTRL-Z>`, `PV`) |
| Handover after TX clear | Wait for App/TNC drain policy then `PTOver` |
| Handover with text | Send Program canned handover text as data, then `PTOver` |
| Seize link | `AG` / `ACHG` (confirmed to work in Pactor despite older AMTOR-centric wording) |
| Disconnect after clearing TX | Embed `<CTRL-D>` so link ends after TNC TX empty (clean; Ch.11) |
| Disconnect immediately | **Stub** — do not call `RC`/`Rcve` until confirmed on hardware |
| Disconnect with text | Send Program canned disconnect text, then clean `<CTRL-D>` path |
| Abort | Listen enabled → `PN`; else → `Pt` |
| Clear TNC TX | `TC` / `TCLEAR` |
| Send | Commit compose per commit mode |
| Save chat | Write transcript to file |

### 10.1 Canned text settings

Program settings store reusable strings for “with text” actions (handover with text, disconnect with text). Alpha may use one shared default string or separate fields; prefer separate labeled fields if cheap.

### 10.2 Alpha init defaults (after compat pass)

| Parameter | Host | Alpha value |
|---|---|---|
| HPOLL | `HP` | OFF |
| EAS | `EA` | OFF |
| PT200 | `PB` | ON |
| PTHUFF | `pH` | OFF |
| WORDOUT | `WO` | OFF |
| Mode | `Pt` | Pactor standby |
| MYCALL / MYPTCALL | `ML` / `Mf` | From config |
| ACRDisp / wrap | `AA` | Mirror configured wrap |

Large “dump all TNC params on connect” UI is **not** Alpha; coded init only.

---

## 11. Compatibility specification

Source of truth: `docs/Compat_Memory_Map.md`.

### 11.1 Reads

| Address | Content |
|---|---|
| `$0006` | YY (hex digits interpreted as decimal year digits) |
| `$0007` | MM |
| `$0008` | DD |
| `$0009` | Hardware / product type byte |

Example: `$86 $07 $29` → 1986-07-29.

### 11.2 Hardware bit patterns (`$0009`)

Unused bits are don’t-care. A match occurs if **either**:

- Bits 7–5 = `011` → PK-232 good, or  
- Bits 7–5 = `100` → unknown UDC-232, or  
- Bits 4–0 = `00010` → PK-232 good, or  
- Bits 4–0 = `01001` → HK-232  

### 11.3 Known fingerprints (bytes `$0006 $0007 $0008 $0009`)

**Hard fail (refuse TNC session)**

| Bytes | Label |
|---|---|
| `86 09 15 C3` | Unsupported |
| `87 03 04 62` | Unsupported |
| `87 06 25 62` | Unsupported |
| `88 02 23 62` | Unsupported |
| `89 10 31 C2` | Unsupported |
| `90 07 19 C2` | Unsupported |
| `91 08 01 C2` | Unsupported |

**Supported (continue; record version)**

| Bytes | Label |
|---|---|
| `93 03 05 C2` | supported v7.0 |
| `93 12 01 C2` | supported v7.0a |
| `95 09 13 C2` | supported v7.1 |
| `98 08 10 C2` | supported v7.2 |

**Warn + allow continue (email KJ7RBS@gmail.com)**

| Bytes | Label |
|---|---|
| `87 06 25 69` | unsupported HK-232 |
| `88 02 23 69` | unsupported HK-232 |
| `89 10 31 69` | unsupported HK-232 |
| UDC-232 bit pattern | unknown UDC-232 |
| Any other unknown 4-byte fingerprint | show hex; ask user to email |

### 11.4 UI on hard fail

- Do not mark `tncConnected` true.
- Show error explaining unsupported firmware/hardware.
- Leave GUI usable for offline layout work.

---

## 12. Configuration specification

### 12.1 Settings → COM Port

Popup to select and apply:

- Port name
- Baud / framing **defaults: 1200 baud, 7 data bits, no parity, 1 stop bit (1200 7N1)**
- Flow control (hardware/software as applicable)

Persist last successful settings.

Note: Host Mode entry (later phase) may require switching the TNC/`AWLEN` path to 8-bit; the portable default for first-run COM settings remains **1200 7N1** unless the user changes them.

### 12.2 Settings → Program

| Setting | Values / notes |
|---|---|
| Local callsign | String |
| Commit mode | Line \| Message |
| Listen on start | Boolean |
| Debug log | Boolean |
| Canned handover text | String |
| Canned disconnect text | String |
| UI list expand states | Persisted booleans |
| Wrap columns (if mirrored) | Integer / follow TNC |

### 12.3 Settings → TNC

Alpha: placeholder for future large parameter list that would be sent on connect.  
Alpha still performs **coded** compat + init on open.

### 12.4 Buddies file

- Local portable file (JSON recommended).
- Store callsign (+ optional display note later).
- Editable via UI eventually; Alpha minimum: load/save list used by Buddies section.

---

## 13. Lists and parsing

### 13.1 Buddies

User-saved; not derived from air.

### 13.2 Heard

- From Listen/monitor text.
- Alpha pattern: callsign following the text ` de` (case handling per implementation; prefer practical amateur callsign matcher).
- Show raw callsign token (including rare `-N` if present).

### 13.3 Mentioned

- Stations being called by someone else (calling station may be unknown).
- Exact patterns TBD; document in `docs/` when samples exist.
- Until then: section can exist empty or with conservative stubs.

---

## 14. Offline / no-TNC behavior

- Application must launch and allow UI inspection without a TNC.
- Maintain boolean `tncConnected` (false until Host session + compat + init succeed).
- Actions requiring TNC: ignore or error dialog; prefer disabling primary Connect when disconnected if easy.
- Dead/mock modem not required; no recorded-trace harness required for Alpha.

---

## 15. Error and notice UX

| Event | UX |
|---|---|
| Connect failed / link lost | Status bar update + non-modal in-window notice; compose read-only if link dead |
| Compat hard fail | Modal/error; session not connected |
| Compat warn (HK/UDC/unknown) | Warning dialog with email `KJ7RBS@gmail.com`; allow continue |
| Host command error codes | Surface in status/debug log; user-visible when action fails |
| Close while linked | Modal Abort / Disconnect / Cancel |

---

## 16. Implementation phases

1. **Maven skeleton** — packages, Java 21, jSerialComm, shade jar, AGPL license file.
2. **UI shell offline** — main + connection windows, commit modes, buffers, menus, `tncConnected` gating.
3. **Serial + Host framer** — open port, enter Host, `HPOLL OFF`, reader loop, debug log.
4. **Compat + init** — fingerprint policy, callsigns, `Pt`, defaults.
5. **Pactor flows** — Listen, Connect, FEC/unproto, control buttons that are not stubbed.
6. **Status + confirmation** — fill deferred detectors (TX empty/idle, OPMODE, incoming ARQ parse).

Do not implement stubbed protocol behaviors by guessing.

---

## 17. Acceptance checklist (Alpha)

- [ ] Uberjar runs on Win10+ / macOS / Linux with Java 21 from a portable folder
- [ ] GUI usable with no TNC
- [ ] COM settings persist; Host session opens on supported unit
- [ ] Compat hard-fail / warn-continue behaviors match §11
- [ ] Callsign written to `ML` and `Mf`
- [ ] Listen window create/destroy; inactive during ARQ; restore after
- [ ] ARQ connect from field or list; one active ARQ max; dead windows retained
- [ ] Line and Message commit modes work
- [ ] IRS holds text in App TX buffer only
- [ ] ISS flushes to TNC and shows grey in transcript
- [ ] Green recolor when confirmation logic available (else remains grey)
- [ ] FEC UI uses `PTSend` internally
- [ ] Handover / seize / abort / disconnect-after-clear / with-text actions match §10
- [ ] Disconnect-immediately and Rcve not falsely implemented
- [ ] Save chat; copy/select-all; scrollback for window lifetime
- [ ] Debug log toggle creates per-launch file
- [ ] No out-of-scope features from §2.2

---

## 18. Glossary

| Term | Meaning |
|---|---|
| ARQ | Linked Pactor error-corrected session (two stations) |
| ISS | Information Sending Station |
| IRS | Information Receiving Station |
| Unproto | Non-linked Pactor transmission (`PTSend`); UI label **FEC** |
| Listen / PTL | `PTList` monitor mode |
| Standby / `Pt` | Pactor standby; ready for connect, not monitoring |
| App TX buffer | Program queue while IRS |
| TNC TX buffer | PK-232 internal transmit buffer |
| Host Mode | Binary framed PK-232 computer protocol |
| EAS | Echo As Sent (off in Alpha) |
| PtRa | Short reference to this specification / program |

---

## 19. Document control

This specification consolidates product decisions from the PactorRATT_Alpha design discussions and architecture draft. Where this file and older notes disagree, **this file + `docs/` technical references** win for Alpha implementation. Protocol byte layouts not listed under Deferred must come from `docs/` or a future dated amendment to this specification—not from guesswork.
