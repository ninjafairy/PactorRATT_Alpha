# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 4 — Packet Radio (PDF p.43–86)

<!-- PDF p.43 -->

### 4.1 Overview

In the last several years Packet has grown to become perhaps the most
popular digital mode on the amateur bands.  Although Packet can be
found on HF (primarily on the 20 meter band) it is most popular on the
VHF and UHF FM bands.  This chapter will start with general Packet
operation and then discuss VHF and UHF Packet.  Packet on the HF bands
requires some special considerations, so we will leave it until the
end of this chapter.

Your PK-900 can operate HF or VHF Packet (on Radio port 2) and any
other digital mode on Radio port 1 at the same time.  This feature can
come in handy allowing you to keep an "ear" on local packet activity
while operating on any of the other digital modes normally found on
HF.  Sorting out the data from both Radio ports also requires some
special considerations and will be discussed later in the chapter
after HF Packet operation has been covered.

#### 4.1.1 Getting Started

You can learn quite a bit about Packet operation with the
PK-900 before even connecting it to a transceiver.  For your first
packet practice, the PK-900 will be connected in a "loopback"
circuit as done in the "Quick Check" performed in Chapter 2.  In this
configuration, the PK-900 will "talk to itself" which allows you to
become familiar with packet before actually going on the air.

#### 4.1.2 Making the Loopback Connection

Make sure the PK-900 is turned OFF and power is removed before
performing one of the following.

I    Locate the  WIRE "Loop-back" jumper and insert it into pins
1 and 4 of the RADIO-1 connector on the PK-900 rear panel.

II   If you cannot locate the wire "Loop-back" jumper and have a
spare Radio cable, you may construct the following:

1.   Get the unused Radio Cable mentioned in Chapter 3.
2.   Strip about 1/4 inch of insulation from the Green and White
wires at the "radio" end of the cable.
3.   Join the Green and White wires by twisting them together
gently.
4.   Insert the 5-pin DIN connector end of the cable into the
"RADIO-1" connector on the PK-900's rear panel.
5.   Go on to section 4.2.

<!-- PDF p.44 -->

### 4.2 Packet Introduction

You've now connected your PK-900's transmit audio output to its
receive audio input.  Your PK-900 can now "talk to itself" in
packet.

1.   Set the rear-panel AFSK level control for RADIO-1 at 50%
(straight up and down) for the following Packet introduction.

NOTE:     If you have adjusted the RADIO-1 rear-panel AFSK level
control for a particular transceiver in chapter 3, then mark
this setting with a pencil so it can be reset when finished.

2.   Turn on your computer.  Load and run your communications program.

If you are using an AEA PAKRATT Program, follow the program
manual's instructions to enter the Packet mode on Radio Port 1,
and then skip to step 4.

If you are using another Computer program or a Terminal, set the
communication parameters as done in Chapters 2 and 3.

3.   Press the PK-900's front panel power switch to the ON position
if you have not already done so.

You should notice the sign-on message seen in chapter 2.  The LCD
Status display should indicate you are in the Packet mode.

4.   After you have seen the sign-on message and/or entered the Packet
mode, you must enter your own callsign (with the MYCALL Command)
if you are to converse with any other Packet stations.  If you
try to connect to a station without entering your call sign, your
PK-900 will send you the following message:

?need MYcall

If you are an SWL and do not intend to transmit, you should enter
"AAA" as a Callsign (MYCALL).

If you are using an AEA PAKRATT program, follow the instructions
in the Program Manual to enter your callsign.  If you are using a
terminal or terminal emulating program on your computer, you must
use the MYCALL command to install your call sign.

Note that since the PK-900 has two radio ports, you must enter
a callsign for each port.  The callsigns may be the same, or can
be different to make life a little easier in the "two Ham"
family.  You must change the callsign from the default "PK900"
on each radio port, or that port will not transmit packets.  For
example, if your Callsign is WX2BBB, enter the following:

cmd:MYCALL WX2BBB/WX2BBB <Enter>

The PK-900 will respond with:

MYcall   was PK900/PK900
MYcall   now WX2BBB/WX2BBB

<!-- PDF p.45 -->

Both Packet radio ports will now assume the callsign WX2BBB.

5.   If you are using an AEA program follow the instructions in the
Program manual to CONNECT in packet mode to your own callsign
(the one you just entered in MYCALL).

If you are using a Computer Terminal or a non-AEA program,
entering the following after the "cmd:" command mode prompt will
cause the PK-900 to Connect to yourself:

CONNECT (your callsign) <Enter>

After pressing <Enter>, you should observe the RADIO 1 TX LCD
and DCD LCD light briefly, and the  TUNING bar graph
spread.
After a few moments, your monitor should display:

*** CONNECTED to (your callsign)

Notice that the front panel RADIO 1 Connect LCD shows CONNECTED.
You may also notice that the  CONVERSE LCD is also on
indicating that the PK-900 is ready to CONVERSE with the
station (in this case it is yourself).

This is how a packet connection is established.  Whether you
are "Connecting" to another Amateur, a Packet Bulletin Board
System (PBBS) or a Networking Switch (more on this later)
this initial procedure must be used to establish each and
every Connection.

6.   Type a few characters to yourself such as "Hello this is my first
packet Connect. My name is ..." then press the <Enter> key.

Shortly after pressing the <Enter> key, you should see the
message you typed reappear on your screen.  If you had connected
to a distant station, they would have seen the message you typed
appear at nearly the same time.

7.   After you have typed a few lines (packets) to yourself, you will
probably want to end the connection or "DISCONNECT".
If you are using an AEA program, follow the instructions to
DISCONNECT from the packet station you are talking to.

If you are using a Computer Terminal or a non-AEA terminal
program, the following will cause the PK-900 to DISCONNECT:

Enter <CONTROL-C> (hold down the Ctrl and then type the letter C).
Your monitor should respond with the command prompt:

cmd:

Enter the following:  D <Enter>.
Your monitor should respond with:

cmd:*** DISCONNECTED: (your callsign)
p1 (your call)*>(your call) (UA)

<!-- PDF p.46 -->

Note that the front panel CONNECT LCD will change to
DISCONNECTED indicating that you are no longer connected.

8.   You have just done the three things necessary in any Packet QSO.

- You started the QSO (with yourself) by CONNECTing. (Step 5)
- You sent some information (to yourself) and then received
the information that you sent. (Step 6)
- You then ended the QSO by DISCONNEcting. (Step 7)

Repeat steps 5, 6 and 7 above until you feel comfortable with
Connecting, exchanging information and Disconnecting.  These
operations will be performed each time you use Packet so they
should be second nature to you before going on the air.

When you feel comfortable Connecting, sending information and
Disconnecting, you are ready to start listening to VHF packet.

9.   Turn OFF and remove power from the PK-900.

- Return the Port 1 rear-panel AFSK level potentiometer to
the setting you marked in Step 1, if any.

- Remove the "Loop-back" jumper from the RADIO-1 port and
reconnect your transceiver or receiver set-up in Chapter 3.

### 4.3 VHF/UHF Packet Operation

We will first listen to (and watch) some of the VHF or UHF Packet
activity in your Local Area.  This will allow you to become a little
better acquainted with packet in your area before going "On The Air".

1.   Construct a Radio Cable for the VHF/UHF transceiver you intend to
use for Packet as described in Chapter 3 and connect your
transceiver to the RADIO-1 connector on the PK-900 rear panel.

2.   Load and run your communications program and enter the Packet
Mode as done in the Packet Introduction section above.

3.   Set Radio Port 1 for VHF Packet operation (default) as follows.

If you are using an AEA program, follow the instructions to
select the VHF modem on Port 1 by turning the VHF Parameter ON,
this will automatically set the radio baud rate HBAUD to 1200.

If you are using a Computer Terminal or a non-AEA terminal
program, the following sets the VHF mode of the PK-900:

Type <CONTROL-C>.  (Type C while pressing the <Ctrl> key down.)
Your monitor should respond with the command prompt:

cmd:

Then enter VHF ON <Enter>
Your monitor should respond with:

<!-- PDF p.47 -->

Vhf    was OFF/ON
Vhf    now ON/ON

Then enter HB 1200 <Enter>
Your monitor should respond with:

HB     was 300/1200
HB     now 1200/1200

4.   Turn ON your VHF or UHF FM transceiver and tune to a known packet
channel in your area.  Most packet operation is on simplex, so
the repeater offset on your transceiver should be disabled.
If you know there is packet in your area, but do not know the
frequency, you should try some of the following frequencies:

2 meter (144 MHz) band:
145.01 MHz, 145.03 MHz, 145.05 MHz, 145.07 MHz, 145.09 MHz
144.99 MHz, 144.97 MHz, 144.95 MHz, 144.93 MHz, 144.91 MHz

1-1/4 meter (220 MHz) band:
223.40 MHz, 223.42 MHz, 223.44 MHz, 223.46 MHz, 223.48 MHz

70 cm (440 MHz) band:
440.975 MHz, 441.000 MHz, 441.050 MHz, 441.025 MHz, 441.075 MHz

You know you've found a packet channel when you hear the
characteristic "Braaaaaap" sound of packet transmissions.

5.   Once you've found an active packet channel, you must make sure
you have enough receive audio (volume) from your transceiver to
light the DCD LCD on the PK-900 when a packet is being
received.  If the DCD LCD does not light when packets are
received, either there is not enough receiver audio or the
Threshold control is turned too far counter clockwise.

You must also make sure that the DCD LCD goes out when no packet
signals are present on the channel.  If the DCD LCD does not go
out when the channel is clear, make sure the Squelch control on
your transceiver is set high enough to silence the speaker.  If
the DCD LCD stays on when the packet channel is quiet, your
PK-900 will not send packets to other stations.

<!-- PDF p.48 -->

#### 4.3.1 What You Should See

If all is operating properly, you should see some packets on your
screen.  Some typical packets you might "Monitor" are shown below:

p1 N7ALW*>WA7GCI [C]
p1 WA7GCI*>N7ALW (UA)

p1 K6RFK>N7ALW*>N7GMF:
Goodnight John, its been nice talking to you.

p1 N7ALW*>WA7GCI:
Hi Bob, how are you this evening?

p1 KD7NM*>MAIL:
Mail for: K6RFK N7ML

p1 N7HWD-8*>ID:
NET/ROM 1.3 (SEA)

p1 SEA*>N7ML:
SEA:N7HWD-8> Connected to #SEA:N7HWD-7

p1 K6RFK>N7ALW*>N7GMF [D]
N7GMF>N7ALW*>K6RFK (UA)

NOTE:  You will probably see data (Packets) on the tuning bar-graph
which do not print on the screen.  This is normal and is a
function of the MONITOR and the MPROTO commands.

#### 4.3.2 What It Means

There are different types of packets that mean different things to the
PK-900.  Your PK-900 keeps track of and knows what to do with the
packets so users need not be concerned with them most of the time.
Since the PK-900 can "Monitor" all Packet activity on a channel,
we'll briefly discuss the types of packets you will most often see.
Skip to the next section if you do not plan on doing much monitoring.

Let's look at the first packet in the examples above and get
acquainted with what it all means.

p1 N7ALW*>WA7GCI [C]

The first two characters in monitored packets represents which radio
port "heard" the packet and will be either "p1" or "p2".  Every single
packet you send will have your callsign (the one you just entered in
MYCALL) as the first callsign of the packet.  The callsign after the
">" is the next station the packet will go to.  So the packet listed
above originates from N7ALW and is being sent to WA7GCI.  All packets
will have at least these two callsign fields.

