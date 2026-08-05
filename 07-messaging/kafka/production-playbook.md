# Kafka Production Playbook

## Netflix

Netflix operates one of the largest Kafka deployments, processing over 700 billion messages daily across thousands of topics. Their architecture uses Kafka as the central nervous system for real-time data pipelines. Netflix built Cruise Control, an automated partition rebalancing tool that monitors broker load and moves partitions to maintain even distribution without manual intervention.

Netflix uses MirrorMaker 2 for cross-region replication between US-East and US-West clusters. Their consumer groups implement custom partition assignment strategies optimized for their workload patterns. Netflix deployed tiered storage to offload cold data to S3, reducing broker disk requirements by 60%. Their monitoring stack combines Burrow (consumer lag monitoring) with custom dashboards tracking ISR shrink rates and under-replicated partitions.

Netflix's Kafka clusters use dedicated broker instances with NVMe SSDs for write performance. Their retention policy is 7 days for most topics, with longer retention for critical event streams. Netflix uses schema evolution with Avro and Confluent Schema Registry to maintain backward compatibility. Their disaster recovery includes automated failover testing and cross-region replication validation.

Netflix monitors end-to-end latency from producer to consumer. Their alerting system considers business impact: streaming quality events have higher priority than analytics events. Netflix uses Kafka Streams for real-time recommendation updates. Their capacity planning uses historical traffic patterns to forecast growth.

Netflix's disaster recovery strategy includes cross-region replication with automated failover testing. They regularly test failover procedures to ensure data durability. Netflix uses consumer group lag monitoring to detect processing bottlenecks. Their operational runbooks document recovery procedures for common failure scenarios.

## Uber

Uber's Kafka infrastructure handles over 50 trillion messages per day across 100+ clusters globally. They developed Uber Kafka (UKafka), a managed Kafka service that automates cluster provisioning, upgrades, and capacity planning. Uber uses a hub-and-spoke architecture: regional hub clusters replicate to spoke clusters at the edge.

Uber built KafkaT (Kafka Topic management tool) for self-service topic creation with enforced schemas. Their consumer offset management uses a custom strategy that considers partition locality to minimize cross-datacenter traffic. Uber implemented rack-aware replication to survive zone failures. Their Schema Registry enforces Avro schema evolution rules, preventing breaking changes from reaching production consumers.

Uber's monitoring tracks end-to-end latency from producer to consumer. Their alerting system considers business impact: ride-matching events have higher priority than analytics events. Uber uses Kafka Streams for real-time fare calculation and driver matching. Their capacity planning uses historical traffic patterns to forecast growth.

Uber's Kafka deployment includes: real-time ride matching, dynamic pricing, driver location updates, and payment processing. Their topic partitioning strategy ensures related events (same ride) are processed in order. Uber monitors consumer lag per partition to identify processing bottlenecks. Their operational metrics include broker CPU utilization, disk I/O, and network throughput.

Uber's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. Uber uses schema evolution with Avro and Confluent Schema Registry. Their operational runbooks document recovery procedures for common failure scenarios.

## LinkedIn

LinkedIn, where Kafka originated, operates clusters with billions of messages per day. They developed Kafka Connect for standardized data ingestion from databases, logs, and custom systems. LinkedIn's Kafka deployment uses hardware with NVMe SSDs for the write path and HDDs for retention.

LinkedIn built Burrow, an external consumer lag monitoring system that evaluates consumer health using a sliding window algorithm rather than simple lag thresholds. Their MirrorMaker-based replication uses topic filters and quota management to control cross-datacenter bandwidth. LinkedIn's Kafka SLA guarantees 99.99% availability per cluster, achieved through multi-rack deployments and automated failover.

LinkedIn's Kafka usage includes event sourcing for member activity tracking, real-time feed updates, and search indexing. Their topic naming convention follows a structured format: {team}.{domain}.{event-type}. LinkedIn uses Kafka for data integration between Hadoop and real-time systems. Their operational metrics include broker CPU utilization, disk I/O, and network throughput.

LinkedIn uses Kafka for data integration between analytical and operational systems. Their Kafka Connect deployments handle database CDC, log aggregation, and metrics collection. LinkedIn monitors Kafka cluster health using custom dashboards. Their capacity planning includes seasonal traffic patterns and growth projections.

LinkedIn's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. LinkedIn uses schema evolution with Avro and Confluent Schema Registry. Their operational runbooks document recovery procedures for common failure scenarios.

## Airbnb

Airbnb uses Kafka for event-driven architectures across their marketplace platform. Their deployment processes millions of events for search ranking, pricing, and notifications. Airbnb built a schema evolution pipeline that validates backward and forward compatibility before allowing schema changes.

