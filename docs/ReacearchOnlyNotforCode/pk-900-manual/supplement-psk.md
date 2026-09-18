# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## PK-900/PSK Sound Card Interface Upgrade Kit A.06265 (PDF p.373–392)

<!-- PDF p.373 -->

1
PK-900/PSK
Sound Card Interface Upgrade Kit A.06265
Installation
and
Operation
Manual

<!-- PDF p.374 -->

2
TIMEWAVE TECHNOLOGY INC.
LIMITED ONE YEAR WARRANTY
This warranty is extended only to the original purchaser of the A.06265.
If your A.06265 fails in normal use because of a defect in workmanship or
materials within one year of the date of purchase, we will repair or replace (at our
option) the equipment at our factory without charge to you.  Timewave will pay
for the return of the warranty-repaired unit to you.
First, double check your connections and operating procedure.  If you're certain
that the unit is faulty, notify Timewave Customer Service immediately.  If
Timewave is unable to resolve the problem by telephone or email, we will give
you an RMA number and ask you to return the unit.  You must pay all shipping
and insurance charges for returning the unit to our factory.
We cannot be responsible for damage caused by accidents, abuse, misuse,
improper installation, or unauthorized attempts to repair the unit. This warranty
does not cover any parts of the PK-900 except the A.06265.
Timewave service work performed in connection with this warranty is warranted
to be free from defects in materials and workmanship for 30 days from the date
of rerpair. All other terms of the limited warranty apply to the service warranty.
Contact Timewave Customer Service by telephone at (651) 489-5080 or  by FAX
at (651) 489-5066.
Mailing and shipping address:
Timewave Technology Inc.
501 W. Lawson Ave.
St.Paul, MN 55117
email: sales@timewave.com
techsupport@timewave.com
service@timewave.com
web: www.timewave.com
TIMEWAVE MAKES NO OTHER WARRANTY, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
WARRANTY
WHO IS COVERED
WHAT WE WILL DO
WHAT YOU MUST DO
WHAT IS NOT
COVERED
SERVICE WARRANTY
HOW TO CONTACT
TIMEWAVE
©2002  by Timewave Technology Inc., St. Paul, MN USA

<!-- PDF p.375 -->

3
Table of Contents
Introduction
1. PSK Sound Card Interface Board Installation
2. PSK Sound Card Interface Board Settings
3. PSK Sound Card Interface Connections
4. PSK Sound Card Interface Operation
5. PSK Sound Card Interface Schematic Diagrams

<!-- PDF p.376 -->

4
Welcome to PSK for the PK-900
Thank you for purchasing the PSK Soundcard Interface upgrade kit for the PK-900 and
PK-900/DSP.  The Timewave part number of this kit is A.06265.
Follow the instructions carefully and you should have no problem in getting the unit on
the air. If you do have questions, Timewave has technical support available at (651) 489-
5080 and service@timewave.com
Please read before installing this upgrade kit.
If your PK-900 has a version A mainboard, contact Timewave for installation instructions.

<!-- PDF p.377 -->

5
PK-900/PSK Upgrade Kit
Section 1
PSK Sound Card Interface Board Installation
Installation steps
If you have purchased a PK-900 DSP upgrade kit, install it before you install the PSK upgrade kit.
Please check the contents of the kit at this time. You should have received:
1 PK-900/PSK Upgrade assembly
1 Eprom I.C. ver. 7.3
1 PAL I.C. ver. C (not used with PK-900/DSP units)
2 cables - 3.5 mm to 3.5 mm stereo phone jack - 6 ft.
1 PK-900/PSK installation and Operation instructions
1 Warranty card
1 Timewave CD ROM (also contains this document in .pdf format)
Here are the tools you will need:
Wire Cutters
Phillips Screwdriver
Needle nose pliers
Flat head screwdriver
3/16" hex nut driver
Solder pencil and solder
Solder sucker tool or solder wick for cleaning the pads on C40,  C89 and
the R253
Clear an area to work on your PK-900. You should take standard anti-static electricity precautions when
working on any ham equipment.
Here are the steps in the upgrade procedure:
Disassembly
Keep all removed parts, some are necessary to re-assemble the PK-900.
1) Remove Power
2) Remove 2 hex jack screws on either side of the RS-232 SERIAL jack screw.
Use a 3/16" hex nut driver to avoid scarring the jack screw finish.

<!-- PDF p.378 -->

