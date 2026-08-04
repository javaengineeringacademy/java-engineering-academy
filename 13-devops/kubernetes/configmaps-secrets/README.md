# Kubernetes ConfigMaps & Secrets

## Overview

ConfigMaps and Secrets are Kubernetes objects used to store configuration data and sensitive information separately from application code.

## ConfigMaps

### Creating ConfigMaps
```yaml
# From literal values
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  APP_ENV: production
  APP_DEBUG: "false"
  config.json: |
    {
      "database": {
        "host": "postgres-service",
        "port": 5432
      }
    }

---
# From file
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-file-config
data:
  nginx.conf: |
    server {
      listen 80;
      server_name localhost;
    }
```

### Using ConfigMaps
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-pod
spec:
  containers:
  - name: my-app
    image: my-app:1.0
    envFrom:
    - configMapRef:
        name: my-config
    volumeMounts:
    - name: config-volume
      mountPath: /etc/config
  volumes:
  - name: config-volume
    configMap:
      name: my-config
```

## Secrets

### Creating Secrets
```yaml
# Opaque secret
apiVersion: v1
kind: Secret
metadata:
  name: my-secret
type: Opaque
data:
  username: YWRtaW4=  # base64 encoded
  password: cGFzc3dvcmQ=  # base64 encoded

# TLS secret
apiVersion: v1
kind: Secret
metadata:
  name: my-tls
type: kubernetes.io/tls
data:
  tls.crt: <base64-encoded-cert>
  tls.key: <base64-encoded-key>

# Docker registry secret
apiVersion: v1
kind: Secret
metadata:
  name: my-registry
type: kubernetes.io/dockerconfigjson
data:
  .dockerconfigjson: <base64-encoded-docker-config>
```

### Using Secrets
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-pod
spec:
  containers:
  - name: my-app
    image: my-app:1.0
    env:
    - name: DB_USERNAME
      valueFrom:
        secretKeyRef:
          name: my-secret
          key: username
    - name: DB_PASSWORD
      valueFrom:
        secretKeyRef:
          name: my-secret
          key: password
    volumeMounts:
    - name: secret-volume
      mountPath: /etc/secrets
  volumes:
  - name: secret-volume
    secret:
      secretName: my-secret
```

## Best Practices

1. **Don't store secrets in code** - Use Kubernetes Secrets
2. **Use external secret management** - Consider Vault or cloud providers
3. **Encrypt secrets at rest** - Enable encryption
4. **Use RBAC** - Control access to secrets
5. **Rotate secrets** - Implement secret rotation
6. **Use immutable ConfigMaps** - Prevent changes
7. **Monitor secret access** - Track usage
8. **Document configuration** - Add descriptions for complex configs
9. **Test configuration** - Verify application behavior
10. **Use namespaces** - Isolate configuration
