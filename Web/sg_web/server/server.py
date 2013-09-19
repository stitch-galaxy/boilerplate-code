from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO

#configuration
from sg_web.server.server_config import serverName
from sg_web.server.server_config import serverPort

#classes
from sg_web.server.upload_design import UploadDesign
from sg_web.server.upload_category import UploadCategory

def application(environ, start_response):

	script_path = environ["PATH_INFO"]

	if (script_path == "/uploadDesign"):

		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)
		designGuid = form.getvalue("designGuid")

		filesDict = dict()
		for key in form.keys():
			if key != "designGuid":
				field = form[key];
				fileName = field.filename
				file = field.file
				filesDict[fileName] = file

		upload = UploadDesign(filesDict, designGuid)
		upload.commit()
		response = upload.getResponse()
	elif (script_path == "/uploadCategory"):

		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		field = form["add.json"]
		file = field.file

		upload = UploadCategory(file)
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