# Chapter 8 — Morse Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 132–136).

**Agent extract:** Enter Morse from Command Mode (type `MORSE` in the scan). Modem **40**. Commands: `MSPEED`, `EAS`, `WORDOUT`, `LOCK`. Center 750 Hz. Special keystrokes are in this chapter; figure of the fist/tuning display did not OCR.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.132)

### 8.1

### 8.2

#### 8.2.1

#### 8.2.2

#### 8.2.3

Overview
The DSP-2232 will both send and receive International Morse Code.
The
computer based Morse operator can use the DSP-2232 to send "perfect"
code at much higher speeds than are typical of hand sent code.
As a rule, no machine can receive Morse as well as the FSK modes.
Your DSP-2232 is no exception. A strong signal and a good " fist"
are both required for the DSP-2232 to do a reasonable job of copying
Morse code.
Don't expect your DSP-2232 to do miracles and produce
good copy from bad fists !
Where to Operate Morse
Before you can operate Morse, you must first know where the activity
is. Morse operation is permitted on any amateur frequency, but most
often occurs in the lower 100 to 250 kHz of a band.
Entering the Morse Mode
If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the Morse mode.
If you are using a terminal, simply type "MORSE" or "MO" from the
Command Mode followed by the <Enter> key to enter the Morse mode on
Radio Port 1.
Packet operation on port 2 is dissabled when in the
Morse mode .
```text
Opmode
Opmode
```
HF Receiver
Set your HF
volume to a
The DSP-2232 responds by displaying the previous mode:
was PAcket
now MOrse
Settings
receiver (or transceiver) to che CW mode. Adjust the
Be certain that any IF-Shift
comfortable listening level.
and Passband Tuning controls are centered or set to the OFF position.
Tuning in Morse Stations
Tuning in Morse stations properly is critical to successful operation.
Follow the procedure below for the best results in tuning in Morse
stations.
Make certain your HF receiver is in the CW mode.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver carefully in the lower portion •of your
favorite amateur band and look for Morse signals.

### (PDF p.133)

When you find a station, slowly vary the VFO on your receiver and
look for a display on the DSP-2232 tuning indicator as shown
below when the station is "keyed down" .
Tuned In
(Key Down)
When the station is not "keyed down" or there is no station on
frequency, the tuning indicator should look like the one below.
Frequency
Qu i et
Adjust the receiver volume so that the DCD LED lights when a
properly tuned Morse station is being received.

### 8.3

_ 2/91
When a Morse station is tuned in, you should see the copy on your
screen. The DSP-2232 will track the speed of the received signal.
Going On The Air
Make sure that you have connected the DSP-2232 to your transmitter for
direct CW keying as discussed in Section 3.3. 6 of this manual.
Although the DSP-2232 is capable of morse transmission using Audio
Keying in the SSB mode of a transmitter, direct CW keying is
preferred. Most modern transmitters and transceivers are designed for
direct CW keying and often allow additional filtering to be switched
in for improved Morse reception when operating in this mode.
Adjust your transmitter for Morse operation as described in your
transmitter's manual. Make sure your antenna is tuned and adjusted
for the band and operating frequency you are using.
If you are using a terminal or terminal program, the following will
place your DSP-2232 and transceiver into the transmit mode.
Make sure that you have selected your transmitted text to go to
Port I by pressing the CHSWITCH character defined in Chapter 4
followed by a number from O through 9.
Type "X" for XMIT and then press the <Enter> key to key your
transmitter and automatically enter the Converse mode.
As soon as you type the <Enter> key you will be transmitting!
At this point you are also in the CONVERSE mode and anything you type
will be sent in Morse by your transmitter.

### (PDF p.134)

When you are finished transmitting, use one of the following methods
to return to receive.
Type <CTRL-D> to shut off your transmitter and return to the
Command Mode .
Type <CTRL-C> to return to the Cornmand Mode and then type "R"
shut down your transmitter and end the contact.
to

#### 8.3.1

### 8.4

