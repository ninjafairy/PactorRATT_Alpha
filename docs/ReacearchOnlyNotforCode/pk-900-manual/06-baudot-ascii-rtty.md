# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 6 — Baudot and ASCII RTTY Operation (PDF p.99–114)

<!-- PDF p.99 -->

### 6.1 Overview

Baudot (pronounced Baw-dough) has been in use for many years.  The
five bit Baudot/Murray code was the basis of the Western Union Telex
service and Baudot RTTY (Radio TeleTYpe) is still widely used on the
HF amateur bands.  The Baudot character set contains the upper-case
letters, the numbers 0-9 and some common punctuation characters.
Because Baudot has only five bits, it is less prone to errors than
seven bit ASCII.  Your PK-900 provides Baudot RTTY at all standard
speeds in use today, including commercial speeds up to 300 bauds.

ASCII (pronounced Ask-kee), the American Standard Code for Information
Interchange has been in use for nearly 30 years.  ASCII is a 7-bit
code and was designed to overcome the limitations of the Baudot
character set by including both upper and lower case letters, numbers,
all punctuation as well as many computer control codes.  ASCII is not
as popular on the amateur bands, but its operation is almost identical
to Baudot RTTY so we will describe them both in this chapter.

Baudot and ASCII may be operated on Radio Port 1 on the PK-900.
Packet may be used on Radio Port 2 at the same time, so you won't miss
any connects while operating Baudot or ASCII on HF.

### 6.2 Where to Operate Baudot and ASCII RTTY

Before you can operate Baudot or ASCII RTTY, you must first know where
the activity is.  Most RTTY operation occurs on the 20-meter amateur
between 14.08 and 14.10 MHz.  RTTY activity can be found on the other
HF amateur bands as well and is most often located between 80 and 100
kHz up from the bottom of the band as it is on 20 meters.

#### 6.2.1 PK-900 Baudot RTTY Parameter Settings

First you must enter the Baudot mode of the PK-900.
If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the Baudot mode.

If you are using a terminal, simply type "BAUDOT" or "BA" from the
Command Mode followed by the <Enter> key to enter the Baudot mode.
The PK-900 responds by displaying the previous mode:

Opmode   was PAcket
Opmode   now BAudot

Your PK-900's front panel LCD display will show the Baudot
operating mode on Radio Port 1 and the SYSTEM COMMAND LCD will also
be on.

<!-- PDF p.100 -->

The following parameters are the most common settings for HF Baudot
operation.  Check the parameters and make sure they are set as follows:

RBAUD     45 (this is the most common amateur speed on HF)
RXREV     OFF
TXREV     OFF
MODEM     1

Note:  The command, QRtty, will automatically select a modem when you
enter the Baudot or ASCII mode.

#### 6.2.2 HF Receiver Settings

Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your PK-900 through the direct FSK keying lines.
In this case, you should select the FSK operating mode.  Adjust the
volume to a comfortable listening level.

#### 6.2.3 Tuning in Baudot and ASCII Stations

Tuning in Baudot and ASCII stations properly is critical to successful
operation.  Since HF Baudot and RTTY stations use either 170 Hz or 200
Hz Frequency Shift Keying to send data, tuning accuracy is very
important.  The command BARgraph will select the type of tuning
display.  (See below.)  Follow the tuning procedure below carefully
for the best results in tuning HF Baudot and ASCII stations.

- Make certain your HF receiver is either in LSB or FSK.

- Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.

- Tune your receiver carefully between 14.08 and 14.10 MHz (or
another band where you know there is Baudot or ASCII activity)
and listen for RTTY stations.

- When you find a station, slowly vary the VFO tuning knob on your
receiver and look for a display on the PK-900 tuning indicator
like the one shown below.

--------------------------------------------------------------MARK |    ] ] ]              ] ] ]          | SPACE
--------------------------------------------------------------Tuning correct for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]  ]   ]  ]   ]   ]  ]           ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning correct for magic eye indicator (BAR 3)

