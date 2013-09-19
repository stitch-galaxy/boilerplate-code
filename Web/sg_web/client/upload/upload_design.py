import os, os.path
import uuid

#configuration
from sg_web.client.upload.upload_config import serverUrl

#clasess
from sg_web.client.upload.design import Designs, Design
from sg_web.client.upload.common import postRequest

#functions
def logProcessingDirectory(directoryName):
	print "INFO: Processing " + directoryName + " directory"

def logDirectorySkipped(path):
	print "WARNING: " + path + " directory skipped"

def logFileSkipped(path):
	print "WARNING: " + path + " file skipped"

#script code

uploadUrl = serverUrl + "/uploadDesign"

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
		if fileName == "design.json":
			design.files[fileName] = filePath
			continue

		mainFileParts = os.path.splitext(fileName)
		if len(mainFileParts) == 2:
			fileNameWithLanguage = mainFileParts[0]
			fileExtension = mainFileParts[1]
			fileParts = fileNameWithLanguage.split("_", 1)

			if len(fileParts) == 2:
				nameOfFile = fileParts[0]
				languageString = fileParts[1]

				#process files we know
				if (nameOfFile == "design" and fileExtension == ".csd") \
				or (nameOfFile == "description" and fileExtension == ".html") \
				or (nameOfFile == "thumbnail" and (fileExtension == ".png" or fileExtension == ".jpg" or fileExtension==".jpeg")) \
				or (nameOfFile == "image" and (fileExtension == ".png" or fileExtension == ".jpg" or fileExtension==".jpeg")) \
				or (nameOfFile == "design" and fileExtension == ".json"):
					design.files[fileName] = filePath
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

	for fileName in design.files:
		files[fileName] = open(design.files[fileName], "rb")

	print "Posting " + designGuid + " design data"
	postRequest(files, requestParameters)