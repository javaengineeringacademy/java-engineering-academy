# AWS Certified Solutions Architect - Associate

Validates ability to design and implement well-architected solutions on AWS. The most sought-after cloud certification for architects and engineers building scalable, resilient systems.

## Table of Contents

- [Exam Details](#exam-details)
- [Domains and Weights](#domains-and-weights)
- [Key Services](#key-services)
- [Design Principles](#design-principles)
- [Deep Dive by Domain](#deep-dive-by-domain)
- [Study Plan](#study-plan)
- [Architecture Patterns](#architecture-patterns)
- [Resources](#resources)

---

## Exam Details

| Attribute | Detail |
|-----------|--------|
| Code | SAA-C03 |
| Duration | 130 minutes |
| Questions | 65 |
| Passing Score | 720/1000 |
| Cost | $150 |
| Format | Multiple choice, multiple answer |
| Delivery | Pearson VUE, online proctored |

---

## Domains and Weights

1. **Secure Architectures (30%)** - IAM, encryption, network security, data protection
2. **Resilient Architectures (26%)** - High availability, disaster recovery, fault tolerance
3. **High-Performing Architectures (24%)** - Compute, storage, database, network optimization
4. **Cost-Optimized Architectures (20%)** - Pricing models, cost control, resource right-sizing

---

## Key Services

### Compute

- **EC2** - Virtual machines, instance types, placement groups
- **Lambda** - Serverless compute, event-driven processing
- **ECS/EKS** - Container orchestration
- **Auto Scaling** - Dynamic capacity management

### Storage

- **S3** - Object storage, lifecycle policies, replication
- **EBS** - Block storage, snapshots, volume types
- **EFS** - Managed NFS file systems
- **Storage Gateway** - Hybrid cloud storage
- **Snow Family** - Data transfer devices

### Database

- **RDS** - Managed relational databases, Multi-AZ, read replicas
- **DynamoDB** - Managed NoSQL, DAX caching
- **ElastiCache** - Redis and Memcached
- **Aurora** - High-performance relational database
- **Redshift** - Data warehousing

### Networking

- **VPC** - Subnets, route tables, NACLs, security groups
- **Route 53** - DNS, routing policies, health checks
- **CloudFront** - CDN, edge locations, caching
- **ELB** - Application, Network, and Gateway load balancers
- **Direct Connect** - Dedicated network connections

### Security

- **IAM** - Users, groups, roles, policies
- **KMS** - Encryption key management
- **WAF** - Web application firewall
- **Shield** - DDoS protection
- **GuardDuty** - Threat detection

---

## Design Principles

### AWS Well-Architected Framework

1. **Operational Excellence** - Automate and improve processes
2. **Security** - Protect data and systems
3. **Reliability** - Recover from failures, meet demand
4. **Performance Efficiency** - Use resources efficiently
5. **Cost Optimization** - Avoid unnecessary costs
6. **Sustainability** - Minimize environmental impact

### Architecture Decision Factors

- **Requirements** - Functional and non-functional needs
- **Constraints** - Budget, timeline, compliance
- **Trade-offs** - Cost vs performance vs reliability
- **Scalability** - Horizontal vs vertical scaling
- **Decoupling** - Reducing service dependencies

---

## Deep Dive by Domain

### Secure Architectures (30%)

Design least-privilege IAM policies. Implement encryption at rest and in transit using KMS. Configure VPC security with NACLs and security groups. Use WAF for application-layer protection. Implement secrets management with Secrets Manager and Parameter Store.

### Resilient Architectures (26%)

Design multi-AZ and multi-region architectures. Implement auto scaling for all tiers. Use S3 cross-region replication for durability. Configure Route 53 failover routing. Design disaster recovery strategies: backup/restore, pilot light, warm standby, multi-site.

### High-Performing Architectures (24%)

Choose appropriate instance types for workloads. Use CloudFront for content delivery. Select optimal database engines for use cases. Implement caching with ElastiCache and DAX. Use placement groups for low-latency networking.

### Cost-Optimized Architectures (20%)

Compare Reserved Instances, Savings Plans, and Spot Instances. Use S3 storage classes strategically. Right-size instances using CloudWatch metrics. Implement auto scaling to match demand. Review AWS Pricing Calculator estimates.

---

## Study Plan

### Weeks 1-2: Core Compute and Storage

- EC2 instance types, pricing models, placement groups
- S3 storage classes, lifecycle policies, replication
- EBS volume types, snapshots, encryption
- EFS and FSx for file storage
- Practice: Design a multi-tier web architecture

### Weeks 3-4: Networking and Databases

- VPC design: subnets, routing, NACLs, security groups
- ELB types and configurations
- Route 53 routing policies
- RDS: Multi-AZ, read replicas, Aurora
- DynamoDB: capacity modes, DAX, global tables
- Practice: Design a VPC for a three-tier application

### Weeks 5-6: Security and High Availability

- IAM deep dive: policies, roles, federation
- KMS and encryption strategies
- WAF and Shield configurations
- Auto Scaling groups and launch templates
- Disaster recovery strategies
- Practice: Implement a highly available architecture

### Weeks 7-8: Cost Optimization and Integration

- Pricing models comparison
- Cost optimization tools and practices
- SQS, SNS, EventBridge for decoupling
- CloudFront and global infrastructure
- Practice: Optimize costs for a given workload

### Weeks 9-10: Practice Exams and Review

- Take 5+ full practice exams
- Review every incorrect answer
- Create flashcards for key differences
- Final review of architecture patterns

---

## Architecture Patterns

### Common Patterns

| Pattern | Use Case | AWS Services |
|---------|----------|--------------|
| Web Application | Public-facing app | ELB, EC2/ECS, RDS, S3 |
| Serverless API | REST/GraphQL API | API Gateway, Lambda, DynamoDB |
| Data Lake | Large-scale analytics | S3, Glue, Athena, Redshift |
| Event Processing | Async workflows | SQS, Lambda, DynamoDB |
| Hybrid Connectivity | On-premises integration | Direct Connect, VPN, Storage Gateway |

---

## Resources

- **AWS Well-Architected Labs**: Hands-on architecture practice
- **A Cloud Guru**: SAA-C03 comprehensive course
- **Stephane Maarek**: SAA-C03 on Udemy
- **Tutorials Dojo**: Practice exams and cheat sheets
- **AWS re:Invent**: Architecture session recordings

---

**Last Updated**: August 2026
