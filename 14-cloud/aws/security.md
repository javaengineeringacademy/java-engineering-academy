# AWS Security

## IAM (Identity and Access Management)

### Policies

JSON documents defining permissions. Can be attached to users, groups, or roles.

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::my-bucket/*"
    },
    {
      "Effect": "Allow",
      "Action": "s3:ListBucket",
      "Resource": "arn:aws:s3:::my-bucket"
    }
  ]
}
```

### Best Practices

- Follow least privilege principle
- Use IAM roles instead of access keys
- Enable MFA for all users
- Use IAM Access Analyzer to find unused permissions
- Rotate credentials regularly
- Use service control policies (SCPs) for guardrails

## KMS (Key Management Service)

Managed service for creating and controlling encryption keys.

### Features

- Hardware security modules (HSMs)
- Automatic key rotation
- Key policies and grants
- Integration with AWS services
- CloudTrail logging

### Usage

```python
import boto3

kms = boto3.client('kms')

# Encrypt
response = kms.encrypt(
    KeyId='alias/my-key',
    Plaintext=b'sensitive data'
)

# Decrypt
response = kms.decrypt(
    CiphertextBlob=response['CiphertextBlob']
)
```

## WAF (Web Application Firewall)

Protects web applications from common exploits and vulnerabilities.

### Features

- SQL injection protection
- Cross-site scripting (XSS) protection
- Rate limiting
- Geographic restrictions
- Custom rules
- Managed rule groups (AWSManagedRules)

### Usage

```python
waf = boto3.client('wafv2')

# Create web ACL
waf.create_web_acl(
    Name='my-web-acl',
    Scope='REGIONAL',
    DefaultAction={'Allow': {}},
    Rules=[
        {
            'Name': 'SQLInjectionRule',
            'Priority': 1,
            'Statement': {
                'SqliMatchStatement': {
                    'FieldToMatch': {'Body': {}},
                    'TextTransformations': [{'Priority': 0, 'Type': 'URL_DECODE'}]
                }
            },
            'Action': {'Block': {}},
            'VisibilityConfig': {
                'SampledRequestsEnabled': True,
                'CloudWatchMetricsEnabled': True,
                'MetricName': 'SQLInjectionRule'
            }
        }
    ],
    VisibilityConfig={
        'SampledRequestsEnabled': True,
        'CloudWatchMetricsEnabled': True,
        'MetricName': 'my-web-acl'
    }
)
```

## AWS Shield

Managed DDoS protection service.

### Shield Standard

- Free protection against most common DDoS attacks
- Automatic protection for all AWS customers

### Shield Advanced

- Enhanced protection for sophisticated attacks
- 24/7 DDoS Response Team (DRT)
- Cost protection for scaling during attacks
- Advanced reporting and metrics

## GuardDuty

Threat detection service that monitors for malicious activity.

### Features

- Continuous monitoring
- Machine learning-based detection
- Integration with CloudWatch Events
- Supports VPC Flow Logs, DNS logs, CloudTrail

### Enable

```bash
aws guardduty create-detector --enable
```

## Security Hub

Central security view and automated compliance checks.

### Features

- Security findings aggregation
- CIS AWS Foundations Benchmark
- AWS Foundational Security Best Practices
- Custom security standards
- Integration with other AWS services

### Enable

```bash
aws securityhub enable-security-hub
```

## Network Security

### Security Groups

Stateful firewall rules for EC2 instances:

```python
ec2 = boto3.client('ec2')

ec2.create_security_group(
    GroupName='my-sg',
    Description='My security group',
    VpcId='vpc-12345678'
)

ec2.authorize_security_group_ingress(
    GroupId='sg-12345678',
    IpPermissions=[
        {
            'IpProtocol': 'tcp',
            'FromPort': 443,
            'ToPort': 443,
            'IpRanges': [{'CidrIp': '0.0.0.0/0'}]
        }
    ]
)
```

### Network ACLs

Stateless firewall rules at subnet level.

## Data Protection

### Encryption at Rest

- S3: SSE-S3, SSE-KMS, SSE-C
- EBS: Encryption enabled by default
- RDS: Encryption at rest
- DynamoDB: Encryption at rest

### Encryption in Transit

- TLS/SSL for all API calls
- HTTPS for S3 bucket policies
- RDS SSL connections
- ElastiCache in-transit encryption

## Compliance

### AWS Artifact

Access compliance reports:
- SOC reports
- PCI DSS reports
- ISO certifications
- GDPR documentation

### CloudTrail

Log API calls for auditing:

```bash
aws cloudtrail create-trail --name my-trail --s3-bucket-name my-bucket
aws cloudtrail start-logging --name my-trail
```

## Secrets Management

### AWS Secrets Manager

Store and retrieve secrets:

```python
import boto3
import json

secrets = boto3.client('secretsmanager')

# Create secret
secrets.create_secret(
    Name='prod/db/password',
    SecretString=json.dumps({'username': 'admin', 'password': 'secret'})
)

# Get secret
response = secrets.get_secret_value(SecretId='prod/db/password')
secret = json.loads(response['SecretString'])
```

### Parameter Store

Store configuration data and secrets:

```python
ssm = boto3.client('ssm')

# Put parameter
ssm.put_parameter(
    Name='/myapp/config/database-url',
    Value='mysql://...',
    Type='SecureString'
)

# Get parameter
response = ssm.get_parameter(
    Name='/myapp/config/database-url',
    WithDecryption=True
)
```

## Security Best Practices

1. Enable MFA for all IAM users
2. Use IAM roles for EC2 and ECS
3. Enable GuardDuty and Security Hub
4. Encrypt data at rest and in transit
5. Use VPC for network isolation
6. Implement least privilege IAM policies
7. Enable CloudTrail logging
8. Regular security audits with AWS Config
9. Use WAF for web application protection
10. Rotate credentials and encryption keys
