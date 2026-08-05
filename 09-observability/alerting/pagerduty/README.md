# PagerDuty

## Overview

PagerDuty is a digital operations management platform that provides incident response, alerting, and on-call management. It helps teams coordinate incident response across multiple communication channels and tools.

## Core Concepts

### Incidents
Representations of issues that require attention, with lifecycle management from trigger to resolution.

### Services
Logical groupings of components that can be monitored and have associated escalation policies.

### Escalation Policies
Rules that determine who gets notified and when, with support for rotations and overrides.

### On-Call Schedules
Rotations that define which team members are responsible for responding to incidents.

## Architecture

```
Alert Sources -> PagerDuty API -> Incident Engine -> Notification Channels
                    |                    |
              Event Intelligence    Escalation
              (AI Analysis)         Management
```

### Integration Methods
- **Events API v2** - HTTP-based event ingestion
- **REST API** - Comprehensive incident management
- **Email Integration** - Convert emails to incidents
- **Webhooks** - Real-time event notifications

## Configuration

### Events API Integration
```python
import requests

def trigger_incident(service_key, summary, severity="critical"):
    url = "https://events.pagerduty.com/v2/enqueue"
    payload = {
        "routing_key": service_key,
        "event_action": "trigger",
        "payload": {
            "summary": summary,
            "severity": severity,
            "source": "order-service",
            "component": "payment",
            "group": "production",
            "class": "failure",
            "custom_details": {
                "order_id": "12345",
                "error": "Payment timeout"
            }
        },
        "links": [
            {
                "href": "https://grafana.example.com/dashboard/1",
                "text": "Dashboard"
            }
        ],
        "images": [
            {
                "src": "https://example.com/screenshot.png",
                "alt": "Error Screenshot"
            }
        ]
    }
    
    response = requests.post(url, json=payload)
    return response.json()
```

### Service Configuration
```json
{
  "service": {
    "name": "Order Service",
    "description": "Handles order processing and payments",
    "escalation_policy": {
      "id": "PXXXXXX"
    },
    "alert_creation": "create_incidents",
    "auto_resolve_timeout": "1800",
    "acknowledgement_timeout": "1800"
  }
}
```

### Escalation Policy
```json
{
  "escalation_policy": {
    "name": "Order Service Escalation",
    "escalation_rules": [
      {
        "escalation_delay_in_minutes": 5,
        "targets": [
          {
            "type": "user_reference",
            "id": "PXXXXXX"
          }
        ]
      },
      {
        "escalation_delay_in_minutes": 15,
        "targets": [
          {
            "type": "user_reference",
            "id": "PYYYYYY"
          }
        ]
      }
    ]
  }
}
```

## Key Features

### Incident Intelligence
- AI-powered noise reduction
- Incident grouping and correlation
- Business impact analysis

### On-Call Management
- Schedule creation and rotation
- Override and swap management
- Calendar integration

### Notification Channels
- Push notifications
- SMS and phone calls
- Email
- Chat (Slack, Teams)

### Analytics
- Response time metrics
- Incident frequency analysis
- Team performance dashboards

## Webhook Integration

### Webhook Configuration
```json
{
  "webhook": {
    "url": "https://myapp.example.com/pagerduty-webhook",
    "events": [
      "incident.triggered",
      "incident.acknowledged",
      "incident.resolved"
    ]
  }
}
```

### Webhook Payload
```json
{
  "event": "incident.triggered",
  "incident": {
    "id": "P12345",
    "title": "High Error Rate in Order Service",
    "status": "triggered",
    "urgency": "high",
    "service": {
      "id": "PXXXXXX",
      "name": "Order Service"
    },
    "created_at": "2024-01-15T10:30:00Z"
  }
}
```

## Best Practices

1. Create meaningful service names for team alignment
2. Set up escalation policies for different severity levels
3. Configure appropriate acknowledgement and resolution timeouts
4. Use incident intelligence for noise reduction
5. Integrate with monitoring tools for automated incident creation
6. Regularly review and adjust on-call schedules
7. Use runbooks for consistent incident response
8. Analyze incident data for continuous improvement
