# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands D (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### DAYStamp ON|OFF                                                     Default: OFF
**Mode:** All    Host: DS
**Source:** (PDF p.219)
**Parameters:**
- ON   -    The DATE is included in CONSTAMP and MSTAMP displays.
- OFF  -    Only the TIME is included in CONSTAMP and MSTAMP displays.
**Description:**
DAYSTAMP activates the date in CONSTAMP and MSTAMP.  Set DAYSTAMP ON when you want a dated record of packet channel activity, or when you're unavailable for local packet operation.


### DAytime date & time                                                 Default: none
**Mode:** All    Host: DA
**Source:** (PDF p.220)
**Parameters:**
- date & time -  Current DATE and TIME to set.
**Description:**
DAYTIME sets the PK-900's internal clock current date and time.  The date & time is used in many modes and should be set when the PK-900 is powered up.

The clock is not set when the PK-900 is turned on.  The DAYTIME command displays the "?clock not set" error message until it is set as follows:

yymmddhhmm[ss]         (spaces and punctuation are allowed)

Example:  cmd:daytime 9108090659

where: yy is the last two digits of the year mm is the two-digit month code (01-12) dd is date (01-31) hh is the hour (00-23) mm is the minutes after the hour (00-59) [ss] is the optional seconds

o    Optionally the Dallas Semiconductor DS-1216C SmartWatch may be added to

the PK-900.  To install this IC carefully remove the 32K RAM IC (U39) and install the SmartWatch in the RAM socket.  Then re-install the RAM IC in the socket provided by the SmartWatch.

Be sure and remove the PK-900 Battery Jumper JP3 when the SmartWatch is installed.  The DS-1216C will automatically back up the installed RAM IC with its own battery.  Leaving the PK-900 battery jumper JP3 connected may deplete the on-board lithium battery.


### DCdconn ON|OFF                                                      Default: OFF
**Mode:** Packet/PACTOR/AMTOR KISS and RAWHDLC    Host: DC
**Source:** (PDF p.220)
**Parameters:**
- ON   -    RS-232 cable Pin 8 follows the state of the CON (or DCD) LCD.
- OFF  -    RS-232 cable Pin 8 is permanently set high (default).
**Description:**
DCDCONN defines how the DCD (Data Carrier Detect) signal affects pin 8 in the RS-232 interface to your computer or terminal.  Some programs such as PBBS software require that DCDCONN be ON.

DCDCONN also works in the RAWHDLC and KISS Modes.  In RAWHDLC and KISS Modes, no packet connections are known to the PK-900.  When DCDCONN is ON, the state of the radio DCD is sent to the RS-232 DCD pin (pin-8).  This may be necessary to some host applications that need to know when the radio channel is busy.


### DELete ON|OFF                                                       Default: OFF
**Mode:** All    Host: DL
**Source:** (PDF p.221)
**Parameters:**
- ON   -    The <DELETE> ($7F) key is used for editing your typing.
- OFF  -    The <BACKSPACE> ($08) key is used for editing your typing.
**Description:**
Use the DELETE command to select the key to use for deleting while editing. Set DELETE OFF (default) if you wish to use the <Backspace> key to edit typing mistakes.  Set DELETE ON if you wish to use the <Delete> key to edit mistakes.

See the BKONDEL command controls how the PK-900 indicates deletion.


### DFrom all,none,yes/no call1[,call2..] / all,none,yes/no call1[,call2..] Default: all/all
**Mode:** Packet    Host: DF
**Source:** (PDF p.221)
**Parameters:**
- call   -  all, none, YES list, NO list. list of up to eight call signs, separated by commas.
**Description:**
DFROM determines how each Radio Port of your PK-900 responds to stations trying to use your station as a digipeater.  DFROM is set to "all/all" when you first start your PK-900.  Type DFROM to display the ALL/NONE/YES_list/NO_list status of station's call signs whose packets will or will not be repeated.

To prevent all stations from digipeating through Radio Port 1 of your station, type DFROM NONE.

To permit one or more specific stations to digipeat through Radio Port 2 of your station, type DFROM /YES (followed by a list of calls signs).  Packets will be digipeated only from stations whose call signs are listed.

To prevent one or more specific stations to digipeat through Radio Port 1 of your station, type DFROM NO (followed by a list of call signs).  Packets will not be digipeated from stations whose call signs are listed.

Clear DFROM with "%" "&" or "OFF" as arguments, or type DFROM ALL/ALL.


### DIDdle : ON|OFF                                                     Default: ON
**Mode:** Baudot, ASCII    Host: DD
**Source:** (PDF p.221)
**Parameters:**
- ON    -   In Baudot, the PK-900 will send the LTRS character when idling. In ASCII, the PK-900 sends the NULL (00) character.
- OFF   -   No characters are sent when idling in transmit.
**Description:**
In RTTY modes, it may be desirable to continue sending data while paused at the keyboard.  With DIDDLE on, the PK-900 sends idle characters when waiting for keyboard entry.


