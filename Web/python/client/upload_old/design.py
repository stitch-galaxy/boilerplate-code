class DesignLocalization:

	def __init__(self):
		self.designFilePath = None
		self.descrpitionFilePath = None
		self.jsonFilePath = None
		self.thumbnailFilePath = None
		self.imageFilePath = None
		self.jsonFilePath = None

class Design:

	def __init__(self):
		self.jsonFilePath = None
		self.dict = dict()

	def getLocalization(self, languageString):
		if not(self.dict.has_key(languageString)):
			self.dict[languageString] = DesignLocalization()
		return self.dict[languageString]

class Designs:

	def __init__(self):
		self.dict = dict()

	def getDesign(self, uuid):
		if not(self.dict.has_key(uuid)):
			self.dict[uuid] = Design()
		return self.dict[uuid]