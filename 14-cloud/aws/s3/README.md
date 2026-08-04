# Amazon S3 (Simple Storage Service)

## Overview

Amazon S3 is an object storage service offering 99.999999999% (11 9s) durability.

## Storage Classes

| Class                  | Access           | Use Case                    | Min Storage Duration |
|------------------------|------------------|-----------------------------|----------------------|
| S3 Standard            | Frequent         | Cloud apps, content dist    | None                 |
| S3 Intelligent-Tiering | Auto-tiered      | Unknown/changing access     | None                 |
| S3 Standard-IA         | Infrequent       | Data accessed monthly       | 30 days              |
| S3 One Zone-IA         | Infrequent       | Recreatable data            | 30 days              |
| S3 Glacier Instant     | Milliseconds     | Archive, rapid access       | 90 days              |
| S3 Glacier Flexible    | Minutes-hours    | Archive, flexible retrieval | 90 days              |
| S3 Glacier Deep Archive| 12 hours         | Long-term archive           | 180 days             |

### Storage Class Decision Tree
```
Is data accessed frequently?
├── Yes → S3 Standard
└── No → Is access pattern known?
    ├── Yes → S3 Standard-IA or S3 One Zone-IA
    └── No → S3 Intelligent-Tiering
    
Need archive storage?
├── Rapid access needed → S3 Glacier Instant
├── Flexible retrieval → S3 Glacier Flexible
└── Long-term archive → S3 Glacier Deep Archive
```

## Bucket Configuration

### Versioning
```bash
# Enable versioning
aws s3api put-bucket-versioning \
  --bucket my-bucket \
  --versioning-configuration Status=Enabled

# List object versions
aws s3api list-object-versions --bucket my-bucket

# Restore specific version
aws s3api copy-object \
  --bucket my-bucket \
  --copy-source my-bucket/object.txt?versionId=abc123 \
  --key object.txt
```

### MFA Delete
```bash
# Enable MFA Delete (requires root account)
aws s3api put-bucket-versioning \
  --bucket my-bucket \
  --versioning-configuration Status=Enabled, MFADelete=Enabled \
  --mfa "arn:aws:iam::123456789012:mfa/root-account-mfa-device 123456"

# Delete specific version with MFA
aws s3api delete-object \
  --bucket my-bucket \
  --key object.txt \
  --version-id abc123 \
  --mfa "arn:aws:iam::123456789012:mfa/root-account-mfa-device 123456"
```

### Lifecycle Rules
```json
{
  "Rules": [
    {
      "ID": "MoveToGlacier",
      "Status": "Enabled",
      "Filter": { "Prefix": "logs/" },
      "Transitions": [
        { "Days": 30, "StorageClass": "STANDARD_IA" },
        { "Days": 90, "StorageClass": "GLACIER" }
      ],
      "Expiration": { "Days": 365 }
    }
  ]
}
```

