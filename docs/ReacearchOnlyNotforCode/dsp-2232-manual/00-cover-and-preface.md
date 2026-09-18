# Cover, Preface, and Firmware Notes

Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` (PDF pp. 1–6).

OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were corrected where the intended English was clear. Command names, hex, Hz, timing, and pinouts were not invented. Garbled tokens are marked `[?]`.

## Warranty / FCC (stub)

The scan's preface (PDF pp. 2–4) is FCC Part 15 Class B text, AEA's 1990–1992 warranty/RMA shipping policy, and a request to return the warranty card. It is not needed to drive the modem. Use shielded cables; internal modifications may increase RF interference and void the user's authority to operate the unit. Copyright AEA, 1990; manual revision marks include 12/91, 12/92, 3/93, 7/92.

## Firmware notes in this scan (PDF pp. 5–6)

### (PDF p.5)

Gateway Upgrade
With the new Gateway firmware in your DSP-2232/1232, packet digipeating is a thing of
the past. The Gateway firmware functions like <The-Net> or NET/ROM. Instead of users
having to digipeat to connect to a distant station, they can now simply connect to your MYGATE
callsign.
Once connected, your Gateway firmware supports local acknowledgment (acks) of
packets just like a full service node. The node supports the following commands:
+++N7ML AEA Gateway. Type ? for help.
de N7ML-3 (B, C, D, J, L, N, S,)>
C (connect) n
C n STAY
D (isconnect)
J (heard)
L (isten)
N (odes)
S (end)
Log off Gateway
Connect to station "n"
Stay connected to gateway when
"n" disconnects
Cancel a connect attempt
Display stations heard
Toggle monitoring
Display nodes heard
Broadcast unproto (call CQ)
The Gateway firmware helps out fellow hams in your local area, by acting as a packet
node.
Your DSP now has the capability never before offered in a multi-mode controller: the
ability to "gateway" from packet-to-AMTOR, packet-to-PACTOR, and of course, packet-to-
packet. Under your command, you can allow packet users connecting to port 2 the ability to
monitor and link to other AMTOR, PACTOR, and even packet stations using your HF radio port.
The unique AEA Signal Identification Mode (SIAM) design now identifies all your
favorite digital modes, including PACTOR.
Up/down Doppler shift for PSK modems, outputs for up.down frequency stepping to
control the radio's frequency.
New Features & Enhancements
ALIST and PLIST have been enhanced to show AMTOR and PACTOR connect attempts.
ARXTOR enables automatic detection and switching between AMTOR and PACTOR
CODE 7 allows for upper and lowercase letters in AMTOR using the protocol that APLINK
stations, European mailboxes, the AMT-3, and B4BMK software use.

### (PDF p.6)

CODE 8 includes all features of CODE 7 and additionally codes new punctuation characters
using NULL.
DAYTIME now allows for the Dallas Semiconductor "Smart Watch" chips to be used to hold
the date and time when power is off.
EXPERT disables some of the less frequently used commands in the verbose mode.
GUSERS allows up to three stations to connect to the callsign of your MYGATE "Node" call.
MHEARD now identifies TCP/IP, NET/ROM and <The Net> stations.
MOPITT simplifies full break-in (QSK), in CW operation.
MYALIAS allows an alternate callsign to be used by other stations to connect to your station.
MYGATE is the callsign of the Node function of your TNC.
REINIT allows you to get out of trouble by re-initializing most of your commands to their
default settings.
SIAM now identifies PACTOR signals.
AEA WeFax 256
With this new Windows program, your DSP-2232/1232 can receive high resolution, true gray
scale weather fax images.
Features include:
The ability to receive from NOAA HF WeFax service or the NOAA APT satellite
service.
Two modes of resolution-500 or 250 pixels per line-which insure that AEA WeFax 256
will always work on your system. Incoming data is stored in a buffer so you can change
the resolution later if you change your mind.
Supports BMP, GIF, PCX, TIF, and JPG image formats so you can share images with
your friends, or use the images in other programs.
Includes complete image processor that gives you the ability to enhance received images.
Enhancements include brightness control, contrast, gamma, sharpness, negative, blur, etc.
It even includes a function that adds "false" color to the images you receive.
Requires: 386 PC compatible computer, Windows 3.1, 2 MB RAM, 5 MB free hard-disk space,
and a 256-color VGA monitor and video card.
