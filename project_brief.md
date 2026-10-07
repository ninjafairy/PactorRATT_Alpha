# PactorRATT_Alpha — Project Brief (resume here)

**Last updated:** 2026-09-15  
**Stylized name:** PactorRATT_Alpha (short: PtR_Alpha / PtRa)  
**Status:** Phase 1 UI, Phase 3+4 TNC init, and Phase 5 Pactor flows are **in**. Link/Listen/Disc/HO paths are **hardware-proven** on a live PK-232. **ARQ live Compose (Build 33) was reverted 2026-09-15** — outbound is App TX + Send + Flush ISS + Line/Message commit again. Grey→green is **out of scope**.  
**License:** AGPL-3.0  
**Support contact (compat popups):** KJ7RBS@gmail.com  
**Last packaged jar:** `target/PactorRATT_Alpha.jar` copied to `Builds/Most Recent Build/PactorRATT_Alpha.jar` — warning line `Build: N  {date time}` (sequential `N` in `build.number.properties`; last package was **build 46**)

---

## What this project is

A portable **Java 21 + Swing** desktop chat program that drives a **PK-232 with Pactor firmware** in **Host Mode**. The TNC owns ARQ; the app is a structured AIM 3.x–inspired terminal with chat, status, and control actions separated.

**Normative docs (read in this order when resuming):**

1. [`PtRa_specification.md`](PtRa_specification.md) — full product/program specification  
2. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — software architecture (includes `hostIoLock` / §4.3–§4.4 pacing)  
3. This file — detailed “where we left off” summary  
4. [`docs/OPmodeResponse.md`](docs/OPmodeResponse.md) — OPMODE table + **hardware Pactor `PN`/`Pt` captures** (not AMTOR)  
5. [`docs/Alpha_Init_Sequence.md`](docs/Alpha_Init_Sequence.md) — ordered TNC connect steps (hardware-updated; includes user `[INIT]`)  
6. Other refs under [`docs/`](docs/) (Host Mode, HostCommands, Ch.4 hostmode, Compat map, Pactor chapter, hardware capture)

---

## Where we left off (2026-09-15)

Packaged **Build 46** (ARQ live Compose revert). Launch from `Run.txt` / `Builds/Most Recent Build/PactorRATT_Alpha.jar`. Portable I/O is **`{jarDir}/config/`** beside the *running* jar (often a copy under Downloads), not the GitHub tree.

**Just finished (2026-10-06 — source):** **ARQ IRS control lock and CHO buttons.** While IRS, every Controls button is disabled except **Abort**, **Seize**, and **Save transcript**. Macros, Compose, and Send stay usable. The same control lock starts on press of **Dump traffic & CHO NOW!**, **CHO after traffic**, or **Canned CHO**, and lasts until OPMODE shows IRS then ISS again. While that hold is pending and the status line still says ISS, the App TX pane is titled **queued until next ISS**. Chat committed during that hold queues in App TX (status stays the real OPMODE role) and goes out on the next real IRS→ISS. Ack failure unlocks immediately and, if still ISS, sends that queued App TX. **CHO after traffic** sends only ch0 CTL `$20` / payload `$1A` and waits for the data-ack. It does not `TC` and does not flush App TX. Disc. after TX clear still flushes App TX then `$04`. The 1-second IRS `OP` poll is removed; UBIT 10 status bytes still request `OP`.

**Just finished (2026-09-15 — source, not yet a new jar):** **Reverted ARQ live Compose.** The 1 s ISS clock, `$08` outbound backspace, locked IRS lines, no-Send editor, and `issOutboundExecutor` / `toHostDataBytes(..., false)` are gone. ARQ is again **App TX + Compose + Send + Flush ISS**, with Program **Line / Message** commit. IRS commits queue in App TX; ISS commits (and Flush ISS / OPMODE IRS→ISS) go to Host ch0 with a trailing CR. Disc/HO after TX clear drains App TX then the control byte. Listen uses the same commit setting; **FEC / End TX** and **CQ** are unchanged.

**Just finished (Build 36):** **TNC → UBIT 10…** debug window. Enable/Disable radios send Host `UB10 ON`/`UB10 OFF`; log shows only CTL `$50` frames (raw hex + decoded). **Hardware-validated on Pactor:** with UBIT 10 ON, *w* changes push `SOH $50 n ETB` (`n` = Idle/Traffic/Error/RQ etc.). Printed UBIT 10 list omitted Pactor; the TNC still does it. Closing the window does not turn UBIT 10 off. Link CONNECTED/DISCONNECTED/Timeout also appear here (same `$50`).

**Hardware-proven (2026-09-11, HO button 2026-09-13, UBIT 10 Pactor 2026-09-14):**

- ARQ window opens on `$50 CONNECTED to <peer>` and dies on `$50 DISCONNECTED: <call>` (colon). Link timeout is `$50 Timeout` then `DISCONNECTED:` — both kill the window.
- Call no-answer is a **single** `$50 Timeout` (`01 50 54 69 6D 65 6F 75 74 0D 0A 17`) with **no** following `DISCONNECTED:`. Same wire text as the linked-timeout first frame; context is Calling vs linked ARQ.
- Old firmware after Listen FEC: `PD` → `Pt` → app sends `PN` if Listen is still on. New firmware: returns to `PN` itself; app does nothing.
- Live-link buttons passed: Disc. after TX clear, Disconnect now (`TC`+`$04`), HO after TX clear, HO/Disc. with text, Seize, and **Clear TX and Handover** (`TC`+`$1A` + HO lock). After-TX-clear **flush** drains **App TX** then `$04`/`$1A`.
- **UBIT 10 ON** on Pactor: Host `UB10 ON` / `UB10 OFF` / query `UB10` (space between `10` and ON/OFF). Status changes emit one-byte `SOH $50 n ETB` (*w* `$30`–`$36`). Not in the printed Pactor UBIT 10 sentence; hardware does it. Distinguish from `$50` CONNECTED/Timeout by payload length/text.

**Software finished this stretch (2026-09-10 → 2026-09-14), Builds 25–33:**

1. Status Monitor names Pactor trailer *u* (100/200 baud) and *s* (`longpath`).
2. Calling **`<call> no answer`**: `$50 Timeout` while Calling (Build 30); 60 s local timer is fallback. TNC not aborted.
3. OPMODE **`PD`** (PTSend / UI FEC) captured and parsed. Post-FEC Listen restore is firmware-aware (`Pt`→`PN` only if needed).
4. `$50` `DISCONNECTED:` / `Timeout` parsed; callsigns come from the payload (not hardcoded).
5. Heard / Mentioned parsers + persist (cap 12) + Stations-tree right-click menus (clear / add buddy / move to top / remove).
6. **`<C>onnect` frames** (Build 31): `?>… <C>` Listen FEC beacons; never Mentioned/Heard; session-only list; 3 identical in a row **or** last-5 stem gate.
7. ARQ status **speed** 100/200 from OPMODE *u* (Build 32).
8. ARQ **live Compose** (Build 33) — **reverted 2026-09-15**. Do not treat as current. App TX + Send + Flush ISS + Line/Message are restored.
9. Portable I/O docs aligned: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) §3, [`PtRa_specification.md`](PtRa_specification.md) §4.1, [`README.md`](README.md) — `{jarDir}/config/` only (no `logs/`, no top-level `buddies.json`).

