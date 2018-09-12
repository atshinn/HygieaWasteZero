# -*- coding: utf-8 -*-
"""
Created on Fri Sep  7 13:07:11 2018

@author: Alec
"""

# =============================================================================
# GOOGLE CLOUD VISION SECTION
# =============================================================================
from google.cloud.vision_v1 import ImageAnnotatorClient
from google.cloud import vision
from google.api_core import retry

def annotate_image(request):
    client = ImageAnnotatorClient()
    response = client.annotate_image(request=request)
    return response

'''
def detect_img_labels(request):
    global cpath
    my_retry = retry.Retry(deadline=60)
    client = ImageAnnotatorClient()
    image = vision.types.Image(content=img_bin)
    response = client.label_detection(image,my_retry,timeout=10)
    result = {"landmark": response.landmark}
    return response.label_annotations
'''

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
    client = vision.ImageAnnotatorClient()
    image = vision.types.Image()

    # Performs label detection on the image file
    response = client.label_detection(image=image, retry=2, timeout=10)

    labels = response.label_annotations
    
    return decide(str(labels))