6
3) Remove the two serrated nuts from the RX IN AUDIO jacks. Use a special nut driver made for
this purpose if you have one. If not, use a spanner wrench if you have one.  A pair of needle nose
pliers can be used as a spanner wrench if you are very careful not to slip and scratch the panel or
break the tips on the pliers.
4) Remove 4 screws holding chassis top - two on each side of the PK-900 top cover.
5) Disconnect the cables connecting the PK-900 front panel and the mother board.
6) Remove 3 screws holding back panel - one on each side and one on the bottom of the PK-900
bottom cover.
7) Remove the back panel from the PK-900.
8) Remove the remaining screws on the bottom of the chassis that fastens the motherboard to the
chassis.
9) Remove the motherboard from the chassis.
10) If you have a 9600 baud board installed near the back panel, carefully remove the 9600 baud
board by removing the four screws holding it down and gently pulling it upward to remove it
from its socket.  Be very careful not to bend the 20 pins on the bottom of the board. Set it aside
in a protected place. It will be reinstalled later.
11) If you have a PK-900/DSP
with a Timewave DSP daughterboard, go to section named "Installation with a DSP Daughterboard"  for further installation instructions.
12) If you have a PK-900 without a Timewave DSP upgrade kit,  go to section named "Installation without a DSP Daughterboard"  for further installation instructions.

<!-- PDF p.379 -->

7
Installation with a DSP Daughterboard
1) Skip this step if  Eprom U7 on the DSP daughterboard is ver. 7.3. If Eprom U7 is ver. 7.2 or lower,
locate and remove U7.
2) Use an IC removal tool if you have one. If not, slide the flat blade of the screwdriver under one end
of the chip and elevate carefully. Do the same thing at the other end of the IC and it will pop out
cleanly. BE SURE the screwdriver is between the IC and the chip socket,
NOT between the chip
socket and the board.
3) Install the new 32 pin eprom in the U7 socket on the DSP daughterboard. Be very careful to orient
the notch in the end of the chip to match the notch in the outline on the circuit board. Make sure all
pins are straight and fit direcltly into the Eprom socket before you carefully press the new eprom
into the socket. All pins should be inserted directly into the socket with no pins curled under the chip
or bent outside the socket.
4) Follow the next steps to remove components from the main PC board.
5) Locate and remove C40  near J1 (see figure B).
We recommend that you remove the capacitors and resistors by desoldering them from the back of
the board. Remove the solder from the holes with a solder sucker tool or solder wick.  Use care not
to overheat the board. You may burn the board or damage the pad or barrel in the hole.  When hole
and the lead are clear of solder, gently remove the component from the top of the board. In some
cases, it may be easier to remove the component by clipping the leads of the component close to the
body of the component before desoldering it..  Then while holding the lead with the needle nose
pliers, CAREFULLY heat the solder just until the lead can be removed without force.  Be very
careful not to pull the barrel from the board by pulling before the solder flows. DO NOT OVER-HEAT.  The hole can then be cleaned with a solder sucker tool or solder wick.
You will also need to clean the solder from the C40 pads. Observe the same precautions as above
and do not overheat the board.
6) Locate and remove the wire on the pad on the DSP daughterboard labeled "C89 Rear". Remove the
other end of the wire from the rear pad of C89 on the PK-900 main board. Discard the the removed
wire.
You will also need to clean the solder from the C89 rear pad and the daughterboard pad labeled
"C89 Rear"  Observe the same precautions as above and do not overheat the board.
7) Remove R253, if it is installed on your PK-900. See Figure A.
You will also need to clean the solder from the R253 pads. Observe the same precautions as above
and do not overheat the board.
8)  Re-install the main board on the chassis using the screws you saved when you removed it.
9)  Reconnect the front panel connectors to the main board.

<!-- PDF p.380 -->

