from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO
import os, os.path
import json

from upload_config import serverName
from upload_config import serverPort
from upload_config import folderToStoreData

CHUNKSIZE = 8192

def copyFile(file, fileName, designGuid):
	if not os.path.exists(folderToStoreData):
		os.makedirs(folderToStoreData)

	designDir = folderToStoreData + designGuid + "/"
	if not os.path.exists(designDir):
		os.makedirs(designDir)

	with open(designDir + fileName, "w+b") as fh:
		chunk = file.read(CHUNKSIZE)
		while True:
			if chunk:
				fh.write(chunk)
				chunk = file.read(CHUNKSIZE)
			else:
				break

def application(environ, start_response):

	response_body = "ok"
	status = "200 OK"

	form = cgi.FieldStorage(fp=cStringIO.StringIO(environ['wsgi.input'].read(int(environ['CONTENT_LENGTH']))), environ=environ)


	designGuid = form.getvalue("designGuid")

	localizationString = form.getvalue("localization")

	locList = localizationString.split("_")
	if len(locList) == 2:
		language = locList[0]
		locale = locList[1]
	else:
		language = locList[0]
		locale = None

	designFileName = None
	descriptionFileName = None
	imageSmallFileName = None
	imageLargeFileName = None
	designName = None
	designDescription = None
	designWidth = None
	designHeight = None
	designColors = None

	if form.has_key("design"):
		field = form["design"]
	 	designFileName = field.filename
		file = field.file
		copyFile(file, designFileName, designGuid)

	if form.has_key("description"):
		field = form["description"]
	 	descriptionFileName = field.filename
		file = field.file
		copyFile(file, descriptionFileName, designGuid)

	if form.has_key("imageSmall"):
		field = form["imageSmall"]
	 	imageSmallFileName = field.filename
		file = field.file
		copyFile(file, imageSmallFileName, designGuid)

	if form.has_key("imageLarge"):
		field = form["imageLarge"]
	 	imageLargeFileName = field.filename
		file = field.file
		copyFile(file, imageLargeFileName, designGuid)

	if form.has_key("json"):
		field = form["json"]
		input = cStringIO.StringIO(field.file.read())
		jsonAsString = input.getvalue()
		jsonDict = json.loads(jsonAsString)
		if jsonDict.has_key("name"):
			designName = jsonDict["name"]
		if jsonDict.has_key("description"):
			designDescription = jsonDict["description"]
		if jsonDict.has_key("width"):
			designWidth = jsonDict["width"]
		if jsonDict.has_key("height"):
			designHeight = jsonDict["height"]
		if jsonDict.has_key("colors"):
			designColors = jsonDict["colors"]

	response_headers = [("Content-Type", "text/plain"),
						("Content-Length", str(len(response_body)))]

	start_response(status, response_headers)

	return [response_body]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()