# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 5 — MailDrop Operation (PDF p.87–98)

<!-- PDF p.87 -->

### 5.1 Overview of MailDrop Operation

The PK-900's MailDrop is a personal mailbox that uses a subset of
the well-known W0RLI/WA7MBL packet BBS commands allowing messages to
be automatically sent and received.  The MailDrop operates in Packet,
AMTOR and PACTOR modes and may be accessed from both Radio Ports,
although not simultaneously.  This allows message traffic to move from
Packet to AMTOR or PACTOR and vice versa.

When your MailDrop is active, distant stations can connect (in Packet)
or Link (in AMTOR/PACTOR) to your PK-900, leave messages for you or
read messages from you.  If you choose to allow it, any station may
leave messages for any other station simply by turning the parameter
3RDPARTY ON.

The Maildrop also supports forwarding and reverse forwarding of Packet
messages if properly coordinated with a local "full service" BBS.
Hierarchical message addressing is supported to simplify the routing
of both national and international traffic.

#### 5.1.1 RAM Space for Message Storage

Approximately 17K bytes of RAM are available to your MailDrop.  RAM
space is dynamically allocated so that it is possible to store as many
messages as you like until all the memory is filled.  If all 18K of
RAM is used, the MailDrop displays the message "*** No free memory".

#### 5.1.2 System Commands

MailDrop operation is completely under your "SYSOP" control from your
local terminal or computer keyboard.  Only you can start and stop
MailDrop service.  The commands shown below provide MailDrop control.

#### 5.1.3 Your MailDrop Callsign

When operating in Packet, your MailDrop can have its own callsign
which you enter with the MYMAIL command.  If you do not enter a
callsign in MYMAIL, the MailDrop will use MYCALL when it is enabled.
When operating the MailDrop in AMTOR, your 4 character MYSELCAL or 7
character MYIDENT is used and must be entered for remote users to
access the MailDrop.  Se the command summary (Appendix A) for
information on how to set MYSELCAL and MYIDENT

#### 5.1.4 Start and Stop MailDrop Operation

Set MAILDROP to ON to start Packet MailDrop operation (default is OFF).
This command activates or deactivates your Packet MailDrop.  Set TMAIL
ON to start AMTOR/PACTOR MailDrop operation.  Again, the default is
OFF.

<!-- PDF p.88 -->

### 5.2 Local Logon

If you are using PC PACKRATT or some other host mode program, follow
the instructions for the program.  If you are using a terminal or
terminal program the following sections apply.

Type MDCHECK to verify that you have local control of the MailDrop.
You must not be connected or linked to any other stations to be able
to do this.

Once logged on to your MailDrop from your local keyboard, you are
shown the MailDrop prompt as though you were the calling station:

[AEA PK-900]  17396 free  (B,E,K,L,R,S) >

You can now EDIT, KILL, LIST, READ or SEND messages.
The number "17396 free" is the RAM available for MailDrop messages.

While you're "logged on" to your MailDrop, a connect request from
another station will cause the PK-900 to send the "BUSY" frame.  In
AMTOR, the PK-900 simply ignores ARQ link requests while you are
logged on to your MailDrop.  When you are finished using the MailDrop,
type "B" (BYE) to "log off".  This returns the PK-900 to normal
operation and makes your MailDrop available to other stations.

In Packet mode, you must leave the MAILDROP command ON if you wish to
make it available for others.  No other stations can reach your
MailDrop unless the MAILDROP command is turned ON.  In AMTOR, TMAIL
must be ON.

You have full control of your PK-900 while the MAILDROP is ON.  You
can connect to others and carry on normal QSOs using the call sign in
MYCALL provided you have entered a separate MYMAIL MailDrop callsign.

#### 5.2.1 Monitor MailDrop Operation

Set MDMON to ON to monitor other stations' use of your MailDrop.  Set
MDMON OFF (default) to cancel MailDrop monitoring.  User bit 13 allows
MailDrop connect and status messages to be disabled as well.  See the
UBIT command in the command summary for specifics.

