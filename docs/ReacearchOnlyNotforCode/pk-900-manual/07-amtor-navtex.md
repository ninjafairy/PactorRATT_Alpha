# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 7 — AMTOR and NAVTEX Operation (PDF p.115–136)

<!-- PDF p.115 -->

### 7.1 Overview

The PK-900 provides AMTOR (AMateur Teletype Over Radio) operation on
Radio Port 1 in accordance  with FCC Part 97.69 and CCIR
Recommendations 476 and 625 for Mode A (ARQ) and Mode B (FEC).  AMTOR
is an adaptation of the SITOR (ShIp Teletype Over Radio) system used
in high-seas telex, which provides error detection and correction.

AMTOR has two basic modes of operation, Mode A (ARQ - Automatic
ReQuest for Reception) and Mode B (FEC - Forward Error Correction).

- ARQ AMTOR is a handshaking protocol that allows only two stations
to communicate in a near error free fashion.  You will hear a
"chirp chirp" sound when you find two stations conversing in ARQ.
PACTOR can be distinguised from AMTOR in that is uses a longer
(about 1 sec) data burst.

- FEC AMTOR is similar to Baudot RTTY and is used to call CQ or to
carry on "round table" contacts.

NAVTEX (NAVigational TEleX) is a form of FEC AMTOR that is used to
send Navigational bulletins and weather information primarily to ships
at sea.  Recently it has been adopted by the ARRL to send bulletins to
amateurs.

### 7.2 Where to Operate AMTOR

Before you can operate AMTOR, you must first know where the activity
ocurrs.  Most AMTOR operation occurs on the 20-meter amateur band
between 14.065 and 14.085 MHz.  AMTOR activity can be found on the
other HF amateur bands as well and is most often located between 65
and 90 kHz up from the bottom of the band as it is on 20 meters.

#### 7.2.1 PK-900 AMTOR Parameter Settings

AMTOR is a bit more complex than Baudot or ASCII operation.  AMTOR
operating modes require SELCALL (Selective Call) codes be entered
before you can operate.  There are two SELCALLs you should enter.

#### 7.2.2 Entering Your SELective CALling Code (MYSELCAL)

This unique character sequence contains four alphabetic characters
that are derived from your call sign.  The PK-900 automatically does
this for you just by entering your amateur callsign into the MYSELCAL
command.  If you are using an AEA PAKRATT program, follow the
instructions in the program manual for entering the command MYSELCAL.

If you are using a terminal, then Type "MYSELCAL" to load your SELCALL
into the PK-900 as shown below:

cmd:MYSELCAL N7ML

<!-- PDF p.116 -->

The PK-900 will tell you,  MYSelcal now NNML

See the MYSELCAL command in the Command Summary if you are interested
in more information on the translation process.

Because the same call sign sequences are assigned in ten US districts,
it is possible that your SELCALL could be used by another station.  If
you think a station in another call district is also active on AMTOR
and is using the same SELCALL, see the MYSELCAL command for
information on how to change your Selcall.

#### 7.2.3 Entering Your Selective calling Code (MYIDENT)

At the present time, most of the AMTOR activity on the amateur bands
is using the four-character SELCALL defined in CCIR 476 and described
above.  The seven-character SELCALL (MYIDENT) defined in CCIR 625
solves the problem of non-unique SELCALLs by providing many more
possible SELCALLs than CCIR 476 does with only four characters.

To enter your seven-character SELCALL all you must do is enter your
amateur callsign.  The PK-900 will do the translation for you.

If you are using an AEA PAKRATT program, follow the instructions in
the program manual for entering the command MYIDENT.

If you are using a terminal, then enter the following

cmd:MYIDENT N7ML

The PK-900 will tell you, MYIdent now VTMFFFF

See the MYIDENT command in the Command Summary if you are interested
in more information on the translation process.

#### 7.2.4 Enter the AMTOR Mode

Now that you have entered your personal MYSELCAL and MYIDENT Selective
Calling codes, you are ready to enter the AMTOR mode.

If you are using an AEA PAKRATT program, follow the instructions in
the program manual to enter the AMTOR mode.

If you are using a terminal, simply type "AMTOR" or "AM" from the
Command Mode followed by the <Enter> key to enter the AMTOR mode.
The PK-900 responds by displaying the previous mode:

Opmode   was PAcket
Opmode   now AMtor

Your PK-900's front panel LCD Status display will show that you are in
the AMTOR Standby mode on Radio Port 1, and the COMMAND LCD will be
lit.

#### 7.2.5 HF Receiver Settings

Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your PK-900 through the direct FSK keying lines.

<!-- PDF p.117 -->

In this case, you should select the FSK or RTTY operating mode.
Adjust the volume to a comfortable listening level.

#### 7.2.6 Tuning in AMTOR Stations

Tuning in AMTOR stations properly is critical to successful operation.
Since HF AMTOR stations use either 170 Hz or 200 Hz Frequency Shift
Keying to send data, tuning accuracy is very important.  The BARgraph
command will select the type of tuning display.  (See the display type
below).  Follow the  procedure below for the best results.

- Make certain your HF receiver is either in LSB or FSK depending
on your PK-900 set-up.

- Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.

- Tune your receiver carefully between 14.065 and 14.085 MHz (or
another band where you know there is AMTOR activity) and listen
for the "chirp chirp" of ARQ or the steady data of FEC stations.

NOTE:     When in the AMTOR Standby mode as you are now, you will not
be able to print the "chirping" ARQ signals.  To print these
stations, you must be in the AMTOR Listen (ALIST) mode.

- When you find a station, slowly vary the VFO on your receiver and
look for a display on the PK-900 tuning indicator as shown.

