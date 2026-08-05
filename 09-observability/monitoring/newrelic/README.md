# New Relic

## Overview

New Relic is a full-stack observability platform that provides application performance monitoring, infrastructure monitoring, log management, and real-user monitoring through a unified interface.

## Core Concepts

### Entities
Everything monitored by New Relic is an entity - services, hosts, databases, and more. Entities are connected through relationships.

### NRQL
New Relic Query Language for querying and analyzing telemetry data.

### Applied Intelligence
AI-powered alerting and root cause analysis capabilities.

## Architecture

```
Applications -> New Relic Agents -> New Relic Platform -> UI/API
                    |
            Telemetry Pipeline
            (Metrics, Events, Logs, Traces)
```

### Agent Types
- **APM Agent** - Application performance monitoring
- **Infrastructure Agent** - Host and container metrics
- **Logs in Context** - Correlated log management
- **Browser Agent** - Real-user monitoring
- **Mobile Agent** - Mobile app monitoring

## Configuration

### Agent Installation
```bash
# Java APM
curl -O https://download.newrelic.com/newrelic/java/newrelic.yml
curl -L https://download.newrelic.com/newrelic/java/newrelic-agent.jar

# Run with agent
java -javaagent:newrelic-agent.jar \
     -Dnewrelic.app_name=Order Service \
     -Dnewrelic.license_key=YOUR_KEY \
     -jar application.jar
```

### Infrastructure Agent
```bash
# Linux
curl -Os https://static.newrelic.com/infrastructure-agent/releases/newrelic-infra_linux_amd64.tar.gz
tar xzf newrelic-infra_linux_amd64.tar.gz
sudo ./install.sh -n -u YOUR_KEY
```

## Key Features

### APM
- Transaction tracing
- Error analytics
- Service maps
- Distributed tracing

### Infrastructure Monitoring
- Host metrics
- Container monitoring (Docker, Kubernetes)
- Cloud integrations
- Network performance

### Log Management
- Log forwarding and parsing
- Log patterns
- Log comparisons
- Logs in context with APM

## NRQL Queries

### Basic Queries
```sql
-- Requests per minute
SELECT rate(count(*), 1 minute) FROM Transaction 
WHERE appName = 'Order Service' 
SINCE 1 hour ago

-- Error rate
SELECT percentage(count(*), WHERE error IS true) 
FROM Transaction 
WHERE appName = 'Order Service'

-- Apdex score
SELECT apdex(duration, t: 0.5) 
FROM Transaction 
WHERE appName = 'Order Service'
```

### Advanced Analytics
```sql
-- Top 5 slowest transactions
SELECT name, duration 
FROM Transaction 
FACET name 
SINCE 1 day ago 
LIMIT 5

-- Throughput by status code
SELECT count(*) 
FROM Transaction 
FACET httpResponseCode 
SINCE 1 hour ago
```

## Alerts

### Alert Conditions
```json
{
  "conditions": [{
    "name": "High Error Rate",
    "type": "static",
    "query": "SELECT percentage(count(*), WHERE error IS true) FROM Transaction WHERE appName = 'Order Service'",
    "critical_threshold": {
      "value": 5,
      "duration_minutes": 5
    }
  }]
}
```

### Anomaly Detection
```json
{
  "conditions": [{
    "name": "Latency Anomaly",
    "type": "anomaly",
    "query": "SELECT average(duration) FROM Transaction WHERE appName = 'Order Service'",
    "anomaly_direction": "upper",
    "sensitivity": "high"
  }]
}
```

## Best Practices

1. Use consistent entity naming across all services
2. Configure distributed tracing for microservices
3. Create dashboards for different team perspectives
4. Use NRQL for custom analytics and reporting
5. Set up alert escalation policies
6. Leverage Applied Intelligence for proactive detection
7. Use service maps for dependency visualization
8. Monitor browser and mobile performance for end-user experience
