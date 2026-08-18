# Dashboard Integration

## Overview

Dashboard integration brings together all synthetic and artificial monitoring data into cohesive visualizations. A well-designed dashboard provides at-a-glance health status, detailed drill-down capabilities, and actionable insights.

---

## Dashboard Architecture

### Layered Dashboard Approach

```
┌─────────────────────────────────────────────────────┐
│                    Executive View                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐          │
│  │ Overall  │  │   SLA    │  │  Error   │          │
│  │   SLA    │  │  Budget  │  │  Budget  │          │
│  └──────────┘  └──────────┘  └──────────┘          │
├─────────────────────────────────────────────────────┤
│                   Service View                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐          │
│  │   API    │  │ Browser  │  │  Infra   │          │
│  │  Health  │  │  Health  │  │  Health  │          │
│  └──────────┘  └──────────┘  └──────────┘          │
├─────────────────────────────────────────────────────┤
│                   Detail View                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐          │
│  │ Endpoint │  │  Browser │  │  DNS/SSL │          │
│  │  Detail  │  │  Detail  │  │  Detail  │          │
│  └──────────┘  └──────────┘  └──────────┘          │
└─────────────────────────────────────────────────────┘
```

---

## Grafana Dashboard Implementation

### Executive Dashboard

```json
{
  "dashboard": {
    "title": "Synthetic Monitoring - Executive",
    "uid": "synthetic-executive",
    "panels": [
      {
        "title": "Overall Availability",
        "type": "stat",
        "gridPos": { "h": 4, "w": 8, "x": 0, "y": 0 },
        "targets": [{
          "expr": "avg(synthetic_check_success_rate)",
          "legendFormat": "Availability"
        }],
        "fieldConfig": {
          "defaults": {
            "unit": "percentunit",
            "thresholds": {
              "steps": [
                { "value": 0, "color": "red" },
                { "value": 0.99, "color": "yellow" },
                { "value": 0.999, "color": "green" }
              ]
            }
          }
        }
      },
      {
        "title": "SLA Compliance",
        "type": "gauge",
        "gridPos": { "h": 4, "w": 8, "x": 8, "y": 0 },
        "targets": [{
          "expr": "sla_compliance_ratio",
          "legendFormat": "{{ service }}"
        }],
        "fieldConfig": {
          "defaults": {
            "min": 0.9,
            "max": 1,
            "unit": "percentunit",
            "thresholds": {
              "steps": [
                { "value": 0.9, "color": "red" },
                { "value": 0.99, "color": "yellow" },
                { "value": 0.999, "color": "green" }
              ]
            }
          }
        }
      },
      {
        "title": "Error Budget Remaining",
        "type": "stat",
        "gridPos": { "h": 4, "w": 8, "x": 16, "y": 0 },
        "targets": [{
          "expr": "error_budget_remaining_ratio",
          "legendFormat": "{{ service }}"
        }],
        "fieldConfig": {
          "defaults": {
            "unit": "percentunit",
            "thresholds": {
              "steps": [
                { "value": 0, "color": "red" },
                { "value": 0.25, "color": "yellow" },
                { "value": 0.5, "color": "green" }
              ]
            }
          }
        }
      },
      {
        "title": "Response Time Trend",
        "type": "timeseries",
        "gridPos": { "h": 8, "w": 24, "x": 0, "y": 4 },
        "targets": [
          {
            "expr": "histogram_quantile(0.50, rate(http_request_duration_seconds_bucket[5m]))",
            "legendFormat": "P50"
          },
          {
            "expr": "histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m]))",
            "legendFormat": "P95"
          },
          {
            "expr": "histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m]))",
            "legendFormat": "P99"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "unit": "s",
            "custom": {
              "drawStyle": "line",
              "lineWidth": 2,
              "fillOpacity": 10
            }
          }
        }
      }
    ]
  }
}
```

### Service Health Dashboard

