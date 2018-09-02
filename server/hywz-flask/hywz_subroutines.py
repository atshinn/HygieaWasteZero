# -*- coding: utf-8 -*-
"""
Created on Thu Aug 30 18:31:11 2018

@author: Alec

This module contains all the subroutines that 
"""

from hywz_core import get_dynamodb_west, get_s3, create_resource, add_bin_to_bucket
from hywz_utils import create_file_name_from_datetime as file_name
from json import JSONEncoder as JEncoder

def post_trash_routine(request):
    je = JEncoder()
    if 'image' in request.files:
        file = request.files['image']
    else:
        raise Exception('REQUEST REQUIRES AN IMAGE')
            
    if file is not None:       
        fn = file_name()                
        img_bin = file.stream.read()
        add_bin_to_bucket(img_bin, fn)
        item = {'file_url': 'https://s3-us-west-1.amazonaws.com/hywz.wastezero/{}'.format(fn)}
        return je.encode(create_resource('Trash', 'id', item))
    else:
        raise Exception("Image is None")


def get_all_trash_routine():
    j_encoder = JEncoder()
    dydb = get_dynamodb_west()
    trash_table = dydb.Table('Trash')
    trash_list = trash_table.scan()['Items']
    return j_encoder.encode(trash_list)
    

def get_trash_routine(request, resource_id):
    j_encoder = JEncoder()
    dydb = get_dynamodb_west()
    trash_table = dydb.Table('Trash')
    key = {'id': resource_id}
    response = trash_table.get_item(Key=key)
    try:
        response['Item']
    except KeyError:
        return '{} is not a valid resource_id'.format(resource_id)
    else:
        return j_encoder.encode(response['Item'])
    