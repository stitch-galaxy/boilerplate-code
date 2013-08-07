from upload_config import dbConfig

import json

import mysql.connector
from mysql.connector import errorcode

class Design:
	def __init__(self):
		self.designGuid = None
		self.releaseDate = None
		self.sales = None
		self.totalRating = None
		self.totalRates = None
		self.blocked = None
		self.error = None

	def update(self):
		try:
			cnx = mysql.connector.connect(**dbConfig)
			cursor = cnx.cursor()
			try:
				cursor.callproc("createOrUpdateDesignInformation", (self.designGuid, self.releaseDate, self.sales, self.totalRating, self.totalRates, self.blocked))
				cnx.commit()
			except Exception as err:
				self.error = err
			else:
				cursor.close()
		except Exception as err:
			self.error = err
		else:
			cnx.close()


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
				cnx.commit()
			except Exception as err:
				self.error = err
			else:
				cursor.close()
		except Exception as err:
			self.error = err
		else:
			cnx.close()