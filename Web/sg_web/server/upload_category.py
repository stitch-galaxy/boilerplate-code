import traceback
import math

from sg_web.server.server_config import mongoConnectionString

from sg_web.lib.mongodb import MongoDbAccessor

MAX_DEPTH = 4
LEVEL_SEED = 100

class UploadCategory(object):
	def __init__(self, json):
		self.json = json
		self.response_body = None
  		self.status = None
		self.response_headers = None

	def commit(self):
		try:
			self.status = "200 OK"
			self.response_body = "Success"

			jsonStr = self.json.read().decode("utf-8-sig")
			jsonDict = json.loads(jsonStr)

			db = MongoDbAccessor(mongoConnectionString)
			#calculate categoryID
			parentPath = jsonDict["parent"]
			if parentPath == "root":
				parentCategoryId = 0
			else:
				parentCategoryId = db.getCategorySeed(parentPath)

			if parentCategoryId < 0:
				raise Exception("Can not find parent category: " + parentPath)

			currParentSubcategoriesNum = db.getSubcategoriesCount(jsonDict["parent"])
			#raise exception if we have too many childs
			if currParentSubcategoriesNum >= LEVEL_SEED -1:
				raise Exception("Subcategories count limit reached")

			depthLevel = jsonDict["path"].count("/")
			if depthLevel > MAX_DEPTH:
				raise Exception("Max depth limit reached")

			#actual formula
			category_seed = parentCategoryId + (currParentSubcategoriesNum + 1) * pow(LEVEL_SEED, (MAX_DEPTH - depthLevel))
			jsonDict["category_seed"] = category_seed

			db.persistCategory(jsonDict)

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)