**Do not reverse:** EDT must never wait on `OP`/`PG` before showing Listen or Calling UI; ARQ window is async from `$50`; never re-lock `SerialPortService.isOpen()` / native COM read on the UI path. Do not send Host `RE`/`PV` to trigger disconnect/handover. Do not invent OPMODE tags. Grey→green stays out of scope. Do not re-land ARQ live Compose (1 s clock / `$08` outbound) unless asked.

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
| CQ | Program settings: **Canned CQ text** + **CQ repeat** 0–10 (default empty / 1). Listen **CQ** button sends the canned line that many times (each on its own line), then `PD` + data + CTRL-D. Repeat 0 or blank canned → nothing. Does not use or clear App TX. |
| FEC end TX | Embedded CTRL-D (`$04`) in Host data after buffer — **not** Host CMD `RE` |
| After FEC / CTRL-D | OPMODE is **`PD`** while sending (Traffic) and Idle immediately before end TX. After CTRL-D is consumed, tag leaves `PD`. **Older firmware → `Pt`:** if Listen is still on, send `PN`. **Later firmware → `PN` on its own:** do nothing. Do not send `PN` while still `PD` (including Idle). No `PN`/`Pt` while ARQ is active. |
| Callsign | One config value → `ML` and `Mf` |
| Idle | `Pt` (case-sensitive) |
| Listen ON | Show Listen window first. Worker: query `OP`. If `Pt` → send `PN`. If already `PN` → leave it. **Any other OPMODE / Host fail → refuse** (close window, uncheck, warn). Offline / no TNC: UI only. |
| Listen OFF / Listen window closed | Query `OP`. If `PN` → send `Pt`. Otherwise leave TNC alone. |
| Listen vs ARQ | While an ARQ window is **active**, **never** send `PN` or `Pt`. Default: Listen window stays inactive and the checkbox stays on. **Close FEC on ARQ link** disposes the FEC window and unchecks Listen without sending `Pt`. **Only 1 ARQ window** disposes leftover ARQ windows when a new link opens; a still-active QSO is not replaced. |
| Abort / Cancel call | Listen checkbox on → `PN`; else → `Pt`; then `markArqDead` (Cancel also hides Calling and sends `OP`). |
| Clean disconnect / handover | Embed CTRL-D (`$04`) / CTRL-Z (`$1A`) in Host **ch0 data** (`$01 $20 … $17`). Do **not** send Host CMD `RE` / `PV` (those only *set* the characters). Same-block for with-text. |
| Disconnect now | Host `TC` (TClear), wait ACK, then ch0 `$04`. Not `RC`/`Rcve`. |
| Handover now | Host `TC`, wait ACK, then ch0 `$1A`. Label: **Dump traffic & CHO NOW!**. Any CHO press locks every Controls button except Abort, Seize, and Save transcript until OPMODE IRS then ISS again. |
| Seize | `AG` (`AChg`) |
| Inbound text CTLs | Treat **all** `0x30`–`0x3F` the same (Pactor channel 0 only; hardware PTL uses `0x3F`) |
| EAS | OFF (`EAN`). No grey→green / TX-empty confirm coloring (out of scope). |
| Defaults | `PBY` (PT200), `PH0` (PTHUFF off), `WON` (WORDOUT off) |
| Portable I/O | **All program files under `{jarDir}/config/`** — not `logs/`, not temp, not the GitHub tree. `PactorRattAlphaApp.resolvePortableRoot()` = folder containing the running jar (IDE fallback: `user.dir`). Settings `config/settings.json`, buddies `config/buddies.json`, heard `config/heard.json`, mentioned `config/mentioned.json`, host-command groups `config/config.ini` (`[INIT]` after coded init), optional debug log `config/debug-YYYYMMDD-HHMMSS.log`. Save-chat still uses a user-chosen path. |
| Serial I/O vs UI | `SerialPortService.isOpen()` is a **volatile flag** — never call jSerialComm from the EDT. Native `readBytes`/`writeBytes` **do not** hold `ioLock`. Do **not** re-introduce `synchronized` on `isOpen()` or hold the service lock across a blocking COM read. |
| Listen ON UI | Open the Listen window **immediately**, then query `OP` / send `PN` on a worker. If OPMODE is not `Pt`/`PN`, close the window, uncheck Listen, warn. |
| Main-window Connect UI | Show **Calling \<call\>…** + Cancel on the main window (no ARQ window yet). Worker sends `PG`+callsign (`!` if **LP:** checked and not already typed). ARQ window opens on **`$50` CONNECTED** (same path as inbound). `PG` fail → error dialog, no window. **`$50 Timeout` while Calling** (no `DISCONNECTED:`) shows **`<call> no answer`** (Cancel hidden; TNC not aborted). 60 s local timer is fallback if that frame is missed. Cancel = Abort Host (`PN` if Listen on, else `Pt`), then `OP`. New Connect while calling sends another `PG` (TNC switches target). Connect stays enabled. |
| Incoming ARQ | `$50` `CONNECTED to ` + peer text (hardware: `… KJ5XF via LONGPATH`). Open ARQ window; Listen inactive, checkbox stays. Packet `Connect request:` ignored. End: `$50` `DISCONNECTED: CALL` (clean) or `$50` `Timeout` then `DISCONNECTED: CALL` (link timeout). OPMODE `Pt` is fallback only. |
| COM default | **9600 8N1** (user-selectable) |
| Compat | Supported v7.x continue; listed pre-v7 **hard refuse**; HK/UDC/unknown **warn + email + continue** |
| UI Connect | **TNC → Connect/Disconnect** = serial/Host session; main-window **Connect** = ARQ `PG` only |
| TNC status | After a successful TNC Connect, query Host `ML` (not ACK `$00`) and show **mycall: \<value\>** after **TNC: connected**. Hidden while offline / connecting. Query fail does not fail Connect. |
| Long path | Main-window **LP:** label then checkbox (right of Listen; session-only, starts unchecked). When checked, Connect label is **Connect Longpath** and every outbound `PG` (button + tree double-click) gets a leading `!` unless the call already starts with `!`. Callsign field is not rewritten. User may still type `!CALL` with LP off. |
| Startup gate | Blocking experimental warning; shows Java + `Build: N  {date time}` |
| OPMODE identify | CTL `$4F` + payload starting `OP` |
| Pactor OPMODE | **Not AMTOR.** Hardware: Listen = `OP PN w x` + 4 trailer; Standby = `OP Pt $30 x` + 4 trailer; ARQ = `OP PG w x` + 4 trailer; FEC = `OP PD w x` + 4 trailer. See [`docs/OPmodeResponse.md`](docs/OPmodeResponse.md). |
| Field *x* | `S` = Tx / ISS, `R` = Rx / IRS — applies to **every** mode that includes *x* (Status Monitor + ARQ ISS/IRS). |
| Field *w* | `$30` Standby … `$37` Sync. Used on AMTOR ARQ/Listen/FEC/SELFEC and **Pactor Listen / ARQ / FEC**. **Not** FAX. |
| Field *v* | **FAX only**; meaning TBD. Do not decode FAX *v* with the *w* table. |
| `Pt` `$30` | Fixed Pactor-standby **marker**, not the *w* sequence. |
| Mystery trailer | Last 4 payload bytes before ETB on Pactor OPMODE (`PN`/`Pt`/`PG`/`PD`): *u* baud, unnamed `$30`, *s* longpath, unnamed `$30`. Status Monitor shows **`100 baud` / `200 baud`** and **`longpath`** when *s*=`$31`. Raw hex only if *u* does not parse. Do not treat as *w*. |
| OPPOLL | Program setting **0–10** (default **0** = off). While TNC connected and ARQ window **linked and not dead**, send Host `OP` that many times/second (`hostIoLock`, skip-if-busy). Status Monitor is display-only and does **not** poll. While HO is locked and OPPOLL is 0, poll `OP` at **2 Hz** so the lock can release. |
| ARQ → dead | **`$50` `DISCONNECTED: <call>`** marks the window dead (notice includes Timeout if that frame came first). After a live OPMODE, `Pt` / *w*=Standby is **fallback** only if `$50` was missed. Early Standby right after `PG` does **not** kill the window. |
| Linked ARQ / PTSend OPMODE | **ARQ is `PG`**. **FEC/unproto is `PD`** (Traffic `$34` while sending, Idle `$33` just before end TX; *u* = baud). Parser tags `PG`/`PD` → Pactor ARQ / Pactor FEC. AMTOR `FE` is not Pactor FEC. Unknown tags decode as `Unknown (xx)` and do not drive ISS/IRS. |
| User INIT file | Hand-edit only. `{jarDir}/config/config.ini`. Created on app start if missing. **Re-read every TNC Connect.** `[INIT]` runs **after** coded init. Unknown sections ignored. Settings UI for this is later. |
| Heard / Mentioned | **Listen inbound only** (after `$08`, scan when a newline completes a line). Not local grey. Not ARQ. Callsign: **1–2 letters + 1 digit + 1–3 letters**, not glued to a letter or digit (space, `>`, other punctuation, or EOL). No SSID. Own `ML` excluded. **Heard:** whole-word `de ` (start of line or after whitespace/punctuation — not the end of `aside`/`made`) immediately followed by that call. **Mentioned:** other matching calls on the same line. A `de CALL` is not also Mentioned for that occurrence. Multiple `de CALL` on one line: each is Heard (last ends up on top). Lists are **independent** (a call may sit on both). Most recent at top; no duplicates; cap **12**; persist `{jarDir}/config/heard.json` + `mentioned.json`. **Connect frames (`?>… <C>`) are never Mentioned or Heard.** |
| `<C>onnect` frames | Listen inbound (FEC/PTL beacons; not ARQ). Line `?>` + token + ` <C>`. `<C>` = connect frame, **not** a full copy. Token is raw (may be truncated). **Session-only** Stations folder **`<C>onnect`** (no json). Cap 12. Own `ML` excluded. Promote if **3 identical tokens in a row** (valid callsign) **or** last **5** connect frames: two longest tokens identical, every other token a leading prefix of that longest, longest is a valid callsign. |
| Stations tree menus | Double-click a callsign → fill field + Connect. Right-click **Heard/Mentioned/`<C>onnect` folder** → Clear (empty that list; Connect Clear also drops the in-memory streak window). Right-click **Heard/Mentioned/`<C>onnect` call** → Add buddy (insert at top of Buddies if new) + Clear (remove from this list only). Right-click **Buddies call** → Move to top + Remove. Placeholders `(…)` have no menu. |
| ARQ Compose | **App TX + Compose + Send + Flush ISS.** Program **Line / Message** commit. **LINE:** Enter commits the current line (Shift+Enter = newline). **MESSAGE:** Enter = newline; Send commits all non-empty lines. **IRS:** commits queue in App TX. **ISS:** commits go to Host ch0 + grey transcript (`toHostDataBytes` appends trailing CR). **IRS→ISS** (OPMODE or Flush ISS): drain App TX to Host. A pending CHO hold queues commits the same way, without flipping the status role. While that hold is pending and status still says ISS, the App TX title is **queued until next ISS**. Disc. after TX clear still drains App TX then `$04`. CHO after traffic does not. |
| Listen Compose | Same Line/Message commit. Send → App TX. **FEC / End TX** = `PD` + data + CTRL-D. **CQ:** canned CQ × CQ repeat on that FEC path; does not use App TX. |
| Out of scope | File xfer, BBS, Winlink, encryption, mobile, Morse-ID disconnect, auto-AAB, other TNCs, **grey→green / TX-empty confirm coloring** |

