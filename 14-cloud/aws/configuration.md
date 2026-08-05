# AWS Configuration

## AWS CLI Configuration

### Installation

```bash
# macOS
brew install awscli

# Linux
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Verify installation
aws --version
```

### Configuration

```bash
# Configure default profile
aws configure
# Enter: Access Key ID, Secret Access Region, Region, Output format

# Configure named profile
aws configure --profile production

# View configuration
aws configure list

# View specific profile
aws configure list --profile production
```

### Configuration Files

```ini
# ~/.aws/config
[default]
region = us-east-1
output = json

[profile production]
region = us-west-2
output = table
role_arn = arn:aws:iam::123456789012:role/CrossAccountRole
source_profile = default

# ~/.aws/credentials
[default]
aws_access_key_id = AKIA...
aws_secret_access_key = ...

[production]
aws_access_key_id = AKIA...
aws_secret_access_key = ...
```

## AWS SDK Setup

### Python (boto3)

```python
import boto3

# Default session
s3 = boto3.client('s3')
ec2 = boto3.resource('ec2')

# Named profile session
session = boto3.Session(profile_name='production')
s3 = session.client('s3')

# Explicit credentials
session = boto3.Session(
    aws_access_key_id='AKIA...',
    aws_secret_access_key='...',
    region_name='us-east-1'
)
```

### Node.js (AWS SDK v3)

```javascript
import { S3Client, ListBucketsCommand } from "@aws-sdk/client-s3";

// Default credentials
const s3 = new S3Client({ region: "us-east-1" });

// Named profile
import { fromIni } from "@aws-sdk/credential-providers";
const s3 = new S3Client({
  region: "us-east-1",
  credentials: fromIni({ profile: "production" })
});

// Explicit credentials
const s3 = new S3Client({
  region: "us-east-1",
  credentials: {
    accessKeyId: "AKIA...",
    secretAccessKey: "..."
  }
});
```

### Go (AWS SDK v2)

```go
import (
    "github.com/aws/aws-sdk-go-v2/config"
    "github.com/aws/aws-sdk-go-v2/service/s3"
)

// Default configuration
cfg, err := config.LoadDefaultConfig(context.TODO())
client := s3.NewFromConfig(cfg)

// Named profile
cfg, err := config.LoadDefaultConfig(context.TODO(),
    config.WithSharedConfigProfile("production"),
)
client := s3.NewFromConfig(cfg)
```

## Environment Variables

```bash
# AWS Credentials
export AWS_ACCESS_KEY_ID="AKIA..."
export AWS_SECRET_ACCESS_KEY="..."

# AWS Configuration
export AWS_DEFAULT_REGION="us-east-1"
export AWS_DEFAULT_OUTPUT="json"
export AWS_PROFILE="production"

# AWS Session
export AWS_SESSION_TOKEN="..."
export AWS_EXPIRATION="..."
```

## Credential Management

### AWS Vault

Securely store and access AWS credentials:

```bash
# Install
brew install aws-vault

# Add credentials
aws-vault add default
aws-vault add production

# Use credentials
aws-vault exec default -- aws s3 ls
aws-vault exec production -- aws ec2 describe-instances

# List profiles
aws-vault list
```

### IAM Roles for EC2

Attach IAM roles to EC2 instances instead of storing credentials:

```bash
# Create instance profile
aws iam create-instance-profile --instance-profile-name my-profile

# Attach role
aws iam add-role-to-instance-profile --instance-profile-name my-profile --role-name my-role

# Associate with EC2
aws ec2 associate-iam-instance-profile --instance-id i-1234567890abcdef0 --iam-instance-profile Name=my-profile
```

## Multi-Account Setup

### AWS Organizations

```bash
# Create organization
aws organizations create-organization --feature-set ALL

# Create account
aws organizations create-account --email billing@example.com --account-name "Production"

# Create SCP
aws organizations create-policy --content file://policy.json --name "RestrictRegion" --type SERVICE_CONTROL_POLICY

# Attach SCP
aws organizations attach-policy --policy-id p-12345678 --target-id 123456789012
```

## Profile Management

### Switching Profiles

```bash
# Set default profile
export AWS_PROFILE=production

# Use for single command
AWS_PROFILE=production aws s3 ls

# Use named profile
aws s3 ls --profile production
```

### Credential Chains

AWS SDKs follow a credential chain:

1. Environment variables
2. Shared credentials file (~/.aws/credentials)
3. Shared config file (~/.aws/config)
4. Assume role provider
5. Boto2 config file
6. Instance metadata (on EC2)
7. Container credentials (ECS)

## Security Best Practices

1. Never hardcode credentials in code
2. Use IAM roles for EC2 and ECS
3. Enable MFA for all IAM users
4. Rotate credentials regularly
5. Use AWS Vault or similar tools
6. Audit credentials with IAM Access Analyzer
7. Use temporary credentials with STS
8. Apply least privilege principle
