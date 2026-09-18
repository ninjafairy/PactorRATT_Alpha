# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## PK-900 Gateway Option Supplement (PDF p.325–352)

<!-- PDF p.325 -->

040-067-9 - December 1993
PK-900
Gateway Option Supplement
Thank you for your purchase of Timewave's Gateway firmware option for the
PK-900! Please read the enclosed sheet, PK-900 EPROM Installation Instruction, for
instructions on how to install your new firmware.
New feature outline:
 Cross-mode Gateway includes packet/AMTOR, packet/PACTOR and packet/
packet operation. (See the new GUSERS and XGATEWAY commands for details).
 AEA packet "node" helps eliminate the need for digipeating.
 Enhanced AMTOR- and PACTOR-listen modes show link and connect attempts.
 Automatic selection of AMTOR or PACTOR modes when a received signal is
tuned with the ARXTOR command.
 Enhanced packet MHEARD function identifies TCP/IP, NET/ROM and <The-Net> stations.
 MYALIAS has been expanded to enable the "two-ham family" to use more than
one packet callsign with their PK-900.
 PACTOR "roundtable" operation has been enhanced with the PTROUND command.
 New EXPERT command now included so you're no longer burdened with a large
number of commands to view.
 Full break-in CW operation is simplified with MOPTT.
 The CODE command has been epxanded to include the upper/lower case extensions used by AMTOR MSO and APLINK stations.
 The SIAM (Signal) mode now indentifies PACTOR stations.

<!-- PDF p.326 -->

> [No extractable text on this page — figure, schematic, or blank.]

<!-- PDF p.327 -->

