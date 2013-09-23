from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO
import uuid

#configuration
from sg_web.server.server_config import serverName
from sg_web.server.server_config import serverPort

#classes
from sg_web.lib.enums import REQUEST_PATH

from sg_web.server.web_response import WebResponse, RESPONSE_STATUS, CONTENT_TYPE

from sg_web.server.post_design_data import PostDesignData
from sg_web.server.post_design_file import PostDesignFile
from sg_web.server.get_design_data import GetDesignData
from sg_web.server.get_design_files import GetDesignFiles
from sg_web.server.delete_design_file import DeleteDesignFile
from sg_web.server.get_categories import GetCategories


from sg_web.server.upload_design import UploadDesign
from sg_web.server.upload_category import UploadCategory

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

		request = PostDesignData(response, designGuid, jsonFile)
		request.post()
	#post design file
	if (script_path == REQUEST_PATH.POST_DESIGN_FILE):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))

		field = form["file"]
		fileName = field.filename
		file = field.file

		request = PostDesignFile(response, designGuid, file)
		request.post()
	#get design data
	if (script_path == REQUEST_PATH.GET_DESIGN_DATA):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))

		request = GetDesignData(response, designGuid)
		request.get()
	#get design files
	if (script_path == REQUEST_PATH.GET_DESIGN_FILES):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))

		request = GetDesignFiles(response, designGuid)
		request.get()
	#delete design file
	if (script_path == REQUEST_PATH.DELETE_DESIGN_FILE):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		designGuid = uuid.UUID(form.getvalue("designGuid"))
		fileName = form.getvalue("fileName")

		request = DeleteDesignFile(response, designGuid, fileName)
		request.delete()
	#get design categories
	if (script_path == REQUEST_PATH.GET_DESIGN_CATEGORIES):
		form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

		parentCategory = uuid.UUID(form.getvalue("parentCategory"))

		request = GetCategories(response, designGuid, parentCategory)
		request.get()
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

	start_response(response.getStatus(), response.getHeaders())

	return [response.getResponseBody()]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()