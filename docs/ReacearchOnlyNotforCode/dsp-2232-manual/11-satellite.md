# Chapter 11 — Satellite Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 149–156).

**Agent extract:** 1200 bps BPSK (Manchester / 1200 Hz clock mentioned), 9600/4800 direct FSK, 1200 AFSK FM AX.25 (UO-14 / DOVE). Sample `PG.CFG` includes `speed 19200`, `port 2`. Tuning-indicator figures did not OCR.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.149)

### 11.1

### 11.2

Overview
Amateurs have always been experimenters and innovators of communicationg
technology. This has certainly been true in the area of satellite
communications. The satellites amateurs have designed and built relay
CW, SSB (voice) and now digital signals over large distances without
the need of ionospheric propagation which is not always reliable.
The space program began for amateurs with the launch of the first
satellite (OSCAR 1) in 1961. OSCAR, which stands for Orbital Satellite
Carrying Amateur Radio is the general term used to refer to most
amateur satellites once in orbit. Since that time some 20 satellites
for amateur use have been launched and more are being planned.
The first satellites were .beacons only and did not actually "relay"
signals.
By the mid 1960s however transponders capable of
retransmitting CW and SSB signals were added to the satellites.
Since
that time digital modes have been added first for telemetry reception
In the most recent satellites
and then for two way communications use.
digital transmissions. have even been used to return pictures to earth.
The special problems of satellite communications including weak signals
and Doppler shift make for some special modem requirements. Your
DSP-2232 supports the 1200 bits/ sec BPSK (Binary Phase Shift Keying)
as well as the 9600 bits/ sec FSK modems currently used in amateur
In fact, the Digital Signal Processing
satellite communications.
technology used in the DSP-2232 is ideal for satellite use because
new and experimental modems can be programmed in software.
Preparat ion
Satellite operation requires some specialized equipment and operating
techniques that cannot possibly be covered in this single chapter.
If you are new to this area of amateur radio, we strongly recommend
obtaining a good introduction to satellite operation from the ARRL or
other source. A guide such as this will discuss setting up a
satellite station, where to find and how to track the satellites ag
well as the satellite operating practices you will need to know.
While an introductory reference guide will get you started with
satellite operation, the most current information is obtained from
AMSAT. AMSAT is the Radio Amateur Satellite Corporation and is made
up of amateurs interested in designing, building, and using
communication satellites . A membership in AMSAT helps support future
amateur satellite efforts as well as provides you with the most
current satellite and space communication information.
AMSAT bulletins are also distributed on the amateur packet network,
Satellite
Compuserve, and other computerized information sources .
users should pay attention to these bulletins as they are both
interesting and sometimes crucial to the life of the satellites.

### (PDF p.150)

### 11.3

### 11.5

### 11.4

