# AWS Cost Optimization

## Overview

AWS Cost Optimization is the process of reducing your overall AWS spend by identifying resources and opportunities to save money.

## Cost Optimization Pillars

```
┌─────────────────────────────────────────────────────────┐
│                 Cost Optimization                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Rightsiz │  │ Purchase │  │  Scale   │             │
│  │ ing      │  │ Options  │  │  Smartly │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Measure &    │                          │
│              │  Monitor      │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Right-Sizing

### EC2 Right-Sizing
```bash
# Get recommendations
aws ce get-rightsizing-recommendation \
  --service "Amazon EC2" \
  --configuration DaysSinceLaunch=30

# Apply recommendation
aws ec2 modify-instance-attribute \
  --instance-id i-1234567890abcdef0 \
  --instance-type "{\"Value\": \"t3.medium\"}"
```

### RDS Right-Sizing
```bash
# Get RDS recommendations
aws ce get-rightsizing-recommendation \
  --service "Amazon RDS" \
  --configuration DaysSinceLaunch=30
```

## Purchasing Options

### Reserved Instances (RI)
| Term    | Discount | Payment Options        |
|---------|----------|------------------------|
| 1 year  | Up to 40%| All Upfront/Partial/No |
| 3 year  | Up to 60%| All Upfront/Partial/No |

```bash
# Purchase RI
aws ec2 purchase-reserved-instances-offering \
  --reserved-instances-offering-id abc123def456 \
  --instance-count 1

# Convertible RI
aws ec2 purchase-reserved-instances-offering \
  --reserved-instances-offering-id abc123def456 \
  --instance-count 1
```

### Savings Plans
```bash
# Compute Savings Plans
aws savingsplans create-savings-plan \
  --savings-plan-type Compute \
  --term-duration-seconds 31536000 \
  --hourly-commitment 0.5 \
  --purchase-time $(date -u +%Y-%m-%dT%H:%M:%SZ)

# EC2 Instance Savings Plans
aws savingsplans create-savings-plan \
  --savings-plan-type EC2Instance \
  --term-duration-seconds 31536000 \
  --hourly-commitment 0.5 \
  --instance-family m5 \
  --region us-east-1
```

### Spot Instances
```bash
# Request Spot Instance
aws ec2 run-instances \
  --instance-type m5.large \
  --spot-price "0.05" \
  --instance-market-options '{
    "MarketType": "spot",
    "SpotOptions": {
      "SpotInstanceType": "persistent",
      "InstanceInterruptionBehavior": "stop"
    }
  }'
```

## Cost Explorer

```bash
# Get cost and usage
aws ce get-cost-and-usage \
  --time-period Start=2024-01-01,End=2024-01-31 \
  --granularity MONTHLY \
  --metrics "BlendedCost" "UnblendedCost" \
  --group-by Type=DIMENSION,Key=SERVICE
```

### Cost Explorer Features
- **Cost visualization**
- **Trend analysis**
- **Forecasting**
- **Reservation recommendations**
- **Savings Plans recommendations**

## Budgets

```bash
# Create budget
aws budgets create-budget \
  --account-id 123456789012 \
  --budget '{
    "BudgetName": "MonthlyBudget",
    "BudgetLimit": {
      "Amount": "1000",
      "Unit": "USD"
    },
    "TimeUnit": "MONTHLY",
    "BudgetType": "COST"
  }' \
  --notifications-with-subscribers '[
    {
      "Notification": {
        "NotificationType": "ACTUAL",
        "ComparisonOperator": "GREATER_THAN",
        "Threshold": 80
      },
      "Subscribers": [
        {
          "SubscriptionType": "EMAIL",
          "Address": "admin@example.com"
        }
      ]
    }
  ]'
```

## Resource Tagging

```bash
# Tag resources
aws ec2 create-tags \
  --resources i-1234567890abcdef0 \
  --tags Key=Environment,Value=production Key=Team,Value=backend

# Cost allocation tags
aws ce create-cost-allocation-tag \
  --tag-name Environment
```

## Storage Optimization

### S3 Lifecycle
```json
{
  "Rules": [
    {
      "ID": "MoveToIA",
      "Transitions": [
        { "Days": 30, "StorageClass": "STANDARD_IA" },
        { "Days": 90, "StorageClass": "GLACIER" }
      ],
      "Expiration": { "Days": 365 }
    }
  ]
}
```

### EBS Optimization
```bash
# Change volume type
aws ec2 modify-volume \
  --volume-id vol-12345678 \
  --volume-type gp3 \
  --iops 3000 \
  --throughput 125
```

## Lambda Optimization

### Memory Optimization
```python
# Test different memory configurations
import boto3
import time

def test_lambda_memory():
    memory_sizes = [128, 256, 512, 1024]
    for memory in memory_sizes:
        # Update function memory
        # Test invocation
        # Measure duration and cost
        pass
```

## Monitoring & Alerts

```bash
# Get cost forecast
aws ce get-cost-forecast \
  --time-period Start=2024-02-01,End=2024-02-28 \
  --metric BLENDED_COST \
  --granularity MONTHLY

# Set up anomaly detection
aws ce create-anomaly-detector \
  --anomaly-detector '{
    "AnomalyDetectorName": "CostAnomaly",
    "MonitorType": "DIMENSIONAL",
    "DimensionKey": "SERVICE"
  }'
```

## Cost Allocation

```bash
# Enable cost allocation tags
aws ce create-cost-allocation-tag \
  --tag-name Environment

# Create cost allocation report
aws ce create-cost-allocation-tag \
  --tag-name Team
```

## Best Practices

1. **Right-size resources** based on usage
2. **Use Reserved Instances** for steady workloads
3. **Use Spot Instances** for fault-tolerant workloads
4. **Implement auto-scaling**
5. **Delete unused resources**
6. **Use lifecycle policies** for storage
7. **Monitor with Cost Explorer**
8. **Set up budgets** and alerts
9. **Tag resources** for cost allocation
10. **Regular cost reviews**
