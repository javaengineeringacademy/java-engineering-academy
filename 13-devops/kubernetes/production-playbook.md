# Kubernetes Production Playbook

## Google

Google developed Kubernetes based on their internal Borg and Omega systems. Their production use spans thousands of clusters serving Search, YouTube, Gmail, and Cloud services. Google's approach emphasizes cluster federation for multi-region deployments and custom schedulers optimized for specific workloads.

Google's production practices include resource classes (pod types with predefined resource profiles), bin-packing optimization for cost efficiency, and custom controllers for workload-specific scheduling. Their monitoring uses Borgmon (predecessor to Prometheus) with per-service SLO tracking. Google's approach to cluster upgrades involves creating new node pools and draining old ones with zero-downtime rolling updates.

Google's Kubernetes usage includes: container orchestration for microservices, batch processing for data analytics, ML model serving for recommendation systems, and infrastructure for Google Cloud offerings. Their production clusters run thousands of nodes with custom schedulers for workload-specific optimization. Google uses cluster autoscaling to handle traffic spikes.

Google monitors Kubernetes cluster health with custom dashboards. Their alerting system considers business impact: search serving has higher priority than batch processing. Google uses node auto-scaling based on pending pod metrics. Their capacity planning includes traffic patterns and growth projections. Google practices chaos engineering to validate resilience.

Google's disaster recovery strategy includes multi-region cluster deployments with automated failover. They regularly test failover procedures to ensure service availability. Google uses cluster autoscaling to handle traffic spikes. Their operational runbooks document recovery procedures for common failure scenarios.

## Spotify

Spotify runs over 200 Kubernetes clusters serving 400+ million users. Their platform team (Backstage originators) built an internal developer portal for self-service Kubernetes deployments. Spotify uses a microservices architecture with hundreds of services deployed on Kubernetes.

Spotify's production practices include progressive delivery with canary deployments and automated rollback. They use Custom Resource Definitions (CRDs) for platform-specific abstractions. Their cost management uses Kubecost for per-team resource attribution. Spotify implemented a multi-cluster service mesh using Istio for cross-cluster communication. Their CI/CD pipeline uses GitOps with Argo CD for declarative deployments.

Spotify's Kubernetes usage includes: microservices deployment for music streaming, data pipeline orchestration for recommendation algorithms, and developer platform infrastructure. Their Backstage platform provides self-service Kubernetes deployments with built-in observability and cost tracking. Spotify uses node affinity to separate ML workloads from general services.

Spotify monitors Kubernetes cluster health with custom dashboards. Their alerting system considers business impact: music streaming has higher priority than analytics. Spotify uses progressive delivery with canary deployments. Their capacity planning includes user growth projections and seasonal traffic patterns. Spotify practices chaos engineering to validate resilience.

Spotify's disaster recovery strategy includes multi-cluster deployments with automated failover. They regularly test failover procedures to ensure service availability. Spotify uses Istio for cross-cluster service mesh. Their operational runbooks document recovery procedures for common failure scenarios.

## Pinterest

Pinterest uses Kubernetes for their recommendation engine and search infrastructure. Their deployment manages thousands of nodes across multiple availability zones. Pinterest built a custom scheduling framework that considers data locality for ML model serving.

Pinterest's production practices include node auto-scaling based on pending pod metrics. They use PodDisruptionBudgets to ensure availability during cluster maintenance. Their monitoring stack combines Prometheus with custom metrics for recommendation quality tracking. Pinterest implemented a cost-aware scheduling strategy that preferentially schedules workloads on spot/preemptible instances. Their disaster recovery includes automated cluster rebuild procedures.

Pinterest's Kubernetes usage includes: ML model serving for recommendation systems, search infrastructure for content discovery, and data pipeline orchestration. Their custom scheduler optimizes for data locality and GPU utilization. Pinterest uses node affinity to separate ML workloads from general services.

Pinterest monitors Kubernetes cluster health with custom dashboards. Their alerting system considers business impact: recommendation serving has higher priority than batch processing. Pinterest uses node auto-scaling based on pending pod metrics. Their capacity planning includes traffic patterns and growth projections. Pinterest practices chaos engineering to validate resilience.

Pinterest's disaster recovery strategy includes multi-AZ deployments with automated failover. They regularly test failover procedures to ensure service availability. Pinterest uses custom scheduling frameworks for workload optimization. Their operational runbooks document recovery procedures for common failure scenarios.

## Shopify

Shopify runs a massive Kubernetes deployment serving millions of merchants. Their platform handles Black Friday traffic spikes with 10x normal load. Shopify uses custom resource types to abstract Kubernetes complexity from developers. Their deployment includes a self-service platform for team-owned infrastructure.

