# Chapter 3 — Radio Installation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 33–44).

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.33)

### 3.1

#### 3.1.1

### 3.2

Overview
This chapter describes how to connect the DSP-2232 to your radio
receiver or transceiver. To receive digital transmisgiong you mugt
connect the receiver audio and Ground to your DSP-2232. To transmit
you will have to add connections to the microphone or low-level
transmit audio and to the (PTT) circuit of your
transceiver .
The most convenient way to connect your transceiver is through a rear
panel ACCESSORY Connector (if your transceiver has one) .
You may also
use the Mic connector if you prefer. MAKE SURE THAT YOU REMOVE POWER
FROM THE DSP-2232 AND YOUR RADIO BEFORE MAKING ANY CONNECTIONS.
Required
You will need the following for complete transmit/ receive connections:
your DSP-2232 Data Controller, computer or Computer Terminal and
software as discussed in Chapter 2 of this manual;
AEA-supp1ied shielded cable for each radio you wish to connect;
your radio and its power supply;
microphone or accessory-plug connector(s) required by your radio;
soldering iron and solder if the radio connectors require it;
wire cutters and strippers and/or a small pocket knife;
Receive-OnIy Radio Connections
If you are a Short Wave Listener (SWL) or only interested in receiving
signals, the connections to the DSP-2232 are simple.
Even if you are
planning on transmitting and receiving, you may initially want to just
receive to become familiar with the DSP-2232. Taking a little time to
tune in and "read the mail" is an excellent way to get acquainted with
the various modes before going on the air.
For receive operation, only the audio from the receiver or transceiver
(and Ground) needs to be connected to the DSP-2232. This can often be
accomplished by simply soldering the included 3.5 mm audio plug to the
GREEN and BROWN wires of a DSP-2232 Radio Cable as shown in figure 3-1
below. The audio plug can then be connected to the External
Speaker/ Earphone jack on the radio you will be using.
NOTE :
Some Short Wave receivers come with low-level outputs
designed for use with a tape recorder. These outputs
typically do NOT have enough level to drive the DSP-2232 .

### (PDF p.34)

osp-tZ3Z/ZZ3Z
z
RN)IO
REAR VIEH
S PIN otN

### 3.3

#### 3.3.1

#### 3.3.2

Figure 3-1 Receive audio connection to the DSP-2232.
If you are using an HF transceiver or Short-Wave receiver you should
consult Chapter 10 for information on the Signal Identification mode.
Chapters 4, 6, 7, 8 and 9 talk specifically about some of the modes
you may encounter on the HF and Short-Wave bandg .
If you are connecting to a VHF scanner or VHF/UHF transceiver you
should look over Chapter 4 on Packet operation.
Transmit and Receive Radio Connections
To connect your DSP-2232 to a HF or VHF/UHF TRANSCEIVER you will need
access to the Receive-Audio, Transmit-Audio (mic-audio) ,
Ground and optionally a Squelch input for shared voice/data channel g.
Most of these signals are typically available on the Mic connector and
If
also often on a rear-panel Accessory connector of the transceiver.
you will be wiring the DSP-2232 to more than one radio, repeat the
procedures in section 3.3.5 for each radio you will connect.
Transceiver's Microphone or Accessory. Connector?
The most convenient way to connect your transceiver is through a rear
If the DSP-2232 is
panel Accessory connector if one is available.
connected as an accessory, the microphone used for voice operation can
sometimes be left connected to the transceiver. This makes changing
between voice and data modes easier than if the microphone must be
unplugged in order to connect the DSP-2232. On most HF radiog
however, the mic is "hot" and should be unplugged during data
operat ion.
Connections for Specific Transceiver Models
APPENDIX E of this manual contains information and diagrams for
connecting the DSP-2232 to many modern HF and VHF trangceiverg.
Please turn to APPENDIX E and locate the transceiver model you will be
If you do not find the exact model of
connecting to your DSP-2232 .
your transceiver in APPENDIX E, then locate a model from the game
manufacturer that has the same Accessory ur Microphone connector as
the unit you will be connecting.

