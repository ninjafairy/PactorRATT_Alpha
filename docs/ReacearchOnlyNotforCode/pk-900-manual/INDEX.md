# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint) — AI index

Open this file first. Converted from `docs/timewave-aea-pk-900-manual-nov-2004.pdf` (392 pages, Word-distilled, not a scan).

## What the PK-900 is

AEA / Timewave **PK-900** dual-port multi-mode HF/VHF data controller (TNC). It sits between a computer (RS-232 DB-25) and one or two radios (5-pin DIN) and does AX.25 packet (HF/VHF), Baudot/ASCII RTTY, AMTOR/SITOR, Morse, HF weather FAX, and Pactor. Receive-only extras: NAVTEX, TDM, bit-inverted Baudot, SIAM (signal identification). Special features called out in Chapter 1: PakMail MailDrop, KISS (Appendix A), **HOST mode for host programs (details in the separate Technical Manual — not this book)**, dual-port gateway.

This operating manual is the human/`cmd:` command set, radio/computer wiring, mode procedures, and the full Appendix A dictionary (verbose name, Host two-letter mnemonic, default, parameters). It does **not** document Host-mode framing (`SOH`/`CTL`/`ETB`). For PK-232 Host framing already in this repo see `docs/PK232_HostMode_Reference.md`; treat PK-900 Host I/O as unspecified until the Technical Manual is converted.

## How to use these files

1. Use the topic table below to open the chapter for the mode you are coding.
2. Look up a command by name or Host mnemonic in the command table (or `commands.json`).
3. Read the matching `commands/commands-X.md` entry — do not invent defaults, hex, or Host codes.
4. Dual-port values are written `port1/port2` (example: `TXdelay` default `30/30` meaning 300 msec on each port).
5. Host two-letter mnemonics are case-sensitive (`Am` vs `AM`, `An` vs `AS`).

## File map

| File | Contents | PDF pages |
|---|---|---|
| `FORMAT-DECISION.md` | Why chapter-split Markdown + JSON catalog | — |
| `INDEX.md` | This entry point | — |
| `commands.json` | Machine catalog of Appendix A commands | 187–290 |
| `00-front-matter.md` | Front matter (legal stub) | 1–4 |
| `00-table-of-contents.md` | Table of contents | 5–14 |
| `01-introduction.md` | Chapter 1 — Introduction | 15–18 |
| `02-computer-installation.md` | Chapter 2 — Computer installation | 19–30 |
| `03-radio-installation.md` | Chapter 3 — Radio installation | 31–42 |
| `04-packet-radio.md` | Chapter 4 — Packet radio | 43–86 |
| `05-maildrop.md` | Chapter 5 — MailDrop / PakMail | 87–98 |
| `06-baudot-ascii-rtty.md` | Chapter 6 — Baudot and ASCII RTTY | 99–114 |
| `07-amtor-navtex.md` | Chapter 7 — AMTOR / SITOR / NAVTEX | 115–136 |
| `08-morse.md` | Chapter 8 — Morse | 137–142 |
| `09-facsimile-sstv.md` | Chapter 9 — Facsimile / SSTV / HF weather FAX | 143–150 |
| `10-siam-tdm.md` | Chapter 10 — SIAM / TDM / bit-inverted Baudot | 151–156 |
| `11-pactor.md` | Chapter 11 — Pactor | 157–178 |
| `appendix-a-command-guide.md` | Appendix A — Command summary guide (how to enter commands, messages) | 179–186 |
| `appendix-a-command-list.md` | Appendix A — Compact command list | 291–296 |
| `appendix-b-schematic.md` | Appendix B — Schematic diagram (stub) | 297–304 |
| `appendix-c-parts-pictorial.md` | Appendix C — Parts pictorial (stub) | 305–308 |
| `appendix-d-self-test.md` | Appendix D — Self test routine (stub) | 309–312 |
| `appendix-e-radio-connections.md` | Appendix E — Specific radio connections | 313–323 |
| `appendix-f-warranty.md` | Appendix F — Warranty (stub) | 324–324 |
| `supplement-gateway.md` | Gateway option supplement (Dec 1993) | 325–352 |
| `supplement-gps.md` | TNC GPS upgrade addendum (Rev F, Mar 2003) | 353–365 |
| `supplement-dsp.md` | PK-900/DSP upgrade kit | 366–372 |
| `supplement-psk.md` | PK-900/PSK sound-card interface upgrade | 373–392 |
| `commands/commands-0-9.md` … `commands-Z.md` | Appendix A dictionary, HostCommands layout | 187–290 |

