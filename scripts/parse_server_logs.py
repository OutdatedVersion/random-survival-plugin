import os
import json
from datetime import datetime

filesToRead = []
for file in os.scandir('logs'):
    if file.name == "latest.log":
        filesToRead.append(
            (
                file.path,
                datetime.now()
            )
        )        
    else:
        filesToRead.append(
        (
            file.path,
            datetime.strptime(file.name[0:10], '%Y-%m-%d')
        )
    )


filesToRead.sort(key=lambda fileTuple: fileTuple[1])

# use file creation date -- we can use the file information off the os.scandir (look at python "os" docs)
# to here -> https://docs.python.org/3/library/os.html#os.stat_result
# which links us here -> https://docs.python.org/3/library/os.html#os.stat_result.st_ctime

joins_and_leaves = []
for (file_path,ts) in filesToRead:
    with open(file_path) as inFile:
        for line in inFile:
            if "joined the game" in line or "left the game" in line:
                usr_name_end_index = line.find(" ", 33)
                parsed_time = datetime.strptime(line[1:9], "%H:%M:%S")
                ts_with_time = ts.replace(hour=parsed_time.hour, minute=parsed_time.minute, second=parsed_time.second, microsecond=0)
                
                action = None
                if line.count("left") > 0:
                    action = "Left"
                else:
                    action = "Joined"
                
                joins_and_leaves.append(
                    {
                        'username': line[33:usr_name_end_index],
                        'action': action,
                        'timestamp': ts_with_time.isoformat(),
                    }
                )

with open('result.json', 'w') as output:
    output.write("[]")

with open('result.json', 'w') as output:
    output.write(json.dumps(joins_and_leaves, indent=2))