Operation
Before you can conmunicate through the satellites with 1200 bits/ sec
BPSK, you must connect the DSP-2232 to your satellite transmitter
and receiver as described in chapter 3.
Special Note for 9600 bits/ sec Operation:
For 9600 bits/ sec operation, the AFSK output of the DSP-2232
must be connected directly to the Varactor modulator stage of
the transmitter. Also note that the DSP-2232 receive audio mugt
be taken directly from the discriminator section of the receiver.
These special 9600 bits/ sec connections must be located on the
radio's schematic diagram by the user. Most often these
connections are not available on any external connector and
must be wired carefully inside the transceiver.
Most two-way digital satellite operation occurs in the Packet mode.
Be sure that the DSP-2232 is set to Packet operation before selecting
most satellite modems. Some satellite telemetry data is sent using
ASCII RTTY. This telemetry mode . is discussed later in the chapter.
Two way satellite operation generally requires that the transmitted
signal be on a different amateur band than the received signal. This
allows satellite QSOs to be in Full -Duplex. The digital satellites
are no exception to this so remember to turn the command FULLDUP ON
when operating through the satellites .
DSP-2232 Satellite Modems
The DSP-2232 contains many modems used in satellite communications.
Presently the
available and
following satellite modems listed by number are
can be listed with the D I Rectory command.
12:
14:
16:
18:
22:
28:
44:
45:
52:
pl Packet
pl Packet
pl Packet
pl Packet
p2 Packet
p2 Packet
1200 bps VHF
1200 bps PSK
4800 bps PACSAT
9600 bps FSK K9NG/G3RUH
1200 bps VHF
9600 bps FSK K9NG/G3RUH
13:
17:
23:
pl Packet 1200 bps PACSAT
pl Packet 4800 bps PSK
p2 Packet 1200 bps PACSAT
DSP data 400 bps OSCAR-13
RTTY/TOR 1200 bps ASCII OSCAR-II
pl Packet 9600 bps G3RUH.U022.eq
1200 bits [sec BPSK Operation
The most widely used satellite modem is the 1200 bits/ sec BPSK
(Binary Phase Shift Keying) often referred to as simply the PSK modem.
To select this modem enter the command MODEM 13 (or MODEM 23 for
radio port 2 ) at the DSP-2232 command prompt. The DSP-2232 is now
ready to receive a 1200 bits/ sec BPSK PACSAT signal from a SSB
receiver .

### (PDF p.151)

#### 11.5.1

#### 11.5.2

With this particular modem the transmit signal is exclusive ORed with
a 1200 Hz clock which is called Manchester encoding. This allows the
transmitted signal to be fed to an FM rather than an SSB transmitter.
This is precisely the way the TAPR/JAS PSK modem works when in the
Fuj i-Oscar 12 satellite mode.
BPSK Transmitter and Receiver Settings
As indicated above, the satellite receiver should be set to either
Upper or Lower Sideband. Due to the characteristics of the f i Iter in
your receiver, you may find that one sideband copies the satellites
better than the other. You should use whichever sideband produces
the best received copy.
The transmitter you use to access the satellite should be set in the
FM mode. Since PSK is a linear mode, it is quite critical that the
audio level has been properly set as described in Chapter 3.
PSK is
much more sensitive than FSK to over-driving of the transmitter.
Be
especially careful that your transmitted signal is no wider than 3.5
KHz which
Tuning in
Tuning in
operat ion.
is approximately of a full 5 KHz deviation.
BPSK Satellite Stations
BPSK satellite stations properly is critical to successful
Follow the procedure below for the best results.
certain your satellite receiver is in the SSB mode.
any IF-Shift and Passband-Tuning controls to the Center or
Make
Turn
OFF position.
Tune your receiver carefully to the BPSK satellite downlink
frequency. Be sure that you have properly calculated the
satellite's path and that it is one you can copy well with good
signal strength. Do not forget thåt the signal may be a few KHz
away from the exact downlink frequency due to Doppler shift .
When you find a station, slowly vary the V FO on your receiver and
look for a display on the DSP-2232 tuning indicator as shown.
Tuned In
If the tuning indicator looks like the one below, the frequency
from your speaker is too low for the DSP-2232 to copy the
signal .
Slowly tune the VFO and make the frequency higher.
Frequency
Too Low

### (PDF p.152)

### 11.6

#### 11.6.1

If the tuning indicator looks like the one below, the frequency
from your speaker is too high for the DSP-2232 to copy the
signal .
Slowly tune the VFO and make the frequency lower.
Frequency
Too High
Adjust the volume of the received signal so that the DCD LED
lights when a properly tuned BPSK signal is being received.
Note that the rear panel Satellite UP/ DOWN Doppler shift compensation
outputs are not yet functional with the 1200 bits/ sec BPSK PACSAT
modem. Satellites that use this mode include the following:
AMSAT OSCAR 16
DOVE OSCAR 17
WEBER OSCAR 18
LUSAT OSCAR 19
FUJI OSCAR 20
9600 bits/ sec Direct •FSK Operation
A modem that has gained some popularity in Packet and Packet satellite
use is the 9600 bits/ sec FSK G3RUH and K9NG compatible modems.
UoSAT OSCAR 14 uses this modem to both uplink and downlink data.
This modem is number 18 in the DSP-2232 and is selected by entering
the command MODEM 18 for radio port 1 or MODEM 28 for radio port
2 at the cornmand prompt. Modem 52 has been optimized for 9600
bits/ sec operation on UO-22.
You may find this modem provides
better performance when operating this satellite.
As mentioned above, this is modem is a direct FSK variety and must be
connected directly to the modulator stage of an FM transmitter in
order to transmit properly. Unfortunately most FM transmitters do
not provide connections to this internal stage and so it must be
found on the schematic and connected to internally by the user.
Similarly, the receive connection for this 9600 bits/ sec modem must
be made directly to the discriminator circuit of an FM receiver.
A few manufacturers do provide external connections to discriminator
audio but Gnfortunately for most receivers this connection must be
determined from the schematic diagram.
FSK Transmitter and Receiver Settinas
As indicated above, the satellite transmitter and receiver should be
set to the FM position.

### (PDF p.153)

#### 11.6.2

Tuning in FSK Satellite Stations
Tuning in 9600 bits/ sec direct FSK satellite stations properly is
critical to successful operation.
Follow the procedure below for
the best results.
Make certain your satellite receiver is in the FM mode.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver carefully to the 9600 bits/ sec satellite
downlink frequency. Be sure that you have properly calculated
the satellite's path and that it is one you can copy well with
good signal strength. Do not forget that the signal may be a few
KHz away from the exact downlink frequency due to Doppler shift .
When you find a station, slowly vary the VFO on your receiver and
look for a display on the DSP-2232 tuning indicator as shown.
Tuned In
If the tuning indicator looks like the one below, adjust the
frequency of your receiver to center the display as shown above.
If the tuning indicator looks like the one below, adjust the
frequency of your receiver to center the display as shown above.
Make sure that the output from the discriminator is high enough
so that the DCD LED lights when a properly tuned FSK signal
is being received.
Note that the rear panel Satellite UP/ DOWN Doppler shift compensation
outputs are not yet functional with the 9600 bits/ sec FSK modem.
The UoSAT OSCAR 14 and 22 satellites currently use these modems.

### (PDF p.154)

#### 11.6.3

Satellite Programs
To send and receive files on the PACSATs, you may wind up using the
PB and PG programs available from AMSAT. These require some
special parameter settings in the DSP-2232 which are listed below.
The following configuration files have been used successfully with
the PB and PG programs. Thanks to Bob McGwier ( N4HY) for the f i leg.
; PB configuration file example (PB. CFG)
Corments start with ; and are ignored.
; Remove and add
as appropriate to your setup.
; Set. your .callsign
mycall N4HY
; Set the satellite's callsign
bdcstcall uosat5-11
; Set a directory for the pb operations if you want to
; this is where all files will be stored
; pbpath C:
; And any other callsign or string that you are known by
; myaddr ALL
; COM Port and TNC settings
; port 1
port 2
speed 19200
restart delay 60
txd 120
Please use graball so the satellite is more useful for everyone
graball 1
; Up to 10 block f type entries keep PB from saving the file types
that you specify. e.g. blockftype 2 gets rid of BBS forwarding
traffic.
blockftype 2
These entries control frame logging
directory broadcast frames
; Iogdbframes 1
message broadcast frames
; logpbframes 1
all other frames
; logothers 1
How long to keep . act and . hol files
actdays 3
How many bytes of pfhdir. pfh is max
maxpfhdirsize 250000
• How many bytes of pfhdir . pfh to leave after archiving
minpfhdirsize 25000
How many minutes of no packets to exit after
; exitafter O
Turn off all automatic downloading THIS WAS O
automode 1
• Turn on batch file builder
; makebat 1
Bats 1 and Bats 2 allow you to customize what ends . up in
the batch file postpass. bat
bats 1 •call my proct
bats2 ' . dl'
ndupesearch 200
select pb. eqn

### (PDF p.155)

The following is a sample PG program configuration file PG.CFG.
speed 19200
port 2
bbscall uosat5-12
bdcstcall uosat5-11
mycall n4hy-O
maxdupes 10
maxsel 50
restart delay 36
break delay 36
The DSP-2232 displays monitored packets with a port identifying "pl"
or "p2" in front of the header to identify which port heard the
packet. While helpful to the human user, computer programs can have
problems with "extra" information such as this. To eliminate the
"pl" and "p2" from monitored packets the User BIT 19 command
(UBIT 19) may be turned OFF. With the versions of PB and PG
available in July of 1992, we recommend turning UBIT 19 OFF.
1200 and 4800 bits/ sec ASCII Operation
The UoSAT OSCAR 11 can transmit 1200 bits/sec FM AFSK ASCII signals
which can be received by the DSP-2232 with MODEM 45.
Place the
DSP-2232 into the ASCII mode and then select MODEM 45.
Finally
select the desired ASCII baud rate (ABAUD) to 1200.
The satellite receiver should be set to the FM mode.
Tuninq in ASCII Satellite Stations
Tuning in 1200 or 4800 bits/ sec AFSK ASCII satellite stations
properly is critical to successful operation.
Follow the procedure
below for the best results.

### 11.7

#### 11.7.1

Make certain your satellite receiver is in the FM mode.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver carefully to the 1200 or 4800 bits/ sec
satellite downlink frequency. Be sure that you have calculated
the satellite's path and that it is one you can copy with good
Do not forget that the signal may be a few KHz
signal strength.
away from the exact downlink frequency due to Doppler shift.
If your FM receiver has discriminator metering, use this as a
check to be sure you are exactly on the proper frequency.
When the station is tuned, the you should see a display as
shown below .
Tuned In

### (PDF p.156)

### 11.8

#### 11.8.1

#### 11.8.2

1200 bits/ sec AFSK FM AX. 25 Operation
The UOSAT OSCAR 14 and DOVE OSCAR 17 satellites can transmit 1200
bits/ sec FM AFSK AX. 25 standard VHF Packet signals. These gignalg
are received in the Packet mode with MODEM 12 for Radio Port 1 and
Modem 22 for Radio Port 2 as in normal VHF 1200 baud packet
AFSK FM Receiver Settings
Ag indicated above, the satellite receiver should be set to the
FM position.
Tuning in AFSK FM Satell ite Stations
Tuning in 1200 bits/ sec AFSK FM AX .25 packet satellite stations
properly is critical to successful operation. Follow the procedure
below for the best results.
Make certain your satellite receiver is in the FM mode.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver carefully to the 1200 bits/ sec satellite
downlink frequency. Be sure that you have properly calculated
the satellite's path and that it is one you can copy well with
good signal strength. Do not forget that the signal may be a
few KHz away from the exact downlink frequency due to Doppler
shift.
If your FM receiver has discriminator metering, use this as a
check to be sure you are exactly on the proper frequency.
When the station is tuned, the you should see a display on the
DSP-2232 LED bar graph as shown below.
Tuned In
Last page of Chapter 11
- Satellite Operation
