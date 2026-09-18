# Chapter 4 — Packet Radio

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 45–84).

**Agent extract:** Dual port: HF/multi-mode typically port 1, VHF packet port 2. Load modems with `MODEM n` (example `MODEM 33` dual-port). HF vs VHF packet defaults (PDF p.69): SLOTTIME 12/30, PACLEN 64-or-less/128, MAXFRAME 1/4, FRACK 8/5, VHF OFF/ON, HBAUD 300/1200, MODEM 10/12. `QHPACKET` / `QVPACKET` remember those modem numbers. HF FSK 200 Hz (2110/2310). First HF copy: `MONITOR 6`. Host Mode framing is **not** in this chapter.

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

### (PDF p.45)

#### 4.1.1

#### 4.1.2

### 4.1

Overview
1.
In the last several years Packet has grown to become perhaps the most
popular digital mode on the amateur bands. Although Packet can be
found on HF (primarily on the 20 meter band) it is most popular on the
VHF and UHF FM bands. This chapter will start with general Packet
operation and then discuss VHF and UHF Packet.
Packet on the HF bands
requires some special considerations, so we will leave it until the
end of this chapter.
Your DSP-2232 can operate HP or VHF Packet (on Radio port 2) and any
other digital mode on Radio port 1 at the same time. This feature can
come in handy allowing you to keep an "ear" on local packet activity
while operating on any of the other digital modes normally found on
HF. Sorting out the data from both Radio ports also requires some
special considerations and will be discussed later in the chapter
after HF Packet operation has been covered.
Getting Started
You can learn quite a bit about Packet operat ion with the
DSP-2232 before even connecting it to a transceiver.
For your first
packet practice, the DSP-2232 will be connected in a "loopback"
circuit as done in the "Quick Check" performed in Chapter 2 .
In this
configuration, the DSP-2232 will "talk to itself" which allows you to
become familiar with packet before actually going on the air.
Making the Loopback Connect ion
Make sure the DSP-2232 is turned OFF and power is removed before
performing one of the following.
1
11
Locate the 5-pin DIN plug with the WIRE "Loop-back" jumper and
insert it into the RADIO-I connector on the DSP-2232 rear panel.
If you cannot locate the pre-made "Loop-back" jumper and have a
spare Radio cable, you may construct the following:
2.
3.
4.
5.
Get the unused Radio Cable from Chapter 3.
Strip about 1/4 inch of insulation from the Green and White
wires at the "radio" end of the cable.
Join the Green and White wires by twisting them together
gently.
Insert the 5-pin DIN connector end of the cable into the
" RADIO-I" connector on
Go on to section 4.2.
the DSP-2232's rear panel.

### (PDF p.46)

### 4.2

Packet Introduction
You •ve now connected your DSP-2232's transmit audio output to its
receive audio input.
Your DSP-2232 can now "talk to itself" in
packet .
1.
Set the side-panel AFSK level control for RADIO-I at
( straight up and down) for the following Packet introduction.
NOTE :
If you have adjusted the RADIO-I side-panel AFSK level
control for a particular transceiver in chapter 3, then mark
this setting with a pencil so it can be reset when finished.
2.
Turn on your computer. Load and run your communications program.
If you are using an AEA PAKRATT Program, follow the program
manual to enter the Packet mode on Radio Port 1, and then skip to
step 4.
If you are using another Computer program or a Terminal, set the
communication parameters as done in Chapters 2 and 3.
3.
Press the DSP-2232's rear panel power switch to the ON position
if you have not already done so.
You should notice the sign-on message seen in chapter 2. The LCD
STATUS display should indicate you are in the Packet mode.
4.
After you have seen the sign-on message and/or entered the Packet
mode, you must enter your own callsign (with the MYCALL Command )
if you are to converse with any other Packet stations.
If you
try to connect to a station without entering your callsign, your
DSP-2232 will send you the following message:
? need MY call
If you are a SWL and do not intend to transmit, you should enter
"AAA" as a Callsign (MYCALL) .
If you are using an AEA PAKRATT program, follow the instructions
If you are using a
in the Program Manual to enter your callsign.
terminal or terminal emulating program on your computer, you must
use the MYCALL command to install your call siqn.
Note that since the DSP-2232 has two radio ports, you must enter
a callsign for each port. The callsigns may be the same, or can
be different to make life a little easier in the "two Ham"
family. You must change the callsign from the default "DSP" on
each radio port, or that port will not transmit packets.
For
example, if your Callsign is WX2BBB, enter the following:
```text
cmd:MYCALL WX2BBB/WX2BBB
```
Mycali was DSP/DSP
now WX2BBB/WX2BBB
MYca11
Both Packet radio ports will now
assume the callsign WX2BBB.

### (PDF p.47)

5.
6.
If you are using an AEA program follow the instructions in the
Program manual to CONNECT in packet mode to your own callsign
(the one you just entered in MYCALL) .
If you are using a Computer Terminal or a non-AEA program,
entering the following after the "cmd:"
command mode prompt will
cause the DSP-2232 to Connect to yourself:
CONNECT (your callsign) <Enter>
After pressing <Enter>, you should observe the Port 1 SEND LED
and DCD LED light, and the Port I TUNING bar graph spread. After
a few moments, your monitor should display:
*** CONNECTED to (your callsign)
Notice that the front panel Port 1 Connect LED (CON) has lighted
and the STATUS display indicates that the DSP-2232 is connected
to a packet station.
You may also notice that the Port 1
Converse LED (CONV) is lit indicating that the DSP-2232 is ready
to CONVerse with the station (in this case it is yourself) .
This is how a packet connection is established. Whether you are
"Connecting" to another Amateur, a Packet Bulletin Board System
(PBBS) or a Netw.orking Switch (more on this later) this initial
procedure must be used to establish each and every Connection.
Type a few characters to yourself such as "Hello this is my first
packet Connect. My name is " then press the <Enter> key.
Shortly after pressing the <Enter> key, you should see the
If you had connected
message you typed reappear on your screen.
to a distant station, they would have seen the message you typed
appear at nearly the same time .
After you have typed a few lines (packets) to yourself, you will
probably want to end the connection or "DISCONNECT" .
If you are using an AEA program, follow the instructions to
DISCONNECT from the packet station you are talking to.
If you are using a Computer Terminal or a non-AEA terminal
program, the following will cause the DSP-2232 to DISCONNECT:
Enter <CONTROL-C> (hold down the Ctrl and then type the letter C) .
Your monitor should respond with the command prompt :
cmd :
Enter D
Your monitor should respond with :
```text
cmd: DISCONNECTED: (your callsign)
```
pl (your call) * > (your call) (UA)
Note that the front panel Connect LED (CON) should go out and the
LCD STATUS display should indicate you are Disconnected.

### (PDF p.48)

DSP-2232 OPERATING
8.
You
1.
MANUAL
have
You
You
the
You
just done the three things necessary in any Packet QSO.
started the QSO (with yourself) by CONNECT ing. (Step 5)
sent some information (to yourself) and then received
information that you sent. (Step 6)
then ended the QSO by DISCONNEcting. (Step 7)
Repeat steps 5, 6 and 7 above until you feel comfortable with
Connecting, exchanging informat ion and Disconnecting. These
operations will be performed each time you use Packet BO they
should be second nature to you before going on the air.
When you feel comfortable Connecting, sending information and
Disconnecting, you are ready to start listening to VHF packet .
Turn OFF and remove power from the DSP-2232 .
Return the Port 1 side-panel AFSK level potentiometer to the
setting you marked in Step 1.
Remove the " Loop-back" jumper from the RADIO-I port and
reconnect your transceiver or receiver set-up in Chapter 3.

### 4.3

VHF /_UHF Packet Operation
We will first listen to (and watch) some of the VHF or UHF Packet
activity in your Local Area. This will allow you to become a little
better acquainted with packet in your area before going "On The Air" .
2.
3.
Construct a Radio Cable for the VHF/UHF transceiver you intend to
use for Packet as described in Chapter 3 and connect your
transceiver to the RADIO-I connector on the DSP-2232 rear panel.
Load and run your communications program and enter the Packet
Mode as done in the Packet Introduction section above.
Set Radio Port 1 for VHF Packet operation (default) as follows.
If you are using an AEA program, follow the instructions to
select the VHF modem on Port 1 by turning the VHF Parameter ON,
this will automatically set the radio baud rate HBAUD to 1200.
If you are using a Computer Terminal or a non-AEA terminal
program, the following sets the VHF mode of the DSP-2232 :
(Type C while pressing the key down. )
Type
Your monitor should respond with the cornmand prompt :
cmd :
Then enter VHF ON <Enter>
Your monitor should respond with:
Vhf
Vh f
was OFF/ON
now ON/ON
HBAUD now 1200/0

### (PDF p.49)

4.
Your DSP-2232 contains other Packet modems that may be selected
with the MODEM command. If you wish to select a different DSP
modem for Packet operation, simply type MODEM followed by a modem
number from
the list below:
10:
11:
12:
13:
14:
15:
16:
17:
18:
50:
51:
pl
pl
pl
pl
pl
pl
pl
pl
pl
pl
pl
pl
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
300 bps HF 2110/2310
300 bps HF 1460/1260
(North America)
( European )
1200
1200
1200
2400
4800
4800
9600
1200
2400
9600
bps
bps
bps
bps
bps
bps
bps
bps
bps
bps
VHF
PACSAT
PSK
V. 26B
PACSAT
PSK
FSK K9NG/G3RUH
MSK
MSK
G3RUH. U022. eq
If you know
the VHF activity in your area uses a modem from the
above list other than the Bell 202 default, you may enter this
modem number in the command QVPACKET. The modem in QVPACKET is
automatically selected when . the VHF parameter is ON and the
Packet mode is entered.
Turn ON your VHF or UHF FM transceiver and tune to a known packet
channel in your area. Most packet operation is on s implex, so
the repeater offset on your transceiver should be disabled.
If you know there is packet in your area, but do not know the
frequency, you should try some of the following frequencies:
2 meter (144 MHz) band:
145.01 MHz, 145.03 MHz, 145.05 MHz,
144.99 MHz, 144.97 MHz, 144.95 MHz,
1-1/4 meter (220 MHz) band:
223.40 MHz, 223.42 MHz, 223.44 MHz,
70 cm (440 MHz) band:
145.07 MHz,
144.93 MHz,
223.46 MHz,
145.09 MHz
144.91 MHz
223.48 MHz
5.
440.975 MHz, 441.000 MHz, 441.050 MHz, 441.025 MHz, 441.075 MHz
You know you •ve found a packet channel when you hear the
characteristic "Braaaaaap" sound of packet transmissions.
Once you 've found an active packet channel, you must make sure
you have enough receive audio (volume) from your transceiver to
light the DCD LED on the DSP-2232 when a packet is beinq
rece i ved .
If the DCD LED does not light when packets are
received, you must increase the audio level from your
transceiver. The DCD LED must light for the DSP-2232 to be able
to receive packets.
You must also make sure that the DCD LED goes out when no packet
signals are present on the channel.
If the DCD LEP does not go
out when the channel is clear, make sure the Squelch control on
If
your transceiver is set high enough to silence the speaker.
the DCD LED stays on when the packet channel is quiet, your
DSP-2232 will never send packets to other stations.

