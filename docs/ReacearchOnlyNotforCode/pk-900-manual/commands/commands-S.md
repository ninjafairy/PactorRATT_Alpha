# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands S (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### SAmple "n"                                                          Immediate Command
**Mode:** Command    Host: SA
**Source:** (PDF p.269)
**Parameters:**
- "n"  -    20 to 255 specifies the sampling rate in baud.
**Description:**
This operating mode is for advanced users interested in decoding unknown synchronous data transmissions.  SAMPLE is similar to the 5BIT and 6BIT modes, but operates on synchronous data, whereas 5BIT and 6BIT are used on signals known to be asynchronous.

SAMPLE synchronizes up on any regularly-paced data transmission received on Radio Port 1 and samples the data once per bit, packaging the data in groups and sending the groups to the user for further analysis.  The user can use SAMPLE to capture data bits from a synchronous transmission, such as FEC, TDM or an "unknown mode" not identified by the SIGNAL command.  The transmission is actually sampled several times per data bit.  The PK-900 does a majority vote on the last few samples to represent the value of the data bit.

One use for the SAMPLE command is to record the output to a disk file, then write a program to analyze the results for synchronous/asynchronous, bit sync patterns, data decoding, etc.

SAMPLE data is captured in 6-bit units; the order of bit reception is MSB first, LSB last.  The TNC sends the data unit to the user with a constant of hex 30 added to each unit, the same as the 6BIT command.  The 6-bit unit is a compromise between hexadecimal and 8-bit binary output.  The 6-bit unit yields shorter disk files than 4-bit hexadecimal characters, but encounters no interference from terminal communications programs and the TNC's Converse and Command modes.  The 6-bit unit's range of $30-6F falls within the printable ASCII range, allowing the TNC to insert end-of-line carriage returns that can be ignored by the user's analysis software.

To use SAMPLE, set ACRDISP to a non-zero value such as 77.  This will break up the recorded disk file into lines.  Tune in the signal, set WIDESHFT ON or OFF as needed, and get the transmission rate from the SIGNAL command.  Now type "SAMPLE (rate)".  As an example, SIGNAL may identify a transmission as 96 baud TDM; in this case type the following:

SAMPLE 96 <Carriage Return>

Now begin the capture to a disk file with the terminal program.  At the end of the session, edit the disk file and remove any TNC commands that were echoed before or after the received data.

Occasionally SIGNAL will identify a Baudot transmission at a rate that SAMPLE cannot sync up on.  This would happen if the Baudot signal had a stop bit duration 1.5 times the data bit duration.  In this case, SAMPLE at twice the baud rate and compensate for the doubled data bits in the analysis software. Note that it might be more useful to let the TNC do the start/stop bit work by using the 5BIT command rather than SAMPLE.  5BIT uses RBAUD, and adds a constant of hex 40 to each 5-bit character received.

Note:     RXREV does affect the sense of the SAMPLE data. RXREV should however not be changed while capturing data.


### SELfec aaaa[aaa]                                                    Immediate Command
**Mode:** AMTOR FEC    Host: SE
**Source:** (PDF p.270)
**Parameters:**
- aaaa  -   Specifies the distant station's SELective CALling code (SELCALL).
**Description:**
The SELFEC command starts a SELective FEC (Mode Bs) transmission to a specific distant station when you enter that station's SELCALL (SELective CALLing) code. The SELFEC command must be accompanied by a unique character sequence (aaaa) that contains four or seven alphabetic characters.  You do not have to type the SELCALL a second time if you intend to call the same station again right away.

See MYSELCAL and MYIDENT to enter your 4- and 7-character SELCALLs.  Other AMTOR commands are ACHG, ACRRTTY, ADELAY, ALFRTTY, ARQTMO, EAS, HEREIS and RECEIVE.


### SEGment "n"                                                         Default: $70
**Mode:** Command    Host: SG
**Source:** (PDF p.270)
**Parameters:**
- "n"   -   a hex number used to access a Segment in the PK-900's memory and I/O location map.
**Description:**
The SEGMENT command selects the start of a 64k segment of memory that programmers may want to access.

This command is used by programmers in conjunction with the ADDRESS, DATA, IO and PK instructions to access the PK-900 RAM, ROM and I/O locations.

