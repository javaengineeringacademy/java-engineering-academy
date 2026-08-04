# Amazon EC2 (Elastic Compute Cloud)

## Overview

Amazon EC2 provides scalable computing capacity in the AWS cloud, eliminating the need to invest in hardware up front.

## Instance Types

| Category   | Examples      | Use Case                    |
|------------|---------------|-----------------------------|
| General    | t3, m5, m6i   | Web servers, dev environments |
| Compute    | c5, c6i       | Batch processing, HPC       |
| Memory     | r5, r6i       | In-memory databases         |
| Storage    | i3, i4i       | NoSQL databases, data warehousing |
| Accelerated| p4, g5        | ML training, graphics       |
| HPC        | hpc7a         | Computational fluid dynamics |

## Instance States

```
pending → running → stopping → stopped
                  ↘ shutting-down → terminated
```

## AMIs (Amazon Machine Images)

### AMI Types
- **Public AMIs** - Provided by AWS and community
- **My AMIs** - AMIs you create
- **AWS Marketplace** - Third-party AMIs
- **Quick Start** - AWS-provided default AMIs

### Creating Custom AMIs
```bash
# Create AMI from running instance
aws ec2 create-image \
  --instance-id i-1234567890abcdef0 \
  --name "My Custom AMI" \
  --description "Custom AMI with app installed"
```

### AMI Copy Across Regions
```bash
aws ec2 copy-image \
  --source-image-id ami-1234567890abcdef0 \
  --source-region us-east-1 \
  --destination-region us-west-2 \
  --name "Copied AMI"
```

## Security Groups

### Inbound Rules
```bash
# Allow SSH from specific IP
aws ec2 authorize-security-group-ingress \
  --group-id sg-12345678 \
  --protocol tcp \
  --port 22 \
  --cidr 203.0.113.0/24

# Allow HTTP from anywhere
aws ec2 authorize-security-group-ingress \
  --group-id sg-12345678 \
  --protocol tcp \
  --port 80 \
  --cidr 0.0.0.0/0
```

### Outbound Rules
```bash
# Allow all outbound (default)
aws ec2 authorize-security-group-egress \
  --group-id sg-12345678 \
  --protocol -1 \
  --cidr 0.0.0.0/0
```

### Security Group Best Practices
- Follow least privilege principle
- Use separate SGs for each tier (web, app, database)
- Never allow 0.0.0.0/0 for SSH/RDP
- Use SG references instead of IPs when possible

## Key Pairs

```bash
# Create key pair
aws ec2 create-key-pair \
  --key-name MyKeyPair \
  --query 'KeyMaterial' \
  --output text > MyKeyPair.pem

chmod 400 MyKeyPair.pem

# Launch instance with key pair
aws ec2 run-instances \
  --image-id ami-12345678 \
  --instance-type t3.micro \
  --key-name MyKeyPair \
  --security-group-ids sg-12345678
```

## Instance Lifecycle

### Starting an Instance
```bash
aws ec2 start-instances --instance-ids i-1234567890abcdef0
```

### Stopping vs Terminating
| Action    | EBS Volume | Instance ID | Costs      |
|-----------|------------|-------------|------------|
| Stop      | Preserved  | Same        | Stop paying|
| Terminate | Deleted*   | Lost        | Stop paying|

*Unless `DeleteOnTermination=false`

## Elastic IP Addresses

```bash
# Allocate Elastic IP
aws ec2 allocate-address --domain vpc

# Associate with instance
aws ec2 associate-address \
  --instance-id i-1234567890abcdef0 \
  --allocation-id eipalloc-12345678

# Release Elastic IP
aws ec2 release-address --allocation-id eipalloc-12345678
```

### Elastic IP Limitations
- 5 Elastic IPs per region (soft limit)
- Charged for unused EIPs attached to stopped instances
- Best practice: Use auto-assign public IP instead

## Placement Groups

| Strategy     | Description                          | Use Case           |
|--------------|--------------------------------------|--------------------|
| Cluster      | Close together in single AZ          | HPC, tightly coupled |
| Spread       | On distinct hardware                 | Critical instances  |
| Partition    | Groups within rack                   | Hadoop, Kafka      |

