# Azure Core Concepts

## Overview

Azure offers a vast array of services for compute, data, networking, and application development. This guide covers the foundational services used in most Azure architectures.

## Virtual Machines (VMs)

VMs provide Infrastructure-as-a-Service (IaaS) for running workloads with full OS control.

- Choose VM sizes based on CPU, memory, and storage needs
- Use managed disks for persistent storage
- Configure availability sets for high availability
- Supported OS: Windows Server, Ubuntu, RHEL, SUSE, and more

### VM Scale Sets

VM Scale Sets automatically increase or decrease the number of VM instances based on demand or a defined schedule.

## Azure App Service

App Service is a fully managed Platform-as-a-Service (PaaS) for hosting web apps, REST APIs, and mobile backends.

- Supports .NET, Java, Node.js, Python, PHP, Ruby
- Built-in auto-scaling, load balancing, and SSL
- Deployment slots for zero-downtime deployments
- Managed identities for secure resource access

## Azure Kubernetes Service (AKS)

AKS simplifies running Kubernetes by managing the control plane.

- Free Kubernetes control plane (you pay for worker nodes)
- Integrated Azure AD, monitoring, and networking
- Node pools with automatic or manual scaling
- Azure CNI or kubenet networking options

## Cosmos DB

Cosmos DB is a globally distributed, multi-model NoSQL database.

- Single-digit millisecond latency at the 99th percentile
- Five consistency models from strong to eventual
- Native support for documents, key-value, graph, and column-family
- Global distribution with multi-master writes

### Throughput Modes

- **Provisioned** - Fixed RU/s capacity per second
- **Autoscale** - Automatically adjust RU/s within a range
- **Serverless** - Pay-per-request model for variable workloads

## Azure Functions

Functions is a serverless compute service for event-driven code execution.

- Trigger types: HTTP, Timer, Queue, Blob, Event Hub, Service Bus
- Supports C#, Java, JavaScript, Python, PowerShell
- Durable Functions for stateful workflows
- Consumption and Premium hosting plans

## Azure SQL Database

Fully managed relational database based on SQL Server.

- Built-in intelligence for performance tuning
- Automatic backups, patching, and failover
- Elastic pools for managing multiple databases
- Serverless compute tier for auto-pause and resume

## Azure Blob Storage

Object storage optimized for massive amounts of unstructured data.

- **Hot tier** - Frequently accessed data
- **Cool tier** - Infrequently accessed, 30-day minimum retention
- **Archive tier** - Rarely accessed, 180-day minimum retention
- Supports static websites, versioning, and lifecycle policies

## Azure CDN

Content Delivery Network for caching and delivering content at edge locations.

- Custom domain and SSL support
- Rules engine for caching behavior
- Integration with Blob Storage and App Service

## Service Bus

Enterprise message broker supporting queues and publish-subscribe topics.

- Reliable message delivery with sessions and transactions
- Dead-letter queues for failed messages
- Topics with subscription filters
