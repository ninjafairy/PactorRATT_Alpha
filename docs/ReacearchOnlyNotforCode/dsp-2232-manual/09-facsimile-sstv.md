# Chapter 9 — Facsimile and SSTV Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 137–144).

**Agent extract:** Analog FAX/SSTV is meant to replace PK-232 B&W FAX; as of July 1992 the scan says no gray-scale display software yet. Commands: `PRTYPE`, `LEFTRITE`, `FAXNEG`, `GRAPHICS`. Images are processed on the PC, not in the TNC. Bar-graph figure (PDF p.141) unread.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.137)

### 9.1

#### 9.1.1

FACSIMILE AND SSTV OPERATION
Overview
While facsimile and SSTV are not digital modes, the personal computer
coupled with a high resolution monitor makes an excellent display for
these images. The DSP-2232 contains modems that can receive both FM
and APT facsimile signals as well amateur Slow Scan Television (SSTV) .
The DSP-2232 filters and digitizes the received audio signal for
processing by software running on your personal computer. Therefore
this chapter only provides an overview of FAX and SSTV operation.
The program used to display FAX and SSTV should provide operating
instructions and detailed information about the modes it supports.
Note: The Analog mode is intended to replace the old black & white
FAX mode originally developed for the PK-232. As of July 1992, there
is no display software yet available for gray scale FAX or SSTV.
The black & white FAX mode is retained only to support the PK-FAX and
MACRATT with FAX programs while oew display software is developed.
Facsimile
Facsimile is the term used to describe the transmission of black &
white images. There are several different Facsimile standards in use.
Over the telephone, either "Group-2" or "Group-3" transmission
standards are used. Sending facsimile by radio has some special
problems so different and incompatible transmission methods are used.
There are two basic types of facsimile signals transmitted over radio.
Frequency Modulated (FM) • facsimile is often found in the HF . Short
Wave bands, while an Amplitude Modulated (APT) system is used by
satellites in the VHF and Microwave bands.
Weather Facsimile (FM WEFAX) is transmitted throughout the Short-
Wave spectrum, primarily to provide information to ships at sea.
Typical stations you may find broadcast maps with weather conditions
and satellite photographs showing cloud cover over a large area.
Not only weather information is transmitted, but also news
photographs from the wire services (see the frequency list below) .
With FY. facsimile, the picture information is modulated in an audio
In between
tone between I, 500 Hz (Black) and 2,300 Hz (White) .
these two frequencies are shades of gray. The DSP-2232's MODEM 41
receives these signals and can discern 250 levels (shades of gray)
between these two frequencies .
Facsimile signals can also be received directly from satellites that
are found in the 137 MHz VHF band as well as the 1691 and 1694.5 MHz
Microwave frequencies. These satellites broadcast images of the
earth as seen from above. The satellites transmit an FM signal with
a 2 400 Hz Amplitude Modulated sub-carrier. A modulation of the
sub-carrier corresponds to black, and an modulat ion level
corresponds to white. The DSP-2232's MODEM 42 receives 256 levels
of modulation (shades of gray) for this type of facsimile signal.

### (PDF p.138)

#### 9.1.2

### 9.2

Slow Scan Television
Slow Scan Television (SSTV) has been around for many years and is
presently found primarily in the 20 meter amateur band. Over the
years SSTV has evolved a great deal and many different formats now
exist for both black & white as well as color image transmission.
SSTV uses a Frequency Modulation scheme similar to FM facsimile where
the video information is usually transmitted by an audio tone between
1,500 Hz and 2,300 Hz.
In SSTV however additional synchronizing
information is often necessary and is typically sent with tones
between 1 100 and 1, 500 Hz. For this reason SSTV requires a
different modem than is used in FM Facsimile. The DSP-2232's Modem
43 is optimized for these frequencies and is used for SSTV reception.
Finding FAX Frequencies
The following is a list of HF FM facsimile frequencies that seem to
broadcast On a regular schedule. A few of these stations transmit
24-hours/day. We suggest you try the weather FAX frequencies listed
below while you are becoming familiar with facsimile operation.
After printing a few images, and you are accustomed to the sound of
facsimile you will be able to tune the bands
frequencies where perhaps different kinds of
in search of other
pictures may be found.
weather: USB 3, 357.0 kHz
Photographs :
10,865.0 kHz
LSB
4,268.0 kHz
12,125.0 kHz
10,680.7 kHz
4,975.0 kHz
20,015.0 kHz
17,673.9 kHz
6,946.0 kHz
18,434.9 kHz
The following HF facsimile frequencies were
Communications Magazine:
USB
obtained from Popular
4,271.
8,502.0
9,389.
5
4,793.
5
9,157.
5
8,080.0
4,802.
5
7,770.
8,459.
4,346.
646.0
VHF and
kH z
kHz
kHz
kHz
kHz
kHz
kHz
kHz
kHz
kHz
kHz
9,890.
12,750.0
11,035.
10,185.
17 , 447.
5
10,854.
9, 440.0
11,090.
8, 682.
17,410.
5
kHz
kHz
kHz
kHz
kHz
kHz
kHz
kHz
kHz
13,510.
12,201.
16,410.
13,862.
13,627.
12,730.
5
5
kHz
kHz
kHz
kHz
kHz
14,671.5
17,151.
2
kHz
kHz
Halifax, Canada
Boston, Masg
Brentwood, NY
Washington, DC
Mobile, A1
Norfolk, VA
Hawaii
Hawaii
Alaska
San Francisco
San Diego
Microwave APT
Facsimile Satellite Frequencies
1691.0 MHz
1694.5 MHz
GOES Geosynchronous Weather Satellite (USA)
METEOSAT Geosynchronous Weather Satellite (ESA)
136.0-138.0 MHz
Orbiting WEFAX APT Weather Satellites
Table 1:
Facs Ie Frequenc i es