### Session 2026-09-10 → 2026-09-14 (detailed)

Packaged as **Builds 25–33**. This is the work since the 2026-09-08 brief.

#### 1. OPMODE trailer *u* / *s* (Build 25)

Pactor `PN` / `Pt` / `PG` / `PD` trailers: first byte *u* = baud (`$31`=100, `$32`=200); third byte *s* = longpath (`$31`). Status Monitor `Mode:` shows `100 baud` / `200 baud` and `longpath` instead of raw mystery hex when *u* parses. Bytes 2 and 4 still unnamed (`$30` in every capture).

#### 2. Calling 60 s “no answer” (Build 25)

If `$50` CONNECTED never arrives, Cancel hides and the strip shows **`<call> no answer`**. Does **not** Abort the TNC. A late CONNECTED still opens ARQ. Build 25 was local 60 s only; Build 30 drives the same strip from `$50 Timeout` while Calling (see §7).

#### 3. PTSend OPMODE `PD` + post-FEC Listen compat (Build 26)

Hardware (100 baud):

```text
01 4F 4F 50 50 44 34 53 31 30 30 30 17   PD Traffic ISS  100 baud
01 4F 4F 50 50 44 33 53 31 30 30 30 17   PD Idle ISS     (right before end TX)
01 4F 4F 50 50 74 30 52 31 30 30 30 17   Pt standby      (older firmware after FEC)
```

