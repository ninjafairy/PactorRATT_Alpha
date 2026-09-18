# Chapter 12 — PACTOR Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 157–176).

**Agent extract:** `MYPTCALL` (else `MYCALL`). Enter mode: `PACTOR` or `PT` → `Opmode now PACTOr` on **radio port 1**. Unproto: `PTSEND`. Listen: `PTLIST`. Changeover: `PTOVER` default `<CTRL-Z>`. Break-in: `ACHG`. MailDrop: `TMAIL`. Dual-port: `RADIO` (example `RADIO /0` → `RAdio now 1/0`). ARQ radio swap **≤100 ms**; compensate with `ADELAY`. 200 Hz FSK (2110/2310 or 1460/1260). `UCMD 0–3` defaults 3 / 6 / 2 / 5. Host two-letter codes are **not** in this chapter.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.157)

### 12.1

### 12.2

Overview
PACTOR is a relatively new amateur data communications mode.
It was
developed in Germany by Hans-Peter Helfert, DL6MAA and Ulrich Strate,
DF4KV. PACTOR combines some of the best features of both AMTOR and
packet as well as providing a few new features.
PACTOR operates at
100 bps or 200 bps depending on radio conditions.
PACTOR also
contains a 16 bit CRC to provide near error-free operation and can
also selectively use a data compression scheme (Huffman encoding) to
increase the throughput when transmitting text.
PACTOR uses an 8-bit
word, allowing the use of the full ASCII character set .
You should
use both upper and lower case in your PACTOR transmissions.
When data blocks are repeated in the case of an error, the receiving
unit can often combine the information in the repeated blocks to
provide a good block without the need of receiving a perfect block.
This scheme is called memory ARQ.
Like AMTOR and packet, PACTOR has two basic modes of operation, an ARQ
mode (Automatic ReQuest for reception) and a non-linked mode used for
CQ calls and roundtable operation.
ARQ PACTOR is a handshaking protocol that allows two stations to
communicate in a near error-free fashion. A PACTOR ARQ exchange
consists of a 0.96 second burst of data from the information
sending station followed by a short burst from the data receiving
stat ion that is an acknowledge (ACK) or non-acknowledge (NAK) .
The NAK is sent by the receiving station when the CRC data test
indicates an error in the data block. Like packet, PACTOR is
mark-space polarity independent, although for different reasons.
The PACTOR protocol alternates the data polarity with every
transmission to reduce the effects of interference on the
received signal.
The unproto (col) mode of operation is a non-linked type of
It is used for roundtable operation or for calling
operation.
CQ. The unproto mode repeats the data blocks a selectable number
It also uses the CRC
of times and can use either 100 or 200 bps.
error check.
Where to Operate PACTOR
Before you can operate PACTOR, you must first know where the activity
occurs. Most PACTOR operation occurs on the 20-meter amateur band
PACTOR activity can be found on the
between 14.065 and 14.085 MHz.
other HF amateur bands as well and is most often located between 65
and 90 kHz up from the bottom of the band as it is on 20 meters.
On 80 meters, most PACTOR will be found between 3660 and 3690 KHz.
PACTOR is not sensitive to to the sideband used, but we recommend
using LSB as in RTTY and AMTOR operating modes.

### (PDF p.158)

### 12.3

#### 12.3.1

#### 12.3.2

DSP-2232 PACTOR Parameter Settings
PACTOR is a bit more complex than Baudot or ASCII operation. PACTOR
operation requires you to have MYPTCALL or MYCALL entered before you
If you do not enter MYPTCALL, the call in MYCALL will be
can operate.
used as the default callsign. PACTOR stations can't use the
Substation IDentification number (SSID) in MYCALL.
Entering Your callsign (MYPTCALL)
If you have not already done go, enter your callsign for PACTOR after
the command prompt ( cmd:) using the MYPTCALL command. For example,
if your call is WX5FAP, you would type:
```text
cmd:MYPTCALL WX5FAP
```
The DSP-2232 will respond with:
```text
MYPTCALL was DSP
MYPTCALL now WX5FAP
```
If you have not entered your call with the MYPTCALL command, the
DSP-2232 will default to the call in MYCALL. MYCALL does not allow
punctuation other than the dash and SSID. MYPTCALL does allow up to
8 characters and punctuation in the call. This allows you to
properly identify when operating portable, e.g. ZL/K6RFK.
If you do not enter a call using MYPTCALL or MYCALL, the DSP-2232
will not allow transmission as the default call DSP is not a valid
call . The error message "Need MYCALL" will be displayed if
transmission is attempted.
Enter the PACTOR Mode
If you are using the AEA PAKRATT for WINDOWS program, follow the
instructions in the program manual to enter the PACTOR mode. The
current AEA PAKRATT for DOS does not support PACTOR except in the
dumb terminal mode. AEA will have PC PAKRATT for DOS with PACTOR
available in later in 1993.
If you are using a terminal or a computer with a terminal emulation
program, simply type "PACTOR" or "PT" from the Command Mode followed
by the <Enter> key to enter the PACTOR mode. The DSP-2232 responds by
displaying the previous mode, for example packet :
```text
Opmode
was PAcket
Opmode
now PACTOr
```
Your DSP-2232'g front panel LCD status display will show that you are
in the PACTOR Standby mode on RADIO port 1, and the COMMAND LED will
be lit.

