# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands G (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### GRaphics "n"                                                        Default: 1 (960 dots)
**Mode:** FAX    Host: GR
**Source:** (PDF p.230)
**Parameters:**
- "n"  -    0 to 6 selects the FAX horizontal graphics dot density printed on the printer from the table below
**Description:**
GRAPHICS determines the horizontal density of dots displayed in FAX mode. The GRAPHICS dot densities for each PRTYPE will be given with the PRTYPE command.  Graphics dot-densities as a function of PRTYPE are shown below.

Density in dots/inch (dpi) as a function of GRAPHICS and PRTYPE

GRAPHICS PRTYPE    0         1         2         3         4         5         6 0-3       60        120       120       240       80        72        90 4-7       60        120       120       240       80        72        90 8-9       60        120       144       200       80        72        90 12-19     136       240       144       160       80        72        96 20-21     60        60        60        60        60        72        100 24-27     60        120       144       240       60        72        . 28-29     60        120       .         .         .         .         . 32-35     60        120       120       240       80        72        90 36        60        60        60        60        60        60        60 40-43     60        120       120       120       60        72        144 44-47     72        144       144       72        72        72        72 48-51     80        160       80        80        80        80        80

In using the various GRAPHICS densities above, the user should be aware that not all the combinations or parameters work, especially with the slower printers (100 CPS or less).  For example, a combination of PRTYPE 2, FSPEED 4, GRAPHICS 1 and ASPECT 4 would require the printer to print a pattern of 8 dots by 960 every 3 seconds which would mean trouble for a 100 CPS printer.  On the other hand, a combination of PRTYPE 2, FSPEED 2, GRAPHICS 0 and ASPECT 2 would work, as it results in a pattern of dots 8 by 480 every 12 seconds.  We know the following combinations of dot densities and FSPEED cause trouble.

FSPEED, 8" width (narrow)      FSPEED 13" width (wide)

Dot Density         0    1    2    3    4         0    1    2    3    4 60   dpi 72   dpi                                                              s 80   dpi                                                              x 90   dpi                                                              x 96   dpi                                                         s    x 100  dpi                                                         s    x 120  dpi                                s                        x    x 136  dpi                                x                        x    x 144  dpi                                x                   s    x    x 160  dpi                           s    x         x    x    x    x    x 200  dpi                           x    x         x    x    x    x    x 240  dpi                      s    x    x         x    x    x    x    x
