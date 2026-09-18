# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands C (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### CALibrate                                                           Immediate Command
**Mode:** Command    Host: Not Supported
**Source:** (PDF p.205)
**Description:**
CALIBRATE is an immediate command that starts the AFSK transmit tone calibration routine.  The PK-900 provides a continuous on-screen display of AFSK generator tone frequencies in Hertz.  The CALIBRATE command simplifies transmitter calibration of the AFSK level for each Radio Port.

When Calibration is checked all packet connections will be lost, and the time-of-day clock will not advance until you quit the calibration routine. Commands available in the calibration routine are:

K         Toggles the PK-900's PTT and CW keying outputs between ON and OFF. Q         Quits the calibration routine. H         Toggles the generator between wide (1000 Hz) and narrow (200 Hz) shift. <SPACE>   Toggles the audio tone between "mark" (low) and "space" (high) tones. D         Toggles between transmitting a continuous tone or alternating the mark and space tones at a rate set by the radio baud (HBAUD) rate.


### CANline "n"                                                         Default: $18 <CTRL-X>
**Mode:** All    Host: CL
**Source:** (PDF p.205)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The parameter "n" is the ASCII code for the character you want to use to cancel an input line.  You can enter the code in either hex or decimal.

When you use the CANLINE character to cancel an input line in Command Mode, the line is terminated with a <BACKSLASH> character and a new prompt (cmd:) appears. When you cancel lines in Converse Mode, only a <BACKSLASH> and a new line appear.

o    You can cancel only the line you are currently typing.

o    Once <CR> or <Enter> has been typed, you cannot cancel an input line.

NOTE:     If your send-packet character is not <CR> or <Enter>, the cancelline           character cancels only the last line of a multi-line packet.


### CANPac "n"                                                          Default: $19 <CTRL-Y>
**Mode:** Packet, Command    Host: CP
**Source:** (PDF p.206)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
The parameter "n" is the ASCII code for the character you want to type in order to cancel an input packet or to cancel display output from the PK-900.

You can only cancel the packet that is being entered in CONVERSE Mode.  When you cancel a packet, the line is terminated with a <BACKSLASH> and a new line.  You must cancel the packet before typing the send-packet character.

In the COMMAND mode, this character cancels displayed output from the PK-900. Typing this character once cancels ALL output from the PK-900 to your display. Typing the cancel-output character again restores normal output.


### CASedisp "n"                                                        Default: 0 (as is)
**Mode:** Packet    Host: CX
**Source:** (PDF p.206)
**Parameters:**
- "n"  -    0 to 2 specifies how your PK-900 sends characters to your terminal.
**Description:**
CASEDISP allows you to set the case of the characters your PK-900 sends to your terminal.  CASEDISP offers three possible modes:

CASEDISP 0     "As is" - characters are not changed. CASEDISP 1     "lower" - all characters are displayed in lower case only. CASEDISP 2     "UPPER" - all characters are displayed in upper case only.

CASEDISP has no effect on your transmitted data.


### CBell ON|OFF                                                        Default: OFF
**Mode:** Packet, PACTOR and AMTOR    Host: CU
**Source:** (PDF p.206)
**Parameters:**
- ON   -    Three BELL characters <CTRL-G> ($07) are sent to your terminal with the "*** CONNECTED to or DISCONNECTED from (call sign)" message.
- OFF  -    BELLS are NOT sent with the CONNECTED or DISCONNECTED message.
**Description:**
Set CBELL ON if you want to be notified when someone connects to or disconnects from your station in Packet.  Bell characters are also sent when someone establishes a link to you in AMTOR.


### CFrom all,none,yes/no call1[,call2..] / all,none,yes/no call1[,call2..] Default: all/all
**Mode:** Packet    Host: CF
**Source:** (PDF p.207)
**Parameters:**
- call   -  all, none, YES list, NO list. List of up to 8 call signs, separated by commas.
**Description:**
CFROM determines how your PK-900 responds to connect requests from other stations on each Radio Port. CFROM is set to "all/all" when you first start your PK-900.

