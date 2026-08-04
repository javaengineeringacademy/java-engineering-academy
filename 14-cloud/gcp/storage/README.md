# Google Cloud Storage

## Overview

Google Cloud Storage (GCS) is a fully managed, highly durable object storage service.

## Storage Classes

| Class               | Access           | Minimum Duration | Use Case                    |
|---------------------|------------------|------------------|-----------------------------|
| Standard            | Frequent         | None             | Frequently accessed data    |
| Nearline            | Infrequent       | 30 days          | Monthly access              |
| Coldline            | Rare             | 90 days          | Quarterly access            |
| Archive             | Rare             | 365 days         | Long-term archive           |

## Creating Buckets

### gcloud CLI
```bash
# Create bucket
gsutil mb -l us-central1 gs://my-bucket

# Create with specific storage class
gsutil mb -s nearline -l us-central1 gs://my-nearline-bucket

# Set default storage class
gsutil defstorageclass set STANDARD gs://my-bucket
```

### Terraform
```hcl
resource "google_storage_bucket" "default" {
  name          = "my-bucket"
  location      = "US"
  storage_class = "STANDARD"

  uniform_bucket_level_access = true

  lifecycle_rule {
    condition {
      age = 30
    }
    action {
      type = "SetStorageClass"
      storage_class = "NEARLINE"
    }
  }
}
```

## Object Versioning

```bash
# Enable versioning
gsutil versioning set on gs://my-bucket

# List object versions
gsutil ls -a gs://my-bucket

# Restore previous version
gsutil cp gs://my-bucket/file.txt#1234567890 gs://my-bucket/file.txt
```

## Lifecycle Rules

```json
{
  "lifecycle": {
    "rule": [
      {
        "action": {
          "type": "SetStorageClass",
          "storageClass": "NEARLINE"
        },
        "condition": {
          "age": 30,
          "isLive": true
        }
      },
      {
        "action": {
          "type": "Delete"
        },
        "condition": {
          "age": 365,
          "isLive": false
        }
      }
    ]
  }
}
```

### Apply Lifecycle Rules
```bash
# Create lifecycle policy
gsutil lifecycle set lifecycle.json gs://my-bucket

# Get lifecycle policy
gsutil lifecycle get gs://my-bucket
```

## Object Lifecycle Management

### Set Object Class
```bash
# Change storage class
gsutil setmeta -h "Content-Type:text/plain" gs://my-bucket/file.txt
```

### Delete Old Versions
```bash
# Delete old versions
gsutil -m rm -r gs://my-bucket/**#1234567890
```

## Access Control

### IAM Policies
```bash
# Grant access to user
gsutil iam ch user:user@example.com:objectViewer gs://my-bucket

# Grant access to service account
gsutil iam ch serviceAccount:my-sa@my-project.iam.gserviceaccount.com:objectAdmin gs://my-bucket
```

### ACLs (Legacy)
```bash
# Set ACL
gsutil acl set public-read gs://my-bucket/file.txt

# Get ACL
gsutil acl get gs://my-bucket/file.txt
```

## Encryption

### Google-Managed Keys
```bash
# Default encryption (automatic)
gsutil cp file.txt gs://my-bucket/
```

### Customer-Managed Keys (CMEK)
```bash
# Create KMS key
gcloud kms keys create my-key \
  --keyring=my-keyring \
  --location=us-central1 \
  --purpose=encryption

# Use CMEK
gsutil -h "x-goog-encryption-key:$(gcloud kms decrypt \
  --key=my-key \
  --keyring=my-keyring \
  --location=us-central1 \
  --plaintext-file=key.txt)" \
  cp file.txt gs://my-bucket/
```

### Customer-Supplied Keys (CSEK)
```bash
# Use CSEK
gsutil -h "x-goog-encryption-key:base64-encoded-key" \
  cp file.txt gs://my-bucket/
```

## Object Lifecycle

### Object States
```
Live → Inaccessible (after retention) → Deleted
  ↓
Soft-deleted → Hard-deleted
```

### Retention Policies
```bash
# Set retention policy
gsutil retention set 30d gs://my-bucket

# Lock retention policy
gsutil retention lock gs://my-bucket
```

## Requester Pays

```bash
# Enable requester pays
gsutil requesterpays set on gs://my-bucket

# Access with requester pays
gsutil -h "x-goog-user-project:my-project" ls gs://my-bucket/
```

## Website Hosting

```bash
# Configure website
gsutil web set -m index.html -e 404.html gs://my-bucket

# Make bucket public
gsutil iam ch allUsers:objectViewer gs://my-bucket
```

## Object Holds

```bash
# Set temporary hold
gsutil retention temp set gs://my-bucket/file.txt

# Set event-based hold
gsutil retention event-based set gs://my-bucket/file.txt
```

## Multi-Regional Storage

```bash
# Create multi-regional bucket
gsutil mb -l us gs://my-multiregional-bucket

# Benefits:
# - 99.999999999% availability
# - Geo-redundancy
# - Low latency globally
```

## Performance

### Parallel Composite Uploads
```bash
# Upload large files
gsutil -m cp -r gs://my-bucket/large-file.tar.gz
```

### Multipart Upload
```bash
# Automatic with gsutil
gsutil cp large-file.tar.gz gs://my-bucket/
```

## Cost Optimization

- **Choose appropriate storage class** based on access patterns
- **Implement lifecycle rules** to transition data
- **Delete old versions** and objects
- **Use dual-region** for high availability
- **Monitor storage usage** with Cloud Monitoring

## Best Practices

1. **Use appropriate storage class**
2. **Enable versioning** for data protection
3. **Implement lifecycle rules**
4. **Use CMEK** for encryption
5. **Set retention policies** for compliance
6. **Use IAM** for access control
7. **Enable logging** for auditing
8. **Use object labels** for organization
9. **Implement lifecycle policies**
10. **Monitor with Cloud Monitoring**
