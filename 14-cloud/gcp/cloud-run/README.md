# Google Cloud Run

## Overview

Cloud Run is a fully managed compute platform for deploying containerized applications.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                    Cloud Run                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Service │  │ Revision │  │  Route   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Container    │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Deploying Services

### Container Image
```bash
# Deploy container
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

### From Source
```bash
# Deploy from source
gcloud run deploy my-service \
  --source . \
  --platform managed \
  --region us-central1
```

## Service Configuration

```bash
# Deploy with full configuration
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --platform managed \
  --region us-central1 \
  --memory 512Mi \
  --cpu 2 \
  --min-instances 1 \
  --max-instances 10 \
  --concurrency 80 \
  --timeout 300 \
  --port 8080 \
  --set-env-vars "DB_HOST=mydb,API_KEY=abc123" \
  --service-account my-sa@my-project.iam.gserviceaccount.com
```

## Revisions

```bash
# Deploy new revision
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:v2 \
  --no-traffic

# Get revisions
gcloud run revisions list --service my-service --region us-central1

# Route traffic to revision
gcloud run services update-traffic my-service \
  --region us-central1 \
  --to-revisions my-service-00001-abc=100
```

## Traffic Splitting

### Canary Deployment
```bash
# Deploy new revision with 10% traffic
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:v2 \
  --no-traffic

# Route 10% to new revision
gcloud run services update-traffic my-service \
  --region us-central1 \
  --to-revisions my-service-00001-abc=90,my-service-00002-def=10
```

### Blue-Green Deployment
```bash
# Deploy new revision
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:v2 \
  --no-traffic

# Switch all traffic
gcloud run services update-traffic my-service \
  --region us-central1 \
  --to-revisions my-service-00002-def=100
```

## Scaling Configuration

```bash
# Configure scaling
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --min-instances 0 \
  --max-instances 100 \
  --concurrency 80
```

### Scaling Behavior
| Setting         | Description                    |
|-----------------|--------------------------------|
| min-instances   | Minimum instances running      |
| max-instances   | Maximum instances allowed      |
| concurrency     | Requests per instance          |

## VPC Connector

```bash
# Deploy with VPC connector
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --vpc-connector my-connector \
  --vpc-egress all-traffic
```

## Identity & Access

```bash
# Deploy with service account
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --service-account my-sa@my-project.iam.gserviceaccount.com

# Set IAM policy
gcloud run services add-iam-policy-binding my-service \
  --region us-central1 \
  --member="user:user@example.com" \
  --role="roles/run.invoker"
```

## Domain Mapping

```bash
# Map domain
gcloud run domain-mappings create \
  --service my-service \
  --domain api.example.com \
  --region us-central1

# Manage DNS records
gcloud run domain-mappings describe \
  --domain api.example.com \
  --region us-central1
```

## Managed SSL

```bash
# Enable managed SSL
gcloud run services update my-service \
  --region us-central1 \
  --managed-certificates my-cert
```

## Secret Manager Integration

```bash
# Deploy with secrets
gcloud run deploy my-service \
  --image gcr.io/my-project/my-image:latest \
  --set-secrets "DB_PASSWORD=db-password:latest"
```

## Eventarc Triggers

```bash
# Create trigger
gcloud eventarc triggers create my-trigger \
  --location us-central1 \
  --event-filters "type=google.cloud.storage.object.v1.finalized" \
  --event-filters "bucket=my-bucket" \
  --destination-run-service my-service \
  --destination-run-region us-central1 \
  --service-account my-sa@my-project.iam.gserviceaccount.com
```

## Monitoring

```bash
# Get service metrics
gcloud monitoring metrics list \
  --filter='metric.type="run.googleapis.com/request_count"'

# Get service logs
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=my-service" --limit 100
```

## Pricing

| Feature         | Free Tier        | Cost            |
|-----------------|------------------|-----------------|
| CPU             | 180,000 vCPU-sec | $0.00002400/vCPU-sec |
| Memory          | 360,000 GiB-sec  | $0.00000250/GiB-sec |
| Requests        | 2 million/month  | $0.40/million   |

## Best Practices

1. **Use revisions** for safe deployments
2. **Implement traffic splitting** for canary
3. **Set appropriate concurrency** limits
4. **Use service accounts** for security
5. **Implement health checks**
6. **Use VPC connectors** for private resources
7. **Monitor with Cloud Logging**
8. **Use managed certificates** for HTTPS
9. **Implement proper scaling** policies
10. **Use Cloud Tasks** for async processing
