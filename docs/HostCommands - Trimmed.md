# PK-232 Host Commands (Trimmed)

Relevant PK-232 commands for **PactorRATT_Alpha**. Irrelevant modes/features were removed; remaining entries are considered in-scope for this project unless noted otherwise.

**Case sensitivity:** Host Mode two-letter mnemonics are **case-sensitive**. Send them exactly as shown in each entry’s `Host:` field (e.g. Pactor standby is `Pt`, not `PT` or `pt`). Mixing case can select a different command or fail.

---

### 3Rdparty ON|OFF                                             Default: OFF
**Mode:** Packet/MailDrop                                       Host: 3R
**Parameters:**
- ON    -   The MailDrop will handle third party traffic.
- OFF   -   The MailDrop will only handle mail to or from MYCALL or MYMAIL.
**Description:**
If 3RDPARTY is ON, then remote MailDrop users may leave messages for any station. 


### AChg                                                   		Immediate Command
**Mode:** AMTOR, Pactor                                       	Host: AG
**Description:**
ACHG is an immediate command used in AMTOR by the receiving station to interrupt  the sending station's transmissions. 
As the receiving station, you usually rely on the distant station, your partner  in the ARQ "handshake", to send the "+?" command to do the changeover.  
However,  in ARQ (Mode A), you can use the ACHG command to "break in" on the sending  station's transmission.  Use the ACHG command only when it is needed.


### ACRDisp "n"                                                 Default: 0
**Mode:** ALL                                                   Host: AA
**Parameters:**
- "n"   -   0 to 255 specifies the screen width, in columns or characters.
- 0 (zero) disables this function.
**Description:**
The numerical value "n" sets the terminal output format for your needs.  
Your  PK-232 sends a <CR><LF> sequence to your computer or terminal at the end of a  line in the Command and Converse Modes when "n" characters have been printed.   
Most computers and terminals do this automatically so ACRDISP defaults to 0. When the PK-232 is in the MORSE mode, received data will be broken up on word  boundaries if possible.  
At a column of 12 less than the ACRDISP value, the  PK-232 starts looking for spaces in the received data.  
The first space received  after this column forces the PK-232 to generate a carriage return.  
If ACRDISP  is 0 (default), this occurs at column 60.  If there are no spaces at or after  this column then a carriage return occurs at ACRDISP. 


### ADDress "n"                                            		Default: $0000
**Mode:** ALL                                              		Host: AE
**Parameters:**
- "n"  -    Zero to 65,535 ($0 to $FFFF) setting an Address in the PK-232 memory.
**Description:**
The ADDRESS sets an address somewhere in the PK-232's memory map.  This command  is usually used with the IO, MEMORY and the PK commands.  
It is used primarily  by programmers and is of little use without the PK-232 Technical Manual. 


### ADelay "n"                                     			   	Default: 4 (40 msec.)
**Mode:** AMTOR                                       			Host: AD
**Parameters:**
- "n"  -    1 to 9 specifies transmitter key-up delay in 10-millisecond intervals.
**Description:**
ADELAY is the length of time in tens of milliseconds between the time when the  PK-232 activates the transmitter's PTT line and the ARQ data begins to flow.   
The ADELAY command allows you to adjust a variable delay, from 10 to 90  milliseconds to handle the PTT (Push-to-Talk) delay of different transmitters. 
In most cases, the default value of 4 (40 milliseconds) is adequate for the  majority of the popular HF transmitters.  
If the AMTOR signal strength is good  and you observe periodic errors caused by loss of phasing (shown by rephase  cycles in the middle of an ARQ contact) during contacts, 
it may be necessary to  adjust the ADELAY value.   

	Be sure that errors and rephasing effects are not caused by the distant  station before changing your ADELAY. 
	If changing your ADELAY values does not improve link performance, reinstall  your original value and ask the other station to try changing his ADELAY. 
	
Because the ARQ mode allows 170 milliseconds for the signal to travel to the  distant station and return, increasing ADELAY will reduce the maximum working  distance.  
The maximum theoretical range of an ARQ contact is limited to about  25,500 kilometers.  Using some of that time as transmit delay leaves less time  for signal propagation.  
Thus the maximum distance available is reduced. Regardless of the setting of ADELAY, ARQ (Mode A) AMTOR may not work very well  over very short distances, e.g., one or two miles.  
However, in very short  distance work, ARQ should not be necessary to achieve error-free copy.


### AFilter ON|OFF                                              Default: OFF
**Mode:** ALL                                                   Host: AZ
**Parameters:**
- ON  -     The ASCII characters specified in the MFILTER are filtered out and 
- never sent to the terminal or computer.
- OFF -     Characters in MFILTER list are only filtered from monitored packets.
**Description:**
Some terminals and computers use special characters to clear the screen or  perform other "special" functions.  
Placing these characters in the MFILTER  list, and turning AFILTER ON will keep the PK-232 from sending them. 
Exception:  When ECHO is ON, and the terminal or computer sends a filtered  character, the PK-232 will echo it back to the terminal or computer. 
AFILTER works regardless of mode, or CONNECT/CONVERSE/TRANSPARENT status.   One must be careful to leave AFILTER OFF during Binary file transfers.


### ALFDisp ON|OFF                                              Default: ON
**Mode:** All                                                   Host: AI
**Parameters:**
- ON   -    A line feed character <LF> IS sent to the terminal after each <CR>.
- OFF  -    A <LF> is NOT sent to the terminal after each <CR>.
**Description:**
ALFDISP controls the display of carriage return characters received, as well as  the echoing of those that are typed in. 
When ALFDISP is ON (default), your PK-232 adds a line feed <LF> to each carriage  return <CR> received, if needed.  
If a line feed was received either immediately  before or after a carriage return, ALFDISP will not add another line feed.  
Use  the PK-232's sign-on message to determine how carriage returns are being  displayed.  ALFDISP affects your display; it does not affect transmitted data. 
Set ALFDISP ON if the PK-232's sign-on message lines are typed over each other. Set ALFDISP OFF if the PK-232's sign-on message is double spaced. 
ALFDISP is set correctly if the PK-232's sign-on message is single spaced.


### ARQTmo "n" 													Default: 60
**Mode:** AMTOR, Pactor 										Host: AO
**Parameters:**
- “n”   -      0 to 250 specifies the number of seconds to send an ARQ SELCALL
- before automatic transmitter shutdown.
**Description:**
- ARQTMO sets the length of time during which your ARQ call will be sent, shutting
- down automatically.  As a general rule, if you can't activate another AMTOR or
- Pactor station in the default time of 60 seconds, you can probably assume that
- the other station can't hear your transmission.



### AUdelay "n"                                       			Default: 2 (20 msec.)
**Mode:** Baudot, ASCII, FEC, FAX and Packet          			Host: AQ
**Parameters:**
- "n"  -    0 - 120 specifies in units of 10 msec. intervals, the delay between 
- PTT going active and the start of the transmit AFSK audio tones.
**Description:**
In some applications it may be desirable to create a delay from the time that  the radio PTT line is keyed and the time that audio is produced from the PK-232.   
Most notably, on HF when an amplifier is used, arcing of the amplifier relay  contacts may occur if drive to the amplifier is applied before the contacts have  closed.  
If arcing occurs, increase AUDELAY slowly until the arcing stops.   
In VHF or UHF FM operation, some synthesized transceivers may produce  undesirable spurious emissions, if audio and PTT are applied at the same time.   
These emissions may be reduced by setting AUDELAY to roughly 1/2 of TXDELAY. Please note that AUDELAY must always be less than TXDELAY.  
It is advisable that  AUDELAY be set lower than TXDELAY by a setting of 10.  For example, you have  determined that a TXDELAY of 20 works well for your transceiver.  
Subtracting 10  from 20 yields 10, which is the recommended setting for AUDELAY.  If a setting  of AUDELAY of 10 is too short, then set both TXDELAY and AUDELAY higher.


### AUTOBaud ON|OFF                                             Default: OFF
**Mode:** Command                                               Host: Ab
**Parameters:**
- ON  -     Autobaud Routine always present at Power-ON or RESTART.
- OFF -     Autobaud Routine active at Power-ON only if battery jumper is removed.
**Description:**
When AUTOBAUD is OFF (default), the unit performs the autobaud function only  when powering ON or after a RESET.  
When AUTOBAUD is ON, the PK-232 performs the  autobaud routine EVERY time it is powered ON, and EVERY time the RESTART command  is entered.  
The stored parameters (e.g. MYCALL) are saved if the battery jumper  is connected.  The unit displays the autobaud message at the same rate as the  last setting of TBAUD.  
AUTOBAUD ON is helpful when moving the unit from one  computer to another, where the terminal data rates are different. 
In the autobaud routine, only one asterisk (*) is needed to set the terminal  speed TBAUD.  The autobaud routine detects 110, 300, 600, 1200, 2400, 4800 and  9600 baud, at either 7 bits even parity, or 8 bits no parity.


### AWlen "n"                                                   Default: 7
**Mode:** All                                                   Host: AW
**Parameters:**
- "n"  -    7 or 8 specifies the number of data bits per word.
**Description:**
The parameter value defines the digital word length used by the serial  input/output (I/O) terminal port and your computer or terminal program. 
AWLEN will probably be set properly by the PK-232 Autobaud routine.  
Still  you may want to change the ASCII word-length at some time to accommodate a  terminal program you wish to use. 
For plain text conversations with the PK-232, an AWLEN of 7 or 8 may be used.   
For binary file transfers and HOST Mode operation, an AWLEN of 8 MUST be used. 
The RESTART command must be issued before a change in word length takes effect. 
Do NOT change AWLEN unless the terminal can be changed to the same setting.

