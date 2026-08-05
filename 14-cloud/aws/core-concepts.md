# AWS Core Concepts

## EC2 (Elastic Compute Cloud)

Virtual servers in the cloud. Provides resizable compute capacity with complete control over networking and security.

### Instance Types

- **General Purpose (m5, m6i)**: Balanced compute, memory, and networking
- **Compute Optimized (c5, c6i)**: CPU-intensive workloads
- **Memory Optimized (r5, r6i)**: Memory-intensive workloads
- **Storage Optimized (i3, d2)**: High sequential read/write
- **Accelerated Computing (p4, g4)**: GPU workloads

### Key Features

- Auto Scaling Groups for automatic scaling
- Elastic Load Balancing for traffic distribution
- Placement Groups for low-latency networking
- ENAs for enhanced networking

## S3 (Simple Storage Service)

Object storage with 99.999999999% durability. Scalable, secure, and cost-effective storage for any data type.

### Storage Classes

- **S3 Standard**: Frequently accessed data
- **S3 Intelligent-Tiering**: Automatic cost optimization
- **S3 Standard-IA**: Infrequent access
- **S3 One Zone-IA**: Single AZ infrequent access
- **S3 Glacier**: Archive storage
- **S3 Glacier Deep Archive**: Long-term archive

### Features

- Versioning for data protection
- Lifecycle policies for cost optimization
- Cross-region replication
- Event notifications
- S3 Access Points for easier access management

## RDS (Relational Database Service)

Managed relational database service supporting MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and Aurora.

### Features

- Automated backups and point-in-time recovery
- Multi-AZ deployments for high availability
- Read replicas for read scaling
- Automated patching and maintenance
- Encryption at rest and in transit

### Aurora

AWS-built relational database compatible with MySQL and PostgreSQL. Offers 5x MySQL and 3x PostgreSQL performance.

## Lambda

Serverless compute service that runs code in response to events without provisioning servers.

### Features

- Automatic scaling from 0 to thousands of instances
- Pay only for compute time used
- Supports Node.js, Python, Java, Go, .NET, Ruby
- Integrated with other AWS services
- Event-driven architecture

### Use Cases

- API backends with API Gateway
- Real-time file processing
- Stream processing with Kinesis
- Scheduled tasks (cron jobs)
- IoT backend

## VPC (Virtual Private Cloud)

Isolated virtual network for AWS resources. Complete control over IP address ranges, subnets, and routing.

### Components

- Subnets (public and private)
- Route tables
- Internet Gateway
- NAT Gateway/Instance
- Security Groups
- Network ACLs
- VPC Peering
- Transit Gateway

## IAM (Identity and Access Management)

Controls access to AWS services and resources securely.

### Components

- Users, Groups, Roles, and Policies
- Federated access
- Multi-factor authentication
- Access Analyzer
- Service Control Policies (SCPs)

## Route 53

Highly available DNS web service. Routes end-user requests to internet applications.

### Features

- Domain registration
- DNS routing (simple, weighted, latency, failover, geolocation)
- Health checks
- Traffic flow for visual routing
- DNSSEC for security

## CloudFront

Content Delivery Network (CDN) service. Delivers content through a global network of edge locations.

### Features

- Low latency content delivery
- DDoS protection with Shield Standard
- Lambda@Edge for edge computing
- Origin access control
- Real-time logs

## ECS (Elastic Container Service)

Managed container orchestration service. Supports Docker containers and Fargate serverless compute.

### Launch Types

- **EC2**: Manage your own EC2 instances
- **Fargate**: Serverless compute for containers

### Features

- Service discovery and load balancing
- Auto Scaling
- Rolling and blue/green deployments
- Task roles for IAM permissions

## EKS (Elastic Kubernetes Service)

Managed Kubernetes control plane. Runs and manages Kubernetes clusters on AWS.

### Features

- Managed control plane
- Integration with IAM
- Fargate support for serverless pods
- EBS and EFS storage drivers
- ALB Ingress controller

## SQS (Simple Queue Service)

Fully managed message queuing service. Enables decoupling and scaling of microservices.

### Queue Types

- **Standard**: Unlimited throughput, at-least-once delivery
- **FIFO**: First-in-first-out ordering, exactly-once processing

## SNS (Simple Notification Service)

Fully managed pub/sub messaging service. Sends notifications to subscribers.

### Features

- Email, SMS, HTTP/S, Lambda, SQS delivery
- Fanout pattern to multiple subscribers
- Message filtering
- Mobile push notifications

## DynamoDB

Fully managed NoSQL database with single-digit millisecond performance.

### Features

- Automatic scaling
- Global tables for multi-region replication
- Point-in-time recovery
- DynamoDB Accelerator (DAX) for caching
- Event-driven programming with DynamoDB Streams

## CloudWatch

Monitoring and observability service. Collects metrics, logs, and events.

### Features

- Metrics and alarms
- CloudWatch Logs for log management
- CloudWatch Events for event-driven automation
- Dashboard for visualization
- Contributor Insights for analysis

## CloudTrail

Tracks user activity and API usage. Provides governance, compliance, and operational auditing.

### Features

- API call logging
- CloudTrail Lake for analytics
- Integration with CloudWatch Logs
- Multi-region and organization trails
- Insights for anomalous activity
