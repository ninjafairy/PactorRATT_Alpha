# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Command Summary guide (PDF p.179–186)

<!-- PDF p.179 -->

COMMAND SUMMARY

### A.1 Introduction

Your PK-900 is controlled by Commands that you enter from the
keyboard.  Most of these commands have a standard (default) value
that provides good performance.  There are however a few commands
that you will need to change.  This section is intended as a command
and error message reference and is not meant to be read from start
to finish.

#### A.1.1 Entering Commands

AEA Software for the PK-900 such as PC-PAKRATT II and MACRATT have
menus and even on-line help for commands.  If you are using an AEA
program, please consult the program manual for instructions on
entering commands.  The instructions below assume you are using a
terminal or terminal-emulating program on your personal computer.

Commands are short names for the instruction you want the PK-900 to
perform.  Commands are entered after the Command prompt "cmd:".

- You may use either UPPER or lower case when entering commands.
- End the command with a carriage return <CR> or the <Enter> key.
- Correct any typing mistakes before typing the final <Enter>.

#### A.1.2 Command Responses

Whenever the PK-900 accepts a command it responds by displaying both
the old and new values.  For example, if you type "XFLOW OFF" - you'll
see the display:

XFlow   was ON
XFlow   now OFF

This message tells you that the value has been changed successfully.

### A.2 Command List

Commands are listed alphabetically in the following command
descriptions.  Each command entry contains several sections:

Command Name, Default Value, Mode(s) in which the command is used,
HOST mode abbreviation (for HOST mode programmers), and Parameters

#### A.2.1 Command Names and Abbreviations

The command name at the beginning of the description is the full word
you can type in order to have your PK-900 execute this command.  The
capital letters indicate the minimum abbreviation you can use instead
of the full word.  For example:

You can enter the command MYCALL by simply typing "MY".  (Note: DO NOT
type the "quotation marks")  The abbreviation "M" is not sufficient,
but "MY", "MYC", "MYCA", "MYCAL" or "MYCALL" are all acceptable.

<!-- PDF p.180 -->

#### A.2.2 Default Values

Almost all commands have initial or default values that are loaded
when the PK-900 is first turned on.  The PK-900 assumes these
default values to be best suited for the "average" amateur station
operation.  There is no rule that says "you must keep the defaults".
You can (and should) change the default values as required for your
individual operating needs, type of equipment, or local customs.

#### A.2.3 Modes in which the command is used

Many commands work only in a specific operating mode such as packet.
Others commands function in all modes.  The second line of the command
description tells the mode or modes in which the command functions.

#### A.2.4 HOST Mode Abbreviations

If you are a computer programmer and wish to write an application for
the PK-900, you may want to consider using the HOST Mode.  Contact
AEA if you are interested in Host Mode programmers information.

### A.3 Parameters and Arguments

If a command requires Parameters, the type of parameter is indicated
after the command name as well as its default value.  Three different
types of parameters are used: Boolean, Numeric and Text or String.

#### A.3.1 Boolean Parameters

Boolean parameters use one value out of a choice of only two possible
values, such as ON or OFF, YES or NO, or EVERY or AFTER.  Boolean
parameters can also be toggled with an argument of "TOGGLE" or "T."
This is useful, for example, in the case of RXREV and TXREV.

#### A.3.2 Numeric Parameters

A parameter designated as "n" is a numeric value.  Numeric values can
be entered by typing them in familiar decimal numbers, or optionally,
in hexadecimal numbers (base 16).

When using hexadecimal notation, you must type a $ in front of the
number to tell the PK-900 that this is a "hex" number.
Here's a brief explanation of "hex" numbers:

- The "digits" of a hex number represent powers of 16 in the same
manner as the powers of 10 represented by a decimal number.

- The numbers 10 through 15 are indicated by hexadecimal digits A
through F.  For example:

$1B  = (1 x 16) + (11 x 1) = 27 (decimal)
$120 = (1 x 16 x 16) + (2 x 16) + (0 x 1) = 288 (decimal)

For numeric parameters the arguments "ON" or "Y" set the parameter
value to its default.  Arguments "OFF" or "N" set the value to 0.
Baud-rate parameters can use arguments UP (U) or DOWN (D) to select
the next higher or lower baud rate.

<!-- PDF p.181 -->

#### A.3.3 Text or String Parameters

A text parameter such as the CTEXT message (your Connect-TEXT
message) can hold most any ASCII character including UPPER and lower
case letters, numbers, spaces, and punctuation.

