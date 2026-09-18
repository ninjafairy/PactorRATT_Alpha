# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands Z (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### ZFree                                                               Immediate Command
**Mode:** All    Host: ZF
**Source:** (PDF p.289)
**Description:**
This command is primarily of interest to HOST mode programmers.  ZFREE is an immediate read-only command that returns the amount of data RAM that is available in the PK-900.  n is the number of free internal memory blocks. Each memory block holds 28 bytes of data.

Use the ZFREE command to avoid the situation in which a host mode application sends a block of data to be transmitted, but the controller cannot issue a data acknowledgement (5F X X 00) to the host computer right away because the TNC is too full.  The application should not send data to the controller if it would cause the value of ZFREE to drop below 64.  To avoid the transmission of RNR (device busy) packets, the application should make sure ZFREE does not drop below 128.

Note that ZFREE is different from the FREE command, which shows the number of message bytes available to the maildrop.  (ZFREE times 28 is always larger than the value of FREE.)


### ZStatus                                                             Immediate Command
**Mode:** All    Host: ZS
**Source:** (PDF p.290)
**Description:**
This command is primarily of interest to HOST mode programmers.  ZSTATUS is an immediate read-only command that returns a data byte in hexadecimal indicating the status of the unit as shown in the table below.

Bit           Meaning if 0             Meaning if 1 ---           ------------             ------------ 0            Transmit character       RTTY & AMTOR characters buffer is empty          are being transmitted 1            No maildrop command      Maildrop command is in progress              in progress 2            No maildrop messages     The maildrop contains for local sysop          messages for local sysop 3-7           Reserved                 Reserved

ZSTATUS is meant to be a read-only command for use by host applications in determining the status of the unit, although the user may write a value to ZSTATUS if desired.  Bit 0 shows whether all characters have been transmitted in non-packet modes.  Bit 1 shows whether the maildrop is busy servicing a command such as Read or List.  (Note: bit 1 may show 0 before the host application has polled all the data from a Read or List command out of one of the unit's buffers.)  Bit 2 shows whether any mail has been sent to the local sysop (same indication as the front panel MAIL LCD flashing).
