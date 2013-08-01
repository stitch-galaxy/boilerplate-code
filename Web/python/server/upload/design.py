from search_config import updateSql
from search_config import dbConfig

import json

import mysql.connector
from mysql.connector import errorcode

class Design:
	def __init__(self):
		self.designGuid = None

		self.name = None
		self.description = None
		self.width = None
		self.height = None
		self.colors = None
        self.error = None

	def update(self):
		throw


class DesignLocalization:

	def __init__(self):
		self.designGuid = None
		self.language = None

		self.designFileName = None
		self.hasDesign = False
		self.descriptionFileName = None
		self.hasDescription = False
		self.thumbnailFileName = None
		self.hasThumbnail = False
		self.imageFileName = None
		self.hasImage = False

		self.name = None
		self.description = None
		self.width = None
		self.height = None
		self.colors = None
        self.error = None

	def update(self):

		try:
			cnx = mysql.connector.connect(**dbConfig)
			cursor = cnx.cursor()
			try:
				cursor.callproc("createOrUpdateDesignLocalization", (self.designGuid, self.language, self.name, self.description, self.width, self.height, self.colors, self.hasThumbnail, self.hasImage, self.hasDescription, self.hasDesign))
			except mysql.connector.Error as err:
				self.error = err
				searchResults.error = "MySql error: {}".format(err)
			else:
				cursor.close()
		except mysql.connector.Error as err:
			self.error = err
			searchResults.error = "MySql connection error: {}".format(err)
		else:
			cnx.close()