## Topic lookup

| Topic | Open |
|---|---|
| Capabilities, specs, modem characteristics, I/O, power | `01-introduction.md` |
| RS-232, autobaud (110–19200), AWLEN/PARITY, loop-back | `02-computer-installation.md` |
| DIN radio cable, PTT polarity, FSK, CW keying, jumpers, AFSK level | `03-radio-installation.md` |
| Packet timing: TXDELAY, AUDELAY, FRACK, PACLEN, MAXFRAME, dual-port, HF packet, Packet Lite, KISS mention | `04-packet-radio.md` |
| PakMail / MailDrop SYSOP and remote commands | `05-maildrop.md` |
| Baudot / ASCII RTTY, RBAUD, ABAUD, USOS, DIDDLE | `06-baudot-ascii-rtty.md` |
| AMTOR ARQ/FEC, SELCAL, NAVTEX, ALIST | `07-amtor-navtex.md` |
| Morse / CW, MSPEED, modem 12 (750 Hz) | `08-morse.md` |
| HF weather FAX / SSTV / Analog | `09-facsimile-sstv.md` |
| SIAM, TDM, bit-inverted Baudot | `10-siam-tdm.md` |
| Pactor ARQ/unproto, MYPTCALL, PTCONN, PTSEND, PTLIST, UCMD 0–3 | `11-pactor.md` |
| How commands are entered; `cmd:` responses; error messages | `appendix-a-command-guide.md` |
| Full command dictionary (name / Host / default / params) | `commands/commands-*.md` + `commands.json` |
| Compact A–Z command list tables | `appendix-a-command-list.md` |
| Per-radio mic/DIN wiring notes (~400 models) | `appendix-e-radio-connections.md` |
| Gateway firmware option (packet↔AMTOR/Pactor node) | `supplement-gateway.md` |
| GPS / APRS firmware commands | `supplement-gps.md` |
| DSP daughterboard install | `supplement-dsp.md` |
| PSK sound-card interface | `supplement-psk.md` |
| Host-mode *framing* (not in this book) | PK-900 Technical Manual (missing here); PK-232 analog: `docs/PK232_HostMode_Reference.md` |

## Command lookup (Appendix A)

Host mnemonics are copied exactly from the extract. If Host is blank, the PDF block did not yield a code — do not guess.

