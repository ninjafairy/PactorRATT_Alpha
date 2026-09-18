# Chapter 7 — AMTOR and NAVTEX Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 111–131).

**Agent extract:** Enter SELCAL with `MYSELCAL` / `MYIDENT`. FEC CQ then ARQ. Break-in: `ACHG`. Listen linked ARQ: `ALIST`. `AAB` answerback. MailDrop uses the same A/B/H/J/K/L/R/S set as packet. Dual-port with packet is supported; switching-time notes and `ADELAY` are in this chapter. NAVTEX is FEC AMTOR. Host codes not printed here.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.111)

### 7.1

### 7.2

#### 7.2.1

#### 7.2.2

Overview
The DSP-2232 provides AMTOR operation on Radio Port 1 in accordance
with FCC Part 97.69 and CCIR Recommendations 476 and 625 for Mode A
(ARQ) and Mode B (FEC). AMTOR is an adaptation of the SITOR system
used in high-seas telex, which provides error detection and correction.
AMTOR has two basic modes of operation, Mode A (ARQ - Automatic
ReQuest for Reception ) and Mode B (FEC - Forward Error Correction) .
ARQ AMTOR is a handshaking protocol that allows only two stations
to communicate in a near error free fashion.
You will hear a
"chirp chirp" sound when you find two stations conversing in ARQ.
AMTOR Mode A (ARQ) is the perhaps the most error-free method of
getting messages through on HF when conditions are poor.
FEC AMTOR is similar to Baudot RTTY and is used to call CQ or to
carry on " round table" contacts.
NAVTEX is a form of FEC AMTOR that is used to send Navigational
bulletins and weather information primarily to ships at sea. Recently
it has been adopted by the ARRL to send bulletins to amateurs.
Where to Operate AMTOR
Before you can operate AMTOR, you must first know where the activity
is. Most AMTOR operation occurs on the 20-meter amateur between
14.065 and 14.085 MHz. AMTOR activity can be found on the other HF
amateur bands as well and is most often located between 65 and 90 kHz
up from the bottom of the band as it is on 20 meters.
DSP-2232 AMTOR Parameter Settinqs
AMTOR is a bit more complex than Baudot or ASCII operation. AMTOR
operating modes require SELCALL (Selective Call) codes be entered
before you can operate. There are two SELCALLs you should enter.
Entering Your SELective CALI ing Code (MYSELCAL)
This unique character sequence contains four alphabetic characters
that are derived from your callsign. The DSP-2232 automatically does
this for you just by entering your amateur callsign into the MYSELCAL
command.
If you are using an AEA PAKRATT program, follow the
instructions in the program manual for entering the command MYSELCAL.
If you are using a terminal, then Type "MYSELCAL" to load your SELCALL
into the DSP-2232 as shown below:
```text
cmd:MYSELCAL N7ML
```
The DSP-2232 will tell you,
MYSe1ca1 now NNML

### (PDF p.112)

#### 7.2.3

#### 7.2.4

#### 7.2.5

See the MYSELCAL command in the Command Summary if you are interested
in more information on the translation process.
Because the same callsign sequences are assigned in ten US district g,
it is possible that your SELCALL could be used by another station.
If
you think a station in another call district is also active on AMTOR
and is using the same SELCALL, see the MYSELCAL command for
information on how to change your Selcall.
Entering Your SELective CALI ing Code (MY IDENT)
At the present time, most of the AMTOR activity on the amateur bandg
is using the four-character SELCALL defined in CCIR 476 and described
above. The seven-character SELCALL (MY IDENT) defined in CCIR 625
solves the problem of non-unique SELCALLs by providing many more
possible SELCALLs than CCIR 476 does with only four characters.
To enter your seven-character SELCÄLL all you must do is enter your
amateur callsign. The DSP-2232 will do the translation for you.
If you are using an AEA PAKRATT program, follow the instructions in
the program manual for entering the command MY IDENT.
If you are using a terminal, then enter the following
```text
cmd:MYIDENT N7ML
```
The DSP-2232 will tell you, MY Ident now VTMFFFF
See the MY IDENT command in the Command Summary if you are interested
in more information on the translation process.
Enter the AMTOR Mode
Now that you have entered your personal MYSELCAL and MY IDENT Selective
Calling codes, you are ready to enter the AMTOR mode.
If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the AMTOR mode.
If you are using a terminal, simply type "AMTOR" or "AM" from the
Command Mode followed by the <Enter> key to enter the AMTOR mode.
The DSP-2232 responds by displaying the previous mode:
```text
Opmode
was PAcket
Opmode
now AMtor
```
Your DSP-2232's front panel STATUS display will show that you are in
the AMTOR Standby mode on Radio Port I, and the CMD LED will be lit .
HF Receiver Settings
Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your DSP-2232 through the direct FSK keying lines.
In this case, you should select the FSK or RTTY operating mode .
Adjust the volume to a comfortable listening level.

### (PDF p.113)

#### 7.2.6

Tuninq in AMTOR Stations
Tuning in AMTOR stations properly is critical to successful operation.
Since HF AMTOR stations use either 170 Hz or 200 Hz Frequency Shift
Keying to send data, tuning accuracy is very important .
FoI low the
procedure below for the best results.
Make certain your HF receiver is either in LSB or FSK depending
on your DSP-2232 set-up.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver carefully between 14.065 and 14.085 MHz (or
another band where you know there is AMTOR activity) and listen
for the "chirp chirp" of ARQ or the steady data of FEC stationg.
When in the AMTOR Standby mode as you are now, you will not
NOTE :
To print these
be able to print the "chirping" ARQ signals.
stations, you must be in the AMTOR Listen (ALIST) mode.
When you find a station, slowly vary the VFO on your receiver and
look for a display on the DSP-2232 tuning indicator as shown.
Tuned In
If the tuning indicator looks like the one below, the frequency
from your speaker is too low for the DSP-2232 to copy the
Slowly tune the V FO and make the frequency higher.
signal .
Frequency
Too Low
If the tuning indicator looks like the one below, the frequency
from your speaker is too high for the DSP-2232 to copy the
signal .
Slowly tune the VFO and make the frequency lower.
Frequency
Too High
Adjust the volume of the received signal so that the DCD LED
lights when a properly tuned FEC AMTOR station is being received.
After you have an FEC AMTOR station tuned in, you should start seeing
If you have tuned in "chirping" ARQ AMTOR
the copy on your screen.
stations, you will not print anything until you enter the ALIST mode.
If you just want to receive, see Chapter 10 on SIGNAL IDENTIFICATION.

