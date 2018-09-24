//
//  SignUpViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Khalid Alqahtani on 9/16/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    UIUsername
    
    
    @IBOutlet var takeUsername: UIUsername!
    
    @IBOutlet var takePassword: UIPassword!
    
    @IBOutlet var takeRepeatPassword: UITextField!
    
    
    
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