### (PDF p.50)

4.3. I
4.302
What You Should See
If all is operating properly, you should see some packets on your
Some typical packets you might "Monitor" are shown below:
screen .
NOTE :
pl (C)
pl (UA)
pl
Goodnight John, its been nice talking to you.
pl
Hi Bob, how are you this evening?
pl
Mail for: K6RFK N7ML
pl
NET/ROM 1.3 (SEA)
pl
connected . to
pl [D)
You will probably see data (Packets) on the tuning bar-graph
which do not print on the screen. This is normal and is a
function of the MONITOR and the MPROTO cornmands .
What It Means
There are different types of packets that mean different things to the
DSP-2232.
Your DSP-2232 keeps track of and knows what to do with the
packets so users need not be concerned with them most Of the time.
Since the DSP-2232 can "Monitor" all Packet activity on a channel,
we' II briefly discuss the types of packets you will most often gee.
Skip to the next section if you do not plan on doing much monitoring.
Let 's look at the first packet in the examples above and get
acquainted with what it all means.
pl [C)
The first two characters in monitored packets represents which radio
Every g ingle
port "heard" the packet and will be either "pl" or "p2" .
packet you send will have your callsign (the one you just entered in
MYCALL) as the first callsign of the packet. The callsign after the
">" is the next station the packet will go to. So the packet listed
above originates from N7ALW and is being sent to WA7GCI. All packets
will have at least these two callsign fields.
The " (C)" immediately following the two callsigns identifies this
So we see that N 7 ALW is requesting a
packet as a CONNECT Request .
packet CONNECTION with WA7GC1.

### (PDF p.51)

The second packet in the above examples is a response to the first.
pl (UA)
In this case we see that WA7GCI is sending to N 7 ALW by the order of
the call signs. This packet acknowledges the Conneet request as shown
by the " (UA) " which stands for Un-numbered Acknowledge.
One benefit of packet radio is that packets can be relayed or
"digipeated" by stations on the same frequency.
In fact, packets can
be relayed by up to eight stations to reach a distant station that
cannot be heard directly.
In practice, digipeating through many
stations does not work very well, but you will often see packets
digipeating through one or two stations to reach their destination.
The packet shown below is an example of a digipeated packet.
pl
Goodnight John, its been nice talking to you .
This packet originated from K6RFK and is being sent to N7GMF but is
"Digipeated" through the station N7ALW. We also see that this packet
contains data by the text "Goodnight John... " .
Another thing that
should be noticed in . this packet is the asterisk ( * ) in the first
line. The asterisk tells which station was actually heard sending the
packet .
In this case, we can see that we actually heard the
digipeating radio station N7ALW. Without the asterisk, we could not
tell whether the transmission came from radio station K6RFK or N 7 ALW .
More will be discussed about digipeating later, but the above example
is typical.
The following packet is a data packet from N 7 ALW to WA7GCI .
pl
Hi Bob, how are you this evening?
Remember that in the first example we saw the two stations Connect .
Now that they are connected, they may exchange data packets.
The following packet is a Beacon packet from KD7NM. Since we see the
packet is addressed to "MAIL" we know KD7NM is probably a Packet
Bulletin Board System (PBBS) .
pl
Mail for; K6RFK N7ML
The data section of this packet says "Mail for: K6RFK N7ML"
This
Beacon lets people know that K6RFK and N7ML have mail waiting on the
KD 7 NM P BBS without having to connect.

### (PDF p.52)

The following Beacon packet is intended as identification for a
NET/ ROM level-3 packet networking switch.
pl
NET/ROM 1.3 (SEA)
In this case, the Packet Switch is using the callsign N7HWD-8, but
also uses the alias SEA as a callsign. There are many types of Packet
Switches now in use, but NET/ ROM is one of the most popular. We will
briefly discuss using a NET/ ROM switch later in this chapter since
most switches operate in much the game way.
The packet below was sent by the network switch SEA to N7ML.
pl
Connected to
The packet above from SEA contains the data "SEA:N7HWD-8> Connected to
#SEA: N7HWD-7" .
This message tells N7ML that he is now connected to
another port on the SEA Node named #SEA. Again, we will talk more
about how and why N7ML might want to do this later in the chapter.
The following packet {s again from K6RFK to N7GMF and is being
digipeated through N7ALW. This packet indicates that K6RFK is
finished talking to N7GMF and wants to Disconnect. Again we see that
we are not hearing K6RFK, rather we are hearing N7ALW as indicated by
the asterisk ( * ) after the callsign.
pl [D)
The following packet is an acknowledgment (or simply called an ACK)
that lets K6RFK know that N7GMF has acknowledged the Disconnect
request sent above. K6RFK and N7GMF are no longer Connected.
pl (UA)
As can be seen, all of the above examples were heard on radio port 1
If a second radio was connected to radio port 2 and
of the DSP-2232.
a dual port modem was loaded, we would have seen some monitored
packets prefaced with a "p2" as well .
See section 4.8 for more
information on controlling both radio ports when you are ready.
NOTE :
Some applicat ions software such as PB and PG for Amateur
Satellite use have problems with the port designators "pl" and
"p2" in front of monitored packets. To disable the port
designators "pl" and "p2" from appearing turn OFF User Bit 19
(UBIT 19) by typing "UBIT 19 OFF at the .command
prompt .

### (PDF p.53)

What Happens When You Connect

##### 4.3.3.1

##### 4.3.3.2

If you are working with a friend who is familiar with packet, you may
If you are on your own, the following
want to skip to section 4.4.
three sections will help you learn what to expect on VHF/UHF packet.
There are three different kinds of packet stations you are likely to
encounter in your first Connects: Standard TNCg, Mailbox Systems and
Network Switches. The following sections discuss each station type.
Standard TNCs
When you first turn on your DSP-2232, it becomes a standard AX. 25
packet TNC ( Terminal Node Controller) . All TNCs and Multimode
controllers have this capability. When you Connect to a TNC, in most
cases you will be connecting directly to someone's computer screen.
If you see an automatic Connect Message (CMSG) similar to the one
below, you know you have reached a TNC.
If I don't respond, please
Welcome to my packet station.
leave a message and Disconnect .
If you get a message like this when you connect to another station,
usually you would type something like "Are you there?"
If you do not
see a response from the other station in a minute or so, simply leave
- just like a telephone answering machine.
a message
The TNC at the other station should then hold your message until the
operator returns to his computer .
Later we will discuss how your
DSP-2232 can do the same for messages it receives from others.
Mailbox Message Systems
Although Standard TNCs allow incoming messages to be saved, there is
no way for the owner to leave a message for someone who will connect
at a future time. The ability to both send and receive messages
without the owner being present is accomplished by a Mailbox .
There are many different Packet Mailbox systems in use.
Some systems
are large and require the use of a dedicated computer. Other systems
are small like the personal MailDrop built into your DSP-2232.
Large systems are often called Packet Bulletin Board Systems (PBBS)
since they serve as electronic message centers for a local area.
PBBS's are a source of information as well as a gateway for messages
that can be sent to and received from other parts of the country or
world. You will probably want to locate the local area PBBS nearest
you and connect to it from time to time.
Mailbox systems are easy to use and most operate in much the same way.
Most Mailboxes and other automatic systems usually have Help available
by sending an "H" or " ? "
If you connect to a Mailbox such as a
DSP-2232 MailDrop you will see something like the following:
* CONNECTED to KD 7 NM
(AEA DSP-2232)
18480 free >

### (PDF p.54)

If you get something like this when you connect to another station,
try typing an "H" or a "?" to get a help list as shown below:
A ( bort )
B(ye)
H(elp)
J ( log)
K (ill)
L (ist)
R ( ead )
S ( end )
Stop Read or List
Log Off
Display this message
Display stations heard
Kill message number n
Kill messages you have read
KM
List message titles
L
. List messages to you
LM
Read message number n
• Read all your unread messages
RM
Send a message to SYSOP
s
Send a message to station n
as H(elp)
Same
18480 free >

##### 4.3.3.3

[AEA DSP-2232)
There are quite a few options available on the MailDrop, but the most
commonly used commands are L (ist), R(ead), S (end) and K (ill) message.
For example, you may first want LIST all the messages that are
available on a mailbox that you connect to. This is done by simply
sending "L" or "LIST" command to the system you have just connected.
If you are interested in any of the message subjects that appear, you
may then READ the messages that interest you. To read a message,
simply send the command "R . (message number) " ,
where (message number)
is the number of the message you are interested in.
After you are finished reading messages, you may want to SEND a
messqge to the SYSOP (short for System Operator) or to another user.
To send a message simply enter "S (callsign) 't where (callsign) is the
call of the station you are sending the message to.
When you are finished Listing, Reading and Sending messages, you will
want to send the Bye command to log-off (disconnect) from the Mailbox.
Feel free to experiment with Mailboxes and other packet systems .
Remember that most automatic systems will send you help on comrnandg if
For more information on setting up and using
you send an "H" or " ? "
your own DSP-2232 MailDrop, see Chapter 5 on MailDrop Operation.
Packet Switches and "Nodes "
When Amateur Packet radio was first beginning there were not many
stations on the air. Amateurs at that time "digipeated" through many
stations (up to 8 ) to connect to others over long distances. As more
users became active on packet, digipeating quickly proved to be an
inefficient way of relaying packets through even a very few stations.
To solve this problem, PJnateurs began working on more efficient
" higher level" ways of routing packets over long distances.
NET/ROM (tm), ROSE, TCP/IP and TEXNET are some of the higher level
protocols that emerged and are currently in use around the world.

### (PDF p.55)

-->
NET/ ROM. developed by Software 2000, quickly became a standard that
others imitated. Many networking "Nodes" today use a similar if not
identical set of commands. We will discuss the typical NET/ ROM
commands you will likely encounter when connecting to a packet switch.
When you connect to a NET/ ROM Node you will not initially get any
Since NET/ ROM commands are few and easily memorized, they did
prompt .
not see a need to clutter the channel with prompts.
Like other
automatic systems however, if you send an "H" or a for Help you
can expect to get a "Help" response similar to the following:
Invalid command (CONNECT INFO NODES ROUTES USERS)
In our example, the line above is from the Seattle node, g imply known
as SEA. The callsign for the node is N7HWD-8.
" Invalid Command"
means that the node did not understand the command you sent, so it
returned the above "help" line to remind the user of the commands it
knows. These are CONNECT, INFO, NODES, ROUTES and USERS.
Most often you will use the nodes CONNECT command to connect to other
stations. Once you have connected to the node, simply send the
command "CONNECT (callsign) " or simply "C (callsign) where (callsign)
is the call of the packet station you want to connect to that is in
range of the node.
Not everyone you want to talk to is in range of your local Node.
Fortunately, NET/ ROM will learn about other nodes it can reach and
allow you to connect to these nodes as well. To f ind out what other
nodes your local station can reach, simply type the command "NODES"
after you connect.
This will display something like the following:
Nodes:
BALDY : WB6VAC-8
ELN : N7HHU-8
MSO: W7DVK-5
PTN:K7TPN-8
SPOKN : WB7NNF-8
BOI : W7SC
EVT : KA7VEE-8
OLY : K 7 APT-8
RLIMB : WORL1-2
SVBBS : KA7RNX
BOISE:N7FYZ-8
LSO:K7ZVV-8
PDT:N7ERT-5
SALEM:AF7S-1
TAC:W7DK-8
COE : KK7X-4
MCW:WB7DOW-12
PDX7 : KA7AGH-8
SEAW: N8GNJ-8
YKM: K3GPJ-8
When you connect to a node (either directly or through another
node) you may want to know who else is using that particular node.
Type the cornmand "USERS" to f ind out who is using the system. You
will see your own call in the list as well as anyone else who is using
the node. An example is shown below:
NET/ROM Version 1.3 (662)
Uplink(W7MCU)
KA7RZK)
Uplink( "your callsign")
Downlink(W7MCU-15 WA7ZUE)
The IDENT command simply sends you an identification packet from the
node that may give its location and owner as shown below:
SEA: N7HWD-8> NORTHWEST AMATEUR PACKET RADIO ASSOCIATION
145.01 MHZ, USER LANt GRASS MTN.
Local BBS is N7HFZ

