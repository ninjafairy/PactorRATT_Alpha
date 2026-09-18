# Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)

## Appendix A — Commands P (PDF p.187–290)

Entries follow the same shape as `docs/HostCommands - Trimmed.md`. Values are copied from the PK-900 extract; nothing was invented.

### PAcket                                                              Immediate Command
**Mode:** Command    Host: PA
**Source:** (PDF p.256)
**Description:**
Use the PACKET command to switch Radio Port 1 of your PK-900 into packet radio mode from any other operating mode.


### PACLen "n/n"                                                        Default: 128/128
**Mode:** Packet    Host: PL
**Source:** (PDF p.257)
**Parameters:**
- "n"  -    0 to 255 specifies the maximum length of the data portion of a packet.
- 0    -    Zero is equivalent to 256.
**Description:**
PACLEN sets the maximum number of data bytes to be carried in each packet's "information field" on each Radio Port.

Most keyboard-to-keyboard operators use the default value of 128 bytes for routine VHF/UHF packet services.  For this reason the default is set to 128 as shown above.  On HF Packet PACLEN should be reduced to 64 or less.

Your PK-900 automatically sends a packet when the number of characters you type for a packet equals "n."


### PACTime EVERY|AFTER "n"                                             Default: AFTER 10 (1000 msec.)
**Mode:** Packet    Host: PT
**Source:** (PDF p.257)
**Parameters:**
- "n"    -  0 to 250 specifies 100-millisecond intervals.
- EVERY  -  Packet time-out occurs every "n" times 100 milliseconds.
- AFTER  -  Packet time-out occurs when "n" times 100 milliseconds elapse without input from the computer or terminal.
**Description:**
The PACTIME parameter sets the amount of time in 100 msec increments that the PK-900 will wait for a character to be received on the serial port before sending a packet in Transparent Mode.  The PACTIME parameter is always used in Transparent Mode but is also used in Converse Mode if CPACTIME is ON.

When EVERY is specified, the characters you type are "packetized" every "n" times 100 milliseconds.  When AFTER is specified, the characters you type are "packetized" when input from the terminal stops for "n" times 100 milliseconds.

The PACTIME timer is not started until the first character or byte is entered. A value of 0 (zero) for "n" means packets are sent with no wait time.


### PACTOr ( PT for Short )                                             Immediate Command
**Mode:** Command    Host: Pt
**Source:** (PDF p.257)
**Description:**
PACTOr is an immediate command that switches the PK-900 into the PACTOR mode of operation on Radio Port 1.

PACTOR is a mode of data communication that combines some of the features of both AMTOR and packet.  The abbreviated command is PT.  It has both a linked mode called ARQ and a non-linked mode called unproto.


### PARity "n"                                                          Default: 3 (even)
**Mode:** All    Host: PR
**Source:** (PDF p.258)
**Parameters:**
- "n"  -    0 to 3 selects a parity option from the table below.
**Description:**
PARITY sets the PK-900's RS-232 terminal parity according to the following:

0 = no parity,  1 = odd parity,  2 = no parity,  3 = even parity

The parity bit, if present, is stripped on input and is not checked in Command and Converse modes.  In Transparent mode all eight bits (including parity) are transmitted.  The change will not take effect until a RESTART is performed. Be sure to change the computer or terminal to the same parity setting.


### PASs "n"                                                            Default: $16 <CTRL-V>
**Mode:** Packet/ASCII    Host: PS
**Source:** (PDF p.258)
**Parameters:**
- "n"  -    0 to $7F (0 to 127 decimal) specifies an ASCII character code.
**Description:**
PASS selects the ASCII code for the character used for the "pass" input editing commands (default <CTRL-V>).  The PASS character signals that the following character is to be included in a packet or ASCII text string.


### PASSAll ON|OFF/ON|OFF                                               Default: OFF/OFF
**Mode:** Packet    Host: PX
**Source:** (PDF p.258)
**Parameters:**
- ON   -    Your PK-900 will accept packets with valid or invalid CRCs.
- OFF  -    Your PK-900 will accept packets with valid CRCs only.
**Description:**
PASSALL turns off the PK-900's packet error-detecting mechanism and displays received packets with invalid CRCs.  The is settable for each Radio Port. PASSALL is normally turned OFF (default); which ensures that packet data is error-free by rejecting packets with invalid CRC fields.  When PASSALL is ON, packets are displayed, despite CRC errors.  The MHEARD logging is disabled since the call signs detected may be incorrect.


### PErsist "n/n"                                                       Default: 63/63
**Mode:** Packet    Host: PE
**Source:** (PDF p.258)
**Parameters:**
- "n"   -   0 to 255 specifies the threshold for a random attempt to transmit.
**Description:**
The PERSIST parameter works with the PPERSIST and SLOTTIME parameters to achieve true p-persistent CSMA (Carrier-Sense Multiple Access) in Packet operation. PERSIST is settable on each radio port to allow for different types of operation.


### PK ["n"]                                                            Default: none
**Mode:** All    Host: PK
**Source:** (PDF p.259)
**Parameters:**
- "n"   -   a hex number used to access the PK-900's memory and I/O locations.
**Description:**
PK (Peek/Poke) permits access to memory locations.  To use the PK command:

o    Set the memory address into the ADDRESS command.

o    Use the PK command without arguments to read that memory location.

o    Use PK with one argument 0-$FF to write to that memory location.

This command is used as a programmer's aid and is not needed for normal use.


