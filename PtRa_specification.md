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

Station A and Station B each run PactorRATT_Alpha on a supported PK-232. A configures callsign and COM, connects to B (or accepts inbound). Typed lines flow according to IRS/ISS rules. Transcript shows remote text in black and local outbound in grey. Listen/FEC paths work. Chat can be saved. Debug Host I/O can be logged. The uberjar runs portably on Windows 10+, macOS, and Linux with Java 21.

---

## 2. Scope

### 2.1 In scope (Alpha)

| Area | Requirement |
|---|---|
| Hardware | One PK-232-class TNC with Pactor firmware, user-selected COM port |
| Air mode | Pactor only (ARQ, Listen/`PTList`, Unproto/`PTSend`) |
| Host protocol | PK-232 Host Mode framing and commands |
| UI | Swing, AIM 3.x spirit, main + connection windows |
| Chat | ARQ App TX + Send + Flush ISS (Line/Message commit); Listen App TX + FEC; combined transcript |
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
- Grey→green / TX-empty confirm coloring (local outbound stays grey)
- Morse-ID disconnect (`CTRL-F`)
- Automatic `AAB` programming
- Spring or other heavy DI frameworks
- Multi-module Maven reactor (single module Alpha)

### 2.3 Deferred (known stubs — do not invent protocol bytes)

| Stub | Reason |
|---|---|
| OPMODE / detailed status-bar field parsing | Docs deferred |
| Host link-block formats for incoming ARQ string | To be captured from hardware |
| `Rcve` (`RC`) as “disconnect immediately” | Must be confirmed manually on Pactor |
| Mentioned-list regex beyond planned patterns | Need monitor samples |
| Settings → TNC large parameter editor | Phase 2 growth |

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
- Long path: main-window **LP:** (right of Listen; session-only). When checked, Connect reads **Connect Longpath** and outbound `PG` is prefixed with `!` unless the call already starts with `!`. The callsign field is not rewritten. Typing `!CALL` still works with LP off.

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

Portable root is the folder that **contains the running jar** (`PactorRattAlphaApp.resolvePortableRoot()`; IDE / class-folder fallback: `user.dir`). **All program files** are under `{portable-root}/config/`. There is no `logs/` directory, no top-level `buddies.json`, no temp-dir state, and no writes into the GitHub tree unless that is where the jar sits. Copying the jar (Downloads, `Builds/Most Recent Build/`, etc.) creates `config/` **beside that copy**.

```text
{jarDir}/                              portable root (folder containing the running jar)
  PactorRATT_Alpha.jar
  config/                              created on demand; all program I/O
    settings.json                      COM, callsign, listen-on-start, FEC, OPPOLL, canned text, debug toggle
    buddies.json                       CRLF JSON array; defaults N0CALL, KJ7RBS if missing
    heard.json                         Listen inbound; cap 12; most recent first
    mentioned.json                     Listen inbound; cap 12; most recent first
    config.ini                         Host-command groups; [INIT] after coded init (hand-edit)
    debug-YYYYMMDD-HHMMSS.log          optional; one file per launch when Program debug log is on
  docs/                                optional in a release zip; not written by the app
```

Save-chat uses a user-chosen path. `<C>onnect` is session-only (no file). No mandatory install to Program Files / Applications.

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
config     — `{jarDir}/config/` settings, buddies, heard/mentioned, config.ini
util       — per-launch debug log under `{jarDir}/config/` (when enabled)
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
- `OutboundPipeline` — compose commit, App TX buffer, flush, grey transcript coordination
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
3. **App TX buffer** panel — IRS hold (ARQ) or queued until FEC / End TX (Listen)
4. **Compose** + **Send**
5. Control button row (ARQ includes **Flush ISS**; Listen may show subset)
6. **Status / information bar**

**Transcript**

- Keep **all** lines for the life of the window.
- Select All and Copy supported.
- On window close: **discard** unless user previously used Save chat.
- Save chat: dump transcript to a user-chosen file (end of session or anytime).
- Colors (transcript only):
  - **Grey** — local outbound text (stays grey; no confirm recolor)
  - **Black** — remote station text and all other transcript text

**Compose**

