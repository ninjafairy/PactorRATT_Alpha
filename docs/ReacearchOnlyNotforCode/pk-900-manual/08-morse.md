# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 8 — Morse Operation (PDF p.137–142)

<!-- PDF p.137 -->

### 8.1 Overview

The PK-900 will both send and receive International Morse Code.  The
computer based Morse operator can use the PK-900 to send "perfect"
code at much higher speeds than are typical of hand sent code.

As a rule, no machine can receive Morse as well as the FSK modes.
Your PK-900 is no exception.  A strong signal and a good "fist"
are both required for the PK-900 to do a reasonable job of copying
Morse code.  Don't expect your PK-900 to do miracles and produce
good copy from bad fists!

### 8.2 Where to Operate Morse

Before you can operate Morse, you must first know where the activity
ocurrs.  Morse operation is permitted on any amateur frequency, but
most often occurs in the lower 100 to 250 kHz of each band.

#### 8.2.1 Entering the Morse Mode

If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the Morse mode.

If you are using a terminal, simply type "MORSE" or "MO" from the
Command Mode followed by the <Enter> key to enter the Morse mode on
Radio Port 1.  Packet operation on port 2 is disabled when in the
Morse mode.  The PK-900 responds by displaying the previous mode:

Opmode   was PAcket
Opmode   now MOrse

#### 8.2.2 HF Receiver Settings

Set your HF receiver (or transceiver) to the CW mode.  Adjust the
volume to a comfortable listening level.  Be certain that any IF-Shift
and Passband Tuning controls are centered or set to the OFF position.

#### 8.2.3 Tuning in Morse Stations

Tuning in Morse stations properly is critical to successful operation.
Follow the procedure below for the best results in tuning in Morse
stations.

- Make certain your HF receiver is in the CW mode.

- Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.

- Tune your receiver carefully in the lower portion of your
favorite amateur band and look for Morse signals.

<!-- PDF p.138 -->

- When you find a station, slowly vary the VFO on your receiver and
look for a display on the PK-900 tuning indicator as shown
below when the station is "keyed down".

--------------------------------------------------------------
| ]   ]   ]   ]   ]   ]   ]   ]    ]    ]    ]      |
--------------------------------------------------------------

When the station is not "keyed down" or there is no station on
frequency, the tuning indicator should look like the one below.

--------------------------------------------------------------
| ]    ]                                                       |
--------------------------------------------------------------Frequency quiet

- Adjust the receiver volume so that the DCD LCD lights when a
properly tuned Morse station is being received.

When a Morse station is tuned in, you should see the copy on your
screen.  The PK-900 will track the speed of the received signal.

### 8.3 Going On The Air

Make sure that you have connected the PK-900 to your transmitter for
direct CW keying as discussed in Section 3.3.6 of this manual.
Although the PK-900 is capable of Morse transmission using Audio
Keying in the SSB mode of a transmitter, direct CW keying is
preferred.  Most modern transmitters and transceivers are designed for
direct CW keying and often allow additional filtering to be switched
in for improved Morse reception when operating in this mode.

Adjust your transmitter for Morse operation as described in your
transmitter's manual.  Make sure your antenna is tuned and adjusted
for the band and operating frequency you are using.

If you are using a terminal or terminal program, the following will
place your PK-900 and transceiver into the transmit mode.

- Make sure that you have selected your transmitted text to go to
Port 1 by pressing the CHSWITCH character defined in Chapter 4
followed by a number from 0 through 9.

- Type "X" for XMIT and then press the <Enter> key to key your
transmitter and automatically enter the Converse mode.

As soon as you type the <Enter> key your transmitter PTT will be
activated.  At this point you are also in the CONVERSE mode and
anything you type will be sent in Morse by your transmitter.

<!-- PDF p.139 -->

When you are finished transmitting, use one of the following methods
to return to receive.

- Type <CTRL-D> to shut off your transmitter and return to the
Command Mode.

- Type <CTRL-C> to return to the Command Mode and then type "R" to
shut down your transmitter and end the contact.

See the following sections for some Morse operating hints.

#### 8.3.1 A Typical Morse Contact

As with most amateur operating modes, you can start a contact either
by "calling CQ" or by answering a "CQ" call by another station.
To call CQ first you must tell your PK-900 to start sending.

- Make sure that you have selected your transmitted text to go to
Port 1 by pressing the CHSWITCH character defined in Chapter 4
followed by the number 0.

- Type "X" to key your transmitter and start the unit sending.

