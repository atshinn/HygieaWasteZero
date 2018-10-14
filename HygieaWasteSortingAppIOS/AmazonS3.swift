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
