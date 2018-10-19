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
        
        let requestURL = URL(string:"https://hywz-auth.auth.us-west-2.amazoncognito.com/login?response_type=code&client_id=7iea4023volnd3pfdp7jstdg61&redirect_uri=hygieawastezeroios://")
        let request = URLRequest(url: requestURL!)
        webView.loadRequest(request)
        //            asynchronousURLPoll{error in if let error = error {
        //                 print("Oops! Something went wrong...")
        //            } else {
        //                print ("finished")
        //                }
        //            }
        
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
    
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        if let text = webView.request?.url?.absoluteString{
            if text.hasPrefix("hygieawastezeroios://") {
                let idToken = text.components(separatedBy: "#id_token=")[1].components(separatedBy: "&")[0]
                print (idToken)
                print (text)
            }
        }
    }
    
    func asynchronousURLPoll(completion: @escaping (Error?) -> Void) {
        var x = false
        while (!x) {
            let tokenString = webView.request?.url?.absoluteString
            x = (tokenString?.hasPrefix("hygieawastezeroios://"))!
        }
        URLParse()
        completion(nil) // Or completion(SomeError.veryBadError)
    }
    
    func URLParse() {
        //        if let error = error {
        //    print("Oops! Something went wrong...")
        //    } else {
        let toSearch = webView.request?.url?.absoluteString.components(separatedBy: "#id_token=")[1].components(separatedBy: "&")[0]
        print (toSearch)
        print ("test")
        //}
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
