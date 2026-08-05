# AWS Hands-On Labs

## Lab 1: Launch an EC2 Instance

### Objective
Launch and connect to an EC2 instance.

### Steps

```bash
# Launch instance
aws ec2 run-instances \
  --image-id ami-0c55b159cbfafe1f0 \
  --instance-type t2.micro \
  --key-name my-key \
  --security-group-ids sg-12345678 \
  --subnet-id subnet-12345678

# Check instance
aws ec2 describe-instances --filters Name=instance-state-name,Values=running

# Connect
ssh -i my-key.pem ec2-user@<public-ip>

# Stop instance
aws ec2 stop-instances --instance-ids i-1234567890abcdef0

# Terminate instance
aws ec2 terminate-instances --instance-ids i-1234567890abcdef0
```

## Lab 2: Create S3 Bucket

### Objective
Create and configure an S3 bucket.

### Steps

```bash
# Create bucket
aws s3 mb s3://my-unique-bucket-12345

# Enable versioning
aws s3api put-bucket-versioning \
  --bucket my-unique-bucket-12345 \
  --versioning-configuration Status=Enabled

# Upload file
aws s3 cp myfile.txt s3://my-unique-bucket-12345/

# List objects
aws s3 ls s3://my-unique-bucket-12345

# Download file
aws s3 cp s3://my-unique-bucket-12345/myfile.txt .

# Delete bucket
aws s3 rb s3://my-unique-bucket-12345 --force
```

## Lab 3: Create RDS Instance

### Objective
Launch and connect to a MySQL RDS instance.

### Steps

```bash
# Create DB instance
aws rds create-db-instance \
  --db-instance-identifier mydb \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password mypassword \
  --allocated-storage 20

# Check status
aws rds describe-db-instances --db-instance-identifier mydb

# Connect
mysql -h mydb.xxxx.us-east-1.rds.amazonaws.com -u admin -p

# Stop instance
aws rds stop-db-instance --db-instance-identifier mydb

# Delete instance
aws rds delete-db-instance \
  --db-instance-identifier mydb \
  --skip-final-snapshot
```

## Lab 4: Create Lambda Function

### Objective
Create and test a Lambda function.

### Steps

```bash
# Create function
cat > lambda_function.py << 'EOF'
def lambda_handler(event, context):
    return {
        'statusCode': 200,
        'body': 'Hello from Lambda!'
    }
EOF

zip function.zip lambda_function.py

aws lambda create-function \
  --function-name my-function \
  --runtime python3.9 \
  --role arn:aws:iam::123456789012:role/lambda-role \
  --handler lambda_function.lambda_handler \
  --zip-file fileb://function.zip

# Test function
aws lambda invoke \
  --function-name my-function \
  --payload '{"key": "value"}' \
  output.json

cat output.json

# Update function
aws lambda update-function-code \
  --function-name my-function \
  --zip-file fileb://function.zip

# Delete function
aws lambda delete-function --function-name my-function
```

## Lab 5: Create VPC

### Objective
Create a VPC with public and private subnets.

### Steps

```bash
# Create VPC
VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text)

# Create subnets
PUBLIC_SUBNET=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.1.0/24 \
  --availability-zone us-east-1a \
  --query 'Subnet.SubnetId' --output text)

PRIVATE_SUBNET=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.2.0/24 \
  --availability-zone us-east-1a \
  --query 'Subnet.SubnetId' --output text)

# Create Internet Gateway
IGW_ID=$(aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text)

# Attach Internet Gateway
aws ec2 attach-internet-gateway --internet-gateway-id $IGW_ID --vpc-id $VPC_ID

# Create route table
RT_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)

# Add route
aws ec2 create-route \
  --route-table-id $RT_ID \
  --destination-cidr-block 0.0.0.0/0 \
  --gateway-id $IGW_ID

# Associate route table
aws ec2 associate-route-table --route-table-id $RT_ID --subnet-id $PUBLIC_SUBNET

# Clean up
aws ec2 delete-vpc --vpc-id $VPC_ID
```

## Lab 6: Create IAM Role

### Objective
Create an IAM role with S3 read access.

### Steps

