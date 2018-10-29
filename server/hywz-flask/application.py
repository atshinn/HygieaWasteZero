from flask import Flask, jsonify, request
import hywz_subroutines as subrout
import boto3 as aws
import os
from aws_s3 import download
from skimage.transform import resize

application = app = Flask(__name__)


# =============================================================================
# Routes
# =============================================================================
@app.route('/image', methods=['GET', 'POST'])
def non_param_image_route():
    if request.method == 'POST':
        return subrout.post_image_routine(request)
    
    elif request.method == 'GET':
        return 'GET'
    else:
        return "Method not supported by url"

@app.route('/predict', methods=['GET'])
def predict():    
    fkey = request.args.get('fkey')
    fp = os.path.join(os.getcwd(), fkey.split('/')[-1])    
    img = download('hywz.wastezero','us-west1', fkey, fp)
    
    img = resize(img, (28,28))
    
    return jsonify(json);


if __name__ == '__main__':
    application.run(debug=True)