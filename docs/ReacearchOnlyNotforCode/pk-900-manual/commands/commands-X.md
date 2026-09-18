# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands X (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### XBaud "n"                                                           Default: 0
**Mode:** ASCII/Baudot    Host: XB
**Source:** (PDF p.287)
**Parameters:**
- "n"   -   Specifies an exact baud rate used in receiving ASCII and Baudot RTTY.
**Description:**
XBAUD enables hardware decoding of ASCII and Baudot signals using the PK-900's 8530 Serial Communications Controller IC.  This can allow the PK-900 to achieve better copy of these signals as well as allow non-standard data rates to be received.

To use XBAUD simply enter the data rate that is desired in either ASCII or Baudot modes.  For example a 20-meter Baudot RTTY enthusiast may want to set XBAUD to 45 to improve copy of weaker signals on the band.

It is important to remember that XBAUD overrides both the RBAUD (in Baudot) and ABAUD (in ASCII) as well as the inverting commands TXREV and RXREV.  This means that if XBAUD has been set to 45 for Baudot operation, it should be reset to 0 before changing modes to ASCII.  Otherwise the PK-900 will attempt to receive ASCII at 45 bauds!  To help reduce the chance of this error occurring, the PK-900 will disable XBAUD by setting it to 0 every time the SIGNAL Identification mode is used and the command OK is entered.

The XBAUD command supports data rates from 1 to 9600 bits per second although the PK-900 internal modem only supports data rates to 1200 baud.


### XFlow ON|OFF                                                        Default: ON
**Mode:** All    Host: XW
**Source:** (PDF p.287)
**Parameters:**
- ON   -    XON/XOFF (software) flow control is activated.
- OFF  -    XON/XOFF flow control is disabled - hardware flow control is enabled.
**Description:**
When XFLOW is ON, software flow control is in effect - it's assumed that the computer or terminal will respond to the PK-900's Start and Stop characters defined by the XON and XOFF commands.  Similarly, the PK-900 will respond to the computers start and stop characters defined by START and STOP.

When XFLOW is OFF, the PK-900 sends hardware flow control commands via the CTS line and is controlled via either the RTS or the DTR line.


### Xmit                                                                Immediate Command
**Mode:** Baudot/ASCII/Morse and FAX    Host: XM
**Source:** (PDF p.288)
**Description:**
XMIT is an immediate command that keys your radio's PTT line on Radio Port 1 and prepares the radio to receive outbound data and Morse characters from the PK-900.

XMIT switches your PK-900 to either Converse or Transparent Mode, depending on the setting of CONMODE.  Typing the CWID or the RECEIVE character will return you to receive.  Typing RCVE from the Command mode will also return to receive.

The XMIT Command can only be used from the Command Mode.


### XMITOk ON|OFF                                                       Default: ON
**Mode:** All    Host: XO
**Source:** (PDF p.288)
**Parameters:**
- ON   -    Transmit functions (PTT line) are active.
- OFF  -    Transmit functions (PTT line) are disabled.
**Description:**
When XMITOK is OFF, the PTT lines to your transmitter on both Radio Ports are disabled - the transmit function is inhibited.  All other PK-900 functions remain the same.  Your PK-900 generates and sends packets as requested, but does not key the radio's PTT line.

Use the XMITOK command to ensure that your PK-900 does not transmit.

Turning XMITOK OFF can be used to enable full break-in CW operation (QSK) on certain transceivers.


### XOff "n"                                                            Default: $13 <CTRL-S>
**Mode:** All    Host: XF
**Source:** (PDF p.288)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
Use XOFF to select the Stop character to be used to stop input from the computer or terminal.

The Stop character default value is <CTRL-S> for computer data transfers.


### XON "n"                                                             Default: $11 <CTRL-Q>
**Mode:** All    Host: XN
**Source:** (PDF p.289)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
XON selects the PK-900 Start character that is sent to the computer or terminal to restart input from that device.

The Start character default value is <CTRL-Q> for computer data transfers.
