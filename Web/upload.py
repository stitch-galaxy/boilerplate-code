import requests
from sgconfig import uploadUrl

files = {'design': open('design.csd', 'rb')}

r = requests.post(uploadUrl, files=files)

print r.text