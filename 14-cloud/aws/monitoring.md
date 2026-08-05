# AWS Monitoring

## CloudWatch

Monitoring and observability service for AWS resources and applications.

### Metrics

Collect and track metrics from AWS services and custom applications:

```python
import boto3

cloudwatch = boto3.client('cloudwatch')

# Put custom metric
cloudwatch.put_metric_data(
    Namespace='MyApp',
    MetricData=[
        {
            'MetricName': 'RequestCount',
            'Value': 100,
            'Unit': 'Count'
        }
    ]
)

# Get metrics
response = cloudwatch.get_metric_statistics(
    Namespace='AWS/EC2',
    MetricName='CPUUtilization',
    StartTime=datetime.utcnow() - timedelta(hours=1),
    EndTime=datetime.utcnow(),
    Period=300,
    Statistics=['Average', 'Maximum']
)
```

### Alarms

```python
cloudwatch.put_metric_alarm(
    AlarmName='HighCPU',
    MetricName='CPUUtilization',
    Namespace='AWS/EC2',
    Statistic='Average',
    Period=300,
    EvaluationPeriods=2,
    Threshold=80.0,
    ComparisonOperator='GreaterThanThreshold',
    Dimensions=[
        {'Name': 'InstanceId', 'Value': 'i-1234567890abcdef0'}
    ],
    AlarmActions=['arn:aws:sns:us-east-1:123456789012:my-topic']
)
```

### CloudWatch Logs

```python
logs = boto3.client('logs')

# Create log group
logs.create_log_group(logGroupName='/myapp/errors')

# Put log events
logs.put_log_events(
    logGroupName='/myapp/errors',
    logStreamName='my-stream',
    logEvents=[
        {
            'timestamp': int(time.time() * 1000),
            'message': 'Error occurred'
        }
    ]
)

# Query logs
response = logs.start_query(
    logGroupName='/myapp/errors',
    startTime=int((datetime.now() - timedelta(hours=1)).timestamp()),
    endTime=int(datetime.now().timestamp()),
    queryString='fields @timestamp, @message | sort @timestamp desc'
)
```

### Dashboards

```python
cloudwatch.put_dashboard(
    DashboardName='MyAppDashboard',
    DashboardBody=json.dumps({
        'widgets': [
            {
                'type': 'metric',
                'properties': {
                    'metrics': [
                        ['AWS/EC2', 'CPUUtilization', {'stat': 'Average'}]
                    ],
                    'period': 300,
                    'title': 'EC2 CPU Usage'
                }
            }
        ]
    })
)
```

## X-Ray

Distributed tracing system for debugging and analyzing applications.

### Features

- End-to-end request tracing
- Service maps
- Latency analysis
- Error and fault analysis
- Integration with CloudWatch

### Usage

```python
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch_all

# Patch supported libraries
patch_all()

@xray_recorder.capture('my_function')
def handler(event, context):
    # Your code here
    xray_recorder.current_subsegment().put_annotation('key', 'value')
    return {'statusCode': 200}
```

## CloudTrail

Tracks user activity and API usage across AWS services.

### Features

- API call logging
- CloudTrail Lake for analytics
- Multi-region trails
- Organization trails
- Insights for anomalous activity

### Setup

```bash
# Create trail
aws cloudtrail create-trail \
  --name my-trail \
  --s3-bucket-name my-cloudtrail-bucket \
  --is-multi-region-trail

# Start logging
aws cloudtrail start-logging --name my-trail
```

### Query with CloudTrail Lake

```python
cloudtrail = boto3.client('cloudtrail-data')

# Create event data store
cloudtrail.create_event_data_store(
    Name='my-event-store',
    MultiRegionEnabled=True,
    OrganizationEnabled=True,
    RetentionPeriod=365
)

# Query events
response = cloudtrail.start_query(
    EventDataStore='my-event-store',
    QueryStatement='SELECT eventTime, eventName FROM my-event-store WHERE eventTime > timestamp \'2024-01-01 00:00:00\''
)
```

## AWS Config

Records configurations of AWS resources and evaluates configuration compliance.

### Features

- Configuration history
- Configuration snapshots
- Compliance evaluation
- Remediation with SSM Automation

### Usage

```python
config = boto3.client('config')

# Put config rule
config.put_config_rule(
    ConfigRule={
        'ConfigRuleName': 's3-bucket-public-read-prohibited',
        'Source': {
            'Owner': 'AWS',
            'SourceIdentifier': 'S3_BUCKET_PUBLIC_READ_PROHIBITED'
        }
    }
)
```

## CloudWatch Events / EventBridge

Serverless event bus for application integration.

### Usage

```python
events = boto3.client('events')

# Put rule
events.put_rule(
    Name='my-schedule',
    ScheduleExpression='rate(5 minutes)',
    State='ENABLED'
)

# Put target
events.put_targets(
    Rule='my-schedule',
    Targets=[
        {
            'Id': 'my-lambda',
            'Arn': 'arn:aws:lambda:us-east-1:123456789012:function:my-function'
        }
    ]
)
```

## Monitoring Best Practices

1. Enable CloudWatch for all resources
2. Set up alarms for critical metrics
3. Use X-Ray for distributed tracing
4. Enable CloudTrail for auditing
5. Create dashboards for visibility
6. Use AWS Config for compliance
7. Set up EventBridge for event-driven monitoring
8. Monitor costs with Cost Explorer
