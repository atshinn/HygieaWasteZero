//
//  ResultViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/20/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class ResultViewController: UIViewController {
    
    
    @IBOutlet weak var viewText: UILabel!
    var loadView = ActIndicatorViewController()
    
    @IBAction func SendToCompost(_ sender: UIButton) {
        //sends image to compost folder
    }
    
    @IBAction func SendToRecycle(_ sender: UIButton) {
        //sends image to recycle folder
    }
    
    
    @IBAction func BackToCamera(_ sender: Any) {
        performSegue(withIdentifier: "BackToCameraSegue" , sender: nil)
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.viewText.text = loadView.resultTxt
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