The "[C]" immediately following the two callsigns identifies this
packet as a CONNECT Request.  So we see that N7ALW is requesting a
packet CONNECTION with WA7GCI.

<!-- PDF p.49 -->

The second packet in the above examples is a response to the first.

p1 WA7GCI*>N7ALW (UA)

In this case we see that WA7GCI is sending to N7ALW by the order of
the callsigns.  This packet acknowledges the Connect request as shown
by the "(UA)" which stands for Un-numbered Acknowledge.

One benefit of packet radio is that packets can be relayed or
"digipeated" by stations on the same frequency.  In fact, packets can
be relayed by up to eight stations to reach a distant station that
cannot be heard directly.  In practice, digipeating through many
stations does not work very well, but you will often see packets
digipeating through one or two stations to reach their destination.
The packet shown below is an example of a digipeated packet.

p1 K6RFK>N7ALW*>N7GMF:
Goodnight John, its been nice talking to you.

This packet originated from K6RFK and is being sent to N7GMF but is
"Digipeated" through the station N7ALW.  We also see that this packet
contains data by the text "Goodnight John...".  Another thing that
should be noticed in this packet is the asterisk (*) in the first
line.  The asterisk tells which station was actually heard sending the
packet.  In this case, we can see that we actually heard the
digipeating radio station N7ALW.  Without the asterisk, we could not
tell whether the transmission came from radio station K6RFK or N7ALW.
More will be discussed about digipeating later, but the above example
is typical.

The following packet is a data packet from N7ALW to WA7GCI.

p1 N7ALW*>WA7GCI:
Hi Bob, how are you this evening?

Remember that in the first example we saw the two stations Connect.
Now that they are connected, they may exchange data packets.

The following packet is a Beacon packet from KD7NM.  Since we see the
packet is addressed to "MAIL" we know KD7NM is probably a Packet
Bulletin Board System (PBBS).

p1 KD7NM*>MAIL:
Mail for: K6RFK N7ML

The data section of this packet says "Mail for: K6RFK N7ML".  This
Beacon lets people know that K6RFK and N7ML have mail waiting on the
KD7NM PBBS without having to connect.

<!-- PDF p.50 -->

The following Beacon packet is intended as identification for a
NET/ROM level-3 packet networking switch.

p1 N7HWD-8*>ID:
NET/ROM 1.3 (SEA)

In this case, the Packet Switch is using the callsign N7HWD-8, but
also uses the alias SEA as a callsign.  There are many types of Packet
Switches now in use, but NET/ROM is one of the most popular.  We will
briefly discuss using a NET/ROM switch later in this chapter since
most switches operate in much the same way.

The packet below was sent by the network switch SEA to N7ML.

p1 SEA*>N7ML:
SEA:N7HWD-8> Connected to #SEA:N7HWD-7

The packet above from SEA contains the data "SEA:N7HWD-8> Connected to
#SEA:N7HWD-7".  This message tells N7ML that he is now connected to
another port on the SEA Node named #SEA.  Again, we will talk more
about how and why N7ML might want to do this later in the chapter.

The following packet is again from K6RFK to N7GMF and is being
digipeated through N7ALW.  This packet indicates that K6RFK is
finished talking to N7GMF and wants to Disconnect.  Again we see that
we are not hearing K6RFK, rather we are hearing N7ALW as indicated by
the asterisk (*) after the callsign.

p1 K6RFK>N7ALW*>N7GMF [D]

The following packet is an acknowledgment (or simply called an ACK)
that lets K6RFK know that N7GMF has acknowledged the Disconnect
request sent above.  K6RFK and N7GMF are no longer Connected.

p1 N7GMF>N7ALW*>K6RFK (UA)

As can be seen, all of the above examples were heard on radio port 1
of the PK-900.  If a second radio was connected to radio port 2,
we would have seen some monitored packets prefaced with a "p2" as
well.  See section 4.8 for more information on controlling both
radio ports when you are ready.

NOTE:  Some applications software such as PB and PG for Amateur
Satellite use have problems with the port designators "p1" and
"p2" in front of monitored packets.  To disable the port
designators "p1" and "p2" from appearing turn OFF User Bit 19
(UBIT 19) by typing "UBIT 19 OFF <Enter>" at the command
prompt.

<!-- PDF p.51 -->

#### 4.3.3 What Happens When You Connect

If you are working with a friend who is familiar with packet, you may
want to skip to section 4.4.  If you are on your own, the following
three sections will help you learn what to expect on VHF/UHF packet.

There are three different kinds of packet stations you are likely to
encounter in your first Connects: Standard TNCs, Mailbox Systems and
Network Switches.  The following sections discuss each station type.

##### 4.3.3.1 Standard TNCs

When you first turn on your PK-900, it becomes a standard AX.25
packet TNC (Terminal Node Controller).  All TNCs and Multimode
controllers have this capability.  When you Connect to a TNC, in most
cases you will be connecting directly to someone's computer screen.
If you see an automatic Connect Message (CMSG) similar to the one
below, you know you have reached a TNC.

Welcome to my packet station.  If I don't respond, please
leave a message and Disconnect.

If you get a message like this when you connect to another station,
usually you would type something like "Are you there?".  If you do not
see a response from the other station in a minute or so, simply leave
a message - just like a telephone answering machine.

The TNC at the other station should then hold your message until the
operator returns to his computer.  Later we will discuss how your
PK-900 can do the same for messages it receives from others.

##### 4.3.3.2 Mailbox Message Systems

Although Standard TNCs allow incoming messages to be saved, there is
no way for the owner to leave a message for someone who will connect
at a future time.  The ability to both send and receive messages
without the owner being present is accomplished by a Mailbox.

There are many different Packet Mailbox systems in use.  Some systems
are large and require the use of a dedicated computer.  Other systems
are small like the personal MailDrop built into your PK-900.

Large systems are often called Packet Bulletin Board Systems (PBBS)
since they serve as electronic message centers for a local area.
PBBS's are a source of information as well as a gateway for messages
that can be sent to and received from other parts of the country or
world.  You will probably want to locate the local area PBBS nearest
you and connect to it from time to time.

Mailbox systems are easy to use and most operate in much the same way.
Most Mailboxes and other automatic systems usually have Help available
by sending an "H" or "?".  If you connect to a Mailbox such as a
PK-900 MailDrop you will see something like the following:

*** CONNECTED to KD7NM
[AEA PK-900]  17480 free  (A,B,H,J,K,L,R,S,V,?) >

<!-- PDF p.52 -->

If you get something like this when you connect to another station,
try typing an "H" or a "?" to get a help list as shown below:

A(bort)   Stop Read or List
B(ye)     Log off
H(elp)    Display this message
J(log)    Display stations heard
K(ill)    K n: Kill message number n
KM : Kill messages you have read
L(ist)    L  : List message titles
LM : List messages to you
R(ead)    R n: Read message number n
RM : Read all your unread messages
S(end)    S  : Send a message to SYSOP
S n: Send a message to station n
?         Same as H(elp)
[AEA PK-900]  17480 free  (A,B,H,J,K,L,R,S,V,?) >

There are quite a few options available on the MailDrop, but the most
commonly used commands are L(ist), R(ead), S(end) and K(ill) message.

For example, you may first want to LIST all the messages that are
available on a mailbox that you connect to.  This is done by simply
sending "L" or "LIST" command to the system you have just connected.

If you are interested in any of the message subjects that appear, you
may then READ the messages that interest you.  To read a message,
simply send the command "R (message number)", where (message number)
is the number of the message you are interested in.

After you are finished reading messages, you may want to SEND a
message to the SYSOP (short for System Operator) or to another user.
To send a message simply enter "S (callsign)" where (callsign) is the
call of the station you are sending the message to.

When you are finished Listing, Reading and Sending messages, you will
want to send the Bye command to log-off (disconnect) from the Mailbox.

Feel free to experiment with Mailboxes and other packet systems.
Remember that most automatic systems will send you help on commands if
you send an "H" or "?".  For more information on setting up and using
your own PK-900 Maildrop, see Chapter 5 on MailDrop Operation.

##### 4.3.3.3 Packet Switches and "Nodes"

When Amateur Packet radio was first beginning there were not many
stations on the air.  Amateurs at that time "digipeated" through many
stations (up to 8) to connect to others over long distances.  As more
users became active on packet, digipeating quickly proved to be an
inefficient way of relaying packets through even a very few stations.

To solve this problem, Amateurs began working on more efficient
"higher level" ways of routing packets over long distances.
NET/ROM (tm), ROSE, TCP/IP and TEXNET are some of the higher level
protocols that emerged and are currently in use around the world.

<!-- PDF p.53 -->

NET/ROM, developed by Software 2000, quickly became a standard that
others imitated.  Many networking "Nodes" today use a similar if not
identical set of commands.  We will discuss the typical NET/ROM
commands you will likely encounter when connecting to a packet switch.

When you connect to a NET/ROM Node you will not initially get any
prompt.  Since NET/ROM commands are few and easily memorized, they did
not see a need to clutter the channel with prompts.  Like other
automatic systems however, if you send an "H" or a "?" for Help you
can expect to get a "Help" response similar to the following:

SEA:N7HWD-8> Invalid Command (CONNECT INFO NODES ROUTES USERS)

In our example, the line above is from the Seattle node, simply known
as SEA.  The callsign for the node is N7HWD-8.  "Invalid Command"
means that the node did not understand the command you sent, so it
returned the above "help" line to remind the user of the commands it
knows.  These are CONNECT, INFO, NODES, ROUTES and USERS.

Most often you will use the nodes CONNECT command to connect to other
stations.  Once you have connected to the node, simply send the
command "CONNECT (callsign)" or simply "C (callsign) where (callsign)
is the call of the packet station you want to connect to that is in
range of the node.

Not everyone you want to talk to is in range of your local Node.
Fortunately, NET/ROM will learn about other nodes it can reach and
allow you to connect to these nodes as well.  To find out what other
nodes your local station can reach, simply type the command "NODES"
after you connect.  This will display something like the following:

SEA:N7HWD-8> Nodes:
BALDY:WB6VAC-8     BOI:W7SC           BOISE:N7FYZ-8    COE:KK7X-4
ELN:N7HHU-8        EVT:KA7VEE-8       LSO:K7ZVV-8      MCW:WB7DOW-12
MSO:W7DVK-5        OLY:K7APT-8        PDT:N7ERT-5      PDX7:KA7AGH-8
PTN:K7TPN-8        RLIMB:W0RLI-2      SALEM:AF7S-1     SEAW:N8GNJ-8
SPOKN:WB7NNF-8     SVBBS:KA7RNX       TAC:W7DK-8       YKM:K3GPJ-8

When you connect to a node (either directly or through another
node) you may want to know who else is using that particular node.
Type the command "USERS" to find out who is using the system.  You
will see your own call in the list as well as anyone else who is using
the node.  An example is shown below:

SEA:N7HWD-8> NET/ROM Version 1.3 (662)
Uplink(W7MCU)                        <-->  Downlink(W7MCU-15 WA7ZUE)
Circuit(SEAW:N8GNJ-8 KA7RZK)
Uplink("your callsign")

The IDENT command simply sends you an identification packet from the
node that may give its location and owner as shown below:

SEA:N7HWD-8> NORTHWEST AMATEUR PACKET RADIO ASSOCIATION
145.01 MHZ, USER LAN, GRASS MTN.
Local BBS is N7HFZ