- Only exists on connection windows (not on main).
- Disabled when ARQ link has ended (dead window).
- Listen compose disabled while ARQ is active; re-enabled when back in Listen.
- On link loss (active → dead): compose becomes **read-only** (not editable) but still selectable/copyable; show non-modal notice + status update.

**App TX buffer**

- Holds lines waiting for ISS (ARQ) or **FEC / End TX** (Listen).
- Not freely editable.
- **Right-click → Edit** (IRS queued lines only):
  1. Flush current compose into App TX buffer (append).
  2. Clear compose.
  3. Move entire App TX buffer contents back into compose.
  4. Clear App TX buffer.

**ARQ Compose**

- Same Compose + App TX + Send as Listen. See §8.2. **Flush ISS** drains App TX to Host.

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

- **Compose:** the outbound editor on connection windows (Enter / Send per commit mode).
- **App TX buffer:** queued outbound while IRS (ARQ) or until **FEC / End TX** (Listen).
- **TNC TX buffer:** bytes/characters held inside the PK-232 awaiting RF transmission.
- **Transcript:** durable (for window lifetime) display of remote text (black) and completed local lines (grey).

### 8.2 ARQ outbound (commit → App TX / ISS flush)

- Program **Line / Message** commit setting. **Send** button. **Flush ISS** button.
- **LINE:** Enter commits the current line (Shift+Enter inserts a newline). **MESSAGE:** Enter inserts a newline; Send commits all non-empty compose lines.
- **IRS:** commits go into the App TX buffer. Nothing is sent to the TNC until ISS.
- **ISS:** commits go to Host ch0 immediately and paint grey on the transcript (`toHostDataBytes` appends a trailing CR).
- **IRS→ISS** (OPMODE or Flush ISS): drain App TX to grey transcript and Host. Empty buffer still flips to ISS so later commits go outbound.
- Disc./HO after TX clear: drain App TX then the control byte in the same block. Empty App TX → control byte only.
- Dead ARQ: compose read-only; Send disabled.

### 8.3 Listen / IRS vs ISS

**Listen**

- Same Line/Message commit as ARQ. **Send** copies compose lines into App TX (while the Listen window is IRS, which is the usual case). **FEC / End TX** is unchanged (`PD` + data + CTRL-D).
- **CQ** loads Program canned CQ text, repeated CQ-repeat times (each copy on its own line), and sends it on that same FEC path. Does not use or clear App TX.

**ARQ IRS / ISS**

- See §8.2. Local outbound stays grey. Do not recolor to green on TX-empty / idle.

**EAS**

- Alpha forces **EAS OFF**.
- Do not implement char-by-char EAS coloring or grey→green confirm coloring.

### 8.4 Listen / FEC send path

1. User types in Listen Compose (Line/Message commit → App TX).
2. App enters unproto via `PTSend` (`PD`) — UI shows FEC.
3. Same grey outbound pipeline against TNC TX (no green recolor).
4. End unproto TX with RECEIVE character `<CTRL-D>` (default `RE` mapping) so TNC returns to receive / Listen posture.
5. Because PK-232 is simplex in this state, inbound ARQ cannot occur during FEC/unproto TX.
6. **CQ** (Listen): Program canned CQ text copied CQ-repeat times (0–10; each on its own line) uses steps 2–5 without App TX. Repeat 0 or blank canned text sends nothing.

### 8.5 Data pacing to TNC

- Host data blocks use CTL `0x20` (channel 0).
- After each data block, wait for data acknowledgment `01 5F … 00 17` (per Host Mode reference) before sending the next data block.
- **Ch. 4 §4.8 Maximum Block Size:** Host→PK-232 blocks are limited to **330 characters** of payload, not including SOH, CTL, DLE, or ETB. Implementation must chunk (or otherwise split) larger outbound text so each framed data block stays within that limit; DLE escaping can increase on-wire size for SOH/DLE/ETB bytes inside the payload, so count payload characters before escape for the 330 limit (as the manual states), and keep escape handling correct per frame.
- **Status:** implemented in `HostSession.sendData` via `HostFrameCodec.MAX_HOST_TO_TNC_PAYLOAD` (shared by ISS flush, with-text, and any other Host data path).