8
.Installation with a DSP Daughterboard
Connections
Follow the next steps to connect the wires from the back panel as shown in Figure A and B.
1) Connect PSK enable J5 on PSK board (grey wire) to PB0 (hole from lead of  R253 closest to left side
of PK-900).
2) Connect J1 (+12V) (red wire) to D21 (1N4004  smaller diode on the left closest to the heatsink)
anode lead on PK-900 main board. The anode lead is toward the front of the PK-900 and is the end
of the diode
without the light-colored band on the body of the diode. Wrap the stripped wire lead
from J1 around the lead of the diode as far as possible from the body of the diode. Solder the two
leads together.
3) Connect J6 (GND) (black wire) to to the solder lug on the top of the DC power jack on PK-900 main
board.
4). Connect J2 (RX Audio in) (violet wire) to C89 Rear Pad on PK-900 main board.
5) Connect J3 (RX audio out) (white wire) to the pad on the PK-900 DSP board labeled "C89 Rear".
6). Connect J7 (TX audio in) (brown wire) to the C40 left pad on PK-900 Main board.
7). Connect J4 (TX Audio out) (orange wire) to the C40 right pad on PK-900 main board.
8) If you have one, re-install the 9600 baud board. Be very careful not to bend the 20 pins on the bottom
of the board.  Fasten the board with the four screws removed when the board was temporaily removed. All the leads from the PSK board should be under the 9600 baud board. Do not pinch any
wires under the board standoffs or sockets.
9) Fasten the new back panel assembly to the bottom cover of the PK-900 with 3 screws removed with
the old back panel.
10) Secure the chassis top with all 4 screws.
11) Place the enclosed sticker (PSK) below the raised "AEA" logo on the left side of the front
panel of the PK-900.  Place it one inch above  the bottom edge of the panel.
12) Reconnect the unit and reset when powering on for the first time.

<!-- PDF p.381 -->

9
Installation without a DSP Daughterboard
1)  Locate and remove U36, U37 and U44 (See Figure A.)
2) Use an IC removal tool if you have one. If not, slide the flat blade of the screwdriver under one end
of the chip and elevate carefully. Do the same thing at the other end of the IC and it will pop out
cleanly. BE SURE the screwdriver is between the IC and the chip socket,
NOT between the chip
socket and the board.
3) Install the new 32 pin eprom in U36. Be very careful to orient the notch in the end of the chip to
match the notch in the outline on the circuit board. Make sure all pins are straight and fit direcltly
into the Eprom socket before you carefully press the new eprom into the socket. All pins should be
inserted directly into the socket with no pins curled under the chip or bent outside the socket.
4) Install the new PAL C  in U44. Be very careful to orient the notch in the end of the chip to match the
notch in the outline on the circuit board. Make sure all pins are straight and fit direcltly into the
Eprom socket before you carefully press the new eprom into the socket. All pins should be inserted
directly into the socket with no pins curled under the chip or bent outside the socket.
5) Locate and remove C40 near J1 (see Figure B).
We recommend that you remove the capacitors and resistors by desoldering them from the back of
the board. Remove the solder from the holes with a solder sucker tool or solder wick.  Use care not
to overheat the board. You may burn the board or damage the pad or barrel in the hole.  When hole
and the lead are clear of solder, gently remove the component from the top of the board. In some
cases, it may be easier to remove the component by clipping the leads of the component close to the
body of the component before desoldering it..  Then while holding the lead with the needle nose
pliers, CAREFULLY heat the solder just until the lead can be removed without force.  Be very
careful not to pull the barrel from the board by pulling before the solder flows. DO NOT OVER-HEAT.  The hole can then be cleaned with a solder sucker tool or solder wick.
You will also need to clean the solder from the C40 pads. Observe the same precautions as above and
do not overheat the board.
6) Locate and remove C89  (see Figure B).
You will also need to clean the solder from the C89 pads. Observe the same precautions as above
and do not overheat the board.
7)  Remove R253, if it is installed on your PK-900. See Figure A.
You will also need to clean the solder from the R253 pads. Observe the same precautions as above
and do not overheat the board.
8)  Re-install the main board on the chassis using the screws you saved when you removed it.
9)  Reconnect the front panel connectors to the main board.

<!-- PDF p.382 -->