### CALibrate                                              		Immediate Command
**Mode:** Command                                          		Host: Not Supported
**Description:**
CALIBRATE is an immediate command that starts the AFSK transmit tone calibration. The PK-232 provides a continuous on-screen display of AFSK generator tone  frequencies in Hertz.  
The frequency is displayed approximately twice per  second, with the part number of the potentiometer associated with that tone. 
When Calibration is checked all packet connections will be lost, and the time- of-day clock will not advance until you quit the calibration routine. 
Commands available in the calibration routine are: 
K         Toggles the PK-232's PTT and CW keying outputs between ON and OFF. 
Q         Quits the calibration routine. 
H         Toggles the generator between wide (1000 Hz) and narrow (200 Hz) shift. <SPACE>   Toggles the audio tone between "mark" (low) and "space" (high) tones. 
D         Toggles between transmitting a continuous tone or alternating the mark  and space tones at a rate set by the radio baud (HBAUD) rate. 

FREQUENCY      RESISTOR                 FUNCTION 
2310           R164             HF SPACE, RTTY SPACE
2200           R165             VHF SPACE, WIDESHIFT SPACE
1200           R167             VHF MARK, CW AFSK, WIDESHIFT MARK This tone should be adjusted before R168
2110           R168             HF MARK, RTTY MARK



### CANline "n"                                       			Default: $18 <CTRL-X>
**Mode:** All                                         			Host: CL
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The parameter "n" is the ASCII code for the character you want to use to cancel  an input line.  You can enter the code in either hex or decimal. 
When you use the CANLINE character to cancel an input line in Command Mode, the  line is terminated with a <BACKSLASH> character and a new prompt (cmd:) appears. 
When you cancel lines in Converse Mode, only a <BACKSLASH> and a new line appear. 
	o    You can cancel only the line you are currently typing. 
	o    Once <CR> or <Enter> has been typed, you cannot cancel an input line. 
	
	NOTE:     If your send-packet character is not <CR> or <Enter>, the cancel-line  character cancels only the last line of a multi-line packet.

### CANPac "n"                                        			Default: $19 <CTRL-Y>
**Mode:** Packet, Command                             			Host: CP
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The parameter "n" is the ASCII code for the character you want to type in order  to cancel an input packet or to cancel display output from the PK-232. 
You can only cancel the packet that is being entered in CONVERSE Mode.  When you  cancel a packet, the line is terminated with a <BACKSLASH> and a new line.  
You  must cancel the packet before typing the send-packet character. In the COMMAND mode, this character cancels displayed output from the PK-232. 
Typing this character once cancels ALL output from the PK-232 to your display.   Typing the cancel-output character again restores normal output.


### CBell ON|OFF                                                Default: OFF
**Mode:** Packet/AMTOR                                          Host: CU
**Parameters:**
- ON   -    Three BELL characters <CTRL-G> ($07) are sent to your terminal with 
- the "*** CONNECTED to or DISCONNECTED from (call sign)" message.
- OFF  -    BELLS are NOT sent with the CONNECTED or DISCONNECTED message.
**Description:**
Set CBELL ON if you want to be notified when someone connects to or disconnects  from your station in Packet, or upon establishing a link in AMTOR.


### CMdtime "n"                                       			Default: 10 (1000 msec.)
**Mode:** All                                         			Host: CQ
**Parameters:**
- "n"  -    0 to 250  specifies TRANSPARENT Mode time-out value in 100-millisecond 
- intervals.
- If "n" is 0 (zero), exit from Transparent Mode requires sending the 
- BREAK signal or interruption of power to the PK-232.
**Description:**
CMDTIME sets the time-out value in Transparent Mode.  A guard time of "n" times  10 seconds allows escape to Command Mode from Transparent Mode, while permitting  any character to be sent as data. 
The same Command Mode entry character COMMAND (default <CTRL-C>) is used to exit Transparent Mode, although the procedure is different than from Converse mode. 
Three Command Mode entry characters must be entered lose than On'. times 10 seconds apart, with no intervening characters, after a delay of 'n' times 10 seconds following the last characters typed. 
The following diagram illustrates this timing: see manual


### COMmand "n" 												Default: 03 <CTRL-C>
**Mode:** All 													Host: CM
**Parameters:**
- "n"        0 to $7F (O to 127 decimal) specifies an ASCII character code.
**Description:**
COMMAND changes the Command Mode entry character (default <CTRL-C>).  Type the COMMAND character to enter Command Made from the Converse or Transparent Mode. 
The Command prompt (cmd:) appears, indicating successful entry to Command Mode. See the CKDTIME command.


### CONmode CONVERSE/TRANS 										Default: CONVERSE
**Mode:** Packet, Pactor 										Host: CB
**Parameters:**
- CONVERSE- Your PK-232 enters Converse Mode when a connection in established.
- TRANS - Your PK-232 enters Transparent when a connection is established.
**Description:**
CONMODE selects the mode your PK-232 uses after entering the CONNECTED state. For most operation, setting CONMODE to CONVERS (default) is most natural.


### CONVerse                 ( K for short )               		Immediate Command
**Mode:** All                                              		Host: Not Supported
**Description:**
CONVERSE is an immediate command that causes the PK-232 to switch from the  Command Mode into the Converse Mode.  
The letter "K" may also be used. Once the PK-232 is in the Converse Mode, all characters typed from the keyboard  are processed and transmitted by your radio.  
To return the PK-232 to the  Command Mode, type the Command Mode entry character (default is <CTRL-C>).


### CUstom "n"                                                  Default: $0A15
**Mode:** All                                                   Host: Cu
**Parameters:**
- "n"  -    0 to $FFFF (0 to 65,535 decimal) specifies a four digit hexadecimal 
- value, where each bit controls a different function described below.
**Description:**
The CUSTOM command was originally introduced to allow specialized features for  "Custom" applications to be added to the PK-232 without burdening all users with  a plethora of commands.  
As the CUSTOM command is quickly filling up, the  command UBIT has been added to replace CUSTOM allow for more features.  
The  CUSTOM command is retained for compatibility, but we recommend using the UBIT  command as it is more flexible and easier to use. 
For those applications that can not take advantage of the UBIT command, 
the  following CUSTOM features are available in this release of the PK-232 MBX. 

Bit 0, position $0001:   If bit 0 is set to 1 (default), the PK-232 will discard  a received packet if the signal is too weak to light  the DCD LED.  If set to 0, packets will be received  regardless of the Threshold control setting. 
Bit 1, position $0002:   If bit 1 is set to 0 (default), then setting the  MONITOR command to either ON or YES will result in a  MONITOR value of 4.  If bit 1 is set to 1, then MONITOR  ON or YES will force the MONITOR value to 6. 
Bit 2, position $0004:   If bit 2 is set to 1 (default), a break on the RS-232  line will put the PK-232 into the Command mode  (except from Host Mode).  If set to 0, a break on the  RS-232 line will not affect the PK-232. 
Bit 3, position $0008:   If bit 3 is set to 0 (default), packet channel numbers  will be numbered from 0-9.  If bit 3 is set to one,  then packet channel numbers are labeled A-J or a-j. 
Bit 4, position $0010:   This bit affects only Baudot transmit.  If bit 4 is set  to 1 (default), the PK-232 inserts the FIGS character  after a space, just prior to sending any figures,  <space><FIGS><number>.  This permits any receiving  station to correctly decode groups of figures  regardless of the USOS setting.  If bit 4 is set to 0,  the PK-232 will NOT insert FIGS characters after each  space.  MARS operators may want to set this bit to 0  for literal operation. 
Bit 5, position $0020:   If bit 5 is set to 0, (default) the PK-232 will always  power up in the Command mode.  If bit 5 is set to 1,  then the PK-232 will remain in the previous mode, i.e.,  converse, command, or transparent Mode. 
Bit 6, position $0040:   If bit 6 is set to 0, (default), then monitoring is  disabled in the Transparent mode.  If bit 6 is set to  1, then monitoring is active in the transparent mode.   MFROM, MTO, MRPT, MONITOR, MCON, MPROTO, MSTAMP,  CONSTAMP, and MBX are all active.
Bit 7, position $0080:   If bit 7 is set to 0 (default), the PK-232 prints  the ..-- Morse character as ^.  If bit 7 is set to 1,  the PK-232 decodes ..-- as a carriage return. 
Bit 8, position $0100:   If bit 8 is set to 0 (default), MORSE will configure  the PK-232 filters for CW as before.  If bit 8 is set  to 1, MORSE configures the filters for FSK (two-tone)  operation, in both receive and transmit.  WIDESHFT,  RXREV and TXREV are active. 
Bit 9, position $0200:   Bit position 9 does for AMTOR what WRU does for Baudot  and ASCII.  If bit 9 is set to 1 (default), a FIGS-D in  ARQ causes the unit to become the ISS, transmit the AAB  string, then revert back to the IRS.  If bit 9 is set  to 0, then a received FIGS-D will have no effect. 
Bit 10, position $0400:  If bit 10 is set to 0 (default), then host polling is  as before.  If bit 10 is set to 1, then any change in  status (e.g. Idle to Tfc) in AMTOR, FAX, TDM or NAVTEX  causes the PK-232 to issue the following host block: SOH  $50  n  ETB where n is $30-36, the same number that the OPMODE  command furnishes.  This block is subject to HPOLL. 
Bit 11, position $0800:  If bit 11 is set to 1 (default), a connected message  appears when an ARQ link is first established using  seven-character SELCALLs (CCIR 625).  If bit 11 is set  to 0, no connected message appears at the beginning of  ARQ communications. 
Bit 12, position $1000:  If bit 12 is set to 0 (default), the Packet Morse ID  (MID) is on/off keying of the low tone.  If bit 12 is  set to 1, the Morse ID is sent in 2-tone FSK with the  low tone being key-down and the high tone representing  key-up.  Use this setting to keep other stations from  sending a packet during the Morse ID. 
Bit 13, position $2000:  If bit 13 is set to 0 (default), MailDrop connect  status messages are always sent to the local user,  regardless of the setting of MDMON.  If bit 13 is set  to 1, remote user dialog and connect status messages  with the MailDrop are shown only if MDMON is ON. 
Bit 14, position $4000:  If bit 14 is set to 0 (default), the transmit buffer  for data sent from the computer to the PK-232 in packet  mode is limited only by available PK-232 memory.  If  bit 14 is set to 1, the serial flow control will permit  only a maximum of 7 I-frames to be held by the PK-232  before transmission.  This solves a problem with the  YAPP binary file transfer program which relies on a  small TNC transmit buffer to operate correctly. 
Bit 15 is unused at the present time.  

