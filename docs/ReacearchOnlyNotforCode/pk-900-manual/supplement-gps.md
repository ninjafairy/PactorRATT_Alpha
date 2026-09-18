# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## TNC GPS Upgrade Addendum (PDF p.353–365)

<!-- PDF p.353 -->

GPS Operation
Overview
This chapter describes how to configure and connect your TNC for GPS operation.
Why do GPS in the TNC?
All Timewave TNCs can run with Automatic Packet Reporting System (APRS™) software.
The TNC, however, has special firmware which makes Stand Alone Tracking and Ultimeter II®
compatibility possible. Timewave also offers an APRS Adapter Cable for the TNC which makes
Hardware Single Port Mode (HSP) operation possible. The reason we chose to put special firmware in the TNC and develop the APRS Adapter Cable for the TNC is because this TNC is perfect for going mobile; which is what Global Positioning Systems are all about.
The TNC contains special GPS features not found in other packet Terminal Node Controllers.
The TNC can detect whether or not a GPS receiver is connected with the GPSAuto command,
it then chooses the appropriate GPSMode of operation. If a GPS receiver is detected upon
power-up, the TNC automatically sends a GPS initialization string. The TNC can be remotely
polled by addressing a packet to its GPOLLCall, or by the polling features in APRS. The
TNC's GPS commands can be remotely programmed using the GREMprog command which is
very convenient in Stand Alone Tracking applications-the unit never needs to be taken out of
the vehicle to change GPS settings. GPS receivers can be remotely programmed via the TNC.
The  TNC's internal time and date can be set from the information obtained from the GPS receiver. There are provisions for an external input to cause the sending of NMEA strings, such as
from a radio control head button. The TNC can even operate packet and GPS at the same time
using the commercial mode. See You're in Command  chaper, G section for GPS commands.
What is APRS?
Automatic Packet Reporting System
(APRS™) is a copyrighted software
program developed by Bob Bruninga
(WB4APR), which is distributed as
shareware for real-time Packet communications. You can obtain a copy
of APRS from http://
web.usna.navy.mil/~bruninga/
aprs.html. For higher level APRS applications you may need a registered
version of the software. The software keeps lists of stations heard and
displays the locations of stations visually on a computerized map like the one in Figure 1. The
maps can be scaled from .5 to 2000 miles. Few of the maps are  complete, however there are
tracking features built into APRS which allow you to create maps just by driving down the
streets of your town.  Stations can appear on the maps and each station may be given a symbol
such as a house, car, or boat. The real benefit of using APRS software is in mobile tracking applications. Vehicles and people can be tracked and mapped using APRS. APRS also includes the
ability to send short messages and bulletins. Using Ultimeter II weather information can also be
shown on the computerized maps.
FIGURE 1: APRS computerized map
Mar. 2003 REV F

<!-- PDF p.354 -->

