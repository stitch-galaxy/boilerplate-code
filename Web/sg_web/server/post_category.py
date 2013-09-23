from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.server_config import mongoConnectionString
from sg_web.lib.mongodb import MongoDbAccessor

from sg_web.lib.convert import JsonBsonConverter

class PostCategory(object):

	def __init__(self, web_response, jsonFile):
		self.web_response = web_response
		self.jsonFile = jsonFile

	def post(self):

		jsonStr = self.jsonFile.read().decode("utf-8-sig")
		jsonDict = json.loads(jsonStr)

		db = MongoDbAccessor(mongoConnectionString)
		db.persistCategory(jsonDict)

		self.web_response.setStatus(RESPONSE_STATUS.OK)
		self.web_response.setContentType(CONTENT_TYPE.TEXT_PLAIN)
		self.web_response.setResponseBody("")
