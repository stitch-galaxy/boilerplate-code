import requests
import os, os.path
import uuid
from design import Designs, Design, DesignLocalization

def logPreparingDirectoryData(directoryName):
	print "INFO: Preparing " + directoryName + " directory data"
def logDirectorySkipped(directoryName):
	print "WARNING: " + directoryName + " directory skipped"

def logFileSkipped(fileName):
	print "WARNING: " + fileName + " skipped"

uploadUrl = "http://127.0.0.1:8051/upload"
#uploadUrl = "http://stitchgalaxy.com/upload"


designs = Designs()

#prepare data for requests
for dataroot, designDirs, _ in os.walk("./data"):
	for directoryName in designDirs:
			designDirPath = os.path.join(dataroot, directoryName)
			if os.path.isdir(designDirPath):
				#design dir found
				try:
					uuid.UUID(directoryName)
					logPreparingDirectoryData(directoryName)
				except:
					logDirectorySkipped(directoryName)
					continue
				design = designs.getDesign(directoryName)

				for filesroot, _, files in os.walk(designDirPath):
					for fileName in files:
						filePath = os.path.join(filesroot, fileName)
						if os.path.isdir(filePath):
							continue
						#form data for post request
						mainFileParts = fileName.split(".")
						if len(mainFileParts) == 2:
							fileNameWithLanguageAndLocation = mainFileParts[0]
							fileExtension = mainFileParts[1]
							fileParts = fileNameWithLanguageAndLocation.split("_", 1)

							if len(fileParts) == 2:
								nameOfFile = fileParts[0]
								localizationString = fileParts[1]
								designLocalization = design.getLocalization(localizationString)
								#process files we know
								if nameOfFile == "design" and fileExtension == "csd":
									designLocalization.designFilePath = filePath
								elif nameOfFile == "description" and fileExtension == "html":
									designLocalization.descrpitionFilePath = filePath
								elif nameOfFile == "design" and fileExtension == "json":
									designLocalization.jsonFilePath = filePath
								else:
									logFileSkipped(fileName)
									continue;
							else:
								logFileSkipped(fileName)
								continue;
						else:
							logFileSkipped(fileName)
							continue;

for designGuid, design in designs.dict.iteritems():
	for localizationString, designLocalization in design.dict.iteritems():
		files = dict()
		if designLocalization.designFilePath != None:
			files["design"] = open(designLocalization.designFilePath, "rb")
		if designLocalization.descrpitionFilePath != None:
			files["description"] = open(designLocalization.descrpitionFilePath, "rb")
   		if designLocalization.jsonFilePath != None:
			files["json"] = open(designLocalization.jsonFilePath, "rb")
		print "Posting " + designGuid + " design data for " + localizationString + " localization"
		retries = 0
		while retries != 3:
			try:
				r = requests.post(uploadUrl, files = files)
			except:
				print "POST request failed"
				retries = retries + 1