The MDMON command permits you to monitor a station's activity on your
MailDrop showing you both sides of the QSO.  Packet headers are not
shown while a caller is connected to your MailDrop.  When a caller is
connected to the MailDrop, MCON determines what packets are monitored.
When your MailDrop is idle, MONITOR determines what packets are seen.

#### 5.2.2 Caller Prompts

MTEXT is the MailDrop connect-message prompt sent to a packet station
connecting to the MailDrop if MMSG is ON.  The default message is:

"Welcome to my AEA PK-900 maildrop.
Type H for help."

MDPROMPT is the Message prompt sent to a station by the MailDrop.
This prompt is given to a station sending a message.  You can enterany text with a maximum length of 80 bytes.  The default prompt is:

"Subject:/Enter message, ^Z (CTRL-Z) or /EX to end"

You may wish to enter a CTEXT message announcing the presence of a
mailbox and the call sign (MYMAIL) used to access your MailDrop.

### 5.3 SYSOP MailDrop Commands

While you have logged on to your MailDrop from your local keyboard
with the MDCHECK command, the commands available to you are:

B, E, K, L, R, S.

These are the "standard" BBS commands available to the MailDrop SYSOP.
Any other command sent by you is answered with the error message
"*** What?".  A brief description of each command follows.  All the
available commands are described in detail following the next section.

B    BYE       Log off the MailDrop
E    EDIT      Edit a MailDrop message
K    KILL      Kill or delete messages
L    LIST      List the message directory
R    READ      Read a specific message
S    SEND      Send a message

### 5.4 Remote User MailDrop Commands

When a remote user has logged on to your MailDrop the following
commands will be available to the distant station:

A, B, H, J, K, L, R, S, V, ?.

A brief description of each command follows.
These MailDrop commands are described in detail in the next sections.

A    ABORT     Aborts the reading of a long message
B    BYE       Log off the MailDrop
H    HELP      Help for the MailDrop commands
J    JLOG      Sends the PK-900 MHeard list
K    KILL      Kill or delete messages
L    LIST      List the message directory
R    READ      Read a specific message
S    SEND      Send a message
V    VERSION   Sends the PK-900 sign-on message
?    HELP      Help for the MailDrop commands

#### 5.4.1 A (ABORT) (Remote only)

The "A" command aborts the Listing or Reading of messages by a calling
station.  This is handy if the remote user decides not to continue
reading a long message.  The message "*** Done" followed by the
MailDrop prompt will be sent after an Abort has been received.  On the
local terminal the SYSOP may type the CANLINE character (default
<CTRL-X>) to abort a long screen dump.

<!-- PDF p.90 -->

#### 5.4.2 B (BYE)

The "B" command (Host abbreviation B1) logs the calling station (and
you) off the MailDrop.  A calling station will be disconnected; you
will see the standard PK-900 "cmd:" prompt.  The calling station
may also simply disconnect.

#### 5.4.3 E (EDIT #) (SYSOP only command)

The Edit command (Host abbreviation E1) is a powerful tool for
controlling the status of messages on your MailDrop.  The SYSOP must
access the MailDrop before typing this command.  Here are all possible
ways to use this command:

E                   Shows the following short help file:
E  msg#
E  msg#  B/T/P
E  msg#  Y/N/F
E  msg#  >/</@  callsign

E 12                Shows message 12's info line.
E 23 > W0RM         Sets "W0RM" as message 23's destination.
E 35 < WH1Z         Sets "WH1Z" as message 35's source.
E 48 @ N7ML         Sets "N7ML" as message 48's destination BBS.
E 49 @ N7ML.MT.NA   Sets "N7ML" as message 49's destination BBS.
and adds the hierarchical forwarding
information .MT.NA signifying that N7ML BBS
is located in the state of Montana (MT)
which is located in North America (NA).
E 58 @              Clears message 58's destination BBS field.
E 60 P              Sets message 60's status to Private.
E 61 B              Sets message 61's status to Bulletin.
E 62 T              Sets message 62's status to Traffic.
E 63 Y              Sets message 63's status to Has-Been-Read.
E 64 N              Sets message 64's status to Has-Not-Been-Read.
E 49 F              Sets message 49's status to Reverse Forward.