See the following sections for some Morse operating hints.
Typical Morse Contact
Ag with most amateur operating modes, you can start a contact either
by "calling CQ" or by answering a "CQ" call by another station.
To call CQ first you must tell your DSP-2232 to start sending.
Make sure that you have selected your transmitted text to go to
Port I by pressing the CHSWITCH character defined in Chapter 4
followed by the number O.
Type "X" to key your transmitter and start the unit sending.
Type in your CQ message (use YOUR callsign) such as the one below:
CQ CQ CQ CQ CQ GQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL K
Type <CTRL-D> at the end of your CQ call. The <CTRL-D> puts both
your radio and the DSP-2232 into the receive mode after all the
text you have entered into the Transmit Buffer has been sent.
Wait a bit to see if you get a response.
If not, you can repeat
the above procedure.
Morse Operating Tips
The following "Special Function Characters"
included for Morse operat ing convenience.
Immediate Commands from the Command Mode:
and imrnediate commands are
" MO "
Locks system to the speed of the incoming signal.
Switches system to receive mode, unlocks receive speed,
forces receive speed to equal transmit speed
Switches system to transmit mode and forces immediate
entry into Converse mode.
Loads the Transmit type ahead buffer
Unlocks the Morse receive speed.
Special Function Characters embedded in transmitted text:
CTRL -D >
Shuts off the transmitter and returns the DSP-2232
to the Command Mode after sending the contents of the
transmit buffer.
Sends the TIME if the DAYTIME clock has been set.

### (PDF p.135)

#### 8.4.1

#### 8.4.2

#### 8.4.3

#### 8.4.4

#### 8.4.5

The DSP-2232 Morse Modem
The DSP-2232 uses a special modem (MODEM 40) for Morse (CW) operation.
This modem has a center frequency of 750 Hz. A 750 Hz tone is also
generated when Morse is transmitted and may be fed to a voice grade
transmitter for Tone modulated CW operation. This is not the way most
HF transceivers should be used, but VHF FM transceivers can use this
for transmitting code practice sessions. At this time, this is the
only Morse modem available in the DSP-2232 .
Speed Chanqe (MSPEED)
Use the MSPEED command to change Morse keying speed.
Type "MSPEED" followed by one or two digits from "5" to "99" and a
<Enter>. The DSP-2232 responds with the previous Morse speed.
MSPeed was 20
MSPeed now xx (whatever new speed digits you typed)
The number you enter becomes the new gransmit speed and replaces the
value used previously. The slowest Morse speed is 5 words per minute.
Echo inq Transmitted Characters As Sent (EAS
Since Morse can be rather slow, some users like to know just when the
characters are actually being sent. The EAS command when turned ON
will Echo characters to the display only when they are transmitted.
Sending Only. Complete Words (WORDOUT)
Some Morse users like to have their words sent out only when they are
compl ete .
This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character.
Turning WORDOUT ON
activates this feature.
See the Command Summary for more information.
Speed Lock (L0CK)
The LOCK command locks the system to the speed of the received signal.
This can help the reception of Morse code in the presence of noise.
To unlock the Morse speed and allow the DSP-2232 to track the received
signal, type "R" or "MO" followed by an <Enter>.

### (PDF p.136)

### 8.5

### 8.6

Special Morse Characters
The DSP-2232's Morse program contains special keystrokes which you can
use to make transmission easier, faster and more enjoyable. The most
frequently used Morse "prosigns" are coded into the keyboard with keys
that have no direct representation in standard Morse.
" reserved" keys are listed below:
Morse
Keystroke
* or
> or
Abbreviat ion
SK
AS
AR
SN
These special
Mean inq
End of QSO
Wait
End of message
Go only
Break or pause
Attention
Understand
New line
Umlaut O
Umlaut U
Swedish A
Swedish E
Morse Code Practice
Use your computer with your DSP-2232 to develop and improve your
manual CW sending and receiving skills.
Set your DSP-2232 for Morse receive operation on Radio Port 1 and
operate the hand key attached to your radio transceiver .
In most
installations your hand keyi.ng will be sent to the DSP-2232 and
displayed on your monitor if your radio has an audio " input monitor"
or "sidetone" output and you •ve turned on those monitor functions .
The frequency of the monitor sidetone must be nearly 750 Hz or the
DSP-2232 will not be able to copy it.
Send test words to familiarize yourself with the relationship between
your hand-keying and the Morse appearing on your screen.
Practice keying at various speeds; observe how the system decodes your
" f ist "
You may be a bit unhappy or surprised at the quality of your
keying but after a few sessions you ' II notice an improvement.
Last page of Chapter 8
- MORSE Operation
