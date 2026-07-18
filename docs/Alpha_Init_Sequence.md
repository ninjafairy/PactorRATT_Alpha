# Alpha TNC Init Sequence

Ordered steps used by `TncInitializer` after **TNC → Connect**. COM framing is whatever the user set under Settings → COM Port (default 1200 7N1).

## 1. Open serial

Open the configured port with baud / data bits / parity / stop / flow from `AppConfig`.

## 2. Autobaud kick

When AUTOBAUD is ON (Host `Ab`), the TNC runs autobaud on power-on and on every RESTART. One asterisk (`*`, `0x2A`, no CR) sets terminal speed TBAUD; the routine detects 110–9600 baud at 7E1 or 8N1.

1. Drain inbound
2. Send `*` (no CR)
3. Monitor serial RX until **500 ms of clear air** (no received bytes), capped at 15 s total wait
4. Capture startup/sign-on text from RX
5. If non-empty, show a modal popup (**OK** only) before continuing init
6. Reset Host frame parser and drain inbound so Host framing starts clean

## 3. Detect Host Mode

1. Send `OGG` probe: `01 4F 47 47 17`
2. Success: `01 4F 47 47 00 17` → already in Host Mode
3. Else try double-SOH resync: `01 01 4F 47 47 17`
4. Else assume command mode and enter Host (step 4)

## 4. Enter Host Mode (if needed)

ASCII command-mode lines (CR-terminated), with short settle delays:

1. `AWLEN 8`
2. `PARITY 0`
3. `8BITCONV ON`
4. `RESTART` (longer settle)
5. Autobaud kick again (`*`, quiet-air capture + optional startup popup) — RESTART re-enters autobaud when AUTOBAUD is ON
6. `HOST ON`
7. Re-probe `OGG` (then resync once)

Failure → close port; stay offline.

## 5. Compatibility gate

1. Host `AE6` (decimal address for `$0006`) then four `MM` reads → bytes `$0006..$0009`
   - `AE` ack: payload `AE` + binary status `0x00` (OK)
   - `MM` response: payload `MM` + `$` + ASCII hex digits (e.g. `MM$93` → byte `0x93`), not binary status + data
2. Apply policy from [`Compat_Memory_Map.md`](Compat_Memory_Map.md):
   - Show a non-blocking **TNC Firmware / Hardware** popup with decoded firmware date (`$0006..$0008`) and all hardware bits of `$0009` (**OK** or auto-close after 4 s); init continues without waiting
   - Listed pre-v7 fingerprints → **hard refuse** (close; no `tncConnected`)
   - Supported v7.x → continue
   - HK / UDC / unknown → **warn** dialog (`KJ7RBS@gmail.com`) + Continue/Cancel

## 6. Coded Alpha init

Case-sensitive Host mnemonics.

**Host Mode encoding** (hardware-verified):

- **No space** between the 2-letter mnemonic and its argument (`HPN`, not `HP N`).
- ON/OFF boolean switches: `Y`/`N` (`HPN` = HPOLL off). Verbose `ON`/`OFF` also work without space (`HPOFF`) but `Y`/`N` is preferred.
- Some parameters take **integers**, not `Y`/`N` — e.g. PTHUFF Host `PH` uses a decimal level (`PH0` = off), not `PHN`.
- `ADDRESS` (`AE`) takes decimal digits after the mnemonic (`AE6` for `$0006`).
- `MEMORY` read response is `MM$hh` (ASCII hex after `$`); see step 5.

| Command | Purpose |
|---|---|
| `HPN` | HPOLL off (async push) |
| `EAN` | EAS off |
| `PBY` | PT200 on |
| `PH0` | PTHUFF off |
| `WON` | WORDOUT off |
| `ML<call>` / `Mf<call>` | MYCALL / MYPTCALL from config (if set) |
| `AA<n>` | Wrap columns from config (decimal) |
| `Pt` | Pactor standby |

## 7. Success

Set `tncConnected = true`. Main-window **Connect** remains ARQ-only; use **TNC → Disconnect** to close the session.

## UI entry points

| Action | Meaning |
|---|---|
| **TNC → Connect** | This init sequence |
| **TNC → Disconnect** | Close Host session / abort in-flight connect |
| **TNC → Debug Monitor…** | Live raw serial hex + ASCII; manual **Cmd** + **Payload** Send (Host CTL `0x4F`, mnemonic + args with no space). If connect fails while this window is open, the serial session stays open for debugging until the monitor is closed (or **TNC → Disconnect**). |
| Main **Connect** button | Start ARQ (`PTConn`) — requires `tncConnected` |
