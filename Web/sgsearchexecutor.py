from sgconfig import searchsql
from sgconfig import dbconfig

from sgsearchresults import SearchResults
from sgsearchresults import Design

import json

import mysql.connector
from mysql.connector import errorcode

class SearchExecutor:

	def __init__(self):
		self.language = "en"
		self.region = "us"
		
	def performSearch(self):
		searchResults = SearchResults()
						
		try:
			cnx = mysql.connector.connect(**dbconfig)
			cursor = cnx.cursor()
			try:
				query = (searchsql)
				cursor.execute(query, (self.language,))
				for (DesignId, ReleaseDate, Width, Height, Colors, Name, Description, ImageSmall, ImageLargeName, DescriptionUrl, DownloadUrl, Sales, AvgRating) in cursor:
					design = Design()
					design.name = Name
					design.description = Description
					design.width = Width
					design.heigth = Height
					design.colors = Colors
					design.imageSmallUrl = ImageSmall
					design.imageLargeUrl = ImageLargeName
					design.releaseDate = ReleaseDate
					design.descriptionUrl = DescriptionUrl
					design.downloadUrl = DownloadUrl
					design.avgRating = AvgRating
					design.sales = Sales
					searchResults.results.append(design)
			except mysql.connector.Error as err:
				searchResults.error = "MySql error: {}".format(err)
			else:
				cursor.close()
		except mysql.connector.Error as err:
			searchResults.error = "MySql connection error: {}".format(err)
		else:
			cnx.close()
		return json.dumps(searchResults.reprJSON())