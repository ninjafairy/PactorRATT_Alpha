# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 2 — Computer Installation (PDF p.19–30)

<!-- PDF p.19 -->

### 2.1 Overview

In this chapter we will connect the PK-900 to the RS-232 Serial port
of your Computer or Computer Terminal.  After the Serial connection
has been made we will perform a quick check of the internal software.
Finally we will check the PK-900's modem by performing a Packet
"loop-back" test.  When you have completed this chapter, you will be
ready to connect the PK-900 to your receiver or transceiver and
begin using it on the air.

#### 2.1.1 Equipment Required

You will need the following for this chapter:

- your PK-900 Data controller;

- a 13.6-volt DC, 1.5-amp (or greater) regulated power supply such
as those sold by Radio Shack (or a Timewave AC-5 or AC-4);
(the power supply must be able to supply at least 13 VDC to the
PK-900 while it is operating under load)

- the included PK-900 DC power cord unless the AC-4 is used;

- your Computer or Computer Terminal;

- a Communications or Terminal Emulation program for your computer;
(not needed if a Computer Terminal is being used)

- the included RS-232 cable with a 25-pin "D" connector on each
end;

- one of the included 5-pin shielded "radio cables";
(note that the radio cables may arrive as a single 10-ft. cable
which should be cut in half producing two 5-ft. cables.)

- The two, wire "Loop-back" jumpers necessary for testing;

- wire cutters and strippers or a small pocket knife, a small
straight-blade screwdriver and a medium Phillips-head screwdriver.

### 2.2 Unpacking the PK-900

Carefully remove the PK-900 from the box and its plastic bag.
Inspect the unit for signs of damage that may have occurred in
shipping.  If there is visible damage, please contact the dealer or
shipper.  Do not attempt to install or use a damaged PK-900.

We will be discussing some of the Controls, Indicators and
Connections in this installation so take a few moments to
familiarize yourself with them.  The figures on the next pages may
help with their locations.

<!-- PDF p.20 -->

#### 2.2.1 Connecting Power

MAKE SURE YOUR POWER SUPPLY IS OFF AND UNPLUGGED BEFORE WIRING
1.   If you are using the AEA AC-4 or Timewave AC-5, skip to 5.
Otherwise, locate the PK-900 Power Cable in the accessory bag. Strip off just enough insulation from the ends to connect it to
your 13-14 Volt DC regulated power supply.

2.   The Center pin of the coaxial power plug is POSITIVE.  Connect
the lead with the White stripe to the POSITIVE (+) lead on your
power supply.  Check this with an Ohm-meter if you have one.

3.   Connect the solid Black (GROUND) lead to the NEGATIVE (-) lead of
your power supply.

4.   Connect the power plug to the 13 VDC Power Receptacle on the left
rear of the PK-900.  DO NOT CONNECT YOUR COMPUTER YET.

5.   Plug in your power supply or AC-4 and turn on power.  Turn on
the PK-900 by depressing the Power Switch on the front of the
unit.  At power-on, the LCD Status display on the left should
light. At this point, turn OFF the PK-900 and move on to
section 2.3.

If the Status display does not light, then re-check the above steps to
insure that 12-14 VDC is available at the power plug and the center
pin is POSITIVE.

If the Status display lights, and status indicators other than the
DCD indicators are displayed, then the PK-900 has already been
initialized.  If the PK-900 has been initialized it is ready to
communicate with a computer or terminal at a specific baud rate
(probably 300, 1200, 2400, 4800 or 9600 baud).  If you know what
this baud rate is then you should continue with the installation at
section 2.3 keeping this in mind.

If you do not know the baud rate the PK-900 has been initialized to
then you should reset the PK-900 by holding the rear-panel RESET
switch at the same time you turn on the power switch.  After this is
done, no status indicators on the LCD Status display should be on
with the possible exception of either or both of the two DCD
indicators.

If the above did not produce the blank status indication, (ignoring
the DCD indicators) contact the Timewave Technical Support Department
as described in the front of this manual.

Figure 2-1  PK-900 Front Panel Controls and indicators

<!-- PDF p.21 -->

