# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 10 — Signal Identification and TDM Operation (PDF p.151–156)

<!-- PDF p.151 -->

### 10.1 Overview

As you tune across the High-Frequency bands these days you find an
ever increasing number of digital signals.  These signals range from
the simple Murray Baudot code to ASCII and even packetized data.  With
the large number of speeds, formats and shifts now in use, it is
difficult to determine what kind of signal you are listening to.  Even
with a knowledge of digital communications, it is still time-consuming
to set the communication parameters correctly.

SIAM stands for Signal Identification and Acquisition Mode, and allows
a wide variety of digital signals to be automatically analyzed so they
can be easily copied with the PK-900.  SIAM will "listen" to a
signal for a few seconds and then display the type of signal and its
speed to the user.  The user can then decide whether or not to copy
the signal, or simply go on to the next signal.

SIAM makes the PK-900 more useful to the radio amateur and the
Short-Wave Listener.  Whether tuning across 20 meters, or searching
the Short-Wave bands, when you find a signal SIAM will help you decide
what it is, and tune it in without time-consuming trial and error.

NOTE:  Outside the Amateur bands, many of the RTTY signals employ
sophisticated encryption schemes and are not copyable by the PK-900.

### 10.2 SIAM  Operation

Before entering the Signal Identification mode, set the default modem
(QSIGNAL) to the number you wish to use from the list below.

1: RTTY/TOR 170: 2125/2295, 45 bps       2: RTTY/TOR 170: 2125/2295, 100 bps
3: RTTY/TOR 200: 2110/2310, 45 bps       4: RTTY/TOR 200: 2110/2310, 100 bps
5: RTTY/TOR 425: 2125/2550, 100 bps      6: RTTY/TOR 850: 2125/2975, 100 bps
7: RTTY/TOR 850: 2125/1275, 100 bps

If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the Signal mode.

If you are using a terminal, simply type "SIGNAL" or "SI" from the
Command Mode followed by the <Enter> key to enter the SIAM mode on
Radio Port 1.  The PK-900 responds by displaying the previous mode:

Opmode   was XXXXX
Opmode   now SIgnal

Now you are ready to tune in an unknown FSK signal.

<!-- PDF p.152 -->

#### 10.2.1 Tuning in FSK Narrow and Wide Stations

Tuning in the Frequency Shift Keying (FSK) signal properly is critical
to successful SIAM operation.  SIAM can only decode a signal properly
if it is tuned correctly.  Follow the tuning procedure below carefully
for the best results in tuning HF FSK stations.

- Set the default Signal Identification modem (QSIGNAL) to the
number you wish to use.

- Make certain your HF receiver is either in LSB or FSK depending
on your PK-900 setup.

- Turn OFF any IF-Shift and Passband-Tuning controls.

- Tune your receiver carefully across the band looking for the
distinctive two tone sound of an FSK signal.

- When you find a station, slowly vary the VFO tuning knob on
your receiver and look for a display on the PK-900 tuning
indicator like the one shown below.

--------------------------------------------------------------MARK |    ] ] ]              ] ] ]          | SPACE
--------------------------------------------------------------Tuning correct for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]  ]   ]  ]   ]   ]  ]           ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning correct for magic eye indicator (BAR 3)

--------------------------------------------------------------
|      ] ]       |
--------------------------------------------------------------Tuning correct for center tune indicator (BAR 2)

<!-- PDF p.153 -->

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

- Adjust the front panel THRESHOLD control so that the DCD LCD
lights when a properly tuned RTTY station is being received.

HINT:     If you adjust the volume control so the DCD LCD goes out
when no station is being received, you will prevent garbage
characters generated by noise from printing on your screen.

<!-- PDF p.154 -->

### 10.3 Using the SIAM Mode

After tuning in a signal as described above, make sure the THRESHOLD
is adjusted so the DCD LCD is lit.  Then after about 10 seconds the
PK-900 should respond with a baud rate indication and confidence
factor similar to the one shown below.

0.47:    50  Baud,

After another 15 seconds or so, the PK-900 should respond with one
of the following signal classes and tell whether or not the signal is
reversed by giving the status of the command RXREV:

ASCII AMTOR ALIST Baudot Unknown noise 6-bit TDM

