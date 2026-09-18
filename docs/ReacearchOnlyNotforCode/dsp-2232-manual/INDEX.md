# DSP-2232 Operating Manual — AI index

**Open this file first.** Later agents writing serial/host code for the AEA DSP-2232 should start here, then open the chapter that matches the feature.

## What this device is

The **AEA DSP-2232** (also sold later under Timewave) is a dual-radio-port DSP multi-mode data controller (TNC/modem). Firmware in this 12/91–3/93 operating-manual scan supports:

- AX.25 packet on HF and VHF (simultaneous dual-port)
- Baudot / ASCII RTTY, AMTOR/SITOR (CCIR 476/625), NAVTEX
- PACTOR ARQ and unproto
- Morse, FAX/SSTV (host PC displays images), SIAM, TDM
- Satellite BPSK / 9600 FSK / 1200 AFSK
- PakMail MailDrop, Gateway/node (`MYGATE`), KISS and **HOST** (HOST details are **not** in this PDF)

It talks to a PC over **RS-232** as DCE. Users type `cmd:` commands in verbose mode. **Host Mode two-letter mnemonics and `SOH/CTL/ETB` framing are not printed in this scan** — they are pointed at a separate “DSP Technical Manual.” For Host framing used by this repo, start with [`docs/PK232_HostMode_Reference.md`](../PK232_HostMode_Reference.md) and [`docs/HostCommands - Trimmed.md`](../HostCommands%20-%20Trimmed.md), then confirm DSP-2232-specific commands/defaults in the chapter files below.

**Source PDF:** [`docs/dsp2232-manual.pdf`](../dsp2232-manual.pdf) — 176 pages, image-only scan (no text layer). This conversion covers **all 176 PDF pages**. The TOC lists appendices (command summary, schematic, parts, radio connections, pictorials) that are **not in this PDF**; the last page is Chapter 12 p. 12-22.

## File map

| File | PDF pages | Use for |
|---|---|---|
| [FORMAT-DECISION.md](FORMAT-DECISION.md) | — | Why chapter-split Markdown + `commands.json` |
| [00-cover-and-preface.md](00-cover-and-preface.md) | 1–6 | Cover; FCC/warranty **stub**; Gateway firmware notes (`MYGATE`, `CODE 7/8`, `ARXTOR`, `SIAM`+PACTOR) |
| [00-table-of-contents.md](00-table-of-contents.md) | 7–15 | Reconstructed TOC (scan TOC is two-column and OCR-jumbled) |
| [01-introduction.md](01-introduction.md) | 16–19 | Modes list; **DSP/modem specs, tones, serial rates, LEDs, power** |
| [02-computer-installation.md](02-computer-installation.md) | 20–32 | Power, **RS-232 pinout**, autobaud `*`, sign-on, loop-back, `XFLOW` |
| [03-radio-installation.md](03-radio-installation.md) | 33–44 | **5-pin DIN radio cable**, PTT, FSK/CW keying, SSB/FM audio setup |
| [04-packet-radio.md](04-packet-radio.md) | 45–84 | Connect, monitor, timing, **HF vs VHF defaults**, `MODEM`, dual-port, Packet Lite |
| [05-maildrop.md](05-maildrop.md) | 85–96 | PakMail commands (`A B E H J K L R S V`), `3RDPARTY`, forwarding |
| [06-baudot-ascii-rtty.md](06-baudot-ascii-rtty.md) | 97–110 | RTTY speeds/shifts, `CODE`, `USOS`, `WORDOUT`, `EAS` |
| [07-amtor-navtex.md](07-amtor-navtex.md) | 111–131 | `MYSELCAL` / `MYIDENT`, FEC/ARQ, `ACHG`, `ADELAY`, NAVTEX, dual-port MailDrop |
| [08-morse.md](08-morse.md) | 132–136 | `MORSE`, `MSPEED`, `LOCK`, MODEM 40, special keystrokes |
| [09-facsimile-sstv.md](09-facsimile-sstv.md) | 137–144 | Analog vs B&W FAX, `PRTYPE`, `FAXNEG`, `GRAPHICS` |
| [10-siam-tdm.md](10-siam-tdm.md) | 145–148 | `SIAM`, `QSIGNAL`, `CODE`, TDM receive |
| [11-satellite.md](11-satellite.md) | 149–156 | 1200 BPSK, 9600 FSK, 1200 AFSK AX.25, sample `PG.CFG` |
| [12-pactor.md](12-pactor.md) | 157–176 | **`MYPTCALL`, `PT`/`PACTOR`, `PTSEND`, `PTLIST`, `PTOVER`, `UCMD`, 100 ms ARQ** |
| [commands.json](commands.json) | — | Command names seen in this scan (no invented Host codes) |

