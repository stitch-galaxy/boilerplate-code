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
from sg_web.server.post_category import PostCategory
from sg_web.server.delete_category import DeleteCategory

def application(environ, start_response):

	response = WebResponse()

	script_path = environ["PATH_INFO"]

	try:
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

			request = PostDesignFile(response, designGuid, fileName, file)
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

			parentCategory = form.getvalue("parentCategory")

			request = GetCategories(response, designGuid, parentCategory)
			request.get()
		#post design category
		if (script_path == REQUEST_PATH.POST_DESIGN_CATEGORY):
			form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

			field = form["json"]
			fileName = field.filename
			jsonFile = field.file

			request = PostCategory(response, jsonFile)
			request.post()
		#delete design categiry
		if (script_path == REQUEST_PATH.DELETE_DESIGN_CATEGORY):
			form = cgi.FieldStorage(fp=cStringIO.StringIO(environ["wsgi.input"].read(int(environ["CONTENT_LENGTH"]))), environ=environ)

			category = form.getvalue("category")

			request = DeleteCategory(response, category)
			request.delete()
	except Exception as err:
		response.setStatus(RESPONSE_STATUS.INTERNAL_SERVER_ERROR)
		response.setContentType(CONTENT_TYPE.TEXT_PLAIN)
		response.setResponseBody(err)

	start_response(response.getStatus(), response.getHeaders())

	return [response.getResponseBody()]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()