### (PDF p.56)

The ROUTES command provides routing information about other nodes that
can be reached.
A complete discussion of NET/ ROM is beyond the scope of this manual,
but we hope the above information will help get you started.
Certainly the CONNECT, NODES and USERS conunands will allow you to
navigate through the network, and find new people to talk to.
Who can 1 Talk To?
Now that you understand a little about the dif ferent packets and
packet stations, you are ready to make your first real connection.
If you do not have a friend on Packet in your local area, then you
will want to choose a station you can reach. Fortunately the DSP-2232
has a command called MHEARD that displays the list of the 18 most
recently heard stations. Check this 1 ist in one of the following ways:
1
11
If you are using an AEA PAKRATT program, follow the instructions
in the program manual for checking the Packet MHEARD list.
If you are using a Terminal • or Terminal Program on your computer,
then first type a <CTRL-C> to make sure you are in the DSP-2232
Command ( cmd:) mode. Then type the command MHEARD as shown.
You should then see a display similar to the one below.
cmd : MHeard
pl
pl
pl
pl
pl
pl
pl
N7GMF
K6RPK
SEA*
N7HWD-8*
KD7NM*
N7ALW*
WA7GC1*
cmd :
The callsigns in the
list are the stations heard by your DSP-2232 with
the most recently heard station at the top of the list. As in the
Monitored packets, the asterisks ( * ) indicate that the station was
heard directly by the DSP-2232. The callsigns without an asterisk
were relayed by another station and so cannot be connected to

#### 4.3.5

directly .
Your First
Choose one
list, or a
If you are
CONNECT in
If you are
The "pl" means the station was heard on radio port 1.
Real Connect
of the stations with an asterisk displayed in YOUR MHEARD
friend that you know is "on the air" near to you.
using an AEA PAKRATT program, follow the instruct ions to
Packet mode to the callsign you chose above.
using a Computer Terminal or a non-AEA program, entering
the following after the "cmd: "
cornmand mode prompt will cause the
DSP-2232 to Connect to the station (Callsign) chosen above:
CONNECT (Cal 1 sign)

### (PDF p.57)

#### 4.3.6

After pressing you should observe the SEND LED light.
Your monitor should soon display:
*** CONNECTED to (Callsign)
If you see this, you have just Connected to your first packet station.
Identify what type of station you have connected to, and respond
appropriately. After you have connected to a few stations, you should
skip to section 4.4 to learn more about the DSP-2232 packet features.
I 'm Havinq Trouble Connecting
If the station you are trying to connect to is connected to someone
else, you may see the following message:
*** BUSY from (Callsign) DISCONNECTED
If you see this, simply wait a few minutes and try again or try
connecting to a different station from your MHEARD list.
If the distant station cannot hear you, you may see the following:
*** Retry count exceeded
* DISCONNECTED :
A number of different things can cause this to occur.
It may simply
be that the station you are trying to connect to is out of your
transmitter's range.
It is possible however that something more
serious is wrong, so you should check the following before proceeding:
The Loopback Test in section 4.2 functions properly.
Your DSP-2232 's AFSK Output Level control, microphone gain, and
deviation are set properly as discussed in Section 3.5.1.
All cables and connectors are properly installed.
Your radio's volume and squelch are set for local conditions.
You are following the correct procedure for Connecting.
Remember that this procedure is slightly different for AEA
PAKRATT programs than it is for terminals or terminal-programs .
The "VHF" cornrnand is "ON" for VHF/UHF operation.
RESET the DSP-2232 with the RESET cormand or RESET switch and
start over with section 4.2 of this chapter.
If none of the above correct the problem, ask one of your area' s
experienced packet operators to listen to your transmissions.
Both
you and your partner should set MONITOR and MCON to 6, and then send
Each station should display packets sent by the other.
some packets .
If only one station is "hearing" packets, check the modulator and
transmitter of that station and the demodulator and receiver of
the other station.
Experiment with the TXDELAY parameter for the sending TNC. Try
If this solves the problem,
setting TXDELAY 64 for a long delay.
decrease TXDELAY to the smallest value that works all the time.
If you still cannot connect to other stations, then you should contact
AEA Technical Support as outlined in the PREFACE of this manual.

### (PDF p.58)

### 4.4

#### 4.4.1

1 •p / 91
More Packet Features
Now that you have worked a few packet gtationg, it is time to learn a
little more about the other packet capabilitieg of the DSP-2232.
Rather than explain all the features in detail, we will leave the
specif icg to the command degcript ions in the Cornmand Summary Appendix .
LCD Status and Mode Indicators
Your DSP-2232'g front panel is broken into three parts ag Bhown below:
DSP-2232
DSP
OtGITA-
TUNE
MULTI-MOOE DATA CONTROLLER
DSP-2232' s Front Panel Indicators.
On the left is the LCD STATUS indi.cator which displays the Status of
Radio Port 1 on the top line, and Radio Port 2 on the second line.
In the middle of the front panel are the two LED bar-graph TUNING
Indicators. The top indicator aids in tuning gignalg on Radio Port 1 ,
and the bottom bar-graph is a Tuning aid for Radio Port 2.
On the right are the LED Status Indicators for both Radio Ports
The
top row of eight LEDg indicates the Status for Radio Port I and the
lower row indicates Status for Radio Port 2.
Your DSP-2232's front-panel LEDs show the Status of each Radio Port at
a glance. Each LED is marked with an abbreviated name. The following
describes the function of each of the front-panel LEDB.
NAME
DCD
SEND
MULT
STA
TRANS
CONV
CON
CMD
DESCRIPTION
LED FUNCTION
Data Carrier Detect Lit when data signa 1B are received
Send
Mult iple
Status
Trangparent
Converge
Connected
Comma nd
Lit when PTT line is active
Lit when multiple connections exigt
Blinkg when receive buffer is full
Lit when you have gent a packet
that has not yet been acknowledged
BI inks when you have MailDrop meggages
Lit when in the Trangparent Mode
Lit when in the Converge Mode
Lit when a packet Connection exist g
Lit when in the Command Mode

### (PDF p.59)

#### 4.4.2

#### 4.4.3

#### 4.4.4

Automatic Greetings
You can tell your DSP-2232 to send an automatic greeting (CTEXT) to
any station that connects to you. This can be used to tell others
that you are out of the shack and to leave you a message or for any
other message you would like to send.
To enable the CTEXT message, set your Connect Text message using the
CTEXT command. Then set CMSG ON to enable the Connect Message
feature. Think of the CTEXT message as the message your telephone
answering machine might give to a caller.
Beacon Operation
Your DSP-2232 can send an automatic "beacon" message at a specified
time interval . A beacon can send special announcements, or let others
know you are on the air. To enable beacon operation do the following:
Set your beacon message with the BTEXT command.
Set the beacon interval using the BEACON EVERY or AFTER command.
A beacon frame is sent to the path given in the UNPROTO command.
In the early days of packet, the beacon was useful to show your
presence on the packet channel. With the growth
feel that beacons have outlived their usefulness
traffic.
Use your beacon with consideration for
As a reminder, if you set the BEACON timing at a
small for busy channels (less than "90"), you ' 11
WARNING: BEACON too often
Digipeater Details
You may wish to connect to a packet station that
of packet, many users
and interfere with
others .
value considered too
see :
is beyond your direct
radio range. If a third packet station is on the air and both you and
the station you want to talk to are in range of that third station,
the third station can relav or "digipeat" your packets.
You g imply
set the "digipeater" routing when you connect.
shows how digipeating can solve problems:
WX2BBB
WXIAAA
Here's a sketch that
WX3ccc
You are station WXIAAA - you want to have a packet QSO with WX3CCC.
There is a mountain between you and WX3CCC; you ' re out of simplex
range of each other. However, you know that there' s a packet station
located on the ridge WX2BBB - which is in ranqe of you and WX3CCC.
Instruct your DSP-2232 to set up a connection to WX3CCC using WX2BBB
as an intermediate digipeater. When you initiate the Connect, type:
"CONNECT WX3ccc VIA WX2BBB" .
If WX2BBB has turned off his station, you can still contact WX3CCC by
going around the ridge through WX2DDD and WX2EEE as shown:

### (PDF p.60)

WXIAAA
WX2DDD
WX3ccc
WX2EEE

##### 4.4.4.1

#### 4.4.5