--------------------------------------------------------------MARK |    ] ] ]              ] ] ]          | SPACE
--------------------------------------------------------------Tuning correct for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]  ]   ]  ]   ]   ]  ]           ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning correct for magic eye indicator (BAR 3)

--------------------------------------------------------------
|      ] ]       |
--------------------------------------------------------------Tuning correct for center tune indicator (BAR 2)

<!-- PDF p.118 -->

If the tuning indicator looks like the one below, the frequency
from your speaker is too low for the PK-900 to copy the signal.
Slowly tune the VFO and make the frequency higher.

--------------------------------------------------------------MARK | ]   ]   ]   ]   ]   ]   ]              | SPACE
--------------------------------------------------------------Tuning too low for discriminator indicator (BAR 0)

--------------------------------------------------------------
| ]   ]   ]   ]   ]   ]   ]   ]     ]        |
--------------------------------------------------------------Tuning too low for magic eye indicator (BAR 3)

--------------------------------------------------------------
|       ]   ]         |
--------------------------------------------------------------Tuning too low for center tune indicator (BAR 2)

If the tuning indicator looks like the one below, the frequency
from your speaker is too high for the PK-900 to copy the
signal.  Slowly tune the VFO and make the frequency lower.

--------------------------------------------------------------MARK |          ]   ]   ]  ]  ]  ]   ]  ] | SPACE
--------------------------------------------------------------Tuning too high for discriminator indicator (BAR 0)

--------------------------------------------------------------
|          ]    ]   ]   ]   ]  ]  ]  ]   ]  ] |
--------------------------------------------------------------Tuning too high for magic eye indicator (BAR 3)

--------------------------------------------------------------
|          ]   ]      |
--------------------------------------------------------------Tuning too high for center tune indicator (BAR 2)

- Adjust the PK-900 front panel THRESHOLD control so that the DCD
LCD lights when a properly tuned FEC AMTOR station is being
received.

After you have an FEC AMTOR station tuned in, you should start seeing
the copy on your screen.  If you have tuned in "chirping" ARQ AMTOR
stations, you will not print anything until you enter the ALIST mode.
If you just want to receive, see Chapter 10 on SIGNAL IDENTIFICATION.

<!-- PDF p.119 -->

### 7.3 Transmitter Adjustments

Make sure your PK-900 is adjusted for your SSB transmitter as
described in section 3.5 and 3.5.2 of this manual before transmitting.
These are very critical adjustments.  If your PK-900's AFSK
level and transmitter microphone gain are not adjusted properly, other
stations will not be able to copy your signals.  Always check your
plate current, collector current or power output of your rig before
transmitting.

#### 7.3.1 Going On The Air

Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using.  Before you transmit, you
must decide if you are going to "call CQ" or answer someone's CQ call.

#### 7.3.2 Calling CQ in FEC AMTOR

If you plan to make a CQ call, you must do so in the FEC AMTOR mode.
This is required since an ARQ AMTOR transmission requires another
station to "Link-up" with.  If you are using an AEA PAKRATT program,
see the program manual to place the PK-900 into FEC transmit.

If you are using a terminal or terminal program, the following will
place your PK-900 and transceiver into the transmit mode.

- Make sure that you have selected your transmitted text to go to
Port 1 by pressing the CHSWITCH character defined in Chapter 4
followed by a number from 0 through 9.

- Type "FEC" then press the <Enter> key to key your transmitter and
automatically enter AMTOR FEC transmit mode.

As soon as you type the <Enter> key you will be transmitting!  At this
point you are also in the CONVERSE mode and anything you type will be
sent in FEC by your transmitter.

- Type in your CQ message.  Make sure you include YOUR Callsign,
your four-character Selcall (MYSELCAL) as well as your sevencharacter Selcall (MYIDENT) so others can respond to your CQ call.
An example is shown below:

CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML) (VTMFFFF)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML) (VTMFFFF)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML) (VTMFFFF)
CQ CQ CQ CQ CQ CQ CQ DE N7ML (NNML) (VTMFFFF)
SELCALL NNML (VTMFFFF) K
<CTRL-D>

- Type <CTRL-D> at the end of your CQ call.  The <CTRL-D> puts both
your radio and the PK-900 into the receive mode.

- Wait a bit to see if you get a response.  If not, you can repeat
the above procedure.

<!-- PDF p.120 -->

#### 7.3.3 Answering an FEC AMTOR CQ

Normally when you see a station calling CQ in FEC AMTOR, you will want
to answer him using ARQ AMTOR.  Remember that ARQ AMTOR is the
protocol that reduces the chance of transmission errors.

Let's assume you hear NNML calling CQ.  To answer, do the following:

- If you are using an AEA PAKRATT program, check the program manual
for instructions on starting an ARQ AMTOR contact.

- If you are using a terminal simply type "ARQ NNML<Enter>" to
start a CCIR 476 ARQ contact, or "ARQ VTMFFFF<Enter>" to start a
CCIR 625 ARQ contact.

After your PK-900 has locked or synchronized with the distant
station, you may begin your conversation.

N7ML N7ML DE YOURCAL YOURCAL...etc

#### 7.3.4 ARQ AMTOR Operating Fundamentals

When you finish typing your comments or traffic to the other station
and wish the distant station to transmit to you Do Not type "KKK" or
anything like that!

- Do type a plus sign immediately followed by a question mark (+?).

"+?" is a software changeover command that switches your system from
being the "Information Sending Station" (ISS) to the "Information
Receiving Station" (IRS), and switches the distant system from being
the IRS to being the ISS.  When your distant partner sees the "+?" he
knows he can begin typing comments or traffic.

