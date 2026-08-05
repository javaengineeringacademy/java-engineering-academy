# AWS Certified SysOps Administrator - Associate

Validates technical expertise in deploying, managing, and operating workloads on AWS. Focuses on operations, monitoring, security, and automation for system administrators and DevOps engineers.

## Table of Contents

- [Exam Details](#exam-details)
- [Domains and Weights](#domains-and-weights)
- [Key Services](#key-services)
- [Deep Dive by Domain](#deep-dive-by-domain)
- [Study Plan](#study-plan)
- [Hands-On Labs](#hands-on-labs)
- [Resources](#resources)

---

## Exam Details

| Attribute | Detail |
|-----------|--------|
| Code | SOA-C02 |
| Duration | 180 minutes |
| Questions | 55 (plus 10 unscored) |
| Passing Score | 750/1000 |
| Cost | $150 |
| Format | Multiple choice, multiple answer, exam lab |
| Delivery | Pearson VUE, online proctored |

---

## Domains and Weights

1. **Monitoring, Logging, and Remediation (20%)** - CloudWatch, CloudTrail, Config
2. **Reliability and Business Continuity (16%)** - Backup, restore, failover, DR
3. **Deployment, Provisioning, and Automation (18%)** - CloudFormation, OpsWorks, Systems Manager
4. **Security and Compliance (16%)** - IAM, KMS, encryption, auditing
5. **Networking and Content Delivery (18%)** - VPC, Route 53, CloudFront
6. **Cost and Performance Optimization (12%)** - Pricing, right-sizing, budgets

---

## Key Services

### Monitoring and Logging

- **CloudWatch** - Metrics, alarms, dashboards, Logs Insights
- **CloudTrail** - API activity logging and auditing
- **AWS Config** - Resource configuration tracking and compliance
- **AWS Trusted Advisor** - Best practice recommendations

### Automation and Management

- **Systems Manager (SSM)** - Patch management, automation, parameter store
- **CloudFormation** - Infrastructure as Code
- **OpsWorks** - Configuration management with Chef/Puppet
- **AWS Backup** - Centralized backup management

### Security

- **IAM** - Users, roles, policies, MFA
- **KMS** - Key management and encryption
- **Inspector** - Vulnerability assessment
- **GuardDuty** - Threat detection
- **AWS Organizations** - Multi-account management

### Networking

- **VPC** - Network isolation, subnets, routing
- **Route 53** - DNS management
- **CloudFront** - Content delivery network
- **ELB** - Load balancing
- **Direct Connect** - Dedicated network links

---

## Deep Dive by Domain

### Monitoring, Logging, and Remediation (20%)

Configure CloudWatch metrics and alarms for EC2, RDS, and custom metrics. Create CloudWatch dashboards for operational visibility. Use CloudTrail to track API calls and detect unauthorized access. Implement AWS Config rules for compliance monitoring. Use Logs Insights for log analysis and troubleshooting.

### Reliability and Business Continuity (16%)

Design and implement backup strategies using AWS Backup. Configure AMI and EBS snapshot management. Implement Route 53 failover routing. Design multi-AZ architectures for high availability. Practice disaster recovery: backup/restore, pilot light, warm standby.

### Deployment, Provisioning, and Automation (18%)

Write CloudFormation templates for automated provisioning. Use Systems Manager for patch management and state management. ImplementOpsWorks for configuration management. Automate operational tasks with Systems Manager Automation runbooks. Use AWS Batch for batch processing workloads.

### Security and Compliance (16%)

Implement least-privilege IAM policies. Configure MFA for all human users. Use KMS for encryption key management. Enable CloudTrail in all regions. Implement AWS Config for compliance monitoring. Use GuardDuty for continuous threat detection.

### Networking and Content Delivery (18%)

Design VPC architectures with public and private subnets. Configure NACLs and security groups appropriately. Set up VPC peering and Transit Gateway. Configure Route 53 routing policies. Implement CloudFront distributions with origin access.

### Cost and Performance Optimization (12%)

Use AWS Cost Explorer and Budgets to track spending. Right-size instances using CloudWatch metrics and Trusted Advisor. Compare pricing models: On-Demand, Reserved, Spot. Implement auto scaling to match demand. Review AWS Cost and Usage reports.

---

## Study Plan

### Weeks 1-2: Monitoring and Logging

- CloudWatch metrics, alarms, custom metrics, dashboards
- CloudTrail configuration and log analysis
- AWS Config rules and compliance
- Systems Manager inventory and insights
- Practice: Set up comprehensive monitoring for a web application

### Weeks 3-4: Reliability and Business Continuity

- AWS Backup configuration and policies
- EBS snapshots and AMI management
- Route 53 health checks and failover
- Multi-AZ and multi-region strategies
- Practice: Implement disaster recovery for a database

### Weeks 5-6: Deployment and Automation

- CloudFormation templates and stacks
- Systems Manager automation and patch manager
- OpsWorks stacks and layers
- AWS CodeDeploy for EC2 and Lambda
- Practice: Automate server provisioning with CloudFormation

### Weeks 7-8: Security and Networking

- IAM policies, SCPs, permission boundaries
- KMS key management and rotation
- VPC design and network configuration
- Security group and NACL rules
- Practice: Implement a secure VPC architecture

### Weeks 9-10: Cost Optimization and Practice Exams

- Cost Explorer, Budgets, Cost and Usage Reports
- Instance right-sizing strategies
- Take 5+ full practice exams
- Review all incorrect answers

---

## Hands-On Labs

1. **Monitoring Setup** - Create CloudWatch dashboard with alarms for a multi-tier application
2. **Backup Strategy** - Configure AWS Backup with lifecycle policies
3. **Automated Deployment** - Deploy an application using CloudFormation and CodeDeploy
4. **Security Audit** - Enable CloudTrail, Config, and GuardDuty across all regions
5. **VPC Architecture** - Build a VPC with public/private subnets, NAT, and bastion host

---

## Resources

- **AWS Documentation**: SOA-C02 Exam Guide
- **A Cloud Guru**: SysOps Administrator course
- **Stephane Maarek**: SysOps course on Udemy
- **Tutorials Dojo**: Practice exams
- **AWS Well-Architected Framework**: Operational Excellence pillar

---

**Last Updated**: August 2026
