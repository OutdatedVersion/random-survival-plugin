import gzip
import shutil
import os
for fileName in os.listdir('logs'):
  with gzip.open("logs/" + fileName, 'rb') as f_in:
     a = fileName.find(".gz") 
     with open("logs/" + fileName[0:a], 'wb') as f_out:
         shutil.copyfileobj(f_in, f_out)
  os.remove("logs/" + fileName)