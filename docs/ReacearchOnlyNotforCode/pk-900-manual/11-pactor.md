# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Chapter 11 — PACTOR Operation (PDF p.157–178)

<!-- PDF p.157 -->

### 11.1 Overview

PACTOR is a relatively new amateur data communications mode.  It was
developed in Germany by Hans-Peter Helfert, DL6MAA and Ulrich Strate,
DF4KV.  PACTOR combines some of the best features of both AMTOR and
packet as well as providing a few new features.  PACTOR operates at
100 bps or 200 bps depending on radio conditions.  PACTOR also
contains a 16 bit CRC to provide near error-free operation and can
also selectively use a data compression scheme (Huffman encoding) to
increase the throughput when transmitting text.  PACTOR uses an 8-bit
word, allowing the use of the full ASCII character set.  You should
use both upper and lower case in your PACTOR transmissions.

When data blocks are repeated in the case of an error, the receiving
unit can often combine the information in the repeated blocks to
provide a good block without the need of receiving a perfect block.
This scheme is called memory ARQ.

Like AMTOR and packet, PACTOR has two basic modes of operation, an ARQ
mode (Automatic ReQuest for reception) and a non-linked mode used for
CQ calls and roundtable operation.

- ARQ PACTOR is a handshaking protocol that allows two stations to
communicate in a near error-free fashion.  A PACTOR ARQ exchange
consists of a 0.96 second burst of data from the information
sending station followed by a short burst from the data receiving
station that is an acknowledge (ACK) or non-acknowledge (NAK).
The NAK is sent by the receiving station when the CRC data test
indicates an error in the data block.  Like packet, PACTOR is
mark-space polarity independent, although for different reasons.
The PACTOR protocol alternates the data polarity with every
transmission to reduce the effects of interference on the
received signal.

- The unproto(col) mode of operation is a non-linked type of
operation.  It is used for roundtable operation or for calling
CQ.  The unproto mode repeats the data blocks a selectable number
of times and can use either 100 or 200 bps.  It also uses the CRC
error check.

### 11.2 Where to Operate PACTOR

Before you can operate PACTOR, you must first know where the activity
occurs.  Most PACTOR operation occurs on the 20-meter amateur band
between 14.065 and 14.085 MHz.  PACTOR activity can be found on the
other HF amateur bands as well and is most often located between 65
and 90 kHz up from the bottom of the band as it is on 20 meters.
On 80 meters, most PACTOR will be found between 3660 and 3690 KHz.
PACTOR is not sensitive to to the sideband used, but we recommend
using LSB as in RTTY and AMTOR operating modes.

<!-- PDF p.158 -->

### 11.3 PK-900 PACTOR Parameter Settings

PACTOR is a bit more complex than Baudot or ASCII operation.  PACTOR
operation requires you to have MYPTCALL or MYCALL entered before you
can operate.  If you do not enter MYPTCALL, the call in MYCALL will be
used as the default callsign.  PACTOR stations can't use the
SubStation IDentification number (SSID) in MYCALL.

#### 11.3.1 Entering Your Callsign (MYPTCALL)

If you have not already done so, enter your call for radio port 1
after the command prompt (cmd:) using the MYPTCALL command.  For
example, if your call is WX5FAP, you would type:

cmd:MYPTCALL WX5FAP <Enter>

The PK-900 will respond with:

MYPTCALL   was  PK900
MYPTCALL   now  WX5FAP

If you have not entered your call with the MYPTCALL command, the
PK-900 will default to the call in MYCALL.  MYCALL does not allow
punctuation other than the dash and SSID.  MYPTCALL does allow
punctuation in the call.  This allows you to properly identify when
operating portable, e.g. ZL/K6RFK.

If you do not enter a call using MYPTCALL or MYCALL, the PK-900
will not allow transmission as the default call PK900 is not a valid
call.  The error message "Need MYCALL" will be displayed if
transmission is attempted.

#### 11.3.2 Enter the PACTOR Mode

If you are using the AEA PAKRATT for WINDOWS program, follow the
instructions in the program manual to enter the PACTOR mode.  The
current AEA PAKRATT for DOS does not support PACTOR except in the
dumb terminal mode.  AEA will have PC PAKRATT for DOS with PACTOR
available in later in 1993.

If you are using a terminal or a computer with a terminal emulation
program, simply type "PACTOR" or "PT" from the Command Mode followed
by the <Enter> key to enter the PACTOR mode.  The PK-900 responds by
displaying the previous mode, for example packet:

Opmode   was PAcket
Opmode   now PACTOr

Your PK-900's front panel LCD status display will show that you are
in the PACTOR Standby mode on RADIO port 1, and the COMMAND LCD will
be lit.

<!-- PDF p.159 -->

### 11.4 HF Receiver Settings

Set your HF receiver (or transceiver) to Lower Sideband (LSB) unless
you connected your PK-900 through the direct FSK keying lines. If you
are using a transceiver with a RTTY or Packet mode and you have the
PK-900 connected for direct FSK, keep in mind that PACTOR uses 200
Hz shift.  If you radio has a 200 Hz shift FSK for packet use, you may
use direct FSK.  If your radio has only 170 Hz shift capability, you
should use the TX audio from your PK-900 to drive the microphone input
of your radio.  You may use either USB or LSB.  By convention, LSB is
generally used.  Adjust the volume to a comfortable listening level.