- Type in your CQ message (use YOUR callsign) such as the one below:

CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL K <CTRL-D>

- Type <CTRL-D> at the end of your CQ call.  The <CTRL-D> puts both
your radio and the PK-900 into the receive mode after all the
text you have entered into the Transmit Buffer has been sent.

- Wait a bit to see if you get a response.  If not, you can repeat
the above procedure.

### 8.4 Morse Operating Tips

The following "Special Function Characters" and immediate commands are
included for Morse operating convenience.

Immediate Commands from the Command Mode:

"L"       Locks system to the speed of the incoming signal.
"R"       Switches system to receive mode, unlocks receive speed,
forces receive speed to equal transmit speed
"X"       Switches system to transmit mode and forces immediate
entry into Converse mode.
"K"       Loads the Transmit type ahead buffer
"MO"      Unlocks the Morse receive speed.

Special Function Characters embedded in transmitted text:

<CTRL-D>       Shuts off the transmitter and returns the PK-900
to the Command Mode after sending the contents of the
transmit buffer.
<CTRL-T>       Sends the TIME if the DAYTIME clock has been set.

<!-- PDF p.140 -->

#### 8.4.1 The PK-900 Morse Modem

The PK-900 uses a special modem (MODEM 12) for Morse (CW) operation.
This modem has a center frequency of 750 Hz.  A 750 Hz tone is also
generated when Morse is transmitted and may be fed to a voice grade
transmitter for Tone modulated CW operation.  This is not the way most
HF transceivers should be used, but VHF FM transceivers can use this
for transmitting code practice sessions.

#### 8.4.2 Speed Change (MSPEED)

Use the MSPEED command to change Morse keying speed.

Type "MSPEED" followed by one or two digits from "5" to "99" and a
<Enter>.  The PK-900 responds with the previous Morse speed.

MSPeed   was 20
MSPeed   now xx (whatever new speed digits you typed)

The number you enter becomes the new transmit speed and replaces the
value used previously.  The slowest Morse speed is 5 words per minute.

#### 8.4.3 Echoing Transmitted Characters As Sent (EAS)

Since Morse can be rather slow, some users like to know just when the
characters are actually being sent.  The EAS command when turned ON
will Echo characters to the display only when they are transmitted.

#### 8.4.4 Sending Only Complete Words (WORDOUT)

Some Morse users like to have their words sent out only when they are
complete.  This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character.  Turning WORDOUT ON
activates this feature.  See the Command Summary for more information.

#### 8.4.5 Speed Lock (LOCK)

The LOCK command locks the system to the speed of the received signal.
This can help the reception of Morse code in the presence of noise.

To unlock the Morse speed and allow the PK-900 to track the received
signal, type "R" or "MO" followed by an <Enter>.

<!-- PDF p.141 -->

### 8.5 Special Morse Characters

The PK-900's Morse program contains special keystrokes which you can
use to make transmission easier, faster and more enjoyable.  The most
frequently used Morse "prosigns" are coded into the keyboard with keys
that have no direct representation in standard Morse.  These special
"reserved" keys are listed below:

Morse           Keystroke          Abbreviation         Meaning
...-.-           * or <                 SK             End of QSO
.-...              &                    AS             Wait
.-.-.              +                    AR             End of message
-.--.              (                    KN             Go only
-...-              =                    BT             Break or pause
-.-.-            > or %                 KA             Attention
...-.              !                    SN             Understand
.-.-               [                    AA             New line
---.               \                    o              Umlaut O
..--               ^                    u              Umlaut U
.--.-              ]                    a              Swedish A
..-..              @                    e              Swedish E

### 8.6 Morse Code Practice

Use your computer with your PK-900 to develop and improve your
manual CW sending and receiving skills.

Set your PK-900 for Morse receive operation on Radio Port 1 and
operate the hand key attached to your radio transceiver.  In most
installations your hand keying will be sent to the PK-900 and
displayed on your monitor if your radio has an audio "input monitor"
or "sidetone" output and you've turned on those monitor functions.
The frequency of the monitor sidetone must be nearly 750 Hz or the
PK-900 will not be able to copy it.

Send test words to familiarize yourself with the relationship between
your hand-keying and the Morse appearing on your screen.

Practice keying at various speeds; observe how the system decodes your
"fist".  You may be a bit unhappy or surprised at the quality of your
keying but after a few sessions you'll notice an improvement.

<!-- PDF p.142 -->

> [No extractable text on this page — figure, schematic, or blank.]
