"""Render + Windows OCR every PDF page, preserving line breaks."""
from __future__ import annotations

import json
import sys
from pathlib import Path

import pymupdf
from PIL import Image
import winocr

ROOT = Path(__file__).resolve().parent
PDF = Path(r"c:\Users\Jadon\Documents\GitHub\PactorRATT_900-2232\docs\dsp2232-manual.pdf")
PAGES = ROOT / "pages"
OCR = ROOT / "ocr"
META = ROOT / "meta"


def lines_to_text(result: dict) -> str:
    lines = result.get("lines") or []
    parts = []
    for line in lines:
        if isinstance(line, dict) and line.get("text"):
            parts.append(line["text"].strip())
        elif isinstance(line, str) and line.strip():
            parts.append(line.strip())
    return "\n".join(parts)


def compact_lines(result: dict) -> list[dict]:
    out = []
    for line in result.get("lines") or []:
        if not isinstance(line, dict):
            continue
        words = line.get("words") or []
        xs = []
        ys = []
        for w in words:
            br = (w or {}).get("bounding_rect") or {}
            if "x" in br:
                xs.append(float(br["x"]))
                ys.append(float(br.get("y", 0)))
        out.append(
            {
                "t": line.get("text", ""),
                "x": min(xs) if xs else None,
                "y": min(ys) if ys else None,
            }
        )
    return out


def ocr_image(path: Path) -> dict:
    img = Image.open(path)
    return winocr.recognize_pil_sync(img)


def render_page(doc: pymupdf.Document, page_index: int, dpi: int = 250) -> Path:
    page = doc[page_index]
    zoom = dpi / 72.0
    mat = pymupdf.Matrix(zoom, zoom)
    pix = page.get_pixmap(matrix=mat, colorspace=pymupdf.csGRAY, alpha=False)
    PAGES.mkdir(parents=True, exist_ok=True)
    out = PAGES / f"page-{page_index + 1:03d}.png"
    pix.save(str(out))
    return out


def main() -> int:
    start = int(sys.argv[1]) if len(sys.argv) > 1 else 1
    end = int(sys.argv[2]) if len(sys.argv) > 2 else 176
    dpi = int(sys.argv[3]) if len(sys.argv) > 3 else 250
    force = "--force" in sys.argv
    delete_images = "--keep-images" not in sys.argv

    doc = pymupdf.open(PDF)
    end = min(end, doc.page_count)
    OCR.mkdir(parents=True, exist_ok=True)
    META.mkdir(parents=True, exist_ok=True)
    for n in range(start, end + 1):
        out = OCR / f"page-{n:03d}.txt"
        meta = META / f"page-{n:03d}.json"
        if (not force) and out.exists() and meta.exists() and out.stat().st_size > 0:
            print(f"skip p{n}", flush=True)
            continue
        img_path = render_page(doc, n - 1, dpi)
        result = ocr_image(img_path)
        text = lines_to_text(result)
        out.write_text(text.replace("\r\n", "\n") + "\n", encoding="utf-8")
        meta.write_text(
            json.dumps({"page": n, "lines": compact_lines(result)}, ensure_ascii=False),
            encoding="utf-8",
        )
        print(f"OCR p{n}: {len(text)} chars / {text.count(chr(10))+1} lines", flush=True)
        if delete_images:
            try:
                img_path.unlink()
            except OSError:
                pass
    return 0


if __name__ == "__main__":
    sys.exit(main())
