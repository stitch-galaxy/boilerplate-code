import requests
from sgconfig.py import siteurl

data = "json serialized params"

try:
    r = requests.get("https://github.com/timeline.json")
except UnicodeEncodeError as err:
    print err
print r.encoding
print r.text.encode('cp866', 'ignore')
