# AWS Interview Questions

## 1. What is AWS and its main services?

Amazon Web Services (AWS) is a comprehensive cloud platform offering over 200 services. Main services include EC2 (compute), S3 (storage), RDS (database), Lambda (serverless), VPC (networking), and IAM (security).

## 2. Explain the Shared Responsibility Model

AWS is responsible for security OF the cloud (infrastructure, hardware, networking). Customers are responsible for security IN the cloud (data, encryption, OS patching, IAM).

## 3. What is a VPC?

Virtual Private Cloud is a logically isolated section of AWS cloud. It includes subnets, route tables, internet gateways, NAT gateways, security groups, and network ACLs.

## 4. What are Security Groups vs Network ACLs?

Security Groups are stateful and operate at instance level. Network ACLs are stateless and operate at subnet level. Security groups allow only allow rules; NACLs allow both allow and deny rules.

## 5. What is S3 and its storage classes?

Simple Storage Service for object storage. Classes include S3 Standard, Intelligent-Tiering, Standard-IA, One Zone-IA, Glacier, and Glacier Deep Archive for different access patterns and costs.

## 6. What is EC2 and instance types?

Elastic Compute Cloud provides virtual servers. Instance types include General Purpose (M-series), Compute Optimized (C-series), Memory Optimized (R-series), Storage Optimized (I-series), and Accelerated Computing (P/G-series).

## 7. What is Lambda?

Serverless compute service that runs code without provisioning servers. Supports automatic scaling, pay-per-use pricing, and integrates with many AWS services.

## 8. What is IAM?

Identity and Access Management controls access to AWS resources. Components include Users, Groups, Roles, and Policies. Follows least privilege principle.

## 9. What are IAM Roles?

IAM Roles provide temporary credentials for AWS services or users. Used for EC2 instances, Lambda functions, ECS tasks, and cross-account access.

## 10. What is RDS?

Relational Database Service is a managed database service supporting MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and Aurora. Features include automated backups, Multi-AZ, and read replicas.

## 11. What is DynamoDB?

Fully managed NoSQL database with single-digit millisecond performance. Features include auto-scaling, global tables, and DAX caching.

## 12. What is CloudFront?

Content Delivery Network that caches content at edge locations globally. Reduces latency and improves performance for end users.

## 13. What is Route 53?

Highly available DNS service. Supports domain registration, DNS routing (simple, weighted, latency, failover, geolocation), and health checks.

## 14. What is SQS?

Simple Queue Service for message queuing. Enables decoupling of microservices. Supports Standard (at-least-once) and FIFO (exactly-once) queues.

## 15. What is ECS vs EKS?

ECS is AWS's container orchestration service supporting Docker. EKS is managed Kubernetes. ECS is simpler; EKS uses standard Kubernetes APIs.

## 16. What is CloudWatch?

Monitoring and observability service. Collects metrics, logs, and events. Supports alarms, dashboards, and custom metrics.

## 17. What is CloudTrail?

Tracks user activity and API usage across AWS services. Provides governance, compliance, and operational auditing.

## 18. How do you secure an S3 bucket?

- Enable block public access
- Use bucket policies with least privilege
- Enable encryption at rest (SSE-S3, SSE-KMS)
- Enable versioning
- Enable logging
- Use VPC endpoints for private access

## 19. What is Auto Scaling?

Automatically adjusts compute capacity based on demand. Supports EC2 Auto Scaling, ECS Service Auto Scaling, Lambda Provisioned Concurrency, and DynamoDB Auto Scaling.

## 20. What is the difference between ALB and NLB?

ALB (Application Load Balancer) operates at layer 7 (HTTP/HTTPS) with advanced routing. NLB (Network Load Balancer) operates at layer 4 (TCP/UDP) with ultra-low latency.

## 21. What is AWS CloudFormation?

Infrastructure as Code service that models AWS resources using JSON/YAML templates. Supports drift detection, rollback, and change sets.

## 22. What is AWS CDK?

Cloud Development Kit allows defining cloud infrastructure using programming languages (TypeScript, Python, Java). Generates CloudFormation templates.

## 23. What is Amazon ElastiCache?

Managed in-memory caching service supporting Redis and Memcached. Used for caching, session management, and real-time analytics.

## 24. What is AWS WAF?

Web Application Firewall protects web applications from common exploits like SQL injection and XSS. Integrates with ALB, CloudFront, and API Gateway.

## 25. What is AWS Shield?

DDoS protection service. Shield Standard provides free protection; Shield Advanced offers enhanced protection with 24/7 DDoS Response Team.

## 26. What is GuardDuty?

Threat detection service that monitors for malicious activity using machine learning. Analyzes CloudTrail, VPC Flow Logs, and DNS logs.

## 27. What is the difference between On-Demand, Reserved, and Spot Instances?

On-Demand: Pay per hour with no commitment. Reserved: 1-3 year commitment for discounts. Spot: Bid for unused capacity with up to 90% discount but can be terminated.

## 28. How do you optimize AWS costs?

- Right-size instances with Compute Optimizer
- Use Reserved Instances or Savings Plans
- Use Spot Instances for fault-tolerant workloads
- Enable S3 Intelligent-Tiering
- Set up billing alerts
- Remove unused resources

## 29. What is AWS Organizations?

Service for managing multiple AWS accounts. Supports consolidated billing, SCPs for guardrails, and cross-account access.

## 30. What is Amazon EKS?

Elastic Kubernetes Service is a managed Kubernetes control plane. Integrates with IAM, VPC, and supports Fargate for serverless pods.
