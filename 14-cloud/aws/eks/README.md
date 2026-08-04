# Amazon EKS (Elastic Kubernetes Service)

## Overview

Amazon EKS is a managed Kubernetes service that makes it easy to run Kubernetes on AWS without needing to install, operate, and maintain your own Kubernetes control plane.

## EKS Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     AWS Cloud                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │              EKS Control Plane                    │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐          │  │
│  │  │  API    │  │ etcd    │  │Scheduler│          │  │
│  │  │ Server  │  │         │  │         │          │  │
│  │  └─────────┘  └─────────┘  └─────────┘          │  │
│  └───────────────────────────────────────────────────┘  │
│                           │                             │
│  ┌───────────────────────────────────────────────────┐  │
│  │              EKS Data Plane                       │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐          │  │
│  │  │  Node   │  │  Node   │  │  Node   │          │  │
│  │  │  Group  │  │  Group  │  │  Group  │          │  │
│  │  └─────────┘  └─────────┘  └─────────┘          │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Managed Node Groups

```bash
# Create managed node group
aws eks create-nodegroup \
  --cluster-name my-cluster \
  --nodegroup-name my-nodes \
  --node-role arn:aws:iam::123456789012:role/eks-node-role \
  --instance-types t3.medium \
  --scaling-config minSize=2,maxSize=10,desiredSize=3 \
  --subnets subnet-12345678 subnet-87654321 \
  --ami-type AL2_x86_64
```

### Node Types
| Type          | Use Case              | Notes              |
|---------------|-----------------------|--------------------|
| m5.large      | General purpose       | Balanced compute   |
| c5.large      | Compute optimized     | CPU-intensive      |
| r5.large      | Memory optimized      | In-memory apps     |
| p3.large      | GPU                   | ML/ML workloads    |
| t3.medium     | Burstable             | Dev/test           |

## EKS on Fargate

```bash
# Create Fargate profile
aws eks create-fargate-profile \
  --cluster-name my-cluster \
  --fargate-profile-name my-profile \
  --selectors '[
    {
      "namespace": "production",
      "labels": {
        "compute": "fargate"
      }
    }
  ]'
```

## Auto Scaling

### Cluster Autoscaler
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cluster-autoscaler
  namespace: kube-system
spec:
  replicas: 1
  selector:
    matchLabels:
      app: cluster-autoscaler
  template:
    metadata:
      labels:
        app: cluster-autoscaler
    spec:
      containers:
      - image: registry.k8s.io/autoscaling/cluster-autoscaler:v1.28.0
        name: cluster-autoscaler
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
```yaml
apiVersion: karpenter.sh/v1alpha5
kind: Provisioner
metadata:
  name: default
spec:
  requirements:
    - key: karpenter.sh/capacity-type
      operator: In
      values: ["spot", "on-demand"]
    - key: node.kubernetes.io/instance-type
      operator: In
      values: ["m5.large", "m5.xlarge", "m5.2xlarge"]
  limits:
    resources:
      cpu: "100"
      memory: 200Gi
  providerRef:
    name: default
---
apiVersion: karpenter.k8s.aws/v1alpha1
kind: AWSNodeTemplate
metadata:
  name: default
spec:
  subnetSelector:
    karpenter.sh/discovery: my-cluster
  securityGroupSelector:
    karpenter.sh/discovery: my-cluster
```

## EKS Add-ons

```bash
# Install add-ons
aws eks create-addon \
  --cluster-name my-cluster \
  --addon-name vpc-cni \
  --resolve-conflicts OVERWRITE

aws eks create-addon \
  --cluster-name my-cluster \
  --addon-name coredns \
  --resolve-conflicts OVERWRITE

aws eks create-addon \
  --cluster-name my-cluster \
  --addon-name kube-proxy \
  --resolve-conflicts OVERWRITE
```

### Popular Add-ons
| Add-on         | Purpose                          |
|----------------|----------------------------------|
| VPC CNI        | Pod networking                   |
| CoreDNS        | DNS resolution                   |
| kube-proxy     | Network proxy                    |
| EBS CSI Driver | Persistent storage               |
| AWS Load Balancer Controller | Load balancing    |
| Metrics Server | Resource metrics                 |

## Service Mesh (App Mesh)

