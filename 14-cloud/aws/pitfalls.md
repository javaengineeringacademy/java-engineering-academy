# AWS Pitfalls

## 1. Cost Overruns

AWS pay-as-you-go pricing can lead to unexpected bills if resources are not monitored.

### Prevention

- Set up AWS Budgets with alerts
- Use Cost Explorer to monitor spending
- Enable billing alerts in CloudWatch
- Review Trusted Advisor recommendations
- Use Cost Allocation Tags

## 2. Over-Provisioning

Using larger instance types than needed wastes money.

### Prevention

- Start small and scale up
- Use Compute Optimizer for right-sizing recommendations
- Use Auto Scaling for dynamic workloads
- Review utilization metrics regularly

## 3. Public S3 Buckets

S3 buckets accidentally made public expose sensitive data.

### Prevention

- Block public access by default
- Use S3 Access Points
- Enable CloudTrail logging for S3
- Use AWS Config rules to detect public buckets
- Regularly audit bucket policies

```python
# Block public access
s3 = boto3.client('s3')
s3.put_public_access_block(
    Bucket='my-bucket',
    PublicAccessBlockConfiguration={
        'BlockPublicAcls': True,
        'IgnorePublicAcls': True,
        'BlockPublicPolicy': True,
        'RestrictPublicBuckets': True
    }
)
```

## 4. Hardcoded Credentials

Embedding access keys in code exposes them in version control.

### Prevention

- Use IAM roles for EC2/ECS/Lambda
- Use AWS Vault for local development
- Never commit credentials to Git
- Use environment variables or secrets managers

## 5. Single Point of Failure

Deploying to a single AZ creates availability risk.

### Prevention

- Deploy across multiple AZs
- Use ELB for load balancing
- Enable Auto Scaling
- Use RDS Multi-AZ

## 6. Missing Encryption

Not encrypting data at rest and in transit exposes sensitive information.

### Prevention

- Enable default EBS encryption
- Use S3 server-side encryption
- Enable RDS encryption
- Use TLS for all connections
- Use KMS for key management

## 7. Ignoring Security Groups

Overly permissive security group rules expose resources.

### Prevention

- Follow least privilege principle
- Restrict SSH access to known IPs
- Use separate security groups per role
- Regularly audit security group rules

## 8. Not Using IAM Policies Properly

Using root credentials or overly permissive policies.

### Prevention

- Create IAM users for daily tasks
- Use roles for cross-account access
- Implement MFA
- Use IAM Access Analyzer

## 9. Ignoring CloudWatch Alarms

Not setting up alarms means issues go undetected.

### Prevention

- Set up alarms for CPU, memory, and disk
- Monitor application metrics
- Enable billing alerts
- Use CloudWatch Synthetics for endpoint monitoring

## 10. Not Backing Up Data

Losing data without backups can be catastrophic.

### Prevention

- Enable RDS automated backups
- Use AWS Backup for centralized backup
- Enable S3 versioning
- Test restore procedures regularly

## 11. Overlooking Lambda Cold Starts

Lambda functions can have high latency on first invocation.

### Prevention

- Use Provisioned Concurrency for critical functions
- Minimize deployment package size
- Choose lightweight runtimes
- Keep functions warm with scheduled invocations

## 12. Not Using VPC Flow Logs

Without VPC Flow Logs, network issues are hard to debug.

### Prevention

- Enable VPC Flow Logs
- Send logs to CloudWatch or S3
- Use VPC Reachability Analyzer
- Monitor for anomalous traffic

## 13. Ignoring Service Quotas

Hitting service quotas can block scaling.

### Prevention

- Review service quotas regularly
- Request quota increases proactively
- Use AWS Service Quotas dashboard
- Plan for peak usage

## 14. Not Tagging Resources

Untagged resources make cost tracking and management difficult.

### Prevention

- Implement tagging strategy
- Use AWS Tag Editor
- Enforce tags with SCPs
- Use CloudFormation for consistent tagging

## 15. Using Default VPC for Production

Default VPC has public subnets and security groups with permissive rules.

### Prevention

- Create custom VPC for production
- Use private subnets for sensitive resources
- Configure proper security groups
- Implement network ACLs
