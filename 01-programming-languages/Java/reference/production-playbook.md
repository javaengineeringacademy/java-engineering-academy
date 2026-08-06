# Java Production Playbook

## Netflix

Netflix runs thousands of Java microservices on AWS. Their platform uses Spring Boot with Netflix OSS libraries (Ribbon, Hystrix, Zuul). Netflix's JVM tuning emphasizes low-latency garbage collection with G1GC and ZGC for their real-time streaming services. Their services handle millions of requests per second with sub-100ms latency requirements.

Netflix's production practices include custom JVM flags tuned per workload type. They use the Netty framework for high-performance networking. Netflix built Eureka for service discovery and Zuul for API gateway routing. Their deployment uses Spinnaker for continuous delivery with automated canary analysis. Netflix monitors GC pause times and adjusts heap sizes based on observed latency percentiles.

Netflix's Java services include: content recommendation engines, video streaming infrastructure, payment processing, and user management. Their monitoring tracks JVM metrics (heap usage, GC pauses, thread counts) alongside business metrics (streaming quality, user engagement). Netflix uses chaos engineering to test JVM resilience under failure conditions.

Netflix uses custom JVM builds optimized for their workload. Their GC tuning prioritizes low-latency for real-time services. Netflix monitors GC pause times using custom metrics. Their deployment pipeline uses Spinnaker with automated canary analysis. Netflix practices chaos engineering by terminating JVM instances.

Netflix's disaster recovery strategy includes multi-region deployments with automated failover. They regularly test failover procedures to ensure service availability. Netflix uses chaos engineering to validate JVM resilience under failure conditions. Their operational runbooks document recovery procedures for common failure scenarios.

## Amazon

Amazon's retail platform uses Java extensively for backend services. Their production emphasis is on cost efficiency at scale. Amazon's JVM tuning prioritizes throughput for batch processing and low-latency for customer-facing services. They use coroutines and virtual threads (Project Loom) for concurrent connection handling.

Amazon's production practices include custom JVM builds optimized for their hardware. They use DynamoDB for persistence with the AWS SDK's connection pooling. Amazon's deployment pipeline uses CodePipeline with blue-green deployments. Their monitoring uses CloudWatch with custom metrics for JVM health. Amazon practices chaos engineering by regularly terminating JVM instances to test resilience.

Amazon's Java services include: product catalog management, order processing, recommendation systems, and search infrastructure. Their JVM tuning varies by service: throughput-oriented for batch processing, latency-oriented for customer-facing APIs. Amazon uses custom JVM flags to optimize for their hardware and workload patterns.

Amazon uses custom JVM builds with performance patches. Their GC tuning prioritizes throughput for batch processing. Amazon monitors JVM health using CloudWatch. Their deployment pipeline uses CodePipeline with automated rollback. Amazon practices chaos engineering by terminating JVM instances.

Amazon's disaster recovery strategy includes multi-region deployments with automated failover. They regularly test failover procedures to ensure service availability. Amazon uses chaos engineering to validate JVM resilience under failure conditions. Their operational runbooks document recovery procedures for common failure scenarios.

## Google

Google uses Java for many internal services and Google Cloud offerings. Their production emphasis is on reliability and developer productivity. Google's JVM tuning uses their custom OpenJDK builds with performance patches. They use gRPC for inter-service communication with protocol buffers.

Google's production practices include detailed load testing before deployment. They use SRE principles with error budgets per service. Google's monitoring uses Monarch (internal time-series database) with alerting on SLO violations. Their deployment uses canary releases with automated rollback. Google practices progressive delivery with traffic shifting.

Google's Java services include: Cloud Platform offerings, internal tools, and data processing pipelines. Their JVM tuning uses custom OpenJDK builds with performance optimizations. Google uses gRPC for high-performance inter-service communication. Their monitoring tracks service-level objectives (SLOs) and error budgets.

Google uses custom OpenJDK builds with performance patches. Their GC tuning uses ZGC for low-latency services. Google monitors JVM health using Monarch. Their deployment uses canary releases with automated rollback. Google practices progressive delivery with traffic shifting.

Google's disaster recovery strategy includes multi-region deployments with automated failover. They regularly test failover procedures to ensure service availability. Google uses SRE principles with error budgets per service. Their operational runbooks document recovery procedures for common failure scenarios.

## LinkedIn

LinkedIn's production Java services handle professional networking features. Their backend uses Spring frameworks with custom middleware. LinkedIn's JVM tuning emphasizes throughput for batch processing (profile updates, search indexing) and low-latency for real-time features (messaging, notifications).

