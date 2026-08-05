# AWS Scaling

## Auto Scaling Groups (ASG)

Automatically adjusts EC2 instance count based on demand.

### Configuration

```python
autoscaling = boto3.client('autoscaling')

# Create ASG
autoscaling.create_auto_scaling_group(
    AutoScalingGroupName='my-asg',
    LaunchTemplate={
        'LaunchTemplateName': 'my-template',
        'Version': '$Latest'
    },
    MinSize=2,
    MaxSize=20,
    DesiredCapacity=3,
    VPCZoneIdentifier='subnet-az1,subnet-az2',
    HealthCheckType='ELB',
    HealthCheckGracePeriod=300,
    TargetGroupARNs=['arn:aws:elasticloadbalancing:...']
)
```

### Scaling Policies

**Target Tracking**

```python
autoscaling.put_scaling_policy(
    AutoScalingGroupName='my-asg',
    PolicyName='cpu-tracking',
    PolicyType='TargetTrackingScaling',
    TargetTrackingConfiguration={
        'PredefinedMetricSpecification': {
            'PredefinedMetricType': 'ASGAverageCPUUtilization'
        },
        'TargetValue': 70.0
    }
)
```

**Step Scaling**

```python
autoscaling.put_scaling_policy(
    AutoScalingGroupName='my-asg',
    PolicyName='step-scaling',
    PolicyType='StepScaling',
    StepAdjustments=[
        {'MetricIntervalLowerBound': 0, 'MetricIntervalUpperBound': 20, 'ScalingAdjustment': 1},
        {'MetricIntervalLowerBound': 20, 'ScalingAdjustment': 3}
    ]
)
```

**Scheduled Scaling**

```python
autoscaling.put_scheduled_update_group_action(
    AutoScalingGroupName='my-asg',
    ScheduledActionName='scale-up-morning',
    Recurrence='0 8 * * MON-FRI',
    MinSize=5,
    MaxSize=20,
    DesiredCapacity=10
)
```

## Elastic Load Balancing

### Application Load Balancer (ALB)

HTTP/HTTPS load balancing with advanced routing:

```python
elbv2 = boto3.client('elbv2')

# Create ALB
response = elbv2.create_load_balancer(
    Name='my-alb',
    Subnets=['subnet-az1', 'subnet-az2'],
    SecurityGroups=['sg-12345678'],
    Scheme='internet-facing',
    Type='application'
)

# Create target group
elbv2.create_target_group(
    Name='my-targets',
    Protocol='HTTP',
    Port=80,
    VpcId='vpc-12345678',
    HealthCheckProtocol='HTTP',
    HealthCheckPath='/health',
    HealthCheckIntervalSeconds=30,
    HealthyThresholdCount=3,
    UnhealthyThresholdCount=3
)

# Create listener
elbv2.create_listener(
    LoadBalancerArn='arn:aws:elasticloadbalancing:...',
    Protocol='HTTPS',
    Port=443,
    Certificates=[{'CertificateArn': 'arn:aws:acm:...'}],
    DefaultActions=[{'Type': 'forward', 'TargetGroupArn': 'arn:aws:elasticloadbalancing:...'}]
)
```

### Network Load Balancer (NLB)

Ultra-low latency, high throughput load balancing:

```python
elbv2.create_load_balancer(
    Name='my-nlb',
    Subnets=['subnet-az1', 'subnet-az2'],
    Type='network',
    Scheme='internet-facing'
)
```

## Lambda Concurrency

### Reserved Concurrency

```python
lambda_client = boto3.client('lambda')

# Set reserved concurrency
lambda_client.put_function_concurrency(
    FunctionName='my-function',
    ReservedConcurrentExecutions=100
)
```

### Provisioned Concurrency

```python
# Publish version
lambda_client.publish_version(FunctionName='my-function')

# Set provisioned concurrency
lambda_client.put_provisioned_concurrency_config(
    FunctionName='my-function',
    Qualifier='1',
    ProvisionedConcurrentExecutions=10
)
```

### Application Auto Scaling for Lambda

```python
appautoscaling = boto3.client('application-autoscaling')

# Register scalable target
appautoscaling.register_scalable_target(
    ServiceNamespace='lambda',
    ResourceId='function:my-function',
    ScalableDimension='lambda:function:ProvisionedConcurrency',
    MinCapacity=5,
    MaxCapacity=100
)

# Create scaling policy
appautoscaling.put_scaling_policy(
    PolicyName='lambda-scaling',
    ServiceNamespace='lambda',
    ResourceId='function:my-function',
    ScalableDimension='lambda:function:ProvisionedConcurrency',
    PolicyType='TargetTrackingScaling',
    TargetTrackingScalingPolicyConfiguration={
        'TargetValue': 0.7,
        'PredefinedMetricSpecification': {
            'PredefinedMetricType': 'LambdaProvisionedConcurrencyUtilization'
        }
    }
)
```

## DynamoDB Auto Scaling

```python
appautoscaling = boto3.client('application-autoscaling')

# Register read capacity
appautoscaling.register_scalable_target(
    ServiceNamespace='dynamodb',
    ResourceId='table/my-table',
    ScalableDimension='dynamodb:table:ReadCapacityUnits',
    MinCapacity=5,
    MaxCapacity=1000
)

# Create scaling policy
appautoscaling.put_scaling_policy(
    PolicyName='read-scaling',
    ServiceNamespace='dynamodb',
    ResourceId='table/my-table',
    ScalableDimension='dynamodb:table:ReadCapacityUnits',
    PolicyType='TargetTrackingScaling',
    TargetTrackingScalingPolicyConfiguration={
        'TargetValue': 70.0,
        'PredefinedMetricSpecification': {
            'PredefinedMetricType': 'DynamoDBReadCapacityUtilization'
        }
    }
)
```

## ECS Auto Scaling

```python
appautoscaling = boto3.client('application-autoscaling')

# Register scalable target
appautoscaling.register_scalable_target(
    ServiceNamespace='ecs',
    ResourceId='service/my-cluster/my-service',
    ScalableDimension='ecs:service:DesiredCount',
    MinCapacity=2,
    MaxCapacity=20
)

# Create scaling policy
appautoscaling.put_scaling_policy(
    PolicyName='ecs-scaling',
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

## Scaling Best Practices

1. Set appropriate min/max capacity
2. Use target tracking for most use cases
3. Configure health checks for accurate scaling
4. Use scheduled scaling for predictable patterns
5. Monitor scaling activities
6. Test scaling under load
7. Consider warm-up time for applications
8. Use lifecycle hooks for graceful shutdown
