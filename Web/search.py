#!/usr/bin/env python

from wsgiref.simple_server import make_server
from cgi import parse_qs, escape
import json


class Design:
    name = None
    description = None
    width = None
    heigth = None
    colors = None
    imageSmallUrl = None
    imageLargeUrl = None
    releaseDate = None
    descriptionUrl = None
    downloadUrl = None
    avgRating = None
    sales = None
    def __init__(self):
        self.width = 0

    def toJson(self):
        return json.dumps({'name': self.name, 'description': self.description, 'width': self.width})

def application(environ, start_response):
    
    # Returns a dictionary containing lists as values.
    d = parse_qs(environ['QUERY_STRING'])
    
    # Read request parameters
    language = d.get('language', [''])[0] # Returns the language value.
    region = d.get('region', [''])[0] # Returns the region value.
    
    # Always escape user input to avoid script injection
    language = escape(language)
    region = escape(region)
    
    #Make db request
    
    #Create data structures
    
    design = Design()
    design.name = 'a'
    design.description = 'b'
    design.width = 15

    response_body = design.toJson()
    
    status = '200 OK'
    
    response_headers = [('Content-Type', 'application/json'),
                        ('Content-Length', str(len(response_body)))]
    start_response(status, response_headers)
    
    return [response_body]

httpd = make_server('localhost', 8051, application)
httpd.serve_forever()


