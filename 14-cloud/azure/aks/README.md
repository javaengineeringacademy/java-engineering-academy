# Azure Kubernetes Service (AKS)

## Overview

AKS is a managed Kubernetes service for deploying containerized applications.

## Cluster Types

| Type            | Description                    |
|-----------------|--------------------------------|
| Standard        | Full control over cluster      |
| Autopilot       | Fully managed nodes            |
| Node Pools      | Custom node configurations     |

## Creating Clusters

### Azure CLI
```bash
# Create AKS cluster
az aks create \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --node-count 3 \
  --node-vm-size Standard_DS2_v2 \
  --enable-addons monitoring \
  --generate-ssh-keys

# Get credentials
az aks get-credentials \
  --resource-group myResourceGroup \
  --name myAKSCluster
```

### ARM Template
```json
{
  "type": "Microsoft.ContainerService/managedClusters",
  "apiVersion": "2023-05-01",
  "name": "myAKSCluster",
  "location": "eastus",
  "properties": {
    "kubernetesVersion": "1.27",
    "dnsPrefix": "myaks",
    "agentPoolProfiles": [
      {
        "name": "nodepool1",
        "count": 3,
        "vmSize": "Standard_DS2_v2"
      }
    ]
  }
}
```

## Node Pools

```bash
# Create node pool
az aks nodepool add \
  --resource-group myResourceGroup \
  --cluster-name myAKSCluster \
  --name mynodepool \
  --node-count 3 \
  --node-vm-size Standard_DS2_v2

# Update node pool
az aks nodepool update \
  --resource-group myResourceGroup \
  --cluster-name myAKSCluster \
  --name mynodepool \
  --node-count 5
```

### Node Pool Types
| Type         | Use Case                    |
|--------------|-----------------------------|
| System       | System workloads            |
| User         | Application workloads       |
| Spot         | Fault-tolerant workloads    |

## Workload Identity

```bash
# Enable workload identity
az aks update \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --enable-workload-identity

# Create managed identity
az identity create \
  --name myIdentity \
  --resource-group myResourceGroup

# Bind to Kubernetes service account
az identity federated-credential create \
  --name myFederatedCredential \
  --identity-name myIdentity \
  --resource-group myResourceGroup \
  --issuer myAKSCluster-oidc-issuer.oidc.prod-east.azmk8s.io \
  --subject system:serviceaccount:default:my-sa
```

## Azure AD Integration

```bash
# Enable Azure AD
az aks update \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --enable-aad \
  --aad-admin-group-object-ids <group-id>
```

## Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    kubernetes.io/ingress.class: azure/application-gateway
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

## Managed Certificates

```yaml
apiVersion: networking.k8s.io/v1
kind: Certificate
metadata:
  name: my-cert
spec:
  dnsNames:
  - myapp.example.com
  issuerRef:
    name: letsencrypt-prod
    kind: ClusterIssuer
```

## Autoscaling

```bash
# Enable cluster autoscaler
az aks update \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --enable-cluster-autoscaler \
  --min-count 1 \
  --max-count 10
```

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-deployment
  minReplicas: 1
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## Azure Policy

```bash
# Enable Azure Policy
az aks update \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --enable-addons azure-policy

# Apply policy
az policy assignment create \
  --name my-policy \
  --policy "require-labels" \
  --scope myAKSCluster
```

## Monitoring

```bash
# Enable monitoring
az aks update \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --enable-addons monitoring

# Get metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.ContainerService/managedClusters/myAKSCluster \
  --metric "node_count"
```

## Cost Optimization

- **Use spot nodes** for fault-tolerant workloads
- **Implement autoscaling** for demand
- **Right-size nodes** based on workload
- **Use Azure reservations** for steady workloads
- **Monitor with Azure Monitor**

## Best Practices

1. **Use workload identity** for security
2. **Implement Azure AD** integration
3. **Enable monitoring** and logging
4. **Use Azure Policy** for compliance
5. **Implement proper RBAC**
6. **Use managed identities**
7. **Enable cluster autoscaler**
8. **Use node pools** for workload isolation
9. **Implement proper networking**
10. **Regular cluster upgrades**
