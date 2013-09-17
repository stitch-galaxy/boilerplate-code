import os, os.path
import traceback
from pymongo import MongoClient
import json
import uuid
import datetime

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

def ConvertJsonToBson(designGuid, jsonDict):
	bsonDict = copy.deepcopy(jsonDict)

	#enrich with guid
	bson["guid"] = designGuid

    #convert to datetime to persist properly
	if bsonDict.has_key("releaseDate"):
		bsonDict["releaseDate"] = datetime.strptime(bsonDict["releaseDate"], "%m-%d-%Y")

    #blocked property
	if bsonDict.has_key("blocked") and bsonDict["blocked"]:
		bsonDict["blocked"] = True
	else:
		bsonDict["blocked"] = False

	if bsonDict.has_key("languages"):
		languages = bsonDict["languages"]

		for language in languages:
			#thumbnail property
			if language.has_key("hasThumbnail") and language["hasThumbnail"]:
				language["hasThumbnail"] = True
			else:
				language["hasThumbnail"] = False
            #image property
			if language.has_key("hasImage") and language["hasImage"]:
				language["hasImage"] = True
			else:
				language["hasImage"] = False
            #design property
			if language.has_key("hasDesign") and language["hasDesign"]:
				language["hasDesign"] = True
			else:
				language["hasDesign"] = False
            #html page property
			if language.has_key("hasHtmlPage") and language["hasHtmlPage"]:
				language["hasHtmlPage"] = True
			else:
				language["hasHtmlPage"] = False

	return bsonDict

class Upload:
	def __init__(self, dict, designGuid):
		self.dict = dict
		self.response_body = None
  		self.status = None
		self.response_headers = None
		self.designGuid = uuid.UUID(designGuid)

	def commit(self):
		try:
			self.status = "200 OK"
			self.response_body = "Success"

			for fileName in self.dict:
				if fileName != "design.json":
					copyFile(self.dict[fileName], fileName, self.designGuid)

			jsonStr = self.dict["design.json"].read().decode("utf-8-sig")
			jsonDict = json.loads(jsonStr)

			bsonDict = ConvertJsonToBson(self.designGuid, jsonDict)

			client = MongoClient(mongoConnectionString)
			db = client["stitch_galaxy"]
			designs = db["design"]

			design = designs.find_one({"guid", designGuid})
			if design != None:
				designId = design["_id"];
				bsonDict["_id"] = designId
				designs.save(bsonDict)
			else:
				designId = designs.insert(bsonDict)

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)