Same *w*/*x*/*u* layout as `PN`/`PG`. AMTOR `FE` is not Pactor FEC. Idle is still `PD` — do not restore Listen until the tag **leaves** `PD`.

After FEC ends:

| Landed on | Listen still on | App |
|---|---|---|
| `Pt` (older FW) | yes | send `PN` |
| `PN` (newer FW auto-Listen) | yes | **nothing** |
| `PN` | no | send `Pt` |

Worker polls `OP` at 2 Hz after `PD`+data+CTRL-D. No `PN`/`Pt` while ARQ is active. **Hardware-proven** on both firmware behaviors.

#### 4. `$50` DISCONNECTED / Timeout (Build 27)

Callsigns are parsed from the payload (examples KJ5XF / KA4UPI are comments only).

```text
01 50 44 49 53 43 4F 4E 4E 45 43 54 45 44 3A 20 … 17   DISCONNECTED: <call>\r\n
01 50 54 69 6D 65 6F 75 74 0D 0A 17                     Timeout\r\n
```

- Clean disc. → `DISCONNECTED: <call>` → mark ARQ dead, notice `ARQ ended — DISCONNECTED: call.`
- Link timeout → `Timeout` then `DISCONNECTED:` → notice `ARQ ended — Timeout (call).`
- OPMODE `Pt` / Standby after a live OPMODE is **fallback** only if `$50` is missed.
- **Hardware-proven:** window dies on both disconnect and timeout.

#### 5. Heard / Mentioned (Build 28)

Scan **Listen inbound** only, when a newline arrives, after `$08` on the current line. [`CallsignLineParser`](src/main/java/com/pactorratt/alpha/hostmode/CallsignLineParser.java). Persist cap 12. Double-click still Connects.

#### 6. Stations right-click menus (Build 29)

Heard/Mentioned folder → Clear. Heard/Mentioned call → Add buddy + Clear. Buddies call → Move to top + Remove. Buddies persist via `ConfigStore.loadBuddyList` / `saveBuddyList` (no 12-cap).

#### 7. `$50` call no-answer (Build 30)

Same Host block as the linked-timeout first frame; **no** `DISCONNECTED:` follows because there was never a link:

```text
01 50 54 69 6D 65 6F 75 74 0D 0A 17   Timeout\r\n
```

- Calling (no ARQ window) → strip **`<call> no answer`**, hide Cancel, poll `OP`. Do **not** Abort. Do **not** set the linked-timeout pending flag.
- Linked ARQ → keep pending; wait for `DISCONNECTED:` (unchanged).
- 60 s local timer remains as fallback if the frame is missed.

#### 8. `<C>onnect` frames (Build 31)

Listen `?>CALL <C>` (hardware PTL). Never Mentioned/Heard. Session-only **`<C>onnect`** folder. Gates: 3 identical in a row, or last-5 stem match (two longest identical; others prefixes). [`CallsignLineParser`](src/main/java/com/pactorratt/alpha/hostmode/CallsignLineParser.java).

#### 9. ARQ status speed from OPMODE *u* (Build 32)

Same `applyOpmodeLink` path that sets ISS/IRS and *w* (Traffic/Error/RQ/…) writes **speed 100** or **speed 200** from Pactor trailer *u*. Leave `--` until a decoded baud arrives.

#### 10. ARQ live Compose (Build 33) — **reverted 2026-09-15**

Tried a merged live editor (1 s ISS clock, `$08` outbound, no App TX/Send/Flush ISS). It caused issues on air/use. **Current code is the pre-33 path:** App TX + Send + Flush ISS + Line/Message. Do not re-implement live Compose unless asked. Historical design notes remain in [`docs/textInput_specs.md`](docs/textInput_specs.md) as a discussion file only.

---

### Session 2026-08-26 → 2026-09-08 (detailed)

This is the work since the previous brief date (2026-08-26). Packaged as **Build 23**.

#### 1. ARQ control chars are ch0 data, not Host `RE`/`PV`

`RE` / `PV` only *set* the disconnect / handover characters. Triggering them is the same as Listen FEC CTRL-D: send the byte as **channel-0 Host data** (`$01 $20 … $17`).

| Button | Wire |
|---|---|
| Disc. after TX clear | Flush App TX, then same-block ch0 data + `$04` |
| Disconnect now | Host `TC`, wait ACK, then ch0 `$04` |
| CHO after traffic | ch0 CTL `$20`, payload `$1A` only; wait for data-ack. No `TC`, no App TX flush. Control lock |
| Dump traffic & CHO NOW! (was “Handover”) | Host `TC`, wait ACK, then ch0 `$1A`; control lock |
| Canned CHO | Canned text + `$1A` same ch0 block; control lock |
| Disc. with text | Canned text + `$04` same ch0 block |
| Seize | `AG` unchanged |
| Abort | `PN` if Listen on, else `Pt`; `markArqDead` |

Empty App TX on Disc. after TX clear → `$04` only. CHO after traffic is always `$1A` alone.

**HO refused while IRS.** While IRS, Controls are disabled except Abort, Seize, and Save transcript.

#### 2. App TX flush (Disc. after TX clear only)

The App TX buffer is the IRS-hold pane. Flush means: drain grey transcript, mark local ISS, then send drained text **plus** the control byte in **one** `sendData` block. Disconnect now / Dump traffic & CHO NOW! still `TC` the TNC buffer first; they do **not** flush App TX. CHO after traffic does not flush App TX either.

#### 3. Handover lock

Dump traffic & CHO NOW!, CHO after traffic, and Canned CHO lock on press, before the ack. The lock disables every Controls button except Abort, Seize, and Save transcript (the same set IRS disables). Stay locked until OPMODE shows **IRS** (the `$1A` was consumed) **then ISS again**. Presses are ISS-only, so the next IRS counts. Send and `>` macro lines during the hold queue in App TX and go out on that later ISS. Ack failure unlocks immediately and, if still ISS, sends the queued App TX. A dead ARQ window unlocks without sending. Backslash Host-command macro lines still run.

#### 4. Transcript: IRS→ISS newline + inbound `$08`

- `ensureTranscriptNewline()` runs before local grey paint (Compose drain / Enter, IRS→ISS dump, Listen `fecEndTx`). Insert `\n` only if the transcript is non-empty and does not already end with `\n`. **No extra CR on RF.**
- Inbound `$08` (BS) is handled in `ConnectionWindow.applyInboundTranscript`: sequential backspace on the **current line only**; does not cross `\n`; extra BS consumed. Debug Monitor stays raw. `$7F` unchanged.

