# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 9 — Facsimile and SSTV Operation (PDF p.143–150)

<!-- PDF p.143 -->

### 9.1 Overview

While facsimile and SSTV are not digital modes, the personal computer
coupled with a high resolution monitor makes an excellent display for
these images.  The PK-900 contains modems that can receive FM
facsimile signals as well amateur Slow Scan Television (SSTV).

The PK-900 filters and digitizes the received audio signal for
processing by software running on your personal computer.  Therefore
this chapter only provides an overview of FAX and SSTV operation.
The program used to display FAX and SSTV should provide operating
instructions and detailed information about the modes it supports.

Note:  The Analog mode is intended to replace the old black & white
FAX mode originally developed for the PK-232.  As of October 1992,
there is no display software yet available for SSTV.  Gray scale FAX
is supported by the AEA FAX program.

The black & white FAX mode is retained only to support the PK-FAX and
MACRATT with FAX programs while new display software is developed.

#### 9.1.1 Facsimile

Facsimile is the term used to describe the transmission of black &
white images.  There are several different Facsimile standards in use.
Over the telephone, either "Group-2" or "Group-3" transmission
standards are used.  Sending facsimile by radio has some special
problems so different and incompatible transmission methods are used.

There are two basic types of facsimile signals transmitted over radio.
Frequency Modulated (FM) facsimile is often found in the HF Short
Wave bands, while an Amplitude Modulated (APT) system is used by
satellites in the VHF and Microwave bands.

Weather Facsimile (FM WEFAX) is transmitted throughout the Short-Wave spectrum, primarily to provide information to ships at sea.
Typical stations you may find broadcast maps with weather conditions
and satellite photographs showing cloud cover over a large area.
Not only weather information is transmitted, but also news
photographs from the wire services (see the frequency list below).

With FM facsimile, the picture information is modulated in an audio
tone between 1,500 Hz (Black) and 2,300 Hz (White).  In between
these two frequencies are shades of gray.  The PK-900's analog modem
receives these signals and provides an output on pin 6 of the serial
connector when Modem 8 is selected.

The PK-900 does not have a demodulator capable of receiving the
satellite amplitude modulated (APT) mode, however an AM to FM
converter is available from:

Overview Systems        or call:  Tim  Heffield, N4IFP
P.O. Box 130014                   305 748-8315
Sunrise FL 33313

<!-- PDF p.144 -->

#### 9.1.2 Slow Scan Television

Amateur Slow Scan Television (SSTV) originated in 1953 and is
presently found primarily in the 20 meter amateur band.  Over the
years SSTV has evolved a great deal and many different formats now
exist for both black & white as well as color image transmission.

SSTV uses a Frequency Modulation scheme similar to FM facsimile where
the video information is usually transmitted by an audio tone between
1,500 Hz and 2,300 Hz.  In SSTV however additional synchronizing
information is often necessary and is typically sent with tones
between 1,100 and 1,500 Hz.  For this reason SSTV requires a
different modem than is used in FM Facsimile.  Modem 8, the analog
modem may also be used for SSTV with your computer program.

### 9.2 Finding FAX Frequencies

The following is a list of HF FM facsimile frequencies that seem to
broadcast on a regular schedule.  A few of these stations transmit
24-hours/day.  We suggest you try the weather FAX frequencies listed
below while you are becoming familiar with facsimile operation.
After displaying a few images, and you are accustomed to the sound of
facsimile you will be able to tune the bands in search of other
frequencies where perhaps different kinds of pictures may be found.

Weather: USB   3,357.0 kHz   4,268.0 kHz   4,975.0 kHz   6,946.0 kHz
10,865.0 kHz  12,125.0 kHz  20,015.0 kHz

Photographs:   LSB          10,680.7 kHz  17,673.9 kHz  18,434.9 kHz

The following HF facsimile frequencies were obtained from Popular
Communications Magazine:

USB