This time, type the connect command like this:
CONNECT WX3ccc VIA WX2DDD, WX2EEE
Type the digipeaters' call signs in the exact order of the intended
path from your station to the station with which you wish to connect.
You can specify a routing list of up to eight intermediate stations.
In practice this does not work very well, and Networking Switches such
as NET/ ROM have replaced digipeating for the most part.
Still, it is
sometimes necessary to digipeat through one or two stations.
Are You biqipeater?
Your packet station can be a digipeater for other stations.
You don't
have to "do" anything
your DSP-2232 will digipeat other stations -
unless vou tell it not to! with the DFROM command.
If your transmitter is keyed when you 're not using it, or during lulls
in your own conversations, you • re being used as a digipeater by some
other stations. This won't bother your chat with your partner.
If you wish to monitor the other stations that are using you as a
digipeater then set the command MDIGI ON.
Monitor inq Other Stations
Use the MONITOR command to determine what kinds of packets you will
"MONITOR" takes a
see when you are NOT connected to another station .
numerical value between "O" and "6. "
Each higher number adds more
detail to your monitoring. The meanings of the MONITOR numbers are:
1
2
3
4
5
6
Monitoring is disabled.
Only unnumbered, "unconnected" frames are displayed. This
setting will display Beacons, but not display connected stations.
Numbered ( I) frames are also displayed. Use this setting to
monitor connected conversations in progress on the channel.
Connect request ( frames and disconnect ( "D") request packets
in addition to the above are displayed.
This is your DSP-2232's default value.
Unnumbered aeknowledgment
(UA) of connect and disconnect frames are also displayed.
Receiver Ready (RR), Receiver Not Ready (RN), Reject (RJ), and
Frame Reject (F R) supervisory frames are also displayed.
Poll/ Final bit and sequence
numbers of monitored frames are shown.

### (PDF p.61)

Understanding all types of packet frames is not necessary to operate
packet .
Packet operators should however understand that there are
many types of control frames that do not contain printable data.
Your DSP-2232 can display these frames, but most users only want to
For this reason, the MONITOR command
see frames with information.
default (4) does not display all the packets that the DSP-2232 hears.
NOTE :
If you will be leaving your DSP-2232 on to accept connects
from others while your computer is off, set MONITOR to O
(zero) and type a <CTRL-S> to hold the data.
If you are using an AEA program, you must set the MONITOR
command to O, but the our programs automatically perform the
<CTRL-S> function for you .

##### 4.4.5.1

##### 4.4.5.2

##### 4.4.5.3

Monitoring the Packet Networking Switches
There are other types of AX. 25 frames used by networking switches that
the DSP-2232 does not normally display. These other frames can be
seen by turning the MPROTO command ON. Some of the packets monitored
with MPROTO ON will contain information that may interfere with the
screen on your terminal or computer causing it to look "funny"
For
this reason the MPROTO command default is OFF.
If you are hearing packets that sound strong but are not displayed,
setting MONITOR to 6 and MPROTO ON should show them. If you are
curious about the packets that do not print, you may find the cornmand
WHYNOT useful. When WHYNOT is turned ON, the DSP-2232 will give a
reason why each packet was not displayed. If you are interested in
exactly how the packets are represented, turn on the TRACE command.
See the Command Summary for more information about WHYNOT and TRACE.
Monitor inq Other Stations While Connected
When you are NOT connected to another station, the MONITOR command
discussed above determines what packets are displayed. When you ARE
connected, the MCON command determines what packets are shown .
The default of MCON is O which tells the DSP-2232 NOT to monitor any
packets while you are connected. Most users like. t.his so they are not
disturbed with monitored channel data when they are communicating with
If it is desired to monitor channel activity while
another station.
you are connected, then remember to set MCON to an appropriate Monitor
number from the list above or the command summary.
Selective Monitor inq
After you have monitored channel activity for a while, you may decide
there are only a few stations you wish to display. The DSP-2232 will
let you do this with the Monitor-TO (MTO) and Monitor-FROM (MFROM)
commands. With the MBELL command, you can even be alerted when a
certain station transmits on the frequency. These commands work in
conjunction with MONITOR and MCON commands.

### (PDF p.62)

##### 4.4.5.4

##### 4.4.5.5

##### 4.4.5.6

#### 4.4.6

##### 4.4.6.1

##### 4.4.6.2

The MPILTER Conunand
Some terminals and computer programs are sensitive to certain
characters that may appear in monitored packets .
You will know this
is happening if occasionally the cursor on your screen moves to
strange places causing the copy to be garbled.
The DSP-2232 default for MFILTER is $80 which prevents most control
If you find a terminal
characters from interfering with your display.
or printer is bothered by certain characters, see the Command Summary
for more information on the .MFILTER command.
Monitor Without Callsign Headers
Sometimes you may wish to monitor certain stations without wanting to
look at the packet callsign headers. This can be useful when
monitoring message traffic from a large Packet Bulletin Board System
The MBX command allows you to choose the callsign of a
(PBBS).
station, or a pair of stations you wish to monitor without seeing the
packet headers.
See the Command Summary for details.
MSTAMP The Monitor Time-Stamp Command
Monitored packets can be time-stamped if the real-time clock has been
set with the DAYTIME tommand. To timestamp monitored packets, turn
the MSTAMP command ON. Turning the DAYSTAMP command ON adds the date
to the timestamp provided by the MSTAMP command.
Packet Connects
When you turn your DSP-2232 on and enter your callsign, anyone can
If you are at your terminal or computer when this
Connect to . you.
occurs you will see a message like the one shown below:
CONNECTED to N7GMF
When a packet connection occurs, the DSP-2232 automatically switches
to the Converse mode so what you type on the keyboard will be sent to
the connected station. The NEWMODE and NOMODE commands control when
and how the DSP-2232 changes to and from Command mode in response to
You will probably never need to
packet connects and disconnects.
change these settings.
Time-Stampinq Connects
Sometimes it is useful to know what time someone connected to you
perhaps for logging. To t ime-stamp your connects and disconnects turn
the command CONSTAMP ON. As discussed in the Monitoring section
above, turning the command DAYSTAMP ON adds the date to this as well.
The DAYTIME command must first be get for this to operate.
Connect Alarm
If you busy doing other things, you may want to be alerted when
someone connects to you. Turning the command CBELL ON r ings the bell
on your terminal when a station connects to, or disconnects from you.

### (PDF p.63)

#### 4.4.7

##### 4.4.7.1

##### 4.4.7.2

##### 4.4.7.3

##### 4.4.7.4

Packet Formatting and Edit inq
Some of your DSP-2232 's command parameters affect how your packets are
formatted
- how your typing appears to the rest of the world. Other
commands let you correct typing errors before your packet is sent,
cancel lines or cancel packets if necessary.
Carriage Returns and Linefeeds in Packets
Most people use packet radio for sending and receiving messages or
conversing with other Amateurs. The character used to gend a packet
is defined with the command SENDPAC which defaults to a Carriage
Return ($OD).
The SENDPAC character may be changed, but most will
find the Carriage Return or Enter key to be a natural choice.
Similarly, your DSP-2232 will include a Carriage Return in the packet
you send to other stations since this makes for a more natural
conversation . The ACRPACK command ( default ON) controls this feature,
but most people never want to change this.
The DSP-2232 also has the capability of adding a linefeed character
( $0A) automatically to packets that you send to others.
If you
encounter a station that says your packets are overprinting, you may
want to turn the ALFPACK or the ILFPACK command ON temporarily.
Cancel inq Lines and Packets
Most of the time, the Backspace key (or the Delete key on some
computers) is all that is needed to edit a line before it is sent.
Occasionally it may be helpful to cancel the line, or the entire
packet you are entering with one key stroke. The CANLINE character
(default <CTRL-X>) will cancel the entire line you are typing. The
CANPAC character (default <CTRL-Y>) will delete the entire packet you
are entering. These commands can be helpful, but use them with care.
Redisplay
If you have erased and retyped many characters, you may want to see
the sentence you are currently entering "redisplayed" by the DSP-2232,
especially if BKONDEL is OFF. Your •DSP-2232 will show the line you ' re
entering when you type the REDISPLAY character (default <CTRL-R>) .
This will also allow you to display any packets you might have
received while you were typing.
The PASS Character
If you are using a terminal or terminal program, the following may be
useful.
Sometimes you may want to include a special input character
such as a Carriage Return (the SENDPAC character) in a packet.
For
example, to send several lines in the same packet, you must include
<CR> at the end of each line. You can include any character in a
packet (including all special characters) by prefixing that character
with the PASS character (default <CTRL-V>) :
I wasn't at the meeting.
What happened?

### (PDF p.64)

Without the PASS character, this message would
By prefixing the first <CR> with <CTRL-V>, you
while maintaining the <CR> as part of the text.
can be useful in formatting text Messages such
Packet Transmit Timinq
go out as two packets.
send it all at once,
The PASS character
as CTEXT as well.

#### 4.4.8

##### 4.4.8.1

Your DSP-2232 has a number of built-in timers used to control the
packet protocol and transmit timing. The default values have been set
at the factory to provide reasonable performance, but the values may
not be opt imum for your local area. Most protocol parameters should
be adjusted only after carefully reading about them later in the
You SHOULD however adjust TXDELAY for your transmitter as
chapter .
indicated below.
TXDELAY and AUDELAY
Radios vary in the time it takes to switch from receive to transmit.
If your DSP-2232 starts sending data before your transmitter is up to
power, the packet will not be received properly at the distant end.
TXDELAY controls the delay between your transmitter's key-up and the
The default value
moment when your DSP-2232 starts sending data.
of • 30 corresponds to a time of 300 mSec and works with most VHF/UHF FM
transceivers. With dodern transceivers TXDELAY can often be reduced
which will improve packet performance in your area. You should
perform the following procedure to optimize TXDELAY for your station.
Find another station who can reliably digipeat your signals.
Set your UNPROTO path to TEST via the callsign of the station who
can digipeat your signals.
Set the MONITOR command to at least 1.
Go to CONVERSE mode and send a few packets by pressing the
<Enter> key. Note that you should see them on your own screen
when they are digipeated by the other station.
Start reducing TXDELAY by units of 5 each time making sure the
other station is still digipeating ALL your UNPROTO packets.
Eventually you will find a value where the other station can no
longer copy your packets to digipeat them.
When this happens, increase TXDELAY in units of one or two until
the other station again digipeats ALL of your packets. This will
be the optimum setting of TXDELAY .
After TXDELAY is adjusted as indicated above you may want to adjust
the audio delay (AUDELAY) as indicated in the Command Summary.
The next sections of this chapter will discuss some of the more
advanced packet features including Multiple Connects, Packet Timing
and Protocol, and HF Packet Operation.

### (PDF p.65)

##### 4.4.8.2

### 4.5

4.5. I

#### 4.5.2

12,/91
AXDELAY and AXHANG
Although it is not common, packet can be used through voice repeaterg.
When sending packets through an audio repeater you may require a
longer key-up delay than is normally needed for direct communications.
The AXDELAY command adds more key-up delay in your DSP-2232 so that
the repeater can stabilize. The AXHANG command sets the time your
DSP-2232 assumes is needed for the repeater to drop.
Packet Protocol Bagicg
Here we will talk a little about the AX. 25 packet protocol. You do
not need to know the protocol to use packet, but it helps in
understanding the protocol parameters .
There are two modes of packet transmissions, Connected mode and
Unconnected mode. Usually you will converse with another packet
station in Connected mode. Still, the Unconnected or Unprotocol mode
comes in handy for beacon transmissions and roundtable conversations.
All packets have basically the same construction.
Packetg contain
source and destination callsigns (and any digipeaters if used), as
well as information identifying the type of packet. This packet
identification can be• seen with the MONITOR command discussed earlier.
All packets contaih an error check code called the CRC. This
virtually ensures that when a packet is received, it will not contain
a single error. The command PASSALL can disable the CRC error check,
but this should only be done for experimental purposes.
Unconnected Packets
In order to allow amateurs to send message beacons and to call CQ, the
AX. 25 protocol has the ability to send packets that are intended for
more than one specific packet station to see.
Since all packets must
have a destination "callsign", the DSP-2232 sends Unprotocol packets
TO the callsign of CQ. This can be changed with the UNPROTO command,
but most people like this since it makes calling CQ easy.
Connected Packets
When you Connect to another station, the AX. 25 packet protocol ensures
that the station to whom you are connected receives all the packets
that you send. Similarly, the protocol ensures you will receive all
the packets that the other station sends to you. The following
describes briefly how the protocol does this.
FRACK and RETRY
When the DSP-2232 sends a packet to a Connected station, it expects an
acknowledgment (ACK) packet from the other station to confirm that the
packet was received. The AX. 25 packet protocol will automatically
retransmit (Retry) packets when an acknowledgment is not received from
the distant end of •the link within a specified time.
The FRACK command (FRame ACKnow1edge time) sets the time lapse allowed
before the originating station retransmits (retries) the packet.

### (PDF p.66)

4.5 5
The RETRY command sets the maximum number of retransmissions before
the sending station terminates the connection (DISCONNECTS) .
The TRIES counter keeps track of the retries that have occurred on the
current packet.
PACLEN and MAXPRAME
Packets will be sent either when the <Enter> key is pressed or when
the maximum packet size is exceeded. The maximum packet size is set
by the PACLEN command which defaults to 128 characters. When large
amounts of data need to be sent, this value can be increased to 256.
When conditions are poor or the channel is crowded as on HP packet,
this value should be reduced to 64 or less.
The packet protocol allows more than one frame to be sent in a single
transmission. The default is set to 4 by the MAXFRAME command. When
conditions are good up to 7 frames can be sent to speed data transfer.
When conditions are poor or the channel is crowded, MAXFRÄME should be
reduced to only 1 frame.
Reducina Errors through Collision Avoidance
If every packet station could hear every other station, there would be
very few "collisions" due to stations transmitting at the same time.
Since packet operates • over radio, there are often many stations on the
same frequency that cannot hear each other. Digipeaters and network
nodes allow these stations to communicate with each other, but this
increases the chances of collisions.
The first attempt to avoid collisions was through the use of the DWAIT
and RESPTIME timers.
DWAIT forced the TNC to delay the transmission
of any packet except for digipeated frames by the time selected. This
fixed timer helped, but packet was still plagued by collisions. The
RESPTIME was added to help with large file transfers.
Still, more
needed to be done to reduce collisions.
Another attempt to reduce collisions was the introduction of AX. 25
version 2 protocol. On VHF packet, most everyone uses version 2 which
is controlled by the AX25L2V2 command ( default ON). On VHF this
helps, but some users on HF packet are turning this command OFF.
An exponentially distributed random wait method was proposed by Phil
Karn ( KA9Q) called P-persistent CSMA. When the cornmand PPERSIST is ON
(default) the DSP-2232 uses the number set in PERSIST and the time
value set by the SLOTTIME command to more randomly distribute the
transmit wait time. This is more efficient than using the DWAIT time.
As a further attempt to improve packet performance, Eric Gustafson
(N7CL) proposed giving priority to acknowledgment packets (ACKs) .
This protocol is controlled by the ACKPRIOR command which currently
defaults OFF. Check with experienced packet users in your area and
find out if they are using priority acknowledge or have changed any
other packet parameters .
-22

### (PDF p.67)

CHECK and RELINK

### 4.6

4.6. I

#### 4.6.2

If someone connects to you and then turns his TNC off, you would
probably not want to stay connected to the station forever. The CHECK
timer determines the amount of time the DSP-2232 will wait before
testing the link if no data has been sent or received.
The RELINK command sets what happens when the CHECK timer expires.
If
If ON,
RELINK is OFF, the DSP-2232 changes to the Disconnected state.
the DSP-2232 attempts to reconnect to the other station.
Multiple Connection Operation
Since packet radio allows many stations to share the same channel,
many QSOs can be going on at the same time. Because packet has this
channel sharing capability, there is no reason you cannot connect to
more than one station at the same time. Being connected to multiple
stations at once is a powerful feature of your DSP-2232.
Multiple Connection Descript ion
The DSP-2232 offers 10 logical packet channels on Radio Port 1 and 26
logical packet channels when using Radio Port 2.
Each logical channel
can support a connection with another packet station. So with the
DSP-2232, you may be connected to a total of 36 other packet stations
s imultaneously.
Multiple connect operation is much like a multi-line telephone with
automatic hold. When you are connected to multiple stations you will
automatically receive everything sent TO you. You must select the
proper channel (in effect push the proper line button on the
telephone) to send data to a particular station.
If you are using an AEA PAKRATT program, this is described in the
If you are using a terminal, the rest of this section
program manual.
will describe how to set up the DSP-2232 for multiple connections.
The Channel Switchinq Character
The logical channels are selected with the CHSWITCH character. You
must choose a CHSWITCH character that you do not normally type such as
the• vertical bar
(ASCII $7C), or the tilde
(ASCII $7E). once
this has been selected and entered into the DSP-2232, you may initiate
multiple connections with others on your radio channel.
The 26
The ten logical channels on Radio Port 1 are numbered 0-9.
logical channels on Radio Port 2 are labeled A-Z. After the CHSWITCH
character has been selected, you can now initiate connects on any of
the logical channels numbered zero through nine (0-9) on Port I or "A"
through "Z" on Port 2.
To change logical channels on Radio Port 1, press the CHSWITCH
character you just defined, and then a number from 0-9. Remember that
the text that you type will only be sent out tb the station connected
to the logical channel your DSP-2232 is currently on.

### (PDF p.68)

#### 4.6.3

#### 4.6.4

#### 4.6.5

#### 4.6.6

#### 4.6.7

### 4.7

.2/91
This same procedure is used to select a logical channel on Radio
Port 2, only instead of pressing a channel "number" from 0-9, a
" letter" from A-Z is pressed. This is in fact the method used to
switch back and forth between Radio Ports 1 and 2.
Please see the
section on switching between Radio Ports later in this chapter for a
more complete description of this process.
Will You Accept Multiple Connects
Setting the CHSWITCH character only allows you to make outgoing
multiple connects.
For the DSP-2232 to allow multiple incoming
connections, you must set the USERS parameter to more than one (1) for
each Port that you wish to allow incoming multiple connections .
The number you enter in the USERS command tells the DSP-2232 how many
other users you will allow to connect to you at one time.
Displav Multiple Connected Callsiqns
Multiple Connection operation can be confusing - especially
remembering who is connected on what channel. To help this, you may
want to turn ON the CHCALL command to display the callsign of the
station who is connected to you on a given channel.
Doubling Received CHSWITCH Characters
If you want to be able to tell the difference between the CHSWITCH
characters you type, and characters from other stations that happen to
be the same as your CHSWITCH character, then set CHDOUBLE ON .
Check inq Your Connect Status with the CSTATUS Command
To check what channels your DSP-2232 is currently set tot as well as
who is connected to you, you may find the CSTATUS command helpful.
CSTATUS is an imxnediate command that shows you the status of all
packet channels as well as the channel you are currently on.
The MULT LED
You will know you are connected to more than one packet station on
each Radio Port when the MULT LED for that port on the front panel of
the DSP-2232 lights.
NOTE :
The MULT LED will blink if the DSP-2232's receive buffer is
filled. This can happen if your computer is not connected to
the DSP-2232 and the MONITOR command was left ON, or if for
some reason, your communications program no longer can accept
any further inbound data.
HF Packet Operat ion
In this section we
HF Packet is much trickier than operating on VHF-
will assume you have as completed section 4.2 of this chapter and at
If at all
least read section 4.3 and the MONITORING sections of 4.4.
possible, get some experience with VHF packet before trying packet on
Although this is not absolutely required, the experience will
help you make HF packet contacts.

### (PDF p.69)

#### 4.7.1

#### 4.7.2

#### 4.7.3

Where to Operate HF Packet
Before you can operate HF Packet, you must first find the activity.
Most HF packet operation is on the 20-meter amateur band starting at
14. 103 MHz and every 2 kHz above that up to 14.111 MHz.
Note that
14. 103 MHz is the HF Packet calling frequency and a good place to
start. The higher frequencies such as 14.109 and 14. 111 are used
mostly by HF PBBS systems and are not good places to look for a QSO.
DSP-2232 HF Packet Settings
Radio Port I on the DSP-2232 is intended for multi-mode operation go
either a VHF or HF transceiver may be connected. The packet
parameter defaults have been set for VHF Packet operation on both
radio ports and should be changed for HF packet operation as shown
be ow.
The table below shows the parameters that should be set differently
for HF and VHF packet operation.
If you will be operating HF Packet,
you should make note of these parameters and change them accordingly.
Recommended 300 baud HF Packet vs port 1 & 2 defaults for 1200 baud VHF (PDF p.69). OCR listed the two columns interleaved; reconstructed:

| Parameter | 300 baud HF Packet | 1200 baud VHF (defaults) |
|---|---|---|
| SLOTTIME | 12 | 30 |
| PACLEN | 64 or less | 128 |
| MAXFRAME | 1 | 4 |
| FRACK | 8 | 5 |
| VHF | OFF | ON |
| HBAUD | 300 | 1200 |
| MODEM | 10 | 12 |
Note that the HF (VHF OFF) modem number is tied to the QHPACKET
command and the VHF (VHF ON) modem number is tied to QVPACKET.
The Radio Port baud rate HBAUD is tied to the Modem number selected.
When HF Packet operation is selected by turning VHF OFF for the Radio
Port in use, the Modem and baud rate (HBAUD) for that port are
automatically selected as well. The modem selected for HF operation
may be set with the QHPACKET command. This is handy s ince it allows
both the modem and the HBAUD rate to be changed with a single command.
You should set MONITOR to 6 on the port used for HF Packet when tuning
in your first HF Packet stations.
HF Receiver Settings
Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your DSP-2232 through the direct FSK keying lines, in
which case you should select the PSK or RTTY operating mode. Adjust
the volume to a comfortable listening level.

### (PDF p.70)

#### 4.7.4

Tuning in HF Packet Stations
Perhaps the most difficult thing about HF Packet operation is making
sure the station is tuned properly and stays tuned. Since HF packet
uses 200 Hz Frequency Shift Keying to send data (2110/2310 Hz), tuning
Being off frequency by only 20 Hz can
accuracy is very important.
make a noticeable difference in the DSP-2232's ability to copy packet
stations.
Follow the tuning procedure below carefully for the best
results in tuning in HP packet stations.
Make certain your HP receiver is either in LSB or FSK depending
on your DSP-2232 set-up.
Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.
Tune your receiver to 14. 103 MHz (or another frequency where you
know there is HF packet activity) and listen to the packets.
Slowly vary the tuning knob on your receiver and look for a
display on the DSP-2232 tuning indicator like the one below.
If the tuning indicator looks like the one
frequency from your speaker is too low for
packets .
Slowly tune the VFO and make the
If the tuning indicator looks like the one
Tuned In
below, the audio
the DSP-2232 to copy
frequency higher.
Frequency
Too Low
below, the audio
frequency from your speaker is too high for the DSP-2232 to copy
Slowly tune the VFO and make the frequency lower.
packets .
Frequency
Too High
Adjust the volume on the receiver so that the DCD LED lights when
a properly tuned packet is being received.
You must also make certain that the DCD LED goes out when no
packet signals are present on the frequency.
After you have a packet station tuned in, you should start seeing
HF packet stations on your display.

### (PDF p.71)

4.7 .5

#### 4.7.6

Transmitter Adjustments
Make sure your DSP-2232 is adjusted for your SSB transmitter ag
described in section 3.5 and 3. 5.2 of this manual before transmitting.
These are very critical adjustments.
If the AFSK level and
transmitter microphone gain are hot adjusted properly, other stations
will not be able to copy your packets. Check your plate or collector
current or the power output of your rig before going on the air.
Going On The Air
Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using.
On HF there are two ways you can go about talking to another station.
First, you can look at the packets you have just MONITORED
(or in your MHEARD list) and choose one of them to connect to.
You can also "Call CQ" by entering the CONVERSE mode and pressing
the <Enter> key a few times.
Either way you decide to go on the air, remember that things happen
HF packet
much more slowly on HF packet than they do on VHF packet.
requires patience and careful tuning in order to be used successfully.
If you are having problems connecting to other HF packet stations, try
working with an experienced HF packet operator in your area and listen
to each other's signals.
See if you can copy each other's packet
signals.
If he cannot copy your signals, have him listen to your
signal in the CALIBRATE mode to make sure you are transmitting a pure
tone. As mentioned earlier, any distortion caused by overmodulation
or RF feedback will make your signal difficult or impossible to copy.
Controlling the Radio Ports
If you are using an AEA PAKRATT program designed for the DSP-2232,
switching between Radio Ports is described in the program manual.
If you are using a computer terminal, terminal program or the
"Dumb Terminal Mode" of an older PAKRÄTT program, this section will
describe how to control and switch between the radio ports. The
RADIO command allows either (or both) Radio Port (s) to be disabled.
Before the second radio port can be used for packet operation, a
modem must be loaded that can access the second port. There are two
types of modems available for radio port 2 .
radio port 1 when in use and is designated
be low.
20:
23:
28:
60:
p2
p2
p2
p2
Packet
Packet
Packet
Packet
300 bps HF 2110/2310
1200 bps PACSAT
9600 bps FSK K9NG/G3RUH
1200 bps MSK
22:
25:
61:
The first type
These are
" p2 " .
p2 Packet 1200
p2 Packet 2400
p2 Packet 2400
disables
listed
bps VHF
bps V. 26B
bps MSK

### (PDF p.72)

operation on radio port 1 and radio port 2 at the same time. The
dual port modems available in the DSP-2232 are listed below.

#### 4.8.1

The second type of modem is "Dual Ported" ,
2:
4:
that is allows for
30:
31:
33:
35:
RTTY/TOR 170: 2125/2295; p2 Packet 300 bps HF 2110/2310
RTTY/TOR 170: 2125/2295; p2 Packet 1200 bps VHF
pl Packet 300 bps HP 2110/2310; p2 Packet 1200 bps VHF
pl Packet 1200 bps VHF; p2 Packet 1200 bps VHF
Selecting and Loading Modems
The various modems available in the DSP-2232 can be seen with the
DIRECT(ory) command. To display all the available modems simply
enter the Command Mode of the DSP-2232 and then type DIR as shown.
DIR
The DSP-2232 will respond with the fol lowing:
(920716)
1:
3:
10:
12:
14:
16:
18:
20:
22:
25:
28:
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
RTTY/TOR 170: 2125/2295
RTTY /TOR 425: 2125/2550
pl
pl
pl
pl
pl
p2
p2
p2
p2
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
Packet
300 bp.s HF 2110/2310
1200 bps VHF
1200 bps PSK
4800 bps PACSAT
II:
13:
15:
17:
9600 bps FSK K9NG/G3RUH
300 bps HF 2110/2310
1200 bps VHF
2400 bps V. 26B
23:
RTTY/TOR 170: 1445/1275
RTTY /TOR 850: 2125/2975
pl Packet 300 bps HF 1460/1260
pl Packet 1200 bps PACSAT
pl Packet 2400 bps V. 26B
pl Packet 4800 bps PSK
p2 Packet 1200 bps PACSAT
9600 bps FSK K9NG/G3RUH
RTTY/TOR 170: 2125/2295; p2 Packet 300 bps HF 2110/2310
RTTY/TOR 170: 2125/2295; p2 Packet 1200 bps VHF
pl Packet 300 bps HF 2110/2310; p2 Packet 1200 bps VHF
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
RTTY /TOR 1200 ASCII OSCAR-II
pl Packet 1200 bps MSK
pl Packet 9600 G3RUH U022 eq
p2 Packet 2400 bps MSK
Any modem from this list may be loaded with the MODEM command.
For example, let's say that you want to operate 300 bps HF packet on
radio port 1 and 1200 bps VHF packet on radio port 2 This operation
is achieved with modem 33. To load modem 33, f irst enter the Command
Mode of the DSP-2232 and then type MODEM 33 as shown below:
MODEM 33
The DSP-2232 will respond with the following:
MODem was 12
MODem now 3 3
```text
cmd: *** HBaud now 300/1200
```

### (PDF p.73)

#### 4.8.2

Display inq Received Data
When a port 2 only modem is loaded, radio port 1 is effectively
disabled. This can be desirable when operating packet on radio
port 2 and you do not want to be disturbed with any HF signals or
noise that may be received on radio port 1.
When a Dual Port modem such as MODEM 33 is loaded in the DSP-2232,
received data is displayed from both Radio Ports at the game time.
This allows you to operate on HF and not miss any local Packet
connects or information from DX spotting nets .
The DSP-2232 displays monitored packets by prefacing each packet with
a port designator "pl" for radio port 1 and "p2" for radio port 2.
For instance, let 's gay that dual port modem number 33 is loaded and
you are monitoring HF packet activity on Radio Port 1, and VHF packet
You might see something like the following:
activity on Radio Port 2.
pl [Ul):
pl (UIJ:
p2 [1, P;ltl):
Hello Bob, how are you?
p2
pl [C, P)
pl (UA,F)
pl
Hello Bob, how are you?
WXIAAA calling CQ on
Radio Port 1 (HE')
An Information Frame from
WY7DDD to WY7EEE monitored
on Radio Port 2 (VHF)
Acknowledgment of the above
I-Frame on Radio Port 2
WX2BBB attempting to connect
to WXIAAA on Radio Port 1 }
WXIAAÄ acknowledging the
connect request }
An Information Frame from
WX2BBB to WXIAAA monitored
on Radio Port 1 (HF)
The first packet shown above is an Unprotocol "CQ" packet from WXIAÄÄ
that was received on Radio Port I. We know this was received on
Port 1 because of the "pl" shown in front of the packet.
Remember from the section on Multi-connect packet operation that
(Packets received on Radio port 1 will be on one of the ten logical
f rom
channels numbered 0-9. ) The second packet is another "CQ"
it was
WXIAAA. Again, there is a "pl" in front of it so we know
also received on Radio Port I .
The third packet is an Information-Frame, or simply an "I-Frame" that
on Radio
was monitored on Radio Port 2 . We know this was received
Port 2 because of the "p2" shown before the packet.
one of the
Remember that Packets received on Radio port 2 will be on
26 logical channels designated A-Z.
The fourth packet is an acknowledgment to the previous I-Frame
received on Radio Port 2.
Again, the "p2" in front of the packet
tells us it was received on Radio Port 2.

### (PDF p.74)

#### 4.8.3

##### 4.8.3.1

##### 4.8.3.2

7/92.
The. fifth packet is a Connect Request from WX2BBB to WXIAAA that was
monitored on Radio Port 1.
The "pl" in front tells us so. The s ixth
packet is an acknowledgment by WXIAAA to the connect request.
The seventh and last packet shown above, is the first I-Frame that
WX2BBB is sending to WXIAAA after they have connected.
Controllinq Your Transmitted Text
If you are using an AEA PAKRATT program designed for the DSP-2232,
switching between Radio Ports is described in the program manual.
If you are using a computer terminal, terminal program or the
"Dumb Terminal Mode" of an older PAKRATT program, this section will
describe how to control and switch between the radio ports.
Defining the Channel Switch ina Character CHSWITCH
Before you can switch between logical channels or Radio Ports you must
define a Channel Switching character that you will use to signal the
DSP-2232 that you want to redirect your transmitted text. This
special character is defined with the CHSWITCH command and should be
one that you do not normally type in conversational text such as the
vertical bar
To enter the vertical
($7C), or the tilde
($7E).
as the CHSWI±CH •character, first enter the Command Mode of the
bar
DSP-2232 and then type CHSWITCH $7 C as shown below:
CHSWITCH $7C
The DSP-2232 will respond with the following:
CHSWitch was $00
CHSWitch now $7C ( l)
Once a CHSWITCH character has been entered, you may switch Radio Ports
or Packet logical channels of the DSP-2232 at will.
Switching Between Radio Ports
The ten logical channels on Radio Port 1 are labeled
The 26
logical channels on Radio Port 2 are labeled A-Z. To select Radio
Port 1, press the CHSWITCH character you just defined, followed by a
number from 0-9. To select Radio Port 2 , press the CHSWITCH
character, followed by a letter from A-Z.
After you change Radio Ports or logical channels, the text you type in
the CONVERSE mode will be sent to the port and channel selected.
If
you are connected to another packet station, the text you type will be
If the channel is not connected, the text • will be
sent to him or her.
sent in the Unprotocol or unconnected mode.
For example, let's say that you are operating HF Packet on Radio Port
1 and are also available for connects on the local VHF Packet channel
on port 2.
You connect to an HF packet
of a QSO when a station on VHF connects
how your screen would look and suggests
The underlined text is
the
occurrence.
stat ion and are in the middle
The following shows
to you.
how you might handle such an
text that you have typed.

### (PDF p.75)

A: 73 Bob.
I : cmd :
<Enter>
WX7EEE
O: DISCONNECTED:
WXIAAA
pl (Ull:
pl (Ull:
```text
cmd:C WXIAAA
```
CONNECTED to WXIAAA
Hel 10 name here is Bob.
Hi Bob, name here is Jim and the
QTH is Boston Mass.
A: CONNECTED to WX7EEE
Nice to meet vou Jim 0TH here is
Seattle WA. You have a nice signal.
Hey Bob, I'm going to the hamfest
this weekend if you want a ride. •
AHe110 Mike L am on HF right
now talk inq to a stati•on in Boston.
O: Thanks Bob, your signal is S-7
here .
I am running 100 Watts into a
tri-bander at 50 ft.
I OThanks for the report you re
S-8 and the antenna here is quad.
I Ayes, L was planning to ge Saturday.
What time do you want to leave?
A: I'll pick you up at 8:00 Bob.
WXIAAA calling CQ on
Radio Port 1 (HF) }
You attempt to connect to
station WXIAAA on HF }
You ' re connected to WXIAAA }
You gend him your name
The other station responds
with his name and location
WX7EEE connects to you on
VHF (Radio Port 2)
{ You send another packet
to Jim on HF }
Your friend on VHF packet
wants to go to the hamfest
You switch ports by typing
and respond on VHF
Jim on HF gives you a signal
report. The "O:"
shows you
this was from Port
You switch back to
the and respond
You switch to Port
to say you want
Your friend on VHF
to you
HF with
to Jim
2 with
to go }
responds
I'm looking forward to it.
O: Well, it's getting late
time to pull the plug Bob .
Sounds qood, I'll see you
Saturday morning.
I OTake care Jim and 73.
here
on
and
Jim on HF tells you he has
shut down for the night
You last sent data on Port 2
and do not need to send IA
You gay 73 to Jim on HF }
Your friend on VHF says 73
You switch to Port 2 and
Disconnect from WX7EEE }
You re now disconnected from
WX7EEE on VHF
WXIAAA disconnects from you
on HF (Port O }