### (PDF p.159)

### 12.4

### 12.5

HF Receiver Settings
Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your DSP-2232 through the direct FSK keying lines. If you
are using a transceiver with a RTTY or Packet mode and you have the
DSP-2232 connected for direct FSK, keep in mind that PACTOR ugeg 200
Hz shift.
If your radio has 200 Hz shift FSK for packet use, you may
use direct FSK. If your radio has only 170 Hz shift capability, you
should use the TX audio from your DSP-2232 to drive the microphone
input of your radio. Although you may use either USB or LSB, LSB is
generally used to make mode changes easier and is reconunended.
Adjust the volume to a comfortable listening level.
Tuning in PACTOR Stations
Tuning in PACTOR stations properly is critical to successful
operation. Since PACTOR stations use 200 Hz Frequency Shift Keying
(FSK) to send data, tuning accuracy is very important.
Make certain your HF receiver is either in LSB or FSK/RTTY/Packet
depending on your DSP-2232 set-up.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Enter the command "PTLIST" for PACTOR Listen to monitor both
linked and unproto PACTOR data.
Tune your receiver carefully between 14.065 and 14.085 MHz (or
another band where you know there is PACTOR activity) and listen
for the I-second data burst of ARQ PACTOR or the steady data of
Be sure that the volume from the
the unproto mode stations.
receiver is high enough to activate the DCD LED indicator during
the PACTOR data burst .
When you find a station, slowly vary the VFO on your receiver and
look for a display on the DSP-2232 tuning indicator as shown.
Tuned In
If the tuning indicator looks like the one below, the frequency
from your speaker is too low for the DSP-2232 to copy the signal.
Slowly tune the VFO and make the frequency higher.

### (PDF p.160)

When you finish typing your comments or traffic to the other
station and wish to let the distant station transmit, you should
type "KKK" or "BTU" to let the other station know that you are
going to change the link direction.
Then, type a <CTRL-Z> (Hold the "CTRL" key down while typing
the Z) to turn the link over to the other station.
<CTRL-Z> is the character defined by the PTOVER command that
switches vour system from being the Information Sending
Station (ISS) to the Information Receiving Station (IRS) and
switches the distant system from being the information receiving
station to the information sending station.

##### 12.6.3.1

The FCC requires station identification once every ten minutes.
It's
sufficient to begin with "QRA (mycall)'t or end your transmission with
"QRA (mycall) " before the <CTRL-Z> changeover code, or use the
<CTRL-B> "HERE-IS" to send your own Auto-AnswerBack message.
For the
"HERE-IS"
command to function, you must have your AAB text entered.
See appendix A for the AAB command.
Ending an ARQ PACTOR Contact
When you 've finished your "final finals" to the distant station and
both stations are ready to end the PACTOR ARQ contact, you can end
the contact and terminate the link in several different ways:
Type <CTRL-D> to stop sending after the transmit buffer is empty.
<CTRL-D> breaks the link and returns your DSP-2232 to Command Mode
in PACTOR Standby. This is the best way to end a PACTOR contact.
Type <CTRL-F> to stop sending after the transmit buffer is empty,
send your Morse ID and return to PACTOR Standby and Command Mode.
Your DSP-2232 sends your callsign (in MYCALL) in Morse code, and
then shuts off your transmitter and returns to PACTOR Standby
and Command Mode.
This is the best way to end a contact if you
want to identify your station in Morse code as well.
Wait until all the text has been sent, then type <CTRL-C> to
return to Command Mode, then type "R" to break the link. The
DSP-2232 will go into the PACTOR Standby mode.
Type <CTRL-C> to return to Command Mode, then type "R <Enter>
R <Enter>" to break the link imrnediately! If there are
characters left in the transmit buffer, they will not be sent.
This method should only be used for an emergency shutdown as it
does not send the control signal to the other station that
informs it you are shutting down. As a result, the other station
will continue to send until its internal timer turns it off.

### (PDF p.161)

#### 12.6.4

#### 12.6.5

Path Contacts
If the station you wigh
the world, i.e. a long
is used to lengthen the
station's call with the
to contact is more than half way around
path station, a special connect command
PACTOR timing.
In this case, precede the
exclamation point :
PTCONN IN7ML
If your station doesn't link within a period of time determined
by the ARQTMO command (default 60 seconds) , your station will
stop transmitting.
LCD Status and Mode Indicators
The front panel LCD display provides mode and status information at a
glance. This is especially useful in PACTOR operation. The following
describes typical status indications you will gee.
Type "PTCONN (CALLSIGN of distant station) . "
The status changes to:
LCD :
LEDs:
PACTOR Phase,
SEND
This shows that your •transmitter is in the SEND condition, in the
"connect" part of an ARQ connect call. Your transmitter will key on
and off sending the distant station's connect request .
As soon as your DSP-2232 is synchronized with the distant station, the
status changes to:
LCD :
LEDs:
PACTOR Tfc,
SEND and CONnected
Verify the link by typing <Enter> a few times and watch the display.
Your traffic will now begin to flow as you type characters.
If EAS
(Echo As Sent) is set ON, your typed characters are displayed as they
The status will change back and forth from IDLE and T fc
are sent.
whenever your typing pauses and resumes .
When errors occur on the link and the distant station sends REQUEST
(request for repeat), the status will show:
LCD :
LEDs :
PACTOR Error and/or Rqst,
SEND and CONnected
ERROR :
REQUEST :
Your DSP-2232 has detected errors in the signals
received from the distant station.
Your DSP-2232 has received a "request for repeat "
code from the distant station.
If the link fails and you lose synchronization with the distant
station the DSP-2232 goes to standby and the status shows:
LCD :
PACTOR, stby