--------------------------------------------------------------
|      ] ]       |
--------------------------------------------------------------Tuning correct for center tune indicator (BAR 2)

<!-- PDF p.101 -->

If the tuning indicator looks like the one below, the frequency
from your speaker is too low for the PK-900 to copy the signal.
Slowly tune the VFO and make the frequency higher.

--------------------------------------------------------------MARK | ]   ]   ]   ]   ]   ]   ]              | SPACE
--------------------------------------------------------------Tuning too low for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]   ]   ]   ]   ]   ]   ]     ]        |
--------------------------------------------------------------Tuning too low for magic eye indicator (BAR 3)

--------------------------------------------------------------
|       ]   ]         |
--------------------------------------------------------------Tuning too low for center tune indicator (BAR 2)

If the tuning indicator looks like the one below, the frequency
from your speaker is too high for the PK-900 to copy the
signal.  Slowly tune the VFO and make the frequency lower.

--------------------------------------------------------------MARK |          ]   ]   ]  ]  ]  ]   ]  ] | SPACE
--------------------------------------------------------------Tuning too high for discriminator indicator (BAR 0)

--------------------------------------------------------------
|          ]    ]   ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning too high for magic eye indicator (BAR 3)

--------------------------------------------------------------
|          ]   ]      |
--------------------------------------------------------------Tuning too high for center tune indicator (BAR 2)

- Adjust the THRESHOLD control so the PK-900's port 1 DCD LCD
is on when a properly tuned RTTY station is being received.

HINT:     If you adjust the THRESHOLD control so the DCD LCD goes out
when no station is being received, you will prevent garbage
characters generated by noise from printing on your screen.

After you have an ASCII or RTTY station tuned in, you should start
seeing the copy printing on your screen.

<!-- PDF p.102 -->

NOTE:     If the text you are receiving is garbled, you may be tuned
to a transmission at a different baud rate.  Either try
tuning in a different station, or see Chapter 10 on
SIGNAL IDENTIFICATION to let the PK-900 determine the
kind of station you are listening to.

### 6.3 Transmitter Adjustments

Make sure your PK-900 is adjusted for your SSB transmitter as
described in section 3.5 and 3.5.2 of this manual before transmitting.
These are very critical adjustments.  If your PK-900's AFSK level
and transmitter microphone gain are not adjusted properly, other
stations will not be able to copy your signals.  Check the plate
current, collector current or power output of your rig before
transmitting.

#### 6.3.1 Going On The Air

Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using.  If you are using an AEA
PAKRATT program, see the program manual for the proper way to place
the PK-900 into RTTY transmit mode.

If you are using a terminal or terminal program, the following will
place your PK-900 and transceiver into the transmit mode.

- Make sure that you have selected Radio Port 1 by pressing the
CHSWITCH character defined in Chapter 4 followed by the number 0.

- Type "X" for XMIT and then press the <Enter> key to key your
transmitter and automatically enter the Converse mode.

As soon as you type the <Enter> key you will be transmitting.  At this
point you are also in the CONVERSE mode and anything you type will be
sent in Baudot by your transmitter.

When you are finished transmitting, use one of the following methods
to return to receive.

- Type <CTRL-D> (the RECEIVE character) to shut off your
transmitter and return to the Command Mode.

- Type <CTRL-F> (the CWID character) to send a Morse ID and shut
off your transmitter and return to Command Mode.

- Type <CTRL-C> (the COMMAND character) to return to the Command
Mode and then type "R" to shut down your transmitter and end the
contact.

See the following sections for a sample QSO as well as some Baudot
operating hints.

### 6.4 A Typical Baudot RTTY Contact

As with most amateur operating modes, you can start a contact eitherby "calling CQ" or by answering a "CQ" call by another station.

#### 6.4.1 Calling CQ

To call CQ first you must tell your PK-900 to start transmitting.

- Type "X" to key your transmitter and start the PK-900 sending.

- Type in your CQ message (use YOUR callsign) such as the one below:

