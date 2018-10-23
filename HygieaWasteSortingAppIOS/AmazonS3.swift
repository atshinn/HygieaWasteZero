//Amazon S3 functions

let S3BucketName: String = "hywz.wastezero"

func uploadDataCompost() {
    
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
       bucket: S3BucketName,
       key: "compost",
       contentType: "hywz.wastezero/compost",
       expression: expression,
       completionHandler: completionHandler).continueWith {
        (task) -> AnyObject! in
        if let error = task.error {
            print("Error: \(error.localizedDescription)")
        }
        
        if let _ = task.result {
            refDownloadTask = downloadTask
            let url = AWSS3.default().configuration.endpoint.url
            let publicURL = url?.appendingPathComponent(downloadRequest.bucket!).appendingPathComponent(downloadRequest.key!)
            print("Downloaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/compost/?region=us-west-2&tab=overview)")
        }
        return nil;
    }
}

func uploadDataRecycle() {
    
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
        bucket: S3BucketName,
        key: "recycle",
        contentType: "hywz.wastezero/recycle",
        expression: expression,
        completionHandler: completionHandler).continueWith {
            (task) -> AnyObject! in
            if let error = task.error {
                print("Error: \(error.localizedDescription)")
            }
            
            if let _ = task.result {
                refDownloadTask = downloadTask
                let url = AWSS3.default().configuration.endpoint.url
                let publicURL = url?.appendingPathComponent(downloadRequest.bucket!).appendingPathComponent(downloadRequest.key!)
                print("Downloaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/recycle/?region=us-west-2&tab=overview)")
            }
            return nil;
    }
}

func downloadDataCompost() {
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
        fromBucket: S3BucketName,
        key: "compost",
        expression: expression,
        completionHandler: completionHandler
        ).continueWith {
            (task) -> AnyObject! in if let error = task.error {
                print("Error: \(error.localizedDescription)")
            }
            
            if let _ = task.result {
                refDownloadTask = downloadTask
                let url = AWSS3.default().configuration.endpoint.url
                let publicURL = url?.appendingPathComponent(downloadRequest.bucket!).appendingPathComponent(downloadRequest.key!)
                print("Downloaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/compost/?region=us-west-2&tab=overview)")
            }
            return nil;
    }
}

func downloadDataRecycle() {
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
        fromBucket: S3BucketName,
        key: "recycle",
        expression: expression,
        completionHandler: completionHandler
        ).continueWith {
            (task) -> AnyObject! in if let error = task.error {
                print("Error: \(error.localizedDescription)")
            }
            
            if let _ = task.result {
                refDownloadTask = downloadTask
                let url = AWSS3.default().configuration.endpoint.url
                let publicURL = url?.appendingPathComponent(downloadRequest.bucket!).appendingPathComponent(downloadRequest.key!)
                print("Downloaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/recycle/?region=us-west-2&tab=overview)")
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
   key: "compost",
   contentType: "hywz.wastezero/compost",
   expression: expression,
   completionHandler: completionHandler).continueWith { (task) -> AnyObject! in
    if let error = task.error {
        print("Error: \(error.localizedDescription)")
    }
    
    if let uploadTask = task.result {
        refUploadTask = uploadTask
        let url = AWSS3.default().configuration.endpoint.url
        let publicURL = url?.appendingPathComponent(uploadRequest.bucket!).appendingPathComponent(uploadRequest.key!)
        print("Uploaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/compost/?region=us-west-2&tab=overview)")
    }
    
    return nil;
    
    key: "recycle",
    contentType: "hywz.wastezero/recycle",
    expression: expression,
    completionHandler: completionHandler).continueWith { (task) -> AnyObject! in
        if let error = task.error {
            print("Error: \(error.localizedDescription)")
        }
        
        if let uploadTask = task.result {
            refUploadTask = uploadTask
            let url = AWSS3.default().configuration.endpoint.url
            let publicURL = url?.appendingPathComponent(uploadRequest.bucket!).appendingPathComponent(uploadRequest.key!)
            print("Uploaded to:\(https://s3.console.aws.amazon.com/s3/buckets/hywz.wastezero/recycle/?region=us-west-2&tab=overview)")
        }
        
        return nil;
}
