# Kubernetes Troubleshooting

## CrashLoopBackOff

A Pod repeatedly crashes and restarts. The container starts, fails, and Kubernetes restarts it with exponential backoff.

### Common Causes

- Application errors (bugs, exceptions)
- Missing configuration or environment variables
- Insufficient resources (OOM killed)
- Failed readiness/liveness probes
- Dependency failures (database, API)

### Debugging Steps

```bash
# Check Pod status
kubectl get pods -n production

# View previous container logs
kubectl logs my-pod --previous -n production

# Check events
kubectl describe pod my-pod -n production

# Check resource limits
kubectl get pod my-pod -n production -o yaml | grep -A 10 resources

# Check environment variables
kubectl exec -it my-pod -- env
```

### Solutions

1. Fix application bugs
2. Ensure all required environment variables are set
3. Increase resource limits if OOM killed
4. Fix failing health checks
5. Check dependency health

## ImagePullBackOff

Kubernetes cannot pull the container image from the registry.

### Common Causes

- Image name or tag incorrect
- Private registry without proper credentials
- Registry rate limiting
- Network connectivity issues
- Image does not exist

### Debugging Steps

```bash
# Check Pod events
kubectl describe pod my-pod -n production

# Check image name
kubectl get pod my-pod -n production -o yaml | grep image

# Test image pull manually
docker pull my-image:my-tag
```

### Solutions

1. Verify image name and tag exist
2. Create image pull secret for private registries
3. Use imagePullSecrets in Pod spec
4. Check network connectivity
5. Wait for rate limit to reset

```yaml
# Image pull secret
spec:
  imagePullSecrets:
  - name: my-registry-secret
```

## OOMKilled

Container exceeded its memory limit and was terminated by the kernel.

### Common Causes

- Memory leak in application
- Insufficient memory limit
- JVM heap not configured correctly
- Caching too much data

### Debugging Steps

```bash
# Check Pod status
kubectl get pod my-pod -n production -o yaml | grep -A 5 lastState

# Check memory usage
kubectl top pod my-pod -n production

# Check memory limits
kubectl get pod my-pod -n production -o jsonpath='{.spec.containers[*].resources.limits.memory}'
```

### Solutions

1. Increase memory limits
2. Fix memory leaks in application
3. Configure JVM heap settings (for Java apps)
4. Optimize caching
5. Use VPA for recommendations

```yaml
resources:
  requests:
    memory: "512Mi"
  limits:
    memory: "1Gi"
```

## Pending Pods

Pods remain in Pending state and are not scheduled to any node.

### Common Causes

- Insufficient node resources
- No matching node for node selector/affinity
- PersistentVolumeClaim not bound
- Resource quota exceeded
- Taints without tolerations

### Debugging Steps

```bash
# Check Pod status
kubectl describe pod my-pod -n production

# Check node resources
kubectl describe nodes | grep -A 5 "Allocated resources"

# Check resource quotas
kubectl get resourcequota -n production

# Check PVC status
kubectl get pvc -n production
```

### Solutions

1. Add more nodes or use larger instances
2. Check node selectors and affinity rules
3. Create and bind PersistentVolumeClaims
4. Increase resource quotas
5. Add tolerations for tainted nodes

## Service Not Reachable

Cannot access a Service from within or outside the cluster.

### Common Causes

- Service selector doesn't match Pod labels
- Endpoints are empty
- Network policies blocking traffic
- Service port misconfiguration
- Ingress not configured

### Debugging Steps

```bash
# Check Service endpoints
kubectl get endpoints my-service -n production

# Check Service selector
kubectl get svc my-service -n production -o yaml

# Check Pod labels
kubectl get pods -n production --show-labels

# Test connectivity from within cluster
kubectl run debug --rm -it --image=busybox -- wget -qO- http://my-service:80
```

### Solutions

1. Ensure Service selector matches Pod labels
2. Verify Pod readiness
3. Check network policies
4. Verify port configuration
5. Configure Ingress for external access

## Node Not Ready

Node shows NotReady status and cannot schedule Pods.

### Common Causes

- kubelet not running
- Network connectivity issues
- Disk pressure
- Memory pressure
- PID pressure
- Certificate issues

### Debugging Steps

```bash
# Check node status
kubectl describe node my-node

# SSH to node and check kubelet
ssh my-node
systemctl status kubelet
journalctl -u kubelet

# Check system resources
df -h
free -m
```

### Solutions

1. Restart kubelet service
2. Fix network connectivity
3. Clear disk space
4. Free up memory
5. Renew certificates

## PVC Stuck in Pending

PersistentVolumeClaim cannot find a matching PersistentVolume.

### Common Causes

- No StorageClass available
- StorageClass provisioner not working
- Insufficient storage capacity
- Access mode mismatch

### Debugging Steps

```bash
# Check PVC status
kubectl describe pvc my-pvc -n production

# Check StorageClasses
kubectl get sc

# Check PVs
kubectl get pv
```

### Solutions

1. Create appropriate StorageClass
2. Verify storage provisioner is running
3. Check storage capacity
4. Verify access modes

## API Server Unreachable

Cannot connect to the Kubernetes API server.

### Common Causes

- API server down
- Network issues
- Certificate issues
- kubeconfig incorrect
- Firewall blocking

### Debugging Steps

```bash
# Check API server
kubectl cluster-info

# Check kubeconfig
kubectl config view

# Test API server connectivity
curl -k https://kubernetes.default.svc:6443/healthz
```

### Solutions

1. Restart API server
2. Fix network connectivity
3. Renew certificates
4. Verify kubeconfig
5. Check firewall rules