To return CUSTOM to the default setting,  type CU Y or CU ON at the command prompt.


### CWid "n"                                          			Default: $06 <CTRL-F>
**Mode:** Baudot/ASCII RTTY/AMTOR/FAX                 			Host: CW
**Parameters:**
- The CWID command lets you change the "send CWID" control character typed at the 
- end of your Baudot and ASCII RTTY dialogue.
- When the PK-232 reads this character embedded in the text or keyboard input, it 
- switches modes and sends your call sign in Morse code, at the keying speed set 
- by MSPEED.  As soon as your call sign has been sent in Morse, the PK-232 turns 
- off your transmitter and returns to receive.
**Description:**



### DAYStamp ON|OFF                                             Default: OFF
**Mode:** All                                                   Host: DS
**Parameters:**
- ON   -    The DATE is included in CONSTAMP and MSTAMP.
- OFF  -    Only the TIME is included in CONSTAMP and MSTAMP.
**Description:**
DAYSTAMP activates the date in CONSTAMP and MSTAMP.  
Set DAYSTAMP ON when you  want a dated record of packet channel activity, or when you're unavailable for  local packet operation.


### DAytime date & time                                         Default: none
**Mode:** All                                                   Host: DA
**Parameters:**
- date & time -  Current DATE and TIME to set.
**Description:**
DAYTIME sets the PK-232's internal clock current date and time.  The date & time  is used in many modes and should be set when the PK-232 is powered up. 
The clock is not set when the PK-232 is turned on.  
The DAYTIME command displays  the "?clock not set" error message until it is set as follows: yymmddhhmm[ss]         (spaces and punctuation are allowed) 
Example:  cmd:daytime 9003090659 where: 
yy is the last two digits of the year 
mm is the two-digit month code (01-12) 
dd is date (01-31) hh is the hour (00-23) 
mm is the minutes after the hour (00-59) 
[ss] is the optional seconds 
	o    Optionally the Dallas Semiconductor DS-1216C SmartWatch may be added to the  PK-232.  
	To install this IC carefully remove the 32K RAM IC and install the  SmartWatch in the RAM socket.  
	Then re-install the RAM IC in the socket  provided by the SmartWatch.


### DCdconn ON|OFF                                              Default: OFF
**Mode:** Packet/AMTOR KISS and RAWHDLC                         Host: DC
**Parameters:**
- ON   -    RS-232 cable Pin 8 follows the state of the CON (or DCD) LED.
- OFF  -    RS-232 cable Pin 8 is permanently set high (default).
**Description:**
DCDCONN defines how the DCD (Data Carrier Detect) signal affects pin 8 in the  RS-232 interface to your computer or terminal.  
Some programs such as PBBS  software require that DCDCONN be ON. DCDCONN also works in the RAWHDLC and KISS Modes.  
In RAWHDLC and KISS Modes, no  packet connections are known to the PK-232.  When DCDCONN is ON, the state of  the radio DCD is sent to the RS-232 DCD pin (pin-8).  
This may be necessary to  some host applications that need to know when the radio channel is busy.


### DELete ON|OFF                                               Default: OFF
**Mode:** All                                                   Host: DL
**Parameters:**
- ON   -    The <DELETE> ($7F) key is used for editing your typing.
- OFF  -    The <BACKSPACE> ($08) key is used for editing your typing.
**Description:**
Use the DELETE command to select the key to use for deleting while editing. Set DELETE OFF (default) if you wish to use the <Backspace> key to edit typing  mistakes.  Set DELETE ON if you wish to use the <Delete> key to edit mistakes. See the BKONDEL command controls how the PK-232 indicates deletion.



### DISPlay [class]                                        		Immediate Command
**Mode:** Command                                          		Host: Not Supported
**Parameters:**
- class -   Optional parameter identifier, one of the following: 
- (A)sync       display asynchronous port parameters 
- (B)BS         display AMTOR and Packet MailDrop parameters
- (C)haracter   display special characters 
- (F)ax         display Facsimile parameters
- (I)d          display ID parameters 
- (L)ink        display link parameters 
- (M)onitor     display monitor parameters 
- (R)TTY        display RTTY parameters
- (T)iming      display timing parameters 
- (Z)           display the entire command/parameter list 
**Description:**
DISPLAY is an immediate command.  When DISPLAY is typed without a parameter, the  PK-232 responds with a short list of often used parameters.


### EAS ON|OFF                                                  Default: OFF
**Mode:** Baudot, ASCII, AMTOR and MORSE                        Host: EA
**Parameters:**
- ON    -   Echo characters when actually sent on the air by the PK-232.
- OFF   -   Echo characters when sent to the PK-232 by the computer.
**Description:**
The ECHO-AS-SENT (EAS) command functions in all modes except packet.  EAS  lets you to choose the way data is displayed on your monitor screen or printer. 
To display your typing exactly as you are typing the keyboard characters or  sending from a disk file, set EAS "OFF" (default).  
To see the actual data being  sent from your PK-232 to your radio and transmitted on the air, set EAS "ON". 
If EAS is ON in AMTOR Mode A (ARQ), you'll see characters echoed on your screen  only after the distant station has validated (Ack'd) your block of three  characters.


### Echo ON|OFF                                                 Default: ON
**Mode:** All                                                   Host: EC
**Parameters:**
- ON   -    Characters received from the terminal ARE echoed by the PK-232.
- OFF  -    Characters are NOT echoed.
**Description:**
The ECHO command controls local echoing by the PK-232 when in Command or  Converse Mode.  Local echoing is disabled in Transparent Mode. 
	o    Set ECHO ON (default) if you don't see your typing appear on your display. 
	o    Set ECHO OFF if you see each character you type doubled. ECHO is set correctly when you see the characters you type displayed correctly.

### ERrchar "n"                                            		Default: $5F (_)
**Mode:** AMTOR, Morse, NAVTEX and TDM                     		Host: ER
**Parameters:**
- "n"   -   A hexadecimal value from $00-$7F used to denote the error character 
- used by the PK-232 for Morse, ARQ, FEC, NAVTEX and TDM.
**Description:**
n is a hex value $00-7F, default $5F (underscore).  This is the character that  the PK-232 displays when it receives a mutilated character in Morse, ARQ,  FEC, NAVTEX or TDM.  
The user may wish to set this character to $2A (asterisk),  $07 (bell), $20 (space) or $00 (null).  ERRCHAR ON or ER Y restores the default. 


### EScape ON|OFF                                               Default: OFF
**Mode:** All                                                   Host: ES
**Parameters:**
- ON   -    The <ESCAPE> character ($1B) is output as "$" ($24).
- OFF  -    The <ESCAPE> character is output as <ESCAPE> ($1B) (default).
**Description:**
The ESCAPE command selects the character to be output when an <ESCAPE> character  is to be sent to the terminal.  
The ESCAPE character selection is provided  because some computers and terminals interpret the <ESCAPE> character as a  special command.  
Set ESCAPE ON if you have an <ESCAPE> sensitive terminal to  avoid unexpected results from accidentally receiving this character.


### FEc                                                    		Immediate Command
**Mode:** AMTOR Mode B                                     		Host: FE
**Description:**
FEC is an immediate command that starts an AMTOR FEC (Mode B) transmission. Use FEC for CQ calls in AMTOR.  
Be sure to include your SELCALL and MYIDENT code  in your CQ message so that the distant station can call you back in ARQ. 
FEC is necessary for all round table AMTOR contacts.  When operating in FEC, let  your PK-232 begin each transmission with three to five seconds of idling.  
The  RTTY practice of transmitting a line of RYRYRY is unnecessary on FEC. 
You can signify the end of your FEC transmission by typing the changeover sign  "+?," internationally recognized as the RTTY equivalent of "KKK."  
However, in  FEC, "+?" is not a software command.  You still have to un-key your transmitter  (with the RECEIVE or CWID characters or the RCVE command) as you would in RTTY.

