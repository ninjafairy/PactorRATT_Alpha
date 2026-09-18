# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands M (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### MAildrop ON|OFF                                                     Default: OFF
**Mode:** Packet    Host: MV
**Source:** (PDF p.238)
**Parameters:**
- ON    -   The PK-900 operates as a personal packet BBS or MailDrop.
- OFF   -   The PK-900 only operates as a normal AX.25 Level 2 TNC (default).
**Description:**
The PK-900's MailDrop is a personal mailbox that uses a subset of the W0RLI/WA7MBL PBBS commands.  When MailDrop is ON, other stations can connect to your PK-900, leave messages for you or read messages from you.

See the 3RDPARTY, MDCHECK, MDPROMPT, MDMON, MTEXT, MMSG and MYMAIL commands.


### MARK "n"                                                            Default: Current modem mark frequency
**Mode:** Command    Host: Mk
**Source:** (PDF p.239)
**Parameters:**
- "n"  -  500 to 3000 specifies the transmit mark frequency in Hz.
**Description:**
The MARK command is used to select a non-standard mark tone transmit frequency. The mark tone frequency range is 500 to 3000 Hz.  If the MARK command is entered without an argument ("n"), the current mark frequency is displayed.


### MARsdisp ON|OFF                                                     Default: OFF
**Mode:** Baudot and AMTOR, RTTY    Host: MW
**Source:** (PDF p.239)
**Parameters:**
- ON   -    The PK-900 translates received LTRS characters to a <CTRL-O>, and FIGS characters to a <CTRL-N> and sends these to the terminal.
- OFF  -    The PK-900 operates as before in Baudot and AMTOR (default).
**Description:**
The MARSDISP command permits the Baudot and AMTOR operator to detect and display every character including LTRS and FIGS sent by the other station.  The ACRDISP and ALFDISP may be turned off to prevent extraneous carriage-returns and Linefeeds from being sent to the display.  If this data is retransmitted, ACRRTTY should be 0, and ALFRTTY should be OFF.  The <CTRL-O> and <CTRL-N> characters will send LTRS and FIGS respectively.


### MAXframe "n/n"                                                      Default: 4/4
**Mode:** Packet    Host: MX
**Source:** (PDF p.239)
**Parameters:**
- "n"  -    1 to 7 signifies a number of packet frames.
**Description:**
MAXFRAME limits the number of unacknowledged packets your PK-900 permits on the radio link for each Radio Port.  It is also the number of contiguous packets your PK-900 will send in a single transmission.

The "best" value of MAXFRAME depends on your local channel conditions.  In most cases of local VHF packet keyboard operation, the default value of MAXFRAME 4 works well.  When the amount of traffic is heavy, the path in use is poor as on HF, you are using many digipeaters, you can actually improve your throughput by reducing MAXFRAME.  When operating HF packet try setting MAXFRAME to 1.


### MBEll ON|OFF/ON|OFF                                                 Default: OFF/OFF
**Mode:** Packet    Host: ME
**Source:** (PDF p.240)
**Parameters:**
- ON   -    Will send 3 BELL characters to the terminal when the callsign(s) of the station(s) monitored match the MFROM and MTO lists.
- OFF  -    The PK-900 will not send BELL characters to the terminal due to MONITORED packets.
**Description:**
MBELL can be used to alert the user to the presence of particular packet station(s) on the frequency.  For example if you want to be alerted when N7ML appears on Radio Port 2 you would set the following:

MBELL ON MONITOR 0/4 MFROM yes N7ML MTO NONE

When MBELL is ON, packets from and to all stations are displayed, but only those packets matching the MFROM and MTO lists cause the bell to ring.


### MBx call1[,call2][-"n"][ALL]                                        Default: none
**Mode:** Packet    Host: MB
**Source:** (PDF p.240)
**Parameters:**
- call  -   The call signs of one or two stations to be monitored.
- "n"   -   0 to 15, indicating an optional SSID.
**Description:**
The MBX command permits you to read or record useful or needed data without having to connect or log on to the source station(s).

MBX filters the received packet data so that only packets from the selected station(s) are shown, without headers or repeated frames.  MBX overrides normal monitor functions and can show one or both sides of a conversation. The operation of MBX command is as follows:

MBX NONE       (Default) All monitored frames are shown with their headers.