To reject all call requests on Radio Port 1, type CFROM NONE.  Your PK-900 sends the calling station a DM packet, or "busy signal."

To reject all call requests on both Radio Ports, type CFROM NONE/NONE. Your PK-900 sends the calling station a DM packet, or "busy signal."

To accept calls from one or more specific stations on Radio Port 2, type CFROM /YES (followed by a list of calls signs).  Connects will be accepted from stations whose call signs are listed after CFROM YES.  For example:

cmd:cfrom /yes WX1AAA,WX2BBB,WX3CCC,WX4DDD

To reject calls from one or more specific stations on Radio Port 1, type CFROM NO (followed by a list of call signs).  Connect requests will be ignored from stations whose call signs are listed after CFROM NO.

You can include optional SSIDs specified as "-n" after the call sign.  If CFROM is set to "no W2JUP", connect attempts from all SSIDs of W2JUP (W2JUP-0 through W2JUP-15) will be ignored.  If CFROM is set to "yes W2JUP-1", then only W2JUP-1 will be allowed to connect.  Clear CFROM with "%" "&" or "OFF" as arguments.


### CHCall ON|OFF                                                       Default: OFF
**Mode:** Packet    Host: CB
**Source:** (PDF p.208)
**Parameters:**
- ON   -    Call sign of the distant station IS displayed in multiple connection or multiple port packet operation.
- OFF  -    Call sign of the distant station is NOT displayed (default).
**Description:**
With CHCALL ON, the call signs of the distant stations appear after the channel identifier when you are connected to more than one packet station, or on more than one Radio Port.  When CHCALL is OFF (default), only the channel designator is displayed in multiple connection operation.

When CHCALL is OFF, the monitored activity looks like this:

:0hi John                                    {data from Radio Port 1} hello Mike how goes it? :A*** CONNECTED to N7GMF                     {data from Radio Port 2} :Amust be a dx record. ge John

When CHCALL is ON, the same contact has the additional underlined information:

:0:N7ML:hi John                         {data from Radio Port 1} hello Mike how goes it? :A:N7GMF:*** CONNECTED to N7GMF         {data from Radio Port 2} :Amust be a dx record. ge John


### CHDouble ON|OFF                                                     Default: OFF
**Mode:** Packet    Host: CD
**Source:** (PDF p.208)
**Parameters:**
- ON   -    Received CHSWITCH characters appear twice (doubled).
- OFF  -    Received CHSWITCH characters appear once (not doubled).
**Description:**
CHDOUBLE ON displays received CHSWITCH characters as doubled characters.

Set CHDOUBLE ON When operating with multiple connections or multiple Radio Ports to tell the difference between CHSWITCH characters received from other stations and CHSWITCH characters generated by your PK-900.  In the following example CHDOUBLE is ON and CHSWITCH is set to "|" ($7C):

|| this is a test.

The sending station actually transmitted:

| this is a test.

The same frame received with CHDOUBLE OFF would be displayed as:

| this is a test.


### CHeck "n/n"                                                         Default: 30/30 (300 sec.)
**Mode:** Packet    Host: CK
**Source:** (PDF p.209)
**Parameters:**
- "n"  -    0 to 250 specifies the check time in ten-second intervals.
- 0    -    Zero disables this feature.
**Description:**
CHECK sets a time-out value for a packet connection on each Radio Port if the distant station has not been heard from for CHECK times 10 seconds.

Without the CHECK feature, if your PK-900 were connected to another station and the other station disappeared, your PK-900 would remain connected indefinitely, perhaps refusing connections from other stations.

Your PK-900 tries to prevent this sort of "lockup" from occurring depending on the settings of AX25L2V2 and RECONNECT, by using the CHECK timer as follows:

o    If a Version 1 link is inactive for (CHECK times 10 seconds), your PK-900

tries to save the link by starting a reconnect sequence.  The PK-900 enters the "connect in progress" state and sends "connect request" frames.

o    If a Version 2 link (AX25L2V2 ON) is inactive and packets have not been