NOTE:     When discussing ARQ operation, we use the terms "Information
Sending Station" (ISS) and "Information Receiving Station"
(IRS) instead of "transmit" and "receive" since in ARQ, both
stations are rapidly switching from transmit to receive.

- Don't bother with multiple call signs and "over-to-you" routines
or "KKK" used in Baudot and ASCII RTTY operation.  The system
does it all for you when you type the "+?".

The FCC requires station identification once every ten minutes.  It's
sufficient to begin with "QRA (mycall)" or end your transmission with
"QRA (mycall)" before the "+?" changeover code, or use the <CTRL-B>
"HERE-IS" to send your own Auto-AnswerBack message.

#### 7.3.5 Ending an ARQ AMTOR Contact

When you've finished your "final finals" to the distant station and
both stations are ready to end the Mode A (ARQ) contact, you can end
the contact and terminate the link in several different ways:

- Type <CTRL-D> to stop sending when the transmit buffer is empty.
<CTRL-D> breaks the link and returns your PK-900 to
Command Mode.

<!-- PDF p.121 -->

- Type <CTRL-F> to break the link and send your Morse ID.

Your PK-900 sends your call sign in Morse code, and then shuts
off your transmitter.

- Type <CTRL-C> to return to Command Mode, then type "R" to break
the link.

The "R" command breaks the ARQ link immediately and returns your
system to AMTOR Standby.  This can be used as an "Emergency
Shutdown" if you need to take your transmitter off the air, but
it does not shut down the other station and should not be used.

#### 7.3.6 LCD Status and Mode Indicator

The front panel LCD display provides mode and status information at a
glance. This is especially useful in AMTOR operation.  The following
describe typical Status indications you will see.

Type "ARQ (SELCALL of distant station)."  The Status changes to:

MODE:     TOR ARQ PHASE
RADIO 1:  TX on

This shows that your transmitter is in the SEND condition, in the
"phasing" part of an ARQ selective call.  Your transmitter will key on
and off, sending the distant station's SELCALL.  As soon as your
PK-900 is synchronized with the distant station, the Status
changes to:

MODE:      TOR ARQ TFC
RADIO 1s:  TX on

Verify the link by typing a few <Enter>s; watch the display.  Your
traffic will now begin to flow as you type characters.  If EAS is set
ON, your typed characters are displayed as they are acknowledged by
the distant station.  The Status will change back and forth from IDLE
and TFC whenever your typing pauses.

If errors occur on the link and the distant station sends REQUEST
(request for Repeat), the Status will show:

MODE:     TOR ARQ ERROR and/or REQUEST
RADIO 1:  TX on

ERROR:    Your PK-900 has detected errors in the signals
received from the distant station

REQUEST:  Your PK-900 has received a "request for repeat"
code from the distant station

If the link fails and you lose synchronization with the distant
station your PK-900 automatically tries to re-establish
synchronization with the distant station.  The Status changes to show:

MODE:     AMTOR ARQ PHASE
RADIO 1:  TX on

<!-- PDF p.122 -->

After typing FEC, your PK-900 displays the system status:

MODE:     AMTOR FEC IDLE
RADIO 1:  TX on

As you send your traffic the Status will change back and forth from
IDLE to TFC.  Whenever you stop typing, the IDLE status is displayed.

### 7.4 AMTOR Operating Tips

The following "Special Function Characters" and immediate commands are
included for AMTOR operating convenience.
Immediate Commands from the Command Mode:

"ARQ <SELCALL>"     Starts Mode A selective call and forces Converse
"FEC"               Starts Mode B transmission and forces Converse
"SELFEC <SELCALL>"  Starts Selective Mode B transmission
"R"                 Stops sending immediately, forces AMTOR Standby
"AM"                Stops transmission, forces AMTOR Standby
"AL"                Forces re-synchronization in ALIST (AMTOR Mode A
Listen)
"L"                 Forces LETTERS case in receive
"N"                 Forces FIGURES case in receive

Special Function Characters embedded in transmitted text:

<CTRL-B>            Sends your AAB string as a HERE-IS message
<CTRL-D>            Stops sending when the transmit buffer is empty
<CTRL-E>            Sends a "Who Are You" request to the other station
<CTRL-F>            Sends call sign in Morse and shuts off transmitter
<CTRL-N>            Sends FIGURES character
<CTRL-O>            Sends LETTERS character
<CTRL-T>            Sends the TIME if the DAYTIME clock has been set

#### 7.4.1 ARQ Break-In (ACHG Command)

In Mode A (ARQ), when you're the "Information Receiving Station," you
can use the "ACHG" command to interrupt the distant station's comments.

As the "Information Receiving Station," you normally rely on the
distant station to send the "+?" to "change-over" at the end of his
comments.  ACHG is a command that forces both systems to reverse the
"Information Receiving" and "Information Sending" status of the link.

- Use the ACHG command only when really needed to interrupt the
distant station.

#### 7.4.2 Entering Your Auto-AnswerBack (AAB)

AMTOR allows you to request the identity of the station you are
conversing with by sending your PK-900 a <CTRL-E>.  This causes the
PK-900 to send a FIGS-D (WRU) request to the other station.  Many
remote Bulletin Board Stations rely on the WRU for identification.

For this reason, you should set your own Auto-AnswerBack (AAB) message
to "DE YOUR-CALL, MYSELCAL, MYIDENT".  Your PK-900 will automaticallysend the AAB message when another station requests your identity, and
then stop sending.

#### 7.4.3 Operating AMTOR with Other Modem frequencies and Shifts

