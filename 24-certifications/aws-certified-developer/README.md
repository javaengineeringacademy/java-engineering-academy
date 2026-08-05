# AWS Certified Developer - Associate

Validates technical expertise in developing and maintaining AWS-based applications. Ideal for developers who build cloud-native applications using AWS services.

## Table of Contents

- [Exam Details](#exam-details)
- [Domains and Weights](#domains-and-weights)
- [Key Services](#key-services)
- [Deep Dive by Domain](#deep-dive-by-domain)
- [Study Plan](#study-plan)
- [Practice Questions](#practice-questions)
- [Resources](#resources)

---

## Exam Details

| Attribute | Detail |
|-----------|--------|
| Code | DVA-C02 |
| Duration | 130 minutes |
| Questions | 65 |
| Passing Score | 720/1000 |
| Cost | $150 |
| Format | Multiple choice, multiple answer |
| Delivery | Pearson VUE, online proctored |

---

## Domains and Weights

1. **Deployment (22%)** - CI/CD pipelines, infrastructure as code, deployment strategies
2. **Security (26%)** - IAM, encryption, secrets management, compliance
3. **Development with AWS Services (24%)** - Serverless, APIs, SDKs, event-driven architecture
4. **Testing and Debugging (18%)** - Unit testing, integration testing, tracing, logging
5. **Optimization (10%)** - Performance tuning, cost optimization, caching

---

## Key Services

### Compute and Serverless

- **AWS Lambda** - Event-driven serverless compute
- **AWS Elastic Beanstalk** - Platform-as-a-service deployment
- **Amazon EC2** - Virtual machines for development and testing

### APIs and Integration

- **Amazon API Gateway** - Managed API creation and management
- **Amazon SQS** - Message queuing for decoupled architectures
- **Amazon SNS** - Pub/sub notifications
- **Amazon EventBridge** - Event-driven application integration

### Data and Storage

- **Amazon DynamoDB** - Managed NoSQL database
- **Amazon S3** - Object storage for application assets
- **Amazon RDS** - Managed relational database
- **Amazon ElastiCache** - In-memory caching

### DevOps and CI/CD

- **AWS CodeBuild** - Build and test code
- **AWS CodeDeploy** - Automated deployments
- **AWS CodePipeline** - CI/CD pipeline orchestration
- **AWS CloudFormation** - Infrastructure as Code
- **AWS CDK** - Programmatically define infrastructure

### Monitoring and Debugging

- **AWS X-Ray** - Distributed tracing
- **Amazon CloudWatch** - Monitoring and observability
- **AWS CloudTrail** - API audit logging

---

## Deep Dive by Domain

### Deployment (22%)

Master CI/CD pipelines using CodePipeline, CodeBuild, and CodeDeploy. Understand deployment strategies including blue-green, canary, and rolling deployments. Practice writing CloudFormation templates and CDK constructs. Know SAM (Serverless Application Model) for serverless deployments.

### Security (26%)

This is the highest-weighted domain. Master IAM policies, roles, and temporary credentials. Understand KMS for encryption at rest and in transit. Practice with Secrets Manager and Parameter Store for configuration. Know Cognito for user authentication in mobile and web apps.

### Development with AWS Services (24%)

Build serverless APIs with Lambda and API Gateway. Implement event-driven architectures using SQS, SNS, and EventBridge. Use the AWS SDK appropriately in your language of choice. Understand Step Functions for workflow orchestration.

### Testing and Debugging (18%)

Write unit tests for Lambda functions. Use CloudWatch Logs for debugging. Implement X-Ray for distributed tracing. Practice integration testing with localstack or SAM local.

### Optimization (10%)

Apply Lambda power tuning for cost and performance. Use DynamoDB DAX for caching. Implement S3 Intelligent-Tiering. Understand provisioned vs on-demand capacity.

---

## Study Plan

### Weeks 1-2: Serverless and Compute

- Lambda deep dive: triggers, limits, layers, concurrency
- API Gateway: REST APIs, HTTP APIs, authorizers
- DynamoDB: single-table design, GSI/LSI, DAX
- Practice: Build a serverless REST API

### Weeks 3-4: CI/CD and Infrastructure as Code

- CodePipeline, CodeBuild, CodeDeploy configuration
- CloudFormation fundamentals and nested stacks
- CDK for TypeScript or Python
- SAM for serverless deployments
- Practice: Set up a complete CI/CD pipeline

### Weeks 5-6: Security and Integration

- IAM policies, roles, assume role, cross-account
- KMS, Secrets Manager, Parameter Store
- Cognito user pools and identity pools
- SQS, SNS, EventBridge patterns
- Practice: Implement secure API with Cognito auth

### Weeks 7-8: Monitoring and Optimization

- CloudWatch metrics, alarms, dashboards
- X-Ray tracing and service maps
- CloudTrail for auditing
- Performance tuning and cost optimization
- Practice: Add monitoring to your application

### Weeks 9-10: Practice Exams and Review

- Take 3-5 full practice exams
- Review incorrect answers thoroughly
- Revisit weak domains
- Final review of key concepts

---

## Practice Questions

1. A Lambda function times out on large payloads. What is the best approach?
   - A) Increase timeout to maximum
   - B) Use SQS to decouple processing
   - C) Use larger Lambda memory
   - D) Both B and C

2. How should you store database credentials for a Lambda function?
   - A) Environment variables in plain text
   - B) Hardcoded in the function code
   - C) AWS Secrets Manager or Parameter Store
   - D) In a public S3 bucket

3. What is the best deployment strategy for zero-downtime updates?
   - A) Rolling update with health checks
   - B) Blue-green deployment with CodeDeploy
   - C) Manual server replacement
   - D) Stop-the-world deployment

---

## Resources

- **AWS Documentation**: DVA-C02 Exam Guide
- **AWS Skill Builder**: Developer learning path
- **A Cloud Guru**: DVA-C02 course
- **Stephane Maarek**: Ultimate Developer course on Udemy
- **Tutorials Dojo**: Practice exams and cheat sheets

---

**Last Updated**: August 2026