Hardware Configurations
There are four different hardware configurations you can use when operating within an APRS
system, each for a different purpose:
1. A computer running APRS, a GPS receiver, a TNC, an APRS Adapter Cable (optional), and a radio:
This complete configuration allows you to track all users on the system in your area (including
yourself), and send/receive messages and bulletins. Using Timewave's APRS Adapter Cable, you
can connect a GPS receiver and a TNC to a single COM port on a computer running APRS, see
Figure 2. Without the APRS Adapter Cable, the TNC and GPS receiver would need to be connected to separate COM ports. Having two COM ports free on most computers is a luxury most us
don't have with all the Windows related equipment such as mice and other serial peripherals. Mobile laptop computer users need the APRS Adapter Cable because most only have one COM port.
/G52/G75/G6E/G6E/G69/G6E/G67
/G41/G50/G52/G53
2. A computer running APRS software, any Timewave TNC, and a radio (No GPS receiver):
This configuration lets you track all users on the system with your location being entered manually. This also lets you send and receive messages within APRS. All Timewave TNCs can be part
of a system such as this. This configuration allows anyone with any Timewave TNC who is running APRS software in their computer to take part in this GPS technology. You don't need a GPS
receiver to pull down location information from the satellites because you enter your position
manually on the APRS computerized map, which in most cases is fine because you are at your
house which doesn't (shouldn't) move. The only time you really need a GPS receiver is when you
are operating packet while mobile because manually updating your position while mobile isn't
practical-you need the GPS receiver to pull in those satellite location signals and update your position as you move.
3. A TNC, a GPS receiver, and a radio (No PC):
This trio allows you to transmit your location automatically to others in an APRS system without
having a computer in the vehicle. This is known as a Stand Alone Tracking Device because others
can track your motion without you having to run APRS or even have a computer. The firmware in
the TNC allows the TNC to parse the incoming location information from the GPS receiver (with-TNCTNCTNCTNCTNC
Running
APRS
FIGURE 2: APRS HSP mode where APRS controls the cable switching between the TNC and GPS receiverout the use of a computer running APRS which usually does the parsing) and then beacon the
information over the ham bands.
Because the TNC can work both as a stand alone tracking device and as a connected APRS
component, this TNC can be of aid in natural disaster situations, during public service events,
and search and rescue missions, among other things.
For example, in a marathon, a pace car, ambulances, and other support vehicles could each
have a TNC acting as a Stand Alone Tracking Device. A  coordinator can be in a centralized location using a TNC, radio, and an APRS Adapter Cable (optional) to connect with a computer
running APRS software to visually track the vehicles. When an emergency arises, such as an
injured runner, the coordinator can see which ambulance is closest to the scene and dispatch it
appropriately through voice communications.
Further, if all vehicles in the previous example were equipped with a PC running APRS, a TNC,
a GPS receiver, a radio, and an APRS Adapter Cable, the coordinator could not only see where
the vehicles were - he could dispatch them directly through the APRS system. The drivers of
the vehicles would receive short messages from the coordinator such as, "Runner down, 4th
and Main. Vehicle 2 respond." These short messages appear on the individual driver's computer
screen much like typical Packet messages. The messages can also be accompanied by computer 'beeps' to alert the driver to an incoming message. APRS includes a bulletin feature where
all vehicles could receive messages simultaneously such as, "Take lunch."
4. TNC, ULTIMETER II, and a radio (No PC):
This configuration requires an ULTIMETER II home weather station, see Figure 3 below. (Information on ULTIMETER II weather station packages can be obtained by calling 1-800-USA-PEET.) In case you are not familiar with ULTIMETER II, it is a weather station package including
a control panel into which a thermometer, wind gauge, and an optional rain gauge are connected.
If you like, you can monitor the temperature of any room in your home, keep track of temperatures in a greenhouse or outbuilding, or set alarm limits on a freezer or other appliance.
/G50/G4B/G2D/G31/G32
/G58/G4D/G54/G44/G43/G44/G43/G4F/G4E/G53/G54/G41/G4D/G41/G49/G4C /G50/G4F/G57/G45/G52
/G52/G61/G64/G69/G6F
/G55/G6C/G74/G69/G6D/G65/G74/G65/G72/G49/G49/G4B/G65/G79/G62/G6F/G61/G72/G64/G44/G69/G73/G70/G6C/G61/G79
/G50/G4B/G2D/G31/G32
/G55/G6C/G74/G69/G6D/G65/G74/G65/G72/G49/G49/G4A/G75/G6E/G63/G74/G69/G6F/G6E/G42/G6F/G78
/G41/G43/G41/G64/G61/G70/G74/G65/G72
/G54/G65/G6D/G70/G65/G72/G61/G74/G75/G72/G65
/G53/G65/G6E/G73/G6F/G72
/G57/G69/G6E/G64/G53/G65/G6E/G73/G6F/G72 /G52/G61/G69/G6E/G47/G61/G75/G67/G65
/G50/G4B/G2D/G31/G32/G2F/G55/G6C/G74/G69/G6D/G65/G74/G65/G72/G49/G49/G53/G74/G61/G6E/G64/G41/G6C/G6F/G6E/G65/G57/G65/G61/G74/G68/G65/G72/G53/G74/G61/G74/G69/G6F/G6E
FIGURE 3: Illustrates components of a stand alone packet weather station (PK-12 can be substituted with any Timewave
TNC with GPS firmware)

<!-- PDF p.356 -->

