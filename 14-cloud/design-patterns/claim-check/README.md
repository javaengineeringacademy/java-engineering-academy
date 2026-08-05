# Claim Check Pattern

## Overview

The Claim Check pattern splits a message into a claim check (identifier/reference) and the full message payload. The claim check is sent through the messaging system while the payload is stored in a separate data store. Consumers use the claim check to retrieve the full payload when needed, reducing message size in transit and improving messaging system performance.

## When to Use

- Sending large payloads through messaging systems
- Reducing memory consumption in message brokers
- Complying with message size limits in cloud messaging services
- Storing sensitive data separately from message metadata
- Improving message routing efficiency with smaller messages
- Processing large files asynchronously

## Implementation

### AWS
- S3 for payload storage with SQS claim checks
- SNS message attributes with S3 references
- EventBridge with S3 payload references
- Lambda retrieving payloads from S3 using claim checks

### Azure
- Azure Blob Storage with Service Bus claim checks
- Azure Storage Account with Event Grid references
- Azure Functions retrieving blobs from claim checks
- Azure Cosmos DB for payload storage with SB references

### Google Cloud
- Cloud Storage with Pub/Sub claim checks
- Firestore for payload references with Pub/Sub
- Cloud Functions retrieving objects from claim checks
- BigQuery for large data payloads with messaging

### Kubernetes
- MinIO or Ceph for S3-compatible object storage
- Custom claim check service with message brokers
- Redis for temporary payload caching
- PostgreSQL large object storage with message queues

## Best Practices

1. Store claim checks with the full message metadata
2. Implement TTL on stored payloads to prevent storage bloat
3. Ensure payload storage is accessible to all consumers
4. Consider encryption for sensitive payload data
5. Implement cleanup jobs for orphaned payloads
6. Use appropriate storage tier based on access patterns
7. Monitor storage costs alongside messaging costs

## Interview Questions

1. When should you use claim check versus sending the full message?
2. How do you handle cleanup of stored payloads after consumption?
3. What are the security considerations for payload storage?
4. How would you ensure consistency between claim check and payload?
5. Describe strategies for handling payload storage failures.

## References

- Claim Check Pattern - Microsoft Azure Architecture Center
- Amazon S3 Documentation
- Azure Blob Storage Documentation
- Google Cloud Storage Documentation
- Enterprise Integration Patterns - Gregor Hohpe
- Cloud Design Patterns - Microsoft Azure Architecture Center
