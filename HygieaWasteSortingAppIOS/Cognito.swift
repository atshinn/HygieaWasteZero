import AWSCore
import AWSCognito

let credentialsProvider = AWSCognitoCredentialsProvider(regionType: .USEast1, identityPoolId: "us-west-2_2KW8CF0tm")
let configuration = AWSServiceConfiguration(region: .USWest2, credentialsProvider: credentialsProvider)
AWSServiceManager.default().defaultServiceConfiguration = configuration

let token = FBSDKAccessToken.currentAccessToken().tokenString
credentialsProvider.logins = [AWSCognitoLoginProviderKey.Facebook.rawValue: token]

credentialsProvider.getIdentityId().continueWith(block: { (task) -> AnyObject? in
    if (task.error != nil) {
        print("Error: " + task.error!.localizedDescription)
    }
    else {
        let cognitoId = task.result!
        print("Cognito id: \(cognitoId)")
    }
    return task;
})

