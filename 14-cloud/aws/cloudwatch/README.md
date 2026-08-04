# Amazon CloudWatch

## Overview

Amazon CloudWatch is a monitoring and observability service for AWS resources and applications.

## Components

```
┌─────────────────────────────────────────────────────────┐
│                    CloudWatch                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Metrics  │  │  Logs    │  │ Alarms   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Dashboards   │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## CloudWatch Metrics

### Standard Metrics
| Metric        | Namespace     | Description               |
|---------------|---------------|---------------------------|
| CPUUtilization| AWS/EC2       | EC2 CPU usage             |
| CPUUtilization| AWS/RDS       | RDS CPU usage             |
| ReadLatency   | AWS/DynamoDB  | DynamoDB read latency     |
| Invocations   | AWS/Lambda    | Lambda invocation count   |

### Custom Metrics
```python
import boto3
cloudwatch = boto3.client('cloudwatch')

cloudwatch.put_metric_data(
    Namespace='MyApp',
    MetricData=[
        {
            'MetricName': 'RequestCount',
            'Dimensions': [
                {'Name': 'Service', 'Value': 'API'},
                {'Name': 'Environment', 'Value': 'prod'}
            ],
            'Value': 1.0,
            'Unit': 'Count'
        }
    ]
)
```

### Metric Math
```yaml
# In CloudWatch dashboard
{ REQUESTS / ERRORS } 
{ AVG(SERVICE_LATENCY) }
{ MAX(ERROR_RATE) * 100 }
```

## CloudWatch Alarms

### Alarm States
- `OK`: Metric within threshold
- `ALARM`: Metric breached threshold
- `INSUFFICIENT_DATA`: Not enough data

### Create Alarm
```bash
aws cloudwatch put-metric-alarm \
  --alarm-name my-cpu-alarm \
  --metric-name CPUUtilization \
  --namespace AWS/EC2 \
  --statistic Average \
  --period 300 \
  --evaluation-periods 2 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=InstanceId,Value=i-1234567890abcdef0 \
  --alarm-actions arn:aws:sns:us-east-1:123456789012:my-alerts \
  --ok-actions arn:aws:sns:us-east-1:123456789012:my-alerts
```

### Composite Alarms
```bash
aws cloudwatch put-metric-alarm \
  --alarm-name my-composite-alarm \
  --alarm-rule 'ALARM("alarm1") AND ALARM("alarm2")' \
  --alarm-actions arn:aws:sns:us-east-1:123456789012:my-alerts
```

### Metric Math Alarms
```bash
aws cloudwatch put-metric-alarm \
  --alarm-name error-rate-alarm \
  --metric-math-expression '{
    "expression": "errors/requests*100",
    "id": "e1"
  }' \
  --metrics '{
    "id": "errors",
    "metricStat": {
      "metric": {"metricName": "Errors", "namespace": "MyApp"},
      "period": 300,
      "stat": "Sum"
    }
  }, {
    "id": "requests",
    "metricStat": {
      "metric": {"metricName": "Requests", "namespace": "MyApp"},
      "period": 300,
      "stat": "Sum"
    }
  }' \
  --threshold 5 \
  --comparison-operator GreaterThanThreshold
```

## CloudWatch Logs

### Log Groups
```bash
# Create log group
aws logs create-log-group --log-group-name /myapp/errors

# Set retention
aws logs put-retention-policy \
  --log-group-name /myapp/errors \
  --retention-in-days 30
```

### Metric Filters
```bash
# Create metric filter
aws logs put-metric-filter \
  --log-group-name /myapp/errors \
  --filter-pattern "ERROR" \
  --metric-transformations '{
    "metricName": "ErrorCount",
    "metricNamespace": "MyApp",
    "metricValue": "1"
  }'
```

### Logs Insights
```sql
-- Query logs
fields @timestamp, @message
| filter @message like /ERROR/
| sort @timestamp desc
| limit 100

-- Aggregate errors
fields @timestamp
| filter @message like /ERROR/
| stats count(*) as errorCount by bin(1h)

-- Top error types
fields @message
| filter @message like /ERROR/
| parse @message "Error: *" as errorType
| stats count(*) as count by errorType
| sort count desc
```

### Subscription Filters
```bash
# Send logs to Lambda
aws logs put-subscription-filter \
  --log-group-name /myapp/errors \
  --filter-name my-filter \
  --filter-pattern "ERROR" \
  --destination-arn arn:aws:lambda:us-east-1:123456789012:process-logs
```

## CloudWatch Dashboards

### Create Dashboard
```bash
aws cloudwatch put-dashboard \
  --dashboard-name MyDashboard \
  --dashboard-body '{
    "widgets": [
      {
        "type": "metric",
        "properties": {
          "metrics": [
            ["AWS/EC2", "CPUUtilization", "InstanceId", "i-1234567890"]
          ],
          "period": 300,
          "stat": "Average",
          "region": "us-east-1",
          "title": "EC2 CPU"
        }
      }
    ]
  }'
```

### Widget Types
| Type      | Description                    |
|-----------|--------------------------------|
| metric    | Time series graph              |
| number    | Single number                  |
| gauge     | Gauge chart                   |
| table     | Tabular data                  |
| text      | Markdown text                 |
| log       | Log group query              |
| alarm     | Alarm status                  |

## CloudWatch Synthetics

```python
# Canary script
from aws_synthetics.selenium import synthetics_webdriver

def main():
    browser = synthetics_webdriver.Chrome()
    browser.get('https://example.com')
    
    # Take screenshot
    browser.take_screenshot('homepage')
    
    # Check for content
    assert 'Welcome' in browser.page_source
```

## CloudWatch Contributor Insights

```bash
# Enable contributor insights
aws cloudwatch put-contributor-insight-rules \
  --config '[
    {
      "RuleName": "TopErrorSources",
      "LogGroupArns": ["arn:aws:logs:us-east-1:123456789012:log-group:/myapp/errors"],
      "Field": "errorCode",
      "MaxResults": 10
    }
  ]'
```

## CloudWatch ServiceLens

```bash
# Enable X-Ray tracing
aws lambda update-function-configuration \
  --function-name my-function \
  --tracing-config Mode=Active
```

## Cost Optimization

- **Custom metrics** cost $0.30/metric/month
- **Standard resolution metrics**: Included
- **High resolution**: $0.30/metric/month
- **Logs ingestion**: $0.50/GB
- **Logs storage**: $0.03/GB/month

## Best Practices

1. **Use namespaces** for organization
2. **Set up alarms** for critical metrics
3. **Use Logs Insights** for log analysis
4. **Implement dashboards** for visibility
5. **Use metric filters** for log-based metrics
6. **Set retention policies** to manage costs
7. **Use Synthetics** for uptime monitoring
8. **Enable Contributor Insights** for anomaly detection
9. **Use CloudWatch Agent** for custom metrics
10. **Set up SNS notifications** for alarms