Gateway Operation
Your PK-900 now has a capability never before offered in a multi-mode controller:
the ability to "gateway" from packet-to-AMTOR, packet-to-PACTOR, and of course
packet -to-packet. Under your command, you can allow packet users, connecting to
Port 2, the ability to monitor and link to other AMTOR, PACTOR and even packet
stations using your HF radio on Port 1.
This powerful capability deserves a note of caution for American operators: under
current FCC regulations, an Amateur isn't allowed to operate an HF station under
automatic control unless a Special Temporary Authorization (STA) is obtained
from the FCC to permit this type of operation. At this writing, rule changes are being
considered to allow HF operation under automatic control in certain band segments;
until they're adopted you, the Control Operator, must be present when the Gateway
function is in use.
The old gateway as a digipeater
The original function of the GATEWAY command in the PK-900 allowed cross-port
packet digipeating which used end-to-end acknowledgment. This meant that an Iframe (packet) sent by the source station needed to be received correctly by the
digipeater(s) and the destination station. The acknowledgement of the packet is also
needed to pass from the destination station through the digipeater, then arrive at the
source station intact. If there were any errors during a hop, or if one of the stations
in the digipeating path failed to forward an ack packet, the whole packet/
acknowledgement cycle had to start over.
Fortunately, this is now a thing of the past.
Your New Gateway as a Node
Your Gateway firmware now supports local acknowledgement (acks) of packets like
a full-service BBS/node does, so instead of users having to digipeat through your
MYALIAS or MYCALL  callsign to connect to a destination station, they can now simply
connect to your MYGATE  callsign. From there, they can issue a connect request to the
station they want to reach, and your station will be responsible for accepting and
sending packet data and acks. (Users can't digipeat through your MYGATE callsign.)
Users can also enter the MHEARD  command to see the last 18 stations your TNC has
heard.
For your node to work, simply enter a call into MYGATE-but not the same one as your
MYCALL, MYALIAS, or MYMAIL-and set GUSERS to a value greater than zero. To
disable the node function, enter MYGATE NONE or set GUSERS to zero.

<!-- PDF p.328 -->

Note: With each station connected to your node, you'll lose a "logical" channel. So,
if you have GUSERS set to 3, and three source stations have connected to three
destination stations through your node, they'll take up six channels.
Users can instruct your Gateway to set up another connection either on the same
radio port or the other one if XGATEWAY is ON.
The requirements for Gateway connection are:
 If Port 1 is to be used, OPMODE must be set to PACKET, AMTOR or PACTOR.
(Port 2, of course, is always PACKET).
 The RADIO command must be set to enable the appropriate port(s).
 A modem supporting the appropriate port(s) and operating mode(s) must be
selected using the MODEM command.
 MYCALL must be set for each radio port to be used.
 MYGATE must have a callsign, but not the same one as MYCALL, MYALIAS, or
MYMAIL.
 GUSERS must be set to 1, 2 or 3.
Once connected to your Gateway, a user deals with a command processor similar to
that in a "regular" node. Command responses from your Gateway start with "+++"
to show that they're coming from your Gateway command processor, rather than the
"***" from your TNC. ("+++" was chosen because the "+" is present in the AMTOR
character set while the other examples are not.)
Operation
Here's what a user would see when using your Gateway as a packet node. In this
example, your MYGATE call is set to N7ML-7:
cmd:C N7ML-7
*** CONNECTED to N7ML-7
+++ N7ML Gateway. Type ? for help.
de N7ML-7 (B,C,D,J,L,N,S,?) >
The first line is the user's connect request to your TNC. The second line is the connect
message from the user's TNC. The third line is the greeting, and the fourth is the
command prompt from the Gateway. The user sends a question mark, ?, to obtain the
following help menu:
B(ye)         Log off gateway
C(onnect) n   Connect to station 'n'
C n STAY      Stay connected to gateway when 'n' disconnects
D(isconnect)  Cancel a connect attempt
J(heard)      Display stations heard
L(isten)      Toggle monitoring
N(odes)       Display nodes heard
S(end)        Broadcast unproto (call CQ)
de N7ML-7 (B,C,D,J,L,N,S,?) >

<!-- PDF p.329 -->

The commands' functions are:
B(ye) This is similar to the Bye command used in the AEA Maildrop and BBS
stations. When a user enters a B, the Gateway will "disconnect."
C(onnect) n Similar in operation to the CONNECT command in the packet mode.
(Also used to connect in AMTOR and PACTOR instead of using the
ARQ and PTCONN commands, respectively.)
For a packet connection, the user may connect to your Gateway, then
specify a string of digipeaters:
C W1AW VIA W2XY, W1XXZ
Your Gateway will try to establish a connection with W1AW as the
destination; the user's callsign will be shown as the source but with a
difference: the user's SSID is decremented by one to avoid protocol
conflicts on the same frequency.
Here is an example of the frames sent in establishing a typical
connection (with the MONITOR command set to 5):
USER>GATE [C]
GATE>USER (UA)
GATE>USER [I]:
+++ N7ML Gateway. Type ? for help.
de GATE (B,C,D,J,L,N,S,?) >
USER>GATE (RR)
USER>GATE [I]:
c remote
GATE>USER (RR) USER-15>REMOTE [C]
REMOTE>USER-15 (UA)
GATE>USER [I]:
+++ CONNECTED to REMOTE at GATE
USER>GATE (RR)
USER>GATE [I]:
hello.
GATE>USER (RR) USER-15>REMOTE [I]:
hello.
REMOTE>USER-15 (RR)
REMOTE>USER-15 [I]:
Yes?
GATE>USER [I]: USER-15>REMOTE (RR)
Yes?
USER>GATE (RR)
Once the connection is established with the destination station, the
Gateway notifies the user that the connection has been made; it then
goes from the "Command" mode into the "Converse" mode. Now,

<!-- PDF p.330 -->

whatever the user sends, goes to the destination station as data
instead of to the Gateway as a command.
Normally, when someone disconnects from your Gateway, no link will
remain. However, if a user adds the word STAY as the last argument in
a Connect request, (e.g.,  C callsign STAY), the user will remain
connected to your Gateway even after disconnecting from the destination station.
If the connect attempt to the destination station retries out or is busy,
your Gateway sends the user a Retry count exceeded or (Remote) busy message, but remains connected to the user even if STAY
wasn't entered.
D(isconnect) (To cancel a connect attempt.) Since the source station remains in the
Command mode until the connection to the destination station is
established, there's no need for the user to wait for your Gateway to
cycle through a full number of retries to attempt a connection-the
user can send your Gateway a Disconnect request which cancels the
Connect request the same way it would in a TNC's Command mode.
(The user stays connected to your Gateway even if STAY wasn't used
in the original Connect command.) The Disconnect command may be
used at any time before the connection is established, regardless of any
preceding commands.
Once a connection is established and your Gateway is in the Converse
mode, the user can end the connection either by sending a B(ye)
command to the destination station if that station supports it, or by
issuing a Disconnect request to the user's own TNC. If the user
disconnects from your Gateway this way, it will force your Gateway to
disconnect the destination station.
J(heard) Your Gateway sends its MHEARD list to the user. Your Gateway's
DAYTIME and DAYSTAMP commands affect the display-the designators
"p1" and "p2" show the port on which each station was heard. A
maximum of eighteen stations are kept in the JHEARD list.
L(isten) The Gateway toggles monitoring on or off. The Gateway monitors
only on the radio port selected by the user (see PORT  below), using the
appropriate operating mode.
N(odes) Your Gateway sends the user a list of nodes heard. The format is the
same as that of the JHEARD command, the difference being that a
callsign is put in the Nodes list only if the monitored packet was a UI
frame with a PID of CF (NET/ROM) or CD (IP). A maximum of ten
stations are kept in the Nodes list. You clear the nodes list and the
MHEARD list simultaneously with the same command, MHEARD %.

<!-- PDF p.331 -->

S(end) Your Gateway responds with. . .
+++ Sending. To end, type '='. . . and sends all subsequent data in the broadcast format appropriate
to the selected port's operating mode. The data characters are held
until the user sends a (RETURN), whereupon the held data is broadcast.
In all operating modes, the user can stop sending "unproto" by
sending the "=" character-the Gateway will then issue a command
prompt. The "=" character shouldn't be used within the user's
broadcast text.
Cross-port gateway:
The requirements for cross-port Gateway connection are, in addition to the above:
 OPMODE must be PACKET, AMTOR, or PACTOR.
 If AMTOR is to be used, MYSELCAL must be set.
 If PACTOR is to be used, MYPTCALL must be set.
 If AMTOR or PACTOR are to be used, the receiver's volume and the DCD
threshold control must be set so that the DCD display is lit if-and only ifsignals are present.
 RADIO must be set to RADIO 1/2 (both ports enabled).
 Modems-using the MODEM command-supporting the appropriate OPMODE on
Port 1 and packet on Port 2 must be selected.
 XGATEWAY must be ON.
Packet (in) / Packet (out)
Here is what a user sees when using a cross-port Gateway as a packet node:
cmd:C N7ML-7
*** CONNECTED to N7ML-7
+++ N7ML Gateway. Other port (2) is 1200 bps Packet. Type ? for
help.
+++ You are on Port 1, 1200 bps Packet. Your ID is KB7B-15.
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
Note that there is extra information showing which radio port the user is on and the
configuration of the other radio port. The Help menu shows an additional command:
P(ort) 1/2    Access Port 1 or 2
P(ort) Changes the port on which the user wants to connect, listen or
broadcast. At the time the packet user connects to the gateway, access
is always on the same radio port so a Port 2 packet user must select

<!-- PDF p.332 -->

PORT 1 to initiate a cross-port connection. Typing "Port" by itself
shows the current value of PORT, the operating mode on that port, and
the user's ID to be used on that port. Port 1 AMTOR and PACTOR
users can only access Port 2 (packet), so the PORT command isn't
implemented for them.
L(isten) None of the Gateway's local commands affect monitoring-packet
frames are shown to the user as if MONITOR and MCON were set to a
value of 4 (UI, I, C, D, UA and DM frames). Other than that,
monitoring behaves as if all other commands were set to their default
values (no MPROTO, etc.).
SEND The Gateway sends UI frames with a destination of "CQ." The source
field is the user's callsign with the SSID decremented by one.
Packet (in) / AMTOR (out)
Here's what a Port 2 packet user (KB7B) would see if accessing the AMTOR mode
through Port 1 of your Gateway (ex.: N7ML-7):
cmd:C N7ML-7
*** CONNECTED to N7ML-7
+++ N7ML Gateway. Other port (1) is AMTOR. Type ? for help.
+++ You are on Port 2, 1200 bps Packet. Your ID is KB7B-15.
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
p 1
+++ Cross Access to Port 1, AMTOR. Your ID is KKBB.
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
At this point there are two ways for KB7B to establish an AMTOR link-the first is
to call a known station directly:
C NBCD
+++ CONNECTED to NBCD at N7ML-7
+++ Note:  Over = "+?", End = <CTRL-D>. You're sending.
N7BCD DE KB7B - HELLO +?
HI THERE...
Keeping in mind that only one station can transmit at a time, the packet user must
end each transmission with +? (RETURN) to let the other station respond. Either
station may end the communication; if it's KB7B, a (CTRL-D) packet frame tells the
Gateway to shut down the AMTOR link.
The other way to set up a link is to call CQ in AMTOR FEC and wait for an ARQ
response:
S
+++ Sending. To end, type '='.
CQ CQ CQ DE KB7B SELCAL KKBB
CQ CQ CQ DE KB7B SELCAL KKBB K=
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
+++ CONNECTED to ? at N7ML-7

<!-- PDF p.333 -->

+++ Note:  Over = "+?", End = <CTRL-D>. You're receiving.
KB7B DE VE7ZZY HI NAME IS RALPH +?
HI, RALPH . . .
Note that the Gateway reports CONNECTED to ? . When using four-character
identification, AMTOR has no provision for identifying the calling station. The caller
generally identifies within the transmission text.
L(isten) The Gateway TNC is placed in ARQ Listen mode (Mode L). If the
Gateway TNC's ARXTOR command is ON, it will also copy AMTOR FEC
(Mode B).
S(end) The Gateway is placed in AMTOR FEC (Mode B) transmit mode.
Transmission ends when the user sends "=".
Packet (in) / PACTOR (out)
Here's what a Port 2 packet user sees when crossing over to PACTOR:
cmd:C N7ML-7
*** CONNECTED to N7ML-7
+++ N7ML Gateway. Other port (1) is PACTOR. Type ? for help.
+++ You are on Port 2, 1200 bps Packet. Your ID is KB7B-15.
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
p 1
+++ Cross Access to Port 1, PACTOR. Your ID is KB7B.
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
One way the user can establish a PACTOR link is to call a station directly:
cmd:C WF7A STAY
+++ CONNECTED to WF7A at N7ML-7
+++ Note:  Over = <CTRL-Z>, End = <CTRL-D>. You're sending.
HI, RICH!
The other way is to call CQ in PACTOR Broadcast mode and wait for a response:
S
+++ Sending. To end, type '='.
CQ CQ CQ DE KB7B
CQ CQ CQ DE KB7B K=
de N7ML-7 (B,C,D,J,L,N,P,S,?) >
+++ CONNECTED to VE7ZZY at N7ML-7
+++ Note: Over = <CTRL-Z>, End = <CTRL-D>. You're receiving.
KB7B DE VE7ZZY - Hello...
Keeping in mind that only one station can transmit at a time, the packet station must
end each transmission with a (CTRL-Z)+(RETURN) to clue the PACTOR station to
respond. Either station may end the communication; if it's to be the packet station,
sending a (CTRL-D) forces the Gateway to shut down the PACTOR link.

<!-- PDF p.334 -->

L(isten) Your Gateway is placed in PACTOR-Listen mode. If the ARXTOR
command is ON, it will also monitor AMTOR FEC and ARQ. If in
doubt, use the PORT command to see which mode is being copied.
S(end) The Gateway is placed in PTSEND. Transmission ends when the user
sends "=".
AMTOR (in) / Packet (out)
With XGATEWAY OFF, your TNC accepts AMTOR calls to your MYSELCAL as normal
AMTOR link-ups. With XGATEWAY ON, an AMTOR call to your MYSELCAL results in
access to the Gateway and packet on Port 2. (The callsign in MYGATE plays no part in
Gateway access from AMTOR.)
The AMTOR Maildrop has priority over the AMTOR gateway function-this means
that with TMAIL ON, a station linking to your MYSELCAL can access your Maildrop.
However, the AMTOR user may type the new Maildrop command ("G") to access the
Gateway-this surrenders the Maildrop to possible access by other users. There is no
command in the Gateway to return to the Maildrop.
If a packet user is already connected to your Gateway on Port 2 and has issued the
PORT 1 command to access AMTOR, the Gateway detects AMTOR calls only to the
packet user's selcall, not your MYSELCAL.
There is no PORT command for AMTOR access. The AMTOR user is given access to
packet on Port 2, automatically.
An AMTOR station linked to the Gateway command processor doesn't need to send
"+?" to let the Gateway transmit. The Gateway detects the (RETURN) at the end of the
line, seizes the link to give the command response, and  then gives transmission back
to the user by ending the transmission with "+?".
Once a packet connection has been established on Port 2, the Gateway seizes the
AMTOR link on Port 1 only when it has data to send to the user, it then gives the link
back by ending the transmission with "+?".
Here is what an AMTOR user sees when connecting to your Gateway:
cmd:ARQ NNML
+++ N7ML GATEWAY. TYPE ? FOR HELP.
+++ ENTER YOUR CALLSIGN: +?
KB7B
+++ You are on PORT 2, 1200 BPS PACKET. YOUR ID IS KB7B-15.
KB7B DE N7ML-7 GA+?
One way to establish a packet connection is to call a station directly:
J
18:30:56  P2 N7BCD
KB7B DE N7ML-7 GA+?
C N7BCD STAY
+++ CONNECTED TO N7BCD AT N7ML-7

<!-- PDF p.335 -->

+?
HELLO
HI, NAME IS WALT ...
The other way is to call CQ in Unproto mode and wait for a response:
S
+++ SENDING. TO END, TYPE '='.
+?
CQ DE KB7B-15 FROM AMTOR=
KB7B DE N7ML-7 GA+?
+++ CONNECTED TO N7BCD AT N7ML-7
+?
HI, NAME IS WALT. WHAT DO YOU MEAN BY "FROM AMTOR"?
HELLO ...
One thing to keep in mind when accessing VHF packet from AMTOR is that the data
rates on the two radio ports are widely different. In reading a long message from a
packet BBS, the BBS sends the entire message to the Gateway packet port, then may
time out and disconnect before the message has completed transmission on the
AMTOR port. (The Gateway buffers the data so none of the message is lost.)
However, on the packet side, the BBS has wasted time waiting for the next command,
preventing other stations from using the BBS.
L(isten) Port 2 packet frames are shown to the user as if MONITOR and
MCON were set to a value of 4 (UI, I, C, D, UA and DM frames). Some
TNC monitoring characters are translated for AMTOR:
L
+++ LISTENING ON
KB7B DE N7ML-7 GA+?
P2 N7BCD)W1AW (C)
P2 N7BCD)W1AW (D)
+?
L
+++ LISTENING OFF
KB7B DE N7ML-7 GA+?
S(end) The Gateway sends UI frames with a destination of "CQ". The source
field is the user's callsign with the SSID decremented by one. The
Gateway holds the data characters until the user sends a (RETURN),
whereupon the held data is broadcast.
PACTOR (in) / Packet (out)
With XGATEWAY OFF, your TNC accepts PACTOR calls to your MYPTCALL as a normal
PACTOR link-up. With XGATEWAY ON, a PACTOR call to your MYPTCALL results in
access to the Gateway and therefore, packet mode on Port 2. (Your MYGATE call plays
no part in Gateway access from PACTOR.)

