# AWS Lambda

## Overview

AWS Lambda is a serverless compute service that runs code without provisioning or managing servers.

## Runtime Support

| Runtime        | Version   | Use Case              |
|----------------|-----------|------------------------|
| Python         | 3.12      | General purpose        |
| Node.js        | 20.x      | JavaScript apps        |
| Java           | 21        | Enterprise apps        |
| Go             | 1.22      | High performance       |
| .NET           | 8         | Microsoft ecosystem    |
| Ruby           | 3.3       | Scripting              |
| Rust           | -         | Performance-critical   |
| Custom Runtime | Any       | Any language           |

## Function Configuration

```bash
# Create function
aws lambda create-function \
  --function-name MyFunction \
  --runtime python3.12 \
  --role arn:aws:iam::123456789012:role/lambda-role \
  --handler lambda_function.lambda_handler \
  --zip-file fileb://function.zip

# Update function code
aws lambda update-function-code \
  --function-name MyFunction \
  --zip-file fileb://function.zip
```

## Triggers

| Service        | Event Type                   | Use Case              |
|----------------|------------------------------|-----------------------|
| API Gateway    | HTTP request                 | REST API              |
| S3             | Object create/delete         | File processing       |
| DynamoDB       | Stream events                | Real-time processing  |
| SQS            | Message received             | Queue processing      |
| SNS            | Message published            | Fan-out               |
| CloudWatch     | Scheduled events             | Cron jobs             |
| Kinesis        | Stream records               | Real-time analytics   |
| IoT            | MQTT messages                | IoT processing        |
| Alexa          | Voice intent                 | Voice apps            |

### S3 Trigger Example
```python
import json
import boto3

def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key']
        
        s3 = boto3.client('s3')
        s3.download_file(bucket, key, f'/tmp/{key}')
        
        # Process file...
        
        return {'statusCode': 200, 'body': json.dumps('Processed')}
```

### DynamoDB Stream Trigger
```python
import json
import boto3

def lambda_handler(event, context):
    for record in event['Records']:
        if record['eventName'] == 'INSERT':
            new_image = record['dynamodb']['NewImage']
            # Process new item
        elif record['eventName'] == 'MODIFY':
            old_image = record['dynamodb']['OldImage']
            new_image = record['dynamodb']['NewImage']
            # Process change
        elif record['eventName'] == 'REMOVE':
            old_image = record['dynamodb']['OldImage']
            # Process deletion
```

## Layers

```bash
# Create layer
aws lambda publish-layer-version \
  --layer-name my-layer \
  --zip-file fileb://layer.zip \
  --compatible-runtimes python3.12

# Use layer in function
aws lambda update-function-configuration \
  --function-name MyFunction \
  --layers arn:aws:lambda:us-east-1:123456789012:layer:my-layer:1
```

### Common Layers
- **AWS Lambda PowerTools**: Observability, utilities
- **Pillow**: Image processing
- **NumPy/Pandas**: Data processing
- **Auth**: Custom authentication libraries

## Cold Start Optimization

### What is Cold Start?
```
Request → Lambda Service → Container Init → Runtime Init → Handler → Response
           (1-10s)        (0.5-2s)        (0.5-1s)      (0-∞)
```

### Optimization Strategies
1. **Reduce deployment package size**
2. **Use provisioned concurrency**
3. **Optimize initialization code**
4. **Keep function warm with scheduled events**
5. **Use Lambda SnapStart (Java)**

### Provisioned Concurrency
```bash
# Enable provisioned concurrency
aws lambda put-provisioned-concurrency-config \
  --function-name MyFunction \
  --qualifier live \
  --provisioned-concurrent-executions 10
```

## Memory & Duration

### Memory Configuration
```
128 MB   → 0.08 vCPU
256 MB   → 0.17 vCPU
512 MB   → 0.33 vCPU
1024 MB  → 0.67 vCPU
1769 MB  → 1 vCPU (full)
```

