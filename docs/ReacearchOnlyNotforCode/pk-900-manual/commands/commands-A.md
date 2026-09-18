# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands A (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### AAb text                                                            Default: empty
**Mode:** Baudot, ASCII, AMTOR, PACTOR    Host: AU
**Source:** (PDF p.188)
**Parameters:**
- text  -   Any combination of characters up to a maximum of 24 characters.
**Description:**
Use the AAB command to enter an acknowledgment text for Baudot, ASCII and AMTOR. AAB sends automatic confirmation in Baudot, ASCII and AMTOR in response to a distant station's WRU? request.  Set WRU ON to activate Auto-Answerback in ASCII and Baudot.  In AMTOR and PACTOR, UserBIT 9 (UBIT 9) controls the Auto-Answerback feature.

Type "AAB (24-character text)" to store your AnswerBack in memory.


### ABaud "n"                                                           Default: 110 bauds
**Mode:** ASCII    Host: AB
**Source:** (PDF p.188)
**Parameters:**
- "n"   -   Specifies the ASCII data rate or signaling speed in bauds from your PK-900 to your radio.
**Description:**
ABAUD sets the radio ("on-air") baud rate only in the ASCII operating mode on Radio Port 1.  This value has no relationship to your computer or terminal program's baud rate.  The available "n" ASCII data rates in bauds are:

45, 50, 57, 75, 100, 110, 150, 200, 300, 400, 600, 1200, 2400, 4800 and 9600


### AChg                                                                Immediate Command
**Mode:** AMTOR, PACTOR    Host: AG
**Source:** (PDF p.188)
**Description:**
ACHG is an immediate command used in AMTOR and PACTOR by the receiving station to interrupt the sending station's transmissions. As the receiving station, you usually rely on the distant station, your partner in the ARQ "handshake", to send the "+?" or PTOver command to do the changeover.  However, in ARQ (Mode A), or linked PACTOR, you can use the ACHG command to "break in" on the sending station's transmission.  Use the ACHG command only when it is needed.


### ACKprior ON|OFF/ON|OFF                                              Default: OFF/OFF
**Mode:** Packet    Host: AN
**Source:** (PDF p.189)
**Parameters:**
- ON    -   Priority Acknowledgment is enabled.
- OFF   -   This feature is disabled.
**Description:**
This command implements the Priority Acknowledge scheme on each radio port. This protocol described by Eric Gustafson (N7CL) proposes to improve multiple access packet performance on HF and VHF simplex channels with hidden terminals. When a busy channel clears, the acknowledgments are sent immediately, while data and poll bits are held off long enough to prevent collisions with the ACKs. By giving priority to data ACKs, fewer ACKs will collide with other station's data, reducing retries.  Digipeated frames are sent immediately.  RAWHDLC and KISS force ACKPRIOR off.  These are the defaults for a P-persistence system with NO Priority Acknowledgment:

ACKPRIOR OFF,  PPERSIST ON,   PERSIST  63,  SLOTTIME 30, RESPTIME 0,    MAXFRAME 4,    FRACK    5

The following are the recommended command settings for Priority Acknowledge:

1200 baud VHF packet                              300 baud HF packet ACKPRIOR ON                                      ACKPRIOR ON PPERSIST ON                                      PPERSIST ON PERSIST  84                                      PERSIST  84 SLOTTIME 30                                      SLOTTIME 8 RESPTIME 0                                       RESPTIME 0 MAXFRAME 1 - 7                                   MAXFRAME 1 FRACK    8                                       FRACK    15 HBAUD    1200                                    HBAUD    300 VHF      ON  (or TONE 3)                         VHF      OFF (or TONE 0) DWAIT    doesn't matter                          DWAIT    doesn't matter

Stations using neither the Priority Acknowledge nor the P-persistence schemes should set DWAIT 73 for 1200 baud and DWAIT 76 for 300 baud work.  Stations using P-persistence but not Priority Acknowledge should set PERSIST and SLOTTIME to the same values that ACKPRIOR stations are using.

