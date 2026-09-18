# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands N (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### NAVMsg all, none, Yes\No (letters)                                  Default: All
**Mode:** NAVTEX    Host: NM
**Source:** (PDF p.253)
**Parameters:**
- letters   -    all, none, YES List, NO List.  List of up to 13 letters which may or may not be separated by spaces, commas or TABs.
**Description:**
NAVMSG uses Letter arguments to determine which NAVTEX messages your PK-900 will print.  NAVTEX messages are grouped into classes by the second letter in the Preamble.  The NAVMSG Command allows ALL, NONE or a list of up to 13 letters representing message types to be Monitored or Rejected.

NAVMSG may be cleared with "%" "&" or "OFF" as arguments.


### NAVStn all, none, Yes\No (letters)                                  Default: All
**Mode:** NAVTEX    Host: NS
**Source:** (PDF p.254)
**Parameters:**
- letters   -    all, none, YES List, NO List.  List of up to 13 letters which may or may not be separated by spaces, commas or TABs.
**Description:**
The NAVSTN command uses letter arguments to determine which NAVTEX transmitting stations the PK-900 will print.  NAVTEX transmitters are identified by the 26 letters of the alphabet A-Z.  The NAVSTN Command allows ALL, NONE or a list of up to 13 letters representing NAVTEX transmitting stations to be Monitored or Rejected.

NAVSTN may be cleared with "%" "&" or "OFF" as arguments.


### NAvtex                                                              Immediate Command
**Mode:** All    Host: NA
**Source:** (PDF p.254)
**Description:**
NAVTEX is an immediate command that switches Radio Port 1 of your PK-900 into the NAVTEX receive mode.  The PK-900 can accept only, or lock-out certain message classes and transmitting stations with the NAVMSG and NAVSTN commands described above.

For logging purposes, NAVTEX mode uses the setting of DAYTIME to print the date and/or time in front of the preamble if MSTAMP and DAYSTAMP are ON.


### NEwmode ON|OFF                                                      Default: ON
**Mode:** All    Host: NE
**Source:** (PDF p.254)
**Parameters:**
- ON   -    The PK-900 automatically returns to the Command Mode at disconnect or return to receive.
- OFF  -    The PK-900 does not return to Command Mode at disconnect or return to receive.
**Description:**
Your PK-900 always switches to a data transfer mode at the time of connection, unless NOMODE is ON.  NEWMODE determines how your PK-900 behaves when the link is broken or when the state is changed from Transmit to Receive with the RECEIVE or CWID characters.

When NEWMODE is ON (default) and the link is disconnected, or if the connect attempt fails, your PK-900 returns to Command Mode.  If NEWMODE is OFF and the link is disconnected, your PK-900 remains in Converse or Transparent Mode unless you have forced it to return to Command Mode.


### NOmode ON|OFF                                                       Default: OFF
**Mode:** All    Host: NO
**Source:** (PDF p.255)
**Parameters:**
- ON   -    The PK-900 switches modes only upon explicit command.
- OFF  -    The PK-900 changes modes according to NEWMODE.
**Description:**
When NOMODE is OFF (default), your PK-900 switches modes automatically according to NEWMODE.  When NOMODE is ON your PK-900 never switches from Converse or Transparent Mode to Command Mode (or vice versa) by itself.  Only specific commands (CONVERSE, TRANS, or <CTRL-C>) change the operating mode.


### NUCr ON|OFF                                                         Default: OFF
**Mode:** All    Host: NR
**Source:** (PDF p.255)
**Parameters:**
- ON   -    <NULL> characters ARE sent to the terminal following <CR> characters.
- OFF  -    <NULL> characters ARE NOT sent to the terminal following <CR>s.
**Description:**
The NULLS command sets the number of <NULL> characters that will be sent. Some older printer-terminals require extra time for the printing head to do a carriage return and line feed.  NUCR ON solves this problem by making your PK-900 send <NULL> characters (ASCII code $00) to your computer or terminal.


### NULf ON|OFF                                                         Default: OFF
**Mode:** All    Host: NF
**Source:** (PDF p.255)
**Parameters:**
- ON   -    <NULL> characters are sent to the terminal following <LF> characters.
- OFF  -    <NULL> characters are not sent to the terminal following <LF>s.
**Description:**
Some older printer-terminals require extra time for the printing head to do a carriage return and line feed.  NULF ON solves this problem my making your PK-900 send <NULL> characters (ASCII code $00) to your computer or terminal. The NULLS command sets the number of <NULL> characters that will be sent.


### NULLs "n"                                                           Default: 0 (zero)
**Mode:** All    Host: NU
**Source:** (PDF p.256)
**Parameters:**
- "n"  -    0 to 30 specifies the number of <NULL> characters to be sent to your computer or terminal after <CR> or <LF> when NUCR or NULF are set ON.
**Description:**
NULLS specifies the number of <NULL> characters (ASCII $00) to be sent to the terminal after a <CR> or <LF> is sent.  NUCR and/or NULF must be set to indicate whether nulls are to be sent after <CR>, <LF> or both.  The null characters are sent from your PK-900 to your computer only in Converse and Command Modes.


### Nums                                                                Immediate Command
**Mode:** Baudot, AMTOR, TDM    Host: NX
**Source:** (PDF p.256)
**Description:**
In Baudot, AMTOR and TDM receive, the NUMS command, or "N" will force the PK-900 into the FIGS case.
