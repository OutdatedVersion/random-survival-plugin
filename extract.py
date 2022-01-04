import gzip
import shutil
import os
for fileName in os.listdir('logs'):
    if not fileName.endswith('.gz'):
        continue
    with gzip.open("logs/" + fileName, 'rb') as f_in:
        suffix = fileName.find(".gz")
        with open("logs/" + fileName[0:suffix], 'wb') as f_out:
            shutil.copyfileobj(f_in, f_out)
        os.remove("logs/" + fileName)