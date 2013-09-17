import datetime

class JsonBsonConverter(object):

	def __init__(self, designGuid):
		self.designGuid= designGuid

	def convertJsonToBson(jsonDict):
		bsonDict = copy.deepcopy(jsonDict)

		#enrich with guid
		bson["guid"] = self.designGuid

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