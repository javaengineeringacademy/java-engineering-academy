# AWS Architecture

## Overview

Amazon Web Services (AWS) is a comprehensive cloud platform offering over 200 services. It follows a global infrastructure model with regions, availability zones, and edge locations.

## Global Infrastructure

### Regions

Geographic areas with multiple Availability Zones. Each region is isolated from others for fault tolerance and stability. Choose regions based on compliance, latency, service availability, and cost.

### Availability Zones (AZs)

Distinct data centers within a region, connected by low-latency networks. Each AZ has independent power, networking, and connectivity. Deploy across multiple AZs for high availability.

### Edge Locations

Content delivery network (CDN) endpoints for CloudFront. Used for caching static content closer to users.

## Virtual Private Cloud (VPC)

A logically isolated section of the AWS cloud. Control virtual networking environment including IP address ranges, subnets, route tables, and network gateways.

### VPC Components

- **Subnets**: IP address ranges within a VPC (public or private)
- **Route Tables**: Rules for routing network traffic
- **Internet Gateway**: Connects VPC to the internet
- **NAT Gateway**: Allows private subnet instances to access internet
- **Security Groups**: Stateful firewall rules
- **Network ACLs**: Stateless firewall rules at subnet level

### VPC Architecture

```
Internet
    |
Internet Gateway
    |
+-------------------------------------------+
|                    VPC                     |
|  +-----------+       +-----------+        |
|  | Public    |       | Public    |        |
|  | Subnet-AZ1|       | Subnet-AZ2|        |
|  | (Web)     |       | (Web)     |        |
|  +-----------+       +-----------+        |
|       |                   |               |
|  +-----------+       +-----------+        |
|  | Private   |       | Private   |        |
|  | Subnet-AZ1|       | Subnet-AZ2|        |
|  | (App)     |       | (App)     |        |
|  +-----------+       +-----------+        |
|       |                   |               |
|  +-----------+       +-----------+        |
|  | Private   |       | Private   |        |
|  | Subnet-AZ1|       | Subnet-AZ2|        |
|  | (DB)      |       | (DB)      |        |
|  +-----------+       +-----------+        |
+-------------------------------------------+
```

## Identity and Access Management (IAM)

Controls who can access AWS resources and what they can do.

### Components

- **Users**: Individual identities with credentials
- **Groups**: Collections of users with shared permissions
- **Roles**: Temporary permissions for AWS services or federated users
- **Policies**: JSON documents defining permissions

### Best Practices

- Follow least privilege principle
- Enable MFA for all users
- Use roles for cross-account access
- Rotate credentials regularly
- Use IAM Access Analyzer

## Shared Responsibility Model

### AWS Responsibility (Security OF the Cloud)

- Physical infrastructure (data centers, hardware)
- Network infrastructure
- Host operating system and virtualization layer
- Managed services (RDS, S3, Lambda)

### Customer Responsibility (Security IN the Cloud)

- Data encryption and protection
- Operating system patching (EC2)
- Application security
- Network configuration (Security Groups, NACLs)
- IAM policies and access management
- Client-side and server-side encryption

## Core Services

### Compute

- **EC2**: Virtual servers in the cloud
- **Lambda**: Serverless compute
- **ECS/EKS**: Container orchestration
- **Elastic Beanstalk**: PaaS deployment

### Storage

- **S3**: Object storage
- **EBS**: Block storage for EC2
- **EFS**: Managed file system
- **FSx**: High-performance file systems

### Database

- **RDS**: Managed relational databases
- **DynamoDB**: NoSQL database
- **ElastiCache**: In-memory caching
- **Redshift**: Data warehousing

### Networking

- **VPC**: Virtual private cloud
- **Route 53**: DNS service
- **CloudFront**: CDN
- **Direct Connect**: Dedicated network connection

## High Availability Patterns

### Multi-AZ Deployment

Deploy resources across multiple AZs for fault tolerance. AWS services like RDS and ELB have built-in multi-AZ support.

### Multi-Region Deployment

Deploy across regions for disaster recovery and global reach. Use Route 53 for DNS-based load balancing.

### Auto Scaling

Automatically adjust compute capacity based on demand. EC2 Auto Scaling, ECS Service Auto Scaling, and Aurora Auto Scaling.

## Cost Optimization

### Pricing Models

- **On-Demand**: Pay per hour/second with no commitment
- **Reserved Instances**: 1-3 year commitment for discounts
- **Spot Instances**: Bid for unused capacity (up to 90% discount)
- **Savings Plans**: Flexible pricing for committed usage

### Cost Management Tools

- **Cost Explorer**: Visualize and forecast costs
- **Budgets**: Set spending thresholds
- **Trusted Advisor**: Cost optimization recommendations
- **Compute Optimizer**: Right-sizing recommendations

## Security Architecture

### Defense in Depth

1. **Edge**: WAF, Shield, CloudFront
2. **Network**: VPC, Security Groups, NACLs
3. **Compute**: EC2 security, ECS task roles
4. **Data**: S3 encryption, RDS encryption
5. **Application**: Code security, dependency scanning
6. **Identity**: IAM, MFA, SSO

### Compliance

AWS maintains compliance certifications for GDPR, HIPAA, PCI DSS, SOC, ISO, and more. Use AWS Artifact for compliance reports.
