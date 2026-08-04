# Azure Cost Optimization

## Overview

Azure Cost Optimization helps you reduce your Azure spend while maintaining performance.

## Cost Optimization Pillars

```
┌─────────────────────────────────────────────────────────┐
│                 Cost Optimization                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Rightsiz │  │Reserva-  │  │  Scale   │             │
│  │ ing      │  │  tions   │  │  Smartly │             │
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

## Reservations

```bash
# Purchase reservation
az reservation create \
  --applied-scope-type Shared \
  --billing-scope /providers/Microsoft.Billing/billingAccounts/{account-id} \
  --display-name "VM Reservation" \
  --quantity 10 \
  --reserved-resource-type VirtualMachines \
  --sku Standard_DS1_v2 \
  --term P1Y

# List reservations
az reservation list
```

### Reservation Types
| Type           | Discount | Term    |
|----------------|----------|---------|
| VM Reservations| Up to 72%| 1-3 yrs |
| SQL Reservations| Up to 65%| 1-3 yrs |
| App Service    | Up to 55%| 1-3 yrs |

## Azure Hybrid Benefit

```bash
# Use Hybrid Benefit
az vm create \
  --resource-group myResourceGroup \
  --name myVM \
  --image WindowsServer \
  --license-type Windows_Server

# Benefits:
# - Use existing Windows Server licenses
# - Up to 40% savings
```

## Spot VMs

```bash
# Create spot VM
az vm create \
  --resource-group myResourceGroup \
  --name mySpotVM \
  --image Ubuntu2204 \
  --priority Spot \
  --eviction-policy Deallocate

# Benefits:
# - Up to 90% discount
# - Good for fault-tolerant workloads
```

## Right-Sizing

```bash
# Get recommendations
az advisor recommendation list \
  --category Cost \
  --query "[?impactedResource.displayName=='myVM']"

# Apply recommendation
az advisor recommendation activate \
  --resource-id /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM \
  --recommendation-id {recommendation-id}
```

## Cost Management

```bash
# Get cost data
az costmanagement query \
  --type Usage \
  --timeframe MonthToDate \
  --dataset-aggregation '{"totalCost":{"name":"PreTaxCost","function":"Sum"}}' \
  --grouping '[{"type":"Dimension","name":"ResourceGroup"}]'

# Create budget
az consumption budget create \
  --amount 1000 \
  --category Cost \
  --time-grain Monthly \
  --start-date 2024-01-01 \
  --end-date 2024-12-31 \
  --resource-group myResourceGroup
```

## Free Tier

| Service         | Free Tier                  |
|-----------------|----------------------------|
| Virtual Machines| 750 hours B1s              |
| Storage         | 5 GB LRS                   |
| SQL Database    | 100,000 vCore seconds      |
| Functions       | 1M requests per month      |

## Cost Allocation

```bash
# Add tags
az resource tag \
  --ids /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM \
  --tags Environment=Production Team=Backend

# Query by tags
az costmanagement query \
  --type Usage \
  --timeframe MonthToDate \
  --filter '{"tags":{"name":"Environment","value":"Production"}}'
```

## Advisor Recommendations

```bash
# Get cost recommendations
az advisor recommendation list \
  --category Cost

# Get all recommendations
az advisor recommendation list
```

## Monitoring & Alerts

```bash
# Create cost alert
az consumption alert create \
  --name my-cost-alert \
  --amount 1000 \
  --condition GreaterThan \
  --type Actual \
  --notify admin@example.com
```

## Best Practices

1. **Use Reservations** for steady workloads
2. **Use Hybrid Benefit** for existing licenses
3. **Use Spot VMs** for batch jobs
4. **Right-size resources**
5. **Implement proper tagging**
6. **Set up budgets** and alerts
7. **Use Cost Management**
8. **Monitor with Advisor**
9. **Implement auto-shutdown**
10. **Regular cost reviews**

## Cost Optimization Checklist

- [ ] Use Reservations for steady workloads
- [ ] Use Hybrid Benefit
- [ ] Use Spot VMs for batch jobs
- [ ] Right-size VMs
- [ ] Implement tagging
- [ ] Set up budgets
- [ ] Monitor with Cost Management
- [ ] Use Advisor recommendations
- [ ] Implement auto-shutdown
- [ ] Regular cost reviews
