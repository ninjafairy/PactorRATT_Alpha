# Chapter 1 — Introduction

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 16–19).

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.16)

### 1.1

#### 1.1.1

#### 1.1.2

Overview
The DSP-2232 was designed by AEA to provide you the Amateur the
complete digital operating position when coupled with a Personal
Computer or Computer Terminal. The DSP-2232 couples your HF or
VHF/UHF (or both) voice transceivers to your computer or terminal so
you can use its keyboard and display to "talk" to other Amateurs.
Capabilities
The DSP-2232 allows you to transmit and receive all legal Amateur
digital modes that are popular on both HF and VHF.
In addition you
can send and receive black-and-white Weather FAX. The DSP-2232 can
receive other modes such as TDM, NAVTEX and bit-inverted Baudot RTTY .
These capabilities together with SIAM (Signal Identification and
Acquisition Mode) make the DSP-2232 ideal for the digital Short Wave
Listener as well.
The DSP-2232 with your Computer
receive the following modes:
or Terminal allows you to transmit and
AX. 25 Packet, both HF
Baudot and ASCII RTTY
AMTOR/SITOR CCIR Rec.
Morse Code
HF Weather FAX
Satellite Operation
PACTOR operation
and
476
VHF
and
625
( Chapter
( Chapter
( Chapter
( Chapter
( Chapter
( Chapter
( Chapter
In addition the DSP-2232 receives the following modes:
- NAVTEX marine broadcasts
- TDM (Time Divisi.on Multiplex) signals
- Bit-inverted Baudot RTTY
( Chapter
( Chapter
( Chapter
The DSP-2232 also has the following special features:
- SIAM for SWLing
4)
7)
8)
11)
12)
7)
10)
10)
(Chapter 10)
- PakMail MailDrop for Automatic Packet Message Handling (Chapter 5 )
- AMTOR MailDrop Operation
(Chapter 7 )
- KISS mode for TCP/IP and special Packet applications
(Appendix A)
(DSP Technical Manual)
- HOST mode for Host application programs
Included Components
Your DSP-2232 Data Controller package contains the following items :
One DSP-2232 Data Controller
Cables to connect your DSP-2232 to two separate radios
Connector package to help setup your DSP-2232
Radio Port "Loop-back" connector with jumper
RS-232 Serial Cable with a DB-9 and a DB-25 connector

### (PDF p.17)

### 1.2

### 1.3

#### 1.3.1

Computer or Computer Terminal Requirements
You will need a Computer or Computer Terminal to "talk to" or control
If you are using a Computer, you will need a
your DSP-2232.
Communications Program or Terminal Program as it is sometimes called.
The most popular computers are the IBM-PC and its compatibles, the
Apple Macintosh and the Commodore-64/128.
These, and most other
computers can be made to work with the DSP-2232.
Although not required, AEA has program packages for the IBM-PC and the
Macintosh computers that are customized for radio communications.
These packages are PC-PAKRATT 11 with FAX for the IBM-PC and
compatibles, and MACRATT with FAX for the Apple Macintosh. Details of
how to connect each of these computers to the DSP-2232 can be found in
Chapter 2 of this manual .
You may use other computers than those
mentioned above if the following technical requirements are met.
The Computer or Computer Terminal you plan to use must have an RS-232
Serial Communications port .
You will also need a Communications
Program that allows your computer to communicate over the RS-232 port
using the ASCII character set.
Details for connecting many computers
can be found in Chapter 2 of this manual.
Station Requirements
We presume that you already have an operating radio transceiver or
Short-Wave receiver to which you will connect your DSP-2232.
In the
Amateur bands most of the VHF activity occurs on the 2-meter FM band,
while most of the HF activity occurs on the 20-meter band. An HF
receiver or transceiver must be capable of SSB operation. While no
specific brand of transceiver is required, we recommend that a modern
transceiver (built in the last 20 years) capable of operation on one
of the two frequency bands mentioned above be used.
Specific
transceiver connections are described in Chapter 3 of this manual.
System Transmitter-Receiver Performance Requirements
Most modern radio transceivers are capable of excellent performance
in Morse, Baudot and ASCII RTTY, AMTOR and packet radio. Although
AMTOR Mode A (ARQ) operation imposes more demanding switching speed
requirements than the other operating modes, most radios will operate
in both AMTOR modes without any modifications. Radio switching times
See the AMTOR operating
are less critical in packet radio operation.
section for further details on timing requirements.
Your DSP-2232 provides software-controlled timing variations that
permits operation with nearly all the HF and VHF/UHF radios in general
use today.

### (PDF p.18)

### 1.4

#### 1.4.1

#### 1.4.2

DSP-2232 Specifications (printed 1-3)

OCR listed labels in one column and values in another. The pairings below are the ones that were unambiguous on the page. Uncertain pairings are marked `[?]`.

| Item | Value (PDF p.18) |
|---|---|
| Modulator / demodulator | Motorola 56001 DSP running at 24 MHz |
| DSP RAM | 24 Kilobytes (may hold two user-uploaded modems) |
| DSP ROM | Up to 128 Kilobytes of DSP modems, loaded by the Z-180 |
| ADC | AD7870 12-bit |
| DAC | AD767 12-bit |
| HF packet | 300 bauds FSK 2110/2310 Hz, also 1260/1460 Hz |
| VHF packet | 1200 bauds FSK 1200/2200 Hz |
| Other packet / sat | 2400 bps Packet DPSK; 1200 bps satellite BPSK; 9600 bps FSK K9NG; 1200/4800 bps ASCII satellite |
| HF RTTY FSK | 2125/2295 and 1445/1275 Hz; also 2125/2550, 1275/2125, 2125/2975 Hz |
| PACTOR | 2110/2310, 1460/1260 Hz |
| Morse | 750 Hz center frequency |
| FAX / SSTV | Facsimile FM and APT 256 gray levels; FM SSTV compatible 256 level |
| Dual-port ROM modems | Dual Port 300/1200 and 1200/1200 Packet; Dual Port RTTY-TOR/1200 baud Packet |
| RX band-pass VHF packet | Center 1700 Hz, bandwidth 2600 Hz |
| RX band-pass HF packet | Center 2210 Hz, bandwidth 450 Hz |
| RX band-pass CW | Center 750 Hz, bandwidth 200 Hz |
| Modulator | Phase-continuous AFSK |
| Output level | 5 to 100 mV RMS into 600 Ohms, side-panel pots each channel |
| Protocol CPU | Zilog Z-180 |
| RAM | 64 Kilobytes |
| ROM | Up to 384 Kilobytes (128 KB DSP ROM modems, 256 KB Z-180 programs) |
| Hardware HDLC | Zilog 8530 SCC |

Available ROM modem list continues as printed (automatically switched by operating mode).

Raw OCR (label column then value column):

```text
Modulator / Demodulator : Motorola 56001 Digital Signal Processor (DSP) running at 24 MHz
DSP RAM: 24 Kilobytes (May hold two user-uploaded modems)
DSP ROM: Up to 128 Kilobytes of DSP modems may be stored and are loaded by the Z-180.
ADC: AD7870 12-bit ADC
DAC: AD767 12-bit DAC
```

### (PDF p.19)

#### 1.4.3

#### 1.4.4

#### 1.4.5

Input/Output connections (printed ~1-4). Two-column OCR; pairings below are the readable ones.

| Item | Value (PDF p.19) |
|---|---|
| Radio interface | Two five-pin DIN connectors, simultaneous operation, software selectable |
| I/O lines | Receive audio, transmit audio, +/- PTT, external squelch, ground |
| Direct FSK outputs | Normal and reverse for each radio port. DSP-2232 voltages printed as `+25 / -40 VDC` `[?]` (DSP-1232 also mentioned on same lines) |
| CW keying | Positive: +100 VDC max, up to 100 mA. Negative: -30 VDC max, up to 20 mA |
| Satellite UP/DOWN | Frequency-control outputs for each port |
| Terminal | RS-232-C 9-pin DB-9P; full hardware and software handshake |
| Terminal data rates | Autobaud 110, 300, 600, 1200, 2400, 4800, 9600, 19200 BPS. `TBAUD` adds 150, 200, 400, 38400 BPS |
| Parallel printer | IBM compatible 25-pin bi-directional (DB-25) |
| Tuning | Ten-segment discriminator bargraph per radio port |
| Display | STATUS LCD (mode status); Power LED (green) |
| Per-channel LEDs | DCD, SEND (XMIT), MULT, STA, CON, TRANS, CONV, CMD |
| Power | +13 VDC (12 to 16 VDC) at 1100 mA |
| Mechanical | 12" x 9.8" x 2.9" (305 x 249 x 74 mm); 3 lb 12 oz (1.69 kg) |
