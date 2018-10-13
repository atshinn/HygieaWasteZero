//
//
//import UIKit
//
//class SignUpViewController: UIViewController {
//
//    //String Inputs
//    var name: String = ""
//    var username: String = ""
//    var email: String = ""
//    var password: String = ""
//    var repeatPassword: String = ""
//
//    //Array
//    let symbols = "QqWwEeRrTtYyUuIiOoPpAaSsDdFfGgHhJjKkLlZzXxCcVvBbNnMm0123456789-_`. "
//
////    //Used for outputing an error statement for user
////    @IBOutlet var ErrorStatement: UILabel!
////
////    //Name input
////    @IBOutlet var takeName: UITextField!
////
////    //Username input
////    @IBOutlet var takeUsername: UITextField!
////
////    //Email input
////    @IBOutlet var takeEmail: UITextField!
////
////    //Password and Password check input
////    @IBOutlet var takePassword: UITextField!
////
////    @IBOutlet var takeRepeatPassword: UITextField!
////
////    //Check to make sure the inputs are correct
////    @IBAction func Register(sender: ViewController) {
////
////        //Take Inputs
////        name = takeName.text!
////        username = takeUsername.text!
////        email = takeEmail.text!
////        password = takePassword.text!
////        repeatPassword = takeRepeatPassword.text!
////
////        //Flag to see if username and password can be accepted
////        var flag: Bool = true
////
////        //Check if passwords match
////        if password != repeatPassword {
////            flag = false
////            ErrorStatement.text = "Passwords do not match"
////        }
////
////        //Checks length of username
////        if username.count < 8 {
////            if username.count > 16 {
////                flag = false
////                ErrorStatement.text = "Username must be between 8 and 16 characters"
////            }
////        }
////
////        //Checks length of password
////        if password.count < 8 {
////            if password.count > 16 {
////                flag = false
////                ErrorStatement.text = "Password must be between 8 and 16 characters"
////            }
////        }
////
////        //Checks that username is the correct symbols
////        for i in username {
////
////            for j in symbols {
////                if i == j {
////                    break
////                }
////                else {
////                    flag = false
////                    ErrorStatement.text = "Username containing incorrect symbols. Please only use Letters, Numbers, Spaces, and the following symbols: '-', '_', '`, '."
////                }
////            }
////        }
////
////        //Checks that password is the correct symbols
////        for i in password {
////
////            for j in symbols {
////                if i == j {
////                    break
////                }
////                else {
////                    flag = false
////                    ErrorStatement.text = "Password containing incorrect symbols. Please only use Letters, Numbers, Spaces, and the following symbols: '-', '_', '`, '."
////                }
////            }
////        }
////
////        if flag == true {
////            //let jsonObject: NSDictionary = [
////            //    "name": name,
////            //    "username": username,
////            //    "password": password,
////            //    "email": email
////            //]
////
////            //struct data {
////            //    let nam = name.self
////            //    let usernam = username
////            //    let passwor = password
////            //    let emai = email
////            //}
////
////            struct jsonData: Codable {
////                let name: String
////                let username: String
////                let email: String
////                let password: String
////                let repeatPassword: String
////            }
////
////            enum CodingKeys: String {
////                case name
////                case username
////                case email
////                case password
////            }
////
////            let encoder = JSONEncoder()
////
////            //let valid = JSONSerialization.isValidJSONObject(jsonData)
////        }
////
////    }
////
////    @IBAction func jumpToLogin(_ sender: LoginViewController) {
////        //jump to Login
////    }
////
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        // Do any additional setup after loading the view.
//    }
//
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//
//
//    /*
//     // MARK: - Navigation
//
//     // In a storyboard-based application, you will often want to do a little preparation before navigation
//     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//     // Get the new view controller using segue.destinationViewController.
//     // Pass the selected object to the new view controller.
//     }
//     */
//
//}
