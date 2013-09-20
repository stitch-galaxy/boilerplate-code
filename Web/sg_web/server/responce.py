class RESPONSE_STATUS:
	OK = "200 OK"
	NOT_IMPLEMETED = ""
	INTERNAL_SERVER_ERROR = "500 Internal Server Error"


class CONTENT_TYPE:
	TEXT_PLAIN = "text/plain"


class WebResponse(object):
	def __init__(self):
		self.status = RESPONSE_STATUS.OK
        self.contentType = CONTENT_TYPE.TEXT_PLAIN

	def setStatus(self, status):
		self.status = status

	def setContentType(self, contentType):
		self.contentType = contentType

	def setResponseBody(self, responseBody):
		self.responseBody = responseBody

	def getStatus(self):
		return status

	def getHeaders(self):
		headers = [("Content-Type", self.contentType), ("Content-Length", str(len(self.responseBody)))]
		return headers

	def getResponseBody(self):
		return self.responseBody