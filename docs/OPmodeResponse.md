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
| Pactor Listen (`PN`) | SOH `$4F` O P P N *w* *x* *u* `?` *s* `?` ETB |
| Pactor Standby (`Pt`) | SOH `$4F` O P P t `$30` *x* *u* `?` *s* `?` ETB |
| Pactor ARQ (`PG` / PTConn) | SOH `$4F` O P P G *w* *x* *u* `?` *s* `?` ETB |

`$30` after `Pt` is always the standby marker for that mode. It is not the *w* status byte.

Identify an OPMODE response as CTL `$4F` followed by the characters `OP`, with more data after.

## Field *x* (direction)

| *x* | Meaning |
|---|---|
| `S` | Transmit |
| `R` | Receive |

## Field *w* (link / mode status)

Used on ARQ, ARQ Listen, FEC, SELFEC, Pactor Listen, and Pactor ARQ. Not used on FAX.

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

## Field *u* (baud rate)

Pactor OPMODE replies (`PN`, `Pt`, `PG`). First of the four trailer bytes after *x*. Hardware capture, not the Host manual.

| *u* | Baud Rate |
|---|---|
| `$31` | 100 |
| `$32` | 200 |

ASCII `'1'` / `'2'`. Matches Pactor I auto speed and the `PTS 1` / `PTS 2` selectors. ARQ capture (`docs/pgmesg status.txt`) phased at `$31`, upshifted to `$32` once linked, downshifted to `$31` after a long error run.

## Field *s* (longpath connect)

Pactor OPMODE replies (`PN`, `Pt`, `PG`). Third of the four trailer bytes after *x*. Hardware capture, not the Host manual.

| *s* | Meaning |
|---|---|
| `$30` | normal |
| `$31` | longpath |

`$31` when longpath (`!call`) was used; stays set after aborting the connection. Switching Pactor mode, or connecting without longpath, sets it back to `$30`.

## Morse speed

For mode Morse, *yz* = present receive Morse code speed in words per minute.

## Pactor signatures (hardware capture)

Pactor is **not** the AMTOR `AM` / `AC` / `AL` / `FE` rows. Host OPMODE uses the Pactor mnemonics themselves: `PN` (PTList), `Pt` (PACTor standby; case-sensitive `t`), and `PG` (PTConn ARQ). Chapter 4 rows above stay as the original Host reference; Pactor rows are from hardware capture.

Captured wire bytes:

| TNC mode | Raw Host block |
|---|---|
| Pactor Listen (`PN`) | `01 4F 4F 50 50 4E 31 52 31 30 30 30 17` |
| Pactor Standby (`Pt`) | `01 4F 4F 50 50 74 30 52 31 30 30 30 17` |
| Pactor ARQ (`PG`) | `01 4F 4F 50 50 47 34 53 32 30 30 30 17` |

Layout after `OP`:

- Listen: `P N` *w* *x* *u* `?` *s* `?`
- Standby: `P t` `$30` *x* *u* `?` *s* `?`
- ARQ: `P G` *w* *x* *u* `?` *s* `?`

Standby and listen captures had *x* = `R` (`$52`). Listen *w* was `$31` (Phasing). The ARQ example above is Traffic ISS at 200 baud (`PG4S2000`); full `PG` sequence is in `docs/pgmesg status.txt`.

On **`Pt`**, `$30` is a fixed standby marker, not the *w* sequence. Linked ARQ uses tag `PG`; listen uses `PN`. Unproto / FEC (`PD` / PTSend) is not yet captured.

Still needed (do not invent rows): OPMODE while **unproto / FEC** (`PD` / PTSend).

### Remaining trailer bytes

Pactor OPMODE replies (`PN`, `Pt`, `PG`) still have two unnamed payload bytes (2 and 4 of the four-byte trailer). They are ASCII digits. Parser/display should keep them as a raw trailer until named.

| Trailer byte | Observed | Meaning |
|---|---|---|
| 1 (*u*) | `$31` / `$32` | Baud rate — see Field *u* |
| 2 | `$30` `'0'` | Constant in every capture so far. |
| 3 (*s*) | `$30` / `$31` | Longpath connect — see Field *s* |
| 4 | `$30` `'0'` | Constant in every capture so far. |

Example trailers: `1000` (*u*=100, *s*=normal), `2000` (*u*=200, *s*=normal), `1010` (*u*=100, *s*=longpath).

ARQ (`PG`) *w* values seen in `docs/pgmesg status.txt`: `$31` Phasing (connect start), `$32` Change-over (only with *x*=`R` in that capture), `$33` Idle, `$34` Traffic, `$35` Error, `$36` RQ. `$30` is not used while `PG` — disconnect returns to `Pt`. `$37` Sync not seen yet.
