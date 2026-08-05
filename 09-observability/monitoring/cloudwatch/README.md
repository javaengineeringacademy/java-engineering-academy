# Amazon CloudWatch

## Overview

Amazon CloudWatch is a monitoring and observability service for AWS cloud resources and applications. It provides metrics, logs, alarms, and dashboards for visibility into system performance and operational health.

## Core Concepts

### Metrics
Numerical time-series data points published by AWS services and custom applications.

### Alarms
Watch a single metric over a time period and perform actions based on value thresholds.

### Logs
Raw data from AWS services and applications, stored and searchable.

### Dashboards
Customizable views of metrics and alarms for operational monitoring.

## Architecture

```
AWS Services -> CloudWatch Agent -> CloudWatch Service -> Dashboards/Alarms
                    |
              Metrics/Logs
```

### Agent Types
- **CloudWatch Agent** - Host-level metrics and log collection
- **EMF Agent** - Embedded metric format for Lambda
- **Synthetics Canary** - Endpoint monitoring

## Configuration

### CloudWatch Agent Configuration
```json
{
  "metrics": {
    "namespace": "MyApp",
    "metrics_collected": {
      "cpu": {
        "measurement": ["cpu_usage_idle", "cpu_usage_iowait"],
        "metrics_collection_interval": 60
      },
      "disk": {
        "measurement": ["used_percent"],
        "metrics_collection_interval": 60,
        "resources": ["*"]
      },
      "mem": {
        "measurement": ["mem_used_percent"],
        "metrics_collection_interval": 60
      }
    }
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/var/log/app/error.log",
            "log_group_name": "/var/log/app/error.log",
            "timestamp_format": "%Y-%m-%d %H:%M:%S"
          }
        ]
      }
    }
  }
}
```

### Custom Metrics
```java
// Using AWS SDK
CloudWatchClient client = CloudWatchClient.builder().build();

MetricDatum datum = MetricDatum.builder()
    .metricName("OrderCount")
    .dimensions(Dimension.builder()
        .name("Service")
        .value("order-service")
        .build())
    .value(1.0)
    .unit(StandardUnit.COUNT)
    .build();

PutMetricDataRequest request = PutMetricDataRequest.builder()
    .namespace("MyApp")
    .metricData(datum)
    .build();

client.putMetricData(request);
```

## Key Features

### Metrics
- **Standard Resolution** - 1-minute granularity
- **High Resolution** - 1-second granularity
- **Custom Metrics** - Application-specific measurements
- **Metric Math** - Calculated metrics from expressions

### Alarms
- **Static Thresholds** - Fixed value boundaries
- **Anomaly Detection** - ML-based unusual pattern detection
- **Composite Alarms** - Combine multiple alarms

### Logs
- **Log Groups** - Logical grouping of log streams
- **Log Streams** - Sequence of log events
- **Metric Filters** - Extract metrics from log data
- **Logs Insights** - Interactive log analytics

## Alarm Configuration

### Static Threshold Alarm
```json
{
  "AlarmName": "HighCPU",
  "MetricName": "CPUUtilization",
  "Namespace": "AWS/EC2",
  "Statistic": "Average",
  "Period": 300,
  "EvaluationPeriods": 2,
  "Threshold": 80.0,
  "ComparisonOperator": "GreaterThanThreshold"
}
```

### Metric Math Alarm
```json
{
  "AlarmName": "HighErrorRate",
  "Metrics": [
    {
      "Id": "errors",
      "MetricStat": {
        "Metric": {
          "Namespace": "MyApp",
          "MetricName": "Errors"
        },
        "Period": 300,
        "Stat": "Sum"
      }
    },
    {
      "Id": "total",
      "MetricStat": {
        "Metric": {
          "Namespace": "MyApp",
          "MetricName": "Requests"
        },
        "Period": 300,
        "Stat": "Sum"
      }
    },
    {
      "Id": "error_rate",
      "Expression": "errors / total * 100"
    }
  ],
  "Threshold": 5.0,
  "ComparisonOperator": "GreaterThanThreshold"
}
```

## Best Practices

1. Use metric dimensions for filtering and aggregation
2. Set up log metric filters for error monitoring
3. Create dashboards for different operational roles
4. Use anomaly detection for baseline-based alerting
5. Implement composite alarms for complex conditions
6. Use CloudWatch Synthetics for endpoint monitoring
7. Set appropriate retention periods for cost optimization
8. Monitor CloudWatch itself with health metrics
