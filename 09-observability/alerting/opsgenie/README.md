# OpsGenie

## Overview

OpsGenie is an incident management platform that provides alerting, on-call scheduling, and incident response capabilities. It helps teams manage alerts from multiple sources and coordinate incident response efforts.

## Core Concepts

### Alerts
Notifications triggered by monitoring tools that require attention from on-call teams.

### Incidents
Aggregated alerts representing a single issue that may affect multiple services.

### Escalation Policies
Rules that define notification paths and escalation chains for alerts.

### Schedules
On-call rotations that determine who receives alerts during specific time periods.

## Architecture

```
Monitoring Tools -> OpsGenie API -> Alert Engine -> Notification Channels
                      |                    |
               Dedup/Grouping        Routing Rules
                      |                    |
               Correlation           On-Call Lookup
```

### Integration Methods
- **API** - RESTful interface for alert management
- **Heartbeat** - Dead man's switch for monitoring availability
- **Integrations** - Pre-built connectors for monitoring tools
- **Webhooks** - Outbound notifications to external systems

## Configuration

### API Integration
```python
import requests

def create_alert(api_key, message, alias, description="", priority="P1"):
    url = "https://api.opsgenie.com/v2/alerts"
    headers = {
        "Authorization": f"GenieKey {api_key}",
        "Content-Type": "application/json"
    }
    payload = {
        "message": message,
        "alias": alias,
        "description": description,
        "priority": priority,
        "tags": ["production", "order-service"],
        "details": {
            "order_id": "12345",
            "error": "Payment timeout",
            "service": "order-service"
        },
        "entity": {
            "type": "service",
            "name": "order-service"
        },
        "actions": ["Restart Service", "View Dashboard", "Runbook"]
    }
    
    response = requests.post(url, json=payload, headers=headers)
    return response.json()
```

### Service Configuration
```json
{
  "name": "Order Service",
  "description": "Handles order processing and payments",
  "team": {
    "id": "team-id"
  },
  "acknowledgment": {
    "acknowledge": {
      "enabled": true,
      "time": "5m"
    }
  },
  "auto_close_action": {
    "enabled": true,
    "time": "30m"
  },
  "notify_on": {
    "created": true,
    "updated": true,
    "closed": true
  }
}
```

### Escalation Policy
```json
{
  "name": "Order Service Escalation",
  "escalation": {
    "rules": [
      {
        "escalationTime": {
          "timeAmount": 5,
          "timeUnit": "minutes"
        },
        "notifyIfNotAcknowledged": true,
        "recipient": {
          "type": "team",
          "id": "team-id"
        }
      },
      {
        "escalationTime": {
          "timeAmount": 15,
          "timeUnit": "minutes"
        },
        "notifyIfNotAcknowledged": true,
        "recipient": {
          "type": "user",
          "id": "user-id"
        }
      }
    ]
  }
}
```

## Key Features

### Alert Management
- Alert grouping and deduplication
- Correlation rules for related alerts
- Alert enrichment with context

### On-Call Scheduling
- Rotation schedules with overrides
- Calendar integration
- Time zone support

### Incident Response
- Incident timelines
- Post-incident reports
- Collaboration tools

### Integrations
- 200+ monitoring tool integrations
- Chat platform integration (Slack, Teams)
- ITSM integration (Jira, ServiceNow)

## Heartbeat Monitoring

### Heartbeat Configuration
```json
{
  "heartbeat": {
    "name": "Order Service Health Check",
    "enabled": true,
    "interval": 1,
    "intervalUnit": "minutes",
    "monitoringTime": {
      "beginningTime": "09:00",
      "endTime": "17:00",
      "timezone": "America/New_York"
    }
  }
}
```

### Heartbeat API
```python
def send_heartbeat(api_key, heartbeat_id):
    url = f"https://api.opsgenie.com/v2/heartbeats/{heartbeat_id}/ping"
    headers = {
        "Authorization": f"GenieKey {api_key}"
    }
    
    response = requests.post(url, headers=headers)
    return response.json()
```

## Webhook Integration

### Outbound Integration
```json
{
  "name": "Slack Integration",
  "type": "slack",
  "enabled": true,
  "url": "https://hooks.slack.com/...",
  "filters": [
    {
      "condition": {
        "match": "any",
        "conditions": [
          {
            "field": "priority",
            "operator": "equals",
            "value": "P1"
          }
        ]
      }
    }
  ],
  "payload": {
    "channel": "#incidents",
    "username": "OpsGenie",
    "message": "{{message}}",
    "description": "{{description}}"
  }
}
```

### Inbound Integration
```json
{
  "name": "Prometheus Integration",
  "type": "webhook",
  "enabled": true,
  "allow_write": true,
  "respond_to_alert_actions": {
    "acknowledged": true,
    "closed": true
  }
}
```

## Best Practices

1. Create meaningful alert messages with actionable information
2. Use alert tags for filtering and routing
3. Configure appropriate escalation policies for different teams
4. Set up heartbeat monitoring for critical services
5. Use incident rules for alert correlation
6. Implement alert suppression during maintenance windows
7. Regularly review and optimize on-call schedules
8. Use OpsGenie analytics for continuous improvement
