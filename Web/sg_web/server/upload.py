import os, os.path
import traceback
from pymongo import MongoClient
import json
import uuid

from sg_web.server.server_config import diskStorageFolder
from sg_web.server.server_config import mongoConnectionString

from sg_web.lib.convert import JsonBsonConverter
from sg_web.lib.storage import Storage, DiskStorage

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

			converter = JsonBsonConverter(self.designGuid)
			storage = DiskStorage(self.designGuid, diskStorageFolder)

			for fileName in self.dict:
				if fileName != "design.json":
					storage.persistFile(self.dict[fileName], fileName)

			jsonStr = self.dict["design.json"].read().decode("utf-8-sig")
			jsonDict = json.loads(jsonStr)

			bsonDict = converter.convertJsonToBson(jsonDict)

			client = MongoClient(mongoConnectionString)
			db = client["stitch_galaxy"]
			designs = db["design"]

			#hexString = '5d78ad35ea5f11e1a183705681b29c47'
 			#newLinkField = { 'guid' : uuid.UUID( hexString ) }
			#design = designs.find_one(newLinkField)
			newLinkField = { 'guid' : self.designGuid }
			design = designs.find_one(newLinkField)
			#design = designs.find_one({"guid", self.designGuid})

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