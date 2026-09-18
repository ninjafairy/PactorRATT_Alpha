# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 1 — Introduction (PDF p.15–18)

<!-- PDF p.15 -->

### 1.1 Overview

The PK-900 was designed by AEA to provide you the Amateur the
complete digital operating position when coupled with a Personal
Computer or Computer Terminal.  The PK-900 couples your HF or
VHF/UHF (or both) voice transceivers to your computer or terminal so
you can use its keyboard and display to "talk" to other Amateurs.

#### 1.1.1 Capabilities

The PK-900 allows you to transmit and receive all legal Amateur
digital modes that are popular on both HF and VHF.  In addition you
can send and receive black-and-white Weather FAX.  The PK-900 can
receive other modes such as TDM, NAVTEX and bit-inverted Baudot RTTY.
These capabilities together with SIAM (Signal Identification and
Acquisition Mode) make the PK-900 ideal for the digital Short Wave
Listener as well.

The PK-900 with your Computer or Terminal allows you to transmit and
receive the following modes:

- AX.25 Packet, both HF and VHF              (Chapter 4)
- Baudot and ASCII RTTY                      (Chapter 6)
- AMTOR/SITOR CCIR Rec. 476 and 625          (Chapter 7)
- Morse Code                                 (Chapter 8)
- HF Weather FAX                             (Chapter 9)
- PACTOR                                     (Chapter 11)

In addition the PK-900 receives the following modes:
- NAVTEX marine broadcasts                   (Chapter 7)
- TDM (Time Division Multiplex) signals      (Chapter 10)
- Bit-inverted Baudot RTTY                   (Chapter 10)

The PK-900 also has the following special features:

- SIAM for SWLing                                         (Chapter 10)
- PakMail Maildrop for Automatic Packet Message Handling   (Chapter 5)
- AMTOR MailDrop Operation                                 (Chapter 7)
- KISS mode for TCP/IP and special Packet applications    (Appendix A)
- HOST mode for Host application programs           (Technical Manual)
- Dualport operation with gateway                          (Chapter 4)

#### 1.1.2 Included Components

Your PK-900 Data Controller package contains the following items:

- One PK-900 Data Controller
- PK-900 Operating Manual (this manual)
- Cables to connect your PK-900 to two separate radios
- Connector package to help set up your PK-900
- Radio Port "Loop-back" connector with jumper
- RS-232 Serial Cable with a DB-25 connector

<!-- PDF p.16 -->

#### 1.1.3 OPTIONS

- 9600 Baud internal G3RUH/K9NG compatable modem.
- AEA-FAX 900 HF Gray-scale fax reception program

### 1.2 Computer or Computer Terminal Requirements

You will need a Computer or Computer Terminal to "talk to" or control
your PK-900.  If you are using a Computer, you will need a
Communications Program or Terminal Program as it is sometimes called.
The most popular computers are the IBM-PC and its compatibles, the
Apple Macintosh and the Commodore-64/128.  These, and most other
computers can be made to work with the PK-900.

Although not required, AEA has program packages for the IBM-PC and the
Macintosh computers that are customized for radio communications.
These packages are PC-PAKRATT II with FAX for the IBM-PC and
compatibles, and MACRATT with FAX for the Apple Macintosh.  Details of
how to connect each of these computers to the PK-900 can be found in
Chapter 2 of this manual.  You may use other computers than those
mentioned above if the following technical requirements are met.

The Computer or Computer Terminal you plan to use must have an RS-232
Serial Communications port.  You will also need a Communications
Program that allows your computer to communicate over the RS-232 port
using the ASCII character set.  Details for connecting many computers
can be found in Chapter 2 of this manual.

### 1.3 Station Requirements

We presume that you already have an operating radio transceiver or
Short-Wave receiver to which you will connect your PK-900.  In the
Amateur bands most of the VHF activity occurs on the 2-meter FM band,
while most of the HF activity occurs on the 20-meter band.  An HF
receiver or transceiver must be capable of SSB operation.  While no
specific brand of transceiver is required, we recommend that a modern
transceiver (built in the last 20 years) capable of operation on one
of the two frequency bands mentioned above be used.  Specific
transceiver connections are described in Chapter 3 of this manual.

