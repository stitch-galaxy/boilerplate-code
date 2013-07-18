﻿from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import cgi
import cStringIO

from sgconfig import serverName
from sgconfig import serverPort

CHUNKSIZE = 8192

def application(environ, start_response):

#	length = environ.get('CONTENT_LENGTH', '0')
#	length = 0 if length == '' else int(length)
#	body = environ['wsgi.input'].read(length)
#
#	response_body = body
	response_body = 'a'

	form = cgi.FieldStorage(fp=cStringIO.StringIO(environ['wsgi.input'].read(int(environ['CONTENT_LENGTH']))), environ=environ)
	if form.has_key('design'):
		desingList = form.getlist('design')

#	form = cgi.FieldStorage(fp=environ['wsgi.input'], environ=environ)

	status = "200 OK"
	field = form.list[0]
	if field.name == 'design':
		filename = field.filename
		file = field.file

		with open("test.txt", "w+b") as fh:
			chunk = file.read(CHUNKSIZE)
			while True:
				if chunk:
					fh.write(chunk)
					chunk = file.read(CHUNKSIZE)
				else:
					break

	response_headers = [("Content-Type", "text/plain"),
						("Content-Length", str(len(response_body)))]

	start_response(status, response_headers)

	return [response_body]

httpd = make_server(serverName, serverPort, application)
httpd.serve_forever()