The command "E n F" sets the message status to enable Reverse
Forwarding of a message number.  To cancel forwarding, set the status
to either "Y" or "N."  Please read the section on Reverse Forwarding
(below) if you are interested in this feature.

#### 5.4.4 H (HELP) (Remote only command)

The "H" command sends the distant station a HELP list of all available
MailDrop commands as shown below.  The "?" will also cause the HELP
file to be sent.

A(bort)   Stop Read or List
B(ye)     Log off
H(elp)    Display this message
J(log)    Display stations heard
K(ill)    K n: Kill message number n
KM : Kill messages you have read
L(ist)    L  : List message titles

<!-- PDF p.91 -->

LM : List messages to you
R(ead)    R n: Read message number n
RM : Read all your unread messages
S(end)    S  : Send a message to SYSOP
S n: Send a message to station n
V(ersion) Display TNC firmware version
?         Same as H(elp)

#### 5.4.5 J (JLOG) (Remote only command)

The "J" command sent by the distant station will cause your MailDrop
to send the PK-900's MHEARD List to the station.  This command is
not available to you the SYSOP since you can simply enter MHEARD at
the PK-900 command prompt.

#### 5.4.6 K n (KILL n [Mine])

The "K n" command (Host abbreviation K1) deletes message number "n"
from the MailDrop.  As SYSOP, you can kill any message.  A calling
station can kill only messages addressed to or from that station.
Messages are killed by number, not call sign.  The remote user may
enter the "KM" (Kill Mine) command to KILL all of his or her messages
that have been read previously.

#### 5.4.7 L (LIST [Mine])

The "L" command (Host abbreviation L1) shows you the SysOp a list of
ALL active messages on the MailDrop.  The list is preceded by the
following column header:

Msg#      Size To     From   @ BBS      Date       Time     Title

All active messages are listed under this line with the most recent
message first.  DAYTIME must be set for the Date and Time to appear.

When a remote user types the LIST command, the MailDrop lists only the
messages that user may read, including messages to "ALL" and "QST."
Messages to other users are not displayed.  The MailDrop accepts the
LM (List Mine) command from the remote user.  This command acts only
on messages addressed to the remote user, not messages to "ALL" or
"QST."

#### 5.4.8 R n (READ n [Mine])

The "R n" command (Host abbreviation R1) displays the header and text
of message number "n".  Messages are read by number, not call sign.
As SYSOP, you can read all messages.  A remote user may READ only
messages addressed to his call sign, or to "ALL" or "QST".  The Mail-Drop accepts the RM (Read Mine) command from remote users.  This
command acts only on messages addressed to the remote user, not
messages to "ALL" or "QST."   The RM command displays only messages
that have not been read previously.

#### 5.4.9 S callsign (SEND callsign)

The "S callsign" command (Host abbreviation S1) notifies the MailDrop
that either you as SYSOP or the calling station will now send text
into a message.

<!-- PDF p.92 -->

If 3RDPARTY is ON, then the calling station can leave a message for a
station other than the SYSOP.  If the station attempts to leave third
party traffic with 3RDPARTY OFF, then the calling station will see:

*** No 3rd party traffic.
[AEA PK-900]  17396 free  (A,B,H,J,K,L,R,S,V,?) >

If all 17K of the RAM is used, the MailDrop displays the message
"*** No free memory".  If there is room, the MailDrop displays the
Subject request message prompt:

"Subject:"

Enter a short (up to 27 character) description of the subject of the
message.  The MailDrop will then send the message prompt:

"Enter message, ^Z (CTRL-Z) or /EX to end"

After entering the message there are two ways to end the message.
Either the <CTRL-Z> may be entered followed by a carriage return, or
the 3 characters "/EX" and a carriage-return on a line by itself will
end the message being sent.  After this the MailDrop prompt should
appear indicating that the MailDrop is ready for another command.

After ending the message, if you or the calling party see the message
"*** No free memory", this means that the message was too large for
the available MailDrop memory and has been deleted.  If this occurs,
you must shorten the message to fit into available memory shown in the
MailDrop prompt, and re-send the message.

