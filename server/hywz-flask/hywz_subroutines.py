# -*- coding: utf-8 -*-
"""
Created on Thu Aug 30 18:31:11 2018

@author: Alec

This module contains all the subroutines that 
"""
from flask import jsonify
from aws_s3 import add_bin_to_bucket
from hywz_utils import create_file_name_from_datetime as file_name, determine_bucket_object_url as img_url
from json import JSONEncoder as JEncoder
import os
from base64 import b64encode

def post_image_routine(app, request, callback=None):
    file = request.files['file']
    fn = file_name()               
    img_bin = file.stream.read()
    add_bin_to_bucket(img_bin, fn)
    item = {'file_url': img_url(fn, os.environ['AWS_BUCKET_NAME'])}
    return str(item)
    

            
def annotate_image_by_id(request, img_id):
    pass


def get_all_trash_routine():
    j_encoder = JEncoder()
    #dydb = get_dynamodb_west()
    #trash_table = dydb.Table('Trash')
    #trash_list = trash_table.scan()['Items']
    #return j_encoder.encode(trash_list)
    

def get_trash_routine(request, resource_id):
    j_encoder = JEncoder()
    #dydb = get_dynamodb_west()
    #trash_table = dydb.Table('Trash')
    #Ekey = {'id': resource_id}
    #response = trash_table.get_item(Key=key)
    '''try:
        response['Item']
    except KeyError:
        return '{} is not a valid resource_id'.format(resource_id)
    else:
        return j_encoder.encode(response['Item'])'''
    