For example:  Segment $70 and address $9123 points to the physical location $79123


### SEndpac "n"                                                         Default: $0D <CTRL-M>
**Mode:** Packet    Host: SP
**Source:** (PDF p.270)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use the SENDPAC command to select the character used to cause a packet to be sent in Converse Mode.  The parameter "n" is the ASCII code for the character you want to use to force your input to be sent.  Use default SENDPAC value $0D for ordinary conversation with ACRPACK ON to send packets at natural intervals.


### SIgnal                                                              Immediate Command
**Mode:** All    Host: SI
**Source:** (PDF p.271)
**Description:**
SIGNAL is an immediate command that causes the PK-900 to enter the Signal Identification and Acquisition Mode (SIAM).  The PK-900 will respond with:

Opmode  was  BAudot Opmode  now  SIgnal

After a few seconds the PK-900 will show the signals baud rate.  A few seconds later it will identify the signal type.


### SLottime "n/n"                                                      Default: 30/30 (300 msec.)
**Mode:** Packet    Host: SL
**Source:** (PDF p.271)
**Parameters:**
- "n"  -    0 to 250 specifies the time the PK-900 waits between generating random numbers to see if it can transmit.
**Description:**
The SLOTTIME parameter works with the PPERSIST and PERSIST parameters to achieve true p-persistent CSMA (Carrier-Sense Multiple Access) in Packet operation. The value for each Radio Port defaults to 30 (300 msec.) for VHF operation. For HF packet operation, a value of 12 is recommended.  See the PPERSIST and the PERSIST commands for more information on this parameter.


### SPACE "n"                                                           Default: Current modem space frequency
**Mode:** Command    Host: Sp
**Source:** (PDF p.271)
**Parameters:**
- "n"  -  500 to 3000 specifies the transmit space frequency in Hz.
**Description:**
The SPACE command is used to select a non-standard space tone transmit frequency.  The space tone frequency range is 500 to 3000 Hz.  If the SPACE command is entered without an argument ("nnnn"), the current space frequency is displayed.


### SQuelch ON|OFF/ON|OFF                                               Default: OFF/OFF
**Mode:** Packet    Host: SQ
**Source:** (PDF p.272)
**Parameters:**
- ON   -    Your PK-900 responds to positive-going squelch voltage.
- OFF  -    Your PK-900 responds to negative-going squelch voltage.
**Description:**
Normally, your PK-900 uses its CSMA (Carrier Sense Multiple Access) circuit to decide whether or not it is clear to transmit on a packet channel.  If there are non-packet signals on the channel you're using (such as voice), you will want to use true RF-carrier CSMA by monitoring the squelch line voltage from your radio. If SQUELCH is OFF (default) for a particular Radio port, the PK-900 inhibits transmissions when there is a POSITIVE voltage on the Radio connectors squelch input line.  When there is no voltage or NO CONNECTION to this pin, the PK-900 allows packets to be sent.

When SQUELCH is ON, the PK-900 will inhibit packet transmissions when there is 0 volts applied to the squelch input pin on the appropriate Radio connector.


### SRXall ON|OFF                                                       Default: OFF
**Mode:** AMTOR    Host: SR
**Source:** (PDF p.272)
**Parameters:**
- ON   -    Receive ALL selective (SELFEC) transmissions.
- OFF  -    Receive only SELCALL-addressed SELFEC transmissions.
**Description:**
SRXALL permits the reception of selectively coded inverse FEC signals normally not available for decoding.  Set SRXALL ON to activate this feature on Radio Port 1.


### STArt "n"                                                           Default: $11 <CTRL-Q>
**Mode:** All    Host: ST
**Source:** (PDF p.272)
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use the START command to choose the user START character (default <CTRL-Q>) you want to use to restart output FROM the PK-900 TO the terminal after it has been halted by typing the user STOP character.  See the XFLOW command.


### STOp "n"                                                            Default: $13 <CTRL-S>
**Mode:** All    Host: SO
**Source:** (PDF p.273)
**Parameters:**
- "n"   -   0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use the STOP command to select the user STOP character (default <CTRL-S>) you will use to stop output FROM the PK-900 TO the terminal.  See the XFLOW command.