### (PDF p.35)

#### 3.3.3

Pin
1
2
3
4
5
Check Your Transceiver's Operating Manual
Locate the Operating Manual for your transceiver and turn to the page
describing the connector to which you will attach your DSP-2232.
Even
if you found the exact model of your transceiver in APPENDIX E, it is
a good idea to verify that your transceiver's manual agrees with the
information in the appendix. If the information does not agree, or
you could not find the exact transceiver model in APPENDIX E, then you
should use the information contained in your transceiver's manual to
connect the DSP-2232.
Specific Connection Points
Whether you are connecting an HF Single Side Band transceiver for
RTTY/FAX operation, or a VHF/UHF transceiver exclusively for packet,
the minimum connections to your transceiver will be almost identical .
HF transceivers have a few optional connections that will be covered
after the basic connections have been made.
The following table and figure will be helpful in identifying the
proper basic connection points to the DSP-2232 radio cable.

**Table 3-1 — J4/J5 radio port cable (PDF p.35)**

| Signal | Wire color | Description |
|---|---|---|
| Microphone audio | White | AFSK from DSP-2232 to transceiver |
| Ground | Brown | Audio and PTT common return |
| Push-To-Talk | Red | DSP-2232 keys transmitter |
| Receive audio | Green | Audio from receiver to DSP-2232 |
| Squelch input | Black | Optional: detect activity on a shared-mode channel |
| Shield / drain | Silver | Shield of cable / microphone ground |

DIN pin numbers 1–5 are shown next to Figure 3-2 on this page; the OCR of the pin-to-color map is garbled (`S PIN OIN`, `CLIP SHtELO`). Use wire **colors** from the table, not the garbled pin sketch.