heard from the distant end for "n" times 10 seconds, your PK-900 sends a "check packet" to test if the link still exists to the other station.  If your PK-900 does not get an answer to the "check packet" after RETRY+1 attempts, it will attempt to reconnect to the distant station.

See the RELINK command for related information.


### CHSwitch "n"                                                        Default: $00
**Mode:** All    Host: CH
**Source:** (PDF p.209)
**Parameters:**
- "n"  -    0 to $FF (0 to 255 decimal) specifies an ASCII character code.
**Description:**
CHSWITCH selects the character used by both the PK-900 and the user to show that a new Radio Port or packet logical channel is being addressed.  Chapter 4 of this manual contains a detailed description of this procedure. DO NOT USE $30 to $39 (0 to 9).

If you plan to use both Radio Ports multiple packet connections, you MUST select a CHannel SWITCHing character.  This character will be interpreted by the PK-900 to indicate that you want to select another Radio Port or "logical" packet channel.  The vertical bar "|" ($7C) is not used often in conversations and makes a good switching character.  To make the Channel Switching character the vertical bar, simply enter the command CHSWITCH $7C.

To change the logical packet channel you are using with the PK-900, then simply type the vertical bar "|" followed by a number 0 through 9 or a letter A through Z.  The numbers 0-9 select a logical channel on Radio Port 1.  The letters A-Z indicate a channel on Radio Port 2.

See CHDOUBLE and CHCALL for further information on the use of CHSWITCH.


### CMdtime "n"                                                         Default: 10 (1000 msec.)
**Mode:** All    Host: CQ
**Source:** (PDF p.210)
**Parameters:**
- "n"  -    0 to 250  specifies TRANSPARENT and HOST Mode time-out value in 100millisecond intervals. If "n" is 0 (zero), exit from TRANSPARENT Mode requires sending the BREAK signal or interruption of power to the PK-900.
**Description:**
CMDTIME sets the time-out value in Transparent and HOST Modes.  A guard time of "n" times 10 seconds allows escape to Command Mode from Transparent Mode, while permitting any character to be sent as data.

The same Command Mode entry character COMMAND (default <CTRL-C>) is used to exit Transparent Mode, although the procedure is different than from Converse mode. Three Command Mode entry characters must be entered less than "n" times 10 seconds apart, with no intervening characters, after a delay of "n" times 10 seconds following the last characters typed.

The following diagram illustrates this timing: last           first       second      third      PK-900 now terminal       COMMAND     COMMAND     COMMAND    in Command input          character   character   character  Mode |              entry       entry       entry         | |              |           |           |             | |              |           |           |             | |<---longer--->|<-shorter->|<-shorter->|<-----n----->| than "n"      than "n"    than "n"


### CMSg ON|OFF/ON|OFF                                                  Default: OFF/OFF
**Mode:** Packet    Host: CM
**Source:** (PDF p.210)
**Parameters:**
- ON   -    The recorded CTEXT message is sent as the first packet after a connection is established by a connect request from a distant station.
- OFF  -    The text message is not sent at all.
**Description:**
CMSG (default OFF) enables or disables automatic transmission of the CTEXT message when your PK-900 accepts a connect request from another station on each Radio Port.

Set CMSG ON to give others a message when they connect to your PK-900 or invite them to leave a message on your MailDrop if you are not there.

See MTEXT for a similar MailDrop message feature.


### CODe "n"                                                            Default: 0 (International)
**Mode:** Baudot RTTY, Morse, AMTOR    Host: C1
**Source:** (PDF p.211)
**Parameters:**
- "n"   -   0 to 5 specifies a code from the list below.
**Description:**
CODE      Meaning                  Morse     Baudot    AMTOR   Packet 0        International            RX/TX     RX/TX     RX/TX      - 1        US teleprinter             -       RX/TX     RX/TX      - 2        Cyrillic                  RX       RX/TX     RX/TX      - 3        Transliterated Cyrillic   RX        RX        RX        - 4        Katakana                 RX/TX       -         -        - 5        Transliterated Katakana   RX         -         -        - 6        European                  RX         -         -       RX Note that not all the codes in the list above can be transmitted.  In the Morse, Baudot and AMTOR columns below, RX means receive only, and RX/TX means both transmit and receive are enabled.