### 11.5 Tuning in PACTOR Stations

Tuning in PACTOR stations properly is critical to successful
operation.  Since HF PACTOR stations use 200 Hz Frequency Shift
Keying (FSK) to send data, tuning accuracy is very important.
The BARgraph command will select the type of tuning display.  See the
command summary Appendix for a description of BAR.  Note the different
displays below.  Follow the procedure below for the best results.

- Make certain your HF receiver is either in LSB or FSK/RTTY/Packet
depending on your PK-900 set-up.

- Turn any IF-Shift and Passband-Tuning controls to the Center or
OFF position.

- Enter the command "PTLIST" for PACTOR Listen to monitor both
linked and unproto PACTOR data.

- Tune your receiver carefully between 14.065 and 14.085 MHz (or
another band where you know there is PACTOR activity) and listen
for the data burst of ARQ PACTOR or the steady data of the
unproto mode stations.  Be sure that the THRESHOLD CONTROL is
turned far enough clockwise to activate the DCD LCD indicator
during the PACTOR data burst.

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

<!-- PDF p.160 -->

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

- Adjust the PK-900 front panel THRESHOLD control so that the port
1 DCD LCD lights when a properly tuned PACTOR station is being
received.

After you have a PACTOR station tuned in, you should start seeing
the copy on your screen.

<!-- PDF p.161 -->

### 11.6 Operating on PACTOR

Make sure your PK-900 is adjusted for your SSB transmitter as
described in section 3.5 and 3.5.2 of this manual before transmitting.
These are very critical adjustments.  If your PK-900's AFSK
level and transmitter microphone gain are not adjusted properly, other
stations will not be able to copy your signals.  Check your plate or
collector current or the power output of your rig before transmitting.

#### 11.6.1 Going On The Air

Make sure your transmitter and antenna are tuned and adjusted for the
band and operating frequency you are using.  Before you transmit, you
must decide if you are going to "call CQ" or answer someone's CQ call.

#### 11.6.2 Calling CQ in Unproto PACTOR

If you plan to call CQ, you must do so in the unproto PACTOR mode.

This is required since an ARQ PACTOR transmission requires another
station to "link" with.  If you are using the AEA PAKRATT for WINDOWS
program, see the program manual to place the PK-900 into unproto
transmit.

If you are using a terminal or terminal emulation program, the
following will place your PK-900 and transceiver into the Unprotocol
transmit mode.

- Make sure that your transmitted text will go to Port 1 by
pressing the CHSWITCH character defined in Chapter 4  followed
by the number 0.

- Type "PTSEND" then press the <Enter> key to key your transmitter
and automatically enter PACTOR unproto transmit mode.

As soon as you type the <Enter> key you will be transmitting!  At this
point you are also in the CONVERSE mode and anything you type will be
sent in unproto PACTOR by your transmitter.

- Type in your CQ message.  Make sure you include YOUR Callsign.
An example is shown below:

CQ CQ CQ CQ CQ CQ CQ DE N7ML
CQ CQ CQ CQ CQ CQ CQ DE N7ML
CQ CQ CQ CQ CQ CQ CQ DE N7ML
CQ CQ CQ CQ CQ CQ CQ DE N7ML  K
<CTRL-D>

- Type <CTRL-D> at the end of your CQ call (Hold the "CTRL" key
down while typing the D).  The <CTRL-D> puts both your radio
into the receive mode and your PK-900 into the PACTOR standby
mode where it is ready for an ARQ call.

PACTOR standby is different than the PACTOR listen mode.  In
PACTOR standby,your PK-900 is ready to receive ARQ PACTOR
connects, but you will not be able to monitor other stations.

<!-- PDF p.162 -->

- Wait a bit to see if you get a response.  Your transmitter will
begin to key on and off and you will see a CONNECTED message when
someone calls you in ARQ PACTOR.  If you do not get a response,
you can repeat the above procedure or you can go look for other
PACTOR stations as described in the next section.

#### 11.6.3 Answering an Unproto PACTOR CQ

You must be in the PACTOR Listen (PTL) mode to monitor other PACTOR
stations.  When you are in the PACTOR Listen mode, you can monitor
both the 1 second "chirps" of connected PACTOR stations, and the
continuous transmissions of unprotocol PACTOR stations calling CQ.

Normally when you see a station calling CQ in unproto PACTOR, you will
want to answer him using ARQ PACTOR.  Remember that ARQ PACTOR is the
protocol that has the smallest chance of transmission errors.

Let's assume you hear N7ML calling CQ.  To answer, do the following:

- If you are using an AEA PAKRATT for WINDOWS program, check the
program manual for instructions on starting an ARQ PACTOR
contact.

- If you are using a terminal or terminal emulation program, simply
type "PTC N7ML<Enter>" to start a linked or connected PACTOR
contact.  After your PK-900 has locked or synchronized with the
distant station, which will be indicated by the LCD display
changing from PHASING to IDLE, you will see a CONNECTED message
on your screen.  You may then begin your conversation.

N7ML DE YOURCALL...etc

- When you finish typing your comments or traffic to the other
station and wish to let the distant station transmit, you will
need to type "KKK" or "BTU" to let the other station know that
you are going to change the link direction.

- Then, type a <CTRL-Z>  (Hold the "CTRL" key down while typing
the Z) to turn the link over to the other station.

