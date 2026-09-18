# Chapter 5 — Maildrop Operation

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 85–96).

**Agent extract:** `MAILDROP ON`, `MYMAIL`, `3RDPARTY`, `MDMON`, `MDPROMPT`, `MMSG`, `HOMEBBS`, local `MDCHECK`. Remote/user letters: A(abort) B(bye) E(edit) H(help) J(jlog) K(kill) L(list) R(read) S(send) V(version). Works on Packet, AMTOR, and PACTOR; both radio ports but not at once.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.85)

### 5.1

Overview of MailDrop Operat ion

#### 5.1.1

#### 5.1.2

#### 5.1.3

#### 5.1.4

The DSP-2232's MailDrop is a personal mailbox that uses a subset of
the well-known WORLI/WA7MBL packet BBS commands allowing messages to
be automatically sent and received. The MailDrop operates in Packet,
AMTOR and PACTOR modes and may be accessed from both Radio Ports,
although not simultaneously. This allows message traffic to move from
Packet to AMTOR or PACTOR and vice versa.
When your MailDrop is active, distant stations can connect (in Packet)
or Link (in AMTOR) to your DSP-2232, leave messages for you or read
messages from you .
If you choose to allow it, any station may leave
messages for any other station simply by turning the parameter
3RDPARTY ON.
The MailDrop also supports forwarding and reverse forwarding of Packet
messages if properly coordinated with a local "full service" BBS.
Hierarchical message addressing is supported to simplify the routing
of both national and international traffic.
RAM Space for Messaqe Storaqe
Approximately 18K bytes of RAM are available to your MailDrop. RAM
space is dynamically allocated so that it is possible to store as many
messages as you like until all the memory is filled. If all 18K of
RAM is used, the MailDrop displays the message "*** No free memory" .
System Commands
MailDrop operation is completely under your "SYSOP" control from your
local terminal or computer keyboard. Only you can start and stop
MailDrop service. The commands shown below provide MailDrop control.
Your MailDrop Callsiqn
When operating in Packet, your MailDrop can have its own callsign
which you enter with the MY MAIL command.
If you do not enter a
callsign in MYMAIL, the MailDrop will use MYCALL when it is enabled.
When operating the MailDrop in AMTOR, your 4 character MYSELCAL or 7
character MY IDENT is used and must be entered for remote users to
access the MailDrop.
Start and Stop MailDrop Operation
Set MAILDROP to ON to start Packet MailDrop operation (default is OFF) .
This command activates or deactivates your Packet MailDrop. Set TMAIL
ON to start AMTOR MailDrop operation. Again, the default is OFF.

### (PDF p.86)

### 5.2

#### 5.2.1

#### 5.2.2

Local Logon
Type MDCHECK to verify that you have local control of the MailDrop.
You must not be connected or linked to any other stations to do this.
Once logged on to your MailDrop from your local keyboard, you are
shown the MailDrop prompt as though you were the calling station:
[AEA DSP-2232)
18396 free >
You can now EDIT, KILL, LIST, READ or SEND messages.
The number " 18396 free" is the RAM available for MailDrop messages.
While you ' re "logged on" to your MailDropt a connect request from
another station will cause the DSP-2232 to send the "BUSY" frame.
In
AMTOR, the DSP-2232 simply ignores ARQ link requests while you are
logged on to your MailDrop. When you are f in i shed using the MailDrop,
type "B" (BYE) to "log off" .
This returns the DSP-2232 to normal
operation and makes your MailDrop available to other stations.
In Packet mode, you must leave the MAILDROP command ON to make it
available for others.
No other stations can reach your MailDrop
unless the MAILDROP command is turned ON .
In AMTOR, TMAIL must be ON.
You have full control• of your DSP-2232 while the MAILDROP is ON.
You
can connect to others and carry on normal QSOs using the callsign in
MYCALL provided you have entered a separate MY MAIL MailDrop callsign.
Monitor MailDrop Operation
Set MDMON to ON to monitor other stations' use of your MailDrop.
Set
MDMON OFF (default) to cancel MailDrop monitoring. User bit 13 allows
MailDrop connect and status messages to be disabled as well.
See the
UBIT command in the commarid summary for specifics.
The MDMON command permits you to monitor a station's activity on your
MailDrop showing you both sides of the QSO. Packet headers are not
shown while a caller is connected to your MailDrop. When a caller is
connected to the MailDrop, MCON determines what packets are monitored.
When your MailDrop is idle, MONITOR determines what packets are seen.
Caller Prompts
MTEXT is the MailDrop connect-message prompt sent to a packet station
connecting to the MailDrop if MMSG is ON. The default message is:
"Welcome to my AEA DSP-2232 maildrop.
Type H for help. "
MDPROMPT is the Message prompt sent to a station by the MailDrop.
This prompt is given to a station sending a message.
You can enter
any text with a maximum length of 80 bytes. The default prompt is:
"Subject: / Enter message,
-Z (CTRL-Z) or /EX to end"
You may wish to enter a CTEXT message announcing the presence of a
mailbox and the callsign (MY MAIL) used to access your Mai_lDrop.

