from datetime import datetime
import copy
import random

class JsonBsonConverter(object):

	def __init__(self, designGuid):
		self.designGuid= designGuid

	def convertJsonToBson(self, jsonDict):
		bsonDict = copy.deepcopy(jsonDict)

		#enrich with guid
		bsonDict["guid"] = self.designGuid

	    #convert to datetime to persist properly
		if bsonDict.has_key("release_date"):
			bsonDict["release_date"] = datetime.strptime(bsonDict["release_date"], "%d-%m-%Y")

		#compute rating
		if bsonDict.has_key("total_rating"):
			bsonDict["total_rating"] = 0
		if bsonDict.has_key("total_rates"):
			bsonDict["total_rates"] = 0
		if bsonDict["total_rates"] != 0:
			bsonDict["rating"] = float(["total_rating"]) / bsonDict["total_rates"]
		else:
			bsonDict["rating"] = 0.0

		#introduce random field
		bsonDict["random"] = random.random()

	    #blocked property
		if bsonDict.has_key("blocked") and bsonDict["blocked"]:
			bsonDict["blocked"] = True
		else:
			bsonDict["blocked"] = False

		if bsonDict.has_key("languages"):
			languages = bsonDict["languages"]

			for language in languages:
				#thumbnail property
				if language.has_key("has_thumbnail") and language["has_thumbnail"]:
					language["has_thumbnail"] = True
				else:
					language["has_thumbnail"] = False
	            #image property
				if language.has_key("has_image") and language["has_image"]:
					language["has_image"] = True
				else:
					language["has_image"] = False
	            #design property
				if language.has_key("has_design") and language["has_design"]:
					language["has_design"] = True
				else:
					language["has_design"] = False
	            #html page property
				if language.has_key("has_html_page") and language["has_html_page"]:
					language["has_html_page"] = True
				else:
					language["has_html_page"] = False

		return bsonDict