MBX ALL        Only the data fields in the I-frames and UI frames are shown. Data from retried frames will be shown each time such a frame is monitored.  The MFROM and MTO commands are active.

MBX CALL 1     Only the data in the I and UI frames to or from CALL 1 are shown. CALL 1 can be either the source or destination station.  Retried frames are not shown.  The MFROM and MTO commands are ignored.

MBX CALL 1,    Only the data in the I and UI frames are shown when CALL 1 is the CALL 2     source and CALL 2 is the destination or vice-versa.  Retried frames are not shown.  The MFROM and MTO commands are ignored.

A packet connection on any channel inhibits monitoring, if MBX is not set to "none".  MCON will only work if MBX is set to "none". Clear MBX with "%" "&" "N" "NO" "NONE" or "OFF" as arguments.


### MCon "n/n"                                                          Default: 0/0 (none)
**Mode:** Packet    Host: MC
**Source:** (PDF p.241)
**Parameters:**
- "n"  -    0 to 6 signifies various levels of monitor indications
**Description:**
Use MCON for selective monitoring of other packet traffic while connected to a distant station.  MCON works in similar fashion to MONITOR, but affects your display only while you are connected to another station.  A different MCON value may be set for each radio port.

If MCON is set to a value between "1" and "5," frames meant for you are displayed as though monitoring was OFF.  You'll see only the data.  If MCON is set to "6," frames meant for you are displayed as any other monitored frame. The headers appear together with the data.

The meanings of the parameter values are:

0    Monitoring while connected is disabled.

1    Only unnumbered (UI) frames resulting from an unconnected transmission are displayed.  Use this for an "unproto," roundtable type QSO.  This setting also display beacons.

2    Numbered (I) frames are also displayed.  Use this to monitor connected conversations in progress.

3    Connect request (SABM or "C") frames and disconnect (DISC or "D") frames are also displayed with the headers.

4    Unnumbered acknowledgment (UA) of connect- and disconnect-state frames are also displayed with either the characters "UA" or "DM" and a header.

5    Receive Ready (RR), Receive Not Ready (RNR), Reject (RJ), Frame Reject (FRMR) and (I)-Frames are also displayed.

6    Poll/Final bit, PID and sequence numbers are also displayed.


### MDCheck                                                             Immediate Command
**Mode:** AMTOR, Packet, PACTOR /MailDrop    Host: M1
**Source:** (PDF p.241)
**Description:**
MDCHECK is an immediate command which allows you to log on to your own MailDrop. After logging on, you can EDIT, LIST, READ, SEND or KILL MailDrop messages.

To use the MDCHECK command, and your PK-900 must not be connected to or linked to any packet, PACTOR or AMTOR stations.  For monitoring purposes, local access of the MailDrop is considered a connection.  Type "B" (BYE) to quit local control of your MailDrop.


### MDigi ON|OFF                                                        Default: OFF
**Mode:** Packet    Host: MD
**Source:** (PDF p.242)
**Parameters:**
- ON   -    I and UI frames having your call sign (MYCALL or MYALIAS) as the next digipeater in the field are displayed, regardless of connected status.
- OFF  -    Normal monitoring as determined by the monitoring mode commands.
**Description:**
MDIGI permits you to display packets when another station uses your station as a digipeater.  If you want to monitor ALL traffic that flows through your packet station, set MDIGI ON.

You may not want to see all the data passing through your station, especially if many others use you as a digipeater.  In this case set MDIGI OFF (default).


### MDMon ON|OFF                                                        Default: OFF
**Mode:** AMTOR and Packet/MailDrop    Host: Mm
**Source:** (PDF p.242)
**Parameters:**
- ON    -   Monitor a calling station's activity on your MailDrop.
- OFF   -   Normal monitoring as determined by the monitoring mode commands.
**Description:**
Set MDMON to ON to monitor activity on your MailDrop.

MDMON permits you to monitor activity on your AMTOR or packet MailDrop showing you both sides of the QSO.  Packet headers are not shown while a caller is logged on.  When no one is connected to your MailDrop, channel activity is monitored according to the setting of MONITOR.

Set MDMON OFF to cancel MailDrop monitoring.  Note that MailDrop connect and link status messages will be displayed even with MDMON OFF.  These status messages are important and allow you to see who is connected to your MailDrop. They can be disabled however with the UBIT 13 command.  See the UBIT command for more information .


