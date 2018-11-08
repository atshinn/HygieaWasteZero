import AWSDynamoDB

Amazon DynamoDB = AWSDynamoDB.default()
let updateInput = AWSDynamoDBUpdateItemInput()

let compostValue = AWSDynamoDBAttributeValue()
compostValue?.s = Compost_ID

let recycleValue = AWSDynamoDBAttributeValue()
recycleValue?.s = Recycle_ID

let unlabeledValue = AWSDynamoDBAttributeValue()
unlabeledValue?.s = Unlabeled_ID


