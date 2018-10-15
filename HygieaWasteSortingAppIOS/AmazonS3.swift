//Amazon S3 functions

func uploadData() {
    
    let data: Data = Data()
    
    let expression = AWSS3TransferUtilityUploadExpression()
    expression.progressBlock = {(task, progress) in
        DispatchQueue.main.async(execute: {
            //Wait until done
        })
    }
    
    var completionHandler: AWSS3TransferUtilityUploadCompletionHandlerBlock?
    completionHandler = { (task, error) -> Void in
        DispatchQueue.main.async(execute: {
            ErrorStatement.text = "Error: Not able to complete AWS S3"
        })
    }
    
    let transferUtility = AWSS3TransferUtility.default()
    
    transferUtility.uploadData(data,
       bucket: "HygieaWasteSortingAppIOS",
       //need
       key: "???",
       //need
       contentType: "???",
       expression: expression,
       completionHandler: completionHandler).continueWith {
        (task) -> AnyObject! in
        if let error = task.error {
            print("Error: \(error.localizedDescription)")
        }
        
        if let _ = task.result {
            //Need
        }
        return nil;
    }
}

func downloadData() {
    let expression = AWSS3TransferUtilityDownloadExpression()
    expression.progressBlock = {(task, progress) in DispatchQueue.main.async(execute: {
        //Wait until done
    })
    }
    
    var completionHandler: AWSS3TransferUtilityDownloadCompletionHandlerBlock?
    completionHandler = { (task, URL, data, error) -> Void in
        DispatchQueue.main.async(execute: {
            ErrorStatement.text = "Error: Not able to complete AWS S3"
        })
    }
    
    let transferUtility = AWSS3TransferUtility.default()
    transferUtility.downloadData(
        fromBucket: "HygieaWasteSortingAppIOS",
        //need
        key: "???",
        expression: expression,
        completionHandler: completionHandler
        ).continueWith {
            (task) -> AnyObject! in if let error = task.error {
                print("Error: \(error.localizedDescription)")
            }
            
            if let _ = task.result {
                //need
                
            }
            return nil;
    }
}

let progressView: UIProgressView! = UIProgressView()
progressView.progress = 0.0;

let data = Data()

let expression = AWSS3TransferUtilityUploadExpression()
expression.progressBlock = {(task, progress) in DispatchQueue.main.async(execute: {
    //Wait until done
    progressView.progress = Float(progress.fractionCompleted)
})
}

let completionHandler: AWSS3TransferUtilityUploadCompletionHandlerBlock = { (task, error) -> Void in DispatchQueue.main.async(execute: {
    if let error = error {
        NSLog("Failed with error: \(error)")
    }
    else if(self.progressView.progress != 1.0) {
        NSLog("Error: Failed.")
    } else {
        NSLog("Success.")
    }
})
}

var refUploadTask: AWSS3TransferUtilityTask?
let transferUtility = AWSS3TransferUtility.default()
transferUtility.uploadData(data,
    //need
    key: "???",
    //need
    contentType: "???",
   expression: expression,
   completionHandler: completionHandler).continueWith { (task) -> AnyObject! in
    if let error = task.error {
        print("Error: \(error.localizedDescription)")
    }
    
    if let uploadTask = task.result {
        //need
        refUploadTask = uploadTask
    }
    
    return nil;
}