### Flow ON|OFF                                                 Default: ON
**Mode:** All                                                   Host: FL
**Parameters:**
- ON   -    Type-in flow control IS active.
- OFF  -    Type-in flow control is NOT active.
**Description:**
When FLOW is ON (default), any character typed on your keyboard causes output  from the PK-232 to the terminal to stop until any of the following occurs: 
	o    A packet is sent (in Converse Mode) 
	o    A line is completed (in Command Mode) 
	o    The packet length (See PACLEN) is exceeded 
	o    The current packet or command line is canceled 
	o    The redisplay-line character is typed 
	o    The logical packet channel is changed Setting FLOW ON prevents received data from interfering with your keyboard data  entry.  
	
When FLOW is OFF, data is sent to the terminal whenever it is available.


### FREe 														Immediate Command
**Mode:** All 													Host: FZ
**Description:**
Typing "FREE" displays the number of usable bytes left in the MailDrop, as in "FREE 3724." This may be useful to a Host mode application using the MailDrop.


### HEReis "n"                                        			Default: $02 <CTRL-B>
**Mode:** Baudot, ASCII and AMTOR                     			Host: HR
**Parameters:**
- "n"   -   Is the hex representation ($01-$7F) of the character that causes the 
- AAB string to be sent in the middle of transmitted text.
**Description:**
If you wish to send your own AAB string for identification during a transmission  simply enter the HEREIS character (default <CTRL-B>).  Also see the command AAB.


### HOMebbs call                                                Default: (none)
**Mode:** Packet/MailDrop                                       Host: HM
**Parameters:**
- call  -   Call Sign of your HOME BBS with which you have made prior arrangements 
- to Auto-Forward.
**Description:**
This is the Call Sign of your local or HOME BBS that you will use for Reverse  Forwarding messages.  
You must make special arrangements with the system  operator of this BBS to set you up for Reverse Forwarding.  
The SSID is not  compared when matching HOMEBBS to the source call sign of an incoming packet.


### HOST "n"                                                    Default: 0
**Mode:** All                                                   Host: HO
**Parameters:**
- "n"   -   A hexadecimal value from $00 through $FF setting bits from the table 
- below that define the Host operation of the PK-232.
**Description:**
The HOST command enables the "computer-friendly" HOST communications mode, over  the PK-232's RS-232 link.  
To cancel HOST mode, send 3-<CTRL-C> characters as if  exiting the Transparent mode, or type <CTRL-A> O H O N <CTRL-W>.  
Sending a  Break signal will not cause the PK-232 to exit from the HOST mode. 

Bit 0:    Controls whether the HOST mode is ON or OFF. 

			If bit 0 is equal to 0, HOST is OFF. 
			
			If bit 0 is equal to 1, HOST is ON. 
			
Bit 1:    Controls the local MailDrop access. 

			If bit 1 is equal to 0, then the Maildrop Send data uses the $20 block. Read data uses the the $2F block as  before.  
									Monitored MXMIT data uses the $3F  (monitored receive) block type. 
															
			If bit 1 is equal to 1, then the MailDrop send data uses the $60 block. Read data uses the $70 block type.   
									Monitored MXMIT data uses the $2F (echoed)  block type to differentiate between monitored  transmitted and received frames. 
									
Bit 2:    Controls the DSP-2232's extended HOST Mode. 

Bits 3-7 are reserved for future use. 

To maintain backward compatibility with older programs written to use the ON/OFF  form of the HOST command, HOST ON is equivalent to HOST $01 described above. 
However programmers must note that HOST now returns a numeric value and not ON  or OFF as before. See Timewave's PK-232 Technical Manual for full information on Host Mode.



### HPoll ON|OFF                                                Default: ON
**Mode:** Host                                                  Host: HP
**Parameters:**
- ON   -    The HOST Mode program must poll the PK-232 for all data (default).
- OFF  -    The HOST Mode program must accept data from the PK-232 at anytime.
**Description:**
When HPOLL is ON (default) the HOST Mode program must poll the PK-232 (using the  <CTRL-A> O G G <CTRL-W>) for all data that might be available to be displayed to  the screen.  
When HPOLL is OFF, the HOST Mode program must be able to accept any  data from the PK-232 whenever it becomes available.


### Id                                                     		Immediate Command
**Mode:** AMTOR/ASCII/Baudot/Packet                        		Host: ID
**Description:**
In AMTOR, the ID command acts like the RCVE command, only adding a Morse ID  before going back to receive.  
In ASCII and Baudot, the ID command causes a CW  ID to be sent much like an immediate version of the CWID character (CTRL-F).   
Because the ID command is immediate, the message "Transmit Data Remaining" will  be displayed if any unsent data remains in the transmit buffer. 
In Packet, ID is an immediate command that sends a special identification  packet.  
The ID command allows you to send a final identification packet when  you're taking your station off the air.  HID must also be set ON.  
The  identification consists of a UI-frame, with its data field containing your  MYALIAS (if any) and your MYCALL and the word "digipeater".  
The ID packet is  sent only if your PK-232 has digipeated any transmissions since the last  automatic identification.


### ILfpack ON|OFF                                              Default: ON
**Mode:** Packet                                                Host: IL
**Parameters:**
- ON   -    The PK-232 ignores all line-feed characters received from the terminal.
- OFF  -    The PK-232 transmits all line-feeds received from the terminal.
**Description:**
The ILFPACK command permits you to control the way the PK-232 handles line-feed  characters received from your computer or terminal while in the Packet mode.


### IO ["n"]                                               		Default: none
**Mode:** All                                              		Host: IO
**Parameters:**
- "n"   -   A hexadecimal value used to access the PK-232's memory and I/O 
- locations, or read values stored at a specified ADDRESS.
**Description:**
The IO command works with the ADDRESS command (ADDRESS $aabb) and permits access  to memory and I/O locations.  
Use the IO command without arguments to read an  I/O location, and with one argument $0 to $FF to write to an I/O location.  
The  value in ADDRESS is not incremented after using the IO command. 
In ADDRESS $aabb, where "aa" (01-FF) is the device address, and "bb" is the  register address on the device. 
If ADDRESS is set to $00bb, the IO command reads or writes data to the device at  I/O address bb.  
There is no register set-up before the access.  This command is  used primarily as a programmer's aid and is not needed for normal PK-232 use.


### KILONFWD  ON|OFF                                            Default: ON
**Mode:** Packet/MailDrop                                       Host: KL
**Parameters:**
- ON    -   The PK-232 kills messages after they have been Reverse Forwarded.
- OFF   -   The PK-232 does not kill messages after Reverse Forwarding.
**Description:**
Controls the disposition of a message that has been Reverse Forwarded to the  station whose call is in HOMEBBS.  
If KILONFWD is ON (default), the message is  killed automatically to make room for other messages.  
If KILONFWD is OFF, the  message's status is changed from "F" to "Y."


### LAstmsg  "n"                                           		Immediate Command
**Mode:**  Packet MailDrop                                 		Host: LA
**Parameters:**
- "n"   -   0 to 999 specifies the message number of the last MailDrop message.
**Description:**
The number 0-999 is the number of the last message sent by a remote user or the  SYSOP to the MailDrop.  
This command is handy for checking the last message sent  to your MailDrop system.  
The LASTMSG command also allows the MailDrop message  counter to be set to any value, or simply reset by setting LASTMSG 0.


### Lock                                                   		Immediate Command
**Mode:**  Morse/Baudot/AMTOR/FAX                          		Host: LO
**Description:**
AMTOR and Baudot:   LOCK is an immediate command used to force a LETTERS shift  in the received data.  
This can be helpful if noise has garbled the LTRS  character causing FIGURES to be displayed. 
FAX:      This is a manual start command for FAX.  
Normally the transmitting  station starts a FAX image with sync pulses so that the receiver automatically  lines up with the edge of the paper.  
If you tune in a signal too late, or there  is so much noise that the sync pulses are not detected, you can start reception  manually with the LOCK command.  
If you issue a LOCK to the PK-232, you will  probably need to use the JUSTIFY command to properly align the image. 
Morse:    LOCK is an immediate command that instructs the PK-232 to lock its  timing to the current measured speed of a Morse signal.  
The LOCK command may  improve the PK-232's ability to decode CW signals in the presence of high noise  levels.



### MARsdisp ON|OFF                                             Default: OFF
**Mode:** Baudot and AMTOR, RTTY                                Host: MW
**Parameters:**
- ON   -    The PK-232 translates received LTRS characters to a <CTRL-O>, and 
- FIGS characters to a <CTRL-N> and sends these to the terminal.
- OFF  -    The PK-232 operates as before in Baudot and AMTOR (default).
**Description:**
The MARSDISP command permits the Baudot and AMTOR operator to detect and display  every character including LTRS and FIGS sent by the other station.  
The ACRDISP  and ALFDISP may be turned off to prevent extraneous carriage-returns and  Linefeeds from being sent to the display.  
If this data is retransmitted,  ACRRTTY should be 0, and ALFRTTY should be OFF.  The <CTRL-O> and <CTRL-N>  characters will send LTRS and FIGS respectively.



