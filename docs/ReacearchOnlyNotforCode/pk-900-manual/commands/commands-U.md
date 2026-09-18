# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands U (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### UBit "n" ON/OFF                                                     Default: 0
**Mode:** All    Host: UB
**Source:** (PDF p.279)
**Parameters:**
- "n"  -    0 to 255 specifying a User BIT that may be set ON or OFF.
**Description:**
The UBIT is an extension of the old CUSTOM command which allows up to 255 ON/OFF functions to be added to the PK-900 without burdening users with a large number of commands.  The functions controlled by UBIT are things that most users will never have to change.  Still they are important enough to some users or application programs that we have included them under the umbrella command UBIT.

The following are examples of how to use the UBIT command:

UBIT 5         Returns the present status of UBIT 5 UBIT 1 ON      Sets the function controlled by UBIT 1 to ON UBIT 10 T      Toggles the state of the function controlled by UBIT 10 UBIT           Returns the state of the last UBIT value that was accessed

Listed below are the UBIT functions and the default state that presently have been assigned.  The default state of each UBIT is always shown first.

UBIT 0:   ON:  The PK-900 will discard a received a PACTOR block or packet if the signal is too weak to light the DCD LCD. OFF: The PK-900 will receive a packet regardless of the DCD status.

UBIT 1:   OFF: Entering the command MONITOR ON or MONITOR YES causes the MONITOR command to be set to 4. ON:  Entering the command MONITOR ON or MONITOR YES causes the MONITOR command to be set to 6.

UBIT 2:   ON:  A Break signal received on the RS-232 line forces the PK-900 into Command mode from all modes except HOST mode. OFF: A Break signal on the RS-232 line is ignored by the PK-900.

UBIT 3:   ON:  Logical packet channels on radio port 2 may be selected by either a-z (lower case) or A-Z (UPPER CASE) letters. OFF: Logical packet channels on radio port 2 must be selected by A-Z (UPPER CASE) letters.  Attempting to use lower case characters gives the "Must be 0-9 or A-Z" error message.

UBIT 4:   ON:  When transmitting in Baudot, the PK-900 inserts the FIGS after a space just prior to sending any figures (<space><FIGS><number>). This permits receiving stations to decode groups of figures correctly regardless of the USOS setting. OFF: The PK-900 will not insert the FIGS character after each space. MARS operators may want to set UBIT 4 OFF for literal operation.

UBIT 5:   OFF: The PK-900 will always power up in Command Mode. ON:  The PK-900 will remain in the last mode (Converse, Command or Transparent) provided the battery is jumper enabled.

UBIT 6:   OFF: In Packet, monitoring is disabled when in the Transparent mode. ON:  Packet monitoring is active in the Transparent mode.  MFROM, MTO, MRPT, MONITOR, MCON, MPROTO, MSTAMP, MXMIT, CONSTAMP and MBX are all active.

UBIT 7:   OFF: In Morse receive, the character ..-- prints as a "^". ON:  In Morse, the character ..-- prints as a <Carriage Return>.

UBIT 8:   Not used in the PK-900.

UBIT 9:   ON:  In AMTOR or PACTOR a received WRU character (FIGS-D) will cause the Auto-Answerback text to be sent regardless of the setting of WRU. This is subject to the setting of the CODE command. OFF: In AMTOR or PACTOR a received WRU character (FIGS-D) will have no effect.

UBIT 10:  OFF: Polling in the HOST mode is subject to HPOLL and must be done for all changes in status. ON:  Status changes (e.g. Idle to Tfc) in AMTOR, FAX, TDM, PACTOR or NAVTEX causes the PK-900 to issue the following host block:

SOH  $50  n  ETB

where n is $30-36, the same number that the OPMODE command furnishes.  This block is subject to HPOLL.

UBIT 11:  ON:  A "Connected" message appears when an ARQ link is first established using seven-character SELCALLs (CCIR 625). OFF: No Connected message appears at the start of ARQ communications.

UBIT 12:  OFF: The Packet Morse ID (MID) is ON/OFF keying of the low tone. ON:  The Packet Morse ID is sent in 2-tone FSK with the low tone being key-down and the high tone representing key-up.  Use this setting to keep other stations from sending a packet during the Morse ID.

UBIT 13:  OFF: MailDrop Connect status messages are always sent to the local user, regardless of the setting of MDMON. ON:  Remote user dialog and Connect status messages with the MailDrop are shown only if MDMON is ON.

UBIT 14:  OFF: In Packet, the transmit buffer for data sent from the computer to the PK-900 is limited only by available PK-900 memory. ON:  In Packet, the serial flow control will permit only a maximum of 7 I-frames to be held by the PK-900 before transmission.  This solves a problem with the YAPP binary file transfer program which relies on a small TNC transmit buffer to operate correctly.

UBIT 15:  Not used in the PK-900.

UBIT 16:  Not used in the PK-900.

UBIT 17:  OFF: Morse, Baudot, ASCII and AMTOR transmissions start when commanded by the user or an application program. ON:  Morse, Baudot, ASCII, PACTOR and AMTOR transmissions will not begin until the channel is clear of signals.  The channel is considered clear when both the DCD and the Squelch input (if used) are inactive.  The PERSIST and SLOTTIME delay functions are used if PPERSIST is ON, otherwise the DWAIT time is used.

