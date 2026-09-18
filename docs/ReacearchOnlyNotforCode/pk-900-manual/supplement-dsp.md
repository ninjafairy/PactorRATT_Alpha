# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## PK-900/DSP Upgrade Kit (PDF p.366–372)

<!-- PDF p.366 -->

501 W. Lawson Ave. Tel: (651) 489-5080
St. Paul, Minnesota 55107 USA Fax: (651) 489-5066
PK900DSPInstall.doc
E-mail: jdouglas@timewave.com http://www.timewave.com
Welcome to DSP for the PK-900
Enclosed find your DSP upgrade kit. Follow the instructions carefully
and you should have no problem in getting the unit on the air. If you
do have questions, Timewave has technical support available at
(651) 489-5080 and DSP@timewave.com
You will want to experiment with different parameter setting in order
to optimize your new DSP Multi-mode controller. We have found that
changing the value of Audelay will improve performance in many
stations. If you operate in a traffic net then changes in the AUdelay at
both ends of the connection may further optimize traffic flow.
You may also require a bit more drive to the radio using the TX Level
1 control for the same level of drive that you had before upgrading
the PK-900.
To take advantage of the new CW filters, you can set the 50/100/200
Hz filters by selecting the proper CW modem 12,13 or 14 (Do a DIR
command to see your new modems) or If you are using the new PK
TERM 99 program, The CW filter selection is done from the menu.
If you would like a demo copy of PK TERM 99 download one from
http://www.timewave.com. If you are running WIN3.1 then you should
upgrade to PC PakRATT for Windows 2.1 to avoid the Y2K problem
in version 2.0
Thanks again for choosing Timewave for your digital station.

<!-- PDF p.367 -->

PK-900/DSP Upgrade Kit
Thank you for purchasing the PK-900/DSP upgrade. Here are the tools you will
need:
Wire Cutters
Phillips Screwdriver
Needle node pliers
Flat head screwdriver
Solder pencil and solder
Solder sucker tool or solder wick for cleaning the pads on C69, C89 and
the U55 "VIA"
Please check the contents of the kit at this time. You should have received:
PK-900/DSP Upgrade board assembly
Package with 1 metal and 1 plastic standoff
PK-900/DSP installation instructions (This document - 4 pages)
Manual supplement request form (1 page)
Warranty card
Clean off an area to work on your PK-900. You should take standard static
electric precautions when working on any ham equipment.
Here are the steps in the upgrade procedure:
1) Remove Power
2) Remove 4 screws holding chassis top
3) Locate and remove U36, U37 and U44 (See figure A.)
Slide the flat blade of the screwdriver under the chip and elevate carefully. Do
the same thing at the other end of the IC and it will pop out cleanly. BE SURE
the screwdriver is between the IC and the chip socket, NOT between the chip
socket and the board
4) Locate and remove C69 and C89 (see figure B)
We recommend that when you remove the capacitors that they be removed
by clipping the leads of the capacitor close to the body of the capacitor. Then
while holding the lead with the needle nose pliers CAREFULLY heat the
solder just until the lead can be removed without force. Be very careful not to
pull the barrel from the board by pulling before the solder flows. DO NOT
OVERHEAT. The hole can then be cleaned with the solder sucker tool or
solder wick.

<!-- PDF p.368 -->

5) Locate the "VIA: hole by U55 (closest to pins 2 & 3 (See figure B)
You will also need to clean the solder from the VIA by U55. Observe the same
precautions as above and do not overheat the board.
6) Remove screw in corner by U60
7) Insert metal standoff supplied into the hole of the screw just removed.
a) Inspect the PK-900/DSP board for damage, bent pins or chips that
may have become unseated. Reseat if necessary.
b) Peel paper back from the plastic standoff and insert in the hole in the
upper right hand side of the DSP board (near U5). The standoff is
inserted from the bottom of the board.
c) Carefully insert the DSP board into sockets U36 and U44 (See Fig A)
8) Re-install screw removed in step 6 through the board into standoff to secure
the board.
9) Solder the wires from the DSP board as follows (See fig B). The wires are
labeled on the DSP board:
C89 Rear Pad
C89 Front Pad
U55 "VIA" hole
C69 Front Pad
10) Secure the chassis top with all 4 screws, reconnect the unit and reset when
powering on for the first time.

<!-- PDF p.369 -->

> [No extractable text on this page — figure, schematic, or blank.]