Do not overwrite existing project docs (`HostCommands - Trimmed.md`, `Pactor_Chapter.md`, `ARCHITECTURE.md`, `PK232_HostMode_Reference.md`). Those are PK-232 / app architecture. This folder is the DSP-2232 operating manual.

## How a coding agent should start

1. Read this `INDEX.md`.
2. Serial bring-up: [02-computer-installation.md](02-computer-installation.md) then the RS-232 / autobaud extract below.
3. Mode + Pactor (this repo’s product): [12-pactor.md](12-pactor.md), then compare [`docs/Pactor_Chapter.md`](../Pactor_Chapter.md) (PK-232 wording is very close).
4. Dual-port / modem numbers / HF packet timing: [04-packet-radio.md](04-packet-radio.md) and specs in [01-introduction.md](01-introduction.md).
5. Command name lookup: [commands.json](commands.json), then the citing chapter. **Do not invent Host two-letter codes** from this scan. If you need Host framing, use the PK-232 Host docs and treat DSP-2232 mnemonic identity as unconfirmed unless hardware/firmware proves it.
6. Cite `(PDF p.N)` when quoting this conversion.

## Device facts extract (from OCR, not guessed)

### Serial / host link (PDF pp. 18–19, 26–31)

| Item | Value in this manual |
|---|---|
| Connector | RS-232-C **DB-9P** on the DSP-2232 (DCE) |
| DSP-2232 RX data (computer TX) | DB-9 **pin 3** (supplied DB-25 cable **pin 2**) |
| DSP-2232 TX data (computer RX) | DB-9 **pin 2** |
| Signal ground | DB-9 **pin 5** |
| Hardware handshake | RTS/CTS on DB-9 **pins 7 and 8**; default is **XON/XOFF** (`XFLOW`; `XFLOW OFF` enables hardware) |
| Autobaud rates | 110, 300, 600, 1200, 2400, 4800, 9600, 19200; `TBAUD` adds 150, 200, 400, 38400 |
| First-run | LCD `Press *` then type `*` for autobaud; sign-on ends at `cmd:` |
| Recommended first terminal setup | 1200 baud, 7 data bits, even parity, 1 stop (manual’s walkthrough); other rates allowed |
| Reset | Rear-panel RESET held while applying power restores autobaud / `Press *` |
| Power | +13 VDC (12–16 VDC) at 1100 mA; center pin of coax power plug **positive** |

**HOST ON / Host frames:** mentioned as a feature for “Host application programs” (PDF p.16) and deferred to the **DSP Technical Manual**. Not specified here.

### Radio ports and cable (PDF pp. 19, 35)

Two 5-pin DIN radio ports, simultaneous operation, software-selectable. Cable (Table 3-1):

| Wire | Color | Signal |
|---|---|---|
| Mic / AFSK to radio | White | TX audio from DSP-2232 |
| Ground | Brown | Audio and PTT common return |
| PTT | Red | Keys transmitter (factory **positive** PTT) |
| Receive audio | Green | RX audio to DSP-2232 |
| Squelch (optional) | Black | Shared voice/data activity |
| Shield / mic ground | Silver | Drain / microphone ground |

Factory PTT is positive. Direct FSK, CW keying, and satellite UP/DOWN outputs are additional (see Ch. 3). Appendix E radio-specific jacks are **not in this PDF**.

### DSP / tone values (PDF p.18)

| Modem | Tones / notes |
|---|---|
| HF packet 300 baud FSK | 2110/2310 Hz, also 1260/1460 Hz |
| VHF packet 1200 baud FSK | 1200/2200 Hz |
| HF RTTY FSK | 2125/2295 and 1445/1275 Hz; also 2125/2550, 1275/2125, 2125/2975 Hz |
| PACTOR | 2110/2310, 1460/1260 Hz (200 Hz shift) |
| Morse | 750 Hz center |
| 9600 bps FSK | K9NG compatible |
| DSP | Motorola 56001 @ 24 MHz; Z-180 host CPU; Zilog 8530 SCC HDLC |
| AFSK out | 5–100 mV RMS into 600 Ω, side-panel pots, phase-continuous |
| RX band-pass | VHF packet 1700 Hz / 2600 Hz BW; HF packet 2210 Hz / 450 Hz BW; CW 750 Hz / 200 Hz BW |

### Packet defaults called out for HF vs VHF (PDF p.69)

| Parameter | 300 baud HF (recommended) | 1200 baud VHF (port defaults) |
|---|---|---|
| SLOTTIME | 12 | 30 |
| PACLEN | 64 or less | 128 |
| MAXFRAME | 1 | 4 |
| FRACK | 8 | 5 |
| VHF | OFF | ON |
| HBAUD | 300 | 1200 |
| MODEM | 10 | 12 |

HF (VHF OFF) modem number is tied to `QHPACKET`; VHF (VHF ON) to `QVPACKET`. `HBAUD` follows the selected modem. HF packet FSK shift 200 Hz (2110/2310). Tune `MONITOR` to 6 when first copying HF.

