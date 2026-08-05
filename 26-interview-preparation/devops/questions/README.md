# DevOps Interview Questions

Comprehensive guide to DevOps interview questions and answers.

## CI/CD Questions

### 1. What is Continuous Integration?

**Answer:** Continuous Integration (CI) is a development practice where developers frequently merge code changes into a central repository. After each merge, automated builds and tests run to detect integration errors early.

**Key Points:**
- Frequent code commits (daily or more)
- Automated build and test processes
- Early bug detection
- Code quality gates
- Fast feedback loop

**Example:**
```yaml
# GitHub Actions CI Pipeline
name: CI Pipeline
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v1
      with:
        java-version: '11'
    - name: Build with Maven
      run: mvn clean package
    - name: Run Tests
      run: mvn test
```

### 2. What is Continuous Deployment?

**Answer:** Continuous Deployment (CD) is the practice of automatically deploying code changes to production after passing all stages of the pipeline. It extends Continuous Delivery by removing manual approval gates.

**Key Points:**
- Automated deployment to production
- No manual intervention required
- Rapid release cycles
- Feature flags for controlled rollouts
- Automated rollback capabilities

**Example:**
```yaml
# Kubernetes Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:latest
        ports:
        - containerPort: 8080
```

### 3. How do you handle failed deployments?

**Answer:** I use a multi-layered approach:

1. **Automated Rollback**: If health checks fail, automatically revert to previous version
2. **Feature Flags**: Disable problematic features without full rollback
3. **Canary Deployments**: Deploy to small percentage first, then expand
4. **Blue-Green Deployments**: Maintain two identical environments for instant switching
5. **Incident Response**: Clear process for investigating and resolving issues

**Example:**
```yaml
# Canary Deployment Strategy
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: my-app
spec:
  hosts:
  - my-app
  http:
  - route:
    - destination:
        host: my-app
        subset: stable
      weight: 90
    - destination:
        host: my-app
        subset: canary
      weight: 10
```

## Docker Questions

### 4. What is the difference between a Docker image and a container?

**Answer:**
- **Docker Image**: A read-only template with instructions for creating a container. It's built from a Dockerfile and stored in a registry.
- **Docker Container**: A runnable instance of an image. It's isolated, has its own filesystem, and can be started/stopped.

**Key Differences:**
| Aspect | Image | Container |
|--------|-------|-----------|
| State | Read-only | Read-write |
| Purpose | Template | Runtime instance |
| Storage | Registry | Local filesystem |
| Lifecycle | Persistent | Temporary |

### 5. How do you optimize Docker images?

**Answer:**
1. **Multi-stage builds**: Separate build and runtime stages
2. **Smaller base images**: Use Alpine or distroless images
3. **Layer optimization**: Order instructions from least to most frequently changing
4. **Remove unnecessary files**: Clean up caches and build artifacts
5. **Use .dockerignore**: Exclude unnecessary files from context

**Example:**
```dockerfile
# Multi-stage build
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build target/*.jar app.jar
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Kubernetes Questions

### 6. Explain Kubernetes architecture.

**Answer:** Kubernetes follows a master-worker architecture:

**Control Plane (Master):**
- **API Server**: Entry point for all REST commands
- **etcd**: Distributed key-value store for cluster data
- **Scheduler**: Assigns pods to nodes
- **Controller Manager**: Maintains desired state

**Worker Nodes:**
- **kubelet**: Agent ensuring containers run in pods
- **kube-proxy**: Network proxy maintaining network rules
- **Container Runtime**: Runs containers (Docker, containerd)

```
┌─────────────────────────────────────────────────┐
│              Control Plane                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │API Server│ │  etcd    │ │Scheduler │       │
│  └──────────┘ └──────────┘ └──────────┘       │
│  ┌──────────────────────────────────────┐      │
│  │       Controller Manager             │      │
│  └──────────────────────────────────────┘      │
└─────────────────────────────────────────────────┘
                      │