#### 1.3.1 System Transmitter-Receiver Performance Requirements

Most modern radio transceivers are capable of excellent performance
in Morse, Baudot and ASCII RTTY, AMTOR and packet radio.  Although
AMTOR Mode A (ARQ) operation imposes more demanding switching speed
requirements than the other operating modes, most radios will operate
in both AMTOR modes without any modifications.  Radio switching times
are less critical in packet radio operation.  See the AMTOR operating
section for further details on timing requirements.

Your PK-900 provides software-controlled timing variations that
permits operation with nearly all the HF and VHF/UHF radios in general
use today.

<!-- PDF p.17 -->

### 1.4 PK-900 Specifications

As part of its program of product improvement, AEA reserves the right
to make changes in this product's specifications.  Changes may also be
made to the information in this document and incorporated in revisions
to this manual.  Prices and specifications are subject to change
without notice or obligation.

#### 1.4.1 Modem Characteristics

Port 1:
Demodulator:                  Programmable Limiter-discriminator type,
preceded by an 8-pole Chebychev 0.5-dB
ripple bandpass filter.
Receive Band-pass:            Automatically switched by operating mode
VHF packet:              Center frequency 1700 Hz,

HF (except CW)           Center frequencies: 1700,  2210,
2337.5 or 2550 Hz depending on
frequency shift chosen.
Bandwidth: 200, 470, 725, or 1150,
optimized for operating mode.

CW                       Center frequency 750 Hz,
bandwidth 200 Hz

Modulator                     Crystal controlled, 1 Hz step
programmable, phase-continuous, Direct
Digital Synthesis sine wave generator

Output Level:                 5 to 100 millivolts RMS into 600 Ohms,
adjustable by a rear-panel control

Port 2:
Modulator/Demodulator         AMD 7910 'World Chip' FSK Modem

Modem Tones:                  Bell 103 and 202

Output Level:                 5 to 100 millivolts RMS into 600 Ohms,
adjustable by a rear-panel control

Options:                      9600 baud direct FSK packet modem
(G3RUH/K9NG compatible)

Grey scale HF FAX receiving program

#### 1.4.2 Processor System

Protocol conversion:          Zilog Z-180  (64180) microprocessor
RAM:                          64 Kilobytes
ROM:                          Up to 256 Kilobytes of ROM may be used

Display and memory ARQ
processor:                    68HC05B4
DSS processor:                68HC05C4

Hardware HDLC:                Zilog 8530 SCC

<!-- PDF p.18 -->

#### 1.4.3 Input/Output Connections

Radio Interface:              Two five-pin DIN connectors;
Input/Output Lines          Receive audio
Transmit audio
+/- Push-To-Talk (PTT) (+25 / - 40 VDC)
External squelch input
Ground

Direct FSK Outputs:           Normal and reverse for each radio port

CW keying Outputs:            Positive: +100 VDC max, at up to 100 mA
Negative: -30 VDC max, at up to 20 mA

Terminal Interface:           RS-232-C 25-pin DB-25S connector
Input/Output                RS-232-C with full handshake (hardware
and software)

Terminal Data Rates         Autobaud selection of 110, 150, 300, 600,
1200, 2400, 4800, 9600 and 19200 BPS.

#### 1.4.4 Controls and Indicators

Front Panel Indicators:         Twenty-segment, selectable display
type:  discriminator, magic eye or
zero center bargraph indicator for
tuning radio port 1.

Power Switch                Front panel push-on push-off

Threshold Control           Front panel knob controlling DCD
sensitivity of radio port 1 demodulator

Status Display              LCD Status Display, with variable
intensity backlight, showing
Mode and data controller status
for both radio ports

#### 1.4.5 General

Power Requirements:           +13 VDC (12 to 16 VDC) at 1100 mA (max)
880 mA with LCD back light off

Mechanical:                   Overall, W 11 13/16" x D 12" x H 3.1/2"
(300 mm X 305 mm X 89 mm)
Weight 6 pounds 4 oz. (2.84
kilograms)
