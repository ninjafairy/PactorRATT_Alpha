# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands R (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### Radio "n1/n2"                                                       Default: 1/2
**Mode:** All    Host: RA
**Source:** (PDF p.264)
**Parameters:**
- "n1"  -    0 or 1 with 0 meaning Radio Port 1 is OFF and 1 meaning it is ON.
- "n2"  -    0 to 2 with 0 meaning Radio Port 2 is OFF and 2 meaning it is ON.
**Description:**
The Radio command allows the user to control whether each of the Radio Ports is enabled.  Sometimes it is desirable to disable one or both of the Radio Ports when operation on that port is not desired.  The RADIO command allows full control by you over operation of each Port.

The first argument controls Radio Port 1.  When the first argument is 0 (zero) Radio Port 1 is Disabled.  When this first argument is a 1, Radio Port 1 is enabled.

The second argument controls Radio Port 2.  When the second argument is 0, (zero) Radio Port 2 is Disabled.  When the second argument is a 2, Radio Port 2 is enabled.

When a Radio Port is disabled, the LCD status for that port will be blank.


### RAWhdlc ON|OFF                                                      Default: OFF
**Mode:** Packet    Host: RW
**Source:** (PDF p.264)
**Parameters:**
- ON   -    The PK-900 operates in a raw HDLC packet mode when HOST is ON.
- OFF  -    The PK-900 operates in standard AX.25.
**Description:**
The RAWHDLC command enables the PK-900 to bypass the AX.25 packet implementation and communicate directly with the hardware HDLC (Z8530) on Radio Port 1. HOST mode must be ON to communicate with the PK-900 in the RAW HDLC mode. Packet Operation on Radio Port 2 is disabled during RAW HDLC operation.


### RBaud "n"                                                           Default: 45 bauds (60 WPM)
**Mode:** Baudot RTTY    Host: RB
**Source:** (PDF p.265)
**Parameters:**
- "n"  -    Specifies the Baudot data rate in bauds from the PK-900 to the radio.
**Description:**
RBAUD sets the radio ("on-air") baud rate only in the Baudot operating mode. This value has no relationship to your computer or terminal program's baud rate. Available Baudot data rates include 45, 50, 57, 75, 100, 110, 150, 200 and 300 bauds (60, 66, 75, 100, 132, 145, 198, 264 and 396 WPM).

You may use RBAUD UP (RB U) to go to the next highest Baudot speed or RBAUD DOWN (RB D) to go to the next lowest Baudot speed.


### Rcve                                                                Immediate Command
**Mode:** Baudot, ASCII, AMTOR, PACTOR, FAX, Morse    Host: RC
**Source:** (PDF p.265)
**Description:**
RCVE is an immediate command, used in Morse, Baudot, ASCII, ARQ, FEC and FAX modes to switch your PK-900 from transmit to receive.

o    You must return to the Command Mode to use the RCVE command.

o    PACTOR:  A single R will generate a protocoled receive while RR will

cause an immediate end of transmission and will not turn off the other station.


### RECeive "n"                                                         Default: $04 <CTRL-D>
**Mode:** Baudot/ASCII/Morse/AMTOR/PACTOR/FAX    Host: RE
**Source:** (PDF p.265)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Parameter "n" is the numeric ASCII code for the character you'll use when you want the PK-900 to return to receive.

The RECEIVE command allows you to insert a character (default <CTRL-D>) in your typed text that will cause the PK-900 to return to receive after all the text has been transmitted.


### REDispla "n"                                                        Default: $12 <CTRL-R>
**Mode:** All    Host: RD
**Source:** (PDF p.266)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
REDISPLA changes the redisplay-line input editing character.

Parameter "n" is the numeric ASCII code for the character you'll use when you want to re-display the current input line.

Type the REDISPLA character (default <CTRL-R>) to re-display a command or text line you've just typed.  This can be helpful when editing a line especially if your terminal does not support <BACKSPACE>.  It can also be used in Packet to display a packet that might have been received while you were typing.  A <BACKSLASH> is appended to old line, and the corrected line is shown below it.


