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
import AWSCore
import AWSCognitoIdentityProvider



class LoginViewController: UIViewController, CLLocationManagerDelegate, UIWebViewDelegate{
    //let vc = ViewController()
    //LocationManager base
    let locManager = CLLocationManager()
    //var loggedIn = false
    var credentialsProvider = AWSCognitoCredentialsProvider()
    
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet var identityIdTask: AWSTask<NSString>!
    @IBOutlet var identityId: NSString!
    var isDev = Bool()
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
                          navigationType: UIWebView.NavigationType) -> Bool {
        //print("Finished loading page")
        if let text = request.url?.absoluteString{
            //print(text)
            if text.hasPrefix("hygieawastezero://") {
                let idToken = text.components(separatedBy: "#id_token=")[1].components(separatedBy: "&")[0]
                print("ID_TOKENTEST:")
                print (idToken)
                getCreds(token: idToken)
            }
        }
        return true
    }
    
    func getCreds(token: String){
        let idProviderIns = idProvider(tokens: ["cognito-idp.us-west-2.amazonaws.com/us-west-2_2KW8CF0tm": token as NSString])
        print("idProviderIns was set!")
        
        credentialsProvider = AWSCognitoCredentialsProvider(
            regionType: .USWest2,
            identityPoolId: "us-west-2:6bd013d4-d707-4d4b-9174-29170cd89ad1",
            identityProviderManager: idProviderIns)
        print("credentialsProvider was set!")
        let configuration = AWSServiceConfiguration(region: .USWest2, credentialsProvider: credentialsProvider)
        AWSServiceManager.default().defaultServiceConfiguration = configuration
        
        identityIdTask = credentialsProvider.getIdentityId().continueWith(block: {(task) -> AnyObject? in
            if (task.error != nil) {
                print("Error: " + task.error!.localizedDescription)
            } else {
                //task result contains identity id
                let cognitoID = task.result!
                print("Cognito id: \(cognitoID)")
            }
            return task;
        }) as! AWSTask<NSString>
        print("identityIdTask was set!")
        print("Determining if developer account...")
        determineDev(token: token)
        print("IsDeveloper?: " + String(isDev))
        identityIdTask.continueWith(block: {(task) -> Any? in
            var t = true
            print("Starting century-long wait...")
            while(t)
            {
                if (task.result != nil){
                    print("Done!")
                    print(task.result!)
                    self.identityId = task.result!
                    t = false
                   // self.loggedIn = true
                    self.performSegue(withIdentifier: "cameraViewSegue", sender: self)
                }
            }
            return task.result!
        })
        //Call identityIdTask.result! to geth the identityId needed
        //identityId = identityIdTask.result!
        //self.performSegue(withIdentifier: "cameraViewSegue", sender: self)
    }
    
    func determineDev(token: String){
        print("Token: " + token)
        let tokenArray = token.split(separator: ".")
        let splitString = String(tokenArray[1].prefix(upTo: String.Index.init(encodedOffset: 500)))
        print("SplitString: " + splitString)
        print("SplitString.length: " + String(splitString.count))
        if let decodedData = Data(base64Encoded: splitString){
            if let decodedString = String(data: decodedData, encoding: .utf8){
                print(decodedString)
                if decodedString.range(of: "\"cognito:groups\":[\"dev\"]") != nil{
                    isDev = true
                } else {
                    print("Isfalse for the RIGHT reasons")
                    isDev = false
                }
            } else {
                print("Isfalse for the WRONG reasons2")
                isDev = false
            }
        } else {
            print("Isfalse for the WRONG reasons1")
            isDev = false
        }
        
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
