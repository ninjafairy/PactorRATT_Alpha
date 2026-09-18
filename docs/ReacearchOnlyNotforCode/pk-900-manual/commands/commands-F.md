# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands F (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### FAx                                                                 Immediate Command
**Mode:** Command    Host: FA
**Source:** (PDF p.226)
**Description:**
FAX is an immediate command that switches Radio Port 1 of your PK-900 into the facsimile mode.  The FAX mode is available only if the maildrop (or FREE command) shows at least 3742 bytes free.  You must kill MailDrop messages until the number reaches this level if you wish to operate FAX.

When the PK-900 is in the FAX mode, Packet operation on Radio Port 2 is disabled.  Facsimile transmissions contain so much data, that there is simply not enough time to handle Packet while facsimile data is being transmitted or received.


### FAXNeg ON|OFF                                                       Default: OFF
**Mode:** FAX    Host: FN
**Source:** (PDF p.226)
**Parameters:**
- ON   -    The white and black senses are reversed
- OFF  -    The white and black senses are normal
**Description:**
One might use FAXNEG ON when receiving an image consisting mostly of black, as in a satellite photo.  In this case it might help to save your printer ribbon, as well as accentuating features such as cloud cover.

FAXNEG ON is NOT the same as RXREV ON.  RXREV reverses the entire signal, including the sync pulses.  FAXNEG keeps the sync pulses so that they can be recognized, but reverses the image data.


### FEc                                                                 Immediate Command
**Mode:** AMTOR Mode B    Host: FE
**Source:** (PDF p.226)
**Description:**
FEC is an immediate command that starts an AMTOR FEC (Mode B) transmission on Radio Port 1 of the PK-900.  Use FEC for CQ calls in AMTOR.  Be sure to include your SELCALL (MYSELCAL) and MYIDENT code in your CQ message so that the distant station can call you back in ARQ.

FEC is necessary for all round table AMTOR contacts.  When operating in FEC, let your PK-900 begin each transmission with three to five seconds of idling.  The RTTY practice of transmitting a line of RYRYRY is unnecessary on FEC.

You can signify the end of your FEC transmission by typing the changeover sign "+?," internationally recognized as the RTTY equivalent of "KKK."  However, in FEC, "+?" is not a software command.  You still have to un-key your transmitter (with the RECEIVE, <CTRL> D, or CWID, <CTRL> F, characters or the RCVE, <CTRL> C R, command) as you would in RTTY.


### Flow ON|OFF                                                         Default: ON
**Mode:** All    Host: FL
**Source:** (PDF p.227)
**Parameters:**
- ON   -    Type-in flow control IS active.
- OFF  -    Type-in flow control is NOT active.
**Description:**
When FLOW is ON (default), any character typed on your keyboard causes output from the PK-900 to the terminal to stop until any of the following occurs:

o    A packet is sent on either Port (in Converse Mode)

o    A line is completed (in Command Mode)

o    The packet length on either Port (See PACLEN) is exceeded

o    The current packet or command line is canceled

o    The redisplay-line character is typed

o    The logical packet channel or Radio Port is changed

Setting FLOW ON prevents received data from interfering with your keyboard data entry.  When FLOW is OFF, data is sent to the terminal whenever it is available.


### FRack "n/n"                                                         Default: 5/5 (5 sec.)
**Mode:** Packet    Host: FR
**Source:** (PDF p.227)
**Parameters:**
- "n"  -    1 to 15, specifying FRame ACKnowledgment timeout in 1 sec. intervals.
**Description:**
FRACK is the FRame Acknowledgment time in seconds that your PK-900 will wait for acknowledgment of a sent protocol frame before "retrying" that frame.  FRACK may be different for each Radio Port, and in fact a setting of 8 is recommended for HF packet operation.

After sending a packet requiring acknowledgment, the PK-900 waits for FRACK seconds before incrementing the retry counter and sending another frame.  If the packet address includes any digipeaters, the time between retries is adjusted to:

Retry interval (in seconds) = "n" x (2 x m + 1) (where m is the number of intermediate relay stations.)

When a packet is retried, a random wait time is added to any other wait times. This avoids lockups where two packet stations repeatedly collide with each other.


### FREe                                                                Immediate Command
**Mode:** All    Host: FZ
**Source:** (PDF p.227)
**Description:**
Typing "FREE" displays the number of usable bytes left in the MailDrop, as in "FREE 16996."  This may be useful to a Host mode application using the MailDrop. The FAX mode is only available only if the FREE command shows at least 3742 bytes free.  You must kill MailDrop messages until the number reaches this level if you wish to operate FAX.


