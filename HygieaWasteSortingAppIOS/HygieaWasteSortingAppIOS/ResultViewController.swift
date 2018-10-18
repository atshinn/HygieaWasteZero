//
//  ResultViewController.swift
//  HygieaWasteSortingAppIOS
//
//  Created by Hesham Alghamdi on 9/20/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//

import UIKit

class ResultViewController: UIViewController {

    @IBAction func BackToCamera(_ sender: Any) {
        performSegue(withIdentifier: "BackToCameraSegue" , sender: nil)
    }
    
    
    @IBOutlet weak var ResultsLabel: UILabel!
    
    @IBOutlet weak var InstructionScrollView: UIScrollView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

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