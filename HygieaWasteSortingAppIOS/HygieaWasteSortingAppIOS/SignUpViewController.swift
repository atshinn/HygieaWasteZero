//
//  SignUpViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Khalid Alqahtani on 9/16/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    //Used for outputing an error statement for user
    @IBOutlet var ErrorStatement: UILabel!
    
    //Username input
    @IBOutlet var takeUsername: UITextField!
    
    //Password and Password check input
    @IBOutlet var takePassword: UITextField!
    
    @IBOutlet var takeRepeatPassword: UITextField!
    
    //Check to make sure the inputs are correct
    @IBAction func Register(sender: UIButton) {
    
        if takePassword != takeRepeatPassword {
            ErrorStatement.text = "Passwords do not match"
        }
        
        var i: Double = 0
        
        for i in takeUsername{
            
        }

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