<!-- PDF p.336 -->

The PACTOR Maildrop (TMAIL ON) has priority over the PACTOR Gateway. The
Maildrop command ("G") gives access to the Gateway as in AMTOR (see above).
If a packet user is already connected to your Gateway on Port 2 and has issued the
PORT 1 command to access PACTOR, the Gateway detects PACTOR calls only to the
packet user's callsign, not your MYPTCALL.
There is no PORT command for PACTOR access-the PACTOR user is given access
to packet on Port 2 automatically.
A PACTOR station linked to the Gateway command processor doesn't need to send
the PTOVER character to put your Gateway into transmit. In the "Command" mode,
the Gateway detects the (RETURN) at the end of the line, seizes the link to give the
response, then gives transmission back to the user. Once a packet connection has
been established on Port 2, the Gateway seizes the PACTOR link on Port 1 only when
it has data to send to the user-then it gives the link back.
Here's what a PACTOR user sees when connecting to your Gateway:
cmd:PTCONN N7ML
+++ N7ML Gateway. Type ? for help.
+++ You are on Port 2, 1200 bps packet. Your ID is KB7B-15.
KB7B de N7ML (B,C,D,J,L,N,S,?) >
One way to establish a packet connection is to call a known station directly:
J
18:30:56  p2 N7BCD
KB7B de N7ML (B,C,D,J,L,N,S,?) >
C N7BCD STAY
+++ CONNECTED to N7BCD at N7ML-7
HELLO
Hi, name is Walt ...
The other way is to call CQ in the Unproto mode and wait for a response:
S
+++ Sending. To end, type =.
CQ DE KB7B-15 FROM PACTOR=
KB7B de N7ML (B,C,D,J,L,N,S,?) >
+++ CONNECTED to N7BCD at N7ML-7
Hi, name is Walt. What do you mean by "from pactor"?
HELLO ...
L(isten) Port 2 Packet frames are shown to the user as if MONITOR and
MCON were set to a value of 4 (UI, I, C, D, UA and DM frames.)
L
+++ Listening ON
KB7B de N7ML (B,C,D,J,L,N,S,?) >
p2 N7BCD>W1AW [C]
p2 N7BCD>W1AW [D]

