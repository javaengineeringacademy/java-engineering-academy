# Amazon Linux

## Overview

Amazon Linux is a Linux distribution provided by Amazon Web Services (AWS) and optimized for running on AWS cloud infrastructure. It provides a stable, secure, and high-performance execution environment for applications on EC2 instances.

## Versions

| Version | Description |
|---------|-------------|
| Amazon Linux 2023 | Latest release with newer packages |
| Amazon Linux 2 | Previous generation, still supported |
| Amazon Linux AMI | Original (legacy) |

## Package Management

### YUM/DNF (Amazon Linux 2023)
```bash
dnf update                    # Update packages
dnf install package-name      # Install package
dnf remove package-name       # Remove package
dnf search keyword            # Search packages
dnf list installed            # List installed packages
```

### YUM (Amazon Linux 2)
```bash
yum update                    # Update packages
yum install package-name      # Install package
yum remove package-name       # Remove package
yum-config-manager --enable repo-name
```

## AWS Integration

### EC2 Instance Metadata
```bash
curl http://169.254.169.254/latest/meta-data/instance-id
curl http://169.254.169.254/latest/meta-data/local-ipv4
```

### AWS CLI
```bash
aws configure                 # Configure AWS CLI
aws s3 ls                     # List S3 buckets
aws ec2 describe-instances    # List EC2 instances
```

### CloudFormation Helper Scripts
```bash
/opt/aws/bin/cfn-init         # Initialize instance
/opt/aws/bin/cfn-signal       # Signal completion
/opt/aws/bin/cfn-get-metadata # Get metadata
```

## System Configuration

### Package Updates
```bash
# Amazon Linux 2023
dnf check-update               # Check for updates
sudo dnf update                # Apply updates

# Amazon Linux 2
yum check-update               # Check for updates
sudo yum update                # Apply updates
```

### Security
```bash
yum update security            # Security updates only
yum updateinfo list security   # List security updates
```

## EC2 Instance Management

### Starting and Stopping
```bash
# Start instance
aws ec2 start-instances --instance-ids i-1234567890abcdef0

# Stop instance
aws ec2 stop-instances --instance-ids i-1234567890abcdef0
```

### User Data Scripts
```bash
#!/bin/bash
yum update -y
yum install -y httpd
systemctl start httpd
systemctl enable httpd
```

## Performance Optimization

### Instance Types
- **General Purpose**: t3, m5, m6i
- **Compute Optimized**: c5, c6i
- **Memory Optimized**: r5, r6i
- **Storage Optimized**: i3, d2

### Storage
- EBS volumes for persistent storage
- Instance store for temporary data
- EFS for shared file systems

## Best Practices

1. Use latest Amazon Linux version
2. Enable automatic updates
3. Use IAM roles for EC2 instances
4. Implement proper security groups
5. Monitor with CloudWatch

## References

- Amazon Linux 2 Documentation
- Amazon Linux 2023 Documentation
- AWS EC2 User Guide