NOTE:     FCC Part 97.69 and 97.131 calls for the use of (CODE 0) "International

Telegraph Alphabet Number 2" five unit teleprinter code.  The Baudot characters "$", "#" and "&" are NOT permitted for use by US Amateurs.

CODE 0:   International

In Morse, this means the International Morse Code. For Baudot and AMTOR, this means the ITA #2 teleprinter code, which is internationally recommended for Baudot and TOR communications and shown below:

LOWER CASE SET                UPPER CASE SET 1 2 3 4 5 6 7 8 9 0 - =       ? ? ? ? ? ? ? ? ( ) ? + Q W E R T Y U I O P ? ?       Q W E R T Y U I O P ? ? A S D F G H J K L ? '         A S D F G H J K L : ? Z X C V B N M , . /           Z X C V B N M , . ?

The following special characters are receive only and are used in non-English Morse alphabets. Morse               Receive ----                   ch -.-..                  c .-..-                  e --..-                  z --.--                  n

In response to requests from European customers, the "national" ITA#2 characters unassigned in the US have been made available for both transmission and reception.  FIGS-F, FIGS-G and FIGS-H have been assigned characters according to standard use and are shown below.  Note that some national alphabets use these characters for accented letters not appearing in English.

Baudot              ITA #2          U.S. Character            CODE 0         CODE 1 FIGS-F                |              ! FIGS-G                {              & FIGS-H                }              # 3rd-Q                 q

CODE 1:   US  Teleprinter

In Morse, this has no effect; the unit will use the International Morse Code and NOT the American Morse code.  In Baudot and AMTOR, the US teleprinter character set shown below is used.  Users of CODE 1 should be aware of the following:

The US teleprinter code (CODE 1) makes the "!", "$", "'" and "#" characters available in Baudot and AMTOR.  The WRU character, "=" and the "+" characters are lost when CODE is set to 1.  Since there is no "+" character in the CODE 1 character set there should be no way to turn over the AMTOR link and change from ISS to the IRS.  To avoid this problem, the PK-900 sends a FIGS-Z when the "+" key is pressed in AMTOR and responds to the reception of the "FIGS-Z" "?" sequence so the direction of traffic can be reversed.

US Teleprinter character set.

LOWER CASE SET                UPPER CASE SET 1 2 3 4 5 6 7 8 9 0 -         !   # $     &   ( ) Q W E R T Y U I O P           Q W E R T Y U I O P A S D F G H J K L ; '         A S D F G H J K L : ' Z X C V B N M , . /           Z X C V B N M , . ?

The differences between ITA #2 and US teleprinter codes are listed below:

Baudot              ITA #2          U.S. Character            CODE 0         CODE 1 FIGS-D               WRU             $ FIGS-F                |              ! FIGS-G                {              & FIGS-H                }              # FIGS-J               BELL            ' FIGS-S                '             BELL FIGS-V                =              ; FIGS-Z                +              " 3rd-Q                 q

Please Note that for U.S. Amateurs, the F.C.C. regulations require Baudot and AMTOR transmissions follow CCIR Recommendations which require the ITA#2 (CODE 0) be used.  Technically it is illegal to use CODE 1 on the U.S. Amateur bands. MARS operators and Amateurs outside the U.S. may however find CODE 1 useful.

CODE 2:   Cyrillic

This code causes a translation to an artificially extended ASCII, so that all received characters are converted to single ASCII characters.  The character set is one which we believe to be used presently in the USSR.

The extensions used in Morse reception are shown below:

Morse          ASCII          English  pronunciation .-.-           $71 q                  YA ---.           $7E ~                  CH                    (Morse only) ..--           $60 '                  YU --.-           $7D ]                 SHCH ----           $7B {                  SH