<!-- PDF p.337 -->

L
+++ Listening OFF
KB7B de N7ML (B,C,D,J,L,N,S,?) >
S(end) The Gateway sends UI frames with a destination of "CQ." The source
field is the user's callsign with the SSID decremented by one. The
Gateway holds the data characters until the user sends a (RETURN),
whereupon the held data is broadcast.
Error messages to the connected station (user)
Bad An invalid callsign in response to the AMTOR prompt Enter your
callsign. An invalid callsign as one of the arguments in the Connect
command. An invalid argument to the PORT command.
Too many Too many callsigns in the Connect command. Arguments in the Bye,
Disconnect, Jheard, Listen, Nodes or Send commands.
Too long Command longer than 85 characters. PACTOR callsign in Connect
command was longer than 8 characters.
Range PORT command argument not 1 or 2.
Callsign No argument in the Connect command.
What? Unrecognized command.
?VIA Connect command: second argument wasn't STAY or VIA.
Not during connect
While a connect attempt was in progress, the user issued a Listen, a
Send, or another Connect command or tried to change Ports; the user
must first disconnect.
Already disconnected
The user tried to disconnect when no connect was in progress.
Need MYSelcal
The packet user tried to make an AMTOR connect, but your TNC's
MYSELCAL wasn't set.
None heard The JHEARD or Nodes list is empty.
Channel busy
Connect or Send command while there is AMTOR or PACTOR
activity on the channel. Gateway uses synced radio DCD to determine
activity.
Port busy Connect, Listen or Send command on a packet port where all multiconnect channels are being used. Attempt to use the PORT command
to access a port whose operating mode isn't packet, AMTOR Standby
or PACTOR Standby. Port 2 user attempt to access AMTOR or