<!-- PDF p.54 -->

The ROUTES command provides routing information about other nodes that
can be reached.

A complete discussion of NET/ROM is beyond the scope of this manual,
but we hope the above information will help get you started.
Certainly the CONNECT, NODES and USERS commands will allow you to
navigate through the network, and find new people to talk to.

#### 4.3.4 Who Can I Talk To?

Now that you understand a little about the different packets and
packet stations, you are ready to make your first real connection.

If you do not have a friend on Packet in your local area, then you
will want to choose a station you can reach.  Fortunately the PK-900
has a command called MHEARD that displays the list of the 18 most
recently heard stations.  Check this list in one of the following ways:

I    If you are using an AEA PAKRATT program, follow the instructions
in the program manual for checking the Packet MHEARD list.

II   If you are using a Terminal or Terminal Program on your computer,
then first type a <CTRL-C> to make sure you are in the PK-900
Command (cmd:) mode.  Then type the command MHEARD as shown.
You should then see a display similar to the one below.

cmd:MHeard
```text
........  p1 N7GMF
........  p1 K6RFK
........  p1 SEA*
........  p1 N7HWD-8*
........  p1 KD7NM*
........  p1 N7ALW*
........  p1 WA7GCI*
```
cmd:

The callsigns in the list are the stations heard by your PK-900 with
the most recently heard station at the top of the list.  As in the
Monitored packets, the asterisks (*) indicate that the station was
heard directly by the PK-900.  The callsigns without an asterisk
were relayed by another station and so cannot be connected to
directly.  The "p1" means the station was heard on radio port 1.

#### 4.3.5 Your First Real Connect

Choose one of the stations with an asterisk displayed in YOUR MHEARD
list, or a friend that you know is "on the air" near to you.

If you are using an AEA PAKRATT program, follow the instructions to
CONNECT in Packet mode to the callsign you chose above.

If you are using a Computer Terminal or a non-AEA program, entering
the following after the "cmd:" command mode prompt will cause the
PK-900 to Connect to the station (Callsign) chosen above:

CONNECT (Callsign) <Enter>

<!-- PDF p.55 -->

After pressing <Enter>, you should observe the TX LCD light.
Your monitor should soon display:

*** CONNECTED to (Callsign)

If you see this, you have just Connected to your first packet station.
Identify what type of station you have connected to, and respond
appropriately.  After you have connected to a few stations, you should
skip to section 4.4 to learn more about the PK-900 packet features.

#### 4.3.6 I'm Having Trouble Connecting

If the station you are trying to connect to is connected to someone
else, you may see the following message:

*** BUSY from (Callsign) DISCONNECTED

If you see this, simply wait a few minutes and try again or try
connecting to a different station from your MHEARD list.

If the distant station cannot hear you, you may see the following:

*** Retry count exceeded
*** DISCONNECTED:

A number of different things can cause this to occur.  It may simply
be that the station you are trying to connect to is out of your
transmitter's range.  It is possible however that something more
serious is wrong, so you should check the following before proceeding:

- The Loopback Test in section 4.2 functions properly.
- Your PK-900's AFSK Output Level control, radio microphone gain,
and deviation are set properly as discussed in Section 3.5.1.
- All cables and connectors are properly installed.
- Your radio's volume and squelch are set for local conditions.
- You are following the correct procedure for Connecting.
Remember that this procedure is slightly different for AEA
PAKRATT programs than it is for terminals or terminal-programs.
- The "VHF" command is "ON" for VHF/UHF operation.
- HB is set to 1200
- RESET the PK-900 with the RESET command or RESET switch and
start over with section 4.2 of this chapter.

If none of the above correct the problem, ask one of your area's
experienced packet operators to listen to your transmissions.  Both
you and your partner should set MONITOR and MCON to 6, and then send
some packets.  Each station should display packets sent by the other.

- If only one station is "hearing" packets, check the modulator and
transmitter of that station and the demodulator and receiver of
the other station.
- Experiment with the TXDELAY parameter for the sending TNC.  Try
setting TXDELAY 64 for a long delay.  If this solves the problem,
decrease TXDELAY to the smallest value that works all the time.

If you still cannot connect to other stations, then you should contact
AEA Technical Support as outlined in the PREFACE of this manual.

<!-- PDF p.56 -->

### 4.4 More Packet Features

Now that you have worked a few packet stations, it is time to learn a
little more about the other packet capabilities of the PK-900.
Rather than explain all the features in detail, we will leave the
specifics to the command descriptions in the Command Summary Appendix.

#### 4.4.1 LCD Status and Mode Indicators

Your PK-900 front panel display is divided into four sections as
shown below:

PK-900's Front Panel LCD Indicators.

The left two thirds of the LCD display contain information
relating to the operation of radio port 1.

The upper right third of the LCD display contains information
about the operation of Radio port 2.

The middle of the right third of the LCD display contains
system information.

The very bottom of the LCD display is a 20 segment bargraph used
for tuning Radio port 1.

Your PK-900's front-panel LCD display shows the Status of each Radio
Port at a glance.  The following describes the function of each of
the LCD annunciators.

NAME            DESCRIPTION             LCD FUNCTION

RADIO 1        Column Heading           On when port 1 enabled
RADIO 2        Column Heading           On when port 2 enabled

DCD            Data Carrier Detect      Lit when data signals
are received

TX             Send                     Lit when PTT line is
active

MODE           Column Heading           Always on.  Indicates
operating mode of port 1

MORSE         Morse Code Mode         Lit when in Morse mode

BAUDOT         5 Level RTTY Mode       Lit when in Baudot mode

<!-- PDF p.57 -->

NAME            DESCRIPTION             LCD FUNCTION

ASCII         7 Level RTTY Mode       Lit when in ASCII mode

FAX            Facsimile Mode           Lit when in Facsimile

SSTV           Slow Scan TV Mode       Lit when in SSTV mode

SIAM           Signal Identification   Lit when in SIAM mode
Mode

TDM            Time Division Multiplex Lit when in TDM mode
Mode

EXP            Used for testing        Lit when test inpt high

PACKET         Packet Mode             Lit when in packet

DISCONNECTED   Packet status            Lit when not connected

DISCONNECT    Packet status            Lit when disconnecting

CONNECT        Packet status            Lit when connecting

CONNECTED      Packet status            Lit when connected

UNACKNOWLEDGED Packet status            Lit when you have sent a
packet that has not yet
been acknowledged

ACKNOWLEDGED   Packet status            Lit when all your
packets have been
acknowledged.

TOR            AMTOR Mode              Lit when TOR mode

FEC            AMTOR or PACTOR Mode    Lit when in FEC mode

ARQ            AMTOR or PACTOR Mode    Lit when in ARQ mode

ARQ L         ARQ Listen mode         Lit when in ARQ L mode

SEL-FEC        AMTOR Selective Calling ModeLit when is SEL-FEC

NAVTEX         NAVTEX Mode             Lit when in NAVTEX

PACTOR         PACTOR Mode             Lit when in PACTOR

IDLE           TOR Status              Lit when no data is
received from an active
channel

REQUEST        TOR Status              Lit when data receiving
station requests repeat.

<!-- PDF p.58 -->

NAME            DESCRIPTION             LCD FUNCTION

PHASE         TOR Status              Indicates synchroniza
tion of the ARQ calling
ARQ listen signal

OVER           TOR Status              Indicates changeover of
data transmission
direction

TRAFFIC        TOR Status              Indicates data reception
or transmission.

ERROR         TOR Status              Indicates reception of
erroneous data

STANDBY        TOR Status              Lit when not receiving
transmitting

COMPRESS       TOR Status              Lit when sending or
receiving compressed
data

SYSTEM         Column Heading           Always on.

HOST           System status            Indicates system is in
host mode.  Used for
PCPackratt program

MULT           System Status            Lit when multiple con
nections exist
Blinks when receive
buffer is full

TRANSPARENT   System Status            Indicates transparent
mode.

COMMAND        System Status            Indicates the PK-900 is
able to accept commands

CONVERSE       System Status            Lit when the PK-900 is
ready to send data

MAIL           Mailbox Status           Lit when mailbox
contains information

MARK           Bargraph                 Indicates mark end of
tuning indicator

SPACE         Bargraph                 Indicates space end of
tuning indicator

#### 4.4.2 Automatic Greetings

You can tell your PK-900 to send an automatic greeting (CTEXT) to
any station that connects to you.  This can be used to tell othersthat you are out of the shack and to leave you a message or for any
other message you would like to send.

To enable the CTEXT message, set your Connect Text message using the
CTEXT command.  Then set CMSG ON to enable the Connect Message
feature.  Think of the CTEXT message as the message your telephone
answering machine might give to a caller.

#### 4.4.3 Beacon Operation

Your PK-900 can send an automatic "beacon" message at a specified
time interval.  A beacon can send special announcements, or let others
know you are on the air.  To enable beacon operation do the following:

- Set your beacon message with the BTEXT command.
- Set the beacon interval using the BEACON EVERY or AFTER command.
- A beacon frame is sent to the path given in the UNPROTO command.

In the early days of packet, the beacon was useful to show your
presence on the packet channel.  With the growth of packet, many users
feel that beacons have outlived their usefulness and interfere with
traffic.  Use your beacon with consideration for others.

As a reminder, if you set the BEACON timing at a value considered too
small for busy channels (less than "90"), you'll see:

WARNING: BEACON too often

#### 4.4.4 Digipeater Details

You may wish to connect to a packet station that is beyond your direct
radio range.  If a third packet station is on the air and both you and
the station you want to talk to are in range of that third station,
the third station can relay or "digipeat" your packets.  You simply
set the "digipeater" routing when you connect.  Here's a sketch that
shows how digipeating can solve problems:

WX2BBB
/    \
WX1AAA _______/      \_________ WX3CCC

You are station WX1AAA - you want to have a packet QSO with WX3CCC.
There is a mountain between you and WX3CCC; you're out of simplex
range of each other.  However, you know that there's a packet station
located on the ridge - WX2BBB - which is in range of you and WX3CCC.

Instruct your PK-900 to set up a connection to WX3CCC using WX2BBB
as an intermediate digipeater.  When you initiate the Connect, type:
"CONNECT WX3CCC VIA WX2BBB".

If WX2BBB has turned off his station, you can still contact WX3CCC by
going around the ridge through WX2DDD and WX2EEE as shown:

<!-- PDF p.60 -->

xxxx
/      \
WX1AAA _________/        \__________ WX3CCC. . .
WX2DDD .  .  .  .  .  .  .  .  WX2EEE

This time, type the connect command like this:

CONNECT WX3CCC VIA WX2DDD,WX2EEE

Type the digipeaters' call signs in the exact order of the intended
path from your station to the station with which you wish to connect.
You can specify a routing list of up to eight intermediate stations.

In practice this does not work very well, and Networking Switches
such as NET/ROM have replaced digipeating for the most part.  Still,
it is sometimes necessary to digipeat through one or two stations.

##### 4.4.4.1 Are You a Digipeater?

Your packet station can be a digipeater for other stations.  You don't
have to "do" anything - your PK-900 will digipeat other stations unless you tell it not to!  with the DFROM command.

If your transmitter is keyed when you're not using it, or during lulls
in your own conversations, you're being used as a digipeater by some
other stations.  This won't bother your chat with your partner.

If you wish to monitor the other stations that are using you as a
digipeater then set the command MDIGI ON.

