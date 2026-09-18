"""
Convert the Timewave/AEA PK-900 Operating Manual PDF into AI-friendly Markdown.

Source: docs/timewave-aea-pk-900-manual-nov-2004.pdf
Output: docs/pk-900-manual/
"""
from __future__ import annotations

import json
import re
from collections import defaultdict
from pathlib import Path

from pypdf import PdfReader

ROOT = Path(r"c:\Users\Jadon\Documents\GitHub\PactorRATT_900-2232")
PDF = ROOT / "docs" / "timewave-aea-pk-900-manual-nov-2004.pdf"
OUT = ROOT / "docs" / "pk-900-manual"
BOOK = "Timewave / AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)"

FOOTER_RE = re.compile(
    r"^(?:"
    r"\d{1,2}/\d{2}\s+[A-Z]{0,4}-?\d{0,4}"
    r"|[12]/\d{2}\s+[ivx]+"
    r"|TOC-\d+"
    r"|The rest of this page is blank\.?"
    r"|This page is used to make the number of pages in this chapter an even number\.?"
    r"|This page intentionally left blank\.?"
    r")\s*$",
    re.I,
)
DATE_FOOTER_RE = re.compile(r"^\d{1,2}/\d{2}\s+\S*\s*$")
UNDERSCORES_RE = re.compile(r"^_+\s*$")
PARAM_BAR_RE = re.compile(r"^_+\s*Parameters:\s*_+\s*$", re.I)
SECTION_RE = re.compile(
    r"^((?:[A-F]|\d{1,2})\.\d{1,2}(?:\.\d{1,2}){0,4})\s+([A-Za-z(].*)$"
)
CHAPTER_BANNER_RE = re.compile(r"^CHAPTER\s+(\d+)\s*$", re.I)
APPENDIX_BANNER_RE = re.compile(r"^APPENDIX\s+([A-F])\s*$", re.I)
CMD_NAME_RE = re.compile(r"^([0-9]?[A-Za-z][A-Za-z0-9]*)\b(.*)$")
HOST_RE = re.compile(r"Host:?\s*([A-Za-z0-9]{1,3})\s*$")
DEFAULT_RE = re.compile(r"(?:^|\s)Default:?\s*(.+?)\s*$", re.I)
IMMEDIATE_RE = re.compile(r"Immediate Command", re.I)
MODE_RE = re.compile(r"^Mode:\s*(.+?)(?:\s+Host:?\s*\S+)?\s*$", re.I)
BULLET_RE = re.compile(r"^o\s+(.+)")
DOT_LEADER_RE = re.compile(r"\.{4,}")


def extract_pages() -> list[tuple[int, str]]:
    reader = PdfReader(str(PDF))
    pages = []
    for i, page in enumerate(reader.pages):
        text = page.extract_text() or ""
        pages.append((i + 1, text))
    return pages


def normalize_chars(text: str) -> str:
    repl = {
        "\u2018": "'",
        "\u2019": "'",
        "\u201c": '"',
        "\u201d": '"',
        "\u2013": "-",
        "\u2014": "-",
        "\u00a0": " ",
        "\ufb01": "fi",
        "\ufb02": "fl",
        "\u000c": "",
        "\ufffd": "'",
        "\x92": "'",
        "\x93": '"',
        "\x94": '"',
        "\x96": "-",
        "\x97": "-",
        "\x85": "...",
    }
    for old, new in repl.items():
        text = text.replace(old, new)
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    return text


def clean_page(text: str) -> str:
    text = normalize_chars(text)
    kept: list[str] = []
    for raw in text.splitlines():
        line = raw.rstrip()
        stripped = line.strip()
        if not stripped:
            if kept and kept[-1] != "":
                kept.append("")
            continue
        if FOOTER_RE.match(stripped) or DATE_FOOTER_RE.match(stripped):
            continue
        if re.match(r"^(PK-900 OPERATING MANUAL|APPENDIX [A-F])\s*$", stripped):
            continue
        if re.match(r"^PK-900 Gateway Option Supplement\d*\s*$", stripped):
            continue
        if re.match(r"^\d+PK-900 Gateway Option Supplement\s*$", stripped):
            continue
        if re.match(r"^TNC GPS Upgrade Addendum/\s*\d+\s*$", stripped):
            continue
        if re.match(r"^\d+\s*/\s*TNC GPS Upgrade Addendum\s*$", stripped):
            continue
        kept.append(line)
    # collapse 3+ blank lines
    out: list[str] = []
    blanks = 0
    for line in kept:
        if line == "":
            blanks += 1
            if blanks <= 2:
                out.append("")
        else:
            blanks = 0
            out.append(line)
    return "\n".join(out).strip()


def join_hyphenation(text: str) -> str:
    lines = text.splitlines()
    out: list[str] = []
    i = 0
    while i < len(lines):
        line = lines[i]
        if i + 1 < len(lines):
            trimmed = line.rstrip()
            nxt = lines[i + 1].lstrip()
            if trimmed.endswith("-") and nxt:
                first = nxt[0]
                rest_of_next = nxt.split(" ", 1)
                word = rest_of_next[0]
                remainder = rest_of_next[1] if len(rest_of_next) == 2 else ""
                if first.islower():
                    keep = word.split("-", 1)[0].lower() in {
                        "of", "to", "in", "up", "on", "and", "or", "the", "a", "an",
                    }
                    joined = (trimmed + word) if keep else (trimmed[:-1] + word)
                    if remainder:
                        joined += " " + remainder
                    lines[i + 1] = joined
                    i += 1
                    continue
                if first.isupper() and not nxt.startswith("The ") and len(word) < 24:
                    joined = trimmed + word
                    if remainder:
                        joined += " " + remainder
                    lines[i + 1] = joined
                    i += 1
                    continue
        out.append(line)
        i += 1
    return "\n".join(out)


