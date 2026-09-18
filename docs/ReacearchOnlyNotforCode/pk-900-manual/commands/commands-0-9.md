# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands 0-9 (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### 3Rdparty ON|OFF                                                     Default: OFF
**Mode:** Packet/AMTOR/PACTOR MailDrop    Host: 3R
**Source:** (PDF p.187)
**Parameters:**
- ON    -   The MailDrop will handle third party traffic.
- OFF   -   The MailDrop will only handle mail to or from MYCALL or MYMAIL.
**Description:**
If 3RDPARTY is ON, then remote AMTOR, PACTOR and Packet MailDrop users may leave messages for any station.


### 5Bit                                                                Immediate Command
**Mode:** Command    Host: 5B
**Source:** (PDF p.187)
**Description:**
5BIT is an immediate command allowing the user to store 5-bit Baudot transmissions received on Radio Port 1 then write a program to decrypt codes that involve bit inversion or transposition.  In 5BIT, a constant of $40 (64 decimal) is added to each received 5-bit character to make it a printable ASCII character in the range of $40-5F.  All characters are treated this way, including CR, LF, LTRS and FIGS.

When the user enters 5BIT, the PK-900 displays "OPMODE now BAUDOT".  This is not strictly true; however, the Baudot mode will be displayed on Port 1 of the LCD Status display.

RXREV, RBAUD and the MODEM number must be set properly for the monitored transmission.  SIGNAL is helpful in determining whether a transmission is 5-bit and the settings for RBAUD and RXREV, but typing OK after SIGNAL selects BAUDOT, not 5BIT.

Do not change modes directly between BAUDOT, 5BIT and 6BIT.  Go through some other mode first, such as Packet.  These commands do not function in 5BIT: BITINV, CCITT, CODE, MARSDISP, TRACE, USOS, WRU and XMIT.


### 6Bit                                                                Immediate Command
**Mode:** Command    Host: 6B
**Source:** (PDF p.187)
**Description:**
Same as 5BIT, except that the unit receives a 6-bit code and adds a constant of $30 (48 decimal) to yield a range of $30-6F.  RXREV, RBAUD and WIDESHFT must be set correctly.  SIGNAL is helpful in determining whether a transmission is 6-bit and the settings for RBAUD and RXREV, but OK will not automatically select 6BIT.

When the user enters 6BIT, the PK-900 displays "OPMODE now BAUDOT".  This is not strictly true; however, the Baudot mode will be displayed on Port 1 of the LCD Status display.

Do not change modes directly between BAUDOT, 5BIT and 6BIT.  Go through some other mode first, such as Packet.  These commands do not function in 6BIT: BITINV, CCITT, CODE, MARSDISP, TRACE, USOS, WRU or XMIT.


### 8Bitconv ON|OFF                                                     Default: OFF
**Mode:** Packet, PACTOR, ASCII    Host: 8B
**Source:** (PDF p.188)
**Parameters:**
- ON   -    The high-order bit IS NOT stripped in Converse Mode.
- OFF  -    The high-order bit IS stripped in Converse Mode. 8BITCONV permits packet and ASCII transmission of 8-bit data in Converse Mode.
**Description:**
When 8BITCONV is OFF (default), the high-order bit (bit seven) of characters received from the terminal is set to 0 before the characters are transmitted.
