# AWS Shield

## Overview

AWS Shield is a managed Distributed Denial of Service (DDoS) protection service.

## Shield Tiers

| Feature              | Standard (Free)         | Advanced ($3,000/mo)    |
|----------------------|-------------------------|-------------------------|
| L3/L4 Protection     | Always-on               | Always-on               |
| L7 Protection        | No                      | Yes                     |
| DDoS Response Team   | No                      | Yes (24/7)              |
| Cost Protection      | No                      | Yes                     |
| Proactive Engagement | No                      | Yes                     |

## Shield Standard

### Features
- **Always-on detection** and mitigation
- **L3/L4 attack protection**
- **No additional cost**
- **Automatic protection**

### Common Attacks Mitigated
- SYN/UDP floods
- Amplification attacks
- Reflection attacks
- Protocol attacks

## Shield Advanced

### Enable Shield Advanced
```bash
aws shield create-protection \
  --name my-protection \
  --resource-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/my-alb/abc123
```

### Protection Groups
```bash
# Create protection group
aws shield create-protection-group \
  --protection-group-id my-group \
  --aggregation PER_PROTECTIONS \
  --pattern ALL \
  --resource-type APPLICATION_LOAD_BALANCER

# Add protection to group
aws shield create-protection \
  --name my-app \
  --resource-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/my-app/abc123
```

## DDoS Response Team (DRT)

### Proactive Engagement
```bash
# Set up proactive engagement
aws shield update-protection-group \
  --protection-group-id my-group \
  --aggregation PER_PROTECTIONS

# Configure SNS notifications
aws shield create-subscription \
  --enable-proactive-engagement
```

### DRT SLAs
- **Initial response**: 15 minutes
- **Escalation**: 15 minutes for critical

## Shield Advanced Protections

| Resource Type                | Protection Level    |
|------------------------------|---------------------|
| Application Load Balancer    | L7                  |
| CloudFront Distribution      | L7                  |
| Elastic IP Address           | L3/L4               |
| Global Accelerator           | L3/L4               |
| Route 53                     | L7                  |

## Cost Protection

```bash
# Shield Advanced includes
- Up to $300/month for WAF
- Up to $100/month for Route 53
- Up to $100/month for CloudFront
```

## Monitoring

```bash
# Get attack information
aws shield describe-attack \
  --attack-id attack-id-12345678

# Get protection details
aws shield describe-protection \
  --protection-id my-protection

# Get subscription details
aws shield describe-subscription
```

### CloudWatch Metrics
| Metric           | Description                    |
|------------------|--------------------------------|
| DDoSDetected    | Attack detected                |
| BitsIn           | Inbound traffic                |
| BitsOut          | Outbound traffic               |

## Incident Response

### Attack Flow
```
1. Attack detected
2. DRT notified
3. Proactive engagement
4. Mitigation applied
5. Attack resolved
6. Post-incident review
```

### Best Practices
1. **Enable proactive engagement**
2. **Configure SNS notifications**
3. **Review attack logs**
4. **Test DDoS response plan**

## Integration with Other Services

### WAF + Shield
```bash
# Use WAF with Shield Advanced
aws wafv2 associate-web-acl \
  --web-acl-arn arn:aws:wafv2:us-east-1:123456789012:regional/webacl/my-webacl/abc123 \
  --resource-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/my-alb/abc123
```

### Route 53 + Shield
```bash
# Enable Shield for Route 53
aws shield create-protection \
  --name my-dns \
  --resource-arn arn:aws:route53:::hostedzone/abc123
```

## Pricing

| Service        | Cost                    |
|----------------|-------------------------|
| Shield Standard| Free                    |
| Shield Advanced| $3,000/month            |
| DRT Support    | Included                |

## Best Practices

1. **Enable Shield Advanced** for production
2. **Use proactive engagement**
3. **Configure SNS notifications**
4. **Implement WAF rules** with Shield
5. **Monitor attack patterns**
6. **Review protection** regularly
7. **Test DDoS response plan**
8. **Use protection groups** for organization
9. **Enable cost protection**
10. **Contact AWS support** during attacks