### (PDF p.139)

#### 9.2.1

### 9.3

Finding SSTV Frequencies
In the US, SSTV is presently most popular on the 20 meter HF amateur
band. Most evenings an active group of SSTV enthusiasts can be
found on 14.230 and 14.233 MHz. This is a good place to start
looking for signals. Once you recognize the different sounds of
common SSTV signals, you may find activity on other frequencies and
bands especially as propagation changes.
FAX and SSTV Analog Signal Operation
To acconunodate the characteristics of gray-scale FAX and SSTV analog
signals, the DSP-2232 uses a special Analog mode.
Software programs
to display images should automatically set the DSP-2232 Analog
parameters so most users need not concern themselves with the
following information. If you are curious how the DSP-2232 works in
this mode, or are interested in writing your own display software,
this information may be helpful.
Note: The Analog mode requires special display software that has been
written for each familly of personal computer be used. Software
is necessary in order to handle the gray scale FAX and SSTV
images. This software is not provided with the DSP-2232
because the unit operates with many different types Of computers.
Without display software, the Analog mode should not be used.
The Analog mode is designed to pass data for communications methods
that require "gray scales" or color such as FAX and SSTV. In HF FAX
and SSTV signals, the information is contained in an audio signal
that varies from 1, 500-2, 300 Hz for FAX and 1, 100-2 , 300 Hz for SSTV.
In APT FAX signals, the information is contained in the amplitude of
a 2,400 Hz audio carrier.
Modems 41, 42 and 43 filter and digitize these analog signals and
convert the range of interest an 8 bit unsigned binary number.
Low values (e.g. $00) correspond to low frequencies and amplitudes,
while high values ($FF) correspond to high amplitudes and frequencies.
In order for the image informat ion to have any meaning, it must be
sampled at a periodic rate. This rate is set with the Analog SAMPLE
(ANSAMPLE) comxnand. ANSAMPLE allows the application program to
choose the sample rate for the signal up to 2,222 times per second.
See the Command Summary Appendix for more detailed information on the
Analog mode and the ANSAMPLE command if you are interested in
prograrnming a display application for the DSP-2232 .

### (PDF p.140)

### 9.4

