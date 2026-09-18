# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands V (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### Vhf ON|OFF/ON|OFF                                                   Default: ON/ON
**Mode:** Packet    Host: VH
**Source:** (PDF p.284)
**Parameters:**
- ON   -    The modem selected in QVPacket is automatically selected.
- OFF  -    The modem selected in QHPacket is automatically selected.
**Description:**
Use the VHF Command for immediate software control of the PK-900's modem selection.  This command makes changing between HF and VHF packet modems  on each radio port easier.  Remember to change HB appropriately.

Set VHF ON for VHF packet operation (default) and set VHF OFF for HF operation.


### VOltage                                                             Immediate Command
**Mode:** Command    Host: VO
**Source:** (PDF p.284)
**Description:**
The VOLTAGE command displays the result of three internal voltage measurements.  These measurements are: 1.  The internal analog reference voltage.  Nominally 6.70 volts, it may range from 6.25 to 6.95 volts. 2.  The 12 volt input.  Two numbers will be displayed which are the minimum and maximum values of the input voltage.  For a well regulated power supply, these numbers will be very nearly the same. For a wall mount transformer, the numbers will typically have a two volt difference.  The lowest value should not be below 11.5 volts. 3.  The internal 5 volt power supply.  The range of values should be between 4.6 and 5.4 volts.
