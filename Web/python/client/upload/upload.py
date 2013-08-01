import requests
import os, os.path
import uuid
from design import Designs, Design, DesignLocalization

def logProcessingDirectory(directoryName):
	print "INFO: Processing " + directoryName + " directory"

def logDirectorySkipped(path):
	print "WARNING: " + path + " directory skipped"

def logFileSkipped(path):
	print "WARNING: " + path + " file skipped"

def postRequest(files, data):
    retries = 0
	while retries != 1:
		try:
			r = requests.post(uploadUrl, files = files, data= requestParameters, timeout = 1000)
			print "INFO: Success"
			break
		except:
			print "ERROR: POST request failed"
			retries = retries + 1

uploadUrl = "http://127.0.0.1:8051/upload"
#uploadUrl = "http://stitchgalaxy.com/upload"


designs = Designs()

bRootDir = True
#prepare data for requests
for dirName, subDirList, fileList in os.walk("./data"):
	#first level directory
	if bRootDir:
		for fileName in fileList:
			logFileSkipped(os.path.join(dirName, fileName))
		bRootDir = False
		continue

	#skip traversing into third level directories
	for subDir in subDirList:
		logDirectorySkipped(os.path.join(dirName, subDir))
	del subDirList[:]

	directoryName = os.path.basename(dirName)
	try:
		uuid.UUID(directoryName)
		logProcessingDirectory(directoryName)
	except:
		logDirectorySkipped(dirName)
		continue

	design = designs.getDesign(directoryName)

	#process designs directory
	for fileName in fileList:
		#process file
		filePath = os.path.join(dirName, fileName)

		#process main.json with design general description
		if fileName == "main.json":
			design.jsonFilePath = filePath
			continue

		mainFileParts = os.path.splitext(fileName)
		if len(mainFileParts) == 2:
			fileNameWithLanguage = mainFileParts[0]
			fileExtension = mainFileParts[1]
			fileParts = fileNameWithLanguage.split("_", 1)

			if len(fileParts) == 2:
				nameOfFile = fileParts[0]
				languageString = fileParts[1]
				designLocalization = design.getLocalization(languageString)
				#process files we know
				if nameOfFile == "design" and fileExtension == "csd":
					designLocalization.designFilePath = filePath
				elif nameOfFile == "description" and fileExtension == "html":
					designLocalization.descrpitionFilePath = filePath
				elif nameOfFile == "thumbnail" and (fileExtension == "png" or fileExtension == "jpg" or fileExtension=="jpeg"):
					designLocalization.thumbnailFilePath = filePath
				elif nameOfFile == "image" and (fileExtension == "png" or fileExtension == "jpg" or fileExtension=="jpeg"):
					designLocalization.imageFilePath = filePath
				elif nameOfFile == "design" and fileExtension == "json":
					designLocalization.jsonFilePath = filePath
				else:
					logFileSkipped(filePath)
					continue;
			else:
				logFileSkipped(filePath)
				continue;
		else:
			logFileSkipped(filePath)
			continue;

for designGuid, design in designs.dict.iteritems():

	requestParameters = dict()
    files = dict()

	requestParameters["designGuid"] = designGuid

	files["json"] = open(design.jsonFilePath, "rb")

	print "Posting " + designGuid + " design data"
	requests.post(file, requestParameters)

	for languageString, designLocalization in design.dict.iteritems():

		requestParameters["designGuid"] = designGuid
		requestParameters["language"] = languageString

		if designLocalization.designFilePath != None:
			files["design"] = open(designLocalization.designFilePath, "rb")
		if designLocalization.descrpitionFilePath != None:
			files["description"] = open(designLocalization.descrpitionFilePath, "rb")
   		if designLocalization.jsonFilePath != None:
			files["json"] = open(designLocalization.jsonFilePath, "rb")
		if designLocalization.thumbnailFilePath != None:
			files["thumbnail"] = open(designLocalization.thumbnailFilePath, "rb")
		if designLocalization.imageFilePath != None:
			files["image"] = open(designLocalization.imageFilePath, "rb")

		print "Posting " + designGuid + " design data for " + languageString + " language"
		postRequest(files, requestParameters)