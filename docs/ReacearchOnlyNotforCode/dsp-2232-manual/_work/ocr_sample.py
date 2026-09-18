"""Render and OCR a page range for TOC / quality checks."""
from __future__ import annotations

import argparse
import sys
from pathlib import Path

import pymupdf
from PIL import Image
import winocr

ROOT = Path(__file__).resolve().parent
PDF = Path(r"c:\Users\Jadon\Documents\GitHub\PactorRATT_900-2232\docs\dsp2232-manual.pdf")
PAGES = ROOT / "pages"
OCR = ROOT / "ocr"


def render_page(doc: pymupdf.Document, page_index: int, dpi: int = 250) -> Path:
    page = doc[page_index]
    zoom = dpi / 72.0
    mat = pymupdf.Matrix(zoom, zoom)
    pix = page.get_pixmap(matrix=mat, colorspace=pymupdf.csGRAY, alpha=False)
    PAGES.mkdir(parents=True, exist_ok=True)
    out = PAGES / f"page-{page_index + 1:03d}.png"
    pix.save(str(out))
    return out


def result_to_text(result) -> str:
    if result is None:
        return ""
    if isinstance(result, str):
        return result
    if isinstance(result, dict):
        if result.get("text"):
            return result["text"]
        lines = result.get("lines") or []
        parts = []
        for line in lines:
            if isinstance(line, dict) and line.get("text"):
                parts.append(line["text"])
            elif isinstance(line, str):
                parts.append(line)
        return "\n".join(parts)
    if hasattr(result, "text") and result.text:
        return result.text
    return str(result)


def ocr_image(path: Path) -> str:
    img = Image.open(path)
    result = winocr.recognize_pil_sync(img)
    return result_to_text(result)


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--start", type=int, default=1)
    ap.add_argument("--end", type=int, default=20)
    ap.add_argument("--dpi", type=int, default=250)
    args = ap.parse_args()

    doc = pymupdf.open(PDF)
    OCR.mkdir(parents=True, exist_ok=True)
    for n in range(args.start, args.end + 1):
        img_path = render_page(doc, n - 1, args.dpi)
        text = ocr_image(img_path)
        out = OCR / f"page-{n:03d}.txt"
        out.write_text(text.replace("\r\n", "\n"), encoding="utf-8")
        print(f"OCR p{n}: {len(text)} chars -> {out.name}", flush=True)
    return 0


if __name__ == "__main__":
    sys.exit(main())