<CTRL-Z> is the character defined by the PTOVER command that
switches your system from being the Information Sending
Station (ISS) to the Information Receiving Station (IRS) and
switches the distant system from being the information receiving
station to the information sending station.

The FCC requires station identification once every ten minutes.  It's
sufficient to begin with "QRA (mycall)" or end your transmission with
"QRA (mycall)" before the <CTRL-Z> changeover code, or use the
<CTRL-B> "HERE-IS" to send your own Auto-AnswerBack message.  For the
"HERE-IS"  command to function, you must have your AAB text entered.
See appendix A for the AAB command.

<!-- PDF p.163 -->

##### 11.6.3.1 Ending an ARQ PACTOR Contact

When you've finished your "final finals" to the distant station and
both stations are ready to end the PACTOR ARQ contact, you can end
the contact and terminate the link in several different ways:

- Type <CTRL-D> to stop sending after the transmit buffer is empty.
<CTRL-D> breaks the link and returns your PK-900 to Command Mode
in PACTOR Standby.  This is the best way to end a PACTOR contact.

- Type <CTRL-F> to stop sending after the transmit buffer is empty,
send your Morse ID and return to PACTOR Standby and Command Mode.

Your PK-900 sends your call sign (in MYCALL) in Morse code, and
then shuts off your transmitter and returns to PACTOR Standby
and Command Mode.  This is the best way to end a contact if you
want to identify your station in Morse code as well.

- Wait until all the text has been sent, then type <CTRL-C> to
return to Command Mode, then type "R" to break the link.  The
PK-900 will go into the PACTOR Standby mode.

- Type <CTRL-C> to return to Command Mode, then type "R  <Enter>
R <Enter>" to break the link immediately!  If there are
characters left in the transmit buffer, they will not be sent.
This method should only be used for an emergency shutdown as it
does not send the control signal to the other station that
informs it you are shutting down.  As a result, the other station
will continue to send until its internal timer turns it off.

#### 11.6.4 Long Path Contacts

If the station you wish to contact is more than half way around
the  world, i.e. a long path station, a special connect command
is used to lengthen the PACTOR timing.  In this case, precede the
station's call with the exclamation point:

PTCONN !N7ML <Enter>

If your station doesn't link within a period of time determined
by the ARQTMO command (default 60 seconds), your station will
stop transmitting.

<!-- PDF p.164 -->

#### 11.6.5 LCD Status and Mode Indicator

The front panel LCD display provides mode and status information at a
glance. This is especially useful in PACTOR operation.  The following
describes typical status indications you will see.

Type "PTCONN (CALLSIGN of distant station)."  The status changes to:

LCD:   PACTOR ARQ PHASE,  TX and CONNECT

This shows that your transmitter is in the SEND condition, in the
"connect" part of an ARQ connect call.  Your transmitter will key on
and off sending the distant station's connect request.

As soon as your PK-900 is synchronized with the distant station, the
status changes to:

LCD:   PACTOR ARQ TFC, TX and CONNECTED

Verify the link by typing <Enter> a few times and watch the display.
Your traffic will now begin to flow as you type characters.  If EAS
(Echo As Sent) is set ON, your typed characters are displayed as they
are sent.  The status will change back and forth from IDLE and
TRAFFIC whenever your typing pauses and resumes.

When errors occur on the link and the distant station sends REQUEST
(request for repeat), the status will show:

LCD:   PACTOR ARQ ERROR and/or REQUEST, TX and CONNECTED

ERROR:    Your PK-900 has detected errors in the signals
received from the distant station.

REQUEST:  Your PK-900 has received a "request for repeat"
code from the distant station.

If the link fails and you lose synchronization with the distant
station the PK-900 goes to standby and the status shows:

LCD:   PACTOR, STANDBY

For the unproto mode:

After typing" PTSEND", to start an unproto transmission, your PK-900
displays the system status:

LCD:   PACTOR FEC IDLE, TX

As you send your traffic the status will change back and forth from
IDLE to TRAFFIC.  Whenever you stop typing, the IDLE status is
displayed.

<!-- PDF p.165 -->

### 11.7 PACTOR Operating Tips

The following "Special Function Characters" and immediate commands are
included for PACTOR operating convenience.

Immediate Commands from the Command Mode:

| Command | Description |
|---|---|
| `PT` | Selects the PACTOR mode |
| `PACTO` | Selects the PACTOR mode as above |
| `PTCONN <CALL>` | Starts a linked connect and forces Converse |
| `PTSEND` | Starts an unproto transmission and forces Converse |
| `R` | Stops sending immediately, forces PACTOR Standby |
| `PTLIST` | Allows reception of both unproto and linked transmissions |
| `PTHUFF <ON\|OFF>` | Off — prevents Huffman compression; On — allows automatic use of compression |
| `PT200 <ON\|OFF>` | Off — prevents 200 baud operation; On — allows automatic speed selection |
| `PTOVER <$HH>` | Selects the changeover character. Defaults to `<CTRL-Z>` (`$1A`) |

Special Function Characters embedded in transmitted text:

