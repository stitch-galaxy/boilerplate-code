from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO

#configuration
from sg_web.server.server_config import serverName
from sg_web.server.server_config import serverPort

#classes
from sg_web.lib.enums import REQUEST_PATH

from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.post_design_data import PostDesignData


from sg_web.server.upload_design import UploadDesign
from sg_web.server.upload_category import UploadCategory

#constants
POST_DESIGN_DATA = "/post_design_data"

def application(environ, start_response):

	response = WebResponse()

	script_path = environ["PATH_INFO"]

    #post design data
	if (script_path == REQUEST_PATH.POST_DESIGN_DATA):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))

		field = form["json"]
		fileName = field.filename
		jsonFile = field.file

		post = PostDesignData(designGuid, jsonFile)
		post.post()
	#post design file
	if (script_path == REQUEST_PATH.POST_DESIGN_FILE):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))

		field = form["file"]
		fileName = field.filename
		file = field.file

		post = PostDesignData(designGuid, jsonFile)



	elif (script_path == "/uploadRequest"):
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