AEA and TAPR use some different command names to handle P-persistence. The following table should help with the AEA/TAPR command differences:

TAPR_SLOTS   MFJ SLOTMASK     AEA_PERSIST   Remarks 1              $00            255       Disables slotting 2              $01            127 3                              84 4              $03             63       Default setting 6                              42 8              $07             31       Very busy channel 12                             20 16             $0F             15       Extremely busy channel 64             $3F              3

AEA products calculate the TAPR ACKTIME value based on the setting of HBAUD. The TAPR DEADTIME command is analogous to the AEA SLOTTIME command.


### ACRDisp "n"                                                         Default: 0
**Mode:** ALL    Host: AA
**Source:** (PDF p.190)
**Parameters:**
- "n"   -   0 to 255 specifies the screen width, in columns or characters. 0 (zero) disables this function.
**Description:**
The numerical value "n" sets the terminal output format for your needs.  Your PK-900 sends a <CR><LF> sequence to your computer or terminal at the end of a line in the Command and Converse Modes when "n" characters have been printed. Most computers and terminals do this automatically so ACRDISP defaults to 0.

When the PK-900 is in the MORSE mode, received data will be broken up on word boundaries if possible.  At a column of 12 less than the ACRDISP value, the PK-900 starts looking for spaces in the received data.  The first space received after this column forces the PK-900 to generate a carriage return. If ACRDISP is 0 (default), this occurs at column 60.  If there are no spaces at or after this column then a carriage return occurs at ACRDISP.


### ACRPack ON|OFF                                                      Default: ON
**Mode:** Packet    Host: AK
**Source:** (PDF p.190)
**Parameters:**
- ON   -    The send-packet character IS added to packets sent in Converse Mode.
- OFF  -    The send-packet character is NOT added to packets.
**Description:**
When ACRPACK is ON (default), all packets sent in Converse Mode include the SEND-PACket character (normally <CR>) as the last character of the packet. When ACRPACK is OFF, the send-packet character is interpreted as a command, and is not included in the packet or echoed to the terminal. ACRPACK ON and SENDPAC $0D produce a natural conversational mode.


### ACRRtty "n"                                                         Default: 71 (69 in AMTOR)
**Mode:** Baudot/ASCII RTTY, AMTOR and PACTOR    Host: AT
**Source:** (PDF p.190)
**Parameters:**
- "n" -     0 - 255 specifies the number of characters on a line before a carriage return <CR> is automatically inserted in your transmitted text. Zero (0) disables the function.
**Description:**
When sending Baudot, ASCII and PACTOR, the ACRRTTY feature automatically sends a carriage return at the first space following the "nth" character or column.

Use this option when you are sending and don't want to be bothered by watching the screen or worrying about line length.  You should NOT use this option when retransmitting text received from another station; for example, ARRL Bulletins.

ACRRTTY is used in AMTOR, except that AMTOR is limited by international telex practices to a maximum of 69 characters per line.  If ACRRTTY is set to 71, in AMTOR the automatic carriage return function operates after 69 characters.


### ADDress "n"                                                         Default: $0000
**Mode:** ALL    Host: AE
**Source:** (PDF p.191)
**Parameters:**
- "n"  -    Zero to 65,535 ($0 to $FFFF) setting an Address in the PK-900 memory.
**Description:**
The ADDRESS sets an address somewhere in the PK-900's memory map.  This command is usually used with the IO, MEMORY and the PK commands.  It is used primarily by programmers and is of no use for normal PK-900 operation.


### ADelay "n"                                                          Default: 4 (40 msec.)
**Mode:** AMTOR, PACTOR    Host: AD
**Source:** (PDF p.191)
**Parameters:**
- "n"  -    1 to 9 specifies transmitter key-up delay in 10-millisecond intervals.
**Description:**
ADELAY is the length of time in tens of milliseconds between the time when the PK-900 activates the transmitter's PTT line and the ARQ data begins to flow. The ADELAY command allows you to adjust a variable delay, from 10 to 90 milliseconds to handle the PTT (Push-to-Talk) delay of different transmitters.

