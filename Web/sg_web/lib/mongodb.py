from pymongo import MongoClient

class MongoDbAccessor(object):
	def __init__(self, connectionString):
		self.connectionString = connectionString
		self.client = MongoClient(mongoConnectionString)
		self.db = client["stitch_galaxy"]

	def persistDesign(self, designGuid, bsonDict):
		designs = db["design"]

		query = { "guid" : designGuid }
		design = designs.find_one(query)

		if design != None:
			designId = design["_id"]
			bsonDict["_id"] = designId
			designs.save(bsonDict)
		else:
			designId = designs.insert(bsonDict)

	def getCategorySeed(self, path):
		categories = db["category"]

		query = { "path", path}
		category = categories.find_one(query)

		if category != None:
			return category["category_seed"];

		return -1

	def getSubcategoriesCount(self, path):
		categories = db["category"]

		query = { "parent_path", path}
		return categories.find(query).count()

	def persistCategory(self, bsonDict):
		categories = db["category"]

		query = { "path", path}
		category = categories.find_one(query)

		if category != None:
			categoryId = category["_id"]
			bsonDict["_id"] = categoryId
			categories.save(bsonDict)
		else:
			categoryId = categories.insert(bsonDict)
