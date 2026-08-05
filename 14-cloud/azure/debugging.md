# Azure Debugging Guide

## Overview

Debugging in Azure involves using CLI tools, portal diagnostics, and log analysis to identify and resolve issues in cloud resources and applications.

## Azure CLI Debugging

### Enable Verbose Output

```bash
az <command> --debug
az <command> --verbose
```

### Resource Inspection

```bash
# Check resource status
az vm show --resource-group myRG --name myVM --query "provisioningState"

# List events for a resource
az resource show --resource-group myRG --name myVM \
  --resource-type Microsoft.Compute/virtualMachines \
  --api-version 2023-03-01
```

### Activity Log Queries

```bash
# Recent operations
az activity-log list --resource-group myRG \
  --start-time "2024-01-01T00:00:00Z" \
  --output table

# Filter by operation name
az activity-log list --resource-group myRG \
  --query "[?contains(operationName.value, 'delete')]"
```

## Azure Portal Debugging

### Activity Log

Access the Activity Log blade for the resource group or subscription to see all control-plane operations.

- Filter by time range, operation name, or resource
- View request and response details
- Correlate operations across services
- Export logs for external analysis

### Resource Health

The Resource Health blade shows the health status of Azure services.

- Check platform-initiated events
- View service health advisories
- Monitor ongoing planned maintenance
- Track health history

### Metrics Explorer

Use Metrics in the Azure Portal to visualize resource performance.

- Select the resource and metric namespace
- Add multiple metrics for comparison
- Apply aggregation (average, sum, count)
- Pin metrics to dashboards

## Log Analytics Debugging

### KQL Queries for Troubleshooting

```kusto
# VM performance issues
Perf
| where TimeGenerated > ago(1h)
| where ObjectName == "Processor" and CounterName == "% Processor Time"
| summarize avg(CounterValue) by Computer
| where avg_Processor_Time > 80
```

```kusto
# Failed deployments
AzureDiagnostics
| where Category == "ResourceDeploymentOperation"
| where status_s == "Failed"
| project TimeGenerated, resourceId, operationName, status_s
```

```kusto
# Application exceptions
exceptions
| where TimeGenerated > ago(24h)
| summarize count() by type, message
| order by count_ desc
```

## VM Debugging

### Boot Diagnostics

Enable boot diagnostics to troubleshoot VM startup failures.

```bash
az vm boot-diagnostics get-boot-log --resource-group myRG --name myVM
```

### Serial Console

Access the VM serial console for boot-level debugging.

```bash
az vm boot-diagnostics enable --resource-group myRG --name myVM
az vm boot-diagnostics get-serial-port --resource-group myRG --name myVM
```

### Agent Troubleshooting

```bash
# Check VM agent status
az vm get-instance-view --resource-group myRG --name myVM \
  --query "instanceView.extensions[?type=='Microsoft.Azure.Extensions.CustomScript']"
```

## Network Debugging

### Connectivity Issues

```bash
# Check NSG rules
az network nsg rule list --resource-group myRG --nsg-name myNSG --output table

# View effective security rules
az network nic effective-route-table show --resource-group myRG --name myNIC

# DNS resolution
az network private-dns record-set list --resource-group myRG \
  --zone-name privatelink.database.windows.net --output table
```

### Network Watcher

Use Network Watcher for network diagnostics.

```bash
# IP flow verify
az network watcher ip-flow-verify --resource-group myRG \
  --vm myVM --direction Inbound --local 443 --protocol TCP

# Packet capture
az network watcher packet-capture create --resource-group myRG \
  --vm myVM --name myCapture --storage-account myStorage
```

## Application Debugging

### Application Insights

Query Application Insights for application-level debugging.

```kusto
# Failed requests
requests
| where success == false
| order by timestamp desc
| take 50
```

```kusto
# Performance issues
dependencies
| where duration > 5s
| summarize count() by target
| order by count_ desc
```

### Live Metrics

Use Application Insights Live Metrics to observe real-time request rates and failures.

## Deployment Debugging

### Deployment History

```bash
# View deployment operations
az deployment group list --resource-group myRG --output table

# Get deployment details
az deployment group show --resource-group myRG --name myDeployment
```

### What-If Operations

```bash
az deployment group what-if --resource-group myRG \
  --template-file main.bicep
```

## Common Debugging Commands

| Task | Command |
|---|---|
| Check resource state | `az resource show` |
| View activity log | `az activity-log list` |
| List diagnostic settings | `az monitor diagnostic-settings list` |
| Query Log Analytics | `az monitor log-analytics query` |
| Test connectivity | `az network watcher test-connectivity` |
