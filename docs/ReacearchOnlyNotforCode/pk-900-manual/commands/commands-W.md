# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands W (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### WHYnot ON|OFF                                                       Default: OFF
**Mode:** Packet    Host: WN
**Source:** (PDF p.285)
**Parameters:**
- ON    -   The PK-900 generates a reason why received packets are not displayed.
- OFF   -   This function is disabled.
**Description:**
During packet operation, the PK-900 may receive many packets that are not displayed.  Turning WHYNOT on will cause the PK-900 to display a message explaining the reason the received packet was not displayed to the screen. The messages and their meanings are shown below:

PASSALL:            The received packet frame had errors, and PASSALL was off, preventing the packet from being displayed to the screen.

DCD Threshold:      The DCD LCD was off when the packet was received.

MONITOR:            The MONITOR value was set too low to receive this frame.

MCON:               MCON was set too low to receive this type of frame.

MPROTO:             MPROTO was set to off, and the received packet was probably a NET/ROM or TCP/IP frame.

MFROM/MTO:          The frame was blocked by the MFROM or MTO command.

MBX:                The call sign of the sending station does not match the call sign setting in the MBX command.

MBX Sequence:       The frame was received out of sequence, probably a retry.

Frame too long:     Incoming packet frame longer than 330 bytes.  Probably a non-AX.25 frame.

Frame too short:    Incoming packet frame shorter than 15 bytes.  Only seen if PASSALL ON.  Probably noise.

RX overrun:         Another HDLC byte was received before we could read the previous one out of the HDLC chip.


### WIdeshft ON|OFF                                                     Default: OFF
**Mode:** All    Host: WI
**Source:** (PDF p.286)
**Description:**
The WIDESHIFT command allows backwards compatibility with hostmode software designed for use with the PK-232.  This command coupled with the QWIDE command selects a modem for wide shift operation such as 850 Hz shift used on some VHF data repeaters.  Some MARS stations use 850 RTTY on HF.  Wide shift must not be used on AMTOR.


### WOrdout ON|OFF                                                      Default: OFF
**Mode:** Baudot, ASCII, AMTOR, PACTOR and Morse    Host: WO
**Source:** (PDF p.286)
**Parameters:**
- ON  -     Typed characters are held in the transmit buffer until a space, CR, LF, TAB, RECEIVE, CWID, ENQ or +? character(s) is typed.
- OFF -     Typed characters are sent directly to the transmitter.
**Description:**
Use the WORDOUT Command to choose whether or not you can edit while entering text for transmission.

When WORDOUT is ON, each character you type is held in a buffer until you type a space, a carriage return, a line feed, ENQ character ($05, <CTRL-E>) or the +?. You can edit words before the transmit buffer's contents are sent to the radio. When WORDOUT is OFF, each character you type is sent to the radio just as you typed it, without any delay.


### WRu ON|OFF                                                          Default: OFF
**Mode:** Baudot, ASCII    Host: WR
**Source:** (PDF p.286)
**Parameters:**
- ON    -   Your Auto-AnswerBack is sent after a distant station's WRU?
- OFF   -   Your Auto-AnswerBack is NOT sent after a distant station's WRU?
**Description:**
Use the WRU command in Baudot and ASCII to enable or disable your PK-900's Automatic-AnswerBack feature.

When WRU is ON, your PK-900 sends the AnswerBack on receipt of a distant station's WRU? request ("FIGS-D" or "$" in Baudot or a <CTRL-E> in ASCII). Your PK-900 keys your transmitter, sends the text stored in the AnswerBack field (AAB), then unkeys your transmitter and returns to receive.

The WRU function is defaulted ON in AMTOR as per the CCIR recommendation. It is also defaulted on in PACTOR.  To allow for special applications where it may be helpful to disable the WRU function in AMTOR or PACTOR, User Bit 9 (UBIT 9) command controls this feature.