The TNC can be connected to the ULTIMETER II weather station, interpret the weather data,
and transmit the weather and location information in packet so other APRS users can see your
location and the weather at your location. The remote polling features of the TNC make it easy
to set up a weather station in a remote location and receive the remote weather data via APRS at
your house, office, or wherever. While at work, you can keep track of the weather situation at
your home (inside or outside temperatures-whichever you prefer). Farmers can keep apprised
of rain, wind, and temperature at a remote field site. In addition, several weather stations set up
in your region can provide you with a comprehensive weather picture of your area-available to
you and all other users at anytime. The TNC, ULTIMETER II, and APRS add a whole new dimension to packet radio-we hope you experiment with this new application. Set-up procedures for an Ultimeter II weather station are discussed later in this chapter.
Setting Up the TNC for GPS  Operation
Set up procedures for the TNC are described below for three Packet/GPS configurations: Stand
Alone Tracking, APRS Hardware Single Port mode, and APRS Dual Port Mode. APRS software
automatically sets the parameters for your TNC, except when the TNC is used as a Stand Alone
Tracking Device (because APRS isn't connected).
Stand Alone Tracking Device Set-up:
To set up the TNC for Stand Alone Tracking, follow these steps:
1. Connect your TNC to your computer and get it up and running following the directions in
Chapter 2.
2. Type MY <your call sign>-9, then press ENTER. For example, if your call sign is WA7GCI
you would enter MY WA7GCI-9, then press ENTER. The -9 will cause others to see you as
a car on the APRS map.
3. Program the UNPROTO command to: UN APRS via RELAY , then press ENTER. This
programs your unproto path to APRS via RELAY.
4. Type GLOCtx 6, then press ENTER. This causes the TNC to transmit its GPS location every 60 seconds. The seconds can range from 0 (disabled) to 255 in ten second increments.
5. Now you need to program the commands, GNMEA1 and/or GNMEA2, for the desired
NMEA (GPS) sentences for the TNC to parse. Not all GPS receivers can send all NMEA
sentences. So, the instructions you see below depend on the ability of your GPS receiver to
send desired NMEA strings. In any case, the TNC can handle any NMEA strings your GPS
or Loran receiver can produce.
To program GNMEA1, type GNMEA1 $GPGLL and press ENTER. This command allows
the TNC to parse the Position-Only NMEA string coming from the GPS receiver and place
the string in the transmit buffer. The result of $GPGLL is that you will be able receive from
the GPS receiver Position-Only information and transmit it over the ham bands. (Any
NMEA string your GPS receiver can produce can be entered in this command line.)
The neat thing about the TNC is that you can choose not one, but two NMEA strings to
parse. You can, for instance, use Position-Only and Course/Speed. To program the second
NMEA string for the TNC, type GNMEA2 $GPVTG  and press ENTER. This sentence
alone allows you to transmit course and speed. When coupled with the GNMEA1, your position, course, and speed will be transmitted together. (Again, you can use any NMEA string
your GPS receiver can produce in this command line.)

<!-- PDF p.357 -->

Note: The NMEA sentence $GPRMC is the recommended sentence for tracking moving
objects as it contains time, latitude, longitude, course, and speed information.
6. Now type GPSAuto ON, then press ENTER. This will cause the TNC to look for GPS
data (coming from an attached GPS receiver) the next time it is powered on, and if it sees
GPS data the TNC will automatically switch to GPSMode 1.
7. Now you need to program the TNC's terminal baud rate to match that of the GPS receiver, which is normally 4800 baud. To program the TNC terminal baud rate, type
TBAUD 4800 , then press ENTER.
8. Now turn your TNC off and disconnect your TNC from the computer. Take the TNC to
your vehicle, connect the TNC to the GPS receiver and the radio, plug it in, power it up
and you're in business. Note: Before powering up the TNC, be sure the GPS receiver is
powered up and outputting its NMEA sentences.
Connecting the GPS receiver to the TNC
Figure 4 below illustrates the pin assignments for connecting a GPS receiver to the TNC. The
same pin assignments can be used to connect to a Peet Bros. ULTIMETER II® weather station.
/G50/G4F/G57/G45/G52
/G2B/G31/G33/G56/G44/G43 /G43/G4F/G4D/G50/G55/G54/G45/G52
/G28/G52/G53/G2D/G32/G33/G32/G29 /G54/G58
/G4C/G45/G56/G45/G4C
/G52/G41/G44/G49/G4F
/G32
/G47/G4E/G44
/G35
/G53/G51
/G34
/G52/G58
/G31
/G54/G58
/G33
/G50/G54/G54
/G52/G65/G61/G72/G56/G69/G65/G77/G6F/G66/G74/G68/G65/G50/G4B/G2D/G31/G32
/G43/G6F/G6E/G6E/G65/G63/G74/G47/G72/G6F/G75/G6E/G64/G74/G6F/G70/G69/G6E/G31
/G43/G6F/G6E/G6E/G65/G63/G74/G74/G68/G65/G47/G50/G53/G72/G65/G63/G65/G69/G76/G65/G72/G6F/G72/G55/G4C/G54/G49/G4D/G45/G54/G45/G52/G49/G49/G4E/G4D/G45/G41/G6F/G75/G74/G70/G75/G74/G74/G6F/G70/G69/G6E/G32
/G4D/G6F/G6D/G65/G6E/G74/G61/G72/G79/G63/G6C/G6F/G73/G75/G72/G65/G74/G6F/G67/G72/G6F/G75/G6E/G64/G63/G61/G75/G73/G65/G73/G54/G4E/G43/G74/G6F/G69/G6D/G6D/G65/G64/G69/G61/G74/G65/G6C/G79/G70/G61/G72/G73/G65/G61/G6E/G64/G74/G72/G61/G6E/G73/G6D/G69/G74/G69/G74/G73/G6C/G6F/G63/G61/G74/G69/G6F/G6E/G28/G70/G69/G6E/G34/G29
/G54/G68/G69/G73/G6F/G70/G74/G69/G6F/G6E/G28/G6E/G6F/G74/G65/G73/G73/G65/G6E/G74/G69/G61/G6C/G66/G6F/G72/G6F/G70/G65/G72/G61/G74/G69/G6F/G6E/G29/G67/G69/G76/G65/G73/G79/G6F/G75/G74/G68/G65/G61/G62/G69/G6C/G69/G74/G79/G74/G6F/G63/G6F/G6E/G66/G69/G67/G75/G72/G65/G61/G72/G61/G64/G69/G6F/G68/G65/G61/G64/G62/G75/G74/G74/G6F/G6E
/G74/G6F/G74/G72/G61/G6E/G73/G6D/G69/G74/G70/G6F/G73/G69/G74/G69/G6F/G6E/G69/G6E/G66/G6F/G72/G6D/G61/G74/G69/G6F/G6E/G61/G74/G61/G6E/G79/G74/G69/G6D/G65/G2E
9. When done using the TNC as a Stand Alone Tracking Device, connect your TNC to the
computer again, verify that your terminal baud rate is at 4800 baud, 8 bits, no parity, one
stop bit. A few seconds after powering up, the TNC should display its sign-on message and
command prompt.
APRS Hardware Single Port Mode (HSP) Set-Up: Requires a registered version of APRS
APRS version 4.03 and higher includes a routine that controls the switching of the Single Port
Mode between the TNC and any GPS receiver using the handshaking lines of the COM port. HSP
allows you to operate APRS on a computer with limited COM ports, such as a laptop which usually has only one COM port, or your PC which uses one of the two COM ports dedicated to a
mouse. This is the only configuration which uses the APRS adapter cable. You will need to have
a registered copy of APRS in able to activate the GPS input. Registration information is contained
in the APRS readme files.
FIGURE 4: Pin assignments for connecting the TNC to a GPS receiver or to an Ultimeter II weather stations. (Same for all Timewa ve
TNCs)

<!-- PDF p.358 -->

Follow these steps to connect the APRS Adapter Cable to your equipment:
1. Look at the APRS adapter cable. It looks like this:
Notice the words on the outside of the rectangle; they say TNC, Computer, and GPS.
This is how the Cable should be connected.
2. Connect the APRS Adapter Cable to the serial port on the TNC. The male side of the
Adapter Cable, labeled TNC, should be connected to the female serial port on the TNC.
3. Connect an RS-232 (not included) to a free COM port on your computer. Connect the other
end of this cable to the RS-232 port marked "Computer" on the APRS Adapter Cable. (The
female side of the Adapter Cable, labeled 'computer', should be connected to a cable going
to the computer.)
4. The cable that comes out the side of the APRS Adapter, labeled "GPS", connects to the
NMEA-0183 port on your GPS receiver. You may need to buy an adaptor or connector to
match the connector on your GPS receiver.
The cable is now correctly connected to your equipment.
To set up the TNC for HSP operation, follow these steps:
1. Connect your TNC to your computer, and to your GPS receiver using the Timewave
APRS Adapter Cable following the steps noted previously. Get the computer running
APRS software and the TNC running. Leave the GPS receiver off until you are ready to
receive the NMEA string.
2. Press  ALT-S to bring up the setup menu.
3. Now press G for GPS.
4. Press H for HSP.
5. Now press ALT-S to save.
6. Enter S for SAVE.
7. Now follow the prompt from APRS to validate your APRS registration and save the mode
changes.
8. Turn your GPS receiver on, and you are ready for APRS HSP operation.
9. When finished using the TNC for APRS HSP operation, simply quit APRS. APRS may ask
you for some information before quitting. Answer the questions and when done, your TNC
will be ready for non-GPS activities.
APRS Dual Port Mode Set-up:
To set up APRS for dual mode operation, follow these steps:
1. Connect your TNC to one of the COM ports on your computer. Get the TNC up and running
following the directions in Chapter 2 of this manual.
2. Connect your GPS receiver to the other COM port on your computer. (You will need to
build your own cable for this connection. Since there are many types of GPS receivers, allwith different connections, we can't provide you with schematics on how to build this
cable.)
3. Get the APRS software program running. APRS will ask you some questions, simply answer the questions and your TNC and APRS will be ready for dual port mode operation.
Building your own APRS adapter cable
The APRS adapter cable works as follows:
The adapter allows for Hardware Single Port mode (HSP) operation, so you only need one
computer COM port for both your GPS receiver and TNC. The adapter normally has the TNC
switched to the computer until APRS decides it's time to send its location report. When it's time
to send position information, the computer running APRS asserts flow control to the TNC to prevent it from sending any more data. The flow control line is also used to switch the adapter to the
GPS receiver. Once the GPS string is received, the computer running APRS releases flow control,
switching back to the TNC.
A pre-made and tested cable can be purchased from Timewave for $30.00, plus shipping and
handling. Visit the Timewave website, http://www.timewave.com.
An APRS adapter cable can be made from the following components and schematic.
ItemItemItemItemItem DescriptionDescriptionDescriptionDescriptionDescription QtyQtyQtyQtyQty Timewave Part #Timewave Part #Timewave Part #Timewave Part #Timewave Part # Radio Shack Part #Radio Shack Part #Radio Shack Part #Radio Shack Part #Radio Shack Part #
1. PCB 1 013-141
2. 1K resistor 1 210-102 271-023
3. 10K resistor 2 210-103 271-034
4. IN4148 diode 2 310-001 276-1122
5. 2N3904 NPN transistor 2 320-001 276-2016
6. DB-25 male connector 1 420-102
7. DB-25 female connector 1 420-105
8. DB-25 hood 1 053-065
9. Screws for hood 1 053-065-1
10. Saddle washers 1 053-065-2
11. Hex nuts 1 053-065-3
12. 6ft shielded cable 1 769-001

<!-- PDF p.360 -->

Connecting the APRS adapter cable
Once you've made your adapter cable, connect as described previously.
Schematic Diagram for the APRS Adapter Cable
TNC/ULTIMETER II® Stand Alone Weather Station Set Up
Connect the ULTIMETER II to the TNC following the illustrations of Figure 3, Figure 4, and
Figure 5. Once the connections are made, follow the directions below to configure the TNC.
1. Connect your TNC to your computer and get it running following the directions in Chapter
2.
2. Type MY <your call sign> , then press ENTER. For example, if your callsign is WA7GCI,
you would enter MY WA7GCI, then press ENTER.
3. Type UN APRS via RELAY , then press ENTER. This programs your UNPROTO path to
APRS via RELAY.
4. Type GLOCtx 60, then press ENTER. This causes the TNC to transmit the weather information every 10 minutes. The seconds  can range from 0 (disabled) to 255 in ten second increments.
5. Now you need to program your Btext with the day of the month, time, and your latitude and
longitude. For example: if your exact location is 47°49.14'N, 122°15.72'W and the time and
date is 9:11 AM  June 23 1995, you would enter BT @230911/4749.14N/
12215.72W_Lynnwood weather station, then press ENTER.

<!-- PDF p.361 -->

6. Now type Beacon E 60 , then press  ENTER. This will cause your TNC to transmit its
beacon text every five minutes. APRS must see the beacon text containing the position information and a weather symbol character before it will display your weather information.
7. Now you need to program the GNMEA1 sentence to match the output of the
ULTIMETER II. Type GNMEA1 #, then press ENTER. This will cause the TNC to begin
parsing the ULTIMETER II data starting with the # digit. ( Note some ULTIMETER IIs
start their string with a # digit and some start their strings with a * digit. This value may
have to be changed to a * if it does not work upon installation.)
8. Type TBAUD 2400 , then press ENTER. This sets the TNC serial baud rate to match the
output of the ULTIMETER II.
9. Now type RESTART, then press ENTER. This will cause the terminal baud rate to
change to 2400 baud matching the ULTIMETER II. Change your terminal baud rate to
2400 baud to match that of the TNC.
10. Now type GPSMode 2 , then press ENTER. Now press ENTER again to enter the standalone weather  station mode. Now disconnect the serial cable and connect the ULTIMETER
II, and you are ready to transmit weather information in an APRS system.
TNC Commands
GENDchar "n" Default: $0D (CTRL-M)
The default  GENDchar of $0D requires the string to end with a CR,LF to conform with the
NMEA format. If this is changed, the sentence will end with whatever character is programmed.
This command does not need to be programmed for use with GPS or Loran devices. It is intended
for use with other formats.
GINIText  text Default: None
text-  Any combination of characters up to a maximum length of 60 characters.
This text will be sent to the serial port when the TNC is powered up with GPSMode NOT equal to
zero. This text is intended for an initialization string for OEM GPS cards requiring setup after
power-down. If not programmed, the TNC will send nothing at power-up.
FIGURE 5: Ultimeter II junction box pin assignments (Same for all Timewave TNCs)