---

## 9. Host Mode protocol requirements

Normative reference: `docs/PK232_HostMode_Reference.md`.

### 9.1 Framing

- `SOH (0x01) | CTL | payload… | ETB (0x17)`
- Escape: if payload contains `SOH`, `DLE (0x10)`, or `ETB`, prefix that byte with `DLE`.
- No CR required inside Host blocks; `ETB` ends the block.

### 9.2 Command / response pacing (Ch. 4 §4.3)

From PK-232 Chapter 4 Host Mode:

- The PK-232 **always** issues a response to each command.
- The computer **must wait** for that response before issuing another command.

Product/init Host I/O uses `sendCommand` (wait for matching CTL `0x4F` response). Do not pipeline Host commands. (Debug Monitor manual Send may fire-and-forget for probing only — not a model for application command paths.)

### 9.3 Polling policy

- **`HPOLL OFF`** for Alpha normal operation.
- Continuous serial reader consumes pushed blocks.
- Use `GG` primarily for Host-entry verification / recovery, not as the steady-state data pump.

### 9.4 Host entry (conceptual sequence)

Before Host:

- Ensure 8-bit / no parity path appropriate for Host (`AWLEN 8`, `PARITY 0`, etc., then `RESTART` as required by manuals).
- `HOST ON` / Host enable bits as documented.
- Verify with `OGG` probe block `01 4F 47 47 17` expecting success response with trailing `00`.

Exact ordered init list should be maintained as implementation proceeds against hardware; see also command encyclopedia.

### 9.5 Case sensitivity

Host two-letter mnemonics are **case-sensitive**.  
Example: Pactor standby is `Pt`, not `PT`.  
See header in `docs/HostCommands - Trimmed.md`.

### 9.6 CTL demux (receiver)

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

### 9.7 Debug logging

- Toggle in Settings → Program.
- New log file each program launch.
- No rotation / no size cap in Alpha (may change later).
- Log raw hex + decoded interpretation: timestamp, direction, CTL, payload, status codes.
- Path: `{jarDir}/config/debug-YYYYMMDD-HHMMSS.log` (not a `logs/` directory). Written only when the Program debug-log toggle is on.

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
| CQ (Listen) | Canned CQ text × CQ repeat; same `PD` + data + `<CTRL-D>` path; does not use App TX |
| Handover now | Append `PTOver` char (default `<CTRL-Z>`, `PV`) |
| Handover after TX clear | Wait for App/TNC drain policy then `PTOver` |
| Handover with text | Send Program canned handover text as data, then `PTOver` |
| Seize link | `AG` / `ACHG` (confirmed to work in Pactor despite older AMTOR-centric wording) |
| Disconnect after clearing TX | Embed `<CTRL-D>` so link ends after TNC TX empty (clean; Ch.11) |
| Disconnect immediately | **Stub** — do not call `RC`/`Rcve` until confirmed on hardware |
| Disconnect with text | Send Program canned disconnect text, then clean `<CTRL-D>` path |
| Abort | Listen enabled → `PN`; else → `Pt` |
| Clear TNC TX | `TC` / `TCLEAR` |
| Save chat | Write transcript to file |

### 10.1 Canned text settings

Program settings store reusable strings for “with text” actions (handover with text, disconnect with text) and Listen **CQ** (canned CQ text × CQ repeat).

### 10.2 Alpha init defaults (after compat pass)

| Parameter | Host | Alpha value |
|---|---|---|
| HPOLL | `HP` | OFF |
| EAS | `EA` | OFF |
| PT200 | `PB` | ON |
| PTHUFF | `PH` | OFF |
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
- Baud / framing **defaults: 9600 baud, 8 data bits, no parity, 1 stop bit (9600 8N1)**
- Flow control (hardware/software as applicable)

Persist last successful settings.

Note: Host Mode still sends `AWLEN 8` / `PARITY 0` on the TNC during Connect. First-run COM settings are **9600 8N1** unless the user changes them.

### 12.2 Settings → Program