def stitch_pages(pages: list[tuple[int, str]]) -> str:
    pieces: list[str] = []
    last_content = ""
    for num, raw in pages:
        cleaned = join_hyphenation(clean_page(raw))
        marker = f"<!-- PDF p.{num} -->"
        if not cleaned:
            pieces.append(f"\n\n{marker}\n\n> [No extractable text on this page — figure, schematic, or blank.]\n")
            last_content = ""
            continue
        if last_content and last_content[-1].isalnum() and cleaned[0].islower():
            pieces.append(cleaned)
        else:
            pieces.append(f"\n\n{marker}\n\n")
            pieces.append(cleaned)
        last_content = cleaned.rstrip()
    return "".join(pieces).strip() + "\n"


def heading_level(number: str) -> int:
    # N.N -> ###, N.N.N -> ####  (# book / ## chapter already used)
    return min(2 + number.count("."), 6)


def looks_like_heading(title: str) -> bool:
    if len(title) > 90:
        return False
    if title.endswith(".") and len(title) > 40:
        return False
    return True


SPEC_LINE_RE = re.compile(r"^[A-Za-z][A-Za-z0-9 +\-/()'']{1,40}:\s{2,}\S")
MODEM_LINE_RE = re.compile(r"^MODEM\s+\d+\s+")


def fence_spec_blocks(lines: list[str]) -> list[str]:
    """Keep aligned spec / modem lists readable for later agents."""
    out: list[str] = []
    i = 0
    while i < len(lines):
        if SPEC_LINE_RE.match(lines[i]) or MODEM_LINE_RE.match(lines[i]):
            block = [lines[i]]
            i += 1
            while i < len(lines) and (
                SPEC_LINE_RE.match(lines[i])
                or MODEM_LINE_RE.match(lines[i])
                or (re.match(r"^ {2,}\S", lines[i]) and not lines[i].lstrip().startswith("#"))
            ):
                if lines[i].startswith("#") or lines[i].startswith("```"):
                    break
                block.append(lines[i])
                i += 1
            if len(block) >= 2:
                out.append("```text")
                out.extend(block)
                out.append("```")
            else:
                out.extend(block)
            continue
        out.append(lines[i])
        i += 1
    return out


def fence_dot_blocks(lines: list[str]) -> list[str]:
    out: list[str] = []
    i = 0
    while i < len(lines):
        if DOT_LEADER_RE.search(lines[i]) or (
            i + 1 < len(lines)
            and "PK-900" in lines[i]
            and DOT_LEADER_RE.search(lines[i + 1])
        ):
            block = [lines[i]]
            i += 1
            while i < len(lines) and (
                DOT_LEADER_RE.search(lines[i])
                or re.match(r"^[A-Za-z0-9+\-/# ]{1,24}\s*$", lines[i].strip())
                or lines[i].strip() == ""
            ):
                if lines[i].strip() == "" and block and block[-1] == "":
                    break
                block.append(lines[i])
                i += 1
                if i < len(lines) and not DOT_LEADER_RE.search(lines[i]) and len(block) >= 2:
                    # allow a short caption/header continuation
                    if re.match(r"^[A-Za-z].{0,40}$", lines[i].strip()) and i + 1 < len(lines) and DOT_LEADER_RE.search(lines[i + 1]):
                        block.append(lines[i])
                        i += 1
                        continue
                    break
            while block and block[-1] == "":
                block.pop()
            out.append("```text")
            out.extend(ln.rstrip() for ln in block)
            out.append("```")
            continue
        out.append(lines[i])
        i += 1
    return out


def format_prose(text: str, book_h1: bool = True, chapter_title: str | None = None) -> str:
    lines = text.splitlines()
    out: list[str] = []
    if book_h1:
        out.append(f"# {BOOK}")
        out.append("")
    if chapter_title:
        out.append(chapter_title)
        out.append("")

    i = 0
    while i < len(lines):
        line = lines[i]
        stripped = line.strip()
        if not stripped:
            if out and out[-1] != "":
                out.append("")
            i += 1
            continue

        if CHAPTER_BANNER_RE.match(stripped) or APPENDIX_BANNER_RE.match(stripped):
            i += 1
            while i < len(lines) and not lines[i].strip():
                i += 1
            if i < len(lines):
                nxt = lines[i].strip()
                if (
                    nxt
                    and len(nxt) < 80
                    and not SECTION_RE.match(nxt)
                    and not nxt.startswith("<!--")
                    and (nxt.isupper() or re.match(r"^[A-Z][A-Za-z /()\-&]+$", nxt))
                ):
                    i += 1
            continue

        # PDF artifact: leftover ".  continuation"
        if re.match(r"^\.\s+\S", stripped) and out and out[-1] and not out[-1].startswith("#"):
            out[-1] = out[-1].rstrip(".") + ". " + stripped[1:].strip()
            i += 1
            continue

        m_sec = SECTION_RE.match(stripped)
        if m_sec and looks_like_heading(m_sec.group(2)):
            hashes = "#" * heading_level(m_sec.group(1))
            out.append(f"{hashes} {m_sec.group(1)} {m_sec.group(2).strip()}")
            out.append("")
            i += 1
            continue

        m_bullet = BULLET_RE.match(stripped)
        if m_bullet:
            out.append(f"- {m_bullet.group(1).strip()}")
            i += 1
            continue

        # unwrap heavily indented body lines into paragraphs
        if line.startswith("          ") or line.startswith("               "):
            out.append(line.strip())
            i += 1
            continue

        out.append(stripped)
        i += 1

    fenced = fence_dot_blocks(out)
    # collapse excess blanks again
    final: list[str] = []
    blanks = 0
    for line in fenced:
        if line == "":
            blanks += 1
            if blanks <= 2:
                final.append("")
        else:
            blanks = 0
            final.append(line)
    return "\n".join(final).rstrip() + "\n"


