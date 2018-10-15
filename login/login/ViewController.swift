
import Foundation
import UIKit
import CoreLocation
//import AWSCognitoIdentityProvider.h


class ViewController: UIViewController, CLLocationManagerDelegate, UIWebViewDelegate {
    
    @IBOutlet weak var webView: UIWebView!
    
    
        //LocationManager base
        let locManager = CLLocationManager()
        
        //String Inputs
        var username: String = ""
        var password: String = ""
        
    
    
        override func viewDidLoad() {
            super.viewDidLoad()
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
    
        
}
