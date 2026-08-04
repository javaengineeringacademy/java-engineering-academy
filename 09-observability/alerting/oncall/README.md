# On-Call Management

## Overview

On-call management ensures 24/7 incident response through rotation schedules, escalation policies, and integration with alerting systems.

## Components

### Rotation Schedules
- **Weekly rotation** - One person on-call for a week
- **Daily rotation** - Daily handoffs
- **Follow-the-sun** - Geographic distribution

### Escalation Policies
```
Level 1: Primary on-call engineer
  ↓ (if no acknowledgment in 5 min)
Level 2: Secondary on-call engineer
  ↓ (if no acknowledgment in 10 min)
Level 3: Engineering manager
  ↓ (if no acknowledgment in 15 min)
Level 4: VP of Engineering
```

## PagerDuty Integration

### Configuration
```yaml
# pagerduty.yml
integrations:
  - name: prometheus
    service_key: 'your-integration-key'
    severity_map:
      critical: critical
      warning: error
      info: info
```

### API Integration
```java
@Service
public class PagerDutyService {
    @Value("${pagerduty.integration-key}")
    private String integrationKey;
    
    public void triggerIncident(Alert alert) {
        Map<String, Object> payload = Map.of(
            "routing_key", integrationKey,
            "event_action", "trigger",
            "payload", Map.of(
                "summary", alert.getSummary(),
                "severity", alert.getSeverity(),
                "source", alert.getService()
            )
        );
        // Send to PagerDuty Events API
    }
}
```

## Best Practices

1. Document runbooks for common alerts
2. Limit on-call duration (max 1 week)
3. Provide compensation for on-call
4. Conduct blameless post-mortems
5. Track and reduce toil
6. Use automation for common fixes
7. Rotate who handles incidents
8. Review and improve processes quarterly
