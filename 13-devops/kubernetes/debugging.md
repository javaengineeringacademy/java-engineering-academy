# Kubernetes Debugging

## kubectl Debug

### Ephemeral Containers

Add a debug container to a running Pod without restarting it:

```bash
kubectl debug -it my-pod --image=busybox --target=my-container
```

### Debug Node

Debug node-level issues by creating a debug Pod:

```bash
kubectl debug node/my-node -it --image=busybox
```

### Debug Profiles

Use pre-defined profiles for common debugging scenarios:

```bash
kubectl debug -it my-pod --image=busybox --profile=general
kubectl debug -it my-pod --image=busybox --profile=sysadmin
```

## Pod Debugging

### Check Pod Status

```bash
# List pods
kubectl get pods -n production

# Get detailed status
kubectl describe pod my-pod -n production

# Check Pod events
kubectl get events --field-selector involvedObject.name=my-pod
```

### View Logs

```bash
# View logs
kubectl logs my-pod -n production

# Follow logs
kubectl logs -f my-pod -n production

# View previous container logs
kubectl logs my-pod --previous -n production

# View logs for specific container
kubectl logs my-pod -c sidecar -n production

# View logs with timestamps
kubectl logs --timestamps=true my-pod -n production
```

### Execute Commands

```bash
# Execute command in Pod
kubectl exec my-pod -n production -- ls /app

# Interactive shell
kubectl exec -it my-pod -n production -- /bin/bash

# Execute in specific container
kubectl exec -it my-pod -c sidecar -n production -- /bin/sh
```

### Port Forwarding

```bash
# Forward port from Pod to local
kubectl port-forward my-pod 8080:80 -n production

# Forward to service
kubectl port-forward svc/my-service 8080:80 -n production

# Forward to specific pod
kubectl port-forward pod/my-pod 8080:80 -n production
```

## Node Debugging

### Check Node Status

```bash
# List nodes
kubectl get nodes

# Describe node
kubectl describe node my-node

# Check node conditions
kubectl get nodes -o custom-columns=NAME:.metadata.name,STATUS:.status.conditions[-1].type,HEALTH:.status.conditions[-1].status
```

### Node Resources

```bash
# Check node resource usage
kubectl top nodes

# Check pod resource usage
kubectl top pods -n production --sort-by=memory

# Check pod resource usage by CPU
kubectl top pods -n production --sort-by=cpu
```

### Debug Node Issues

```bash
# Check kubelet logs
ssh my-node
journalctl -u kubelet

# Check system resources
df -h
free -m
top
```

## Service Debugging

### Check Service Endpoints

```bash
# Get service endpoints
kubectl get endpoints my-service -n production

# Describe service
kubectl describe svc my-service -n production

# Check service selector
kubectl get svc my-service -n production -o yaml
```

### Test Service Connectivity

```bash
# Run a debug Pod to test connectivity
kubectl run debug --rm -it --image=busybox -- /bin/sh

# Inside debug Pod
wget -qO- http://my-service:80
nslookup my-service
```

## Deployment Debugging

### Check Deployment Status

```bash
# Get deployment status
kubectl get deployment my-deployment -n production

# Describe deployment
kubectl describe deployment my-deployment -n production

# Check rollout status
kubectl rollout status deployment/my-deployment -n production
```

### Rollback

```bash
# View rollout history
kubectl rollout history deployment/my-deployment -n production

# Rollback to previous version
kubectl rollout undo deployment/my-deployment -n production

# Rollback to specific revision
kubectl rollout undo deployment/my-deployment --to-revision=2 -n production
```

## Network Debugging

### Check Network Policies

```bash
# List network policies
kubectl get networkpolicy -n production

# Describe network policy
kubectl describe networkpolicy my-policy -n production
```

### DNS Debugging

```bash
# Run DNS lookup test
kubectl run dns-test --rm -it --image=busybox -- nslookup my-service

# Check CoreDNS pods
kubectl get pods -n kube-system -l k8s-app=kube-dns
```

## Storage Debugging

### Check PVC Status

```bash
# List PVCs
kubectl get pvc -n production

# Describe PVC
kubectl describe pvc my-pvc -n production

# Check PVs
kubectl get pv
```

## Log Aggregation

### Centralized Logging

```bash
# Query Loki for logs
kubectl port-forward svc/loki 3100:3100 -n monitoring

# Query via curl
curl -G http://localhost:3100/loki/api/v1/query_range \
  --data-urlencode 'query={namespace="production"}'
```

## Performance Debugging

### CPU and Memory Analysis

```bash
# Install resource profiler
kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/master/test/images/resource-consumer/cleanup.yaml

# Check resource consumption
kubectl top pods -n production
kubectl top nodes
```

### Network Debugging

```bash
# Check network latency
kubectl run nettest --rm -it --image=nicolaka/netshoot -- ping my-service

# Check network policies
kubectl get networkpolicy -n production -o yaml
```
