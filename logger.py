import os
b = os.listdir('logs')
#time
for fileName in b:
    with open("logs/" + fileName) as inFile:
        for line in inFile:
         if "joined the game" in line:
            a = line.find(" ", 33)
            print(fileName[0:-4] + " " + line[1:9] + " " + line[33:a] + " Joined the game")
        if "left the game" in line:
            a = line.find(" ", 33)
            print(fileName[0:-4] + " " + line[1:9] + " " + line[33:a] + " Left the game")                   