Figure 2-2  PK-900 Rear Panel Connections and Controls

### 2.3 Connecting Your Computer or Computer Terminal

MAKE SURE THE PK-900 AND YOUR COMPUTER ARE SWITCHED OFF
- Locate the PK-900 Serial Cable.  Connect the 25-pin Female
connector to the RS-232 connector on the PK-900 rear panel.

- Connect the other end of this cable (Female DB-25) to the RS-232
Serial Port of your personal computer or Computer Terminal.
Details on connecting to common machines are listed below.

NOTE:  This cable was designed to connect directly to a 25-pin IBM-PC
compatible RS-232 port.  Many machines on the market today support
this configuration.  Some less-common machines are listed in section
2.6.  Please be certain you have properly connected the PK-900 to
your RS-232 computer or Terminal then proceed to section 2.4.

#### 2.3.1 IBM-PC/XT/AT and Compatibles

IBM compatible 25-pin RS-232 serial ports should connect directly to
the supplied serial cable.  Some IBM compatible machines are equipped
with a 9-pin serial port.  For these machines a DB-9 to DB-25 adapter
should be obtained from a Radio Shack store or a computer dealer.

#### 2.3.2 Apple Macintosh Series of Computers

AEA presently sells the program MacRATT with FAX which contains the
serial cable for the newer models of the Macintosh (models Mac + and
later).  If you intend to use another communications program with the
PK-900 a Modem adapter cable must be purchased from your Apple
dealer to connect the Mac to your PK-900.  For the newer machines
such as the Mac +, Mac SE and Mac II, a mini-8 to DB-25 adapter cable
is required (included with the AEA MACRATT with FAX program).  For the
older Mac 128 and 512 machines, a DB-9 to DB-25 adapter cable from
your Apple dealer is needed.

#### 2.3.3 Commodore 64 and 128 Computers

The Commodore 64/128 computers do not have an RS-232 port as standard
equipment.  For these machines RS-232 adapters are available from
manufacturers such as Commodore or OMNITRONIX.  These may work with
modem or terminal programs for your Commodore.  Section 2.4 of this
chapter deals more with programs for personal computers.  The AEA
Program COM-PAKRATT With FAX is NOT recommended for the PK-900.

<!-- PDF p.22 -->

#### 2.3.4 Computer Terminal

If you have an RS-232 Computer Terminal, sometimes called a Dumb-Terminal, Smart-Terminal or ASCII-Terminal, you may need to change the
gender of the cable provided with your PK-900.  This can be
accomplished with an inexpensive double-male RS-232 gender changing
adapter available from Radio Shack and other computer dealers.  The
Radio Shack part number is 26-243.

### 2.4 Setting Up Your Communications or Terminal Software Program

If you will be using your PK-900 with a Computer, you will need to
read parts of this section to set up your Communications or Terminal
Software.  If you will be using your PK-900 with a Terminal you will
not need any software and may skip to section 2.5.

Setting up a communications program for your PK-900 is very
important.  How your screen looks when you use your PK-900 depends
completely on your Communications program.  AEA currently makes
available programs for the IBM-PC and compatibles and the Apple
Macintosh computers.  These products are customized for radio
communications and are available at extra cost from your AEA
dealer.

The PK-900 operates in much the same manner as a telephone modem and
most modem Terminal Programs will control a PK-900 quite nicely.
Some of these programs are "Public Domain" which means they are FREE.
Other Terminal Programs are "Share-ware" which means you may get them
from a friend and try them before you buy them.  Whether you are using
an AEA program or one of your own choosing, see the section below for
the particular type of computer you plan to use.

#### 2.4.1 Terminal Programs for IBM PCs and Compatibles

Although you can use almost any terminal program with your IBM PC or
close compatible, AEA currently sells the PC-PAKRATT II w/FAX program
which provides many features not available in "telephone modem"
programs.  See your AEA dealer for information on PC-PAKRATT II w/FAX.

If you already have the PC-PAKRATT II program, follow the program
manual and install the software on your computer.  You should also
read through the PACKET OPERATION chapter of the PC-PAKRATT II manual.
Familiarity with Packet operation of PC-PAKRATT will be necessary for
performing a quick-check of the PK-900 in section 2.5 of THIS
manual.

