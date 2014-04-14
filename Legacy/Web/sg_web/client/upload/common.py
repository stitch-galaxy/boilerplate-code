import requests

def postRequest(files, requestParameters):
	retries = 0
	while retries != 3:
		try:
			r = requests.post(uploadUrl, files = files, data = requestParameters, timeout = 1000)
			print "INFO: Success"
			break
		except:
			print "ERROR: POST request failed"
			retries = retries + 1