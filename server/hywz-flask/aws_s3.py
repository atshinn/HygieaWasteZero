# -*- coding: utf-8 -*-
"""
Created on Fri Sep  7 13:01:48 2018

@author: Alec
"""
import boto3 as aws
import os
from matplotlib import pyplot as plt
def _get_aws_resource(resource, region=None):
    print('resource={} region={}'.format(resource, region))   
    
    def _validate_resource_request(func):
        print('validating resource request')
            
        if resource == "dynamodb" and region is None:
            raise Exception('Resource Requires region_name')
        if os.environ['AWS_SAKID'] is None or os.environ['AWS_SAK'] is None:
            raise Exception('AWS Verification Credentials not found')
                
        def decorator():
            if region is not None:
                return func(aws.resource(resource, region_name=region, aws_access_key_id=os.environ['AWS_SAKID'], aws_secret_access_key = os.environ['AWS_SAK']))
            else:
                return func(aws.resource(resource, aws_access_key_id=os.environ['AWS_SAKID'], aws_secret_access_key = os.environ['AWS_SAK'])) 
        return decorator
    return _validate_resource_request
    

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


def download(bucket_name, region, fkey, fp):
    s3 = aws.client('s3', aws_access_key_id=os.environ['AWS_AK'], aws_secret_access_key=os.environ['AWS_SAK'], region_name=region)
    finished = False
    fsize = s3.head_object(Bucket=bucket_name, Key=fkey)['ContentLength']
    print('FSIZE =', fsize)
    
    def callback(transferred):
        nonlocal fsize
        nonlocal finished
        print(fsize, finished)
        if(fsize == transferred):
            finished = True
    cb = callback
    
    fp = os.path.join(os.getcwd(), fkey.split('/')[-1])
    
    s3.download_file(bucket_name, fkey, fp, Callback=cb)
    while not finished:
        pass
    print('download finished')
    
    return plt.imread(fp)

def add_bin_to_bucket(i_bin, key):
    try:
        s3 = get_s3()
        bucket = s3.Bucket(os.environ['AWS_BUCKET_NAME'])
        
        bucket.put_object(Key=key, Body=i_bin)
    except Exception:
        return 'UNABLE TO ADD BIN TO BUCKET'
    else:
        return '{} ADDED TO BUCKET'.format(key)
    return 'NOT SURE WHAT HAPPENED HERE'