### MDCheck                                                		Immediate Command
**Mode:** AMTOR Packet/MailDrop                            		Host: M1
**Description:**
MDCHECK is an immediate command which allows you to log on to your own MailDrop. 
After logging on, you can EDIT, LIST, READ, SEND or KILL MailDrop messages. 
To use the MDCHECK command, and your PK-232 must not be connected to or linked  to any packet or AMTOR stations.  
For monitoring purposes, local access of the  MailDrop is considered a connection.  Type "B" (BYE) to quit local control of  your MailDrop.


### MDMon ON|OFF                                                Default: OFF
**Mode:** AMTOR and Packet/MailDrop                             Host: Mm
**Parameters:**
- ON    -   Monitor a calling station's activity on your MailDrop.
- OFF   -   Normal monitoring as determined by the monitoring mode commands. 
**Description:**
Set MDMON to ON to monitor activity on your MailDrop. MDMON permits you to monitor activity on your AMTOR or packet MailDrop showing  you both sides of the QSO.  
Packet headers are not shown while a caller is  logged on.  
When no one is connected to your MailDrop, channel activity is  monitored according to the setting of MONITOR. Set MDMON OFF to cancel MailDrop monitoring.  
Note that MailDrop connect and  link status messages will be displayed even with MDMON OFF.  
These status  messages are important and allow you to see who is connected to your MailDrop.   
They can be disabled however with the UBIT 13 command.  See the UBIT command for  more information .


### MDPrompt text                                          		Default: (see text)
**Mode:** Packet/MailDrop                                  		Host: Mp
**Parameters:**
- text  -   Any combination of characters and spaces up to a maximum of 80 bytes.
**Description:**
MDPROMPT is the command line sent to a calling station by your MailDrop in  response to a Send message command.  
The default text is: "Subject:/Enter message, ^Z (CTRL-Z) or /EX to end" Text before the first slash is sent to the user as the subject prompt; text  after the slash is sent as the message text prompt.  
If there is no slash in the  text, the subject prompt is "Subject:" and the text prompt is from MDPROMPT.

### MEmory "n"                                                  Default: none
**Mode:** All                                                   Host: MM
**Parameters:**
- "n"   -   A hexadecimal value used to access the PK-232's memory locations, or 
- read values stored at a specified ADDRESS.
**Description:**
The MEMORY command works with the ADDRESS command (ADDRESS $aabb) and permits  access to memory locations.  
Use the Memory command without arguments to read a  memory, and with one argument $0 to $FF to write to a memory location.  
The  value in ADDRESS is incremented after using the MEMORY command.

### MFIlter n1[,n2[,n3[,n4]]]                                   Default: $80
**Mode:** Morse, Baudot ASCII, AMTOR and Packet                 Host: MI
**Parameters:**
- "n"  -    0 to $80 (0 to 128 decimal) specifies an ASCII character code.
- Up to four characters may be specified separated by commas.
**Description:**
Use MFILTER to select up to 4 characters to be "filtered," or excluded from  Morse, Baudot, ASCII, AMTOR and monitored packets.  
Parameters "n1," - "n4" are  the ASCII codes for the characters you want to filter.  
The special value of $80  (default) filters all characters above $7F and all control-characters except  carriage-return ($0D), linefeed ($0A), and TAB ($09).


### MHeard                                                 		Immediate Command
**Mode:** Packet/AMTOR MailDrop                            		Host: MH
**Description:**
MHEARD is an immediate command that displays a list of up to 18 most recently  heard stations.  
Stations that are heard directly are marked with a * in the  heard log.  Stations that have been repeated by a digipeater are not marked. 
When DAYTIME has been set, entries in the heard log are time stamped.  When  DAYSTAMP is ON the date is also shown.  
An example of the MHEARD display is  shown below: 
DAYSTAMP ON                             DAYSTAMP OFF 
05-Jul-86  21:42:27  WA1FJW             21:42:27  WA1FJW 
05-Jul-86  21:42:24  WA1IXU*            21:42:24  WA1IXU* 
Clear the MHEARD list with a "%", "&", "N," "NO," "NONE" or "OFF" as arguments.


### MMsg ON|OFF                                                 Default: OFF
**Mode:** Packet/AMTOR MailDrop                                 Host: MU
**Parameters:**
- ON  -     The stored MTEXT message is sent as the first response after an AMTOR 
- link or Packet connect to the MailDrop is established.
- OFF -     The MTEXT message is not sent at all.
**Description:**
MMSG enables or disables automatic transmission of the MTEXT message when your  AMTOR or Packet MailDrop links with another station.


### MTExt text                                        			Default: See sample
**Mode:** AMTOR/Packet MailDrop                       			Host: Mt
**Parameters:**
- text      Any printable message up to a maximum of 120 characters.
**Description:**
MTEXT is the "MailDrop automatic answer" text similar to CTEXT.  If MMSG is ON,  the MTEXT message is sent when a station links to your AMTOR or Packet MailDrop. 
The default text is: "Welcome to my AEA PK-232M maildrop. Type H for help." MTEXT can be cleared with a "%", "&", "N," "NO," "NONE" or "OFF" as arguments.

### MYcall call [-"n"]                                     Default: PK232
**Mode:** Packet                                           Host: ML
**Parameters:**
- call  -   Your call sign
- "n"   -   0 - 15, indicating an optional substation ID, (SSID)
**Description:**
Use the MYCALL command to load your call sign into your PK-232. The "PK232" default call sign is present in your PK-232's ROM when the system is  manufactured.  This "artificial call" must be changed for packet operation. Two or more stations cannot use the same call and SSID on the air at the same  time.  Use a different SSID if you have more than one packet station on the air. 

### MYMail call[-“n”] 											Default: none
**Mode:** Packet/MailDrop 										Host: Me
**Parameters:**
- Call    -      The Call Sign you wish to use for the MailDrop.
- “n”      -     Numeral indicating an optional substation ID (SSID) or extension.
- Call is the call sign of the MailDrop, default -none.
- “Call” may have an optional SSID, and must not be the same call sign and SSID as
- MYCALL.  If you do not set MYMAIL, the MailDrop will use the same call sign and
- SSID as entered in MYCALL.  For example, if you have set MYCALL to N7ML then
- MYMAIL may be N7ML-1 through N7ML-15.  You can use the CTEXT and MTEXT messages
- to inform other stations who connect of your MYCALL and MYMAIL call signs.


### MYPTcall callsign 											Default PK232
**Mode:** Pactor 												Host:   Mf
**Description:**
- Use the MYPTCALL command to load your call sign Into your PK-232.
- If you have not loaded a call into the PK-232 with MYPTCALL, the call loaded in
- MYCALL will be used.  The difference between MYCALL and MYPTCALL is that MYCALL
- allows only the dash (-) to be used while MYPTCALL will allow any punctuation
- with the call.
- If calls have not been loaded into either MYCALL or MYPTCALL, the PK-232 will not
- allow transmission on Pactor.  An error message “Need MYCALL" will be displayed
- if transmission is attempted.
- Example:
- MYPTCALL K6RFK/ZL <Enter>
- 
### NEwmode ON/OFF 												Default: ON
**Mode:**  All 													Host: NE
**Parameters:**
- ON — The PK-232 automatically returns to the Command Mode at disconnect.  
- OFF  — The PK-232 does not return to Command Mode at disconnect.
**Description:**
- Your PK-232 always switches to a data transfer mode at the time of connection,
- unless NOMODE is ON.  NEWMODE determines how your PK-232 behaves when the link is
- broken.
- When NEWMODE is ON (default) and the link is disconnected, or if the connect
- attempt fails, your PK-232 returns to Command Mode.  If NEWMODE is OFF and the
- link is disconnected, your PK-232 remains in Converse or Transparent Mode unless
- you have forced it to return to Command Mode.


### NOmode ON|OFF                                               Default: OFF
**Mode:** All                                                   Host: NO
**Parameters:**
- ON   -    The PK-232 switches modes only upon explicit command.
- OFF  -    The PK-232 changes modes according to NEWMODE.
**Description:**
When NOMODE is OFF (default), your PK-232 switches modes automatically according  to NEWMODE.  
When NOMODE is ON your PK-232 never switches from Converse or  Transparent Mode to Command Mode (or vice versa) by itself.  
Only specific  commands (CONVERSE, TRANS, or <CTRL-C>) typed by you change the operating mode.


### NUCr ON|OFF                                                 Default: OFF
**Mode:** All                                                   Host: NR
**Parameters:**
- ON   -    <NULL> characters ARE sent to the terminal following <CR> characters.
- OFF  -    <NULL> characters ARE NOT sent to the terminal following <CR>s. 
**Description:**
The NULLS command sets the number of <NULL> characters that will be sent. 
Some older printer-terminals require extra time for the printing head to do a  carriage return and line feed.  
NUCR ON solves this problem by making your  PK-232 send <NULL> characters (ASCII code $00) to your computer or terminal.

### NULf ON|OFF                                                 Default: OFF
**Mode:** All                                                   Host: NF
**Parameters:**
- ON   -    <NULL> characters are sent to the terminal following <LF> characters.
- OFF  -    <NULL> characters are not sent to the terminal following <LF>s.
**Description:**
Some older printer-terminals require extra time for the printing head to do a  carriage return and line feed.  
NULF ON solves this problem my making your  PK-232 send <NULL> characters (ASCII code $00) to your computer or terminal. 
The  NULLS command sets the number of <NULL> characters that will be sent.