### RELink ON|OFF/ON|OFF                                                Default: OFF/OFF
**Mode:** Packet    Host: RL
**Source:** (PDF p.266)
**Parameters:**
- ON   -    The PK-900 will try to automatically reconnect the distant station
- after the link has timed out on retries.
- OFF  -    The PK-900 will not attempt to re-establish the failed link.
**Description:**
Set RELINK ON if you want the PK-900 to automatically try to reconnect to a distant packet station if the link fails.  This is settable for each radio port.


### RESET                                                               Immediate Command
**Mode:** Command    Host: RS
**Source:** (PDF p.266)
**Description:**
RESET is an immediate command that resets all parameters to PK-900's PROM default settings and reinitializes the PK-900.  All personalized parameters, monitor lists and MailDrop messages will be lost.


### RESptime "n/n"                                                      Default: 0/0 (000 msec.)
**Mode:** Packet    Host: RP
**Source:** (PDF p.266)
**Parameters:**
- "n"  -    0 to 250 specifies 100-millisecond intervals.
**Description:**
RESPTIME adds a minimum delay before your PK-900 sends acknowledgment packets. This delay may run concurrently with the default wait time set by DWAIT and any random wait in effect.  RESPTIME may be set for each Radio Port.

During a file transfer, RESPTIME can help avoid data/ack collisions caused by the sending stations TNC pausing briefly between transmitted data frames.


### RESTART                                                             Immediate Command
**Mode:** Command    Host: RT
**Source:** (PDF p.267)
**Description:**
RESTART is an immediate command that reinitializes the PK-900 while retaining the user's settings.  The effect of the RESTART command is the same as turning the PK-900 OFF, then ON again.

RESTART does not reset the values in bbRAM.  See the RESET command.


### REtry "n/n"                                                         Default: 10/10
**Mode:** Packet    Host: RY
**Source:** (PDF p.267)
**Parameters:**
- "n"  -    0 to 15 specifies the maximum number of packet retries.
**Description:**
The AX.25 protocol uses the retransmission of frames that have not been acknowledged as a means to insure that ALL transmitted frames are received. The number of retries that the PK-900 will attempt is set by the RETRY command (default 10).  If the number of retries is exceeded, the packet link may be lost.  The number of retries allowed on each Radio Port may be selected.


### RFec ON|OFF                                                         Default: ON
**Mode:** AMTOR    Host: RF
**Source:** (PDF p.267)
**Parameters:**
- ON   -    Mode B (FEC) signals are displayed in AMTOR Standby (default).
- OFF  -    Mode B (FEC) signals are not displayed in AMTOR Standby.
**Description:**
Turn the RFEC command OFF to prevent the reception and display of all FEC signals received while in AMTOR Standby.


### RFRame ON|OFF                                                       Default: OFF
**Mode:** Baudot and ASCII RTTY    Host: RG
**Source:** (PDF p.268)
**Parameters:**
- ON   -    Check received Baudot and ASCII characters for framing errors.
- OFF  -    Print received Baudot and ASCII characters regardless of errors.
**Description:**
When RFRAME is OFF (default), Baudot and ASCII modes operate as always, that is characters are copied based on the presence of the DCD signal.

When RFRAME is ON, the PK-900 checks received Baudot and ASCII characters for framing errors.  A framing error on a character in an asynchronous mode (such as Baudot and ASCII) occurs when the bit in the stop position is detected to be the wrong polarity (the polarity of the start bit is supposed to be space or 0, while the stop bit is supposed to be mark or 1).  The unit stops copying characters when 4 out of the last 12 characters had framing errors.  Copy resumes when the most recent 12 characters are error-free.  This should significantly reduce the copying of garbage characters when no signals are present.  When RFRAME is ON, characters are copied based on the recent history of framing errors.


### RXRev ON|OFF                                                        Default: OFF
**Mode:** Baudot and ASCII RTTY/AMTOR    Host: RX
**Source:** (PDF p.268)
**Parameters:**
- ON   -    Received data polarity is reversed (mark-space reversal).
- OFF  -    Received data polarity is normal.
**Description:**
Use the RXREV Command to invert the polarity of the data demodulated from the received mark and space tones.

In some cases, you may be trying to copy a station that's transmitting "upside down" although it is receiving your signals correctly.  This is especially true when listening to signals in the Short Wave bands.  Set RXREV ON to reverse the data sense of received signals.

Although RXR will reverse the mark and space polarity in PACTOR, the mode is polarity insensitive so RXR will have no effect.