PROSE_AFTER_RE = re.compile(
    r"^(is|are|was|were|sets?|controls?|enables?|allows?|the|this|when|if|use|used|your|an|a)\b",
    re.I,
)
CMD_NAME_STOPLIST = {
    "call", "the", "this", "when", "if", "use", "see", "note", "same",
}


def looks_like_cmd_name(name: str) -> bool:
    if not name or name.lower() in CMD_NAME_STOPLIST:
        return False
    if name[0].isdigit():
        return True
    if any(c.islower() for c in name):
        return True
    return name.isupper() and 2 <= len(name) <= 10


def is_command_header(block: str) -> bool:
    lines = [ln.strip() for ln in block.splitlines() if ln.strip()]
    if not lines:
        return False
    first = lines[0]
    if first.lower().startswith("parameters"):
        return False
    m = CMD_NAME_RE.match(first)
    if not m:
        return False
    name, after = m.group(1), (m.group(2) or "").strip()
    if not looks_like_cmd_name(name):
        return False
    if after and PROSE_AFTER_RE.match(after):
        return False
    if " is " in first.lower() or " are " in first.lower():
        return False
    # Label forms: "Default: OFF" or end-of-line "Immediate Command"
    label_here = bool(re.search(r"\bDefault:\s*\S", first) or re.search(r"\bDefault\s+\S", first))
    label_here = label_here or bool(re.search(r"\bImmediate Command\s*$", first))
    if not label_here and len(lines) > 1:
        nxt = lines[1]
        label_here = bool(
            re.match(r"Default:", nxt)
            or nxt.startswith("Mode:")
            or re.search(r"\bImmediate Command\s*$", nxt)
        )
    return label_here


def parse_command_header(block: str) -> dict:
    lines = [ln.strip() for ln in block.splitlines() if ln.strip()]
    first = lines[0]
    rest_header = " ".join(lines[1:4]) if len(lines) > 1 else ""
    blob = first + " " + rest_header

    m = CMD_NAME_RE.match(first)
    name = m.group(1) if m else first.split()[0]
    after = (m.group(2) if m else "").strip()

    immediate = bool(IMMEDIATE_RE.search(blob))
    default = None
    dm = DEFAULT_RE.search(blob)
    if dm:
        default = dm.group(1).strip()
        default = re.sub(r"\s+Mode:.*$", "", default).strip()

    syntax = after
    syntax = re.sub(r"\s*Default:.*$", "", syntax, flags=re.I)
    syntax = re.sub(r"\s*Immediate Command.*$", "", syntax, flags=re.I)
    syntax = syntax.strip()

    host = None
    if re.search(r"Host:\s*Not Supported", blob, re.I):
        host = "Not Supported"
    else:
        hm = HOST_RE.search(blob)
        if hm:
            host = hm.group(1)

    mode = None
    for ln in lines[:6]:
        mm = MODE_RE.match(ln)
        if mm:
            mode = mm.group(1).strip()
            mode = re.sub(r"\s+Host:?\s*.*$", "", mode).strip()
            break
    if mode is None:
        mm = re.search(r"Mode:\s*(.+?)(?:\s+Host:?\s*\S+)?\s*$", blob)
        if mm:
            mode = mm.group(1).strip()

    return {
        "name": name,
        "syntax": syntax,
        "default": default,
        "immediate": immediate,
        "mode": mode,
        "host": host,
    }


PARAM_LINE_RE = re.compile(
    r'^("?n"?|ON|OFF|text|call|call1|aaaa\[aaa\]|aaaa|"n x"|EVERY|AFTER|YES|NO|all|none)\b(.*)$',
    re.I,
)


def split_params_and_desc(body: str) -> tuple[list[str], str]:
    lines = [ln.rstrip() for ln in body.splitlines()]
    # drop leftover parameter bars
    cleaned = []
    for ln in lines:
        if PARAM_BAR_RE.match(ln.strip()) or UNDERSCORES_RE.match(ln.strip()):
            continue
        cleaned.append(ln)
    text = "\n".join(cleaned).strip()
    if not text:
        return [], ""

    raw_lines = text.splitlines()
    # drop a leading "Parameters:" label left over after underscore-bar splits
    if raw_lines:
        first = raw_lines[0].strip()
        if first.lower() in ("parameters:", "parameters"):
            raw_lines = raw_lines[1:]
        elif first.lower().startswith("parameters:"):
            rest = first.split(":", 1)[1].strip()
            raw_lines = ([rest] if rest else []) + raw_lines[1:]
    params: list[str] = []
    desc_start = 0
    # parameter section is the leading run of "token - explanation" lines
    i = 0
    while i < len(raw_lines):
        s = raw_lines[i].strip()
        if not s:
            i += 1
            continue
        if PARAM_LINE_RE.match(s) or re.match(r'^.+\s+-\s+\S', s):
            # stop if this looks like prose that happens to have a dash
            if s[0].isupper() and " is " in s.lower() and " - " not in s[:20]:
                break
            chunk = [s]
            i += 1
            while i < len(raw_lines):
                cont = raw_lines[i]
                cs = cont.strip()
                if not cs:
                    break
                if PARAM_LINE_RE.match(cs) or re.match(r'^.+\s+-\s+\S', cs):
                    break
                if cont.startswith(" ") or cont.startswith("\t") or (len(cont) - len(cont.lstrip()) >= 8):
                    chunk.append(cs)
                    i += 1
                    continue
                # wrapped continuation of the param explanation
                if not cs[0].isupper() or cs[0] in '"$' or cs.startswith("0 ") or cs[0].isdigit():
                    chunk.append(cs)
                    i += 1
                    continue
                break
            params.append(" ".join(chunk))
            continue
        break
    desc_start = i
    desc = "\n".join(raw_lines[desc_start:]).strip()
    # unwrap description into paragraphs
    desc = unwrap_paragraphs(desc)
    return params, desc


