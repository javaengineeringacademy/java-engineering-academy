# Kubernetes Gateway API

## Overview

Gateway API is a Kubernetes-native API for managing ingress traffic. It provides a more expressive and extensible alternative to Ingress.

## GatewayClass

```yaml
apiVersion: gateway.networking.k8s.io/v1
kind: GatewayClass
metadata:
  name: my-gateway-class
spec:
  controllerName: example.com/gateway-controller
```

## Gateway

```yaml
apiVersion: gateway.networking.k8s.io/v1
kind: Gateway
metadata:
  name: my-gateway
  namespace: default
spec:
  gatewayClassName: my-gateway-class
  listeners:
  - name: http
    protocol: HTTP
    port: 80
  - name: https
    protocol: HTTPS
    port: 443
    tls:
      mode: Terminate
      certificateRefs:
      - name: my-tls-cert
```

## HTTPRoute

```yaml
apiVersion: gateway.networking.k8s.io/v1
kind: HTTPRoute
metadata:
  name: my-route
  namespace: default
spec:
  parentRefs:
  - name: my-gateway
  hostnames:
  - myapp.example.com
  rules:
  - matches:
    - path:
        type: PathPrefix
        value: /api
    backendRefs:
    - name: api-service
      port: 8080
  - matches:
    - path:
        type: PathPrefix
        value: /
    backendRefs:
    - name: frontend-service
      port: 80
```

## TCPRoute

```yaml
apiVersion: gateway.networking.k8s.io/v1alpha2
kind: TCPRoute
metadata:
  name: my-tcp-route
spec:
  parentRefs:
  - name: my-gateway
    sectionName: tcp
  rules:
  - backendRefs:
    - name: my-tcp-service
      port: 3306
```

## Best Practices

1. **Use GatewayClass** - Define controller implementations
2. **Implement TLS** - Encrypt traffic
3. **Use hostnames** - Virtual hosting
4. **Implement path routing** - Route based on URL
5. **Use backend references** - Target services
6. **Monitor gateways** - Track traffic and errors
7. **Use annotations** - Configure controller features
8. **Document routes** - Add descriptions for complex configs
9. **Test routing** - Verify path and host matching
10. **Use cert-manager** - Automate TLS certificate management
