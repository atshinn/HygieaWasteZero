#imports
import json

#read ins
#NEED TO READ IN FROM APP???
username = ''
password = ''

#flag for if the login is correct
flag = false

#----------CHECK FUNCTION----------
def check():
    checkLogin(username, password)
    if flag:
        return "true"
    else:
        return "Incorrect Username or Password. Please enter in again."
    

#----------CHECK LOGIN INFO----------
def checkLogin(username, password):

    #open data file
    with open('data.txt') as json_file:
        data = json.load(json_file)
        
        #check through data file
        for i in data['login']:
            
            #check if username is correct
            if p['username'] == username:
                
                #check if password is correct
                if p['password'] == password:
                    
                    #login is correct
                    flag = true
                    
    return flag
    