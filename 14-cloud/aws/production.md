# AWS Production Guide

## Multi-AZ Deployment

### EC2 Multi-AZ

Deploy instances across multiple Availability Zones for high availability:

```python
ec2 = boto3.client('ec2')

# Create instances in multiple AZs
for az in ['us-east-1a', 'us-east-1b', 'us-east-1c']:
    ec2.run_instances(
        ImageId='ami-12345678',
        InstanceType='m5.large',
        MinCount=1,
        MaxCount=1,
        SubnetId=f'subnet-{az}',
        SecurityGroupIds=['sg-12345678']
    )
```

### RDS Multi-AZ

```python
rds = boto3.client('rds')

# Create Multi-AZ DB instance
rds.create_db_instance(
    DBInstanceIdentifier='mydb',
    DBInstanceClass='db.r5.large',
    Engine='mysql',
    MultiAZ=True,
    AllocatedStorage=100
)
```

### ELB Multi-AZ

```python
elbv2 = boto3.client('elbv2')

# Create load balancer
elbv2.create_load_balancer(
    Name='my-alb',
    Subnets=['subnet-az1', 'subnet-az2', 'subnet-az3'],
    SecurityGroups=['sg-12345678'],
    Scheme='internet-facing',
    Type='application'
)
```

## Auto Scaling

### EC2 Auto Scaling

```python
autoscaling = boto3.client('autoscaling')

# Create Auto Scaling Group
autoscaling.create_auto_scaling_group(
    AutoScalingGroupName='my-asg',
    LaunchTemplate={
        'LaunchTemplateName': 'my-template',
        'Version': '$Latest'
    },
    MinSize=2,
    MaxSize=10,
    DesiredCapacity=3,
    VPCZoneIdentifier='subnet-az1,subnet-az2',
    TargetGroupARNs=['arn:aws:elasticloadbalancing:...'],
    HealthCheckType='ELB',
    HealthCheckGracePeriod=300
)

# Create scaling policy
autoscaling.put_scaling_policy(
    AutoScalingGroupName='my-asg',
    PolicyName='scale-out',
    PolicyType='TargetTrackingScaling',
    TargetTrackingConfiguration={
        'PredefinedMetricSpecification': {
            'PredefinedMetricType': 'ASGAverageCPUUtilization'
        },
        'TargetValue': 70.0
    }
)
```

### ECS Auto Scaling

```python
appautoscaling = boto3.client('application-autoscaling')

# Register scalable target
appautoscaling.register_scalable_target(
    ServiceNamespace='ecs',
    ResourceId='service/my-cluster/my-service',
    ScalableDimension='ecs:service:DesiredCount',
    MinCapacity=2,
    MaxCapacity=10
)

# Create scaling policy
appautoscaling.put_scaling_policy(
    PolicyName='cpu-scaling',
    ServiceNamespace='ecs',
    ResourceId='service/my-cluster/my-service',
    ScalableDimension='ecs:service:DesiredCount',
    PolicyType='TargetTrackingScaling',
    TargetTrackingScalingPolicyConfiguration={
        'TargetValue': 70.0,
        'PredefinedMetricSpecification': {
            'PredefinedMetricType': 'ECSServiceAverageCPUUtilization'
        }
    }
)
```

## Backup Strategy

### AWS Backup

```python
backup = boto3.client('backup')

# Create backup plan
backup.create_backup_plan(
    BackupPlan={
        'BackupPlanName': 'my-backup-plan',
        'Rules': [
            {
                'RuleName': 'daily-backup',
                'TargetBackupVaultName': 'my-vault',
                'ScheduleExpression': 'cron(0 12 * * ? *)',
                'StartWindowMinutes': 60,
                'CompletionWindowMinutes': 180,
                'Lifecycle': {
                    'MoveToColdStorageAfterDays': 30,
                    'DeleteAfterDays': 365
                }
            }
        ]
    }
)

# Assign resources
backup.create_backup_selection(
    BackupPlanId='my-backup-plan',
    BackupSelection={
        'SelectionName': 'my-resources',
        'IamRoleArn': 'arn:aws:iam::123456789012:role/backup-role',
        'Resources': [
            'arn:aws:rds:us-east-1:123456789012:db:mydb',
            'arn:aws:ec2:us-east-1:123456789012:volume/vol-12345678'
        ]
    }
)
```

### S3 Cross-Region Replication

```python
s3 = boto3.client('s3')

# Enable versioning
s3.put_bucket_versioning(
    Bucket='my-source-bucket',
    VersioningConfiguration={'Status': 'Enabled'}
)

# Create replication rule
s3.put_bucket_replication(
    Bucket='my-source-bucket',
    ReplicationConfiguration={
        'Role': 'arn:aws:iam::123456789012:role/replication-role',
        'Rules': [
            {
                'ID': 'replicate-all',
                'Status': 'Enabled',
                'Destination': {
                    'Bucket': 'arn:aws:s3:::my-destination-bucket',
                    'StorageClass': 'STANDARD'
                }
            }
        ]
    }
)
```

## Disaster Recovery

### RPO and RTO

- **RPO (Recovery Point Objective)**: Maximum acceptable data loss
- **RTO (Recovery Time Objective)**: Maximum acceptable downtime

### DR Strategies

| Strategy | RPO | RTO | Cost |
|----------|-----|-----|------|
| Backup & Restore | Hours | Hours | Low |
| Pilot Light | Minutes | 10s of minutes | Medium |
| Warm Standby | Seconds | Minutes | High |
| Multi-Site Active/Active | Zero | Zero | Very High |

### Route 53 Failover

```python
route53 = boto3.client('route53')

# Create failover record
route53.change_resource_record_sets(
    HostedZoneId='Z1234567890',
    ChangeBatch={
        'Changes': [
            {
                'Action': 'CREATE',
                'ResourceRecordSet': {
                    'Name': 'myapp.example.com',
                    'Type': 'A',
                    'SetIdentifier': 'primary',
                    'Failover': 'PRIMARY',
                    'TTL': 60,
                    'ResourceRecords': [{'Value': '1.2.3.4'}],
                    'HealthCheckId': 'health-check-id'
                }
            }
        ]
    }
)
```

## Production Checklist

- [ ] Multi-AZ deployment for all critical resources
- [ ] Auto Scaling configured
- [ ] Backup and restore tested
- [ ] DR strategy documented and tested
- [ ] Monitoring and alerting configured
- [ ] Security group rules reviewed
- [ ] IAM policies audited
- [ ] Encryption enabled for data at rest and in transit
- [ ] VPC flow logs enabled
- [ ] CloudTrail logging enabled
- [ ] Cost budgets set
- [ ] Tagging strategy implemented