In most cases, the default value of 4 (40 milliseconds) is adequate for the majority of the popular HF transmitters.  If the AMTOR signal strength is good and you observe periodic errors caused by loss of phasing (shown by rephase cycles in the middle of an ARQ contact) during contacts, it may be necessary to adjust the ADELAY value.

o    Be sure that errors and rephasing effects are not caused by the distant

station before changing your ADELAY.

o    If changing your ADELAY values does not improve link performance, reinstall

your original value and ask the other station to try changing his ADELAY.

Because the ARQ mode allows 170 milliseconds for the signal to travel to the distant station and return, increasing ADELAY will reduce the maximum working distance.  The maximum theoretical range of an ARQ contact is limited to about 25,500 kilometers.  Using some of that time as transmit delay leaves less time for signal propagation.  Thus the maximum distance available is reduced.

Regardless of the setting of ADELAY, ARQ (Mode A) AMTOR may not work very well over very short distances, e.g., one or two miles.  However, in very short distance work, ARQ should not be necessary to achieve error-free copy.

PACTOR is less sensitive to transmitter turn on timing and values up to 10 (100 ms) may be used.


### AFilter ON|OFF                                                      Default: OFF
**Mode:** ALL    Host: AZ
**Source:** (PDF p.192)
**Parameters:**
- ON  -     The ASCII characters specified in the MFILTER are filtered out and never sent to the terminal or computer.
- OFF -     Characters in MFILTER list are only filtered from monitored packets.
**Description:**
Some terminals and computers use special characters to clear the screen or perform other "special" functions.  Placing these characters in the MFILTER list, and turning AFILTER ON will keep the PK-900 from sending them.

Exception:  When ECHO is ON, and the terminal or computer sends a filtered character, the PK-900 will echo it back to the terminal or computer.

AFILTER works regardless of mode, or CONNECT/CONVERSE/TRANSPARENT status. One must be careful to leave AFILTER OFF during Binary file transfers.


### ALFDisp ON|OFF                                                      Default: ON
**Mode:** All    Host: AI
**Source:** (PDF p.192)
**Parameters:**
- ON   -    A line feed character <LF> IS sent to the terminal after each <CR>.
- OFF  -    A <LF> is NOT sent to the terminal after each <CR>.
**Description:**
ALFDISP controls the display of carriage return characters received, as well as the echoing of those that are typed in.

When ALFDISP is ON (default), your PK-900 adds a line feed <LF> to each carriage return <CR> received, if needed.  If a line feed was received either immediately before or after a carriage return, ALFDISP will not add another line feed.  Use the PK-900's sign-on message to determine how carriage returns are being displayed.  ALFDISP affects your display; it does not affect transmitted data.

Set ALFDISP ON if the PK-900's sign-on message lines are typed over each other.  Set ALFDISP OFF if the PK-900's sign-on message is double spaced. ALFDISP is set correctly if the PK-900's sign-on message is single spaced.


### ALFPack ON|OFF                                                      Default: OFF
**Mode:** Packet    Host: AP
**Source:** (PDF p.193)
**Parameters:**
- ON   -    A <LF> character IS added after each <CR> sent in outgoing packets.
- OFF  -    A <LF> is NOT added to outgoing packets (default).
**Description:**
ALFPACK is similar to ALFDISP, except that the <LF> characters are added to outgoing (transmitted) packets, rather than to text displayed locally.

o    If the person you are talking to reports overprinting of packets from your

station, set ALFPACK ON.  ALFPACK is disabled in Transparent Mode.


### ALFRtty ON|OFF                                                      Default: ON
**Mode:** Baudot/ASCII RTTY    Host: AR
**Source:** (PDF p.193)
**Parameters:**
- ON   -    A line feed character <LF> IS sent after each carriage return <CR>.
- OFF  -    A <LF> is NOT sent after each <CR>.
**Description:**
If ALFRTTY is set ON when transmitting Baudot or ASCII RTTY, a line feed character is added and sent automatically after each <CR> character you type.