### (PDF p.114)

### 7.3

#### 7.3.1

#### 7.3.2

Transmitter Adjustments
Make sure your DSP-2232 is adjusted for your SSB transmitter as
described in section 3.5 and 3. 5.2 of this manual before transmitting.
These are very critical adjustments.
If your DSP-2232's AFSK
level and transmitter microphone gain are not adjusted properly, other
stations will not be able to copy your s ignals. Check your plate or
collector current or the power output of your rig before transmitting.
Going On The Air
Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using. Before you transmit, you
must decide if you are going to "call CQ" or answer someone's CQ call.
Call inq CQ in FEC AMTOR
If you plan to make a CQ call, you must do so in the FEC AMTOR mode.
This is required since an ARQ AMTOR transmission requires another
station to "Link-up" with.
If you are using an AEA PAKRATT program,
see the program manual to place the DSP-2232 into FEC transmit.
If you are using a terminal or terminal program, the following will
place your DSP-2232 and transceiver into the transmit mode.
Make sure that you have selected your transmitted text to go to
Port 1 by pressing the CHSWITCH character def ined in Chapter 4
followed by a number from O through 9.
Type "FEC" then press the <Enter> key to key your transmitter and
automatically enter AMTOR FEC transmit mode.
As soon as you type the < Enter > key you will be transmitting! At this
point you are also in the CONVERSE mode and anything you type will be
sent in FEC by your transmitter .
Type in your CQ message. Make sure you include YOUR Call sign,
your four-character Selcall (MYSELCAL) as well as your seven-
character Selcall (MY IDENT) so others can respond to your CQ call.
An example is shown below:
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML)
SELCALL NNML (VTMFFFF) K
Type <CTRL-D> at the end of your CQ
( VTMFFFF)
( VTMFFFF)
( VTMFFFF)
( VTMFFFF)
The put:g both
call.
your radio and the DSP-2232 into the receive mode.
Wait a bit to see if you get a response.
If not, you can repeat
the above procedure .

### (PDF p.115)

#### 7.3.3

#### 7.3.4

#### 7.3.5

Answering an FEC AMTOR CQ
Normally when you see a station calling CQ in FEC AMTOR, you will want
to answer him using ARQ AMTOR. Remember that ARQ AMTOR is the
protocol that reduces
Let s assume you hear
If you are using
for instructions
If you are using
start a CCIR 476
the chance of transmission errors.
NNML calling CQ. To answer, do the following:
an AEA PAKRATT program, check the program manual
on starting an ARQ AMTOR contact.
a terminal simply type "ARQ NNML<Enter>" to
ARQ contact, or "ARQ VTMFFFF<Enter>" to start a
CCIR 625 ARQ contact.
After your DSP-2232 has locked or synchronized with the distant
station, you may begin your conversation.
N7ML N7ML DE YOURCAL YOURCAL... etc
ARQ AMTOR Operating Fundamentals
When you finish typing your comments or traffic to the other station
and wish the distant station to transmit to you Do Not type "KKK" or
anything like that !
Do type a plus sign immediately followed by a question mark (+?) .
is a software changeover command that switches your system from
being the "Information Sending Station" (ISS) to the " Information
Receiving Station" (IRS), and switches the distant system from being
the IRS to being the I SS. When your distant partner sees the he
knows he can begin typing comments or traffic.
NOTE :
When discussing ARQ operation, we use the terms "Information
Sending Station" ( I SS) and " Information Receiving Station "
( IRS) instead of "transmit" and " receive" since in ARQ, both
stations are rapidly switching from transmit to receive.
Don 't bother with multiple call signs and "over-to-you" routines
or "KKK" used in Baudot and ASCII RTTY operation. The system
does it all for you when you type the " + ? "
The FCC requires station identification once every ten minutes.
It's
sufficient to begin with "QRA (mycall) " or end your transmission with
"QRA (mycall) " before the "+?" changeover code, or use the <CTRL-B>
"HERE-IS" to send your own Auto-AnswerBack message
Ending an ARQ AMTOR Contact
When you 've finished your "final finals" to the distant station and
both stations are ready to end the Mode A (ARQ) contact, you can end
the contact and terminate the link in several different• ways:

### (PDF p.116)

Type <CTRL-D> to stop sending when the transmit buffer is empty.
<CTRL-D> breaks the link and returns your DSP-2232 to
Command Mode .
Type <CTRL-F> to break the link and send your Morse ID.
Your DSP-2232 sends your callsign in Morse code, and then shuts
off your transmitter.
Type <CTRL-C> to return to Command Mode, then type "R" to break
the link.
The "R" command breaks the ARQ link immediately and returns your
system to AMTOR Standby. This can be used as an " Emerqency.
Shutdown" if you need to take your transmitter off the air.

#### 7.3.6