### PPersist ON|OFF/ON|OFF                                              Default: ON/ON
**Mode:** Packet, PACTOR    Host: PP
**Source:** (PDF p.259)
**Parameters:**
- ON   -    The PK-900 uses p-persistent CSMA (Carrier Sense Multiple Access).
- OFF  -    The PK-900 uses DWAIT for TAPR-type 1-persistent CSMA.
**Description:**
When PPERSIST is ON (default), the PK-900 uses the PERSIST and SLOTTIME parameters for p-persistent CSMA instead of the older DWAIT CSMA procedure. PPERSIST may be enabled or disabled on each Radio Port.

When your computer has queued data for transmission, the PK-900 monitors the DCD signal from its modem.  When the channel clears, the PK-900 generates a random number between 0 and 255.  If this number is less-than or equal to "PERSIST", the PK-900 transmits all frames in its queue.  If the random number is greater than "P", the PK-900 waits .01 * SLOTTIME seconds and repeats the attempt.  PPERSIST can be used in both KISS and normal AX.25 operation.


### PRType "n"                                                          Default: 2 (Epson)
**Mode:** FAX    Host: PY
**Source:** (PDF p.259)
**Parameters:**
- "n"   -   0 to 255, specifying a code for dot graphics sequences used in FAX.
**Description:**
The following is a list of the different printer graphics types the PK-900 supports the convey the received FAX graphics information to your computer.

PRTYPE    Printer                       PRTYPE    Printer 0         Epson                         4         IBM 8         Radio Shack (Tandy)           12        Apple (G) 16        Apple (S)                     20        old Okidata 24        Okidata                       28        Gemini 10, 15 32        Star Micronics                36        GX-100, Gorilla 40        Texas Instruments             44        Genicom 48        Miscellaneous (HP ThinkJet)   52        Citizen 56        NEC                           60        Anadex

Unsupported PRTYPE settings are treated as PRTYPE 0.


### PT200 ON|OFF                                                        Default: ON
**Mode:** PACTOR    Host: PB
**Source:** (PDF p.260)
**Description:**
PACTOR uses an adaptive data rate selection scheme.  The normal data rate is 100 baud.  If PT200 is ON (default) and conditions permit, the data rate will be automatically shifted to 200 baud.  If the error rate becomes too high at 200 baud the data rate will automatically be reduced to 100 baud.  There can be conditions where the data rate is frequently shifting, causing a loss in the actual information data rate.  When PT200 is OFF, the PACTOR data rate is fixed at 100 baud.  See the UCMD command for the PACTOR baud rate threshold settings.


### PTConn [!]a(aaaaaaa)                                                Immediate Command
**Mode:** PACTOR    Host: PG
**Source:** (PDF p.260)
**Parameters:**
- aaaa(aa) is the call sign of the station to be called.
**Description:**
PTConn is an immediate command that starts the PACTOR connect protocol.  To start a PACTOR connect, type "PTC" followed by the other station's call sign:

Example:  PTC N7ML or, for longpath stations, use the exclamation point

before the call:  PTC !N7ML.

As soon as the <CR> is typed, the PK-900 will begin keying your transmitter on Radio Port 1 with the PACTOR connect sequence.


### PTHUFF "n"                                                  Default 0 Default: 0
**Mode:** PACTOR    Host: PH
**Source:** (PDF p.260)
**Parameters:**
- "n"   -   0 to 3 specifies the type of data compression used in PACTOR.
**Description:**
To enhance the effective data rate in PACTOR, a data compression scheme may be automatically enabled.  The number "n" corresponds to the type of compression used, with 0 disabling data compression (default).

When PTH is set to 1, Huffman compression will be used if it is more effective. The numbers 2 and 3 are reserved for future compression schemes.

Instead of using the normal 8-bit ASCII representation of a character, Huffman encoding assigns each character a code that may be as few as 2 bits for the most used characters to as long as 15 bits for the least used characters.  For English (and most other) languages, the use of Huffman compression results in a smaller number of bits necessary for a given message.


### PTList                                                              Immediate Command
**Mode:** PACTOR    Host: PN
**Source:** (PDF p.261)
**Description:**
PTList is an immediate command that switches your PK-900 into the PACTOR listen mode.

You can usually monitor a PACTOR contact between two connected stations using the PACTOR listen mode.  Since your station is not part of the error free link, if the CRC check does not produce a correct check sum, nothing will be displayed.


### PTOver "n"                                                          Default: <CTRL-Z> ($1A)
**Mode:** PACTOR    Host: PV
**Source:** (PDF p.261)
**Parameters:**
- "n"  -  A hexadecimal value from $00 to $7F used to select the change-over character used in linked PACTOR.
**Description:**
PTOver is the character, conventionally <CTRL-Z>, that is used to change the direction of data transmission in a linked PACTOR operation.  When you are finished transmitting information and you are ready to receive information from the other station, use the PTOver character.  Also see AChg.


### PTSend -  "n,x"                            Default 1,2              Default: 1,2
**Mode:** PACTOR    Host: PD
**Source:** (PDF p.261)
**Parameters:**
- "n"  -  1 or 2  selects the transmit baud rate
- "x"  -  1 to 5 selects the number of times each packet is sent.
**Description:**
PTS "nx" initiates an unprotocoled PACTOR transmission.  To end the transmission, type <ctrl-D>.

"n" 1 selects 100 baud, 2 selects 200 baud.

In order to increase the probability of correct transmission, the unproto PACTOR transmission sends the message data a selected number of times.  The parameter x sets the number of times each packet is sent.

The transmission may be started using the default, 100 baud, two repeats, by typing "PTS" without "nx."

Example:

PTSEND 2 3

Sends each packet 3 times at 200 baud.