9 . 4.2
7 92
Black & White FAX Operat ion
This mode of black & white FAX operation will eventually be deleted
from the DSP-2232 since the Analog mode has been created. Still, if
you own an older AEA PAKRATT WITH FAX program, you should have the
ability to display weather FAX on the screen of your PC.
If you are
using an AEA PAKRATT WITH PAX program, follow the program manual
instructions to run the program with the DSP-2232.
To hook-up the DSP-2232 for facsimile operation the radio must be
connected to Radio Port I.
If all you want is to receive FAX, you
only need to connect the audio from your receiver.
If you wish to
transmit as well, follow the instructions in Chapter 3 of this manual
for complete Radio Connections.
If you will be printing directly to a graphics printer with a parallel
printer cable, an IBM printer cable must be attached to the parallel
printer port on the rear on the DSP-2232. This port feeds a
Centronics Parallel printer and supports many graphics standards.
the PRTYPE ' command in the command summary for a list of supported
printer graphics formats.
NOTE :
Facsimile transmissions contain such a large amount of data
that the DSP-2232 can not support Packet operation on Radio
Port 2 whilé in the Facsimile mode.
HF Receiver Settings
Set your HF receiver (or transceiver) to upper Sideband (USB) and
disable any IF-Shift or Passband-tuning controls. Adjust the volume
to a comfortable listening level.
Tuning In HF Facsimile Stations
Facsimile is most often found on Upper Sideband and sounds similar to
monitoring an AMTOR QSO with both stations being of equal strength.
The most common facsimile signals are WEFAX, so we have set the
DSP-2232 FAX default parameters to copy weather charts and many
satellite photographs. We recommend starting start with one of the
listed weather frequencies, or frequencies from popular Communications
in Table 1 when first receiving facsimile.
Upon tuning into a WEFAX signal, you will notice that the facsimile
sound seems to repeat at the rate of twice a second. This is the
horizontal scan frequency, and allows you to distinguish different
facsimile services by speed. Common horizontal scan rates are 2 lines
per second, which is typically used in weather facsimile broadcasts,
1 line per second for photographs, and 4 lines per second for some
foreign facsimile stations.
Listen for these repetition rates as you
tune across the bands in search of new pictures.
The DSP-2232 uses a center frequency of 1.9 kHz for copying facsimile
transmissions. As a result, you must tune I .9 kHz lower than the
frequencies listed in Table 1 when using Upper Sideband.
in Lower Sideband, one must tune 1.9 kHz higher than the frequencies
listed in the table.

### (PDF p.141)

The DSP-2232 LED bar-graph should be tuned so that the facsimile
signal is roughly centered in the display as shown in the middle of
Figure I below. If the audio frequency is too low, the bar-graph will
If the audio frequency is
look something like the left-most display.
too high, it will look something like the right-most display.
Facsimile tuning is not especially critical when copying WEFAX, but a
properly tuned signal is needed when printing facsimile photographg.
Frequency too low
Tuned In
Frequency too High

#### 9.4.3

### 9.5

Figure 1:
Facsimile tuning indicator conditiong for FAX tuning
DSP-2232 Facsimile Parameter Settings
To start receiving WEFAX broadcasts on your computer screen, follow
the setup instructions in your PAKRATT WITH FAX program manual.
To start receiving FAX on your graphics compatible printer, you first
must tell the DSP-2232 that there is a parallel printer connected by
issuing the
PRCon
PRCon
command PRCON ON. The DSP-2232 will respond with:
was OFF
now ON
Now all that is necessary is to put the DSP-2232 into FAX mode by
typing FAX. The DSP-2232 will respond with:
```text
Opmode now FAX
```
Receiving Facsimile Broadcasts
The DSP-2232 is now in the Facsimile Standby-Receive mode which means
it i 3 waiting for a synchronization signal from a facsimile
transmitter to begin a new picture. Verify this by entering the
OPMODE command. The DS? -2232 should respond with:
```text
Opmode FAX
```
STBY RCVE
At this point make sure the receiver volume is high enough go that the
If you do not
DCD LED lights, otherwise the printer may not print.
want to wait for the beg inning of a new picture, you may type
Lock

### (PDF p.142)

#### 9.6.1

#### 9.6.2

This forces a synchronization-lock, and starts the printer printing
regardless of what kind of signal is being monitored. Since this
synchronization lock was not sent by the transmitting station, the
picture will probably not be correctly positioned on the page.
Rather, it will likely appear to be split in half with the left half
of the picture on the right half of the page, and the right half of
the picture on the left half of the page.
To correct for thist a justification command has been included that
allows the user to shift the entire image to the left in 1/2 inch
inc rementg .
For example if the left-edge of the picture appears
roughly 4-1/2 inches away from the left edge of the paper, issuing the
cornmand JUSTIFY 9 will shift the left edge of the picture to the left
4-1/2 inches (9 X 0.5 inch) correcting its justification.
The DSP-2232 will respond with:
JUstify now 9
This procedure will not be necessary if the DSP-2232 synchronizes from
the transmitted facsimile signal. This procedure is also not
necessary if you are using an Am PAKRATT with FAX program. AEA
programs allow the image to be justified after it has been received.
To stop the printer, you may either exit the facsimile mode by
changing to another mode or enter the RCVE command, which puts the
DSP-2232 back into facsimile standby receive. Standby receive is the
same as if you have just entered the FAX mode from another mode. The
DSP-2232 will wait until it receives the synchronization signal from a
facsimile station before beginning to print again.
Facsimile Operating Tips
The following section contains tips and information on FAX reception
for those printing directly to a graphics printer.
If you are using
an AEA PAKRATT WITH FAX program, consult the program manual for more
inform#ion and helpful hints.
Settinq PRTYPE for Your Printer
Before FAX can be printed on the printer you have connected, you must
choose the correct printer type with the PRTYPE Command. Most
printers these days support either the Epson or the IBM 8-bit image
graphics formats.
For this reason the DSP-2232 defaults to the Epson
format .
See the PRTYPE command in the Command Summary if your printer
supports a different graphics standard.
Printinq Direction (LEFTRITE)
Weather FAX as well as most other facsimile prints from left to right,
but occasionally you may find a station that is reversed.
If you come
across such a transmission, you may simply issue the command LEFT RITE
OFF to correct this. The DSP-2232 will respond with:
LEftr ite now OFF

