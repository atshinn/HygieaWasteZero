import http.client
from flask import Flask, Request

PORT = 8888
application = app = Flask(__name__)

@app.route('/image/annotate', methods=['POST'])
def annotate_image():
	payload = Request.get_json()

	conn = http.client.HTTPConnection("localhost")

	headers = {
	    'content-type': "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW",
	    'Content-Type': "multipart/form-data",
	    'Cache-Control': "no-cache",
	    }

	conn.request("POST", "image", payload, headers)

	res = conn.getresponse()
	data = res.read()

	print(data.decode("utf-8"))

if __name__ == "__main__":
	app.run(debug=True)