def unwrap_paragraphs(text: str) -> str:
    paras: list[str] = []
    buf: list[str] = []
    for ln in text.splitlines():
        s = ln.strip()
        if not s:
            if buf:
                paras.append(" ".join(buf))
                buf = []
            continue
        if s.startswith("o  ") or s.startswith("- ") or s.startswith("Example") or s.startswith("NOTE"):
            if buf:
                paras.append(" ".join(buf))
                buf = []
            paras.append(s)
            continue
        buf.append(s)
    if buf:
        paras.append(" ".join(buf))
    return "\n\n".join(paras)


def extract_page_from_block(block: str, fallback: int | None) -> int | None:
    ms = re.findall(r"<!-- PDF p\.(\d+) -->", block)
    if ms:
        return int(ms[-1])
    return fallback


def parse_command_pages(stitched: str) -> list[dict]:
    # keep page markers inside blocks
    parts = re.split(r"_{12,}", stitched)
    commands: list[dict] = []
    current: dict | None = None
    body_parts: list[str] = []
    last_page = None

    def flush():
        nonlocal current, body_parts
        if current is None:
            return
        body = "\n".join(body_parts)
        params, desc = split_params_and_desc(re.sub(r"<!-- PDF p\.\d+ -->", "", body))
        current["params"] = params
        current["description"] = desc
        commands.append(current)
        current = None
        body_parts = []

    for part in parts:
        page = extract_page_from_block(part, last_page)
        if page:
            last_page = page
        stripped = re.sub(r"<!-- PDF p\.\d+ -->", "\n", part).strip()
        if not stripped:
            continue
        if is_command_header(stripped):
            flush()
            current = parse_command_header(stripped)
            current["pdf_page"] = page
            body_parts = []
            # leftover lines after header (rare) go to body
            hdr_lines = [ln for ln in stripped.splitlines() if ln.strip()]
            extra = []
            skip = True
            for ln in hdr_lines:
                if skip:
                    if ln.strip().startswith("Mode:") or HOST_RE.search(ln) or DEFAULT_RE.search(ln) or IMMEDIATE_RE.search(ln) or CMD_NAME_RE.match(ln.strip()):
                        continue
                    skip = False
                extra.append(ln)
            if extra:
                body_parts.append("\n".join(extra))
        elif current is not None:
            body_parts.append(stripped)
    flush()
    # drop duplicate/prose leftovers: same name, no Host, immediately after a real entry
    cleaned_cmds: list[dict] = []
    for cmd in commands:
        if (
            cleaned_cmds
            and cmd["name"].lower() == cleaned_cmds[-1]["name"].lower()
            and not cmd.get("host")
            and cleaned_cmds[-1].get("host")
        ):
            extra = (cmd.get("description") or "").strip()
            if extra:
                prev = cleaned_cmds[-1]
                prev["description"] = ((prev.get("description") or "") + "\n\n" + extra).strip()
            continue
        cleaned_cmds.append(cmd)
    return cleaned_cmds


def command_heading(cmd: dict) -> str:
    name = cmd["name"]
    syn = cmd.get("syntax") or ""
    left = f"{name} {syn}".rstrip()
    if cmd.get("immediate") and not cmd.get("default"):
        right = "Immediate Command"
    else:
        right = f"Default: {cmd.get('default') or '?'}"
    pad = max(1, 68 - len(left))
    return f"### {left}{' ' * pad}{right}"


def format_command(cmd: dict) -> str:
    lines = [command_heading(cmd)]
    mode = cmd.get("mode") or "(not stated)"
    host = cmd.get("host")
    host_s = f"Host: {host}" if host else "Host: (not stated in extract)"
    lines.append(f"**Mode:** {mode}    {host_s}")
    if cmd.get("pdf_page"):
        lines.append(f"**Source:** (PDF p.{cmd['pdf_page']})")
    params = cmd.get("params") or []
    if params:
        lines.append("**Parameters:**")
        for p in params:
            lines.append(f"- {p}")
    desc = cmd.get("description") or ""
    if desc:
        lines.append("**Description:**")
        lines.append(desc)
    lines.append("")
    return "\n".join(lines)


def command_file_key(name: str) -> str:
    ch = name[0]
    if ch.isdigit():
        return "0-9"
    return ch.upper()


