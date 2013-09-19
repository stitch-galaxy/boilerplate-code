import os, os.path

#configuration
from sg_web.client.upload.upload_config import serverUrl

#clasess
from sg_web.client.upload.common import postRequest

#script body
uploadUrl = serverUrl + "/uploadCategory"

designs = Designs()

#prepare data for requests
if os.path.exists("./data/categories/add.json"):
	requestParameters = dict()
	files = dict()
	files["add.json"] = open("./data/categories/add.json", "rb")
	print "Posting category"
	postRequest(files, requestParameters)