LCD Status and Mode Indicator
The LCD STATUS indicator and the LEDs on the front of the DSP-2232 are
there to help give you the unit's status at a glance. This is
especially true in AMTOR operation. The following describe typical
STATUS indications you will see.
Type "ARQ (SELCALL of. distant station) . "
The STATUS changes to:
STATUS :
LEDs:
AMTOR ARQ Phase
SEND lit
This shows that your transmitter is in the SEND condition, in the
"phasing" part of an ARQ selective call. Your transmitter will key on
and off sending the distant station's SELCALL. As soon as your
DSP-2232 is synchronized with the distant station, the STATUS
changes to:
STATUS :
LEDs :
AMTOR ARQ Tfc
SEND lit
Verify the link by typing a few <Enter>s; watch the display. Your
traffic will now begin to flow as you type characters.
If EAS is set
ON, your typed characters are displayed as they are acknowledged by
the distant station. The STATUS will change back and forth from Idle
and T fc whenever your typing pauses.
If errors occur on the link and the distant station sends RQ (Request
for Repeat), the STATUS will show:
STATUS :
LEDs:
ERROR :
RQ:
AMTOR ARQ ERROR and/or RQ
SEND lit
Your DSP-2232 has detected errors in the signals
received from the distant station
Your DSP-2232 has received a "request for repeat "
code from the distant station

### (PDF p.117)

If the link fails and you lose synchronization with the distant
station your DSP-2232 automatically tries to re-establish
synchronization with the distant station. The STATUS changes to show:
STATUS :
LEDs:
AMTOR ARQ Phase
SEND lit
After typing FEC, your DSP-2232 displays the system status:
STATUS :
LEDs:
AMTOR FEC Idle
SEND lit
As you send your traffic the STATUS will change back and forth from
Idle to Tfc. Whenever you stop typing, the Idle status is displayed.

### 7.4

#### 7.4.1

AMTOR Operating Tips
The following "Special Function Characters"
included for AMTOR operating convenience.
Immediate Commands from the Command Mode :
and immediate commands are
"ARQ
" SELFEC
Special Function
<CTRL-B>
<CTRL-D>
<CTRL-O>
Starts Mode A selective call and forces Converse
Starts Mode B transmission and forces Converse
Starts Selective Mode B transmission
Stops sending immediately, forces AMTOR Standby
Stops transmiss ion, forces AMTOR Standby
Forces re-synchronization in ALIST (AMTOR Mode A
Listen)
Forces LETTERS case in receive
Forces FIGURES case in receive
Characters embedded in transmitted text:
Sends
Stops
Sends
Sends
Sends
Sends
Sends
your AAB string as a HERE-IS message
sending when the transmit buffer is empty
a "Who Are You" request to the other station
callsign in Morse and shuts off transmitter
FIGURES character
LETTERS character
the TIME if the DAYTIME clock has been set
ARQ Break-in (ACHG CGmand)
In Mode A (ARQ), when you ' re the "Information Receiving Station, " you
can use the "ACHG" command to interrupt the distant station's comments.
As the "Information Receiving Station, " you normally rely on the
distant station to send the " + ? " to " change-over" at the end of his
comments. ACHG is a command that forces both systems to reverse the
" Information Receiving" and "Information Sending" status of the link.
Use the ACHG command only. when really needed to interrupt the
distant station.

### (PDF p.118)

#### 7.4.2

#### 7.4.3

#### 7.4.4

#### 7.4.5

#### 7.4.6

Entering Your Auto-AnswerBack (AAB)
AMTOR allows you to request the identity
conversing with by sending your DSP-2232
DSP-2232 to send a FIGS-D request to the
For this reason, you should set your own
of the station you are
a < This causes the
other station.
Auto-AnswerBack (AAB) message
to "DE YOUR-CALL MYSELCAL MY IDENT" .
Your DSP-2232 will automatically
send the AAB message when another station requests your identity, and
then stop sending.
Operating AMTOR with Other Modem frequencies and Shifts
All Amateur (AMTOR) and commercial (SITOR) stations that we know of
use either 170 or 200 Hz shift FSK modems. Modem 1 (default) is
therefore the best choice for ARQ or FEC AMTOR use. The DSP-2232
allows other modems to be used in AMTOR should the need arise. The
following other modems may
be selected with the MODEM cornmand.
MODEM
MODEM
MODEM
MODEM
MODEM
MODEM
1
2
3
4
30:
31:
AFSK Modem,
AFSK Modem,
APSK Modem,
AFSK Modem,
170 Hz shift, Mark=2125 Hz,
170 Hz shift, Mark=1445 Hz,
425 He shift, Mark-2125 Hz,
850 Hz shift, Mark=212S Hze
Space=2295 Hz
space=1275 Hz
Space=2550 Hz
Space=2975 Hz
(Dual Port Modems)
RTTY/TOR 170 Hz: 2125/2295; p2 Packet 300 bps HF
RTTY/TOR 170 Hz: 2125/2295; p2 Packet 1200 bps VHF
Note that Modems 3 and 4 are wideshift modems and are not recommended.
Speed Change Not Permitted
In accordance with FCC 97 . 69 and international regulations, AMTOR is
operated at 100 bauds.
The DSP-2232 does not permit other speeds.
Echoing Transmitted Characters As Sent _(EAS)
EAS has special significance in ARQ AMTOR. If EAS is on, you will see
characters echoed to your screen only after your partner in the AMTOR
link, has validated them. With EAS ON, the characters appear on your
screen three at a time.
If the data scrolls across your monitor at an even rate, you can
assume that you have a good ARQ link.
If • the data hesitates or scrolls in "jerky" intermittent fashion,
that 's generally a sign that the radio link is not too good.
If the characters stop appearing on your monitor, the link is
failing or has failed. The STATUS display will tell you this.
Send inq Only Complete Words (WORDOUT}
Some AMTOR users like to have their words sent out only when they are
complete. This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character.
Turning WORDOUT ON
activates this feature.
See the Command Summary for more information.

