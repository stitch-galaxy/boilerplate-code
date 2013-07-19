class DesignLocalization:

	def __init__(self):
		self.designFilePath = None
		self.descrpitionFilePath = None
		self.jsonFilePath = None

class DesignToUpload:

	def __init__(self):
		self.dict = dict()

	def getLocalization(self, localizationString):
		if not(self.dict.has_key(localizationString)):
			self.dict[localizationString] = DesignLocalization()
        return self.dict[localizationString]