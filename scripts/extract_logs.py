import gzip
import shutil
import os
for fileName in os.listdir('logs'):
    if not fileName.endswith('.gz'):
        continue
    with gzip.open(f"{fileName}",'rb') as f_in:
        suffix = fileName.find(".gz")
        with open(f"{fileName}",'wb') as f_out:
            shutil.copyfileobj(f_in, f_out)
        os.remove(f"{fileName}")