As we mentioned above, an AEA program is not required to use the
PK-900.  Many terminal programs can be found throughout the amateur
radio community or can be downloaded from Compuserve, GEnie and from
many telephone bulletin boards.

A partial list of PC programs tested with the PK-900 includes:

PROCOMM, CROSSTALK-XVI, SMARTCOMM, RELAY, BITCOM, QMODEM, PC-TALK,
CTERM, HAMCOM, HAMPAC, YAPP and the terminal program included with
Microsoft Windows 3.0 (tm).

<!-- PDF p.23 -->

Follow the installation directions that come with the Terminal
program you wish to use.  Once installed on the computer, you
should start the program and set the communication parameters for
the following:

Data Rate = 1200 bits per second (Bauds)
Data bits = 7
Parity    = EVEN
Stop bits = 1

Once these settings have been achieved and the correct serial
communications port chosen, you may proceed to section 2.5.

#### 2.4.2 Terminal Programs for the Apple Macintosh

Although you can use almost any terminal program with your Macintosh,
AEA presently sells the MACRATT with FAX program which provides many
features not available in "telephone modem" programs.  See your AEA
dealer for information on MACRATT with FAX.

If you already have the MACRATT program, please follow the program
manual and install the software on your Mac.  You should also read
through the PACKET OPERATION chapter of the MACRATT manual.
Familiarity with Packet operation of MACRATT will be necessary for
performing a quick check of the PK-900 in section 2.5 of THIS
manual.

As we mentioned above, an AEA program is not required to use the
PK-900.  Many terminal programs can be found throughout the amateur
radio community and can be downloaded from Compuserve, GEnie and from
many telephone bulletin boards.

A partial list of Mac programs tested with the PK-900 includes:

MAC TERMINAL, RED RYDER, MICROPHONE, SMARTCOMM II and MOCK TERMINAL

Follow the installation directions that come with the Terminal program
you wish to use.  Once installed on the computer, you should start the
program and set the communication parameters for the following:

COMPATIBILITY:
1200 bauds, 7 bits/character, even parity, Handshake XON/XOFF,
FULL-DUPLEX, Modem connection, "telephone" port.

Once these settings have been achieved, proceed to section 2.5.

#### 2.4.3 Terminal Programs for the Commodore 64, 64C and 128

Although AEA presently sells the COM-PAKRATT with FAX program package
for the PK-232, this program cannot access all the features of the
PK-900.  AEA therefore cannot recommend using the COM-PAKRATT
package with the PK-900.  If you already have this package, it will
certainly get you started with the PK-900 by using the "Dumb
Terminal" mode.  You may wish to find another program which provides
more features than are available in the COM-PAKRATT program in the
Dumb Terminal Mode.  Other ideas for terminal programs for the
Commodore-64 series of computers are listed below.

<!-- PDF p.24 -->

Many terminal programs can be found throughout the amateur community
or can be downloaded from Compuserve and from many telephone
bulletin  boards.  In addition a BASIC communications program is
listed in the Programmer's Reference Guide published by Commodore.
Use the program listing for "True ASCII".  We suggest you operate
your PK-900 at 300 bauds with these computers to avoid possible
speed difficulties.

Follow the installation directions that come with the Terminal program
you wish to use.  Once installed on the computer, you should start the
program and set the communication parameters for the following:

Data Rate = 300 bits per second (Bauds)
Data bits = 7
Parity    = EVEN
Stop bits = 1

Once these settings have been achieved, proceed to section 2.5.

### 2.5 System Startup and Loop-back Test

Make sure that you have connected your PK-900 to a 12-14 Volt DC
power source and to the RS-232 port of your computer or Terminal.
If you are using a computer, you must also have a communications
program and be familiar with its operation.  You are now ready to
begin the following PK-900 Startup and Loop-back test procedure.

1.   Don't connect any cables to your radio yet!

2.   Remove the 16 gauge wire "Loop-back" connectors from the
PK-900 accessory bag.

3.   Plug these into pins 1 and 4 of each of the radio connectors on
the PK-900's rear panel.

