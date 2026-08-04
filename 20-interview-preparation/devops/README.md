# DevOps Interview Guide

Master DevOps interviews with comprehensive coverage of CI/CD, infrastructure, and SRE practices.

## Overview

DevOps interviews test your knowledge of software delivery practices, infrastructure management, and operational excellence.

## Key Topics

### 1. Continuous Integration/Continuous Deployment (CI/CD)

**Continuous Integration:**
- Frequent code commits
- Automated builds and tests
- Early bug detection
- Code quality gates

**Continuous Deployment:**
- Automated deployment pipeline
- Environment provisioning
- Release management
- Rollback strategies

**Pipeline Components:**
```
Code Commit → Build → Test → Deploy → Monitor
     │          │       │       │         │
     ▼          ▼       ▼       ▼         ▼
   Git       Maven   JUnit   Kubernetes  Prometheus
```

**Interview Questions:**
- How do you design a CI/CD pipeline?
- What metrics do you track for deployment frequency?
- How do you handle failed deployments?

### 2. Containerization and Orchestration

**Docker:**
- Container creation and management
- Dockerfile best practices
- Image optimization
- Multi-stage builds

```dockerfile
# Multi-stage build
FROM maven:3.8-openjdk-11 AS build
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Kubernetes:**
- Pod management
- Services and networking
- ConfigMaps and Secrets
- Horizontal pod autoscaling

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
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
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

**Interview Questions:**
- Explain the difference between Docker and Kubernetes
- How do you manage application configurations in Kubernetes?
- Describe Kubernetes networking concepts

### 3. Infrastructure as Code (IaC)

**Terraform:**
- Resource provisioning
- State management
- Modules and reusability
- Plan and apply workflow

```hcl
resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  tags = {
    Name = "WebServer"
  }
}

resource "aws_security_group" "web" {
  name        = "web-sg"
  description = "Security group for web servers"

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

**Ansible:**
- Configuration management
- Playbooks and roles
- Idempotent operations
- Inventory management

**Interview Questions:**
- How do you manage infrastructure state?
- Describe your approach to infrastructure testing
- How do you handle infrastructure drift?

### 4. Monitoring and Observability

**Prometheus:**
- Metrics collection
- Alerting rules
- Query language (PromQL)
- Dashboard visualization

```yaml
groups:
- name: example
  rules:
  - alert: HighErrorRate
    expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
    for: 5m
    labels:
      severity: critical
    annotations:
      summary: "High error rate detected"
```

**Grafana:**
- Dashboard creation
- Data source integration
- Alerting
- Team collaboration

**ELK Stack:**
- Log aggregation
- Full-text search
- Analytics and visualization
- Alerting

**Interview Questions:**
- How do you monitor application performance?
- Describe your approach to log management
- How do you set up effective alerting?

### 5. Site Reliability Engineering (SRE)

**Key Concepts:**
- Service Level Objectives (SLOs)
- Error budgets
- Toil reduction
- Incident management

**Monitoring:**
- Golden signals (latency, traffic, errors, saturation)
- RED method (Rate, Errors, Duration)
- USE method (Utilization, Saturation, Errors)

**Interview Questions:**
- How do you define and measure SLOs?
- Describe your incident management process
- How do you reduce operational toil?

### 6. Security Practices

**DevSecOps:**
- Security in CI/CD
- Vulnerability scanning
- Secret management
- Compliance automation

**Container Security:**
- Image scanning
- Runtime protection
- Network policies
- RBAC

**Interview Questions:**
- How do you integrate security into DevOps?
- Describe your approach to secret management
- How do you ensure container security?

## Common Interview Questions

### 1. How do you design a CI/CD pipeline?

**Answer Framework:**
1. **Source Control**: Git workflow (feature branches, PRs)
2. **Build**: Automated compilation and packaging
3. **Test**: Unit, integration, and end-to-end tests
4. **Security**: Vulnerability scanning and SAST/DAST
5. **Deploy**: Staged deployment (dev → staging → production)
6. **Monitor**: Post-deployment verification

### 2. How do you handle production incidents?

**Answer Framework:**
1. **Detection**: Monitoring alerts
2. **Triage**: Assess severity and impact
3. **Response**: Immediate mitigation
4. **Resolution**: Root cause fix
5. **Post-mortem**: Learn and improve
6. **Prevention**: Implement safeguards

### 3. Describe your approach to infrastructure testing.

**Answer Framework:**
1. **Unit Tests**: Individual resource validation
2. **Integration Tests**: Resource interaction testing
3. **Plan Review**: Manual inspection of changes
4. **Staged Deployment**: Test in non-production first
5. **Monitoring**: Verify expected behavior
6. **Rollback Testing**: Ensure recovery capability

### 4. How do you manage secrets in production?

**Answer Framework:**
1. **Secret Storage**: HashiCorp Vault, AWS Secrets Manager
2. **Access Control**: Least privilege principle
3. **Rotation**: Regular secret rotation
4. **Audit**: Access logging and monitoring
5. **Encryption**: At rest and in transit
6. **Emergency Access**: Break-glass procedures

### 5. How do you ensure high availability?

**Answer Framework:**
1. **Redundancy**: Multiple instances and regions
2. **Load Balancing**: Traffic distribution
3. **Health Checks**: Automated failure detection
4. **Failover**: Automatic recovery
5. **Circuit Breaking**: Prevent cascading failures
6. **Disaster Recovery**: Backup and restoration

## Best Practices

### 1. Version Control
- All code in version control
- Infrastructure as code
- GitOps for Kubernetes
- Tagging and release management

### 2. Automation
- Automated testing at all levels
- Automated deployments
- Automated rollbacks
- Automated scaling

### 3. Monitoring
- Comprehensive logging
- Metrics collection
- Distributed tracing
- Alerting and escalation

### 4. Security
- Shift-left security
- Regular vulnerability scanning
- Secret management
- Access control

### 5. Collaboration
- Shared responsibility
- Documentation
- Knowledge sharing
- Blameless post-mortems

## Study Plan

### Week 1-2: Fundamentals
- Learn Git workflows
- Study CI/CD concepts
- Practice Docker basics

### Week 3-4: Kubernetes
- Deploy applications
- Manage configurations
- Practice networking

### Week 5-6: Infrastructure as Code
- Learn Terraform
- Practice Ansible
- Understand state management

### Week 7-8: Monitoring and SRE
- Set up Prometheus/Grafana
- Learn alerting strategies
- Practice incident response

## Resources

### Books
- "The Site Reliability Workbook" by Google SRE Team
- "Infrastructure as Code" by Kief Morris
- "Kubernetes in Action" by Marko Lukša

### Online
- Kubernetes Documentation
- Terraform Documentation
- Prometheus Documentation
- Grafana Tutorials

### Certifications
- Certified Kubernetes Administrator (CKA)
- Terraform Associate
- AWS DevOps Engineer Professional