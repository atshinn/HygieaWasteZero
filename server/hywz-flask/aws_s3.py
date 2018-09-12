# -*- coding: utf-8 -*-
"""
Created on Fri Sep  7 13:01:48 2018

@author: Alec
"""
import boto3 as aws
import os

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


def retreive_object_from_bucket(auth, key, bucket, fn, region_name):
    s3 = aws.resource('s3', region_name=region_name, aws_acess_key_id=auth[0], aws_secret_access_key=auth[1])
    
    buck = s3.Bucket(bucket)
    
    

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