LinkedIn's production practices include custom GC tuning based on workload profiling. They use Kafka for event-driven communication between services. LinkedIn's deployment uses automated staging environments that mirror production. Their monitoring tracks end-to-end latency through the request lifecycle. LinkedIn practices capacity planning with growth projections.

LinkedIn's Java services include: member profile management, messaging infrastructure, search indexing, and recommendation systems. Their JVM tuning balances throughput for batch processing with latency for real-time features. LinkedIn uses Kafka for event-driven communication between Java services.

LinkedIn uses custom GC tuning based on workload profiling. Their monitoring tracks end-to-end latency. LinkedIn uses Kafka for event-driven communication. Their deployment uses automated staging environments. LinkedIn practices capacity planning with growth projections.

LinkedIn's disaster recovery strategy includes multi-region deployments with automated failover. They regularly test failover procedures to ensure service availability. LinkedIn uses chaos engineering to validate JVM resilience under failure conditions. Their operational runbooks document recovery procedures for common failure scenarios.

## Uber

Uber's Java services handle ride matching, pricing, and payment processing. Their production emphasis is on low-latency and high-throughput. Uber's JVM tuning uses ZGC for real-time services that require sub-millisecond pause times. They use custom serialization frameworks to reduce network overhead.

Uber's production practices include detailed chaos engineering (Chaos Kong for region failover). They use a custom service mesh for traffic management. Uber's monitoring uses a combination of StatsD and custom dashboards. Their deployment uses automated rollback based on error rate thresholds. Uber practices continuous profiling to identify performance bottlenecks.

Uber's Java services include: ride matching algorithms, dynamic pricing engines, payment processing, and driver management. Their JVM tuning uses ZGC for latency-sensitive services and G1GC for throughput-oriented services. Uber uses custom serialization to reduce network overhead in inter-service communication.

Uber uses ZGC for low-latency services. Their monitoring uses StatsD with custom dashboards. Uber practices chaos engineering for region failover. Their deployment uses automated rollback. Uber practices continuous profiling for performance optimization.

Uber's disaster recovery strategy includes multi-region deployments with automated failover. They regularly test failover procedures to ensure service availability. Uber uses chaos engineering to validate JVM resilience under failure conditions. Their operational runbooks document recovery procedures for common failure scenarios.

## Common Production Patterns

Java production deployments consistently emphasize the following. JVM tuning is workload-specific: throughput-oriented for batch processing, latency-oriented for real-time services. Garbage collector selection depends on latency requirements: G1GC for balanced, ZGC for ultra-low latency, Shenandoah as alternative.

Memory management includes: proper heap sizing (avoid excessive memory causing long GC pauses), off-heap memory for large caches, and direct byte buffers for NIO. Thread pool tuning prevents thread explosion: bounded queues, proper rejection handlers, and monitoring of pool metrics.

Production monitoring tracks: GC pause times (p50, p99, p999), heap usage, thread counts, connection pool utilization, and request latency percentiles. Profiling tools (async-profiler, JProfiler) identify hotspots. Load testing validates capacity before production deployment.

Operational runbooks cover: OOM kills (heap dumps, memory leak analysis), high CPU (thread dumps, deadlock detection), connection pool exhaustion (pool sizing, leak detection), and class loading issues (classpath verification). Disaster recovery includes data backup, state restoration, and traffic failover procedures.

Production Java services use containerized deployment with Kubernetes. JVM flags are configured via environment variables. Health checks verify JVM and application health. Rolling deployments ensure zero-downtime updates. Blue-green deployments provide instant rollback capability. Feature flags enable progressive feature rollout.

Java disaster recovery strategies include: multi-region deployments with automated failover, JVM crash analysis using heap dumps and thread dumps, and chaos engineering for resilience testing. Production runbooks document recovery procedures for OOM kills, high CPU, connection pool exhaustion, and class loading issues. Regular disaster recovery testing validates backup and restoration procedures.

Java production deployments require careful JVM tuning. Garbage collector selection depends on latency requirements: G1GC for balanced, ZGC for ultra-low latency, Shenandoah as alternative. Memory management includes proper heap sizing, off-heap memory for large caches, and direct byte buffers for NIO. Thread pool tuning prevents thread explosion with bounded queues and proper rejection handlers.

Production monitoring tracks GC pause times (p50, p99, p999), heap usage, thread counts, connection pool utilization, and request latency percentiles. Profiling tools (async-profiler, JProfiler) identify hotspots. Load testing validates capacity before production deployment. JVM flags are configured via environment variables for containerized deployments. Regular performance reviews ensure JVM tuning remains optimal.