### (PDF p.119)

#### 7.4.7

### 7.5

### 7.6

#### 7.6.1

Operating on the Wrona Sideband
In AMTOR operation it is important to be operating on the correct
sideband, otherwise other stations will not be able to copy you.
If
you find a station operating on the wrong sideband, you can reverse
your receive sense with the RXREV command.
Similarly, if someone tells you that you are on the wrong sideband,
you can correct your transmit signal sense with the TXREV command.
See the Command summary for more information on these commands .
Monitor inq ARQ AMTOR Contacts with ALIST
Use the "ALIST" command to monitor ARQ traffic flowing between two
stations linked in an ARQ contact.
Your DSP-2232 will try to
synchronize with whichever of the two linked ARQ stations is the
Information Sending Station at the moment.
Mode L Listen operation does not give you error detection or error
correction; your DSP-2232 is not one of the two stations locked to
each other.
If the other two st9tions are enjoying a good link,
you 11 probably get good copy from that link.
Your DSP•-2232 will not print a block of data if that block contains
the same information as the previous block. If the " I SS" ( Information
Sending Station) is repeating the same block, you won't print it
twice, unless receive an error.
If the stations you ' re monitoring
are sending error and RQ codes and repeating blocks of characters
across their link, you may see some repeated character blocks.
If
they ' re having link problems, the data on your screen can look very
strange indeed, although the two synchronized stations are getting
error-free copy .
AMTOR MailDrop Operat ion
The DSP-2232 allows AMTOR as well as Packet access to the MailDrop.
Messages that originate in Packet can be accessed remotely in AMTOR
and messages that originate from a remote AMTOR station can be
accessed by Packet users of your MailDrop. This section of the manual
talks about basic AMTOR mailbox operation. Section 7.7 will discuss
how to pass message traffic from AMTOR to Packet and vice versa.
Make sure that you understand MailDrop Operation in Chapter 5 and the
basic AMTOR operation described earlier in this chapter before putting
your AMTOR MailDrop on the air.
Special Operating Considerations
The AMTOR MailDrop has been designed with a "Watchdog" safety feature
If a remote
so that it may perform safely without constant attention.
station is linked with your AMTOR MailDrop and no traffic is passed
for 5 minutes, the link will drop and your transmitter will shut off.

### (PDF p.120)

#### 7.6.2

#### 7.6.3

#### 7.6.4

##### 7.6.4.1

At this time however, unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary Authorization
(STA) from the FCC for this purpose. This restriction may someday
change, but until then US amateurs must be sure to always have control
of their HF transmitters when any automatic device such as the
DSP-2232 MailDrop is in operation.
With this in mind, we have designed the AMTOR MailDrop so that it can
be disabled and then re-enabled at any time during an ARQ link simply
by turning the command TMAIL (TOR MAIL) OFF. This allows you the
SYSOP to make your MailDrop available to other stations and still
break in to chat with remote stations at any time. This could come in
handy should you want to provide some help or information to a remote
station using your AMTOR MailDrop.
Settings For AMTOR MailDrop Operation
Before a remote AMTOR user can access your MailDrop, be certain that
MYCALL (on Port 1) is set to your Amateur callsign and MYSELCAL is set
to your 4 -character AMTOR SelCa11. To allow CCIR 625 AMTOR access to
your MailDrop, your 7 -character MY IDENT must also be entered. Once
these commands have been entered, you must then enter the AMTOR mode.
Start inq AMTOR Mail Dfop Operation
Remote access to your AMTOR MailDrop is controlled by the cornrnand
TMAIL which is short for TOR MAIL. The TMAIL command controls remote
access to the AMTOR MailDrop in the same way that the MAILDROP command
controls remote Packet access.
Turn the TMAIL command ON (default OFF) to allow remote stations to
access your MailDrop in ARQ AMTOR. Turn TMAIL OFF to have normal ARQ
QSOs with other stations in the AMTOR mode.
Local Logon to the MailDrop
To locally access your MailDrop use the MDCHECK command as described
in chapter 5 of this manual on MailDrop operation.
Remote Logon to your AMTOR MailDrop
The AMTOR maildrop user interface is slightly different from the
packet interface due to the differences between the two modes.
When CODE is set to O and the ITA#2 alphabet is used in AMTOR, only
UPPER case characters are sent.
If you the SYSOP set CODE to 2
enabling the Cyrillic extensions, both upper and lower case characters
can be sent and received. See the CODE command for information and
limitations of this feature.
When a station links with your AMTOR MailDrop, your DSP-2232 first
identifies your station by sending your callsign and the amount of
free MailDrop memory as shown below:
DE WX7AAA (AEA DSP-2232) 17528 FREE.

### (PDF p.121)

#### 7.6.5

Since AMTOR transmissions do not self-identify, your MailDrop will
force the remote user to identify in one of three possible ways.
The first way is automatic:
Your MailDrop will send "STAND BY" and then the WRU request to the
remote user. Always be sure you have entered a proper Auto-Answerback
(AAB) message consisting of "QRA YOURCALL YOUR MYSELCAL YOUR MY IDENT"
as described earlier in this chapter.
The second way covers beginning AMTOR users:
AMTOR users who have not entered a proper Auto-AnswerBack response or
for some reason have the WRU feature disabled cannot be automatically
identified by your MailDrop. In this case, your MailDrop will ask the
calling station to identify as follows:
After 10 seconds your MailDrop will ask the calling station to
identify by sending "QRZ? DE "your callsign+?" to the calling station.
The calling station then has 3 minutes to respond with its callsign.
The ID must contain either "QRA" or "DE" and must end with " + ? " .
An Amateur with the call WX7BBB would send the following:
QRA WX7BBB +?
If no satisfactory IV occurs within 3 minutes from the establishment
of the link, the link is automatically shut down.
The third way covers experienced users:
Experienced AMTOR users may want to save time by simply sending QRA
followed by their callsign immediately after establishing the link.
For example station WX7BBB may simply enter the following immediately
after establishing the ARQ link.
QRA WX7BBB +?
The DSP-2232 then sends the user the MTEXT string if the MailDrop
message command (MMSG) is ON. The default text is shown below:
WELCOME TO MY AEA DSP-2232 MAILDROP.
TYPE H FOR HELP.
Caller Prompts
The command prompt that the MailDrop sends the remote user in AMTOR is
shortened from that used in the Packet mode and is shown below:
WX7BBB DE WX7AAA GA+?
TMPROMPT is the AMTOR MailDrop message prompt sent to a remote station
by your MailDrop. The default prompt is:
GA subj / GA msg,
/ EX' to end.
Text before the first slash is sent to the user as the subject prompt;
text after the slash is sent as
the message text prompt.