| Character | Description |
|---|---|
| `<CTRL-B>` | Sends your AAB string as a HERE-IS message |
| `<CTRL-D>` | Stops sending after the transmit buffer is empty |
| `<CTRL-E>` | Sends a "Who Are You" request to the other station |
| `<CTRL-F>` | Same as `<CTRL-D>` but sends your callsign in Morse |
| `<CTRL-T>` | Sends the TIME if the DAYTIME clock has been set |
| `<CTRL-Z>` | Changes your PK-900 from send (ISS) to receive (IRS) |

#### 11.7.1 ARQ Break-In (ACHG Command)

In the linked or connected mode, (ARQ), when you're the "Information
Receiving Station," you can use the "ACHG" command to interrupt the
distant station's comments.

As the "Information Receiving Station," you normally rely on the
distant station to send the <CTRL-Z> to "change-over" at the end of
his comments.  ACHG is a command that forces both systems to reverse
the "Information Receiving" and "Information Sending" status of the
link.

- Use the ACHG command only when really needed to interrupt the
distant station.

#### 11.7.2 Entering Your Auto-AnswerBack (AAB)

AEA PACTOR allows you to request the identity of the station you are
conversing with by sending your PK-900 a <CTRL-E>.  This causes the
PK-900 to send an inquiry (WRU) request to the other station.

For this reason, you should set your own Auto-AnswerBack (AAB) message
to "DE YOUR-CALL".  Your PK-900 will automatically send the AAB
message when another station requests your identity, and
then stop sending.

<!-- PDF p.166 -->

#### 11.7.3 Operating PACTOR with Other Modem frequencies and Shifts

All PACTOR operation uses 200 Hz shift FSK modems.  Modem 4 (default)
is therefore the best choice for ARQ or unproto PACTOR use.  The
PK-900 allows other modems to be used in PACTOR should the need arise.
The following other modems may be selected with the MODEM command.

Radio Port 1 Modems

1: FSK 45 bps 170: 2125/2295  (not recommended)
2: FSK 100 bps 170: 2125/2295
3: FSK 45 bps 200: 2110/2310  (not recommended)
4: FSK 100 bps 200: 2110/2310 (default modem RECOMMENDED)
5: FSK 100 bps 425: 2125/2550
6: FSK 100 bps 850: 2125/2975
7: FSK 100 bps 850: 1275/2125
9: FSK 2400 bps 800: 1300/2100
10: FSK 300 bps 200: 2110/2310
11: FSK 1200 bps 1000: 1200/2200

#### 11.7.4 Automatic Speed Change

If the command PT200 is set on, the linked PACTOR mode will
automatically change the transmitted data rate from 100 bps to 200 bps
if a certain number of error-free 100 bps packets are received in a
row. If the error rate at 200 bps is excessive, the data rate will
automatically revert to 100 bps.  There may be some propagation
conditions that will cause the system to vacillate between the two
data rates.  This may be prevented by setting PT200 to OFF, which will
force 100 bps operation.  See the UCMD command to control these
thresholds.

#### 11.7.5 Echoing Transmitted Characters As Sent (EAS)

EAS (Echo As Sent) operates the same as in ARQ AMTOR.  If EAS is on,
you will see characters echoed to your screen only the first time your
PK-900 sends them.  If the data is not acknowledged by the receiving
station and is re-transmitted, the characters are not echoed again.
With EAS OFF, characters are echoed to your screen as you type them.
With EAS ON:

- If the data scrolls across your monitor at an even rate, you can
assume that you have a good ARQ link.

- If the data hesitates or scrolls in "jerky" intermittent fashion,
that's generally a sign that the radio link is not very good.

- If the characters stop appearing on your monitor, the link is
failing or has failed.  The Status display will tell you this
by showing ERROR or REQUEST nearly continuously.

<!-- PDF p.167 -->

#### 11.7.6 Sending Only Complete Words (WORDOUT)

Some PACTOR users like to have their words sent out only when they are
complete.  This allows the word you are currently typing to be edited
as long as you have not typed a <Space> character or punctuation.
Turning WORDOUT ON activates this feature.  See the Command Summary
for more information.

#### 11.7.7 Operating on the "Wrong Sideband"

PACTOR, like packet is mark-space polarity insensitive.  Once linked
the PACTOR protocol alternates the data polarity every transmission.
A specific header synchronizes the system during non-linked operation.
For this reason, there is no "wrong" sideband.  You may operate on
either LSB or USB.  If you are going to change to other modes, for
example Baudot,  then LSB must be used.  It is suggested that LSB be
used to make mode changes simple as well as keeping the radio dial
frequency reading consistent with other users.

If you are using a radio that has direct FSK inputs, keep in mind that
PACTOR uses 200 Hz shift and most direct FSK capable radios are set
for 170 Hz shift.  If the 170 Hz shift can not be adjusted to 200 Hz,
use the TX audio from the PK-900 to drive the microphone input in LSB.

#### 11.7.8 Little Used PACTOR Commands

There are four seldom-used PACTOR commands that are accessible with
the UCmd command.  This command is of the form UCmd n x, where n is
the UCmd number and x is the setting.  Several examples are shown in
the use of UCmd:

UCMD 5         Will show the current setting of command 5
UCMD 4 10      Will set command 4 to the value 10
UCMD 1 OFF     Will set the value of command 1 to zero
UCMD 3 ON      Will set the command 3 to its default value
UCMD           Will show the setting of the last UCMD entered

The PACTOR UCmd commands are:

| Command | Default | Maximum | Description |
|---|---:|---:|---|
| `UCMD 0` | 3 | 30 | Number of correct packets in a row that must be received before generating an automatic request to change from 100 to 200 baud. Also see `PT200` in 11.7.4. |
| `UCMD 1` | 6 | 30 | Number of incorrect packets in a row that must be received before generating an automatic request to change from 200 to 100 baud. Also see `PT200` in 11.7.4. |
| `UCMD 2` | 2 | 9 | Number of packets sent in a baud rate speed-up attempt. |
| `UCMD 3` | 5 | 60 | Maximum number of Memory ARQ packets that are combined to form one good packet. When this number is exceeded, all stored packets are erased and Memory ARQ is re-initialized. |

<!-- PDF p.168 -->

### 11.8 Monitoring ARQ PACTOR Contacts with PTLIST

Use the "PTLIST" command to monitor ARQ traffic flowing between two
stations linked in a PACTOR ARQ contact.  Your PK-900 will try to
display the text of whichever of the two linked ARQ stations is the
Information Sending Station at the moment.

Monitoring two linked PACTOR ARQ stations does not provide the error
correction enjoyed by the linked stations.  Since your PK-900 is not
part of the "handshake" you do not generate the request for repeat.
Your PK-900 will test for the correct CRC error check and will not
display messages with errors.  Data blocks with errors will be
designated with four error symbols.  The default error symbol is the
underline (_).  See the command summary for ERchr, the error symbol.

Your PK-900 will not print a block of data if that block contains
the same sequence number as the previous block.  If the "ISS"
(Information Sending Station) is repeating the same block, you won't
print it twice.

### 11.9 PACTOR MailDrop Operation

The PK-900 allows PACTOR as well as Packet and AMTOR access to the
MailDrop.  Messages that originate in Packet or AMTOR can be accessed
remotely in PACTOR and messages that originate from a remote PACTOR
station can be accessed by Packet and AMTOR users of your MailDrop.
This section of the manual talks about basic PACTOR mailbox operation.
Section 11.10 will discuss how to pass message traffic from PACTOR to
Packet and vice versa.

Make sure that you understand MailDrop Operation in Chapter 5 and the
basic PACTOR operation described earlier in this chapter before
putting your PACTOR MailDrop on the air.

#### 11.9.1 Special Operating Considerations

The PACTOR MailDrop has been designed with a "Watchdog" safety feature
so that it may perform safely without constant attention.  If a remote
station is linked with your PACTOR MailDrop and no traffic is passed
for 5 minutes, the link will drop and your transmitter will shut off.

At this writing however, unattended operation below 30 MHz is not
legal for US amateurs unless they hold a Special Temporary
Authorization (STA) from the FCC for this purpose.  This restriction
may soon change, but until then US amateurs must be sure to always
have control of their HF transmitters when any automatic device such
as the PK-900 MailDrop is in operation.

With this in mind, we have designed the PACTOR MailDrop so that it can
be disabled at any time during an ARQ link simply  by turning the
command TMAIL (TOR MAIL) OFF.  This allows you the SYSOP to make your
MailDrop available to other stations and still break in to chat with
remote stations at any time.  This could come in handy should you
want to provide some help or information to a remote station using
your PACTOR MailDrop.

<!-- PDF p.169 -->

#### 11.9.2 Settings For PACTOR MailDrop Operation

Before a remote PACTOR user can access your MailDrop, be certain that
MYPTCALL and MYCALL (on Port 1) are set to your Amateur callsign.

#### 11.9.3 Starting PACTOR MailDrop Operation

Remote access to your PACTOR MailDrop is controlled by the command
TMAIL which is short for TOR MAIL.  The TMAIL command controls remote
access to the PACTOR and AMTOR MailDrop in the same way that the
MAILDROP command controls remote Packet access.

Turn the TMAIL command ON (default OFF) to allow remote stations to
access your MailDrop in ARQ PACTOR.  Turn TMAIL OFF to have normal ARQ
QSOs with other stations in the PACTOR mode.

#### 11.9.4 Local Logon to the MailDrop

To locally access your MailDrop use the MDCHECK command as described
in chapter 5 of this manual on MailDrop operation.

##### 11.9.4.1 Remote Logon to your PACTOR MailDrop

The PACTOR maildrop user interface is slightly different from the
packet interface due to the differences between the two modes.

When a station links with your PACTOR MailDrop, your PK-900 first
identifies your maildrop by sending the amount of free MailDrop
memory as shown below:

Type H for help.
(AEA PK-900) 17528 FREE.

The PK-900 then sends the user the MTEXT string if the MailDrop
Message command (MMSG) is ON.  The Default text is shown below:

Welcome to my AEA PK-900 maildrop.
Type H for help.

#### 11.9.5 Caller Prompts

The command prompt that the MailDrop sends the remote user in PACTOR
is similar to that used in the Packet mode and is shown below:

WX7BBB DE WX7AAA (A,B,H,J,K,L,R,S,V,?) >

As in packet, MDPROMPT is the PACTOR MailDrop message prompt sent to
a remote station by your MailDrop.  The default prompt is:

Subject:/Enter Message,^Z (CTRL-Z) or /EX to End

Text before the first slash is sent to the user as the subject prompt;
text after the first slash is sent as the message text prompt.

<!-- PDF p.170 -->