#### 5. ARQ window on `$50` CONNECTED (inbound + outbound)

Hardware dump:

```text
01 50 43 4F 4E 4E 45 43 54 45 44 20 74 6F 20 4B 4A 35 58 46 20 76 69 61 20 4C 4F 4E 47 50 41 54 48 0D 0A 17
→ CONNECTED to KJ5XF via LONGPATH\r\n
01 50 44 49 53 43 4F 4E 4E 45 43 54 45 44 3A 20 4B 4A 35 58 46 0D 0A 17
→ DISCONNECTED: KJ5XF\r\n
01 50 54 69 6D 65 6F 75 74 0D 0A 17
01 50 44 49 53 43 4F 4E 4E 45 43 54 45 44 3A 20 4B 41 34 55 50 49 0D 0A 17
→ Timeout\r\n then DISCONNECTED: KA4UPI\r\n
```

- Parser: [`LinkMessageParser`](src/main/java/com/pactorratt/alpha/hostmode/LinkMessageParser.java) — `CONNECTED to `; `DISCONNECTED: <call>` (colon); exact `Timeout`.
- **Do not** open the ARQ window on Connect click / `PG` ACK. `PG` ACK `$00` means “TNC started calling,” not linked.
- Connect / buddy double-click: main-window top strip **Calling \<call\>…** + **Cancel** next to Mode/TNC. Worker sends `PG`+call (no space; `!` from **LP:** or already typed). Another Connect while calling sends another `PG` (TNC switches). Connect stays enabled.
- `$50` CONNECTED (in or out) → hide Calling, `openArqWindowForLink`, then `OP`. Default: deactivate Listen window (checkbox stays). **Close FEC on ARQ link**: dispose FEC and uncheck Listen, no `Pt`. **Only 1 ARQ window**: dispose leftover ARQ windows first; ignore CONNECTED while a QSO is still active.
- Cancel = Abort Host (`PN` if Listen on, else `Pt`), hide Calling, `OP`. End of calling (CONNECTED / cancel / 60 s timeout / `PG` fail) also sends `OP`; Mode from OPMODE if no ARQ window (`PN`→Listen, `Pt`→Idle).
- `$50 Timeout` while Calling (no following `DISCONNECTED:`) hides Cancel and shows **`<call> no answer`**. Does not Abort the TNC. 60 s local timer is fallback. Linked end is `$50` `DISCONNECTED:` (optional leading `Timeout`).
- Ignore packet `Connect request:`.
- Hang-fix preserved: Calling UI immediately; ARQ window from async `$50`; never wait on `PG` before painting UI.

#### 6. User Host-command file `config/config.ini`

INI (not JSON) at `{jarDir}/config/config.ini`. Created on app start if missing (comments + empty `[INIT]`, CRLF). **Re-read every TNC Connect.** `[INIT]` runs **after** coded init (`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML`/`Mf`, `AA`, `Pt` unchanged). Listen-on-start `PN` still after INIT. Unknown `[sections]` ignored until wired.

| File line | Wire |
|---|---|
| `HP N` | `HPN` (first space stripped) |
| `Pt` | `Pt` (no space OK) |
| `HPN` already joined | sent as-is |
| Extra spaces after mnemonic | collapsed, **warn**, then sent |
| `OP` / `MM` / `AE` | **skip, warn, continue** (not simple ACK commands) |
| Bad Host ACK | **abort** TNC Connect; FAILED dialog shows command + `0x` status |
| `#` comments / blank lines | OK |

Code: [`HostCommandIni`](src/main/java/com/pactorratt/alpha/config/HostCommandIni.java), [`TncInitializer.runUserInit`](src/main/java/com/pactorratt/alpha/hostmode/TncInitializer.java), [`InitWarningUi`](src/main/java/com/pactorratt/alpha/hostmode/InitWarningUi.java) (blocking warn on EDT via `invokeAndWait`). Hand-edit only; no Settings UI yet.

### Phase 1 — Offline UI shell (done)

| Package | Role |
|---|---|
| `app` | Entry, `AppController`, `AppMode`, TNC + ARQ Host actions, inbound listener |
| `ui` | Main / connection windows, settings, Debug + Status Monitor, startup warning, `WrapLayout` |
| `config` | Portable `config/settings.json` + `config/buddies.json` + `config/heard.json` + `config/mentioned.json` + `config/config.ini` |
| `util` | Per-launch debug log under **`config/`** (when enabled in Program settings) |
| `hostmode` | Framing, demux, session, compat, init, data send, **OPMODE `PN`/`Pt`/`PG`/`PD`**, **`$50` CONNECTED / DISCONNECTED / Timeout**, Heard/Mentioned/`<C>onnect` parse |
| `serial` | jSerialComm + byte listeners |

Working offline: Stations `JTree`, Listen/ARQ preview windows, buddies, menus, portable layout.

### Phase 3+4 — Serial + Host init (done, hardware-proven)

**Session lifecycle**

- **TNC → Connect** runs `TncInitializer` off the EDT.
- Open COM at configured baud/bits/parity/stop/flow.
- Autobaud: send `*` (`0x2A`, no CR); wait **2 s clear air** (max 15 s); capture sign-on; modal popup if non-empty.
- Host detect: `OGG` / double-SOH resync; else ASCII `AWLEN 8` → `PARITY 0` → `8BITCONV ON` → `RESTART` → `*` again → `HOST ON` → re-probe.
- Compat: `AE6` + four `MM` reads for `$0006..$0009`.
- Firmware/hardware info popup (date + all 8 bits of `$0009`; OK or **4 s auto-close**), then hard-refuse / warn / supported.
- Coded init, then **user `[INIT]`**, then `tncConnected = true`. After success, if Listen is already checked, same Listen-ON Host rule (`OP` then `PN` if `Pt`).
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

`HPN`, `EAN`, `PBY`, `PH0`, `WON`, `ML…` / `Mf…`, `AA…`, `Pt`, then user `[INIT]`.

**Build:** `target/PactorRATT_Alpha.jar` (shaded). Manifest + `build-info.properties` include `Build-Time` and `Build-Number`. Maven **initialize** increments `build.number.properties` then packages. Local Maven may be under `.tools/` if system `mvn` is missing.

```powershell
.\.tools\apache-maven-3.9.6\bin\mvn.cmd -q package
Copy-Item -Force target\PactorRATT_Alpha.jar "Builds\Most Recent Build\PactorRATT_Alpha.jar"
```

### Phase 5 — Pactor flows (in; link/Listen/Disc/HO hardware-proven)

#### Host data channel + §4.4 / §4.8 (done)

