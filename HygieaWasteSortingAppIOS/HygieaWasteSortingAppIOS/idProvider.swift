import Foundation
import AWSCore
import AWSCognitoIdentityProvider
class idProvider: NSObject, AWSIdentityProviderManager{
    var tokens : [NSString : NSString]?
    init(tokens: [NSString : NSString]) {
        self.tokens = tokens as NSDictionary as! [NSString : NSString]
    }
    @objc func logins() -> AWSTask<NSDictionary> {
        return AWSTask(result: tokens)
    }
    
}
