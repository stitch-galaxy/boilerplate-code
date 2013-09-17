from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO

from upload import Upload

from server_config import serverName
from server_config import serverPort

def application(environ, start_response):

	script_path = environ["PATH_INFO"]

	if (script_path == "/upload"):

		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)
		designGuid = form.getvalue("designGuid")

		filesDict = dict()
		for key in form.keys():
			if key != "designGuid":
				field = form[key];
				fileName = field.filename
				file = field.file
				filesDict[fileName] = file

		upload = Upload(filesDict, designGuid)
		upload.commit()
		response = upload.getResponse()

	else:
		status = "501 Not Implemented"
		response_body = "Not implemeted"
		response_headers = [("Content-Type", "text/plain"),
						("Content-Length", str(len(response_body)))]
		response = (status, response_headers, response_body)

	start_response(response[0], response[1])

	return [response[2]]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()