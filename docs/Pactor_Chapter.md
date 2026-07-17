# Chapter 11 — Pactor Operation

> Converted from `basic pactor operation.odt` for easy reference. Obvious OCR/scan artifacts from the source were corrected where the intended meaning was clear.

## Overview

Like AMTOR and packet, Pactor has two basic modes of operation: an ARQ mode (Automatic ReQuest for reception) and a non-linked mode used for CQ calls and roundtable operation.

The unproto(col) mode of operation is a non-linked type of operation. It is used for roundtable operation or for calling CQ. The unproto mode repeats the data blocks a selectable number of times and can use either 100 or 200 bps.

## PK-232 Pactor Parameter Settings

Pactor is a bit more complex than Baudot or ASCII operation. Pactor operation requires you to have `MYPTCALL` or `MYCALL` entered before you can operate. If you do not enter `MYPTCALL`, the call in `MYCALL` will be used as the default callsign. Pactor stations can't use the Substation Identification number (SSID) in `MYCALL`.

### Entering Your Callsign (MYPTCALL)

If you have not already done so, enter your call after the command prompt (`cmd:`) using the `MYPTCALL` command.

If you do not enter a call using `MYPTCALL`, the PK-232 will use the call entered in `MYCALL`. `MYCALL` does not allow punctuation other than the dash (`-`). `MYPTCALL` does allow punctuation in the call. This allows you to properly identify when operating portable, e.g. `MYPTCALL ZL2/KGRFK`.

If a callsign has not been entered in either `MYCALL` or `MYPTCALL`, the PK-232 will not allow you to transmit in Pactor.

## Calling CQ In Unproto Pactor

If you plan to make a CQ call, you must do so in the unproto Pactor mode.

This is required since an ARQ Pactor transmission requires another station to "link-up" with. If you are using a Timewave terminal program, see the program manual to place the PK-232 into unproto transmit. If you are using a terminal or terminal emulation program, the following will place your PK-232 and transceiver into the transmit mode.

1. Type `PTSEND` then press `<Enter>` to key your transmitter and automatically enter Pactor unproto transmit mode.

   As soon as you type `<Enter>` you will be transmitting. At this point you are also in the CONVERSE mode and anything you type will be sent in unproto Pactor by your transmitter.

2. Type `<CTRL-D>` at the end of your CQ call. `<CTRL-D>` puts your radio into the receive mode and your PK-232 into the Pactor standby mode where it is ready for a call.

Pactor standby is different from the Pactor Listen mode. In Pactor standby, your PK-232 is ready to receive Pactor connects, but you will not be able to monitor other Pactor stations.

`<CTRL-Z>` is the character defined by the `PTOVER` command that switches your system from being the Information Sending Station (ISS) to the Information Receiving Station (IRS) and switches the distant system from being the information receiving station to the information sending station. This single character operates like the `+?` does in AMTOR.

The FCC requires station identification once every ten minutes. It's sufficient to begin with `QRA (mycall)` or end your transmission with `QRA (mycall)` before the `<CTRL-Z>` changeover, or use the `<CTRL-B>` "HERE-IS" to send your own Auto-AnswerBack message. For the "HERE-IS" command to function, you must have your AAB text entered.

See appendix A for the `AAB` command.

## Ending An ARQ Pactor Contact

When you've finished your "final finals" to the distant station and both stations are ready to end the Pactor ARQ contact, you can end the contact and terminate the link in several different ways:

1. Type `<CTRL-D>` to stop sending after the transmit buffer is empty.

   `<CTRL-D>` breaks the link and returns your PK-232 to Command Mode in Pactor Standby. This is the best way to end a Pactor contact.

2. Type `<CTRL-F>` to stop sending after the transmit buffer is empty, send your Morse ID, and return to Pactor Standby and Command Mode.

   This is the best way to end a contact if you want to identify your station in Morse code as well.

3. Wait until all the text has been sent, then type `<CTRL-C>` to return to Command Mode, then type `R` to break the link. The PK-232 will then go into the Pactor Standby mode.

4. Type `<CTRL-C>` to return to Command Mode, then type `R <Enter> R <Enter>` to break the link immediately.

   If there are characters left in the transmit buffer, they will not be sent. This method should only be used for an emergency shutdown as it does not send the control signal to the other station that informs it you are shutting down. As a result the other station will continue to send until its internal timer turns it off.

## Long Path Contacts

If the station you are going to call is more than halfway around the earth (i.e. a long path contact), precede that station's callsign with an exclamation point. This will change the Pactor timing to allow for the extended radio propagation delay. You would type:

```text
PTCONN !N7ML <Enter>
```

If your station doesn't link within a period of time determined by the `ARQTMO` command (default 60 seconds), your station will automatically stop transmitting.

## Pactor Operating Tips

The following special function characters and immediate commands are included for Pactor operating convenience.

### Immediate Commands from the Command Mode

