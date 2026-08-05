# AWS Debugging

## CloudWatch Logs

### View Logs

```bash
# Get log events
aws logs get-log-events \
  --log-group-name /aws/lambda/my-function \
  --log-stream-name 2024/01/15/[$LATEST]abc123

# Filter logs
aws logs filter-log-events \
  --log-group-name /aws/lambda/my-function \
  --filter-pattern "ERROR"

# Tail logs
aws logs tail /aws/lambda/my-function --follow
```

### Python

```python
import boto3

logs = boto3.client('logs')

# Get log events
response = logs.get_log_events(
    logGroupName='/aws/lambda/my-function',
    logStreamName='2024/01/15/[$LATEST]abc123'
)

# Filter logs
response = logs.filter_log_events(
    logGroupName='/aws/lambda/my-function',
    filterPattern='ERROR'
)
```

## X-Ray Tracing

### Enable X-Ray

```python
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch_all

patch_all()

@xray_recorder.capture('my_function')
def handler(event, context):
    # Your code here
    pass
```

### View Traces

```bash
# Get traces
aws xray get-trace-summaries \
  --start-time 2024-01-15T00:00:00Z \
  --end-time 2024-01-15T23:59:59Z

# Get trace details
aws xray get-trace-summaries --trace-id 1-12345678-abcdef012345678901234567
```

## EC2 Debugging

### Instance Status

```bash
# Check instance status
aws ec2 describe-instance-status --instance-ids i-1234567890abcdef0

# Get console output
aws ec2 get-console-output --instance-id i-1234567890abcdef0

# Get system log
aws ec2 get-console-output --instance-id i-1234567890abcdef0 --output text
```

### Network Debugging

```bash
# Check security groups
aws ec2 describe-security-groups --group-ids sg-12345678

# Check network ACLs
aws ec2 describe-network-acls --filters Name=vpc-id,Values=vpc-12345678

# Check route tables
aws ec2 describe-route-tables --filters Name=vpc-id,Values=vpc-12345678

# Check VPC Flow Logs
aws ec2 describe-flow-logs --filter Name=resource-id,Values=vpc-12345678
```

## Lambda Debugging

### View Function Configuration

```bash
# Get function details
aws lambda get-function --function-name my-function

# Get function configuration
aws lambda get-function-configuration --function-name my-function

# Get recent invocations
aws lambda list-invocations --function-name my-function --max-items 10
```

### Test Function

```bash
# Invoke function
aws lambda invoke \
  --function-name my-function \
  --payload '{"key": "value"}' \
  output.json

# View output
cat output.json
```

## RDS Debugging

### Check Instance Status

```bash
# Get instance status
aws rds describe-db-instances --db-instance-identifier mydb

# Get events
aws rds describe-events --source-identifier mydb --source-type db-instance

# Get logs
aws rds download-db-log-file-contents \
  --db-instance-identifier mydb \
  --log-file-name error/mysql-error-log
```

## S3 Debugging

### Check Bucket

```bash
# List bucket
aws s3 ls s3://my-bucket

# Check bucket policy
aws s3api get-bucket-policy --bucket my-bucket

# Check bucket versioning
aws s3api get-bucket-versioning --bucket my-bucket

# Check bucket encryption
aws s3api get-bucket-encryption --bucket my-bucket
```

## DynamoDB Debugging

### Check Table Status

```bash
# Describe table
aws dynamodb describe-table --table-name my-table

# Check capacity
aws dynamodb describe-table --table-name my-table \
  --query 'Table.ProvisionedThroughput'

# Scan table
aws dynamodb scan --table-name my-table --limit 10
```

## SQS/SNS Debugging

### Check Queue

```bash
# Get queue attributes
aws sqs get-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attribute-names All

# Send test message
aws sqs send-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --message-body "test"
```

## API Gateway Debugging

### Check API

```bash
# Get API
aws apigateway get-rest-api --rest-api-id abc123

# Get stages
aws apigateway get-stages --rest-api-id abc123

# Get logs
aws logs get-log-events \
  --log-group-name API-Gateway-Execution-Logs_abc123/prod \
  --log-stream-name stream-name
```

## CloudFormation Debugging

### Check Stack

```bash
# Describe stack
aws cloudformation describe-stacks --stack-name my-stack

# Get stack events
aws cloudformation describe-stack-events --stack-name my-stack

# Validate template
aws cloudformation validate-template --template-body file://template.yaml
```

## ECS Debugging

### Check Service

```bash
# Describe service
aws ecs describe-services --cluster my-cluster --services my-service

# Get task definition
aws ecs describe-task-definition --task-definition my-task

# Check tasks
aws ecs list-tasks --cluster my-cluster --service-name my-service
```
