class Design:

	def __init__(self):
		self.jsonFilePath = None
		self.files = dict()

class Designs:

	def __init__(self):
		self.dict = dict()

	def getDesign(self, uuid):
		if not(self.dict.has_key(uuid)):
			self.dict[uuid] = Design()
		return self.dict[uuid]