The complete information from the PK-900 signal analysis will look
something like the following:

0.47    50  Baud,  Baudot,    RXREV  OFF

This means that the PK-900 has found the signal to be a 50-Baud
Baudot signal that is not inverted (since RXREV is OFF).  The 0.47
means that the PK-900 is 47% sure that the baud rate is correct.

SIAM can identify and copy ASCII, ARQ and FEC AMTOR, Baudot and TDM
signals.  To begin printing one of these signals, all that must be
done is to type the command OK after the analysis has been completed.
You should immediately begin to see text appear on your screen.

If the PK-900 determined the signal to be Unknown, 6-bit or noise
which it cannot decode, typing OK will cause the response:

?bad

The SIGNAL routine will run repeatedly until the operating mode is
changed either by typing OK, or forcing a change to another mode.  If
you tune to a different signal during an analysis, simply type SIGNAL
again to restart the analysis routine.

#### 10.3.1 Copying Encoded RTTY Transmissions

In the Short Wave bands many RTTY stations do not transmit in plain
text.  Most of these stations are using sophisticated encryption
techniques that make receiving them almost impossible.  There are a
few stations that use a relatively simple bit-inversion technique.
For these stations, the PK-900 has included the BITINV command.

If the text is not plain, but appears to be encoded, you can try
different settings of BITINV.  BITINV will Exclusive-OR a number from
$00 to $1F with the received character of a Baudot signal thus
inverting specific bits.  By varying BITINV from 0 through 31, you
will test all the different inversion possibilities that may encode a
Baudot signal.  If only simple bit-inversion is being used, one of
the BITINV settings should cause the transmission to become readable.
If none of the 32 possibilities reveal plain text, then the
transmitting station is likely using a more sophisticated technique.
Computer programmers may be interested in the 5BIT and 6BIT commands.

<!-- PDF p.155 -->

#### 10.3.2 The CODE command for International RTTY Compatibility

The CODE command allows the PK-900 to receive (and sometimes send)
other RTTY character sets.  Look up the CODE command in the Command
Summary Appendix for information on some of the other character sets
you may encounter on the HF bands.

### 10.4 TDM Receive Operation

The SIAM mode described above will recognize and decode TDM signals
for receive only.  The TDM receive mode can be entered directly simply
by typing TDM at the PK-900 command prompt.

TDM is an immediate command that places the PK-900 in the TDM
receive mode.  TDM stands for Time Division Multiplexing, also known
as Moore code and is the implementation of CCIR Recommendation 342.
The following describes the TDM mode and commands in detail.

#### 10.4.1 TDM Parameters

If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the TDM mode.

If you are using a terminal, simply type "TDM" from the Command Mode
followed by the <Enter> key to enter the TDM mode.  The PK-900
responds by displaying the previous mode:

Opmode   was PAcket
Opmode   now TDm

#### 10.4.2 Monitoring TDM Signals

The TDM command forces bit phasing; do this when changing frequency to
another TDM signal.  This is also useful when the PK-900
synchronizes on the wrong bit in the character stream, which is likely
on a signal which is idling.  TDM stations idle MOST of the time, so
you may have to leave the PK-900 monitoring for an hour or two
before any data is received.

TDM signals allow multiple data streams to share the same RF channel.
The PK-900 can receive either 1, 2 or 4 channel TDM signals.  When
monitoring 2 or 4 channel TDM, the TDCHAN command allows you to select
which channels will be displayed.  The TDCHAN command takes an
argument from 0 to 3 to allow any one of the four channels of a
4-channel TDM station to be monitored.

TDM signals operate at different data rates.  The TDBAUD command
allows any data rate from 0 to 200 baud to be selected, but only the
values in the following list are valid.

1-channel:  48,  72,  96
2-channel:  86,  96, 100
4-channel: 171, 192, 200

<!-- PDF p.156 -->

#### 10.4.3 Where to Find TDM Signals

We have heard TDM signals on the following frequencies which should be
used as a starting point when looking for TDM signals.

9.125.9  LSB    11.246.5  USB    12.061.7  USB    14.623.3  USB
14.956.7  USB    18.983.6  USB    19.101.9  LSB    19.647.4  LSB

The above signals were using several different shifts.
