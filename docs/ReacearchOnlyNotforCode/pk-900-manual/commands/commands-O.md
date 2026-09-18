# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands O (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### OK                                                                  Immediate Command
**Mode:** SIGNAL    Host: OK
**Source:** (PDF p.256)
**Description:**
OK normally follows the SIGNAL command after it has determined the class and speed of the received station.  Typing OK will change the commands RXREV, RBAUD or ABAUD and OPMODE to their proper value.  If the SIGNAL command did not reveal any useful information, typing OK produces the "?bad" error message.  Typing OK when not in the SIGNAL mode produces the "?not while in (mode)" error message.


### Opmode                                                              Immediate Command
**Mode:** Command    Host: OP
**Source:** (PDF p.256)
**Description:**
OPMODE is an immediate command that shows the PK-900's current mode of operation on Radio Port 1 as well as system status.  Opmode also displays the MORSE speed when in the Morse mode.  Use the OPMODE command at any time when your PK-900 is in the Command Mode to display the present operating mode. Here is a typical example:

cmd:o OPmode   AScii      RCVE
