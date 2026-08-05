# Decision Tree: AWS Service Selection Guide

## Overview
AWS offers hundreds of services. This guide helps you choose the right services for common use cases.

## Compute Selection

```mermaid
flowchart TD
    Start[Compute Need] --> Q1{Need serverless?}
    Q1 -->|Yes| Lambda[AWS Lambda]
    Q1 -->|No| Q2{Need containers?}
    
    Q2 -->|Yes| Q3{Want managed?}
    Q2 -->|No| Q4{Need VMs?}
    
    Q3 -->|Yes| ECS[ECS/Fargate]
    Q3 -->|No| EKS[EKS]
    
    Q4 -->|Yes| Q5{Need GPU?}
    Q4 -->|No| Q6{Need bare metal?}
    
    Q5 -->|Yes| P2[P2/P3/P4 Instances]
    Q5 -->|No| EC2[EC2]
    
    Q6 -->|Yes| EC2Metal[EC2 Metal]
    Q6 -->|No| EC2
```

## Database Selection

```mermaid
flowchart TD
    Start[Data Need] --> Q1{Relational data?}
    Q1 -->|Yes| Q2{Need fully managed?}
    Q1 -->|No| Q3{NoSQL needed?}
    
    Q2 -->|Yes| RDS[Amazon RDS]
    Q2 -->|No| EC2DB[EC2 + Database]
    
    Q3 -->|Yes| Q4{Document store?}
    Q3 -->|No| Q5{Key-value?}
    
    Q4 -->|Yes| DynamoDB[DynamoDB]
    Q4 -->|No| Q6{Graph data?}
    
    Q5 -->|Yes| ElastiCache[ElastiCache]
    Q5 -->|No| Q7{Time series?}
    
    Q6 -->|Yes| Neptune[Amazon Neptune]
    Q6 -->|No| Q8{Wide column?}
    
    Q7 -->|Yes| Timestream[Amazon Timestream]
    Q7 -->|No| Q9{Search needed?}
    
    Q8 -->|Yes| Keyspaces[Amazon Keyspaces]
    Q8 -->|No| Q10{Ledger?}
    
    Q9 -->|Yes| OpenSearch[OpenSearch]
    Q9 -->|No| Q11{Cache needed?}
    
    Q10 -->|Yes| QLDB[Amazon QLDB]
    Q10 -->|No| RDS
    
    Q11 -->|Yes| ElastiCache
    Q11 -->|No| RDS
```

## Storage Selection

```mermaid
flowchart TD
    Start[Storage Need] --> Q1{Object storage?}
    Q1 -->|Yes| S3[Amazon S3]
    Q1 -->|No| Q2{File storage?}
    
    Q2 -->|Yes| EFS[Amazon EFS]
    Q2 -->|No| Q3{Block storage?}
    
    Q3 -->|Yes| EBS[Amazon EBS]
    Q3 -->|No| Q4{Archive storage?}
    
    Q4 -->|Yes| Glacier[Amazon Glacier]
    Q4 -->|No| Q5{Hybrid cloud?}
    
    Q5 -->|Yes| StorageGateway[Storage Gateway]
    Q5 -->|No| S3
```

## Service Selection Matrix

### Compute Services

| Service | Use Case | Scaling | Cost Model |
|---------|----------|---------|------------|
| EC2 | Traditional applications | Manual/Auto | Instance hours |
| Lambda | Event-driven, microservices | Automatic | Per request |
| ECS | Container orchestration | Manual/Auto | Container hours |
| EKS | Kubernetes workloads | Manual/Auto | Cluster hours |
| Lightsail | Simple web apps | Manual | Monthly |

### Database Services

| Service | Use Case | Scaling | Best For |
|---------|----------|---------|----------|
| RDS | Relational data | Vertical/Horizontal | Traditional apps |
| DynamoDB | Key-value, document | Automatic | High-scale apps |
| ElastiCache | Caching, sessions | Cluster | Performance |
| Neptune | Graph data | Vertical | Social networks |
| OpenSearch | Search, analytics | Cluster | Log analytics |

### Storage Services

| Service | Use Case | Durability | Access Pattern |
|---------|----------|------------|----------------|
| S3 | Object storage | 11 9s | REST API |
| EFS | Shared file storage | High | NFS |
| EBS | Block storage | High | Single EC2 |
| Glacier | Archive storage | High | Batch retrieval |

## Cost Optimization

```mermaid
graph TD
    subgraph "Cost Saving Strategies"
        A[Reserved Instances] --> B[Save 30-70%]
        C[Spot Instances] --> D[Save 60-90%]
        E[Right-sizing] --> F[Save 20-50%]
        G[Savings Plans] --> H[Save 20-70%]
    end
```

## Architecture Patterns

### Three-Tier Web Application
```mermaid
graph LR
    ALB[ALB] --> EC2[EC2 Instances]
    EC2 --> RDS[RDS Database]
    EC2 --> S3[S3 Static Assets]
    EC2 --> ElastiCache[ElastiCache]
```

### Serverless Application
```mermaid
graph LR
    API[API Gateway] --> Lambda[Lambda]
    Lambda --> DynamoDB[DynamoDB]
    Lambda --> S3[S3]
    Lambda --> SQS[SQS]
```

## Migration Considerations

### Common Migration Paths:
- On-premises to EC2: Lift and shift
- On-premises to Containers: Containerize first
- Monolith to Microservices: Decompose gradually
- SQL to NoSQL: Evaluate data model fit

## When to Use Specific Services

### Choose EC2 When:
- Need full control over OS
- Running legacy applications
- Need specific instance types
- Custom networking required

### Choose Lambda When:
- Event-driven architecture
- Variable workloads
- Microservices
- Quick prototyping

### Choose ECS When:
- Container workloads
- Want managed orchestration
- Need AWS integration
- Simpler than Kubernetes

### Choose RDS When:
- Relational data model
- Need ACID compliance
- Standard SQL queries
- Traditional applications

### Choose DynamoDB When:
- Key-value access patterns
- High-scale requirements
- Serverless architecture
- Need millisecond latency

## Decision Checklist

Choose compute based on:
- [ ] Workload type (batch, real-time, etc.)
- [ ] Scaling requirements
- [ ] Cost constraints
- [ ] Team expertise
- [ ] Integration needs

Choose database based on:
- [ ] Data model (relational, document, etc.)
- [ ] Query patterns
- [ ] Scaling requirements
- [ ] Consistency needs
- [ ] Budget constraints

Choose storage based on:
- [ ] Data type (objects, files, blocks)
- [ ] Access patterns
- [ ] Durability requirements
- [ ] Performance needs
- [ ] Cost constraints