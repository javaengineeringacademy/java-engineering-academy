# AWS CloudFormation

## Overview

AWS CloudFormation is an Infrastructure as Code (IaC) service that uses JSON/YAML templates to model and provision AWS resources.

## Template Structure

```yaml
AWSTemplateFormatVersion: '2010-09-09'
Description: 'My CloudFormation Stack'
Metadata:
  AWS::CloudFormation::Interface:
    ParameterGroups:
      - Label:
          default: "Network Configuration"
        Parameters:
          - VpcCIDR
Resources:
  MyVPC:
    Type: AWS::EC2::VPC
    Properties:
      CidrBlock: !Ref VpcCIDR
      EnableDnsHostnames: true
Outputs:
  VpcId:
    Value: !Ref MyVPC
Parameters:
  VpcCIDR:
    Type: String
    Default: "10.0.0.0/16"
```

## Resource Types

| Resource              | Type                              |
|-----------------------|-----------------------------------|
| EC2 Instance          | AWS::EC2::Instance                |
| S3 Bucket             | AWS::S3::Bucket                   |
| RDS Instance          | AWS::RDS::DBInstance              |
| Lambda Function       | AWS::Lambda::Function             |
| DynamoDB Table        | AWS::DynamoDB::Table              |
| IAM Role              | AWS::IAM::Role                    |
| VPC                   | AWS::EC2::VPC                     |
| Security Group        | AWS::EC2::SecurityGroup           |

## Intrinsic Functions

### Ref
```yaml
# Reference a resource
!Ref MyVPC

# Reference a parameter
!Ref VpcCIDR
```

### Fn::GetAtt
```yaml
# Get attribute from resource
!GetAtt MyVPC.VpcId
!GetAtt MyInstance.PublicIp
```

### Fn::Join
```yaml
!Join
  - ":"
  - - "arn:aws:s3:::"
    - !Ref MyBucket
```

### Fn::If
```yaml
!If
  - IsProduction
  - - !Ref ProdInstanceType
  - - !Ref DevInstanceType
```

### Fn::Select
```yaml
!Select
  - 0
  - !Split ",", "a,b,c"
```

### Fn::Sub
```yaml
!Sub
  - "arn:aws:s3:::${BucketName}/*"
  - BucketName: !Ref MyBucket
```

### Fn::ImportValue
```yaml
!ImportValue SharedVpcId
```

## Conditions

```yaml
Conditions:
  IsProduction: !Equals [!Ref Environment, "production"]
  IsMultiAZ: !Not [!Equals [!Ref MultiAZ, "false"]]
  HasBackup: !Or
    - !Condition IsProduction
    - !Condition IsStaging
```

## Stacks

### Create Stack
```bash
aws cloudformation create-stack \
  --stack-name my-stack \
  --template-body file://template.yaml \
  --parameters ParameterKey=Env,ParameterValue=dev \
  --tags Key=Environment,Value=dev
```

### Update Stack
```bash
aws cloudformation update-stack \
  --stack-name my-stack \
  --template-body file://template.yaml
```

### Delete Stack
```bash
aws cloudformation delete-stack --stack-name my-stack
```

### Stack Events
```bash
aws cloudformation describe-stack-events \
  --stack-name my-stack
```

## Change Sets

```bash
# Create change set
aws cloudformation create-change-set \
  --stack-name my-stack \
  --change-set-name my-changes \
  --template-body file://template.yaml

# View changes
aws cloudformation describe-change-set \
  --stack-name my-stack \
  --change-set-name my-changes

# Execute change set
aws cloudformation execute-change-set \
  --stack-name my-stack \
  --change-set-name my-changes
```

## Drift Detection

```bash
# Detect drift
aws cloudformation detect-stack-drift \
  --stack-name my-stack

# View drift status
aws cloudformation describe-stack-drift-detection-status \
  --stack-drift-detection-id drift-id-12345678
```

## Nested Stacks

```yaml
# Parent template
Resources:
  NetworkStack:
    Type: AWS::CloudFormation::Stack
    Properties:
      TemplateURL: https://s3.amazonaws.com/mybucket/network.yaml
      Parameters:
        VpcCIDR: 10.0.0.0/16
```

## Stack Sets

```bash
# Create stack set
aws cloudformation create-stack-set \
  --stack-set-name my-stack-set \
  --template-body file://template.yaml

# Create stack instances
aws cloudformation create-stack-instances \
  --stack-set-name my-stack-set \
  --accounts '["123456789012"]' \
  --regions '["us-east-1", "us-west-2"]'
```

## Custom Resources

```yaml
Resources:
  CustomResource:
    Type: Custom::MyResource
    Properties:
      ServiceToken: !GetAtt MyLambdaFunction.Arn
      Input: "my-input"
```

## CloudFormation Macros

```yaml
Transform: 'AWS::Serverless-2016-10-31'

Resources:
  MyFunction:
    Type: AWS::Serverless::Function
    Properties:
      Runtime: python3.12
      Handler: lambda_function.lambda_handler
```

## Drift Detection

### What is Drift?
```
Template: SecurityGroup allows port 80
Actual: SecurityGroup allows ports 80, 443
Result: Drift detected
```

### Drift Types
- `IN_SYNC`: Resource matches template
- `MODIFIED`: Resource changed
- `DELETED`: Resource removed
- `NOT_FOUND`: Resource exists but not in template

## Best Practices

1. **Version control** templates
2. **Use parameters** for reusability
3. **Implement change sets** before updates
4. **Use drift detection** regularly
5. **Tag all resources**
6. **Use nested stacks** for modularity
7. **Implement rollback triggers**
8. **Use Stack Sets** for multi-account
9. **Test templates** with `aws cloudformation validate-template`
10. **Use AWS CDK** for complex templates
