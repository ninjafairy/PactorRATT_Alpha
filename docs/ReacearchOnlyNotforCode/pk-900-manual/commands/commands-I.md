# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands I (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### Id                                                                  Immediate Command
**Mode:** AMTOR/ASCII/Baudot/Packet    Host: ID
**Source:** (PDF p.234)
**Description:**
In AMTOR, the ID command acts like the RCVE command, only adding a Morse ID before going back to receive.  In ASCII and Baudot, the ID command causes a CW ID to be sent much like an immediate version of the CWID character (CTRL-F). Because the ID command is immediate, the message "Transmit Data Remaining" will be displayed if any unsent data remains in the transmit buffer.

In Packet, ID is an immediate command that sends a special identification packet.  The ID command allows you to send a final identification packet when you're taking your station off the air.  HID must also be set ON.  Be sure that you select the correct Radio Port with the CHSWITCH command before issuing the ID command.  If you do not, the ID may be sent to the wrong Radio Port.

The identification consists of a UI-frame, with its data field containing your MYALIAS (if any) and your MYCALL and the word "digipeater".  The ID packet is sent only if your PK-900 has digipeated any transmissions since the last automatic identification.


### ILfpack ON|OFF                                                      Default: ON
**Mode:** Packet    Host: IL
**Source:** (PDF p.234)
**Parameters:**
- ON   -    The PK-900 ignores line-feed characters received from the terminal.
- OFF  -    The PK-900 transmits all line-feeds received from the terminal.
**Description:**
The ILFPACK command permits you to control the way the PK-900 handles linefeed characters received from your computer or terminal while in the Packet mode.


### IO ["n"]                                                            Default: none
**Mode:** All    Host: IO
**Source:** (PDF p.235)
**Parameters:**
- "n"   -   A hexadecimal value used to access the PK-900's memory and I/O locations, or read values stored at a specified ADDRESS.
**Description:**
The IO command works with the ADDRESS command (ADDRESS $aabb) and permits access to memory and I/O locations.  Use the IO command without arguments to read an I/O location, and with one argument $0 to $FF to write to an I/O location.  The value in ADDRESS is not incremented after using the IO command.

In ADDRESS $aabb, where "aa" (01-FF) is the device address, and "bb" is the register address on the device.

If ADDRESS is set to $00bb, the IO command reads or writes data to the device at I/O address bb.  There is no register set-up before the access.  This command is used as a programmer's aid and is not needed for normal PK-900 use.
