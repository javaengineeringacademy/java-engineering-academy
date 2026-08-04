# Google Cloud Functions

## Overview

Cloud Functions is a serverless execution environment for building and connecting cloud services.

## Runtime Support

| Runtime     | Version | Use Case              |
|-------------|---------|------------------------|
| Node.js     | 18/20   | JavaScript apps        |
| Python      | 3.11/3.12 | General purpose      |
| Go          | 1.21    | High performance       |
| Java        | 17      | Enterprise apps        |
| .NET        | 6       | Microsoft ecosystem    |
| Ruby        | 3.3     | Scripting              |
| PHP         | 8.2     | Web applications       |

## Gen2 Functions

```bash
# Deploy gen2 function
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http \
  --allow-unauthenticated
```

### Gen2 Features
- **Eventarc triggers** - Event-driven architecture
- **Cloud Run integration** - Built on Cloud Run
- **Up to 60 minutes** - Longer execution time
- **1GB memory** - More memory available
- **Minimum instances** - Keep instances warm

## Gen1 Functions

```bash
# Deploy gen1 function
gcloud functions deploy my-function \
  --runtime=nodejs20 \
  --region=us-central1 \
  --trigger-http \
  --allow-unauthenticated
```

## Trigger Types

| Trigger              | Use Case                    |
|----------------------|-----------------------------|
| HTTP                 | REST API, webhooks          |
| Cloud Storage        | File processing             |
| Pub/Sub              | Event processing            |
| Firestore            | Database triggers           |
| Cloud Scheduler      | Cron jobs                   |
| Firebase             | Mobile app events           |

### HTTP Trigger
```javascript
exports.helloWorld = (req, res) => {
  res.send('Hello World!');
};
```

### Cloud Storage Trigger
```bash
gcloud functions deploy processUpload \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --trigger-event-filters="type=google.cloud.storage.object.v1.finalized" \
  --trigger-event-filters="bucket=my-bucket" \
  --source=. \
  --entry-point=processUpload
```

### Pub/Sub Trigger
```bash
gcloud functions deploy processMessage \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --trigger-topic=my-topic \
  --source=. \
  --entry-point=processMessage
```

### Firestore Trigger
```bash
gcloud functions deploy firestoreTrigger \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --trigger-event-filters="type=google.cloud.firestore.document.v1.created" \
  --trigger-event-filters="database=(default)" \
  --trigger-event-filters="document=users/{userId}" \
  --source=. \
  --entry-point=onUserCreated
```

## Function Configuration

```bash
# Configure memory and timeout
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --memory=512MB \
  --timeout=300s \
  --min-instances=1 \
  --max-instances=10 \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http
```

## Environment Variables

```bash
# Set environment variables
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --set-env-vars="DB_HOST=mydb,API_KEY=abc123" \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http
```

## VPC Connector

```bash
# Deploy with VPC connector
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --vpc-connector=my-connector \
  --egress-settings=all \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http
```

## Cloud Run Integration

### Benefits
- **Consistent platform** with Cloud Run
- **Eventarc triggers** for event-driven
- **Better scaling** and networking
- **Traffic splitting** for canary deployments

## Identity & Access

```bash
# Deploy with service account
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --service-account=my-sa@my-project.iam.gserviceaccount.com \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http

# Deploy with IAM
gcloud functions add-iam-policy-binding my-function \
  --region=us-central1 \
  --member="user:user@example.com" \
  --role="roles/cloudfunctions.invoker"
```

## Monitoring

```bash
# Get function logs
gcloud functions logs read my-function --region=us-central1

# Get function metrics
gcloud monitoring metrics list \
  --filter='metric.type="cloudfunctions.googleapis.com/function/execution_count"'
```

## Deployment Strategies

### Rolling Updates
```bash
# Deploy new version
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http
```

### Traffic Splitting
```bash
# Deploy with traffic splitting
gcloud functions deploy my-function \
  --gen2 \
  --runtime=nodejs20 \
  --region=us-central1 \
  --source=. \
  --entry-point=helloWorld \
  --trigger-http \
  --traffic-split=100=REV-001,0=REV-002
```

## Best Practices

1. **Use gen2 functions** for new deployments
2. **Implement proper error handling**
3. **Use environment variables** for configuration
4. **Set appropriate timeouts**
5. **Implement retries** for idempotent operations
6. **Use VPC connectors** for private resources
7. **Monitor with Cloud Logging**
8. **Set min instances** for latency-sensitive apps
9. **Use Cloud Tasks** for async processing
10. **Implement proper IAM** roles
