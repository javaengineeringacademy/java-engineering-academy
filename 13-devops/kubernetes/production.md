# Kubernetes Production Guide

## Cluster Configuration

### High Availability Control Plane

- Deploy 3 or 5 control plane nodes for etcd quorum
- Use external load balancer for API server
- Keep control plane nodes dedicated (not running workloads)
- Use separate etcd cluster for large deployments

### Node Configuration

```yaml
# Node labels and taints
kubectl label nodes node1 node-type=compute
kubectl taint nodes node1 dedicated=gpu:NoSchedule

# Node affinity in deployment
affinity:
  nodeAffinity:
    requiredDuringSchedulingIgnoredDuringExecution:
      nodeSelectorTerms:
      - matchExpressions:
        - key: node-type
          operator: In
          values:
          - compute
```

## Backup Strategy

### etcd Backup

```bash
# Backup etcd
ETCDCTL_API=3 etcdctl snapshot save /backup/etcd-snapshot-$(date +%Y%m%d).db \
  --endpoints=https://127.0.0.1:2379 \
  --cacert=/etc/kubernetes/pki/etcd/ca.crt \
  --cert=/etc/kubernetes/pki/etcd/server.crt \
  --key=/etc/kubernetes/pki/etcd/server.key

# Restore etcd
ETCDCTL_API=3 etcdctl snapshot restore /backup/etcd-snapshot.db \
  --data-dir=/var/lib/etcd-restored
```

### Velero Backup

```bash
# Install Velero
velero install \
  --provider aws \
  --bucket my-cluster-backups \
  --secret-file ./credentials-velero \
  --backup-location-config region=us-west-2

# Backup all namespaces
velero backup create full-backup

# Backup specific namespace
velero backup create prod-backup --include-namespaces production

# Restore from backup
velero restore create --from-backup prod-backup
```

## Disaster Recovery

### RPO and RTO

- **RPO (Recovery Point Objective)**: Maximum data loss acceptable
- **RTO (Recovery Time Objective)**: Maximum downtime acceptable
- For etcd: RPO = backup interval, RTO = restore time
- For applications: RPO = deployment frequency, RTO = redeployment time

### DR Strategies

- **Multi-Cluster**: Active-passive or active-active clusters
- **Multi-Region**: Deploy across AWS regions
- **Backup and Restore**: Regular etcd and volume backups
- **GitOps**: Store all manifests in Git, use ArgoCD/Flux

## Cluster Upgrades

### Upgrade Process

```bash
# Check available versions
kubectl get nodes -o wide

# Upgrade control plane (kubeadm)
kubeadm upgrade plan
kubeadm upgrade apply v1.28.0

# Upgrade kubelet on nodes
apt-get update && apt-get install -y kubelet=1.28.0-00 kubectl=1.28.0-00
systemctl daemon-reload
systemctl restart kubelet
```

### Managed Cluster Upgrades

```bash
# EKS
aws eks update-cluster-version --name my-cluster --kubernetes-version 1.28

# GKE
gcloud container clusters upgrade my-cluster --master --cluster-version 1.28
gcloud container clusters upgrade my-cluster --node-pool default-pool --cluster-version 1.28

# AKS
az aks upgrade --resource-group myRG --name myAKS --kubernetes-version 1.28
```

## Resource Management

### Namespace Isolation

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    env: production
    team: platform
---
apiVersion: v1
kind: ResourceQuota
metadata:
  name: prod-quota
  namespace: production
spec:
  hard:
    requests.cpu: "20"
    requests.memory: 40Gi
    limits.cpu: "40"
    limits.memory: 80Gi
    pods: "100"
    services: "20"
    persistentvolumeclaims: "10"
```

## Monitoring and Observability

### Essential Metrics

- Cluster health: node status, pod status, API server latency
- Application: request rate, error rate, latency (RED metrics)
- Infrastructure: CPU, memory, disk, network usage
- Business: custom metrics per application

### Log Aggregation

```yaml
# Fluentd DaemonSet for log collection
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluentd
  namespace: kube-system
spec:
  template:
    spec:
      containers:
      - name: fluentd
        image: fluent/fluentd-kubernetes-daemonset:v1.16
        volumeMounts:
        - name: varlog
          mountPath: /var/log
        - name: containers
          mountPath: /var/lib/docker/containers
          readOnly: true
```

## Security Hardening

1. Enable RBAC and Pod Security Standards
2. Use network policies for traffic control
3. Encrypt secrets at rest and in transit
4. Scan images for vulnerabilities
5. Implement audit logging
6. Rotate certificates and credentials
7. Limit pod privileges (non-root, read-only filesystem)
8. Use external secret stores

## Production Checklist

- [ ] HA control plane (3+ nodes)
- [ ] etcd backups configured
- [ ] Velero for workload backups
- [ ] Monitoring stack deployed
- [ ] Log aggregation configured
- [ ] Network policies implemented
- [ ] Resource quotas enforced
- [ ] Pod security standards applied
- [ ] Ingress with TLS configured
- [ ] DNS and service discovery working
- [ ] RBAC policies reviewed
- [ ] Upgrade plan documented
- [ ] Runbooks for common issues
- [ ] DR procedures tested
