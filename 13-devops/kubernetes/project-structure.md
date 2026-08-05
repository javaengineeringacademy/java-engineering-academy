# Kubernetes Project Structure

## Standard Manifests Layout

Organize Kubernetes manifests by environment and resource type for clarity and maintainability.

```
kubernetes/
├── base/                        # Base configuration
│   ├── namespace.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   └── hpa.yaml
├── overlays/                    # Environment overlays
│   ├── dev/
│   │   ├── kustomization.yaml
│   │   ├── deployment-patch.yaml
│   │   └── resource-quota.yaml
│   ├── staging/
│   │   ├── kustomization.yaml
│   │   ├── deployment-patch.yaml
│   │   └── resource-quota.yaml
│   └── production/
│       ├── kustomization.yaml
│       ├── deployment-patch.yaml
│       ├── resource-quota.yaml
│       └── network-policy.yaml
├── helm/                        # Helm chart
│   └── my-app/
│       ├── Chart.yaml
│       ├── values.yaml
│       ├── values-dev.yaml
│       ├── values-staging.yaml
│       ├── values-production.yaml
│       └── templates/
│           ├── deployment.yaml
│           ├── service.yaml
│           ├── ingress.yaml
│           ├── configmap.yaml
│           ├── secret.yaml
│           ├── hpa.yaml
│           ├── _helpers.tpl
│           ├── NOTES.txt
│           └── tests/
│               └── test-connection.yaml
├── kustomize/                   # Kustomize configuration
│   ├── base/
│   │   ├── kustomization.yaml
│   │   ├── deployment.yaml
│   │   └── service.yaml
│   └── overlays/
│       ├── dev/
│       ├── staging/
│       └── production/
├── scripts/                     # Deployment scripts
│   ├── deploy.sh
│   ├── rollback.sh
│   └── cleanup.sh
└── docs/                        # Documentation
    ├── architecture.md
    └── runbook.md
```

## Helm Chart Structure

### Chart.yaml

```yaml
apiVersion: v2
name: my-app
description: My application Helm chart
type: application
version: 1.0.0
appVersion: "1.0.0"
maintainers:
  - name: Team
    email: team@example.com
dependencies:
  - name: postgresql
    version: "12.x.x"
    repository: "https://charts.bitnami.com/bitnami"
    condition: postgresql.enabled
```

### values.yaml

```yaml
replicaCount: 3

image:
  repository: my-app
  pullPolicy: IfNotPresent
  tag: "latest"

service:
  type: ClusterIP
  port: 80

ingress:
  enabled: true
  className: nginx
  annotations:
    cert-manager.io/cluster-issuer: letsencrypt-prod
  hosts:
    - host: my-app.example.com
      paths:
        - path: /
          pathType: Prefix
  tls:
    - secretName: my-app-tls
      hosts:
        - my-app.example.com

resources:
  requests:
    cpu: 250m
    memory: 256Mi
  limits:
    cpu: 500m
    memory: 512Mi

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 70
```

## Kustomize Structure

### kustomization.yaml

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - deployment.yaml
  - service.yaml
  - configmap.yaml

namespace: production

commonLabels:
  app: my-app
  env: production

patches:
  - target:
      kind: Deployment
      name: my-app
    patch: |
      - op: replace
        path: /spec/replicas
        value: 5

images:
  - name: my-app
    newName: my-app
    newTag: v1.2.3
```

## File Naming Conventions

- Use lowercase with hyphens: deployment.yaml, not Deployment.yaml
- Group by resource type: deployments/, services/, configmaps/
- Use descriptive names: nginx-deployment.yaml, not deploy.yaml
- Separate environment configs: dev/, staging/, production/
- Include README.md for each directory

## Manifest Organization Patterns

### By Resource Type

```
resources/
├── deployments/
├── services/
├── configmaps/
├── secrets/
├── ingress/
├── rbac/
└── networking/
```

### By Application

```
apps/
├── frontend/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ingress.yaml
├── backend/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── configmap.yaml
└── database/
    ├── statefulset.yaml
    ├── service.yaml
    └── pvc.yaml
```

## Best Practices

1. Use Kustomize or Helm for environment management
2. Separate base and overlay configurations
3. Store secrets in external secret managers
4. Include resource requests and limits in all deployments
5. Add health checks (readiness and liveness probes)
6. Document custom resources and annotations
7. Use .gitignore to exclude generated files
8. Implement RBAC for namespace isolation
