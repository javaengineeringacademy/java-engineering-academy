# AWS Performance Optimization

## EC2 Instance Types

### General Purpose (M-Series)

Balanced compute, memory, and networking. Ideal for web servers, app servers, and development environments.

| Instance | vCPU | Memory | Network | Use Case |
|----------|------|--------|---------|----------|
| m5.large | 2 | 8 GiB | Up to 10 Gbps | Web servers |
| m5.xlarge | 4 | 16 GiB | Up to 10 Gbps | Application servers |
| m5.2xlarge | 8 | 32 GiB | Up to 10 Gbps | Medium databases |

### Compute Optimized (C-Series)

High-performance processors for compute-intensive workloads. Batch processing, ML inference, gaming.

| Instance | vCPU | Memory | Use Case |
|----------|------|--------|----------|
| c5.large | 2 | 4 GiB | Batch processing |
| c5.xlarge | 4 | 8 GiB | ML inference |
| c5.2xlarge | 8 | 16 GiB | High-performance computing |

### Memory Optimized (R-Series)

High memory-to-CPU ratio for memory-intensive workloads. Databases, in-memory caching, real-time analytics.

| Instance | vCPU | Memory | Use Case |
|----------|------|--------|----------|
| r5.large | 2 | 16 GiB | In-memory databases |
| r5.xlarge | 4 | 32 GiB | Relational databases |
| r5.2xlarge | 8 | 64 GiB | SAP, Oracle |

## S3 Performance

### Throughput Optimization

- Use multipart upload for objects > 100 MB
- Transfer acceleration for cross-region uploads
- S3 Select for querying data without downloading

### Multipart Upload

```python
import boto3
from boto3.s3.transfer import TransferConfig

s3 = boto3.client('s3')

config = TransferConfig(
    multipart_threshold=100 * 1024 * 1024,  # 100MB
    multipart_chunksize=100 * 1024 * 1024,
    max_concurrency=10
)

s3.upload_file(
    'large-file.zip',
    'my-bucket',
    'large-file.zip',
    Config=config
)
```

### S3 Transfer Acceleration

```python
s3 = boto3.client('s3', config=boto3.session.Config(
    s3={'use_accelerate_endpoint': True}
))
```

## RDS Performance

### Instance Sizing

- Start with db.t3.micro for development
- Use db.r5.large+ for production workloads
- Enable Read Replicas for read-heavy workloads

### Parameter Tuning

```sql
-- MySQL
innodb_buffer_pool_size = 70% of available memory
innodb_log_file_size = 256MB
max_connections = 200

-- PostgreSQL
shared_buffers = 25% of memory
effective_cache_size = 75% of memory
work_mem = 4MB
```

### Performance Insights

Enable Performance Insights for query-level monitoring:

```bash
aws rds modify-db-instance \
  --db-instance-identifier mydb \
  --enable-performance-insights \
  --performance-insights-retention-period 7
```

## Lambda Performance

### Cold Start Optimization

- Use Provisioned Concurrency for critical functions
- Minimize deployment package size
- Use Lambda Layers for shared dependencies
- Choose lightweight runtimes (Node.js, Python)

### Memory and CPU

Lambda allocates CPU proportional to memory:

| Memory | vCPU | Network |
|--------|------|---------|
| 128 MB | ~0.08 | 128 Mbps |
| 512 MB | ~0.33 | 256 Mbps |
| 1024 MB | ~0.58 | 1 Gbps |
| 1769 MB | 1 vCPU | 1 Gbps |

### Provisioned Concurrency

```python
import boto3
lambda_client = boto3.client('lambda')

# Put provisioned concurrency
lambda_client.put_provisioned_concurrency_config(
    FunctionName='my-function',
    Qualifier='prod',
    ProvisionedConcurrentExecutions=10
)
```

## ElastiCache Performance

### Redis vs Memcached

| Feature | Redis | Memcached |
|---------|-------|-----------|
| Data structures | Lists, sets, hashes | Strings only |
| Persistence | Yes | No |
| Replication | Yes | No |
| Clustering | Yes | Yes |

### Node Type Selection

- **cache.t3.micro**: Development/testing
- **cache.r5.large**: Production caching
- **cache.r5.xlarge**: High-throughput caching

## CloudFront Performance

### Optimization

- Enable compression for text-based content
- Use Origin Shield for origin protection
- Configure appropriate TTLs
- Use Lambda@Edge for dynamic content

### Cache Behaviors

```python
cloudfront = boto3.client('cloudfront')

cloudfront.create_distribution(
    DistributionConfig={
        'DefaultCacheBehavior': {
            'ViewerProtocolPolicy': 'redirect-to-https',
            'Compress': True,
            'CachePolicyId': '658327ea-f89d-4fab-a63d-7e88639e58f6'  # CachingOptimized
        }
    }
)
```

## Auto Scaling Optimization

### EC2 Auto Scaling

```python
autoscaling = boto3.client('autoscaling')

# Create launch template
autoscaling.create_launch_template(
    LaunchTemplateName='my-template',
    LaunchTemplateData={
        'InstanceType': 'm5.large',
        'ImageId': 'ami-12345678'
    }
)

# Create auto scaling group
autoscaling.create_auto_scaling_group(
    AutoScalingGroupName='my-asg',
    LaunchTemplate={'LaunchTemplateName': 'my-template', '$Latest': True},
    MinSize=2,
    MaxSize=10,
    TargetGroupARNs=['arn:aws:elasticloadbalancing:...'],
    HealthCheckType='ELB',
    HealthCheckGracePeriod=300
)
```

## Performance Monitoring

### CloudWatch Metrics

- CPUUtilization (EC2)
- DatabaseConnections (RDS)
- Invocations, Duration (Lambda)
- CacheHitRate (ElastiCache)
- Requests (CloudFront)

### X-Ray Tracing

Enable X-Ray for distributed tracing:

```python
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch_all

patch_all()

@xray_recorder.capture('my_function')
def handler(event, context):
    # Your code here
    pass
```

## Cost-Performance Balance

- Use Reserved Instances for predictable workloads
- Use Spot Instances for fault-tolerant workloads
- Right-size instances with Compute Optimizer
- Use Savings Plans for flexible commitments
- Monitor with Cost Explorer and set budgets
