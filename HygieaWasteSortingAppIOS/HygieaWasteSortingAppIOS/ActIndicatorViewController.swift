//
//  ActIndicatorViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/19/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class ActIndicatorViewController: UIViewController {
    
    
    @IBOutlet weak var activity: UIActivityIndicatorView!
    //Functions that might be used later
    //    var activityIndicator: UIActivityIndicatorView = UIActivityIndicatorView()
    //    func loading(){
    //
    //        activityIndicator.center = self.view.center
    //        activityIndicator.hidesWhenStopped = true
    //        activityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
    //        view.addSubview(activityIndicator)
    //        activityIndicator.startAnimating()
    //    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //activity.stopAnimating()
        //loading()
        
        // Do any additional setup after loading the view.
    }
//    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//        performSegue(withIdentifier: "ResultSegue" , sender: nil)
//    }
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
