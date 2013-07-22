class DesignLocalization:

	def __init__(self):
		self.designFilePath = None
		self.descrpitionFilePath = None
		self.jsonFilePath = None
		self.imageSmallFilePath = None
		self.imageLargeFilePath = None
		self.jsonFilePath = None

class Design:

	def __init__(self):
		self.dict = dict()

	def getLocalization(self, localizationString):
		if not(self.dict.has_key(localizationString)):
			self.dict[localizationString] = DesignLocalization()
		return self.dict[localizationString]

class Designs:

	def __init__(self):
		self.dict = dict()

	def getDesign(self, uuid):
		if not(self.dict.has_key(uuid)):
			self.dict[uuid] = Design()
		return self.dict[uuid]