J4 and J5 radio port and cable connections (Figure 3-2, rear view). Figure text is unreadable.
TO
EXTERNAL
SPEAKER
(OPT
REAR VIEW
S PIN OIN
CLIP SHtELO
THIS EFO
BRN
TO SPEXER
HI c GRCRR-•O
Figure 3-2
DSP-2232 to Radio Cable Connections

### (PDF p.36)

#### 3.3.5

##### 3.3.5.1

##### 3.3.5.2

##### 3.3.5.3

##### 3.3.5.4

1.
Begin Assembling your Radio Cable
Assemble the tools, DSP-2232 Radio cable and connectors you will need
for each radio you wish to connect. You will probably also need a
small soldering iron (20-40 watts) and solder at your work area.
Prepare the Radio Cable
2.
3.
4.
NOTE :
Locate one of the 5 ft DSP-2232 radio cables included with your
DSP-2232.
Note that the Radio cables may have been shipped as a
single 10 ft cable which should be cut in half before use.
Prepare the bare end of one of the radio cables by removing an
appropriate amount of the jacket for the connector you will be
attaching.
Usually this is 1/2 to 3/4 inch.
Carefully remove the foil shield exposing the colored wires
underneath. Be careful not to nick or cut the shield wire.
Strip back 1/8 inch of colored insulation from the GREEN, RED,
WHITE and BROWN wires.
The BLACK wire is the squelch input and normally not used.
The black wire is only needed for Packet operation if the
channel you plan to operate on is used for both voice and
If you need this connection, strip away 1/8 inch of
data.
BLACK insulation as done with the other four wires.
If
this wire is not needed, then leave the insulation intact.
Verify the Connection Points with Your Manual
Look at the connector closely (with a magnifying glass if necessary)
and locate pin 1. Compare this to the location of pin 1 on the
connector drawing in your transceiver's manual and also in APPENDIX E.
This is important as some diagrams show the connector from the inside
of the transceiver, not the outside of the plug you are wiring. This
will help insure that the plug is not wired backwards.
Prepare the Connector
Now that the cable is prepared, you are ready to prepare the connector
for wiring.
If the connector you are wiring has a shell, be sure that
it is placed over the cable before any connect ions are made.
If this
is not done, an otherwise perfect wiring may have to be redone.
Wire the Connector
The following connections must be made for transmit and receive
operation of the DSP-2232. Refer to table 3-1 and f igure 3-2 as well
as APPENDIX E and your transceiver's manual when making these
connect ions .
HINT:
When wiring a Connector, it is often easier to wire the
inside or middle connections first and work your way to the
outside pins.
For this reason the following steps are not
numbered and may be done in any convenient order.

### (PDF p.37)

Connect the Shield/ Drain wire (Silver wire with no insulation) to
the Microphone GROUND connection if your transceiver has one.
If your transceiver does not have a separate Microphone or Audio-
in Ground connection, then this wire should connect to the single
Ground along with the Brown wire.
See the next step.
Connect the BROWN wire to the main GROUND on the connector. This
Ground is the one used for the PTT and receive audio. You should
connect the Silver Shield/ Drain wire to this GROUND only if there
is not a separate Microphone Ground as described in the previous
step .
Connect the RED wire to the Push-To-Talk (PTT) terminal on the
connector. At this t ime, check the manual to determine whether
your transceiver uses positive (+) or negative (
PTT.
The DSP-2232 comes from the factory set for Positive PTT since
most transceivers use this method of keying. This will be
discussed in more detail in the Adjustment sections below. If
you are connecting a Handheld transceiver to your DSP-2232, you
will probably need a resistor and/or capacitor to isolate this
connection from the AFSK audio.
Check APPENDIX E.
Connect the WHITE wire to the MICROPHONE AUDIO terminal on the
connector. This connection carries the low level Audio Frequency
Shift Keying (AVSK) to the transmitter's microphone audio
section. If you are connecting a Handheld transceiver to your
DSP-2232, you will probably need a resistor and/or capacitor to
isolate this connection from the PTT. Check APPENDIX E .
Connect the GREEN wire to the RECEIVER AUDIO terminal on the
connector. For the DSP-2232 to operate properly, we recommend at
least 200 mV RMS of receive audio be available. If you are
connecting to an Accessory Jack, make sure the available level is
at least 200 mV RMS.
For CW work 400 mV may improve operation.
If you will be using a Packet Radio channel that is shared with
voice users then you should connect the BLACK wire to the SQUELCH
status pin of the connector. This will prevent the DSP-2232 from
transmitting when there is a received signal strong enough to
open the Squelch. If you connect this pin you may have to change
the setting of the SQUELCH command in the DSP-2232. Most VHF/UHF
Packet channels are no longer shared with voice users so this
connection will probably not be needed.
This completes the minimum necessary connections for transmit and
receive operation with the DSP-2232.
If you are interested in using
the DSP-2232 to transmit Morse code (CW) or transmit RTTY using FSK
inputs on your HF transceiver, the following three sections (3. 3.6,
3.3.7, and 3.3.8) should be read.
If you will not be using any of the connections described in the
following sections, then skip ahead to the Final Adjustment section

### 3.4 where you will set levels and prepare to go "On the Air" .

### (PDF p.38)

#### 3.3.6

1.
Wiring Your HF Transceiver for Direct CW Key inq
The DSP-2232 can directly key CW with HF and VHF multi-mode
transceivers. This requires that a cable be wired from the CW KEY OUT
jack on the DSP-2232 'g rear panel to your CW keying input of your
transceiver on the correct Radio Port. Refer to the instructiong
below and Figure 3-3 to wire the DSP-2232 side of the cable.
REM
v-1232/2232
Cu KEYING
OR :
Figure 3-3:
Cu XEYttC,
SAI ELO
Direct CW Keying Cable diagram
2.
3.
4.
Locate an RCA connector from the DSP-2232 •s accessory bag.
Locate some shielded audio cable from Radio Shack or other cable
house and solder the RCA connector to the cable as shown above.
Locate the connector for the CW keying input to your transceiver.
These are often supplied in your transceiver's accessory kit.
Wire the transceiver connector as per the instructions in your
transceiver •s manual for a "Straight key"
Consult your radio •s instruction manual to determine if your radio
uses neqative (Grid Block) or positive keying polarity. Connect the
shielded cable you just wired from the DSP-2232 's or negative
keying jacks to your radio's CW key input connector.
See the Specifications on page 1-3 for maximum limits.
Connections for Direct FSK Operation on RTTY
Some HF SSB radios provide direct FSK (Frequency-Shift Keying) for
RTTY operation. Direct FSK can be an advantage when using RTTY and
AMTOR and can sometimes help in HF packet operation.
FSK operation
may be helpful if your transceiver can switch in f ilters. Be cautious
of narrow filters as they can limit your data rate. Direct FSK is not
always recommended for data speeds above 110 bauds. Consult your
transceiver's manual for further recommendations on direct FSK.
To install and operate your DSP-2232 and radio in the FSK mode:
1.
NOTE :
Connect a shielded cable from the DSP-2232'g J 3 (DIN) receptacle,
Pins 3
pins 1 or 4, to the radio's FSK input for radio port 1.
or 5 are the PSK outputs for radio port 2 .
Polarity of the FSK signals is not standardized by the radio
manufacturers. We have observed that I com radios most often
use FSKN (pins 1 and 3), while Kenwood radios most often use
Consult your transceiver's manual to
FSKR (pins 4 and 5).
identify the proper polarity.

### (PDF p.39)

2.
1.
Connect the FSK lines from the DSP-2232 to your radio's PSK or
RTTY input
See Figure
in accordance with your radio's requirement g.
F SKN
FSKR
FSKR
PSKN
RAO 10
OR
RAOIO
OR
RAO 10
3-4 below.
2
I NPUT
RADIO 2 FSK
INPUT
3
FSK OUT
CONNECTOR
NOTE :
Figure 3-4 Connector J 3 FSK Connections
When using FSK, the same power and duty cycle limits apply
ag cited earlier for AFSK operation. Consult your radio's
operating manual for any power or transmit time limits .

#### 3.3.8

Connections for a Pa•cket Satellite Receiver
If you will be operating Packet Radio through a satellite, you should
connect the DSP-2232 to your receiver's UP/ DOWN frequency control .
To
connect the DSP-2232 to your satellite receiver, do the following:
Connect a shielded cable from the DSP-2232'g J 7 (DIN) receptacle,
to the UP and DOWN frequency control pins on the microphone jack
or rear panel accessory connector of your satellite receiver .
Pins I and 4 are UP and DOWN, to the radio's frequency control
Pins 3 and 5 control UP/ DOWN for radio
input for radio port I.
See Figure 3-5 below.
port 2 .
07
3
SATELL TE
UP/OOÅN
CONNECTOR
Figure 3-
5
Connector J 7 Satellite
RADIO 1 UP PREOUENCY
RAOIO i DOWN FREOUENCY
GNO
GNO
RAOIO 2 OONN FREOUENCY
RADIO Z UP FREOUENCY
UP/ DOWN Frequency Connections

### (PDF p.40)

### 3.4

#### 3.4.1

#### 3.4.2

### 3.5

#### 3.5.1

1.
DSP-2232 Configuration Jumpers and Connections
Before operating the DSP-2232, you must first make sure it is
correctly configured for your radio's PTT. After this has been
checked you should then connect the cables you constructed above.
Push-To-Talk (PTT) Conf iqurat ion
Before you connect the Radio cable(s) you just made to the
DSP-2232, consult your transceiver's manual for keying
polarity. Most transmitters and transceivers made in the last 15
years use Positive PTT keying. However some gear, especially if it
contains vacuum tubes, may use a negative PTT keying voltage.
The DSP-2232 is configured for positive PTT at the factory so it will
operate with most equipment without changes. However, if necessary ,
you can change the polarity of the PTT configuration on either Port 1
or Port 2.
Follow these steps :
Remove four screws from the top and eight screws from the sides
of the DSP-2232 chassis cover and lift off the cover.
Locate Jumper posts JMP4 and JMP5 which are about 4 inches in
from the right-rear corner of the PCB.
JMP4 conf igures Port 1 and JMP5 conf igures Port 2
When the shorting jumpers are towards the fuse, the port is
configured for Positive (+) PTT. When the shorting jumpers are
towards the center, the port is configured for Negative (
- ) PTT.
Replace the cover and twelve screws when you are finished conf iguring
the polarity of the DSP-2232 PTT circuit.
DSP-2232 Connections
Remove power from the DSP-2232, your transceiver and all accessories
before making any connections .
Connect the Radio Cable(s) you constructed in section 3.3. 5 between a
Radio port on the DSP-2232 and your transceiver(s) .
If you wired cableg for CW keying or Direct FSK then connect these to
the appropriate point on your equipment .
Transceiver Adjustments
This section is split into separate procedures for FM and SSB radios.
You may adjust either port at any time without affecting the other.
FM Transceiver Final Adjustments
Turn on your computer and DSP-2232 and start your terminal
program .

### (PDF p.41)

2.
Connect the radio to a dummy load.
Be prepared to monitor your
transmissions with another nearby radio such as a handheld
transceiver .
Verify that your DSP-2232 and FM radio are connected as shown in
Figure 3-6 below.
3.
4.
5.
to
E X E RNC•L
REAR
S PIN OIN PLUG
CLIP SHIELO
THIS
Figure 3-6 Connect ions
TO SPE*ER N.JOto
NOTE:
If you are using an AEA program such as PC-PAKRATT 11 or MACRATT,
you must enter the Dumb Terminal mode to access the CALIBRATE
Mode as described below.
Enter the Calibrate mode by typing: "CAL <Enter>. "
In the Calibrate mode only, the "K" key toggles the
transmitter PTT line on and off. The "SPACE BAR" toggles
the DSP-2232 's tone generator from "Mark" (the lower pitched
tone) to Space" ( the hiqher pitched tone) The DSP-2232
has a transmit watchdog timer circuit that unkeys your
transmitter automatically after sixty ( 60) seconds. As you
perform the following adjustments, unkey periodically, then
rekey the transmitter by typing "K. "
Press the "K" key on the keyboard to key the transmitter.
should hear a continuous tone in the monitor receiver.
You
Tap the space bar several times until the higher pitched of the
two tones ( "space") is heard.

### (PDF p.42)

8.
10.
11.
12.
13.
14.
Refer to the figure below of the DSP-2232's side panel and locate
the AFSK level potentiometer adjustment for the port you are
cal ibrating .
DO NOT ADJUST THE AGC CONTROLS, THEY ARE FACTORY SET.
Port 2
AGC Level
Port 1
Port 2
AFSK Level
Port 1
AFSK Level
Figure 3-7 DSP-2232 S ide Panel Controls
With the DSP-2232 keying the transmitter and sending the higher
of the two tones, adjust the transmit audio level for the port
you are calibrating as follows:
Ty pe
Ty pe
Listen to the monitor receiver; turn the DSP-2232's g ide-
panel AFSK Output Level adjustment screw clockwise (CW)
until you hear no increase in output level in the monitoring
receiver.
Rotate the AFSK Output Level adjustment screw
counterclockwise (CCW) until the audio signal on the
monitoring receiver is slightly but noticeably reduced from
the maximum level.
"K" to return to receive mode.
"Q" to "Quit" (exit) the calibration routine.
You've now set your FM transmitter's deviation to an approximate
level which will be adequate for initial operation.
With your radio in the receive mode, open the squelch control go
that a steady hiss or noise is heard on a speaker.
Set the receiver's volume control so the DCD LED on your
DSP-2232 just lights with the receiver unsquelched. This is the
approximate proper level for best receive performance from your
DSP-2232's modem.
Reset your receiver's squelch control for normal operation.
3-1 c

### (PDF p.43)

RAD 10

#### 3.5.2

SSB Transceiver Final Adjustments
Digital modes with an SSB radio require some different settings of
the radio's operating controls for proper AMTOR and packet operation.
Be sure to observe the following setting precautions:
Set VOX to OFF.
Set speech compression to OFF .
Set AGC to FAST (if available) .
Disconnect the ALC cables between your SSB radio and any external
RF amplifier you wish to use in AMTOR or packet radio service.
Remember
- Baudot and ASCII RTTY and Mode B (FEC) AMTOR are
continuous key-down condit ions
- Your radio's dutv cycle is 100% for
the duration of each transmission.
If your SSB radio isn't designed
for continuous full-power operation, you must operate vour radio at
reduced output power .
Consult the manufacturer's specif i cations for
details on the operating duty cycle.
NOTE :
1.
2.
3.
4.
5.
6.
8.
NOTE :
Make all connections with all power off .
Connect your DSP-2232 and SSB radio as shown in Figure 3-6.
Turn on your DSP-2232 and your computer and start your terminal
program.
Connect your SSB radio to a dummy load such as the AEA DL-1500.
an audio
If your SSB radio has a "monitor" facility, i.e. ,
output that lets you listen to the audio signals entering the
microphone or phone patch jacks, turn that monitor circuit on.
Set the radio's MODE selector to LSB ( lower sideband) .
Set the radio's meter switch to the "ALC" position.
If the
radio doesn't have an "ALC" indication, set the meter switch to
" I p" or "lc" to read plate/collector current.
If a current
reading isn't available, set the meter to indicate power output.
If you are using an AEA program such as PC-PAKRATT II,
COM-PAKRATT or MACRATT, you must enter the Dumb-Terminal mode
( see program manual) to access the CALIBRATE Mode described below.
Enter the Calibrate mode by typing: "CAL "
In the Calibrate mode only, the " K" key toggles the
transmitter PTT line on and off . The " SPACE BAR" toggles
the DSP-2232's tone generator from "Mark" (the lower pitched
The DSP-2232
tone) to "Space" (the hiqher pitched tone) .
has a transmit watchdog timer circuit that unkeys your
transmitter automatically after sixty ( 60) seconds. As you
perform the following adjustments, unkey periodically, then
rekey the transmitter by typing "K. "
Adjust the transceiver's Microphone Gain control to its minimum
setting.

