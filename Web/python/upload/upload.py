import requests

uploadUrl = "http://127.0.0.1:8051/upload"
#uploadUrl = "http://stitchgalaxy.com/upload"

files = {'design': open("data/design.csd", 'rb')}

r = requests.post(uploadUrl, files=files)

print r.text