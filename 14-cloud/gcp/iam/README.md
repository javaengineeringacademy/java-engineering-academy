# Google Cloud IAM

## Overview

Cloud IAM lets you manage access to Google Cloud resources.

## IAM Hierarchy

```
Organization
    │
    └── Folders
            │
            └── Projects
                    │
                    └── Resources
```

## Roles

### Basic Roles
| Role              | Description                    |
|-------------------|--------------------------------|
| Owner             | Full access                    |
| Editor            | Read/write access              |
| Viewer            | Read-only access               |

### Predefined Roles
```bash
# List roles
gcloud iam roles list

# Get role details
gcloud iam roles describe roles/compute.instanceAdmin
```

### Custom Roles
```bash
# Create custom role
gcloud iam roles create my-role \
  --title="My Custom Role" \
  --permissions=compute.instances.list,compute.instances.get \
  --stage=GA
```

## Service Accounts

```bash
# Create service account
gcloud iam service-accounts create my-sa \
  --display-name="My Service Account"

# Create key
gcloud iam service-accounts keys create key.json \
  --iam-account=my-sa@my-project.iam.gserviceaccount.com

# Grant role
gcloud projects add-iam-policy-binding my-project \
  --member="serviceAccount:my-sa@my-project.iam.gserviceaccount.com" \
  --role="roles/storage.objectViewer"
```

## IAM Policies

```json
{
  "bindings": [
    {
      "role": "roles/storage.objectViewer",
      "members": [
        "user:user@example.com",
        "serviceAccount:my-sa@my-project.iam.gserviceaccount.com"
      ],
      "condition": {
        "title": "Expires 2024-12-31",
        "description": "Expires at end of 2024",
        "expression": "request.time < timestamp('2025-01-01T00:00:00Z')"
      }
    }
  ]
}
```

## IAM Conditions

```python
# Time-based condition
condition = {
    "title": "Expires 2024-12-31",
    "description": "Expires at end of 2024",
    "expression": "request.time < timestamp('2025-01-01T00:00:00Z')"
}

# Resource-based condition
condition = {
    "title": "Only production buckets",
    "description": "Only access production buckets",
    "expression": "resource.name.startsWith('projects/_/buckets/prod-')"
}
```

## Workforce Identity

```bash
# Create workforce pool
gcloud iam workforce-pools create my-pool \
  --location=global \
  --display-name="My Workforce Pool"

# Create workforce provider
gcloud iam workforce-pools providers create-oidc my-provider \
  --workforce-pool=my-pool \
  --issuer-uri="https://accounts.google.com" \
  --allowed-audiences="my-client-id"
```

## Workload Identity Federation

```bash
# Create workload identity pool
gcloud iam workload-identity-pools create my-pool \
  --location=global

# Create provider
gcloud iam workload-identity-pools providers create-oidc my-provider \
  --workforce-pool-id=my-pool \
  --issuer-uri="https://token.actions.githubusercontent.com" \
  --allowed-audiences="https://github.com/my-org"
```

## Audit Logging

```bash
# Enable audit logs
gcloud projects get-iam-policy my-project

# View audit logs
gcloud logging read 'protoPayload.serviceName="cloudresourcemanager.googleapis.com"' \
  --limit=100
```

## IAM Recommender

```bash
# Get recommendations
gcloud iam recommendations list \
  --project=my-project

# Apply recommendation
gcloud iam recommendations apply RECOMMENDATION_ID
```

## Deny Policies

```bash
# Create deny policy
gcloud resource-manager org-policies set-policy my-project \
  --policy=deny-policy.json
```

## Pricing

- **IAM**: Free
- **Workforce Identity**: Free
- **Workload Identity Federation**: Free

## Best Practices

1. **Use least privilege** principle
2. **Use predefined roles** over basic roles
3. **Implement service accounts** for applications
4. **Use IAM Conditions** for temporary access
5. **Enable audit logging**
6. **Regularly review IAM policies**
7. **Use Workforce Identity** for workforce
8. **Implement proper key management**
9. **Use IAM Recommender** for optimization
10. **Implement deny policies** for restrictions