### (PDF p.44)

10.
11.
12.
13.
14.
NOTE :
15.
16.
Press the "K"
Increase the
a continuous
key on the keyboard to key the transmitter.
transceiver's microphone gain control until you hear
tone in the radio's monitor output.
If you are able to hear the tones in radio's speaker, tap the
space bar several times until you hear the lower pitched of the
two tones ( "mark" ) .
With the DSP-2232 keying the transmitter and transmitting the
lower of the two tones, adjust the transmit audio level as
fol lows :
Type
Type
1.
11.
111.
"Q"
You have
your SSB
Rotate the microphone gain control clockwise (CW)
approximately in the one-quarter ON position.
Turn the DSP-2232 's side-panel AFSK Output Level
adjustment screw clockwise (CW) until until the ALC
meter shows a small deflection from the unmodulated
reading. Check the radio's plate/ collector current or
output power indicators.
Adjust the AFSK Output Level control until the radio's
indicators show approximately thirty percent (30%) of
the manufacturer's rated full-power reading.
If the rnanufacturer's plate/collector current
EXAMPLE :
specification for CW operation is 200 mA, set
the AFSK Output Level control and your
microphone gain control so that the
plate/ collector current indicates
approximately 75 mA.
to return to receive mode.
to "Quit" (exit) the calibration routine.
now set the DSP-2232's transmit audio output level and
radio's microphone gain control to an approximately
correct level for all operating modes .
For Mode A (ARQ) AMTOR and packet radio operation, the
radio's microphone gain control can be adjusted to produce
the full-power output plate current recommended by the radio
manufacturer. These modes are "burst y" modes; the
transmitter is keyed on and off automatically by the
DSP-2232. The resulting duty cycle is much less than 100%
and full-power operation is generally acceptable.
With your radio in receive mode, tune the receiver to a clear,
unoccupied frequency .
Set the receiver's audio volume control (AF GAIN) to the position
you would normally use for CW reception. This is the approximate
receiver audio output level
your DSP-2232's modem.
for best receive performance from
