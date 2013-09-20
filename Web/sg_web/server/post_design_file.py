from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.server_config import diskStorageFolder

from sg_web.lib.storage import Storage, DiskStorage


class PostDesignFile(object):
	def __init__(self, web_response, designGuid, fileName, file):
		self.web_response = web_response
		self.designGuid = designGuid
		self.fileName = fileName
		self.file = file


	def post(self):
		storage = DiskStorage(self.designGuid, diskStorageFolder)
		storage.persistFile(self.file, self.fileName)

		self.web_response.setStatus(RESPONSE_STATUS.OK)
		self.web_response.setContentType(CONTENT_TYPE.TEXT_PLAIN)
		self.web_response.setResponseBody("")