### DIRect "n"                                                          Immediate Command
**Mode:** Packet    Host: DQ
**Source:** (PDF p.222)
**Description:**
DIRECT is an immediate command that displays a directory listing of all available PK-900 Modems.  When the DIRECT command is given, the modem list is displayed as shown below:

The DIRECT command can also take a modem number as an argument to display only the information on that particular modem.

Radio Port 1                                   Radio Port 2

1:  FSK  45 bps 170:   2125/2295      1:  Internal 200:   1070/1270 2:  FSK 100 bps 170:   2125/2295      2:  Internal 200:   2025/2225 3:  FSK  45 bps 200:   2110/2310      3:  Internal 1000:  1200/2200 4:  FSK 100 bps 200:   2110/2310      4:  Internal 1000:  1200/2200 eq 5:  FSK 100 bps 425:   2125/2550      5:  Internal 200:   1180/980 6:  FSK 100 bps 850:   2125/2975      6:  Internal 200:   1850/1650 7:  FSK 100 bps 850:   1275/2125      7:  Internal 800:   2100/1300 8:  Analog 900/2500                    8:  Internal 800:   2100/1300 eq 9:  FSK 2400 bps 800:  1300/2100      9:  Internal option:  9600 bps 10: FSK 300 bps 200:   2110/2310     10:  Modem disconnect header 11: FSK 1200 bps 1000: 1200/2200 12: Morse 750 Hz center frequency


### Disconne                                                            Immediate Command
**Mode:** Packet    Host: DI
**Source:** (PDF p.222)
**Description:**
DISCONNE is an immediate command that initiates a disconnect command to the distant station to which you are connected.  The DISCONNE command acts on the Radio Port and logical channel you last sent data to.  See the discussion in Chapter 4 on switching between logical channels and Radio Ports.

If your disconnect command is successful, your monitor will display:

*** DISCONNECTED: (call sign)

Other commands can be entered while a disconnect is in progress. New connections are not allowed until the disconnect is completed.

o    If another disconnect command is entered while your PK-900 is trying to

disconnect, your PK-900 will instantly switch to the disconnected state.


### DISPlay [class]                                                     Immediate Command
**Mode:** Command    Host: Not Supported
**Source:** (PDF p.223)
**Parameters:**
- class -   Optional parameter identifier, one of the following: (A)sync       display asynchronous port parameters (B)BS         display AMTOR and Packet MailDrop parameters (C)haracter   display special characters (F)ax         display Facsimile parameters (I)d          display ID parameters (L)ink        display link parameters (M)onitor     display monitor parameters (Q)Modem      display MODEM default parameters (R)TTY        display RTTY parameters (S)tep        display Step parameters (T)iming      display timing parameters (Z)           display the entire command/parameter list
**Description:**
DISPLAY is an immediate command.  When DISPLAY is typed without a parameter, the PK-900 responds with a short list of often used parameters.

(See also DISPLAY A,B,C,F,I,L,M,R,T,Z) Connect   Link state is: DISCONNECTED Opmode    PAcket FRack     5/5 (5 sec.) HBaud     1200/1200 MAXframe  4/4 Monitor   4/4 (UA DM C D I UI) MYcall    PK900/PK900 MYSelcal  none PACLen    128/128 RBaud     45 TXdelay   30/30 (300 msec.) Vhf       ON/ON

You can display subgroups of related parameters by specifying the optional class parameter.  For example, to display the MailDrop parameters type:

disp b 3Rdparty  OFF FREe      16996 KILONFWD  ON LAstmsg   0 MAildrop  OFF MDMon     OFF MDPrompt  Subject:/Enter message, ^Z (CTRL-Z) or /EX to end MMsg      OFF MTExt     Welcome to my AEA PK-900 maildrop. Type H for help. MYMail    none TMail     OFF TMPrompt  GA subj/GA msg, '/EX' to end.

Command names are shown with UPPER-CASE letters indicating the minimum number of characters required for the command.  The lower-case letters indicate the (optional) rest of the command name.


### DWait "n/n"                                                         Default: 16/16 (160 msec.)
**Mode:** Packet    Host: DW
**Source:** (PDF p.224)
**Parameters:**
- "n"  -    0 to 250 specifies wait time in ten-millisecond intervals.
**Description:**
Unless the PK-900 is waiting to transmit digipeated packets, DWAIT forces your PK-900 to pause DWAIT x 10 mSec after last hearing data on the channel, before it begins its transmitter key-up sequence.  DWAIT may be set for each Radio Port of the PK-900.

DWAIT is an older way collisions with digipeated packets were avoided.  These days the P-PERSISTENT method is generally used.  When the PPERSIST command is ON (default) the DWAIT timer is ignored.
