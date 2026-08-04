# AWS WAF (Web Application Firewall)

## Overview

AWS WAF helps protect your web applications and APIs against common web exploits and bots.

## Components

```
┌─────────────────────────────────────────────────────────┐
│                      AWS WAF                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Rules   │  │  Web ACLs│  │  Rate    │             │
│  │          │  │          │  │ Limiting │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Resources   │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Rule Types

| Type                | Description                          |
|---------------------|--------------------------------------|
| Managed Rule Groups | AWS and AWS Marketplace rules        |
| Custom Rules        | Your own rules                       |
| Rate-Based Rules    | Limit request rates                  |
| IP Set Rules        | Allow/block IP addresses             |
| Geographic Rules    | Block by country                     |

## Creating Web ACL

```bash
aws wafv2 create-web-acl \
  --name my-web-acl \
  --scope REGIONAL \
  --default-action Block='{}' \
  --rules '[
    {
      "Name": "RateLimitRule",
      "Priority": 1,
      "Action": {"Block": {}},
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "RateLimit"
      },
      "RateBasedStatement": {
        "Limit": 2000,
        "AggregateKeyType": "IP"
      }
    }
  ]' \
  --visibility-config '{
    "SampledRequestsEnabled": true,
    "CloudWatchMetricsEnabled": true,
    "MetricName": "MyWebACL"
  }'
```

## AWS Managed Rule Groups

### Common Rule Groups
| Rule Group               | Protection           |
|--------------------------|----------------------|
| AWSManagedRulesCommonRuleSet | Common attacks    |
| AWSManagedRulesSQLiRuleSet   | SQL injection     |
| AWSManagedRulesKnownBadInputs | Known bad inputs |
| AWSManagedRulesAnonymousIpList | Anonymous IPs   |

```bash
# Add managed rule group
aws wafv2 update-web-acl \
  --name my-web-acl \
  --scope REGIONAL \
  --rules '[
    {
      "Name": "AWSManagedRulesCommonRuleSet",
      "Priority": 0,
      "OverrideAction": {"None": {}},
      "Statement": {
        "ManagedRuleGroupStatement": {
          "VendorName": "AWS",
          "Name": "AWSManagedRulesCommonRuleSet"
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "CommonRuleSet"
      }
    }
  ]'
```

## Custom Rules

### IP Set Rule
```bash
# Create IP set
aws wafv2 create-ip-set \
  --name blocked-ips \
  --scope REGIONAL \
  --addresses 203.0.113.0/24 198.51.100.0/24

# Add IP set rule
aws wafv2 update-web-acl \
  --rules '[
    {
      "Name": "BlockIPs",
      "Priority": 2,
      "Action": {"Block": {}},
      "Statement": {
        "IPSetReferenceStatement": {
          "ARN": "arn:aws:wafv2:us-east-1:123456789012:regional/ipset/blocked-ips/abc123"
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "BlockIPs"
      }
    }
  ]'
```

### Geographic Matching
```bash
# Block countries
aws wafv2 update-web-acl \
  --rules '[
    {
      "Name": "BlockCountries",
      "Priority": 3,
      "Action": {"Block": {}},
      "Statement": {
        "GeoMatchStatement": {
          "CountryCodes": ["CN", "RU"]
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "BlockCountries"
      }
    }
  ]'
```

## Rate Limiting

```bash
# Rate-based rule
aws wafv2 update-web-acl \
  --rules '[
    {
      "Name": "RateLimit",
      "Priority": 1,
      "Action": {"Block": {}},
      "Statement": {
        "RateBasedStatement": {
          "Limit": 2000,
          "AggregateKeyType": "IP"
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "RateLimit"
      }
    }
  ]'
```

## SQL Injection Protection

```bash
# SQL injection rule
aws wafv2 update-web-acl \
  --rules '[
    {
      "Name": "SQLInjection",
      "Priority": 4,
      "Action": {"Block": {}},
      "Statement": {
        "SqliMatchStatement": {
          "FieldToMatch": {"Body": {}},
          "TextTransformations": [
            {"Priority": 0, "Type": "URL_DECODE"}
          ]
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "SQLInjection"
      }
    }
  ]'
```

## XSS Protection

```bash
# Cross-site scripting rule
aws wafv2 update-web-acl \
  --rules '[
    {
      "Name": "XSSProtection",
      "Priority": 5,
      "Action": {"Block": {}},
      "Statement": {
        "XssMatchStatement": {
          "FieldToMatch": {"Body": {}},
          "TextTransformations": [
            {"Priority": 0, "Type": "URL_DECODE"}
          ]
        }
      },
      "VisibilityConfig": {
        "SampledRequestsEnabled": true,
        "CloudWatchMetricsEnabled": true,
        "MetricName": "XSSProtection"
      }
    }
  ]'
```

## AWS Shield Integration

### Shield Standard
- Free with WAF
- Protection against common attacks
- L3/L4 protection

### Shield Advanced
- $3,000/month per account
- L7 protection
- DDoS protection
- 24/7 DDoS Response Team

## Monitoring

```bash
# Get Web ACL metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/WAFV2 \
  --metric-name BlockedRequests \
  --dimensions Name=WebACL,Value=my-web-acl Name=Region,Value=us-east-1 \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Sum
```

## Best Practices

1. **Start with managed rule groups**
2. **Implement rate limiting** for APIs
3. **Use geographic blocking** if needed
4. **Enable logging** to S3/CloudWatch
5. **Test rules** before production
6. **Use IP sets** for allowlisting/blocklisting
7. **Implement custom rules** for specific needs
8. **Monitor with CloudWatch** metrics
9. **Use rule groups** for organization
10. **Regular rule review** and updates
