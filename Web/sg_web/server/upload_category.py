import traceback

from sg_web.server.server_config import mongoConnectionString

from sg_web.lib.mongodb import MongoDbAccessor

class UploadCategory(object):
	def __init__(self):
		self.response_body = None
  		self.status = None
		self.response_headers = None

	def addCategory(self, json):
		return

	def loadSubcategories(self, parentCategory):
		try:
			self.status = "200 OK"
			self.response_body = "Success"

			db = MongoDbAccessor(mongoConnectionString)
			db.loadCategories(parentCategory)

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)