//
//  LoginViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/16/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//
import Foundation
import UIKit
import CoreLocation
//import AWSCognitoIdentityProvider.h



class LoginViewController: UIViewController, CLLocationManagerDelegate, UIWebViewDelegate{
    
    //LocationManager base
    let locManager = CLLocationManager()
    
    @IBOutlet weak var webView: UIWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        locManager.requestWhenInUseAuthorization()
        locManager.requestAlwaysAuthorization()
        startReceivingLocation()
        
        webView.delegate = self
        // Do any additional setup after loading the view.
        
        let requestURL = URL(string:"https://hywz-auth.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=8mrvs89q1frh6hqooc4nt9b0&redirect_uri=hygieawastezero://")
        //let requestURL = URL(string:"https://www.google.com/")
        let request = URLRequest(url: requestURL!)
        webView.loadRequest(request)
        
    }
    
    //    func webView(_ webView: UIWebView, shouldStartLoadWithRequest request: NSURLRequest, navigationType navigationType: UIWebView.NavigationType) -> Bool {
    //        if request.url?.scheme == "hygieawastezeroios" {
    //            self.performSegue(withIdentifier: "cameraViewSegue", sender: self)
    //        }
    //    }
    
    
    //    function onsubmit() {
    //    window.location = "https://hywz-auth.auth.us-west-2.amazoncognito.com/login?response_type=code&client_id=7iea4023volnd3pfdp7jstdg61&redirect_uri=hygieawastezeroios://"
    //    }
    
    
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
    
    
    func webView(_ webView: UIWebView,
                          shouldStartLoadWith request: URLRequest,
                          navigationType: UIWebViewNavigationType) -> Bool {
        //print("Finished loading page")
        if let text = request.url?.absoluteString{
            //print(text)
            if text.hasPrefix("hygieawastezero://") {
                let idToken = text.components(separatedBy: "#id_token=")[1].components(separatedBy: "&")[0]
                print("ID_TOKENTEST:")
                print (idToken)
            }
        }
        return true
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