### MDPrompt text                                                       Default: (see text)
**Mode:** Packet/PACTOR MailDrop    Host: Mp
**Source:** (PDF p.242)
**Parameters:**
- text  -   Any combination of characters and spaces up to a maximum of 80 bytes.
**Description:**
MDPROMPT is the command line sent to a calling station by your MailDrop in response to a Send message command.  The default text is:

"Subject:/Enter message, ^Z (CTRL-Z) or /EX to end"

Text before the first slash is sent to the user as the subject prompt; text after the slash is sent as the message text prompt.  If there is no slash in the text, the subject prompt is "Subject:" and the text prompt is from MDPROMPT.


### MEmory "n"                                                          Default: none
**Mode:** All    Host: MM
**Source:** (PDF p.243)
**Parameters:**
- "n"   -   A hexadecimal value used to access the PK-900's memory locations, or read values stored at a specified ADDRESS.
**Description:**
The MEMORY command works with the ADDRESS command (ADDRESS $aabb) and permits access to memory locations.  Use the Memory command without arguments to read a memory, and with one argument $0 to $FF to write to a memory location.  The value in ADDRESS is incremented after using the MEMORY command.


### MFIlter n1[,n2[,n3[,n4]]]                                           Default: $80
**Mode:** Morse, Baudot ASCII, AMTOR, PACTOR and Packet    Host: MI
**Source:** (PDF p.243)
**Parameters:**
- "n"  -    0 to $80 (0 to 128 decimal) specifies an ASCII character code. Up to four characters may be specified separated by commas.
**Description:**
Use MFILTER to select up to 4 characters to be "filtered," or excluded from Morse, Baudot, ASCII, AMTOR and monitored packets.  Parameters "n1," - "n4" are the ASCII codes for the characters you want to filter.  The special value of $80 (default) filters all characters above $7F and all control-characters except carriage-return ($0D), linefeed ($0A), and TAB ($09).


### MFrom ALL/NONE or YES/NO call1[,call2..]/ALL/NONE or YES/NO call1[,call2..] Default: ALL/ALL
**Mode:** Packet    Host: MF
**Source:** (PDF p.243)
**Parameters:**
- call  -   ALL/NONE or YES_list/NO_list (list of up to eight call signs, separated by commas).
**Description:**
MFROM determines what packets are monitored on each radio port.  To monitor all packets, set MFROM to ALL.  To stop any packets from being displayed, set MFROM and MTO to NONE.

To display packets from one or more specific stations, type MFROM YES followed by a list of call signs you WANT to monitor packets from.  To hide packets from one or more specific stations, type MFROM NO followed by a list of call signs you want NOT to monitor packets from.  When using MFROM, set MTO to NONE.

You can include optional SSIDs specified as "-n" after the call sign.  If MFROM is set to "NO N6IA," any combination N6IA-0,...N6IA-15 will NOT be monitored. If MFROM is set to "YES N6IA-1," then only N6IA-1 will be monitored. When MFROM and MTO contain different arguments, the following priority applies:

1.   ALL,     2.   NO_list,     3.   YES_list,     4.   NONE

Clear MFROM with "%" "&" or "OFF" as arguments.


### MHeard                                                              Immediate Command
**Mode:** Packet/AMTOR MailDrop    Host: MH
**Source:** (PDF p.244)
**Description:**
MHEARD is an immediate command that displays a list of up to 18 most recently heard stations.  Stations that are heard directly are marked with a * in the heard log.  Stations that have been repeated by a digipeater are not marked.

When DAYTIME has been set, entries in the heard log are time stamped.  When DAYSTAMP is ON the date is also shown.  An example of the MHEARD display is shown below: DAYSTAMP ON                             DAYSTAMP OFF 05-Jul-86  21:42:27  WA1FJW             21:42:27  WA1FJW 05-Jul-86  21:42:24  WA1IXU*            21:42:24  WA1IXU*

Clear the MHEARD list with a "%", "&", "N," "NO," "NONE" or "OFF" as arguments.


