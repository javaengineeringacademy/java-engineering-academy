# Amazon SNS (Simple Notification Service)

## Overview

Amazon SNS is a fully managed pub/sub messaging service for application-to-application (A2A) and application-to-person (A2P) communication.

## Message Patterns

| Pattern        | Description                    | Use Case              |
|----------------|--------------------------------|-----------------------|
| Pub/Sub        | One message, many subscribers  | Event notifications   |
| Fan-Out        | SNS → SQS → Multiple consumers | Parallel processing   |
| Point-to-Point | Direct message delivery        | Simple notifications  |

## Creating Topics

```bash
# Create standard topic
aws sns create-topic --name my-topic

# Create FIFO topic
aws sns create-topic \
  --name my-topic.fifo \
  --attributes '{"FifoTopic": "true", "ContentBasedDeduplication": "true"}'
```

## Subscriptions

### Email Subscription
```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol email \
  --notification-endpoint user@example.com
```

### SQS Subscription
```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol sqs \
  --notification-endpoint arn:aws:sqs:us-east-1:123456789012:my-queue
```

### Lambda Subscription
```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol lambda \
  --notification-endpoint arn:aws:lambda:us-east-1:123456789012:function:my-function
```

### HTTP Subscription
```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol https \
  --notification-endpoint https://example.com/webhook
```

## Publishing Messages

```bash
# Publish standard message
aws sns publish \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --subject "Alert" \
  --message "Something happened"

# Publish with message attributes
aws sns publish \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --message '{"orderId": "123"}' \
  --message-attributes '{
    "EventType": {
      "DataType": "String",
      "StringValue": "order"
    }
  }'

# Publish FIFO message
aws sns publish \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic.fifo \
  --message "Order update" \
  --message-group-id "order-123" \
  --message-deduplication-id "update-1"
```

## Fan-Out Pattern

```
                    ┌──→ SQS Queue 1 ──→ Consumer 1
                    │
SNS Topic ─────────┼──→ SQS Queue 2 ──→ Consumer 2
                    │
                    └──→ SQS Queue 3 ──→ Consumer 3
```

### Benefits
- **Decoupled architecture**
- **Parallel processing**
- **Independent scaling**
- **Failure isolation**

## Message Filtering

```bash
# Create subscription with filter
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol sqs \
  --notification-endpoint arn:aws:sqs:us-east-1:123456789012:my-queue \
  --attributes '{
    "FilterPolicy": "{\"eventType\": [\"order.created\"]}"
  }'

# Filter with string matching
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789012:my-topic \
  --protocol sqs \
  --notification-endpoint arn:aws:sqs:us-east-1:123456789012:my-queue \
  --attributes '{
    "FilterPolicy": "{\"store\": [\"example_corp\"], \"event\": [\"order.placed\"]}"
  }'
```

## Topic Policies

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sns:Publish",
      "Resource": "arn:aws:sns:us-east-1:123456789012:my-topic",
      "Condition": {
        "ArnLike": {
          "aws:SourceArn": "arn:aws:s3:::my-bucket"
        }
      }
    }
  ]
}
```

## SMS Notifications

```bash
# Set SMS preferences
aws sns set-sms-attributes \
  --attributes '{
    "DefaultSMSType": "Transactional",
    "DefaultSenderID": "MyApp"
  }'

# Send SMS
aws sns publish \
  --phone-number +1234567890 \
  --message "Hello from SNS"
```

## Platform Applications (Push Notifications)

```bash
# Create platform application
aws sns create-platform-application \
  --name my-app \
  --platform GCM \
  --attributes '{
    "PlatformCredential": "your-api-key"
  }'

# Create platform endpoint
aws sns create-platform-endpoint \
  --platform-application-arn arn:aws:sns:us-east-1:123456789012:app/GCM/my-app \
  --token device-token-12345678

# Send push notification
aws sns publish \
  --target-arn arn:aws:sns:us-east-1:123456789012:app/GCM/my-app/device-token \
  --message "New notification" \
  --message-structure json \
  --message-attributes '{
    "GCM": {
      "DataType": "String",
      "StringValue": "{\"notification\": {\"title\": \"Alert\", \"body\": \"New message\"}}"
    }
  }'
```

## FIFO Topics

### Features
- **Strict ordering** of messages
- **Exactly-once delivery**
- **Message deduplication**
- **Same-region only**

### Use Cases
- Order processing
- Financial transactions
- Sequenced event processing

## SNS → Lambda Trigger

```python
import json

def lambda_handler(event, context):
    for record in event['Records']:
        sns_message = record['Sns']
        message = json.loads(sns_message['Message'])
        
        # Process message
        print(f"Received: {message}")
```

## Cost Optimization

- **Standard topics**: $0.50/million requests
- **FIFO topics**: $0.50/million + $0.50/million for deduplication
- **Delivery**: Varies by protocol
- Use **message filtering** to reduce unnecessary delivery
- Use **SNS batch** operations when possible

## Best Practices

1. **Use fan-out pattern** for parallel processing
2. **Implement message filtering** to reduce noise
3. **Use FIFO topics** for order-critical messages
4. **Set up DLQs** for failed deliveries
5. **Encrypt messages** at rest
6. **Use VPC endpoints** for private access
7. **Monitor delivery** with CloudWatch
8. **Implement retry logic** in subscribers
9. **Use message attributes** for routing
10. **Set appropriate retention** periods
