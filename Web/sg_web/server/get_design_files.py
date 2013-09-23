from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.server_config import diskStorageFolder

from sg_web.lib.storage import Storage, DiskStorage

import json

class GetDesignFiles(object):

	def __init__(self, web_response, designGuid):
		self.web_response = web_response
		self.designGuid = designGuid

	def get(self):

		storage = DiskStorage(self.designGuid, diskStorageFolder)
		files = storage.getFiles()


		for dirName, subDirList, fileList in os.walk("./data"):
			for fileName in fileList:

			#skip traversing into third level directories
			for subDir in subDirList:
				del subDirList[:]

		jsonStr = json.dumps(files, ensure_ascii = False, encoding="utf-8")

		self.web_response.setStatus(RESPONSE_STATUS.OK)
		self.web_response.setContentType(CONTENT_TYPE.APPLICATION_JSON)
		self.web_response.setResponseBody(jsonStr)