- [`HostFrameCodec`](src/main/java/com/pactorratt/alpha/hostmode/HostFrameCodec.java): `encodeData` → CTL `0x2x`; `isDataAck`; `isDataStatusError`; `MAX_HOST_TO_TNC_PAYLOAD = 330`.
- [`HostSession.sendData`](src/main/java/com/pactorratt/alpha/hostmode/HostSession.java): chunks &gt;330; wait data-ack per block; fail-fast on `$5F…W/Y`; timeout clears `statusQueue` before unlock.
- **`hostIoLock`:** serializes `sendCommand`, `sendData`, `probeOgg`, `readMemoryByte`, Debug F&F writes, **OPPOLL**, Listen `OP`/`PN`/`Pt`.
- Text → Host bytes: `\n`→`\r`; `toHostDataBytes(text)` appends a trailing CR if missing.

#### ARQ App TX → TNC / ISS flush (done; OPMODE-driven)

- While **IRS**: commits sit in App TX (not sent).
- **IRS→ISS:** drain App TX to Host + grey transcript (`flushIss` / `drainAppTxBufferToTranscript`).
- While **ISS**: Line/Message commits go to Host immediately + grey transcript.
- **Flush ISS** does the same drain even if already ISS (empty buffer still marks ISS).
- **OPMODE *x*:** `S` → ISS (`flushIss`); `R` → IRS (hold). Same *x* table for every mode that includes *x*.
- ARQ status slot that used to say `DEAD`/`ARQ` shows **last *w* word** when known (Idle/Traffic/Standby/…). **speed** is **100** or **200** from Pactor OPMODE *u* (same baud byte as Status Monitor).
- Listen window has **no** ISS flush control (FEC is the Listen send path).

#### Listen FEC / End TX (done)

- Button **FEC / End TX** (Listen only): requires non-empty App TX buffer.
- Button **CQ** (Listen only): Program canned CQ text copied CQ-repeat times (0–10), each on its own line; same `PD` + data + CTRL-D path. Does not use App TX.
- Flow: grey transcript + clear buffer → Host `PD`+`n,x` → ch0 data + CTRL-D `$04` → UI mode FEC. Worker polls `OP` until the tag leaves `PD` (Idle is still FEC).
- **Post-FEC Listen compat:** `Pt` + Listen on → `PN`; already `PN` → no Host change; Listen off + `PN` → `Pt`.
- Program Settings: **FEC 200** + **Retries** 1–5; **Canned CQ text** + **CQ repeat** 0–10; `fec200` / `fecRetries` / `cannedCqText` / `cqRepeat` in `settings.json`.

#### Listen toggle Host `PN` / `Pt` (done 2026-08-20; window-first 2026-08-21)

- Main **Listen** checkbox ON: `applyListenUiOn()` **first** (window appears immediately), then worker `enterListenHostThenUi()` — `OP` then `PN` if `Pt`; already `PN` OK; other modes / Host timeout → `refuseListenOn` (dispose Listen window, uncheck, warn).
- Listen OFF or Listen window close: `leaveListenHostIfPn()` — `OP` then `Pt` if `PN`.
- After TNC Connect success with Listen already checked (incl. listen-on-start): same ON path (init always lands on `Pt`, then user INIT, then this).
- No Host I/O while ARQ is active.

#### Inbound frame demux + transcript (done)

- All CTL `0x30`–`0x3F` → `INBOUND_DATA` → active **ARQ else Listen** transcript.
- Inbound `$08` (BS) backspaces the current transcript line (does not cross `\n`; extra BS consumed). Debug Monitor stays raw.
- Local grey paint calls `ensureTranscriptNewline` first so remote and local never share a line.
- `0x4F` → `commandQueue` **and** `HostEvent` (OPMODE decode for Status Monitor + ARQ).
- `0x50`–`0x5E` → `LINK_MESSAGE` → `$50` CONNECTED opens ARQ; `Timeout` while Calling → `<call> no answer`; `Timeout` + `DISCONNECTED:` ends linked ARQ.
- `0x5F` → `statusQueue`; other types event-only.
- PTL samples: `01 3F … 17` ([`docs/Hardware_Capture_Sample.md`](docs/Hardware_Capture_Sample.md)).

#### Debug Monitor + Status Monitor (done)

- **TNC → Debug Monitor…** — live TX/RX hex+ASCII; RX coalesced per `SOH…ETB`. **Hides** complete OPMODE frames (`$4F` + `OP…`).
- **TNC → Status Monitor…** — **only** OPMODE TX polls and RX replies, same hex/ASCII format; **Mode:** line from `OpmodeParser.Decoded.statusLine()` (mode, *w*, Tx/Rx, Morse WPM, Pactor baud, `longpath`).
- Manual Debug Cmd+Payload → `0x4F` fire-and-forget.
- Failed connect: keep serial while either monitor remains open.

#### OPMODE parser + OPPOLL (done 2026-08-19/20)

- [`OpmodeParser`](src/main/java/com/pactorratt/alpha/hostmode/OpmodeParser.java) — Ch.4 tags plus hardware **`PN` / `Pt` / `PG` / `PD`**. AMTOR `AM`/`AC`/`AL`/`FE` are **not** used as Pactor stand-ins.
- Program Settings **OPPOLL** 0–10; `opPoll` in `settings.json`.
- `AppController` scheduler: `OP` at OPPOLL Hz while ARQ linked; apply ISS/IRS + *w*; `Pt` / *w*=Standby after a live OPMODE is **fallback** only if `$50` was missed.

#### ARQ connection-window controls (wired)

| UI control | Host action |
|---|---|
| Disc. after TX clear | Flush App TX, then ch0 `$04` in the same block |
| Disconnect now | Host `TC`, wait ACK, then ch0 `$04` |
| Dump traffic & CHO NOW! | Host `TC`, wait ACK, then ch0 `$1A`; lock Controls (except Abort, Seize, Save transcript) until IRS then ISS again |
| CHO after traffic | ch0 CTL `$20`, payload `$1A` only; wait for data-ack; same control lock. No App TX flush |
| Flush ISS | Drain App TX to Host + grey transcript; mark local ISS |
| Seize | CMD `AG` |
| Abort | Listen checkbox on → `PN`, else `Pt`; then `markArqDead` |
| Canned CHO | Canned handover + `$1A` in the same ch0 block; same control lock |
| Disc. with text | Canned disconnect + `$04` in the same ch0 block |

Canned strings: `cannedHandoverText` / `cannedDisconnectText` (defaults `KKK` / `SK`); `cannedCqText` / `cqRepeat` (default empty / 1).

#### Main-window ARQ Connect (wired; window on `$50` CONNECTED)

