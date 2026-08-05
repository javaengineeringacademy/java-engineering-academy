# Azure Monitor

## Overview

Azure Monitor is a comprehensive monitoring solution for collecting, analyzing, and acting on telemetry from cloud and on-premises environments. It provides metrics, logs, alerts, and dashboards for Azure resources.

## Core Concepts

### Metrics
Time-series data describing the behavior of Azure resources over time.

### Logs
Log Analytics workspace stores log data collected from Azure resources and applications.

### Alerts
Notifications triggered when defined conditions are met in metrics or logs.

### Application Insights
Application performance management feature for monitoring live applications.

## Architecture

```
Azure Resources -> Azure Monitor -> Log Analytics Workspace
                        |                    |
                    Metrics                Logs
                        |                    |
                    Alerts        Dashboards/Workbooks
```

### Data Sources
- **Platform Metrics** - Built-in Azure resource metrics
- **Activity Logs** - Control plane operations
- **Resource Logs** - Resource-specific diagnostic logs
- **Application Logs** - Application-level logging

## Configuration

### Diagnostic Settings
```json
{
  "properties": {
    "logs": [
      {
        "category": "AuditEvent",
        "enabled": true,
        "retentionPolicy": {
          "enabled": true,
          "days": 90
        }
      }
    ],
    "metrics": [
      {
        "category": "AllMetrics",
        "enabled": true,
        "retentionPolicy": {
          "enabled": true,
          "days": 90
        }
      }
    ]
  }
}
```

### Application Insights
```json
{
  "connectionString": "InstrumentationKey=YOUR_KEY;IngestionEndpoint=https://centralus-1.in.applicationinsights.azure.com/",
  "samplingSettings": {
    "isEnabled": true,
    "maxTelemetryItemsPerSecond": 5
  }
}
```

### Log Analytics Query
```kusto
// Request rate and failure rate
requests
| where timestamp > ago(1h)
| summarize 
    RequestCount = count(),
    FailureCount = countif(success == false)
    by bin(timestamp, 5m)
| extend FailureRate = FailureCount / RequestCount * 100

// Top slow operations
requests
| where timestamp > ago(24h)
| summarize 
    AvgDuration = avg(duration),
    P95Duration = percentile(duration, 95),
    Count = count()
    by operation_Name
| order by AvgDuration desc
| take 10
```

## Key Features

### Metrics Explorer
- Pre-built metric charts for Azure resources
- Custom metric creation from logs
- Metric alerts with dynamic thresholds

### Log Analytics
- Kusto Query Language (KQL) for log analysis
- Saved queries and shared dashboards
- Cross-resource querying

### Workbooks
- Interactive reporting templates
- Custom visualization creation
- Data exploration tools

### Alert Rules
```json
{
  "properties": {
    "displayName": "High CPU Alert",
    "severity": 2,
    "evaluationFrequency": "PT5M",
    "windowSize": "PT15M",
    "criteria": {
      "odata.type": "Microsoft.Azure.Monitor.MultipleResourceMultipleMetricCriteria",
      "allOf": [
        {
          "metricName": "Percentage CPU",
          "operator": "GreaterThan",
          "threshold": 80,
          "timeAggregation": "Average"
        }
      ]
    }
  }
}
```

## Best Practices

1. Enable diagnostic settings for all Azure resources
2. Use Log Analytics workspaces for centralized log management
3. Create alert rules with appropriate action groups
4. Use Workbooks for operational dashboards
5. Implement metric alerts with dynamic thresholds
6. Configure Application Insights for all applications
7. Use Azure Monitor Agent for hybrid environments
8. Set up Azure Sentinel for security monitoring