### (PDF p.122)

#### 7.6.6

#### 7.6.7

#### 7.6.8

##### 7.6.8.1

##### 7.6.8.2

##### 7.6.8.3

##### 7.6.8.4

##### 7.6.8.5

##### 7.6.8.6

Monitor MailDrop Operat ion
The local user (SYSOP) can monitor the dialog by setting MDMON ON.
The DSP-2232 stays in command mode during remote MailDrop access.
SYSOP MailDrop Commands
The MailDrop commands that you the SYSOP have access to are the game
as those described in Chapter 5 of the manual on MailDrop Operation.
Remote User MailDrop Commands
When a remote user has logged onto your MailDrop the following
commands are available to
The remote user may end a
the distant station:
command with either + ? or a carriage return.
A brief description of each command follows in the next sections. The
description is expanded where the command operation differs from the
Packet MailDrop section found in • Chapter 5.
(ABORT) (Remote only)
The "A" command aborts the listing or reading of messages by the
remote calling station as described in chapter 5. The difference in
AMTOR is that the remote
reverse the direction of
The remote user also has
been mis-typed by typing
(BYE)
The "B" command logs the
user must send the ACHG command first to
the link before he can issue the Abort cornmand .
the ability to abort a command that may have
" /// " on the same line as the bad command.
remote station off the MailDrop. In AMTOR
the remote station may simply gracefully shut down the link with the
RECEIVE character or the CWID character .
(HELP)
The "H" command sends the remote station a help list of the available
commands shown in Chapter 5.
(JLOG) (Remote only command)
The "J" command sent by the distant station will cause the MailDrop to
send the list of stations who have logged in to your AMTOR MailDrop.
n (KILL n [Mine))
The "K n" command deletes message number
described in Chapter 5.
(LIST [Mine l)
from the MailDrop as
The "L" command shows the remote user only a list of the messages he
or she may read as descr i. bed in Chapter 5.

### (PDF p.123)

##### 7.6.8.7

##### 7.6.8.8

(READ n [Mine)
The "R n" cornmand lets thes remote user read any of the message numbers
displayed in the LIST command. The command operates as described in
Chapter 5 except that the column headers are not displayed .
S callsign (SEND callsian)
Due to the nature of AMTOR, character errors may occur at any time, so
extra safeguards are built into the system. In AMTOR, the MailDrop
echoes the actual SEND command, then asks for conf irmation by sending
"CFM YES/NO+?" .
If the remote user's reply is "N", the MailDrop
cancels the SEND command and gives the "GA" cornmand prompt instead.
If the reply is "Y",
the message can then be sent as shown below.
In the SEND cornmand, the words "AT " "FROM" and "BID" must be used in
place of the "@,
< and "$" signs used in packet. Hierarchical
addresses are also supported in AMTOR mode, but not forwarding. You
the SYSOP may edit any message so it can be forwarded in Packet mode.
Since <CTRL-Z> is not available in the AMTOR character set, the "/EX"
command or must be used to end all AMTOR MailDrop messages.
After the " / EX" or " + ? " has been detected, the MailDrop will confirm
that the message has been sent by returning the message "FILED MSG n"
to the remote user. ön
WX7BBB DE WX7AAA GA+?
s wx2zzz at wx2yyy
S WX2zzz AT WX2YYY
18340 FREE.
CFM YES/NO
Going to the Hamfest?
GA MSG, '/EX' TO END.+?•
example of sending a message is shown below:
I haven't heard from you and
you are going to the Hamfest
Hope to see you there. 73
/ ex
WX7BBB DE WX7AAA FILED MSG 1
wondered if
next month?
{MailDrop prompt
{User' s SEND command}
{MailDrop echoes SEND cotnmand
and awaits confirmation}
{User confirms}
{MailDrop Subject prompt}
{User enters Subject}
{MailDrop Send prompt}
{Message text}
{Message text}
{Message text}
{User ends message}
{MailDrop prompt

### (PDF p.124)

### 7.7

#### 7.7.1

Simultaneous AMTOR and Packet Operation
Your DSP-2232 can operate AMTOR on Radio Port 1 and HF or VHF Packet
on Radio Port 2 at the same time. With this feature you won't miss
any local Packet activity while operating AMTOR.
Before the second radio port can be used for packet operation, a
modem must be loaded that can access the second port. There are two
types of RTTY /AMTOR modems available. The f irst type is a single
port modem which disables
packet operation on radio port 2 .
are listed
MODEM
MODEM
MODEM
MODEM
below.
1 AFSK Modem,
2 AFSK Modem,
3 AFSK Modem,
4 AFSK Modem,
170 Hz shift,
170 Hz shift,
425 Hz shift,
850 Hz shift,
Mark=2125 Hz,
Mark=1445 Hz,
Mark=2125 Hz,
Mark=2125 Hz,
Space-
Space-
Space=
Space-
These
-2295 Hz
-1275 Hz
2550. Hz
-2975 Hz
The second type of modem is "Dual Ported", that is allows for
RTTY /AMTOR operat ion on radio port 1 and
packet operation on radio
port 2 at the same time. The dual port RTTY modems available in the
DSP-2232 are listed below.
MODEM 30: RTTY/TOR 170 Hz:
MODEM 31: RTTY/TOR 170 Hz:
Selecting and Load inq Modems
2125/2295; p2 Packet 300 bps HF
2125/2295; p2 Packet 1200 bps VHF
The various modems available in the DSP-2232 can be seen with the
DIRECT (or y) command. To display all the available modems s imply
enter the Command Mode of the DSP-2232 and then type DIR as shown.
DIR
The DSP-2232 will respond with the following:
(920716)
1:
3:
10:
12:
14:
16:
18:
22:
25:
30:
31:
33:
35:
40:
42:
44:
46:
51:
60:
cmd :
RTTY /TOR 170: 2125/2295
RTTY/TOR 425: 2125/2550
pl Packet 300 bps HF 2110/2310
pl Packet 1200 bps VHF
pl Packet 1200 bps PSK
pl Packet 4800 bps PACSAT
pl Packet 9600 FSK K9NG/G3RUH
p2 Packet 1200 bps VHF
p2 Packet 2400 bps V. 26B
2:
4:
II:
13:
15:
17:
20:
23:
28:
RTTY/TOR 170: 2125/2295; p2 Packet
RTTY/TOR 170: 2125/2295; p2 Packet
pl Packet 300 bps HF 2110/2310; p2
RTTY/TOR 170: 1445/1275
RTTY /TOR 850: 2125/2975
pl Packet 300 bps HF 1460/1260
pl Packet 1200 bps PACSAT
pl Packet 2400 bps V. 26B
pl Packet 4800 bps PSK
p2 Packet 300 bpg HF 2110/2310
p2 Packet 1200 bps PACSAT
p2 Packet 9600 FSK K9NG/G3RUH
300 bps HF 2110/2310
1200 bps VHF
Packet 1200 bps VHF
pl Packet 1200 bps VHF; p2 Packet 1200 bps VHF
Morse 750 Hz
Analog FAX APT
DSP data 400 bps OSCAR-13
DSP data Spectrum
pl Packet 2400 bps MSK
p2 Packet 1200 bps MSK
41:
43:
45:
50:
52:
61:
Analog FAX HF
Analog SSTV
RTTY/TOR 1200 ASCII OSCAR-II
pl Packet 1200 bps MSK
pl Packet 9600 G3RUH U022 eq
p2 Packet 2400 bps MSK

### (PDF p.125)

#### 7.7.2

#### 7.7.3

Any modem from the list may be loaded with the MODEM command, but
only the RTTY / TOR modems 1 isted will operate in AMTOR. For example,
to operate AMTOR on radio port 1 and 1200 bps VHF packet on radio
port 2 you must load modem 31. To load modem 31, first enter the
Command Mode of the DSP-2232 and then type MODEM 31 as shown below:
MODEM 31
The DSP-2232 will respond with the following:
MODem was 1
MODem now 31
Display inq Received Data
When a port 1 only modem is loaded, port 2 packet operation is
effectively disabled. This can be desirable when operating AMTOR
and you do not want to be disturbed with any packet signals that may
be received on radio port 2.
When a Dual Port modem such as MODEM 31 is loaded in the DSP-2232,
received data is displayed from both Radio Ports at the same time.
This allows you to operate on HF and not miss any local Packet
connects or informat ion from DX spotting nets.
The DSP-2232 sorts and displays received data from each Radio Port
using the same technique as multi-connect packet operation described
in Chapter 4.
That is, when operating on one port and the other
port becomes active, the displayed data from the inactive port is
shown. prefaced by the "channel designator" followed by a colon ( .
Recall that Radio Port 1 is designated by "logical" channels from 0-9
and that Port 2 is designated by " logical" channels A-Z. This is
true whether a sir-gle port or a dual port modem is loaded.
Switching Between Ports
If you are using an AEA PAKRATT program, switching between Radio Ports
is described in the program manual.
If you are using a terminal
program, this section describes how to direct your transmitted text.
Switching between AMTOR on Port I and Packet on Port 2 is similar to
If you have not yet read through
switching between Packet and Packet .
the Switching Between Radio Ports section of Chapter 4, please do go
now and define a CHSWITCH character before reading the example below.
Recall from chapter 4 that the channels on Port I are labeled 0-9 and
the channels on Port 2 are labeled A-Z. To select Radio Port 1
(AMTOR) press the CHSWITCH character you def ined, followed by the
number O. To select Radio Port 2 (Packet), press the CHSWITCH
character, followed by a letter from A-Z.
For example, you are conversing with an AMTOR station and are in the
middle of a QSO when a station on VHF connects to you .
The following
shows how your screen would look and suggests how you might handle
such an occurrence.
The underlined text is the text that you type.

### (PDF p.126)

Hello YQ_g are print inq solid
here and have an S 7 signal.+?
THANKS BOB, YOUR ALSO A SOLID S 7
HERE AS WELL. +?
A: CONNECTED to WX7EEE
Thanks for the signal report Jim
only runninq 100 watts here. + ?
Hey Bob, I •m going to the hamfest
this weekend if you want a ride.
IAHe110 Mike am on HF AMTOR
talking to a station in Boston.
O: WELL BOB, 1 HAD BETTER BE GOING
TO
BED. WORK STARTS PRETTY EARLY 73.+0
1073 Jim was nice met ina you.
WXIAAA de WX7BBB SK <CTRL-D>
You send the AMTOR station
a signal report }
The other station responds
with a signal report
WX7EEE connects to you on
VHF (Radio Port 2)
You make another transmission
to Jim on HF AMTOR }
Your friend on VHF packet
wants to go to the hamfest }
You switch to Port 2 by
typing to answer on VHF
Jim on HF AMTOR signs off
with you.
You switch back to HF with
and sign off with Jim
As you may have noticed, communicating with different modes on the two
Radio Ports at the same time is almost identical to the method used in
Let's discuss the sample
chapter 4 for Packet and Packet operation.
QSOs above to see how the Port switching occurs.
The first text we see in the sample above is the signal report you are
sending to Jim, the HF AMTOR station you are communicating with. This
example assumes you have already set up the ARQ AMTOR contact with the
ARQ comznand discussed earlier in the chapter. When you are through
sending your signal report to Jim on AMTOR, you turn the link over to
The DSP-2232 then responds by
become the IRS by sending the "+? "
sending a blank line to the display to break up the received text.
The next text you see is your signal report received from Jim on
AMTOR.
You are in the middle of a QSO with Jim on AMTOR and all of a sudden
your friend WX7EEE connects to you. WX7EEE has connected on Radio
Port 2 (VHF) which is shown by the "A: " before the connect message.
Remember that the 26 channel designators for Radio Port 2 are A-Z.
Before you respond to WX7EEE on VHF, you send a transmission to Jim on
AMTOR telling him how much power your transmitter is running. When
you are finished with your text, you again cortunand the DSP-2232 to be
the IRS by sending a "+0 "
After making this transmission on AMTOR, you see WX7EEE on VHF has
offered you a ride to the hamfest. We know this text •is from Radio
Port 2 since the previous packet displayed by the DSP-2232 was the
"A: *** CONNECTED" message from Port 2 .

### (PDF p.127)

#### 7.7.4

Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF. This way he will understand
that it may take you a little longer to respond to his packetg.
Before you can send data to Radio Port 2, you must switch to this Port
with or the text you type will be sent to Radio Port 1 on AMTOR
when Jim turns the link over to you again.
You receive a transmission from Jim on AMTOR telling you he needs to
in front of the text showg this was
sign off to go to bed. The "O: "
received on Radio Port 1.
Now you want to sign off with Jim on HF. Again, first you must switch
to Radio Port I with the " O" since your last transmission was
Now you can make your final transmission to Jim
directed to Port 2.
ending with his callsign followed by your callsign.
This time when
you are through typing your text, you send a <CTRL-D> to the DSP-2232
which breaks the ARQ link and returns Radio Port 1 to AMTOR Standby
after the text has been sent.
More Thoughts on Port Switch inq
One problem of having more than one Radio Port is remembering which
port you are currently using. In the dual port sample QSOs above,
this was not a problem, but after it has been hours or days since you
have used your DSP-2232, you may forget which port you last used.
With AEA Pakratt Software programs, the on-screen status will alwayg
show which port you are using so this is not a problem. With other
programs, you will have to query the DSP-2232 with the CSTATUS SHORT
command. The CSTATUS command displays. the status of the logical
channels of Port 1 and Port 2 of the DSP-2232. The CSTATUS SHORT
command displays the status of the active channel and any packet
channels that are connected. After completing the sample QSOs above,
the DSP-2232 would display the following.
```text
cmd:CSTATUS S
```
Ch. A - 10 DISCONNECTED
This reminds you that Channel A is your current 1/0 channel. Any text
that you type in the Converse mode will be sent to channel A on Radio
If you had been connected to any other packet stations, the
Port 2 .
callsign and channel would have also shown in the display.
Sometimes you might not want to be bothered with anything from the
Radio Port you are not using. For these times either Radio Port may
For example, let's say that in
be turned OFF with the RADIO command.
the above example QSO you wanted to work HF packet and did not want to
be interrupted with any VHF connects. Typing the following command
would cause Radio Port 2 to be disabled.
```text
cmd:RADIO /0
RAdio was 1/2
RAdio now 1/0
```
When a DSP-2232 Radio Port is disabled, the front panel LCD •STATUS
indicator for that port will be extinguished as a reminder.

### (PDF p.128)

#### 7.7.5

##### 7.7.5.1

##### 7.7.5.2

##### 7.7.5.3

Dual Port AMTOR/Packet MailDrop Operat ion
Your DSP-2232 MailDrop will operate both on Packet and AMTOR and can
be used to allow message traffic that originates on AMTOR to be
reverse-forwarded into the Packet network. Similarly, traffic
originating on Packet may be picked up by remote stations on AMTOR.
Before you begin dual port Packet/ AMTOR MailDrop operation, be sure
that you are familiar with the operation of Packet, AMTOR and the
MailDrop as described in Chapters 4, 5 and 7 of this manual. Also be
sure that a dual port .modem such as MODEM 31 is loaded. The more
experience you have with each of these modes will help when getting up
a dual port MailDrop system.
Packet MailDrop Cornmand Sett inqs
First set up the packet side of the MailDrop (on Radio Port 2) by
getting MYCALL and MYMAIL. You may also want to enter a custom MTEXT
to let others know about the AMTOR feature. -Be sure to set 3RDPARTY ,
MDMON, MDPROMPT, and MMSG, as desired.
If you will be reverse
forwarding to a full-service BBS, you must set HOMEBBS to the callsign
of that BBS. Do not forget to turn MAILDROP ON. As a test you should
connect tc your own maildrop via a digipeater or network node to make
sure the radio link is working properly and the user prompts are what
you desire.
AMTOR MailDrop Command Sett inqs
Once the Packet side is working properly, the AMTOR side of things on
Radio Port I must be configured. First be sure that MYSELCAL and
MY IDENT are entered properly. You may want to customize the AMTOR
MailDrop prompt (TMPROMPT) or compose a message to ALL that tells
remote users about your system. Finally, remember to turn TMAIL ON
and enter the AMTOR mode of the DSP-2232 to start the AMTOR MailDrop.
Dual Port MailDrop Operat ion Notes
With the above parameters set, packet connections to the MY MAIL
callsign on Radio Port 2 will be sent to your MailDrop if you or a
If you or any other station is
remote AMTOR station is not using it .
using your MailDrop, the remote user attempting the packet connection
will be sent a "*** Busy" message.
Similarly, a remote AMTOR station linking to you will be given access
to your MailDrop provided no one else is using it.
If you or a remote
packet station is using your MailDrop, the AMTOR station may link to
you, but will not be given access to your MailDrop. For this reason,
you may wish to disable Radio Port 1 (by turning the RADIO parameter
to 0/2) when logging into your own MailDrop for maintenance. This
will prevent remote AMTOR stations from linking with you .
If you will be Reverse Forwarding messages into the Packet network, be
sure to check your MailDrop often for new messages. Remember that you
must use the Edit command in the MailDrop to select which messages
will be Reverse Forwarded.

### (PDF p.129)

Note:

### 7.8

#### 7.8.1

7--19
At this time unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Spec ial Temporary
Authorization ( STA) f rom the FCC. US amateurs must be gure
to have control of their HF transmitters when any device
such as the DSP-2232 AMTOR/Packet MailDrop is in operation.
AMTOR Switchinq-Time Considerations
For operation in AMTOR Mode A (ARQ), your transceiver or transmitter-
receiver combination must be able to change between transmit and
receive within 20 milliseconds. Most semiconductor-based radios can
easily meet this specification. Many older tube-type radios that use
electromechanical relays operate very well in AMTOR Mode A (ARQ) .
If the changeover from transmit to receive is too long, the minimum
working distance is extended; the signal to the distant station will
arrive before the station has switched back to receive. However, if
the transmitting station is further away, the transmission time over
the propagation path will delay the arrival of the signal until after
the station has switched to receive. For this reason, you may be able
to U Link with" stat ions across the country, but not across town.
If the receiving station's changeover from transmit to receive is too
slow, the transmitting station delay between "PTT" and "data send" can
be extended. See the ADELA Y command in the Command Summary to adjust
the DSP-2232's AMTOR timing characteristics to compensate for this.
Suggested AMTOR Operating Settinas
If you have trouble synchronizing with another AMTOR ARQ station, try
some of the following operating tips before calling AEA or deciding
that your radio equipment needs modif ications:
Try to work the distant station on Mode B (FEC) to establish that
the other station's system is fully functional.
- use the PTT line from your interface.
Don't use VOX control
Turn off the AGC circuit
- use the RF gain control to prevent
receiver blocking on stronger signals.
Turn off all compression or other audio processing.
Keep the AFSK audio input level to the microphone circuit as low
as possible
- avoid overdriving the audio input stages.
Disable the ALC circuit or reduce excessive ALC action; use more
effective RF antenna loading to adjust output power levels.

### (PDF p.130)

#### 7.8.2

Possible Areas for AMTOR Performance Improvement
If switching-time problems persist, you may have to make changes in
the radio to eliminate excessive time delays:
Remove large decoupl ing capacitors from the Push-To-Talk line to
allow faster PTT (transmitter) activation;
Improve power supply decoupling, especially in audio stages .
Do not use squelch.
In case you can't solve your radio's switching-time problems, please
call AEA Technical Support Department (see the front of this manual) .

### (PDF p.131)

### 7.9

NAVTEX Operation
NAVTEX is an international system which stands for NAVIGATIONAL TELEX.
It is a direct printing service designed to distribute navigational
and meteorological warnings and other urgent information to ships.
TO enter the NAVTEX mode, simply type "NAVTEX" at the command prompt .
The ARRL has also adopted this format for transmitting bulleting.
In
amateur radio this same format is starting to be referred to as AMTEX.
AMTEX transmissions can be found on ARRL bulletin frequencies.
NAVTEX is broadcast in Mode-B AMTOR (SITOR) on a frequency of 518 kHz.
NAVTEX may be selectively monitored, so you will see only information
of interest and never see the same message twice, It is this unique
feature of NAVTEX that the DSP-2232 uses with the NAVSTN and NAVMSG
commands to allow the user to monitor only messages of importance.
NAVTEX/AMTEX messages are prefaced by the characters " ZCZC" and
then a four character Preamble as diagramed below.
Zczc AA99
Serial Number 2nd Digit.
Serial Number 1st Digit-J
Message Classification (A to Z)
NÅVTEX Station Identification (A to Z)
The first character of the Preamble is a letter that identifies the
NAVTEX transmitter. Transmitter Identification letters can be any of
the characters A through Z. This 1 imits the number of NAVTEX stations
in an area to 26.
The NAVSTN Command can be used to selectively
monitor or reject certain NAVTEX transmitters.
The second character of the Preamble is the Message Classification.
The NAVMSG command is used to selectively monitor or reject any of the
message classes shown below:
NAVTEX
Navigational Warnings
A.
Meteorological Warnings (Storm Warnings)
B.
C.
Ice Reports
Search and Rescue Information
D.
Weather Forecasts
E.
Pilot Service Messages
F.
DECCA System Information
G.
LORAN-C System Informat ion
H.
I.
Omega Systems Messages
J.
SATNAV System Messages
K-Z. Reserved for future use
The exception to this is that message classes A, B and D CANNOT be
excluded and will always be copied if the transmitting station is
enabled by NAVSTN .
The last two numbers form a serial number from 00 through 99 that is
The DSP-2232 remembers the Preamble of
different for each message.
the 200 most recent messages and will not re-print a message that has
the same preamble if it has already been received without many errors.
Last page of Chapter 7 - AMTOR and NAVTEX Operation