```json
{
  "dashboard": {
    "title": "Synthetic Monitoring - Service Health",
    "uid": "synthetic-service-health",
    "panels": [
      {
        "title": "Monitor Status Overview",
        "type": "table",
        "gridPos": { "h": 8, "w": 12, "x": 0, "y": 0 },
        "targets": [{
          "expr": "synthetic_check_success_rate",
          "format": "table",
          "instant": true
        }],
        "transformations": [
          {
            "id": "organize",
            "options": {
              "excludeByName": {
                "Time": true
              },
              "renameByName": {
                "monitor": "Monitor",
                "Value": "Success Rate"
              }
            }
          }
        ]
      },
      {
        "title": "Active Alerts",
        "type": "alertlist",
        "gridPos": { "h": 8, "w": 12, "x": 12, "y": 0 },
        "options": {
          "alertName": "",
          "dashboardAlerts": false,
          "stateFilter": {
            "firing": true,
            "pending": true,
            "noData": true,
            "normal": false,
            "error": true
          },
          "limitOptions": {
            "limitCount": 10
          }
        }
      },
      {
        "title": "Check Duration Distribution",
        "type": "heatmap",
        "gridPos": { "h": 8, "w": 24, "x": 0, "y": 8 },
        "targets": [{
          "expr": "rate(synthetic_check_duration_seconds_bucket[5m])",
          "legendFormat": "{{ le }}"
        }]
      },
      {
        "title": "SSL Certificate Status",
        "type": "table",
        "gridPos": { "h": 6, "w": 12, "x": 0, "y": 16 },
        "targets": [{
          "expr": "ssl_certificate_days_remaining",
          "format": "table",
          "instant": true
        }],
        "fieldConfig": {
          "defaults": {
            "custom": {
              "cellOptions": {
                "type": "color-background"
              }
            },
            "thresholds": {
              "steps": [
                { "value": 0, "color": "red" },
                { "value": 7, "color": "orange" },
                { "value": 30, "color": "yellow" },
                { "value": 90, "color": "green" }
              ]
            }
          }
        }
      },
      {
        "title": "DNS Resolution Time",
        "type": "timeseries",
        "gridPos": { "h": 6, "w": 12, "x": 12, "y": 16 },
        "targets": [{
          "expr": "dns_resolution_duration_seconds",
          "legendFormat": "{{ domain }} - {{ record_type }}"
        }]
      }
    ]
  }
}
```

---

## Dashboard Panels

### Common Panel Types

| Panel Type | Use Case | Best For |
|------------|----------|----------|
| **Stat** | Single value KPIs | Availability, SLA |
| **Gauge** | Threshold visualization | Error budget, Compliance |
| **Timeseries** | Trend analysis | Latency, Throughput |
| **Table** | Detailed data | Monitor status, SSL |
| **Heatmap** | Distribution | Duration distribution |
| **Alertlist** | Active issues | Incidents, Warnings |

### Panel Configuration Examples

```json
{
  "stat_panel": {
    "type": "stat",
    "options": {
      "reduceOptions": {
        "calcs": ["lastNotNull"],
        "fields": "",
        "values": false
      },
      "orientation": "auto",
      "textMode": "auto",
      "colorMode": "background",
      "graphMode": "area"
    }
  },
  
  "timeseries_panel": {
    "type": "timeseries",
    "options": {
      "tooltip": {
        "mode": "multi",
        "sort": "desc"
      },
      "legend": {
        "displayMode": "table",
        "placement": "bottom",
        "calcs": ["mean", "max", "min"]
      }
    }
  },
  
  "heatmap_panel": {
    "type": "heatmap",
    "options": {
      "calculate": false,
      "yAxis": {
        "unit": "s"
      },
      "color": {
        "mode": "scheme",
        "scheme": "Oranges"
      }
    }
  }
}
```

---

## Dashboard Variables

### Dynamic Filtering

```json
{
  "dashboard": {
    "templating": {
      "list": [
        {
          "name": "service",
          "type": "query",
          "query": "label_values(synthetic_check_success_rate, service)",
          "refresh": 2,
          "multi": true,
          "includeAll": true
        },
        {
          "name": "region",
          "type": "query",
          "query": "label_values(synthetic_check_success_rate{service=~\"$service\"}, region)",
          "refresh": 2,
          "multi": true,
          "includeAll": true
        },
        {
          "name": "interval",
          "type": "interval",
          "query": "1m,5m,15m,30m,1h",
          "auto": false,
          "current": {
            "text": "5m",
            "value": "5m"
          }
        }
      ]
    }
  }
}
```

