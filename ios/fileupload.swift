func myImageUploadRequest()
    {
      
        let url = NSURL(string: "http://www.swiftdeveloperblog.com/http-post-example-script/");
        let req = NSMutableURLRequest(URL:url!);
        req.HTTPMethod = "POST";
        
        let params = 
        [
            "firstName"  : "Sergey",
            "lastName"    : "Kargopolov",
            "userId"    : "9"
        ]
        
        let boundary = generateBoundaryString()
        
        req.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
         
        let img = UIImageJPEGRepresentation(myImageView.image!, 1)
        
        if(img==nil)
        {
         return; 
        }
        
        req.HTTPBody = createBodyWithParameters(params, filePathKey: "file", imgDataKey: img!, boundary: boundary)
        
        let task = NSURLSession.sharedSession().dataTaskWithRequest(req) {
            data, res, error in
            
            if error != nil {
                print("error=\(error)")
                return;
            }
            
            do {
                let json = try NSJSONSerialization.JSONObjectWithData(data!, options: []) as? NSDictionary
                
                dispatch_async(dispatch_get_main_queue(),{
                });
                
            }catch
            {
                print(error)
            }
            
        }
        
        task.resume()
    }