### (PDF p.76)

As Y ou may have noticed, the above technique of communicating on more
than one Port at the same time is almost identical to that used for
single-port multiconnect packet operation.
Let 's discuss the gamp Ie
QSOs above to see how the Port switching occurs.
The f irst packet f rame, we see is a CQ f rom WXIAAA. This frame is
preceded by a "pl" which shows it was received on Radio Port 1.
You have seen WXIAAA calling CQ and decide to connect to him on HF.
You simply go to the Command mode (by typing <CTRL-C>) and issue the
CONNECT command C WXIAAA as shown on the third 1 i ne. The fourth 1 ine
shows that you are now Connected to WXIAAA. Note that you did not
have to type " O" before sending the connect request since the
DSP-2232 defaults to Port 1 until it has been changed to Port 2.
Now that you are connected to WXIAÄA you greet him and send your name.
WXIAAA then gives you his name (Jim) and location which is the way
most QSOs begin. Remember in the above example, the underlined text
is the text you would have typed.
You have just begun your QSO with WXIAÄA when all of a sudden your
friend WX7EEE connects to you. WX7EEE has connected on Radio Port 2
(VHF) which is shown by the "A: " before the connect message. Remember
that the 26 channel designators for Radio Port 2 are A-Z.
Before you respond to WX7EEE on VHF, you send a packet to Jim on HF
giving him your location. After sending this packet, you see WX7EEE
on VHF has offered you a ride to the hamfest. We know this packet is
from Radio Port 2 since the previous packet displayed by the DSP-2232
was the "A: *** CONNECTED" message from Port 2.
Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF. This way he will understand
that it may take you a little longer to respond to his packets.
Before you can send data to Radio Port 2, you must switch to this Port
with or the text you type will be sent to Radio Port 1.
You receive another packet from Jim on HF giving you a signal report
and describing his antenna. Again, the "O: "
in front of the text
shows this was received on Port 1.
Now you want to give Jim a signal report so you must switch to Port 1
with IQ before your text or it will be sent to WX7EEE on Port 2.
After sending Jim a signal report, you tell your friend on VHF that
you want to go to the hamfest with him. You must switch back to
Port 2 by typing before sending to WX7EEE.
In the next received packet, we see that WX7EEE on VHF agrees to pick
you up at 8:00 to go to the hamfest. The "A: " in front of the packet
shows that this packet was received on Port 2.
Iinmediately following, you see a packet from Jim on HF saying it ' s
time to shut down. The "O: "
identifies this as a Port 1 packet.

