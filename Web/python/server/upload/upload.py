import os, os.path
import traceback
from pymongo import MongoClient
import cStringIO
import json

from upload_config import folderToStoreData
from upload_config import mongoConnectionString

CHUNK_SIZE = 8192

def copyFile(file, fileName, designGuid):
	if not os.path.exists(folderToStoreData):
		os.makedirs(folderToStoreData)

	designDir = folderToStoreData + designGuid + "/"
	if not os.path.exists(designDir):
		os.makedirs(designDir)

	with open(designDir + fileName, "w+b") as fh:
		chunk = file.read(CHUNK_SIZE)
		while True:
			if chunk:
				fh.write(chunk)
				chunk = file.read(CHUNK_SIZE)
			else:
				break

class Upload:
	def __init__(self, dict, designGuid):
		self.dict = dict
		self.response_body = None
  		self.status = None
		self.response_headers = None
		self.designGuid = designGuid

	def commit(self):
		try:
			self.status = "200 OK"
			self.response_body = "Success"

			for fileName in self.dict:
				if fileName != "design.json":
					copyFile(self.dict[fileName], fileName, self.designGuid)

			jsonStr = self.dict["design.json"].read().decode("utf-8-sig")
			jsonDict = json.loads(jsonStr)

			client = MongoClient(mongoConnectionString)
			db = client["stitch_galaxy"]
			designs = db["design"]
			designId = designs.insert(jsonDict)
			a = 'a'

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)