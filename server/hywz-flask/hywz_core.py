# -*- coding: utf-8 -*-
"""
Created on Thu Aug 30 14:05:39 2018

@author: Alec
"""
# =============================================================================
# AWS SECTION
# =============================================================================
import boto3 as aws
import uuid

_AWS_ACCESS_KEY_ID = 'AKIAIVABEJNTQ76CZGVQ'
_AWS_SECRET_ACCESS_KEY = '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'
_AWS_S3_BUCKET = 'hywz.wastezero'

def _get_aws_resource(resource, region=None):
    print('resource={} region={}'.format(resource, region))   
    
    def _validate_resource_request(func):
        print('validating resource request')
        global _AWS_ACCESS_KEY_ID, _AWS_SECRET_ACCESS_KEY
            
        if resource == "dynamodb" and region is None:
            raise Exception('Resource Requires region_name')
        if _AWS_ACCESS_KEY_ID is None or _AWS_SECRET_ACCESS_KEY is None:
            raise Exception('AWS Verification Credentials not found')
                
        def decorator():
            if region is not None:
                return func(aws.resource(resource, region_name=region, aws_access_key_id=_AWS_ACCESS_KEY_ID, aws_secret_access_key = _AWS_SECRET_ACCESS_KEY))
            else:
                return func(aws.resource(resource, aws_access_key_id=_AWS_ACCESS_KEY_ID, aws_secret_access_key = _AWS_SECRET_ACCESS_KEY)) 
        return decorator
    return _validate_resource_request
    

@_get_aws_resource('dynamodb',region='us-east-2')
def get_dynamodb(res=None):
    if res is None:
        raise Exception('decorator failed to pass resource as parameter')
    return res

@_get_aws_resource('dynamodb',region='us-west-2')
def get_dynamodb_west(res=None):
    if res is None:
        raise Exception('decorator failed to pass resource as parameter')
    return res

@_get_aws_resource('s3')
def get_s3(res=None):
    if res is None:
        raise Exception('decorator failed to pass resource as parameter')
    return res

def create_uid(table, id_key):
    uid_not_found = True
    
    while(uid_not_found):
        uid = str(uuid.uuid1()).replace('-','')
        key = {id_key: uid}
        response = table.get_item(Key=key)            
        try:
            response['Item']
        except KeyError:
            uid_not_found = False
    return uid 


def create_resource(table_name, id_key, item):
    dydb = get_dynamodb_west()
    table = dydb.Table(table_name)
    
    uid = create_uid(table, id_key)
    
    item[id_key] = uid
    item['resource_url'] = 'http://hywz.us-east-2.elasticbeanstalk.com/trash/{}'.format(uid)
    table.put_item(Item=item)
    return item
    
def create_resource_json_response(res_name):
    res = {}
    res['resource'] = res_name
    res['resourceLocation'] = 'http://hywz.us-east-2.elasticbeanstalk.com/trash/{}'.format()
    
def get_item_path_from_bucket(key):
    pass    

def add_bin_to_bucket(i_bin, key):
    global _AWS_S3_BUCKET
    s3 = get_s3()
    bucket = s3.Bucket(_AWS_S3_BUCKET)
    bucket.put_object(Key=key, Body=i_bin)
    
# =============================================================================
# GOOGLE CLOUD VISION SECTION
# =============================================================================
from google.cloud.vision_v1 import ImageAnnotatorClient
from google.cloud import vision
from google.oauth2.service_account import Credentials
from google.api_core import retry


_G_CREDS = Credentials.from_service_account_file(r'gcvServiceKey.json')
_G_CLIENT = vision.ImageAnnotatorClient(credentials=_G_CREDS)

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
    global _G_CREDS
    # Instantiates a client
    
    client = vision.ImageAnnotatorClient(credentials=_G_CREDS)
    image = vision.types.Image(content=img_bin)

    # Performs label detection on the image file
    response = client.label_detection(image=image, retry=2, timeout=10)

    labels = response.label_annotations
    
    return decide(str(labels))