<!-- PDF p.338 -->

PACTOR Port 1 if it was already in use by another Port 2 Packet user
of the Gateway.
Not while listening
While listening, the user issued a Send or Connect command, or tried
to change Ports. User must first type "Listen" again to toggle listening
off.
Miscellaneous
Every 9.5 minutes, the Gateway sends a Packet ID frame containing the Gateway
callsign. The ID frame is sent even if the HID command is OFF.
In translating AMTOR/PACTOR input to packet output, the Gateway bundles the
characters and sends them in packets every 10 seconds. The AMTOR/PACTOR user
should be aware that the packet station may receive the data characters anywhere
from 0 to 10 seconds after they've been sent from the Gateway.
No-activity timers: When the TNC's CHECK timer runs out on a packet channel
involving the Gateway, the Gateway stations are disconnected; CHECK defaults to 5
minutes. For AMTOR and PACTOR Gateway access, if "Traffic" status has not been
achieved for 5 minutes, the Gateway terminates the link gracefully and disconnects
any related packet connection on Port 2.
The MDMON command controls the monitoring of Gateway activity in addition to
monitoring Maildrop activity.
To operate AMTOR or PACTOR modes normally (no gateway), disable the crossmode gateway by setting XGATEWAY OFF. Subsequent user connect attempts to your
MYSELCAL or MYPTCALL will go to the TNC, not the Gateway.
Making changes in the OPMODE, RADIO, or MODEM command settings while a user is
connected won't kill the gateway session. However, it is a good idea to check first to
see if it's in use-do this by entering CSTATUS before changing the TNC's configuration.
It's important to set the receiver's volume and the DCD threshold control on the
AMTOR/PACTOR radio port so the DCD display follows the presence or absence of
signals. This prevents the Gateway SEND and CONNECT commands from interfering
with other stations. If DCD is active at the time of a command that transmits, the
Gateway sends the user a Channel busy message.
You can set ARXTOR ON and leave OPMODE set to PACTOR. This gives both AMTOR
and PACTOR users access to the Gateway (and/or the Maildrop.)
You can designate upper/lower case AMTOR operation for better packet interfacing
by setting CODE 2 (Cyrillic method), CODE 7, or 8. The AMTOR user, of course, must
set the comparable commands in his own TNC.

<!-- PDF p.339 -->

The local TNC commands CTEXT, CMSG, MTEXT, and MMSG  have no effect on Gateway
operation.
See the following pages for information about the new commands available to you
and enhancements to the current ones.
PACTOR & AMTOR operation with ARXTOR
The ARXTOR command has been added to enhance PACTOR operation. When ARXTOR
is turned ON, your DSP1232 will recognize either PACTOR or AMTOR link attempts.
In addition, when monitoring PACTOR stations with PTLIST, AMTOR stations will
also be heard if ARXTOR is ON.
ARXTOR is also useful for those running the PACTOR or AMTOR Maildrop. When
ARXTOR is ON, remote stations can connect to your maildrop in either PACTOR or
AMTOR. See the ARXTOR command description later in this supplement for the full
details.

<!-- PDF p.340 -->

### AList Immediate Command
**Mode:** AMTOR    Host: AL
In AMTOR Listen (and PACTOR Listen) modes, monitored link attempts are now
displayed like this:
>W1AW <C>
The callsigns are shown one per line and are meant to resemble the way the TNC
monitors packet connect frames. Since neither mode supports identification
within the calling blocks, no source callsign can be shown.
### ARXTor ON|OFF Default: OFF
**Mode:** AMTOR and PACTOR    Host: Ar
**Parameters:**
ON Enables automatic detection and switching between AMTOR and PACTOR
modes.
OFF Disables the automatic detection of non-selected operating modes.
The ARXTOR command allows the automatic switching from PACTOR Listen mode to
AMTOR FEC receive, and from PACTOR Standby to AMTOR ARQ. It also allows the
automatic switching from AMTOR Listen to AMTOR FEC receive.
With ARXTOR ON, an AMTOR FEC signal is detected by ALIST and PTLIST modes as
well as AMTOR Standby mode. There are two methods of FEC mode recognition.
AMTOR Standby, ALIST and PTLIST all use FEC idles to recognize FEC transmissions. However, in the AMTOR Standby mode, FEC text patterns are recognized as
an additional quick recognition method which may speed locking onto a FEC
signal.
ARXTOR ON enables PTLIST mode to monitor AMTOR ARQ transmissions.
ARXTOR ON also means an incoming AMTOR ARQ call is recognized in PACTOR
Standby mode. Only a call for the selcall in MYSELCAL is recognized; PACTOR
cannot detect ARQ calls to MYALTCAL or MYIDENT.
When a PACTOR mode detects an AMTOR transmission, there is an added delay
before the text is shown. Your TNC must switch from PACTOR to AMTOR, where the
signal is again detected. For fastest detection of AMTOR FEC, your TNC should
be in the AMTOR Standby mode. For fastest syncing on AMTOR ARQ signals, the
TNC should be in ALIST mode.
At the end of the new mode (AMTOR ARQ or FEC), your TNC returns to the original monitoring mode (AMTOR, ALIST, PACTOR or PTLIST).

<!-- PDF p.341 -->