CQ CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL
CQ CQ CQ CQ CQ CQ CQ DE YOURCAL YOURCAL YOURCAL K
<CTRL-D>

- Type <CTRL-D> at the end of your CQ call.  The <CTRL-D> puts both
your radio and the PK-900 into the receive mode.

- Wait a bit to see if you get a response.  If not, you can repeat
the above procedure.

#### 6.4.2 Answering a CQ

Let's assume you hear KZ7G calling CQ.  To answer, do the following:

- Type "X" to key your transmitter and start the PK-900 sending.

- Call the other station by giving his call followed by your call,
(KZ7G DE YOURCAL).  Start the transmission with a line of RYs as
a tuning signal for the distant station.  Here's an example:

RYRYRYRYRYRYRYRYRYRYRYRYRYRYRYRYRYR
KZ7G KZ7G KZ7G DE YOURCAL YOURCAL YOURCAL
KZ7G KZ7G KZ7G DE YOURCAL YOURCAL YOURCAL
KZ7G KZ7G KZ7G DE YOURCAL YOURCAL YOURCAL
<CTRL-D>

(If the other station can't copy these four lines of text, the
chances are he won't copy any more than that.  No need to waste
time and bandwidth by typing 15 or 20 lines of the same thing.)

- Type <CTRL-D> at the end of your call.  The <CTRL-D> puts both
your radio and the PK-900 into the receive mode.

Always end every transmission with a carriage return to force the
distant station's screen cursor or teleprinter back to the left margin
on a new line.  It's a good operating habit that keeps things neat.

- Wait a bit to see if you get a response.  If not, you can repeat
the above procedure.

### 6.5 Baudot RTTY Operating Tips

The PK-900 can automatically determine the speed of the receivedsignals with the SIGNAL IDENTIFICATION (SIAM) mode.  However, you can
manually step through all the available RTTY receiving speeds with the
RBAUD command.

The following "Function Keys" and immediate commands are included for
Baudot RTTY operating convenience.

Immediate Commands from the Command Mode:

"L"       Forces LETTERS case in receive.
"N"       Forces FIGURES case in receive.
"R"       Switches system to receive mode, forces LETTERS case.
"X"       Switches system to transmit mode and forces immediate
entry into Converse mode.
"K"       Go to CONVERSE Mode in order to load Transmit type
ahead buffer.

"Function Key" characters embedded in transmitted text:

<CTRL-B>  Sends AAB string as a HEREIS message.
<CTRL-E>  Sends "Who Are You" request to distant station.
<CTRL-O>  Sends LETTERS shift character.
<CTRL-N>  Sends FIGURES shift character.
<CTRL-D>  Shuts off transmitter after sending character buffer.
<CTRL-F>  Sends call sign in Morse and shuts off transmitter.
<CTRL-T>  Sends the Time if the DAYTIME clock has been set.

#### 6.5.1 Changing Speed

Assume you've been receiving at 45 bauds and wish to increase the baud
rate in steps.  From the Command mode, type RB U (Up) followed by an
<Enter>.  The PK-900 responds with:

RBaud   was 45
RBaud   now 50

The RBAUD command sets the Baudot RTTY speed.  The most common speed
is 45 bauds on HF, but other speeds including commercial speeds are
supported.  See the Command Summary for all the supported speeds.

#### 6.5.2 Formatting Your Transmitted and Received Text

The default configuration of the PK-900 RTTY parameters are set for
natural conversation and traffic.  Sometimes it is desired to alter
how your text looks on the screen of the station you are talking to.
The commands ACRRTTY and ALFRTTY allow for customizing the Carriage
Return and Linefeed characters in your transmitted text.

To allow for changing how received text is displayed on your screen or
printer, see the ACRDISP and ALFDISP commands in the Command Summary.

MARS operators have some special requirements for RTTY operation and
displaying text.  To accommodate these, the CRADD and MARSDISP
commands are included and should be reviewed in the Command Summary.

<!-- PDF p.105 -->

