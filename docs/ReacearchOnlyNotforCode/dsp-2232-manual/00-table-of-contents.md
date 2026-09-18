# Table of Contents

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 7–15, printed TOC-1 through TOC-9).

The scan TOC is two-column. Windows OCR interleaves columns, so this file is a **reconstructed** TOC from readable headings plus the chapter-start PDF pages. Printed page numbers (1-1, 4-21, …) are from the TOC where they were readable; they are **not** PDF page numbers. Use `(PDF p.N)` in the chapter files as the citation for this repo.

Raw OCR of the TOC pages remains in `_work/ocr/page-007.txt` … `page-015.txt`.

| Chapter | Title | PDF pages | Printed start (TOC) |
|---|---|---|---|
| — | Cover, preface, Gateway firmware notes | 1–6 | — |
| TOC | Table of contents | 7–15 | TOC-1 … TOC-9 |
| 1 | Introduction | 16–19 | 1-1 |
| 2 | Computer installation | 20–32 | 2-1 … 2-13 |
| 3 | Radio installation | 33–44 | 3-1 … 3-11 |
| 4 | Packet radio | 45–84 | 4-1 … 4-40 |
| 5 | MailDrop operation | 85–96 | 5-1 … 5-12 |
| 6 | Baudot and ASCII RTTY | 97–110 | 6-1 … 6-13 |
| 7 | AMTOR and NAVTEX | 111–131 | 7-1 … 7-21 |
| 8 | Morse | 132–136 | 8-1 … 8-5 |
| 9 | Facsimile and SSTV | 137–144 | 9-1 … 9-8 |
| 10 | SIAM and TDM | 145–148 | 10-1 … 10-4 |
| 11 | Satellite | 149–156 | 11-1 … 11-8 |
| 12 | PACTOR | 157–176 | 12-1 … 12-22 |
| App. | Command summary, schematic, parts, radio connections, pictorials | **not in this PDF** | listed on TOC-9 |

## Chapter 1 — Introduction (PDF p.16)

- 1.1 Overview / capabilities / included components — 1-1
- 1.2 Computer or terminal requirements — 1-2
- 1.3 Station / transmitter-receiver performance — 1-2
- 1.4 Specifications (modem, I/O, controls) — 1-3

## Chapter 2 — Computer installation (PDF p.20)

- 2.1 Overview / equipment / unpacking
- 2.2 Connecting power
- 2.3 Connecting the computer (IBM, Macintosh, C64/128, terminal)
- 2.4 Terminal software
- 2.5 System startup and loop-back test
- 2.6 Detailed RS-232 for other machines (Apple II, VIC-20, PCjr, CoCo, Model 100/102, NEC 8201)
- 2.7 Non-standard serial ports

## Chapter 3 — Radio installation (PDF p.33)

- 3.1 Overview / equipment
- 3.2 Receive-only connections
- 3.3 Transmit and receive connections (mic/accessory, Table 3-1 wire colors, FSK, CW)
- 3.4 Configuration jumpers / PTT
- 3.5 Transceiver adjustments (FM and SSB)

## Chapter 4 — Packet radio (PDF p.45)

- 4.1 Overview / getting started / loop-back
- 4.2 Packet introduction
- 4.3 VHF/UHF operation, monitoring, first connect
- 4.4 Mailbox / switches / “who can I talk to”
- 4.5 LCD, CTEXT greetings, beacon, digipeater, `MFILTER`, `MSTAMP`
- 4.6 Packet formatting, `PASS`, TXDELAY/AUDELAY, AXDELAY/AXHANG, FRACK/RETRY, PACLEN/MAXFRAME, CHECK/RELINK
- 4.7 Multiple connects, `CHSWITCH`, `CSTATUS`, collision avoidance
- 4.8 HF packet, `MODEM`, port switching
- 4.9 Transparent, Gateway, 8-bit converse, `CFROM`, full-duplex, HID
- 4.10 Seldom-used commands
- 4.11 Packet Lite
- 4.12 Packet meteor-scatter extension summary

## Chapter 5 — MailDrop (PDF p.85)

- 5.1 Overview, RAM, `MYMAIL`, start/stop, monitor, prompts
- 5.2 SYSOP vs remote user commands (A B E H J K L R S V)
- 5.3 Sample sessions
- 5.4 Auto-forwarding / `HOMEBBS`

## Chapter 6 — Baudot and ASCII RTTY (PDF p.97)

- 6.1–6.4 Where to operate, DSP settings, receiver, on-the-air
- 6.5 Operating tips (`EAS`, `WORDOUT`, USOS, `CODE`, commercial/wide shifts)
- 6.6 ASCII operation
- 6.7 Simultaneous RTTY and packet / modem / `RADIO`

## Chapter 7 — AMTOR and NAVTEX (PDF p.111)

- 7.1–7.4 Overview, `MYSELCAL` / `MYIDENT`, FEC CQ, ARQ, `ACHG`, `AAB`, `ALIST`
- 7.5 AMTOR MailDrop
- 7.6 Dual-port AMTOR/packet MailDrop
- 7.7 Switching-time / suggested settings
- 7.8 NAVTEX

## Chapter 8 — Morse (PDF p.132)

- 8.1–8.4 Overview, enter Morse, on-the-air, MODEM 40, `MSPEED`, `EAS`, `WORDOUT`, `LOCK`
- 8.5–8.6 Special characters / code practice

## Chapter 9 — Facsimile and SSTV (PDF p.137)

- 9.1–9.3 Overview, frequencies, analog vs B&W
- 9.4–9.8 Receive FAX, `PRTYPE`, `LEFTRITE`, `FAXNEG`, `GRAPHICS`, 4.0 MHz oscillator

## Chapter 10 — SIAM and TDM (PDF p.145)

- 10.1–10.3 SIAM, `CODE`, encoded RTTY
- 10.4 TDM receive / `QSIGNAL`

## Chapter 11 — Satellite (PDF p.149)

- 11.1–11.5 Overview, 1200 BPSK
- 11.6 9600/4800 FSK, `PG.CFG`
- 11.8 1200 AFSK FM AX.25 (UO-14 / DOVE)

## Chapter 12 — PACTOR (PDF p.157)

- 12.1–12.6 Overview, `MYPTCALL`, `PT`/`PACTOR`, `PTSEND`, long-path, LCD
- 12.7 Tips, `ACHG`, `AAB`, shifts, `EAS`, `WORDOUT`, `UCMD`, `PTLIST`
- 12.9 PACTOR MailDrop
- 12.10 Simultaneous PACTOR + packet, `RADIO`, dual-port MailDrop
- 12.11 Switching time (100 ms) / `ADELAY` / suggested settings

## Appendices listed on TOC-9 — **absent from this PDF**

- Command summary
- Schematic diagram
- Parts pictorial / parts list
- Radio connections (Appendix E is referenced from Ch. 3)