ARXTOR is defaulted OFF to accommodate old application programs that have no
provision for handling a spontaneous change of modes from PACTOR to AMTOR.
ARXTOR is most versatile when in the PACTOR or PTLIST operating mode.
Here is a summary of the AMTOR/PACTOR mode switching:
Target mode              Original operating mode
detection           AMTOR ALIST PACTOR PTLIST
AMTOR FEC if RFEC if ARXT if ARXT
AMTOR ARQ (MYSELCAL) always if ARXT
AMTOR ARQ (MYALTCAL) always
AMTOR ARQ (MYIDENT) always
monitor AMTOR ARQ always if ARXT
SELFECalways if ARXT if ARXT
monitor SELFEC if SRXALL if ARXT & SRX if ARXT & SRX
PTCONN always always
monitor PACTOR always
Note: When automatically switching from PACTOR to AMTOR and back again, the
modem isn't changed.
### ATxrtty "n" Default: 0
**Mode:** Morse, Baudot and ASCII    Host: At
**Parameters:**
"n" 0 to 250, signifying the length of time (in units of 100 msec.) to delay
before sending text.
ATXRTTY allows Morse, Baudot or ASCII characters to be transmitted automatically whenever they're typed and the TNC is in the Converse mode. When all the
characters in the buffer have been sent, the unit reverts to receive.
The number
n represents the length of time from the last character typed to
the dropping of PTT. This feature makes the repeated use of the commands RCVE
and XMIT unnecessary.
### CODe "n" Default: 0 (International)
**Mode:** Baudot RTTY, Morse, AMTOR, PACTOR and packet    Host: C1
**Parameters:**
"n"  0 to 8 specifies a code from the list below.
CODE Meaning Morse Baudot AMTOR Packet
7 TOR Lower Case - - RX/TX -
8 Extended Lower Case - - RX/TX -

<!-- PDF p.342 -->