#### 6.5.3 Sending a Synchronous Idle or DIDDLE

Some RTTY users like to send an idle signal when no data is being
transmitted.  To allow for this the PK-900 has the DIDDLE command.
See the Command Summary for more information.

#### 6.5.4 Echoing Transmitted Characters As Sent (EAS)

Since Baudot RTTY at 45 baud is rather slow, some users like to know
when the characters are actually being sent.  The EAS command when ON
echoes characters to the display only when they are sent over the air.

#### 6.5.5 Sending Only Complete Words

Some RTTY users like to have their words sent out only when they are
complete.  This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character.  Turning WORDOUT ON
activates this feature.  See the Command Summary for more information.

#### 6.5.6 Operating on the Wrong Sideband

In RTTY operation it is important to operate on the correct sideband,
otherwise other stations will not be able to copy your transmissions.
If you find another station operating on the wrong sideband, you can
reverse your receive sense with the RXREV command so you will not have
to change sidebands yourself.

Similarly, if someone tells you that you are on the wrong sideband,
you can correct your transmit signal sense with the TXREV command.
See the Command summary for more information on these commands.

#### 6.5.7 Framing errors

Baudot and ASCII RTTY operation traditionally do not check for errors
and tend to be prone to receiving "garbage".  The PK-900 has the
ability to check for framing errors on received characters which
can reduce the amount of "garbage" characters on the screen.  To
reduce the amount of erroneous characters printed on the screen, turn
the command RFRAME ON (default OFF).  See the Command Summary for a
complete description of the RFRAME command.

#### 6.5.8 Unshift-On-Space (USOS)

The Unshift-On-Space (USOS Command) automatically changes the received
Baudot/Murray code characters to the LETTERS or lower case condition
after any "space" character is received.

When operating Baudot RTTY under poor conditions, a received LETTERS-SHIFT character can be garbled, or another character can be wrongly
interpreted as a FIGURES-SHIFT character.  Turning USOS ON helps
reduce reception errors under these conditions.

Some commercial, weather and utility RTTY services send groups of
numbers separated by spaces.  When receiving such non-amateur signals,
USOS should be OFF to prevent displaying LETTERS-shifted characters
when the originator may have intended the data to be FIGURES-shifted.

<!-- PDF p.106 -->

#### 6.5.9 Operating at Commercial or VHF Wide RTTY Shifts

Most commercial stations found in the non amateur Short Wave bands
operate with a wide Frequency Shift keying of either 425 or 850 Hz
shift.  To allow these stations to be received other modems are
available in the PK-900 and can be selected with the MODEM command.
The following modems are available for Baudot and ASCII operation:

Radio Port 1 Modems

MODEM  1  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 45  bps
MODEM  2  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 100 bps
MODEM  3  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz,  45 bps
MODEM  4  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz, 100 bps
MODEM  5  AFSK Modem, 425 Hz shift, M 2125 Hz, S 2550 Hz, 100 bps
MODEM  6  AFSK Modem, 850 Hz shift, M 2125 Hz, S 2975 Hz, 100 bps
MODEM  7  AFSK Modem, 850 Hz shift, M 2125 Hz, S 1275 Hz, 100 bps
MODEM 10  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz, 300 bps

If your license permits, you can also transmit to these stations when
the appropriate MODEM number and data rate is selected.

#### 6.5.10 The CODE Command for International RTTY Compatibility

