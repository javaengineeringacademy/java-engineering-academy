# Kubernetes Best Practices

## 1. Use Declarative Configuration

Store all manifests in version control. Never make manual changes to production. Use GitOps tools like ArgoCD or Flux for automated deployments.

## 2. Set Resource Requests and Limits

Always define CPU and memory requests and limits. This ensures proper scheduling and prevents resource starvation.

```yaml
resources:
  requests:
    cpu: "250m"
    memory: "256Mi"
  limits:
    cpu: "500m"
    memory: "512Mi"
```

## 3. Implement Health Checks

Use readiness and liveness probes to detect and recover from failures.

```yaml
readinessProbe:
  httpGet:
    path: /healthz
    port: 8080
  initialDelaySeconds: 5
  periodSeconds: 10
livenessProbe:
  httpGet:
    path: /healthz
    port: 8080
  initialDelaySeconds: 15
  periodSeconds: 20
```

## 4. Use Namespaces for Isolation

Separate environments, teams, and applications with namespaces. Apply resource quotas and network policies per namespace.

## 5. Implement RBAC

Use least-privilege access. Create roles per namespace and per team. Never use cluster-admin for applications.

## 6. Use Labels and Selectors

Apply consistent labeling strategy for all resources. Use labels for environment, app, version, and team.

```yaml
labels:
  app: my-app
  env: production
  version: v1.2.3
  team: platform
```

## 7. Use ConfigMaps and Secrets

Externalize configuration from container images. Use ConfigMaps for non-sensitive data and Secrets (or external stores) for credentials.

## 8. Implement Network Policies

Default deny all traffic. Allow only necessary communication between services. This reduces attack surface.

## 9. Use Pod Disruption Budgets

Protect applications during voluntary disruptions (node drains, upgrades).

```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: my-app-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: my-app
```

## 10. Use Anti-Affinity Rules

Spread Pods across nodes for high availability.

```yaml
affinity:
  podAntiAffinity:
    preferredDuringSchedulingIgnoredDuringExecution:
    - weight: 100
      podAffinityTerm:
        labelSelector:
          matchExpressions:
          - key: app
            operator: In
            values:
            - my-app
        topologyKey: kubernetes.io/hostname
```

## 11. Implement Logging and Monitoring

Deploy Prometheus, Grafana, and centralized logging. Use structured logging for applications.

## 12. Use Helm for Package Management

Package complex applications as Helm charts. Use different values files for environments.

## 13. Enable Audit Logging

Track API access and changes for security and compliance.

## 14. Scan Images for Vulnerabilities

Integrate image scanning into CI/CD pipeline. Block images with critical vulnerabilities.

## 15. Use Read-Only Root Filesystems

Prevent runtime modifications to container filesystems.

```yaml
securityContext:
  readOnlyRootFilesystem: true
```

## 16. Run as Non-Root

Never run containers as root in production.

```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
```

## 17. Use Graceful Shutdown

Handle SIGTERM signals and implement preStop hooks.

```yaml
lifecycle:
  preStop:
    exec:
      command: ["/bin/sh", "-c", "sleep 15"]
terminationGracePeriodSeconds: 30
```

## 18. Implement Backup and Restore

Use Velero for cluster backups. Test restore procedures regularly.

## 19. Document Everything

Maintain runbooks for common operations. Document architecture and deployment processes.

## 20. Regular Upgrades

Keep Kubernetes and dependencies up to date. Follow upgrade best practices with canary deployments.

## 21. Use Init Containers

Use init containers for setup tasks and dependency checks before main container starts.

## 22. Optimize Container Images

Use minimal base images (Alpine, distroless). Reduce image size for faster pulls and smaller attack surface.

## 23. Implement Rate Limiting

Protect services with rate limiting and circuit breakers.

## 24. Use External Secret Stores

For production, use HashiCorp Vault, AWS Secrets Manager, or Sealed Secrets instead of Kubernetes Secrets.

## 25. Test in Production-Like Environments

Use staging clusters that mirror production. Test deployments and scaling in staging first.