#### 11.9.6 Monitor MailDrop Operation

The local user (SYSOP) can monitor the dialog by setting MDMON ON.
The PK-900 stays in command mode during remote MailDrop access.

#### 11.9.7 SYSOP MailDrop Commands

The MailDrop commands that you the SYSOP have access to are the same
as those described in Chapter 5 of the manual on MailDrop Operation.

#### 11.9.8 Remote User MailDrop Commands

When a remote user has logged onto your MailDrop the following
commands are available to the distant station:

A, B, H, J, K, L, R, S, V, ?.

The remote user may end a command with either <CTRL-Z> or a carriage
return.

A brief description of each command follows in the next sections.  The
description is expanded where the command operation differs from the
Packet Maildrop section found in Chapter 5.

##### 11.9.8.1 A (ABORT) (Remote only)

The "A" command aborts the listing or reading of messages by the
remote calling station as described in chapter 5.  The difference in
PACTOR is that the remote user must send the ACHG command first to
reverse the direction of the link before he can issue the Abort
command.
The remote user also has the ability to abort a command that may have
been mis-typed by typing "///" on the same line as the bad command.

##### 11.9.8.2 B (BYE)

The "B" command logs the remote station off the MailDrop.  In PACTOR
the remote station may gracefully shut down the link with the RECEIVE
character (<CTRL-D>) or the CWID character (<CTRL-F>).

##### 11.9.8.3 H (HELP)

The "H" command sends the remote station a help list of the available
commands shown in Chapter 5.

##### 11.9.8.4 J (JLOG) (Remote only command)

The "J" command sent by the distant station will cause the MailDrop to
send the list of stations who have logged in to your MailDrop.

##### 11.9.8.5 K n (KILL n [Mine])

The "K n" command deletes message number "n" from the MailDrop as
described in Chapter 5.  The "KM" command will kill all of your
messages that have been read.

<!-- PDF p.171 -->

##### 11.9.8.6 L (LIST [Mine])

The "L" command shows the remote user only a list of the messages he
or she may read as described in Chapter 5.  The "LM" command lists
only those messages addressed to the user.

##### 11.9.8.7 R n (READ n [Mine])

The "R n" command lets the remote user read any of the message numbers
displayed in the LIST command.  The command operates as described in
Chapter 5 except that the column headers are not displayed.  The "RM"
command displays messages addressed to the remote user that have not
been read previously.

##### 11.9.8.8 S callsign (SEND callsign)

Either a <CTRL-Z> or the "/EX" command must be used to end all PACTOR
MailDrop messages.  After the <CTRL-Z> or "/EX" has been detected, the
MailDrop will confirm that the message has been sent by returning the
message "Filed msg n" to the remote user.  An example of sending a
message is shown below:

WX7BBB DE WX7AAA (A,B,H,J,K,L,R,S,V,?) >            {MailDrop prompt}
s wx2zzz @ wx2yyy                               {User's SEND command}
Subject:                                    {MailDrop Subject prompt}
Going to the Hamfest?                           {User enters Subject}
Enter Message, ^Z (CTRL-Z) or /EX to End       {MailDrop Send prompt}
I haven't heard from you and wondered if               {Message text}
you are going to the Hamfest next month?               {Message text}
Hope to see you there. 73                              {Message text}
/ex                                               {User ends message}
WX7BBB DE WX7AAA Filed msg 1 (A,B,H,J,K,L,R,S,V,?) >{MailDrop prompt}

##### 11.9.8.9 V (VERSION) (Remote only command)

The "V" command causes the PK-900 to send the sign-on message and
firmware date to the remote user only.

11.9.8.10 ? (HELP) (Remote only command)

The "?" command sends the distant station a HELP list of all available
MailDrop commands shown above under the "H" command.  Both the "?" and
the "H" cause this same file to be sent to the remote user.

### 11.10 Simultaneous PACTOR and Packet Operation

Your PK-900 can operate PACTOR on Radio Port 1 and HF or VHF Packet
on Radio Port 2 at the same time.  With this feature you won't miss
any local Packet activity while operating PACTOR.

Before the second radio port can be used for packet operation, you
must be sure the correct modem and packet baud rate (HBAUD) are
selected for the second port.  Use the MODem command to select either
or both modems, for example, MOD 4/4.  For Radio Port 2, the command
would be MOD /4 to select the 1200 baud VHF packet modem.

<!-- PDF p.172 -->

Always make sure that the port 2 packet baud rate HBAUD is appropriate
for the modem you have chosen.  For instance to operate 1200 baud VHF
packet on radio port 2, the packet baud rate (HBAUD) must be 1200
bauds.  Type HBAUD /1200 to select 1200 baud on radio port 2.

#### 11.10.1 Selecting  Modems

The various modems available in the PK-900 can be seen with the
DIRECT(ory) command.  To display all the available modems simply
enter the Command Mode of the PK-900 and then type DIR as shown.

DIR <Enter>

The PK-900 will respond with the following:

- Port 1 -                              - Port 2 -
1: FSK 45 bps 170: 2125/2295            1: Internal 200: 1070/1270
2: FSK 100 bps 170: 2125/2295           2: Internal 200: 2025/2225
3: FSK 45 bps 200: 2110/2310            3: Internal 1000: 1200/2200
4: FSK 100 bps 200: 2110/2310           4: Internal 1000: 1200/2200
eq.
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
only the 100 (or higher) baud modems listed will operate in PACTOR.
For example, to operate PACTOR on radio port 1 and 1200 bps VHF packet
on radio port 2 you must select modems for radio ports 1 and 2.
First, enter the Command Mode of the PK-900 and then type MODEM 4/4 as
shown below:

MODEM 4/4 <Enter>

The PK-900 will respond with the following:

MODem was  x/x (The previous modems)
MODem now  4/4

#### 11.10.2 Displaying Received Data

The Radio command may be used to disable port 2.  This may be
desirable when operating PACTOR and you do not want to be disturbed
with any packet signals that may be received on radio port 2.  To
disable port 2, enter:

RADIO ON/OFF <Enter>  or  RADIO 1/0 <Enter>

For dual port operation, type

RADIO ON/ON <Enter>  or  RADIO 1/2 <Enter>

<!-- PDF p.173 -->

The PK-900 sorts and displays received data from each Radio Port
using the same technique as multi-connect packet operation described
in Chapter 4.  That is, when operating on one port and the other
port becomes active, the displayed data from the inactive port is
shown prefaced by the "channel designator" followed by a colon (:).
Recall that Radio Port 1 is designated by "logical" channels from 0-9
and that Port 2 is designated by "logical" channels A-Z.

#### 11.10.3 Switching Between Ports

If you are using an AEA PAKRATT program, switching between Radio Ports
is described in the program manual.  If you are using a terminal
program, this section describes how to direct your transmitted text.

Switching between PACTOR on Port 1 and Packet on Port 2 is similar to
switching between Packet and Packet.  If you have not yet read through
the Switching Between Radio Ports section of Chapter 4, please do so
now and define a CHSWITCH character before reading the example below.

Recall from chapter 4 that the channels on Port 1 are labeled 0-9 and
the channels on Port 2 are labeled A-Z.  To select Radio Port 1
(PACTOR) press the CHSWITCH character you defined, followed by the
number 0.  To select Radio Port 2 (Packet), press the CHSWITCH
character, followed by a letter from A-Z.

For example, you are conversing with an PACTOR station and are in the
middle of a QSO when a station on VHF connects to you.  The following
shows how your screen would look and suggests how you might handle
such an occurrence.  The underlined text is the text that you type.

Hello Jim, you are printing solid       { You send the PACTOR station
here and have an S7 signal.<CTRL-Z>       a signal report }

Thanks Bob, you're also a solid S7      { The other station responds
here as well.                             with a signal report }

