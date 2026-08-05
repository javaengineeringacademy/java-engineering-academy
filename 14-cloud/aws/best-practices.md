# AWS Best Practices

## 1. Use IAM Roles Instead of Access Keys

Never hardcode credentials. Use IAM roles for EC2, ECS, Lambda, and other services.

```python
# BAD: Hardcoded credentials
s3 = boto3.client('s3',
    aws_access_key_id='AKIA...',
    aws_secret_access_key='...'
)

# GOOD: Use IAM role
s3 = boto3.client('s3')  # Uses instance role automatically
```

## 2. Enable Multi-Factor Authentication

Enable MFA for all IAM users, especially root and admin accounts.

## 3. Follow Least Privilege Principle

Grant only the minimum permissions needed. Use IAM Access Analyzer to find unused permissions.

## 4. Use VPC for Network Isolation

Deploy resources in VPC with public and private subnets. Keep databases and internal services in private subnets.

## 5. Enable Encryption at Rest and in Transit

Use KMS for encryption at rest. Use TLS/SSL for encryption in transit.

## 6. Implement Auto Scaling

Configure auto scaling for EC2, ECS, Lambda, and DynamoDB to handle traffic spikes.

## 7. Use Managed Services

Use RDS instead of self-managed databases. Use ElastiCache instead of self-managed Redis. Use S3 instead of self-managed storage.

## 8. Tag All Resources

Implement consistent tagging for cost allocation, automation, and access control.

```python
tags = [
    {'Key': 'Environment', 'Value': 'production'},
    {'Key': 'Team', 'Value': 'platform'},
    {'Key': 'Project', 'Value': 'my-app'}
]
```

## 9. Enable CloudTrail and Config

Log all API calls with CloudTrack. Track resource changes with AWS Config.

## 10. Use S3 Versioning and Lifecycle Policies

Enable versioning for data protection. Use lifecycle policies to optimize costs.

## 11. Implement Backup Strategy

Use AWS Backup for centralized backup management. Test restore procedures regularly.

## 12. Use CloudFront for Content Delivery

Cache static content at edge locations. Use Lambda@Edge for dynamic content.

## 13. Implement WAF for Web Protection

Protect web applications from SQL injection, XSS, and other attacks.

## 14. Use Secrets Manager for Credentials

Store and rotate credentials with Secrets Manager. Never hardcode secrets.

## 15. Monitor with CloudWatch

Set up alarms for critical metrics. Use dashboards for visibility.

## 16. Use Cost Explorer and Budgets

Monitor costs regularly. Set budgets and alerts for cost overruns.

## 17. Implement Disaster Recovery

Document DR procedures. Test backup and restore regularly. Choose appropriate RPO/RTO.

## 18. Use Infrastructure as Code

Define infrastructure with CloudFormation, CDK, or Terraform. Store templates in version control.

## 19. Implement CI/CD Pipelines

Use CodePipeline, GitHub Actions, or similar for automated deployments.

## 20. Regular Security Audits

Run Trusted Advisor checks. Review IAM policies. Audit S3 bucket permissions.

## 21. Use Spot Instances for Fault-Tolerant Workloads

Save up to 90% with Spot Instances for batch processing, dev/test, and fault-tolerant applications.

## 22. Optimize Lambda Cold Starts

Use Provisioned Concurrency for critical functions. Minimize deployment packages.

## 23. Use DynamoDB Accelerator (DAX)

Cache DynamoDB reads with DAX for microsecond response times.

## 24. Implement Rate Limiting

Use API Gateway throttling and WAF rate limiting to protect APIs.

## 25. Use AWS Organizations for Multi-Account

Separate environments with AWS Organizations. Use SCPs for guardrails.
