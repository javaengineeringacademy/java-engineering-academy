# AppDynamics

## Overview

AppDynamics is an application performance monitoring (APM) solution from Cisco that provides full-stack observability with business transaction monitoring, code-level diagnostics, and AI-powered root cause analysis.

## Core Concepts

### Business Transactions
AppDynamics automatically discovers and groups application code into business transactions that represent key user activities.

- **Automatic Discovery** - Identifies transactions without manual configuration
- **Custom Transactions** - Define additional transaction patterns
- **Transaction Snapshots** - Detailed call graphs for slow transactions

### Flow Maps
Visual representation of application topology showing service dependencies, data flows, and performance metrics.

### Health Rules
Configurable policies that evaluate application health based on metrics and thresholds.

## Architecture

```
Application -> App Agent -> Controller -> UI/Dashboards
                  |
             Analytics Agent
```

### Components
- **App Agent** - Instruments application code
- **Machine Agent** - Collects infrastructure metrics
- **Controller** - Processes and stores data
- **Events Service** - Handles analytics data

## Configuration

### Agent Installation
```bash
# Java agent
java -javaagent:/opt/appdynamics/appagent/javaagent.jar \
     -Dappdynamics.agent.applicationName=order-service \
     -Dappdynamics.agent.tierName=web-tier \
     -Dappdynamics.agent.nodeName=node-1 \
     -jar application.jar
```

### Analytics Configuration
```xml
<appdynamics-analytics>
  <account-name>my-account</account-name>
  <global-account-name>my-account</global-account-name>
  <license-key>license-key</license-key>
</appdynamics-analytics>
```

## Key Features

### Code-Level Visibility
- Method-level performance data
- SQL query execution tracking
- Stack traces for slow transactions
- Error tracking with full context

### Baseline and Anomaly Detection
- Dynamic baselines that adapt to traffic patterns
- ML-powered anomaly detection
- Comparative analytics across time periods

### Business Intelligence
- Correlate technical performance with business metrics
- Revenue impact analysis
- Custom analytics queries

## Best Practices

1. Name business transactions meaningfully for team alignment
2. Configure health rules based on SLAs and business requirements
3. Use transaction snapshots for root cause analysis
4. Set up dashboards for different audiences (ops, dev, business)
5. Leverage baselines for dynamic threshold alerting
6. Integrate with CI/CD for deployment impact analysis
7. Use analytics for capacity planning
8. Monitor agent overhead and adjust sampling as needed