### (PDF p.77)

##### 4.8.3.3

The last packet you sent was to WX7EEE on VHF so you decide to reply
to him f irst. This way you do not have to change Ports. After your
reply to WX7EEE, you change to Port I with IQ and send your good-byes
to Jim on HF.
Your friend WX7EEE on VHF says 73 and signs off with you. After two
stations agree to sign off, one of them must initiate a Disconnect.
You decide to disconnect from WX7EEE by first switching to Port 2 with
and then entering Cornmand mode with a <Ctr1-C>. Once you see the
Command prompt (cmd: ) , enter the the Disconnect "D" command.
Shortly after you do this you see the display
" DISCONNECTED: WX7EEE" from the DSP-2232.
Jim on HF has decided to initiate the disconnect with you so the final
packet you see is the "O: *** DISCONNECTED: WXIAAA" frame from Port 1.
More Thouqhts on Port Switching
One problem of having more than one Radio Port is remembering which
port you are currently using. In the dual port sample QSOg above,
this was not a problem, but after it has been hours or days since you
have used your DSP-2232, you may • forget which port you last used.
With AEA Pakratt Software programs, the on-screen status will always
show which port you are using so this is not a problem. With other
programs, you will have to query the DSP-2232 with the CSTATUS SHORT
command. The CSTATUS command displays the status of the logical
channels of Port 1 and Port 2 of the DSP-2232. The CSTATUS SHORT
command displays the status of the active channel and any other
connected packet channels . After completing the sample QSOs above,
the DSP-2232 would display the following.
```text
cmd:CSTATUS S
```
Ch. A - 10 DISCONNECTED
This reminds you that Channel A is your current I /O channel. Any text
that you type in the Converse mode will be sent to channel A on Radio
If you had been connected to any other packet stations, the
Port 2 .
callsign and channel would have also shown in the display.
Sometimes you might not want to be bothered with anything from the
For these times either Radio Port may
Radio Port you are not using.
For example, let's say that in
be turned OFF with the RADIO command.
the above example QSO you wanted to work HF packet and did not want to
Typing the following cornmand
be interrupted with any VHF connects.
would cause Radio Port 2 to be disabled.
```text
cmd:RADIO /0
RAdio was 1/2
RAdio now 1/0
```
When a DSP-2232 Radio Port is disabled, the front panel LCD STATUS
indicator for that port will be extinguished as a reminder.

