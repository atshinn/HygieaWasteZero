#from __future__ import print_function
#import boto3
#import json
#import decimal
#from boto3.dynamodb.conditions import Key, Attr
#
#class DecimalEncoder(json.JSONEncoder):
#    def default(self, o):
#        if isinstance(o, decimal.Decimal):
#            if o % 1 > 0:
#                return float(o)
#            else:
#                return int(o)
#        return super(DecimalEncoder, self).default(o)
#
####   COMPOST   ###
#dynamodb = boto3.resource('dynamodb_Compost', region_name='us-west-2', endpoint_url="https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/compost/?region=us-west-2&tab=overview")
#
#table = dynamodb.Table('Compost')
#
#print("Compost")
#
#
####   RECYCLE   ###
#dynamodb = boto3.resource('dynamodb_Recycle', region_name='us-west-2', endpoint_url="https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/recycle/?region=us-west-2&tab=overview")
#
#table = dynamodb.Table('Recyle')
#
#print("Recycle")
#
#
####   UNLABELED   ###
#dynamodb = boto3.resource('dynamodb_Unlabeled', region_name='us-west-2', endpoint_url="https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/unlabeled/?region=us-west-2&tab=overview")
#
#table = dynamodb.Table('Unlabeled')
#
#print("Unlabeled")
#
#    #response = table.query(
#    #                       KeyConditionExpression=Key('???').eq(???)
##                       )
#
##for i in response['Items']:
##    print(i['???'], ":", i['???'])

import boto3

dynamodb = boto3.resource('dynamodb')

table = dynamodb.create_table(
    TableName='Compost',
    KeySchema=[
        {
            'AttributeName': 'username',
            'KeyType': 'HASH'
        },
        {
            'AttributeName': 'last_name',
            'KeyType': 'RANGE'
        }
        ],
    AttributeDefinitions=[
        {
            'AttributeName': 'username',
            'AttributeType': 'S'
        },
        {
            'AttributeName': 'last_name',
            'AttributeType': 'S'
        },
        ],
    ProvisionedThroughput={
            'ReadCapacityUnits': 1,
            'WriteCapacityUnits': 1
        }
        )

table.meta.client.get_waiter('table_exists').wait(TableName='users')
print(table.item_count)
