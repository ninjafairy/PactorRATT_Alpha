# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands T (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### TBaud "n"                                                           Default: 1200 bauds
**Mode:** All    Host: TB
**Source:** (PDF p.273)
**Parameters:**
- "n"   -   Specifies the data rate in bauds, on the RS-232 serial I/O port.
**Description:**
TBAUD sets the baud rate you are using to communicate with the PK-900 from your terminal or computer.  Set TBAUD to specify the terminal baud rate to be activated at the next power-on or RESTART.  A warning message reminds you of this.  Be sure you can set your terminal for the same rate.

The TBAUD command supports the following serial port data rates of 45, 50, 57, 75, 100, 110, 150, 200, 300, 400, 600, 1200, 2400, 4800, 9600, 19200 and 38400 bauds.

Note:  The Autobaud routine does not support 38400 bits/sec, so this data rate must be set manually.


### TClear                                                              Immediate Command
**Mode:** Command    Host: TC
**Source:** (PDF p.273)
**Description:**
The TCLEAR command clears your PK-900's transmit buffer on the "Logical Channel" you have selected and cancels any further transmission of data when in the Baudot, ASCII, AMTOR, PACTOR or Morse operating modes.  In Packet Mode, all data is cleared except for a few remaining packets.

You must be in the Command Mode to use TCLEAR.


### TDBaud "n"                                                          Default: 96
**Mode:** TDM    Host: TU
**Source:** (PDF p.274)
**Parameters:**
- "n"   -   Specifies the data rate in bauds of the TDM signal you are receiving.
**Description:**
The default value of n is 96.  TDB can be set to 0-200, but only some of these are legal values: 1-channel:  48,  72,  96 2-channel:  86,  96, 100 4-channel: 171, 192, 200

No error checking is done for values other than above.  Bad values result in an internal TDBAUD of 96.


### TDChan "n"                                                          Default: 0
**Mode:** TDM    Host: TN
**Source:** (PDF p.274)
**Parameters:**
- "n"   -   Specifies the TDM channel number.
- "n" selects which data channel (default 0) to separate out from the multiplexed
**Description:**
TDM signal.  n can be set to 0-3, but only some of these have unique effects:

1-channel: No effect. 2-channel: 0 and 2 show Channel A. 1 and 3 show Channel B. 4-channel: 0 shows Channel A. 1 shows Channel B. 2 shows Channel C. 3 shows Channel D.


### TDm                                                                 Immediate Command
**Mode:** TDM    Host: TV
**Source:** (PDF p.274)
**Description:**
TDM is an immediate command that places Radio Port 1 of the PK-900 in the TDM receive mode.  TDM stands for Time Division Multiplexing, also known as Moore code and is the implementation of CCIR Recommendation 342.

Use the PK-900 SIGNAL command first to determine the bit rate and to make sure that the signal is actually TDM.  The SIGNAL command can detect one or two channel TDM transmissions.

The TDM command forces bit phasing; do this when changing frequency to another TDM signal.  This is also useful when the PK-900 synchronizes on the wrong bit in the character stream, which is likely on a signal which is idling. TDM stations idle MOST of the time, so you may have to leave the PK-900 monitoring for an hour or two before any data is received.


### TEST                                                                Immediate Command
**Mode:** Command    Host: TE
**Source:** (PDF p.275)
**Description:**
TEST initiates the PK-900 self test function.   For the modem self test to operate, both radio ports must have the loopback jumpers installed.

The self test feature tests operating voltages, both radio channel modems and all keying lines.  A probe connected to either an internal test point or pin 16 of the serial connector is necessary to test the keying lines.


### TIme "n"                                                            Default: $14 <CTRL-T>
**Mode:** All    Host: TM
**Source:** (PDF p.275)
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The TIME command specifies which control character sends the time-of-day in the text you type into the transmit buffer or into a text file stored on disk.