### MId "n/n"                                                           Default: 0/0 (00 sec.)
**Mode:** Packet    Host: Mi
**Source:** (PDF p.244)
**Parameters:**
- "n"  -    0 - 250 specifies the Morse ID timing in units of 10 second intervals. 0 (zero) disables this function.
**Description:**
If "n" is set to some value from 1 to 250, the PK-900 will periodically issue a 20 wpm Morse ID on any given Radio Port.  For example, an MID of 0/177 would cause a Morse ID every 1,770 seconds (29.5 minutes) on Radio Port 2.  A Morse ID will only be transmitted if a packet was sent since the last Morse ID.  The Morse ID uses TXDELAY, PPERSIST, and DCD.

If MID is set to a value other than 0, ID will force a Morse ID immediately. If both HID and MID are active, the Morse ID will be sent first.

MID normally sends a Morse ID using on/off keying of the low tone.  If FSK keying of both tones is desired to prevent stations from transmitting over your Morse ID, see the UBIT 12 command.


### MMsg ON|OFF                                                         Default: OFF
**Mode:** Packet/AMTOR/PACTOR MailDrop    Host: MU
**Source:** (PDF p.244)
**Parameters:**
- ON  -     The stored MTEXT message is sent as the first response after an AMTOR link or Packet connect to the MailDrop is established.
- OFF -     The MTEXT message is not sent at all.
**Description:**
MMSG enables or disables automatic transmission of the MTEXT message when your AMTOR or Packet MailDrop links with another station.


### MODem "n/n"                                                         Default: 11/4
**Mode:** All    Host: Mq
**Source:** (PDF p.245)
**Parameters:**
- "n/n"  -    1 to 12/1 to 10  signifies modem numbers from the list below
**Description:**
The MODEM command determines what Modem is selected for both Radio Ports  of the PK-900.  To select, for example modem 10 for Radio Port 1 enter the command:

MODem 10

To select, for example modem 4 for Radio Port 2 enter the command:

MODem /4

To select, for example modems 11 for Radio Port 1 and modem 3 for Radio Port 2, enter the command as:

MODem 11/3

The modems included in the PK-900 can be shown with the DIRECT(ory) command and are listed below:

Radio Port 1                  Radio Port 2

1:   FSK  45 bps    170: 2125/2295 1:   Internal 200:  1070/1270 2:   FSK  100 bps   170: 2125/2295 2:   Internal 200:  2025/2225 3:   FSK  45 bps    200: 2110/2310 3:   Internal 1000: 1200/2200 4:   FSK  100 bps   200: 2110/2310 4:   Internal 1000: 1200/2200 eq 5:   FSK  100 bps   425: 2125/2550 5:   Internal 200:  1180/980 6:   FSK  100 bps   850: 2125/2975 6:   Internal 200:  1850/1650 7:   FSK  100 bps   850: 2125/1275 7:   Internal 800:  2100/1300 8:   Analog 900/2500              8:   Internal 800:  2100/1300 eq 9:   FSK 2400 bps   800: 1300/2100 9:   Internal option 9600 bps 10:  FSK  300 bps   200: 2110/2310 10:  Modem disconnect header 11:  FSK  1200 bps  1000:1200/2200 12:  Morse 750 Hz center frequency cmd:


### Monitor "n/n"                                                       Default: 4/4 (UA DM C D I UI)
**Mode:** Packet    Host: MN
**Source:** (PDF p.246)
**Parameters:**
- "n"  -    0 to 6 signifies various levels on monitor indications
**Description:**
The Monitor command determines what kind of packets on each Radio Port are displayed when the PK-900 is NOT connected to any other packet stations.

The meanings of the parameter values are:

0    All packet monitoring functions are disabled.

1    Only unnumbered (UI) frames resulting from an unconnected transmission are displayed.  Use this for an "unproto," round-table type QSO.  This setting also displays beacons.

2    Numbered (I) frames are also displayed.  I-frames are numbered in order of generation and result from a connected transmission.  Use this to monitor connected conversations in progress.

3    Connect request (SABM or "C") frames and disconnect (DISC or "D") frames are also displayed with the headers.

4    Unnumbered acknowledgment (UA) of connect- and disconnect-state frames are also displayed with either the characters "UA" or "DM" and a header.

5    Receive Ready (RR), Receive Not Ready (RNR), Reject (RJ), Frame Reject (FRMR) and (I)-Frames are also displayed.

6    Poll/Final bit, PID and sequence numbers are also displayed.