### (PDF p.87)

### 5.3

### 5.4

#### 5.4.1

#### 5.4.2

SYSOP MailDrop Commands
While you have logged on to your MailDrop from your local keyboard
with the MDCHECK command, the commands available to you are:
These are the " standard" BBS commands available to the MailDrop SYSOP.
Any other command sent by you is answered with the error message
A brief description of each command follows. All the
What?"
available commands are described in detail following the next section.
B
E
K
L
R
s
BYE
EDIT
KILL
LIST
READ
SEND
Log off the MailDrop
Edit a MailDrop message
Kill or delete messages
List the message directory
Read a specific message
Send a message
Remote User MailDrop Commands
When a remote user has logged on to your MailDrop the following
commands will be available to the distant station:
A brief description of each command follows.
These MailDrop commands are described in detail in the next sections.
A
B
H
K
L
R
s
ABORT
BYE
HELP
JLOG
KILL
LIST
READ
SEND
VERS ION
HELP
Aborts the reading of a long message
Log off the MailDrop
Help for the MailDrop commands
Sends the DSP-2232 MHeard list
Kill or delete messages
List the message directory
Read a specific message
Send a message
Sends the DSP-2232 sign-on message
Help for the MailDrop commands
(ABORT) (Remote only)
The "A" command aborts the Listing or Reading of messages by a calling
station. This is handy if the remote user decides not to continue
reading a long message. The message " *** Done" followed by the
MailDrop prompt will be sent after an Abort has been received. On the
local terminal the SYSOP may type the CANLINE character (default
<CTRL-X>) to abort a long screen dump.
(BYE)
The "B" command (Host abbreviation B 1) logs the calling station (and
you) off the MailDrop. A calling station will be disconnected; you
will see the standard DSP-2232
may also simply disconnect.
prompt. The calling station
" cmd : "

### (PDF p.88)

#### 5.4.3

#### 5.4.4

(EDIT (SYSOP only command)
The Edit command (Host abbreviation E 1) is a powerful tool for
controlling the status of messages on your MailDrop. The SYSOP must
access the MailDrop before typing this command.
Here are all possible
ways to use
this command:
WORM
WHIZ
N7ML
N7ML.MT.NA
E
E
E
E
E
E
E
E
E
12
48
49
58
61
62
63
64
>
B
T
Y
Shows the following short help file:
E msg#
E msg# B/ T/ P
E msg# Y/N/F
E msg# >/</@ callsign
Shows message 12' s info line.
Sets "WORM" as message 23' s destination.
Sets "WHIZ" as message 35' s source.
Sets "N7ML" as message 48 •s destination BBS.
Sets "N7ML" as message 49' s destination BBS.
and adds the hierarchical forwarding
information . MT. NA signifying that N7ML BBS
is located in the state of Montana (MT)
which is located in North America (NA) .
Clears message 58' s destination BBS field.
status to Private.
Sets message 60's
status to Bulletin.
Sets message 61' s
status to Traffic.
Sets message 62 's
status to Has-Been-Read.
Sets message 63 's
status to Has-Not-Been-Read.
Sets message 64's
status to Reverse Forward.
Sets message 49 's
The command
"E n F" sets the message status to enable Reverse
Forwarding of a message number. To cancel forwarding, set the status
to either "Y" or "N. "
Please read the section on Reverse Forwarding
(below) if you are interested in this feature.
( HELP) ( Remote only command)
The "H" command sends the distant station a HELP list of all available
MailDrop commands as shown below. The " ? " will also cause the HELP
file to be sent.
bort-)
J ( log)
K (ill)
L (ist)
R (ead)
S (end)
V ( ersion)
Stop Read or List
Log off
Display this message
Display stations heard
L
RM
s
Kill
Kill
: List
: List
Read
Read
Send
Send
message number n
messages you have read
message titles
messages to you
message number n
all your unread messages
a message to SYSOP
a message to station n
Display TNC firmware version
Same as H (e 1 p)