All Amateur (AMTOR) and commercial (SITOR) stations that we know of
use either 170 or 200 Hz shift FSK modems.  Modem 2 (default) is
therefore the best choice for ARQ or FEC AMTOR use.  The PK-900
allows other modems to be used in AMTOR should the need arise.  The
following other modems may be selected with the MODEM command.

Radio Port 1 Modems

MODEM  1  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 45  bps
MODEM  2  AFSK Modem, 170 Hz shift, M 2125 Hz, S 2295 Hz, 100 bps
MODEM  3  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz,  45 bps
MODEM  4  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz, 100 bps
MODEM  5  AFSK Modem, 425 Hz shift, M 2125 Hz, S 2550 Hz, 100 bps
MODEM  6  AFSK Modem, 850 Hz shift, M 2125 Hz, S 2975 Hz, 100 bps
MODEM  7  AFSK Modem, 850 Hz shift, M 2125 Hz, S 1275 Hz, 100 bps
MODEM 10  AFSK Modem, 200 Hz shift, M 2110 Hz, S 2310 Hz, 300 bps

#### 7.4.4 Speed Change Not Permitted

In accordance with FCC 97.69 and international regulations, AMTOR is
operated at 100 bauds.  The PK-900 does not permit other speeds.  For
that reason, Modems 1 and 3 are not recommended.

#### 7.4.5 Echoing Transmitted Characters As Sent (EAS)

EAS has special significance in ARQ AMTOR.  If EAS is ON, you will see
characters echoed to your screen only after your partner in the AMTOR
link, has validated the previous block.  With EAS ON, the characters
appear on your screen three at a time.

- If the data scrolls across your monitor at an even rate, you can
assume that you have a good ARQ link.

- If the data hesitates or scrolls in "jerky" intermittent fashion,
that's generally a sign that the radio link is not very good.

- If the characters stop appearing on your monitor, the link is
failing or has failed.  The LCD Status display will tell you this
by showing ERROR or REQUEST nearly continuously.

#### 7.4.6 Sending Only Complete Words (WORDOUT)

Some AMTOR users like to have their words sent out only when they are
complete.  This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character.  Turning WORDOUT ON
activates this feature.  See the Command Summary for more information.

<!-- PDF p.124 -->

#### 7.4.7 Operating on the Wrong Sideband

In AMTOR operation it is important to be operating on the correct
sideband, otherwise other stations will not be able to copy you.  If
you find a station operating on the wrong sideband, you can reverse
your receive sense with the RXREV command.

Similarly, if someone tells you that you are on the wrong sideband,
you can correct your transmit signal sense with the TXREV command.
See the Command summary for more information on these commands.

### 7.5 Monitoring ARQ AMTOR Contacts with ALIST

Use the "ALIST" command to monitor ARQ traffic flowing between two
stations linked in an ARQ contact.  Your PK-900 will try to
synchronize with whichever of the two linked ARQ stations is the
Information Sending Station at the moment.

ARQ Listen operation does not give you error detection or error
correction; your PK-900 is not one of the two stations locked to
each other.  If the other two stations are enjoying a good link,
you'll probably get good copy from that link.

Your PK-900 will not print a block of data if that block contains
the same information as the previous block.  If the "ISS" (Information
Sending Station) is repeating the same block, you won't print it
twice, unless you receive an error.  If the stations you're monitoring
are sending error and RQ codes and repeating blocks of characters
across their link, you may see some repeated character blocks.  If
they're having link problems, the data on your screen can look very
strange indeed, although the two synchronized stations are getting
error-free copy.

### 7.6 AMTOR MailDrop Operation

The PK-900 allows AMTOR as well as Packet access to the MailDrop.
Messages that originate in Packet can be accessed remotely in AMTOR
and messages that originate from a remote AMTOR station can be
accessed by Packet users of your MailDrop.  This section of the manual
talks about basic AMTOR mailbox operation.  Section 7.7 will discuss
how to pass message traffic from AMTOR to Packet and vice versa.

Make sure that you understand MailDrop Operation in Chapter 5 and the
basic AMTOR operation described earlier in this chapter before putting
your AMTOR MailDrop on the air.

#### 7.6.1 Special Operating Considerations

The AMTOR MailDrop has been designed with a "Watchdog" safety feature
so that it may perform safely without constant attention.  If a remote
station is linked with your AMTOR MailDrop and no traffic is passed
for 5 minutes, the link will drop and your transmitter will shut off.

At this time however, the legality of unattended operation below 30
MHz is uncertain for US amateurs.  US amateurs must be sure to alwayshave control of their HF transmitters when any automatic device such
as the PK-900 MailDrop is in operation.

With this in mind, we have designed the AMTOR MailDrop so that it can
be disabled and then re-enabled at any time during an ARQ link simply
by turning the command TMAIL (TOR MAIL) OFF.  This allows you the
SYSOP to make your MailDrop available to other stations and still
break in to chat with remote stations at any time.  This could come in
handy should you want to provide some help or information to a remote
station using your AMTOR MailDrop.

#### 7.6.2 Settings For AMTOR MailDrop Operation

Before a remote AMTOR user can access your MailDrop, be certain that
MYCALL (on Port 1) is set to your Amateur callsign and MYSELCAL is set
to your 4-character AMTOR SelCall.  To allow CCIR 625 AMTOR access to
your MailDrop, your 7-character MYIDENT must also be entered.  Once
these commands have been entered, you must then enter the AMTOR mode.

#### 7.6.3 Starting AMTOR MailDrop Operation

Remote access to your AMTOR MailDrop is controlled by the command
TMAIL which is short for TOR MAIL.  The TMAIL command controls remote
access to the AMTOR MailDrop in the same way that the MAILDROP command
controls remote Packet access.