### Timeout Settings
- **Default**: 3 seconds
- **Maximum**: 900 seconds (15 minutes)
- **Best practice**: Set appropriate timeout for function

## Lambda@Edge

```javascript
// CloudFront origin request trigger
exports.handler = async (event) => {
    const request = event.Records[0].cf.request;
    const headers = request.headers;
    
    // Add custom header
    headers['x-custom-header'] = [{ key: 'X-Custom', value: 'value' }];
    
    return request;
};
```

## Event Source Mappings

### SQS Trigger Configuration
```python
# Configuration
{
    "EventSourceArn": "arn:aws:sqs:us-east-1:123456789012:my-queue",
    "Enabled": True,
    "BatchSize": 10,
    "MaximumBatchingWindowInSeconds": 5,
    "ScalingConfig": {
        "MaximumConcurrency": 10
    }
}
```

## Async Lambda

```python
# Async invocation
lambda_client = boto3.client('lambda')
response = lambda_client.invoke(
    FunctionName='MyFunction',
    InvocationType='Event',  # Async
    Payload=json.dumps({'key': 'value'})
)

# With DLQ
# Configure on Lambda function or SQS trigger
```

## Lambda Destinations

```bash
# Configure destination for async invocations
aws lambda put-function-event-invoke-config \
  --function-name MyFunction \
  --maximum-retry-attempts 2 \
  --destination-config '{
    "OnSuccess": {
      "Destination": "arn:aws:sqs:us-east-1:123456789012:my-queue"
    },
    "OnFailure": {
      "Destination": "arn:aws:sqs:us-east-1:123456789012:my-dlq"
    }
  }'
```

## Step Functions Integration

```python
import json

def lambda_handler(event, context):
    # Process event
    result = {
        'statusCode': 200,
        'result': {'key': 'value'}
    }
    return result

# Step Functions will handle the state machine
```

## Lambda SnapStart (Java)

```bash
# Enable SnapStart
aws lambda publish-version \
  --function-name MyFunction \
  --snap-start '{"ApplyOn": "PublishedVersions"}'
```

**Benefits**:
- Reduces cold start by up to 10x
- No additional cost
- Java 11+ only

## Concurrency Controls

### Reserved Concurrency
```bash
# Set reserved concurrency
aws lambda put-function-concurrency \
  --function-name MyFunction \
  --reserved-concurrent-executions 100
```

### Account Limits
| Region          | Default Limit |
|-----------------|---------------|
| us-east-1       | 1,000         |
| eu-west-1       | 1,000         |
| ap-southeast-1  | 1,000         |

## VPC Access

```bash
# Configure VPC access
aws lambda update-function-configuration \
  --function-name MyFunction \
  --vpc-config '{
    "SubnetIds": ["subnet-12345678"],
    "SecurityGroupIds": ["sg-12345678"]
  }'
```

## Monitoring

```bash
# Get function logs
aws logs filter-log-events \
  --log-group-name /aws/lambda/MyFunction \
  --start-time $(date -d '1 hour ago' +%s)000

# Get function metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/Lambda \
  --metric-name Duration \
  --dimensions Name=FunctionName,Value=MyFunction \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Average
```

## Best Practices

1. **Minimize deployment package** - Remove unnecessary files
2. **Use layers** for shared dependencies
3. **Implement idempotency** - Handle duplicate invocations
4. **Set appropriate timeout** - Don't use default 3s
5. **Use environment variables** for configuration
6. **Implement error handling** with retries and DLQ
7. **Monitor with CloudWatch** - Set alarms for errors
8. **Use provisioned concurrency** for latency-sensitive apps
9. **Optimize memory** - More memory = more CPU
10. **Keep functions warm** - Avoid cold starts

## Common Patterns

- **Fan-out/fan-in**: Use SQS + Lambda
- **Event processing**: Use EventBridge
- **File processing**: S3 + Lambda
- **Real-time data**: Kinesis + Lambda
- **Scheduled tasks**: EventBridge Scheduler
- **API backend**: API Gateway + Lambda