Two new settings of the CODE command have been added to support the European
and APLINK implementations of upper/lower case AMTOR.
CODE 7: TOR lowercase
CODE 7 applies to AMTOR operation only. It codes upper and lowercase letters
using the NULL character as a shift while in LTRS case. This protocol is used
by APLINK stations, European mailboxes, the AMT-3 and G4BMK software. The
difference between CODE 7 and CODE 2 (Cyrillic) upper/lower case is that CODE
2 uses LTRS for upper case and NULL for lowercase, while CODE 7 uses the NULL
to toggle between upper and lowercase. CODE 7 is invisible to stations using
classic AMTOR (CODE 0). However, a CODE 7 station talking to a station using
CODE 2, (AEA's already existing upper/lower case protocol), will result in
upper/lowercase reversals or constant lower case text.
CODE 8: Extended TOR lowercase
CODE 8 also applies to AMTOR only. It includes the features of CODE 7 above,
and additionally codes new punctuation characters using NULL as an escape
while in FIGS case. Thus, CODE 8 supports all 95 printable ASCII characters
($20-7E) plus CR, LF, space and ENQ while in AMTOR operating mode, but not
BELL, backspace and TAB. At the moment, this protocol is used only on links
between mailboxes forwarding messages. It could be used with the AEA AMTOR-to-Packet Gateway if all users had CODE 8. CODE 8 isn't invisible to other users.
### DAytime date and time Default: none
**Mode:** All    Host: DA
**Parameters:**
date and time - Current date and time to set.
DAYTIME sets the data controllers real time date and time clock. Optionally,
the following types of Dallas Semiconductor "Smart Watch" chips may be used to
hold the date and time when power is turned off.
RAM-Use the DS1216C in U7 (high RAM).
ROM-Use the DS1216E or DS1216F in U3.
Setting the DAYTIME command sets the time of day into any SmartWatch present
in the system. The SmartWatch is read only on power-up, RESTART or RESET.
### EXPert ON|OFF Default: OFF
**Mode:** All    Host: EX
**Parameters:**
OFF Disables some of the less frequently used data controller commands in
verbose mode.
ON Enables all data controller commands in verbose mode.

<!-- PDF p.343 -->

The EXPERT command controls your access to the TNC's command set. Because some
new TNC owners understandably find the large number of available commands
confusing or daunting, this command limits the newcomer's access to the commands to the simplest or most often used. Generally, about half of the total
number of commands are available to you after a RESET (EXPERT OFF). Once you
feel confident using the TNC's basic commands, turn EXPERT ON to explore and
try out its advanced command set.
With EXPERT OFF, expert-level commands can't be accessed and won't appear in
any output of the DISPLAY command-an attempt to use one of these commands will
result in the error message, "?EXPERT command."
All immediate commands (e.g. CONNECT, PACKET) are "Novice" commands. The error
message for an Expert command is now separate from the unknown command message:
cmd:FRICK
?EXPERT command
In Host mode, all commands are available regardless of the setting of EXPERT.
This command will not affect operation of AEA PAKRATT programs.
The following DISPLAY lists denote when a command is available while EXPERT is
OFF ("Novice"); "Retain" means the command keeps its setting during a REINIT
operation.
cmd: DISPLAY A
8Bitconv Novice Retain
ACRDisp
AFilter
ALFDisp
AUTOBaud
AWlen Novice Retain
BBSmsgs
CASedisp
DCdconn
Echo
EScape
Flow
ILfpack
NUCr
NULf
NULLs
PARity Novice Retain
TBaud Novice Retain
TRFlow
TXFlow
XFlow
cmd: DISPLAY B
3Rdparty Novice Retain
FREe Retain
KILONFWD Retain
LAstmsg Retain
MAildrop Novice Retain
MDMon Novice Retain
MDPrompt Novice Retain
MMsg Novice Retain
MTExt Novice Retain
MYMail Novice Retain
TMail Novice Retain
TMPrompt Novice Retain
cmd: DISPLAY C
BKondel
CANline
CANPac
CHCall
CHDouble
CHSwitch Novice
COMmand
CWid

<!-- PDF p.344 -->

DELete
ERrchar
HEReis
PASs
PTOver Novice
RECeive
REDispla
SEndpac
STArt
STOp
TIme
XOff
XON
cmd:  DISPLAY F
ASPect Novice
FAXNeg
FSpeed Novice
GRaphics Novice
LEftrite
PRCon Novice
PRFax
PROut
PRType Novice
cmd: DISPLAY I
Unproto Novice Retain
AAb Novice Retain
Beacon
BText Retain
CBell Retain
CMSg Novice
CText Novice Retain
HId Novice
HOMebbs Retain
MId Novice
MYAlias Retain
MYALTcal Retain
MYcall Novice Retain
MYGate Novice Retain
MYIdent Novice Retain
MYPTcall Novice Retain
MYSelcal Novice Retain
WRu
cmd: DISPLAY L
ACRPack
ALFPack
Ax25l2v2
CFrom Retain
CONMode
CONPerm Retain
DFrom Retain
FUlldup
GUsers Novice
HBaud Novice Retain
LIte
MAXframe Novice
NEwmode
NOmode
PACLen Novice
PASSAll
RAdio Novice
RELink
REtry Novice
SQuelch
TRIes Novice Retain
USers Novice
Vhf Novice
XGateway Novice
XMITOk Novice
cmd: DISPLAY M
CONStamp Novice
DAYStamp Novice
HEAderln
MBEll
MBx Retain
MCon Novice
MDigi Novice
MFIlter Novice
MFrom Retain
Monitor Novice
MProto
MRpt
MStamp Novice
MTo Retain
MXmit
TRACe
WHYnot Novice
cmd: DISPLAY Q
MODem Novice
QHpacket Novice
QVpacket Novice
QFax

<!-- PDF p.345 -->

QMORse
QPTor Novice
QRtty Novice
QSignal Novice
QTDm
QTor Novice
cmd: DISPLAY R
ABaud Novice
ACRRtty
ADelay Novice
ALFRtty
ANSample Novice
ARQTmo
ARQTOL
ARXTor Novice
ATxrtty
BItinv
CODe
CRAdd
DIDdle Novice
EAS Novice
MARsdisp
MOPtt Novice
MSPeed Novice
MWeight
NAVMsg Retain
NAVStn Retain
PT200 Novice
PTHuff Novice
PTRound Novice
RBaud Novice
RFec
RFRame
RXRev Novice
SRXall
TDBaud
TDChan
TXRev Novice
USOs Novice
WIdeshft Novice
WOrdout Novice
XBaud
cmd: DISPLAY T
ACKprior
AUdelay
AXDelay
AXHang
CHeck
CMdtime
CPactime
DWait
FRack Novice
FRIck
PACTime
PErsist Novice
PPersist
RESptime
SLottime Novice
TXdelay Novice
### GUsers "n" Default: 0
**Mode:** Packet, AMTOR, PACTOR    Host: GU
**Parameters:**
"n" 0 to 3 specifies the maximum number of users allowed on the gateway.
GUSERS allows up to "n" number of stations to connect to the callsign in
MYGATE for gateway operation. The variable "n" may be 0-3, with a default 0
meaning no other station may use your station as a gateway. Alternatively, n
can be thought of as the maximum number of pairs of stations which may be
connected through your Gateway.
GUSERS must be set to a number greater than 0 to enable the Gateway.

<!-- PDF p.346 -->

### MHeard Immediate Command
**Mode:** Packet and AMTOR/PACTOR Maildrop    Host: MH
The MHEARD display has been enhanced to support the "Nodes" command in the
gateway.
Previously, stations heard directly were displayed with an asterisk ("W1AW*")
and digipeated stations were shown without ("W2SZ"). Digipeating isn't used as
much as it used to be. Most stations now use nodes so this release discards
the asterisk. However, for those few cases in which a station is heard indirectly through a digipeater, the station's callsign is displayed with the
message, "via digi".
In addition, I- and U-frame packets with PIDs of CF and CD are shown with the
indicators "N/R" (for Net/ROM) and "IP" respectively. AMTOR and PACTOR stations accessing the Maildrop or the gateway are shown in the MHEARD list with
an "AMTOR" or "PACTOR" indicator.
### MId "n/n" Default: 0/0 (0 sec.)
**Mode:** Packet, AMTOR, PACTOR    Host: Mi
**Parameters:**
"n" 0 to 250 specifies the Morse ID timing in units of 10-second intervals.
Morse ID now works in AMTOR and PACTOR modes on both ARQ and broadcast transmissions. At intervals you set, your TNC identifies itself in Morse Code while
maintaining the internal timing for AMTOR or PACTOR. Because of the nature of
these operating modes, the destination station will go into an error state
when your TNC sends a Morse ID, but it should recover data synchronization
immediately afterwards.
### MOPtt ON|OFF Default: ON
**Mode:** Morse    Host: Mo
**Parameters:**
ON Enables PTT in Morse transmit mode.
OFF Disables PTT in Morse transmit mode.
MOPTT controls the PTT output in Morse mode only. To enable PTT for Morse
transmissions, both XMITOK and MOPTT must be ON. XMITOK OFF still disables PTT
for all operating modes.
The most probable use of MOPTT is to disable PTT in Morse to allow full break-in operation but enable PTT in all the other modes. Setting XMITOK ON and
MOPTT OFF accomplishes this.

<!-- PDF p.347 -->

MOPTT doesn't affect the Morse IDs generated by the MID command (in Packet
mode) and by the CWID character (in other digital operating modes).
### MYAlias call[-n]/call[-n] Default: none
**Mode:** Packet    Host: MA
**Parameters:**
call Alternate packet callsign may be used by other stations to connect to
your station.
"n" 0 to 15, an optional substation ID (SSID)
For those households with two operators taking turns using the PK-900, the TNC
will now accept connections to both MYCALL and MYALIAS. Previously, MYALIAS
had been reserved for stations digipeating through your station.
If MYMAIL isn't set, the Maildrop also accepts connections to either MYCALL or
MYALIAS.
Outgoing connect attempts and Unproto frames use only MYCALL as the source
callsign.
### MYGate call[-n] Default: none
**Mode:** Packet    Host: MY
**Parameters:**
call Packet Gateway and "Node" callsign used by other stations.
"n" 0 to 15, an optional substation ID (SSID)
Call is the callsign of the Gateway or Node function of your TNC.
See the Gateway section for a description of the AEA Node and cross-mode gateway features.
### OVer Immediate Command
**Mode:** AMTOR/PACTOR    Host: OV
An immediate command that reverses the link direction from ISS to IRS; this
can be considered the opposite of the function of the ACHG command.
The changeover happens as soon as possible-the TNC doesn't wait for all the
characters in the buffer to be sent. OVER should be thought of as analogous to
the RCVE command (neither command waits for the buffer to empty) the same way
the PTOVER character is analogous to the RECEIVE character (both wait for
empty). Host applications can use the ZSTATUS command to detect when all characters have been sent.

