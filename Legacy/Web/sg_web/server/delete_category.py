from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.server_config import mongoConnectionString
from sg_web.lib.mongodb import MongoDbAccessor

from sg_web.lib.convert import JsonBsonConverter

class DeleteCategory(object):

	def __init__(self, web_response, category):
		self.web_response = web_response
		self.category = category

	def delete(self):
		db = MongoDbAccessor(mongoConnectionString)
		db.deleteCategory(category)

		self.web_response.setStatus(RESPONSE_STATUS.OK)
		self.web_response.setContentType(CONTENT_TYPE.TEXT_PLAIN)
		self.web_response.setResponseBody("")