| Command | Description |
|---|---|
| `PT` | Selects the Pactor mode |
| `PACTO` | Selects the Pactor mode as above |
| `PTConn <CALL>` | Starts a linked connect and forces Converse |
| `PTSend` | Starts an unproto transmission and forces Converse |
| `R` | Stops sending immediately, forces Pactor Standby |
| `PTList` | Allows reception of both unproto and linked transmissions |
| `PTHUFF <ON/OFF>` | `OFF` — prevents Huffman compression; `ON` — allows automatic use of compression |
| `PT200 <ON/OFF>` | `OFF` — prevents 200 baud operation; `ON` — allows automatic speed selection |
| `PTOver <$HH>` | Selects the changeover character. Defaults to `<CTRL-Z>` (`$1A`) |

### Special Function Characters Embedded in Transmitted Text

| Character | Description |
|---|---|
| `<CTRL-B>` | Sends your AAB string as a HERE-IS message |
| `<CTRL-D>` | Stops sending when the transmit buffer is empty |
| `<CTRL-E>` | Sends a "Who Are You" request to the other station |
| `<CTRL-F>` | Same as CTRL-D but sends your callsign in Morse |
| `<CTRL-T>` | Sends the TIME if the DAYTIME clock has been set |
| `<CTRL-Z>` | Changes your PK-232 from send (ISS) to receive (IRS) |

## ARQ Break-In (ACHG Command)

In the linked or connected mode (ARQ), when you're the Information Receiving Station, you can use the `ACHG` command to interrupt the distant station's comments.

As the Information Receiving Station, you normally rely on the distant station to send `<CTRL-Z>` to "change-over" at the end of his comments. `ACHG` is a command that forces both systems to reverse the Information Receiving and Information Sending status of the link.

> Use the `ACHG` command only when really needed to interrupt the distant station.

## Entering Your Auto-AnswerBack (AAB)

Timewave Pactor allows you to request the identity of the station you are conversing with by sending your PK-232 a `<CTRL-E>`. This causes the PK-232 to send an inquiry Who aRe yoU (WRU) request to the other station.

For this reason, you should set your own Auto-AnswerBack (`AAB`) message to `DE YOUR-CALL`. Your PK-232 will automatically send the AAB message when another station requests your identity, and then stop sending.

## Operating Pactor with Other Modem Frequencies and Shifts

All Pactor operation uses 200 Hz shift PSK tones as does HF packet.

## Automatic Speed Change

If the command `PT200` is set ON, the linked Pactor mode will automatically change the transmitted data rate from 100 bps to 200 bps if a certain number of error-free 100 bps packets are received in a row. If the error rate at 200 bps is excessive, the data rate will automatically revert to 100 bps.

There may be some propagation conditions that will cause the system to vacillate between the two data rates. This may be prevented by setting `PT200` to OFF, which will force 100 bps operation.

## Echoing Transmitted Characters As Sent (EAS)

EAS (Echo As Sent) operates the same as in ARQ AMTOR. If EAS is ON, you will see characters echoed to your screen only the first time your PK-232 sends them. If the data is not acknowledged by the receiving station and is re-transmitted, the characters are not echoed again. With EAS OFF, characters are echoed to your screen as you type them.

With EAS ON:

- If the data scrolls across your monitor at an even rate, you can assume that you have a good ARQ link.
- If the data hesitates or scrolls in "jerky" intermittent fashion, that's generally a sign that the radio link is not very good.
- If the characters stop appearing on your monitor, the link is failing or has failed. The STATUS display will tell you this by showing ERROR or REQUEST nearly continuously.

## Sending Only Complete Words (WORDOUT)

Some Pactor users like to have their words sent out only when they are complete. This allows the word you are currently typing to be edited as long as you have not typed a `<Space>` character or punctuation. Turning `WORDOUT` ON activates this feature. See the Command Summary for more information.

## Little Used Pactor Commands

There are four seldom-used Pactor commands that are accessible with the `UCmd` command. This command is of the form `UCmd n x`, where `n` is the UCmd number and `x` is the setting. See Appendix A for more information.

The Pactor UCMD commands are:

| Command | Default | Maximum | Description |
|---|---:|---:|---|
| `UCMD 0` | 3 | 30 | Number of correct packets in a row that must be received before generating an automatic request to change from 100 to 200 baud. Also see `PT200`. |
| `UCMD 1` | 6 | 30 | Number of incorrect packets in a row that must be received before generating an automatic request to change from 200 to 100 baud. Also see `PT200`. |
| `UCMD 2` | 2 | 9 | Number of packets sent in a baud rate speed-up attempt. |
| `UCMD 3` | 5 | 120 | Maximum number of Memory ARQ packets that are combined to form one good packet. When this number is exceeded, all stored packets are erased and Memory ARQ is re-initialized. |

## Monitoring ARQ Pactor Contacts with PTLIST

Use the `PTLIST` command to monitor ARQ traffic flowing between two stations linked in a Pactor ARQ contact. Your PK-232 will try to synchronize with whichever of the two linked ARQ stations is the Information Sending Station at the moment.

Monitoring two linked Pactor ARQ stations does not provide the error correction enjoyed by the linked stations. Since your PK-232 is not part of the "handshake," you do not generate the request for repeat. Your PK-232 will check for the correct CRC error check and will not display messages with errors. Missing blocks will be designated with four error symbols. The default error symbol is the underline (`_`).

See the command summary for `ERchar`, the error symbol.

Your PK-232 will not print a block of data if that block contains the same sequence number as the previous block. If the ISS (Information Sending Station) is repeating the same block, you won't print it twice.
