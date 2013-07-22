from search_config import updateSql
from search_config import dbConfig

import json

import mysql.connector
from mysql.connector import errorcode

class DesignLocalization:

	def __init__(self):
		self.designGuid = None
		self.language = None
		self.region = None
		self.designFileName = None
		self.descriptionFileName = None
		self.imageSmallFileName = None
		self.imageLargeFileName = None
		self.designName = None
		self.designDescription = None
		self.designWidth = None
		self.designHeight = None
		self.designColors = None
        self.error = None

	def update(self):

		try:
			cnx = mysql.connector.connect(**dbConfig)
			cursor = cnx.cursor()
			try:
				query = (updateSql)
				cursor.execute(query, (self.language, self.region))
			except mysql.connector.Error as err:
				searchResults.error = "MySql error: {}".format(err)
			else:
				cursor.close()
		except mysql.connector.Error as err:
			searchResults.error = "MySql connection error: {}".format(err)
		else:
			cnx.close()