At transmit time, the PK-900 reads the embedded control code (default <CTRL-T>), reads the time-of-day from the PK-900's internal clock and then sends the time to the radio in the data transmission code in use at that time. If the DAYTIME has not been set, and a control-T will cause the PK-900 to send an asterisk (*). When DAYSTAMP is set ON, the date is transmitted with the time.

NOTE:  The TIME command cannot be embedded in CTEXT, BTEXT, MTEXT or AAB.


### TMail ON|OFF                                                        Default: OFF
**Mode:** AMTOR, PACTOR    Host: TL
**Source:** (PDF p.275)
**Parameters:**
- ON    -   The PK-900 operates as a personal AMTOR or PACTOR BBS or MailDrop.
- OFF   -   The PK-900 only operates as a normal CCIR 476 or 625 controller.
**Description:**
The PK-900's MailDrop is a personal mailbox that uses a subset of the W0RLI/WA7MBL PBBS commands and is similar to operation of APLINK stations.  When TMAIL is ON and another station establishes an ARQ link with your MYSELCAL or MYIDENT, the remote AMTOR station may leave messages for you or read messages from you.  Third-party messages are not accepted by your AMTOR MailDrop unless 3RDPARTY is ON.

See the MDCHECK, TMPROMPT, MDMON, MTEXT, MMSG MYSELCAL and MYIDENT commands.


### TMPrompt text                                                       Default: (see text)
**Mode:** AMTOR/MailDrop    Host: Tp
**Source:** (PDF p.276)
**Parameters:**
- text  -   Any combination of characters and spaces up to a maximum of 80 bytes.
**Description:**
TMPROMPT is the command line sent to a calling station by your AMTOR MailDrop in response to a Send message command.  The default text is:

"GA subj/GA msg, '/EX' to end."

Text before the first slash is sent to the user as the subject prompt; text after the slash is sent as the message text prompt.  If there is no slash in the text, the subject prompt is "SUBJECT:" and the text prompt is from TMPROMPT.


### TRACe ON|OFF                                                        Default: OFF
**Mode:** Packet/FAX/Baudot/AMTOR/PACTOR/Analog    Host: TR
**Source:** (PDF p.276)
**Parameters:**
- ON   -    Trace function is activated.
- OFF  -    Trace function is disabled.
**Description:**
Packet: The TRACE command activates the AX.25 protocol display.  When TRACE is ON all received frames are displayed in their entirety, including all header information.  The TRACE display is shown as it appears on an 80-column display. The following monitored frame is a sample:

W2JUP*>TESTER <UI>: This is a test message packet.

