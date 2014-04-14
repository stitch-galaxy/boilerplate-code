#!/usr/bin/env python

from wsgiref.simple_server import make_server
from cgi import parse_qs, escape

from searchexecutor import SearchExecutor

from search_config import serverName
from search_config import serverPort

from pymongo import MongoClient

class SearchOrder:
	MOST_RECENT = 0,
	TOP_SALE = 1,
	TOP_RATE = 2,
	RANDOM = 3,
	MOST_RELEVANT = 4,
	PRICE_ASC = 5,
	PRICE_DESC = 6,
	COMPLEXITY_ASC = 7,
	COMPLEXITY_DESC = 8

class Search(object):
	def __init__(self, pageIndex, resultsPerPage, searchOrder, searchText):
		self.response_body = None
  		self.status = None
		self.response_headers = None

	def performSearch(self):
		try:
			self.status = "200 OK"
			self.response_body = "Success"


			client = MongoClient(mongoConnectionString)
			db = client["stitch_galaxy"]
			designs = db["design"]

			search = designs.find().skip(pageIndex * resultsPerPage).limit(resultsPerPage)
			totalResults = search.count()

		except Exception as err:
			self.status = "500 Internal Server Error"
			self.response_body = traceback.format_exc()
  		self.response_headers = [("Content-Type", "text/plain"), ("Content-Length", str(len(self.response_body)))]

	def getResponse(self):
		return (self.status, self.response_headers, self.response_body)