<!-- PDF p.348 -->

In PTCONN, this command accomplishes the same thing as sending the PTOVER
character. The OVER command is useful in Host mode when sending transparent
data (CONMODE TRANS). To change from ISS to IRS, you would normally send the
PTOVER character, but in Transparent mode, the character would be sent as data
and would not change the link direction. The OVER command changes the direction without the need to change CONMODE to CONV first.
In AMTOR ARQ, this command inserts "+?" into the data stream being sent. If
EAS is ON, the "+?" is echoed to the terminal.
### PTList Immediate Command
**Mode:** PACTOR    Host: PN
**Parameters:**
In PACTOR Listen (and AMTOR LISTEN) modes, monitored connect attempts are now
displayed like this:
>W1AW <C>
The callsigns are shown one per line and are meant to resemble the way the TNC
monitors connect frames in the packet mode. Since neither mode supports identification within the calling blocks, no source callsign can be shown.
### PTRound ON|OFF Default: OFF
**Mode:** PACTOR    Host: Pr
**Parameters:**
OFF Returns the TNC to the PACTOR-Standby mode after a PTSEND transmission.
ON Returns the TNC to the PACTOR-Listen mode after a PTSEND transmission.
PTROUND facilitates PACTOR "roundtable" conversations with multiple stations
using the PTSEND (FEC) mode as opposed to the Connected mode.
As the unit finishes sending a PTSEND transmission, it normally returns to
PACTOR Standby mode. If PTROUND is ON, the unit returns to PTLIST instead in
order to copy another station's PACTOR transmission. PTROUND has no effect
when PACTOR connection ends-the unit will always return to PACTOR Standby.
### REINIT Immediate Command
**Mode:** All    Host: RI
This is an immediate command that you can invoke to get out of trouble caused
by setting a lot of commands-especially timing parameters-to strange values.
REINIT can be thought of as being halfway between RESTART and RESET. REINIT
re-initializes most of the  commands to their default settings, then does a

<!-- PDF p.349 -->

RESTART, but the contents of the Maildrop and the NAVTEX message history buffers are preserved. The commands that are preserved are:
MYCALL MYALIAS MYMAIL HOMEBBS MYGATE MYSELCAL
MYALTCAL MYIDENT MYPTCALL UNPROTO AWLEN PARITY
TBAUD BTEXT CTEXT AAB MDPROMPT TMPROMPT
CFROM DFROM MFROM MTO MBX LASTMSG
MTEXT NAVSTN NAVMSG HOST 8BITCONV 3RDPARTY
FREE KILONFWD MAILDROP MDMON MMSG TMAIL
CBELL CONPERM HBAUD EXPERT KISS TRIES
In Host mode, the REINIT command is acknowledged by a RESTART response (RT).
### SIgnal Immediate Command
**Mode:** All    Host: SI
SIGNAL is an immediate command that causes the PK-900 to enter the Signal
Identification and Acquisition Mode (SIAM).  The PK-900 now identifies
the PACTOR mode.
### WOrdout ON|OFF Default: OFF
**Mode:** Baudot, ASCII, AMTOR PACTOR and Morse    Host: WO
**Parameters:**
OFF Typed characters are sent directly to the transmitter.
ON Type characters are held in the PK-900's transmit buffer until a space,
CR, LF, TAB, RECEIVE CWID, ENQ or '+?' character(s) is typed.
With WORDOUT OFF, the backspace character is transmitted in Baudot, ASCII,
AMTOR and PACTOR modes. With WORDOUT ON, pressing the backspace key cancels
out the preceding character and neither are transmitted.
In Baudot and AMTOR, the backspace character is transmitted as a "?" since
there's no backspace in those modes.
In ASCII the backspace character is transmitted, but the destination station
must be able to pass it. AEA products should have MFILTER set to zero to allow
backspaces to print when monitoring.

<!-- PDF p.350 -->

### XGateway ON|OFF Default: OFF
**Mode:** Packet, AMTOR and PACTOR    Host: XG
**Parameters:**
OFF Stations connecting to your MYGATE callsign don't have access to the
other radio port.
ON Stations connecting to your MYGATE callsign can cross-connect to the
other radio port.
XGATEWAY must be turned on to allow cross-port connections; the PORT command
(see GUSERS) must be enabled in order to accomplish this.

<!-- PDF p.351 -->

> [No extractable text on this page — figure, schematic, or blank.]

<!-- PDF p.352 -->

> [No extractable text on this page — figure, schematic, or blank.]