### FRIck "n/n"                                                         Default: 0/0 (0 sec.)
**Mode:** Packet    Host: FF
**Source:** (PDF p.228)
**Parameters:**
- "n"  -    0 to 250, specifying the Frame Acknowledgment timeout for Meteor Scatter work in 10 millisecond intervals.
**Description:**
FRICK is a short version of FRACK, meant to be used in packet radio meteor scatter work.  If FRICK is 0 (default), the FRACK timer is then in use and the unit operates as before, with the retry timer in units of whole seconds.  If FRICK is 1 to 250, FRICK overrides FRACK as the unit's retry timer, and the retry timer is in units of 10 msec. up to 2500 msec. (2.5 seconds).

Unlike FRACK, FRICK does not take into account the number of digipeaters in the connect path.  FRICK assumes there are no digipeaters being used.

Note: Do not operate the unit with multiple packet connections while FRICK is active (1-250).  In contrast to FRACK, which provides one retry timer per multi-connect channel, there is only one FRICK timer for each radio port in the PK-900.  Each logical channel will try to use the same FRICK timer, causing interference to the operation of the other channels.

Due to the sporadic nature of meteor scatter work, a Master/Slave mode can be enabled in the PK-900 with User BIT 18 (UBIT 18).  When UBIT 18 OFF, Frame Acknowledge operation is as in previous firmware versions.

When UBIT 18 is ON, a master/slave relationship is established in packet radio connections.  This is done to reduce the possibility of simultaneous transmissions by both sides of a packet connection.  In this mode, the master station sends either an I-frame or a polling frame upon the expiration of FRICK (or FRACK if FRICK = 0).  The FRICK or FRACK timer then starts counting again.  The master station therefore sends packets constantly, even if all its I-frames have been acknowledged.  The slave station sends nothing, not even I-frames, until it receives a polling frame from the master.  A station becomes the master upon its transmission of a SABM (connect) frame; a station becomes the slave upon its transmission of a UA (acknowledgement of the SABM) frame.

Recommended settings for this method of meteor scatter work (both stations should use these settings):

UBIT 18 ON RETRY 0 AX25L2V2 ON  (default) MAXFRAME 1 (CHECK doesn't matter) FRICK n, where n is large enough to allow the other station time to send the start of an acknowledgement frame

Note:  This is an experimental mode and we welcome any comments or suggestions you might have.  Please make them in writing and direct them to the AEA Engineering Department.  Thank You.


### FSpeed "n"                                                          Default: 2 (120)
**Mode:** FAX    Host: FS
**Source:** (PDF p.229)
**Parameters:**
- "n"  -    0 to 4 selects the FAX horizontal scan rate from the table below: 1:       1 line/Second        60 lines/Minute 2:       2 lines/Second      120 lines/Minute 3:       3 lines/Second      180 lines/Minute 4:       4 lines/Second      240 lines/Minute 0:     1.5 lines/Second       90 lines/Minute
**Description:**
You can tell the scan rate by listening to the signal.  Most weather charts are transmitted at 2 lines/Second (default), or 120 lines/Minute.  Some facsimile photographs and Japanese news is sent at 60 lines/minute.

With wide-carriage printers, the maximum print densities are reduced.  Here are the maximum print densities at various scan speeds and carriage widths:

FSPEED       (LPM)    Standard carriage      Wide carriage 0           90         183 dpi                113 dpi 1           60         275 dpi                169 dpi 2           120        138 dpi                 85 dpi 3           180         92 dpi                 56 dpi 4           240         69 dpi                 42 dpi


### FUlldup ON|OFF/ON|OFF                                               Default: OFF/OFF
**Mode:** Packet    Host: FU
**Source:** (PDF p.229)
**Parameters:**
- ON   -    Full duplex mode is ENABLED.
- OFF  -    Full duplex mode is DISABLED.
**Description:**
When full-duplex mode is OFF (default), the PK-900 makes use of the DCD (Data Carrier Detect) signal from its modem to avoid collisions.  FULLDUP may be set independently for each Radio Port.

When full-duplex mode is ON the PK-900 ignores the DCD signal and acknowledges packets individually.

Full-duplex operation is useful for full-duplex radio operation, such as through OSCAR satellites.  It should not be used unless both your station and the distant station can operate in full-duplex.