#### 4.4.5 Monitoring Other Stations

Use the MONITOR command to determine what kinds of packets you will
see when you are NOT connected to another station.  "MONITOR" takes a
numerical value between "0" and "6."  Each higher number adds more
detail to your monitoring.  The meanings of the MONITOR numbers are:

0    Monitoring is disabled.

1    Only unnumbered, "unconnected" frames are displayed.  This
setting will display Beacons, but not display connected stations.

2    Numbered (I) frames are also displayed.  Use this setting to
monitor connected conversations in progress on the channel.

3    Connect request ("C") frames and disconnect ("D") request packets
in addition to the above are displayed.

4    This is your PK-900's default value.  Unnumbered acknowledgment
(UA) of connect and disconnect frames are also displayed.

5    Receiver Ready (RR), Receiver Not Ready (RN), Reject (RJ), and
Frame Reject (FR) supervisory frames are also displayed.

6    Poll/Final bit and sequence numbers of monitored frames are shown.

<!-- PDF p.61 -->

Understanding all types of packet frames is not necessary to operate
packet.  Packet operators should however understand that there are
many types of control frames that do not contain printable data.

Your PK-900 can display these frames, but most users only want to
see frames with information.  For this reason, the MONITOR command
default (4) does not display all the packets that the PK-900 hears.

NOTE:     If you will be leaving your PK-900 on to accept connects
from others while your computer is off, set MONITOR to 0
(zero) and type a <CTRL-S> to hold the data.
If you are using an AEA program, you must set the MONITOR
command to 0, but our programs automatically perform the
<CTRL-S> function for you.  If this is not done, the PK-900
memory will be filled with useless data and prevent others
from connecting with you.

##### 4.4.5.1 Monitoring the Packet Networking Switches

There are other types of AX.25 frames used by networking switches that
the PK-900 does not normally display.  These other frames can be
seen by turning the MPROTO command ON.  Some of the packets monitored
with MPROTO ON will contain information that may interfere with the
screen on your terminal or computer causing it to look "funny".  For
this reason the MPROTO command default is OFF.

If you are hearing packets that sound strong but are not displayed,
setting MONITOR to 6 and MPROTO ON should show them.  If you are
curious about the packets that do not print, you may find the command
WHYNOT useful.  When WHYNOT is turned ON, the PK-900 will give a
reason why each packet was not displayed.  If you are interested in
exactly how the packets are represented, turn on the TRACE command.
See the Command Summary for more information about WHYNOT and TRACE.

##### 4.4.5.2 Monitoring Other Stations While Connected

When you are NOT connected to another station, the MONITOR command
discussed above determines what packets are displayed.  When you ARE
connected, the MCON command determines what packets are shown.

The default of MCON is 0 which tells the PK-900 NOT to monitor any
packets while you are connected.  Most users like this so they are not
disturbed with monitored channel data when they are communicating with
another station.  If it is desired to monitor channel activity while
you are connected, then remember to set MCON to an appropriate Monitor
number from the list in 4.4.5 or the in command summary.

##### 4.4.5.3 Selective Monitoring

After you have monitored channel activity for a while, you may decide
there are only a few stations you wish to display.  The PK-900 will
let you do this with the Monitor-TO (MTO) and Monitor-FROM (MFROM)
commands.  With the MBELL command, you can even be alerted when a
certain station transmits on the frequency.  These commands work in
conjunction with MONITOR and MCON commands.

<!-- PDF p.62 -->

##### 4.4.5.4 The MFILTER Command

Some terminals and computer programs are sensitive to certain
characters that may appear in monitored packets.  You will know this
is happening if occasionally the cursor on your screen moves to
strange places causing the copy to be garbled.

The PK-900 default for MFILTER is $80 which prevents most control
characters from interfering with your display.  If you find a terminal
or printer is bothered by certain characters, see the Command Summary
for more information on the MFILTER command.

##### 4.4.5.5 Monitor Without Callsign Headers

Sometimes you may wish to monitor certain stations without wanting to
look at the packet callsign headers.  This can be useful when
monitoring message traffic from a large Packet Bulletin Board System
(PBBS).  The MBX command allows you to choose the callsign of a
station, or a pair of stations you wish to monitor without seeing the
packet headers.  See the Command Summary for details.

##### 4.4.5.6 MSTAMP, The Monitor Time-Stamp Command

Monitored packets can be time-stamped if the real-time clock has been
set with the DAYTIME command.  To timestamp monitored packets, turn
the MSTAMP command ON.  Turning the DAYSTAMP command ON adds the date
to the timestamp provided by the MSTAMP command.

#### 4.4.6 Packet Connects

When you turn your PK-900 on and enter your callsign, anyone can
Connect to you.  If you are at your terminal or computer when this
occurs you will see the LCD for the appropriate channel showing
the word  CONNECTED.

When a packet connection occurs, the PK-900 automatically switches
to the Converse mode so what you type on the keyboard will be sent to
the connected station.  The NEWMODE and NOMODE commands control when
and how the PK-900 changes to and from Command mode in response to
packet connects and disconnects.  You will probably never need to
change these settings.

##### 4.4.6.1 Time-Stamping Connects

Sometimes it is useful to know what time someone connected to you perhaps for logging.  To time-stamp your connects and disconnects turn
the command CONSTAMP ON.  As discussed in the Monitoring section
above, turning the command DAYSTAMP ON adds the date to this as well.
The DAYTIME command must first be set for this to operate.

##### 4.4.6.2 Connect Alarm

If you busy doing other things, you may want to be alerted when
someone connects to you.  Turning the command CBELL ON rings the bell
on your terminal when a station connects to, or disconnects from you.

<!-- PDF p.63 -->

#### 4.4.7 Packet Formatting and Editing

Some of your PK-900's command parameters affect how your packets are
formatted - how your typing appears to the rest of the world.  Other
commands let you correct typing errors before your packet is sent,
cancel lines or cancel packets if necessary.

##### 4.4.7.1 Carriage Returns and Linefeeds in Packets

Most people use packet radio for sending and receiving messages or
conversing with other Amateurs.  The character used to send a packet
is defined with the command SENDPAC which defaults to a Carriage
Return ($0D).  The SENDPAC character may be changed, but most will
find the Carriage Return or Enter key to be a natural choice.
Similarly, your PK-900 will include a Carriage Return in the packet
you send to other stations since this makes for a more natural
conversation.  The ACRPACK command (default ON) controls this feature,
but most people never want to change this.

The PK-900 also has the capability of adding a linefeed character
($0A) automatically to packets that you send to others.  If you
encounter a station that says your packets are overprinting, you may
want to turn the ALFPACK or the ILFPACK command ON temporarily.

##### 4.4.7.2 Canceling Lines and Packets

Most of the time, the Backspace key (or the Delete key on some
computers) is all that is needed to edit a line before it is sent.
Occasionally it may be helpful to cancel the line, or the entire
packet you are entering with one key stroke.  The CANLINE character
(default <CTRL-X>) will cancel the entire line you are typing.  The
CANPAC character (default <CTRL-Y>) will delete the entire packet you
are entering.  These commands can be helpful, but use them with care.

##### 4.4.7.3 Redisplay

If you have erased and retyped many characters, you may want to see
the sentence you are currently entering "redisplayed" by the PK-900,
especially if BKONDEL is OFF.  Your PK-900 will show the line you're
entering when you type the REDISPLAY character (default <CTRL-R>).
This will also allow you to display any packets you might have
received while you were typing.

##### 4.4.7.4 The PASS Character

If you are using a terminal or terminal program, the following may be
useful.  Sometimes you may want to include a special input character
such as a Carriage Return (the SENDPAC character) in a packet.  For
example, to send several lines in the same packet, you must include
<CR> at the end of each line.  You can include any character in a
packet (including all special characters) by prefixing that character
with the PASS character (default <CTRL-V>):

I wasn't at the meeting.<CTRL-V><CR>
What happened?

Without the PASS character, this message would go out as two packets.

<!-- PDF p.64 -->

By prefixing the first <CR> with <CTRL-V>, you send it all at once,
while maintaining the <CR> as part of the text.  The PASS character
can be useful in formatting text Messages such as CTEXT as well.

#### 4.4.8 Packet Transmit Timing

Your PK-900 has a number of built-in timers used to control the
packet protocol and transmit timing.  The default values have been set
at the factory to provide reasonable performance, but the values may
not be optimum for your local area.  Most protocol parameters should
be adjusted only after carefully reading about them later in the
chapter.  You SHOULD however adjust TXDELAY for your transmitter as
indicated below.

##### 4.4.8.1 TXDELAY and AUDELAY

Radios vary in the time it takes to switch from receive to transmit.
If your PK-900 starts sending data before your transmitter is up to
power, the packet will not be received properly at the distant end.

TXDELAY controls the delay between your transmitter's key-up and the
moment when your PK-900 starts sending data.  The default value of
30 corresponds to a time of 300 ms and works with most VHF/UHF FM
transceivers.  With modern transceivers TXDELAY can often be reduced
which will improve packet performance in your area.  You should
perform the following procedure to optimize TXDELAY for your radio.

- Find another station who can reliably digipeat your signals.

- Set your UNPROTO path to TEST via the callsign of the station who
can digipeat your signals.  Example:  UNPROTO TEST VIA WX2ABC

- Set the MONITOR command to at least 1.

- Go to CONVERSE mode and send a few packets by pressing the
<Enter> key.  Note that you should see them on your own screen
when they are digipeated by the other station.

- Start reducing TXDELAY by units of 5 each time making sure the
other station is still digipeating ALL your UNPROTO packets.
Eventually you will find a value where the other station can no
longer copy your packets to digipeat them.

- When this happens, increase TXDELAY in units of one or two until
the other station again digipeats ALL of your packets.  This will
be the optimum setting of TXDELAY.

After TXDELAY is adjusted as indicated above you may want to adjust
the audio delay (AUDELAY) as indicated in the Command Summary.

The next sections of this chapter will discuss some of the more
advanced packet features including Multiple Connects, Packet Timing
and Protocol, and HF Packet Operation.

##### 4.4.8.2 AXDELAY and AXHANG

Although it is not common, packet can be used through voice repeaters.

<!-- PDF p.65 -->

When sending packets through an audio repeater you may require a
longer key-up delay than is normally needed for direct communications.
The AXDELAY command adds more key-up delay in your PK-900 so that
the repeater can stabilize.  The AXHANG command sets the time your
PK-900 assumes is needed for the repeater to drop.

### 4.5 Packet Protocol Basics

Here we will talk a little about the AX.25 packet protocol.  You do
not need to know the protocol to use packet, but it helps in
understanding the protocol parameters.

There are two modes of packet transmissions, Connected mode and
Unconnected mode.  Usually you will converse with another packet
station in Connected mode.  Still, the Unconnected or Unprotocol mode
comes in handy for beacon transmissions and roundtable conversations.

All packets have basically the same construction.  Packets contain
source and destination callsigns (and any digipeaters if used), as
well as information identifying the type of packet.  This packet
identification can be seen with the MONITOR command discussed earlier.
All packets contain an error check code called the CRC.  This
virtually ensures that when a packet is received, it will not contain a single error.  The command PASSALL can disable the CRC error
check, but this should only be done for experimental purposes.

#### 4.5.1 Unconnected Packets