A:*** CONNECTED to WX7EEE               { WX7EEE connects to you on
VHF (Radio Port 2) }

Thanks for the signal report Jim       { You make another transmission
only running 100 watts here.<CTRL-Z>      to Jim on HF PACTOR }

Hey Bob, I'm going to the hamfest       { Your friend on VHF packet
this weekend if you want a ride.          wants to go to the hamfest }

|AHello Mike, I am on HF PACTOR          { You switch to Port 2 by
talking to a station in Boston.           typing |A to answer on VHF }

0:Well Bob, I had better be going to    { Jim on HF PACTOR signs off
bed.  Work starts pretty early 73.        with you. }

|073 Jim, it was nice meeting you.      { You switch back to HF with
WX1AAA de WX7BBB SK <CTRL-D>              |0 and sign off with Jim }

<!-- PDF p.174 -->

As you may have noticed, communicating with different modes on the two
Radio Ports at the same time is almost identical to the method used in
chapter 4 for Packet and Packet operation.  Let's discuss the sample
QSOs above to see how the Port switching occurs.

The first text we see in the sample above is the signal report you are
sending to Jim, the HF PACTOR station you are communicating with.

This example assumes you have already set up the ARQ PACTOR contact
with the ARQ command discussed earlier in the chapter.  When you are
through sending your signal report to Jim on PACTOR, you turn the link
over to become the IRS by sending the <CTRL-Z>.  The PK-900 then
responds by sending a blank line to the display to break up the
received text.

The next text you see is your signal report received from Jim on
PACTOR.

You are in the middle of a QSO with Jim on PACTOR and all of a sudden
your friend WX7EEE connects to you on Packet.  WX7EEE has connected on
Radio Port 2 (VHF) which is shown by the "A:" before the connect
message.  Remember that the 26 channel designators for Radio Port 2
are A-Z.

Before you respond to WX7EEE on VHF, you send a transmission to Jim on
PACTOR telling him how much power your transmitter is running.  When
you are finished with your text, you again command the PK-900 to be
the IRS by sending a <CTRL-Z>.

After making this transmission on PACTOR, you see WX7EEE on VHF has
offered you a ride to the hamfest.  We know this text is from Radio
Port 2 since the previous packet displayed by the PK-900 was the
"A:*** CONNECTED" message from Port 2.

Now you want to let WX7EEE on VHF know that you are there, but that
you are involved in another QSO on HF.  This way he will understand
that it may take you a little longer to respond to his packets.
Before you can send data to Radio Port 2, you must switch to this Port
with "|A" or the text you type will be sent to Radio Port 1 on PACTOR
when Jim turns the link over to you again.

You receive a transmission from Jim on PACTOR telling you he needs to
sign off to go to bed.  The "0:" in front of the text shows this was
received on Radio Port 1.