UBIT 18:  OFF: In Packet operation, the FRACK (or FRICK if enabled) timer is used to retry packets that were not acknowledged. ON:  An experimental Master/Slave relationship is established when a Packet connection is made.  This is designed for meteor scatter operation and is described in detail under the FRICK command.

UBIT 19:  ON:  The "p1" and "p2" radio port designators are shown ahead of monitored data on each radio port. OFF: The "p1" and "p2" radio port designators are not displayed.

UBIT 20:  OFF: In the Analog receive mode, each 8 bit sample is sent to the computer as an 8 bit byte of data. ON:  In the Analog receive mode, each 8 bit sample is truncated to the 4 Most Significant bits.  Two 4 bit samples are then packed together to form an 8 bit byte before being sent to the computer.

UBIT 21:  Not used in the PK-900.

UBIT 22:  ON:  In the Packet mode, the PK-900 will respond to the receipt of an UNPROTO frame addressed to QRA by sending an UNPROTO ID packet frame within 1 to 10 seconds.  This feature is compatible with TAPR's ANSWRQRA command. OFF: The PK-900 does not respond to UNPROTO frames addressed to QRA.

UBIT 23   ON:  The TEST function halts at the end of each test measurement. Pressing any key except "Q" starts the next test.  Pressing "Q" halts the test and returns to command mode.

OFF: The TEST function will halt for the discriminator adjustment functions only.  The rest of the test will continue until complete or may be stopped by pressing the "Q" key.

UBIT settings 24 and above are unused at the present time but are reserved for future expansion.


### UCmd "n x"                                                          Default: 0
**Mode:** PACTOR    Host: UB
**Source:** (PDF p.282)
**Parameters:**
- "n"  -    0 to 15 specifying a User BYTE that may be set.
- "x"  -    0 to 255 specifiying the value of the specifice byte to be set.
**Description:**
The UCMD is an extension of the UBIT command which allows up to 15 commands that take numeric arguemants to be added to the PK-900 without burdening users with a large number of commands.  The functions controlled by UCMD are things that most users will never have to change.  Still they are important enough to some users or application programs that we have included them under the umbrella command UCMD.

The following are examples of how to use the UBIT command:

UCMD 5        Returns the present status of UCMD 5. UCMD 4 5      Sets user command 4 to the value 5. UCMD 12 OFF   Sets user command 12 to the value of 0. UCMD ON       Restores user command 8 to its default value. UCMD          Shows the setting ofthe last UCMD entered.

Listed below are the UCMD functions and the default states that presently have been assigned.  The default state of each UCMD is always shown first.

UCMD 0:   This is a PACTOR command.  It sets the number of correct packets in a row that must be received before generating an automatic request to change from 100 to 200 baud.

UCMD 1:   This is a PACTOR command.  It sets thenumber of incorrect packets in a row that must be received before generating an automatic requenst to change from 200 to 100 baud.

UCMD 2:   This is a PACTOR command.  It sets the number of packets sent in a speed-up attempt.

UCMD 3:   This is a PACTOR command.  It sets the maximum number of Memory ARQ packets that are combined to form one good packet.  When this number is exceeded, all stored packets are cleared and Memory ARQ is re-initialized.

UCMD 4-15 are unused for now.


### Unproto call1 [VIA call2[,call3..,call9]]/call1 [VIA call2[,call3..,call9]] Default: CQ/CQ
**Mode:** Packet    Host: UN
**Source:** (PDF p.283)
**Parameters:**
- call1    -     Call sign to be placed in the TO address field.
- call2-9  -     Optional digipeater call list, up to eight calls.
**Description:**
UNPROTO sets the digipeat and destination address fields of packets sent in the unconnected (unprotocol) mode.

Unconnected packets are sent as Unnumbered I-frames (UI frames) with the destination and digipeat fields taken from "call1" through "call9" options. When a destination is not specified, unconnected packets are sent to "CQ."

Unconnected packets sent from other packet stations can be monitored by setting MONITOR to a value greater than "1" and setting MFROM to ALL.

The UNPROTO path and address is also used for beacon packets.


### USers "n/n"                                                         Default: 1/1
**Mode:** Packet    Host: UR
**Source:** (PDF p.283)
**Parameters:**
- "n"  -    0 to 10 specifies the number of active simultaneous connections that can be established with your PK-900.
**Description:**
USERS affects the way that incoming connect requests are handled on each Radio Port  of the PK-900.  It does not affect the number of connections you initiate with your PK-900.  For example:

USERS 0   allows incoming connections on any free logical channel USERS 1   rejects incoming connections if there are connections on 1 or more logical channels. USERS 2   rejects incoming connections if there are connections on 2 or more logical channels.

And so on, through USERS 10.


### USOs ON|OFF                                                         Default: OFF
**Mode:** Baudot RTTY    Host: US
**Source:** (PDF p.284)
**Parameters:**
- ON   -    Letters (LTRS) case IS forced after receiving a space character.
- OFF  -    Letters (LTRS) is NOT forced after receiving a space character.
**Description:**
Use the USOS Command (UnShift On Space) when you want your PK-900 to automatically change from figures to letters after receiving a space character.

When using Baudot RTTY in poor HF receiving conditions, a received character can be incorrectly interpreted as a FIGURES-SHIFT character, forcing the received data into the wrong case.  Many otherwise good characters received after this will be interpreted as figures (numbers and punctuation), not as the letters sent by the distant station.  USOS ON helps reduce these receiving errors.
