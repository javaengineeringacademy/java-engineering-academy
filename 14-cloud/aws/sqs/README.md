# Amazon SQS (Simple Queue Service)

## Overview

Amazon SQS is a fully managed message queuing service enabling you to decouple and scale microservices and distributed systems.

## Queue Types

| Type       | Order    | Exactly-Once | Max Throughput | Use Case           |
|------------|----------|--------------|----------------|--------------------|
| Standard   | Best-effort | No         | Unlimited      | General purpose    |
| FIFO       | Strict   | Yes          | 3,000 msg/s    | Order-critical     |

## Creating Queues

### Standard Queue
```bash
aws sqs create-queue \
  --queue-name my-standard-queue \
  --attributes '{
    "VisibilityTimeout": "30",
    "MessageRetentionPeriod": "345600",
    "ReceiveMessageWaitTimeSeconds": "20"
  }'
```

### FIFO Queue
```bash
aws sqs create-queue \
  --queue-name my-queue.fifo \
  --attributes '{
    "FifoQueue": "true",
    "ContentBasedDeduplication": "true",
    "DeduplicationScope": "messageGroup",
    "FifoThroughputLimit": "perMessageGroupId"
  }'
```

## Sending Messages

```bash
# Send standard message
aws sqs send-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --message-body "Hello World"

# Send FIFO message
aws sqs send-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue.fifo \
  --message-body "Hello World" \
  --message-group-id "order-123" \
  --message-deduplication-id "dedup-1"

# Send message with attributes
aws sqs send-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --message-body '{"orderId": "123", "status": "pending"}' \
  --message-attributes '{
    "Priority": {
      "DataType": "String",
      "StringValue": "high"
    }
  }'
```

## Receiving Messages

```bash
# Receive messages
aws sqs receive-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --max-number-of-messages 10 \
  --wait-time-seconds 20 \
  --message-attribute-names All

# Delete message after processing
aws sqs delete-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --receipt-handle "message-receipt-handle"
```

## Dead Letter Queues (DLQ)

```bash
# Create DLQ
aws sqs create-queue --queue-name my-dlq

# Configure redrive policy
aws sqs set-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attributes '{
    "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:123456789012:my-dlq\",\"maxReceiveCount\":\"5\"}"
  }'
```

### DLQ Flow
```
Message → Queue → Consumer Fails → Retry (5x) → DLQ
```

## Visibility Timeout

```
Message received → Invisible for 30s → Processed → Delete
                                     ↓
                              Timeout → Reappear → Re-receive
```

```bash
# Change visibility timeout
aws sqs change-message-visibility \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --receipt-handle "message-receipt-handle" \
  --visibility-timeout 60
```

## Message Retention

| Duration     | Default | Max      |
|--------------|---------|----------|
| Standard     | 4 days  | 14 days  |
| FIFO         | 4 days  | 14 days  |

```bash
# Set retention period
aws sqs set-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attributes '{"MessageRetentionPeriod": "1209600"}'
```

## Batch Operations

```bash
# Send batch messages
aws sqs send-message-batch \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --entries '[
    {
      "Id": "msg1",
      "MessageBody": "Message 1"
    },
    {
      "Id": "msg2",
      "MessageBody": "Message 2"
    }
  ]'

# Delete batch messages
aws sqs delete-message-batch \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --entries '[
    {
      "Id": "msg1",
      "ReceiptHandle": "handle1"
    }
  ]'
```

## Queue Policies

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "arn:aws:sqs:us-east-1:123456789012:my-queue",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "arn:aws:s3:::my-bucket"
        }
      }
    }
  ]
}
```

## Long Polling

```bash
# Enable long polling
aws sqs receive-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --wait-time-seconds 20

# Set default long polling
aws sqs set-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attributes '{"ReceiveMessageWaitTimeSeconds": "20"}'
```

## Server-Side Encryption (SSE)

```bash
# Enable SSE with SQS-managed key
aws sqs set-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attributes '{"SqsManagedSseEnabled": "true"}'

# Enable SSE with KMS
aws sqs set-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attributes '{
    "KmsMasterKeyId": "alias/my-key",
    "KmsDataKeyReusePeriodSeconds": "300"
  }'
```

## AWS Lambda Trigger

```python
# Lambda function triggered by SQS
import json

def lambda_handler(event, context):
    for record in event['Records']:
        body = json.loads(record['body'])
        # Process message
        print(f"Processing: {body}")
        
    return {'batchItemFailures': []}
```

## Cost Optimization

- **Long polling** reduces empty responses
- **Batch operations** reduce API calls
- **Use standard queues** when order doesn't matter
- **Delete messages** immediately after processing

## Best Practices

1. **Use DLQs** for failed messages
2. **Implement idempotency** in consumers
3. **Use long polling** to reduce costs
4. **Set appropriate visibility timeout**
5. **Use batch operations** for efficiency
6. **Monitor queue depth** with CloudWatch
7. **Use FIFO queues** for order-critical messages
8. **Implement message retention** policies
9. **Use VPC endpoints** for private access
10. **Encrypt messages** at rest
