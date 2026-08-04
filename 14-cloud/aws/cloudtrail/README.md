# AWS CloudTrail

## Overview

AWS CloudTrail is a service that enables governance, compliance, operational auditing, and risk auditing of your AWS account.

## Features

- **API logging**: Records all API calls
- **Event history**: 90-day event history
- **Log file validation**: Integrity checking
- **Multi-region**: Cross-region logging
- **Integration**: S3, CloudWatch, EventBridge

## Creating Trails

```bash
# Create trail
aws cloudtrail create-trail \
  --name my-trail \
  --s3-bucket-name my-cloudtrail-bucket \
  --is-multi-region-trail \
  --enable-log-file-validation

# Start logging
aws cloudtrail start-logging --name my-trail
```

## Log File Contents

### API Call Information
```
- User identity
- Event time
- Source IP
- Request parameters
- Response elements
- AWS services called
```

### Event Structure
```json
{
  "eventVersion": "1.0",
  "userIdentity": {
    "type": "IAMUser",
    "principalId": "AIDA1234567890",
    "arn": "arn:aws:iam::123456789012:user/MyUser",
    "accountId": "123456789012",
    "userName": "MyUser"
  },
  "eventTime": "2024-01-15T10:30:00Z",
  "eventSource": "ec2.amazonaws.com",
  "eventName": "RunInstances",
  "awsRegion": "us-east-1",
  "sourceIPAddress": "203.0.113.0",
  "requestParameters": {
    "instanceType": "t3.micro",
    "imageId": "ami-12345678"
  }
}
```

## CloudWatch Logs Integration

```bash
# Create CloudWatch log group
aws logs create-log-group --log-group-name /aws/cloudtrail/my-trail

# Create IAM role for CloudTrail
aws iam create-role \
  --role-name CloudTrailRole \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [{
      "Effect": "Allow",
      "Principal": {"Service": "cloudtrail.amazonaws.com"},
      "Action": "sts:AssumeRole"
    }]
  }'

# Update trail to send to CloudWatch
aws cloudtrail update-trail \
  --name my-trail \
  --cloud-watch-logs-log-group-arn arn:aws:logs:us-east-1:123456789012:log-group:/aws/cloudtrail/my-trail:* \
  --cloud-watch-logs-role-arn arn:aws:iam::123456789012:role/CloudTrailRole
```

## EventBridge Integration

```bash
# Create rule for CloudTrail events
aws events put-rule \
  --name ec2-instance-events \
  --event-pattern '{
    "source": ["aws.ec2"],
    "detail-type": ["AWS API Call via CloudTrail"],
    "detail": {
      "eventSource": ["ec2.amazonaws.com"],
      "eventName": ["RunInstances", "TerminateInstances"]
    }
  }'
```

## Log File Validation

```bash
# Validate log files
aws cloudtrail validate-logs \
  --trail-arn arn:aws:cloudtrail:us-east-1:123456789012:trail/my-trail \
  --start-time 2024-01-15T00:00:00Z
```

## S3 Bucket Configuration

### Bucket Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AWSCloudTrailAclCheck",
      "Effect": "Allow",
      "Principal": {"Service": "cloudtrail.amazonaws.com"},
      "Action": "s3:GetBucketAcl",
      "Resource": "arn:aws:s3:::my-cloudtrail-bucket"
    },
    {
      "Sid": "AWSCloudTrailWrite",
      "Effect": "Allow",
      "Principal": {"Service": "cloudtrail.amazonaws.com"},
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::my-cloudtrail-bucket/*",
      "Condition": {
        "StringEquals": {"s3:x-amz-acl": "bucket-owner-full-control"}
      }
    }
  ]
}
```

## Insights

```bash
# Create insights rule
aws cloudtrail put-insight-rules \
  --insight-rules '[
    {
      "Name": "ApiCallRateInsight",
      "IsEnabled": true,
      "InsightRuleCriteria": {
        "InsightRuleCriteria": {
          "InsightRuleMetric": {
            "LogEventValue": "eventName",
            "ComparisonOperator": "GT",
            "Threshold": 1000,
            "Period": 300
          }
        }
      }
    }
  ]'
```

## Multi-Region Trail

```bash
# Create multi-region trail
aws cloudtrail create-trail \
  --name my-multi-region-trail \
  --s3-bucket-name my-cloudtrail-bucket \
  --is-multi-region-trail \
  --include-global-service-events
```

## Organization Trail

```bash
# Create organization trail
aws cloudtrail create-trail \
  --name my-org-trail \
  --s3-bucket-name my-cloudtrail-bucket \
  --is-organization-trail \
  --is-multi-region-trail
```

## Event Selectors

```bash
# Enable data events for S3
aws cloudtrail put-event-selectors \
  --trail-name my-trail \
  --event-selectors '[
    {
      "ReadWriteType": "All",
      "IncludeManagementEvents": true,
      "DataResources": [
        {
          "Type": "AWS::S3::Object",
          "Values": ["arn:aws:s3:::my-bucket/"]
        }
      ]
    }
  ]'
```

## CloudTrail Lake

```bash
# Create event data store
aws cloudtrail create-event-data-store \
  --name my-event-store \
  --retention-period 365 \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-12-31T23:59:59Z

# Query events
aws cloudtrail lookup-events \
  --event-name RunInstances \
  --start-time 2024-01-15T00:00:00Z \
  --end-time 2024-01-15T23:59:59Z
```

## Compliance

### PCI DSS
- Enable log file validation
- Send logs to S3
- Enable CloudWatch logs

### HIPAA
- Enable encryption
- Set appropriate retention
- Enable logging

### SOC 2
- Enable multi-region trail
- Monitor API calls
- Implement alerts

## Best Practices

1. **Enable multi-region trail** for full coverage
2. **Enable log file validation** for integrity
3. **Send logs to S3** for long-term storage
4. **Use CloudWatch Logs** for real-time monitoring
5. **Enable data events** for S3 if needed
6. **Set up alerts** for critical events
7. **Implement retention policies**
8. **Use CloudTrail Lake** for advanced queries
9. **Enable organization trail** for multi-account
10. **Regular review** of event logs