In Baudot and AMTOR, Russian transmitters use a third register to transmit Cyrillic characters in addition to the LTRS and FIGS.  They use LTRS to transmit the Roman alphabet.  As LTRS and FIGS characters are used to access the first and second registers, they use the BLK or NUL character (00) to access the third register.  The PK-900 displays third-register characters as lower case alphabetic characters, and all FIGS characters as in CODE 0 with the following exceptions:

Character      CODE 2         English  pronunciation FIGS-F        $7C  |                E FIGS-G        $7B  {                SH FIGS-H        $7D  }               SHCH FIGS-J        $60  '                YU 3rd-Q         $71  q                YA

If several words end in "OJ", "OW" or "OGO" the transmission is probably Russian.

There is no separate Baudot combination for the CH character.  The Russians use a "4" because the Cyrillic character for CH resembles a "4".

It is safe to leave CODE set to 2 if you are not sure which alphabet the transmitting station is using.  You will be able to see the message in either alphabet with minimal garbling, and you can then set CODE to either 0 or 3.

Another interesting side effect of being able to send and receive in CODE 2 is that it is now possible to send and receive both upper and lower case text in Baudot and AMTOR modes.  To do this, both stations must have CODE 2 enabled and of course both must be running 1992 or later firmware in their PK-900's. Other users will see only upper case characters and not be aware that anything unusual is happening, as the feature merely inserts NULL characters at strategic times.  An AEA PK-900 using CODE 2 in QSO with a unit in CODE 0 (or any other equipment) will exchange data in upper case only, with no adverse effects.

This feature may be advantageous to users of the AMTOR MailDrop who want their messages to be forwarded to the packet network.  The ability to send and receive upper and lower case characters in AMTOR should improve message readability when it is translated to Packet and vice versa.

CODE  3:  Transliterated Cyrillic

This code is similar to CODE 2, except that some characters are transliterated into English phonetic equivalents for easier reading.

CODE 2         CODE 3 w              V v              ZH h              KH c              TS ~              CH     (Morse only) {              SH ]             SHCH x              ' |              E       (RTTY only) '             YU q             YA

CODE  4:  Katakana

Katakana is the phonetic character set used in Japan for spelling out words of foreign (to Japan) origin.  The Japanese also use Katakana for Morse and some computer communication.  There are about 50 Katakana characters.  CODE 4 translates the Katakana Morse code into an 8-bit extended version of ASCII.  The characters displayed are generally in the range from $A0 to $DF, except for numerals and punctuation.  If you are using CODE 4, remember to set the PK-900 for AWLEN 8, PARITY 0 and 8BITCONV ON.

CODE  5:  Transliterated Katakana

This is similar to CODE 4, except that the extended ASCII is transliterated into English equivalents for easier reading.  The Morse characters are translated into 2- and 3-letter syllables.

CODE  6:  European

This is primarily for users with German language terminals. The differences in Morse coding are as follows:

Morse          CODE 0         CODE 6 .-.-            $5B            $5B ---.            $5C            $5C ..--            $5E            $5D .--.-           $5D

In addition, CODE 6 avoids the use of square brackets ($5B, $5D) in monitored packet headers and maildrop prompts, using parentheses instead. US ASCII square bracket characters are used as extended alphabetic characters in most languages outside of English.


### COMmand "n"                                                         Default: $03 <CTRL-C>
**Mode:** All    Host: CN
**Source:** (PDF p.215)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
COMMAND changes the Command Mode entry character (default <CTRL-C>).  Type the COMMAND character to enter Command Mode from the Converse, Transparent or HOST Modes.  The Command prompt (cmd:) appears, indicating successful entry to Command Mode.

See the CMDTIME command for related information.


### CONMode CONVERSE|TRANS                                              Default: CONVERSE
**Mode:** Packet, AMTOR and PACTOR    Host: CE
**Source:** (PDF p.215)
**Description:**
CONVERSE- Your PK-900 enters Converse Mode when a Packet connection or AMTOR Link is established. TRANS   - Your PK-900 enters Transparent Mode when a Packet connection or AMTOR Link is established. CONMODE selects the mode your PK-900 uses after entering the CONNECTED Packet state, or Linked ARQ AMTOR state.

