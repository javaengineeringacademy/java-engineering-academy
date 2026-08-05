# Kubernetes Performance Optimization

## Resource Tuning

### CPU Resources

CPU is a compressible resource. Containers can be throttled when exceeding limits. Requests guarantee minimum CPU share; limits cap maximum usage.

```yaml
resources:
  requests:
    cpu: "500m"      # 0.5 cores guaranteed
  limits:
    cpu: "2000m"     # 2 cores maximum
```

Best practices:
- Set requests based on observed average usage
- Set limits 2-3x requests for burst capacity
- Use CPU quotas carefully to avoid throttling
- Monitor CPU throttling with cAdvisor metrics

### Memory Resources

Memory is non-compressible. Exceeding limits causes OOM kills. Requests determine scheduling; limits prevent runaway consumption.

```yaml
resources:
  requests:
    memory: "256Mi"   # 256 MiB guaranteed
  limits:
    memory: "512Mi"   # 512 MiB maximum
```

Best practices:
- Set requests to P95 memory usage
- Set limits to 1.5-2x requests
- Monitor working set, not just RSS
- Consider JVM heap settings for Java apps

### Node Resource Management

```yaml
# Resource quota per namespace
apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-quota
spec:
  hard:
    requests.cpu: "20"
    requests.memory: 40Gi
    limits.cpu: "40"
    limits.memory: 80Gi
    pods: "100"

# Limit ranges for defaults
apiVersion: v1
kind: LimitRange
metadata:
  name: default-limits
spec:
  limits:
  - default:
      cpu: "1"
      memory: 512Mi
    defaultRequest:
      cpu: 100m
      memory: 128Mi
    type: Container
```

## Horizontal Pod Autoscaler (HPA)

Automatically scales Pods based on metrics. Requires metrics-server for CPU/memory metrics.

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
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60
      policies:
      - type: Percent
        value: 100
        periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 10
        periodSeconds: 60
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
```

## Vertical Pod Autoscaler (VPA)

Automatically adjusts CPU and memory requests based on historical usage. Recommended mode adjusts requests but does not restart Pods.

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
    updateMode: "Off"  # "Off" for recommendations only
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

## Cluster Autoscaling

Automatically adjusts the number of nodes based on pod scheduling needs and node utilization.

### Cluster Autoscaler

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
        - --node-group-auto-discovery=asg:tag=k8s.io/cluster-autoscaler/enabled,k8s.io/cluster-autoscaler/my-cluster
```

### Karpenter

AWS-native node provisioner that provides more flexible and cost-effective node provisioning than Cluster Autoscaler.

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
  - key: karpenter.sh/capacity-type
    operator: In
    values:
    - on-demand
  providerRef:
    name: default
  ttlSecondsAfterEmpty: 30
  ttlSecondsUntilExpired: 2592000
```

## Node Pool Optimization

### Instance Type Selection

| Workload | Recommended Instance | vCPU | Memory | Use Case |
|----------|---------------------|------|--------|----------|
| General | m5.large | 2 | 8 GiB | Web servers, apps |
| Compute | c5.xlarge | 4 | 8 GiB | Batch processing |
| Memory | r5.2xlarge | 8 | 64 GiB | Databases, caches |
| GPU | p3.2xlarge | 8 | 61 GiB | ML inference |

### Node Affinity and Taints

```yaml
# Node affinity for scheduling
affinity:
  nodeAffinity:
    requiredDuringSchedulingIgnoredDuringExecution:
      nodeSelectorTerms:
      - matchExpressions:
        - key: node-type
          operator: In
          values:
          - compute-optimized

# Taints and tolerations
tolerations:
- key: "dedicated"
  operator: "Equal"
  value: "gpu"
  effect: "NoSchedule"
```

## Network Performance

### CNI Plugin Selection

- **Calico**: Best for network policies and performance
- **Cilium**: Best for eBPF-based high performance
- **AWS VPC CNI**: Native VPC networking, no overlay overhead
- **Flannel**: Simple but limited network policies

### Service Mesh Considerations

- Istio adds ~1ms latency per hop
- Linkerd is lighter weight than Istio
- Consider service mesh only when needed (mTLS, traffic management)
- Use ambient mesh mode for reduced overhead

## Storage Performance

### Volume Types

- **Local SSD**: Best IOPS, no network latency
- **EBS gp3**: Good balance of IOPS and cost
- **EBS io2**: Maximum IOPS for databases
- **NFS**: Shared storage, moderate performance

### Storage Class Configuration

```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: fast-ssd
provisioner: ebs.csi.aws.com
parameters:
  type: gp3
  iops: "10000"
  throughput: "250"
  encrypted: "true"
reclaimPolicy: Retain
allowVolumeExpansion: true
volumeBindingMode: WaitForFirstConsumer
```

## Performance Monitoring

### Key Metrics

- Pod CPU/Memory utilization vs requests
- Node CPU/Memory utilization
- Pod restart counts
- Pod scheduling latency
- API server request latency
- etcd operation latency

### Performance Testing

```bash
# Install kube-burner for benchmarking
kubectl apply -f https://raw.githubusercontent.com/cloud-bulldozer/kube-burner/master/docs/manifests/performance-report.yaml

# Run kubectl top
kubectl top nodes
kubectl top pods --sort-by=memory
kubectl top pods --sort-by=cpu
```
