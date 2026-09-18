# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands Q (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### QHpacket "n/n"                                                      Default: 10/2 (Modems 10/2)
**Mode:** Packet    Host: QH
**Source:** (PDF p.262)
**Parameters:**
- "n/n"  -    Modem numbers to be selected when the HF Packet mode is entered.
**Description:**
QHPACKET sets the PK-900 modem that will automatically selected when the HF Packet mode is entered.  To enter the HF packet mode, the VHF command must be OFF for the selected Radio Port.

See the PACKET and MODEM commands for more information.


### QMORse "n"                                                          Default: 12 (Modem 12)
**Mode:** Morse    Host: QO
**Source:** (PDF p.262)
**Parameters:**
- "n"  -    Modem number to be selected when the MORSE mode is entered.
**Description:**
QMORSE sets the PK-900 modem that will automatically selected when the Morse mode is entered.

See the MORSE and MODEM commands for more information.


### QRtty "n"                                                           Default: 1 (Modem 1)
**Mode:** Baudot and ASCII RTTY    Host: QR
**Source:** (PDF p.262)
**Parameters:**
- "n"  -    Modem number to be selected when the ASCII or Baudot RTTY modes are entered.
**Description:**
QRTTY sets the PK-900 modem that will automatically selected when the Baudot or ASCII RTTY modes are entered.

See the BAUDOT, ASCII and MODEM commands for more information.


### QSignal "n"                                                         Default: 2 (Modem 2)
**Mode:** Signal    Host: QS
**Source:** (PDF p.263)
**Parameters:**
- "n"  -    Modem number to be selected when the SIGNAL mode is entered.
**Description:**
QSIGNAL sets the PK-900 modem that will automatically selected when the SIGNAL Identification mode is entered.  See the SIGNAL and MODEM commands for more information.


### QTDm "n"                                                            Default: 3 (Modem 3)
**Mode:** TDM    Host: QD
**Source:** (PDF p.263)
**Parameters:**
- "n"  -    Modem number to be selected when the TDM receive mode is entered.
**Description:**
QTDM sets the PK-900 modem that will automatically selected when the TDM receive mode is entered.  See the TDM and MODEM commands for more information.


### QTor "n"                                                            Default: 2 (Modem 2)
**Mode:** AMTOR    Host: QT
**Source:** (PDF p.263)
**Parameters:**
- "n"  -    Modem number to be selected when the AMTOR mode is entered.
**Description:**
QTOR sets the PK-900 modem that will automatically selected when the AMTOR mode is entered.  See the AMTOR and MODEM commands for more information.


### QVpacket "n/n"                                                      Default: 11/4  (Modems 11/4)
**Mode:** Packet    Host: QV
**Source:** (PDF p.263)
**Parameters:**
- "n/n"  -    Modem numbers to be selected when the VHF Packet mode is entered for radio port 1/radio port 2.
**Description:**
QVPACKET sets the PK-900 modem that will automatically selected when the VHF Packet mode is entered.  To enter the VHF packet mode, the VHF command must be ON for the selected Radio Port.

If the QVpacket argument is entered as "n/n" the default modems for both radio channels will be set.  If the argument is entered as "n" the default VHF packet modem for radio channel 1 will be set.  If the argument is entered as "/n" the default VHF modem for radio channel two will be set. See the PACKET and MODEM commands for more information.


### QWide "n"                                                           Default: 7 (Modem 7)
**Mode:** All    Host: WI
**Source:** (PDF p.264)
**Parameters:**
- "n"  -  Modem number to be selected when WIde shift is selected.
**Description:**
QWide sets the PK-900 modem that will be selected when the command WIDE is set ON.  This command is for software compatability with PK-232 host mode software.
