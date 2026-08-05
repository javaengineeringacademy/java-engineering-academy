# AWS Installation and Setup

## AWS CLI

### Installation

```bash
# macOS
brew install awscli

# Linux (64-bit)
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Windows
msiexec.exe /i https://awscli.amazonaws.com/AWSCLIV2.msi

# Verify
aws --version
```

### Configuration

```bash
# Configure default profile
aws configure

# Configure specific profile
aws configure --profile production

# List profiles
aws configure list
```

## AWS SDKs

### Python (boto3)

```bash
# Install
pip install boto3

# Or with extras
pip install boto3[additional-packages]
```

### Node.js

```bash
# Install individual packages
npm install @aws-sdk/client-s3
npm install @aws-sdk/client-dynamodb
npm install @aws-sdk/client-lambda
```

### Go

```bash
# Install SDK
go get github.com/aws/aws-sdk-go-v2
```

### Java

```xml
<!-- Maven -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>
```

## AWS Cloud Development Kit (CDK)

### Installation

```bash
# Install CDK CLI
npm install -g aws-cdk

# Verify
cdk --version

# Initialize project
cdk init app --language typescript
cdk init app --language python
cdk init app --language java
```

### Deploy

```bash
# Synthesize template
cdk synth

# Deploy stack
cdk deploy

# Diff against deployed stack
cdk diff

# Destroy stack
cdk destroy
```

## AWS Serverless Application Model (SAM)

### Installation

```bash
# macOS
brew install aws-sam-cli

# Linux
pip install aws-sam-cli

# Initialize project
sam init

# Build
sam build

# Deploy
sam deploy --guided

# Local invoke
sam local invoke MyFunction --event event.json

# Start API
sam local start-api
```

## LocalStack

### Installation

```bash
# Docker
docker pull localstack/localstack

# Run
docker run -d -p 4566:4566 -p 4510-4559:4510-4559 localstack/localstack

# Install CLI
pip install awscli-local

# Use
awslocal s3 ls
```

## Terraform with AWS

### Installation

```bash
# macOS
brew install terraform

# Initialize
terraform init

# Plan
terraform plan

# Apply
terraform apply

# Destroy
terraform destroy
```

## AWS CLI Profiles

```bash
# Add credentials
aws configure --profile dev
# Enter Access Key, Secret Key, Region, Output

aws configure --profile prod
# Enter Access Key, Secret Key, Region, Output

# Use profile
aws s3 ls --profile dev
AWS_PROFILE=dev aws ec2 describe-instances

# List all profiles
cat ~/.aws/config | grep profile
```

## Verification

```bash
# Test AWS CLI
aws sts get-caller-identity

# Check region
aws configure get region

# List S3 buckets
aws s3 ls

# List EC2 instances
aws ec2 describe-instances --query 'Reservations[*].Instances[*].[InstanceId,State.Name]' --output table
```