4,271.0 kHz   9,890.0 kHz  13,510.0 kHz                Halifax, Canada
8,502.0 kHz  12,750.0 kHz                              Boston, Mass
9,389.5 kHz  11,035.0 kHz                              Brentwood, NY
4,793.5 kHz  10,185.0 kHz  12,201.0 kHz  14,671.5 kHz  Washington, DC
9,157.5 kHz  17,447.5 kHz                              Mobile, Al
8,080.0 kHz  10,854.0 kHz  16,410.0 kHz                Norfolk, VA
4,802.5 kHz   9,440.0 kHz  13,862.5 kHz                Hawaii
7,770.0 kHz  11,090.0 kHz  13,627.5 kHz                Hawaii
8,459.0 kHz                                            Alaska
4,346.0 kHz   8,682.0 kHz  12,730.0 kHz  17,151.2 kHz  San Francisco
8,646.0 kHz  17,410.5 kHz                              San Diego

VHF and Microwave APT Facsimile Satellite Frequencies

1691.0 MHz    GOES Geosynchronous Weather Satellite (USA)
1694.5 MHz    METEOSAT Geosynchronous Weather Satellite (ESA)

136.0-138.0 MHz     Orbiting WEFAX APT Weather Satellites

Table 1:  Facsimile Frequencies

<!-- PDF p.145 -->

#### 9.2.1 Finding SSTV Frequencies

In the US, SSTV is presently most popular on the 20 meter HF amateur
band.  Most evenings an active group of SSTV enthusiasts can be
found on 14.230 and 14.233 MHz.  This is a good place to start
looking for signals.  Once you recognize the different sounds of
common SSTV signals, you may find activity on other frequencies and
bands especially as propagation changes.

### 9.3 FAX and SSTV Analog Signal Operation

To accommodate the characteristics of gray-scale FAX and SSTV analog
signals, the PK-900 uses a special Analog mode.  Software programs
to display images should automatically set the PK-900 Analog
parameters so most users need not concern themselves with the
following information.  If you are curious how the PK-900 works in
this mode, or are interested in writing your own display software,
this information may be helpful.

Note: The Analog mode requires special display software that has been
written for each family of personal computer be used. Software
is necessary in order to handle the gray scale FAX and SSTV
images.  This software is not provided with the PK-900
because the unit operates with many different types of computers.
Without display software, the Analog mode should not be used.

The Analog mode is designed to pass data for communications methods
that require "gray scales" or color such as FAX and SSTV.  In HF FAX
and SSTV signals, the information is contained in an audio signal
that varies from 1,500-2,300 Hz for FAX and 1,100-2,300 Hz for SSTV.

The analog modem is designed to bandpass filter the analog signal.
The filtered signal is then passed to a limiter - zero crossing
detector.  The detected signal is further filtered to reduce noise
then digitized.  This signal is converted to RS-232 levels and
switched to pin 6 of the DB-25 serial connector.  This pin is used for
data only when modem 8 is selected.

<!-- PDF p.146 -->

### 9.4 Black & White FAX Operation

This mode of black & white FAX operation will eventually be deleted
from the PK-900 since the Analog mode has been created.  Still, if
you own an older AEA PAKRATT WITH FAX program, you should have the
ability to display weather FAX on the screen of your PC.  If you are
using an AEA PAKRATT WITH FAX program, follow the program manual
instructions to run the program with the PK-900.

To hook-up the PK-900 for facsimile operation the radio must be
connected to Radio Port 1.  If all you want is to receive FAX, you
only need to connect the audio from your receiver.  If you wish to
transmit as well, follow the instructions in Chapter 3 of this manual
for complete Radio Connections.

NOTE:     Facsimile transmissions contain such a large amount of data
that the PK-900 can not support Packet operation on Radio
Port 2 while in the Facsimile mode.

#### 9.4.1 HF Receiver Settings

Set your HF receiver (or transceiver) to upper Sideband (USB) and
disable any IF-Shift or Passband-tuning controls.  Adjust the volume
to a comfortable listening level.

#### 9.4.2 Tuning In HF Facsimile Stations