### (PDF p.89)

#### 5.4.5

#### 5.4.6

#### 5.4.7

#### 5.4.8

5 . 4.9
(J LOG) (Remote only command)
The "J" command sent by the distant station will cause your MailDrop
to send the DSP-2232ts MHEARD List to the station. This command is
not available to you the SYSOP since you can simply enter MHEARD at
the DSP-2232 coaunand prompt .
(KILL [Mine 1)
The "K n" command (Host abbreviation KI) deletes message number "n"
from the MailDrop. As SYSOP, you can kill any. message. A calling
station can kill only messages addressed to or from that station.
Messages are killed by number, not callsign. The remote user may
enter the "KM" (Kill Mine) cornmand to KILL all of his or her messages
that have been read previously.
(LIST [Mine 1)
The "L" command (Host abbreviation L 1) shows you the SysOp a I i Bt of
ALL active messages on the MailDrop. The 1 ist is preceded by the
following column header:
Msg#
Size To
From @ BBS
Date
T ime
Title
AIL active messages are listed under this line with the most recent
message first.
DAYTIME must be set for the Date and Time to appear.
When a remote user types the LIST command, the MailDrop lists only the
messages that user may read, including messages to "ALL" and "QSTe t'
The MailDrop accepts the
Messages to other users are not displayed.
LM ( List Mine) command from the remote user. This command actg only
on messages addressed to the remote user, not messages to "ALL" or
"QST."
(READ n [Mine)
The "R n" command displays the header and text of message number "n"
Messages are read by number, not callsign. As SYSOP, you can read
all messages. A remote user may READ only messages addressed to hig
callsign, or to "ALL" or "QST" .
The MailDrop accepts the RM (Read
Mine) command from remote users. This command acts only on messages
The RM
addressed to the remote user, not messages to "ALL" or "QST. "
command displays only messages that have not been read previously.
callsign (SEND callsiqn)
The "S cållsign" command notifies the MailDrop that either you as
SYSOP or the calling station will now send text into a message.
If 3RDPARTY is ON, then the calling station can leave a message for a
If the station attempts to leave third
station other than the SYSOP.
party traffic with 3RDPARTY OFF,
*** No 3rd party traffic.
(AEA DSP-2232)
18396 free
then the calling station will see:

### (PDF p.90)

If all 18K of the RAM is used, the MailDrop displays the message
If there is room, the MailDrop displays the
Subject request message prompt :
" Subject : "
Enter a short (up to 27 character) description of the subject of the
message. The MailDrop will then send the message prompt:
"Enter message,
-Z (CTRL-Z) or /EX to end"

##### 5.4.9.1

After entering the message there are two ways to end the message.
Either the <CTRL-Z> may be entered followed by a carriage return, or
the 3 characters "/EX" and a carriage-return on a line by itself will
end the message being sent. After this the MailDrop prompt should
appear indicating that the MailDrop is ready for another command.
After ending the message, if you or the calling party see the message
" *** No free memory", this means that the message was too large for
If this occurs,
the available MailDrop memory and has been deleted.
you must shorten the message to fit into available memory shown in the
MailDrop prompt, and re-send the• message.
When logged on from your local keyboard, if you use the "S" command
without a callsign, •you '11 see the error message "*** Need callsign" .
However, a calling station may use the S command without a callsign;
it is understood that the message is directed to the DSP-2232's SYSOP.
As soon as a calling station uses the S command to send you (the
SYSOP) a message, the STA light starts blinking to show that a message
has been left for you. When you log on to your MailDrop with the
MDCHECK command the STA LED will stop blinking .
Sendinq Other Types of Messages with SEND
Each message in the DSP-2232 MailDrop has a flag to show whether it is
Private, Traffic or a Bulletin. A "P, " "T" or "B" after the message
number shows the status of every message. A user sets this with the
The SYSOP may set this with
SP, ST and SB forms of the Send command.
If only S is used as the Send
the Edit (E) cormand described above.
command, the MailDrop will ass ign the message a Private (P) status.
The DSP-2232 MailDrop also accepts SEND cornmands of the form
The callsign after the "<" goes into the "From"
"SP SYSOP < WIAW."
field of the message header.
For example, your MailDrop accepts the following additional
information in a Send command:
S N7ML @ K6RFK < N61A
The above means you want to send a message to N 7 ML who uses the K6RFK
Bulletin-Board and the message is from N61A.

### (PDF p.91)

The DSP-2232 MailDrop accepts hierarchical forwarding information that
is helpful in reverse forwarding to full service BBS stations. An
example of this is shown below:
SP N7ML @ K6RFK.WA.NA
The above means that you want to send a message to N7ML who uses the
K6RFK Bulletin-Board which is located WAshington which is located in
North America.
The DSP-2232 MailDrop also supports BIDs (Bulletin I Ds) .
This support
is required for Reverse Forwarding (see below) .
The BID beg ing with a
"$" character and is gent and received in the Send command line:
S N7ML @ K6RFK < N61A $345 KB7B
With the DSP-2232 MailDrop you may also use just the
as shown in the two examples below:
all by itself
S N7ML @ K6RFK < N61A $
S ALL $

#### 5.4.10

#### 5.4.11

### 5.5

#### 5.5.1

In this case, the DSP-2232 MailDrop will assign its own BID to these
messages .
y (VERSION) (Remote only command )
The "V" command causes the DSP-2232 to send the sign-on message and
firmware date to the remote user only.
? (HELP) (Remote only command)
The "?" command sends the distant station a HELP list of all available
MailDrop commands shown above under the "H" command. Both the "?" and
the "H" cause this same file to be sent to the remote user.
Sample MailDrop Session The Remote User's Point of View
Let's see what the MailDrop looks like to a calling station. Let's
assume that your call is "WXIAÄÄ", and that you wish to connect and
log on to "WX2BBB's" MailDrop system. During your session on his
MailDrop, you wish to list the messages to see if there is a message
for you, read it if it exists, kill it after you 're done reading it,
send a return message to WX2BBB and finally log off or disconnect from
his MailDrop.
Connect and Logon
From the cmd: prompt, type the
usual connect request:
```text
cmd:c wx2bbb
```
*** CONNECTED to WX2BBB
You have mail
[AEA DSP-2232)
16508 free
{Connect request}
{DSP-2232 status line}
{informs the user
mail is waiting}
(A, > {MailDrop's prompt}

### (PDF p.92)

#### 5.5.2

#### 5.5.3

5.5 . 4
LIST Messages
You • re logged on and have gotten the MailDrop's prompt :
(AEA DSP-2232) 16508 free > {MailDrop•g prompt}
Now, type " L"
to LIST all the
messages in the
MailDrop.
{The LIST command}
{The MailDrop responds}
Msg#
5 BY
4 BY
1 BY
Size
184
287
178
56
To
WXIAAA
ALL
QST
ALL
From @
WX2BBB
WX2BBB
WX2BBB
WX2BBB
BBS
Date
O I-Jun-
O I-Jun-
O I-Jun
T ime
90 20:15
01-Jun-90 18:42
90 17:30
-90 10:22
Title
Hello Joe
Question
Ma 11 box
APLINK
(AEA DSP-2232J 16508 free
READ Messaqes
You've seen the list of messages
the MailDrop' s prompt :
>
{MailDrop' B prompt }
and
wish to READ
yours .
You've seen
(AEA DSP-22321
Now, type "R
16508 free
to READ the
From
one message number to
> {MailDrop's prompt
you in the MailDrop.
{The READ 6 command}
MailDrop responds}
Msg#
Size To
{The
Date
@ BBS
notice
T ime
Title
Hello Joe
144 WXIAAA WX2BBB
Hello Joe. Did you get the
01-Jun-90 20:15
about next month's meeting of the
Radio Society at the Firehouse?
Will you be going?
- I need a ride.
73.
[AEA DSP-2232J 16508 free
KILL Messages
You tve read the message addressed to
you see the MailDrop's prompt :
{The message header}
{The message}
V, ? ) > {MailDrop's prompt}
you and
message
wish to KILL it.
Again
(AEA DSP-2232]
Now, type "K # "
[AEA DSP-2232)
16508 free
to KILL one specific
16704 free
v t ? ) > {MailDrop' g prompt}
in the MailDrop.
{The KILL 6 command}
{MailDrop confirms}
> {MailDrop's prompt

### (PDF p.93)

#### 5.5.5

#### 5.5.6

SEND Messages
You've killed the message and wish to SEND a reply to the MailDrop' B
operator, WX2BBB. Again you see the MailDrop's prompt. Remember the
number after the right bracket " ) " shows you how much memory space is
available in the MailDrop. Always verify that the MailDrop has enough
memory remaining for the length of message you intend to send.
[AEA DSP-2232) 16704 free > {MailDrop'g prompt}
Now, type "S (callsign] " to SEND a message to the MailDrop's SYSOP. If
you omit the callsign, the MailDrop will address the message to the
MYMAIL or MYCALL callsign. Messages sent with the S command are
considered Private unless they are sent to "ALL" or to "QST"
S WX2BBB
Subj ect :
1 WILL BE GOING
Enter message,
-Z (CTRL-Z) or /EX to
YEAH, 1 GOT THE MAILING
AND WILL
BE GLAD TO PICK YOU UP.
WHAT TIME
DO YOU WANT ME THERE? IS MARY
GOING? CUL. WXIAAA
[AEA DSP-2232J
16508 free (A, B, H, J,
{The " SEND callsign" command}
{MailDrop's Subject prompt}
{Your subject entry}
{MailDrop answers you}
end
{Type your message to WX2BBB}
{End the message with / EX}
> {MailDrop's prompt}
NOTE :
If the message was ended with the " /EX" ,
contain a " / E" when the message is read.
the <CTRL-Z> to end messages .
the last line will
To avoid this, use
LQ_g Off and Disconnect
You're f inished with this session. Time to log off the MailDrop.
[AEA DSP-2232) 16508 free > {MailDrop's prompt}
Now, type "B" (for Bye) to LOG OFF the MailDrop.
{The Bye command}
The MailDrop issues an immediate disconnect command to your DSP-2232
and the connection is over.
* * DI S CONNECTED
{DSP-2232is status line}

### (PDF p.94)

### 5.6

Sample MailDrop Session
- MailDrop SYSOP's Point of View
Here is a transcription of the entire session described in the
previous section, exactly as it would appear to the MailDrop' g
operator (SYSOP) .
We' re assuming that the MDMON command is get to ON.
When MDMON is ON, you have the ability to supervise the activities of
any station logged on to your MailDrop and
corrective action.
WXIAAA>WX2BBB
You have mail
[AEA DSP-2232) 16508 free
CONNECTED to WXIAAA (MailDrop)
- if needed
Msg#
5 BY
4 BY
1 BY
[ AEA
Msg#
Size
184
287
178
56
To
WXIAAA
ALL
QST
ALL
From @ BBS
WX2BBB
WX2BBB
WX2BBB
WX2BBB
Date
01-Jun-90
01 -Jun
-90
01-Jun-90
90
01 -Jun-
T ime
20:15
18:42
17:30
10:22
DSP-2232 )
Size To
16508 free >
From @ BBS
- take any
Title
Hello Joe
Quest ion
Mailbox
APLINK
Title
Hello Joe
Date
O I-Jun-
end
T ime
90 20:15
184 WXIAAA WX2BBB
Hello Joe. Did you get the notice
about next month's meeting of the
Radio Society at the Firehouse?
Will you
be going?
- I need a ride.
73.
[AEA DSP-2232)
16508 free
Done.
[AEA DSP-2232)
16704 free
S WX2BBB
Subject:
1 WILL BE GOING
Enter message,
-Z (CTRL-Z) or /EX to
YEAH, 1 GOT THE MAILING AND WILL
BE GLAD TO PICK YOU UP. WHAT TIME
DO YOU WANT ME THERE?
GOING? CUL. WXIAAÄ
(AEA DSP-2232)
16508
* * DISCONNECTED
IS MARY
free

### (PDF p.95)

#### 5.6.1

### 5.7

#### 5.7.1

Message Numbers
Any message that is sent to the DSP-2232 MailDrop by a remote user or
you, the SYSOP is given a message number. Message numbers start at 1
and over time work their way up to 999 and then wrap back around to 1
again.
Sometime it is desirable to reset the message counter. This
can be done with the LASTMSG command which is described in the Command
summary.
Forwarding and Reverse Forwarding with the DSP-2232 MailDrop
Forwarding allows your large community Bulletin Board System (BBS) may
automatically connect to your MailDrop and send messages to you.
Similarly, Reverse Forwarding allows your conununity BBS to connect to
your MailDrop and get messages you wish to send to others.
Forwarding and Reverse Forwarding (or simply Auto-Forwarding) can be
an advantage in a local area. The community BBS can be set to connect
to your MailDrop at times when local traffic is low, such as late at
night. This can spread out the traffic volume on a packet frequency
which can become quite heavy in the "prime time" early evening hours.
Auto-Forwarding is involved and requires the cooperation of both you
and your community BBS Operator. Not all large BBSs will forward to
individual users.
Some packet frequencies are so busy forwarding to
other BBSs that they can not forward to individuals.
You must contact
the community BBS SYSOP to determine the guidelines in your area.
MailDrop Settings for Auto-Forwarding
The following must all be set properly for Auto-Forwarding to operate.
Enter your MYCALL. Enter your MYMAIL if you desire to use a
separate callsign for the MailDrop.
Make arrangements with your local BBS SYSOP to Auto-Forward to
your MailDrop. Make sure you let the him know the MailDrop call
sign you will use. The BBS SYSOP must program his system to
connect to your MailDrop or Auto-Forwarding will not function.
Enter the callsign of this comrnunity BBS in the HOMEBBS command.
Leave your DSP-2232 and radio ON THE AIR so that your local BBS
can connect to your MailDrop. If your packet station is not on
when the local BBS tries to connect, the advantage of Auto-
Forwarding is lost and the BBS SYSOP may drop you from the
Forwarding list .
Once the above have been completed, you a.re ready to receive messages
automatically from your local BBS. The next section describes how to
prepare messages for Reverse Forwarding to the local BBS (HOMEBBS) .

### (PDF p.96)

#### 5.7.2

Entering a Message for Reverse Forward ina
To prepare a message for Reverse Forwarding to another station:
Type MDCHECK to access your MailDrop from your terminal.
Using the Send command, type the message you want forwarded.
Use
the "@" field to set the destination BBS where the addressee will
pick up his mail.
For example if you want to send a message to
N6UND who you know uses the BBS N611U enter the following:
S N6UND @ N611U.CA.NA
Note that the "@" callsign does not need to be the same as the
HOMEBBS callsign. The "@" callsign can be typed as part of the Send
command or as part of the Edit command described earlier.
The " . CA. NA" is optional "Hierarchical forwarding" information that in
this case designates that N611U is located in the state of California.
Enter the Subject and text of the message as described above in
the Send command section. Don't forget to end your message with
<CTRL-Z> or " / EX" on its own separate line also described above.
Use the Edit command to set the Forwarding flag for each message
that will be Recerse Forwarded to HOMEBBS. This is described in
the Edit command section above.
For example the following will
mark message number 53 for Reverse Forwarding.
E 53 F
(Sets Reverse Forwarding for message 53. )
Log off your MailDrop with the B (Bye) command.
If you wish each message to disappear as it is Reverse Forwarded,
"-f you wish to keep each message after it has
leave KILONFWD ON.
been Forwarded, turn KILONFWD OFF. After forwarding, the message
flag will change from "F" to "Y" to show that it has been read.
Last page of Chapter 5
- MailDrop Operation
