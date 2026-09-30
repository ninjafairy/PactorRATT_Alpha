# Bugs and quirks

Firmware notes are from the PK-232MBX image dated 5 March 1993 (product byte `$C2`). The listing is a reconstructed disassembly. Command names in ROM are stored with bit 7 set on the last character, and the Host mnemonic is the first two stored name bytes reversed.

## Host `PD` (PTSend) ignores the repeat count

The manual says `PD` `n,x` sets the baud (`n` = 1 for 100, 2 for 200) and how many times each packet is sent (`x` = 1–5). With no arguments the default is `1,2`. Host status `00` only means the command was accepted. It does not mean `n,x` were applied.

On the air, `PD1,5` matches `PD1,1`. A 145-byte CQ after `PD1,5` stayed in Traffic (`$34` → `$33`) for 19.1 s, one 100-baud pass. Five passes would be about 91 s, and the default two passes about 36 s. Bare `PD` (no arguments) runs as 100 baud, two repeats. Any arguments run as one repeat, using the first number for both baud and the repeat count. `PD2` is the one-argument case: 200 baud, and the default repeat of 2. Two 200-baud passes of 32 bytes are about 3.8 s, the same length as one 100-baud pass, so that trial does not separate baud from repeat count.

Host `PD` matches the dictionary name `DPTSend`. The handler is `$4DBB` in the low-EPROM `$8000` bank (CPU `$4000`). It stores the repeat count at RAM `$8E56`. While the TNC is not linked, argument helper `$5480` points the parser at `$8CB8` on every call. That byte is the first character after the two Host letters, so for `PD1,5` it is `1`. The baud read consumes `1`. The repeat read calls `$5480` again, which puts the pointer back on that same `1`, and the parser stores `1`. Verbose `PTS 1 5` uses the forward path of `$5480` and can read both numbers.

The Pactor sender’s only read of `$8E56` is at `$4B44` in the high-EPROM `$4000` bank. When the live countdown `$8AD9` hits zero, that instruction copies `$8E56` back into `$8AD9`. Entering the transmit phase forces `$8AD9` to `1` at `$4AD5`.

`PK` and `MM` refuse writes below `$8000`, and `$5480` is ROM with no RAM vector, so Host software cannot patch the parser. Writing the spinner value to `$8E56` and `$8AD9` from the PD start lab, including immediately before `PD` and again as soon as its ack returned, did not change key-down time. The airtime still followed bare `PD` as two repeats and any arguments as one. Correcting `x` for every Host program takes a ROM change.

Production Listen FEC still sends `PD` plus `n,x` (`AppConfig.ptSendHostCommand`). The PD start lab is a debug window and is not that path.
