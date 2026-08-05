# Kubernetes Scaling

## Horizontal Pod Autoscaler (HPA)

Automatically scales the number of Pod replicas based on observed metrics. Requires metrics-server or custom metrics adapter.

### Configuration

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60
      policies:
      - type: Percent
        value: 100
        periodSeconds: 60
      - type: Pods
        value: 4
        periodSeconds: 60
      selectPolicy: Max
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 10
        periodSeconds: 120
```

### Metrics

- **Resource metrics**: CPU, memory utilization
- **Custom metrics**: Application-specific metrics (requests per second, queue length)
- **External metrics**: Metrics from outside the cluster

## Vertical Pod Autoscaler (VPA)

Automatically adjusts CPU and memory requests based on historical usage. Does not change replica count.

### Three Modes

- **Off**: Only provides recommendations
- **Initial**: Sets requests on Pod creation only
- **Auto**: Can evict and recreate Pods with new requests

### Configuration

```yaml
apiVersion: autoscaling.k8s.io/v1
kind: VerticalPodAutoscaler
metadata:
  name: my-app-vpa
spec:
  targetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  updatePolicy:
    updateMode: "Auto"
  resourcePolicy:
    containerPolicies:
    - containerName: my-app
      minAllowed:
        cpu: 100m
        memory: 128Mi
      maxAllowed:
        cpu: 2000m
        memory: 4Gi
      controlledResources: ["cpu", "memory"]
```

## Cluster Autoscaler

Automatically adjusts the number of nodes when Pods cannot be scheduled due to insufficient resources or nodes are underutilized.

### Features

- Scales node groups based on pending pods
- Removes underutilized nodes after rescheduling pods
- Supports multiple node groups with different instance types
- Works with AWS ASG, GCP MIG, Azure VMSS

### Configuration

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cluster-autoscaler
  namespace: kube-system
spec:
  template:
    spec:
      containers:
      - name: cluster-autoscaler
        image: k8s.gcr.io/autoscaling/cluster-autoscaler:v1.27.0
        command:
        - ./cluster-autoscaler
        - --v=4
        - --stderrthreshold=info
        - --cloud-provider=aws
        - --skip-nodes-with-local-storage=false
        - --expander=least-waste
        - --node-group-auto-discovery=asg:tag=k8s.io/cluster-autoscaler/enabled
        - --balance-similar-node-groups
        - --skip-nodes-with-system-pods=false
```

### Expander Strategies

- **random**: Random selection among best options
- **least-waste**: Choose node group with least idle resources
- **priority**: Choose highest priority node group
- **price**: Choose cheapest node group (cloud provider)

## Karpenter

AWS-native node provisioner that launches right-sized nodes based on pod requirements.

### Provisioner

```yaml
apiVersion: karpenter.sh/v1alpha5
kind: Provisioner
metadata:
  name: default
spec:
  requirements:
  - key: kubernetes.io/arch
    operator: In
    values:
    - amd64
    - arm64
  - key: karpenter.sh/capacity-type
    operator: In
    values:
    - on-demand
    - spot
  - key: karpenter.k8s.aws/instance-category
    operator: In
    values:
    - m
    - c
    - r
  - key: karpenter.k8s.aws/instance-generation
    operator: Gt
    values:
    - "2"
  providerRef:
    name: default
  ttlSecondsAfterEmpty: 30
  ttlSecondsUntilExpired: 2592000
  limits:
    resources:
      cpu: "100"
      memory: 200Gi
```

### NodePool (v1beta1)

```yaml
apiVersion: karpenter.sh/v1beta1
kind: NodePool
metadata:
  name: default
spec:
  template:
    spec:
      requirements:
      - key: kubernetes.io/arch
        operator: In
        values:
        - amd64
      - key: karpenter.sh/capacity-type
        operator: In
        values:
        - on-demand
  limits:
    cpu: "100"
    memory: 200Gi
  disruption:
    consolidationPolicy: WhenUnderutilized
    expireAfter: 720h
```

## Scaling Best Practices

1. Set resource requests accurately for HPA to work
2. Use multiple metrics for scaling decisions
3. Configure stabilization windows to prevent flapping
4. Set minReplicas high enough for availability
5. Use PDB to protect during scaling events
6. Test scaling with load testing tools
7. Monitor scaling events and adjust thresholds
8. Consider cold start time for fast scaling
