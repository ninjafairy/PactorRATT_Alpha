"""Print first ~12 lines of each OCR page for chapter mapping."""
from pathlib import Path

OCR = Path(r"c:\Users\Jadon\Documents\GitHub\PactorRATT_900-2232\docs\dsp-2232-manual\_work\ocr")
for p in sorted(OCR.glob("page-*.txt")):
    lines = p.read_text(encoding="utf-8").splitlines()
    head = " | ".join(x.strip() for x in lines[:10] if x.strip())[:220]
    print(f"{p.stem} ({len(lines):3d}L {p.stat().st_size:5d}B): {head}")