When logged on from your local keyboard, if you use the "S" command
without a call sign, you'll see the error message "*** Need callsign"
However, a calling station may use the S command without a callsign;
it is understood that the message is directed to the PK-900's SYSOP.

As soon as a calling station uses the S command to send you (the
SYSOP) a message, the MAIL LCD starts blinking to show that a
message has been left for you.  When you log on to your MailDrop
with the MDCHECK command the MAIL LCD will stop blinking.

##### 5.4.9.1 Sending Other Types of Messages with SEND

Each message in the PK-900 MailDrop has a flag to show whether it is
Private, Traffic or a Bulletin.  A "P," "T" or "B" after the message
number shows the status of every message.  A user sets this with the
SP, ST and SB forms of the Send command.  The SYSOP may set this with
the Edit (E) command described above.  If only S is used as the Send
command, the MailDrop will assign the message a Private (P) status.

A Private message can be listed, read and killed only by the
reciepient or sysop. Bulletins are listable and readable by all and
may be killed by only the originator or sysop.  Traffic is handled
much like private messages but are intended for BBS handling. The
PK-900 MailDrop also accepts SEND commands of the form  "SP SYSOP
< W1AW."  The call sign after the "<" goes into the "From" field of
the message header.

<!-- PDF p.93 -->

For example, your MailDrop accepts the following additional
information in a Send command:

S N7ML @ K6RFK < N6IA

The above means you want to send a message to N7ML who uses the K6RFK
Bulletin-Board and the message is from N6IA.

The PK-900 MailDrop accepts hierarchical forwarding information that
is helpful in reverse forwarding to full service BBS stations.  An
example of this is shown below:

SP N7ML @ K6RFK.WA.NA

The above means that you want to send a message to N7ML who uses the
K6RFK Bulletin-Board which is located WAshington which is located in
North America.

The PK-900 MailDrop also supports BIDs (Bulletin IDs).  This support
is required for Reverse Forwarding (see below).  The BID begins with a
"$" character and is sent and received in the Send command line:

S N7ML @ K6RFK < N6IA $345_KB7B

With the PK-900 MailDrop you may also use just the "$" all by itself
as shown in the two examples below:

S N7ML @ K6RFK < N6IA $
or   S ALL $

In this case, the PK-900 MailDrop will assign its own BID to these
messages.

#### 5.4.10 V (VERSION) (Remote only command)

The "V" command causes the PK-900 to send the sign-on message and
firmware date to the remote user only.

5.4.11    ? (HELP) (Remote only command)

The "?" command sends the distant station a HELP list of all available
MailDrop commands shown above under the "H" command.  Both the "?" and
the "H" cause this same file to be sent to the remote user.

### 5.5 Sample MailDrop Session - The Remote User's Point of View

Let's see what the MailDrop looks like to a calling station.  Let's
assume that your call is "WX1AAA", and that you wish to connect and
log on to "WX2BBB's" MailDrop system.  During your session on his
MailDrop, you wish to list the messages to see if there is a message
for you, read it if it exists, kill it after you're done reading it,
send a return message to WX2BBB and finally log off or disconnect from
his MailDrop.

<!-- PDF p.94 -->

#### 5.5.1 Connect and Logon

From the cmd: prompt, type the usual connect request:

cmd:c wx2bbb                                       {Connect request}
*** CONNECTED to WX2BBB                          {PK-900 status line}