### NULLs "n"                                              		Default: 0 (zero)
**Mode:** All                                              		Host: NU
**Parameters:**
- "n"  -    0 to 30 specifies the number of <NULL> characters to be sent to your 
- computer or terminal after <CR> or <LF> when NUCR or NULF are set ON.
**Description:**
NULLS specifies the number of <NULL> characters (ASCII $00) to be sent to the  terminal after a <CR> or <LF> is sent.  
NUCR and/or NULF must be set to indicate  whether nulls are to be sent after <CR>, <LF> or both.  
The null characters are  sent from your PK-232 to your computer only in Converse and Command Modes.


### Opmode                                                 		Immediate Command
**Mode:**  Command                                         		Host: OP
**Description:**
OPMODE is an immediate command that shows the PK-232's current mode of operation  and system status.  
Opmode also displays the MORSE speed when in the Morse mode. Use the OPMODE command at any time when your PK-232 is in the Command Mode to  display the present operating mode.  
Here is a typical example: cmd:o 
	OPmode   AScii      RCVE


### PACTor (PT for short) 										Immediate Command 
**Mode:** Command 												Host: Pt
**Description:**
Pactor is an immediate command that switches the PK-232 into the Pactor mode of operation.  
This mode is an option. Pactor is a mode of data communication that combines some of the features of both AMTOR and packet.  
The abbreviated command is PT.  It has both a linked mode called ARQ and a non-linked mode called unproto.  See Chapter 11 for details.

### PARity "n" 													Default: 3 (even)
**Mode:** All 													Host: PR
**Parameters:**
- “n”      — 0 to 3 selects a parity option from the table below.
- PARITY sets the PK-232's parity for RS-232 terminal according to the table below:
- 0 no parity 2 no parity
- 1 odd parity 3 even parity
- The parity bit, if present, is stripped automatically on input and is not checked
- in command and Converse Modes.  In Transparent Mode all eight bits (including
- parity) are transmitted.
- The change will not take effect until a RESTART is performed.  Be sure to change
- the computer or terminal to the same parity setting.
- 

### PASs  "n” 													Default: $16 <CTRL-V>
**Mode:** Packet, ASCII and Pactor 								Host:  PS
**Parameters:**
- 0 to $7F (O to 127 decimal) specifies an ASCII character code.
- PASS selects the ASCII character used for the "pass' input editing commands.  The
- parameter “n” is the ASCII code for the character used to pass editing characters
- (default <CTRL-V>).  The PASS character signals that the following character is
- to be included in a packet Pactor or ASCII text string.
- 

### PASSAll ON/OFF 												Default: OFF
**Mode:** Packet 												Host: PK
**Parameters:**
- ON -Your PK-232 will accept packets with valid or invalid  CRCS.
- OFF -Your PK-232 will accept packets with valid CRCs only.
- PASSALL turns off the PK-232's packet error-detecting mechanism and displays
- received packets with invalid CRCS.  PASSALL is normally turned OFF (default);
- which ensures that packet data la error-free by rejecting packets with invalid
- CRC fields.  When PASSALL is ON, packets are displayed, despite CRC errors.  The
- MHEARD logging is disabled since the call signs detected may be incorrect.



### PK [“n”] 													Default: none
**Mode:** All 													Host: PK
**Parameters:**
- "n"   - a hex number used to access the PK-232's memory and I/O
- locations.
- PK (Peek/Poke) permits access to memory locations.      To use the PK command:
- • Set the memory address into the ADDRESS command.
- • Use the PK command without arguments to read that memory location.
- • Use PK with one argument O-$FF to write to that memory location.
- PK-232 RAM locations are $8000-$FFFF.  ROM begins at $0000.  This command is used
- primarily as a programmer's aid and is not needed for normal PK-232 use.


### PT200 ON/OFF 												Default: ON
**Mode:** Pactor 												Host: PB
**Description:**
Pactor uses an adaptive data rate selection scheme.  The normal data rate is 100 baud.  If conditions permit, the data rate will be shifted to 200 baud automatically.  
If the error rate becomes too high at 200 baud the data rate will automatically be reduced to 100 baud.  
There can be conditions where the data rate is frequently shifting, causing a loss in the actual information data rate. 
The command PT200 when off, will prevent the data rate from automatically changing to 200 baud.  
When PT200 is ON (default), the PK-232 will allow automatic data rate selection.


### PTConn [!]aaaa(aa)                                      
**Mode:** Pactor  												Host: PG
**Parameters:**
- aaaa(aa) is the call sign of the Pactor station to be called
- PTConn is an immediate command that starts the Pactor connect protocol.  To start
- a Pactor connect, type "PIC' followed by the other station's call sign:
- Example: PTC N7ML <Enter>
- As soon as the <CR> is typed, the PK-232 will begin keying your transmitter on
- and off with the Pactor connect sequence.
- If you are connecting with a station via long path, i.e. more than half way
- around the world, use an exclamation mark before the callsign:
- Example:  PTC !N7ML <enter>
- This changes the Pactor timing to allow for the extended radio propagation time.


### PTHUFF “n” 													Default: 0
**Mode:** Pactor 												Host: PH
**Description:**
"n"   —    0 to 3, specifying a type of compression that may be used in Pactor. 
To enhance the effective data rate in Pactor, a data compression scheme called may be automatically enabled. 
0    - is no compression (default). 
1    - is Huffman encoding. 
2,3 - presently not implemented but reserved for future use. 

Instead of using the normal 8 bit ASCII representation of a character, Huffman encoding assigns each character a code that may be as few as 2 bits for the most used characters to as long as 15 bits for the least used characters.  
For English (and most other) languages, the use of Huffman compression results in a smaller number of bits necessary for a given message.  
For messages consisting of non- text information such as computer programs, Huffman compression would need more bits than ASCII and would be less efficient. 
If PTH is set off, Pactor will never use Huffman compression.  When PTH is set on, Huffman compression will be used if it is more effective.  
Do not use Huffman compression with binary file transfers as it only works with 7 bit data.


### PTList 														Immediate Command
**Mode:** Pactor 												Host: PN
**Description:**
PTList is an immediate command that switches your PK-232 into the Pactor listen mode. 
You can usually monitor a Pactor contact between two connected stations using the Pactor listen mode.  
Since your station is not part of the error free link, if the CRC check does not produce a correct check sum, nothing will be displayed. 
If the receiving station requests a repeat, and you have copied the packet, it will not print twice.



### PTOver “n” 													Default: <CTRL-Z> ($lA) 
**Mode:** Pactor 												Host: PV
**Description:**
“n”  — A hexadecimal value from $00 to $7F used to select the change-over character used in linked Pactor.  
The default is <CTRL-Z>. PTOver is the character, conventionally <CTRL-Z>, that is used to change the direction of data transmission in a linked Pactor operation.  
When you are finished transmitting information and you are ready to receive information from the other station, use the PTOver character.  Also see AChg.


### PTSend – “n, x” 											Default: 1,2
**Mode:** Pactor 												Host: PD
**Parameters:**
- ”n” - 1 or 2 selects the transmit baud rate.
- - 1 to 5 selects the number of times the data is repeated.
- PTS “n, x” initiates an unproto Pactor transmission.  To end the transmission,
- type <ctrl-D>.
- “n”
- 1 selects 100 baud,
- 2 selects 200 baud.
- In order to increase the probability of correct transmission, the unproto
- Pactor transmission sends the message data a selected number of times.
- The parameter x sets the number of times the data is sent.
- Example:
- PTS 1 3 <Enter> would start a 100 baud unproto transmission with the
- message data sent three times.
- The transmission may be started using the default, 100 baud, two repeats, by
- typing "PTS" without “n x.”
- Example:
- PTS <Enter>
**Description:**


### Rcve                                                   		Immediate Command
**Mode:** Baudot/ASCII/AMTOR/FAX/Morse                     		Host: RC
**Description:**
RCVE is an immediate command, used in Morse, Baudot, ASCII, ARQ, FEC and FAX  modes to switch your PK-232 from transmit to receive. 
o    You must return to the Command Mode to use the RCVE command. 

### RECeive "n"                                       			Default: $04 <CTRL-D>
**Mode:** Baudot/ASCII/Morse/AMTOR/FAX/Pactor                			Host: RE
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Parameter "n" is the numeric ASCII code for the character you'll use when you  want the PK-232 to return to receive. 
The RECEIVE command allows you to insert a character (default <CTRL-D>) in your  typed text that will cause the PK-232 to return to receive after all the text  has been transmitted.


### REDispla "n"                                      			Default: $12 <CTRL-R>
**Mode:** All                                         			Host: RD
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
REDISPLA changes the redisplay-line input editing character. Parameter "n" is the numeric ASCII code for the character you'll use when you  want to re-display the current input line. 
Type the REDISPLA character (default <CTRL-R>) to re-display a command or text  line you've just typed.  
This can be helpful when editing a line especially if  your terminal does not support <BACKSPACE>.  
It can also be used in Packet to  display a packet that might have been received while you were typing.  
A  <BACKSLASH> is appended to old line, and the corrected line is shown below it.


### RESET                                                  		Immediate Command
**Mode:** Command                                          		Host: RS
**Description:**
RESET is an immediate command that resets all parameters to PK-232's PROM  default settings and reinitializes the PK-232.  
All personalized parameters,  monitor lists and MailDrop messages will be lost.


