## Azure Storage

Cloud storage services including Blob, Queue, Table, and File storage for .NET applications.

## Overview

Azure Storage provides durable, highly available, massively scalable cloud storage. It includes Blob (objects), Queue (messaging), Table (NoSQL), and File (file shares) services.

## Why It Matters

- Durable and highly available storage
- Massive scalability for any data size
- Cost-effective tiering options
- Integration with all Azure services
- REST API and SDK support

## Key Concepts

- **Blob Storage**: Object storage for files, images, videos
- **Queue Storage**: Simple message queuing
- **Table Storage**: NoSQL key-value store
- **File Storage**: SMB file shares in the cloud
- **Storage Account**: Top-level container for all storage
- **Access Tiers**: Hot, Cool, Cold, Archive

## Core Topics

- Blob containers and blobs
- Queue messaging patterns
- Table Storage entity operations
- File share mounting
- Access tiers and lifecycle management
- Shared access signatures (SAS)
- Azure Storage SDK for .NET

## Best Practices

- Use appropriate access tiers for cost optimization
- Implement SAS tokens for temporary access
- Use managed identities instead of connection strings
- Enable soft delete for data protection
- Use lifecycle management for blob archival

## Hands-on Labs

- Upload and download blobs
- Send and receive queue messages
- Query Table Storage entities
- Mount Azure Files in a container

## Interview Questions

1. What are the different Azure Storage services?
2. What is a Shared Access Signature?
3. How do access tiers work in Blob Storage?

## References

- https://learn.microsoft.com/azure/storage/
- https://learn.microsoft.com/azure/storage/blobs/
- https://learn.microsoft.com/azure/storage/queues/
