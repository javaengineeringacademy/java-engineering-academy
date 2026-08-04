# AWS Secrets Manager

## Overview

AWS Secrets Manager helps you protect secrets needed to access applications, services, and IT resources.

## Features

- **Automatic rotation** of secrets
- **Centralized secret management**
- **Encryption at rest** using KMS
- **Audit** with CloudTrail
- **Cross-account access**
- **Disaster recovery**

## Creating Secrets

### Basic Secret
```bash
aws secretsmanager create-secret \
  --name my-secret \
  --secret-string '{"username":"admin","password":"P@ssw0rd"}'
```

### From JSON
```bash
aws secretsmanager create-secret \
  --name my-db-secret \
  --secret-string '{
    "host": "mydb.cluster.us-east-1.rds.amazonaws.com",
    "port": 3306,
    "username": "admin",
    "password": "P@ssw0rs"
  }'
```

### Binary Secret
```bash
aws secretsmanager create-secret \
  --name my-binary-secret \
  --secret-binary fileb://binary-data.bin
```

## Retrieving Secrets

```bash
# Get secret value
aws secretsmanager get-secret-value \
  --secret-id my-secret

# Get specific version
aws secretsmanager get-secret-value \
  --secret-id my-secret \
  --version-stage AWSCURRENT

# Get previous version
aws secretsmanager get-secret-value \
  --secret-id my-secret \
  --version-stage AWSPREVIOUS
```

### SDK Usage
```python
import boto3
import json

client = boto3.client('secretsmanager')
response = client.get_secret_value(SecretId='my-secret')
secret = json.loads(response['SecretString'])
```

## Secret Rotation

### Enable Rotation
```bash
aws secretsmanager rotate-secret \
  --secret-id my-secret \
  --rotation-lambda-arn arn:aws:lambda:us-east-1:123456789012:function:rotate-secret \
  --rotation-rules '{"AutomaticallyAfterDays": 30}'
```

### Rotation Lambda Function
```python
import boto3
import json

def lambda_handler(event, context):
    secret_arn = event['SecretId']
    token = event['ClientRequestToken']
    step = event['Step']
    
    service_client = boto3.client('secretsmanager')
    
    if step == "createSecret":
        # Generate new secret
        new_secret = generate_password()
        service_client.put_secret_value(
            SecretId=secret_arn,
            ClientRequestToken=token,
            SecretString=new_secret,
            VersionStages=['AWSPENDING']
        )
    
    elif step == "setSecret":
        # Set secret in service
        pass
    
    elif step == "testSecret":
        # Test secret works
        pass
    
    elif step == "finishSecret":
        # Finalize rotation
        service_client.update_secret_version_stage(
            SecretId=secret_arn,
            VersionStage='AWSCURRENT',
            MoveToVersionId=token,
            RemoveFromVersionId='previous-version-id'
        )
```

## Resource Policies

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::123456789012:role/MyRole"
      },
      "Action": "secretsmanager:GetSecretValue",
      "Resource": "arn:aws:secretsmanager:us-east-1:123456789012:secret:my-secret-*"
    }
  ]
}
```

## VPC Endpoints

```bash
# Create VPC endpoint for Secrets Manager
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-12345678 \
  --service-name com.amazonaws.us-east-1.secretsmanager \
  --vpc-endpoint-type Interface \
  --subnet-ids subnet-12345678 \
  --security-group-ids sg-12345678
```

## Secret Recovery

```bash
# Enable recovery
aws secretsmanager update-secret \
  --secret-id my-secret \
  --recovery-window-in-days 30

# Recover deleted secret
aws secretsmanager restore-secret \
  --secret-id my-secret
```

## Tagging

```bash
# Add tags
aws secretsmanager tag-resource \
  --secret-id my-secret \
  --tags '[
    {"Key": "Environment", "Value": "production"},
    {"Key": "Team", "Value": "backend"}
  ]'
```

## Cross-Account Access

```bash
# Share secret across accounts
aws secretsmanager put-resource-policy \
  --secret-id my-secret \
  --resource-policy '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "AWS": "arn:aws:iam::other-account-id:root"
        },
        "Action": "secretsmanager:GetSecretValue",
        "Resource": "*"
      }
    ]
  }'
```

## Monitoring

```bash
# Get secret rotation status
aws secretsmanager describe-secret --secret-id my-secret

# Get secret value with metadata
aws secretsmanager get-secret-value --secret-id my-secret \
  --query '{ARN:ARN,VersionId:VersionId,VersionStages:VersionStages}'
```

### CloudWatch Metrics
| Metric           | Description                    |
|------------------|--------------------------------|
| GetSecretValue   | Successful retrievals          |
| PutSecretValue   | Successful writes              |
| RotateSecret     | Successful rotations           |

## Best Practices

1. **Use automatic rotation** for database credentials
2. **Encrypt secrets** with KMS
3. **Implement least privilege** access
4. **Use VPC endpoints** for private access
5. **Enable CloudTrail** for auditing
6. **Use resource policies** for access control
7. **Implement secret recovery** window
8. **Tag secrets** for organization
9. **Monitor with CloudWatch** metrics
10. **Use SDKs** for secret retrieval