Use this option when you are typing into the transmit buffer and don't want to be bothered worrying about line length.  You should NOT use this option when retransmitting text received from another station; for example, ARRL Bulletins.

o    ALFRTTY has no effect in AMTOR; a line feed is automatically added after

each carriage return.


### AList                                                               Immediate Command
**Mode:** AMTOR    Host: AL
**Source:** (PDF p.193)
**Description:**
ALIST is an immediate command that switches your PK-900 into the ARQ Listen mode.

You can usually monitor an ARQ AMTOR contact between two linked stations using the ARQ Listen mode (also called Mode L).  This mode may need a few seconds to phase or acquire synchronization with the other stations.  Your ability to synchronize with the master station depends on operating conditions such as interference.  Since you are not part of the "error free" link, your monitor will display retries that occur if the two linked stations you are monitoring experience ARQ errors.

Type ALIST (or AL) repeatedly if you lose synchronization.


### ALTModem "n"                                                Default 0 Default: 0
**Mode:** Command    Host: Am
**Source:** (PDF p.194)
**Parameters:**
- "n"  -   0 - 2.  0 selects the normal port 2 modems,  1 selects the internal option, (modem 9) and 2 selects the modem disconnect header.
**Description:**
ALTModem is a command that has is used in the PK-232 to select an internal modem.  It has been retained in the PK-900 for host mode software compatibility and is used to select the radio port 2 internal modem option. ALTModem 0 allows normal port 2 modem selection.  (See the MODem command.) ALTModem 1 selects the radio port 2 internal optional modem.  A 9600 baud K9NG/G3RUH compatible, internal modem is available.


### AMtor                                                               Immediate Command
**Mode:** Command    Host: AM
**Source:** (PDF p.194)
**Description:**
AMTOR is an immediate command that switches Radio Port 1 of your PK-900 into the AMTOR mode.  Your PK-900 is automatically placed in ARQ Standby condition.

Your station is then available for automatic access by and response to any AMTOR station that sends your SELCALL.  The PK-900 can communicate using either the CCIR 476 (4-character SELCALL) or the CCIR 625 (7-character SELCALL) protocol. Your monitor will also display any inbound FEC (Mode B) transmissions.

See the MYSELCAL and MYIDENT commands to enter your 4 and 7 character SELCALLs.


### ANalog                                                              Immediate Command
**Mode:** Command    Host: An
**Source:** (PDF p.195)
**Description:**
ANALOG is an immediate command that switches Radio Port 1 of your PK-900 into the ANALOG mode.  The Analog mode passes data for communication modes that require "gray scales" such as FAX.  Your PK-900 is automatically placed in the ANALOG receive condition.  Most PK-900 users will not use this mode the way Packet and Baudot are used.  Rather, application programs such as AEA FAX-900 use this mode to process FAX signals allowing gray scale images to be displayed.

In receive, the ANALOG mode enables a zero-crossing audio detector which feeds this information to the computer via the RS-232 serial port.  A computer program must further process this information for it to be of any use.

In transmit, the PK-900 takes bytes of binary information from the serial port and converts them to audio frequencies from 900.0 - 2493.75 Hz.  This conversion is done in a linear fashion where $00 corresponds to 900 Hz and $FF corresponds to 2493.75 Hz.  This gives a frequency resolution of 6.25 Hz per step.

This is convenient for FAX transmissions which typically range from 1500 Hz to 2300 Hz.  A binary value of $60 received on the serial port will cause the PK-900 to produce a frequency of 1500 Hz.  A binary value of $E0 causes 2300 Hz to be produced.  This allows for fax transmissions with 128 shades of gray.

To use the Analog mode, the program should first set ANSAMPLE then enter the ANALOG mode.  ANALOG initializes in the receive state.  The following commands function in other operating modes and now control the ANALOG mode as well.

R (Receive):  Receive. (return to receive) X (Xmit):     Data transmit.

Since binary data is often needed for ANALOG transmission, the CONMODE command should be set to TRANSparent.