Airbnb's Kafka infrastructure uses a centralized platform team that provides Kafka-as-a-Service to engineering teams. Their monitoring uses custom metrics for end-to-end latency tracking. Airbnb implemented Kafka exactly-once semantics for their critical financial transactions (payments, payouts) using idempotent producers and transactional consumers. Their disaster recovery strategy includes cross-region replication with automated failover testing.

Airbnb uses Kafka for pricing optimization, listing updates, and booking events. Their topic partitioning strategy ensures related events (same listing) are processed in order. Airbnb monitors consumer lag per partition to identify processing bottlenecks. Their capacity planning includes seasonal traffic patterns (summer bookings, holiday travel).

Airbnb's Kafka deployment includes: search ranking updates, pricing optimization, notification delivery, and payment event processing. Their schema enforcement ensures event consistency across services. Airbnb uses Kafka Streams for real-time pricing calculations. Their operational practices include regular disaster recovery testing and chaos engineering exercises.

Airbnb's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. Airbnb uses schema evolution with Avro and Confluent Schema Registry. Their operational runbooks document recovery procedures for common failure scenarios.

## Stripe

Stripe uses Kafka as the backbone for their payment processing pipeline, handling millions of payment events daily. Their architecture requires exactly-once delivery for financial transactions. Stripe implemented a custom partitioning strategy that ensures all events for a given merchant are processed in order.

Stripe's Kafka deployment uses encryption at rest and in transit for PCI compliance. Their consumer groups implement backpressure mechanisms to handle traffic spikes without message loss. Stripe built a custom consumer lag alerting system that considers the business impact of delayed messages (payment confirmations vs. analytics events).

Stripe's Kafka usage includes payment event processing, fraud detection, and merchant notifications. Their schema enforcement ensures event consistency across services. Stripe uses Kafka Streams for real-time fraud scoring and payment routing. Their operational practices include regular disaster recovery testing and chaos engineering exercises.

Stripe monitors Kafka cluster health with custom dashboards. Their alerting system considers business impact: payment events have higher priority than analytics events. Stripe uses schema evolution with Avro and Confluent Schema Registry. Their capacity planning uses historical transaction volumes to forecast growth.

Stripe's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. Stripe uses encryption at rest and in transit for PCI compliance. Their operational runbooks document recovery procedures for common failure scenarios.

## Common Production Patterns

Kafka production deployments consistently emphasize the following patterns. Cluster sizing accounts for peak throughput with 30-50% headroom. Replication factor of 3 is standard for production topics. `min.insync.replicas=2` with `acks=all` provides durable writes. Rack-aware replica assignment survives zone failures.

Monitoring covers broker metrics (CPU, disk I/O, network), topic metrics (throughput, partition count), consumer metrics (lag, rebalance frequency), and producer metrics (batch size, compression ratio). Alerting thresholds are based on rate of change rather than absolute values to account for varying workloads.

Operational runbooks cover common failure scenarios: broker failure (automatic under KRaft, manual under ZK), ISR shrink (check broker health, network), consumer lag (scale consumers, check processing time), and disk full (increase retention, add capacity). Chaos engineering practices regularly test failure scenarios to validate resilience.

Production Kafka clusters use dedicated hardware with NVMe SSDs. Broker JVM tuning optimizes heap size and GC settings for the workload. Topic configuration includes replication factor, partition count, and retention policy. Producer and consumer configurations are tuned for throughput and latency requirements.

Security practices include: SASL authentication for client-broker communication, ACLs for topic authorization, encryption at rest and in transit, and audit logging for compliance. Multi-tenant clusters use quotas to prevent resource contention. Schema evolution is managed through Schema Registry with compatibility checks.

Kafka disaster recovery strategies include: cross-region replication with MirrorMaker 2, automated failover testing, and consumer group lag monitoring. Production runbooks document recovery procedures for broker failures, ISR shrink, consumer lag, and disk full scenarios. Chaos engineering practices regularly test failure scenarios to validate resilience and data durability.

Kafka production clusters require careful capacity planning. Broker sizing accounts for peak throughput with 30-50% headroom. Topic partition counts are planned based on consumer parallelism requirements. Replication factor of 3 is standard for production topics. The `min.insync.replicas=2` with `acks=all` provides durable writes while maintaining availability.

Monitoring covers broker metrics (CPU, disk I/O, network), topic metrics (throughput, partition count), consumer metrics (lag, rebalance frequency), and producer metrics (batch size, compression ratio). Alerting thresholds are based on rate of change rather than absolute values to account for varying workloads. Regular operational reviews ensure monitoring coverage and alerting effectiveness. Continuous improvement processes incorporate lessons learned from incidents and operational reviews.