def write(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")


LEGAL_STUB = f"""# {BOOK}

## Front matter (PDF p.1–4) — stub

Source PDF: `docs/timewave-aea-pk-900-manual-nov-2004.pdf`  
Title page: Model PK-900 Data Controller Operating Manual, Timewave Technology Inc.  
Producer: Acrobat Distiller from `PK900MAN.doc` (Nov 2004 reprint of earlier 1993–1994 chapter dates).

Warranty registration, shipping/RMA, and the full FCC Part 15 reprint are **not** reproduced here. Use the PDF if you need the legal text. Operational constraints from those pages that affect installation and software:

- Use shielded cable for all connections.
- Internal modifications may increase RF interference and can void the user's authority to operate the unit.
- The unit is certified under FCC Part 15 as a Class B digital device.
- For service, Timewave asked for the serial number and the software version date shown on the first power-on screen, plus a description of attached equipment.
- **HOST Mode protocol details are not in this operating manual.** Chapter 1 only notes that HOST exists for host application programs and points to the separate Technical Manual.

Typical 1990s Timewave support contacts printed in the preface (likely obsolete): techsupport@timewave.com, service@timewave.com, (651) 489-5080, St. Paul, MN.
"""


def format_command_list_tables(text: str) -> str:
    """Turn the compact Appendix A command-list pages into Markdown tables."""
    lines = [ln.rstrip() for ln in text.splitlines()]
    out: list[str] = [f"# {BOOK}", "", "## Appendix A — Compact command list (PDF p.291–296)", ""]
    in_table = False
    for ln in lines:
        s = ln.strip()
        if s.startswith("<!-- PDF"):
            out.append(s)
            out.append("")
            continue
        if re.match(r"^COMMAND\s+DEFAULT\s+FUNCTION", s) or re.match(r"^COMMAND\s+MNEMONIC\s+FUNCTION", s):
            if in_table:
                out.append("")
            if "MNEMONIC" in s:
                out.append("| Command | Mnemonic / default | Function |")
            else:
                out.append("| Command | Default | Function |")
            out.append("|---|---|---|")
            in_table = True
            continue
        if in_table and s:
            # COMMAND   DEFAULT   FUNCTION  — first token, then two+ spaces
            m = re.match(r"^(\S+)\s{2,}(.+?)\s{2,}(.+)$", s)
            if m:
                out.append(f"| `{m.group(1)}` | {m.group(2).strip()} | {m.group(3).strip()} |")
            else:
                out.append(s)
            continue
        if s.startswith("PK-900 COMMAND LIST"):
            out.append("### PK-900 command list")
            out.append("")
            continue
        if s:
            if in_table:
                out.append("")
                in_table = False
            out.append(s)
    return "\n".join(out).rstrip() + "\n"


def format_inline_supplement_commands(text: str) -> str:
    """Light structure for Gateway/GPS command blurbs (no underscore bars)."""
    lines = text.splitlines()
    out: list[str] = []
    i = 0
    cmd_line = re.compile(
        r"^([0-9]?[A-Za-z][A-Za-z0-9]*)\b(.*(?:Default:.*|Immediate Command.*))$"
    )
    while i < len(lines):
        s = lines[i].strip()
        m = cmd_line.match(s)
        nxt = lines[i + 1].strip() if i + 1 < len(lines) else ""
        if m and nxt.startswith("Mode:"):
            name, rest = m.group(1), m.group(2).strip()
            out.append(f"### {name} {rest}".rstrip())
            hm = HOST_RE.search(nxt)
            mm = MODE_RE.match(nxt)
            mode = mm.group(1).strip() if mm else nxt
            host = f"Host: {hm.group(1)}" if hm else ""
            out.append(f"**Mode:** {mode}    {host}".rstrip())
            i += 2
            if i < len(lines) and lines[i].strip().lower().startswith("parameters"):
                out.append("**Parameters:**")
                i += 1
            continue
        out.append(lines[i])
        i += 1
    return "\n".join(out)


CHAPTERS = [
    {
        "file": "00-front-matter.md",
        "title": "Front matter (legal stub)",
        "pages": (1, 4),
        "kind": "legal",
    },
    {
        "file": "00-table-of-contents.md",
        "title": "Table of contents",
        "pages": (5, 14),
        "kind": "prose",
        "heading": "## Table of contents (PDF p.5–14)",
    },
    {
        "file": "01-introduction.md",
        "title": "Chapter 1 — Introduction",
        "pages": (15, 18),
        "kind": "prose",
        "heading": "## Chapter 1 — Introduction (PDF p.15–18)",
    },
    {
        "file": "02-computer-installation.md",
        "title": "Chapter 2 — Computer installation",
        "pages": (19, 30),
        "kind": "prose",
        "heading": "## Chapter 2 — Computer Installation (PDF p.19–30)",
    },
    {
        "file": "03-radio-installation.md",
        "title": "Chapter 3 — Radio installation",
        "pages": (31, 42),
        "kind": "prose",
        "heading": "## Chapter 3 — Radio Installation (PDF p.31–42)",
    },
    {
        "file": "04-packet-radio.md",
        "title": "Chapter 4 — Packet radio",
        "pages": (43, 86),
        "kind": "prose",
        "heading": "## Chapter 4 — Packet Radio (PDF p.43–86)",
    },
    {
        "file": "05-maildrop.md",
        "title": "Chapter 5 — MailDrop / PakMail",
        "pages": (87, 98),
        "kind": "prose",
        "heading": "## Chapter 5 — MailDrop Operation (PDF p.87–98)",
    },
    {
        "file": "06-baudot-ascii-rtty.md",
        "title": "Chapter 6 — Baudot and ASCII RTTY",
        "pages": (99, 114),
        "kind": "prose",
        "heading": "## Chapter 6 — Baudot and ASCII RTTY Operation (PDF p.99–114)",
    },
    {
        "file": "07-amtor-navtex.md",
        "title": "Chapter 7 — AMTOR / SITOR / NAVTEX",
        "pages": (115, 136),
        "kind": "prose",
        "heading": "## Chapter 7 — AMTOR and NAVTEX Operation (PDF p.115–136)",
    },
    {
        "file": "08-morse.md",
        "title": "Chapter 8 — Morse",
        "pages": (137, 142),
        "kind": "prose",
        "heading": "## Chapter 8 — Morse Operation (PDF p.137–142)",
    },
    {
        "file": "09-facsimile-sstv.md",
        "title": "Chapter 9 — Facsimile / SSTV / HF weather FAX",
        "pages": (143, 150),
        "kind": "prose",
        "heading": "## Chapter 9 — Facsimile and SSTV Operation (PDF p.143–150)",
    },
    {
        "file": "10-siam-tdm.md",
        "title": "Chapter 10 — SIAM / TDM / bit-inverted Baudot",
        "pages": (151, 156),
        "kind": "prose",
        "heading": "## Chapter 10 — Signal Identification and TDM Operation (PDF p.151–156)",
    },
    {
        "file": "11-pactor.md",
        "title": "Chapter 11 — Pactor",
        "pages": (157, 178),
        "kind": "prose",
        "heading": "## Chapter 11 — PACTOR Operation (PDF p.157–178)",
    },
    {
        "file": "appendix-a-command-guide.md",
        "title": "Appendix A — Command summary guide (how to enter commands, messages)",
        "pages": (179, 186),
        "kind": "prose",
        "heading": "## Appendix A — Command Summary guide (PDF p.179–186)",
    },
    {
        "file": "appendix-a-command-list.md",
        "title": "Appendix A — Compact command list",
        "pages": (291, 296),
        "kind": "cmdlist",
    },
    {
        "file": "appendix-b-schematic.md",
        "title": "Appendix B — Schematic diagram (stub)",
        "pages": (297, 304),
        "kind": "figure",
        "note": "Schematic drawings. No usable text extracted. See source PDF pages 297–304.",
    },
    {
        "file": "appendix-c-parts-pictorial.md",
        "title": "Appendix C — Parts pictorial (stub)",
        "pages": (305, 308),
        "kind": "figure",
        "note": "Board parts pictorials (display board, main board logic). See source PDF pages 305–308.",
    },
    {
        "file": "appendix-d-self-test.md",
        "title": "Appendix D — Self test routine (stub)",
        "pages": (309, 312),
        "kind": "figure",
        "note": "Self-test routine pages are figure/blank in this PDF (printed D-4 on p.312). See source PDF pages 309–312.",
    },
    {
        "file": "appendix-e-radio-connections.md",
        "title": "Appendix E — Specific radio connections",
        "pages": (313, 323),
        "kind": "prose",
        "heading": "## Appendix E — Specific Radio Connections (PDF p.313–323)",
    },
    {
        "file": "appendix-f-warranty.md",
        "title": "Appendix F — Warranty (stub)",
        "pages": (324, 324),
        "kind": "legal_app",
    },
    {
        "file": "supplement-gateway.md",
        "title": "Gateway option supplement (Dec 1993)",
        "pages": (325, 352),
        "kind": "supplement",
        "heading": "## PK-900 Gateway Option Supplement (PDF p.325–352)",
    },
    {
        "file": "supplement-gps.md",
        "title": "TNC GPS upgrade addendum (Rev F, Mar 2003)",
        "pages": (353, 365),
        "kind": "supplement",
        "heading": "## TNC GPS Upgrade Addendum (PDF p.353–365)",
    },
    {
        "file": "supplement-dsp.md",
        "title": "PK-900/DSP upgrade kit",
        "pages": (366, 372),
        "kind": "supplement",
        "heading": "## PK-900/DSP Upgrade Kit (PDF p.366–372)",
    },
    {
        "file": "supplement-psk.md",
        "title": "PK-900/PSK sound-card interface upgrade",
        "pages": (373, 392),
        "kind": "supplement",
        "heading": "## PK-900/PSK Sound Card Interface Upgrade Kit A.06265 (PDF p.373–392)",
    },
]


def slice_pages(all_pages: list[tuple[int, str]], start: int, end: int) -> list[tuple[int, str]]:
    return [(n, t) for n, t in all_pages if start <= n <= end]


def write_index(commands: list[dict], files_written: list[tuple[str, int]]) -> None:
    lines = [
        f"# {BOOK} — AI index",
        "",
        "Open this file first. Converted from `docs/timewave-aea-pk-900-manual-nov-2004.pdf` (392 pages, Word-distilled, not a scan).",
        "",
        "## What the PK-900 is",
        "",
        "AEA / Timewave **PK-900** dual-port multi-mode HF/VHF data controller (TNC). It sits between a computer (RS-232 DB-25) and one or two radios (5-pin DIN) and does AX.25 packet (HF/VHF), Baudot/ASCII RTTY, AMTOR/SITOR, Morse, HF weather FAX, and Pactor. Receive-only extras: NAVTEX, TDM, bit-inverted Baudot, SIAM (signal identification). Special features called out in Chapter 1: PakMail MailDrop, KISS (Appendix A), **HOST mode for host programs (details in the separate Technical Manual — not this book)**, dual-port gateway.",
        "",
        "This operating manual is the human/`cmd:` command set, radio/computer wiring, mode procedures, and the full Appendix A dictionary (verbose name, Host two-letter mnemonic, default, parameters). It does **not** document Host-mode framing (`SOH`/`CTL`/`ETB`). For PK-232 Host framing already in this repo see `docs/PK232_HostMode_Reference.md`; treat PK-900 Host I/O as unspecified until the Technical Manual is converted.",
        "",
        "## How to use these files",
        "",
        "1. Use the topic table below to open the chapter for the mode you are coding.",
        "2. Look up a command by name or Host mnemonic in the command table (or `commands.json`).",
        "3. Read the matching `commands/commands-X.md` entry — do not invent defaults, hex, or Host codes.",
        "4. Dual-port values are written `port1/port2` (example: `TXdelay` default `30/30` meaning 300 msec on each port).",
        "5. Host two-letter mnemonics are case-sensitive (`Am` vs `AM`, `An` vs `AS`).",
        "",
        "## File map",
        "",
        "| File | Contents | PDF pages |",
        "|---|---|---|",
        "| `FORMAT-DECISION.md` | Why chapter-split Markdown + JSON catalog | — |",
        "| `INDEX.md` | This entry point | — |",
        "| `commands.json` | Machine catalog of Appendix A commands | 187–290 |",
    ]
    for spec in CHAPTERS:
        a, b = spec["pages"]
        lines.append(f"| `{spec['file']}` | {spec['title']} | {a}–{b} |")
    lines.append("| `commands/commands-0-9.md` … `commands-Z.md` | Appendix A dictionary, HostCommands layout | 187–290 |")
    lines.extend(
        [
            "",
            "## Topic lookup",
            "",
            "| Topic | Open |",
            "|---|---|",
            "| Capabilities, specs, modem characteristics, I/O, power | `01-introduction.md` |",
            "| RS-232, autobaud (110–19200), AWLEN/PARITY, loop-back | `02-computer-installation.md` |",
            "| DIN radio cable, PTT polarity, FSK, CW keying, jumpers, AFSK level | `03-radio-installation.md` |",
            "| Packet timing: TXDELAY, AUDELAY, FRACK, PACLEN, MAXFRAME, dual-port, HF packet, Packet Lite, KISS mention | `04-packet-radio.md` |",
            "| PakMail / MailDrop SYSOP and remote commands | `05-maildrop.md` |",
            "| Baudot / ASCII RTTY, RBAUD, ABAUD, USOS, DIDDLE | `06-baudot-ascii-rtty.md` |",
            "| AMTOR ARQ/FEC, SELCAL, NAVTEX, ALIST | `07-amtor-navtex.md` |",
            "| Morse / CW, MSPEED, modem 12 (750 Hz) | `08-morse.md` |",
            "| HF weather FAX / SSTV / Analog | `09-facsimile-sstv.md` |",
            "| SIAM, TDM, bit-inverted Baudot | `10-siam-tdm.md` |",
            "| Pactor ARQ/unproto, MYPTCALL, PTCONN, PTSEND, PTLIST, UCMD 0–3 | `11-pactor.md` |",
            "| How commands are entered; `cmd:` responses; error messages | `appendix-a-command-guide.md` |",
            "| Full command dictionary (name / Host / default / params) | `commands/commands-*.md` + `commands.json` |",
            "| Compact A–Z command list tables | `appendix-a-command-list.md` |",
            "| Per-radio mic/DIN wiring notes (~400 models) | `appendix-e-radio-connections.md` |",
            "| Gateway firmware option (packet↔AMTOR/Pactor node) | `supplement-gateway.md` |",
            "| GPS / APRS firmware commands | `supplement-gps.md` |",
            "| DSP daughterboard install | `supplement-dsp.md` |",
            "| PSK sound-card interface | `supplement-psk.md` |",
            "| Host-mode *framing* (not in this book) | PK-900 Technical Manual (missing here); PK-232 analog: `docs/PK232_HostMode_Reference.md` |",
            "",
            "## Command lookup (Appendix A)",
            "",
            "Host mnemonics are copied exactly from the extract. If Host is blank, the PDF block did not yield a code — do not guess.",
            "",
            "| Command | Host | Default / type | Mode | File | PDF |",
            "|---|---|---|---|---|---|",
        ]
    )
    for cmd in commands:
        key = command_file_key(cmd["name"])
        fn = f"commands/commands-{key}.md"
        host = f"`{cmd['host']}`" if cmd.get("host") else ""
        default = "Immediate" if cmd.get("immediate") and not cmd.get("default") else (cmd.get("default") or "")
        mode = cmd.get("mode") or ""
        page = cmd.get("pdf_page") or ""
        lines.append(
            f"| `{cmd['name']}` | {host} | {default} | {mode} | `{fn}` | {page} |"
        )
    lines.extend(
        [
            "",
            "## Coverage and gaps",
            "",
            "- All 392 PDF pages were extracted with `pypdf` 6.x.",
            "- Preface/warranty/FCC/shipping (p.1–4, p.324): stubbed; operational FCC notes kept.",
            "- Appendix B schematic, Appendix C parts pictorial, Appendix D self-test: figure pages with little or no text.",
            "- Tuning-indicator bar graphs and some FAX/CW illustrations do not extract; surrounding numeric settings were kept.",
            "- Appendix E connection *figures* after the radio table are mostly drawings (p.318–323 nearly empty).",
            "- DSP/PSK install drawings are figure-only; jumper and connector notes that extracted as text were kept.",
            "- Hyphenation at line wraps was rejoined when the next token was clearly a word fragment. Mid-word page breaks without a hyphen were joined when the next page started lowercase.",
            "- Compact-list aliases that are not separate dictionary entries: `K` = `CONVerse`; `PT` = `PACTor`; `CALibrat` = `CALibrate`. The compact list marks `CCITT` and `CUstom` as superseded by `CODe` / `UBit`. The compact list spelling `KILLONFWD` is the `KILONFWD` command.",
            "- Do not treat this conversion as a substitute for the PK-900 Technical Manual (Host protocol).",
            "",
        ]
    )
    write(OUT / "INDEX.md", "\n".join(lines) + "\n")


def main() -> None:
    print("Reading PDF...")
    all_pages = extract_pages()
    assert len(all_pages) == 392, len(all_pages)

    files_written: list[tuple[str, int]] = []

    # chapters / supplements
    for spec in CHAPTERS:
        start, end = spec["pages"]
        sl = slice_pages(all_pages, start, end)
        dest = OUT / spec["file"]
        kind = spec["kind"]
        if kind == "legal":
            write(dest, LEGAL_STUB)
        elif kind == "legal_app":
            write(
                dest,
                f"# {BOOK}\n\n## Appendix F — Warranty (PDF p.324) — stub\n\n"
                "Original AEA one-year limited warranty. The PDF itself marks this "
                "warranty obsolete (PK-900 no longer in production, note dated 12/17/02). "
                "Full legal reprint omitted. See the source PDF if needed.\n",
            )
        elif kind == "figure":
            write(
                dest,
                f"# {BOOK}\n\n## {spec['title']} (PDF p.{start}–{end})\n\n"
                f"{spec['note']}\n\n"
                + stitch_pages(sl)
                + "\n",
            )
        elif kind == "cmdlist":
            write(dest, format_command_list_tables(stitch_pages(sl)))
        elif kind == "supplement":
            body = format_prose(stitch_pages(sl), book_h1=True, chapter_title=spec["heading"])
            body = format_inline_supplement_commands(body)
            write(dest, body)
        else:
            write(dest, format_prose(stitch_pages(sl), book_h1=True, chapter_title=spec.get("heading")))
        files_written.append((spec["file"], dest.stat().st_size))
        print(f"  wrote {spec['file']} ({dest.stat().st_size} bytes)")

    # Appendix A command dictionary
    print("Parsing Appendix A commands...")
    cmd_pages = slice_pages(all_pages, 187, 290)
    stitched = stitch_pages(cmd_pages)
    commands = parse_command_pages(stitched)
    print(f"  parsed {len(commands)} command entries")

    grouped: dict[str, list[dict]] = defaultdict(list)
    for cmd in commands:
        grouped[command_file_key(cmd["name"])].append(cmd)

    cmd_dir = OUT / "commands"
    cmd_dir.mkdir(parents=True, exist_ok=True)
    for key in sorted(grouped, key=lambda k: (k != "0-9", k)):
        cmds = grouped[key]
        parts = [
            f"# {BOOK}",
            "",
            f"## Appendix A — Commands {key} (PDF p.187–290)",
            "",
            "Entries follow the same shape as `docs/HostCommands - Trimmed.md`. "
            "Values are copied from the PK-900 extract; nothing was invented.",
            "",
        ]
        for cmd in cmds:
            parts.append(format_command(cmd))
            parts.append("")
        dest = cmd_dir / f"commands-{key}.md"
        write(dest, "\n".join(parts).rstrip() + "\n")
        files_written.append((f"commands/{dest.name}", dest.stat().st_size))
        print(f"  wrote commands/{dest.name} ({len(cmds)} cmds, {dest.stat().st_size} bytes)")

    catalog = {
        "source": "Timewave/AEA PK-900 Data Controller Operating Manual (Nov 2004 reprint)",
        "pdf": "docs/timewave-aea-pk-900-manual-nov-2004.pdf",
        "pdf_pages": "187-290",
        "note": (
            "Populated only from extracted Appendix A text. "
            "Missing Host/default/mode fields are omitted rather than guessed. "
            "Host two-letter mnemonics are case-sensitive."
        ),
        "count": len(commands),
        "commands": [
            {
                "name": c["name"],
                "syntax": c.get("syntax") or None,
                "host": c.get("host"),
                "default": c.get("default"),
                "immediate": bool(c.get("immediate")),
                "mode": c.get("mode"),
                "params": c.get("params") or [],
                "description": c.get("description") or "",
                "pdf_page": c.get("pdf_page"),
                "file": f"commands/commands-{command_file_key(c['name'])}.md",
            }
            for c in commands
        ],
    }
    dest = OUT / "commands.json"
    dest.write_text(json.dumps(catalog, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    files_written.append(("commands.json", dest.stat().st_size))
    print(f"  wrote commands.json ({dest.stat().st_size} bytes)")

    write_index(commands, files_written)
    print(f"  wrote INDEX.md ({(OUT / 'INDEX.md').stat().st_size} bytes)")

    # coverage check
    covered = set()
    for spec in CHAPTERS:
        a, b = spec["pages"]
        covered.update(range(a, b + 1))
    covered.update(range(187, 291))
    missing = [i for i in range(1, 393) if i not in covered]
    print("missing pages:", missing)
    print("command names:", ", ".join(c["name"] for c in commands[:20]), "...")
    hosts = sum(1 for c in commands if c.get("host"))
    print(f"commands with Host mnemonic: {hosts}/{len(commands)}")


if __name__ == "__main__":
    main()
