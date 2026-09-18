# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands H (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### HBaud "n/n"                                                         Default: 1200/1200 bauds
**Mode:** Packet    Host: HB
**Source:** (PDF p.231)
**Parameters:**
- "n"  -    values specifying the Packet data rate in bits per second from each Radio Port of the PK-900 to the radio.
**Description:**
HBAUD sets the radio ("on-air") baud rate only in the Packet operating mode for each Radio Port.  The default is 1200 bits/sec (VHF) for Radio Port 1 and 0 (disabled) on Radio Port 2.

HBAUD has no relationship to your computer terminal program's baud rate. Available HDLC packet data rates "n" include 45, 50, 57, 75, 100, 110, 150, 200, 300, 400, 600, 1200, 2400, 4800 and 9600 bauds.


### HEAderln ON|OFF                                                     Default: ON
**Mode:** Packet    Host: HD
**Source:** (PDF p.231)
**Parameters:**
- ON   -    The header in a monitored packet is printed on a separate line from the text.
- OFF  -    The header and text of monitored packets are printed on the same line.
**Description:**
When HEADERLN is ON (default), the address is shown, followed by a <CR><LF> that puts the packet text on a separate line as shown below:

WX1AAA>WX2BBB: Go ahead and transfer the file.

HEADERLN affects the display of monitored packets.  When HEADERLN is OFF, the address information is shown on the same line as the packet text as shown below:

WX1AAA>WX2BBB: Go ahead and transfer the file.


### Help                                                                Immediate Command
**Mode:** Command    Host: Not Supported
**Source:** (PDF p.232)
**Description:**
While in Command Mode, type the command "H" to read the abbreviated on-line HELP file.  Your monitor displays the following brief list:

Help: AScii    AMtor     PAcket BAudot     ARq       Connect MOrse      AList     Disconn DISPlay    FEc       MHeard CALibrate  AChg      CStatus NAvtex   SIgnal    FAx       TDm ANalog PACTOr   PTConn    PTSend    PTList DIRect   2DIRect   VOltage CONVerse Trans Opmaode  Xmit      Rcve      Lock RESTART  TESET     MDCHECK   TClear

You can enter Command Mode at any time to list the HELP text.


### HEReis "n"                                                          Default: $02 <CTRL-B>
**Mode:** Baudot, ASCII, AMTOR and PACTOR    Host: HR
**Source:** (PDF p.232)
**Parameters:**
- "n"   -   Is the hex representation ($01-$7F) of the character that causes the AAB string to be sent in the middle of transmitted text.
**Description:**
If you wish to send your own AAB string for identification during a transmission simply enter the HEREIS character (default <CTRL-B>).  Also see the command AAB.


### HId ON|OFF/ON|OFF                                                   Default: OFF/OFF
**Mode:** Packet    Host: HI
**Source:** (PDF p.232)
**Parameters:**
- ON   -    Your PK-900 sends HDLC identification as a digipeater.
- OFF  -    Your PK-900 does not send HDLC identification.
**Description:**
Set HID ON to force your PK-900 to send an ID packet every 9.5 minutes when it's being used as a digipeater.  Otherwise leave HID OFF (default). HID is settable for either Radio Port on the PK-900 since it is not likely both ports will  be performing the digipeater function.

This identification consists of a UI-frame with your station identification (MYCALL) and MYALIAS in the data field.  The packet is addressed to "ID".

NOTE:     You cannot change the 9.5-minute automatic interval timing.


### HOMebbs call                                                        Default: (none)
**Mode:** Packet/MailDrop    Host: HM
**Source:** (PDF p.233)
**Parameters:**
- call  -   Call Sign of your HOME BBS with which you have made prior arrangements to Auto-Forward.
**Description:**
This is the Call Sign of your local or HOME BBS that you will use for Reverse Forwarding messages.  You must make special arrangements with the system operator of this BBS to set you up for Reverse Forwarding.  The SSID is not compared when matching HOMEBBS to the source call sign of an incoming packet.


### HOST "n"                                                            Default: 0
**Mode:** All    Host: HO
**Source:** (PDF p.233)
**Parameters:**
- "n"   -   A hexadecimal value from $00 through $FF setting bits from the table below that define the Host operation of the PK-900.
**Description:**
The HOST command enables the "computer-friendly" HOST communications mode, over the PK-900's RS-232 link.  To cancel HOST mode, send 3-<CTRL-C> characters as if exiting the Transparent mode, or type <CTRL-A> O H O N <CTRL-W>.  Sending a Break signal will not cause the PK-900 to exit from the HOST mode.

Bit 0:    Controls whether the HOST mode is ON or OFF.

If bit 0 is equal to 0, HOST is OFF.

If bit 0 is equal to 1, HOST is ON.

Bit 1:    Controls the local MailDrop access.

If bit 1 is equal to 0, then the Maildrop Send data uses the $20 block.  Read data uses the $2F block as before.  Monitored MXMIT data uses the $3F (monitored receive) block type.

If bit 1 is equal to 1, then the MailDrop send data uses the $60 block type.  Read data uses the $70 block type. Monitored MXMIT data uses the $2F (echoed) block type to differentiate between monitored transmitted and received frames.

Bit 2:    Controls the PK-900's extended HOST Mode.

Bits 3-7 are reserved for future use.

To maintain backward compatibility with older programs written to use the ON/OFF form of the HOST command, HOST ON is equivalent to HOST $01 described above. However programmers must note that HOST now returns a numeric value and not ON or OFF as before.

See AEA's PK-232 or PK-900 Technical Manual for information on Host Mode.


### HPoll ON|OFF                                                        Default: ON
**Mode:** Host    Host: HP
**Source:** (PDF p.234)
**Parameters:**
- ON   -    The HOST Mode program must poll the PK-900 for all data (default).
- OFF  -    The HOST Mode program must accept data from the PK-900 at anytime.
**Description:**
When HPOLL is ON (default) the HOST Mode program must poll the PK-900 (using the <CTRL-A> O G G <CTRL-W>) for all data that might be available to be displayed to the screen.  When HPOLL is OFF, the HOST Mode program must be able to accept any data from the PK-900 whenever it becomes available.
