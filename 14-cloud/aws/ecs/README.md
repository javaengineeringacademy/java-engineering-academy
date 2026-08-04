# Amazon ECS (Elastic Container Service)

## Overview

Amazon ECS is a container orchestration service supporting Docker containers with two launch types: EC2 and Fargate.

## Launch Types

| Feature         | EC2                              | Fargate                    |
|-----------------|----------------------------------|----------------------------|
| Infrastructure  | You manage                       | AWS manages                |
| Scaling         | Auto Scaling Groups              | Per-task scaling           |
| Cost            | EC2 instance costs               | Per vCPU/hour              |
| Networking      | ENI on instance                  | ENI per task               |
| Storage         | Instance/EBS                     | Ephemeral                  |
| Use Case        | Large workloads, custom AMIs     | Variable workloads         |

## Task Definitions

```json
{
  "family": "my-task",
  "networkMode": "awsvpc",
  "executionRoleArn": "arn:aws:iam::123456789012:role/ecsExecutionRole",
  "taskRoleArn": "arn:aws:iam::123456789012:role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "app",
      "image": "123456789012.dkr.ecr.us-east-1.amazonaws.com/my-app:latest",
      "portMappings": [
        {
          "containerPort": 80,
          "hostPort": 80,
          "protocol": "tcp"
        }
      ],
      "essential": true,
      "memory": 512,
      "cpu": 256,
      "healthCheck": {
        "command": ["CMD-SHELL", "curl -f http://localhost/ || exit 1"],
        "interval": 30,
        "timeout": 5,
        "retries": 3
      },
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/my-task",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

## Services

### Service Types
| Type    | Description                    | Use Case              |
|---------|--------------------------------|-----------------------|
| REPLICA | Maintains desired count        | Web servers           |
| DAEMON  | One per instance/node          | Monitoring agents     |

### Service Configuration
```bash
# Create service
aws ecs create-service \
  --cluster my-cluster \
  --service-name my-service \
  --task-definition my-task:1 \
  --desired-count 3 \
  --launch-type FARGATE \
  --network-configuration '{
    "awsvpcConfiguration": {
      "subnets": ["subnet-12345678"],
      "securityGroups": ["sg-12345678"],
      "assignPublicIp": "ENABLED"
    }
  }'
```

## Clusters

```bash
# Create cluster
aws ecs create-cluster \
  --cluster-name my-cluster \
  --capacity-providers FARGATE FARGATE_SPOT \
  --default-capacity-provider-strategy '[
    {
      "capacityProvider": "FARGATE",
      "weight": 1,
      "base": 1
    },
    {
      "capacityProvider": "FARGATE_SPOT",
      "weight": 3
    }
  ]'
```

## ECS on EC2

### Auto Scaling
```bash
# Create capacity provider
aws ecs create-capacity-provider \
  --name my-capacity-provider \
  --auto-scaling-group-provider '{
    "autoScalingGroupArn": "arn:aws:autoscaling:us-east-1:123456789012:autoScalingGroup:my-asg",
    "managedScaling": {
      "status": "ENABLED",
      "targetCapacity": 80
    }
  }'
```

### AMI Types
- **Amazon ECS-optimized**: Pre-configured for ECS
- **Bottlerocket**: Purpose-built for containers
- **Custom AMI**: Your own configuration

## Fargate

### Fargate Configuration
| vCPU | Memory Options         | Use Case              |
|------|-------------------------|-----------------------|
| 0.25 | 0.5, 1 GB              | Microservices         |
| 0.5  | 1, 2 GB                | Small applications    |
| 1    | 2, 4, 8 GB            | Standard workloads    |
| 2    | 4, 8, 16 GB           | Medium workloads      |
| 4    | 8, 16, 30 GB          | Large workloads       |
| 8    | 16, 32, 60 GB         | Heavy workloads       |
| 16   | 32, 64, 120 GB        | Extra large           |

### Fargate Spot
```bash
# Use Fargate Spot for cost savings
--capacity-provider-strategy '[
  {
    "capacityProvider": "FARGATE_SPOT",
    "weight": 1
  }
]'
```

## Service Discovery

```bash
# Create private DNS namespace
aws servicediscovery create-private-dns-namespace \
  --name my.local \
  --vpc vpc-12345678