### (PDF p.78)

### 4.9

#### 4.9.1

#### 4.9.2

#### 4.9.3

#### 4.9.4

Advanced Packet Operat ion
Your DSP-2232 has many commands and features that are not used for
Still, as you become more familiar
day-to-day connects conversations.
with packet, some of these features may become important to you.
Transparent Mode
The TRANSPARENT mode allows any 8-bit binary character to be sent by
You usually must use the TRANSPARENT mode to
your packet station.
transfer binary and program files to and from other stations.
You can enter the TRANSPARENT mode either by typing TRANS at the cmd:
prompt after you connect, or by setting CONMODE to TRANS. Either way,
once in the transparent mode, any character you type will be sent
automatically after the PACTIME setting. This way the DSP-2232 can
send any character. We recornrnend using HARDWARE f low control in
transparent mode but SOFTWARE flow control is available with the
TRFLOW and TXFLOW commands. To get back to Command mode after you are
finished with transparent mode, you must type the COMMAND character
(default <CTRL-C>) 3 times within the "Guard time" set by the
CMDTIME command (default 1 second) .
Gateway Operation
The DSP-2232 can allow packet stations on one radio port to communicate
When the DSP-2232 bridges two packet
with stations on the other port.
frequencies in this manner, it is said to be a "Gateway.
operation is a dual port feature and requires that one of the dual
port packet modems be loaded. Once this has been done the gateway
function is enabled by entering a callsign in MYGATE. The callsign
must not be the same callsign and SS ID as MYCALL or MYMAIL.
When another station digipeats via the callsign in MYGATE, your
If
DSP-2232 will Gateway between Radio Port 1 and Radio Port 2.
provide this feature to users in your area, you may want to BEACON a
message on both ports informing users that your gateway is available.
At this time however, unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary Authorization
(STA) from the FCC. This restriction may someday change, but until
then US amateurs must always have control of their HF transmitters
when an automatic device such as the DSP-2232 Gateway is in use.
Sending 8-bit Data in Converse Mode
Sometimes you may need to send a file that contains some 8-bit data,
but
you
The
The
and
new
to.
BIT
In this case,
not need all the features of the TRANSPARENT mode.
may find turning the command 8B ITCONV ON is all that is needed.
Packet ORA Feature
DSP-2232 recognizes UI frames with a destination field of "QRA"
This is helpful for others
will respond by sending an ID packet.
to your area that are looking for other packet stations to talk
To disable this feature and remain anonymous, simply set User
is ON.
22 OFF (UBIT 22 OFF).
The defaulv

### (PDF p.79)

#### 4.9.5

#### 4.9.7

#### 4.9.8

4.9 9

#### 4.9.10

#### 4.9.11

### 4.10

If you wish to see who is available in your local area, simply set
your UNPROTO path to QRA and send a packet. Within 1 to 16 seconds
other stations should respond to your QRA request by sending an ID
packet of their own. This feature is compatible with TAPR's QRA
feature introduced in the I. 1.8 firmware release.
The CFROM Command
If you ever want to exclude certain stations from connecting or only
allow friends to connect, you may do so with the CFROM Command.
See
The Command Summary for details of its operation.
Operating in Ful I-Duplex
Most packet operation is carried on over Half -Duplex transceivers that
can transmit and receive but not do both at the same time. When a
separate transmitter and receiver is used such as in satellite
operation, you may want to use the FULLDUP Command in the DSP-2232.
Identifying as A Diqipeater
If your DSP-2232 is being used as the primary digipeater in a local
HID will automatically
area, you may want to enable the HID command.
identify your station for others to see.
Digipeater Alias Calls ian
If your packet station is being used as the primary digipeater in your
local area, you may want to choose a simpler identifier for others to
use with the MYALIAS Command.
Morse ID in Packet
In most countries packet is an accepted mode of identification so this
In some countries however a Morse ID is
command should be left OFF.
required when packet is used and so the MID command should be enabled.
Sharing Packet Channels with Voice Operation
Although it is seldom needed, the DSP-2232 does have an input for
This
SQUELCH information from a transceiver on the RADIO connectors.
input should be used and the SQUELCH command set if the packet channel
is to be shared with voice operation.
Disablinq Transmit Operation
Occasionally for test purposes it may be desired to disable the PTT
circuit in the DSP-2232. This is done with the XMITOK Command.
Seldom Used Commands
The following commands operate in Packet, but are seldom needed.
They are listed for reference and described in the Command Summary.
AFi1ter
MDMon
BBSmsgs
MRpt
CONPerm
MXmit
CP act ime DCdconn
Flow
HEAder1n

### (PDF p.80)

### 4.11

#### 4.11.1