In order to allow amateurs to send message beacons and to call CQ, the
AX.25 protocol has the ability to send packets that are intended for
more than one specific packet station to see.  Since all packets must
have a destination "callsign", the PK-900 sends Unprotocol packets
TO the callsign of CQ.  This can be changed with the UNPROTO command,
but most people like this since it makes calling CQ easy.

#### 4.5.2 Connected Packets

When you Connect to another station, the AX.25 packet protocol ensures
that the station to whom you are connected receives all the packets
that you send.  Similarly, the protocol ensures you will receive all
the packets that the other station sends to you.  The following
describes briefly how the protocol does this.

#### 4.5.3 FRACK and RETRY

When the PK-900 sends a packet to a Connected station, it expects an
acknowledgment (ACK) packet from the other station to confirm that the
packet was received.  The AX.25 packet protocol will automatically
retransmit (Retry) packets when an acknowledgment is not received from
the distant end of the link within a specified time.

The FRACK command (FRame ACKnowledge time) sets the time lapse allowed
before the originating station retransmits (retries) the packet.

The RETRY command sets the maximum number of retransmissions before
the sending station terminates the connection (DISCONNECTS).

<!-- PDF p.66 -->

The TRIES counter keeps track of the retries that have occurred on the
current packet.

#### 4.5.4 PACLEN and MAXFRAME

Packets will be sent either when the <Enter> key is pressed or when
the maximum packet size is exceeded.  The maximum packet size is set
by the PACLEN command which defaults to 128 characters.  When large
amounts of data need to be sent, this value can be increased to 256.
When conditions are poor or the channel is crowded as on HF packet,
this value should be reduced to 64 or less.

The packet protocol allows more than one frame to be sent in a single
transmission.  The default is set to 4 by the MAXFRAME command.  When
conditions are good up to 7 frames can be sent to speed data transfer.
When conditions are poor or the channel is crowded, MAXFRAME should be
reduced to only 1 frame.

#### 4.5.5 Reducing Errors through Collision Avoidance

If every packet station could hear every other station, there would be
very few "collisions" due to stations transmitting at the same time.
Since packet operates over radio, there are often many stations on the
same frequency that cannot hear each other.  Digipeaters and network
nodes allow these stations to communicate with each other, but this
increases the chances of collisions.

The first attempt to avoid collisions was through the use of the DWAIT
and RESPTIME timers.  DWAIT forced the TNC to delay the transmission
of any packet except for digipeated frames by the time selected.  This
fixed timer helped, but packet was still plagued by collisions.  The
RESPTIME was added to help with large file transfers.  Still, more
needed to be done to reduce collisions.

Another attempt to reduce collisions was the introduction of AX.25
version 2 protocol.  On VHF packet, most everyone uses version 2 which
is controlled by the AX25L2V2 command (default ON).  On VHF this
helps, but some users on HF packet are turning this command OFF.

An exponentially distributed random wait method was proposed by Phil
Karn (KA9Q) called P-persistent CSMA.  When the command PPERSIST is ON
(default) the PK-900 uses the number set in PERSIST and the time
value set by the SLOTTIME command to more randomly distribute the
transmit wait time.  This is more efficient than using the DWAIT time.

As a further attempt to improve packet performance, Eric Gustafson
(N7CL) proposed giving priority to acknowledgment packets (ACKs).
This protocol is controlled by the ACKPRIOR command which currently
defaults OFF.  Check with experienced packet users in your area and
find out if they are using priority acknowledge or have changed any
other packet parameters.

#### 4.5.6 CHECK and RELINK

If someone connects to you and then turns his TNC off, you would
probably not want to stay connected to the station forever.  The CHECK
timer determines the amount of time the PK-900 will wait beforetesting the link if no data has been sent or received.

The RELINK command sets what happens when the CHECK timer expires.  If
RELINK is OFF, the PK-900 changes to the Disconnected state.  If ON,
the PK-900 attempts to reconnect to the other station.

### 4.6 Multiple Connection Operation

Since packet radio allows many stations to share the same channel,
many QSOs can be going on at the same time.  Because packet has this
channel sharing capability, there is no reason you cannot connect to
more than one station at the same time.  Being connected to multiple
stations at once is a powerful feature of your PK-900.

#### 4.6.1 Multiple Connection Description

The PK-900 offers 10 logical packet channels on Radio Port 1 and 26
logical packet channels when using Radio Port 2.  Each logical channel
can support a connection with another packet station.  So with the
PK-900, you may be connected to a total of 36 other packet stations
simultaneously.

Multiple connect operation is much like a multi-line telephone with
automatic hold.  When you are connected to multiple stations you
will automatically receive everything sent TO you.  You must select
the proper channel (in effect push the proper line button on the
telephone) to send data to a particular station.

If you are using an AEA PAKRATT program, this is described in the
program manual.  If you are using a terminal, the rest of this section
will describe how to set up the PK-900 for multiple connections.

#### 4.6.2 The Channel Switching Character

The logical channels are selected with the CHSWITCH character.  You
must choose a CHSWITCH character that you do not normally type such as
the vertical bar "|" (ASCII $7C), or the tilde "~" (ASCII $7E).  Once
this has been selected and entered into the PK-900, you may initiate
multiple connections with others on your radio channel.

The ten logical channels on Radio Port 1 are numbered 0-9.  The 26
logical channels on Radio Port 2 are labeled A-Z.  After the CHSWITCH
character has been selected, you can now initiate connects on any of
the logical channels numbered zero through nine (0-9) on Port 1 or "A"
through "Z" on Port 2.

To change logical channels on Radio Port 1, press the CHSWITCH
character you just defined, and then a number from 0-9.  Remember that
the text that you type will only be sent out to the station connected
to the logical channel your PK-900 is currently on.

This same procedure is used to select a logical channel on Radio
Port 2, only instead of pressing a channel "number" from 0-9, a
"letter" from A-Z is pressed.  This is in fact the method used to
switch back and forth between Radio Ports 1 and 2.  Please see thesection on switching between Radio Ports later in this chapter for a
more complete description of this process.

#### 4.6.3 Will You Accept Multiple Connects

Setting the CHSWITCH character only allows you to make outgoing
multiple connects.  For the PK-900 to allow multiple incoming
connections, you must set the USERS parameter to more than one (1) for
each Port that you wish to allow incoming multiple connections.
The number you enter in the USERS command tells the PK-900 how many
other users you will allow to connect to you at one time.  If more
than this number try to connect, they will get the "busy" response.

#### 4.6.4 Display Multiple Connected Callsigns

Multiple Connection operation can be confusing - especially
remembering who is connected on what channel.  To help this, you may
want to turn ON the CHCALL command to display the callsign of the
station who is connected to you on a given channel.

#### 4.6.5 Doubling Received CHSWITCH Characters

If you want to be able to tell the difference between the CHSWITCH
characters you type, and characters from other stations that happen to
be the same as your CHSWITCH character, then set CHDOUBLE ON.

#### 4.6.6 Checking Your Connect Status with the CSTATUS Command

To check what channels your PK-900 is currently set to, as well as
who is connected to you, you may find the CSTATUS command helpful.
CSTATUS is an immediate command that shows you the status of all
packet channels as well as the channel you are currently on.

#### 4.6.7 The MULT LCD

You will know you are connected to more than one packet station on
each Radio Port when the MULT LCD the PK-900 lights.

NOTE:   The MULT LCD will blink if the PK-900's receive buffer is
filled.  This can happen if your computer is not connected to
the PK-900 and the MONITOR command was left ON, or if for
some reason, your communications program no longer can accept
any further inbound data.

### 4.7 HF Packet Operation

HF Packet is much trickier than operating on VHF.  In this section we
will assume you have as completed section 4.2 of this chapter and at
least read section 4.3 and the MONITORING sections of 4.4.  If at all
possible, get some experience with VHF packet before trying packet on
HF.  Although this is not absolutely required, the experience will
help you make HF packet contacts.

<!-- PDF p.69 -->

#### 4.7.1 Where to Operate HF Packet

Before you can operate HF Packet, you must first find the activity.
Most HF packet operation is on the 20-meter amateur band starting at
14.103 MHz and every 2 kHz above that up to 14.111 MHz.  Note that
14.103 MHz is the HF Packet calling frequency and a good place to
start.  The higher frequencies such as 14.109 and 14.111 are used
mostly by HF PBBS systems and are not good places to look for a QSO.

#### 4.7.2 PK-900 HF Packet Settings

Radio Port 1 on the PK-900 is intended for multi-mode operation so
either a VHF or HF transceiver may be connected.  The packet
parameter defaults have been set for VHF Packet operation on both
radio ports and should be changed for HF packet operation as shown
in the table below.

The table below shows the parameters that should be set differently
for HF and VHF packet operation.  If you will be operating HF Packet,
you should make note of these parameters and change them accordingly.

Recommended                 Port 1 & 2 Defaults
300 baud HF Packet            1200 baud VHF Packet
SLOTTIME 12                   SLOTTIME 30
PACLEN   64 or less           PACLEN   128
MAXFRAME 1                    MAXFRAME 4
FRACK    8                    FRACK    5
VHF      OFF                  VHF      ON
HBAUD    300                  HBAUD    1200
MODEM     10                  MODEM     11/4

Note that the HF (VHF OFF) modem number is tied to the QHPACKET
command and the VHF (VHF ON) modem number is tied to QVPACKET.
The Radio Port baud rate is not changed when you change modems.  You
select the appropriate baud rate with the HBaud command.

You should set MONITOR to 6 on the port used for HF Packet when tuning
in your first HF Packet stations.

#### 4.7.3 HF Receiver Settings

Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your PK-900 through the direct FSK keying lines, in
which case you should select the FSK or RTTY operating mode.  Adjust
the volume to a comfortable listening level.

#### 4.7.4 Tuning in HF Packet Stations

Perhaps the most difficult thing about HF Packet operation is making
sure the station is tuned properly and stays tuned.  Since HF packet
uses 200 Hz Frequency Shift Keying to send data (2110/2310 Hz),
tuning accuracy is very important.  Being off frequency by only 20
Hz can make a noticeable difference in the PK-900's ability to copy
packet stations.  Your have a choice of three types of tuning indicator types.  Use the BARgraph command to change the indicator type.

<!-- PDF p.70 -->

Follow the tuning procedure below carefully for the best results in
tuning in HF packet stations.

- Make certain your HF receiver is either in LSB or FSK depending
on your PK-900 set-up.

- Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.

- Tune your receiver to 14.103 MHz (or another frequency where you
know there is HF packet activity) and listen to the packets.

- Slowly vary the tuning knob on your receiver and look for a
display on the PK-900 tuning indicator like the one below.

--------------------------------------------------------------MARK |    ] ] ]              ] ] ]          | SPACE
--------------------------------------------------------------Tuning correct for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]  ]   ]  ]   ]   ]  ]           ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning correct for magic eye indicator (BAR 3)

--------------------------------------------------------------
|      ] ]       |
--------------------------------------------------------------Tuning correct for center tune indicator (BAR 2)

If the tuning indicator looks like the one below, the audio
frequency from your speaker is too low for the PK-900 to copy
packets.  Slowly tune the VFO and make the frequency higher.

--------------------------------------------------------------MARK | ]   ]   ]   ]   ]   ]   ]              | SPACE
--------------------------------------------------------------Tuning too low for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]   ]   ]   ]   ]   ]   ]     ]        |
--------------------------------------------------------------Tuning too low for magic eye indicator (BAR 3)

--------------------------------------------------------------
|       ]   ]         |
--------------------------------------------------------------Tuning too low for center tune indicator (BAR 2)