<!-- PDF p.362 -->

GLOCtx  "n" Default: 0 (00 sec.)
"n" - 0 to 255 sets the location timing in ten-second increments.
"0" - Zero disables automatic location transmissions.
GLOCtx is used to periodically transmit GPS data received on the serial port. GLOCtx is programmable from 0 (never) to 2500 seconds in 10 second intervals. A transmission will only occur
if, GPSMode = 1 or 2, GLOCtx is not zero, and the correct NMEA information is received on the
serial port.
GNMEA1 Text (0-8 chars)
Default: GPGLL (GPS position only)
This is the string the TNC will parse from the NMEA device. The text can be any ASCII character.
GNMEA2  Text (0-8 chars) Default: None
This is another string the TNC will parse from the NMEA device. The text can be any ASCII
character.
GPOLLCal  call[-"n"] Default: None
call  -  The callsign you wish to use for polling.
"n"  -  Numeral indicating an optional SSID.
The POLLCal can be any callsign other than one already programmed in the TNC. If programmed
the same as the MYAlias, MYCall, MYGate, or MYMail callsigns, this polling feature will not
work. When a packet is received addressed to the GPOLLCall callsign, the TNC will begin parsing the programmed NMEA command or commands. When the NMEA headers are successfully
received, the TNC will transmit the GPS data as a UI-Frame. This GPOLLCall can also be used
for digipeating.
GPSAuto ON|OFF Default: OFF
ON - When powered up or restarted, the TNC switches to GPSMode 1 if it parses its GNMEA1 or
2 sentence within the first 4 seconds after power up.
OFF - The TNC powers up normally.
If you are using your TNC in your vehicle as a stand alone tracking device and in your home for
regular packet operation, this command will save you a lot of  hassles. Simply program all your
GPS parameters except for the GPSMode command, turn GPSAuto ON and you're set. Each time
you turn your TNC on, the TNC will spend the first four seconds after power up trying to parse its
GNMEA sentence or sentences. If it successfully parses one or both of them, it will automatically
switch to GPSMode 1.
To have the TNC auto detect a GPS receiver, follow these steps:
1. Be sure you have your GPS command programmed.
2. Be sure GPSAuto is turned ON.
3. Connect the GPS receiver to the TNC serial connector.

