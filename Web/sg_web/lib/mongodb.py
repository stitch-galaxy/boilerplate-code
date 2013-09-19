from pymongo import MongoClient

class MongoDbAccessor(object):
	def __init__(self, connectionString):
		self.connectionString = connectionString
		self.client = MongoClient(mongoConnectionString)
		self.db = client["stitch_galaxy"]

	def persistDesign(self, designGuid, bsonDict):
		designs = db["design"]

		newLinkField = { 'guid' : designGuid }
		design = designs.find_one(newLinkField)

		if design != None:
			designId = design["_id"];
			bsonDict["_id"] = designId
			designs.save(bsonDict)
		else:
			designId = designs.insert(bsonDict)