For most packet and AMTOR operation, setting CONMODE to CONVERS (default) is most natural.


### Connect call1 [VIA call2[,call3...,call9]]                          Immediate Command
**Mode:** Packet and PACTOR    Host: CO
**Source:** (PDF p.216)
**Parameters:**
- call1  -  Call sign of the distant station to which you wish to be connected.
- call2  -  Optional call sign(s) of up to eight digipeaters via which you'll be -call9    repeated to reach the distant station.
**Description:**
Use the CONNECT command to send a Packet connect request to station "call1," directly or via one or more digipeaters (call2 through call9).  Each call sign can include an optional SSID "-n" immediately after the call sign.

The CONNECT command works on either Radio Port when the PK-900 is in the Packet mode.  Make sure that the correct Radio Port has been selected with the CHSWITCH character as described in Chapter 4 of this manual.

The part of the command line shown in brackets below is optional, and is used only when connecting through one or more digipeaters.  Type the digipeater fields in the exact sequence you wish to use to route your packets to destination station "call1."  (Don't type the brackets or quotation marks)

VIA call2[, call3...,call9]

You can type the command CONNECT at any time to check the status. If you are trying to connect to another station, you will see the message:

Link state is: CONNECT in progress

If the distant station doesn't "ack" your connect request after the number of tries in RETRY, the CONNECT attempt is canceled.  Your monitor displays:

cmd:*** Retry count exceeded *** DISCONNECTED: (call sign)

In PACTOR, the CONNECT command will return the callsign of the staton you are presently linked with.  Use the PTCONN command to issue a PACTOR connect.


### CONPerm ON|OFF                                                      Default: OFF
**Mode:** Packet    Host: CY
**Source:** (PDF p.216)
**Parameters:**
- ON   -    The connection on the current channel is maintained.
- OFF  -    The current channel can be disconnected from the other stations.
**Description:**
When ON, CONPERM forces the PK-900 to maintain the current connection, even when frames to the other station exceed RETRY attempts for an acknowledgment.

Should power to the PK-900 fail, the unit will attempt to re-establish the connection if CONPERM is ON and the battery backup in the PK-900 is enabled.


### CONStamp ON|OFF                                                     Default: OFF
**Mode:** Packet, PACTOR    Host: CG
**Source:** (PDF p.217)
**Parameters:**
- ON   -    Connect status messages ARE time stamped.
- OFF  -    Connect status messages are NOT time stamped.
**Description:**
CONSTAMP activates time stamping of *** CONNECTED status messages. If CONSTAMP is ON and DAYTIME (the PK-900's internal clock) is set, the time is sent with CONNECT and DISCONNECT messages.  For example, if the clock is set and CONSTAMP is ON, a connect and disconnect sequence appears as follows:

cmd:10:55:23  *** CONNECTED to W2JUP cmd:10:55:59  *** DISCONNECTED: W2JUP


### CONVerse ( K for short )                                            Immediate Command
**Mode:** All    Host: Not Supported
**Source:** (PDF p.217)
**Description:**
CONVERSE is an immediate command that causes the PK-900 to switch from the Command Mode into the Converse Mode.  The letter "K" may also be used.

Once the PK-900 is in the Converse Mode, all characters typed from the keyboard are processed and transmitted by your radio.  To return the PK-900 to the Command Mode, type the Command Mode entry character (default is <CTRL-C>).


### CPactime ON|OFF                                                     Default: OFF
**Mode:** Packet    Host: CI
**Source:** (PDF p.217)
**Parameters:**
- ON   -    Packet transmit timer IS used in Converse Mode.
- OFF  -    Packet transmit timer is NOT used in Converse Mode.
**Description:**
CPACTIME activates automatic, periodic packet transmission in the Converse Mode. When CPACTIME is ON, characters are packetized and transmitted periodically as if in Transparent Mode.  Local keyboard editing and display features of the Converse Mode are available.  See the PACTIME command for a discussion of how periodic packetizing works.


### CRAdd ON|OFF                                                        Default: OFF
**Mode:** Baudot RTTY    Host: CR
**Source:** (PDF p.218)
**Parameters:**
- ON   -    Send <CR CR LF> in Baudot RTTY.
- OFF  -    Send <CR LF> in Baudot RTTY (default).
**Description:**
The CRADD command permits you to set the PK-900's "newline" sequence so that an additional carriage return is ADDed automatically at the end of a typed line.

When CRADD is ON the line-end sequence is <CR><CR><LF>.  When CRADD is OFF the line-end sequence is <CR><LF>.  The double carriage return is required in some RTTY services such as MARS.  CRADD has no effect on received data.


### CStatus [Short]                                                     Immediate Command
**Mode:** Packet    Host: Not Supported
**Source:** (PDF p.218)
**Description:**
CSTATUS is an immediate command helpful in multiple connections.

When CSTATUS is typed, your monitor displays Link State of all ten logical channels on Radio Port 1 (designated 0-9) and all 26 logical channels on Radio Port 2 (designated A-Z) as well as the current input/output channel as follows:

NOT CONNECTED TO ANY STATION            CONNECTED TO TWO STATIONS cmd:cs                                  cmd:cs Ch. 0 - IO DISCONNECTED                 Ch. 0 - IO CONNECTED to WX1AAA Ch. 1 -    DISCONNECTED                 Ch. 1 -    DISCONNECTED Ch. 2 -    DISCONNECTED                 Ch. 2 -    DISCONNECTED Ch. 3 -    DISCONNECTED                 Ch. 3 -    DISCONNECTED Ch. 4 -    DISCONNECTED                 Ch. 4 -    DISCONNECTED Ch. 5 -    DISCONNECTED                 Ch. 5 -    DISCONNECTED Ch. 6 -    DISCONNECTED                 Ch. 6 -    DISCONNECTED Ch. 7 -    DISCONNECTED                 Ch. 7 -    DISCONNECTED Ch. 8 -    DISCONNECTED                 Ch. 8 -    DISCONNECTED Ch. 9 -    DISCONNECTED                 Ch. 9 -    DISCONNECTED Ch. A -    DISCONNECTED                 Ch. A -    CONNECTED to WX7BBB .                                       . .                                       . Ch. Z -    DISCONNECTED                 Ch. Z -    DISCONNECTED

CSTATUS will give a short display if desired.  CSTATUS SHORT (or CS S) displays only the current input/output channel or those channels which are connected.

This form of the command is useful to remind you which Radio Port the PK-900 considers to be active.  For example, lets say that yesterday your last Packet Connect occurred on Radio Port 2, typing the CSTATUS Short command would show the following:

Ch. A - IO DISCONNECTED

This means that if you want to send data to Radio Port 1, you must first select is with the CHSWITCH character as described in Chapter 4 of this manual.


### CText text                                                          Default: empty
**Mode:** Packet    Host: CT
**Source:** (PDF p.219)
**Parameters:**
- text  -   Any combination of characters up to a maximum of 120 characters.
**Description:**
CTEXT is the "automatic answer" text sent when CMSG is ON.  The message is sent only when another station connects to you.  A typical CTEXT message might be:

"I'm not available right now.  Please leave a message on my MailDrop."

Clear CTEXT with "%", "&", "NO", "NONE" or "OFF", or simply set CMSG OFF.

See MTEXT for a similar feature available for Packet MailDrop connections.


### CWid "n"                                                            Default: $06 <CTRL-F>
**Mode:** Baudot, ASCII, RTTY, AMTOR, FAX, PACTOR    Host: CW
**Source:** (PDF p.219)
**Description:**
The CWID command lets you change the "send CWID" control character typed at the end of your RTTY dialogue.

When the PK-900 reads this character embedded in the text or keyboard input, it switches modes and sends your call sign in Morse code, at the keying speed set by MSPEED.  As soon as your call sign has been sent in Morse, the PK-900 turns off your transmitter and returns to receive and displays the command mode prompt "cmd:".
