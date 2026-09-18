# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands K (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### KILONFWD ON|OFF                                                     Default: ON
**Mode:** Packet/MailDrop    Host: KL
**Source:** (PDF p.235)
**Parameters:**
- ON    -   The PK-900 kills messages after they have been Reverse Forwarded.
- OFF   -   The PK-900 does not kill messages after Reverse Forwarding.
**Description:**
Controls the disposition of a message that has been Reverse Forwarded to the station whose call is in HOMEBBS.  If KILONFWD is ON (default), the message is killed automatically to make room for other messages.  If KILONFWD is OFF, the message's status is changed from "F" to "Y."


### KIss "n"                                                            Default: 0
**Mode:** Packet    Host: KI
**Source:** (PDF p.236)
**Parameters:**
- "n"   -   Is a HEX number from $00 (KISS disabled) through $FF that enables the KISS mode selected from the table below.
**Description:**
The KISS mode must be entered to prepare the PK-900 for KISS operation. TCP/IP and other special applications have been written that require the KISS mode be enabled to operate correctly.  For normal AX.25 Packet operation, this command should be left at 0 or OFF (default).  When KISS operation is enabled, the PK-900 no longer operates in any other modes on either Radio Port.

The KISS command, takes a numerical argument from $00 - $FF. The table below describes available KISS options.

KISS $00: KISS disabled (formerly displayed as KISS OFF) KISS $01: Standard KISS (same as KISS ON or KISS YES) KISS $03: Extended KISS KISS $07: Extended KISS + KISS polling enabled KISS $0B: Extended KISS + KISS checksum enabled KISS $0F: Extended KISS + KISS polling and checksum enabled

Note that KISS ON enables standard KISS operation for compatibility with existing applications.

Extended KISS mode adds these commands to the standard commands ($x0-$x5):

$xC signifies data to be transmitted.  Unlike the $x0 command, the $xC byte is followed by two frame ID bytes, then the data; when the TNC transmits the frame, it notifies the host application by echoing back FEND, the $xC byte, the two frame ID bytes, and FEND.

$xE is the polling command, similar to the HOST "GG" command existing in AEA products.  Polling makes multi-TNC KISS operation possible.  If KISS polling is enabled, the TNC holds received data until the host application sends the poll command.  If the TNC is holding no data, it echoes back  FEND $xE FEND.  The "x" in "$xE" must match the number in the KISSADDR command for the TNC to respond.

If KISS checksum is enabled, a checksum byte is added to the end (before the final FEND) of all KISS blocks flowing between the TNC and the host application. The checksum is the exclusive-OR of all other bytes between the FEND bytes, taken before KISS escape transpositions.  A checksum is helpful when using multiple TNCs on a marginal RS-232 link.  If the PK-900 receives a KISS block with a bad checksum, it does not transmit the data.

In KISS and Raw HDLC modes, the SYSTEM status HOST LCD will be on.


### KISSAddr "n/n"                                                      Default: 0/1
**Mode:** Packet    Host: KA
**Source:** (PDF p.237)
**Parameters:**
- "n"   -   Is a number from 0-15 signifying the KISS address of the TNC's radio port.
**Description:**
Radio port addressing is available in the high nibble of the KISS command byte. The PK-900 compares the high nibble of the KISS command byte to KISSADDR only if extended KISS mode is enabled.  In this way both radio ports of the PK-900 can be addressed when the extended KISS mode is used.  The KISSADDR default sets PK-900 radio port 1 to be KISS address 0 and radio port 2 to address 1.

If the command does not match KISSADDR, the TNC takes no action. Exception: the exit-KISS command $FF works no matter what the value of KISSADDR or the status of extended KISS mode.