<!-- PDF p.363 -->

4. Apply power to the GPS receiver (NOT THE TNC YET).
5. Be sure that the GPS receiver is outputting its NMEA data before powering up the
TNC.
6. Power up the TNC and within 4 seconds, you should notice the TNC transmit LED
lighting to transmit its location.
GPSMode "n" Default: 0 Disabled
This is a variable ranging from 0-3 that selects a specific mode of operation specific to GPS.
Three CTRL-C's returns unit to GPSMode 0.
Mode 0 = Normal Packet TNC operation. (GPS operation disabled.)
Mode 1 = TNC connected directly to a GPS receiver.
The TNC will begin parsing its programmed NMEA sentences under the following
conditions:
1. GLOCtx timer expires
2. The TNC receives an Unproto frame containing ?APRS?
3. The TNC receives an Unproto frame containing MYCALL..:?APRS?
4. The TNC receives an Unproto frame addressed to its POLLCALL
Upon reception of one of these types of frames, the TNC will begin to parse its programmed
NMEA sentences. If the TNC successfully parses one or both of its NMEA sentences, it will
transmit the information parsed as a single UI frame. If there is no data received from the GPS receiver within four seconds, it will reset its GLOCtx timer and continue.
Mode 2 = TNC connected directly to an ULTIMETER II.
The TNC will begin parsing its programmed NMEA sentences under the following
conditions:
1. GLOCtx timer expires
2. The TNC receives an Unproto frame containing ?WX?
3. The TNC receives an Unproto frame containing MYCALL..:?APRS?
4. The TNC receives an Unproto frame addressed to its POLLCALL
Upon reception of one of these types of frames, the TNC will begin to parse its programmed
NMEA sentences. If the TNC successfully parses one or both of its NMEA sentences, it will
transmit the information parsed as a single UI frame. If there is no data received from the GPS receiver within four seconds, it will reset its GLOCtx timer and continue.
GREMprog ON|OFF Default: OFF
ON  -  Remote programming is enabled in GPS mode.
OFF - Remote programming is disabled in GPS mode.
While operating in one of the GPS modes, the following commands can be programmed:
1. GLOCtx
2. GNMEA1
3. GNMEA2
4. GINIText

