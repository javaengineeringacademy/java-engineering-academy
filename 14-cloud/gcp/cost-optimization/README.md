# Google Cloud Cost Optimization

## Overview

Cost optimization helps you reduce your Google Cloud spend while maintaining performance.

## Cost Optimization Pillars

```
┌─────────────────────────────────────────────────────────┐
│                 Cost Optimization                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Rightsiz │  │ Committed│  │  Scale   │             │
│  │ ing      │  │   Use    │  │  Smartly │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Monitor &    │                          │
│              │  Optimize     │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Committed Use Discounts (CUDs)

```bash
# Purchase CUD
gcloud compute reservations create my-reservation \
  --zone=us-central1-a \
  --count=10 \
  --machine-type=e2-standard-4 \
  --commitment=1y

# List CUDs
gcloud compute reservations list
```

### CUD Types
| Type          | Discount | Term    |
|---------------|----------|---------|
| Compute CUD   | Up to 57%| 1-3 yrs |
| Cloud SQL CUD | Up to 55%| 1-3 yrs |
| Filestore CUD | Up to 55%| 1-3 yrs |

## Sustained Use Discounts

- **Automatic discount** for sustained usage
- **No upfront commitment** required
- **Up to 30% discount** for VMs
- **Applied automatically** to bills

## Right-Sizing

```bash
# Get recommendations
gcloud recommender recommendations list \
  --recommender=google.compute.instance.MachineTypeRecommendation \
  --project=my-project

# Apply recommendation
gcloud recommender recommendations apply RECOMMENDATION_ID
```

### Right-Sizing Tools
| Tool           | Description                    |
|----------------|--------------------------------|
| Recommender    | AI-powered recommendations     |
| Monitoring     | Custom metrics analysis        |
| Cost Explorer  | Cost visualization             |

## Spot VMs

```bash
# Create spot VM
gcloud compute instances create my-spot-vm \
  --zone=us-central1-a \
  --machine-type=e2-medium \
  --provisioning-model=SPOT \
  --instance-termination-action=STOP

# Benefits:
# - Up to 91% discount
# - Good for fault-tolerant workloads
# - Can be preempted
```

## Preemptible VMs (Legacy)

```bash
# Create preemptible VM
gcloud compute instances create my-preemptible-vm \
  --zone=us-central1-a \
  --machine-type=e2-medium \
  --preemptible
```

## Cost Management Tools

### Billing Export
```bash
# Export to BigQuery
gcloud billing budgets create \
  --billing-account=my-billing-account \
  --display-name="My Budget" \
  --budget-amount=1000 \
  --threshold-rule=percent=80

# Export to GCS
gcloud billing accounts export-to-bigquery my-billing-account \
  --dataset=billing_export
```

### Budget Alerts
```bash
# Create budget
gcloud billing budgets create \
  --billing-account=my-billing-account \
  --display-name="Monthly Budget" \
  --budget-amount=1000 \
  --all-updates-rule-pubsub-topic=projects/my-project/topics/my-topic
```

### Cost Reports
```bash
# Get cost report
gcloud billing reports list \
  --billing-account=my-billing-account \
  --month=2024-01
```

## Free Tier

| Service         | Free Tier                  |
|-----------------|----------------------------|
| Compute Engine  | 1 non-shared CPU per month |
| Cloud Storage   | 5 GB standard storage      |
| BigQuery        | 1 TB queries per month     |
| Cloud Functions | 2M invocations per month   |
| Cloud Run       | 2M requests per month      |

## Cost Allocation

```bash
# Create label
gcloud compute instances add-labels my-instance \
  --labels=environment=production,team=backend

# Query by labels
gcloud billing budgets list \
  --filter="labels.environment=production"
```

## Monitoring & Alerts

```bash
# Get cost forecast
gcloud billing budgets describe my-budget \
  --billing-account=my-billing-account

# Get cost anomalies
gcloud billing budgets list \
  --filter="displayName='Monthly Budget'"
```

## Best Practices

1. **Use CUDs** for steady workloads
2. **Use Spot VMs** for fault-tolerant workloads
3. **Right-size resources** based on usage
4. **Implement proper labeling**
5. **Set up budgets** and alerts
6. **Use Free Tier** appropriately
7. **Monitor with Cost Explorer**
8. **Use recommender** for optimization
9. **Implement proper tagging**
10. **Regular cost reviews**

## Cost Optimization Checklist

- [ ] Use CUDs for steady workloads
- [ ] Use Spot VMs for batch jobs
- [ ] Right-size instances
- [ ] Implement auto-scaling
- [ ] Delete unused resources
- [ ] Use appropriate storage class
- [ ] Set up budgets and alerts
- [ ] Monitor with Cost Explorer
- [ ] Use labels for cost allocation
- [ ] Regular cost reviews
