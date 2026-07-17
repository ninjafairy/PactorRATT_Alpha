Software Date Code and hardware memory addresses. 
returns a hex value to be interpeted as human readable decimal
$86 $07 $29 decodes litterally as YY MM DD so 86-07-29 or 1986 July 29

$0006   YY year
$0007   MM month
$0008   DD day

$0009 hold hardware version

older software returns $C3

case 1: bits 7-6-5 as 0-1-1 indicates PK-232 good hardware
case 2: bits 7-6-5 as 1-0-0 indicates unknown UDC-232 make popup telling user to email me
case 3: bits 4-3-2-1-0 as 0-0-0-1-0 indicates PK-232 good hardware
case 4: bits 4-3-2-1-0 as 0-1-0-0-1 indicates HK-232 good hardware

unknown if the low bits in cases 1+2 or high bits in cases 3+4 are relevant or if only need to match the specified bits.

assume unused bits in $0009 are dont care for now

known fingerprints given in groups of 2 chars  representing the recieved HEX byte

## Following the text inside the * are the 4 bytes from $0006-$0009
The first 3 represent YY-MM-DD
The last one is the Product Type Code
##


*Unsupported* 86 09 15 C3

*unsupported* 87 03 04 62

*unsupported* 87 06 25 62

*unsupported* 88 02 23 62

*unsupported* 89 10 31 C2

*unsupported* 90 07 19 C2

*unsupported* 91 08 01 C2

*supported v7.0* 93 03 05 C2

*supported v7.0a* 93 12 01 C2

*supported v7.1* 95 09 13 C2

*supported v7.2* 98 08 10 C2

*unsupported HK-232* 87 06 25 69

*unsupported HK-232* 88 02 23 69

*unsupported HK-232* 89 10 31 69


all other unknown fingerprints should cause a popup error with unknown and the 4 byte fingerprint and instructions to email it to KJ7RBS@gmail.com