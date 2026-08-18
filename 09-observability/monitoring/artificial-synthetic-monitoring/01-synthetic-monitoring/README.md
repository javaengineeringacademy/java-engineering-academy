# Synthetic Monitoring

## Overview

Synthetic monitoring is a proactive observability technique that simulates user interactions with applications and services to detect failures, measure performance, and verify availability before real users are affected.

---

## Core Concepts

### What is Synthetic Monitoring?

Synthetic monitoring involves automated scripts that execute predefined user journeys at regular intervals. These scripts mimic real user behavior to provide consistent, repeatable measurements of application health.

**Key Characteristics:**
- **Proactive Detection:** Identifies issues before users encounter them
- **Consistent Measurement:** Same inputs produce comparable results
- **External Perspective:** Monitors from the user's point of view
- **24/7 Coverage:** Operates continuously regardless of user traffic
- **Controlled Environment:** Eliminates variables in testing

### Types of Synthetic Monitoring

| Type | Description | Use Case |
|------|-------------|----------|
| **HTTP/API** | Tests REST, GraphQL endpoints | API health verification |
| **Browser** | Renders pages with real browsers | Full user experience |
| **Network** | ICMP, DNS, SSL checks | Infrastructure health |
| **Transaction** | Multi-step user journeys | Critical path validation |

---

## Implementation Guide

### Step 1: Identify Critical Paths

```python
CRITICAL_PATHS = [
    {
        'name': 'User Login',
        'type': 'transaction',
        'steps': [
            {'action': 'navigate', 'url': '/login'},
            {'action': 'fill', 'selector': '#email', 'value': 'test@example.com'},
            {'action': 'fill', 'selector': '#password', 'value': 'password'},
            {'action': 'click', 'selector': 'button[type="submit"]'},
            {'action': 'wait', 'selector': '#dashboard'},
        ]
    },
    {
        'name': 'API Health',
        'type': 'http',
        'url': '/api/health',
        'method': 'GET',
        'expected_status': 200
    },
    {
        'name': 'Search',
        'type': 'transaction',
        'steps': [
            {'action': 'navigate', 'url': '/search'},
            {'action': 'fill', 'selector': '#search-input', 'value': 'test query'},
            {'action': 'click', 'selector': '#search-button'},
            {'action': 'wait', 'selector': '.search-results'},
        ]
    }
]
```

### Step 2: Configure Monitoring Intervals

| Check Type | Recommended Interval | Reason |
|------------|---------------------|--------|
| HTTP Health | 30-60 seconds | Quick detection of outages |
| Browser Synthetic | 5-15 minutes | Resource intensive |
| DNS Check | 5-10 minutes | Changes are infrequent |
| SSL Check | 24 hours | Certificates change monthly |
| Transaction | 1-5 minutes | Balance speed vs cost |

### Step 3: Set Up Metrics Collection

```python
from prometheus_client import Histogram, Counter, Gauge

# Define metrics
CHECK_DURATION = Histogram(
    'synthetic_check_duration_seconds',
    'Duration of synthetic checks',
    ['check_type', 'endpoint'],
    buckets=[0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0]
)

CHECK_RESULT = Counter(
    'synthetic_check_result_total',
    'Result of synthetic checks',
    ['check_type', 'endpoint', 'result']
)

AVAILABILITY = Gauge(
    'synthetic_availability_ratio',
    'Current availability ratio',
    ['service']
)
```

---

## Best Practices

### 1. Start Simple
Begin with basic HTTP health checks before adding complex browser transactions.

### 2. Use Realistic Data
Synthetic scripts should use data that resembles real user inputs.

### 3. Monitor from Multiple Locations
Geographic diversity reveals regional issues.

### 4. Set Appropriate Thresholds
Alert on meaningful degradation, not minor fluctuations.

### 5. Integrate with Alerting
Connect synthetic results to your incident response workflow.

---

## Common Pitfalls

| Pitfall | Impact | Solution |
|---------|--------|----------|
| Too many checks | Resource waste, false positives | Focus on critical paths |
| Unrealistic data | False confidence | Use production-like data |
| Single location | Miss regional issues | Multi-region deployment |
| Ignoring recovery | Extended outages | Monitor recovery time |
| Static thresholds | Alert fatigue | Use adaptive thresholds |

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│            Synthetic Monitoring Platform              │
├─────────────────────────────────────────────────────┤
│                                                       │
│  ┌─────────────┐    ┌─────────────┐                  │
│  │  Scheduler  │───▶│   Runner    │                  │
│  └─────────────┘    └──────┬──────┘                  │
│                            │                          │
│              ┌─────────────┼─────────────┐           │
│              │             │             │            │
│        ┌─────▼─────┐ ┌────▼────┐ ┌─────▼─────┐     │
│        │   HTTP    │ │ Browser │ │  Network  │     │
│        │  Checks   │ │ Checks  │ │  Checks   │     │
│        └─────┬─────┘ └────┬────┘ └─────┬─────┘     │
│              │             │             │            │
│              └─────────────┼─────────────┘           │
│                            │                          │
│                    ┌───────▼───────┐                  │
│                    │   Collector   │                  │
│                    └───────┬───────┘                  │
│                            │                          │
│              ┌─────────────┼─────────────┐           │
│              │             │             │            │
│        ┌─────▼─────┐ ┌────▼────┐ ┌─────▼─────┐     │
│        │Prometheus │ │  Logs   │ │  Alerts   │     │
│        └───────────┘ └─────────┘ └───────────┘     │
│                                                       │
└─────────────────────────────────────────────────────┘
```

---

## Next Steps

- [API Monitoring](../02-api-monitoring/README.md) - REST, GraphQL, and gRPC monitoring
- [Browser Monitoring](../03-browser-monitoring/README.md) - Browser-based synthetic checks
- [Load Testing](../04-load-testing/README.md) - Load testing as monitoring