<!-- PDF p.370 -->

Figure B
REMOVE
VIA

<!-- PDF p.371 -->

22
Analog & Digital I/O AP.06261
4-Jan-2003
Title:
Size:
P/N:
Date:
File:
Sheet ofB
Engineer:
Project:
Danville Signal Processing, Inc.
A. Clark
PK-900 DSP
PC_Beep 12
BIT_CLK6
SDATA_IN8
SYNC10
CS045
CS146
CHAIN_IN47
CHAIN_OUT48
SDATA_OUT5
Line_Out_L 35
Mic1 21
NC39,40,41,43,44
XTL_OUT3
Phone_In 13
Video_L 16Aux_R 15Aux_L 14
Video_R 17
VREFOut 28
VREF 27
AFilt1 29
AFilt2 30
Line_Out_R 36
Mic2 22
DVss4
DVss7
AVss42AVss26
DVdd9 DVdd1
AVdd38 AVdd25
CD_L 18
CD_GND 19
Line_In_R 24
Line_In_L 23
CD_R 20
Mono_Out 37
Filt_L 32
Filt_R 31
RX3D 33
CX3D 34RESET11
XTL_IN2
U6
AD1819A
C_CLK
DR
SCLK
DT
FSYNC
C_RESET
Vd+5
C4
.1uF
C3
.1uF
C7
.1uF
C15
1nF
R16
3.3K
R15
4.7K
C14
1uF
C8
1uF
C9
1uF
C10
1nF
C11
1nF
C13
.1uF
C12
10uF
SB_PK900
Vd+5
R18
10K
L1
FBEAD
I/OE2/GCLK2 40
IN/GCLK1 37
IN/OE1 38
INPUT/GCLR 39
TCK 26
IO18
IO19
IO20
IO21
IO23
IO25
IO27
IO28
IO30
IO31
IO33
IO34
IO35
IO22
IO 2
IO 3
IO 5
IO 6
IO 42
IO 43
IO 44
IO 8
IO 10
IO 11
IO 12
IO 13
IO 14
IO 15
TDI 1
TDO 32
TMS 7
VCCINT
GND
U5
EPM7064STC44
1 2
3 4
5 6
7 8
9 10
JH1
Program
1
14
2
13
3
12
4
11
5
17
6
16
7
15
8
9
10
20
19
18
J1
PK900 16V8
HD0
HD1
HD2
HD3
HD4
HD5
HD6
HD7
Vd+5
HD0
HD1
HD2
HD3
HD4
HD5
HD6
HD7
Vd+5
HA0
HA1
HA2
HA3
HA4
HA5
HA6
HA7
HA8
HA9
HA10
HA11
HA12
HA13
HA14
HA15
HA16
HA0
HA1
HA2
HA3
HA4
HA5
HA6
HA7
HA8
HA9
HA10
HA11
HA12
HA13
HA14
HA15
HA16
Vd+5
C6
10uF
HD0
HD1
HD2
HD3
HA3
HA4
HA5
HA6
HA7
HA2
HA0
HA1
Vd+5
IORD
IOWR
DSP_D10
DSP_D9
DSP_A0
DSP_D11
Vd+5
DSP_A1
DSP_WR
DSP_CS
DSP_A2
DSP_RD
DSP_A0
DSP_D8
DSP_D9
DSP_D10
DSP_D11
IO5
IO4
IO3
IO2
IO6
R12
1K
R11
1K
R10
1K
R13
1K
HA[0..16]
HD[0..7]
R14
2
C29
10nF
C21
10nF
C22
10nF
C23
10nF
C24
10nF
C25
10nF
C26
10nF
C27
10nF
C28
10nF
Vd+5
1
3
2
D1
BAV99
R17
2.2K
C16
.1uF
C17
.1uF
C18
.1uF
1
JP2
C89 Rear Side
1
JP3
C89 Front Side
1
JP4
C69 Front Side
Modulator
DSP Filter Output
DSP Filter Input
A012
A111
A210
A39
A48
A57
A66
A75
A827
A926
A1023
A1125
A124
A1328
A1429
A153
CE22
OE24
D0 13
D1 14
D2 15
D3 17
D4 18
D5 19
D6 20
D7 21A162
Vcc 32
Vpp 1
GND 16
PGM 31
NC 30
U7
27C010
A012
A111
A210
A39
A48
A57
A66
A75
A827
A926
A1023
A1125
A124
A1328
A1429
A153
CE22
OE24
D0 13
D1 14
D2 15
D3 17
D4 18
D5 19
D6 20
D7 21A162
Vcc 32
Vpp 1
GND 16
PGM 31
NC 30
J2
U36
C5
.1uF
C19
10uF
C20
10uF
C34
10nF
C31
10nF
C32
10nF
C33
10nF
C30
10nF
9,17,29,41
4,16,24,36
Vd+5
HA[0..16]
HD[0..7]
HA[0..16]

