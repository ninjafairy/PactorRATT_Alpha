"""Convert line-broken OCR pages into chapter Markdown + command catalog."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(r"c:\Users\Jadon\Documents\GitHub\PactorRATT_900-2232\docs\dsp-2232-manual")
OCR = ROOT / "_work" / "ocr"
OUT = ROOT

CHAPTERS = [
    {
        "file": "00-cover-and-preface.md",
        "title": "Cover, Preface, and Firmware Notes",
        "start": 1,
        "end": 6,
        "kind": "front",
    },
    {
        "file": "00-table-of-contents.md",
        "title": "Table of Contents",
        "start": 7,
        "end": 15,
        "kind": "toc",
    },
    {
        "file": "01-introduction.md",
        "title": "Chapter 1 — Introduction",
        "start": 16,
        "end": 19,
        "kind": "chapter",
        "ch": 1,
    },
    {
        "file": "02-computer-installation.md",
        "title": "Chapter 2 — Computer Installation",
        "start": 20,
        "end": 32,
        "kind": "chapter",
        "ch": 2,
    },
    {
        "file": "03-radio-installation.md",
        "title": "Chapter 3 — Radio Installation",
        "start": 33,
        "end": 44,
        "kind": "chapter",
        "ch": 3,
    },
    {
        "file": "04-packet-radio.md",
        "title": "Chapter 4 — Packet Radio",
        "start": 45,
        "end": 84,
        "kind": "chapter",
        "ch": 4,
    },
    {
        "file": "05-maildrop.md",
        "title": "Chapter 5 — Maildrop Operation",
        "start": 85,
        "end": 96,
        "kind": "chapter",
        "ch": 5,
    },
    {
        "file": "06-baudot-ascii-rtty.md",
        "title": "Chapter 6 — Baudot and ASCII RTTY Operation",
        "start": 97,
        "end": 110,
        "kind": "chapter",
        "ch": 6,
    },
    {
        "file": "07-amtor-navtex.md",
        "title": "Chapter 7 — AMTOR and NAVTEX Operation",
        "start": 111,
        "end": 131,
        "kind": "chapter",
        "ch": 7,
    },
    {
        "file": "08-morse.md",
        "title": "Chapter 8 — Morse Operation",
        "start": 132,
        "end": 136,
        "kind": "chapter",
        "ch": 8,
    },
    {
        "file": "09-facsimile-sstv.md",
        "title": "Chapter 9 — Facsimile and SSTV Operation",
        "start": 137,
        "end": 144,
        "kind": "chapter",
        "ch": 9,
    },
    {
        "file": "10-siam-tdm.md",
        "title": "Chapter 10 — Signal Identification and TDM",
        "start": 145,
        "end": 148,
        "kind": "chapter",
        "ch": 10,
    },
    {
        "file": "11-satellite.md",
        "title": "Chapter 11 — Satellite Operation",
        "start": 149,
        "end": 156,
        "kind": "chapter",
        "ch": 11,
    },
    {
        "file": "12-pactor.md",
        "title": "Chapter 12 — PACTOR Operation",
        "start": 157,
        "end": 176,
        "kind": "chapter",
        "ch": 12,
    },
]

WORD_FIXES = [
    (r"DSP[\u2014\u2013\u2012\-�]2232", "DSP-2232"),
    (r"PK[\u2014\u2013\-]232", "PK-232"),
    (r"PK[\u2014\u2013\-]FAX", "PK-FAX"),
    (r"\bThig\b", "This"),
    (r"\bthig\b", "this"),
    (r"\bcauge\b", "cause"),
    (r"\bParaqraph\b", "Paragraph"),
    (r"Overv\s*i\s*ew", "Overview"),
    (r"\bPAC\s*TOR\b", "PACTOR"),
    (r"\bAM\s*TOR\b", "AMTOR"),
    (r"\bcomrnand\b", "command"),
    (r"\bcorrunand\b", "command"),
    (r"\bconunand\b", "command"),
    (r"\bconmand\b", "command"),
    (r"\bComrnand\b", "Command"),
    (r"\bcomrnunicat", "communicat"),
    (r"\bConununicat", "Communicat"),
    (r"\bOperatinq\b", "Operating"),
    (r"\bEnterinq\b", "Entering"),
    (r"\bEnter inq\b", "Entering"),
    (r"\bGettinq\b", "Getting"),
    (r"\bConnect inq\b", "Connecting"),
    (r"\bConnectinq\b", "Connecting"),
    (r"\bAdiustments\b", "Adjustments"),
    (r"\bProqrams\b", "Programs"),
    (r"\bProqram\b", "Program"),
    (r"\bWirinq\b", "Wiring"),
    (r"\bFind inq\b", "Finding"),
    (r"\bInvert inq\b", "Inverting"),
    (r"\bIdentif\s*i\s*cation\b", "Identification"),
    (r"\bSuqqegted\b", "Suggested"),
    (r"\bSettinqg\b", "Settings"),
    (r"\bAngwerBack\b", "AnswerBack"),
    (r"\bAngwer\b", "Answer"),
    (r"\bcallBign\b", "callsign"),
    (r"\bcall sign\b", "callsign"),
    (r"\bBtation\b", "Station"),
    (r"\bacceggible\b", "accessible"),
    (r"\baccegg\b", "access"),
    (r"\bModu\s*ator\b", "Modulator"),
    (r"\bDemodu\s*ator\b", "Demodulator"),
    (r"\bdiscr\s*iminator\b", "discriminator"),
    (r"\bMULTipIe\b", "MULTiple"),
    (r"Phase[\u2014\-]cont\s*inuous", "Phase-continuous"),
    (r"\bZ\s*i\s*log\b", "Zilog"),
    (r"\bPush-To-Ta1k\b", "Push-To-Talk"),
    (r"\bPush—To—Ta1k\b", "Push-To-Talk"),
    (r"\b4EA\b", "AEA"),
    (r"\bPARRATT\b", "PAKRATT"),
    (r"\bMorge\b", "Morse"),
    (r"\bunlegg\b", "unless"),
    (r"\bBimply\b", "simply"),
    (r"\bDigplavinq\b", "Displaying"),
    (r"\bsiqnal\b", "signal"),
    (r"\bA1\s*BO\b", "Also"),
    (r"\bWe 1 come\b", "Welcome"),
    (r"\bPolicv\b", "Policy"),
    (r"\bf ine\b", "fine"),
    (r"\bcontaing\b", "contains"),
    (r"\bTrangmitter\b", "Transmitter"),
    (r"\bSpecif icationg\b", "Specifications"),
    (r"\bAg part\b", "As part"),
    (r"\balgo be\b", "also be"),
    (r"\bI nput\b", "Input"),
    (r"\bBingle\b", "single"),
    (r"\bkeyg\b", "keys"),
    (r"\bcornmon\b", "common"),
    (r"\bDescr\s*ion\b", "Description"),
    (r"\bCo Ior\b", "Color"),
    (r"\bWh ite\b", "White"),
    (r"\bOperatinq Manual\b", "Operating Manual"),
    (r"\bgatelliteg\b", "satellites"),
    (r"\bf requency\b", "frequency"),
    (r"\bcomplet e\b", "complete"),
    (r"\bCqnplete\b", "Complete"),
    (r"\bWordB\b", "Words"),
    (r"\bCorrunand\b", "Command"),
    (r"\bconunand\b", "command"),
    (r"\biB\b", "is"),
    (r"\bi tg\b", "its"),
    (r"\bitg\b", "its"),
    (r"\bThig\b", "This"),
    (r"\bwag\b", "was"),
    (r"\bhag\b", "has"),
    (r"\big\b", "is"),
    (r"\bag the\b", "as the"),
    (r"\bag well\b", "as well"),
    (r"\bag a\b", "as a"),
    (r"\bag described\b", "as described"),
    (r"\bag shown\b", "as shown"),
    (r"\bused ag\b", "used as"),
    (r"\bgome\b", "some"),
    (r"\bbeet\b", "best"),
    (r"\bHang—Peter\b", "Hans-Peter"),
    (r"\bHang-Peter\b", "Hans-Peter"),
    (r"\bARO\b", "ARQ"),
    (r"\bPAKRATT\b", "PAKRATT"),
    (r"\bgetting of command\b", "setting of command"),
    (r"\bgetting of\b", "setting of"),
    (r"\baccegg\b", "access"),
    (r"\bLoqon\b", "Logon"),
    (r"\bMinen\b", "Mine)"),
    (r"\bory\)\b", "ory)"),
    (r"DIRECT \(ory\)", "DIRECT(ory)"),
    (r"\bF\s+SK\b", "FSK"),
    (r"\bHP Packet\b", "HF Packet"),
    (r"\b11E\b", "IIe"),
    (r"\b11\+\b", "II+"),
    (r"\bIBM—PCB\b", "IBM-PCs"),
    (r"\bWindows 3\.\s*l\b", "Windows 3.1"),
    (r"\bT\s*IF\b", "TIF"),
    (r"\bbread-in\b", "break-in"),
    (r"\bMY GATE\b", "MYGATE"),
    (r"\bMY ALIAS\b", "MYALIAS"),
    (r"\bPakMaiI\b", "PakMail"),
    (r"\bMail Drop\b", "MailDrop"),
    (r"\bMaildrop\b", "MailDrop"),
    (r"\bCormands\b", "Commands"),
    (r"\bCompat ib i I it y\b", "Compatibility"),
    (r"\bCompat ibi 1 i ty\b", "Compatibility"),
    (r"\bInternat ional\b", "International"),
    (r"\bBreak-ln\b", "Break-in"),
    (r"\bconf iguration\b", "configuration"),
    (r"\bConf iguration\b", "Configuration"),
    (r"\bTrangceiver\b", "Transceiver"),
    (r"\btransceiver •g\b", "transceiver's"),
    (r"\btransceiver' g\b", "transceiver's"),
    (r"\bradio' g\b", "radio's"),
    (r"\bradio' B\b", "radio's"),
    (r"\bDSP—2232 ts\b", "DSP-2232's"),
    (r"\bDSP-2232 ts\b", "DSP-2232's"),
    (r"\bUS •amateurs\b", "US amateurs"),
    (r"\bcan •t\b", "can't"),
    (r"\blet •g\b", "let's"),
    (r"\bwon 't\b", "won't"),
    (r"\bYou 've\b", "You've"),
    (r"\bYou 're\b", "You're"),
    (r"\bdidn 't\b", "didn't"),
    (r"\bisn 't\b", "isn't"),
    (r"\bdoesn 't\b", "doesn't"),
    (r"[\u2014\u2013]", "-"),
    (r"�", "-"),
]

HEADER_EXACT = {
    "DSP-2232 OPERATING MANUAL",
    "DSP-2232 OPERATING -MANUAL",
    "PREFACE",
    "TABLE OF CONTENTS",
    "CHAPTER TABLE OF CONTENTS",
    "COMPUTER INSTALLATION",
    "RADIO INSTALLATION",
    "RADIO",
    "INSTALLATION",
    "PACKET RADIO",
    "MAILDROP OPERATION",
    "BAUDOT AND ASCII OPERATION",
    "BAUDOT AND ASCII RTTY OPERATION",
    "AMTOR AND NAVTEX OPERATION",
    "MORSE OPERATION",
    "FACSIMILE OPERATION",
    "SIAM AND TDM OPERATION",
    "SATELLITE OPERATION",
    "PACTOR OPERATION",
    "INTRODUCTION",
    "- INTRODUCTION",
}

HEADER_RE = re.compile(
    r"^(CHAPTER\s+\d+|CHAPTER\s+\d+\s*-\s*.+|DSP-2232\s+OPERATING\s+MANUAL.*)$",
    re.I,
)
PAGE_NUM_RE = re.compile(r"^(\d{1,2}-\d{1,2}|[ivx]+|TOC-\d+)\s*$", re.I)
DATE_RE = re.compile(r"^\d{1,2}/\d{2}$")
SECTION_ONLY_RE = re.compile(r"^\d{1,2}(?:\.\s*\d{1,2}){1,4}\.?\s*$")
SECTION_TITLE_RE = re.compile(
    r"^(\d{1,2}(?:\.\s*\d{1,2}){1,4})\s+([A-Za-z(].+)$"
)
NOISE_RE = re.compile(r"^(rd|U\)|\(d|c:|53|or)$")

CMD_NEAR = re.compile(
    r"\b(the|command|type|typed|using|enter|set|setting|turn|turning)\s+"
    r"([A-Z][A-Z0-9]{1,11})\b"
    r"|"
    r"\b([A-Z][A-Z0-9]{1,11})\s+(command|ON|OFF|was|now)\b"
    r"|"
    r"cmd:([A-Z][A-Za-z0-9]+)",
    re.I,
)

KNOWN_CMDS = {
    "3RDPARTY", "AAB", "ACHG", "ADELAY", "ALIST", "ARXTOR", "AUDELAY",
    "AXDELAY", "AXHANG", "CFROM", "CHECK", "CHSWITCH", "CODE", "CSTATUS",
    "DAYTIME", "DIRECT", "EAS", "ERCHR", "EXPERT", "FAXNEG", "FRACK",
    "GRAPHICS", "GUSERS", "HOMEBBS", "HOST", "LEFTRITE", "LOCK",
    "MAILDROP", "MAXFRAME", "MDCHECK", "MDMON", "MDPROMPT", "MFILTER",
    "MHEARD", "MMSG", "MODEM", "MOPITT", "MSPEED", "MSTAMP", "MYALIAS",
    "MYCALL", "MYGATE", "MYIDENT", "MYMAIL", "MYPTCALL", "MYSELCAL",
    "PACLEN", "PACTOR", "PLIST", "PRTYPE", "PT", "PT200", "PTCONN",
    "PTLIST", "PTSEND", "QSIGNAL", "RADIO", "REINIT", "RELINK", "RETRY",
    "SIAM", "TBAUD", "TMAIL", "TXDELAY", "UCMD", "USOS", "WORDOUT",
    "AWLEN", "PARITY", "8BITCONV", "RESTART", "PTSEND", "PTOVER",
    "MONITOR", "CONVERSE", "TRANS", "KISS", "FULLDUP", "BEACON",
    "DIGIPEAT", "HID", "MYALIAS", "CONSTAMP", "CMSG", "CTEXT",
}


def load_page(n: int) -> str:
    return (OCR / f"page-{n:03d}.txt").read_text(encoding="utf-8")


def apply_fixes(text: str) -> str:
    for pat, repl in WORD_FIXES:
        text = re.sub(pat, repl, text)
    text = text.replace("\u00a0", " ")
    return text


def is_header_footer(line: str) -> bool:
    s = line.strip()
    if not s:
        return False
    if s in HEADER_EXACT:
        return True
    if HEADER_RE.match(s) and len(s) < 60:
        return True
    if PAGE_NUM_RE.match(s) or DATE_RE.match(s):
        return True
    if NOISE_RE.match(s):
        return True
    if s in {"CHAPTER 1", "CHAPTER 2", "CHAPTER 3", "CHAPTER 4", "CHAPTER 5",
             "CHAPTER 6", "CHAPTER 7", "CHAPTER 8", "CHAPTER 9", "CHAPTER 10",
             "CHAPTER 11", "CHAPTER 12"}:
        return True
    return False


def section_heading(line: str) -> str | None:
    s = line.strip()
    m = SECTION_TITLE_RE.match(s)
    if m:
        num = re.sub(r"\s+", "", m.group(1))
        title = m.group(2).strip()
        depth = num.count(".") + 2
        depth = min(max(depth, 2), 5)
        return f"{'#' * depth} {num} {title}"
    m = SECTION_ONLY_RE.match(s)
    if m:
        num = re.sub(r"\s+", "", s.rstrip("."))
        if num.endswith("."):
            num = num[:-1]
        # skip lone "4." noise from TOC
        if re.fullmatch(r"\d{1,2}\.?", num) and len(num) <= 3 and num.endswith("."):
            return None
        depth = num.count(".") + 2
        depth = min(max(depth, 2), 5)
        return f"{'#' * depth} {num}"
    return None


def clean_page_lines(raw: str, kind: str) -> list[str]:
    raw = apply_fixes(raw)
    out: list[str] = []
    for line in raw.splitlines():
        s = line.strip()
        if not s:
            if out and out[-1] != "":
                out.append("")
            continue
        if kind != "toc" and is_header_footer(s):
            continue
        if kind == "toc" and s in {"DSP-2232 OPERATING MANUAL", "DSP-2232"}:
            continue
        heading = None if kind in {"front", "toc"} else section_heading(s)
        if heading:
            if out and out[-1] != "":
                out.append("")
            out.append(heading)
            out.append("")
            continue
        if s in {"o", "O", "0"}:
            continue
        if s.startswith("o "):
            s = "- " + s[2:]
        out.append(s)
    # collapse 3+ blanks
    collapsed: list[str] = []
    blank = 0
    for line in out:
        if line == "":
            blank += 1
            if blank <= 1:
                collapsed.append("")
        else:
            blank = 0
            collapsed.append(line)
    return collapsed


def fence_cmd_lines(lines: list[str]) -> list[str]:
    out: list[str] = []
    i = 0
    while i < len(lines):
        s = lines[i]
        if re.match(r"^(cmd:|Opmode|MY[A-Z]+ was|MY[A-Z]+ now|RAdio was|RAdio now)", s):
            block = [s]
            i += 1
            while i < len(lines) and re.match(
                r"^(cmd:|Opmode|MY[A-Z]+ was|MY[A-Z]+ now|RAdio was|RAdio now|\s{0,2}was |\s{0,2}now )",
                lines[i],
            ):
                block.append(lines[i])
                i += 1
            out.append("```text")
            out.extend(block)
            out.append("```")
            continue
        out.append(s)
        i += 1
    return out


def build_chapter(spec: dict) -> str:
    parts = [
        f"# {spec['title']}",
        "",
        f"Source: AEA DSP-2232 Operating Manual scan `docs/dsp2232-manual.pdf` "
        f"(PDF pp. {spec['start']}–{spec['end']}).",
        "",
        "OCR from an image-only scan (Windows.Media.Ocr). Obvious word errors were "
        "corrected where the intended English was clear. Command names, hex, Hz, "
        "timing, and pinouts were not invented. Garbled tokens are marked `[?]`.",
        "",
    ]
    if spec["kind"] == "front":
        parts += [
            "## Warranty / FCC (stub)",
            "",
            "The scan's preface (PDF pp. 2–4) is FCC Part 15 Class B text, AEA's "
            "1990–1992 warranty/RMA shipping policy, and a request to return the "
            "warranty card. It is not needed to drive the modem. Use shielded "
            "cables; internal modifications may increase RF interference and void "
            "the user's authority to operate the unit. Copyright AEA, 1990; "
            "manual revision marks include 12/91, 12/92, 3/93, 7/92.",
            "",
            "## Firmware notes in this scan (PDF pp. 5–6)",
            "",
        ]
    for n in range(spec["start"], spec["end"] + 1):
        raw = load_page(n)
        if spec["kind"] == "front" and n <= 4:
            continue
        lines = clean_page_lines(raw, spec["kind"])
        lines = fence_cmd_lines(lines)
        if not any(x.strip() for x in lines):
            parts.append(f"### (PDF p.{n})")
            parts.append("")
            parts.append("*[Page is a figure or nearly empty after header stripping.]*")
            parts.append("")
            continue
        parts.append(f"### (PDF p.{n})")
        parts.append("")
        parts.extend(lines)
        if parts[-1] != "":
            parts.append("")
    text = "\n".join(parts)
    text = re.sub(r"\n{3,}", "\n\n", text)
    return text.rstrip() + "\n"


def extract_commands() -> list[dict]:
    found: dict[str, dict] = {}
    for spec in CHAPTERS:
        for n in range(spec["start"], spec["end"] + 1):
            raw = apply_fixes(load_page(n))
            for m in re.finditer(r"cmd:([A-Za-z][A-Za-z0-9]+)", raw):
                name = m.group(1).upper()
                rec = found.setdefault(
                    name,
                    {
                        "name": name,
                        "host": None,
                        "pages": [],
                        "examples": [],
                        "notes": [],
                    },
                )
                if n not in rec["pages"]:
                    rec["pages"].append(n)
                ex = m.group(0)
                if ex not in rec["examples"] and len(rec["examples"]) < 5:
                    rec["examples"].append(ex)
            for token in re.findall(r"\b[A-Z][A-Z0-9]{2,11}\b", raw):
                if token not in KNOWN_CMDS:
                    continue
                rec = found.setdefault(
                    token,
                    {
                        "name": token,
                        "host": None,
                        "pages": [],
                        "examples": [],
                        "notes": [],
                    },
                )
                if n not in rec["pages"]:
                    rec["pages"].append(n)
            # capture UCMD defaults from ch12
            for m in re.finditer(
                r"UCMD\s+([0-9O]):\s*(?:Default\s+([^,\.]+))?",
                raw,
                re.I,
            ):
                key = f"UCMD {m.group(1).replace('O', '0')}"
                rec = found.setdefault(
                    key,
                    {
                        "name": key,
                        "host": None,
                        "pages": [],
                        "examples": [],
                        "notes": [],
                    },
                )
                if n not in rec["pages"]:
                    rec["pages"].append(n)
                if m.group(2):
                    rec["notes"].append(f"default mentioned near OCR: {m.group(2).strip()}")
    # Host mnemonics are NOT printed in this operating-manual scan.
    rows = []
    for name, rec in sorted(found.items(), key=lambda kv: kv[0]):
        rec["pages"] = sorted(rec["pages"])
        rec["source"] = "AEA DSP-2232 Operating Manual OCR; Host mnemonic not printed in this scan"
        rec["host"] = None
        rows.append(rec)
    return rows


def main() -> None:
    OUT.mkdir(parents=True, exist_ok=True)
    created = []
    for spec in CHAPTERS:
        text = build_chapter(spec)
        path = OUT / spec["file"]
        path.write_text(text, encoding="utf-8")
        created.append((path, path.stat().st_size))
        print(f"wrote {path.name} {path.stat().st_size} bytes")
    cmds = extract_commands()
    catalog = {
        "device": "AEA DSP-2232 Data Controller",
        "source_pdf": "docs/dsp2232-manual.pdf",
        "page_count": 176,
        "coverage": "Operating manual chapters 1-12 only. Appendix command summary is NOT in this PDF.",
        "host_mnemonics": "Not printed in this scan. See PK-232 HostCommands docs or a DSP technical manual if available.",
        "commands": cmds,
    }
    cpath = OUT / "commands.json"
    cpath.write_text(json.dumps(catalog, indent=2), encoding="utf-8")
    print(f"wrote {cpath.name} {cpath.stat().st_size} bytes / {len(cmds)} commands")


if __name__ == "__main__":
    main()
