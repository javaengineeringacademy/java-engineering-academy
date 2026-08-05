# Terraform Hands-on Labs

## Lab 1: AWS VPC

Create a VPC with public and private subnets.

- Create VPC with CIDR block
- Add public subnet with internet gateway
- Add private subnet with NAT gateway
- Configure route tables
- Output VPC and subnet IDs

## Lab 2: EC2 Instance

Deploy an EC2 instance with security group.

- Create security group with rules
- Launch EC2 instance
- Add user data for bootstrapping
- Associate elastic IP
- Output instance details

## Lab 3: S3 Bucket

Create an S3 bucket with versioning and encryption.

- Create bucket with name
- Enable versioning
- Configure server-side encryption
- Add lifecycle rules
- Set up bucket policy

## Lab 4: RDS Database

Deploy a PostgreSQL database.

- Create DB subnet group
- Configure security group
- Launch RDS instance
- Set up parameter group
- Configure automated backups

## Lab 5: Module Development

Build a reusable module for EC2 instances.

- Create module structure
- Define input variables
- Define output values
- Add documentation
- Publish to registry

## Lab 6: Multi-Environment

Deploy infrastructure across dev, staging, and prod.

- Create environment directories
- Use workspaces for separation
- Configure remote state per environment
- Implement environment-specific variables
- Deploy and test each environment

## Lab 7: VPC Peering

Connect two VPCs with peering.

- Create two VPCs
- Establish peering connection
- Update route tables
- Test connectivity
- Clean up resources

## Lab 8: Auto Scaling Group

Deploy an auto scaling group with load balancer.

- Create launch template
- Configure auto scaling group
- Set up load balancer
- Add scaling policies
- Test scaling behavior

## Lab 9: Static Website

Host a static website on S3 with CloudFront.

- Create S3 bucket for hosting
- Configure bucket policy
- Set up CloudFront distribution
- Configure SSL certificate
- Test website delivery

## Lab 10: Disaster Recovery

Implement backup and restore procedures.

- Configure S3 cross-region replication
- Set up database snapshots
- Create recovery scripts
- Test restore procedures
- Document recovery process