### MOrse                                                               Immediate Command
**Mode:** Command    Host: MO
**Source:** (PDF p.246)
**Description:**
MORSE is an immediate command that switches Radio Port 1 of your PK-900 into the Morse mode.

Unless you change MSPEED, your PK-900 uses the default Morse transmit speed value of 20 WPM.


### MProto ON|OFF/ON|OFF                                                Default: OFF/OFF
**Mode:** Packet    Host: MQ
**Source:** (PDF p.247)
**Parameters:**
- ON   -    Monitors all I and UI frames as before.
- OFF  -    Monitors only those I and UI frames with a PID byte of $F0.
**Description:**
This is in response to NET/ROM, which sends frames that have a PID of $CF, and that contain Control characters.  If you want to monitor every frame including those used by NET/ROM, you must turn MPROTO ON.  This is setable for each radio port.


### MRpt ON|OFF/ON|OFF                                                  Default: ON/ON
**Mode:** Packet    Host: MR
**Source:** (PDF p.247)
**Parameters:**
- ON   -    Show digipeater path in the packet header.
- OFF  -    Show only originating and destination stations in the packet header.
**Description:**
MRPT affects the way monitored packets are displayed for each radio port. When MRPT is ON (default), the call signs of all stations in the digipeat path are displayed.  Call signs of stations heard directly are flagged with an asterisk (*) as shown:

W2JUP-4*>WA1IXU>W1AW-5>W1AW-4 <I;0,3>:

When MRPT is OFF, only the originating station and the destination stations are displayed are displayed in the monitored packet header as shown below:

W2JUP-4*>W1AW-4 <I;0,3>:


### MSPeed "n"                                                          Default: 20 WPM
**Mode:** Morse    Host: MP
**Source:** (PDF p.247)
**Parameters:**
- "n"   -   5 to 99 signifies your PK-900's Morse transmit speed.
**Description:**
The MSPEED command sets the Morse code keying (transmit) speed for Radio Port 1 of your PK-900 in the Morse Mode.  The slowest available Morse code speed is 5 words per minute.  When using Morse speeds between 5 and 14 WPM, the transmitted code is sent with Farnsworth spacing at a character speed of 15 words per minute.  The spacing between characters is lengthened to produce an overall code rate of 5 to 14 WPM.


### MStamp ON|OFF                                                       Default: OFF
**Mode:** Packet    Host: MS
**Source:** (PDF p.248)
**Parameters:**
- ON   -    Monitored frames ARE time stamped.
- OFF  -    Monitored frames ARE NOT time stamped.
**Description:**
The MSTAMP command activates time stamping of monitored packets.  When your PK-900's internal software clock is set, date and time information is available for automatic logging of packet activity and other applications. Remember to set the date and time with the DAYTIME command.

When MSTAMP is OFF, the packet header display looks like this:

W2JUP-4*>KA2EYW-1>AI2Q <I;2,2>:

When MSTAMP is ON and DAYSTAMP is OFF, the display looks like this:

22:51:33  W2JUP-4*>KA2EYW-1>AI2Q <I;2,2>:


### MTExt text                                                          Default: See sample
**Mode:** AMTOR/PACTOR/Packet MailDrop    Host: Mt
**Source:** (PDF p.248)
**Parameters:**
- text      Any printable message up to a maximum of 120 characters.
**Description:**
MTEXT is the "MailDrop automatic answer" text similar to CTEXT.  If MMSG is ON, the MTEXT message is sent when a station links to your AMTOR or Packet MailDrop. The default text is: "Welcome to my AEA PK-900 maildrop. Type H for help."

MTEXT can be cleared with a "%", "&", "N," "NO," "NONE" or "OFF" as arguments.


### MTo ALL/NONE or YES/NO call1[,call2..]/ALL/NONE or YES/NO call1[,call2..] Default: none/none
**Mode:** Packet    Host: MT
**Source:** (PDF p.249)
**Parameters:**
- call  -   ALL/NONE or YES_list/NO_list (list of up to eight call signs, separated by commas).
**Description:**
MTO determines what packets are monitored on each radio port.  To monitor all packets, set MTO to ALL.  To stop any packets from being displayed, set MTO and MFROM to NONE.