Turn the TMAIL command ON (default OFF) to allow remote stations to
access your MailDrop in ARQ AMTOR.  Turn TMAIL OFF to have normal ARQ
QSOs with other stations in the AMTOR mode.

#### 7.6.4 Local Logon to the MailDrop

To locally access your MailDrop use the MDCHECK command as described
in chapter 5 of this manual on MailDrop operation.

##### 7.6.4.1 Remote Logon to your AMTOR MailDrop

To the remote user, the AMTOR maildrop user interface is slightly
different from the packet interface due to the differences between the
two modes.

When CODE is set to 0 and the ITA#2 alphabet is used in AMTOR, only
UPPER case characters are sent.  If you the SYSOP set CODE to 2
enabling the Cyrillic extensions, both upper and lower case characters
can be sent and received.  See the CODE command for information and
limitations of this feature.

When a station links with your AMTOR MailDrop, your PK-900 first
identifies your station by sending your callsign and the amount of
free MailDrop memory as shown below:

DE WX7AAA (AEA PK-900) 17528 FREE.

Since AMTOR transmissions do not self-identify, your MailDrop will
force the remote user to identify in one of three possible ways.

<!-- PDF p.126 -->

The first way is automatic:
Your MailDrop will send "STAND BY" and then the WRU request to the
remote user.  Always be sure you have entered a proper Auto-Answerback
(AAB) message consisting of "QRA YOURCALL YOUR_MYSELCAL YOUR_MYIDENT"
as described earlier in this chapter.

The second way covers beginning AMTOR users:
AMTOR users who have not entered a proper Auto-AnswerBack response or
for some reason have the WRU feature disabled cannot be automatically
identified by your MailDrop.  In this case, your MailDrop will ask the
calling station to identify as follows:

After 10 seconds your MailDrop will ask the calling station to
identify by sending "QRZ? DE your callsign+?" to the calling station.

The calling station then has 3 minutes to respond with its callsign.
The ID must contain either "QRA" or "DE" and must end with "+?".

An Amateur with the call WX7BBB would send the following:

QRA WX7BBB +?

If no satisfactory ID occurs within 3 minutes from the establishment
of the link, the link is automatically shut down.

The third way covers experienced users:
Experienced AMTOR users may want to save time by simply sending QRA
followed by their callsign immediately after establishing the link.
For example station WX7BBB may simply enter the following immediately
after establishing the ARQ link.

QRA WX7BBB +?

The PK-900 then sends the user the MTEXT string if the MailDrop
message command (MMSG) is ON.  The default text is shown below:

WELCOME TO MY AEA PK-900 MAILDROP.
TYPE H FOR HELP.

#### 7.6.5 Caller Prompts

The command prompt that the MailDrop sends the remote user in AMTOR is
shortened from that used in the Packet mode and is shown below:

WX7BBB DE WX7AAA GA+?

TMPROMPT is the AMTOR MailDrop message prompt sent to a remote station
by your MailDrop.  The default prompt is:

GA subj/GA msg, '/EX' to end.

Text before the first slash is sent to the user as the subject prompt;
text after the slash is sent as the message text prompt.

<!-- PDF p.127 -->

#### 7.6.6 Monitor MailDrop Operation

The local user (SYSOP) can monitor the dialog by setting MDMON ON.
The PK-900 stays in command mode during remote MailDrop access.

#### 7.6.7 SYSOP MailDrop Commands

The MailDrop commands that you the SYSOP have access to are the same
as those described in Chapter 5 of the manual on MailDrop Operation.

#### 7.6.8 Remote User MailDrop Commands

When a remote user has logged onto your MailDrop the following
commands are available to the distant station:

A, B, H, J, K, L, R, S, V, ?.

The remote user may end a command with either +? or a carriage return.

A brief description of each command follows in the next sections.  The
description is expanded where the command operation differs from the
Packet Maildrop section found in Chapter 5.

##### 7.6.8.1 A (ABORT) (Remote only)

The "A" command aborts the listing or reading of messages by the
remote calling station as described in chapter 5.  The difference in
AMTOR is that the remote user must send the ACHG command first to
reverse the direction of the link before he can issue the Abort
command.
The remote user also has the ability to abort a command that may have
been mis-typed by typing "///" on the same line as the bad command.

##### 7.6.8.2 B (BYE)

The "B" command logs the remote station off the MailDrop.  In AMTOR
the remote station may simply gracefully shut down the link with the
RECEIVE character (<CTRL-D>) or the CWID character (<CTRL-F>).

##### 7.6.8.3 H (HELP)

The "H" command sends the remote station a help list of the available
commands shown in Chapter 5.

##### 7.6.8.4 J (JLOG) (Remote only command)

The "J" command sent by the distant station will cause the MailDrop to
send the list of stations who have logged in to your AMTOR MailDrop.

##### 7.6.8.5 K n (KILL n [Mine])

The "K n" command deletes message number "n" from the MailDrop as
described in Chapter 5.

<!-- PDF p.128 -->

##### 7.6.8.6 L (LIST [Mine])

The "L" command shows the remote user only a list of the messages he
or she may read as described in Chapter 5.

##### 7.6.8.7 R n (READ n [Mine])

The "R n" command lets the remote user read any of the message numbers
displayed in the LIST command.  The command operates as described in
Chapter 5 except that the column headers are not displayed.

##### 7.6.8.8 S callsign (SEND callsign)

Due to the nature of AMTOR, character errors may occur at any time, so
extra safeguards are built into the system.  In AMTOR, the MailDrop
echoes the actual SEND command, then asks for confirmation by sending
"CFM YES/NO+?".  If the remote user's reply is "N", the MailDrop
cancels the SEND command and gives the "GA" command prompt instead.
If the reply is "Y", the message can then be sent as shown below.