┌─────────────────────┼───────────────────────────┐
│                     │                           │
│  ┌──────────────────▼──────────────────┐       │
│  │              Worker Node            │       │
│  │  ┌──────────┐  ┌──────────┐        │       │
│  │  │  kubelet │  │kube-proxy│        │       │
│  │  └──────────┘  └──────────┘        │       │
│  │  ┌────────────────────────────┐    │       │
│  │  │         Pods               │    │       │
│  │  │  ┌─────┐ ┌─────┐ ┌─────┐ │    │       │
│  │  │  │ Pod │ │ Pod │ │ Pod │ │    │       │
│  │  │  └─────┘ └─────┘ └─────┘ │    │       │
│  │  └────────────────────────────┘    │       │
│  └─────────────────────────────────────┘       │
└─────────────────────────────────────────────────┘
```

### 7. What is the difference between a Service and an Ingress?

**Answer:**
- **Service**: Exposes a set of pods as a network service with a stable IP address and DNS name
- **Ingress**: Manages external access to services in the cluster, typically HTTP routing

**Service Types:**
- ClusterIP: Internal only
- NodePort: Exposes on each node's IP
- LoadBalancer: Exposes via cloud provider's load balancer

**Ingress Features:**
- HTTP/HTTPS routing
- SSL termination
- Name-based virtual hosting
- Path-based routing

### 8. How do you manage application configurations in Kubernetes?

**Answer:** I use multiple approaches based on sensitivity:

**ConfigMaps:**
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  DATABASE_URL: "jdbc:mysql://db:3306/mydb"
  LOG_LEVEL: "INFO"
```

**Secrets:**
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
type: Opaque
data:
  DB_PASSWORD: cGFzc3dvcmQxMjM=
  API_KEY: c2VjcmV0LWFwaS1rZXk=
```

**Environment Variables:**
```yaml
env:
- name: DATABASE_URL
  valueFrom:
    configMapKeyRef:
      name: app-config
      key: DATABASE_URL
- name: DB_PASSWORD
  valueFrom:
    secretKeyRef:
      name: app-secrets
      key: DB_PASSWORD
```

## Infrastructure as Code Questions

### 9. How do you manage infrastructure state?

**Answer:** I use Terraform with remote state management:

**State Management:**
- Remote state storage (S3, GCS, Azure Blob)
- State locking (DynamoDB, GCS)
- State versioning
- Workspace separation

**Best Practices:**
- Never store state in version control
- Use remote backends for team collaboration
- Enable state locking to prevent conflicts
- Separate state for different environments
- Use data sources for existing resources

**Example:**
```hcl
terraform {
  backend "s3" {
    bucket         = "my-terraform-state"
    key            = "prod/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-lock"
    encrypt        = true
  }
}
```

### 10. How do you handle infrastructure drift?

**Answer:** Infrastructure drift occurs when actual infrastructure differs from the desired state defined in code.

**Detection:**
- Regular `terraform plan` runs
- Automated drift detection in CI/CD
- Monitoring and alerting

**Prevention:**
- GitOps workflows
- Manual changes discouraged
- Change approval processes
- Documentation of exceptions

**Remediation:**
- Review and understand drift
- Import existing resources if needed
- Update code to reflect actual state
- Apply changes incrementally

## Monitoring Questions

### 11. How do you monitor application performance?

**Answer:** I use the three pillars of observability:

**Metrics:**
- Application metrics (request rate, error rate, latency)
- Infrastructure metrics (CPU, memory, disk, network)
- Business metrics (conversion rate, revenue)
- Tools: Prometheus, Datadog, CloudWatch

**Logs:**
- Application logs
- Infrastructure logs
- Access logs
- Tools: ELK Stack, Fluentd, Loki

**Traces:**
- Distributed tracing
- Request flow
- Latency analysis
- Tools: Jaeger, Zipkin, X-Ray

**Example Prometheus Configuration:**
```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "alert_rules.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

scrape_configs:
  - job_name: 'my-app'
    static_configs:
      - targets: ['my-app:8080']