4.   Set both AFSK levels on the back panel of the PK-900 to 50%
rotation (straight up and down) using a small screwdriver.

5.   Turn on your computer.  Load and run your communications program.

If you are using an AEA PAKRATT program, follow the program
instructions to enter the Packet mode, then skip to step 11.

If you are using another Terminal Program or a Computer Terminal,
Set your computer's terminal program to:

- 1200 bauds (if available);
- seven-bit word;
- even parity;
- one stop bit.

NOTE: You may use other terminal baud rates with the PK-900 - we
recommend 1200 baud here to keep this procedure easy and consistent.

6.   Press the PK-900's power switch to the ON position.
At power-on, the LCD Status display on the left should light,

<!-- PDF p.25 -->

showing either no status indications or one or both of the DCD
indicators.  If the Status display lights and other status
indicators are displayed, then the PK-900 has already been
initialized.  If you know the terminal baud rate the PK-900 has
been set to, you may proceed to step 11; otherwise you must
reset the PK-900 as described below.

To reset the PK-900, simply hold the rear-panel RESET switch in
at the same time you turn on the power switch.  After this is
done, then the only LCD Status indicators showing may be the DCD
indicators.  If your serial port is operating at 1200 bauds as we
recommend, you'll see the "autobaud" message:

Please type a star ( * ) for autobaud routine.

If your serial port is operating at 300, 2400, 4800 or 9600
bauds, you may see some "garbage" characters.
This is normal and you should proceed with step 7.

7.   Type an asterisk (*).  When the PK-900 has "recognized" your
computer's data rate, the CMD LCD will light.  Your screen will
then display the sign-on message:

PK-900 is using default values.

AEA PK-900 Data Controller
Copyright (C) 1986-1993 by
Advanced Electronic Applications, Inc.
Release DD.MMM.YY
cmd:

Make note of the Release date on the first page of this manual.
This is important should you ever call AEA for technical support.
It should match the firmware release sticker on the bottom of
your PK-900.

8.   If you are using an AEA program, follow the instructions in the
program manual to enter the packet callsign (MYCALL) of AAA into
the PK-900.  Even though this is not your callsign, please do
this for this procedure.  You must change it to YOUR OWN CALLSIGN
after completing this procedure.

If you are using a Computer Terminal or a non-AEA terminal
program the following will set your packet callsign to AAA:

Enter MYCALL by typing MY AAA <Enter> (or <RETURN>).
(<RETURN> or <Enter> means type the single key on your keyboard.)
Your  monitor should display:

MYcall   was PK900/PK900
MYcall   now AAA/PK900

9.   If you are using an AEA program follow the instructions to
CONNECT in packet mode to AAA.  Since you have just entered your
callsign as AAA, you will connect to yourself.

If you are using a Computer Terminal or a non-AEA program,

<!-- PDF p.26 -->

entering the following after the "cmd:" command mode prompt
will cause the PK-900 to Connect to AAA:

C AAA <Enter>

After a few moments, your monitor should display:

*** CONNECTED to AAA

10.  Type HELLO SELF <Enter>
After a few moments, your monitor should echo the same message.

If you have gotten this far then the digital section of the
PK-900 and the VHF packet modem of port 1 are both working.

11.  We will now check the PK-900's HF modem.  If you are using an
AEA program, follow the instructions to select the HF modem by
turning the VHF Parameter OFF; this will automatically set the
radio baud rate HBAUD to 300 for HF packet work.

If you are using a Computer Terminal or a non-AEA terminal
program, the following sets the HF mode of the PK-900:

Type <CONTROL-C>.  (Type C while pressing the <Ctrl> key down.)
Your monitor should respond with the command prompt:

cmd:

Then enter VHF OFF <Enter>
Your monitor should respond with:

Vhf    was ON/ON
Vhf    now OFF/ON

Then enter HB 300  <Enter>
Your monitor should respond with:

HB    was 1200/1200
HB    now 300/1200

12.  If you are using an AEA program type HELLO SELF <Enter>
Your monitor should soon echo the message you've just typed.