The CODE command allows the PK-900 to receive (and sometimes send)
other RTTY character sets.  Part 97.69 of the FCC rules specifies that
the International Telegraph Alphabet Number 2 (ITA #2) must be used by
U.S. stations when operating RTTY.  This corresponds to the CODE 0
command (default), but you may want to see the CODE command for more
information on the capabilities of your PK-900.

#### 6.5.11 Copying Encoded RTTY Transmissions

In the Short Wave bands many RTTY stations can be found that are not
transmitting in plain text.  Most of these stations are using
sophisticated encryption techniques that make receiving them almost
impossible.  There are a few stations however that use a relatively
simple bit-inversion technique to make them hard to copy.  For these
stations, the PK-900 has included the BITINV command to allow the
SWL to decode these simple forms of encoded RTTY stations.

### 6.6 ASCII RTTY Operation

ASCII RTTY operation is almost identical to Baudot operation but there
are a few differences you must know.  Because the ASCII code uses
seven bits to define a character (instead of the five bits used in the
Baudot/Murray code), the probability of receiving errors is somewhat
higher.  For these reasons, ASCII is not used widely on the HF amateur
bands.  However, some commercial and military HF stations as well as
W1AW do use ASCII.

#### 6.6.1 Starting ASCII Operation

First you must enter the ASCII mode of the PK-900.
If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the ASCII mode.

<!-- PDF p.107 -->

If you are using a terminal, simply type "ASCII" or "AS" from the
Command Mode followed by the <Enter> key to enter the ASCII mode.

The PK-900 responds by displaying the previous mode:

Opmode   was BAudot
Opmode   now AScii

Your PK-900's front panel LCD Status display will show that you are in
the ASCII mode on Radio Port 1 and the COMMAND LCD will be on.

The following parameters are the most common settings for HF ASCII
operation.  Check the parameters and make sure they are set as follows:

ABAUD     110 (or whatever speed you wish)
RXREV     OFF
TXREV     OFF
MODEM     2 (or 10 for 300 baud operation)

Some VHF Bulletin Boards and MSOs use ASCII at 110 and 300 bauds,
most commonly on two meters.  Be sure that you have selected the
appropriate modem.  When you change mode to ASCII, the QRTTY command
will select a modem that is correct for Baudot but may not be
appropriate for 300 baud ASCII.

#### 6.6.2 ASCII RTTY Operating Tips

Follow the general operating procedures shown in the sections above
for Baudot RTTY.  As in Baudot operation, you can step the system
through all the available receiving speeds.

The following "Special Function Characters" and immediate commands are
included for ASCII RTTY operating convenience.

Immediate Commands From the Command Mode:

"R"       Switches system to receive mode.
"X"       Switches system to transmit mode and forces immediate
entry into Converse mode.
"K"       Go to CONVERSE Mode in order to load the Transmit type
ahead buffer.

Special Function Characters embedded in transmitted text:

<CTRL-B>  Sends AAB string as a HEREIS message.
<CTRL-D>  Shuts off transmitter after sending character buffer.
<CTRL-E>  Sends "Who Are You" request to distant station.
<CTRL-F>  Sends call sign in Morse and shuts off the transmitter.
<CTRL-T>  Sends the Time if the DAYTIME clock has been set.

#### 6.6.3 Changing ASCII Baud Rates

Assume you've been receiving at 110 bauds and wish to increase the
baud rate in steps.  From the Command mode, type AB U followed by an
<Enter>.  The PK-900 responds with:

<!-- PDF p.108 -->

ABaud   was 110
ABaud   now 150

The ABAUD command sets the ASCII RTTY speed.  The most common speed
is 110 bauds on HF, but other speeds including commercial speeds are
supported.  See the Command Summary for all the supported speeds.

#### 6.6.4 Operating at Commercial or VHF Wide ASCII RTTY Shifts

Most commercial stations found in the non amateur Short Wave bands
operate with a wide Frequency Shift keying of either 425 or 850 Hz
shift.  To allow these stations to be received other modems are
available in the PK-900 and can be selected with the MODEM command.

The following modems are available for Baudot and ASCII operation
on Radio Port 1:

MODEM  1  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 45  bps
MODEM  2  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 100 bps
MODEM  3  AFSK Modem, 200 Hz shift, M 2125 Hz, S 2550 Hz,  45 bps
MODEM  4  AFSK Modem, 200 Hz shift, M 2125 Hz, S 2975 Hz, 100 bps
MODEM  5  AFSK Modem, 425 Hz shift, M 2125 Hz, S 2550 Hz, 100 bps
MODEM  6  AFSK Modem, 850 Hz shift, M 2125 Hz, S 2975 Hz, 100 bps
MODEM  7  AFSK Modem, 850 Hz shift, M 2125 Hz, S 1275 Hz, 100 bps
MODEM 10  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz, 300 bps

If your license permits, you can also transmit to these stations when
the appropriate MODEM number and data rate is selected.

#### 6.6.5 Other RTTY Commands for ASCII Operation

Many of the commands mentioned above in the Baudot Section also
operate in the ASCII RTTY mode as well.  They are listed below:

AAB        ACRDISP    ALFDISP    DIDDLE     EAS   RFRAME
RXREV      TXREV      WORDOUT    WRU

### 6.7 Simultaneous RTTY and Packet Operation

Your PK-900 can operate on Baudot or ASCII RTTY on Radio Port 1 and
HF or VHF Packet on Radio Port 2 at the same time.  With this feature
you won't miss any local Packet activity while operating RTTY.

Before the second radio port can be used for packet operation, a
modem must be selected for the second port.  Use the MODem
command to select either or both modems, for example, MOD 1/4.  For
Radio Port 2, the command would be MOD /4 to select modem 4.

<!-- PDF p.109 -->

The Radio Port 2 modems are:

MODEM  1  AFSK Modem, 200  Hz shift, Mark=1270 Hz, Space=1070 Hz
MODEM  2  AFSK Modem, 200  Hz shift, Mark=2225 Hz, Space=2025 Hz
MODEM  3  AFSK Modem, 1000 Hz shift, Mark=1200 Hz, Space=2200 Hz
MODEM  4  AFSK Modem, 1000 Hz shift, Mark=1200 Hz, Space=2200 Hz eq
MODEM  5  AFSK Modem, 200  Hz shift, Mark=980  Hz, Space=1180 Hz
MODEM  6  AFSK Modem, 200  Hz shift, Mark=1650 Hz, Space=1850 Hz
MODEM  7  AFSK Modem, 800  Hz shift, Mark=1300 Hz, Space=2100 Hz
MODEM  8  AFSK Modem, 800  Hz shift, Mark=1300 Hz, Space=2100 Hz eq
MODEM  9  AFSK Modem, Direct FSK 9600 baud internal option.
MODEM 10  Modem disconnect header External option, user supplied.

#### 6.7.1 Selecting Modems

The various modems available in the PK-900 can be seen with the
DIR(ectory) command.  To display all the available modems simply
enter the Command Mode of the PK-900 and then type DIR as shown.

DIR <Enter>

The PK-900 will respond with the following:

- Port 1 -                              - Port 2 -
1: FSK 45 bps 170: 2125/2295            1: Internal 200: 1070/1270
2: FSK 100 bps 170: 2125/2295           2: Internal 200: 2025/2225
3: FSK 45 bps 200: 2110/2310            3: Internal 1000: 1200/2200
4: FSK 100 bps 200: 2110/2310           4: Internal 1000: 1200/2200 eq.
5: FSK 100 bps 425: 2125/2550           5: Internal 200: 1180/980
6: FSK 100 bps 850: 2125/2975           6: Internal 200: 1850/1650
7: FSK 100 bps 850: 1275/2125           7: Internal 800: 2100/1300
8: Analog 900/2500                      8: Internal 800: 2100/1300 eq.
9: FSK 2400 bps 800: 1300/2100          9: Internal option
10: FSK 300 bps 200: 2110/2310          10: Modem disconnect header
11: FSK 1200 bps 1000: 1200/2200
12: Morse 750
cmd:

Any modem from the list may be loaded with the MODEM command, but
only Radio Port 1 modems 1 through 7 and 10 should be used for
Baudot or ASCII RTTY.

For example, to operate 45 baud Baudot on radio port 1 and 1200 bps
VHF packet on radio port 2 you must load modem 1/4.  To load modem
1 from Radio Port directory 1 and modem 4 from Radio Port directory 2,
first enter the Command Mode  and then type MODEM 1/4 as
shown below:

MODEM 1/4 <Enter>

The PK-900 will respond with the following:

MODem was  x/x  (will show previous modems)
MODem now  1/4

When starting Baudot or ASCII RTTY operation, the PK-900 defaults tothe modem number set in the QRTTY command.  You may wish to change the
number in QRTTY to the modem number you prefer to use in Baudot RTTY.

#### 6.7.2 Displaying Received Data

The Radio command may be used to disable port 2.  This may be desirable when operating RTTY and you do not want to be disturbed with
any packet signals that may be received on radio port 2.  To disable
port 2, enter:

RADIO ON/OFF <Enter>     or      RADIO 1/0

To enable both radio ports, type:

RADIO ON/ON <Enter>     or     RADIO 1/2

When a Dual Port operation is selected, received data is displayed
from both Radio Ports at the same time. This allows you to operate
on HF and not miss any local Packet connects or information from DX
spotting nets.

The PK-900 sorts and displays received data from each Radio Port
using the same technique as multi-connect packet operation described
in Chapter 4.  That is, when operating on one port and the other
port becomes active, the displayed data from the inactive port is
shown prefaced by the "channel designator" followed by a colon (:).
Recall that Radio Port 1 is designated by "logical" channels from 0-9
and that Port 2 is designated by "logical" channels A-Z.  This is
true whether single port or dual port operation is selected.

#### 6.7.3 Switching Between Ports

If you are using an AEA PAKRATT program, switching between Radio Ports
is described in the program manual.  If you are using a terminal
program, this section describes how to direct your transmitted text.

Switching between RTTY on Port 1 and Packet on Port 2 is similar to
switching between Packet and Packet.  If you have not yet read through
the Switching Between Radio Ports section of Chapter 4, please do so
now and define a CHSWITCH character before reading the example below.

Recall from chapter 4 that the channels on Port 1 are labeled 0-9 and
the channels on Port 2 are labeled A-Z.  To select Radio Port 1 (RTTY)
press the CHSWITCH character you defined, followed by the number 0.
To select Radio Port 2 (Packet), press the CHSWITCH character,
followed by a letter from A-Z.

For example, you are conversing with an HF RTTY station and are in the
middle of a QSO when a station on VHF connects to you.  The follow
ing shows how your screen would look and suggests how you might
handle such an occurrence.  The underlined text is the text that you
type.

x <Enter>
Hello Jim, you are printing solid       { You send the RTTY station
here and have an S7 signal.<CTRL-D>       a signal report }
cmd:

<!-- PDF p.111 -->

THANKS BOB, YOU'RE ALSO A SOLID S7        { The other station responds
HERE AS WELL.                              with a signal report }