Now you want to sign off with Jim on HF.  Again, first you must switch
to Radio Port 1 with the "|0" since your last transmission was
directed to Port 2.  Now you can make your final transmission to Jim
ending with his callsign followed by your callsign.  This time when
you are through typing your text, you send a <CTRL-D> to the PK-900
which breaks the ARQ link and returns Radio Port 1 to PACTOR Standby
after the text has been sent.

<!-- PDF p.175 -->

#### 11.10.4 More Thoughts on Port Switching

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

When a PK-900 Radio Port is disabled, the front panel LCD status
indicator for that port will be extinguished as a reminder.

#### 11.10.5 Dual Port PACTOR/Packet Maildrop Operation

Your PK-900 MailDrop will operate both on Packet and PACTOR and can
be used to allow message traffic that originates on PACTOR to be
reverse-forwarded into the Packet network.  Similarly, traffic
originating on Packet may be picked up by remote stations on PACTOR.

Before you begin dual port Packet/PACTOR MailDrop operation, be sure
that you are familiar with the operation of Packet, PACTOR and the
MailDrop as described in Chapters 4, 5 and 7 of this manual.  Also be
sure that appropriate modems are selected.  The more experience you
have with each of these modes will help when setting up a dual port
MailDrop system.

<!-- PDF p.176 -->

##### 11.10.5.1 Packet MailDrop Command Settings

First set up the packet side of the MailDrop (on Radio Port 2) by
setting MYCALL and MYMAIL.  You may also want to enter a custom MTEXT
to let others know about the PACTOR feature.  Be sure to set 3RDPARTY,
MDMON, MDPROMPT and MMSG as desired.  If you will be reverse
forwarding to a full-service BBS, you must set HOMEBBS to the callsign
of that BBS.  Do not forget to turn MAILDROP ON.  As a test you should
connect to your own maildrop via a digipeater or network node to make
sure the radio link is working properly and the user prompts are what
you desire.

##### 11.10.5.2 PACTOR MailDrop Command Settings

Once the Packet side is working properly, the PACTOR side of things on
Radio Port 1 must be configured.  First be sure that MYCALL
is entered properly.  You may want to customize the PACTOR
MailDrop prompt (MDPROMPT) or compose a message to ALL that tells
remote users about your system.  Finally, remember to turn TMAIL ON
and enter the PACTOR mode of the PK-900 to start the PACTOR MailDrop.

##### 11.10.5.3 Dual Port MailDrop Operation Notes

With the above parameters set, packet connections to the MYMAIL
callsign on Radio Port 2 will be sent to your MailDrop if you or a
remote PACTOR station is not using it.  If you or any other station is
using your MailDrop, the remote user attempting the packet connection
will be sent a "*** Busy" message.

Similarly, a remote PACTOR station linking to you will be given access
to your MailDrop provided no one else is using it.  If you or a remote
packet station is using your MailDrop, the PACTOR station may link to
you, but will not be given access to your MailDrop.  For this reason,
you may wish to disable Radio Port 1 (by turning the RADIO parameter
to 0/2) when logging into your own MailDrop for maintenance.  This
will prevent remote PACTOR stations from linking with you.

If you will be Reverse Forwarding messages into the Packet network, be
sure to check your MailDrop often for new messages.  Remember that you
must use the Edit command in the MailDrop to select which messages
will be Reverse Forwarded.

Note:     At this time unattended operation below 30 MHz is not legal
for US amateurs unless they hold a Special Temporary
Authorization (STA) from the FCC.  Although this may soon
change, US amateurs must be sure to have control of their
HF transmitters when any device such as the PK-900
PACTOR/Packet MailDrop is in operation.

<!-- PDF p.177 -->

### 11.11 PACTOR Switching-Time Considerations

For operation in PACTOR ARQ, your transceiver or transmitterreceiver combination must be able to change between transmit and
receive within 100 milliseconds.  Most modern solid state radios can
easily meet this specification.  Many older tube-type radios that use
electromechanical relays also operate very well in PACTOR ARQ.

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
the PK-900's PACTOR timing characteristics to compensate for this.

#### 11.11.1 Suggested PACTOR Operating Settings

If you have trouble synchronizing with another PACTOR ARQ station, try
some of the following operating tips before calling AEA or deciding
that your radio equipment needs modifications:

- Try to work the distant station on the unproto mode to establish
that the other station's system is fully functional.

- Don't use VOX control - use the PTT line from your interface.

- Turn off the AGC circuit - use the RF gain control to prevent
receiver blocking on stronger signals.

- Turn off all compression or other audio processing.

- Keep the AFSK audio input level to the microphone circuit as low
as possible - avoid over-driving the audio input stages.

- Disable the ALC circuit or reduce excessive ALC action; use more
effective RF antenna loading to adjust output power levels.

#### 11.11.2 Possible Areas for PACTOR Performance Improvement

If switching-time problems persist, you may have to make changes in
the radio to eliminate excessive time delays:

- Remove large decoupling capacitors from the Push-To-Talk line to
allow faster PTT (transmitter) activation;

- Improve power supply decoupling, especially in audio stages.

- Do not use the squelch control.

In case you can't solve your radio's switching-time problems, please
call AEA's Customer Service Department (see the front of this manual).

<!-- PDF p.178 -->

> [No extractable text on this page — figure, schematic, or blank.]
