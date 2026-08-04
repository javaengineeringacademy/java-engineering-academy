# Kubernetes RBAC

## Overview

Role-Based Access Control (RBAC) regulates access to Kubernetes resources based on the roles of individual users.

## Roles

### Role (Namespace-scoped)
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
  namespace: default
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log"]
  verbs: ["get", "watch", "list"]
- apiGroups: ["apps"]
  resources: ["deployments"]
  verbs: ["get", "list"]
```

### ClusterRole (Cluster-scoped)
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: secret-reader
rules:
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]
- apiGroups: [""]
  resources: ["nodes"]
  verbs: ["get", "watch", "list"]
```

## Bindings

### RoleBinding
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: read-pods
  namespace: default
subjects:
- kind: User
  name: jane
  apiGroup: rbac.authorization.k8s.io
- kind: ServiceAccount
  name: my-sa
  namespace: default
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
```

### ClusterRoleBinding
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: read-secrets
subjects:
- kind: User
  name: admin
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: secret-reader
  apiGroup: rbac.authorization.k8s.io
```

## Service Accounts

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: my-sa
  namespace: default
automountServiceAccountToken: true
```

## kubectl Commands

```bash
# Check roles
kubectl get roles
kubectl get clusterroles

# Check bindings
kubectl get rolebindings
kubectl get clusterrolebindings

# Check permissions
kubectl auth can-i list pods
kubectl auth can-i create deployments --namespace=production
```

## Best Practices

1. **Use least privilege** - Grant minimal required permissions
2. **Use namespaces** - Isolate permissions
3. **Use ServiceAccounts** - Don't use default SA
4. **Audit RBAC** - Review permissions regularly
5. **Use bindings carefully** - Avoid ClusterRoleBinding when possible
6. **Document permissions** - Add descriptions for complex configs
7. **Test permissions** - Verify access with kubectl auth
8. **Use aggregation** - Combine ClusterRoles
9. **Monitor access** - Track API server logs
10. **Implement audit logging** - Track access changes