A:*** CONNECTED to WX7EEE               { WX7EEE connects to you on
VHF (Radio Port 2) }

<CTRL-C>cmd:x <Enter>
Thanks for the signal report Jim        { You make another transmission
only running 100 watts here.<CTRL-D>      to Jim on HF RTTY }
cmd:

Hey Bob, I'm going to the hamfest       { Your friend on VHF packet
this weekend if you want a ride.          wants to go to the hamfest }

k <<Enter>
|AHello Mike, I am on HF RTTY           { You switch to Port 2 by
talking to a station in Boston.       typing |A and respond on VHF }

0:WELL BOB, I HAD BETTER BE GOING TO    { Jim on HF RTTY signs off
BED.  WORK STARTS PRETTY EARLY 73.        with you. }

|0<CTRL-C>0:cmd:x
73 Jim, it was nice meting you.     { You switch back to HF with
WX1AAA de WX7BBB SK <CTRL-D>            the |0 and sign off with Jim }

As you may have noticed, communicating with different modes on the two
Radio Ports at the same time is almost identical to the method used in
chapter 4 for Packet and Packet operation.  Let's discuss the sample
QSOs above to see how the Port switching occurs.

The first text we see in the sample above is the signal report you are
sending to Jim, the HF RTTY station you are communicating with.
Notice that you must type an "x" followed by the Enter key on the
keyboard to place Radio Port 1 of the PK-900 into RTTY Transmit mode.
When you are through sending your signal report to Jim on HF RTTY, you
tell the PK-900 to return to receive by sending a <CTRL-D>.  The
PK-900 then responds with the Command Prompt "cmd:".