### RESTART 													Immediate Command
**Mode:** Command 												Host:   RT
**Description:**
RESTART is an immediate command that reinitializes the PK-232 while retaining the user's settings.  
The effect of the RESTART command is the same as turning the PK-232 OFF, then ON again. RESTART does not reset the values in bbRAM.   
See the RESET command.


### STArt "n"                                         			Default: $11 <CTRL-Q>
**Mode:** All                                         			Host: ST
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use the START command to choose the user START character (default <CTRL-Q>) you  want to use to restart output FROM the PK-232 TO the terminal after it has been  halted by typing the user STOP character.  
See the XFLOW command. 

### STOp "n"                                          			Default: $13 <CTRL-S>
**Mode:** All                                         			Host: SO
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use the STOP command to select the user STOP character (default <CTRL-S>) you  will use to stop output FROM the PK-232 TO the terminal.  See the XFLOW command.


### TBaud "n"                                              		Default: 1200 bauds
**Mode:** All                                              		Host: TB
**Parameters:**
- "n"   -   Specifies the data rate in bauds, on the RS-232 serial I/O port.
**Description:**
TBAUD sets the baud rate you are using to communicate with the PK-232 from your  terminal or computer.  
Use TBAUD to set terminal rates not covered by the  autobaud routine, such as 110 and 600 bauds.  
Set TBAUD to specify the terminal  baud rate to be activated at the next power-on or RESTART.  A warning message  reminds you of this.  
Be sure you can set your terminal for the same rate. 
The TBAUD command supports the following serial port data rates of 45, 50, 57,  75, 100, 110, 150, 200, 300, 400, 600, 1200, 2400, 4800 and 9600 bauds.


### TClear                                                 		Immediate Command
**Mode:** Command                                          		Host: TC
**Description:**
The TCLEAR command clears your PK-232's transmit buffer and cancels any further  transmission of data when in the Baudot, ASCII, AMTOR or Morse operating modes.   
In Packet Mode, all data is cleared except for a few remaining packets. You must be in the Command Mode to use TCLEAR.


### TIme "n"                                          			Default: $14 <CTRL-T>
**Mode:** All                                         			Host: TM
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The TIME command specifies which control character sends the time-of-day in the  text you type into the transmit buffer or into a text file stored on disk. 
At transmit time, the PK-232 reads the embedded control code (default <CTRL-T>),  reads the time-of-day from the PK-232's internal clock and then sends the time  to the radio in the data transmission code in use at that time.  
If the DAYTIME  has not been set, and a control-T will cause the PK-232 to send an asterisk (*). When DAYSTAMP is set ON, the date is transmitted with the time. 
NOTE:  The TIME command cannot be embedded in CTEXT, BTEXT, MTEXT or AAB.


### TMail ON|OFF                                                Default: OFF
**Mode:** AMTOR                                                 Host: TL 
**Parameters:**
- ON    -   The PK-232MBX operates as a personal AMTOR BBS or MailDrop.
- OFF   -   The PK-232MBX only operates as a normal CCIR 476 or 625 controller. 
**Description:**
The PK-232's MailDrop is a personal mailbox that uses a subset of the  W0RLI/WA7MBL PBBS commands and is similar to operation of APLINK stations.  
When  TMAIL is ON and another station establishes an ARQ link with your MYSELCAL or  MYIDENT, the remote AMTOR station may leave messages for you or read messages  from you.  
Third-party messages are not accepted by your AMTOR MailDrop unless  3RDPARTY is ON. See the MDCHECK, TMPROMPT, MDMON, MTEXT, MMSG MYSELCAL and MYIDENT commands.

### TMPrompt text                                          		Default: (see text)
**Mode:** AMTOR/MailDrop                                   		Host: Tp
**Parameters:**
- text  -   Any combination of characters and spaces up to a maximum of 80 bytes.
**Description:**
TMPROMPT is the command line sent to a calling station by your AMTOR MailDrop in  response to a Send message command.  
The default text is: "GA subj/GA msg, '/EX' to end." Text before the first slash is sent to the user as the subject prompt; 
text  after the slash is sent as the message text prompt.  If there is no slash in the  text, the subject prompt is "SUBJECT:" and the text prompt is from TMPROMPT.


### Trans                                                  		Immediate Command
**Mode:** All                                              		Host: Not Supported
**Description:**
TRANS is an immediate command that switches the PK-232 switch from the Command  Mode to Transparent Mode.  The current state of the radio link is not affected. 
Transparent Mode is primarily useful for computer communications.  In  Transparent Mode "human interface" features such as input editing, echoing of  input characters, and type-in flow control are disabled. 
	o    Use Transparent Mode for transferring binary or other non-text files. 
	o    To exit the Transparent mode, type the COMMAND character (default <CTRL-C>)  three times within the time period set by CMDTIME (default 1 Second).

### TRFlow ON|OFF                                               Default: OFF
**Mode:** All                                                   Host: TW
**Parameters:**
- ON   -    Software flow control for the computer or terminal RECEIVING data is 
- activated in Transparent Mode.
- OFF  -    Software flow control for the computer or terminal RECEIVING data is 
- disabled in Transparent Mode.
**Description:**
When TRFLOW is ON, the type of flow control used by the computer RECEIVING data  in Transparent Mode is determined by how START and STOP are set. 
When TRFLOW is OFF, only "hardware" flow control (RTS, DTR) is available to the  computer RECEIVING data from the PK-232 in Transparent Mode. 
If TRFLOW is ON, and START and STOP are set to values other than zero, software  flow control is enabled for the user's computer or terminal.  
The PK-232  responds to the user START and user STOP characters while remaining transparent  to all other characters from the terminal



### UBit "n" ON/OFF                                             Default: 0
**Mode:** All                                                   Host: UB
**Parameters:**
- "n"  -    0 to 255 specifying a User BIT that may be set ON or OFF.
**Description:**
The UBIT is an extension of the CUSTOM command which allows up to 255 ON/OFF  functions to be added to the PK-232 without burdening users with a large number  of commands.  
The functions controlled by UBIT are not things that most users  will ever have to change.  
Still they are important enough to some users or  application programs that we have included them under the umbrella command UBIT.   
The following are examples of how to use the UBIT command: 
UBIT 5         Returns the present status of UBIT 5 
UBIT 1 ON      Sets the function controlled by UBIT 1 to ON 
UBIT 10 T      Toggles the state of the function controlled by UBIT 10 
UBIT           Returns the state of the last UBIT value that was accessed

Listed below are the UBIT functions and the default state that presently have been assigned.  The default state of each UBIT is always shown first.

UBIT 0: 	ON:  The PX-232 will discard a received packet if the signal is too weak to light the DCD LED. 
			OFF: The PK-232 will receive a packet regardless of the DCD status or the THRESHOLD control setting.

UBIT 1: 	OFF: Entering the command MONITOR ON or MONITOR YES causes the MONITOR command to be set to 4. 
			ON:  Entering the command MONITOR ON or MONITOR YES causes the MONITOR command to be set to 6.

UBIT 2:  	ON:  A Break signal received on the RS-232 line forces the PK-232 into Command mode from all modes except HOST mode. 
			OFF: A Break signal on the RS-232-line is ignored by the PK-232.

UBIT 3:  	OFF: Multiple connect Packet channels are numbd-red from 0-9. 
			ON:  Multiple connect Packet channels are numbered A-J.

UBIT 4:  	ON:  When transmitting in Baudot, the PK-232 inserts the FIGS after a space just prior to sending any figures space><FIGS><number>). This permits receiving stations to decode groups of figures correctly regardless of the USOS setting. 
			OFF: The PK-232 will not insert the FIGS character after each space. MARS operators may want to set UBIT 4 OFF for literal operation.

UBIT 5:   	OFF: The PK-232 will always power up in Command Mode. 
			ON:  The PK-232 will remain in the last mode Converse, Command or Transparent) provided the battery jumper enabled.

UBIT 6: 	OFF: In Packet, monitoring is disabled when in the Transparent mode. 
			ON:  Packet monitoring is active in the Transparent mode.  MFROM, MT0, MRPT, MONITOR, MCON, MPROTO, MSTAMP, MXMIT, CONSTAMP and MBX are all active. 
			
UBIT 7:   	OFF: In Morse receive, the character ..-- prints as a “^” 
			ON:  In Morse, the character ..-- prints as a <Carriage Return>.

UBIT 8: 	OFF: In the Morse mode, the PK-232's modem is configured for standard Al-A single-tone ON/OFF keyed Morse. 
			ON:  In the Morse mode, the PK-232's modem is configured for 2-tone FSK Morse operation on both transmit and receive.  WIDESHFT, RXREV and TXREV are all active.

UBIT 9:   	ON:  In AMTOR a received WRU character (FIGS-D) or in Pactor a  <CTRL-E> will cause the Auto-Answerback text to be sent regardless of the setting of WRU.  In AMTOR this is subject to the setting of the CODE command. 
			OFF: In AMTOR and Pactor a received WRU character will have no effect.

UBIT 10: 	OFF: polling in the HOST mode is subject to HPOLL and must be done for all changes in status. 
			ON:  Status changes (e.g., Idle to Tfc) in AMTOR, FAX, TDM or NAVTEX causes the PK-232 to issue the following host block: SOH $50 n ETB where n is $30-36, the same number that the OPMODE command furnishes. This block is subject to EPOLL.

