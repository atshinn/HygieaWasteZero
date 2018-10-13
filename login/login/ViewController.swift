
import Foundation
import UIKit
import CoreLocation
//import AWSCognitoIdentityProvider.h


class ViewController: UIViewController, CLLocationManagerDelegate {
    
    @IBOutlet weak var webView: UIWebView!
    
   
    
    
    
        //LocationManager base
        let locManager = CLLocationManager()
        
        //String Inputs
        var username: String = ""
        var password: String = ""
        
       // @IBOutlet var ErrorStatement: UILabel!
        
  
        
    @IBOutlet weak var takeUsername: UITextField!
    
    @IBOutlet weak var takePassword: UITextField!
    
    @IBAction func checkLogin(_ sender: ViewController) {
    
        
        
        
//            //Take Inputs
//            username = takeUsername.text!
//            password = takePassword.text!
//
//            //Flag to see if username and password are correct
//            var flag: Bool = false
//
//            //Info from Json
//            struct StoredData {
//                var name: String
//                var username: String
//                var email: String
//                var password: String
//            }
//
//            struct jsonData: Codable {
//                let name: String
//                let username: String
//                let email: String
//                let password: String
//            }
//
//            let data = try! JSONSerialization.data(withJSONObject: StoredData.self, options: [])
//            let decoder = JSONDecoder()
//            let stored = try! decoder.decode(jsonData.self, from: data)
//
//            if username == stored.username {
//                if password == stored.password {
//                    flag = true
//                }
//            }
//            
//            if flag == true {
//                //Successful login
//            }
//            else {
//                ErrorStatement.text = "Incorrect Username or Password"
//            }
//
        }
        
//        @IBAction func jumpToSignUp(_ sender: SignUpViewController) {
//            //Jump to Signup
//        }
//
//        @IBAction func jumpToGuest(_ sender: ViewController) {
//            //Jump to Camera for now
//        }
    
        
        override func viewDidLoad() {
            super.viewDidLoad()
            
            // Do any additional setup after loading the view.
            locManager.requestWhenInUseAuthorization()
            locManager.requestAlwaysAuthorization()
            startReceivingLocation()
            let requestURL = URL(string:"https://hywz-auth.auth.us-west-2.amazoncognito.com/login?response_type=code&client_id=7iea4023volnd3pfdp7jstdg61&redirect_uri=hygieawastezeroios://")
            let request = URLRequest(url: requestURL!)
             webView.loadRequest(request)
        }
    
    func asynchronousURLPoll(completion: @escaping (Error?) -> Void) {
        while !(webView.request?.url?.absoluteString.hasPrefix("hygieawastezeroios://"))! {
            
        }
        completion(URLParse) // Or completion(SomeError.veryBadError)
    }
    
    func URLParse() {
    if let error = error {
    print("Oops! Something went wrong...")
    } else {
   let toSearch = webView.request.URL.absoluteString.components(separatedBy: "#id_token=")[1].components(separatedBy: "&")[0]
    
    }
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
