# AWS Fundamentals

## Overview
Amazon Web Services (AWS) is the world's most comprehensive cloud platform.

## Topics
- EC2
- S3
- RDS
- Lambda
- VPC
- IAM
- CloudFormation
- DynamoDB
- SQS/SNS
- CloudWatch

## Learning Objectives
- Navigate AWS console
- Deploy basic infrastructure
- Understand pricing models

## Prerequisites
- Basic networking

## Architecture

```mermaid
graph TD
    subgraph Compute
        EC2[EC2]
        Lambda[Lambda]
        ECS[ECS]
        EKS[EKS]
    end

    subgraph Storage
        S3[S3]
        EBS[EBS]
        EFS[EFS]
    end

    subgraph Database
        RDS[RDS]
        DynamoDB[DynamoDB]
        ElastiCache[ElastiCache]
    end

    subgraph Networking
        VPC[VPC]
        ALB[ALB/ELB]
        Route53[Route 53]
    end

    subgraph Security
        IAM[IAM]
        KMS[KMS]
        WAF[WAF]
    end

    subgraph Monitoring
        CloudWatch[CloudWatch]
        XRay[X-Ray]
        CloudTrail[CloudTrail]
    end

    EC2 --> VPC
    Lambda --> VPC
    S3 --> IAM
    RDS --> VPC
    CloudWatch --> EC2

    style EC2 fill:#ff9900,stroke:#333,stroke-width:2px
    style S3 fill:#3f8624,stroke:#333,stroke-width:2px
    style RDS fill:#c925d1,stroke:#333,stroke-width:2px
    style VPC fill:#8c4fff,stroke:#333,stroke-width:2px
    style IAM fill:#dd344c,stroke:#333,stroke-width:2px
    style CloudWatch fill:#e7157b,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Workload Type} -->|Compute| Compute[Choose Service]
    Start -->|Storage| Storage[Choose Storage]
    Start -->|Database| Database[Choose Database]

    Compute -->|Virtual Machine| EC2[EC2 Instances]
    Compute -->|Serverless| Lambda[Lambda Functions]
    Compute -->|Containers| Containers[ECS/EKS]

    EC2 -->|Web Server| Web[EC2 + ALB]
    EC2 -->|Batch Processing| Batch[EC2 + SQS]

    Lambda -->|API| API[API Gateway + Lambda]
    Lambda -->|Event Processing| Event[EventBridge + Lambda]

    Containers -->|Simple| Simple[ECS Fargate]
    Containers -->|Complex| Complex[EKS]

    Storage -->|Static Assets| Static[S3]
    Storage -->|Block Storage| Block[EBS]
    Storage -->|File Storage| File[EFS]

    Database -->|Relational| Relational[RDS/Aurora]
    Database -->|NoSQL| NoSQL[DynamoDB]
    Database -->|Cache| Cache[ElastiCache]

    style Compute fill:#ff9900,stroke:#333,stroke-width:2px
    style Storage fill:#3f8624,stroke:#333,stroke-width:2px
    style Database fill:#c925d1,stroke:#333,stroke-width:2px
```
