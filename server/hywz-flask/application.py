from flask import Flask, request
from datetime import datetime as dt
from re import sub
from google.cloud.vision_v1 import ImageAnnotatorClient
from google.cloud import vision
from google.oauth2.service_account import Credentials
from google.api_core import retry
from traceback import format_exc
import boto3 as aws

_AWS_ACCESS_KEY_ID = 'AKIAIVABEJNTQ76CZGVQ'
_AWS_SECRET_ACCESS_KEY = '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'
_AWS_S3_BUCKET = 'hywz.wastezero'

application = app = Flask(__name__)


def detect_img_labels(img_bin):
    global cpath
    my_retry = retry.Retry(deadline=60)
    creds = Credentials.from_service_account_file(r'gcvServiceKey.json')
    client = ImageAnnotatorClient(credentials=creds)
    image = vision.types.Image(content=img_bin)
    response = client.label_detection(image,my_retry,timeout=10)
    return response.label_annotations


def decide(arg):
    #array of recycle words
    arg=str(arg)
    recycle = ['plastic', 'aluminum', 'bottle', 'recycle', 'recyclable', 'paper']
    #if the word from Json is the one that needs to be read is there
    if type(arg) == type(''):
        for i in recycle:
            if i in arg.lower():
                return 'Recycle'
        return 'Compost'
    
    else:
        return 'error'


def call_vision(img_bin):
    _G_CREDS = Credentials.from_service_account_file(r'gcvServiceKey.json')
    # Instantiates a client
    client = vision.ImageAnnotatorClient(credentials=_G_CREDS)
    image = types.Image(content=img_bin)

    # Performs label detection on the image file
    response = client.label_detection(image=image, retry=2, timeout=10)

    labels = response.label_annotations
    
    return decide(str(labels))


@app.route('/trash',methods=['POST','GET'])
def upload_image():
    global _AWS_ACCESS_KEY_ID, _AWS_S3_BUCKET, _AWS_SECRET_ACCESS_KEY
    try:
        
        if request.method == 'POST':
            sess = aws.session.Session(aws_access_key_id=_AWS_ACCESS_KEY_ID, aws_secret_access_key=_AWS_SECRET_ACCESS_KEY)
            s3 = s3 = aws.resource('s3', aws_access_key_id=_AWS_ACCESS_KEY_ID, aws_secret_access_key=_AWS_SECRET_ACCESS_KEY)
            rekog = sess.client('rekognition',region_name= aws_access_key_id=_AWS_ACCESS_KEY_ID, aws_secret_access_key=_AWS_SECRET_ACCESS_KEY)
            bucket = s3.Bucket(_AWS_S3_BUCKET)
            if 'image' in request.files:
                file = request.files['image']
            else:
                return 'IMAGE NOT IN REQUEST.FILES'
            
            if file is not None:
                fn = '{}.{}'.format(str(dt.now().strftime("%c")),file.filename.rsplit('.')[-1])
                fn = sub(' ', '_', fn)
                fn = sub(':','-', fn)
                
                img_bin = file.stream.read()
                bucket.put_object(Key=fn, Body=img_bin)
                return fn
            else:
                return 'FILE IS NONE'
    
        if request.method == 'GET':
            return \
                    '''
                    <html>
                        <head></head>
                        <body>
                            <form action="http://hywz.us-east-2.elasticbeanstalk.com/trash" method="POST">
                                <input type="file" name="image" accept="image/*">
                                <input type="submit">
                            </form>
                        </body>
                    </html>
                    '''
    except Exception:
        return str(format_exc())

@app.route('/')
def landing():
    return "landing page for hywz"
if __name__ == '__main__':
    application.run(debug=True)