```

### 12. How do you set up effective alerting?

**Answer:** I follow these principles:

**Alert Design:**
- Actionable alerts only
- Clear severity levels
- Specific thresholds
- Useful annotations

**Alert Routing:**
- PagerDuty for critical alerts
- Slack for warnings
- Email for informational
- Runbooks for response

**Example Alert Rule:**
```yaml
groups:
- name: application
  rules:
  - alert: HighErrorRate
    expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
    for: 5m
    labels:
      severity: critical
    annotations:
      summary: "High error rate detected"
      description: "Error rate is {{ $value }} per second"
      runbook_url: "https://wiki/runbooks/high-error-rate"
```

## Security Questions

### 13. How do you integrate security into DevOps?

**Answer:** I implement DevSecOps practices:

**Shift-Left Security:**
- Security in design phase
- Threat modeling
- Security requirements
- Code review for security

**CI/CD Security:**
- SAST (Static Application Security Testing)
- DAST (Dynamic Application Security Testing)
- Dependency scanning
- Container scanning

**Runtime Security:**
- Runtime protection
- Network policies
- RBAC
- Audit logging

**Example Security Scanning:**
```yaml
# SAST with SonarQube
- name: SonarQube Scan
  uses: sonarcloud/sonarcloud-github-action@master
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}

# Container Scanning with Trivy
- name: Run Trivy vulnerability scanner
  uses: aquasecurity/trivy-action@master
  with:
    image-ref: 'my-app:latest'
    format: 'table'
    exit-code: '1'
    severity: 'CRITICAL,HIGH'
```

### 14. Describe your approach to secret management.

**Answer:** I use a multi-layered approach:

**Secret Storage:**
- HashiCorp Vault for centralized management
- Cloud provider secrets managers (AWS Secrets Manager, GCP Secret Manager)
- Kubernetes Secrets for container orchestration

**Access Control:**
- Least privilege principle
- Role-based access control
- Just-in-time access
- Regular access reviews

**Secret Rotation:**
- Automated rotation policies
- Zero-downtime rotation
- Audit logging
- Emergency access procedures

**Example Vault Configuration:**
```hcl
path "secret/data/myapp/*" {
  capabilities = ["read", "list"]
}

path "secret/data/myapp/config" {
  capabilities = ["read"]
  required_parameters = ["version"]
}
```

## Incident Management Questions

### 15. How do you handle production incidents?

**Answer:** I follow a structured incident management process:

**Detection:**
- Monitoring alerts
- User reports
- Automated detection
- Canary deployments

**Triage:**
- Assess severity (P0-P4)
- Determine impact
- Assign incident commander
- Open communication channel

**Response:**
- Mitigate immediately
- Communicate status
- Preserve evidence
- Document actions

**Resolution:**
- Root cause analysis
- Fix implementation
- Verification
- Monitoring

**Post-Mortem:**
- Blameless review
- Timeline analysis
- Action items
- Follow-up

**Example Incident Response Template:**
```markdown
# Incident Report

## Summary
- **Severity**: P1
- **Duration**: 45 minutes
- **Impact**: 50% of users unable to access service
- **Root Cause**: Database connection pool exhaustion

## Timeline
- 14:00 - Alert triggered
- 14:05 - Incident commander assigned
- 14:10 - Root cause identified
- 14:20 - Fix implemented
- 14:45 - Service restored

## Root Cause
Database connection pool was exhausted due to increased traffic and slow queries.

## Action Items
- [ ] Increase connection pool size
- [ ] Optimize slow queries
- [ ] Add connection pool monitoring
- [ ] Implement circuit breaker
```

## Best Practices Summary

### 1. Automation
- Automate repetitive tasks
- Use Infrastructure as Code
- Implement comprehensive testing
- Automate deployments

### 2. Monitoring
- Monitor all critical metrics
- Set up meaningful alerts
- Use distributed tracing
- Centralize logging

### 3. Security
- Shift security left
- Scan for vulnerabilities
- Manage secrets properly
- Implement access controls

### 4. Collaboration
- Share knowledge
- Document processes
- Blameless post-mortems
- Continuous learning

### 5. Reliability
- Design for failure
- Implement circuit breakers
- Test disaster recovery
- Monitor error budgets