# Create service
aws servicediscovery create-service \
  --name my-service \
  --namespace-id ns-12345678 \
  --dns-config '{
    "DnsRecords": [{"Type": "A", "TTL": 10}]
  }'
```

## Load Balancing

### ALB Configuration
```bash
# Create target group
aws elbv2 create-target-group \
  --name my-targets \
  --protocol HTTP \
  --port 80 \
  --target-type ip \
  --vpc-id vpc-12345678

# Register target
aws elbv2 register-targets \
  --target-group-arn arn:aws:elasticloadbalancing:us-east-1:123456789012:targetgroup/my-targets/50dc6c495c0c9188 \
  --targets Id=10.0.1.5
```

## Task Placement

### Placement Strategies
| Strategy  | Description                          | Use Case           |
|-----------|--------------------------------------|--------------------|
| spread    | Distribute across AZs                | High availability  |
| binpack   | Pack tasks on instances              | Cost optimization  |
| random    | Random placement                     | Simple scenarios   |

### Constraints
```bash
# Instance attribute constraint
aws ecs create-service \
  --placement-constraints '[
    {
      "type": "memberOf",
      "expression": "attribute:ecs.instance-type == t3.large"
    }
  ]'
```

## Exec Command

```bash
# Execute command in running container
aws ecs execute-command \
  --cluster my-cluster \
  --task abc123def456 \
  --container app \
  --interactive \
  --command "/bin/sh"
```

## Service Connect

```bash
# Enable Service Connect
aws ecs create-service \
  --service-connect-configuration '{
    "enabled": true,
    "namespace": "my.local",
    "services": [
      {
        "portName": "http",
        "discoveryName": "my-service",
        "clientAliases": [
          {"port": 80, "dnsName": "my-service"}
        ]
      }
    ]
  }'
```

## Capacity Providers

### Managed Scaling
```bash
# Configure managed scaling
aws ecs put-cluster-capacity-providers \
  --cluster my-cluster \
  --capacity-providers FARGATE FARGATE_SPOT \
  --default-capacity-provider-strategy '[
    {
      "capacityProvider": "FARGATE",
      "weight": 1,
      "base": 1
    }
  ]'
```

## Cost Optimization

### Fargate Spot
- Up to 70% discount
- Use for fault-tolerant workloads
- Can be interrupted with 2-minute notice

### Right-Sizing
```bash
# Get task definition recommendations
aws ce get-rightsizing-recommendation \
  --service "Amazon ECS"
```

### Best Practices
1. **Use Fargate Spot** for non-critical workloads
2. **Right-size tasks** based on actual usage
3. **Use capacity providers** for cost management
4. **Implement auto-scaling** to match demand
5. **Clean up unused resources**

## Monitoring

```bash
# Get service metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/ECS \
  --metric-name CPUUtilization \
  --dimensions Name=ClusterName,Value=my-cluster Name=ServiceName,Value=my-service \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Average
```

## Security

### IAM Roles
- **Task Role**: For application permissions
- **Execution Role**: For ECS agent permissions

### Network Security
- Use VPC with private subnets
- Security groups for task-level control
- No public IPs for sensitive workloads

### Secrets Management
```bash
# Pass secrets from Secrets Manager
--container-definitions '[
  {
    "secrets": [
      {
        "name": "DB_PASSWORD",
        "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:db-password"
      }
    ]
  }
]'
```

## Best Practices

1. **Use task definitions** as code
2. **Implement health checks** for all services
3. **Use service discovery** for microservices
4. **Enable Container Insights** for monitoring
5. **Use ECR** for container images
6. **Implement rolling deployments**
7. **Use Service Connect** for service mesh
8. **Monitor costs** with Cost Explorer
9. **Use Fargate Spot** for dev/test
10. **Implement proper logging** with CloudWatch