You have mail                                      {informs the user
mail is waiting}
[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

#### 5.5.2 LIST Messages

You're logged on and have gotten the MailDrop's prompt:

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

Now, type "L" to LIST all the messages in the MailDrop.

L                                                  {The LIST command}
{The MailDrop responds}
Msg#      Size To     From   @ BBS      Date       Time     Title
6 PN     184 WX1AAA WX2BBB            01-Jun-90 20:15     Hello Joe
5 BY     287 ALL    WX2BBB            01-Jun-90 18:42     Question
4 BY     178 QST    WX2BBB            01-Jun-90 17:30     Mailbox
1 BY      56 ALL    WX2BBB            01-Jun-90 10:22     APLINK
[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

#### 5.5.3 READ Messages

You've seen the list of messages and wish to READ yours.  You've seen
the MailDrop's prompt:

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) >  {MailDrop's prompt}

Now, type "R #" to READ the one message number to you in the MailDrop.

R 6                                               {The READ 6 command}
{The MailDrop responds}
Msg#      Size To     From   @ BBS      Date       Time     Title
6 PN     144 WX1AAA WX2BBB             01-Jun-90 20:15     Hello Joe
{The message header}
Hello Joe.  Did you get the notice            {The message}
about next month's meeting of the
Radio Society at the Firehouse?
Will you be going? - I need a ride.
73.

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

#### 5.5.4 KILL Messages

You've read the message addressed to you and wish to KILL it.  Again
you see the MailDrop's prompt:

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

<!-- PDF p.95 -->

Now, type "K #" to KILL one specific message in the MailDrop.

K 6                                               {The KILL 6 command}
*** Done.                                         {MailDrop confirms}
[AEA PK-900]  16704 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

#### 5.5.5 SEND Messages

You've killed the message and wish to SEND a reply to the MailDrop's
operator, WX2BBB.  Again you see the MailDrop's prompt.  Remember the
number after the right bracket "]" shows you how much memory space is
available in the MailDrop.  Always verify that the MailDrop has enough
memory remaining for the length of message you intend to send.

[AEA PK-900]  16704 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

Now, type "S [callsign]" to SEND a message to the MailDrop's SYSOP. If
you omit the call sign, the MailDrop will address the message to the
MYMAIL or MYCALL call sign.  Messages sent with the S command are
considered Private unless they are sent to "ALL" or to "QST".

S WX2BBB                                {The "SEND callsign" command}
Subject:                                {MailDrop's Subject prompt}
I WILL BE GOING                         {Your subject entry}
Enter message, ^Z (CTRL-Z) or /EX to end     {MailDrop answers you}

YEAH, I GOT THE MAILING AND WILL        {Type your message to WX2BBB}
BE GLAD TO PICK YOU UP.  WHAT TIME
DO YOU WANT ME THERE?  IS MARY
GOING?  CUL. WX1AAA
/EX                                     {End the message with /EX}

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

NOTE:     If the message was ended with the "/EX", the last line will
contain a "/E" when the message is read.  To avoid this, use
the <CTRL-Z> to end messages.

#### 5.5.6 Log Off and Disconnect

You're finished with this session.  Time to log off the MailDrop.

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) > {MailDrop's prompt}

Now, type "B" (for Bye) to LOG OFF the MailDrop.

B                                                 {The Bye command}

The MailDrop issues an immediate disconnect command to your PK-900
and the connection is over.

*** DISCONNECTED                             {PK-900's status line}

<!-- PDF p.96 -->

### 5.6 Sample MailDrop Session - MailDrop SYSOP's Point of View

Here is a transcription of the entire session described in the
previous section, exactly as it would appear to the MailDrop's
operator (SYSOP).  We're assuming that the MDMON command is set to ON.

When MDMON is ON, you have the ability to supervise the activities of
any station logged on to your MailDrop and - if needed - take any
corrective action.

WX1AAA>WX2BBB <C,P>
You have mail
[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) >
*** CONNECTED to WX1AAA (Maildrop)

L
Msg#      Size To     From   @ BBS      Date       Time     Title
6 PN     184 WX1AAA WX2BBB            01-Jun-90 20:15     Hello Joe
5 BY     287 ALL    WX2BBB            01-Jun-90 18:42     Question
4 BY     178 QST    WX2BBB            01-Jun-90 17:30     Mailbox
1 BY      56 ALL    WX2BBB            01-Jun-90 10:22     APLINK
[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) >
R 6
Msg#      Size To     From   @ BBS      Date       Time     Title
6 PN     184 WX1AAA WX2BBB            01-Jun-90 20:15     Hello Joe

Hello Joe.  Did you get the notice
about next month's meeting of the
Radio Society at the Firehouse?
Will you be going? - I need a ride.
73.

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) >
K 6
*** Done.
[AEA PK-900]  16704 free  (A,B,H,J,K,L,R,S,V,?) >
S WX2BBB
Subject:
I WILL BE GOING
Enter message, ^Z (CTRL-Z) or /EX to end

YEAH, I GOT THE MAILING AND WILL
BE GLAD TO PICK YOU UP.  WHAT TIME
DO YOU WANT ME THERE?  IS MARY
GOING?  CUL. WX1AAA
/EX

[AEA PK-900]  16508 free  (A,B,H,J,K,L,R,S,V,?) >
B
*** DISCONNECTED

<!-- PDF p.97 -->

#### 5.6.1 Message Numbers

Any message that is sent to the PK-900 MailDrop by a remote user or
you, the SYSOP, is given a message number.  Message numbers start at 1
and over time work their way up to 999 and then wrap back around to 1
again.  Sometime it is desirable to reset the message counter.  This
can be done with the LASTMSG command which is described in the Command
summary.

### 5.7 Forwarding and Reverse Forwarding with the PK-900 MailDrop

Forwarding allows your large community Bulletin Board System (BBS) to
automatically connect to your MailDrop and send messages to you.
Similarly, Reverse Forwarding allows your community BBS to connect to
your MailDrop and get messages you wish to send to others.

Forwarding and Reverse Forwarding (or simply Auto-Forwarding) can be
an advantage in a local area.  The community BBS can be set to connect
to your MailDrop at times when local traffic is low, such as late at
night.  This can spread out the traffic volume on a packet frequency
which can become quite heavy in the "prime time" early evening hours.

Auto-Forwarding is involved and requires the cooperation of both you
and your community BBS Operator.  Not all large BBSs will forward to
individual users.  Some packet frequencies are so busy forwarding to
other BBSs that they can not forward to individuals.  You must contact
the community BBS SYSOP to determine the guidelines in your area.

#### 5.7.1 MailDrop Settings for Auto-Forwarding

The following must all be set properly for Auto-Forwarding to operate.

-    Enter your MYCALL.  Enter your MYMAIL if you desire to use a
separate call sign for the MailDrop.

-    Make arrangements with your local BBS SYSOP to Auto-Forward to
your MailDrop.  Make sure you let the him know the MailDrop call
sign you will use.  The BBS SYSOP must program his system to
connect to your MailDrop or Auto-Forwarding will not function.

-    Enter the call sign of this community BBS in the HOMEBBS command.

-    Leave your PK-900 and radio ON THE AIR so that your local BBS
can connect to your MailDrop.  If your packet station is not on
when the local BBS tries to connect, the advantage of Auto-Forwarding is lost and the BBS SYSOP may drop you from the
Forwarding list.

Once the above have been completed, you are ready to receive messages
automatically from your local BBS.  The next section describes how to
prepare messages for Reverse Forwarding to the local BBS (HOMEBBS).

<!-- PDF p.98 -->

#### 5.7.2 Entering a Message for Reverse Forwarding

To prepare a message for Reverse Forwarding to another station:

-    Type MDCHECK to access your MailDrop from your terminal.

-    Using the Send command, type the message you want forwarded.  Use
the "@" field to set the destination BBS where the addressee will
pick up his mail.  For example if you want to send a message to
N6UND who you know uses the BBS N6IIU enter the following:

S N6UND @ N6IIU.CA.NA

Note that the "@" call sign does not need to be the same as the
HOMEBBS call sign.  The "@" call sign can be typed as part of the Send
command or as part of the Edit command described earlier.
The ".CA.NA" is optional "Hierarchical forwarding" information that in
this case designates that N6IIU is located in the state of California.

-    Enter the Subject and text of the message as described above in
the Send command section.  Don't forget to end your message with
<CTRL-Z> or "/EX" on its own separate line also described above.

-    Use the Edit command to set the Forwarding flag for each message
that will be Reverse Forwarded to HOMEBBS.  This is described in
the Edit command section above.  For example the following will
mark message number 53 for Reverse Forwarding.

E 53 F              (Sets Reverse Forwarding for message 53.)

-    Log off your MailDrop with the B (Bye) command.

-    If you wish each message to disappear as it is Reverse Forwarded,
leave KILONFWD ON.  If you wish to keep each message after it has
been Forwarded, turn KILONFWD OFF.  After forwarding, the message
flag will change from "F" to "Y" to show that it has been read.
