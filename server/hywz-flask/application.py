from flask import Flask, request_started, jsonify, request, g, appcontext_pushed, has_app_context, has_request_context
import hywz_subroutines as subrout
from google.cloud.vision import ImageAnnotatorClient
from os.path import join, exists
from os import environ, chdir, getcwd as get_cwd
import boto3 as aws
from base64 import b64encode as encode, b64decode as decode
from json import load
from json import JSONEncoder as j_encoder, JSONDecoder as j_decoder
from hywz_vision import annotate_image as annotator
from traceback import format_exc
import sys

application = app = Flask(__name__)

# =============================================================================
# Setup Methods
# =============================================================================
@app.before_first_request
def init_setup_request():
    # has both app and request context
    fp = join(app.root_path, "vision_call_template.json")
    g.vision_call_template = load(app.open_resource(fp))    
    environ['GOOGLE_APPLICATION_CREDENTIALS'] = join(app.root_path,'resources', 'gcvServiceKey.json')
    if exists(environ['GOOGLE_APPLICATION_CREDENTIALS']):
        print('GOOGLE_APP_CREDS EXIST', file=sys.stderr)
    else:
        print('GOOGLE_APP_CREDS DO NOT EXIST', file=sys.stderr)   
    
def before_each_request(sender, **extra):
    print("before each request", file=sys.stderr)
    fp = join(app.root_path, "vision_call_template.json")
    if exists(fp):
        g.vision_call_template = load(app.open_resource(fp)) 
        

@app.route('/annotate', methods=["POST"])
def annotate_image():
    json = request.get_json()
    img_data = decode(json['image'])    
    g_request = getattr(g,'vision_call_template',None)
    g_request['image']['content'] = img_data
    response = client.annotate_image(request=request)
    result = {"classification": "Recycle", "annotation": j_encoder().encode(str(response))}
    return jsonify(str(result))

@app.route('/test/empty', methods=['GET','POST','PUT','DELETE'])
def empty_test():
    return "empty test complete"

@app.route('/test/env', methods=['GET'])
def env_test():
    try:
        if(exists(environ['GOOGLE_APPLICATION_CREDENTIALS'])):
            return "TEST PASSED"
        else:
            return "TEST FAILED BUT NO EXCEPTION"
    except Exception:
        return format_exc()
    
    
@app.route('/test/vision_call_template', methods=['GET'])
def get_vision_call_template():
    return str(g.vision_call_template)


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

    
@app.route('/image/<string:img_id>', methods=['GET', 'POST'])
def select_image(img_id):
    jcfg = open_resource_file(app, 'config.json')
    result = {}
    
    try:
        s3 = aws.resource('s3', region_name=jcfg['s3']['region'], aws_access_key_id=jcfg['s3']['sakid'], aws_secret_access_key=jcfg['s3']['sak'])
        bucket = s3.Bucket(jcfg['s3']['bucket_name'])
        
        with open(r'resources/downloads/{}'.format(img_id),'wb') as download:
            bucket.download_fileobj(img_id, download)
            download.close()
        
        with open(r'resources/downloads/{}'.format(img_id), 'rb') as img_file:
            img_bin = img_file.read()
            
        encoded_bin = encode(img_bin)   
        
    except KeyError as key_err:
        result['status'] = 500
    else:
        result['status'] = 200
    
    result['image_content']=str(encoded_bin)
    
    return jsonify(result)
    
@app.route('/image/annotate/', methods=['POST', 'PUT'])
def annotate__image():
    result = {}
    if request.method == 'POST':
        vision_call = request.get_json()
        #vision_call = open_resource_file(app, join("resources", "vision_call_template.json"))
        #vision_call['results'][0]['image']['source']['image_uri'] = get_url(img_fn,'hywz.wastezero')
        client = ImageAnnotatorClient()
        response = client.annotate_image(vision_call)
        return jsonify(response)

# =============================================================================
# Teardown
# =============================================================================
request_started.connect(before_each_request, app)


if __name__ == '__main__':
    application.run(debug=True)