UBIT 11: 	ON:  A "Connected" message appears when an AMTOR or Pactor ARQ link is first established using seven-character SELCALLs (CCIR 625). 
			OFF: No Connected message appears at the start of ARQ communications.

UBIT 12: 	OFF: The Packet Morse ID (MID) is ON/OFF keying of the low tone. 
			ON:  The Packet Morse ID is sent in 2-tone FSK with the low tone being key-down and the high tone representing key-up.  Use this setting to keep other stations from sending a packet during the Morse ID.

UBIT 13: 	OFF: MailDrop Connect status messages are always sent to the local user, regardless of the setting of MDMON. 
			ON:  Remote user dialog and Connect status messages with the MailDrop are shown only if MDMON is ON.

UBIT 14: 	OFF: In Packet, the transmit buffer for data sent from the computer to the PK-232 is limited only by available PK-232 memory. 
			ON:  In Packet, the serial flow control will permit only a maximum of 7 I-Frames to be held by the PK-232 before transmission.  This solves a problem with the YAPP binary file transfer program which relies on a small TNC transmit buffer to operate correctly.

UBIT 15:  Not used in the PK-232.

UBIT 16:  Not used in the PK-232.

UBIT 17: 	OFF: Morse, Baudot, ASCII and AMTOR transmissions start when commanded by the user or an application program. 
			ON:  Morse, Baudot, ASCII and AMTOR transmissions will not begin until the channel is clear of signals.  The channel is considered clear when both the DCD and the Squelch input are inactive.  The PERSIST and SLOTTIME delay functions are used if PPERSIST is ON, otherwise he DWAIT time is used.

UBIT 18: 	OFF:  In Packet operation, the FRACK (or FRICK if enabled) timer is used to retry packets that were not acknowledged. 
			ON:  An experimental Master/Slave relationship is established when a Packet connection is made.  This is designed for meteor scatter operation and is described in detail under the FRICK command.

UBIT 19: Not used in the PK-232.

UBIT 20: Not used in the PK-232.

UBIT 21: Not used in the PK-232.

UBIT 22:  	ON:  In the Packet mode, the PK-232 will respond to the receipt of an UNPROTO frame addressed to QRA by sending an LTNPROTO ID packet frame within 1 to 10 seconds.  This feature is compatible with TAPR's ANSWRQTLA command. 
			OFF: The PK-232 does not respond to UNPROTO frames addressed to QRA.

UBIT settings 23 and above are reserved for future expansion. 

### Ucmd   “n” [x] 												Default: 0
**Mode:** All 													Host: UC
**Parameters:**
- “n” - 0 to 4 specifying a User command that may be set.
**Description:**
The UCMD command is similar to the UBIT command.  UCMD allows seldom used commands that take numeric arguments (rather than ON/OFF) to be set.  
Presently the functions controlled by UCMD are Pactor settings that most users will never have to change.  
Still they are important enough to some users or application programs that we have included them under the umbrella command UCMD. 
The following are examples of how to use the UCMD command: 
UCMD 2 Returns the present value of UCMD 2. 
UCMD 1 3 Sets the function controlled by UCMD I to 3. 
UCMD 3 Y Returns the value of UCMD 3 to its default. 
UCMD Returns the value of the last UCMD value that was accessed. 

Listed below are the UCMD functions and the default value that presently has been assigned. 
UCMD 0: [x] x = 0-30, 	default 3. Sets the number of correct Pactor packets in a row that must be received before generating an automatic request to change from 100 to 200 baud. 
UCMD 1: [x] x = 0-30, 	default 6. Sets the number of incorrect Pactor packets in a row that must be received before generating an automatic request to change from 200 to 100 baud. 
UCMD 2: [x] x 0-9, 		default 2. Sets the number of Pactor packets sent in a speed-up attempt. 
UCMD 3: [x] x = 0-60, 	default 5. Sets the maximum number of Memory ARQ Pactor packets that are combined to form one good packet.  When this number is exceeded, all stored packets are cleared and Memory ARQ is re-initialized. “UCMD 3 0” disables Memory ARQ.


### Vhf  ON/OFF 												Default: ON
**Mode:** Packet 												Host: VH
**Parameters:**
- ON — Packet tones are shifted 1000 Hz. 
- OFF — Packet tones are shifted 200 Hz.
**Description:**
Use the VHF Command for immediate software control of the PK-232's modem tones. Changing components or switch settings is not required. 
Set VHF ON for VHF operation (default), and set VHF OFF for HF packet operation. NOTE: Be sure to change HBAUD to 300 bauds when operating below 28 MHz.

### WHYnot  ON/OFF 												Default: OFF
**Mode:** Packet 												Host: WN
**Parameters:**
- ON — The PK-232 generates a reason why received packets were not
- displayed.
- OFF — This function is disabled.
**Description:**
During packet operation, the PK-232 may receive many packets that are not displayed.  
Turning WHYNOT on will cause the PK-232 to display a message explaining the reason the received packet was not displayed to the screen.  
The messages and their meanings are shown below: PASSALL: The received packet frame had errors, and PASSALL was off, preventing the packet from being displayed to the screen. 
DCD Threshold: The Threshold control was set too far counter clockwise.  The DCO LED was off when the packet was received. 
MONITOR: The MONITOR value was set too low to receive this frame. 
MCON: MCON was set too low to receive this type of frame. 
MPROTO: MPROTO was set to off, and the received packet was probably a NET/ROM or TCP/IP frame. 
MFROM/MTO: The frame was blocked by the MFROM or MTO command. 
MBX: The call sign of the sending station does not match the call sign setting in the MBX command. 
MBX Sequence: The frame was received out of sequence, probably a retry. 
Frame too long: Incoming packet frame longer than 330 bytes.  Probably a non- AX.25 frame. 
Frame too short: Incoming packet frame shorter than 15 bytes. Only seen if PASSALL ON.  Probably noise. 
RX overrun: Another HDLC byte was received before we could read the previous one out of the HDLC chip.

### Wideshft  ON/OFF 											Default: OFF
**Mode:**  Baudot/ASCII RTTY/AMTOR/Pactor 						Host:  WI
**Parameters:**
- ON   — RTTY tones are shifted 1000 Hz.
- OFF  —    RTTY tones are shifted 200 Hz (emulates 170-Hz shift).
**Description:**
The WIDESHFT command permits the use of the PK-232 on VHF or HF with either wide (1000 Hz) or narrow (200 Hz) shifts.  
Many amateur radio VHF and HF RTTY operators use 170 Hz shift.  The PK-232's 200 Hz shift is well within the filter tolerances of any RTTY demodulator in general service.  
MARS stations will find WIDESHFT generally compatible with MARS and commercial 850 Hz shift RTTY operations.


### WOrdout ON/OFF 												Default: OFF
**Mode:** Baudot, ASCII, AMTOR, Pactor and Morse 				Host: WO
**Parameters:**
- ON — Typed characters are held in the transmit buffer until a space, CR,
- LF, TAB, RECEIVE, CWID, ENQ or +? characters(s) is typed.
- OFF — Typed characters are sent directly to the transmitter.
**Description:**
Use the WORDOUT Command to choose whether or not you can edit while entering text for transmission. 
When WORDOUT is on, each character you type is held in a buffer until space, a carriage return, a line fed, ENQ character ($05, <CTRL-E->) or the +?. 
You can edit words before the transmit buffer's contents are sent to the radio.  
When WORDOUT is OFF, each character you type is sent to the radio just as you typed it, without any delay.


### IFlow  ON/OFF 												Default: ON 
**Mode:**  All 													Host: XW
**Parameters:**
- ON — XCN/XOFF (software) flow control is activated.
- OFF — XON/XOFF flow control is disabled - hardware flow control is enabled.
**Description:**
When XFLOW is ON, software flow control is in effect - it's assumed that the computer or terminal will respond to the PK-232's Start and Stop characters defined by the XON and XOFF commands.  
Similarly, the PK-232 will respond to the computer’s start and stop characters defined by START and STOP. 
When XFLOW is OFF, the PK-232 sends hardware flow control commands via the CTS and is controlled via either the RTS or the DTR line.



### XMITok   ON/OFF 											Default:  ON
**Mode:**  All 													Host: XO
**Parameters:**
- ON — Transmit functions (PTT line) are active.
- OFF   — Transmit  functions (PTT line) are disabled.
**Description:**
When XMITOK is OFF, the PTT line to your transmitter is disabled - the transmit function is inhibited.  All other PK-232 functions remain the same. 
Your PK-232 generates and sends packets as requested, but does not key the radio's PTT line. Use the XMITOK command at any time to ensure that your PK-232 does not transmit. 
Turning XMITOK OFF can help enable full break-in CW (QSK) on certain transceivers.


### XOFF “n” 													Default:$131 <CTRL-S>
**Mode:** All 													Host: XF
**Parameters:**
- “n” —  0 to $7F  (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use XOFF to select the Stop character to be used to stop input from the computer or terminal. The Stop character default value is <CTRL-S> for computer data transfers.

### XON “n” 													Default: $11 <CTRL-Q>
**Mode:** All 													Host: XN
**Parameters:**
- “n” —  0 to $7F  (0 to 127 decimal) specifies an ASCII character code.
**Description:**
XON selects the PK-232 Start character that is sent to the computer or terminal to restart input from that device. 
The start character default value is <CTRL-Q> for computer data transfers.