Facsimile is most often found on Upper Sideband and sounds similar to
monitoring an AMTOR QSO with both stations being of equal strength.
The most common facsimile signals are WEFAX, so we have set the
PK-900 FAX default parameters to copy weather charts and many
satellite photographs.  We recommend starting with one of the
listed weather frequencies, or frequencies from Popular Communications
in Table 1 when first receiving facsimile.

Upon tuning into a WEFAX signal, you will notice that the facsimile
sound seems to repeat at the rate of twice a second.  This is the
horizontal scan frequency, and allows you to distinguish different
facsimile services by speed.  Common horizontal scan rates are 2 lines
per second, which is typically used in weather facsimile broadcasts,
1 line per second for photographs, and 4 lines per second for some
foreign facsimile stations.  Listen for these repetition rates as you
tune across the bands in search of new pictures.

The PK-900 uses a center frequency of 1.9 kHz for copying facsimile
transmissions.  As a result, you must tune 1.9 kHz lower than the
frequencies listed in Table 1 when using Upper Sideband.  Similarly,
in Lower Sideband, one must tune 1.9 kHz higher than the frequencies
listed in the table.

<!-- PDF p.147 -->

The PK-900 LCD bar-graph should be tuned so that the facsimile
signal is roughly centered in the display as shown in Figure 1a
below.  If the audio frequency is too low, the bar-graph will look
something like Figure 1b.  If the audio frequency is too high, it
will look something like Figure 1c.  Facsimile tuning is not
especially critical when copying WEFAX, but a properly tuned signal is
needed when displaying facsimile photographs.

--------------------------------------------------------------
|               ]   ]   ]   ]   ]   ]   ]    ]    ]    ] ]                  |
--------------------------------------------------------------Figure 1a.   Properly Tuned

--------------------------------------------------------------
| ]   ]   ]   ]   ]   ]   ]   ]    ]    ]    ]      |
--------------------------------------------------------------

Figure 1b.   Frequency too low

--------------------------------------------------------------
|                               ]  ]   ]   ]   ]   ]   ]   ]    ]    ]    ] |
--------------------------------------------------------------Figure 1c.   Frequency too high

Figure 1:  Facsimile tuning indicator conditions for FAX tuning

#### 9.4.3 PK-900 Facsimile Parameter Settings

To start receiving WEFAX broadcasts on your computer screen, follow
the setup instructions in your PAKRATT WITH FAX program manual.

Now all that is necessary is to put the PK-900 into FAX mode by
typing FAX. The PK-900 will respond with:

Opmode  now  FAx

### 9.5 Receiving Facsimile Broadcasts

The PK-900 is now in the Facsimile Standby-Receive mode which means
it is waiting for a synchronization signal from a facsimile
transmitter to begin a new picture.  Verify this by entering the
OPMODE command.  The PK-900 should respond with:

Opmode  FAX    STBY  RCVE

At this point make sure the receiver volume is high enough so that
the DCD LCD lights, otherwise nothing will be displayed.  If you do
not want to wait for the beginning of a new picture, you may type

Lock

<!-- PDF p.148 -->

This forces a synchronization-lock, and starts the FAX displaying
regardless of what kind of signal is being monitored.  Since this
synchronization lock was not sent by the transmitting station, the
picture will probably not be correctly positioned on the page.
Rather, it will likely appear to be split in half with the left half
of the picture on the right half of the page, and the right half of
the picture on the left half of the page.

To correct for this, a justification command has been included that
allows the user to shift the entire image to the left in 1/2 inch
increments.  For example if the left-edge of the picture appears
roughly 4-1/2 inches away from the left edge of the paper, issuing the
command JUSTIFY 9 will shift the left edge of the picture to the left
4-1/2 inches (9 X 0.5 inch) correcting its justification.

The PK-900 will respond with:

JUstify  now  9

This procedure will not be necessary if the PK-900 synchronizes from
the transmitted facsimile signal.  This procedure is also not
necessary if you are using an AEA PAKRATT with FAX program.  AEA
programs allow the image to be justified after it has been received.

To stop the display, you may either exit the facsimile mode by
changing to another mode or enter the RCVE command, which puts the
PK-900 back into facsimile standby receive.  Standby receive is the
same as if you have just entered the FAX mode from another mode.  The
PK-900 will wait until it receives the synchronization signal from a
facsimile station before beginning to display again.