In the SEND command, the words "AT," "FROM" and "BID" must be used in
place of the "@," "<" and "$" signs used in packet.  Hierarchical
addresses are also supported in AMTOR mode, but not forwarding.  You
the SYSOP may edit any message so it can be forwarded in Packet mode.

Since <CTRL-Z> is not available in the AMTOR character set, the "/EX"
command or "+?" must be used to end all AMTOR MailDrop messages.
After the "/EX" or "+?" has been detected, the MailDrop will confirm
that the message has been sent by returning the message "FILED MSG n"
to the remote user.  An example of sending a message is shown below:

WX7BBB DE WX7AAA GA+?                            {MailDrop prompt}
s wx2zzz at wx2yyy+?                             {User's SEND command}

S WX2ZZZ AT WX2YYY                       {MailDrop echoes SEND command
18340 FREE.  CFM YES/NO +?                and awaits confirmation}
y +?                                      {User confirms}

GA SUBJ+?                                    {MailDrop Subject prompt}
Going to the Hamfest? +?                     {User enters Subject}
GA MSG, '/EX' TO END.+?                      {MailDrop Send prompt}
I haven't heard from you and wondered if               {Message text}
you are going to the Hamfest next month?               {Message text}
Hope to see you there. 73                              {Message text}
/ex +?                                       {User ends message}

WX7BBB DE WX7AAA FILED MSG 1 GA+?                 {MailDrop prompt}

### 7.7 Simultaneous AMTOR and Packet Operation

Your PK-900 can operate AMTOR on Radio Port 1 and HF or VHF Packet
on Radio Port 2 at the same time.  With this feature you won't miss
any local Packet activity while operating AMTOR.

Before the second radio port can be used for packet operation, a
modem must be selected for the second port.  Use the MODemcommand to select either or both modems, for example, MOD 1/4.  For
Radio Port 2, the command would be MOD /4 to select modem 4.

The Radio Port two modems are:

MODEM  1  AFSK Modem, 200  Hz shift, Mark=1270 Hz, Space=1070 Hz
MODEM  2  AFSK Modem, 200  Hz shift, Mark=2225 Hz, Space=2025 Hz
MODEM  3  AFSK Modem, 1000 Hz shift, Mark=1200 Hz, Space=2200 Hz
MODEM  4  AFSK Modem, 1000 Hz shift, Mark=1200 Hz, Space=2200 Hz eq
MODEM  5  AFSK Modem, 200  Hz shift, Mark=980  Hz, Space=1180 Hz
MODEM  6  AFSK Modem, 200  Hz shift, Mark=1650 Hz, Space=1850 Hz
MODEM  7  AFSK Modem, 800  Hz shift, Mark=1300 Hz, Space=2100 Hz
MODEM  8  AFSK Modem, 800  Hz shift, Mark=1300 Hz, Space=2100 Hz eq
MODEM  9  AFSK Modem, Direct FSK 9600 baud internal option.
MODEM 10  External option, user installed.

#### 7.7.1 Selecting  Modems

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
cmd:

Any modem from the list may be loaded with the MODEM command, but
only the 100 baud modems listed will operate in AMTOR.  For example,
to operate AMTOR on radio port 1 and 1200 bps VHF packet on radio
port 2 you must select modems for radio ports 1 and 2. First, enter
the Command Mode of the PK-900 and then type MODEM 2/4 as shown below:

MODEM 2/4 <Enter>

The PK-900 will respond with the following:

MODem was  x/x (The previous modems)
MODem now  2/4

<!-- PDF p.130 -->

#### 7.7.2 Displaying Received Data

The Radio command may be used to disable port 2.  This may be
desirable when operating RTTY and you do not want to be disturbed with
any packet signals that may be received on radio port 2.  To disable
port 2, enter:

RADIO ON/OFF <Enter>  or  RADIO 1/0 <Enter>

For dual port operation, type

RADIO ON/ON <Enter>  or  RADIO 1/2 <Enter>

The PK-900 sorts and displays received data from each Radio Port
using the same technique as multi-connect packet operation described
in Chapter 4.  That is, when operating on one port and the other
port becomes active, the displayed data from the inactive port is
shown prefaced by the "channel designator" followed by a colon (:).
Recall that Radio Port 1 is designated by "logical" channels from 0-9
and that Port 2 is designated by "logical" channels A-Z.

#### 7.7.3 Switching Between Ports

If you are using an AEA PAKRATT program, switching between Radio Ports
is described in the program manual.  If you are using a terminal
program, this section describes how to direct your transmitted text.

Switching between AMTOR on Port 1 and Packet on Port 2 is similar to
switching between Packet and Packet.  If you have not yet read through
the Switching Between Radio Ports section of Chapter 4, please do so
now and define a CHSWITCH character before reading the example below.

Recall from chapter 4 that the channels on Port 1 are labeled 0-9 and
the channels on Port 2 are labeled A-Z.  To select Radio Port 1
(AMTOR) press the CHSWITCH character you defined, followed by the
number 0.  To select Radio Port 2 (Packet), press the CHSWITCH
character, followed by a letter from A-Z.

For example, you are conversing with an AMTOR station and are in the
middle of a QSO when a station on VHF connects to you.  The following
shows how your screen would look and suggests how you might handle
such an occurrence.  The underlined text is the text that you type.

Hello Jim, you are printing solid       { You send the AMTOR station
here and have an S7 signal.+?             a signal report }

THANKS BOB, YOUR ALSO A SOLID S7        { The other station responds
HERE AS WELL.+?                            with a signal report }

A:*** CONNECTED to WX7EEE               { WX7EEE connects to you on
VHF (Radio Port 2) }

Thanks for the signal report Jim        { You make another transmission
only running 100 watts here.+?            to Jim on HF AMTOR }

<!-- PDF p.131 -->

Hey Bob, I'm going to the hamfest       { Your friend on VHF packet
this weekend if you want a ride.          wants to go to the hamfest }

