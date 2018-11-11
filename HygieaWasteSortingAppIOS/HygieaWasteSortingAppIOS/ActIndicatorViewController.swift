//
//  ActIndicatorViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/19/18.
//  Edited by Theodore Kallaus on 11/4/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit
import AWSCore
import AWSS3

class ActIndicatorViewController: UIViewController {
    
    //Note to self, use XCode's Product -> Clean next time I get the chance to work with this.
    @IBOutlet weak var activity: UIActivityIndicatorView!


    let loginView = LoginViewController()
    let camView = ViewController()
    var resultTxt: String = String()
    var data: Data = Data()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.activity.startAnimating()
        uploadData()
        //self.performSegue(withIdentifier: "ResultViewSegue", sender: nil)
    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func uploadData() {
        
        
        //let data: Data = camView.persImageData! // Data to be uploaded
        
        let expression = AWSS3TransferUtilityUploadExpression()
        expression.progressBlock = {(task, progress) in
            DispatchQueue.main.async(execute: {
                // Do something e.g. Update a progress bar.
            })
        }
        
        var completionHandler: AWSS3TransferUtilityUploadCompletionHandlerBlock?
        completionHandler = { (task, error) -> Void in
            DispatchQueue.main.async(execute: {
                // Do something e.g. Alert a user for transfer completion.
                // On failed uploads, `error` contains the error object.
            })
        }
        
        let transferUtility = AWSS3TransferUtility.default()
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let fileDate = formatter.string(from: Date())
//        let fileString = camView.pressedBtnString + fileDate
//        transferUtility.uploadData(data,
//                                   bucket: "hywz.wastezero",
//                                   key: fileString,
//                                   contentType: "image/jpeg",
//                                   expression: expression,
//                                   completionHandler: completionHandler).continueWith {
//                                    (task) -> AnyObject? in
//                                    if let error = task.error {
//                                        print("Error: \(error.localizedDescription)")
//                                    }
//
//                                    if let _ = task.result {
//                                        self.resultTxt = fileString
                                        self.activity.stopAnimating()
                                        self.performSegue(withIdentifier: "ResultViewSegue", sender: nil)
//                                    }
//                                    return nil;
//        }
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
