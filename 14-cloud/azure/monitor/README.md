# Azure Monitor

## Overview

Azure Monitor is a comprehensive monitoring solution for collecting, analyzing, and responding to telemetry.

## Components

```
┌─────────────────────────────────────────────────────────┐
│                  Azure Monitor                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Logs   │  │Metrics   │  │ Alerts   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Dashboards   │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Application Insights

```bash
# Create Application Insights
az monitor app-insights component create \
  --app myappinsights \
  --resource-group myResourceGroup \
  --location eastus \
  --application-type web

# Get instrumentation key
az monitor app-insights component show \
  --app myappinsights \
  --resource-group myResourceGroup \
  --query "instrumentationKey"
```

## Log Analytics Workspace

```bash
# Create workspace
az monitor log-analytics workspace create \
  --resource-group myResourceGroup \
  --workspace-name myworkspace \
  --location eastus

# Query logs
az monitor log-analytics query \
  --workspace myworkspace \
  --analytics-query "Heartbeat | where TimeGenerated > ago(1h) | count"
```

## Metrics

```bash
# Get metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM \
  --metric "Percentage CPU"

# Create metric alert
az monitor metrics alert create \
  --name myalert \
  --resource-group myResourceGroup \
  --scopes /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM \
  --condition "avg Percentage CPU > 80" \
  --action myactiongroup
```

## Log Alerts

```bash
# Create log alert
az monitor scheduled-query create \
  --name mylogalert \
  --resource-group myResourceGroup \
  --scopes /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.OperationalInsights/workspaces/myworkspace \
  --condition "count > 100" \
  --query "Heartbeat | where TimeGenerated > ago(5m)" \
  --action myactiongroup
```

## Action Groups

```bash
# Create action group
az monitor action-group create \
  --resource-group myResourceGroup \
  --name myactiongroup \
  --short-name myag

# Add receiver
az monitor action-group update \
  --resource-group myResourceGroup \
  --name myactiongroup \
  --add-receiver email admin@example.com
```

## Dashboards

```bash
# Create dashboard
az portal dashboard create \
  --resource-group myResourceGroup \
  --name mydashboard \
  --input-path dashboard.json
```

## Diagnostic Settings

```bash
# Enable diagnostic settings
az monitor diagnostic-settings create \
  --name mylogs \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/virtualMachines/myVM \
  --logs '[{"category":"AuditEvent","enabled":true}]' \
  --metrics '[{"category":"AllMetrics","enabled":true}]'
```

## Autoscale

```bash
# Create autoscale setting
az monitor autoscale create \
  --resource-group myResourceGroup \
  --resource myvmss \
  --resource-type Microsoft.Compute/virtualMachineScaleSets \
  --min-count 1 \
  --max-count 10

# Add rule
az monitor autoscale rule create \
  --resource-group myResourceGroup \
  --autoscale-name myvmss \
  --condition "Percentage CPU > 70 avg 5m" \
  --scale out 1
```

## Workbooks

```bash
# Create workbook
az monitor app-insights workbook create \
  --resource-group myResourceGroup \
  --name myworkbook \
  --display-name "My Workbook"
```

## Pricing

| Service          | Cost                          |
|------------------|-------------------------------|
| Metrics          | Free (150 metrics)            |
| Logs             | $2.30/GB ingested             |
| Alerts           | $0.10/alert rule/month        |
| Action Groups    | $0.50/notification             |

## Best Practices

1. **Enable Application Insights**
2. **Use Log Analytics** for analysis
3. **Implement proper alerts**
4. **Use dashboards** for visibility
5. **Enable diagnostic settings**
6. **Implement autoscale**
7. **Use workbooks** for visualization
8. **Monitor costs**
9. **Regular review** of alerts
10. **Implement proper logging**