### Bucket Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowPublicRead",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::my-bucket/*"
    },
    {
      "Sid": "DenyUnencryptedUploads",
      "Effect": "Deny",
      "Principal": "*",
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::my-bucket/*",
      "Condition": {
        "StringNotEquals": {
          "s3:x-amz-server-side-encryption": "aws:kms"
        }
      }
    }
  ]
}
```

## Encryption

| Type        | Description                              | Use Case           |
|-------------|------------------------------------------|--------------------|
| SSE-S3      | AWS managed keys (AES-256)              | Default encryption |
| SSE-KMS     | AWS KMS managed keys                     | Compliance, audit  |
| SSE-C       | Customer-provided keys                   | Key management     |
| Client-side | Encrypt before upload                    | Full control       |

```bash
# Enable default encryption with SSE-KMS
aws s3api put-bucket-encryption \
  --bucket my-bucket \
  --server-side-encryption-configuration '{
    "Rules": [{
      "ApplyServerSideEncryptionByDefault": {
        "SSEAlgorithm": "aws:kms",
        "KMSMasterKeyID": "arn:aws:kms:us-east-1:123456789012:key/my-key"
      }
    }]
  }'
```

## Versioning & Lifecycle

### Object Versioning States
```
Bucket Versioning: Enabled
├── Object1 → v1, v2, v3 (current)
├── Object2 (delete marker) → v1, v2, v3
└── Object3 → v1 (current)
```

### Lifecycle Transitions
```
Day 0: S3 Standard
Day 30: → S3 Standard-IA
Day 90: → S3 Glacier Flexible
Day 365: → Delete
```

## Cross-Region Replication (CRR)

```bash
# Enable CRR
aws s3api put-bucket-replication \
  --bucket my-source-bucket \
  --replication-configuration '{
    "Role": "arn:aws:iam::123456789012:role/S3ReplicationRole",
    "Rules": [{
      "Status": "Enabled",
      "Destination": {
        "Bucket": "arn:aws:s3:::my-destination-bucket"
      }
    }]
  }'
```

## S3 Transfer Acceleration

```bash
# Enable transfer acceleration
aws s3api put-bucket-accelerate-configuration \
  --bucket my-bucket \
  --accelerate-configuration Status=Enabled

# Use with transfer acceleration endpoint
aws s3 cp file.txt s3://my-bucket/file.txt \
  --endpoint-url https://my-bucket.s3-accelerate.amazonaws.com
```

## Pre-Signed URLs

```bash
# Generate pre-signed URL
aws s3 presign s3://my-bucket/private-file.txt \
  --expires-in 3600

# Python example
import boto3
s3 = boto3.client('s3')
url = s3.generate_presigned_url(
    'get_object',
    Params={'Bucket': 'my-bucket', 'Key': 'private-file.txt'},
    ExpiresIn=3600
)
```

## Multi-Object Deletion

```bash
# Delete with prefix
aws s3 rm s3://my-bucket/logs/ --recursive

# Using lifecycle for automated deletion
# (See lifecycle rules above)
```

## Multipart Upload

```bash
# Start multipart upload
aws s3api create-multipart-upload --bucket my-bucket --key large-file.zip

# Upload part
aws s3api upload-part \
  --bucket my-bucket \
  --key large-file.zip \
  --upload-id upload-id \
  --part-number 1 \
  --body part1.bin

# Complete multipart upload
aws s3api complete-multipart-upload \
  --bucket my-bucket \
  --key large-file.zip \
  --upload-id upload-id \
  --multipart-upload '{
    "Parts": [{"PartNumber": 1, "ETag": "etag1"}]
  }'
```

## S3 Event Notifications

```json
{
  "LambdaFunctionConfigurations": [
    {
      "LambdaFunctionArn": "arn:aws:lambda:us-east-1:123456789012:function:MyFunction",
      "Events": ["s3:ObjectCreated:*"],
      "Filter": {
        "Key": { "FilterRules": [{ "Name": "prefix", "Value": "uploads/" }] }
      }
    }
  ]
}
```

## Access Points

```bash
# Create access point
aws s3control create-access-point \
  --account-id 123456789012 \
  --name my-access-point \
  --bucket my-bucket \
  --public-access-block-configuration '{
    "BlockPublicAcls": true,
    "BlockPublicPolicy": true
  }'
```

## S3 Select

```python
import boto3
s3 = boto3.client('s3')
result = s3.select_object_content(
    Bucket='my-bucket',
    Key='data.csv',
    Expression="SELECT * FROM s3object WHERE age > 25",
    ExpressionType='SQL',
    InputSerialization={'CSV': {'FileHeaderInfo': 'Use'}},
    OutputSerialization={'CSV': {}}
)
```

## Performance

### Request Rate Performance
- **Prefixes**: 5,500 GET/HEAD requests per second per prefix
- **Same prefix**: 3,500 PUT/COPY/POST/DELETE per second
- **Best practice**: Use multiple prefixes for high throughput

### Best Practices
1. Use **parallelization** for large datasets
2. Use **multipart upload** for objects > 100MB
3. Use **byte-range fetches** for parallel downloads
4. Implement **exponential backoff** for retries

## Cost Optimization

- Use **Intelligent-Tiering** for unpredictable access
- Set **lifecycle policies** to transition old data
- Use **S3 Storage Lens** for visibility
- Delete **incomplete multipart uploads**
- Enable **S3 Inventory** for analysis

## Security Best Practices

1. **Block public access** at account level
2. Use **bucket policies** with least privilege
3. Enable **access logging**
4. Use **VPC endpoints** for private access
5. Enable **versioning** for recovery
6. Use **MFA Delete** for critical buckets
7. Encrypt data at rest and in transit
8. Use **S3 Access Points** for simplified access management