Byte                Hex                   Shifted ASCII         ASCII 000: A88AA6A8 8AA460AE 6494AAA0 406103F0  TESTER0W2JUP 0.x  ......`.d...@a.. 010: 54686973 20697320 61207465 7374206D  *449.49.0.:29:.6  This is a test m 020: 65737361 67652070 61636B65 742E0D    299032.80152:..   essage packet...

The byte column shows the offset into the packet of the first byte of the line. The hex display column shows the next 16 bytes of the packet, exactly as received, in standard hex format.  The shifted ASCII column decodes the highorder seven bits of each byte as an ASCII character code.  The ASCII column decodes the low-order seven bits of each byte as an ASCII character code.

FAX and Analog: When Operating in FAX mode, TRACE is ON, and PRFAX is OFF, the graphics escape sequences and data bytes are sent to the terminal with each byte expanded to two Hexadecimal characters.  This helps get around the limitations of many terminal programs that do not allow 8-bit data to be saved to disk as an ASCII file.

Interspersed command prompts and even the L and R commands would have no effect on the final data and it could be translated back to binary data with a computer program.


### Trans                                                               Immediate Command
**Mode:** All    Host: Not Supported
**Source:** (PDF p.277)
**Description:**
TRANS is an immediate command that switches the PK-900 switch from the Command Mode to Transparent Mode.  The current state of the radio link is not affected. Transparent Mode is primarily useful for computer communications.  In Transparent Mode "human interface" features such as input editing, echoing of input characters, and type-in flow control are disabled.

o    Use Transparent Mode for transferring binary or other non-text files.

o    To exit the Transparent mode, type the COMMAND character (default <CTRL-C>)

three times within the time period set by CMDTIME (default 1 Second).


### TRFlow ON|OFF                                                       Default: OFF
**Mode:** Transparent    Host: TW
**Source:** (PDF p.277)
**Parameters:**
- ON   -    Software flow control for the computer or terminal RECEIVING data is activated in Transparent Mode.
- OFF  -    Software flow control for the computer or terminal RECEIVING data is disabled in Transparent Mode.
**Description:**
When TRFLOW is ON, the type of flow control used by the computer RECEIVING data in Transparent Mode is determined by how START and STOP are set.

When TRFLOW is OFF, only "hardware" flow control (RTS, DTR) is available to the computer RECEIVING data from the PK-900 in Transparent Mode.

If TRFLOW is ON, and START and STOP are set to values other than zero, software flow control is enabled for the user's computer or terminal.  The PK-900 responds to the user START and user STOP characters while remaining transparent to all other characters from the terminal.


### TRIes "n"                                                           Default: 0
**Mode:** Packet    Host: TI
**Source:** (PDF p.277)
**Parameters:**
- "n"  -    0 to 15 specifies the current RETRY level on the selected input channel.
**Description:**
TRIES retrieves (or forces) the count of "retry counter" on the data channel presently selected.

If you type TRIES without an argument, the PK-900 returns the current number of tries if an outstanding unacknowledged frame exists.  If no outstanding unacknowledged frame exists, the PK-900 returns the number of tries required to get an ACK for the previous frame.

If you type TRIES with an argument the "tries" counter is forced to the entered value.  Using this command to force a new count of tries is not recommended.


### TXdelay "n/n"                                                       Default: 30/30 (300 msec.)
**Mode:** Packet, Baudot and ASCII    Host: TD
**Source:** (PDF p.278)
**Parameters:**
- "n"  -    0 to 120 specifies ten-millisecond intervals.
**Description:**
The TXDELAY command tells your PK-900 how long to wait before sending packet frame data after keying your transmitter's PTT line.  All transmitters need some amount of start-up time to put a signal on the air.  This parameter should be set separately for Radio Port 1 and Radio Port 2 since it is unlikely that the radio transmitters on each port will require exactly the same amount of key-up time.  The default value of 300 msec is a good starting point should work with almost all transceivers.

In fact many of the newer transceivers can use smaller TXDELAY values.  Crystal controlled transceivers can often use smaller values as well.  On the other hand, tube-type transceivers and amplifiers can require a longer time to switch and may require TXDELAY to be increased.  Experiment with the value to determine the shortest setting you can use in reliably in Packet.

Baudot and ASCII use TXDELAY between PTT ON and the start of transmitted data.


### TXFlow ON|OFF                                                       Default: OFF
**Mode:** Transparent    Host: TF
**Source:** (PDF p.278)
**Parameters:**
- ON   -    Software flow control for the PK-900 is active in Transparent Mode.
- OFF  -    Software flow control for the PK-900 is disabled in Transparent Mode.
**Description:**
When TXFLOW is ON, the setting of XFLOW determines the type of flow control used in Transparent Mode by the PK-900 to control TRANSMITTED data. When TXFLOW is OFF, the PK-900 uses only hardware flow control to control TRANSMITTED data; all data sent to the terminal remains fully transparent.

When TXFLOW and XFLOW are ON, the PK-900 uses the Start and Stop characters (set by XON and XOFF) to control the input from the computer.


### TXRev ON|OFF                                                        Default: OFF
**Mode:** All    Host: TX
**Source:** (PDF p.278)
**Parameters:**
- ON   -    Transmit data polarity is reversed (mark-space reversal).
- OFF  -    Transmit data polarity is normal.
**Description:**
Use the TXREV Command to reverse the mark and space in the transmitted AFSK and FSK signals.

In some cases, the station you're working may be receiving inverted data although it is transmitting in correct polarity.  Set TXREV ON to reverse the sense of your transmitted signals.
