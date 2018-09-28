//
//  LoginViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/16/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit
import CoreLocation

class LoginViewController: UIViewController, CLLocationManagerDelegate{

    //LocationManager base
    let locManager = CLLocationManager()
    
    //String Inputs
    var username: String = ""
    var password: String = ""
    
    @IBOutlet var ErrorStatement: UILabel!
    
    @IBOutlet var takeUsername: UITextField!
    
    @IBOutlet var takePassword: UITextField!
    
    @IBAction func checkLogin(_ sender: UIButton) {
        //Take Inputs
        username = takeUsername.text!
        password = takePassword.text!
        
        //Flag to see if username and password are correct
        var flag: Bool = false
        
        //Need Json connection???
        if username == username {
            if password == password {
                flag = true
            }
        }
        
        if flag == true {
            //Successful login
        }
        else {
            ErrorStatement.text = "Incorrect Username or Password"
        }
        
    }
    
    @IBAction func jumpToSignUp(_ sender: UIButton) {
        //Jump to Signup
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        locManager.delegate = self
        locManager.requestWhenInUseAuthorization()
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
