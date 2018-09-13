#imports
import json

#read ins
#NEED TO READ IN FROM APP???
username = ''
password = ''

#flag to check username and password with guidelines
#flag = false

#Arrays
#Letters = ['Q', 'q', 'W', 'w', 'E', 'e', 'R', 'r', 'T', 't', 'Y', 'y', 'U', 'u', 'I', 'i', 'O', 'o', 'P', 'p', 'A', 'a', 'S', 's', 'D', 'd', 'F', 'f', 'G', 'g', 'H', 'h', 'J', 'j', 'K', 'k', 'L', 'l', 'Z', 'z', 'X', 'x', 'C', 'c', 'V', 'v', 'B', 'b', 'N', 'n', 'M', 'm']
#Numbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
#Symbols = ['-', '_', '`', '.', ' ']

def guidelines(username, password):
    if username.isascii() and username.len() > 7 and username.len() < 17:
        if password.isascii() and password.len() > 7 and password.len() < 17:
            return true