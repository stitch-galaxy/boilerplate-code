from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO
import os, os.path
import json
from design import DesignLocalization
from datetime import datetime

from upload_config import serverName
from upload_config import serverPort
from upload_config import folderToStoreData

CHUNK_SIZE = 8192

def copyFile(file, fileName, designGuid):
	if not os.path.exists(folderToStoreData):
		os.makedirs(folderToStoreData)

	designDir = folderToStoreData + designGuid + "/"
	if not os.path.exists(designDir):
		os.makedirs(designDir)

	with open(designDir + fileName, "w+b") as fh:
		chunk = file.read(CHUNK_SIZE)
		while True:
			if chunk:
				fh.write(chunk)
				chunk = file.read(CHUNK_SIZE)
			else:
				break

def application(environ, start_response):

	response_body = "ok"
	status = "200 OK"

	form = cgi.FieldStorage(fp=cStringIO.StringIO(environ['wsgi.input'].read(int(environ['CONTENT_LENGTH']))), environ=environ)


	if form.has_key("language"):
		designLocalization = DesignLocalization()

		designLocalization.designGuid = form.getvalue("designGuid")
		designLocalization.language = form.getvalue("language")

		if form.has_key("design"):
			field = form["design"]
		 	designLocalization.designFileName = field.filename
			designLocalization.hasDesign = True
			file = field.file
			copyFile(file, designLocalization.designFileName, designLocalization.designGuid)

		if form.has_key("description"):
			field = form["description"]
		 	designLocalization.descriptionFileName = field.filename
			designLocalization.hasDescription = True
			file = field.file
			copyFile(file, designLocalization.descriptionFileName, designLocalization.designGuid)

		if form.has_key("thumbnail"):
			field = form["thumbnail"]
		 	designLocalization.thumbnailFileName = field.filename
			designLocalization.hasThumbnail = True
			file = field.file
			copyFile(file, designLocalization.thumbnailFileName, designLocalization.designGuid)

		if form.has_key("image"):
			field = form["image"]
		 	designLocalization.imageFileName = field.filename
			designLocalization.hasImage = True
			file = field.file
			copyFile(file, designLocalization.imageFileName, designLocalization.designGuid)

		if form.has_key("json"):
			field = form["json"]
			input = cStringIO.StringIO(field.file.read())
			jsonAsString = input.getvalue()
			jsonDict = json.loads(jsonAsString)
			if jsonDict.has_key("name"):
				designLocalization.name = jsonDict["name"]
			if jsonDict.has_key("description"):
				designLocalization.description = jsonDict["description"]
			if jsonDict.has_key("width"):
				designLocalization.width = jsonDict["width"]
			if jsonDict.has_key("height"):
				designLocalization.height = jsonDict["height"]
			if jsonDict.has_key("colors"):
				designLocalization.colors = jsonDict["colors"]

		designLocalization.update()
	else:
		design = Design()
		design.designGuid = form.getvalue("designGuid")

		if form.has_key("json"):
			field = form["json"]
			input = cStringIO.StringIO(field.file.read())
			jsonAsString = input.getvalue()
			jsonDict = json.loads(jsonAsString)

		if jsonDict.has_key("releaseDate"):
				design.releaseDate = datetime.strptime(jsonDict["releaseDate"], "%d-%m-%Y")
		if jsonDict.has_key("sales"):
				design.sales = jsonDict["sales"]
		if jsonDict.has_key("totalRating"):
				design.totalRating = jsonDict["totalRating"]
		if jsonDict.has_key("totalRates"):
				design.totalRates = jsonDict["totalRates"]
		if jsonDict.has_key("blocked"):
				design.blocked = jsonDict["blocked"]

	response_headers = [("Content-Type", "text/plain"),
						("Content-Length", str(len(response_body)))]

	start_response(status, response_headers)

	return [response_body]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()