Packet Lite HF Packet
Amateur radio needs a
Baudot and ASCII have
Protocol Extension
better communications mode for HF operation.
no provision for error detection. AMTOR FEC and
ARQ are more resistant to errors, but do not carry the full ASCII
character set.
300 baud AX. 25 packet is undesirable because the long
transmissions are prone to bit errors, any one of which invalidate the
whole frame .
Packet Lite is, as its name suggests, an abbreviated form of packet.
It is designed as a transparent extension to the AX. 25 protocol that
reduces the "overhead" of all HF packet frames without digipeaters.
Packet Lite does not solve HF packet 's problems, but it should provide
some throughput improvement on HF where it is desperately needed.
AEA's engineering department is interested in hearing from Packet Lite
users with any comments or suggestions on improving the protocol.
A Brief Description of the Packet Lite Protocol
The main feature of Packet Lite that reduces overhead is that it uses
an address f ield of only 4 bytes. A standard AX. 25 header without
digipeaters uses 14 bytes of addressing.
Shortening the packet frame
header lessens the possibility of any given frame taking a hit.
Packet Lite reduces the length of an I-frame slightly, but its real
strength is the shortening of the acknowledgment frames, resulting in
fewer garbled acks and therefore fewer unnecessary retries. An ack
(RR, RNR or REJ frame) in standard AX. 25 consists of 19 consecutive
bytes that must be copied with no hits; Packet Lite reduces the length
to 9 bytes, or 47% of the standard ack length.
A couple of restrictions are necessary to accomplish this.
First, Packet Lite works only between two stations connected directly ,
with no digipeating allowed.
If digipeaters are introduced to the
address field, the advantage of the reduced overhead disappears.
Second, all Packet Lite connections emulate AX .25 version 2.0 (RR
polling instead of retrying I-frames) . This is necessary for the 10-
minute identification described below. Also, the main reason version
1 continues to be used on HF is that on a retry a [RR,PI polling frame
is so long that one might as well just send the I-frame again.
Packet
Lite's polling and ack frames are so short that the AX. 25 version 2.0
polling method is now worth doing.
Enablinq Packet Lite
To begin using Packet Lite, first make sure you have made all the
proper HF Packet settings discussed earlier in this chapter.
Be
careful to ensure VHF is OFF and a 300 bps Modem has been selected.
Once these settings have been made, to enable the Packet Lite protocol
extensions, turn the command LITE ON for the appropriate Port.
For example to enable Packet Lite on Port 1, type "LITE ON" at the
You must not be connected to any other stations on
command prompt .
that Port or you will not be allowed to change the LITE command.
- 36

### (PDF p.81)

Once all the above settings have been made, simply issue the standard
CONNECT request as described earlier in the chapter .
If the station you are connecting to also has Packet Lite enabled, a
Packet Lite connection will result and you and the distant station

#### 4.11.2

#### 4.11.3

Initiatinq a Packet Lite Connection
2.
3.
-O
should enjoy a more reliable QSO than others on
Compatibility With Standard AX. 25 Stations
If the station you connect to does not have the
extensions, there are three known possibilities
1.
WAIABC>WB2XYZ (C, P) 01 3B 38 58 32
WB2XYZ>WAIABC (UA, F)
the same frequency .
Packet LITE protocol
that will occur.
{Packet Lite attempt}
{Standard ack}
In this case, the non-Lite station sees the [C, P) control byte but
ignores the non-standard bytes following it.
It replies with a
standard UA frame, and the connection proceeds as standard AX. 25.
.WAIABC>WB2XYZ [C, P) 01 3B 38 58 32
WB2XYZ>WAIABC (FR): 3F 00 03
WAIABC>WB2XYZ (C, P)
WB2XYZ>WAIABC (UA,F)
{Packet Lite attempt}
{Frame Reject}
{Standard attempt}
{Standard ack}
In this case, the non-Lite station notices the non-standard bytes
following the control byte and issues a FRMR (Frame Reject) to signify
that a protocol violation has taken place. The DSP-2232 receives the
FRMR and automatically reverts to standard AX. 25, sending the connect
retries without the Lite PID and address bytes.
WAIABC>WB2XYZ [C, P) 01 3E 38 58 32
{Packet Lite attempt}
{No response}
In this case, the non-Lite station notices the non-standard bytes
following the control byte but sends no response at all.
If this
occurs, you must turn the command LITE OFF and try to connect again to
the distant station. No adverse effects are caused by this, but
transparency with standard AX. 25 is lost when the receiving station
does not acknowledge a Packet Lite connect request in some manner.
We know that TCP/IP, NET-ROM and DRSI stations ignore Packet Lite
Connect requests. These stations are normally found on VHF, but to be
safe, the LITE command should be turned OFF when not in use.
Packet Lite Protocol Enhancement Summarv
The following describes the Packet Lite protocol extension in depth
for those interested in the technical details.
It is not necessary to
read or understand the following section to use the protocol.
Here is a summary of a Packet Lite exchange, where WAIABC calls WB2XYZ:
Connect :
dest inat ion
B
c
source
SABM 01
CTRL PID
3E 38 58 32
I short address

### (PDF p.82)

The destination and the source are both 7 bytes long.
-o
-o
-O
Everything up
to the CTRL byte ( SABM) is standard AX. 25 version 2 . O. The "Protocol
I D" of 01 hex is Packet Lite's reserved value, which provides a way of
interpreting the following bytes. This leaves room for other
extensions to AX. 25 in the future. The short address bytes are the
right-justified bytes of the address field that WAIABC proposes to use
in subsequent Packet Lite frames with WB2XYZ.
In this case, the AEA
3E38 is a
implementation of the short address is illustrated .
compressed version of the destination WB2XYZ and 5832 is a compression
of WA 1 ABC. However, any combination of 26 bits may be used
(see "Technical Details" below) .
Connect acknowledgment :
destination
WB2XYZ replies to WAIABC.
Y
z
source
UA 01
CTRL PID
58 32 3B 38
I short address
Again everything up to the CTRL byte (UA)
is standard AX. 25. The "Pl D" of 01 and the short address confirm that
The short address is
WB2XYZ has accepted the Packet Lite connection.
again the right-justified representation of the address field that
In this
WB2XYZ will be using in subsequent Packet Lite transmissions.
case WB2XYZ has accepted the short address field suggested by WA 1 ABC,
and has shown his acceptance by echoing the address back in reverse
order ( 5832 = WAIABC and 3E38 = WB2XYZ) . AEA products always accept
the short address from the SABM frame; however, the Packet Lite
protocol allows the sender of the UA frame to propose a different
combination of 26 bits, to avoid conflicting with another Lite QSO.
In either case, the sender of the original SABM must accept the 26
bits in the UA frame, reversing the address order for its own transmissions.
Transmission of data:
3E38 shifted
short dest.
BO 65
5832 shifted
short source
10
CTRL •
PID
Test <CR>
text
The address field
WAIABC sends data to WB2XYZ in Packet Lite format.
consists of the short address from WB2XYVs UA frame, reversed and
left-shifted. The added bits come from AX. 25 version 2 .O's command
and response bits, and the end-of-address bit .
Acknowledgment of data :
BO 64
5832 shifted
short dest.
3E38 shifted
short source
31
CTRL
WB2XYZ acknowledges the data from WA 1 ABC. The address f ield is
This is the shortest length frame possible in Packet Lite.
reversed .
4 address bytes + 1 CTRL byte + 2 flags + 2 CRC bytes =
Every 10 minutes the stations must identify using both
addresses :
9 bytes .
long and short
w
w
B
destination
dest inat ion
w
B
source
source
B
Y
c
z
01
CTRL PID
01
CTRL PID
3E 38
I short
58 32
short
58 32
address
BE 38
address

### (PDF p.83)

Either station may initiate
-o
-O
the ID
source
source
exchange .
D isconnect :
destinat ion
Disconnect acknowledgment :
destination
B
Y
c
z
DISC 01
CTRL PID
UA 01
CTRL PID
3B 38
I short
58 32
I short
58 32
address
3E 38
address
At the end of the connect ion, the two stations must once again
identify using both long and short addresses.
AEA firmware supporting Packet Lite also contains code that permits
monitoring of Packet Lite and extended AX. 25 frames.
Packet Lite Shortened Address Technical Details
The Packet Lite address field consists of 26 bits distributed over 4
bytes (or octets, as the AX. 25 spec calls them) .
These bits are
considered to be two groups
of 13 bits each, roughly equivalent to a
destination and a source ID.
If •we label the bits and show their
use in the address field of
a Lite frame, the bits are distributed as
fol lows :
ABC DE F GO
L MO N OPQRSTO y UVWXYZI
x HI JK
The least significant bit of each byte is used to show whether or not
the byte is the final byte in the address field, as in standard AX. 25.
The bits "x" and "y" (lower case) have the function of command and
response, similar to the function of the standard AX. 25 version 2.0
SS ID byte C bits (see AX. 25 Protocol version 2 . O, section 2.4. 1.2) .
In the AEA implementation of Lite addressing, the standard callsigns
are compressed to yield short addresses. The destination callsign is
compressed into bits A-Mt and the source into N-Z. These bits are
used as the address field suggestion following the control byte in the
Connect ( SABM) frame. When the bits are used following the control
byte as an ID suggestion or a real extended ID, the format is:
OABCDEFG O OH 1 JK LM O N OPQRST O OUVWXYZ
If we label the 7 standard right-justified callsign bytes 1-6 and
SSID, here is how we derive the first group of address bitg A-M:
ABCDE
FGHI
JKLM
(byte 1 XOR byte 4 )
(byte 2 XOR byte 5 )
AND
AND
(byte 3 XOR byte 6 XOR SSID) AND
AEA firmware derives the second group of bits the
implementations are free to select any combinat ion of
game way. Other
26 bits when
setting up the short address in either the initial SABM frame or its
UA response.

### (PDF p.84)

### 4.12

7 / 92
Packet Meteor Scat tor Extens ion
A new packet protocol extension has been added for meteor scatter
work that allows a Master/ Slave packet connection to be established.
This is done to reduce the possibility of simultaneous transmissions
by both sides of a packet connection over a long meteor scatter path.
This experimental protocol is activated by turning User BIT 18 ON
(UBIT 18 ON). When UBIT 18 is ON (default OFF) the packet station
who initiates a packet connect will become the Master station and the
station who acknowledges the connect becomes the Slave.
After a Meteor Scatter connection has been established, the Master
station will continually send either information frames (I-frames)
or polling frames and await an acknowledgement from the slave. The
Master station therefore sends packets constantly, even if all its
I-frames have been acknowledged. The slave station sends nothing,
not even I-frames, . until it receives a polling frame from the master.
The Slave station may only Send an I-frame to the Master after a
poli frame has been received.
The packet timing of the Master station is critical for proper Meteor
Scatter operation.
In a normal AX. 25 packet connect, the FRACK timer
counts down until it reaches zero and then a Retry of a poll frame is
sent. The FRACK timer counts in units of seconds however and a finer
timing resolution is desirable for Meteor Scatter work. A new timer
called FRICK has been added which t imes in 10 msec increments. The
FRICK timer can be set from 0 (disabled) to 250 which corresponds to
a time of up to 2.5 Seconds.
See the Command Summary for a complete
description of the FRICK timer.
The following settings are recommended for this method of Meteor
Scatter work. Both packet stations should use these same settings.
UBIT 18 ON
RETRY O
AX25L2V2 ON (default)
MAXFRAME 1
(CHECK doesn't matter)
FRICK n, where n is large enough to allow the other station time
Note :
to send the start of an acknowledgement frame
Do not operate the unit with multiple packet connections while
In contrast to FRACK, which provides
FRICK is active (1-250).
one retry timer per multi-connect channel, there is only one
Each logical
FRICK timer on each radio port of the DSP-2232.
channel will try to use the same FRICK timer, causing
interference to the operation of the other channels.
Digipeaters should not be used when in the Meteor Scatter mode.
The FRICK timer (unlike FRACK) does not allow any extra time when
digipeater stations are specif i ed .
To return to normal AX. 25 packet operation turn User BIT 18 OFF.
Also, be sure to disable the FRICK timer (by setting FRICK to O)
when you are through operating in Meteor Scatter mode.
