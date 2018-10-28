import Foundation
import AWSCore
import AWSCognitoIdentityProvider
class idProvider: NSObject, AWSIdentityProviderManager{
    var tokens : NSDictionary?
    init(tokens: NSDictionary) {
        self.tokens = tokens
    }
    @objc func logins() -> AWSTask<NSDictionary> {
        print("Setting Logins...")
        return AWSTask(result: tokens)
    }
    
}
