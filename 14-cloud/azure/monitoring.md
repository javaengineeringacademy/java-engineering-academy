# Azure Monitoring

## Overview

Azure Monitor is a comprehensive monitoring solution for collecting, analyzing, and responding to telemetry from cloud and on-premises environments.

## Data Collection

### Metrics

Numerical time-series data collected at regular intervals.

- Platform metrics from Azure services
- Guest metrics from VMs via agents
- Custom metrics via SDK or REST API
- Stored for up to 93 days

### Logs

Collected via Log Analytics workspace using Kusto Query Language (KQL).

- Azure Activity Logs
- Resource diagnostic logs
- Application logs
- Security logs
- Custom logs via data collection rules

### Application Insights

Application Performance Management (APM) for live applications.

- Request rates and response times
- Dependency call tracking
- Exception logging
- Distributed tracing
- Availability monitoring (URL ping tests)

## Log Analytics Workspace

Central repository for log data from Azure and hybrid environments.

### KQL Example

```kusto
Heartbeat
| where TimeGenerated > ago(24h)
| summarize count() by Computer
| order by count_ desc
```

### Data Sources

| Source | Description |
|---|---|
| Azure Activity Log | Control-plane operations |
| Syslog | Linux system logs |
| Windows Event Logs | Windows system logs |
| Performance Counters | OS and app metrics |
| Custom Logs | Application-specific logs |

### Data Collection Rules

Define what data to collect and where to send it:

- Source resources (VMs, namespaces)
- Transform queries for filtering
- Destinations (workspaces, stores)

## Alerts

Proactive notification when metrics or logs indicate issues.

### Alert Types

- **Metric alerts** - Based on metric thresholds
- **Log alerts** - Based on KQL query results
- **Activity log alerts** - Based on Azure operations
- **Smart detection** - ML-based anomaly detection
- **Prometheus alerts** - Prometheus-compatible alerts

### Action Groups

Define actions triggered by alerts:

- Email and SMS notifications
- Push notifications via mobile app
- Azure Functions for automated remediation
- Logic Apps for workflow integration
- ITSM integration for ticket creation

## Workbooks

Interactive reports combining metrics, logs, and other data sources.

- Pre-built templates for common scenarios
- Custom workbook creation with KQL
- Shared workbooks across teams
- Export to PDF for reporting

## Dashboards

Pin metrics and charts to customizable dashboards.

- Real-time monitoring views
- Team-specific monitoring surfaces
- Shared across subscriptions
- Mobile-friendly layout

## Azure Monitor for Containers

Monitoring for AKS and Azure Container Instances.

- Container health and resource utilization
- Pod and node-level metrics
- Kubernetes events
- Container Insights with KQL queries

## Azure Monitor for VMs

Comprehensive monitoring for virtual machines.

- Performance metrics (CPU, memory, disk, network)
- Dependency mapping (processes and connections)
- Boot diagnostics for startup issues
- Guest health monitoring

## Autoscale Integration

Automatic scaling based on monitoring signals.

- Scale out when CPU exceeds threshold
- Scale in when load decreases
- Scheduled scaling for predictable patterns
- Multiple instance metrics for complex rules

## Cost Management

- View monitoring costs in Azure Cost Management
- Set up budget alerts for Log Analytics
- Archive old logs to cheaper storage tiers
- Use basic logs for low-frequency queries
- Enable sampling in Application Insights

## Best Practices

- Use data collection rules for consistent log configuration
- Create alert rules for critical metrics
- Use action groups for automated responses
- Leverage workbooks for team dashboards
- Archive logs to storage for long-term retention
