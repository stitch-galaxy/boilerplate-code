import traceback
import uuid

from sg_web.server.server_config import diskStorageFolder
from sg_web.server.server_config import mongoConnectionString

from sg_web.lib.convert import JsonBsonConverter
from sg_web.lib.storage import Storage, DiskStorage
from sg_web.lib.mongodb import MongoDbAccessor

class UploadDesign(object):
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

			db = MongoDbAccessor(mongoConnectionString)
			db.persistDesign(self.designGuid, bsonDict)

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)