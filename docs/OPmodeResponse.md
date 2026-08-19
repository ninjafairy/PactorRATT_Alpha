# 4.3.2 OPMODE Response

Source: PK-232 Technical Manual, Chapter 4 Host Mode, §4.3.2.

The computer can use the OPMODE command (`OP`) to interrogate the PK-232 for information on its current operating mode. The PK-232 sends one of the following responses:

| Mode | Host block |
|---|---|
| Packet | SOH `$4F` O P P A ETB |
| Morse | SOH `$4F` O P M O *x* *y* *z* ETB |
| Baudot | SOH `$4F` O P B A *x* ETB |
| ASCII | SOH `$4F` O P A S *x* ETB |
| AMTOR Standby | SOH `$4F` O P A M `$30` *x* ETB |
| ARQ | SOH `$4F` O P A C *w* *x* ETB |
| ARQ Listen | SOH `$4F` O P A L *w* R ETB |
| FEC | SOH `$4F` O P F E *w* *x* ETB |
| SELFEC | SOH `$4F` O P S E *w* *x* ETB |
| FAX | SOH `$4F` O P F A *v* *x* ETB |
| Pactor Listen (`PN`) | SOH `$4F` O P P N *w* *x* `?` `?` `?` `?` ETB |
| Pactor Standby (`Pt`) | SOH `$4F` O P P t `$30` *x* `?` `?` `?` `?` ETB |

`$30` after `Pt` is always the standby marker for that mode. It is not the *w* status byte.

Identify an OPMODE response as CTL `$4F` followed by the characters `OP`, with more data after.

## Field *x* (direction)

| *x* | Meaning |
|---|---|
| `S` | Transmit |
| `R` | Receive |

## Field *w* (link / mode status)

Used on ARQ, ARQ Listen, FEC, SELFEC, and Pactor Listen. Not used on FAX.

| *w* | Meaning |
|---|---|
| `$30` | Standby |
| `$31` | Phasing |
| `$32` | Change-over |
| `$33` | Idle |
| `$34` | Traffic |
| `$35` | Error |
| `$36` | RQ |
| `$37` | Sync |

## Field *v* (FAX only)

FAX is the only OPMODE reply that has a *v* byte. Meaning to be added later.

## Morse speed

For mode Morse, *yz* = present receive Morse code speed in words per minute.

## Pactor signatures (hardware capture)

Pactor is **not** the AMTOR `AM` / `AC` / `AL` / `FE` rows. Host OPMODE uses the Pactor mnemonics themselves: `PN` (PTList) and `Pt` (PACTor standby; case-sensitive `t`). Chapter 4 rows above stay as the original Host reference; Pactor rows are from hardware capture.

Captured wire bytes:

| TNC mode | Raw Host block |
|---|---|
| Pactor Listen (`PN`) | `01 4F 4F 50 50 4E 31 52 31 30 30 30 17` |
| Pactor Standby (`Pt`) | `01 4F 4F 50 50 74 30 52 31 30 30 30 17` |

Layout after `OP`:

- Listen: `P N` *w* *x* then four trailing bytes
- Standby: `P t` `$30` *x* then four trailing bytes

In these captures *x* was `R` (`$52`). Listen *w* was `$31` (Phasing in the *w* table).

On **`Pt`**, `$30` is a fixed standby marker, not the *w* sequence. Linked or listen Pactor uses a different tag/layout (`PN` today; ARQ / PTSend not yet captured).

Still needed (do not invent rows): OPMODE while **linked ARQ** (`PG` / PTConn) and while **unproto / FEC** (`PD` / PTSend).

### Trailing four mystery bytes

Both Pactor OPMODE replies ended with four extra payload bytes before ETB:

`31 30 30 30`  (ASCII `1000`)

Meaning is unknown. Do not decode them as *w* / Morse *yz* until a later capture shows they change with a known TNC state. Parser/display should keep them as raw trailer.

byte 3 switches from $30 to $31 when longpath is used and stays set after aborting connection
switching to a different pactor mode or connecting without longpath set it back to $30