|AHello Mike, I am on HF AMTOR          { You switch to Port 2 by
talking to a station in Boston.           typing |A to answer on VHF }

0:WELL BOB, I HAD BETTER BE GOING TO    { Jim on HF AMTOR signs off
BED.  WORK STARTS PRETTY EARLY 73.+?      with you }

|073 Jim, it was nice meting you.       { You switch back to HF with
WX1AAA de WX7BBB SK <CTRL-D>              |0 and sign off with Jim }

As you may have noticed, communicating with different modes on the two
Radio Ports at the same time is almost identical to the method used in
chapter 4 for Packet and Packet operation.  Let's discuss the sample
QSOs above to see how the Port switching occurs.

The first text we see in the sample above is the signal report you are
sending to Jim, the HF AMTOR station you are communicating with.  This
example assumes you have already set up the ARQ AMTOR contact with the
ARQ command discussed earlier in the chapter.  When you are through
sending your signal report to Jim on AMTOR, you turn the link over to
become the IRS by sending the "+?".  The PK-900 then responds by
sending a blank line to the display to break up the received text.

The next text you see is your signal report received from Jim on
AMTOR.

You are in the middle of a QSO with Jim on AMTOR and all of a sudden
your friend WX7EEE connects to you.  WX7EEE has connected on Radio
Port 2 (VHF) which is shown by the "A:" before the connect message.
Remember that the 26 channel designators for Radio Port 2 are A-Z.

Before you respond to WX7EEE on VHF, you send a transmission to Jim on
AMTOR telling him how much power your transmitter is running.  When
you are finished with your text, you again command the PK-900 to be
the IRS by sending a "+?".

After making this transmission on AMTOR, you see WX7EEE on VHF has
offered you a ride to the hamfest.  We know this text is from Radio
Port 2 since the previous packet displayed by the PK-900 was the
"A:*** CONNECTED" message from Port 2.

Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF.  This way he will understand
that it may take you a little longer to respond to his packets.
Before you can send data to Radio Port 2, you must switch to this Port
with "|A" or the text you type will be sent to Radio Port 1 on AMTOR
when Jim turns the link over to you again.

You receive a transmission from Jim on AMTOR telling you he needs to
sign off to go to bed.  The "0:" in front of the text shows this was
received on Radio Port 1.

Now you want to sign off with Jim on HF.  Again, first you must switch
to Radio Port 1 with the "|0" since your last transmission was
directed to Port 2.  Now you can make your final transmission to Jimending with his callsign followed by your callsign.  This time when
you are through typing your text, you send a <CTRL-D> to the PK-900
which breaks the ARQ link and returns Radio Port 1 to AMTOR Standby
after the text has been sent.

#### 7.7.4 More Thoughts on Port Switching

One problem of having more than one Radio Port is remembering which
port you are currently using.  In the dual port sample QSOs above,
this was not a problem, but after it has been hours or days since you
have used your PK-900, you may forget which port you last used.

With AEA Pakratt Software programs, the on-screen status will always
show which port you are using so this is not a problem.  With other
programs, you will have to query the PK-900 with the CSTATUS SHORT
command.  The CSTATUS command displays the status of the logical
channels of Port 1 and Port 2 of the PK-900.  The CSTATUS SHORT
command displays the status of the active channel and any packet
channels that are connected.  After completing the sample QSOs above,
the PK-900 would display the following.

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

#### 7.7.5 Dual Port AMTOR/Packet Maildrop Operation

Your PK-900 MailDrop will operate both on Packet and AMTOR and can
be used to allow message traffic that originates on AMTOR to be
reverse-forwarded into the Packet network.  Similarly, traffic
originating on Packet may be picked up by remote stations on AMTOR.

Before you begin dual port Packet/AMTOR MailDrop operation, be sure
that you are familiar with the operation of Packet, AMTOR and the
MailDrop as described in Chapters 4, 5 and 7 of this manual.  Also be
sure that appropriate modems are selected.  Any experience you
have with each of these modes will help when setting up a dual port
MailDrop system.

<!-- PDF p.133 -->

##### 7.7.5.1 Packet MailDrop Command Settings

First set up the packet side of the MailDrop (on Radio Port 2) by
setting MYCALL and MYMAIL.  You may also want to enter a custom MTEXT
to let others know about the AMTOR feature.  Be sure to set 3RDPARTY,
MDMON, MDPROMPT, and MMSG, as desired.  If you will be reverse
forwarding to a full-service BBS, you must set HOMEBBS to the callsign
of that BBS.  Do not forget to turn MAILDROP ON.  As a test you should
connect to your own maildrop via a digipeater or network node to make
sure the radio link is working properly and the user prompts are what
you desire.

##### 7.7.5.2 AMTOR MailDrop Command Settings

Once the Packet side is working properly, the AMTOR side of things on
Radio Port 1 must be configured.  First be sure that MYSELCAL and
MYIDENT are entered properly.  You may want to customize the AMTOR
MailDrop prompt (TMPROMPT) or compose a message to ALL that tells
remote users about your system.  Finally, remember to turn TMAIL ON
and enter the AMTOR mode of the PK-900 to start the AMTOR MailDrop.

