# -*- coding: utf-8 -*-
"""
Created on Thu Aug 30 13:00:10 2018

@author: Alec Shinn
"""
from datetime import datetime as dt
from re import sub
from os.path import join, exists
from json import load as jload


def create_file_name_from_datetime(file_type='jpg'):
     fn = '{}.{}'.format(str(dt.now().strftime("%c")), file_type)
     fn = sub(' ', '_', fn)
     fn = sub(':','-', fn)
     return fn

# TODO REPLACE METHOD WITH ONE THAT UTILIZES THE CONTEXT STORAGE
def determine_bucket_object_url(fn, bucket_name, region='https://s3-us-west-1.amazonaws.com'):
    return "{}/{}/{}".format(region, bucket_name, fn)


def open_resource_file(app, filename, callback=None,**kwargs):
    fp = join(app.root_path,filename)
    if exists(fp):
        with app.open_resource(fp,'rb') as json_file:
            result = jload(json_file)
    else:
        raise Exception("{} does not exist".format(fp))
    return result


if __name__ == '__main__':
    print(create_file_name_from_datetime())
    print(create_file_name_from_datetime())