Shopify's production practices include capacity forecasting using historical traffic patterns. They implement progressive canary deployments with automated analysis. Their security posture uses network policies to enforce microsegmentation. Shopify built a custom autoscaler that considers both Kubernetes metrics and business metrics (orders per second).

Shopify's Kubernetes usage includes: merchant-facing services, payment processing infrastructure, and developer platform. Their custom CRDs simplify application deployment and configuration. Shopify uses cluster autoscaling to handle traffic spikes and implements PodDisruptionBudgets for zero-downtime maintenance. Their monitoring tracks business metrics alongside Kubernetes metrics.

Shopify monitors Kubernetes cluster health with custom dashboards. Their alerting system considers business impact: payment processing has higher priority than merchant services. Shopify uses progressive delivery with canary deployments. Their capacity planning includes seasonal traffic patterns and growth projections. Shopify practices chaos engineering to validate resilience.

Shopify's disaster recovery strategy includes multi-AZ deployments with automated failover. They regularly test failover procedures to ensure service availability. Shopify uses custom autoscalers for business metric-driven scaling. Their operational runbooks document recovery procedures for common failure scenarios.

## Slack

Slack uses Kubernetes for their real-time messaging platform. Their deployment handles millions of concurrent WebSocket connections. Slack uses custom controllers for managing their persistent connection infrastructure. Their pod lifecycle management includes graceful shutdown handling for in-flight messages.

Slack's production practices include topology spread constraints for zone-aware deployment. They use node affinity to separate latency-sensitive workloads from batch jobs. Their monitoring tracks connection establishment latency and message delivery SLAs. Slack implemented a custom admission webhook for enforcing resource requests and limits.

Slack's Kubernetes usage includes: WebSocket server deployment for real-time messaging, background job processing, and API gateway infrastructure. Their custom controllers manage connection lifecycle and graceful shutdown. Slack uses topology spread constraints to ensure zone redundancy for critical services.

Slack monitors Kubernetes cluster health with custom dashboards. Their alerting system considers business impact: message delivery has higher priority than background jobs. Slack uses node auto-scaling based on connection counts. Their capacity planning includes user growth projections and traffic patterns. Slack practices chaos engineering to validate resilience.

Slack's disaster recovery strategy includes multi-AZ deployments with automated failover. They regularly test failover procedures to ensure service availability. Slack uses topology spread constraints for zone redundancy. Their operational runbooks document recovery procedures for common failure scenarios.

## Common Production Patterns

Kubernetes production deployments consistently emphasize the following. Cluster sizing accounts for system pods (kube-system, monitoring, logging) consuming 15-20% of resources. Node pools separate workload types (general, memory-optimized, GPU). Resource quotas per namespace prevent noisy neighbor problems.

Security practices include: Pod Security Standards (restricted profile), network policies for microsegmentation, RBAC with least-privilege principles, secrets encryption at rest, and image vulnerability scanning. Runtime security uses Falco or Sysdig for syscall monitoring.

Operational runbooks cover: node failure (automatic eviction, node drain), pod eviction (priority classes, disruption budgets), control plane failures (HA etcd, API server redundancy), and certificate rotation. Chaos engineering validates resilience through pod killing, node termination, and network partition injection.

Monitoring and observability use Prometheus for metrics, Loki or EFK for logging, Jaeger for tracing, and custom dashboards for SLA tracking. Alerting covers: node NotReady, pod CrashLoopBackOff, etcd latency, and scheduler throughput. Capacity planning uses historical metrics to forecast resource needs.

Production Kubernetes clusters use managed services (EKS, GKE, AKS) for control plane availability. Multi-cluster deployments provide geographic redundancy. GitOps workflows ensure configuration consistency across environments. Regular disaster recovery testing validates backup and restoration procedures.

Kubernetes disaster recovery strategies include: multi-region cluster deployments with automated failover, etcd backup and restoration procedures, and chaos engineering for resilience testing. Production runbooks document recovery procedures for node failures, pod evictions, control plane failures, and certificate rotation. Regular disaster recovery testing validates backup and restoration procedures.

Kubernetes production clusters require careful resource management. Resource quotas prevent noisy neighbor problems. Node pools separate workload types (general, memory-optimized, GPU). PodDisruptionBudgets ensure availability during maintenance. The `kubectl top` command monitors resource usage. Capacity planning uses historical metrics to forecast resource needs.

Security practices include Pod Security Standards, network policies for microsegmentation, RBAC with least-privilege principles, secrets encryption at rest, and image vulnerability scanning. Runtime security uses Falco or Sysdig for syscall monitoring. Regular security audits validate compliance with organizational policies. Continuous monitoring ensures security posture remains effective.