```bash
# Create placement group
aws ec2 create-placement-group \
  --group-name MyCluster \
  --strategy cluster

# Launch in placement group
aws ec2 run-instances \
  --placement-group-name MyCluster \
  --placement AvailabilityZone=us-east-1a
```

## User Data

```bash
# Launch with user data script
aws ec2 run-instances \
  --image-id ami-12345678 \
  --instance-type t3.micro \
  --user-data file://userdata.sh
```

### userdata.sh Example
```bash
#!/bin/bash
yum update -y
yum install -y httpd
systemctl start httpd
systemctl enable httpd
echo "<h1>Hello from EC2</h1>" > /var/www/html/index.html
```

## Instance Metadata

```bash
# Get instance metadata (from within instance)
curl http://169.254.169.254/latest/meta-data/
curl http://169.254.169.254/latest/meta-data/instance-id
curl http://169.254.169.254/latest/meta-data/iam/security-credentials/
```

### IMDSv2 (Recommended)
```bash
# Get token
TOKEN=$(curl -X PUT "http://169.254.169.254/latest/api/token" \
  -H "X-aws-ec2-metadata-token-ttl-seconds: 21600")

# Use token
curl -H "X-aws-ec2-metadata-token: $TOKEN" \
  http://169.254.169.254/latest/meta-data/
```

## EBS (Elastic Block Store)

| Volume Type | Use Case              | Max IOPS    | Throughput   |
|-------------|-----------------------|-------------|--------------|
| gp3         | General purpose       | 16,000      | 1,000 MB/s   |
| gp2         | General purpose       | 16,000      | 250 MB/s     |
| io2         | Critical databases    | 64,000      | 4,000 MB/s   |
| st1         | Throughput-optimized  | 500         | 500 MB/s     |
| sc1         | Cold storage         | 250         | 250 MB/s     |

### Snapshots
```bash
# Create snapshot
aws ec2 create-snapshot \
  --volume-id vol-12345678 \
  --description "My snapshot"

# Restore from snapshot
aws ec2 create-volume \
  --snapshot-id snap-12345678 \
  --availability-zone us-east-1a
```

## Cost Optimization

### Instance Purchasing Options
| Option              | Discount | Commitment  | Best For            |
|---------------------|----------|-------------|---------------------|
| On-Demand           | 0%       | None        | Sporadic workloads  |
| Reserved (1yr)      | Up to 40%| 1 year      | Steady-state        |
| Reserved (3yr)      | Up to 60%| 3 years     | Long-term           |
| Savings Plans       | Up to 72%| $/hr commit | Flexible workloads  |
| Spot                | Up to 90%| None        | Fault-tolerant      |
| Dedicated Host      | -        | 1 year      | Compliance/licensing|

### Right-Sizing
```bash
# Get recommended instance types
aws ce get-rightsizing-recommendation \
  --service "Amazon EC2" \
  --configuration DaysSinceLaunch=30
```

## Monitoring

```bash
# Get CPU utilization
aws cloudwatch get-metric-statistics \
  --namespace AWS/EC2 \
  --metric-name CPUUtilization \
  --dimensions Name=InstanceId,Value=i-1234567890abcdef0 \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Average
```

## Common CLI Commands

```bash
# List instances
aws ec2 describe-instances \
  --filters "Name=instance-state-name,Values=running"

# Get public IP
aws ec2 describe-instances \
  --instance-ids i-1234567890abcdef0 \
  --query 'Reservations[*].Instances[*].PublicIpAddress'

# Connect via SSM
aws ssm start-session --target i-1234567890abcdef0

# Create volume
aws ec2 create-volume \
  --availability-zone us-east-1a \
  --size 100 \
  --volume-type gp3
```

## Best Practices

1. **Security**: Use IMDSv2, restrict security groups, encrypt EBS volumes
2. **Monitoring**: Enable detailed monitoring, set up CloudWatch alarms
3. **Cost**: Use Spot Instances for fault-tolerant workloads, right-size instances
4. **Availability**: Use multiple AZs, health checks, and auto-scaling
5. **Networking**: Use ENI for multi-homed instances, VPC endpoints for private access
6. **Backup**: Automate snapshots, test restore procedures
7. **Compliance**: Use AWS Systems Manager for patching and inventory
