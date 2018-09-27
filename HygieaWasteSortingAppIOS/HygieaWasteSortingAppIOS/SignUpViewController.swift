//
//  SignUpViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Khalid Alqahtani on 9/16/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    //String Inputs
    var username: String = ""
    var password: String = ""
    var repeatPassword: String = ""
    
    //Array
    let symbols = ["Q", "q", "W", "w", "E", "e", "R", "r", "T", "t", "Y", "y", "U", "u", "I", "i", "O", "o", "P", "p", "A", "a", "S", "s", "D", "d", "F", "f", "G", "g", "H", "h", "J", "j", "K", "k", "L", "l", "Z", "z", "X", "x", "C", "c", "V", "v", "B", "b", "N", "n", "M", "m", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "_", "`", ".", " "]
    
    //Used for outputing an error statement for user
    @IBOutlet var ErrorStatement: UILabel!
    
    //Username input
    @IBOutlet var takeUsername: UITextField!
    
    //Password and Password check input
    @IBOutlet var takePassword: UITextField!
    
    @IBOutlet var takeRepeatPassword: UITextField!
    
    //Check to make sure the inputs are correct
    @IBAction func Register(sender: UIButton) {
    
        //Take Inputs
        username = takeUsername.text!
        password = takePassword.text!
        repeatPassword = takeRepeatPassword.text!
        
        //Variables for checking symbols
        var i: Double = 0
        var j: Double = 0
        //var temp: String = ""
        
        //Flag to see if username and password can be accepted
        var flag: Bool = true
        
        //Check if passwords match
        if password != repeatPassword {
            flag = false
            ErrorStatement.text = "Passwords do not match"
        }
        
        //Checks length of username
        if username.count < 8 {
            if username.count > 16 {
                flag = false
                ErrorStatement.text = "Username must be between 8 and 16 characters"
            }
        }
        
        //Checks length of password
        if password.count < 8 {
            if password.count > 16 {
                flag = false
                ErrorStatement.text = "Password must be between 8 and 16 characters"
            }
        }
        
        //Checks that username is the correct symbols
        for i in username {
            
            //temp = username[i]
            
            for j in symbols {
                //ERROR NEED HELP
                if username[i] == symbols[j] {
                    break
                }
                else {
                    flag = false
                    ErrorStatement.text = "Username containing incorrect symbols. Please only use Letters, Numbers, Spaces, and the following symbols: '-', '_', '`, '."
                }
            }
        }
        
        //Checks that password is the correct symbols
        for i in password {
            
            //temp = username[i]
            
            for j in symbols {
                //ERROR NEED HELP
                if password[i] == symbols[j] {
                    break
                }
                else {
                    flag = false
                    ErrorStatement.text = "Password containing incorrect symbols. Please only use Letters, Numbers, Spaces, and the following symbols: '-', '_', '`, '."
                }
            }
        }
        
        if flag == true {
            //NEED HELP CANT FIGURE OUT JSON
            //store username and password
        }

    }
    
    @IBAction func jumpToLogin(_ sender: UIButton) {
        //jump to Login
    }
    
        
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