The next text you see is your signal report received from Jim on RTTY.

You are in the middle of a QSO with Jim on HF RTTY and all of a sudden
your friend WX7EEE connects to you.  WX7EEE has connected on Radio
Port 2 (VHF) which is shown by the "A:" before the connect message.
Remember that the 26 channel designators for Radio Port 2 are A-Z.

Before you respond to WX7EEE on VHF, you send a transmission to Jim on
HF RTTY telling him how much power your transmitter is running.  In
order to do this however you must first enter the command mode of the
PK-900 by typing a <CTRL-C> and then giving the RTTY Transmit
command "x" followed by the <Enter> key.  When you are finished with
your text, you command the PK-900 back to receive with a <CTRL-D>.

After making this transmission on HF RTTY, you see WX7EEE on VHF has
offered you a ride to the hamfest.  We know this text is from Radio
Port 2 since the previous packet displayed by the PK-900 was the
"A:*** CONNECTED" message from Port 2.

<!-- PDF p.112 -->

Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF.  This way he will understand
that it may take you a little longer to respond to his packets.
Before you can send data to Radio Port 2, you must enter Converse Mode
with the "k" command and then switch to this Port with "|A" or the
text you type will be sent to Radio Port 1 on RTTY the next time you
enter the transmit command "x".

You receive a transmission from Jim on HF RTTY telling you he needs to
sign off to go to bed.  The "0:" in front of the text shows this was
received on Radio Port 1.

