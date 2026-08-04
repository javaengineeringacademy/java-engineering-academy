# Amazon VPC (Virtual Private Cloud)

## Overview

Amazon VPC lets you provision a logically isolated section of the AWS Cloud where you can launch AWS resources.

## VPC Components

```
┌─────────────────────────────────────────────────────────────┐
│                        VPC                                  │
│  ┌───────────────────────┐  ┌───────────────────────┐      │
│  │   Public Subnet       │  │   Private Subnet       │      │
│  │   10.0.1.0/24         │  │   10.0.2.0/24         │      │
│  │   ┌─────┐             │  │   ┌─────┐             │      │
│  │   │ EC2 │             │  │   │ EC2 │             │      │
│  │   └──┬──┘             │  │   └──┬──┘             │      │
│  └──────┼────────────────┘  └──────┼────────────────┘      │
│         │                          │                        │
│    ┌────┴────┐                ┌────┴────┐                   │
│    │ Internet│                │   NAT   │                   │
│    │Gateway  │                │ Gateway │                   │
│    └─────────┘                └─────────┘                   │
└─────────────────────────────────────────────────────────────┘
```

## VPC CIDR Blocks

| CIDR         | Usable IPs   | Use Case              |
|--------------|--------------|-----------------------|
| /16           | 65,534       | Large VPC             |
| /20           | 4,094        | Medium VPC            |
| /24           | 254          | Small VPC             |

### Reserved IPs
| IP         | Purpose                    |
|------------|----------------------------|
| .0         | Network address           |
| .1         | VPC router                |
| .2         | DNS server                |
| .3         | Future use                |
| .251-255   | AWS services              |

## Subnets

### Public Subnet
```bash
# Create public subnet
aws ec2 create-subnet \
  --vpc-id vpc-12345678 \
  --cidr-block 10.0.1.0/24 \
  --availability-zone us-east-1a

# Enable auto-assign public IP
aws ec2 modify-subnet-attribute \
  --subnet-id subnet-12345678 \
  --map-public-ip-on-launch
```

### Private Subnet
```bash
# Create private subnet
aws ec2 create-subnet \
  --vpc-id vpc-12345678 \
  --cidr-block 10.0.2.0/24 \
  --availability-zone us-east-1a

# No auto-assign public IP (default)
```

## Internet Gateway

```bash
# Create and attach IGW
aws ec2 create-internet-gateway
aws ec2 attach-internet-gateway \
  --internet-gateway-id igw-12345678 \
  --vpc-id vpc-12345678

# Route table for public subnet
aws ec2 create-route \
  --route-table-id rtb-12345678 \
  --destination-cidr-block 0.0.0.0/0 \
  --gateway-id igw-12345678
```

## NAT Gateway

```bash
# Create Elastic IP
aws ec2 allocate-address --domain vpc

# Create NAT Gateway in public subnet
aws ec2 create-nat-gateway \
  --subnet-id subnet-public \
  --allocation-id eipalloc-12345678

# Route private subnet traffic to NAT
aws ec2 create-route \
  --route-table-id rtb-private \
  --destination-cidr-block 0.0.0.0/0 \
  --nat-gateway-id nat-12345678
```

### NAT Gateway vs NAT Instance
| Feature       | NAT Gateway              | NAT Instance            |
|---------------|---------------------------|-------------------------|
| Availability  | Managed, highly available | Single instance         |
| Performance   | Up to 45 Gbps            | Instance-dependent      |
| Maintenance   | AWS managed               | Self-managed            |
| Cost          | Higher                    | Lower                   |
| Security      | Stateless                 | Stateful (iptables)     |

## Network ACLs

### Stateless Rules
```bash
# Create NACL
aws ec2 create-network-acl \
  --vpc-id vpc-12345678

# Allow HTTP inbound
aws ec2 create-network-acl-entry \
  --network-acl-id acl-12345678 \
  --rule-number 100 \
  --protocol tcp \
  --rule-action allow \
  --cidr-block 0.0.0.0/0 \
  --port-range From=80,To=80 \
  --ingress

# Deny all outbound (default)
# NACLs are stateless - must allow both directions
```

### NACL Rules Evaluation
```
Rule #   Protocol   Action
100      TCP:80     ALLOW (inbound)
200      TCP:443    ALLOW (inbound)
*        ALL        DENY (implicit)
```

## Security Groups