### ANSample "n"                                                        Default: 2000
**Mode:** Analog    Host: As
**Source:** (PDF p.195)
**Parameters:**
- "n"   -   900 to 65535 decimal specifies a number to be loaded into the 8536 timer chip to control the Analog mode sample rate.
**Description:**
The value n is sent directly to the 8536 timer chip to control the sample rate of the ANALOG mode.  The number of samples per second is controlled by the following formula:

Samples/second  =  2,000,000 / ANSAMPLE.

The default value of n is 2000, meaning 1000 samples/sec.  The lower limit on ANSAMPLE (900) corresponds to 2,222 Samples/second.  Attempting to set ANSAMPLE to a value lower than 900 (more than 2222 samples per second) produces the "range" error.  This limit may change in the future.  Presently, the PK-900 may not be able to keep up with values of ANSAMPLE below 1600.


### ARq aaaa[aaa]                                                       Immediate Command
**Mode:** AMTOR    Host: AC
**Source:** (PDF p.196)
**Parameters:**
- aaaa[aaa]   -  The distant station's 4-character or 7-character SELCALL code.
**Description:**
ARQ is an immediate command that starts an AMTOR Mode A (ARQ) SELCALL (SELective CALL) to a distant station.

To begin the Mode A (ARQ) selective call type "ARQ" followed by the other station's SELCALL:

Example:  ARQ NNML            (4-character SELCALL)

or        ARQ VTMFFFF         (7-character SELCALL)

As soon as a <CR> is typed, your PK-900 will begin keying your transmitter on Radio Port 1 in the three-character AMTOR ARQ burst sequence.  If the distant station receives and decodes your selective call successfully, the two AMTOR systems synchronize and begin the Mode A (ARQ) AMTOR "handshaking" process.

See the MYSELCAL and MYIDENT commands to enter your 4- and 7- character SELCALLs. Other AMTOR commands are ACHG, ACRRTTY, ADELAY, ALFRTTY, ARQTMO, EAS, HEREIS and RECEIVE.

An ARQ transmission may be terminated by typing <CTRL> D.


### ARQE                                                                Immediate Command
**Mode:** Command    Host: Ae
**Source:** (PDF p.196)
**Description:**
ARQE is an immediate command that switches Radio Port 1 of the PK-900 into the ARQ-E receiving mode.

ARQ-E is similar to 1-channel TDM, except that the 7-bit code is different. Like TDM most ARQ-E stations send idle signals for long periods of time.  The PK-900 can only phase on ARQ-E signals that are idling so this is not a problem.

The SIGNAL Identification (SIAM) mode will identify ARQ-E signals for the user. They are identified as "TDM ARQ-E:4" or "TDM ARQ-E:8," referring to 4- and 8character repetition cycles used in this mode.


### ARQTmo "n"                                                          Default: 60
**Mode:** AMTOR, PACTOR    Host: AO
**Source:** (PDF p.197)
**Parameters:**
- "n"  -    0 to 250 specifies the number of seconds to send an ARQ SELCALL or PAConn before automatic transmitter shutdown.
**Description:**
ARQTMO sets the length of time during which your ARQ SELCALL or PACTOR call will be sent, shutting down automatically.  As a general rule, if you can't activate another AMTOR station in the default time of 60 seconds, you can probably assume that the other station can't hear your transmission.


### ARQTOL "n"                                                          Default: 3
**Mode:** AMTOR ARQ    Host: Ao
**Source:** (PDF p.197)
**Parameters:**
- "n"   -   1 to 5, specifying a relative tolerance for bit boundary jitter.
**Description:**
ARQTOL controls the tolerance for received bit boundary jitter in AMTOR ARQ mode.  n is a number from 1 (tight tolerance) to 5 (loose tolerance).  The number signifies how far away from the expected bit transition time the actual received transition may be, in tenths of a bit (milliseconds).  If the transition occurs further away than expected, the received block is counted as an error, even if all three characters in the block appear to be valid AMTOR characters.  The default value of ARQTOL 3 is the equivalent of the fixed tolerance of previous firmware releases.