### 9.6 Facsimile Operating Tips

If you are using an AEA PAKRATT WITH FAX program, consult the
program manual for more information and helpful hints.

#### 9.6.1 Setting PRTYPE

Before FAX can be sent to your compute, you must choose the correct
"printer" type with the PRTYPE Command.  AEA programs do this for you
automatically.  The PK-900 defaults to the Epson format.  See the
PRTYPE command in the Command Summary for the other types supported.

#### 9.6.2 Printing Direction (LEFTRITE)

Weather FAX as well as most other facsimile displays from left to
right, but occasionally you may find a station that is reversed.  If
you come across such a transmission, you may simply issue the command
LEFTRITE OFF to correct this. The PK-900 will respond with:

LEftrite  now  OFF

<!-- PDF p.149 -->

#### 9.6.3 Inverting Black and White (FAXNEG)

You may occasionally come across a station that appears to be
inverted; that is displaying black where you expect white, and leaving
white where you expect black.  In this case you may issue the command
FAXNEG ON.  The PK-900 will respond with:

FAXNeg  now ON

What was before displaying black will now be white, and vice versa.

#### 9.6.4 Display Density (GRAPHICS)

How the graphics will look on your display depends on the setting of
the GRAPHICS command.  There are 7 graphics commands providing
horizontal dot densities from 480 dots to 1,920 dots horizontally
across a page.  The default setting is 960.  See the GRAPHICS command
description in the Command Summary for a complete description of
this command.

### 9.7 Displaying Other Services

Most of the weather services in the US use a facsimile scan speed of
2 lines per second, which corresponds to FSPEED 2 (Default).
Facsimile photographs often use 1 line per second, which is FSPEED 1.
Some foreign services use speeds of 4 lines per second, which is
FSPEED 4.  Speeds of 1.5 and 3 lines per second are also supported.
See the command summary for more information on FSPEED.

When different horizontal scan speeds are used, the number of lines
per vertical inch can also vary.  If nothing is done to change the
number of lines displayed, the pictures may appear squashed or
elongated.  The ASPECT command resolves this by allowing from one to
six lines to be displayed out for every six lines received.  The
default setting is ASPECT 2 which means that 2 out of 6, or 1 out
of every 3 horizontal lines is displayed.  This is the most common
setting you will use for WEFAX, but other services may require using
other values to display pictures without aspect ratio distortions.

#### 9.7.1 The PK-900 FAX Modem

The PK-900 uses a special modem (MODEM 8) for HF WEFAX operation.
This modem has a bandwidth of 800 Hz and a center frequency of 1900
Hz.

### 9.8 Transmitting FAX

The PK-900 does support FAX transmission, but attempting to do this
without an AEA's PC-PAKRATT II WITH FAX program is difficult.  We
recommend you consider this program if FAX transmission is desired.

<!-- PDF p.150 -->

### 9.9 Adjusting the PK-900 4.0 MHz Oscillator

Note:  The following only applies to the black & white FAX mode of
the PK-900 and does not apply to the Analog mode.

If you ever observe the received FAX from a Commercial station does
not display straight up and down the page, your 4.00 MHz oscillator
inside the PK-900 has probably drifted off frequency. If you have a
frequency counter with a high-impedance input, you may do the
following:

Step 1: Open the PK-900 by removing the 4 screws that hold the gray
top chassis in place and separate it from the bottom chassis.

Step 2: Reconnect the PK-900 to +13 VDC, then turn it on, and allow
it to warm-up for 30 minutes.

Step 3: With the help of Figure 2 below, locate the variable
capacitor C112 in the left-front quadrant of the PK-900 circuit
board.  It is located near the U43 and Y3.

Step 4: Place the probe of the frequency counter on pin-16 of IC
U35, the Z8536 which will provide a strong square-wave clock signal.

Step 5: Adjust C112 until the counter reads 4.00000 MHz +/- 10 Hz.

Figure 2:  PK-900 circuit board layout showing the location of C112
