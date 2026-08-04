# Kubernetes Ingress

## Overview

Ingress exposes HTTP and HTTPS routes from outside the cluster to services within the cluster. It provides load balancing, SSL termination, and name-based virtual hosting.

## Basic Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
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

## TLS Configuration

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-tls-ingress
  annotations:
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
  - hosts:
    - myapp.example.com
    secretName: my-tls-secret
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

## Path-Based Routing

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
spec:
  rules:
  - host: myapp.example.com
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: api-service
            port:
              number: 8080
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 80
```

## Ingress Controllers

### NGINX Ingress
```bash
# Install NGINX Ingress
helm install nginx-ingress ingress-nginx/ingress-nginx

# Check status
kubectl get pods -n ingress-nginx
```

### Traefik
```bash
# Install Traefik
helm install traefik traefik/traefik
```

## Best Practices

1. **Use TLS** - Encrypt traffic
2. **Implement rate limiting** - Protect services
3. **Use annotations** - Configure controller features
4. **Monitor ingress** - Track traffic and errors
5. **Use path types** - Specify path matching
6. **Implement health checks** - Ensure service availability
7. **Use host-based routing** - Virtual hosting
8. **Document ingress rules** - Add descriptions for complex configs
9. **Test routing** - Verify path and host matching
10. **Use cert-manager** - Automate TLS certificate management