<!-- PDF p.364 -->

5. GSYMchar
6. Unproto
To remotely program the TNC, the programming station must have its Unproto destination callsign set to match the GPOLLcall of the TNC.
Example:
Let's say you have a TNC in your car as a stand alone tracker with its GPOLLcall set to CAR. In
your house, you would program your TNC's Unproto to CAR ( U CAR). Now place your TNC in
converse mode by typing either ( K or CONVerse). If you wanted to change the TNC in the
car's Unproto path, you would simply type U APRS via RELAY <CR>. Your TNC in your home
would transmit the following frame:
Home TNC:
{your callsign}*>CAR [UI]:
U APRS via RELAY
TNC in car would reply:
PK12*>RELAY>APRS [UI]:
++ done
Note that the TNC in the vehicle replied with the message ++ done. This indicates that the command was properly received and the parameter was changed. The following is a list of additional
messages and their meaning.
Remote programming messages:
Message Meaning
++ done the parameter was programmed
++ bad not supported,  incorrect format, or bad data
++ too many too many parameters entered
++ range numeric entry is too large
GSYMChar  "n" Default: 0 None
This is a Hex value ranging from 0-255 that represents a symbol in APRS software. The symbol
char will be enclosed in curly brackets before all Beacon transmissions.
KA7SYZ*>RELAY>APRS [UI]:
{SYMChar} This is a sample UI frame...
GUNSTart  ON|OFFDefault: OFF
ON  -  The TNC starts in CONVERSE mode after a restart or power-up.
OFF -  The TNC will power-up in command mode.
This command allows the TNC to power-up in the converse mode if turned on.

<!-- PDF p.365 -->

> [No extractable text on this page — figure, schematic, or blank.]