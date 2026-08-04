# Google Cloud VPC

## Overview

VPC (Virtual Private Cloud) provides networking for your cloud resources.

## VPC Modes

| Mode      | Description                          |
|-----------|--------------------------------------|
| Auto      | Automatic subnet creation            |
| Custom    | Manual subnet creation               |
| Legacy    | Automatic network (deprecated)       |

## Creating VPCs

### Auto Mode
```bash
# Create auto VPC
gcloud compute networks create my-vpc \
  --subnet-mode=auto \
  --bgp-routing-mode=global
```

### Custom Mode
```bash
# Create custom VPC
gcloud compute networks create my-custom-vpc \
  --subnet-mode=custom

# Create subnet
gcloud compute networks subnets create my-subnet \
  --network=my-custom-vpc \
  --region=us-central1 \
  --range=10.0.1.0/24
```

## Subnets

```bash
# Create subnet
gcloud compute networks subnets create my-subnet \
  --network=my-vpc \
  --region=us-central1 \
  --range=10.0.1.0/24 \
  --enable-private-ip-google-access

# List subnets
gcloud compute networks subnets list
```

## Firewall Rules

```bash
# Allow HTTP
gcloud compute firewall-rules create allow-http \
  --network=my-vpc \
  --allow=tcp:80 \
  --source-ranges=0.0.0.0/0 \
  --target-tags=http-server

# Allow SSH
gcloud compute firewall-rules create allow-ssh \
  --network=my-vpc \
  --allow=tcp:22 \
  --source-ranges=203.0.113.0/24

# Allow internal
gcloud compute firewall-rules create allow-internal \
  --network=my-vpc \
  --allow=tcp,udp,icmp \
  --source-ranges=10.0.0.0/8
```

### Firewall Rules Priority
```
Priority 1000: Allow HTTP (tcp:80)
Priority 2000: Allow SSH (tcp:22)
Priority 65534: Deny all (implicit)
```

## Cloud NAT

```bash
# Create Cloud Router
gcloud compute routers create my-router \
  --network=my-vpc \
  --region=us-central1

# Create Cloud NAT
gcloud compute routers nats create my-nat \
  --router=my-router \
  --region=us-central1 \
  --nat-all-subnet-ip-ranges \
  --auto-allocate-nat-external-ips
```

## Shared VPC

```bash
# Enable Shared VPC host
gcloud compute shared-vpc enable my-host-project

# Attach service project
gcloud compute shared-vpc associated-projects add my-service-project \
  --host-project=my-host-project
```

## Private Google Access

```bash
# Enable Private Google Access
gcloud compute networks subnets update my-subnet \
  --region=us-central1 \
  --enable-private-ip-google-access
```

## VPC Peering

```bash
# Create peering
gcloud compute networks peering create my-peering \
  --network=my-vpc \
  --peer-project=other-project \
  --peer-network=other-vpc

# Accept peering (from other project)
gcloud compute networks peering accept my-peering \
  --network=other-vpc \
  --peer-project=my-project
```

## Cloud Interconnect

```bash
# Create interconnect
gcloud compute interconnects create my-interconnect \
  --location=us-central1 \
  --link-type=DEDICATED \
  --requested-link-speed=10000

# Create VLAN attachment
gcloud compute interconnects attachments dedicated create my-attachment \
  --interconnect=my-interconnect \
  --region=us-central1 \
  --network=my-vpc \
  --vlan=100
```

## VPN

```bash
# Create VPN gateway
gcloud compute vpn-gateways create my-vpn-gateway \
  --network=my-vpc \
  --region=us-central1

# Create VPN tunnel
gcloud compute vpn-tunnels create my-vpn-tunnel \
  --peer-address=203.0.113.1 \
  --ike-version=2 \
  --shared-secret=my-secret \
  --target-vpn-gateway=my-vpn-gateway \
  --region=us-central1
```

## Network Policies

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend
spec:
  podSelector:
    matchLabels:
      role: frontend
  policyTypes:
  - Ingress
  - Egress
```

## Cloud DNS

```bash
# Create private zone
gcloud dns managed-zones create my-zone \
  --dns-name="internal.example.com." \
  --visibility=private \
  --networks=my-vpc

# Add record
gcloud dns record-sets transaction start \
  --zone=my-zone

gcloud dns record-sets transaction add 10.0.1.5 \
  --name="app.internal.example.com." \
  --ttl=300 \
  --type=A \
  --zone=my-zone

gcloud dns record-sets transaction execute --zone=my-zone
```

## Monitoring

```bash
# Get network metrics
gcloud monitoring metrics list \
  --filter='metric.type="compute.googleapis.com/network/received_bytes_count"'
```

## Cost Optimization

- **Use Cloud NAT** instead of VMs for NAT
- **Implement proper firewall rules**
- **Use Private Google Access** for private resources
- **Use Shared VPC** for multi-project environments
- **Monitor network usage**

## Best Practices

1. **Use custom mode VPCs** for control
2. **Implement proper firewall rules**
3. **Use Cloud NAT** for internet access
4. **Enable Private Google Access**
5. **Use Shared VPC** for multi-project
6. **Implement VPC peering** for connectivity
7. **Use Cloud Interconnect** for hybrid
8. **Implement proper DNS**
9. **Monitor network metrics**
10. **Regular security review**
