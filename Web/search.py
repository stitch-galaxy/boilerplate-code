#!/usr/bin/env python

from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import json
import mysql.connector
from mysql.connector import errorcode
import datetime

#sql = "select * from T_DESIGN_LOCALIZATION where Language = %s"
sql = """select d.DesignId, d.ReleaseDate, p.Width, p.Heigth, p.Colors, p.Name, p.Description, p.ImageSmall, p.ImageLargeName, p.DescriptionUrl, p.DownloadUrl, dc.Sales, dc.AvgRating
from T_DESIGNS d 
INNER JOIN T_DESIGN_LOCALIZATION dl on dl.DesignId = d.DesignId
INNER JOIN T_PARAMETERS p on p.ParametersId = dl.ParametersId
LEFT OUTER JOIN T_DESIGN_COUNTERS dc on d.DesignId = dc.DesignId
WHERE dl.Language = %s"""

class SearchResults:

	def __init__(self):
		self.results = []
		self.error = None
		
	def reprJSON(self):
		self.dict = dict()
		self.dict['results'] = self.results
		if self.error != None:
			self.dict['error'] = self.error
		
		return self.dict

class Design:
	
    def __init__(self):
	self.name = None
	self.description = None
	self.width = None
	self.heigth = None
	self.colors = None
	self.imageSmallUrl = None
	self.imageLargeUrl = None
	self.releaseDate = None
	self.descriptionUrl = None
	self.downloadUrl = None
	self.avgRating = None
	self.sales = None

    def reprJSON(self):
		self.dict = dict()
		if self.name != None:
			self.dict['name'] = self.name
		if self.description != None:
			self.dict['description'] = self.description
		if self.width != None:
			self.dict['width'] = self.width
		if self.heigth != None:
			self.dict['heigth'] = self.heigth
		if self.colors != None:
			self.dict['colors'] = self.colors
		if self.imageSmallUrl != None:
			self.dict['imageSmallUrl'] = self.imageSmallUrl
		if self.imageLargeUrl != None:
			self.dict['imageLargeUrl'] = self.imageLargeUrl
		if self.releaseDate != None:
			self.dict['releaseDate'] = self.releaseDate
		if self.descriptionUrl != None:
			self.dict['descriptionUrl'] = self.descriptionUrl
		if self.downloadUrl != None:
			self.dict['downloadUrl'] = self.downloadUrl
		if self.avgRating != None:
			self.dict['avgRating'] = self.avgRating
		if self.sales != None:
			self.dict['sales'] = self.sales
			
		return self.dict
		
class ComplexEncoder(json.JSONEncoder):
	def default(self, obj):
		if hasattr(obj,'reprJSON'):
			return obj.reprJSON()
		else:
			return json.JSONEncoder.default(self, obj)

def application(environ, start_response):

	# Returns a dictionary containing lists as values.
	d = parse_qs(environ['QUERY_STRING'])
	
	# Read request parameters
	language = d.get('language', [''])[0] # Returns the language value.
	region = d.get('region', [''])[0] # Returns the region value.
	
	if not language:
		language = "en"
	if not region:
		region = "us"
	
	# Always escape user input to avoid script injection
	language = escape(language)
	region = escape(region)
	
	#Make db request
	
	#Create data structures
	
	searchResults = SearchResults()
	
	try:
		cnx = mysql.connector.connect(user='root', password='root', host='127.0.0.1', database='stitchgalaxy')
	except mysql.connector.Error as err:
		if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
			searchResults.error = "MySql connection cannot be established: credentials error"
		elif err.errno == errorcode.ER_BAD_DB_ERROR:
			response_body = "MySql connection cannot be established: stitchgalaxy database does not exists"
		else:
			response_body = err
	else:
		cursor = cnx.cursor()
		query = (sql)
		cursor.execute(query, (language,))
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
		
		cursor.close()
		
		response_body = json.dumps(searchResults.reprJSON(), cls=ComplexEncoder)
		cnx.close()

	status = '200 OK'

	response_headers = [('Content-Type', 'application/json'),
						('Content-Length', str(len(response_body)))]

	start_response(status, response_headers)

	return [response_body]

httpd = make_server('localhost', 8051, application)
httpd.serve_forever()


