import os, os.path

class Storage(object):

	def __init__(self, designGuid):
		self.designGuid = designGuid

	def persistFile(self, file, fileName):
		raise NotImplementedError("Abstract class")

	def getFiles(self):
		raise NotImplementedError("Abstract class")

CHUNK_SIZE = 8192

class DiskStorage(Storage):

	def __init__(self, designGuid, folderToStoreData):
		super(DiskStorage, self).__init__(designGuid)
		self.folderToStoreData = folderToStoreData

	def persistFile(self, file, fileName):
		if not os.path.exists(self.folderToStoreData):
			os.makedirs(self.folderToStoreData)

		designDir = self.folderToStoreData + str(self.designGuid) + "/"
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

	def getFiles(self):
		files = list()

		rootDir = self.folderToStoreData + str(self.designGuid) + "/"

		for dirName, subDirList, fileList in os.walk(rootDir):
			for fileName in fileList:
				files.append(fileName)

			#skip traversing into third level directories
			for subDir in subDirList:
				del subDirList[:]

		return files

	def deleteFile(self, fileName):
		fileName = self.folderToStoreData + str(self.designGuid) + "/" + fileName
		os.remove(fileName)

class CDNStorage(Storage):

	def __init__(self, designGuid):
		super(DiskStorage, self).__init__(designGuid)

	def persistFile(self, file, fileName):
		raise NotImplementedError("Abstract class")