Some commands such as CONNECT require call signs as parameters.  These
parameters are usually call signs, but may be any string of numbers
and at least one letter up to six characters in length.  Some commands
such as CFROM (your "Connect FROM" list) have parameters which are
actually lists of call signs.  You must separate multiple call signs
with either spaces or commas.

#### A.3.4 Commands With Two Arguments

Some packet commands accept separate arguments for each Radio Port.
For example, the Packet TXDELAY Command (Transmit Delay) must be
settable for both Radio ports.  The default setting of TXDELAY is
"12/30" meaning that Radio Port 1 defaults to 120 msec as used on HF,
and Radio Port 2 defaults to 300 msec for VHF and UHF transceivers.

To change the "Port 1" value of a two argument command, type the
command name followed by the new argument.  For example to set the
Port 1 value of TXDELAY to 30 for VHF packet, type the following at
the command prompt:

cmd:TXDELAY 30
TXDelay  was 12/30
TXDelay  now 30/30

To change the "Port 2" value of a two argument command, place a
forward slash "/" in front of the argument typed after the command
name.  For example, to set the Port 2 value of TXDELAY to 12 for HF
packet, type the following at the command prompt:

cmd:TXDELAY /12
TXDelay  was 30/30
TXDelay  now 30/12

the arguments for both ports may be changed at the same time by typing
the desired arguments for both ports.  For example, to return the
TXDELAY command to its default values, type the following:

cmd:TXDELAY 12/30
TXDelay  was 30/12
TXDelay  now 12/30

#### A.3.5 Using Commands Without Arguments

All commands that accept values or parameters may be typed without any
arguments to check their status.
Typing the only the command name "VHF" with no arguments displays:

cmd:VHF             -    Command with no arguments
Vhf OFF/ON          -    displays the present value

NOTE:  The DISPLAY command shows you groups of related parameters.

<!-- PDF p.182 -->

### A.4 Controller Messages

From time to time, the PK-900 will generate messages informing you
of its status.  Error messages will also be generated if the PK-900
did not understand a command you have entered.  This section describes
the PK-900's messages and the circumstances which cause them.

#### A.4.1 General Status Messages

Sign-On Message

AEA PK-900 Data Controller
Copyright (C) 1986 - 1992 by
Advanced Electronic Applications, Inc.
Release DD-MMM-YY
cmd:

The sign-on message appears when you turn on your PK-900, after
system "RESTART" or "RESET" after the autobaud routine.  The release
date is updated whenever the firmware is changed.  Please write the
date on the first page of this manual in case it is needed.

PK900 is using default values

This message appears the first time you turn on your PK-900 or every
time you turn the PK-900 on if the battery jumper is removed.  The
message will also appear in response to the RESET command.

cmd:

This is the Command Mode prompt.  When this prompt appears, the
PK-900 is waiting for you to issue a command.  Anything you type
after this prompt is interpreted as a command to the PK-900.

was
now

Whenever you change one of the PK-900's parameters, both the previous
value and the new value are displayed.

bbRAM scanned, checksum failed!

This message indicates there has been an error in the battery backed
RAM.  A low lithium battery is the likely cause for this.

ERROR: Subroutine,  Bank, Addr

This message indicates a program error has occurred.  Write down any
information accompanying this message and call AEA's Technical Support
department.  If possible, print a DISPLAY Z parameter listing.

ROM error, checksum $xxxx

This message indicates there has been a failure in the PK-900.  If
this message appears, call AEA's Technical Support department.

<!-- PDF p.183 -->

#### A.4.2 General Error Messages

An error message is displayed if the PK-900 does not understand what
you typed, or needs more information.  If you see an Error message,
look up the Command to make sure you are entering it properly.

?What?

Your first entry is not a command or a command abbreviation - your
PK-900 did not understand your instructions!

?bad

You typed a command name correctly, but the remainder of the command
line was not understood.

?callsign

You typed a call sign that does not meet the PK-900's requirements.

?clock not set

You typed the command DAYTIME, but you haven't yet set the clock!

?not enough

You didn't type enough arguments for a command that needs several.

?range

You typed a numeric argument too large or too small for that command.

?too many

You typed too many arguments for the command to accept.

?too long

You typed a command line that is too long.  For example, if you type a
BTEXT or CTEXT message that is too long you'll receive this message.

?need ALL/NONE/YES/NO

This message indicates you have forgotten the ALL, NONE, YES or NO
parameter in the CFROM, DFROM or MFROM commands.