To display packets TO one or more specific stations, type MTO YES followed by a list of call signs you WANT to monitor packets to.  To hide packets TO one or more specific stations, type MTO NO followed by a list of call signs you want NOT to monitor packets to.  When using MTO, set MFROM to NONE.

You can include optional SSIDs specified as "-n" after the call sign.  If MTO is set to "NO N6IA," any combination N6IA-0,...N6IA-15 will NOT be monitored. If MTO is set to "YES N6IA-1," then only N6IA-1 will be monitored.

When MFROM and MTO contain different arguments, the following priority applies:

1.   ALL, 2.   NO_list, 3.   YES_list, 4.   NONE

Clear MTO with "%" "&" or "OFF" as arguments.


### MWeight "n"                                                         Default: 10
**Mode:** All except Packet    Host: Mw
**Source:** (PDF p.249)
**Parameters:**
- "n"  -    5 to 15, specifies roughly 10 times the ratio of one dot length to one inter-element space length in transmitted Morse code.
**Description:**
A value of 10 results in a 1:1 dot-space ratio.  A setting of 5 results in a 0.5:1 ratio, while a setting of 15 (maximum) results in a 1.5:1 ratio. MWEIGHT applies only to the Morse transmit mode and the CW ID in all modes except packet on Radio Port 1.

MWEIGHT does not affect the code output by the MID command.


### MXmit ON|OFF                                                        Default: OFF
**Mode:** Packet    Host: Mx
**Source:** (PDF p.250)
**Parameters:**
- ON  -     Monitor transmitted packets in the same manner as received packets.
- OFF -     Do not monitor transmitted packets.
**Description:**
When MXMIT is ON, transmitted packets are monitored in the same manner as received packets.  The monitoring of transmitted packets is subject to the settings of MONITOR, MCON, MFROM, MTO, MRPT and TRACE.  Most transmitted packets occur during connections so MCON should probably be set to a non-zero value.


### MYAlias call[-n]/call[-n]                                           Default: none/none
**Mode:** Packet    Host: MA
**Source:** (PDF p.250)
**Parameters:**
- call  -   Alternate packet digipeater identity of your PK-900
- "n"   -   0 to 15, an optional substation ID (SSID)
**Description:**
MYALIAS specifies an alternate call sign (in addition to the call sign specified in MYCALL) for use as a digipeater only.  a MYALIAS In some areas wide-coverage digipeater operators change their call sign to a shorter and easier to remember identifier.


### MYALTcal aaaa                                                       Default: none
**Mode:** AMTOR    Host: MK
**Source:** (PDF p.250)
**Parameters:**
- aaaa  -   Your alternate SELective CALling code (SELCALL)
**Description:**
Use the MYALTCAL command to specify an your alternate SELCALL which, under certain conditions, may be convenient or necessary.  You can enter an additional SELCALL code not related to your call sign.  The alternate SELCALL can be any four alphabetical characters, or can be numeric strings of either four or five numbers.  MYALTCAL is generally used for special applications such as receiving network or group broadcasts in AMTOR Mode B Selective (Bs or SELFEC).


### MYcall call[-"n"]/call[-"n"]                                        Default: PK900/PK900
**Mode:** Packet, PACTOR    Host: ML
**Source:** (PDF p.251)
**Parameters:**
- call  -   Your call sign
- "n"   -   0 - 15, indicating an optional substation ID, (SSID)
**Description:**
Use the MYCALL command to load your call signs into your PK-900.  Radio Port 1 and Radio Port 2 may each have their own Packet callsign.

The "PK900" default call sign is present in your PK-900's ROM for each Radio Port when the system is manufactured.  This "artificial call" must be changed for packet or PACTOR operation.

The callsign for each Port may be the same or different, but bear in mind that two or more stations cannot use the same call and SSID on the same frequency. Use a different SSID for each Radio Port if both Ports are on the same band or frequency.

On PACTOR, MYCALL is only used if MYPTCALL is not entered. An SSID in MYCALL is not used in PACTOR and will be ignored.


### MYIdent aaaaaaa[aa]                                                 Default: none
**Mode:** AMTOR    Host: Mg
**Source:** (PDF p.251)
**Parameters:**
- aaaaaaa[aa] -  Specifies the 7-character SELCALL as described in CCIR Rec. 625.
**Description:**
The MYIDENT command holds the CCIR Rec. 625 seven-character AMTOR SELCALL.