### (PDF p.162)

For the unproto mode:
After typing" PTSEND", to start an unproto transmission, your DSP-2232
displays the system status:
LCD :
LEDs :
PACTOR IDLE,
SEND
As you send your traffic the status will change back and forth from
IDLE to Traffic. When you stop typing, the IDLE status is displayed.

### 12.7

#### 12.7.1

PACTOR operating Tips
The following "Special Function Characters"
included for PACTOR operating convenience.
Immediate Commands from the Command Mode:
and immediate commands are
" PACTO"
"PTCONN
"PTSEND"
"R"
"PTLIST"
" PTHUFF
"PT200 IOFF>"
"PTOVER
Selects the PACTOR mode
Selects the PACTOR mode as above
Starts a linked connect and forces Converse
Starts an unproto transmission and forces Converse
Stops sending immediately, forces PACTOR Standby
Allows reception of both unproto and linked
transmissions
O - prevents compression
allows automatic use of Huffman compression
Off - prevents 200 baud operation
- allows automatic speed selection
On
Used to select the changeover character
Defaults to ($1A)
Special Function Characters embedded in transmitted text:
<CTRL-Z>
Sends your AAB string as a HERE-IS message
Stops sending after the transmit buffer is empty
Sends a "Who Are You" request to the other station
Same as <CTRL-D> but sends your callgign in Morse
Sends the TIME if the DAYTIME clock has been set
Changes your DSP-2232 from send (ISS) to receive
( IRS)
BBQ Break-in (ACHG Command)
In the linked or connected mode, (ARQ) , when you ' re the " Information
Receiving Station, " you can use the "ACHG" command to interrupt the
distant station's comments.
As the " Information Receiving Station, " you normally rely on the
distant station to send the <CTRL-Z> to "change-over" at the end of
his comments.
ACHG is a command that forces both syst.ems to reverse
the "Information Receiving" and "Information Sending" status of the
link.
Use the ACHG command only when really needed to interrupt the
distant station.

### (PDF p.163)

#### 12.7.2

#### 12.7.3

#### 12.7.4

Entering Your Auto-AnswerBack (AAB)
AEA PACTOR allows you to request the identity of the Station you are
converging with by sending your DSP-2232 a This causes the
DSP-2232 to gend an inquiry (WRU) request to the other station.
For this reason, you should get your own Auto-AnswerBack (AAB) message
to "DE YOUR-CALL"
Your DSP-2232 will automatically gend the AAB
message when another station requests your identity, and
then stop Bending.
PACTOR Modem Frequenc ies and Shifts
All PACTOR operation uses 200 Hz shift FSK modems. Modem 5 , (default)
6, 32 or 34 are the best choices for PACTOR use. Modem 5 is a single
port PACTOR modem using the standard 2110 Hz and 2310 Hz tones.
Modem 6 allows the lower tone (1460 Hz and 1260 Hz) pair to be used.
Modems 32 and 34 are dual port PACTOR/PACKET modems allowing
simultaneous HF or VFH packet operation on radio port 2 while
operating PACTOR on radio port 1.
MODEM 5 AFSK PACTOR Modem, 200 Hz shift, Mark=2110 Hz, space=2310 Hz
MODEM 6 AFSK PACTOR Modem, 200 Hz shift, Mark=1460 Hz, space-1260 Hz
MODEM 32: PACTOR 200:
MODEM 34: PACTOR 200:
Automatic Speed Chanqe
(Dual Port modems )
2110/2310; p2 Packet 300 bps HF 2110/2310
2110/2310; p2 Packet 1200 bps VHF
If the command PT200 is set on, the linked PACTOR mode will
automatically change the transmitted data rate from 100 bps to 200 bps
if a certain number of error-free 100 bps packets are received in a
row. If the error rate at 200 bps is excessive, the data rate will
automatically revert to 100 bps. There may be some propagation
conditions that will cause the system to vacillate between the two
data rates. This may be prevented by setting PT200 to OFF, which will
force 100 bps operation .
thresholds .
See the UCMD command to control these

### (PDF p.164)

#### 12.7.5

#### 12.7.6

#### 12.7.7

