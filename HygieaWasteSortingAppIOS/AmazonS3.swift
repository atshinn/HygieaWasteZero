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
