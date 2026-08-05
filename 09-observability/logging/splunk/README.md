# Splunk

## Overview

Splunk is an enterprise platform for searching, monitoring, and analyzing machine-generated data. It provides real-time visibility into IT infrastructure, applications, and security through its powerful query language and analytics capabilities.

## Core Concepts

### Indexes
Repositories where Splunk stores and retrieves data. Data is stored in buckets across different tiers.

### Search Processing Language (SPL)
Splunk's query language for searching, filtering, and analyzing data.

### Saved Searches
Reusable queries that can be scheduled, alerted, and shared across teams.

## Architecture

```
Data Sources -> Forwarders -> Indexers -> Search Heads -> Users
                  |              |              |
             Parse/Route    Store/Index    Query/Dashboard
```

### Components
- **Forwarders** - Lightweight agents that send data to indexers
- **Indexers** - Store and index incoming data
- **Search Heads** - Handle search queries and dashboards
- **Deployment Server** - Central configuration management

## Configuration

### Universal Forwarder
```ini
# outputs.conf
[tcpout:indexers]
server = indexer1:9997,indexer2:9997

[tcpout:indexers:indexer1]
server = indexer1:9997

[tcpout:indexers:indexer2]
server = indexer2:9997

# props.conf
[mysourcetype]
TIME_FORMAT = %Y-%m-%dT%H:%M:%S.%NZ
TIME_PREFIX = "timestamp"
MAX_TIMESTAMP_LOOKAHEAD = 30
```

### Heavy Forwarder
```ini
# inputs.conf
[monitor:///var/log/app/*.log]
sourcetype = app_logs
index = main
disabled = false

# transforms.conf
[extract-service]
REGEX = "service=(?<service>\w+)"
FORMAT = service::$1
```

## Key Features

### Data Collection
- Universal and heavy forwarders
- HTTP Event Collector (HEC)
- Modular inputs for custom data sources

### Search and Analytics
- Real-time streaming searches
- Statistical aggregations and reports
- Correlation across multiple data sources

### Dashboards and Visualizations
- Simple XML dashboards
- Dashboard Studio for advanced visualizations
- Splunk Embedded Reporting

## SPL Queries

### Basic Search
```spl
index=main sourcetype=app_logs level=ERROR
| fields _time, service, message
| sort -_time
| head 100
```

### Statistical Analysis
```spl
index=main sourcetype=app_logs
| stats count by service, level
| sort -count
```

### Transaction Analysis
```spl
index=main sourcetype=app_logs user_id=*
| transaction user_id maxspan=1h
| stats avg(duration) as avg_duration by user_id
| sort -avg_duration
```

### Timechart
```spl
index=main sourcetype=app_logs level=ERROR
| timechart span=1h count by service
```

## Dashboards

### Simple XML Dashboard
```xml
<dashboard version="1.1" theme="dark">
  <title>Application Overview</title>
  <row>
    <panel>
      <title>Request Rate</title>
      <chart>
        <search>
          <query>
            index=main sourcetype=app_logs
            | timechart span=5m count by service
          </query>
        </search>
      </chart>
    </panel>
    <panel>
      <title>Error Rate</title>
      <chart>
        <search>
          <query>
            index=main sourcetype=app_logs level=ERROR
            | timechart span=5m count
          </query>
        </search>
      </chart>
    </panel>
  </row>
</dashboard>
```

## Alerts

### Alert Configuration
```ini
# savedsearches.conf
[High Error Rate]
search = index=main sourcetype=app_logs level=ERROR | stats count | where count > 100
alert.severity = 3
alert.track = 1
actions = email,webhook
action.email.to = team@example.com
action.webhook.url = https://hooks.slack.com/...
```

## Best Practices

1. Use appropriate data models for efficient searching
2. Implement field extractions at index time when possible
3. Use summary indexing for expensive queries
4. Create accelerations for common dashboards
5. Implement role-based access control
6. Use forwarders for efficient data routing
7. Monitor index performance and storage
8. Regularly review and optimize saved searches