### (PDF p.143)

#### 9.6.3

#### 9.6.4

### 9.7

#### 9.7.1

### 9.8

Inverting Black and White (FAXNEG)
You may occasionally come across a station that appears to be
inverted; that is print ing black where you expect white, and leaving
white where you expect black.
In this case you may issue the command
FAXNEG ON. The DSP-2232 will respond with:
FAXNeg now ON
What was before printing black will now be white,
Printing Density (GRAPHICS)
and vice versa.
How the graphics will look on your printer depends on the setting of
the GRAPHICS command. There are 7 graphics commands providing
horizontal dot densities from 480 dots to 1, 920 dots horizontally
across a page. The default setting is 960 which Epson compatible
graphics printers can reproduce. eee the GRAPHICS command description
in the Cornmand Summary for a complete description of this command.
Printing Other Services
Most of the weather services in the US use a facsimile scan speed of
2 lines per second, which corresponds to FSPEED 2 (Default) .
Facsimile photographs often use 1 line per second, which is FSPEED I.
Some foreign services use speeds of 4 lines per second, which is
Speeds of 1.5 and 3 lines per second are also supported.
FSPEED 4.
See the command summary for more information on F SPEED .
When different horizontal scan speeds are used, the number of lines
per vertical inch can also vary. If nothing is done to change the
number of lines printed by the printer, the pictures may appear
squashed or elongated. The ASPECT command resolves this by allowing
from one to six lines to be printed out for every six lines received.
The default setting is ASPECT 2 which means that 2 out of 6, or 1 out
of every 3 horizontal lines is printed. This is the most common
setting you will use for WEFAX, but other services may require using
other values to print pictures without aspect ratio distortions.
The DSP-2232 FAX Modem
The DSP-2232 uses a special modem (MODEM 41) for HF WEFAX operation.
This modem has a bandwidth of 800 Hz and a center frequency of 1900
Hz. The APT FAX modem (Modem 42) may also be selected for black &
white Facsimile operation. To select this modem, set the default FAX
modem to be modem 42. This is done from a terminal program of from
the dumb terminal modem of the PC-PAKRATT program by setting the
command QFAX to 42 .
Transmittinq FAX
The DSP-2232 does support FAX transmission, but attempting to do this
without an AEA's PC-PAKRATT 11 WITH FAX program is difficult. We
recommend you consider this program if FAX transmission is desired.

### (PDF p.144)

Adjust inq the DSP-2232 4.0 MHz Oscillator
Note: The following only applies to the black & white FAX mode of
the DSP-2232 and does not apply to the Analog mode.
If you ever observe the received FAX from a Commercial station does
not print or display straight up and down the page, your 4.00 MHz
oscillator inside the DSP-2232 has probably drifted off frequency.
If you have a frequency counter with a high-impedance input, you may
do the following:
Step 1: Open the DSP-2232 by removing the 12 screws that hold the
gray top chassis in place and separate it from the bottom chassis.
Step 2: Reconnect the DSP-2232 to +13 VDC, then turn it on, and allow
it to warm-up for 30 minutes.
Step 3: With the help of Figure 2 below, locate the variable capacitor
CII in the left-rear quadrant of the DSP-2232 circuit board. It is
located near the parallel printer connector and the 64 pin processor.
Step 4: Place the probe of the frequency counter on pin-16 of IC U8,
the Z8536 which will provide a strong square-wave clock signal.
Step 5: Adjust CII until the counter reads 4.00000 MHz + / - 10 Hz.
CZI
Cit
circuit
016
UZZ
Uts
RI 43
Figure 2:
2232
board layout showing the location of Cll