Now you want to sign off with Jim on HF.  Again, first you must switch
to Radio Port 1 with the "|0" since your last transmission was
directed to Port 2.  After this you must enter the command mode of the
PK-900 by sending a <CTRL-C> and then place Port 1 into RTTY
transmit with the "x" command.  Now you can make your final
transmission to Jim ending with his callsign followed by your
callsign.  As before, when you are through typing your text, you send
a <CTRL-D> to the PK-900 which returns Radio Port 1 to receive after
the text has been sent.

#### 6.7.4 More Thoughts on Port Switching

One problem of having more than one Radio Port is remembering which
port you are currently using.  In the dual port sample QSOs above,
this was  not a problem, but after it has been hours or days since you
have used your PK-900, you may forget which port you last used.

With AEA Pakratt Software programs, the on-screen status will always
show which port you are using so this is not a problem.  With other
programs, you will have to query the PK-900 with the CSTATUS SHORT
command.  The CSTATUS command displays the status of the logical
channels of Port 1 and Port 2 of the PK-900.  The CSTATUS SHORT
command displays the status of the active channel and any packet
channels that are connected.  After completing the sample QSOs above,
the PK-900 would display the following.

cmd:CSTATUS S

Ch. A - IO DISCONNECTED

This reminds you that Channel A is your current I/O channel.  Any text
that you type in the Converse mode will be sent to channel A on Radio
Port 2.  If you had been connected to any other packet stations, the
callsign and channel would have also shown in the display.

Sometimes you might not want to be bothered with anything from the
Radio Port you are not using.  For these times either Radio Port may
be turned OFF with the RADIO command.  For example, let's say that in
the above example QSO you wanted to work HF packet and did not want to
be interrupted with any VHF connects.  Typing the following command
would cause Radio Port 2 to be disabled.

cmd:RADIO /0  or RADIO /OFF
RAdio  was  1/2
RAdio  now  1/0

<!-- PDF p.113 -->

When a PK-900 Radio Port is disabled, the front panel LCD indicators
for that port will be extinguished as a reminder.

<!-- PDF p.114 -->

> [No extractable text on this page — figure, schematic, or blank.]
