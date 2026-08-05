# AWS Learning Roadmap

## Phase 1: Cloud Fundamentals (Weeks 1-2)

### Cloud Concepts
- Cloud computing benefits and models
- AWS global infrastructure (Regions, AZs, Edge Locations)
- Shared Responsibility Model

### Core Services
- IAM (users, roles, policies)
- EC2 (instances, security groups, key pairs)
- S3 (buckets, storage classes, versioning)
- VPC (subnets, route tables, gateways)

### Practice
- Create an AWS account
- Launch an EC2 instance
- Create an S3 bucket and upload files
- Set up IAM users and policies

## Phase 2: Compute and Networking (Weeks 3-4)

### EC2 Deep Dive
- Instance types and families
- Auto Scaling Groups
- Elastic Load Balancing (ALB, NLB)
- Placement Groups

### Advanced Networking
- VPC peering and endpoints
- NAT Gateway and Bastion hosts
- Security Groups vs Network ACLs
- Route 53 DNS

### Practice
- Set up a VPC with public/private subnets
- Configure an ALB with target groups
- Implement Auto Scaling
- Register a domain with Route 53

## Phase 3: Data Services (Weeks 5-6)

### Databases
- RDS (MySQL, PostgreSQL, Aurora)
- DynamoDB (tables, indexes, DAX)
- ElastiCache (Redis, Memcached)
- Redshift (data warehousing)

### Storage
- S3 advanced features
- EBS volume types
- EFS and FSx
- Storage Gateway

### Practice
- Deploy an RDS instance with Multi-AZ
- Create a DynamoDB table with auto-scaling
- Set up ElastiCache for session management
- Implement S3 lifecycle policies

## Phase 4: Serverless (Weeks 7-8)

### Lambda
- Function basics and runtimes
- Event sources and triggers
- Layers and packaging
- Provisioned Concurrency

### API Gateway
- REST and HTTP APIs
- Custom authorizers
- Usage plans and API keys
- WebSocket APIs

### Step Functions
- State machines
- Parallel and choice states
- Error handling
- Express workflows

### Practice
- Build a serverless API with Lambda and API Gateway
- Create a file processor with S3 triggers
- Implement a workflow with Step Functions
- Set up a scheduled task with EventBridge

## Phase 5: Containers (Weeks 9-10)

### Docker Basics
- Containers vs VMs
- Dockerfile and images
- Docker Compose
- Container registries (ECR)

### ECS
- Task definitions
- Services and load balancing
- Service Discovery
- Fargate

### EKS
- Kubernetes on AWS
- Managed node groups
- Fargate profiles
- IAM roles for service accounts

### Practice
- Containerize an application
- Deploy to ECS with Fargate
- Set up ECR for image storage
- Create an EKS cluster

## Phase 6: DevOps and CI/CD (Weeks 11-12)

### CodePipeline
- Source, build, deploy stages
- Custom actions
- Cross-account deployments

### CloudFormation/CDK
- Templates and stacks
- CDK constructs
- Drift detection
- Stack policies

### Monitoring
- CloudWatch metrics and alarms
- X-Ray tracing
- CloudTrail logging
- AWS Config

### Practice
- Create a CI/CD pipeline with CodePipeline
- Deploy infrastructure with CloudFormation
- Set up monitoring and alerting
- Implement logging and tracing

## Phase 7: Security and Advanced (Ongoing)

### Security
- WAF and Shield
- GuardDuty and Security Hub
- KMS and Secrets Manager
- Compliance and auditing

### Advanced Topics
- Multi-account strategy
- Disaster recovery
- Cost optimization
- Performance tuning

### Certification
- AWS Cloud Practitioner
- AWS Solutions Architect Associate
- AWS Developer Associate
- AWS SysOps Administrator

## Key Resources

- AWS Documentation and whitepapers
- AWS Well-Architected Framework
- AWS re:Invent videos
- A Cloud Guru and Linux Academy
- AWS Free Tier for hands-on practice