| Command | Host | Default / type | Mode | File | PDF |
|---|---|---|---|---|---|
| `3Rdparty` | `3R` | OFF | Packet/AMTOR/PACTOR MailDrop | `commands/commands-0-9.md` | 187 |
| `5Bit` | `5B` | Immediate | Command | `commands/commands-0-9.md` | 187 |
| `6Bit` | `6B` | Immediate | Command | `commands/commands-0-9.md` | 187 |
| `8Bitconv` | `8B` | OFF | Packet, PACTOR, ASCII | `commands/commands-0-9.md` | 188 |
| `AAb` | `AU` | empty | Baudot, ASCII, AMTOR, PACTOR | `commands/commands-A.md` | 188 |
| `ABaud` | `AB` | 110 bauds | ASCII | `commands/commands-A.md` | 188 |
| `AChg` | `AG` | Immediate | AMTOR, PACTOR | `commands/commands-A.md` | 188 |
| `ACKprior` | `AN` | OFF/OFF | Packet | `commands/commands-A.md` | 189 |
| `ACRDisp` | `AA` | 0 | ALL | `commands/commands-A.md` | 190 |
| `ACRPack` | `AK` | ON | Packet | `commands/commands-A.md` | 190 |
| `ACRRtty` | `AT` | 71 (69 in AMTOR) | Baudot/ASCII RTTY, AMTOR and PACTOR | `commands/commands-A.md` | 190 |
| `ADDress` | `AE` | $0000 | ALL | `commands/commands-A.md` | 191 |
| `ADelay` | `AD` | 4 (40 msec.) | AMTOR, PACTOR | `commands/commands-A.md` | 191 |
| `AFilter` | `AZ` | OFF | ALL | `commands/commands-A.md` | 192 |
| `ALFDisp` | `AI` | ON | All | `commands/commands-A.md` | 192 |
| `ALFPack` | `AP` | OFF | Packet | `commands/commands-A.md` | 193 |
| `ALFRtty` | `AR` | ON | Baudot/ASCII RTTY | `commands/commands-A.md` | 193 |
| `AList` | `AL` | Immediate | AMTOR | `commands/commands-A.md` | 193 |
| `ALTModem` | `Am` | 0 | Command | `commands/commands-A.md` | 194 |
| `AMtor` | `AM` | Immediate | Command | `commands/commands-A.md` | 194 |
| `ANalog` | `An` | Immediate | Command | `commands/commands-A.md` | 195 |
| `ANSample` | `As` | 2000 | Analog | `commands/commands-A.md` | 195 |
| `ARq` | `AC` | Immediate | AMTOR | `commands/commands-A.md` | 196 |
| `ARQE` | `Ae` | Immediate | Command | `commands/commands-A.md` | 196 |
| `ARQTmo` | `AO` | 60 | AMTOR, PACTOR | `commands/commands-A.md` | 197 |
| `ARQTOL` | `Ao` | 3 | AMTOR ARQ | `commands/commands-A.md` | 197 |
| `AScii` | `AS` | Immediate | Command | `commands/commands-A.md` | 197 |
| `ASPect` | `AY` | 2 (576) | FAX | `commands/commands-A.md` | 198 |
| `AUdelay` | `AQ` | 2/2 (20 msec.) | Baudot, ASCII, FEC, FAX, PACTOR and Packet | `commands/commands-A.md` | 199 |
| `AUTOBaud` | `Ab` | OFF | Command | `commands/commands-A.md` | 199 |
| `AWlen` | `AW` | 7 | All | `commands/commands-A.md` | 200 |
| `Ax25l2v2` | `AV` | ON/ON | Packet | `commands/commands-A.md` | 200 |
| `AXDelay` | `AX` | 0/0 (00 msec.) | Packet | `commands/commands-A.md` | 201 |
| `AXHang` | `AH` | 0/0 (000 msec.) | Packet | `commands/commands-A.md` | 201 |
| `BARgraph` | `BG` | 0 | Command | `commands/commands-B.md` | 202 |
| `BAudot` | `BA` | Immediate | Command | `commands/commands-B.md` | 202 |
| `BBSmsgs` | `BB` | OFF | Packet | `commands/commands-B.md` | 202 |
| `Beacon` | `BE` | EVERY 0/EVERY 0 (00 sec.) | Packet | `commands/commands-B.md` | 203 |
| `BItinv` | `BI` | $00 | RTTY | `commands/commands-B.md` | 203 |
| `BKondel` | `BK` | ON | All | `commands/commands-B.md` | 204 |
| `BRight` | `BR` | 50 | Command | `commands/commands-B.md` | 204 |
| `BText` | `BT` | empty | Packet | `commands/commands-B.md` | 204 |
| `CALibrate` | `Not Supported` | Immediate | Command | `commands/commands-C.md` | 205 |
| `CANline` | `CL` | $18 <CTRL-X> | All | `commands/commands-C.md` | 205 |
| `CANPac` | `CP` | $19 <CTRL-Y> | Packet, Command | `commands/commands-C.md` | 206 |
| `CASedisp` | `CX` | 0 (as is) | Packet | `commands/commands-C.md` | 206 |
| `CBell` | `CU` | OFF | Packet, PACTOR and AMTOR | `commands/commands-C.md` | 206 |
| `CFrom` | `CF` | all/all | Packet | `commands/commands-C.md` | 207 |
| `CHCall` | `CB` | OFF | Packet | `commands/commands-C.md` | 208 |
| `CHDouble` | `CD` | OFF | Packet | `commands/commands-C.md` | 208 |
| `CHeck` | `CK` | 30/30 (300 sec.) | Packet | `commands/commands-C.md` | 209 |
| `CHSwitch` | `CH` | $00 | All | `commands/commands-C.md` | 209 |
| `CMdtime` | `CQ` | 10 (1000 msec.) | All | `commands/commands-C.md` | 210 |
| `CMSg` | `CM` | OFF/OFF | Packet | `commands/commands-C.md` | 210 |
| `CODe` | `C1` | 0 (International) | Baudot RTTY, Morse, AMTOR | `commands/commands-C.md` | 211 |
| `COMmand` | `CN` | $03 <CTRL-C> | All | `commands/commands-C.md` | 215 |
| `CONMode` | `CE` | CONVERSE | Packet, AMTOR and PACTOR | `commands/commands-C.md` | 215 |
| `Connect` | `CO` | Immediate | Packet and PACTOR | `commands/commands-C.md` | 216 |
| `CONPerm` | `CY` | OFF | Packet | `commands/commands-C.md` | 216 |
| `CONStamp` | `CG` | OFF | Packet, PACTOR | `commands/commands-C.md` | 217 |
| `CONVerse` | `Not Supported` | Immediate | All | `commands/commands-C.md` | 217 |
| `CPactime` | `CI` | OFF | Packet | `commands/commands-C.md` | 217 |
| `CRAdd` | `CR` | OFF | Baudot RTTY | `commands/commands-C.md` | 218 |
| `CStatus` | `Not Supported` | Immediate | Packet | `commands/commands-C.md` | 218 |
| `CText` | `CT` | empty | Packet | `commands/commands-C.md` | 219 |
| `CWid` | `CW` | $06 <CTRL-F> | Baudot, ASCII, RTTY, AMTOR, FAX, PACTOR | `commands/commands-C.md` | 219 |
| `DAYStamp` | `DS` | OFF | All | `commands/commands-D.md` | 219 |
| `DAytime` | `DA` | none | All | `commands/commands-D.md` | 220 |
| `DCdconn` | `DC` | OFF | Packet/PACTOR/AMTOR KISS and RAWHDLC | `commands/commands-D.md` | 220 |
| `DELete` | `DL` | OFF | All | `commands/commands-D.md` | 221 |
| `DFrom` | `DF` | all/all | Packet | `commands/commands-D.md` | 221 |
| `DIDdle` | `DD` | ON | Baudot, ASCII | `commands/commands-D.md` | 221 |
| `DIRect` | `DQ` | Immediate | Packet | `commands/commands-D.md` | 222 |
| `Disconne` | `DI` | Immediate | Packet | `commands/commands-D.md` | 222 |
| `DISPlay` | `Not Supported` | Immediate | Command | `commands/commands-D.md` | 223 |
| `DWait` | `DW` | 16/16 (160 msec.) | Packet | `commands/commands-D.md` | 224 |
| `EAS` | `EA` | OFF | Baudot, ASCII, AMTOR, PACTOR and MORSE | `commands/commands-E.md` | 224 |
| `Echo` | `EC` | ON | All | `commands/commands-E.md` | 225 |
| `ERrchar` | `ER` | $5F (_) | AMTOR, PACTOR, Morse, NAVTEX and TDM | `commands/commands-E.md` | 225 |
| `EScape` | `ES` | OFF | All | `commands/commands-E.md` | 225 |
| `FAx` | `FA` | Immediate | Command | `commands/commands-F.md` | 226 |
| `FAXNeg` | `FN` | OFF | FAX | `commands/commands-F.md` | 226 |
| `FEc` | `FE` | Immediate | AMTOR Mode B | `commands/commands-F.md` | 226 |
| `Flow` | `FL` | ON | All | `commands/commands-F.md` | 227 |
| `FRack` | `FR` | 5/5 (5 sec.) | Packet | `commands/commands-F.md` | 227 |
| `FREe` | `FZ` | Immediate | All | `commands/commands-F.md` | 227 |
| `FRIck` | `FF` | 0/0 (0 sec.) | Packet | `commands/commands-F.md` | 228 |
| `FSpeed` | `FS` | 2 (120) | FAX | `commands/commands-F.md` | 229 |
| `FUlldup` | `FU` | OFF/OFF | Packet | `commands/commands-F.md` | 229 |
| `GRaphics` | `GR` | 1 (960 dots) | FAX | `commands/commands-G.md` | 230 |
| `HBaud` | `HB` | 1200/1200 bauds | Packet | `commands/commands-H.md` | 231 |
| `HEAderln` | `HD` | ON | Packet | `commands/commands-H.md` | 231 |
| `Help` | `Not Supported` | Immediate | Command | `commands/commands-H.md` | 232 |
| `HEReis` | `HR` | $02 <CTRL-B> | Baudot, ASCII, AMTOR and PACTOR | `commands/commands-H.md` | 232 |
| `HId` | `HI` | OFF/OFF | Packet | `commands/commands-H.md` | 232 |
| `HOMebbs` | `HM` | (none) | Packet/MailDrop | `commands/commands-H.md` | 233 |
| `HOST` | `HO` | 0 | All | `commands/commands-H.md` | 233 |
| `HPoll` | `HP` | ON | Host | `commands/commands-H.md` | 234 |
| `Id` | `ID` | Immediate | AMTOR/ASCII/Baudot/Packet | `commands/commands-I.md` | 234 |
| `ILfpack` | `IL` | ON | Packet | `commands/commands-I.md` | 234 |
| `IO` | `IO` | none | All | `commands/commands-I.md` | 235 |
| `JUstify` | `JU` | Immediate | FAX | `commands/commands-J.md` | 235 |
| `KILONFWD` | `KL` | ON | Packet/MailDrop | `commands/commands-K.md` | 235 |
| `KIss` | `KI` | 0 | Packet | `commands/commands-K.md` | 236 |
| `KISSAddr` | `KA` | 0/1 | Packet | `commands/commands-K.md` | 237 |
| `LAstmsg` | `LA` | Immediate | Packet MailDrop | `commands/commands-L.md` | 237 |
| `LEftrite` | `LR` | ON | FAX | `commands/commands-L.md` | 237 |
| `LIte` | `LI` | OFF/OFF | Packet | `commands/commands-L.md` | 238 |
| `Lock` | `LO` | Immediate | Morse/Baudot/AMTOR/FAX | `commands/commands-L.md` | 238 |
| `MAildrop` | `MV` | OFF | Packet | `commands/commands-M.md` | 238 |
| `MARK` | `Mk` | Current modem mark frequency | Command | `commands/commands-M.md` | 239 |
| `MARsdisp` | `MW` | OFF | Baudot and AMTOR, RTTY | `commands/commands-M.md` | 239 |
| `MAXframe` | `MX` | 4/4 | Packet | `commands/commands-M.md` | 239 |
| `MBEll` | `ME` | OFF/OFF | Packet | `commands/commands-M.md` | 240 |
| `MBx` | `MB` | none | Packet | `commands/commands-M.md` | 240 |
| `MCon` | `MC` | 0/0 (none) | Packet | `commands/commands-M.md` | 241 |
| `MDCheck` | `M1` | Immediate | AMTOR, Packet, PACTOR /MailDrop | `commands/commands-M.md` | 241 |
| `MDigi` | `MD` | OFF | Packet | `commands/commands-M.md` | 242 |
| `MDMon` | `Mm` | OFF | AMTOR and Packet/MailDrop | `commands/commands-M.md` | 242 |
| `MDPrompt` | `Mp` | (see text) | Packet/PACTOR MailDrop | `commands/commands-M.md` | 242 |
| `MEmory` | `MM` | none | All | `commands/commands-M.md` | 243 |
| `MFIlter` | `MI` | $80 | Morse, Baudot ASCII, AMTOR, PACTOR and Packet | `commands/commands-M.md` | 243 |
| `MFrom` | `MF` | ALL/ALL | Packet | `commands/commands-M.md` | 243 |
| `MHeard` | `MH` | Immediate | Packet/AMTOR MailDrop | `commands/commands-M.md` | 244 |
| `MId` | `Mi` | 0/0 (00 sec.) | Packet | `commands/commands-M.md` | 244 |
| `MMsg` | `MU` | OFF | Packet/AMTOR/PACTOR MailDrop | `commands/commands-M.md` | 244 |
| `MODem` | `Mq` | 11/4 | All | `commands/commands-M.md` | 245 |
| `Monitor` | `MN` | 4/4 (UA DM C D I UI) | Packet | `commands/commands-M.md` | 246 |
| `MOrse` | `MO` | Immediate | Command | `commands/commands-M.md` | 246 |
| `MProto` | `MQ` | OFF/OFF | Packet | `commands/commands-M.md` | 247 |
| `MRpt` | `MR` | ON/ON | Packet | `commands/commands-M.md` | 247 |
| `MSPeed` | `MP` | 20 WPM | Morse | `commands/commands-M.md` | 247 |
| `MStamp` | `MS` | OFF | Packet | `commands/commands-M.md` | 248 |
| `MTExt` | `Mt` | See sample | AMTOR/PACTOR/Packet MailDrop | `commands/commands-M.md` | 248 |
| `MTo` | `MT` | none/none | Packet | `commands/commands-M.md` | 249 |
| `MWeight` | `Mw` | 10 | All except Packet | `commands/commands-M.md` | 249 |
| `MXmit` | `Mx` | OFF | Packet | `commands/commands-M.md` | 250 |
| `MYAlias` | `MA` | none/none | Packet | `commands/commands-M.md` | 250 |
| `MYALTcal` | `MK` | none | AMTOR | `commands/commands-M.md` | 250 |
| `MYcall` | `ML` | PK900/PK900 | Packet, PACTOR | `commands/commands-M.md` | 251 |
| `MYIdent` | `Mg` | none | AMTOR | `commands/commands-M.md` | 251 |
| `MYGate` | `MY` | none | Packet | `commands/commands-M.md` | 252 |
| `MYMail` | `Ma` | none | Packet, PACTOR/MailDrop | `commands/commands-M.md` | 252 |
| `MYPTcall` | `Mf` | PK900 | PACTOR | `commands/commands-M.md` | 252 |
| `MYSelcal` | `MG` | none | AMTOR | `commands/commands-M.md` | 253 |
| `NAVMsg` | `NM` | All | NAVTEX | `commands/commands-N.md` | 253 |
| `NAVStn` | `NS` | All | NAVTEX | `commands/commands-N.md` | 254 |
| `NAvtex` | `NA` | Immediate | All | `commands/commands-N.md` | 254 |
| `NEwmode` | `NE` | ON | All | `commands/commands-N.md` | 254 |
| `NOmode` | `NO` | OFF | All | `commands/commands-N.md` | 255 |
| `NUCr` | `NR` | OFF | All | `commands/commands-N.md` | 255 |
| `NULf` | `NF` | OFF | All | `commands/commands-N.md` | 255 |
| `NULLs` | `NU` | 0 (zero) | All | `commands/commands-N.md` | 256 |
| `Nums` | `NX` | Immediate | Baudot, AMTOR, TDM | `commands/commands-N.md` | 256 |
| `OK` | `OK` | Immediate | SIGNAL | `commands/commands-O.md` | 256 |
| `Opmode` | `OP` | Immediate | Command | `commands/commands-O.md` | 256 |
| `PAcket` | `PA` | Immediate | Command | `commands/commands-P.md` | 256 |
| `PACLen` | `PL` | 128/128 | Packet | `commands/commands-P.md` | 257 |
| `PACTime` | `PT` | AFTER 10 (1000 msec.) | Packet | `commands/commands-P.md` | 257 |
| `PACTOr` | `Pt` | Immediate | Command | `commands/commands-P.md` | 257 |
| `PARity` | `PR` | 3 (even) | All | `commands/commands-P.md` | 258 |
| `PASs` | `PS` | $16 <CTRL-V> | Packet/ASCII | `commands/commands-P.md` | 258 |
| `PASSAll` | `PX` | OFF/OFF | Packet | `commands/commands-P.md` | 258 |
| `PErsist` | `PE` | 63/63 | Packet | `commands/commands-P.md` | 258 |
| `PK` | `PK` | none | All | `commands/commands-P.md` | 259 |
| `PPersist` | `PP` | ON/ON | Packet, PACTOR | `commands/commands-P.md` | 259 |
| `PRType` | `PY` | 2 (Epson) | FAX | `commands/commands-P.md` | 259 |
| `PT200` | `PB` | ON | PACTOR | `commands/commands-P.md` | 260 |
| `PTConn` | `PG` | Immediate | PACTOR | `commands/commands-P.md` | 260 |
| `PTHUFF` | `PH` | 0 | PACTOR | `commands/commands-P.md` | 260 |
| `PTList` | `PN` | Immediate | PACTOR | `commands/commands-P.md` | 261 |
| `PTOver` | `PV` | <CTRL-Z> ($1A) | PACTOR | `commands/commands-P.md` | 261 |
| `PTSend` | `PD` | 1,2 | PACTOR | `commands/commands-P.md` | 261 |
| `QHpacket` | `QH` | 10/2 (Modems 10/2) | Packet | `commands/commands-Q.md` | 262 |
| `QMORse` | `QO` | 12 (Modem 12) | Morse | `commands/commands-Q.md` | 262 |
| `QRtty` | `QR` | 1 (Modem 1) | Baudot and ASCII RTTY | `commands/commands-Q.md` | 262 |
| `QSignal` | `QS` | 2 (Modem 2) | Signal | `commands/commands-Q.md` | 263 |
| `QTDm` | `QD` | 3 (Modem 3) | TDM | `commands/commands-Q.md` | 263 |
| `QTor` | `QT` | 2 (Modem 2) | AMTOR | `commands/commands-Q.md` | 263 |
| `QVpacket` | `QV` | 11/4  (Modems 11/4) | Packet | `commands/commands-Q.md` | 263 |
| `QWide` | `WI` | 7 (Modem 7) | All | `commands/commands-Q.md` | 264 |
| `Radio` | `RA` | 1/2 | All | `commands/commands-R.md` | 264 |
| `RAWhdlc` | `RW` | OFF | Packet | `commands/commands-R.md` | 264 |
| `RBaud` | `RB` | 45 bauds (60 WPM) | Baudot RTTY | `commands/commands-R.md` | 265 |
| `Rcve` | `RC` | Immediate | Baudot, ASCII, AMTOR, PACTOR, FAX, Morse | `commands/commands-R.md` | 265 |
| `RECeive` | `RE` | $04 <CTRL-D> | Baudot/ASCII/Morse/AMTOR/PACTOR/FAX | `commands/commands-R.md` | 265 |
| `REDispla` | `RD` | $12 <CTRL-R> | All | `commands/commands-R.md` | 266 |
| `RELink` | `RL` | OFF/OFF | Packet | `commands/commands-R.md` | 266 |
| `RESET` | `RS` | Immediate | Command | `commands/commands-R.md` | 266 |
| `RESptime` | `RP` | 0/0 (000 msec.) | Packet | `commands/commands-R.md` | 266 |
| `RESTART` | `RT` | Immediate | Command | `commands/commands-R.md` | 267 |
| `REtry` | `RY` | 10/10 | Packet | `commands/commands-R.md` | 267 |
| `RFec` | `RF` | ON | AMTOR | `commands/commands-R.md` | 267 |
| `RFRame` | `RG` | OFF | Baudot and ASCII RTTY | `commands/commands-R.md` | 268 |
| `RXRev` | `RX` | OFF | Baudot and ASCII RTTY/AMTOR | `commands/commands-R.md` | 268 |
| `SAmple` | `SA` | Immediate | Command | `commands/commands-S.md` | 269 |
| `SELfec` | `SE` | Immediate | AMTOR FEC | `commands/commands-S.md` | 270 |
| `SEGment` | `SG` | $70 | Command | `commands/commands-S.md` | 270 |
| `SEndpac` | `SP` | $0D <CTRL-M> | Packet | `commands/commands-S.md` | 270 |
| `SIgnal` | `SI` | Immediate | All | `commands/commands-S.md` | 271 |
| `SLottime` | `SL` | 30/30 (300 msec.) | Packet | `commands/commands-S.md` | 271 |
| `SPACE` | `Sp` | Current modem space frequency | Command | `commands/commands-S.md` | 271 |
| `SQuelch` | `SQ` | OFF/OFF | Packet | `commands/commands-S.md` | 272 |
| `SRXall` | `SR` | OFF | AMTOR | `commands/commands-S.md` | 272 |
| `STArt` | `ST` | $11 <CTRL-Q> | All | `commands/commands-S.md` | 272 |
| `STOp` | `SO` | $13 <CTRL-S> | All | `commands/commands-S.md` | 273 |
| `TBaud` | `TB` | 1200 bauds | All | `commands/commands-T.md` | 273 |
| `TClear` | `TC` | Immediate | Command | `commands/commands-T.md` | 273 |
| `TDBaud` | `TU` | 96 | TDM | `commands/commands-T.md` | 274 |
| `TDChan` | `TN` | 0 | TDM | `commands/commands-T.md` | 274 |
| `TDm` | `TV` | Immediate | TDM | `commands/commands-T.md` | 274 |
| `TEST` | `TE` | Immediate | Command | `commands/commands-T.md` | 275 |
| `TIme` | `TM` | $14 <CTRL-T> | All | `commands/commands-T.md` | 275 |
| `TMail` | `TL` | OFF | AMTOR, PACTOR | `commands/commands-T.md` | 275 |
| `TMPrompt` | `Tp` | (see text) | AMTOR/MailDrop | `commands/commands-T.md` | 276 |
| `TRACe` | `TR` | OFF | Packet/FAX/Baudot/AMTOR/PACTOR/Analog | `commands/commands-T.md` | 276 |
| `Trans` | `Not Supported` | Immediate | All | `commands/commands-T.md` | 277 |
| `TRFlow` | `TW` | OFF | Transparent | `commands/commands-T.md` | 277 |
| `TRIes` | `TI` | 0 | Packet | `commands/commands-T.md` | 277 |
| `TXdelay` | `TD` | 30/30 (300 msec.) | Packet, Baudot and ASCII | `commands/commands-T.md` | 278 |
| `TXFlow` | `TF` | OFF | Transparent | `commands/commands-T.md` | 278 |
| `TXRev` | `TX` | OFF | All | `commands/commands-T.md` | 278 |
| `UBit` | `UB` | 0 | All | `commands/commands-U.md` | 279 |
| `UCmd` | `UB` | 0 | PACTOR | `commands/commands-U.md` | 282 |
| `Unproto` | `UN` | CQ/CQ | Packet | `commands/commands-U.md` | 283 |
| `USers` | `UR` | 1/1 | Packet | `commands/commands-U.md` | 283 |
| `USOs` | `US` | OFF | Baudot RTTY | `commands/commands-U.md` | 284 |
| `Vhf` | `VH` | ON/ON | Packet | `commands/commands-V.md` | 284 |
| `VOltage` | `VO` | Immediate | Command | `commands/commands-V.md` | 284 |
| `WHYnot` | `WN` | OFF | Packet | `commands/commands-W.md` | 285 |
| `WIdeshft` | `WI` | OFF | All | `commands/commands-W.md` | 286 |
| `WOrdout` | `WO` | OFF | Baudot, ASCII, AMTOR, PACTOR and Morse | `commands/commands-W.md` | 286 |
| `WRu` | `WR` | OFF | Baudot, ASCII | `commands/commands-W.md` | 286 |
| `XBaud` | `XB` | 0 | ASCII/Baudot | `commands/commands-X.md` | 287 |
| `XFlow` | `XW` | ON | All | `commands/commands-X.md` | 287 |
| `Xmit` | `XM` | Immediate | Baudot/ASCII/Morse and FAX | `commands/commands-X.md` | 288 |
| `XMITOk` | `XO` | ON | All | `commands/commands-X.md` | 288 |
| `XOff` | `XF` | $13 <CTRL-S> | All | `commands/commands-X.md` | 288 |
| `XON` | `XN` | $11 <CTRL-Q> | All | `commands/commands-X.md` | 289 |
| `ZFree` | `ZF` | Immediate | All | `commands/commands-Z.md` | 289 |
| `ZStatus` | `ZS` | Immediate | All | `commands/commands-Z.md` | 290 |

## Coverage and gaps

- All 392 PDF pages were extracted with `pypdf` 6.x.
- Preface/warranty/FCC/shipping (p.1–4, p.324): stubbed; operational FCC notes kept.
- Appendix B schematic, Appendix C parts pictorial, Appendix D self-test: figure pages with little or no text.
- Tuning-indicator bar graphs and some FAX/CW illustrations do not extract; surrounding numeric settings were kept.
- Appendix E connection *figures* after the radio table are mostly drawings (p.318–323 nearly empty).
- DSP/PSK install drawings are figure-only; jumper and connector notes that extracted as text were kept.
- Hyphenation at line wraps was rejoined when the next token was clearly a word fragment. Mid-word page breaks without a hyphen were joined when the next page started lowercase.
- Compact-list aliases that are not separate dictionary entries: `K` = `CONVerse`; `PT` = `PACTor`; `CALibrat` = `CALibrate`. The compact list marks `CCITT` and `CUstom` as superseded by `CODe` / `UBit`. The compact list spelling `KILLONFWD` is the `KILONFWD` command.
- Do not treat this conversion as a substitute for the PK-900 Technical Manual (Host protocol).