```yaml
apiVersion: appmesh.k8s.aws/v1beta2
kind: Mesh
metadata:
  name: my-mesh
spec:
  selector:
    matchLabels:
      mesh: my-mesh
  namespaceSelector:
    matchLabels:
      mesh: my-mesh
---
apiVersion: appmesh.k8s.aws/v1beta2
kind: VirtualService
metadata:
  name: my-service
spec:
  meshRef:
    name: my-mesh
  http:
    hosts:
    - my-service.mesh.local
      virtualServiceRef:
        name: my-service
```

## Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip
spec:
  rules:
  - host: myapp.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: my-service
            port:
              number: 80
```

## Storage

### EBS CSI Driver
```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: ebs-sc
provisioner: ebs.csi.aws.com
parameters:
  type: gp3
  encrypted: "true"
volumeBindingMode: WaitForFirstConsumer
reclaimPolicy: Retain
```

### EFS CSI Driver
```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: efs-sc
provisioner: efs.csi.aws.com
parameters:
  provisioningMode: efs-ap
  fileSystemId: fs-12345678
  directoryPerms: "700"
  gidRangeStart: "1000"
  gidRangeEnd: "2000"
```

## IAM Roles for Service Accounts (IRSA)

```bash
# Create IAM role for service account
eksctl create iamserviceaccount \
  --cluster=my-cluster \
  --name=my-service-account \
  --namespace=default \
  --role-name=EKS-IRSA-Role \
  --attach-policy-arn=arn:aws:iam::123456789012:policy/MyPolicy \
  --approve
```

## Pod Identity

```bash
# Create Pod Identity association
aws eks create-pod-identity-association \
  --cluster-name my-cluster \
  --namespace default \
  --service-account my-service-account \
  --role-arn arn:aws:iam::123456789012:role/MyRole
```

## Network Configuration

### VPC CNI Configuration
```bash
# Configure VPC CNI for custom networking
kubectl set env daemonset aws-node \
  -n kube-system \
  -c aws-node \
  AWS_VPC_K8S_CNI_CUSTOM_NETWORK_CFG=true

# Set environment variable
kubectl set env daemonset aws-node \
  -n kube-system \
  -c aws-node \
  ENI_CONFIG_LABEL_DEF=topology.kubernetes.io/zone
```

## EKS Anywhere

```bash
# Create EKS Anywhere cluster
eksctl create cluster -f cluster.yaml

# cluster.yaml
apiVersion: anywhere.eks.amazonaws.com/v1alpha1
kind: Cluster
metadata:
  name: my-cluster
spec:
  controlPlaneConfiguration:
    count: 3
    machineGroupRef:
      kind: VSphereMachineConfig
      name: my-cp-machines
  kubernetesVersion: "1.28"
  workerNodeGroupConfigurations:
  - count: 3
    machineGroupRef:
      kind: VSphereMachineConfig
      name: my-worker-machines
```

## EKS Auto Mode

```bash
# Enable EKS Auto Mode
aws eks update-cluster-config \
  --name my-cluster \
  --compute-config '{
    "enabled": true,
    "nodeRoleArn": "arn:aws:iam::123456789012:role/eks-node-role"
  }'
```

## Monitoring

```bash
# Get cluster metrics
aws cloudwatch get-metric-statistics \
  --namespace ContainerInsights \
  --metric-name pod_cpu_utilization \
  --dimensions Name=ClusterName,Value=my-cluster \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Average
```

## Cost Optimization

### Strategies
1. **Use Spot Instances** for worker nodes
2. **Right-size nodes** based on pod requirements
3. **Use Karpenter** for optimal node selection
4. **Implement pod disruption budgets**
5. **Clean up unused resources**

### Savings Plans
```bash
# Purchase Compute Savings Plans
aws savingsplans create-savings-plan \
  --savings-plan-type Compute \
  --term-duration-seconds 31536000 \
  --hourly-commitment 0.5 \
  --purchase-time $(date -u +%Y-%m-%dT%H:%M:%SZ)
```

## Security

### Network Policies
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend
spec:
  podSelector:
    matchLabels:
      role: frontend
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          role: backend
  egress:
  - to:
    - podSelector:
        matchLabels:
          role: database
```

### Pod Security Standards
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    pod-security.kubernetes.io/enforce: restricted
```

## Best Practices

1. **Use managed node groups** for easier management
2. **Enable Container Insights** for monitoring
3. **Use IRSA or Pod Identity** for IAM
4. **Implement network policies** for security
5. **Use Karpenter** for optimal scaling
6. **Enable audit logging**
7. **Use private endpoints** for API server
8. **Implement RBAC** for access control
9. **Use Helm/CDK** for deployments
10. **Regular cluster upgrades**
