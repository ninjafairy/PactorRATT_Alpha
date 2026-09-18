# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands E (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### EAS ON|OFF                                                          Default: OFF
**Mode:** Baudot, ASCII, AMTOR, PACTOR and MORSE    Host: EA
**Source:** (PDF p.224)
**Parameters:**
- ON    -   Echo characters when actually sent on the air by the PK-900.
- OFF   -   Echo characters when sent to the PK-900 by the computer.
**Description:**
The ECHO-AS-SENT (EAS) command functions in all modes except packet.  EAS lets you to choose the way data is displayed on your monitor screen or printer. To display your typing exactly as you are typing the keyboard characters or sending from a disk file, set EAS "OFF" (default).  To see the actual data being sent from your PK-900 to your radio and transmitted on the air, set EAS "ON".

When EAS is ON in Morse and Baudot RTTY, you'll see only UPPER CASE characters on your screen - the data actually transmitted to the distant station. When EAS is ON in AMTOR Mode A (ARQ), you'll see characters echoed on your screen only after the distant station has validated (Ack'd) your previous block of three characters.  In PACTOR, you will see the data blocks echoed as they are sent.  For Packet the MXMIT command should be used.

Nulls ($00) are not echoed, including the nulls produced by DIDDLE ON in ASCII.


### Echo ON|OFF                                                         Default: ON
**Mode:** All    Host: EC
**Source:** (PDF p.225)
**Parameters:**
- ON   -    Characters received from the terminal ARE echoed by the PK-900.
- OFF  -    Characters are NOT echoed.
**Description:**
The ECHO command controls local echoing by the PK-900 when in Command or Converse Mode.  Local echoing is disabled in Transparent Mode.

o    Set ECHO ON (default) if you don't see your typing appear on your display.

o    Set ECHO OFF if you see each character you type doubled.

ECHO is set correctly when you see the characters you type displayed correctly.


### ERrchar "n"                                                         Default: $5F (_)
**Mode:** AMTOR, PACTOR, Morse, NAVTEX and TDM    Host: ER
**Source:** (PDF p.225)
**Parameters:**
- "n"   -   A hexadecimal value from $00-$7F used to denote the error character used by the PK-900 for Morse, ARQ, FEC, NAVTEX and TDM.
- n is a hex value $00-7F, default $5F (underscore).  This is the character that the PK-900 displays when it receives a mutilated character in Morse, ARQ,
**Description:**
FEC, NAVTEX or TDM.  The user may wish to set this character to $2A (asterisk), $07 (bell), $20 (space) or $00 (null).  ERRCHAR ON or ER Y restores the default.


### EScape ON|OFF                                                       Default: OFF
**Mode:** All    Host: ES
**Source:** (PDF p.225)
**Parameters:**
- ON   -    The <ESCAPE> character ($1B) is output as "$" ($24).
- OFF  -    The <ESCAPE> character is output as <ESCAPE> ($1B) (default).
**Description:**
The ESCAPE command selects the character to be output when an <ESCAPE> character is to be sent to the terminal.  The ESCAPE character selection is provided because some computers and terminals interpret the <ESCAPE> character as a special command.  Set ESCAPE ON if you have an <ESCAPE> sensitive terminal to avoid unexpected results from accidentally receiving this character.
