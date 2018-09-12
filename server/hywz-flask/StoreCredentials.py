#imports
import json

#read ins
#NEED TO READ IN FROM APP???
username = ''
password = ''

#----------DATA FILE----------

#creates data
data = {}

#creates file info
data['login'] = []

#ADD LOGIN INFO
def add():

    #----------ADDING IN CREDENTIALS----------

    #creates new user
    data['login'].append({
        'username': username,
        'password': password
    })

    #----------ADD TO FILE----------

    #dumps data to file
    with open('data.txt', 'w') as outfile:
        json.dump(data, outfile)
        
        