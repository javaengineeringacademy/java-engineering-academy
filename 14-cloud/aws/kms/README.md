# AWS KMS (Key Management Service)

## Overview

AWS KMS is a managed service that makes it easy to create and control the cryptographic keys used to protect your data.

## Key Types

| Type                 | Description                          | Use Case              |
|----------------------|--------------------------------------|-----------------------|
| AWS Managed Keys     | Created by AWS services              | Default encryption    |
| Customer Managed     | Created and managed by you           | Custom requirements   |
| HMAC Keys            | Hash-based message authentication    | Data validation       |
| Asymmetric Keys      | RSA/ECC key pairs                    | Digital signatures    |

## Creating Keys

### Customer Managed Key
```bash
aws kms create-key \
  --description "My encryption key" \
  --tags TagKey=Environment,TagValue=production

# Enable key
aws kms enable-key --key-id key-id-12345678
```

### Key Policy
```json
{
  "Version": "2012-10-17",
  "Id": "key-consolepolicy-3",
  "Statement": [
    {
      "Sid": "Enable IAM User Permissions",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::123456789012:root"
      },
      "Action": "kms:*",
      "Resource": "*"
    },
    {
      "Sid": "Allow use of the key",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::123456789012:user/MyUser"
      },
      "Action": [
        "kms:Encrypt",
        "kms:Decrypt",
        "kms:ReEncrypt*",
        "kms:GenerateDataKey*",
        "kms:DescribeKey",
        "kms:CreateGrant"
      ],
      "Resource": "*"
    }
  ]
}
```

## Envelope Encryption

```python
import boto3

kms = boto3.client('kms')

# Generate data key
response = kms.generate_data_key(
    KeyId='alias/my-key',
    KeySpec='AES_256'
)

plaintext_key = response['Plaintext']
encrypted_key = response['CiphertextBlob']

# Encrypt data with plaintext key
encrypted_data = encrypt_with_key(plaintext_key, my_data)

# Store encrypted_data and encrypted_key
```

## Encrypt/Decrypt Operations

### Direct Encryption
```bash
# Encrypt
aws kms encrypt \
  --key-id alias/my-key \
  --plaintext "sensitive data" \
  --output text \
  --query CiphertextBlob

# Decrypt
aws kms decrypt \
  --ciphertext-blob "encrypted-data-base64" \
  --output text \
  --query Plaintext
```

### Generate Data Key
```bash
# Generate data key
aws kms generate-data-key \
  --key-id alias/my-key \
  --key-spec AES_256

# Use data key to encrypt
# Then store encrypted key with data
```

## Key Rotation

```bash
# Enable automatic rotation
aws kms enable-key-rotation --key-id key-id-12345678

# Rotate key manually
aws kms rotate-key-on-demand --key-id key-id-12345678
```

### Rotation Schedule
- **Automatic**: Every 365 days
- **Manual**: On-demand
- **Previous versions**: Retained for decryption

## Grants

```bash
# Create grant
aws kms create-grant \
  --key-id key-id-12345678 \
  --grantee-principal arn:aws:iam::123456789012:user/MyUser \
  --operations "Decrypt" "Encrypt"
```

## Key Aliases

```bash
# Create alias
aws kms create-alias \
  --alias-name alias/my-key \
  --target-key-id key-id-12345678

# Update alias
aws kms update-alias \
  --alias-name alias/my-key \
  --target-key-id new-key-id-12345678
```

## Custom Key Store

```bash
# Create custom key store
aws kms create-custom-key-store \
  --custom-key-store-name my-custom-store \
  --cloudhsm-cluster-id cluster-id-12345678 \
  --custom-key-store-password my-password \
  --key-store-password my-key-store-password
```

## Multi-Region Keys

```bash
# Create multi-region key
aws kms create-key \
  --multi-region

# Create replica in another region
aws kms replicate-key \
  --key-id key-id-12345678 \
  --replica-region us-west-2
```

## Import Key Material

```bash
# Create key without material
aws kms create-key \
  --origin EXTERNAL

# Import key material
aws kms import-key-material \
  --key-id key-id-12345678 \
  --encrypted-key-material fileb://encrypted-key-material.bin \
  --import-token fileb://import-token.bin \
  --expiration-model KEY_MATERIAL_DOES_NOT_EXPIRE
```

## Monitoring

```bash
# Get key rotation status
aws kms get-key-rotation-status --key-id key-id-12345678

# Get key policy
aws kms get-key-policy \
  --key-id key-id-12345678 \
  --policy-name default
```

### CloudTrail Events
- `CreateKey`
- `Encrypt`
- `Decrypt`
- `GenerateDataKey`
- `CreateGrant`
- `EnableKeyRotation`

## Best Practices

1. **Use customer managed keys** for sensitive data
2. **Enable automatic rotation** for all keys
3. **Implement least privilege** key policies
4. **Use grants** for temporary access
5. **Use aliases** for key management
6. **Enable CloudTrail** for auditing
7. **Use VPC endpoints** for private access
8. **Implement key deletion** window
9. **Use multi-region keys** for global apps
10. **Monitor with CloudWatch** metrics
