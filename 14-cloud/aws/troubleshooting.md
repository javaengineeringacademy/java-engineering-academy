# AWS Troubleshooting

## EC2 Instance Issues

### Instance Won't Start

**Symptoms**: Instance stuck in pending state or fails to launch.

**Causes**:
- Insufficient instance limits
- VPC configuration issues
- Security group misconfiguration
- EBS volume issues

**Solutions**:
```bash
# Check instance limits
aws service-quotas get-service-quota \
  --service-code ec2 \
  --quota-code L-1216C47A

# Check instance status
aws ec2 describe-instance-status --instance-ids i-1234567890abcdef0

# Check console output
aws ec2 get-console-output --instance-id i-1234567890abcdef0
```

### Instance Can't Connect

**Symptoms**: Cannot SSH to instance or instance unreachable.

**Causes**:
- Security group rules
- Network ACLs
- Incorrect key pair
- Instance not in public subnet

**Solutions**:
```bash
# Check security group
aws ec2 describe-security-groups --group-ids sg-12345678

# Check instance state
aws ec2 describe-instances --instance-ids i-1234567890abcdef0

# Check VPC settings
aws ec2 describe-subnets --filters Name=vpc-id,Values=vpc-12345678
```

## RDS Connection Issues

### Cannot Connect to Database

**Symptoms**: Application cannot connect to RDS instance.

**Causes**:
- Security group rules
- VPC configuration
- Database credentials
- Endpoint incorrect

**Solutions**:
```bash
# Check DB instance status
aws rds describe-db-instances --db-instance-identifier mydb

# Check security groups
aws rds describe-db-instances --db-instance-identifier mydb \
  --query 'DBInstances[*].VpcSecurityGroups'

# Test connectivity
telnet mydb.xxxx.us-east-1.rds.amazonaws.com 3306
```

## Lambda Function Issues

### Function Timeout

**Symptoms**: Function times out after 3 seconds.

**Causes**:
- Insufficient timeout setting
- Cold starts
- External API latency
- Memory allocation

**Solutions**:
```python
# Increase timeout
lambda_client.update_function_configuration(
    FunctionName='my-function',
    Timeout=30
)

# Increase memory
lambda_client.update_function_configuration(
    FunctionName='my-function',
    MemorySize=1024
)
```

### Function Permission Errors

**Symptoms**: Function cannot access other AWS services.

**Causes**:
- Missing IAM role permissions
- Resource-based policy missing
- VPC configuration

**Solutions**:
```bash
# Check function role
aws lambda get-function-configuration --function-name my-function \
  --query 'Role'

# Check role permissions
aws iam list-attached-role-policies --role-name my-role
```

## S3 Access Issues

### Access Denied

**Symptoms**: Cannot access S3 bucket or objects.

**Causes**:
- Bucket policy
- IAM permissions
- Block public access settings
- ACLs

**Solutions**:
```bash
# Check bucket policy
aws s3api get-bucket-policy --bucket my-bucket

# Check block public access
aws s3api get-public-access-block --bucket my-bucket

# Check IAM permissions
aws iam simulate-principal-policy \
  --policy-source-arn arn:aws:iam::123456789012:role/my-role \
  --action-names s3:GetObject \
  --resource-arns arn:aws:s3:::my-bucket/*
```

## VPC and Networking Issues

### Cannot Reach Internet

**Symptoms**: Instances in private subnet cannot access internet.

**Causes**:
- Missing NAT Gateway
- Route table configuration
- Internet Gateway

**Solutions**:
```bash
# Check route tables
aws ec2 describe-route-tables --filters Name=vpc-id,Values=vpc-12345678

# Check NAT Gateway
aws ec2 describe-nat-gateways --filter Name=vpc-id,Values=vpc-12345678

# Check Internet Gateway
aws ec2 describe-internet-gateways --filters Name=attachment.vpc-id,Values=vpc-12345678
```

### Cross-VPC Connectivity Issues

**Symptoms**: Cannot communicate between VPCs.

**Causes**:
- VPC peering not configured
- Route tables missing
- Security groups blocking

**Solutions**:
```bash
# Check VPC peering
aws ec2 describe-vpc-peering-connections

# Check routes
aws ec2 describe-route-tables --filters Name=vpc-id,Values=vpc-12345678
```

## CloudFormation Issues

### Stack Creation Fails

**Symptoms**: Stack rolls back during creation.

**Causes**:
- Resource limits
- Permissions
- Invalid template
- Resource dependency

**Solutions**:
```bash
# Check stack events
aws cloudformation describe-stack-events --stack-name my-stack

# Validate template
aws cloudformation validate-template --template-body file://template.yaml

# Check IAM permissions
aws cloudformation describe-stack-resources --stack-name my-stack
```

## SQS Message Issues

### Messages Not Processing

**Symptoms**: Messages stuck in queue.

**Causes**:
- Visibility timeout
- Dead letter queue
- Lambda function errors

**Solutions**:
```bash
# Check queue attributes
aws sqs get-queue-attributes \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue \
  --attribute-names All

# Receive messages
aws sqs receive-message \
  --queue-url https://sqs.us-east-1.amazonaws.com/123456789012/my-queue
```

## DynamoDB Issues

### Throttling Errors

**Symptoms**: ProvisionedThroughputExceededException.

**Causes**:
- Exceeding provisioned capacity
- Hot partitions
- Large items

**Solutions**:
```bash
# Check table throughput
aws dynamodb describe-table --table-name my-table

# Enable auto scaling
aws application-autoscaling register-scalable-target \
  --service-namespace dynamodb \
  --resource-id table/my-table \
  --scalable-dimension dynamodb:table:ReadCapacityUnits \
  --min-capacity 5 \
  --max-capacity 1000
```

## Cost Issues

### Unexpected High Bill

**Symptoms**: AWS bill much higher than expected.

**Causes**:
- Unused resources
- Data transfer costs
- Over-provisioned resources

**Solutions**:
```bash
# Check cost breakdown
aws ce get-cost-and-usage \
  --time-period Start=2024-01-01,End=2024-01-31 \
  --granularity MONTHLY \
  --metrics BlendedCost

# Check unused resources
aws ce get-rightsizing-recommendation \
  --service ec2
```
