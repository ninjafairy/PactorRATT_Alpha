# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands B (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### BARgraph "n"                                                        Default: 0
**Mode:** Command    Host: BG
**Source:** (PDF p.202)
**Parameters:**
- "n"  -  0 to 6, selects the type of channel 1 tuning bargraph presentation.
**Description:**
Bargraph selects the type of Mark-Space presentation on the LCD 20 segment tuning display.

0  -  Discriminator Mark-Space display.  Tune for maximum separation of the active segments.  The active segments should be equidistant from the center of the display. 1  -  Morse display.  Tune for maximum deflection to the right. 2  -  Center tune display.  Tune during signals to the center of the display. 3  -  Magic eye display.  Tune so that the left and right halves of the active segments meet in the middle of the display. 6  -  Used to light all segments sequentially for self test. Use BAR 1 to 3 to exit the test.


### BAudot                                                              Immediate Command
**Mode:** Command    Host: BA
**Source:** (PDF p.202)
**Description:**
BAUDOT is an immediate command that switches the PK-900 into the Baudot mode on Radio Port 1.

Baudot RTTY operation is very common around the world, and is the basis of the telex network and most radio press, weather and point-to-point message services. The Baudot/Murray and ITA #2 character sets do not contain lower case or the special punctuation and control characters found in ASCII.  Because the Baudot/ITA #2 code requires only five information bits to define each character, it will generally suffer fewer errors than ASCII code at the same data rate.


### BBSmsgs ON|OFF                                                      Default: OFF
**Mode:** Packet    Host: BB
**Source:** (PDF p.202)
**Parameters:**
- ON  -     Makes the PK-900 status messages look like the TAPR-style output.
- OFF -     The PK-900 status messages work as before (default).
**Description:**
When BBSMSGS is ON, some of the status messages change or are suppressed which may improve operation of the PK-900 with some BBS software.  The following AEA PK-900 status messages are suppressed or changed if BBSMSGS is ON:

No "(parm) was (value)" No "(parm) now (value)" Connect messages: No "; v2; 1 unACKed" No "xxx in progress: (dest) via (digis)" No space after comma in digipeater lists "VIA" in upper case If MRPT is ON, digi paths are displayed in TAPR format No "*** connect request:" No "*** retry count exceeded" Sends carriage return before all other "***" No "(callsign) busy" message


### Beacon EVERY|AFTER "n"/EVERY|AFTER "n"                              Default: EVERY 0/EVERY 0 (00 sec.)
**Mode:** Packet    Host: BE
**Source:** (PDF p.203)
**Parameters:**
- EVERY  -  Send the beacon at regular intervals.
- AFTER  -  Send the beacon after the specified time interval without activity.
- "n"    -  0 to 250 sets beacon timing in ten-second intervals.
- "0"    -  Zero turns off the beacon (default).
**Description:**
The BEACON command sets the conditions under which your beacon will be sent on each Radio Port.

A beacon frame contains the text that you've typed into the BTEXT message in a packet addressed to the UNPROTO address.  When the keyword EVERY is specified a beacon packet is sent every "n" times ten seconds.  When AFTER is specified, a beacon is sent after "n" times ten seconds have passed without packet activity.

If you set the BEACON timing less than "90" - a value judged as too short for busy channels - you'll see the following message at each command prompt:

WARNING: BEACON too often

Use Beacons with care and consideration for other users of the Packet channel.


### BItinv "n"                                                          Default: $00
**Mode:** RTTY    Host: BI
**Source:** (PDF p.203)
**Parameters:**
- "n"   -   0 to $1F, (0 to 31 decimal) specifies a number to be exclusive-ORed with every received Baudot character.  BITINV 0 is plain text.
**Description:**
Bit inversion is used to prevent listeners from reading some commercial Baudot transmissions.  Usually either 2 or 3 bits of each character are inverted to give the appearance of an encrypted transmission.  Try different settings of BITINV on a Baudot signal after the baud rate has been determined.  If you are interested encrypted transmissions try experimenting with the 5BIT command.


### BKondel ON|OFF                                                      Default: ON
**Mode:** All    Host: BK
**Source:** (PDF p.204)
**Parameters:**
- ON   -    The sequence <BACKSPACE><SPACE><BACKSPACE> is echoed when a character is deleted from the input line.
- OFF  -    The <BACKSLASH> character <\> is echoed when a character is deleted.
**Description:**
BKONDEL determines how character deletion is displayed in Command or Converse mode.  When BKONDEL is ON (default) the <BACKSPACE><SPACE><BACKSPACE> sequence is produced which updates the video display screen erasing the character.

On a printing terminal the <BACKSPACE><SPACE><BACKSPACE> sequence will result in overtyped text.  Set BKONDEL OFF if you have a paper-output display, or if your terminal does not respond to the <BACKSPACE> character <CTRL-H>.  When BKONDEL is OFF the PK-900 displays a <BACKSLASH> for each character you delete.  You can get a display of the corrected input by typing the REDISPLAYline character.


### BRight "n"                                                          Default: 50
**Mode:** Command    Host: BR
**Source:** (PDF p.204)
**Parameters:**
- "n"  -  0 to 100  specifies the brightness of the LCD backlight.
**Description:**
The bright(ness) command controls the brightness of the LCD backlight.  The number "n" controls the duty cycle (percentage) of the backlight.  The PK-900 power consumption is dependent on the brightness of the display.  The total display backlight current, with "n"=100 is 240 ma.


### BText text                                                          Default: empty
**Mode:** Packet    Host: BT
**Source:** (PDF p.204)
**Parameters:**
- text  -   Any combination of characters up to a maximum length of 120 characters.
**Description:**
BTEXT is the content of the data portion of a beacon packet.  The default text is an empty string (no message).  When and how packet beacons are sent is discussed in more detail under the BEACON command.

Although the beacon subject is controversial in packet circles, you can use beacon texts intelligently and benefit the packet community.

o    Don't type your call sign in BTEXT - the normal packet header shows it.

o    Don't fill BTEXT with screen graphics.  Use BTEXT for meaningful

information.

o    After you've beaconed for a week or two and people know who you are, follow

the practice used by more experienced packeteers:  SET BEACON EVERY 0!

o    Use a "%," "&", "N," "NO," "NONE," or OFF as the first characters in the

text to clear the BTEXT text.