##### 7.7.5.3 Dual Port MailDrop Operation Notes

With the above parameters set, packet connections to the MYMAIL
callsign on Radio Port 2 will be sent to your MailDrop if you or a
remote AMTOR station is not using it.  If you or any other station is
using your MailDrop, the remote user attempting the packet connection
will be sent a "*** Busy" message.

Similarly, a remote AMTOR station linking to you will be given access
to your MailDrop provided no one else is using it.  If you or a remote
packet station is using your MailDrop, the AMTOR station may link to
you, but will not be given access to your MailDrop.  For this reason,
you may wish to disable Radio Port 1 (by turning the RADIO parameter
to 0/2) when logging into your own MailDrop for maintenance.  This
will prevent remote AMTOR stations from linking with you.

If you will be Reverse Forwarding messages into the Packet network, be
sure to check your MailDrop often for new messages.  Remember that you
must use the Edit command in the MailDrop to select which messages
will be Reverse Forwarded.

Note:     At this time unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary
Authorization (STA) from the FCC.  US amateurs must be sure
to have control of their HF transmitters when any device
such as the PK-900 AMTOR/Packet MailDrop is in operation.

### 7.8 AMTOR Switching-Time Considerations

For operation in AMTOR Mode A (ARQ), your transceiver or transmitterreceiver combination must be able to change between transmit and
receive within 20 milliseconds.  Most semiconductor-based radios can
easily meet this specification.  Many older tube-type radios that use
electromechanical relays also operate very well in AMTOR Mode A (ARQ).

<!-- PDF p.134 -->

If the changeover from transmit to receive is too long, the minimum
working distance is extended; the signal to the distant station will
arrive before the station has switched back to receive.  However, if
the transmitting station is further away, the transmission time over
the propagation path will delay the arrival of the signal until after
the station has switched to receive.  For this reason, you may be able
to "Link with" stations across the country, but not across town.

If the receiving station's changeover from transmit to receive is too
slow, the transmitting station delay between "PTT" and "data send" can
be extended.  See the ADELAY command in the Command Summary to adjust
the PK-900's AMTOR timing characteristics to compensate for this.

#### 7.8.1 Suggested AMTOR Operating Settings

If you have trouble synchronizing with another AMTOR ARQ station, try
some of the following operating tips before calling AEA or deciding
that your radio equipment needs modifications:

- Try to work the distant station on Mode B (FEC) to establish that
the other station's system is fully functional.

- Don't use VOX control - use the PTT line from your interface.

- Turn off the AGC circuit - use the RF gain control to prevent
receiver blocking on stronger signals.

- Turn off all compression or other audio processing.

- Keep the AFSK audio input level to the microphone circuit as low
as possible - avoid over-driving the audio input stages.

- Disable the ALC circuit or reduce excessive ALC action; use more
effective RF antenna loading to adjust output power levels.

#### 7.8.2 Possible Areas for AMTOR Performance Improvement

If switching-time problems persist, you may have to make changes in
the radio to eliminate excessive time delays:

- Remove large decoupling capacitors from the Push-To-Talk line to
allow faster PTT (transmitter) activation;

- Improve power supply decoupling, especially in audio stages.

- Do not use squelch.

In case you can't solve your radio's switching-time problems, please
call AEA Technical Support Department (see the front of this manual).

<!-- PDF p.135 -->

### 7.9 NAVTEX Operation

NAVTEX is an international system which stands for NAVIGATIONAL TELEX.
It is a direct printing service designed to distribute navigational
and meteorological warnings and other urgent information to ships.
To enter the NAVTEX mode, simply type "NAVTEX" at the command prompt.

The ARRL has also adopted this format for transmitting bulletins.  In
amateur radio this same format is starting to be referred to as AMTEX.
AMTEX transmissions can be found on ARRL bulletin frequencies.

NAVTEX is broadcast in Mode-B AMTOR (SITOR) on a frequency of 518 kHz.
NAVTEX may be selectively monitored, so you will see only information
of interest and never see the same message twice.  It is this unique
feature of NAVTEX that the PK-900 uses with the NAVSTN and NAVMSG
commands to allow the user to monitor only messages of importance.

NAVTEX/AMTEX messages are prefaced by the characters "ZCZC" and
then a four character Preamble as diagramed below.

ZCZC AA99
||||
|||+-Serial Number 2nd Digit--+
||+--Serial Number 1st Digit--+
|+---Message Classification (A to Z)
+----NAVTEX Station Identification (A to Z)

The first character of the Preamble is a letter that identifies the
NAVTEX transmitter.  Transmitter Identification letters can be any of
the characters A through Z.  This limits the number of NAVTEX stations
in an area to 26.   The NAVSTN Command can be used to selectively
monitor or reject certain NAVTEX transmitters.

The second character of the Preamble is the Message Classification.
The NAVMSG command is used to selectively monitor or reject any of the
NAVTEX message classes shown below:

A. Navigational Warnings
B. Meteorological Warnings (Storm Warnings)
C. Ice Reports
D. Search and Rescue Information
E. Weather Forecasts
F. Pilot Service Messages
G. DECCA System Information
H. LORAN-C System Information
I. Omega Systems Messages
J. SATNAV System Messages
K-Z. Reserved for future use

The exception to this is that message classes A, B and D CANNOT be
excluded and will always be copied if the transmitting station is
enabled by NAVSTN.

The last two numbers form a serial number from 00 through 99 that is
different for each message.  The PK-900 remembers the Preamble of
the 200 most recent messages and will not re-print a message that has
the same preamble if it has already been received without many errors.

<!-- PDF p.136 -->

> [No extractable text on this page — figure, schematic, or blank.]
