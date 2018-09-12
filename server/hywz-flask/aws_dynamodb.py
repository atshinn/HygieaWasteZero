# -*- coding: utf-8 -*-
"""
Created on Fri Sep  7 00:45:03 2018

@author: Alec
"""
import uuid
import boto3 as aws

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
    dydb = None #get_dynamodb_west()
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