<!-- PDF p.71 -->

If the tuning indicator looks like the one below, the audio
frequency from your speaker is too high for the PK-900 to copy
packets.  Slowly tune the VFO and make the frequency lower.

--------------------------------------------------------------MARK |          ]   ]   ]  ]  ]  ]   ]  ] | SPACE
--------------------------------------------------------------Tuning too high for discriminator indicator (BAR 0)

--------------------------------------------------------------
|          ]    ]   ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning too high for magic eye indicator (BAR 3)

--------------------------------------------------------------
|          ]   ]      |
--------------------------------------------------------------Tuning too high for center tune indicator (BAR 2)

- Adjust the volume on the receiver so that the DCD LCD lights
when a properly tuned packet is being received.
You must also make certain that the DCD LCD goes out when no
packet signals are present on the frequency.

After you have a packet station tuned in, you should start seeing
HF packet stations on your display.

#### 4.7.5 Transmitter Adjustments

Make sure your PK-900 is adjusted for your SSB transmitter as
described in section 3.5 and 3.5.2 of this manual before transmitting.
These are very critical adjustments.  If the AFSK level and
transmitter microphone gain are not adjusted properly, other stations
will not be able to copy your packets.  Check your plate or collector
current or the power output of your rig before going on the air.

#### 4.7.6 Going On The Air

Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using.

On HF there are two ways you can go about talking to another station.

- First, you can look at the packets you have just MONITORED
(or in your MHEARD list) and choose one of them to connect to.

- You can also "Call CQ" by entering the CONVERSE mode and pressing
the <Enter> key a few times.

Either way you decide to go on the air, remember that things happen
much more slowly on HF packet than they do on VHF packet.  HF packet
requires patience and careful tuning in order to be used successfully.

<!-- PDF p.72 -->

If you are having problems connecting to other HF packet stations, try
working with an experienced HF packet operator in your area and listen
to each other's signals.  See if you can copy each other's packet
signals.  If he cannot copy your signals, have him listen to your
signal in the CALIBRATE mode to make sure you are transmitting a pure
tone.  As mentioned earlier, any distortion caused by overmodulation
or RF feedback will make your signal difficult or impossible to copy.

### 4.8 Controlling the Radio Ports

If you are using an AEA PAKRATT program designed for the PK-900,
switching between Radio Ports is described in the program manual.
If you are using a computer terminal, terminal program or the
"Dumb Terminal Mode" of an older PAKRATT program, this section will
describe how to control and switch between the radio ports.  The
RADIO command allows either (or both) Radio Port(s) to be disabled.
Before the second radio port can be used for packet operation, the
proper modem must be selected using the 2MODem command.

Radio Port 2 Modems

1: Internal 200: 1070/1270    2: Internal 200: 2025/2225
3: Internal 1000: 1200/2200   4: Internal 1000: 1200/2200 eq.
5: Internal 200: 1180/980     6: Internal 200: 1850/1650
7: Internal 800: 2100/1300    8: Internal 800: 2100/1300 eq.
9: Internal option           10: Modem disconnect header

#### 4.8.1 Selecting Modems

The various modems available in the PK-900 can be seen with the
DIRECT(ory) command.  To display all the available modems simply
enter the Command Mode of the PK-900 and then type DIR as shown.

DIR <Enter>

The PK-900 will respond with the following:

- Port 1 -                              - Port 2 -
1: FSK 45 bps 170: 2125/2295            1: Internal 200: 1070/1270
2: FSK 100 bps 170: 2125/2295           2: Internal 200: 2025/2225
3: FSK 45 bps 200: 2110/2310            3: Internal 1000: 1200/2200
4: FSK 100 bps 200: 2110/2310           4: Internal 1000: 1200/2200 eq.
5: FSK 100 bps 425: 2125/2550           5: Internal 200: 1180/980
6: FSK 100 bps 850: 2125/2975           6: Internal 200: 1850/1650
7: FSK 100 bps 850: 1275/2125           7: Internal 800: 2100/1300
8: Analog 900/2500                      8: Internal 800: 2100/1300 eq.
9: FSK 2400 bps 800: 1300/2100          9: Internal option
10: FSK 300 bps 200: 2110/2310          10: Modem disconnect header
11: FSK 1200 bps 1000: 1200/2200
12: Morse 750

<!-- PDF p.73 -->

Any modem from this list may be selected with the command MODEM.   For
example, let's say that you want to operate 300 bps HF packet on radio
port 1 and 1200 bps VHF packet on radio port 2.
This operation is achieved with radio port 1 modem 10 and radio
port 2 modem 4.  To load these modems, first enter the Command
Mode of the PK-900 and then type MODEM 10/4 as shown below:

MODEM 10/4 <Enter>

The PK-900 will respond with the following:

MODem was  x/x  (whatever modems were in use)
MODem now  10/4
cmd:*** HBaud now 300/1200

Either modem may be selected individually.  To change only radio
port one's modem, for example, type MODEM 10 <Enter>.  To change
only radio port two's modem, you would type MODEM /4 <Enter>.

#### 4.8.2 Displaying Received Data

When radio port 2 is active in the PK-900, received data is displayed
from both Radio Ports at the same time.  This allows you to operate
on HF and not miss any local Packet connects or information from VHF
DX spotting nets.

The PK-900 displays monitored packets by prefacing each packet with
a port designator "p1" for radio port 1 and "p2" for radio port 2.
For instance, let's say that  modems 10/4 are selected and you
are monitoring HF packet activity on Radio Port 1, and VHF packet
activity on Radio Port 2.  You might see something like the following:

p1 WX1AAA*>CQ [UI]:                     { WX1AAA calling CQ on
Radio Port 1 (HF) }
p1 WX1AAA*>CQ [UI]:

p2 WY7DDD*>WY7EEE [I,P;1,1]:            { An Information Frame from
Hello Bob, how are you?                   WY7DDD to WY7EEE monitored
on Radio Port 2 (VHF) }

p2 WY7EEE*>WY7DDD (RR,F;1)              { Acknowledgment of the above
I-Frame on Radio Port 2 }

p1 WX2BBB*>WX1AAA [C,P]                 { WX2BBB attempting to connect
to WX1AAA on Radio Port 1 }

p1 WX1AAA*>WX2BBB (UA,F)                { WX1AAA acknowledging the
connect request }

p1 WX2BBB*>WX1AAA [I,P;0,0]:            { An Information Frame from
Hello Bob, how are you?                   WX2BBB to WX1AAA monitored
on Radio Port 1 (HF) }

The first packet shown above is an Unprotocol "CQ" packet from WX1AAA
that was received on Radio Port 1.  We know this was received on Port
1 because of the "p1" shown in front of the packet.  (Remember fromthe section on Multi-connect packet operation that Packets received on
Radio port 1 will be on one of the ten logical channels numbered 0-9.)
The second packet is another "CQ" from WX1AAA.  Again, there is a "p1"
in front of it so we know it was also received on Radio Port 1.

The third packet is an Information-Frame, or simply an "I-Frame" that
was monitored on Radio Port 2.  We know this was received on Radio
Port 2 because of the "p2" shown before the packet.
Remember that Packets received on Radio port 2 will be on one of the
26 logical channels designated A-Z.

The fourth packet is an acknowledgment to the previous I-Frame
received on Radio Port 2.  Again, the "p2" in front of the packet
tells us it was received on Radio Port 2.

The fifth packet is a Connect Request from WX2BBB to WX1AAA that was
monitored on Radio Port 1.  The "p1" in front tells us so.  The sixth
packet is an acknowledgment by WX1AAA to the connect request.
The seventh and last packet shown above, is the first I-Frame that
WX2BBB is sending to WX1AAA after they have connected.

#### 4.8.3 Controlling Your Transmitted Text

If you are using an AEA PAKRATT program designed for the PK-900,
switching between Radio Ports is described in the program manual.
If you are using a computer terminal, terminal program or the
"Dumb Terminal Mode" of an older PAKRATT program, this section will
describe how to control and switch between the radio ports.

##### 4.8.3.1 Defining the Channel Switching Character CHSWITCH

Before you can switch between logical channels or Radio Ports you must
define a Channel Switching character that you will use to signal the
PK-900 that you want to redirect your transmitted text.  This
special character is defined with the CHSWITCH command and should be
one that you do not normally type in conversational text such as the
vertical bar "|" ($7C), or the tilde "~" ($7E).  To enter the vertical
bar "|" as the CHSWITCH character, first enter the Command Mode of the
PK-900 and then type CHSWITCH $7C as shown below:

CHSWITCH $7C <Enter>

The PK-900 will respond with the following:

CHSWitch was  $00
CHSWitch now  $7C (|)

Once a CHSWITCH character has been entered, you may switch Radio
Ports or Packet logical channels of the PK-900 at will.

<!-- PDF p.75 -->

##### 4.8.3.2 Switching Between Radio Ports

The ten logical channels on Radio Port 1 are labeled 0-9.  The 26
logical channels on Radio Port 2 are labeled A-Z.  To select Radio
Port 1, press the CHSWITCH character you just defined, followed by a
number from 0-9.  To select Radio Port 2, press the CHSWITCH
character, followed by a letter from A-Z.

After you change Radio Ports or logical channels, the text you type in
the CONVERSE mode will be sent to the port and channel selected.  If
you are connected to another packet station, the text you type will be
sent to him or her.  If the channel is not connected, the text will be
sent in the Unprotocol or unconnected mode.

For example, let's say that you are operating HF Packet on Radio Port
1 and are also available for connects on the local VHF Packet channel
on port 2.  You connect to an HF packet station and are in the middle

of a QSO when a station on VHF connects to you.  The following shows
how your screen would look and suggests how you might handle such an
occurrence.  The underlined text is the text that you have typed.

p1 WX1AAA*>CQ [UI]:                     { WX1AAA calling CQ on
Radio Port 1 (HF) }
p1 WX1AAA*>CQ [UI]:

cmd:C WX1AAA                            { You attempt to connect to
station WX1AAA on HF }

*** CONNECTED to WX1AAA                 { You're connected to WX1AAA }

Hello, name here is Bob.                { You send him your name }

Hi Bob, name here is Jim and the        { The other station responds
QTH is Boston Mass.                       with his name and location }

A:*** CONNECTED to WX7EEE               { WX7EEE connects to you on
VHF (Radio Port 2) }

Nice to meet you Jim, QTH here is      { You send another packet
Seattle, WA. You have a nice signal.     to Jim on HF }

Hey Bob, I'm going to the hamfest       { Your friend on VHF packet
this weekend if you want a ride.          wants to go to the hamfest }

|AHello Mike, I am on HF right          { You switch ports by typing
now talking to a station in Boston.       |A and respond on VHF }

0:Thanks Bob, your signal is S-7        { Jim on HF gives you a signal
here.  I am running 100 Watts into a      report.  The "0:" shows you
tri-bander at 50 ft.                      this was from Port 1 (HF) }

|0Thanks for the report Jim, you're     { You switch back to HF with
S-8 and the antenna here is a quad.       the |0 and respond to Jim }

<!-- PDF p.76 -->

|AYes, I was planning to go Saturday.   { You switch to Port 2 with
What time do you want to leave?           |A to say you want to go }

A:I'll pick you up at 8:00 Bob.         { Your friend on VHF responds
I'm looking forward to it.                to you }

0:Well, it's getting late here and      { Jim on HF tells you he has
time to pull the plug Bob.                shut down for the night }