- Connect / buddy double-click → **Calling \<call\>…** + **Cancel** on the main-window top strip (Mode / TNC row). Worker `PG`+callsign (no space; `!` from **LP:** unless already typed). No ARQ window yet. New Connect while calling sends another `PG` (TNC switches target).
- `PG` ACK `$00` = TNC started calling, not linked. Fail/timeout of the Host command → error dialog, hide Calling, `OP` to refresh Mode.
- **`$50` CONNECTED to \<peer\>** (payload may include `via LONGPATH` + CR LF) → hide Calling, open ARQ window titled with the text after `CONNECTED to `. Same path for inbound. Default: Listen window inactive, checkbox stays on. **Close FEC on ARQ link** disposes FEC and unchecks Listen (no `Pt`; FEC stays closed after the link ends). **Only 1 ARQ window** disposes leftover ARQ windows first and does not replace a QSO that is still active. Then `OP` (ISS/IRS + Mode).
- **`$50` `DISCONNECTED: <call>`** → mark ARQ dead. If a `$50` `Timeout` frame came first: notice `ARQ ended — Timeout (call).` Else `ARQ ended — DISCONNECTED: call.` Then `OP`. OPMODE `Pt` is fallback only.
- Cancel while calling = Abort Host (`PN` if Listen on, else `Pt`), hide Calling, `OP`.
- **`$50 Timeout` while Calling** (single frame; no `DISCONNECTED:`) shows **`<call> no answer`** (Cancel hidden; TNC not aborted). 60 s local timer is fallback. Packet `Connect request:` ignored.

#### Heard / Mentioned (done Build 28; menus Build 29)

- Scan: Listen window inbound only; line complete = `\n` after `$08`. Not grey outbound, not ARQ.
- Pattern: `1–2 letters + digit + 1–3 letters`, not glued to a letter or digit (space, `>`, other punctuation, or EOL). Whole-word `de ` → Heard. Other matches → Mentioned.
- Persist: `config/heard.json`, `config/mentioned.json` (CRLF JSON arrays, most recent first, cap 12).
- Own callsign (`settings.json` / `ML`) never added.
- Tree: double-click Connect. Right-click as in the Stations tree menus decision.
- Connect-frame lines (`?>… <C>`) are not Mentioned or Heard (Build 31).

#### `<C>onnect` frames (done Build 31)

- Same Listen inbound scan. Session-only folder **`<C>onnect`**. Cap 12. No persist.
- Gates: 3 identical tokens in a row, or last-5 stem (two longest identical; others prefixes of that call).

#### Buddies

- Path: **`config/buddies.json`**. Missing → create defaults `N0CALL`, `KJ7RBS` with **CRLF** (does not overwrite existing).
- Right-click a buddy: **Move to top** / **Remove**. Heard/Mentioned **Add buddy** inserts at the top if new.

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
- Connect: **Calling…** immediately, then `PG` on a worker; ARQ window on `$50` CONNECTED (not on `PG` ACK).
- `FrameParser`: reset payload/raw after a complete `ETB` frame (health leftover, not the hang).

**Debug session leftover:** `AgentDbg`, `EdtWatch`, 15 s health sampler, and `config/debug-737444.log` ingest were **removed** in Build 13. Do not re-add them. Product debug log remains `config/debug-YYYYMMDD-HHMMSS.log` when enabled in Program settings.

**Testbed notes:** jar often copied to Downloads; Java **23** was used there. Portable root is the folder **containing the jar**, so `config/` appears next to that copy, not next to the GitHub tree.

---

## What is intentionally not done yet

- Listen/FEC live send — later (still App TX + Send + FEC / End TX)
- Settings → TNC large parameter editor (not implementing yet)
- Other `config.ini` groups besides `[INIT]`; Settings UI for host-command groups (hand-edit only for now)
- Name Pactor trailer bytes 2 and 4 — wait for a capture where either byte is **not** `$30`, or a firmware/manual name. Parser already keeps them; Status Monitor only shows raw trailer hex when *u* does not parse. No code until a second value appears.
- Status-bar packet-type ticker from UBIT 10 `$50 n` (event stream is hardware-proven; UI still stubbed). Not in coded init yet.

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

**Hardware debug tip:** Open **TNC → Debug Monitor…** and/or **Status Monitor…** and/or **UBIT 10…**, then **TNC → Connect**. OPMODE frames (`01 4F 4F 50 … 17`) appear **only** in Status Monitor. `$50` frames (link text and, if UBIT 10 is Enable, Pactor *w* status-change `n`) appear in UBIT 10. Listen ON should move OPMODE from `Pt` to `PN` (window appears first; Host follows). After Listen FEC, expect `PD` Traffic then `PD` Idle, then `Pt` (app sends `PN` if Listen on) or `PN` (later firmware; no Host change). User `[INIT]` commands appear in Debug Monitor after coded `Pt`. To leave Host Mode: Debug Cmd `HO` Payload `N`.

---

## Recommended next steps (implementation order)

1. Status-bar packet-type ticker from UBIT 10 `$50 n` (Pactor hardware-proven; UI stubbed; not in coded init yet).
2. Listen/FEC live send — when wanted (leave FEC/End TX as-is until then).
3. Settings → TNC large parameter editor; extra `config.ini` groups + Settings UI for host commands (not implementing yet).
4. Name Pactor trailer bytes 2 and 4 **if** a capture shows a value other than `$30`, or a document names them.
5. Git is managed outside Cursor.

**Do not:** re-lock `SerialPortService.isOpen()` / native read on the UI path; do not wait for `OP`/`PG` before showing Listen windows or the Calling strip; ARQ window opens from `$50` CONNECTED (async), then `OP`. Do not send Host `RE`/`PV` to trigger disconnect/handover. Do not re-land ARQ live Compose unless asked.

---

## Source map (quick)