ARQTOL should be set to a low number (tighter tolerance) for applications that require nearly error-free communications.  The tradeoff is that good received character blocks are counted as bad if the bit transitions are suspect, thereby causing retransmissions and lowering the effective character rate.

ARQTOL does not affect FEC, SELFEC or ARQ Listen modes.


### AScii                                                               Immediate Command
**Mode:** Command    Host: AS
**Source:** (PDF p.197)
**Description:**
ASCII is an immediate command that switches Radio Port 1 your PK-900 into the ASCII mode.

ASCII is the proper mode to use if you wish to use RTTY to transmit text, data or other information containing lower case and special characters not present in the Baudot/Murray and ITA #2 alphabets or character sets.  When 8BITCONV is set ON, 8-bit ASCII data may also be sent and received.

Because the ASCII character set requires a minimum of seven bits to define each character, under worst-case conditions, ASCII is more subject to data errors and garbled text than Baudot/ITA#2 at the same data rate.


### ASPect "n"                                                          Default: 2 (576)
**Mode:** FAX    Host: AY
**Source:** (PDF p.198)
**Parameters:**
- "n"   -   1 to 6, specifying the number of FAX scan lines the PK-900 displays out of every 6 lines received.
**Description:**
ASPECT controls the aspect ratio of the length to the width of a FAX image by controlling the number of lines the PK-900 displays out of each 6 received lines.

On most weather charts, the default of ASPECT 2 keeps the shapes received in the right proportion.  On other transmissions, you may want more resolution. See the table below for suggested settings.

CCITT IOCs for narrow and wide carriage printers are given for each ASPECT setting below.

ASPECT    CCITT IOC (narrow)                 CCITT IOC (wide) 1             1100                              1788 2              550  (Weather Charts 576)         894 3              367  (Wirephotos 352)             596  (Weather Charts 576) 4              275  (WEFAX Satellite 288)        447 5              220                               358  (Wirephotos 352) 6              183                               298  (WEFAX Satellite 288)

The Index Of Cooperation, or IOC is an international measure of aspect ratio. The formula for the CCITT IOC is:

(vertical scan line density) X (horizontal width) 3.14159

Weather charts are transmitted at a nominal CCITT IOC of 576.  ASPECT 2 is so close to this that the charts display with no noticeable distortion.


### AUdelay "n/n"                                                       Default: 2/2 (20 msec.)
**Mode:** Baudot, ASCII, FEC, FAX, PACTOR and Packet    Host: AQ
**Source:** (PDF p.199)
**Parameters:**
- "n"  -    0 - 120 specifies in units of 10 msec. intervals, the delay between PTT going active and the start of the transmit AFSK audio tones on each PK-900 Radio Port.
**Description:**
In some applications it may be desirable to create a delay from the time that the radio PTT line is keyed and the time that audio is produced from the PK-900.  Most notably, on HF when an amplifier is used, arcing of the amplifier relay contacts may occur if drive to the amplifier is applied before the contacts have closed.  If arcing occurs, increase AUDELAY for that Radio Port slowly until the arcing stops.

In VHF or UHF FM operation, some synthesized transceivers may produce undesirable spurious emissions, if audio and PTT are applied at the same time. These emissions may be reduced by setting AUDELAY to roughly 1/2 of TXDELAY.

Please note that AUDELAY must always be less than TXDELAY.  It is advisable that AUDELAY be set lower than TXDELAY by a setting of 10.  For example, you have determined that a TXDELAY of 20 works well for your transceiver.  Subtracting 10 from 20 yields 10, which is the recommended setting for AUDELAY.  If a setting of AUDELAY of 10 is too short, then set both TXDELAY and AUDELAY higher.