Sounds good, I'll see you on            { You last sent data on Port 2
Saturday morning.                         and do not need to send |A }

|0Take care Jim, and 73.                { You say 73 to Jim on HF }

A:73 Bob.                               { Your friend on VHF says 73 }

|A<Ctrl-C>A:cmd:D <Enter>               { You switch to Port 2 and
Disconnect from WX7EEE }

*** DISCONNECTED: WX7EEE                { You're now disconnected from
WX7EEE on VHF.  The A:
doesn't show because you last
received data on port 2. }

0:*** DISCONNECTED: WX1AAA              { WX1AAA disconnects from you
on HF (Port 0 }

As you may have noticed, the above technique of communicating on more
than one Port at the same time is almost identical to that used for
single-port multiconnect packet operation.  Let's discuss the sample
QSOs above to see how the Port switching occurs.

The first packet frame, we see is a CQ from WX1AAA.  This frame is
preceded by a "p1" which shows it was received on Radio Port 1.

You have seen WX1AAA calling CQ and decide to connect to him on HF.
You simply go to the Command mode (by typing <CTRL-C>) and issue the
CONNECT command C WX1AAA as shown on the third line.  The fourth line
shows that you are now Connected to WX1AAA.  Note that you did not
have to type "|0" before sending the connect request since the
PK-900 defaults to Port 1 until it has been changed to Port 2. If you
want to be sure of which channel you want to be on, you could type |0.

Now that you are connected to WX1AAA you greet him and send your name.
WX1AAA then gives you his name (Jim) and location which is the way
most QSOs begin.  Remember in the above example, the underlined text
is the text you would have typed.

You have just begun your QSO with WX1AAA when all of a sudden your
friend WX7EEE connects to you.  WX7EEE has connected on Radio Port 2
(VHF) which is shown by the "A:" before the connect message.  Remember
that the 26 channel designators for Radio Port 2 are A-Z.

Before you respond to WX7EEE on VHF, you send a packet to Jim on HF
giving him your location.  After sending this packet, you see WX7EEE
on VHF has offered you a ride to the hamfest.  We know this packet is
from Radio Port 2 since the previous packet displayed by the PK-900was the "A:*** CONNECTED" message from Port 2.

Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF.  This way he will understand
that it may take you a little longer to respond to his packets.
Before you can send data to Radio Port 2, you must switch to this Port
with "|A" or the text you type will be sent to Radio Port 1.

You receive another packet from Jim on HF giving you a signal report
and describing his antenna.  Again, the "0:" in front of the text
shows this was received on Port 1.

Now you want to give Jim a signal report so you must switch to Port 1
with |0 before your text or it will be sent to WX7EEE on Port 2.

After sending Jim a signal report, you tell your friend on VHF that
you want to go to the hamfest with him.  You must switch back to
Port 2 by typing |A before sending to WX7EEE.

In the next received packet, we see that WX7EEE on VHF agrees to pick
you up at 8:00 to go to the hamfest.  The "A:" in front of the packet
shows that this packet was received on Port 2.

Immediately following, you see a packet from Jim on HF saying it's
time to shut down.  The "0:" identifies this as a Port 1 packet.

The last packet you sent was to WX7EEE on VHF so you decide to reply
to him first.  This way you do not have to change Ports.  After your
reply to WX7EEE, you change to Port 1 with |0 and send your good-byes
to Jim on HF.

Your friend WX7EEE on VHF says 73 and signs off with you.  After two
stations agree to sign off, one of them must initiate a Disconnect.
You decide to disconnect from WX7EEE by first switching to Port 2 with
|A and then entering Command mode with a <Ctrl-C>.  Once you see the
Command prompt (cmd:), enter the Disconnect "D" command.
Shortly after you do this you see the display
"*** DISCONNECTED: WX7EEE" from the PK-900.

Jim on HF has decided to initiate the disconnect with you so the final
packet you see is the "0:*** DISCONNECTED: WX1AAA" frame from Port 1.

##### 4.8.3.3 More Thoughts on Port Switching

One problem of having more than one Radio Port is remembering which
port you are currently using.  In the dual port sample QSOs above,
this was not a problem, but after it has been hours or days since you
have used your PK-900, you may forget which port you last used.

With AEA Pakratt Software programs, the on-screen status will always
show which port you are using so this is not a problem.  With other
programs, you will have to query the PK-900 with the CSTATUS SHORT
command.  The CSTATUS command displays the status of the logical
channels of Port 1 and Port 2 of the PK-900.  The CSTATUS SHORT
command displays the status of the active channel and any other
connected packet channels.  After completing the sample QSOs above,
the PK-900 would display the following.

<!-- PDF p.78 -->

cmd:CSTATUS S

Ch. A - IO DISCONNECTED

This reminds you that Channel A is your current I/O channel.  Any text
that you type in the Converse mode will be sent to channel A on Radio
Port 2.  If you had been connected to any other packet stations, the
callsign and channel would have also shown in the display.

Sometimes you might not want to be bothered with anything from the
Radio Port you are not using.  For these times either Radio Port may
be turned OFF with the RADIO command.  For example, let's say that in
the above example QSO you wanted to work HF packet and did not want to
be interrupted with any VHF connects.  Typing the following command
would cause Radio Port 2 to be disabled.

cmd:RADIO /0
RAdio  was  1/2
RAdio  now  1/0

When a PK-900 Radio Port is disabled, the front panel LCD Status
indicator for that port will be extinguished as a reminder.

### 4.9 Advanced Packet Operation

Your PK-900 has many commands and features that are not used for
day-to-day connects conversations.  Still, as you become more familiar
with packet, some of these features may become important to you.

#### 4.9.1 Transparent Mode

The TRANSPARENT mode allows any 8-bit binary character to be sent by
your packet station.  You usually must use the TRANSPARENT mode to
transfer binary and program files to and from other stations.

You can enter the TRANSPARENT mode either by typing TRANS at the cmd:
prompt after you connect, or by setting CONMODE to TRANS.  Either way,
once in the transparent mode, any character you type will be sent
automatically after the PACTIME setting.  This way the PK-900 can
send any character.  We recommend using HARDWARE flow control in
transparent mode but SOFTWARE flow control is available with the
TRFLOW and TXFLOW commands.  To get back to Command mode after you are
finished with transparent mode, you must type the COMMAND character
(default <CTRL-C>) 3 times within the "Guard time" set by the
CMDTIME command (default 1 second).  (Hold down the CTRL key and press
C three times rapidly.)

#### 4.9.2 Gateway Operation

The PK-900 can allow packet stations on one radio port to communicate
with stations on the other port.  When the PK-900 bridges two packet
frequencies in this manner, it is said to be a "Gateway."  Gateway
operation is a dual port feature and requires that  the correct
port 2 packet modem be loaded.  Once this has been done the gateway
function is enabled by entering a callsign in MYGATE.  The callsign
must not be the same call sign and SSID as MYCALL or MYMAIL.

<!-- PDF p.79 -->

When another station digipeats via the callsign in MYGATE, your
PK-900 will Gateway between Radio Port 1 and Radio Port 2.  If you
provide this feature to users in your area, you may want to BEACON a
message on both ports informing users that your gateway is available.

At this time however, unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary Authorization
(STA) from the FCC.  This restriction may someday change, but until
then US amateurs must always have control of their HF transmitters
when an automatic device such as the PK-900 Gateway is in use.

#### 4.9.3 Sending 8-bit Data in Converse Mode

Sometimes you may need to send a file that contains some 8-bit data,
but not need all the features of the TRANSPARENT mode.  In this case,
you may find turning the command 8BITCONV ON is all that is needed.

#### 4.9.4 The Packet QRA Feature

The PK-900 recognizes UI frames with a destination field of "QRA"
and will respond by sending an ID packet.  This is helpful for others
new to your area that are looking for other packet stations to talk
to.  To disable this feature and remain anonymous, simply set User
BIT 22 OFF (UBIT 22 OFF).  The default is ON.

If you wish to see who is available in your local area, simply set
your UNPROTO path to QRA and send a packet.  Within 1 to 16 seconds
other stations should respond to your QRA request by sending an ID
packet of their own.  This feature is compatible with TAPR's QRA
feature introduced in the 1.1.8 firmware release.

#### 4.9.5 The CFROM Command

If you ever want to exclude certain stations from connecting or only
allow friends to connect, you may do so with the CFROM Command.  See
The Command Summary for details of its operation.

#### 4.9.6 Operating in Full-Duplex

Most packet operation is carried on over Half-Duplex transceivers that
can transmit and receive but not do both at the same time.  When a
separate transmitter and receiver is used such as in satellite
operation, you may want to use the FULLDUP Command in the PK-900.

#### 4.9.7 Identifying as a Digipeater

If your PK-900 is being used as the primary digipeater in a local
area, you may want to enable the HID command.  HID will automatically
identify your station for others to see.

#### 4.9.8 Digipeater Alias Callsign

If your packet station is being used as the primary digipeater in your
local area, you may want to choose a simpler identifier for others to
use with the MYALIAS Command.

<!-- PDF p.80 -->

#### 4.9.9 Morse ID in Packet

In most countries, packet is an accepted mode of identification so
this command should be left OFF.  In some countries, however, a Morse
ID is required when packet is used and so the MID command should be
enabled.

#### 4.9.10 Sharing Packet Channels with Voice Operation

Although it is seldom needed, the PK-900 does have an input for
SQUELCH information from a transceiver on the RADIO connectors.  This
input should be used and the SQUELCH command set if the packet channel
is to be shared with voice operation.

#### 4.9.11 Disabling Transmit Operation

Occasionally for test purposes it may be desired to disable the PTT
circuit in the PK-900.  This is done with the XMITOK Command.

### 4.10 Seldom Used Commands

The following commands operate in Packet, but are seldom needed.
They are listed for reference and described in the Command Summary.

AFilter   BBSmsgs   CONPerm   CPactime  DCdconn   Flow      HEAderln
MDMon     MRpt      MXmit

### 4.11 Packet Lite HF Packet Protocol Extension

Amateur radio needs a better communications mode for HF operation.
Baudot and ASCII have no provision for error detection.  AMTOR FEC and
ARQ are more resistant to errors, but do not carry the full ASCII
character set.  300 baud AX.25 packet is undesirable because the long
transmissions are prone to bit errors, any one of which invalidate the
whole frame.  PACTOR is a recent protocol improvement.

Packet Lite is, as its name suggests, an abbreviated form of packet.
It is designed as a transparent extension to the AX.25 protocol that
reduces the "overhead" of all HF packet frames without digipeaters.
Packet Lite does not solve HF packet's problems, but it should provide
some throughput improvement on HF where it is desperately needed.

AEA's engineering department is interested in hearing from Packet Lite
users with any comments or suggestions on improving the protocol.

A Brief Description of the Packet Lite Protocol

The main feature of Packet Lite that reduces overhead is that it uses
an address field of only 4 bytes.  A standard AX.25 header without
digipeaters uses 14 bytes of addressing.  Shortening the packet frame
header lessens the possibility of any given frame taking a hit.

Packet Lite reduces the length of an I-frame slightly, but its real
strength is the shortening of the acknowledgment frames, resulting infewer garbled acks and therefore fewer unnecessary retries.  An ack
(RR, RNR or REJ frame) in standard AX.25 consists of 19 consecutive
bytes that must be copied with no hits; Packet Lite reduces the length
to 9 bytes, or 47% of the standard ack length.

A couple of restrictions are necessary to accomplish this.
First, Packet Lite works only between two stations connected directly,
with no digipeating allowed.  If digipeaters are introduced to the
address field, the advantage of the reduced overhead disappears.

Second, all Packet Lite connections emulate AX.25 version 2.0 (RR
polling instead of retrying I-frames).  This is necessary for the 10minute identification described below.  Also, the main reason version
1 continues to be used on HF is that on a retry a [RR,P] polling frame
is so long that one might as well just send the I-frame again.  Packet
Lite's polling and ack frames are so short that the AX.25 version 2.0
polling method is now worth doing.

#### 4.11.1 Enabling Packet Lite

To begin using Packet Lite, first make sure you have made all the
proper HF Packet settings discussed earlier in this chapter.  Be
careful to ensure VHF is OFF and a 300 bps Modem has been selected.

Once these settings have been made, to enable the Packet Lite protocol
extensions, turn the command LITE ON for the appropriate Port.
For example to enable Packet Lite on Port 1, type "LITE ON" at the
command prompt.  You must not be connected to any other stations on
that Port or you will not be allowed to change the LITE command.

Initiating a Packet Lite Connection

Once all the above settings have been made, simply issue the standard
CONNECT request as described earlier in the chapter.

If the station you are connecting to also has Packet Lite enabled, a
Packet Lite connection will result and you and the distant station
should enjoy a more reliable QSO than others on the same frequency.

#### 4.11.2 Compatibility With Standard AX.25 Stations

If the station you connect to does not have the Packet LITE protocol
extensions, there are three known possibilities that will occur.

1.   WA1ABC>WB2XYZ [C,P] 01 3E 38 58 32          {Packet Lite attempt}
WB2XYZ>WA1ABC (UA,F)                        {Standard ack}

In this case, the non-Lite station sees the [C,P] control byte but
ignores the non-standard bytes following it.  It replies with a
standard UA frame, and the connection proceeds as standard AX.25.

2.   WA1ABC>WB2XYZ [C,P] 01 3E 38 58 32          {Packet Lite attempt}
WB2XYZ>WA1ABC (FR): 3F 00 03                {Frame Reject}
WA1ABC>WB2XYZ [C,P]                         {Standard attempt}
WB2XYZ>WA1ABC (UA,F)                        {Standard ack}

<!-- PDF p.82 -->

In this case, the non-Lite station notices the non-standard bytes
following the control byte and issues a FRMR (Frame Reject) to signify
that a protocol violation has taken place.  The PK-900 receives the
FRMR and automatically reverts to standard AX.25, sending the connect
retries without the Lite PID and address bytes.

3.   WA1ABC>WB2XYZ [C,P] 01 3E 38 58 32          {Packet Lite attempt}
...                                         {No response}

In this case, the non-Lite station notices the non-standard bytes
following the control byte but sends no response at all.  If this
occurs, you must turn the command LITE OFF and try to connect again to
the distant station.  No adverse effects are caused by this, but
transparency with standard AX.25 is lost when the receiving station
does not acknowledge a Packet Lite connect request in some manner.

We know that TCP/IP, NET-ROM and DRSI stations ignore Packet Lite
Connect requests.  These stations are normally found on VHF, but to be
safe, the LITE command should be turned OFF when not in use.

#### 4.11.3 Packet Lite Protocol Enhancement Summary

The following describes the Packet Lite protocol extension in depth
for those interested in the technical details.  It is not necessary to
read or understand the following section to use the protocol.
Here is a summary of a Packet Lite exchange, where WA1ABC calls WB2XYZ:

Connect:
W  B  2  X  Y  Z  -0   W  A  1  A  B  C  -0   SABM  01  3E  38  58  32
destination    |      source          | CTRL PID | short address

The destination and the source are both 7 bytes long.  Everything up
to the CTRL byte (SABM) is standard AX.25 version 2.0.  The "Protocol
ID" of 01 hex is Packet Lite's reserved value, which provides a way of
interpreting the following bytes.  This leaves room for other
extensions to AX.25 in the future.  The short address bytes are the
right-justified bytes of the address field that WA1ABC proposes to use
in subsequent Packet Lite frames with WB2XYZ.  In this case, the AEA
implementation of the short address is illustrated.  3E38 is a
compressed version of the destination WB2XYZ and 5832 is a compression
of WA1ABC.  However, any combination of 26 bits may be used
(see "Technical Details" below).

Connect acknowledgment:
W  A  1  A  B  C  -0   W  B  2  X  Y  Z  -0    UA   01  58  32  3E  38
destination    |      source          | CTRL PID | short address

WB2XYZ replies to WA1ABC.  Again everything up to the CTRL byte (UA)
is standard AX.25.  The "PID" of 01 and the short address confirm that
WB2XYZ has accepted the Packet Lite connection.  The short address is
again the right-justified representation of the address field that
WB2XYZ will be using in subsequent Packet Lite transmissions.  In this
case WB2XYZ has accepted the short address field suggested by WA1ABC,
and has shown his acceptance by echoing the address back in reverse
order (5832 = WA1ABC and 3E38 = WB2XYZ).  AEA products always accept
the short address from the SABM frame; however, the Packet Lite
protocol allows the sender of the UA frame to propose a differentcombination of 26 bits, to avoid conflicting with another Lite QSO.
In either case, the sender of the original SABM must accept the 26
bits in the UA frame, reversing the address order for its own
transmissions.

Transmission of data:
7C F0          B0 65          10       F0   Test <CR>
3E38 shifted   5832 shifted   [I,P;0,0]
short dest.    short source     CTRL     PID   text

WA1ABC sends data to WB2XYZ in Packet Lite format.  The address field
consists of the short address from WB2XYZ's UA frame, reversed and
left-shifted.  The added bits come from AX.25 version 2.0's command
and response bits, and the end-of-address bit.

Acknowledgment of data:
B0 64          7C F1          31
5832 shifted   3E38 shifted   (RR,F;1)
short dest.    short source     CTRL

WB2XYZ acknowledges the data from WA1ABC.  The address field is
reversed.  This is the shortest length frame possible in Packet Lite.
4 address bytes + 1 CTRL byte + 2 flags + 2 CRC bytes = 9 bytes.

Every 10 minutes the stations must identify using both long and short
addresses:

W  B  2  X  Y  Z  -0   W  A  1  A  B  C  -0   [RR,P;0] 01  3E  38  58  32
destination    |      source          |   CTRL  PID | short address

W  A  1  A  B  C  -0   W  B  2  X  Y  Z  -0   (RR,F;1) 01  58  32  3E  38
destination    |      source          |   CTRL  PID | short address

Either station may initiate the ID exchange.

Disconnect:
W  B  2  X  Y  Z  -0   W  A  1  A  B  C  -0   DISC  01  3E  38  58  32
destination    |      source          | CTRL PID | short address

Disconnect acknowledgment:
W  A  1  A  B  C  -0   W  B  2  X  Y  Z  -0    UA   01  58  32  3E  38
destination    |      source          | CTRL PID | short address

At the end of the connection, the two stations must once again
identify using both long and short addresses.

AEA firmware supporting Packet Lite also contains code that permits
monitoring of Packet Lite and extended AX.25 frames.

Packet Lite Shortened Address Technical Details

The Packet Lite address field consists of 26 bits distributed over 4
bytes (or octets, as the AX.25 specification calls them).  These bits
are considered to be two groups of 13 bits each, roughly equivalent to
a destination and a source ID.  If we label the bits A-Z and show
their use in the address field of a Lite frame, the bits are
distributed as follows:

<!-- PDF p.84 -->

A B C D E F G 0   x H I J K L M 0   N O P Q R S T 0   y U V W X Y Z 1

The least significant bit of each byte is used to show whether or not
the byte is the final byte in the address field, as in standard AX.25.
The bits "x" and "y" (lower case) have the function of command and
response, similar to the function of the standard AX.25 version 2.0
SSID byte C bits (see AX.25 Protocol version 2.0, section 2.4.1.2).

In the AEA implementation of Lite addressing, the standard callsigns
are compressed to yield short addresses.  The destination callsign is
compressed into bits A-M, and the source into N-Z.  These bits are
used as the address field suggestion following the control byte in the
Connect (SABM) frame.  When the bits are used following the control
byte as an ID suggestion or a real extended ID, the format is:

0 A B C D E F G   0 0 H I J K L M   0 N O P Q R S T   0 0 U V W X Y Z

If we label the 7 standard right-justified callsign bytes 1-6 and
SSID, here is how we derive the first group of address bits A-M:

ABCDE  =  (byte 1  XOR  byte 4)             AND  $1F
FGHI  =  (byte 2  XOR  byte 5)             AND  $0F
JKLM  =  (byte 3  XOR  byte 6  XOR  SSID)  AND  $0F

AEA firmware derives the second group of bits N-Z the same way.  Other
implementations are free to select any combination of 26 bits when
setting up the short address in either the initial SABM frame or its
UA response.

### 4.12 Packet Meteor Scatter Extension

A new packet protocol extension has been added for meteor scatter
work that allows a Master/Slave packet connection to be established.
This is done to reduce the possibility of simultaneous transmissions
by both sides of a packet connection over a long meteor scatter path.

This experimental protocol is activated by turning User BIT 18 ON
(UBIT 18 ON).  When UBIT 18 is ON (default OFF) the packet station
who initiates a packet connect will become the Master station and the
station who acknowledges the connect becomes the Slave.

After a Meteor Scatter connection has been established, the Master
station will continually send either information frames (I-frames)
or polling frames and await an acknowledgement from the slave.  The
Master station therefore sends packets constantly, even if all its
I-frames have been acknowledged.  The slave station sends nothing,
not even I-frames, until it receives a polling frame from the master.
The Slave station may only send an I-frame to the Master after a
poll frame has been received.

The packet timing of the Master station is critical for proper Meteor
Scatter operation.  In a normal AX.25 packet connect, the FRACK timer
counts down until it reaches zero and then a Retry of a poll frame is
sent.  The FRACK timer counts in units of seconds however and a finer
timing resolution is desirable for Meteor Scatter work.  A new timer
called FRICK has been added which times in 10 mSec increments.  The

<!-- PDF p.85 -->

FRICK timer can be set from 0 (disabled) to 250 which corresponds to
a time of up to 2.5 Seconds.  See the Command Summary for a complete
description of the FRICK timer.

The following settings are recommended for this method of Meteor
Scatter work.  Both packet stations should use these same settings.

UBIT 18 ON
RETRY 0
AX25L2V2 ON  (default)
MAXFRAME 1
(CHECK doesn't matter)
FRICK n, where n is large enough to allow the other station time
to send the start of an acknowledgement frame

Note: Do not operate the unit with multiple packet connections while
FRICK is active (1-250).  In contrast to FRACK, which provides
one retry timer per multi-connect channel, there is only one
FRICK timer on each radio port of the PK-900.  Each logical
channel will try to use the same FRICK timer, causing
interference to the operation of the other channels.

Digipeaters should not be used when in the Meteor Scatter mode.
The FRICK timer (unlike FRACK) does not allow any extra time when
digipeater stations are specified.

To return to normal AX.25 packet operation turn User BIT 18 OFF.
Also, be sure to disable the FRICK timer (by setting FRICK to 0)
when you are through operating in Meteor Scatter mode.

<!-- PDF p.86 -->

> [No extractable text on this page — figure, schematic, or blank.]
