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

#import boto3
#
#dynamodb = boto3.resource('dynamodb')
#
#table = dynamodb.create_table(
#    TableName='Compost',
#    KeySchema=[
#        {
#            'AttributeName': '???',
#            'KeyType': '???'
#        },
#        {
#            'AttributeName': '???',
#            'KeyType': '???'
#        }
#        ],
#    AttributeDefinitions=[
#        {
#            'AttributeName': '???',
#            'AttributeType': '???'
#        },
#        {
#            'AttributeName': '???',
#            'AttributeType': '???'
#        },
#        ],
#    ProvisionedThroughput={
#            'ReadCapacityUnits': 1,
#            'WriteCapacityUnits': 1
#        }
#        )
#
#table.meta.client.get_waiter('table_exists').wait(TableName='???')
#print(table.item_count)

import os
import boto3

#configuring
client = boto3.client('dynamodb')

client = boto3.client('dynamodb', aws_access_key_id='AKIAIK2KN2KQZFS7P6H', aws_secret_access_key='zXHC2ZYOtD0woz34BexonTMGc9LjRtpTDbyDJZ56', region_name='us-west-2')

key = os.environ['Compost']
key = os.environ['Recycle']
key = os.environ['Unlabeled']

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

ddb = new AWS.DynamoDB({apiVersion: '2018-11-09'});

### COMPOST ###

var params = {
    AttributeDefinitions: [
       {
           AttributeName: 'Compost_ID',
           AttributeType: 'String'
       }
    ],
    KeySchema: [
       {
           AttributeName: 'Compost_ID',
           KeyType: 'String'
       }
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 1,
        WriteCapacityUnits: 1
    },
    TableName: 'Compost',
        StreamSpecification: {
        StreamEnabled: false
    }
};

ddb.createTable(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});

### RECYCLE ###

var params = {
    AttributeDefinitions: [
       {
           AttributeName: 'Recycle_ID',
           AttributeType: 'String'
       }
   ],
   KeySchema: [
       {
           AttributeName: 'Recycle_ID',
           KeyType: 'String'
       }
   ],
        ProvisionedThroughput: {
           ReadCapacityUnits: 1,
           WriteCapacityUnits: 1
        },
        TableName: 'Recycle',
            StreamSpecification: {
            StreamEnabled: false
    }
};

ddb.createTable(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});

### UNLABELED ###

var params = {
    AttributeDefinitions: [
       {
           AttributeName: 'Unlabeled_ID',
           AttributeType: 'String'
       }
    ],
    KeySchema: [
       {
           AttributeName: 'Unlabeled_ID',
           KeyType: 'String'
       }
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 1,
        WriteCapacityUnits: 1
    },
    TableName: 'Unlabeled',
        StreamSpecification: {
        StreamEnabled: false
    }
};

ddb.createTable(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});

### LISTING ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

ddb = new AWS.DynamoDB({apiVersion: '2018-11-13'});

ddb.listTables({Limit: 3}, function(err, data) {
    if (err) {
        console.log("Error", err.code);
    } else {
        console.log("Table names are ", data.TableNames);
    }
});

### DESCRIBING ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

ddb = new AWS.DynamoDB({apiVersion: '2018-11-13'});

var params = {
    TableName: process.argv[2]
};

ddb.listTables({Limit: 3}, function(err, data) {
    if (err) {
        console.log("Error", err.code);
    } else {
        console.log("Success", data.Table.KeySchema);
    }
});

### DELETING ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

// Create the DynamoDB service object
ddb = new AWS.DynamoDB({apiVersion: '2018-11-13'});

var params = {
    TableName: process.argv[2]
};

ddb.deleteTable(params, function(err, data) {
    if (err && err.code === 'ResourceNotFoundException') {
        console.log("Error: Table not found");
    } else if (err && err.code === 'ResourceInUseException') {
        console.log("Error: Table in use");
    } else {
        console.log("Success", data);
    }
});

### ADDING ITEMS INTO TABLE : COMPOST ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

var docClient = new AWS.DynamoDB.DocumentClient({apiVersion: '2018-11-15'});

var params = {
    TableName: 'Compost',
    Item: {
        'ITEM': LINK,
    }
};

docClient.put(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});

### ADDING ITEMS INTO TABLE : RECYCLE ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

var docClient = new AWS.DynamoDB.DocumentClient({apiVersion: '2018-11-15'});

var params = {
    TableName: 'Recycle',
    Item: {
        'ITEM': LINK,
    }
};

docClient.put(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});

### ADDING ITEMS INTO TABLE : UNLABELED ###

var AWS = require('aws-sdk');
AWS.config.update({region: 'us-west-2'});

var docClient = new AWS.DynamoDB.DocumentClient({apiVersion: '2018-11-15'});

var params = {
    TableName: 'Unlabled',
    Item: {
        'ITEM': LINK,
    }
};

docClient.put(params, function(err, data) {
    if (err) {
        console.log("Error", err);
    } else {
        console.log("Success", data);
    }
});