### PACTOR (PDF pp. 157–176) — this repo’s critical path

| Action | Command / value |
|---|---|
| Set Pactor call | `MYPTCALL` (up to 8 chars + punctuation). Else `MYCALL` (dash/SSID only). Default call `DSP` is invalid → `"Need MYCALL"` |
| Enter standby | `PACTOR` or `PT` from Command Mode → `Opmode now PACTOr` on Radio **port 1** |
| Unproto TX / CQ | `PTSEND` (then converse); end with `<CTRL-D>` (see chapter) |
| Listen to others | `PTLIST` |
| ARQ changeover | `<CTRL-Z>` = `PTOVER` default (like AMTOR `+?`) |
| Break-in | `ACHG` |
| Auto-answerback | `AAB` / `<CTRL-B>` HERE-IS |
| MailDrop | `TMAIL ON`, `MDCHECK` local, same A/B/H/J/K/L/R/S/V user set |
| Dual-port off | `RADIO` e.g. `RADIO /0` disables port 2 (`RAdio now 1/0`) |
| ARQ T/R time | Radio must swap TX/RX within **100 ms**; stretch TX delay with `ADELAY` |
| Speed | 100 or 200 bps; `PT200`; `UCMD 0–3` tweak auto speed-up / Memory ARQ |
| `UCMD 0` | Default **3**, max 30 — good packets in a row before 100→200 |
| `UCMD 1` | Default **6**, max 30 — bad packets in a row before 200→100 |
| `UCMD 2` | Default **2**, max 9 — packets sent in a speed-up attempt |
| `UCMD 3` | Default **5**, max 60 — Memory ARQ packets combined |
| Error symbol | `ERchr` (default underline) — details in missing Command Summary |

Verbose `Opmode` strings in the scan: `PAcket`, `PACTOr` (mixed case is how the unit prints them).

## Command / topic lookup

| Topic | Open |
|---|---|
| Autobaud, `cmd:`, `MYCALL`, loop-back | Ch. 2 |
| RS-232 pinout, `XFLOW`, DCE | Ch. 2, specs in Ch. 1 |
| Radio DIN colors, PTT polarity, FSK/CW | Ch. 3 |
| `CONNECT`, monitor, digi, `CHSWITCH`, dual port | Ch. 4 |
| `MODEM n`, `QHPACKET`, `QVPACKET`, `HBAUD`, `VHF` | Ch. 4 |
| MailDrop `MAILDROP`, `MYMAIL`, `3RDPARTY`, `HOMEBBS` | Ch. 5 |
| RTTY `CODE`, diddle, `USOS`, commercial shifts | Ch. 6 |
| AMTOR `MYSELCAL`, `MYIDENT`, `ACHG`, `ALIST`, NAVTEX | Ch. 7 |
| Morse `MSPEED`, MODEM 40 | Ch. 8 |
| FAX `PRTYPE`, `LEFTRITE`, `FAXNEG` | Ch. 9 |
| `SIAM`, `QSIGNAL`, TDM | Ch. 10 |
| BPSK / 9600 / `PG.CFG` | Ch. 11 |
| PACTOR `PT`, `PTSEND`, `PTLIST`, `PTOVER`, `UCMD`, `TMAIL` | Ch. 12 |
| Gateway node `MYGATE`, `GUSERS` | Preface firmware notes |
| Host Mode frames | **Not in this PDF** → `docs/PK232_HostMode_Reference.md` |
| Full command dictionary with Host: letters | **Not in this PDF** → `docs/HostCommands - Trimmed.md` (PK-232) |

## Gaps / unreadable figures

- **Appendices A–E are missing** from `dsp2232-manual.pdf` (command summary, schematic, parts list, radio connector charts, pictorials). TOC promised them; PDF ends at Ch. 12.
- **HOST / KISS protocol** deferred by this manual to a **DSP Technical Manual** (not in repo).
- Two-column TOC and several **tables** were OCR-jumbled; reconstructed tables above use values that were readable. Remaining column-scrambled tables in chapter files are marked by broken row order — prefer the extracts here.
- **Figures** (rear panel, DIN drawings, bar-graph “tuned in” sketches, side-panel pots) do not OCR. Notable: PDF pp. 22, 32, 34–35, 39, 42, 141, 152, 159.
- Windows OCR leftover errors: `o` bullets, split words, `HP`/`HF`, `ugeg`/`reconunended`. Uncertain tokens stay as `[?]` when we could not read them.
- Warranty/RMA/CompuServe preface is stubbed on purpose.

## OCR method

- Render: PyMuPDF, **250 DPI grayscale**
- OCR: **Windows.Media.Ocr** via `winocr` (Tesseract winget install was canceled by UAC twice)
- Work dir: `docs/dsp-2232-manual/_work/` (page PNGs deleted after OCR; raw `.txt` + line boxes kept)
