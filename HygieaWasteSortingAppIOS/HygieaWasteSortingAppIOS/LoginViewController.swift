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
        
        //Info from Json
        struct StoredData {
            var name: String
            var username: String
            var email: String
            var password: String
        }
        
        let decoder = JSONDecoder()
        
        let stored = try decoder.decode(StoredData.self, from: jsonObject)
        
        if username == stored.username {
            if password == stored.password {
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
    
    @IBAction func jumpToGuest(_ sender: UIButton) {
        //Jump to Camera for now
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        locManager.requestWhenInUseAuthorization()
        locManager.requestAlwaysAuthorization()
        startReceivingLocation()
    }

    //// ALL OF THESE FUNCTIONS SHOULD BE USED WHEN LOCATION IS RETRIEVED ////
    func startReceivingLocation(){
        let authStatus = CLLocationManager.authorizationStatus()
        if authStatus != .authorizedAlways {
            //User hasn't authorized access to location info
            return
        }
        
        if !CLLocationManager.significantLocationChangeMonitoringAvailable() {
            // Service is not available
            return
        }
        
        locManager.delegate = self
        //Might need to be moved; start only when needed, stop when not needed later
        locManager.startMonitoringSignificantLocationChanges()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let mostRecentLoc = locations.last!
        
        //do something with the location
        let coordinate = mostRecentLoc.coordinate
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        if let error = error as? CLError, error.code == .denied {
            //Loc updates are not authorized
            manager.stopMonitoringSignificantLocationChanges()
            return
        }
        //This func notifies users of errors, more cases may need to be added later
    }
    ////---------------(End "All of these functions")-------------------////
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
