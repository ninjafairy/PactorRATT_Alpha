# Chapter 2 — Computer Installation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 20–32).

**Agent extract (do not invent extra pins):** DSP-2232 is RS-232 **DCE**, DB-9. Computer TX → pin **3**; computer RX ← pin **2**; GND pin **5**; RTS/CTS pins **7/8**. Default `XFLOW` software XON/XOFF; `XFLOW OFF` for hardware handshake. Autobaud: LCD `Press *`, type `*`. First-run walkthrough uses 1200 7E1. Sign-on ends at `cmd:`. `MYCALL` example `MY AAA` → `MYcall now AAA/DSP`. Loop-back connect `C AAA`. Rear RESET + power restores autobaud. Center pin of DC plug is **+**.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.20)

### 2.1

#### 2.1.1

### 2.2

Overview
In this chapter we will connect the DSP-2232 to the RS-232 Serial port
of your Computer or Computer Terminal . After the Serial connection
has been made we will perform a quick check of the internal software.
Finally we will check the DSP-2232's modem by performing a Packet
" loop-back" test. When you have completed this chapter, you will be
ready to connect the DSP-2232 to your receiver or transceiver and
begin using it on the air.
Equ ipment Required
You will need the following for this chapter:
your DSP-2232 Data controller;
a 13.6-v01t DC, 1. 5-amp (or. greater) regulated power supply such
as those sold by Radio Shack (or an AEA AC-4) ;
(the power supply must be able to supply at least 12 VDC to the
DSP-2232 while it is operating under load)
the included DSP-2232 DC power cord unless the AC-4 is used;
your Computer or Computer Terminal ;
a Communications or Terminal Emulation program for your computer;
(not needed if a Computer Terminal is being used)
the included RS-232 cable with 9-pin "D" connector on one end and
a 25-pin "D" connector on the other end;
one of the included 5-pin shielded "Radio cables" •
(note that the radio cables may arrive as a single 10-ft.
which should be cut in half producing two 5-ft. cables.
the 5-pin DIN connector with the jumper wire "Loop-back" .
cable
wire cutters and strippers or a small pocket knife, a small
straight-blade screwdriver and a medium Phillips-head screwdriver .
Unpacking the DSP-2232
Carefully remove the DSP-2232 from the box and its plastic bag.
Inspect the unit for signs of damage that may have occurred in
shipping. If there is visible damage, please contact the dealer or
shipper. Do not attempt to install or use a damaged DSP-2232.
We will be discussing some of the Controls, Indicators and Connections
in this installation so take a few moments to familiarize yourself
with them. The f igures on the next pages may help with their
locations .

### (PDF p.21)

#### 2.2.1

Connecting Power
MAKE SURE YOUR POWER SUPPLY IS OFF AND UNPLUGGED BEFORE WIRING
Locate the DSP-2232 Power Cable in the acceggory bag. Strip Off
just enough insulation from the endg to connect it to your 12-14
Volt DC requ lated power supply.
The Center pin of the coaxial power plug is POSITIVE. Connect
the lead with the White stripe to the POSITIVE (+) lead on your
power gupply. Check this with an Ohm-meter if you have one.
Connect the golid Black (GROUND) lead to the NEGATIVE (-) lead of
your power supply.
(The AEA AC-4 Wall Trangformer is already
wired correctly. )
Connect the power plug to the 13 VDC Power
rear of the DSP-2232.
DO NOT CONNECT YOUR
Plug in your power supply or AC-4 and turn
DSP-2232 by depressing the Power Switch on
Receptacle on the left
COMPUTER YET.
on power. Turn on the
the rear of the unit.
left should light and
occurs, then switch
At power-on, the
soon display the
OFF the DSP-2232
If the STATUS display
insure that 12-14 VDC
pin is POSITIVE.
If the STATUS display
LCD STATUS display on the
words "Pregg
If this
and move on to section 2.3.
does not light, then re-check the above gtepg to
is available at the power plug and the center
lights, but a message other than "Press *" is
displayed, then the DSP-2232 has probably been initialized. If the
DSP-2232 has been initialized it is ready to communicate with a
computer or terminal at a specific baud rate (probably 300, 1200,
2400, 4800 or 9600 baud).
If you know what this baud rate is then you
should continue with the installation at section 2.3 keeping this in
mind .
If you do not know what baud rate the DSP-2232 has been initialized to
then you should reset the DSP-2232 by pressing the rear-panel RESET
switch at the same time you turn on the power switch. After this is
done, the "Press *" message should be observed on the LCD STATUS
display.
If the above did not produce the "Press *" message, then contact AEA
Technical Support Department as described in the front of this manual.
osp-Z232
DSP
OIGITN- SIGNN- PRCESSOR
TUNE
MULTI-MODE DATA CONTROLLER
Figure 2-1
DSP-2232 Front Panel Controls and indicators

### (PDF p.22)

snr-
Cu KEYING OJT
PARALLEL
ttC.
usa
RESET
PR 1 N TER
ASSEHOLCO IN U.s.n.
+t3VOC RCOIO RMIO
z
Figure 2-2
DSP-2232 Rear Panel Connections and Control g
Port 1
AGC Level
Port 2
Port 2
AFSK Level
Port 1
AFSK Level
Figure 2-3 DSP-2232 Side Panel AFSK Controls
Connecting Your Computer or Computer Terminal

### 2.3

#### 2.3.1

MAKE SURE THE DSP-2232 AND YOUR COMPUTER ARE SWITCHED OFF
Locate the DSP-2232 Serial Cable. Connect the 9-pin Female
connector to the RS-232 connector on the DSP-2232 rear panel.
Connect the other end of this cable (Female DB-25) to the RS-232
Serial Port of your personal computer or Computer Terminal.
Details on connecting to common machinea are listed below.
NOTE: This cable was designed to connect directly to a 25-pin IBM-PC
compatible RS-232 port. Many machines on the market today support
this configuration.
Some legs-common machineg are ligted in gection

### 2.6

P leage be certain you have properly connected the DSP-2232 to
your RS-232 computer or Terminal then proceed to section 2.4.
IBM-PC/.XT/AT and Compatibles
IBM compatible 25-pin RS-232 serial port g should connect directly to
the supplied serial cable.
Some IBM compatible machines are equ ipped
with a 9 -pin serial port.
For these machines a DB-9 to DB-25 adapter
should be obtained from a Radio Shack store or a computer dealer.

### (PDF p.23)

#### 2.3.2

#### 2.3.3

### 2.4

Apple Macintosh Series of Computers
AEA presently sells the program MacRATT with FAX which contains the
serial •cable for the newer models of the Macintosh (models Mac + and
later).
If you intend to use another communications program with the
DSP-2232 a Modem adapter cable must be purchased from your Apple
dealer to connect the Mac to your DSP-2232.
For the newer machines
such as the Mac + , Mac SE and Mac II, a mini-8 to DB-25 adapter cable
is required ( included with the AEA MACRATT with FAX program) .
For the
older Mac 128 and 512 machines, a DB-9 to DB-25 adapter cable from
your Apple dealer is needed.
Commodore 64 and 128 Computers
The Commodore 64/ 128 computers do not have an RS-232 port as standard
equ ipment .
For these machines RS-232 adapters are available from
manufacturers such as Commodore or OMNITRONIX. These may work with
modem or terminal programs for your Commodore. Section 2.4 of this
chapter deals more with programs for personal computers. The AEA
Program COM-PAKRATT With FAX is NOT recommended for the DSP-2232.
Computer Terminal
If you have an RS-232 Computer Terminal, sometimes called a Dumb-
Terminal, Smart-Termi•nal or ASCII-Terminal, you may need to change the
gender of the cable provided with your DSP-2232.
This can be
accomplished with an inexpensive double-male RS-232 gender changing
adapter available from Radio Shack and other computer dealers. The
Radio Shack part number is 26-243.
Setting up Your Communications or Terminal Software Program
If you will be using your DSP-2232 with a Computer, you will need to
read parts of this section to set up your Communications or Terminal
If you will be using your DSP-2232 with a Terminal you will
Software .
not need any software and may skip to section 2.5.
Setting up a Communications program for your DSP-2232 is very
important. How your screen looks when you use your DSP-2232 depends
completely on your Communications program. AEA currently makes
available programs for the IBM-PC and compatibles and the Apple
Macintosh computers. These products are customized for radio
communications and are available at extra cost from your AEA dealer.
The DSP-2232 operates in much the same manner as a telephone modem and
most modem Terminal Programs will control a DSP-2232 quite nicely.
Some of these programs are "Public Domain" which means they are FREE.
Other Terminal Programs are "Share-ware" which means you may get them
from a friend and try them before you buy them. Whether you are using
an AEA program or one of your own choosing, see the section below for
the particular type of computer you plan to use.

### (PDF p.24)

#### 2.4.1

#### 2.4.2

Terminal Programs for IBM PCs and Compatibles
Although you can use almost any terminal program with your IBM PC or
close compatible, AEA currently sells the PC-PAKRATT II w/ FAX program
which provides many features not available in "telephone modem"
see your AEA dealer for information on PC-PAKRATT 11 w/ FAX.
prog rams .
If you already have the PC-PAKRATT II program, follow the program
manual and install the software on your computer.
You should algo
read through the PACKET OPERATION chapter of the PC-PAKRATT 11 manual.
Familiarity with Packet operation of PC-PAKRATT will be necessary for
performing a quick-check of the DSP-2232 in section 2.5 of THIS
manual.
As we mentioned above, an AEA program is not required to use the
DSP-2232. Many terminal programs can be found throughout the amateur
radio community or can be downloaded from Compuserve, GEnie and from
many telephone bulletin boards .
A partial list of PC programs tested with the DSP-2232 includes:
PROCOMM, CROSSTALK-XVI, SMARTCOMN, RELAY, BITCOM, QMODEM, PC-TALKf
CTERM, HAMCOM, HAMPAC, YAPP and the terminal program included with
Microsoft Windows 3.0 (tm) .
Follow the installation directions that come with the Terminal program
you wish to use. Once installed on the computer, you should start the
program and set the communication parameters for the following:
Data Rate
Data bits
Parity
Stop bits
= 1200 bits per second (Bauds)
= EVEN
Once these settings have been achieved and the correct serial
communications port chosen, you may proceed to section 2.5.
Terminal Programs for the Macintosh
Although you can use almost any terminal program with your Macintosh,
AEA presently sells the MACRATT with FAX program which provides many
features not available in "telephone modem" programs .
See your AEA
dealer for information on MACRATT with FAX.
If you already have the MACRATT program, please follow the program
manual and install the software on your Mac.
You should also read
through the PACKET OPERATION chapter of the MACRATT manual .
Familiarity with Packet operation of MACRATT will be necessary for
performing a quick check of the DSP-2232 in section 2.5 of THIS
manual.
As we mentioned above, an AEA program is not required to use the
DSP-2232. Many terminal programs can be found throughout the amateur
radio community and can be downloaded from Compuserve, GEnie and from
many telephone bulletin boards.

### (PDF p.25)

A partial list of Mac programs tested with the DSP-2232 includes:
MAC TERMINAL, RED RYDER, MICROPHONE, SMARTCOMM 11 and MOCK TERMINAL
Follow the installation directions that come with the Terminal program
you wish to use. Once installed on the computer, you should start the
program and set the communication parameters for the following:
COMPATIBILITY :
1200 bauds, 7 bits/ character, even parity,
FULL-DUPLEX, Modem connect ion ,
" t e 1 ephone "
Once these settings have been achieved, proceed
Terminal Proarams for the Commodore 64 64C and
Handshake XON/XOFF,
port .
to section 2.5.
128

#### 2.4.3

Although AEA presently sells the COM-PAKRATT with FAX program package
for the PK-232, this program cannot access all the features Of the
DSP-2232. AEA therefore cannot recommend using the COM-PAKRATT
If you already have this package, it will
package with the DSP-2232.
certainly get you started with the DSP-2232 by using the "Dumb
Terminal" mode. You may wish to . find another program which provides
more features than are available in the COM-PAKRATT program in the
Dumb Terminal Mode. Other ideas for terminal programs for the
Commodore-64 series Of computers are listed below.
Many terminal programs can be found throughout the amateur community
or can be downloaded from Compuserve and from many telephone bulletin
boards .
In addition a BASIC communications program is listed in the
Programmer's Reference Guide published by Commodore. Use the program
We suggest you operate your DSP-2232 at 300
listing for "True ASCII" .
bauds with these computers to avoid possible speed difficulties.
Follow the installation directions that come with the Terminal . program
you wish to use. Once installed on the computer, you should start the
program and set the communication parameters for the following:
Data Rate
= 300 bits per second (Bauds)
Data bits
Parity
= EVEN
Stop bits = I
Once these settings have been achieved,
proceed to section 2.5.

### (PDF p.26)

### 2.5

1.
System Startup and Loop-back Test
Make sure that you have connected your DSP-2232 to a 12-14 Volt DC
power source and to the RS-232 port of your computer or Terminal.
If you are using a computer, you must also have a communications
program and be familiar with its operation.
You are now ready to
begin the following DSP-2232 Startup and Loop-back test procedure.
2.
3.
4.
5.
Don •t connect any cables to your radio yet !
Remove the 5-pin DIN "Loop-back" connector from the DSP-2232
accessory bag .
Plug this connector into the RADIO-I connector on the
DSP-2232's rear panel.
Set both AFSK levels on the right side of the DSP-2232 to
rotation (straight up and down) using a small screwdriver.
Turn on your computer. Load and run your communications program.
If you are using an AEA PAKRATT program, follow the program
instructions to enter the Packet mode, then skip to step 11.
If you are using another Terminal Program or a Computer Terminal,
Set your computer' s terminal program to:
1200 bauds (if available) ;
seven-bit word;
even parity;
one stop bit.
NOTE: You may use other terminal baud rates with the DSP-2232 - we
recommend 1200 baud here to keep this procedure easy and consistent .
Press the DSP-2232's power switch to the ON position.
At power-on, the LCD STATUS display on the left should light and
If the STATUS display lights,
soon display the words "Press *"
but a message other than "Press *" is displayed, then the
DSP-2232 has probably been initialized. If you know the terminal
baud rate the DSP-2232 has been set to, you may proceed to step
11; otherwise you must reset the DSP-2232 as described below.
To reset the DSP-2232, simply press the rear-panel RESET switch
at the same time you turn on the power switch. After this is
done, the "Press *" message should be observed on the LCD STATUS
display .
If your serial port is operating at 1200 bauds as we recommend,
you ' 11 see the "autobaud" message:
Please type a star ( * ) for autobaud routine.
If your serial port is operating at 300, 2400, 4800 or 9600
bauds, you may see some "garbage" characters.
This is normal and you should proceed with step 10.

### (PDF p.27)

Type an asterisk ( * ) •
computer's data rate,
then display the sign-on message:
DSP-2232 is
When the DSP-2232 has
the CMD LED will light.
using default values.
AEA DSP-2232 Data controller
Copyright (C) 1986-1991 by
Advanced Electronic Applications,
Release DD.MMM. YY
cmd :
" recognized" your
Your screen will
Inc.
8.
10.
Make note of the Release date on the first page of this manual.
This is important should you ever call AEA for technical support.
It should match the firmware release sticker on the bottom of
your DSP-2232
If you are using an AEA program, follow the instructions in the
program manual to enter the packet callsign (MYCALL) of AAA into
the DSP-2232.
Even though this is not your callsign, please do
this for this procedure. You must change it to YOUR OWN CALLSIGN
after completing this procedure.
If you are using a Computer Terminal or a non-AEA terminal
program the following will set your packet callsign to AAA:
Enter MYCALL by typing MY AAA (or .
(<RETURN> or <Enter> means type the single key on your keyboard. )
Your monitor should display:
MYca11 was DSP/DSP
MYca11 now AAA/DSP
If you are using an AEA program follow the instructions to
CONNECT in packet mode to AAA. Since you have j ust entered your
callsign as AAA, you will connect to yourself.
If you are using a Computer Terminal or a non-AEA program,
entering the following after the "cmd:" command mode prompt will
cause the DSP-2232 to Connect to AAA:
C AAA
After a few moments, your monitor should display:
* * * CONNECTED to AAA
Type HELLO SELF
After a few moments, your monitor should echo the same message.
If you have gotten this far then the digital section of the
DSP-2232 and the VHF packet modem are both working .

### (PDF p.28)

If you are using an
11.
12.
13.
We will now check the DSP-2232's HF modem.
AEA program, follow the instructions to select the HF modem by
turning the VHF Parameter OFF; this will automatically set the
radio baud rate HBAUD to 300 for HF packet work.
If you are using a Computer Terminal or a non-AEA terminal
program, the following sets the HF mode of the DSP-2232:
Type
(Type C while pressing the <Ctr1> key down. )
Your monitor should respond with the command prompt :
cmd :
Then enter VHF OFF
Your monitor should respond with:
Vhf
Vhf
was ON/ON
now OFF/ON
*** HBAUD now 300/0
If you are using an AEA program type HELLO SELF <Enter>
Your monitor should soon echo the message you 've just typed.
If you are using a Computer Terminal or a non-AEA terminal
program, you must first type CONV or K followed by a <Enter>.
Now you may type a few characters. Your monitor should soon echo
the characters you 've just typed.
If you are using an AEA program, follow the instructions to
DISCONNECT from a Packet station.
If you are using a Computer Terminal or a non-AEA terminal
program the following will cause the DSP-2232 to DISCONNECT:
Enter <CONTROL-C>
Your monitor should respond with the command prompt :
cmd :
Enter D <Enter>
Your monitor should respond with:
```text
cmd: DISCONNECTED: AAA
```
pl (UA)
If all of the above steps were successful, you 've completed the quick-
check and are ready to proceed to Chapter 3.
In Chapter 3 you will
connect your DSP-2232 to your radio and begin using it "on the air" .
If you have problems with the steps shown above, go back to Step 1
AFTER checking all cables and connectors. Read each step again
carefully.
The most common problems are trying to connect to a call
different from AAA, not having the loop-back jumper connected, or
not setting the AFSK level to rotation.
If you still have problems, leave your DSP-2232 ON and contact AEA's
Technical Support Department as suggested in the front of this manual.

### (PDF p.29)

#### 2.6.1

### 2.62

### 2.63

#### 2.6.4

#### 2.6.5

### 2.6

Detailed RS-232 Connections for Other Computers
If the type of computer you plan to use with the DSP-2232 was not
mentioned in the beginning of this chapter, you may find specific
connection information in the sections below. You will also need a
Communications program to use with your computer which AEA can not
provide. See section 2.7 for information regarding Cornmunication
programs for many of these machines.
Some computers require a serial port adapter card that incorporates
the necessary RS-232-C interface circuitry. The IBM-PC and Apple I I
series of computers are good examples of this.
Computers that do not have a serial port or do not permit use of a
suitable adapter or level converter cannot be used with the DSP-2232.
Apple II Series
The Apple II, 11+ and Ile computers require an RS-232 Serial card to
The most popular we know about is the
connect to your DSP-2232.
Super-Seri al Card which should be available from your Apple dealer.
Commodore C-64 C-128 and Vic 20
Commodore, OMNI TRON IX and other manufacturers sell a signal level
converter that is installed in the User Port Connector on the rear of
the computer. The converter changes the computer's internal TTL
voltage levels to the proper RS-232-C voltage levels and polarities .
IBM PCir
The PCjr uses standard RS-232-C voltage levels; however, the
connector is not standard and is hard to find. Pin-out information
Some dealers
can be found in the IBM PCjr Technical Reference Manual.
sell a 'IBM PCjr Adapter Cable for Serial Devices' that converts the
connector on the PCjr to standard The cable attaches
between the PCjr and the DSP-2232 Serial Cable.
Tandy Color Computer
The coco series (except for the Micro coco) uses a four-pin DIN
connector for its serial interface. Wire a cable as shown below.
All
necessary parts
coco
4
2
3
should be
available from your Radio Shack dealer.
DSP-
8201
2232 (DB9S)
3
Tandy. Model 100 / 102
and
NEC
The Model 100/102 and NEC 8201 have built-in standard RS-232 serial
You'll need a DB-25
ports which are compatible with the DSP-2232 .
male-male gender changing adapter to use the supplied DSP-2232 Serial
Cable.

### (PDF p.30)

#### 2.6.6

#### 2.6.7

DSP-2232
Other Computers with RS-232-C Ports
If your computer has an RS-232 port, consult your computer manuals to
see which pins are used for Transmit-Datar Received-Data and Signal-
Ground. Read the manufacturer's recommendations for connecting the
serial port to a modem and connect your DSP-2232 in the game way.
Your DSP-2232 is conf igured as Data Communications Equipment (DCE)
which receives data on pin 3 of the 9-pin DB-9 connector or pin 2 of
the 25 pin cable supplied with the unit. Most computers and terminal g
are configured as Data Terminal Equipment (DTE) transmitting data on
pin 3 of a DB-9 or pin 2 of a DB-25 RS-232 connector.
If your computer is configured as DTE:
Use the supplied RS-232 cable with a Gender changing adapter if
necessary. These are available from Radio Shack ( Part # 26-243)
and other computer stores .
If your computer is configured as DCE:
You may want to purchase a Null Modem adapter from Radio Shack
(Part # 26-1496) or other computer store.
You may also wire your own cable directly to the DSP-2232 's DB-9
connector by wiring the Transmit Data (TXD), Receive Data (RXD)
and the Signal Ground
to
diagrammed
coco
TXD
RXD
GND
below:
As a default
the
provides
a DB-9S (Female) connector as
DSP-2232 (DB9S)
5
XON/XOFF software flow-control
to the computer or terminal. The command XFLOW can be turned OFF
to enable hardware handshake if your computer requires it.
Hardware flow control is achieved with RTS/CTS (pins 7 and 8) of
the 9-pin connector on the DSP-2232 's rear panel.
Other Computers with Non-standard Serial Ports
Computers with non-standard serial ports must meet the following
condit ions :
The signal levels must be compatible with The DSP-2232
requires the voltage levels from the computer be greater than +3
volts in the "asserted" state and O volts or less in the "non-
asserted" state.
The signal polarity must conform to the RS-232-C standard.
O or negative-voltage state must correspond to logical " 1 "
the positive-voltage state to logical "O. "
The
and
The computer must be able to correctly receive a signal that
meets asynchronous RS-232-C specifications. The DSP-2232
supplies signals that meet this specification.

### (PDF p.31)

Make or buy a cable that provides the following connections:

### 2.7

#### 2.7.1

#### 2.7.2

#### 2.7.3

#### 2.7.4

The computer's serial port signal ground or common pin must be
connected to pin 5 of the DSP-2232'g 9-pin connector.
The pin on which the computer SENDS data must be connected to
pin 3 of the DSP-2232's 9-pin serial connector.
The pin on which the computer RECEIVES data must be connected to
pin 2 of the DSP-2232's 9-pin serial connector.
If your computer requires any other signals, you must arrange to
provide them. The DSP-2232 has the standard hardware handshake lines
available. As a default the DSP-2232 provides XON/XOFF software flow
control to the computer or terminal. The command XFLOW can be turned
OFF disabling software flow control and enabling hardware handshake if
your computer requires it. The documentation provided with your
computer or serial card should clarify any special requirements.
Terminal (Modem) Software for Other Computers
Any communications program that enables your computer to emulate or
act as an ASCII terminal with a telephone modem should work with
your DSP-2232.
If you have a familiar program you have used
successfully, use it to communicate with your DSP-2232.
Terminal Programs for the Apple Ile and I IC
The DSP-2232 operates well with the Apple II family of computers using
both Apple-supplied or third-party serial interface cards. Terminal
programs include Modem Manager, ASCII EXPRESS PRO, Hayes SMARTCOMM 11,
and DataCapture 4.0.
Terminal Programs for the Commodore Vic 20
A BASIC communications program is printed in the VIC 20 Programmer' s
Reference Guide published by Commodore.
Use the program listing for
"True ASCII" • Commodore computers internally use a modified ASCII
format. We suggest you operate your DSP-2232 at 300 bauds with these
computers to avoid possible data speed difficulties.
Terminal Program for the IBM PCir
The PCjr's BASIC cartridge contains a terminal program. Start the
program by typing TERM. Refer to the PCjr's BASIC manual for
details on the program. For best results with the PCjr do not run the
DSP-2232's serial port baud rate faster than 1200 bauds.
Terminal Programs for the Tandv Color Computer
Several terminal programs are available for the coco. We suggest
that you use a commercial program rather than writing your own. The
COCO's " software UART" may be difficult to program in BASIC.

### (PDF p.32)

#### 2.7.5

Terminal Program for the Tandy. 100/102 and NEC 8201
The Model 100, 102 and NEC 8201 have built-in terminal programs in
Consult the
ROM which control the modem and the RS-232C port.
computer documentation for instructions in their use. Make sure that
you do not use the program to control the built-in telephone modem.