Echo inq Transmitted Characters As Sent (EAS
EAS (Echo Ag Sent) operates the game as in ARQ AMTOR. If EAS is on,
you will gee characters echoed to your screen only the first time your
DSP-2232 sends them. If the data is not acknowledged by the receiving
station and is re-trangmitted, the characters are not echoed again.
An exception to this occurs when the speed BhiftB from 200 to 100
bauds.
In this cage, the block will be echoed again. With EAS OFF,
characters are echoed to your screen ag you type them. With EAS ON:
If the data scrolls across your monitor at a fairly even rate,
you can assume that you have a good ARQ PACTOR link.
If the data hesitates for a few seconds at a time, that 'g
generally a sign that the radio link is not very good.
If the characters stop appearing on your monitor, the link is
failing or has failed. The Status display will tell you this
by showing ERROR or REQUEST nearly continuously.
Sending On Iv Complete Words (WORDOUT)
Some PACTOR users like to have their words sent out only when they are
complete. This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character or punctuation.
Turning WORDOUT ON activates this feature. See the Command Summary
for more information.
Operating on the "Wrong Sideband"
PACTOR, like packet is mark-space polarity insensitive. Once linked
the PACTOR protocol alternates the data polarity every transmission.
A specific header synchronizes the system during non-linked operation.
For this reason, there is no "wrong" sideband.
You may operate on
either LSB or USB.
If you are going to change to other modes, for example AMTOR, then
LSB must be used. It is suggested that LSB be used to make mode
changes simple as well as keeping the radio dial frequency reading
consistent with other users.
If you are using a radio that has direct FSK inputs, keep in mind that
PACTOR uses 200 Hz shift and most direct FSK capable radios are set
for 170 Hz shift.
If the 170 Hz shift can not be adjusted to 200 Hz,
use the TX audio from the DSP-2232 to drive the microphone input in
LSB.

### (PDF p.165)

#### 12.7.8

### 12.8

Little Used PACTOR Commands
There are four seldom-used PACTOR commands that are accessible with
the UCmd command. This command is of the form UCmd n x, where n is
the UCmd number and x is
the use of UCmd:
UCMD
UCMD
UCMD
UCMD
UCMD
2
4
1
3
10
OFF
ON
Will
Will
will
Will
Will
The PACTOR UCmd commands
the setting. Several examples are shown in
show the current setting of command 2
set command 4 to the value 10
set the value of command 1 to zero
set the command 3 to its default value
show the setting of the last UCMD entered
are :
maximum 30. This command sets the number of
UCMD O:
UCMD 1:
UCMD 2:
UCMD 3:
Default 3,
correct packets in a row that must be received before
generating an automatic request to change from 100 to
200 baud. Also, see the command PT200 in 12.7.4.
Default 6 , maximum 30. This command sets the number of
incorrect packets in a row that must be received before
generating an automatic request to change from 200 to
100 baud. Also see the command PT200 in 12.7.4
Default 2 , maximum 9 . This command sets the number of
packets sent in a baud rate speed-up attempt .
Default 5 , maximum 60. This command sets the maximum
number of Memory ARQ packets that are combined to form
one good packet. When this number is exceeded, all
stored packets are erased and Memory ARQ is
re-initialized.
Monitor inq ARQ PACTOR Contacts with PTLIST
Use the "PTLIST" command to monitor ARQ traffic flowing between two
stations linked in a PACTOR ARQ contact. Your DSP-2232 will try to
display the text of whichever of the two linked ARQ stations is the
Information Sending Station at the moment.
Monitoring two linked PACTOR ARQ stations does not provide the error
correction enjoyed by the linked stations.
Since your DSP-2232 is not
part of the "handshake" you do not generate the request for repeat.
Your DSP-2232 will test for the correct CRC error check and will not
display messages with errors.
Data blocks with errors will be
designated with four error symbols.
The default error symbol is the
underline ( ) •
See the command summary for ERchr, the error symbol .
Your DSP-2232 will not print a block of data if that block contains
the same sequence number as the previous block. If the " I SS"
(Information Sending Station) is repeating the same block, you won't
print it twice.

### (PDF p.166)

### 12.9

#### 12.9.1

#### 12.9.2

#### 12.9.3

PACTOR MailDrop Operat ion
The DSP-2232 allows PACTOR as well ag Packet and AMTOR access to the
MailDrop. Messages that originate in Packet or AMTOR can be accessed
remotely in PACTOR and messages that originate from a remote PACTOR
station can be accessed by Packet and AMTOR users of your MailDrop.
This section of the manual talks about basic PACTOR mailbox operation.
Section 12.10 will discuss how to pass message traffic from PACTOR to
Packet and vice versa.
Make sure that you understand MailDrop Operation in Chapter 5 and the
basic PACTOR operation described earlier in this chapter before
putting your PACTOR MailDrop on the air.
Special Operating Considerations
The PACTOR MailDrop has been designed with a "Watchdog" safety feature
so that it may perform safely without constant attention.
If a remote
station is linked with your PACTOR MailDrop and no traffic is passed
for 5 minutes, the link will drop and your transmitter will Bhut off.
At this writing however, unattended operation below 30 MHz is not
legal for US amateurs unless they hold a Special Temporary
Authorization ( STA) from the FCC for this purpose. This restriction
may soon change, but •until then US amateurs must be sure to always
have control of their HF transmitters when any automatic device such
as the DSP-2232 MailDrop is in operation.
With this in mind, we have designed the PACTOR MailDrop so that it can
be disabled at any time during an ARQ link simply by turning the
command TMAIL (TOR MAIL) OFF. This allows you the SYSOP to make your
MailDrop available to other stations and still break in to chat with
remote stations at any time. This could come in handy should you
want to provide some help or information to a remote station using
your PACTOR MailDrop.
Settings For PACTOR MailDrop Operation
Before a remote PACTOR user can access your MailDrop, be certain that
MYPTCALL and MYCALL (on Port 1) are set to your Arnateur callsign.
Start inq PACTOR MailDrop Operation
Remote access to your PACTOR MailDrop is controlled by the command
TMAIL which is short for TOR MAIL. The TMAIL command controls remote
access to the PACTOR and AMTOR MailDrop in the same way that the
MAILDROP command controls remote Packet access .
Turn the TMAIL command ON (default OFF) to allow remote stations to
access your MailDrop in ARQ PACTOR. Turn TMAIL OFF to have normal ARQ
QSOg with other stations in the
PACTOR mode.

### (PDF p.167)

#### 12.9.4

##### 12.9.4.1

#### 12.9.5

#### 12.9.6

#### 12.9.7

Local Logon to the MailDrop
To locally access your MailDrop use the MDCHECK command as described
in chapter 5 of this manual on MailDrop operation.
Remote Logon to Your PACTOR MailDrop.
The PACTOR maildrop user interface is Blight ly different from the
packet interface due to the differences between the two modes.
When a station links with your PACTOR MailDrop, your DSP-2232 first
identifies your maildrop by sending the amount of free MailDrop
memory as shown below:
Type H for help.
(AEA DSP-2232) 17528 FREE.
The DSP-2232 then sends the user the MTEXT string if the MailDrop
Message command (MMSG) is ON. The Default text is shown below:
Welcome to my AEA DSP-2232 maildrop.
Type H for help.
Cal ler Prompts
The command prompt that the MailDrop sends the remote user in PACTOR
is similar to that used in the Packet mode and is shown below:
WX7BBB DE WX7AAA >
As in packet, MDPROMPT is the PACTOR MailDrop message prompt gent to
a remote station by your MailDrop. The default prompt is:
Subject: / Enter Message, - Z (CTRL-Z) or / EX to End
Text before the first slash is sent to the user as the subject prompt;
text after the first slash is sent as the message text prompt.
Monitor MailDrop Operation
The local user (SYSOP) can monitor the dialog by setting MDMON ON.
The DSP-2232 stays in cornmand mode during remote MailDrop access.
SYSOP MailDrop Commands
The MailDrop commands that you the SYSOP have access to are the same
as those described in Chapter 5 of the manual on MailDrop Operation.

### (PDF p.168)

#### 12.9.8

##### 12.9.8.1

##### 12.9.8.2

##### 12.9.8.3

##### 12.9.8.4

##### 12.9.8.5

##### 12.9.8.6

Remote User MailDrop Corrunands
When a remote user has logged onto your MailDrop the following
cotrmandg are available to the distant station:
The remote user may end a command with either <CTRL-Z> or a carriage
return.
A brief description of each command follows in the next sections. The
description is expanded where the command operation differs from the
Packet MailDrop section found in Chapter 5.
(ABORT) (Remote only)
The "A" command aborts the listing or reading of messages by the
remote calling station as described in chapter 5 . The difference in
PACTOR is that the remote user must send the ACHG command first to
reverse the direction of the link before he can issue the Abort
command.
The remote user also has the ability to abort a command that may have
been mis-typed by typing " /// " on the same line as the bad command.
E (BYE)
The "B" command logs the remote station off the MailDrop. In PACTOR
the remote station may gracefully shut down the link with the RECEIVE
character or the CWID character .
(HELP)
The "H" command sends the remote station a help list of the available
commands shown in Chapter 5.
( J LOG) (Remote only command)
The "J" command sent by the distant station will cause the MailDrop to
send the list of stations who have logged in to your MailDrop.
(KILL n r Mine n
The "K n" command deletes message number "n" from the MailDrop ag
described in Chapter 5. The "KM't command will kill all of your
messages that have been read.
(LIST r Mine 1)
The "L" command shows the remote user only a list of the messages he
The "LM" command lists
or she may read as described in Chapter 5.
only those messages addressed to the user.

### (PDF p.169)

##### 12.9.8.7

##### 12.9.8.8

##### 12.9.8.9

(READ n (Mine)
The "R n" command lets the remote user read any of the message numbers
displayed in the LIST command. The cornmand operates as described in
Chapter 5 except that the column headers are not displayed. The "RM"
command displays messages addressed to the remote user that have not
been read previously.
S callsiqn (SEND callgiqn)
Either a <CTRL-Z> or the "/EX" command must be used to end all PACTOR
MailDrop messages. After the <CTRL-Z> or " /EX" has been detected, the
MailDrop will confirm that the message has been gent by returning the
message "Filed msg n" to the remote user. An example of sending a
message is shown below:
WX7BBB DE WX7AAA >
s wx2zzz @ WX2YYY
Subject:
Going to the Hamfegt?
Enter Message, - Z (CTRL-Z) or / EX to End
I haven't heard from you and wondered if
you are going to the Hamfest next month?
Hope to see you there. 73
/ ex
WX7BBB DE WX7AAA Filed msg 1
y (VERSION) (Remote only command)
The "V" command causes the DSP-2232 to send
firmware date to the remote user only.
12.9.8.10 ? (HELP) (Remote only command)
The "?" command sends the distant station a
MailDrop commands shown above under the "H"
{MailDrop prompt
{User's SEND command}
{MailDrop Subject prompt}
{User enters Subject}
{MailDrop Send prompt}
{Message text}
{Message text}
{Message text}
{User ends message}
S, V, ? ) >{Mai1Drop prompt}
sign-on message and
the
HELP list of all available
command. Both the " ? " and

### 12.10

the "H" cause this same file to be sent to the remote user.
Simultaneous PACTOR and Packet Operat ion
Your DSP-2232 can operate PACTOR on Radio Port 1 and HF or VHF Packet
on Radio Port 2 at the same time. With this feature you won't miss
any local Packet activity while operating PACTOR.
Before the second radio port can be used for packet operation, you
must be sure the correct modem is selected to enable the second port.
Use the MODem command to select either MODEM 32 or MODEM 34, to enable
dual port operation.
For example, typing MODEM 34 enables dual port PACTOR operation on
radio 1, and VHF Packet operation on radio port 2 .

### (PDF p.170)

#### 12.10.1

Select inq Modems
The various modems available in the DSP-2232 can be seen with the
DIRECT(ory) command. To display all the available modems simply
enter the Conmand Mode of the DSP-2232 and then DIR as shown.
DIR
The DSP-2232 will respond with the following:
(930315)
1:
3:
5:
10:
12:
14:
16:
18:
22:
25:
30:
31:
32:
33:
34:
35:
40:
42:
44:
46:
51:
61:
cmd :

#### 12.10.2

RTTY/TOR 170: 2125/2295
RTTY/TOR 425: 2125/2550
RTTY/TOR 200: PACTOR 2110/2310
pl Packet 300 bps HF 2110/2310
pl Packet 1200 bps VHF
pl Packet 1200 bps PSK
pl Packet 4800 bps PACSAT
pl Packet 9600 bps FSK K9NG/G3RUH
p2 Packet 1200 bps VHF
p2 Packet 2400 bps V. 26B
2:
4:
6:
11:
13:
15:
17:
20:
23:
28:
RTTY/TOR 170: 2125/2295; p2 Packet 300
RTTY/TOR 170: 1445/1275
RTTY/TOR 850: 2125/2975
RTTY/TOR 200: PACTOR 1460/1260
pl Packet 300 bps HF 1460/1260
pl Packet 1200 bps PACSAT
pl Packet 2400 bps V. 26B
pl Packet 4800 bps PSK
p2 Packet 300 bps HF 2110/2310
p2 Packet 1200 bps PACSAT
p2 Packet 9600 bps FSK K9NG/G3RUH
bps HF 2110/2310
RTTY/TOR 170: 2125/2295; p2 Packet 1200 bps VHF
RTTY/TOR 200:PACTOR 2119/2310; p2 Packet 300 bps HF 2110/2310
pl Packet 300 bps HF 2110/2310; p2 Packet 1200 bps VHF
RTTY/TOR 200: PACTOR 2110/2310; p2 Packet 1200 bps VHF
pl Packet 1200 bps VHF; p2 Packet 1200 bps VHF
Morse 750 Hz
Analog FAX APT
DSP data 400 bps OSCAR-13
DSP data Spectrum
pl Packet 2400 bps MSK
p2 Packet 2400 bps MSK
41:
43:
45:
50:
60:
Analog FAX HF
Analog SSTV
RTTY/TOR 1200 bps ASCII OSCAR-II
pl Packet 1200 bps MSK
p2 Packet 1200 bps MSK
Any modem from the list may be loaded with the MODEM command, but
only modems 5, 6, 32 and 34 will operate correctly in PACTOR.
For example, to operate PACTOR on radio port I and 1200 bps VHF packet
on radio port 2 you must select MODEM 34.
First, enter the Command
Mode of the DSP-2232 and then type MODEM 34 as shown below:
MODEM 34
The DSP-2232 will respond with the following:
MODem was x (The previous modem)
MODem now 34
Selectinq A Default PACTOR Modem
When you enter the PACTOR mode, the modem number stored in the QPTOR
command is automatically loaded. QPTOR defaults to 5 which
automatically loads the 2110/2310 single port PACTOR modem. If you
wish to load the low-tone modem (MODEM 6), or one of the dual port
PACTOR modems (MODEM 32 or 34), enter the corresponding modem number
into QPTOR.

### (PDF p.171)

#### 12.10.3

#### 12.10.4

Displaying Received Data
The Radio command may be used to disable port 2.
This may be
desirable when operating PACTOR and you do not want to be disturbed
with any packet signal B that may be received on radio port 2. To
disable port 2, enter:
RADIO ON/OFF or RADIO 1/0
For dual port operation, type
RADIO ON/ON or RADIO 1/2
The DSP-2232 gortB and displays received data from each Radio Port
using the same technique as multi-connect packet operation described
in Chapter 4. That is, when operating on one port and the other
port becomes active, the displayed data from the inactive port is
shown prefaced by the "channel designator" followed by a colon ( : ) .
Recall that Radio Port 1 is designated by "logical" channels from 0-9
and that Port 2 is designated by "logical" channels A-Z.
Switchinq Between Ports
If you are using an AEA PAKRATT program, switching between Radio Ports
is described in the program manual.
If you are using a terminal
program, this section describes how to direct your transmitted text.
Switching between PACTOR on Port 1 and Packet on Port 2 is similar to
switching between Packet and Packet .
If you have not yet read through
the Switching Between Radio Ports section of Chapter 4, please do go
now and define a CHSWITCH character before reading the example below.
Recall from chapter 4 that the channels on Port 1 are labeled 0-9 and
the channels on Port 2 are labeled A-Z. To select Radio Port 1
( PACTOR) press the CHSWITCH character you defined, followed by the
number O. To select Radio Port 2 (Packet), pregg the CHSWITCH
character, followed by a letter from A-Z.
For example, you are conversing with an PACTOR station and are in the
middle of a QSO when a station on VHF connects to you. The following
shows how your screen would look and suggests how you might handle
such an occurrence.
The underlined text is the text that you type.

### (PDF p.172)

Hello you are print inq solid
here and have an S7 signal
Thanks Bob, you 're also a solid S7
here as well.
A: CONNECTED to WX7EEE
Thanks for the signal report Jim
on Iv runninq 100 watts here.
Hey Bob, I'm going to the hamfest
this weekend if you want a ride.
I AHe110 Mike L am on HF PACTOR
talk inq to a station in Boston.
O: Well Bob, I had better be going
bed. Work starts pretty early 73.
| 073 Jim it was nice meet inq vou.
WXIAAA de WX7BBB SK
to
You gend the PACTOR Station
a Bignal report }
The other station regpondg
with a signal report
WX7EEE connecte to you on
VHF (Radio Port 2)
{ You make another transmission
to Jim on HF PACTOR
{ Your friend on VHF packet
wants to go to the hamfest
{ You switch to Port 2 by
typing to answer on VHF
{ Jim on HF PACTOR signs off
with you.
{ You switch back to HF with
and sign off with Jim }
As you may have noticed, communicating with different modes on the two
Radio Ports at the same time is almost identical to the method used in
chapter 4 for Packet and Packet operation. Let's discuss the sample
QSOs above to see how the Port switching occurs.
The first text we see in the sample above is the signal report you are
sending to Jim, the HF PACTOR station you are communicating with.
This example assumes you have already set up the ARQ PACTOR contact
with the ARQ command discussed earlier in the chapter. When you are
through sending your signal report to Jim on PACTOR, you turn the link
over to become the IRS by sending the The DSP-2232 then
responds by sending a blank line to the display to break up the
received text.
The next text you see is your signal report received from Jim on
PACTOR .
You are in the middle of a QSO with Jim on PACTOR and all of a sudden
your friend WX7EEE connects to you on Packet. WX7EEE has connected on
Radio Port 2 (VHF) which is shown by the "A:" before the connect
message. Remember that the 26 channel designators for Radio Port 2
are A-Z.
Before you respond to WX7EEE on VHF, you send a transmission to Jim on
PACTOR telling him how much power your transmitter is running. When
you are finished with your text, you again command the DSP-2232 to be
the IRS by sending a <CTRL-Z>.

### (PDF p.173)

#### 12.10.5

After making this transmission on PACTOR, you gee WX7EEE on VHF has
offered you a ride to the hamfest. We know this text from Radio
Port 2 since the previous packet displayed by the DSP-2232 was the
"A: *** CONNECTED" message from Port 2.
Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF. This way he will understand
that it may take you a little longer to respond to his packets.
Before you can gend data to Radio Port 2, you must switch to this Port
with or the text you type will be gent to Radio Port 1 on PACTOR
when Jim turns the link over to you again.
You receive a transmission from Jim on PACTOR telling you he needs to
Bign off to go to bed. The "O: "
in front of the text shows this was
received on Radio Port 1.
Now you want to sign off with Jim on HF. Again, first you must switch
to Radio Port 1 with the " 10" since your last transmission was
directed to Port 2 .
Now you can make your final transmission to Jim
ending with his callsign followed by your callsign. This time when
you are through typing your text, you send a <CTRL-D> to the DSP-2232
which breaks the ARQ link and returns Radio Port 1 to PACTOR Standby
after the text has been sent.
More Thouqhts on Port. Switchinq
One problem of having more than one Radio Port is remembering which
port you are currently using.
In the dual port sample QSOs above,
this was not a problem, but after it has been hours or days since you
have used your DSP-2232, you may forget which port you last used.
With AEA Pakratt Software programs, the on-screen status will always
show which port you are using so this is not a problem. With other
programs, you will have to query the DSP-2232 with the CSTATUS SHORT
command. The CSTATUS command displays the status of the logical
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
Port 2 .
If you had been connected to any other packet stations, the
callsign and channel would have also shown in the display.

### (PDF p.174)

#### 12.10.6

Sometimes you might not want to be bothered with anything from the
Radio Port you are not using. For these times either Radio Port may
be turned OFF with the RADIO command. For example, let's gay that in
the above example QSO you wanted to work HF packet and did not want to
be interrupted with any VHF connects. Typing the following cormand
would cause Radio Port 2 to be disabled.
```text
cmd:RADIO /0
RAdio was 1/2
RAdio now 1/0
```
When a DSP-2232 Radio Port is disabled, the front panel LCD status
indicator for that port will be extinguished as a reminder.
Dual Port PACTOR(Packet MailDrop Operation
Your DSP-2232 MailDrop will operate both on Packet and PACTOR and can
be used to allow message traffic that originates on PACTOR to be
reverse-forwarded into the Packet network. Similarly, traffic
originating on Packet may be picked up by remote stations on PACTOR.
Before you begin dual port Packet/ PACTOR MailDrop operation, be sure
that you are familiar with the operation of Packet, PACTOR and the
MailDrop as describeä in Chapters 4, 5 and 7 of this manual. Also be
sure that appropriate modems are selected. The more experience you
have with each of these modes will help when setting up a dual port
MailDrop system.

##### 12.10.6.1 Packet MailDrop Command Settings

First set up the packet side of the MailDrop (on Radio Port 2) by
setting MYCALL and MYMAIL. You may also want to enter a custom MTEXT
to let others know about the PACTOR feature. Be sure to set 3RDPARTY ,
MDMON, MDPROMPT and MMSG as desired.
If you will be reverse
forwarding to a full-service BBS, you must set HOMEBBS to the callsign
of that BBS.
Do not forget to turn MAILDROP ON. As a test you should
connect to your own maildrop via a digipeater or network node to make
sure the radio link is working properly and the user prompts are what
you desire.

##### 12.10.6.2 PACTOR MailDrop Command Settinqs

Once the Packet side is working properly, the PACTOR side of things on
Radio Port 1 must be configured. First be sure that MYCALL
is entered properly. You may want to customize the PACTOR
MailDrop prompt (MDPROMPT) or compose a message to ALL that tells
remote users about your system. Finally, remember to turn TMAIL ON
and enter the PACTOR mode of the DSP-2232 to start the PACTOR MailDrop.

### (PDF p.175)

12 .10.6.3 Dual Port MailDrop Operation Notes
With the above parameters set, packet connections to the MYMAIL
call Bign on Radio Port 2 will be sent to your MailDrop if you or a
remote PACTOR station is not using it.
If you or any other station is
using your MailDrop, the remote user attempting the packet connection
will be sent a "*** Busy" message.
Similarly, a remote PACTOR station linking to you will be given access
to your MailDrop provided no one else is using it.
If you or a remote
packet station is using your MailDrop, the PACTOR station may link to
you, but will not be given access to your MailDrop. For this reason,
you may wish to disable Radio Port I (by turning the RADIO parameter
to 0/2) when logging into your own MailDrop for maintenance. This
will prevent remote PACTOR stations from linking with you.
If you will be Reverse Forwarding messages into the Packet network, be
sure to check your MailDrop often for new messages. Remember that you
must use the Edit command in the MailDrop to select which messages
will be Reverse Forwarded.
Note :
At this time unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary

### 12.11

Authorization ( STA) from the FCC. Although this may soon
change, US amateurs must be sure to have control of their
HF transmitters when any device such as the DSP-2232
PACTOR/Packet MailDrop is in operation.
PACTOR Switchinq-Time Considerations
For operation in PACTOR ARQ, your transceiver or transmitter-
receiver combination must be able to change between transmit and
receive within 100 milliseconds. Most modern solid state radios can
easily meet this specification. Many older tube-type radios that use
electromechanical relays also operate very well in PACTOR ARQ.
If the changeover from transmit to receive is too long, the minimum
working distance is extended; the signal to the distant station will
arrive before the station has switched back to receive. However, if
the transmitting station is further away, the transmission time over
the propagation path will delay the arrival of the signal until after
For this reason, you may be able
the station has switched to receive.
to "Link with" stations across the country, but not across town.
If the receiving station's changeover from transmit to receive is too
slow, the transmitting station delay between "PTT" and "data send" can
be extended. See the ADELAY command in the Command Summary to adjust
the DSP-2232's PACTOR timing characteristics to compensate for this.

### (PDF p.176)

#### 12.11.1

#### 12.11.2

Suggested PACTOR Operat ing Settings
If you have trouble synchronizing with another PACTOR ARQ Station, try
some of the following operating tips before calling AEA or deciding
that your radio equipment needs modif i cations:
Try to work the distant station on the unproto mode to establish
that the other station's system is fully functional.
- use the PTT line from your interface.
Don't use VOX control
Turn off the AGC circuit - use the RF gain control to prevent
receiver blocking on stronger signals.
Turn off all compression or other audio processing.
Keep the AFSK audio input level to the microphone circuit as low
as possible
- avoid over-driving the audio input stages.
Disable the ALC circuit or reduce excessive ALC action; use more
effective RF antenna loading to adjust output power levels.
Possible Areas for PACTOR Performance Improvement
If switching-time problems persist, you may have to make changes in
the radio to eliminate excessive time delays:
Remove large decoupling capacitors from the line to
allow faster PTT (transmitter) activation;
Improve power supply decoupling, especially in audio gtageg.
Do not use the squelch control.
In case you can't solve your radio's switching-time problems, please
call AEA's Customer Service Department (see the front of this manual) .
This is the last page of Chapter 12
- PACTOR Operat ion