```text
app/
  PactorRattAlphaApp.java   entry, portableRoot (jar folder), startup warning gate
  AppController.java        modes, windows, connectTnc/disconnectTnc,
                            Listen ON/OFF: window first then Host OP+PN / OP+Pt,
                            requestConnect: Calling UI then PG; ARQ window on $50 CONNECTED,
                            ARQ dead on $50 DISCONNECTED: / linked Timeout,
                            Calling $50 Timeout → <call> no answer (60 s fallback),
                            cancelOutboundCall, arq* Host actions (ch0 $04/$1A, TC+$04, AG, Abort),
                            sendOutboundChat (ISS commit/flush; trailing CR), listenFecEndTx (PD+data+CTRL-D; CQ uses same path),
                            post-FEC OP watch (Pt→PN only if Listen on),
                            OPPOLL scheduler, applyOpmodeDecoded,
                            HostEvent INBOUND_DATA + COMMAND_RESPONSE (OPMODE) + LINK_MESSAGE,
                            debug/status/compat/startup dialogs, session retain,
                            ensure config.ini on start
hostmode/
  HostFrameCodec.java       SOH/CTL/ETB + DLE; encodeData; classifyCtl;
                            MAX_HOST_TO_TNC_PAYLOAD (330); isDataAck / isDataStatusError;
                            FrameParser (reset buffers on ETB)
  HostSession.java          demux, commandQueue/statusQueue, sendCommand,
                            sendData (chunk + data-ack), hostIoLock round-trips,
                            readerLoop (no lock across serial.read),
                            OGG, AE/MM$hh
  OpmodeParser.java         $4F+OP detect; Ch.4 + Pactor PN/Pt/PG/PD decode; *w*/*x*;
                            *u* baud / *s* longpath; statusLine()
  LinkMessageParser.java    $50 CONNECTED to <peer>; DISCONNECTED: <call>; Timeout
                            (alone while Calling = no answer; + DISCONNECTED: = link timeout)
  CallsignLineParser.java   Listen inbound de CALL / bare CALL / ?>… <C> connect frames
                            (3-streak or last-5 stem gate)
  HostEvent.java            typed demux events
  TncInitializer.java       full connect/init; coded init then runUserInit
  InitWarningUi.java        blocking INIT skip/extra-space warnings on EDT
  CompatChecker.java        fingerprint policy
serial/
  SerialPortService.java    jSerialComm; volatile opened; native I/O off ioLock
config/
  AppConfig, ConfigStore    {jarDir}/config/settings.json (opPoll, fec200, fecRetries, cannedCqText, cqRepeat)
                            + config/buddies.json (CRLF defaults)
                            + config/heard.json + mentioned.json (cap 12)
                            + config/config.ini ([INIT] Host extras after coded init)
  HostCommandIni            parse/ensure config.ini
ui/
  MainWindow.java           tree, ARQ Connect, Calling…/Cancel, <call> no answer, Listen, LP: checkbox, mycall:
                            Heard/Mentioned/<C>onnect/Buddies right-click (clear, add buddy, move/remove)
                            (Debug Monitor + Status Monitor + UBIT 10)
  ConnectionWindow.java     ARQ App TX + Send + Flush ISS (Line/Message); Listen App TX + FEC + CQ
                            + OPMODE-driven ISS/IRS + HO lock + inbound $08
                            + ensureTranscriptNewline / applyInboundTranscript
  DebugMonitorWindow.java   coalesced RX; hides OPMODE frames
  StatusMonitorWindow.java  OPMODE-only stream + Mode: line
  Ubit10MonitorWindow.java  UBIT 10 radios (`UB10 ON`/`UB10 OFF`); `$50` frames + decode
  ProgramSettingsDialog.java  canned text (HO/Disc/CQ), CQ repeat, FEC 200/Retries, OPPOLL
  StartupWarningDialog.java warning + Java + Build: N  datetime
util/
  DebugLog.java             optional config/debug-YYYYMMDD-HHMMSS.log
docs/OPmodeResponse.md      OPMODE table + Pactor hardware captures
docs/Alpha_Init_Sequence.md canonical init + Host encoding + pacing + [INIT]
docs/Ch. 4 hostmode         Chapter 4 source
docs/Hardware_Capture_Sample.md  PTL RX samples
build.number.properties     sequential build N (Maven initialize)
tools/IncrementBuild.java   bumps build.number.properties
Run.txt                     launch Most Recent Build jar
```

---

## Explicit non-goals (reminder)

File transfer, multi-user, store-and-forward, BBS, Winlink, encryption, network logging, mobile apps, non-PK-232 TNCs, EAS/per-char confirm coloring, **grey→green / TX-empty confirm coloring**, Morse-ID disconnect, auto-`AAB`, Spring/DI frameworks, generic TNC abstraction layers.

---

## Resume prompt (paste into a new chat)

> Resume PactorRATT_Alpha from `project_brief.md` (last updated 2026-09-15), `docs/OPmodeResponse.md`, and `docs/Alpha_Init_Sequence.md`. Last package **Build 46** in `Builds/Most Recent Build/`. Phase 1 UI, Phase 3+4 TNC init, and Phase 5 link/Listen/Disc/HO are hardware-proven. **ARQ live Compose was reverted 2026-09-15.** **Do not reverse:** EDT never waits on `OP`/`PG`; ARQ window from async `$50` CONNECTED; never lock `SerialPortService.isOpen()` / native COM read on the UI path; never send Host `RE`/`PV` to trigger disc/HO. Do not re-land ARQ live Compose unless asked. **ARQ:** open on `$50` `CONNECTED to <peer>`; dead on `$50` `DISCONNECTED: <call>` (colon; Timeout frame first = link timeout notice). OPMODE `Pt` is fallback only. Calling UI then worker `PG` (`LP:` prefixes `!` unless already typed; session-only checkbox right of Listen). Call no-answer is `$50 Timeout` alone while Calling (same wire as linked Timeout; no `DISCONNECTED:`); 60 s local `<call> no answer` is fallback. Disc/HO are ch0 `$04`/`$1A` (with-text same block; after-TX-clear flushes App TX then the control byte; Disconnect now / Clear TX and Handover are `TC` then the control byte). HO lock until IRS then ISS. **ARQ Compose:** App TX + Send + Flush ISS; Program Line/Message commit; IRS queues in App TX; ISS commits go to Host with trailing CR. **Listen/FEC:** still App TX + Send + FEC/End TX; **CQ** = canned CQ text × CQ repeat (0–10) on the same `PD`+CTRL-D path (not App TX). **Listen:** window first, then `Pt`→`PN`. After FEC (`PD` Traffic then Idle), restore `PN` only if firmware landed on `Pt`. **Heard/Mentioned:** Listen inbound newline only; whole-word `de `; persist cap 12; tree right-click Clear / Add buddy / Move to top / Remove. **`<C>onnect`:** Listen `?>… <C>` never Heard/Mentioned; session-only; 3 identical in a row or last-5 stem gate. **UBIT 10 debug:** TNC → UBIT 10…; radios `UB10 ON`/`UB10 OFF`; log is `$50` only. **Hardware:** Pactor *w* changes push `SOH $50 n ETB` when UBIT 10 is ON (printed list omitted Pactor). **Portable I/O:** `{jarDir}/config/` only (see [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) §3). Grey→green is out of scope. **Next:** status-bar packet-type ticker from UBIT 10 `$50 n` when wanted (not in coded init yet); Listen/FEC live send later; TNC settings editor / extra `[INIT]` groups when wanted; name trailer bytes 2/4 if a non-`$30` capture appears. Git is managed outside Cursor.
