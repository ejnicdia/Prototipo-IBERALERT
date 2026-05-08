import PyPDF2
import re

def extract_text(pdf_path):
    text = ""
    with open(pdf_path, 'rb') as file:
        reader = PyPDF2.PdfReader(file)
        for page in reader.pages:
            text += page.extract_text() + "\n"
    return text

text = extract_text('3_DSI_Grupo_07.pdf')

# Find context around "informe" or "tipos"
matches = re.finditer(r'.{0,100}informe.{0,100}', text, re.IGNORECASE)
for i, match in enumerate(matches):
    if i > 50: break
    print(f"Match {i}: {match.group(0).strip()}")