10
Installation without a DSP Daughterboard
Connections
Follow the next steps to connect the wires from the back panel as shown in Figure A and B.
1) Connect PSK enable J5 on PSK board (grey wire) to PB0 (hole from lead of  R253 closest to left side
of PK-900).
2) Connect J1 (+12V) (red wire) to D21 (1N4004 smaller diode on the left closest to the heatsink) anode
lead on PK-900 main board. The anode lead is toward the front of the PK-900 and is the end of the
diode
without the light-colored band on the body of the diode. Wrap the stripped wire lead from J1
around the lead of the diode as far as possible from the body of the diode. Solder the two leads
together.
3) Connect J6 (GND) (black wire) to to the solder lug on the top of the DC power jack on PK-900 main
board.
4). Connect J2 (RX Audio in) (violet wire) to C89 Rear Pad  on PK-900 main board.
5) Connect J3 (RX audio out) (white wire) to the C89 Front pad on the PK-900 main board.
6). Connect J7 (TX audio in) (brown wire) to the C40 left pad on PK-900 Main board.
7). Connect J4 (TX Audio out) (orange wire) to the C40 right pad on PK-900 main board.
8) If you have one, re-install the 9600 baud board. Be very careful not to bend the 20 pins on the bottom
of the board.  Fasten the board with the four screws removed when the board was temporaily removed. All the leads from the PSK board should be under the 9600 baud board. Do not pinch any
wires under the board standoffs or sockets.
9) Fasten the new back panel assembly to the bottom cover of the PK-900 with 3 screws removed with
the old back panel, the two knurled nuts on the RX AUDIO IN jacks, and the two jack screws on the
RS-232 Serial Connector.
10) Secure the chassis top with all 4 screws.
11) Place the enclosed sticker (PSK) below the raised "AEA" logo on the left side of the front
panel of the PK-900.  Place it one inch above  the bottom edge of the panel.
12) Reconnect the unit and reset when powering on for the first time.

<!-- PDF p.383 -->

11

<!-- PDF p.384 -->

12

<!-- PDF p.385 -->

13
Jumper settings
JH1 Jumper must be ON for units with a DSP board installed. It must be OFF for all other units.
JH3 and JH4 Jumpers adjust the receive gain to the input of the PK-900/PSK according to following
table:
JH3        JH4         Relative Gain
OFF    OFF High
ON    OFF Medium (Factory Default)
OFF    ON Low
ON    ON Low
Section 2
PK900/PSK Sound Card Interface Board Settings
Trimpot settings
Trimpot RV1 adjusts the receiver audio signal output level to the sound card input.  Use this trimpot to
reduce the signal level to the soundcard if  your received signal level is overdriving your sound card
input. (Check the sound card input level indicators on your computer audio control program.)
Trimpot RV2 adjusts the Sound Card audio signal output level to the transmitter audio input.  Use this
trimpot to reduce the signal level to the transmit if  your sound card output signal level is overdriving
your transmitter input.  It is very important not to overdrive your transmitter to avoid interfering
with other stations!

<!-- PDF p.386 -->

14
Section 3
Sound Card Interface Connections for the PK-900/PSK
The Sound Card interface connections are made to the gold connectors labeled "SOUND
CARD AUDIO" on the back of the PK-900/PSK.  Timewave supplies cables to connect
the sound card to the PK-900/PSK.  The cables are supplied with each PK-900/PSK and
upgrade kit. This connection information is provided for the user's convenience.
PK-900/PSK                   Connection
TO To Sound Card Audio input.
INPUT Insert phone plug into sound card input jack (blue ring around
jack on most sound cards). Use the mic jack input (pink) if
you need more gain.
The phone plug tip and ring are the signal connections. The
tip is the factory default connection.  To select the ring use
Jumper JH1 on the Jack PC board mounted on the back panel.
Phone plug sleeve is the signal return connection.
FROM From Sound Card Audio Ouput (to transmitter audio input)
OUTPUT Insert phone plug into sound input card jack (green ring
around jack on most sound cards).
The phone plug tip and ring are the signal connections. The
tip is the factory default connection.  To select the ring use
Jumper JH2 on the Jack PC board mounted on the back panel.
Phone plug sleeve is the signal return connection.
TO Plug computer speaker into this jack. It is a parallel connection
SPKRS to the" FROM OUTPUT " jack, so that you don't have to have
a separate "Y" connector to plug in your computer speakers.

<!-- PDF p.387 -->

15
Computer
PK-900/PSK
Radio
Transceiver
PK-900 CPU
PK-900
Modems
Computer
Sound Card
Computer
CPU
Isolation
Transformers
Serial -RS-232
Sound Card
Audio Port
PTT IN
AFSK IN
Radio
Port 1
DSP
Filters
IN
OUT
AFSK OUT
Sound Card
Adaptor
System Block Diagram
PK-900/PSK
Multimode Controller
PTT
Data Audio
Sound Card
Data Audio
PK-900
Data Audio
Serial -RS-232
To Computer
Speakers

<!-- PDF p.388 -->