If you are using a Computer Terminal or a non-AEA terminal
program, you must first type CONV or K followed by a <Enter>.
Now you may type a few characters.  Your monitor should soon echo
the characters you've just typed.

13.  If you are using an AEA program, follow the instructions to
DISCONNECT from a Packet station.

If you are using a Computer Terminal or a non-AEA terminal
program the following will cause the PK-900 to DISCONNECT:

<!-- PDF p.27 -->

Enter <CONTROL-C>
Your monitor should respond with the command prompt:

cmd:

Enter D <Enter>
Your monitor should respond with:

cmd:*** DISCONNECTED: AAA
p1 AAA*>AAA (UA)

If all of the above steps were successful, you've completed the quickcheck and are ready to proceed to Chapter 3.  In Chapter 3 you will
connect your PK-900 to your radio and begin using it "on the air".

If you have problems with the steps shown above, go back to Step 1
AFTER checking all cables and connectors.  Read each step again
carefully.  The most common problems are trying to connect to a call
different from AAA, not having the "loopback" jumpers in the
correct pins, or not setting the AFSK levels to 50% rotation.

If you still have problems, leave your PK-900 ON and contact AEA's
Technical Support Department as suggested in the front of this manual.

### 2.6 Detailed RS-232 Connections for Other Computers

If the type of computer you plan to use with the PK-900 was not
mentioned in the beginning of this chapter, you may find specific
connection information in the sections below.  You will also need a
Communications program to use with your computer which AEA can not
provide.  See section 2.7 for information regarding Communication
programs for many of these machines.

Some computers require a serial port adapter card that incorporates
the necessary RS-232-C interface circuitry.  The IBM-PC and Apple II
series of computers are good examples of this.

Computers that do not have a serial port or do not permit use of a
suitable adapter or level converter cannot be used with the PK-900.

#### 2.6.1 Apple II Series

The Apple II, II+ and IIe computers require an RS-232 Serial card to
connect to your PK-900.  The most popular we know about is the
Super-Serial Card which should be available from your Apple dealer.

#### 2.6.2 Commodore C-64, C-128 and Vic 20

Commodore, OMNITRONIX and other manufacturers sell a signal level
converter that is installed in the User Port Connector on the rear of
the computer.  The converter changes the computer's internal TTL
voltage levels to the proper RS-232-C voltage levels and polarities.

#### 2.6.3 IBM PCjr

The PCjr uses standard RS-232-C voltage levels; however, theconnector is not standard and is hard to find.  Pin-out information
can be found in the IBM PCjr Technical Reference Manual.  Some
dealers sell a 'IBM PCjr Adapter Cable for Serial Devices' that
converts the connector on the PCjr to standard RS-232-C.  The
cable attaches between the PCjr and the PK-900 Serial Cable.

#### 2.6.4 Tandy Color Computer

The CoCo series (except for the Micro CoCo) uses a four-pin DIN
connector for its serial interface.  Wire a cable as shown below.  All
necessary parts should be available from your Radio Shack dealer.

```text
CoCo                PK-900 (DB25P)
4 ..................... 2
2 ..................... 3
3 ..................... 7
```

#### 2.6.5 Tandy Model 100/102 and NEC 8201

The Model 100/102 and NEC 8201 have built-in standard RS-232 serial
ports which are compatible with the PK-900.  You'll need a DB-25
male-male gender changing adapter to use the supplied PK-900 Serial
Cable.

#### 2.6.6 Other Computers with RS-232-C Ports

If your computer has an RS-232 port, consult your computer manuals to
see which pins are used for Transmit-Data, Received-Data and Signal-Ground.  Read the manufacturer's recommendations for connecting the
serial port to a modem and connect your PK-900 in the same way.

Your PK-900 is configured as Data Communications Equipment (DCE)
which receives data on pin 2 of the 25-pin DB-25 connector or pin 2
of the 25 pin cable supplied with the unit.  Most computers and
terminals are configured as Data Terminal Equipment (DTE)
transmitting data on pin 3 of a DB-9 or pin 2 of a DB-25 RS-232
connector.

- If your computer is configured as DTE:

Use the supplied RS-232 cable with a Gender changing adapter if
necessary.  These are available from Radio Shack (Part # 26-243)
and other computer stores.

- If your computer is configured as DCE:

You may want to purchase a Null Modem adapter from Radio Shack
(Part # 26-1496) or other computer store.

You may also wire your own cable directly to the PK-900's DB-25
connector by wiring the Transmit Data (TXD), Receive Data
(RXD), and the Signal Ground (GND) to a DB-25P (Male)
connector as diagramed below:

<!-- PDF p.29 -->

```text
Computer                   PK-900 (DB25)
TXD .......................... 2
RXD .......................... 3
GND .......................... 7
```

- As a default the PK-900 provides XON/XOFF software flow-control
to the computer or terminal.  The command XFLOW can be
turned OFF to enable hardware handshake if your computer
requires it.
Hardware flow control is achieved with RTS/CTS (pins 4 and 5)
of the 25-pin connector on the PK-900's rear panel.

#### 2.6.7 Other Computers with Non-Standard Serial Ports

Computers with non-standard serial ports must meet the following
conditions:

- The signal levels must be compatible with RS-232-C.  The PK-900
requires the voltage levels from the computer be greater than +3
volts in the "asserted" state and 0 volts or less in the "nonasserted" state.

- The signal polarity must conform to the RS-232-C standard.  The
0 or negative-voltage state must correspond to logical "1" and
the positive-voltage state to logical "0."

- The computer must be able to correctly receive a signal that
meets asynchronous RS-232-C specifications.  The PK-900
supplies signals that meet this specification.

Make or buy a cable that provides the following connections:

- The computer's serial port signal ground or common pin must be
connected to pin 7 of the PK-900's 25-pin connector.

- The pin on which the computer SENDS data must be connected to
pin 2 of the PK-900's 25-pin serial connector.

- The pin on which the computer RECEIVES data must be connected to
pin 3 of the PK-900's 25-pin serial connector.

If your computer requires any other signals, you must arrange to
provide them.  The PK-900 has the standard hardware handshake lines
available.  As a default the PK-900 provides XON/XOFF software flow
control to the computer or terminal.  The command XFLOW can be turned
OFF disabling software flow control and enabling hardware handshake if
your computer requires it.  The documentation provided with your
computer or serial card should clarify any special requirements.

### 2.7 Terminal (Modem) Software for Other Computers

Any communications program that enables your computer to emulate or
act as an ASCII terminal with a telephone modem should work with
your PK-900.  If you have a familiar program you have used
successfully, use it to communicate with your PK-900.

<!-- PDF p.30 -->

#### 2.7.1 Terminal Programs for the Apple II, II+, IIe and IIC

The PK-900 operates well with the Apple II family of computers using
both Apple-supplied or third-party serial interface cards.  Terminal
programs include Modem Manager, ASCII EXPRESS PRO, Hayes SMARTCOMM II,
and DataCapture 4.0.

#### 2.7.2 Terminal Programs for the Commodore Vic 20

A BASIC communications program is printed in the VIC 20 Programmer's
Reference Guide published by Commodore.  Use the program listing for
"True ASCII"; Commodore computers internally use a modified ASCII
format.  We suggest you operate your PK-900 at 300 bauds with these
computers to avoid possible data speed difficulties.

#### 2.7.3 Terminal Program for the IBM PCjr

The PCjr's BASIC cartridge contains a terminal program.  Start the
program by typing TERM.  Refer to the PCjr's BASIC manual for
details on the program.  For best results with the PCjr do not run the
PK-900's serial port baud rate faster than 1200 bauds.

#### 2.7.4 Terminal Programs for the Tandy Color Computer

Several terminal programs are available for the CoCo.  We suggest
that you use a commercial program rather than writing your own.  The
CoCo's "software UART" may be difficult to program in BASIC.

#### 2.7.5 Terminal Program for the Tandy 100/102 and NED 8201

The Model 100, 102 and NEC 8201 have built-in terminal programs in
ROM which control the modem and the RS-232C port.  Consult the
computer documentation for instructions in their use.  Make sure that
you do not use the program to control the built-in telephone modem.