| Setting | Values / notes |
|---|---|
| Local callsign | String |
| Commit mode | Line (Enter commits line) or Message (Send commits compose) |
| Listen on start | Boolean |
| Debug log | Boolean |
| Canned handover text | String |
| Canned disconnect text | String |
| Canned CQ text | String (Listen CQ button) |
| CQ repeat | Integer 0–10 (copies of canned CQ; 0 = send nothing) |
| UI list expand states | Persisted booleans |
| Wrap columns (if mirrored) | Integer / follow TNC |

### 12.3 Settings → TNC

Alpha: placeholder for future large parameter list that would be sent on connect.  
Alpha still performs **coded** compat + init on open, then hand-edited `{jarDir}/config/config.ini` `[INIT]` (re-read every TNC Connect). Unknown INI sections ignored. Settings UI for host-command groups is later.

### 12.4 Buddies file

- `{jarDir}/config/buddies.json` (CRLF JSON array).
- Created with defaults `N0CALL`, `KJ7RBS` if missing; existing file is never overwritten.
- Store callsign (+ optional display note later).
- Editable via Stations-tree right-click (Move to top / Remove; Heard/Mentioned Add buddy).

---

## 13. Lists and parsing

### 13.1 Buddies

User-saved; not derived from air.

### 13.2 Heard

- Listen inbound lines only (scan on newline after `$08`; not local grey).
- Whole-word `de ` (d, e, space) immediately followed by a matching callsign.
- Callsign: 1–2 letters, 1 digit, 1–3 letters, not glued to a letter or digit on either side (space, `>`, other punctuation, or end of line). No SSID. Own `ML` excluded.
- Most recent at top; no duplicates; persist `{jarDir}/config/heard.json`; cap 12.

### 13.3 Mentioned

- Same callsign pattern on the same completed inbound line, without a leading whole-word `de `.
- A line may add to both lists. Lists are independent (a call may sit on both).
- Persist `{jarDir}/config/mentioned.json`; cap 12; most recent at top.

### 13.4 `<C>onnect` frames

- Listen inbound only (same newline / `$08` scan). Air: FEC/PTL beacons, not ARQ.
- Line shape: `?>` + token + ` <C>`. `<C>` means connect/calling frame, **not** a complete copy.
- Token is raw text between the wrappers (may be `W` / `WA` / `WA6HVC`). Never Heard or Mentioned.
- Session-only list (no `connect.json`). Cap 12. Own `ML` excluded.
- Promote when either gate passes:
  - three identical connect tokens in a row, and that token is a valid callsign; or
  - last 5 connect frames: the two longest tokens are identical, every other token is a leading prefix of that longest call, and the longest is a valid callsign.
- Truncations like `WA` / `WA6` / `W` around two `WA6HVC` copies pass as `WA6HVC`. A foreign call (`KE6O`) in that window fails. Two identical incomplete longest calls (`WA6HV`) may pass as that shorter call.

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
6. **Status** — fill deferred detectors (OPMODE, incoming ARQ / `$50` parse). No grey→green.

Do not implement stubbed protocol behaviors by guessing.

---

## 17. Acceptance checklist (Alpha)

- [ ] Uberjar runs on Win10+ / macOS / Linux with Java 21 from a portable folder; program I/O is `{jarDir}/config/` only (no `logs/`)
- [ ] GUI usable with no TNC
- [ ] COM settings persist; Host session opens on supported unit
- [ ] Compat hard-fail / warn-continue behaviors match §11
- [ ] Callsign written to `ML` and `Mf`
- [ ] Listen window create/destroy; inactive during ARQ; restore after
- [ ] ARQ connect from field or list; one active ARQ max; dead windows retained
- [ ] Line/Message commit; IRS hold in App TX; ISS flush → grey transcript (stays grey)
- [ ] ISS live-send; completed lines grey in transcript (stays grey)
- [ ] FEC UI uses `PTSend` internally
- [ ] Handover / seize / abort / disconnect-after-clear / with-text actions match §10
- [ ] Disconnect-immediately and Rcve not falsely implemented
- [ ] Save chat; copy/select-all; scrollback for window lifetime
- [ ] Debug log toggle creates per-launch `{jarDir}/config/debug-YYYYMMDD-HHMMSS.log`
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