16
Section 4
Sound Card Interface Operation for the PK-900/PSK
To use the sound card interface, you will need a PC software program for the mode you
wish to use. PK-Term for Windows and Digipan are two programs that include PSK-31.
PK-Term fully supports the PK-900/PSK for seamless automatic sound card operation.
Programs like Digipan may not support the new PK-900 SCD command to select the PK-
900 Sound Card mode and switch the PK-900 PTT output between transmit and receive.
To support programs like Digipan that have not yet been updated, Timewave has included
a free utility program called ModemSwitch™. This program helps you set up the correct
COM port and baud rate on your computer, and lets you select the sound card mode and
operate the PK-900 PTT output with a few clicks of your computer mouse buttons. It
even has a rudimentary dumb terminal for a quick check of your data controller. Read the
ModemSwitch help file for more information. Be sure to download the latest upgrade for
ModemSwitch™. See the Timewave document "PSKSoftware.pdf" for more third party
software information on Sound Card programs.  Both are on the Timewave CD and the
the Timewave website:
http://www.timewave.com
A new command has been added to the PK-900 to select the sound card interface.  The
new command is "Soundcard". The short version of the command is "SCD".  To select
the sound card interface, type "SCD ON" when you are in  the PK-900 command mode
(CMD:).  To return to the normal TNC mode, type "SCD OFF" when you are in  the PK-
900 command mode (CMD:).  Some programs, such as PK-Term for Windows from CSS,
have integrated the sound card command into their operation. In those programs the
sound card command will be executed automatically whenever you select the PSK-31
mode.
If you would like the latest demo copy of PK-Term for Windows, download it from http:/
/www.cssincorp.com.  A copy is also on the Timewave CD for your convenience.

<!-- PDF p.389 -->

17
Section 5
PK-900/PSK Sound Card Interface Schematic Diagrams

<!-- PDF p.390 -->

11
23-Dec-2002
N:\PRODUC~1\PK-900\PK900PSK\PCBOAR~1\PK232P~1\PK900PS4
Title:
Size:
TW P/N:
Date:
File:
Sheet ofA
Engineer:
Project:
Timewave Technology Inc.
R. Gawtry
1 4
3 6
PS
T1
XFMR
1 4
3 6
PS
T2
XFMR
Q2
MBT4401
16
98
116
134
1
K1
RELAY_DPDT
C1
.01 uF
D1
1N4004
JH1 DSP Cap bypass
J1
+12VDC
J2
Rx Audio
J4

J5
Sound card select - low  for sound card
J6
Gnd
J3
RX Audio
J7
TX Audio
Radio 1 jack on PK-900
GND
Radio 1 Jack on PK-900
+12 VDC
From PK-900 PB0 - R253
Sound Card Speaker Out
Sound Card Audio in
From RX Audio out
To TX Audio in
From PK-900
From PK-900
From PK-900 Audio out
To PK-900 Audio in
RV2
500
R1
0
R2
10K
R9
10K
RV1
500 J8
PAD
J9
PAD
J10
PAD
D2
LED
R10
3.3K
3
2
1
8 4
U1A
OPAMP_DUAL
5
6
7
U1B
OPAMP_DUAL
R3
10K
R12
20K
R4
10K
C5
10 uF
C6
10 uF
C7
10 uF
C8
1 uF
JH3 RX Gain
R13 47K
JH4 RX Gain - default on
VI1
ADJ4
VO 2
VO 3
VO 6
VO 7
U2
LM317_SO8
R14
270R11
1K
C9 1.uF
R5
10K
+12
+12
C10
10uF
C11
10uF
C12
10uF
C2
.01 uF
R15
47
C3
.01 uF
R16
0
R17
0
R6
10K
R7
10K
R8
10K
C13
470.0 uF
J12
PAD
Sound Card in audio level
R18
0

R22
620
or to DSP board in
Q1
MBT4401
R23
10K
+12
Sound Card Signal Ground
Sound Card Signal Ground

<!-- PDF p.391 -->

11
9-Dec-2002
N:\PRODUC~1\PK-900\PK900PSK\PCBOAR~1\PK900P~1\PK900JAC
Title:
Size:
TW P/N:
Date:
File:
Sheet ofA
Engineer:
Project:
Timewave Technology Inc.
R. Gawtry
J1
PHONEJACK STEREO
J2
PHONEJACK STEREO
J3
PHONEJACK STEREO
J4
PAD
J5
PADJ6
PAD
J7
PAD
C5
1 nF
C6
1 nF
C7
1 nF
C8
1 nF
1
2
3
JH1
JHS3
1
2
3
JH2
JHS3

<!-- PDF p.392 -->

> [No extractable text on this page — figure, schematic, or blank.]