### AUTOBaud ON|OFF                                                     Default: OFF
**Mode:** Command    Host: Ab
**Source:** (PDF p.199)
**Parameters:**
- ON  -     Autobaud Routine always present at Power-ON or RESTART.
- OFF -     Autobaud Routine active at Power-ON only if battery jumper is removed.
**Description:**
When AUTOBAUD is OFF (default), the unit performs the autobaud function only when powering ON or after a RESET.  When AUTOBAUD is ON, the PK-900 performs the autobaud routine EVERY time it is powered ON, and EVERY time the RESTART command is entered.  The stored parameters (e.g. MYCALL) are saved if the battery jumper is connected.  The unit displays the autobaud message at the same rate as the last setting of TBAUD.  AUTOBAUD ON is helpful when moving the unit from one computer to another, where the terminal data rates are different.

In the autobaud routine, only one asterisk (*) is needed to set the terminal speed TBAUD.  The autobaud routine detects 110, 300, 600, 1200, 2400, 4800 and 9600 baud, at either 7 bits even parity, or 8 bits no parity.


### AWlen "n"                                                           Default: 7
**Mode:** All    Host: AW
**Source:** (PDF p.200)
**Parameters:**
- "n"  -    7 or 8 specifies the number of data bits per word.
**Description:**
The parameter value defines the digital word length used by the RS-232 serial input/output (I/O) terminal port and your computer or terminal program.

AWLEN should be set properly by the PK-900 Autobaud routine.  Still you may want to change the ASCII word-length at some time to accommodate a new terminal program you wish to use.

For plain text conversations with the PK-900, an AWLEN of 7 or 8 may be used. For binary file transfers and HOST Mode operation, an AWLEN of 8 MUST be used.

The RESTART command must be issued before a change in word length takes effect. Do NOT change AWLEN unless the terminal can be changed to the same setting.


### Ax25l2v2 ON|OFF/ON|OFF                                              Default: ON/ON
**Mode:** Packet    Host: AV
**Source:** (PDF p.200)
**Parameters:**
- ON   -    The PK-900 uses AX.25 Level 2 Version 2.0 protocol on Port x.
- OFF  -    The PK-900 uses AX.25 Level 2 Version 1.0 protocol on Port x.
**Description:**
This command allows the selection of either the old (version 1) version of the AX.25 packet protocol or the current (version 2.0) protocol on each Radio Port.

Some implementations of version 1 of AX.25 protocol won't properly digipeat Version 2.0 AX.25 packets.  Most users run AX.25 version 2 but this command allows returning to the older version if necessary for compatibility.

Some users also prefer to run AX.25 version 1 on HF and version 2 on VHF.  The default is to run version 2 on both Radio Ports, but it can easily be changed.


### AXDelay "n/n"                                                       Default: 0/0 (00 msec.)
**Mode:** Packet    Host: AX
**Source:** (PDF p.201)
**Parameters:**
- "n"  -    0 to 180  specifies a key-up delay for voice repeater operation in ten-millisecond intervals.
- AXDELAY specifies the period of time the PK-900 will wait - in addition to the
- delay set by TXDELAY - after keying the transmitter and before data is sent on each Radio Port.
**Description:**
Packet groups using a standard "voice" repeater to extend the range of the local area network may need to use this feature.

Repeaters with slow electromechanical relays, auxiliary links (or other circuits which delay transmission after the RF carrier is present) require more time to get RF on the air.  Try various values to find the best value for "n" if you're using a repeater that hasn't been used for packet operations before. If other packet stations have been using the repeater, check with them for the proper setting.  AXDELAY acts together with AXHANG.


### AXHang "n/n"                                                        Default: 0/0 (000 msec.)
**Mode:** Packet    Host: AH
**Source:** (PDF p.201)
**Parameters:**
- "n"  -    0 to 20 specifies voice repeater "hang time" in 100-millisecond intervals.
**Description:**
AXHANG allows you to increase efficiency when sending packets through an audio repeater that has a hang time greater than 100 milliseconds.

When the PK-900 has heard a packet sent within the AXHANG period, it does not add the repeater key-up delay (AXDELAY) to the key-up time.  Try various values to find the best value for "n" if you are using a voice repeater that hasn't been used for packet operations before.  If other packet stations have been using the repeater, check with them for the proper setting.
