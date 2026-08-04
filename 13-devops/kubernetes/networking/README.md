# Kubernetes Networking

## Overview

Kubernetes networking provides connectivity between pods, services, and external resources through a flat network model.

## Pod-to-Pod Communication

### Network Model
- Every Pod gets its own IP address
- Pods on any node can communicate with all pods on all nodes
- Agents on a node can communicate with all pods on that node

### CNI Plugins
- **Calico** - Network policy and BGP routing
- **Flannel** - Simple overlay network
- **Weave Net** - Multi-host networking
- **Cilium** - eBPF-based networking

## DNS

### Service DNS
```
# ClusterIP service
my-service.my-namespace.svc.cluster.local

# Headless service
my-headless.my-namespace.svc.cluster.local

# StatefulSet pod
pod-0.my-statefulset.my-namespace.svc.cluster.local
```

### CoreDNS Configuration
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: coredns
  namespace: kube-system
data:
  Corefile: |
    .:53 {
        errors
        health {
            lameduck 5s
        }
        ready
        kubernetes cluster.local in-addr.arpa ip6.arpa {
            pods insecure
            fallthrough in-addr.arpa ip6.arpa
            ttl 30
        }
        prometheus :9153
        forward . /etc/resolv.conf
        cache 30
        loop
        reload
        loadbalance
    }
```

## Network Policies

### Default Deny All
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny-all
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
```

### Allow Specific Traffic
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend-to-backend
spec:
  podSelector:
    matchLabels:
      app: backend
  policyTypes:
  - Ingress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
    ports:
    - protocol: TCP
      port: 8080
```

## Services

### Service Types
```yaml
# ClusterIP
apiVersion: v1
kind: Service
metadata:
  name: my-clusterip
spec:
  type: ClusterIP
  selector:
    app: my-app
  ports:
  - port: 80

# NodePort
apiVersion: v1
kind: Service
metadata:
  name: my-nodeport
spec:
  type: NodePort
  selector:
    app: my-app
  ports:
  - port: 80
    nodePort: 30080

# LoadBalancer
apiVersion: v1
kind: Service
metadata:
  name: my-loadbalancer
spec:
  type: LoadBalancer
  selector:
    app: my-app
  ports:
  - port: 80
```

## Best Practices

1. **Use CNI plugins** - Choose appropriate networking solution
2. **Implement network policies** - Control traffic flow
3. **Use DNS** - Service discovery
4. **Monitor network** - Track traffic and errors
5. **Test connectivity** - Verify pod-to-pod communication
6. **Use network policies** - Implement zero-trust
7. **Document network** - Add descriptions for complex configs
8. **Monitor performance** - Track latency and throughput
9. **Implement security** - Use encryption and authentication
10. **Plan network** - Consider scalability requirements