```bash
# Create trust policy
cat > trust-policy.json << 'EOF'
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

# Create role
aws iam create-role \
  --role-name my-role \
  --assume-role-policy-document file://trust-policy.json

# Attach policy
aws iam attach-role-policy \
  --role-name my-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess

# Create instance profile
aws iam create-instance-profile --instance-profile-name my-profile

# Add role to instance profile
aws iam add-role-to-instance-profile \
  --instance-profile-name my-profile \
  --role-name my-role

# Clean up
aws iam remove-role-from-instance-profile \
  --instance-profile-name my-profile \
  --role-name my-role

aws iam delete-instance-profile --instance-profile-name my-profile

aws iam detach-role-policy \
  --role-name my-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess

aws iam delete-role --role-name my-role
```

## Lab 7: Set Up CloudWatch Alarm

### Objective
Create a CloudWatch alarm for EC2 CPU utilization.

### Steps

```bash
# Create alarm
aws cloudwatch put-metric-alarm \
  --alarm-name high-cpu \
  --metric-name CPUUtilization \
  --namespace AWS/EC2 \
  --statistic Average \
  --period 300 \
  --evaluation-periods 2 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=InstanceId,Value=i-1234567890abcdef0 \
  --alarm-actions arn:aws:sns:us-east-1:123456789012:my-topic

# Check alarm
aws cloudwatch describe-alarms --alarm-names high-cpu

# Delete alarm
aws cloudwatch delete-alarms --alarm-names high-cpu
```

## Lab 8: Create API Gateway

### Objective
Create a REST API with Lambda integration.

### Steps

```bash
# Create API
API_ID=$(aws apigateway create-rest-api --name my-api --query 'id' --output text)

# Get root resource
ROOT_ID=$(aws apigateway get-resources --rest-api-id $API_ID --query 'items[0].id' --output text)

# Create resource
RESOURCE_ID=$(aws apigateway create-resource \
  --rest-api-id $API_ID \
  --parent-id $ROOT_ID \
  --path-part myresource \
  --query 'id' --output text)

# Create method
aws apigateway put-method \
  --rest-api-id $API_ID \
  --resource-id $RESOURCE_ID \
  --http-method GET \
  --authorization-type NONE

# Deploy API
aws apigateway create-deployment \
  --rest-api-id $API_ID \
  --stage-name prod

# Clean up
aws apigateway delete-rest-api --rest-api-id $API_ID
```

## Lab 9: Create SQS Queue

### Objective
Create and configure an SQS queue.

### Steps

```bash
# Create queue
aws sqs create-queue --queue-name my-queue

# Get queue URL
QUEUE_URL=$(aws sqs get-queue-url --queue-name my-queue --query 'QueueUrl' --output text)

# Send message
aws sqs send-message \
  --queue-url $QUEUE_URL \
  --message-body "Hello SQS!"

# Receive message
aws sqs receive-message --queue-url $QUEUE_URL

# Delete message
aws sqs delete-message \
  --queue-url $QUEUE_URL \
  --receipt-handle <receipt-handle>

# Set attributes
aws sqs set-queue-attributes \
  --queue-url $QUEUE_URL \
  --attributes '{"VisibilityTimeout":"60"}'

# Delete queue
aws sqs delete-queue --queue-url $QUEUE_URL
```

## Lab 10: Create CloudFormation Stack

### Objective
Deploy infrastructure using CloudFormation.

### Steps

```bash
# Create template
cat > template.yaml << 'EOF'
AWSTemplateFormatVersion: '2010-09-09'
Description: My first CloudFormation template
Resources:
  MyBucket:
    Type: AWS::S3::Bucket
    Properties:
      BucketName: my-cfn-bucket-12345
EOF

# Create stack
aws cloudformation create-stack \
  --stack-name my-stack \
  --template-body file://template.yaml

# Wait for completion
aws cloudformation wait stack-create-complete --stack-name my-stack

# Check stack
aws cloudformation describe-stacks --stack-name my-stack

# Update stack
aws cloudformation update-stack \
  --stack-name my-stack \
  --template-body file://template.yaml

# Delete stack
aws cloudformation delete-stack --stack-name my-stack

# Wait for deletion
aws cloudformation wait stack-delete-complete --stack-name my-stack
```