<!-- PDF p.372 -->

12
DSP Core AP.06261
4-Jan-2003
Title:
Size:
P/N:
Date:
File:
Sheet ofB
Engineer:
Project:
Danville Signal Processing, Inc.
A. Clark
PK-900 DSP
MCLR1
RA02 RB6 27RB7 28
RA24 RB5 26
RB4 25RA13
RA35
RA46 RB2 23RB3 24
Vss8 RB1 22
RB0 21RA57
OSC19
OSC210 Vss 19Vdd 20
RC112 RC7 18
RC6 17RC011
RC213
RC314 RC4 15RC5 16
U3
PIC16C63A
Vd+5
D0
D1
D2
D3
D4
D5
D6
D7
Vd+5
A0
A1
A2
A3
A4
A5
A6
A7
A8
A9
A10
A11
A12
A13
A0
A1
A2
A3
A4
A5
A6
A7
A8
A9
A10
A11
A12
A13
D0
D1
D2
D3
D4
D5
D6
D7
A[0..13]
C_CLK
R7
4.7K
Vd+5
SCLK
FSYNC
C_RESET
DR
DT
Vd+5
R2
4.7K
D0
D1
D2
D3
D4
D5
D6
D7
Vd+5
D[0..7]
Y1
18.432MHz
PRE4
CLK3
D2
CLR1
Q 5
Q 6
U2A
74AC74
A108
A119
A1210
A1311
D055
D156
D257
A86 A75 A64 A52 A41 A3100 A299 A198
D764 D663 D562 D461
BR52
EINT 50
A97
D1777
D1878
D1979
D2081
D2182
D2283
D2384
D1676
DMS22
PMS23
IOMS24
EBG 53
Vdd15
BG54
BMS21
CMS25
PF0/MODA 94
D968
D1069
D1170
D1272
D1373
D1474
D1575
D865
D358
PF1/MODB 93PF2/MODC 89
ELIN 49
ELOUT 48
ECLK 47
PF6 29
PF3 88
PF5 27
PF7 30
FL0 87FL1 86FL2 85
CLKIN 13
XTAL 14
CLKOUT 16
DT1 37TFS1 38
SCK1 42
RFS1 39
Vdd36
Vdd59
Vdd67
Vdd90
GND3
GND12
GND17
GND28
GND41
GND60
GND66
GND80
GND92
Vdd18
GND71
A097
RD20
WR19
DR1 40
DT0 31TFS0 32
SCK0 35
RFS0 33
DR0 34
EBR 51
EE 46
EMS 45
ERESET 43
BGH 95
PF4 26
PWDACK 96
RESET 44
PDN91 U1
ADSP-2184
Vd+5
R3
4.7K
Vd+5
PRE10
CLK11
D12
CLR13
Q 9
Q 8
U2B
74AC74
C1
22pF
C2
22pF
Vd+5
R8
22
R9
22
R6
10K
R4
10K
R5
10K
R1
4.7K
Vd+5
DSP_D9
DSP_CS
DSP_WR
DSP_RD
DSP_D10
DSP_D11
DSP_D8
DSP_A0
DSP_A1
DSP_A2
A0
A1
A2
D0
D1
D2
D3
12
34
56
78
910
JH101
JHD10
12
34
56
78
910
JH102
JHD102
R101 R102
R103 R104
R105
Vd+5 Vd+5
1
JP1b
U55 Via
TXDA2
SA_PK900
VCC
A010
A19
A28
A37
A46
A55
A64
A73
A825
A924
A1021
A1123
A122
A1326
A1427
A151
CE20
OE22
D0 11
D1 12
D2 13
D3 15
D4 16
D5 17
D6 18
D7 19
Vcc 28
GND 14
U4
27C512
1
2
3
4
JH103
JHS4
1
JP1a
U56 Pin 3
TXDA2