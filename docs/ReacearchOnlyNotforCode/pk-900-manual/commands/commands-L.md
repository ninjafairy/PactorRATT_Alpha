# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands L (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### LAstmsg "n"                                                         Immediate Command
**Mode:** Packet MailDrop    Host: LA
**Source:** (PDF p.237)
**Parameters:**
- "n"   -   0 to 999 specifies the message number of the last MailDrop message.
**Description:**
The number 0-999 is the number of the last message sent by a remote user or the SYSOP to the MailDrop.  This command is handy for checking the last message sent to your MailDrop system.  The LASTMSG command also allows the MailDrop message counter to be set to any value, or simply reset by setting LASTMSG 0.


### LEftrite ON|OFF                                                     Default: ON
**Mode:** FAX    Host: LR
**Source:** (PDF p.237)
**Parameters:**
- ON   -    The FAX signal is scanned from left to right
- OFF  -    The FAX signal is scanned from right to left
**Description:**
Occasionally you may come across FAX images that are obviously backwards. Turning LEFTRITE OFF will reverse the scanning direction.


### LIte ON|OFF/ON|OFF                                                  Default: OFF/OFF
**Mode:** Packet    Host: LI
**Source:** (PDF p.238)
**Parameters:**
- ON   -    The PK-900 will attempt to use the HF Packet Lite extensions.
- OFF  -    The PK-900 uses AX.25 Level 2 Version 1.0 or 2.0 protocol.
**Description:**
Enables AEA's Packet Lite HF extensions to the AX.25 packet protocol.

A Packet Lite connection is established only if both stations have LITE ON. As with the AX25L2V2 command, LITE may not be changed if the TNC is in a connected state.  Setting LITE ON overrides the AX25L2V2 setting, and the unit acts as if AX25L2V2 were ON.

See the section of the Packet Chapter (Chapter 4) regarding Packet Lite operation and restrictions.


### Lock                                                                Immediate Command
**Mode:** Morse/Baudot/AMTOR/FAX    Host: LO
**Source:** (PDF p.238)
**Description:**
AMTOR and Baudot: LOCK is an immediate command used to force a LETTERS shift in the received data. This can be helpful if noise has garbled the LTRS character causing FIGURES to be displayed.

FAX: This is a manual start command for FAX.  Normally the transmitting station starts a FAX image with sync pulses so that the receiver automatically lines up with the edge of the paper.  If you tune in a signal too late, or there is so much noise that the sync pulses are not detected, you can start reception manually with the LOCK command.  If you issue a LOCK to the PK-900, you will probably need to use the JUSTIFY command to properly align the image.

Morse: LOCK is an immediate command that instructs the PK-900 to lock its timing to the current measured speed of a Morse signal.  The LOCK command may improve the PK-900's ability to decode CW signals in the presence of high noise levels.
