from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.server_config import mongoConnectionString
from sg_web.lib.mongodb import MongoDbAccessor

from sg_web.lib.convert import JsonBsonConverter


import json

class GetDesignData(object):

	def __init__(self, web_response, designGuid):
		self.web_response = web_response
		self.designGuid = designGuid

	def post(self):
		db = MongoDbAccessor(mongoConnectionString)
		bsonDict = db.getDesign(self.designGuid)

		converter = JsonBsonConverter(self.designGuid)
		jsonDict = converter.convertBsonToJson(bsonDict)

		jsonStr = json.dumps(jsonDict, ensure_ascii = False, encoding="utf-8")

		self.web_response.setStatus(RESPONSE_STATUS.OK)
		self.web_response.setContentType(CONTENT_TYPE.APPLICATION_JSON)
		self.web_response.setResponseBody(jsonStr)