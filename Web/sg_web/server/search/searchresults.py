class SearchResults:

	def __init__(self):
		self.results = []
		self.error = None
		
	def reprJSON(self):
		self.dict = dict()
		self.dict["results"] = self.results
		if self.error != None:
			self.dict["error"] = self.error
		
		return self.dict

class Design:
	
    def __init__(self):
		self.name = None
		self.description = None
		self.width = None
		self.heigth = None
		self.colors = None
		self.imageSmallUrl = None
		self.imageLargeUrl = None
		self.releaseDate = None
		self.descriptionUrl = None
		self.downloadUrl = None
		self.avgRating = None
		self.sales = None

    def reprJSON(self):
		self.dict = dict()
		if self.name != None:
			self.dict["name"] = self.name
		if self.description != None:
			self.dict["description"] = self.description
		if self.width != None:
			self.dict["width"] = self.width
		if self.heigth != None:
			self.dict["heigth"] = self.heigth
		if self.colors != None:
			self.dict["colors"] = self.colors
		if self.imageSmallUrl != None:
			self.dict["imageSmallUrl"] = self.imageSmallUrl
		if self.imageLargeUrl != None:
			self.dict["imageLargeUrl"] = self.imageLargeUrl
		if self.releaseDate != None:
			self.dict["releaseDate"] = self.releaseDate
		if self.descriptionUrl != None:
			self.dict["descriptionUrl"] = self.descriptionUrl
		if self.downloadUrl != None:
			self.dict["downloadUrl"] = self.downloadUrl
		if self.avgRating != None:
			self.dict["avgRating"] = self.avgRating
		if self.sales != None:
			self.dict["sales"] = self.sales
			
		return self.dict