*** Transmit data remaining

If the PK-900 is commanded back to receive with the RCVE command
when it still has data in the transmit buffer waiting to be sent.

not while in (MODE)

This message will appear any time you try to do something that is not
permitted in the current PK-900 operating mode.

<!-- PDF p.184 -->

Serial port configuration will change on next RESTART

This message means you have changed the terminal baud rate TBAUD,
word-length AWLEN or the PARITY.  When you type the RESTART command,
the new baud rate will take affect.

?need MYSelcal or ?need MYIdent

This message will appear if you try to communicate with another AMTOR
station before you have entered a valid SELCALL (MYSELCAL) or MYIDENT.

?not enough memory available

This message appears if you try to change to FAX when the MailDrop is
too full.

#### A.4.3 Packet Error Messages

In addition to the General Error Messages described above, the
following messages may appear when entering Packet related commands.

?need MYcall

This message appears if you attempt to make a packet Connection but
have not yet entered your callsign in MYCALL.

?not while connected

You attempted to change MYCALL or AX25L2V2 while in a connected state.

?not while disconnected

You tried to set CONPERM while disconnected.

LINK OUT OF ORDER, possible data loss

You are CONPERMed to another packet station but the link has failed.

?VIA

You typed more than one call sign for the CONNECT or UNPROTO commands
without the VIA keyword.

?channel must be 0-9, A-Z

You typed an invalid channel character after the CHSWITCH character.

?different connectees

You tried to CONNECT to more than one station on the same channel.

?already connected (or attempting connection) to that station

You tried to CONNECT to a station to which you're already connected.

<!-- PDF p.185 -->

too many packets outstanding

The message appears if you've typed enough data to fill the outgoing
buffer in either Converse or Transparent Mode.  You cannot re-enter
Converse or Transparent until some of the packets have been sent.

WARNING: Beacon too often

This Warning message appears if you have set the BEACON interval timer
to less than 90 (15 minutes) which is too often for busy channels.

WARNING: CHeck/FRack too small

This Warning message appears if you have set the CHECK timer for too
short a time relative to the FRACK timer.

WARNING: RESptime/FRack too large

This Warning message appears if you have set the RESPTIME timer for
too long a time relative to the FRACK timer.

WARNING: TXdelay too short

This Warning message appears if the TXDELAY timer has been set too
short to send at least one complete flag before the start of a packet.

WARNING: AUdelay > TXdelay

This Warning message appears if the AUDELAY timer has been set equal
to or greater than the TXDELAY timer.

#### A.4.4 Packet Link Status Messages

Link status messages show you the status of AX.25 connections in which
your PK-900 is involved.

*** CONNECTED to: call1 [via call2[,call3...,call9]]

This message appears when your PK-900 switches to the connected
state.

*** Connect request:  call1 [via call2[,call3...,call9]]

Your PK-900 has received but not accepted a connect request from a
distant station.

*** DISCONNECTED: (call sign)

Your PK-900 has switched to the disconnected state.  This message
may be preceded by a message explaining the reason for the disconnect.

*** Retry count exceeded
*** DISCONNECTED: <call sign>

Your PK-900 has been disconnected because of a retry failure, rather
than a disconnect request from one of the stations.

<!-- PDF p.186 -->

*** <call sign> busy
*** DISCONNECTED: <call sign>

Your connect request was rejected by a busy signal from another
station.

FRMR sent: xx xx xx  or FRMR rcvd: xx xx xx

Your PK-900 is connected but a protocol error has occurred.  Your
PK-900 is trying to re-synchronize frame numbers with the distant
station's packet system.  The string xx xx xx is replaced with the hex
codes for the three bytes sent in the FRMR frame.

#### A.4.5 MailDrop Error Messages

The following messages appear as a result of entering an invalid or
unrecognized command to the PK-900's MailDrop.

*** What?

You have entered a command that the MailDrop does not recognize.

*** Need callsign.

You have tried to send a message but not specified who you want to
send it to.

*** Message not found.

You have tried to List or Read message(s) that the MailDrop could not
find.  This also appears if you try to List messages when there are no
messages in your MailDrop.

*** No free memory.

You have tried to send a message, but the MailDrop does not have any
memory for more messages.  You must Kill messages to make room.

*** Not your message.

A remote user will get this when trying to read a message number that
is addressed to another station.

?not while in (mode).

This message will be displayed if the "OK" command is entered when
the PK-900 is in any mode other than Signal.
