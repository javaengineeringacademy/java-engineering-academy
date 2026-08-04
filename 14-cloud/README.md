# Module 14: Cloud Computing

## Overview

Cloud computing provides on-demand computing resources over the internet. This module covers the three major cloud providers: AWS, GCP, and Azure, with deep dives into their core services, architecture patterns, and best practices.

## Table of Contents

### Amazon Web Services (AWS)
| Topic | Description |
|-------|-------------|
| [EC2](aws/ec2/README.md) | Instances, AMIs, security groups |
| [S3](aws/s3/README.md) | Storage classes, lifecycle, versioning |
| [RDS](aws/rds/README.md) | Aurora, Multi-AZ, read replicas |
| [Lambda](aws/lambda/README.md) | Serverless, triggers, layers |
| [ECS](aws/ecs/README.md) | Fargate, task definitions |
| [EKS](aws/eks/README.md) | Managed Kubernetes |
| [VPC](aws/vpc/README.md) | Subnets, routing, peering |
| [IAM](aws/iam/README.md) | Roles, policies, federation |
| [CloudFormation](aws/cloudformation/README.md) | Stacks, templates |
| [CloudWatch](aws/cloudwatch/README.md) | Alarms, logs, metrics |
| [API Gateway](aws/api-gateway/README.md) | REST/HTTP APIs |
| [SQS](aws/sqs/README.md) | Queues, DLQ |
| [SNS](aws/sns/README.md) | Topics, subscriptions |
| [DynamoDB](aws/dynamodb/README.md) | NoSQL, DAX, streams |
| [ElastiCache](aws/elasticache/README.md) | Redis, Memcached |
| [Cognito](aws/cognito/README.md) | User pools, identity pools |
| [Secrets Manager](aws/secrets-manager/README.md) | Secret rotation |
| [Systems Manager](aws/systems-manager/README.md) | Parameter Store, automation |
| [CloudTrail](aws/cloudtrail/README.md) | Audit logging |
| [WAF](aws/waf/README.md) | Web application firewall |
| [Shield](aws/shield/README.md) | DDoS protection |
| [KMS](aws/kms/README.md) | Key management |
| [Cost Optimization](aws/cost-optimization/README.md) | Reserved instances, savings |
| [Well-Architected](aws/well-architected/README.md) | Framework pillars |
| [Dev Tools](aws/dev-tools/README.md) | CodeCommit, CodeBuild, CodeDeploy |

### Google Cloud Platform (GCP)
| Topic | Description |
|-------|-------------|
| [Compute Engine](gcp/compute/README.md) | GCE, instance groups |
| [Cloud Storage](gcp/storage/README.md) | Buckets, classes |
| [BigQuery](gcp/bigquery/README.md) | SQL analytics, ML |
| [Cloud Functions](gcp/cloud-functions/README.md) | Serverless functions |
| [Cloud Run](gcp/cloud-run/README.md) | Serverless containers |
| [GKE](gcp/gke/README.md) | Managed Kubernetes |
| [Cloud SQL](gcp/cloud-sql/README.md) | Managed databases |
| [Pub/Sub](gcp/pub-sub/README.md) | Messaging |
| [Dataflow](gcp/dataflow/README.md) | Apache Beam |
| [AI Platform](gcp/ai-platform/README.md) | ML training |
| [Vertex AI](gcp/vertex-ai/README.md) | ML platform |
| [Cloud Logging](gcp/cloud-logging/README.md) | Centralized logging |
| [Cloud Monitoring](gcp/cloud-monitoring/README.md) | Metrics, dashboards |
| [IAM](gcp/iam/README.md) | Service accounts |
| [VPC](gcp/vpc/README.md) | Networking |
| [Cost Optimization](gcp/cost-optimization/README.md) | Savings plans |
| [Well-Architected](gcp/well-architected/README.md) | Best practices |

### Microsoft Azure
| Topic | Description |
|-------|-------------|
| [Azure VMs](azure/compute/README.md) | Virtual machines, ACI |
| [Blob Storage](azure/storage/README.md) | File storage |
| [Azure SQL](azure/sql/README.md) | Cosmos DB |
| [Azure Functions](azure/functions/README.md) | Serverless |
| [AKS](azure/aks/README.md) | Managed Kubernetes |
| [Cosmos DB](azure/cosmos-db/README.md) | Multi-model database |
| [Service Bus](azure/service-bus/README.md) | Queues, topics |
| [API Management](azure/api-management/README.md) | API gateway |
| [Azure DevOps](azure/devops-services/README.md) | CI/CD, boards |
| [Azure AD](azure/active-directory/README.md) | Identity, B2C |
| [Key Vault](azure/key-vault/README.md) | Secrets, keys |
| [Azure Monitor](azure/monitor/README.md) | App Insights |
| [Cost Optimization](azure/cost-optimization/README.md) | Reservations |
| [Well-Architected](azure/well-architected/README.md) | Best practices |

## Key Concepts

### Cloud Service Models
```
┌─────────────────────────────────────────────────┐
│  SaaS (Software as a Service)                   │
│  e.g., Gmail, Office 365, Salesforce            │
├─────────────────────────────────────────────────┤
│  PaaS (Platform as a Service)                   │
│  e.g., Heroku, App Engine, Azure App Service    │
├─────────────────────────────────────────────────┤
│  IaaS (Infrastructure as a Service)             │
│  e.g., EC2, GCE, Azure VMs                     │
├─────────────────────────────────────────────────┤
│  On-Premises                                     │
│  Physical servers, data centers                 │
└─────────────────────────────────────────────────┘
```

### Cloud Architecture Pillars
1. **Security** - Defense in depth, least privilege
2. **Reliability** - Fault tolerance, disaster recovery
3. **Performance Efficiency** - Right-sizing, caching
4. **Cost Optimization** - Pay for what you use
5. **Operational Excellence** - Automation, monitoring

### Well-Architected Framework
- **AWS**: 6 pillars (Security, Reliability, Performance, Cost, Operational Excellence, Sustainability)
- **GCP**: 4 pillars (Operational Excellence, Security, Reliability, Cost)
- **Azure**: 5 pillars (Security, Reliability, Cost Optimization, Operational Excellence, Performance Efficiency)

### Multi-Cloud & Hybrid Strategies
```
On-Premises ←→ Private Cloud ←→ Public Cloud
     │              │                │
     └──────────────┴────────────────┘
              Hybrid/Multi-Cloud
```

## Learning Path

1. Choose a primary cloud provider (AWS, GCP, or Azure)
2. Learn compute services (EC2/GCE/VMs)
3. Master storage (S3/Cloud Storage/Blob)
4. Explore databases (RDS/Cloud SQL/Azure SQL)
5. Study networking (VPC/VPC/VNet)
6. Implement serverless (Lambda/Cloud Functions/Azure Functions)
7. Deploy containerized workloads (ECS/GKE/AKS)
8. Master security and IAM
9. Implement monitoring and logging
10. Optimize costs
