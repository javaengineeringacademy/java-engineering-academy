# AWS Cheat Sheet

## EC2 Commands

```bash
# Launch instance
aws ec2 run-instances --image-id ami-xxx --instance-type t2.micro --key-name my-key

# List instances
aws ec2 describe-instances --filters Name=instance-state-name,Values=running

# Stop/Start/Terminate
aws ec2 stop-instances --instance-ids i-xxx
aws ec2 start-instances --instance-ids i-xxx
aws ec2 terminate-instances --instance-ids i-xxx

# Connect
ssh -i key.pem ec2-user@<ip>

# Key pairs
aws ec2 create-key-pair --key-name my-key
aws ec2 describe-key-pairs

# Security groups
aws ec2 create-security-group --group-name my-sg --description "My SG"
aws ec2 authorize-security-group-ingress --group-id sg-xxx --protocol tcp --port 22 --cidr 0.0.0.0/0
```

## S3 Commands

```bash
# Create bucket
aws s3 mb s3://my-bucket

# List buckets
aws s3 ls

# Upload/Download
aws s3 cp file.txt s3://my-bucket/
aws s3 cp s3://my-bucket/file.txt .

# Sync directory
aws s3 sync ./local s3://my-bucket/

# Delete
aws s3 rm s3://my-bucket/file.txt
aws s3 rb s3://my-bucket --force

# Bucket policies
aws s3api get-bucket-policy --bucket my-bucket
aws s3api put-bucket-policy --bucket my-bucket --policy file://policy.json
```

## RDS Commands

```bash
# Create instance
aws rds create-db-instance --db-instance-identifier mydb --db-instance-class db.t3.micro --engine mysql

# List instances
aws rds describe-db-instances

# Stop/Start/Delete
aws rds stop-db-instance --db-instance-identifier mydb
aws rds start-db-instance --db-instance-identifier mydb
aws rds delete-db-instance --db-instance-identifier mydb --skip-final-snapshot

# Snapshots
aws rds create-db-snapshot --db-instance-identifier mydb --db-snapshot-identifier my-snap
aws rds describe-db-snapshots
```

## Lambda Commands

```bash
# Create function
aws lambda create-function --function-name my-func --runtime python3.9 --role arn:aws:iam::xxx:role/xxx --handler lambda_function.lambda_handler --zip-file fileb://function.zip

# List functions
aws lambda list-functions

# Invoke
aws lambda invoke --function-name my-func --payload '{}' output.json

# Update code
aws lambda update-function-code --function-name my-func --zip-file fileb://function.zip

# Update config
aws lambda update-function-configuration --function-name my-func --timeout 30
```

## IAM Commands

```bash
# Users
aws iam create-user --user-name my-user
aws iam list-users

# Roles
aws iam create-role --role-name my-role --assume-role-policy-document file://trust.json
aws iam list-roles

# Policies
aws iam list-policies
aws iam attach-role-policy --role-name my-role --policy-arn arn:aws:iam::aws:policy/xxx

# Groups
aws iam create-group --group-name my-group
aws iam add-user-to-group --user-name my-user --group-name my-group
```

## VPC Commands

```bash
# Create VPC
aws ec2 create-vpc --cidr-block 10.0.0.0/16

# Subnets
aws ec2 create-subnet --vpc-id vpc-xxx --cidr-block 10.0.1.0/24

# Internet Gateway
aws ec2 create-internet-gateway
aws ec2 attach-internet-gateway --internet-gateway-id igw-xxx --vpc-id vpc-xxx

# Route tables
aws ec2 create-route-table --vpc-id vpc-xxx
aws ec2 create-route --route-table-id rtb-xxx --destination-cidr-block 0.0.0.0/0 --gateway-id igw-xxx

# NAT Gateway
aws ec2 create-nat-gateway --subnet-id subnet-xxx --allocation-id eipalloc-xxx
```

## ECS Commands

```bash
# Create cluster
aws ecs create-cluster --cluster-name my-cluster

# Register task definition
aws ecs register-task-definition --cli-input-json file://task.json

# Create service
aws ecs create-service --cluster my-cluster --service-name my-service --task-definition my-task --desired-count 2

# List services
aws ecs list-services --cluster my-cluster

# Update service
aws ecs update-service --cluster my-cluster --service my-service --desired-count 3
```

## CloudWatch Commands

```bash
# Put metric
aws cloudwatch put-metric-data --namespace MyApp --metric-name RequestCount --value 100

# Create alarm
aws cloudwatch put-metric-alarm --alarm-name my-alarm --metric-name CPUUtilization --namespace AWS/EC2 --threshold 80 --comparison-operator GreaterThanThreshold

# List alarms
aws cloudwatch describe-alarms

# Logs
aws logs create-log-group --log-group-name /my/app
aws logs get-log-events --log-group-name /my/app --log-stream-name my-stream
```

## SQS Commands

```bash
# Create queue
aws sqs create-queue --queue-name my-queue

# Send message
aws sqs send-message --queue-url https://sqs.xxx/xxx/my-queue --message-body "Hello"

# Receive message
aws sqs receive-message --queue-url https://sqs.xxx/xxx/my-queue

# Delete message
aws sqs delete-message --queue-url https://sqs.xxx/xxx/my-queue --receipt-handle xxx

# Delete queue
aws sqs delete-queue --queue-url https://sqs.xxx/xxx/my-queue
```

## SNS Commands

```bash
# Create topic
aws sns create-topic --name my-topic

# Subscribe
aws sns subscribe --topic-arn arn:aws:sns:xxx:xxx:my-topic --protocol email --notification-endpoint user@example.com

# Publish
aws sns publish --topic-arn arn:aws:sns:xxx:xxx:my-topic --message "Hello"

# List topics
aws sns list-topics
```

## CloudFormation Commands

```bash
# Create stack
aws cloudformation create-stack --stack-name my-stack --template-body file://template.yaml

# Describe stack
aws cloudformation describe-stacks --stack-name my-stack

# Update stack
aws cloudformation update-stack --stack-name my-stack --template-body file://template.yaml

# Delete stack
aws cloudformation delete-stack --stack-name my-stack

# List stacks
aws cloudformation list-stacks
```

## STS Commands

```bash
# Get caller identity
aws sts get-caller-identity

# Assume role
aws sts assume-role --role-arn arn:aws:iam::xxx:role/my-role --role-session-name my-session

# Decode access key
aws sts decode-authorization-message --encoded-message xxx
```

## Common Options

```bash
# Output formats
--output json (default)
--output table
--output text

# Query
--query 'Reservations[*].Instances[*].[InstanceId,State.Name]'

# Filters
--filters Name=instance-state-name,Values=running

# Region
--region us-east-1

# Profile
--profile my-profile
```