Amateurs may simply enter their callsign and the PK-900 will automatically translate it to a 7-character SELCALL as shown below:

MYIDENT  KA1XYZ    becomes  MYIDENT  KAIXYZZ.

If aaaaaaa[aa] is nine numerals, the unit translates the numerals to seven letters according to Recommendation 491.  If aaaaaaa consists of seven legal characters, the PK-900 accepts the characters without modifying them.  Legal SELCALL characters for Rec. 625 are the letters A-Z except G, H, J, L, N and W. If aaaaaaa is a string of characters of any length that includes illegal characters, the PK-900 will do the following translation on the characters:

0: O           4: Y           8: B           J: U 1: I           5: S           9: P           L: F 2: Z           6: D           G: C           N: V 3: E           7: T           H: K           W: M

All other letters are unchanged.

If MYSELCAL and MYIDENT are both none (defaults), no incoming ARQ or SELFEC call can establish communications with the unit.


### MYGate call[-"n"]                                                   Default: none
**Mode:** Packet    Host: MY
**Source:** (PDF p.252)
**Parameters:**
- call -    The Call Sign you wish to use for the Gateway.
- "n"  -    Numeral indicating an optional substation ID (SSID) or extension.
**Description:**
Call is the call sign of the Gateway, default "none."

"Call" may have an optional SSID, and must not be the same call sign and SSID as MYCALL or MYMAIL.  When another station digipeats via the callsign set in MYGATE, your PK-900 will provide Gateway operation between Radio Port 1 and Radio Port 2 provided both ports are enabled for Packet operation. See Chapter 4 for details of Gateway operation and limitations.


### MYMail call[-"n"]                                                   Default: none
**Mode:** Packet, PACTOR/MailDrop    Host: Ma
**Source:** (PDF p.252)
**Parameters:**
- call -    The Call Sign you wish to use for the MailDrop.
- "n"  -    Numeral indicating an optional substation ID (SSID) or extension.
**Description:**
Call is the call sign of the MailDrop, default "none."

"Call" may have an optional SSID, and must not be the same call sign and SSID as MYCALL.  If you do not set MYMAIL, the MailDrop will use the same call sign and SSID as entered in MYCALL.  For example, if you have set MYCALL to N7ML then MYMAIL may be N7ML-1 through N7ML-15.  You can use the CTEXT and MTEXT messages to inform other stations who connect of your MYCALL and MYMAIL call signs.


### MYPTcall call                                                       Default: PK900
**Mode:** PACTOR    Host: Mf
**Source:** (PDF p.252)
**Description:**
Use the MYPTCALL comand to load your call sign into your PK-900.  Only radio port 1 will operate on PACTOR.

If you have not loaded a call into the PK-900 with MYPTCALL the call loaded in MYCALL will be used.  The difference between MYCALL and MYPTCALL is that MYCALL allows only the dash (-) to be used while MYPTCALL will allow any punctuation with the call.

If calls have not been loaded into either MYCALL  or MYPTCALL, the PK-900 will not allow transmission on PACTOR.  An error message "Need MYCALL" will be displayed if transmission is attempted.


### MYSelcal aaaa                                                       Default: none
**Mode:** AMTOR    Host: MG
**Source:** (PDF p.253)
**Parameters:**
- aaaa  -   Specifies your SELective CALling code (SELCALL)
**Description:**
Use the MYSELCAL command to enter the SELCALL (SELective CALLing) code required in AMTOR ARQ (Mode A) and SELFEC operating modes.  MYSELCAL is a unique character string which must contain four alphabetic characters and is normally derived from your call sign.

Amateurs may simply enter their callsign and the PK-900 will automatically translate it to a 4-character SELCALL using the grouping table below:

GROUP     CALL     SELCALL 1 by 2    W1XY       WWXY 1 by 3    W1XYZ      WXYZ 2 by 1    AB1X       AABX 2 by 2    AB1XY      ABXY 2 by 3    KA1XYZ     KXYZ

Although the convention is to form the SELCALL from the call sign, your PK-900 can include any AMTOR character in the SELCALL.  In accordance with CCIR Recommendation 491, four- or five-digit numbers may be entered; the PK-900 automatically translates the numeric entry to your four-letter alpha SELCALL.
