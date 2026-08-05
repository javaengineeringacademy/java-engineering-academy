# Azure Scaling

## Overview

Azure provides multiple scaling mechanisms to handle varying workloads. Effective scaling ensures performance during peak loads and cost efficiency during quiet periods.

## VM Scale Sets (VMSS)

Automatically increase or decrease VM instances based on demand.

### Autoscale Rules

```json
{
  "metricTrigger": {
    "metricName": "Percentage CPU",
    "timeAggregation": "Average",
    "operator": "GreaterThan",
    "threshold": 70,
    "metricNamespace": "Virtual Machine Scale Sets"
  },
  "scaleAction": {
    "type": "ChangeCount",
    "value": "2",
    "cooldown": "PT5M"
  }
}
```

### Scaling Profiles

- **Default** - Normal operating conditions
- **Schedule** - Predictable peak times (business hours)
- **Recurrence** - Weekly or monthly patterns

### Instance Limits

| Setting | Description |
|---|---|
| Minimum | Lowest instance count |
| Maximum | Highest instance count |
| Default | Starting instance count |
| Cooldown | Time before next scale action |

## Azure App Service Scaling

### Built-in Autoscale

Scale App Service plans based on metrics:

- CPU percentage
- Memory percentage
- HTTP queue length
- Data in/out

### Deployment Slots

Swap slots for zero-downtime scaling:

- Production slot handles live traffic
- Staging slot for pre-deployment testing
- Auto-swap after health check passes
- Pre-warmed instances for fast swap

### Scaling Limits by Tier

| Tier | Max Instances | Auto-scale |
|---|---|---|
| Basic | Up to 10 | Manual |
| Standard | Up to 30 | Yes |
| Premium | Up to 100 | Yes |
| Isolated | Up to 100 | Yes |

## Azure Load Balancer

### Standard Load Balancer

Distributes inbound traffic across backend pool instances.

- Zone-redundant for high availability
- Supports up to 1,000 instances per backend pool
- Health probes detect unhealthy instances
- Session persistence for stateful applications

### Configuration

```bash
az network lb create \
  --resource-group myRG \
  --name myLB \
  --frontend-ip-name myFrontend \
  --backend-pool-name myBackendPool \
  --sku Standard
```

## Azure Front Door

Global HTTP load balancer with built-in WAF and CDN.

### Features

- Route traffic to nearest backend pool
- End-to-end SSL/TLS
- Health probing with custom intervals
- URL path-based routing
- Rate limiting and WAF rules

### Scaling Capabilities

- Automatic global distribution
- Backend pool scaling without Front Door changes
- Caching to reduce origin load
- Compression for faster content delivery

## Azure Application Gateway

Layer 7 load balancer for web applications.

### Features

- SSL offloading
- URL path-based routing
- Multi-site hosting
- WebSocket support
- WAF (OWASP rules)

### Autoscaling

- Enable autoscale for variable traffic
- Configure minimum and maximum instance counts
- Per-instance capacity limits (10-500 units)

## Kubernetes Scaling (AKS)

### Horizontal Pod Autoscaler (HPA)

Scale pods based on CPU, memory, or custom metrics.

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

### Cluster Autoscaler

Automatically add or remove nodes based on pod scheduling needs.

- Monitors pending pods
- Scales nodes up when resources are insufficient
- Scales down when nodes are underutilized
- Configurable cooldown periods

### KEDA (Kubernetes Event-Driven Autoscaling)

Scale based on event sources:

- Azure Service Bus queue length
- Azure Storage queue messages
- Prometheus metrics
- Custom external metrics

## Azure Functions Scaling

### Consumption Plan

- Scale automatically based on demand
- Up to 200 instances
- 5-minute default timeout (10 minutes max)
- Pay per execution

### Premium Plan

- Pre-warmed instances for faster startup
- VNet integration
- Unlimited duration
- Up to 100 instances

## Scaling Best Practices

- Set minimum instance counts for baseline availability
- Use cooldown periods to prevent scale thrashing
- Monitor scaling metrics to tune thresholds
- Test autoscale under load before production
- Combine vertical and horizontal scaling when appropriate
- Use scheduled scaling for predictable traffic patterns
- Review scaling costs regularly
