# AWS IAM (Identity and Access Management)

## Overview

AWS IAM enables you to manage access to AWS services and resources securely.

## IAM Components

```
┌─────────────────────────────────────────────────────────┐
│                      IAM                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Users   │  │  Groups  │  │  Roles   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │   Policies    │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## IAM Users

```bash
# Create user
aws iam create-user --user-name alice

# Create access key
aws iam create-access-key --user-name alice

# Create login profile
aws iam create-login-profile \
  --user-name alice \
  --password MyP@ssw0rd \
  --password-reset-required
```

## IAM Groups

```bash
# Create group
aws iam create-group --group-name Developers

# Add user to group
aws iam add-user-to-group \
  --group-name Developers \
  --user-name alice

# Attach policy to group
aws iam attach-group-policy \
  --group-name Developers \
  --policy-arn arn:aws:iam::aws:policy/PowerUserAccess
```

## IAM Roles

### Trust Policy
```json
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
```

### Create Role
```bash
# Create EC2 role
aws iam create-role \
  --role-name EC2-Role \
  --assume-role-policy-document file://trust-policy.json

# Attach policy
aws iam attach-role-policy \
  --role-name EC2-Role \
  --policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess

# Associate with EC2 instance
aws iam associate-role \
  --role-name EC2-Role \
  --instance-id i-1234567890abcdef0
```

## IAM Policies

### Managed Policies
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
      ],
      "Resource": "arn:aws:s3:::my-bucket/*"
    }
  ]
}
```

### Inline Policies
```bash
# Attach inline policy to user
aws iam put-user-policy \
  --user-name alice \
  --policy-name S3Access \
  --policy-document file://policy.json
```

### Policy Evaluation Logic
```
1. Default Deny
2. Explicit Deny overrides Allow
3. No default Allow - must have policy
```

## IAM Policy Conditions

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": "arn:aws:s3:::my-bucket/*",
      "Condition": {
        "StringEquals": {
          "aws:RequestedRegion": "us-east-1"
        },
        "Bool": {
          "aws:SecureTransport": "true"
        },
        "IpAddress": {
          "aws:SourceIp": "203.0.113.0/24"
        }
      }
    }
  ]
}
```

## IAM Roles for Workloads

### EC2 Instance Profile
```bash
# Create instance profile
aws iam create-instance-profile --instance-profile-name MyProfile

# Add role to instance profile
aws iam add-role-to-instance-profile \
  --instance-profile-name MyProfile \
  --role-name EC2-Role

# Launch instance with profile
aws ec2 run-instances \
  --iam-instance-profile Name=MyProfile
```

### Lambda Execution Role
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

## AWS SSO (IAM Identity Center)

```bash
# Create permission set
aws sso-admin create-permission-set \
  --instance-arn arn:aws:sso:::instance/ssoins-12345678 \
  --name DeveloperAccess \
  --session-duration PT1H

# Assign account to permission set
aws sso-admin create-account-assignment \
  --instance-arn arn:aws:sso:::instance/ssoins-12345678 \
  --target-id 123456789012 \
  --target-type AWS_ACCOUNT \
  --permission-set-arn arn:aws:sso:::permissionSet/ssoins-12345678/ps-12345678 \
  --principal-type USER \
  --principal-id user-id-12345678
```

## Federation

### SAML 2.0
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::123456789012:saml-provider/MyProvider"
      },
      "Action": "sts:AssumeRoleWithSAML",
      "Condition": {
        "StringEquals": {
          "SAML:aud": "https://signin.aws.amazon.com/saml"
        }
      }
    }
  ]
}
```

### Web Identity
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "cognito-identity.amazonaws.com"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "cognito-identity.amazonaws.com:aud": "us-east-1:example"
        }
      }
    }
  ]
}
```

## IAM Access Analyzer

```bash
# Create analyzer
aws accessanalyzer create-analyzer \
  --analyzer-name my-analyzer \
  --type ACCOUNT

# Get findings
aws accessanalyzer get-findings \
  --analyzer-arn arn:aws:accessanalyzer:us-east-1:123456789012:analyzer/my-analyzer
```

## IAM Best Practices

### Root Account
- Enable MFA
- Don't use for daily tasks
- Use for billing only

### Users
- One user per person
- Use groups for permissions
- Enforce strong passwords

### Policies
- Follow least privilege
- Use managed policies
- Regular audit permissions

### Roles
- Use roles for EC2/Lambda
- Avoid long-term credentials
- Use federation for humans

### Security
- Enable MFA everywhere
- Use IAM Access Analyzer
- Rotate access keys regularly
- Monitor with CloudTrail

## Common Policies

### Read-Only Access
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:Get*",
        "s3:List*",
        "ec2:Describe*"
      ],
      "Resource": "*"
    }
  ]
}
```

### S3 Full Access
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": [
        "arn:aws:s3:::my-bucket",
        "arn:aws:s3:::my-bucket/*"
      ]
    }
  ]
}
```

### Lambda Execution
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
      ],
      "Resource": "arn:aws:s3:::my-bucket/*"
    }
  ]
}
```

## Audit & Compliance

```bash
# Get account summary
aws iam get-account-summary

# List MFA devices
aws iam list-mfa-devices

# Get credential report
aws iam generate-credential-report
aws iam get-credential-report
```

## Pricing

- **IAM**: Free
- **IAM Access Analyzer**: $0.10 per policy analyzed per month
- **AWS SSO**: Free for standard, $1/user/month for admin
