# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands J (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### JUstify "n"                                                         Immediate Command
**Mode:** FAX    Host: JU
**Source:** (PDF p.235)
**Parameters:**
- "n"   -   0 to 25 specifies the number of half-inches the facsimile image will be moved closer to the edge of the paper.
**Description:**
The number 0-25 is in units of half-inches, or 1/16 of standard (8") paper width.  In most cases entering JUSTIFY n will move the image to the left.  If LEFTRITE is OFF, then JUSTIFY will move the image to the right.

For example if the left-hand edge of the image is 4-1/2 inches away from the edge of the paper, try entering JUSTIFY 8.  This will move the image 4 inches to the left.  If this is not enough, you can always enter JUSTIFY 1, which will move the image the additional half-inch to the left.

JUSTIFY should only be needed after a manual start has been issued with the LOCK command in the FAX mode.