---

## Report Generation

### Automated Reports

```python
from datetime import datetime, timedelta
from typing import Dict, List
import json

class MonitoringReport:
    def __init__(self, prometheus_client):
        self.prom = prometheus_client
    
    def generate_daily_report(self, date: datetime) -> Dict:
        """Generate daily monitoring report."""
        start = date.replace(hour=0, minute=0, second=0)
        end = start + timedelta(days=1)
        
        return {
            'date': date.isoformat(),
            'summary': self._get_summary(start, end),
            'availability': self._get_availability(start, end),
            'performance': self._get_performance(start, end),
            'incidents': self._get_incidents(start, end),
            'sla': self._get_sla_compliance(start, end),
            'recommendations': self._generate_recommendations(start, end)
        }
    
    def _get_summary(self, start, end) -> Dict:
        """Get summary metrics."""
        return {
            'total_checks': self.prom.query_sum(
                'synthetic_checks_total',
                start, end
            ),
            'successful_checks': self.prom.query_sum(
                'synthetic_checks_total{result="success"}',
                start, end
            ),
            'failed_checks': self.prom.query_sum(
                'synthetic_checks_total{result="failure"}',
                start, end
            )
        }
    
    def _get_availability(self, start, end) -> Dict:
        """Get availability metrics."""
        return {
            'overall': self.prom.query_avg(
                'synthetic_check_success_rate',
                start, end
            ),
            'by_service': self.prom.query_avg_by_label(
                'synthetic_check_success_rate',
                'service',
                start, end
            )
        }
    
    def _get_performance(self, start, end) -> Dict:
        """Get performance metrics."""
        return {
            'p50': self.prom.query_quantile(
                'http_request_duration_seconds_bucket',
                0.5, start, end
            ),
            'p95': self.prom.query_quantile(
                'http_request_duration_seconds_bucket',
                0.95, start, end
            ),
            'p99': self.prom.query_quantile(
                'http_request_duration_seconds_bucket',
                0.99, start, end
            )
        }
    
    def _get_incidents(self, start, end) -> List[Dict]:
        """Get incidents during period."""
        return self.prom.query_alerts(
            alertstate='firing',
            start=start,
            end=end
        )
    
    def _get_sla_compliance(self, start, end) -> Dict:
        """Get SLA compliance."""
        return {
            'overall': self.prom.query_avg(
                'sla_compliance_ratio',
                start, end
            ),
            'by_service': self.prom.query_avg_by_label(
                'sla_compliance_ratio',
                'service',
                start, end
            ),
            'error_budget_remaining': self.prom.query_avg(
                'error_budget_remaining_ratio',
                start, end
            )
        }
    
    def _generate_recommendations(self, start, end) -> List[str]:
        """Generate recommendations based on data."""
        recommendations = []
        
        availability = self._get_availability(start, end)
        if availability['overall'] < 0.99:
            recommendations.append(
                "Overall availability below 99% - review failing checks"
            )
        
        performance = self._get_performance(start, end)
        if performance['p99'] > 2:
            recommendations.append(
                "P99 latency above 2s - investigate slow endpoints"
            )
        
        sla = self._get_sla_compliance(start, end)
        if sla['error_budget_remaining'] < 0.5:
            recommendations.append(
                "Error budget below 50% - reduce change velocity"
            )
        
        return recommendations
```

---

## Best Practices

1. **Layer Dashboards:** Executive → Service → Detail views
2. **Use Variables:** Enable dynamic filtering by service, region, etc.
3. **Set Thresholds:** Visual indicators for normal/warning/critical
4. **Include Context:** Add annotations for deployments and incidents
5. **Automate Reports:** Generate daily/weekly reports automatically
6. **Mobile Responsive:** Ensure dashboards work on mobile devices
7. **Regular Review:** Update dashboards as systems evolve
8. **Document Dashboards:** Maintain runbooks for each dashboard

---

## Next Steps

- [Examples](../examples/README.md) - Complete examples
- [Practices](../practices/README.md) - Exercises
- [Solutions](../solutions/README.md) - Complete solutions