### Stateful Rules
```bash
# Create security group
aws ec2 create-security-group \
  --group-name my-sg \
  --description "My security group" \
  --vpc-id vpc-12345678

# Allow HTTP from anywhere
aws ec2 authorize-security-group-ingress \
  --group-id sg-12345678 \
  --protocol tcp \
  --port 80 \
  --cidr 0.0.0.0/0

# Allow all outbound (default)
aws ec2 authorize-security-group-egress \
  --group-id sg-12345678 \
  --protocol -1 \
  --cidr 0.0.0.0/0
```

### NACL vs Security Group
| Feature       | NACL                        | Security Group            |
|---------------|-----------------------------|---------------------------|
| Layer         | Subnet                      | Instance/ENI              |
| Rules         | Stateless                   | Stateful                  |
| Default       | Deny all                    | Allow all outbound        |
| Evaluation    | Rules in order              | All rules evaluated       |

## VPC Endpoints

### Gateway Endpoints
```bash
# Create S3 gateway endpoint
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-12345678 \
  --service-name com.amazonaws.us-east-1.s3 \
  --route-table-ids rtb-12345678

# Create DynamoDB gateway endpoint
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-12345678 \
  --service-name com.amazonaws.us-east-1.dynamodb \
  --route-table-ids rtb-12345678
```

### Interface Endpoints
```bash
# Create interface endpoint
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-12345678 \
  --service-name com.amazonaws.us-east-1.secretsmanager \
  --vpc-endpoint-type Interface \
  --subnet-ids subnet-12345678 \
  --security-group-ids sg-12345678
```

### Endpoint Policies
```json
{
  "Statement": [
    {
      "Sid": "AllowAll",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "*",
      "Resource": "*"
    }
  ]
}
```

## VPC Peering

```bash
# Create peering connection
aws ec2 create-vpc-peering-connection \
  --vpc-id vpc-12345678 \
  --peer-vpc-id vpc-87654321

# Accept peering (from peer VPC)
aws ec2 accept-vpc-peering-connection \
  --vpc-peering-connection-id pcx-12345678

# Add route
aws ec2 create-route \
  --route-table-id rtb-12345678 \
  --destination-cidr-block 10.1.0.0/16 \
  --vpc-peering-connection-id pcx-12345678
```

## Transit Gateway

```bash
# Create transit gateway
aws ec2 create-transit-gateway \
  --description "My Transit Gateway"

# Attach VPC
aws ec2 create-transit-gateway-vpc-attachment \
  --transit-gateway-id tgw-12345678 \
  --vpc-id vpc-12345678 \
  --subnet-ids subnet-12345678

# Add route
aws ec2 create-route \
  --route-table-id rtb-12345678 \
  --destination-cidr-block 10.1.0.0/16 \
  --transit-gateway-id tgw-12345678
```

## Flow Logs

```bash
# Create flow log
aws ec2 create-flow-logs \
  --resource-type VPC \
  --resource-ids vpc-12345678 \
  --traffic-type ALL \
  --log-destination-type cloud-watch-logs \
  --log-group-name /vpc/flowlogs
```

### Flow Log Fields
```
version account-id interface-id srcaddr dstaddr srcport dstport protocol packets bytes start end action log-status
```

## IPv6

```bash
# Enable IPv6 on VPC
aws ec2 modify-vpc-attribute \
  --vpc-id vpc-12345678 \
  --assign-ipv6-cidr-block

# Assign IPv6 to subnet
aws ec2 modify-subnet-attribute \
  --subnet-id subnet-12345678 \
  --assign-ipv6
```

## DNS Configuration

```bash
# Enable DNS resolution
aws ec2 modify-vpc-attribute \
  --vpc-id vpc-12345678 \
  --enable-dns-support

# Enable DNS hostnames
aws ec2 modify-vpc-attribute \
  --vpc-id vpc-12345678 \
  --enable-dns-hostnames
```

## Cost Optimization

- **Use VPC endpoints** to avoid NAT gateway charges
- **Choose appropriate subnet sizes**
- **Use NAT Gateway per AZ** for high availability
- **Monitor flow logs** for unnecessary traffic
- **Use Gateway endpoints** for S3 and DynamoDB (free)

## Best Practices

1. **Plan CIDR blocks** carefully
2. **Use multiple AZs** for high availability
3. **Implement least privilege** with security groups
4. **Use VPC endpoints** for AWS services
5. **Enable flow logs** for monitoring
6. **Use NACLs** for subnet-level protection
7. **Implement VPC peering** or Transit Gateway for multi-VPC
8. **Use private subnets** for most workloads
9. **Enable DNS hostnames** for service discovery
10. **Regular audit** security groups and NACLs
