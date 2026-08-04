# Kafka Monitoring

## Kafka Metrics, Consumer Lag, JMX, and Monitoring Best Practices

---

## Table of Contents

- [Overview](#overview)
- [Monitoring Architecture](#monitoring-architecture)
- [Key Metrics](#key-metrics)
- [Consumer Lag Monitoring](#consumer-lag-monitoring)
- [JMX Monitoring](#jmx-monitoring)
- [Prometheus and Grafana](#prometheus-and-grafana)
- [Alerting](#alerting)
- [Best Practices](#best-practices)

---

## Overview

Monitoring Kafka clusters is essential for ensuring reliability, performance, and capacity planning. This guide covers key metrics, monitoring tools, and alerting strategies.

### Monitoring Goals

- **Availability**: Ensure brokers are reachable
- **Performance**: Track throughput and latency
- **Reliability**: Monitor replication and data durability
- **Capacity**: Plan for growth and resource usage

---

## Monitoring Architecture

### Monitoring Stack

```
┌─────────────────────────────────────────────────────────────┐
│                    Monitoring Stack                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │  Kafka   │────▶│   JMX    │────▶│  Exporter│            │
│  │  Broker  │     │          │     │          │            │
│  └──────────┘     └──────────┘     └────┬─────┘            │
│                                         │                   │
│                                         ▼                   │
│                                    ┌──────────┐            │
│                                    │Prometheus │            │
│                                    └────┬─────┘            │
│                                         │                   │
│                                         ▼                   │
│                                    ┌──────────┐            │
│                                    │ Grafana  │            │
│                                    └──────────┘            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Components

| Component | Purpose |
|-----------|---------|
| Kafka Broker | Exposes metrics via JMX |
| JMX Exporter | Exports JMX metrics to Prometheus format |
| Prometheus | Scrapes and stores metrics |
| Grafana | Visualizes metrics with dashboards |

---

## Key Metrics

### Broker Metrics

| Metric | Description | Threshold |
|--------|-------------|-----------|
| `UnderReplicatedPartitions` | Partitions with ISR < replication factor | > 0 |
| `IsrShrinkPerSec` | Rate of ISR shrink events | > 0 |
| `ActiveControllerCount` | Number of active controllers | != 1 |
| `OfflinePartitionsCount` | Partitions without leader | > 0 |
| `LeaderElectionRateAndTimeMs` | Leader election rate and latency | Monitor |
| `UncleanLeaderElectionsPerSec` | Unclean leader elections | > 0 |

### Producer Metrics

| Metric | Description | Threshold |
|--------|-------------|-----------|
| `record-error-rate` | Record error rate | > 0 |
| `record-send-rate` | Records sent per second | Monitor |
| `record-queue-time-avg` | Average time in queue | > 100ms |
| `record-send-rate` | Records sent per second | Monitor |
| `batch-size-avg` | Average batch size | Monitor |
| `compression-rate-avg` | Compression ratio | Monitor |

### Consumer Metrics

| Metric | Description | Threshold |
|--------|-------------|-----------|
| `records-lag-max` | Maximum consumer lag | Monitor |
| `records-consumed-rate` | Records consumed per second | Monitor |
| `fetch-rate` | Fetch requests per second | Monitor |
| `fetch-latency-avg` | Average fetch latency | > 100ms |

### Network Metrics

| Metric | Description | Threshold |
|--------|-------------|-----------|
| `network processor avg idle percent` | Network thread utilization | < 30% |
| `request handler avg idle percent` | Request handler utilization | < 30% |
| `RequestHandlerAvgIdlePercent` | Request handler idle percentage | < 30% |

---

## Consumer Lag Monitoring

### Consumer Lag Concept

```
Topic: orders
Partition 0:
  Log End Offset: 1000 (latest message)
  Current Offset: 850 (last consumed)
  Lag: 150 messages

Partition 1:
  Log End Offset: 1200
  Current Offset: 1150
  Lag: 50 messages

Total Lag: 200 messages
```

### Monitoring Lag

```bash
# Check consumer group lag
kafka-consumer-groups.sh --describe \
  --group order-processor \
  --bootstrap-server localhost:9092

# Output:
# GROUP           TOPIC  PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG
# order-processor orders 0          850             1000            150
# order-processor orders 1          1150            1200            50
```

### Lag Thresholds

| Lag Status | Threshold | Action |
|------------|-----------|--------|
| Healthy | < 1000 | None |
| Warning | 1000-10000 | Investigate |
| Critical | > 10000 | Immediate action |

### Lag Monitoring Tools

```bash
# Burrow (LinkedIn)
# Kafka Lag Exporter (Lightbend)
# Confluent Control Center
# Custom scripts with kafka-consumer-groups.sh
```

---

## JMX Monitoring

### Enabling JMX

```bash
# Start broker with JMX
export KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.port=9999"

kafka-server-start.sh config/server.properties
```

### JMX MBeans

```
Kafka Broker MBeans:
├── kafka.server:type=BrokerTopicMetrics
│   ├── MessagesInPerSec
│   ├── BytesInPerSec
│   └── BytesOutPerSec
├── kafka.server:type=ReplicaManager
│   ├── UnderReplicatedPartitions
│   ├── IsrShrinkPerSec
│   └── IsrExpandPerSec
├── kafka.server:type=BrokerChannel
│   ├── BytesInPerSec
│   └── BytesOutPerSec
└── kafka.controller:type=KafkaController
    ├── ActiveControllerCount
    └── OfflinePartitionsCount
```

### JMX Commands

```bash
# List MBeans
jmxterm -l localhost:9999

# Get metric value
echo "get -b kafka.server:type=BrokerTopicMetrics MessagesInPerSec" | \
  jmxterm -l localhost:9999

# Using jmxtrans
java -jar jmxtrans-agent.jar
```

---

## Prometheus and Grafana

### JMX Exporter Configuration

```yaml
# jmxexporter.yml
hostPort: localhost:9999
lowercaseOutputName: true
lowercaseOutputLabelNames: true

rules:
  - pattern: kafka.server<type=BrokerTopicMetrics, name=(\w+)><>OneMinuteRate
    name: kafka_server_brokertopicmetrics_$1_one_minute_rate
    help: "Kafka broker topic metrics $1 one minute rate"
    type: GAUGE
    
  - pattern: kafka.server<type=ReplicaManager, name=(\w+)><>Value
    name: kafka_server_replicamanager_$1
    help: "Kafka replica manager $1"
    type: GAUGE
    
  - pattern: kafka.controller<type=KafkaController, name=(\w+)><>Value
    name: kafka_controller_kafkacontroller_$1
    help: "Kafka controller $1"
    type: GAUGE
```

### Prometheus Configuration

```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'kafka'
    static_configs:
      - targets: ['localhost:7071']
    scrape_interval: 30s
```

### Grafana Dashboard

```
Dashboard Panels:
├── Broker Overview
│   ├── Messages In/Out per Second
│   ├── Bytes In/Out per Second
│   ├── Active Controllers
│   └── Under Replicated Partitions
├── Consumer Groups
│   ├── Consumer Lag
│   ├── Messages Consumed per Second
│   └── Rebalance Rate
├── Replication
│   ├── ISR Shrink Rate
│   ├── ISR Expand Rate
│   └── Under Replicated Partitions
└── Network
    ├── Request Rate
    ├── Request Latency
    └── Network Processor Idle
```

### Key Grafana Panels

```json
{
  "panels": [
    {
      "title": "Under Replicated Partitions",
      "targets": [
        {
          "expr": "kafka_server_replicamanager_underreplicatedpartitions"
        }
      ],
      "thresholds": {
        "steps": [
          {"value": 0, "color": "green"},
          {"value": 1, "color": "red"}
        ]
      }
    },
    {
      "title": "Consumer Lag",
      "targets": [
        {
          "expr": "kafka_consumergroup_lag_sum"
        }
      ]
    }
  ]
}
```

---

## Alerting

### Alert Rules

```yaml
# Prometheus alerting rules
groups:
  - name: kafka
    rules:
      - alert: KafkaBrokerDown
        expr: up{job="kafka"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Kafka broker down"
          
      - alert: KafkaUnderReplicatedPartitions
        expr: kafka_server_replicamanager_underreplicatedpartitions > 0
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Under replicated partitions"
          
      - alert: KafkaConsumerLagHigh
        expr: kafka_consumergroup_lag_sum > 10000
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Consumer lag high"
          
      - alert: KafkaOfflinePartitions
        expr: kafka_controller_kafkacontroller_offlinepartitionscount > 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Offline partitions"
```

### Alert Thresholds

| Alert | Metric | Threshold | Duration |
|-------|--------|-----------|----------|
| Broker Down | `up` | == 0 | 1m |
| Under Replicated | `under_replicated_partitions` | > 0 | 5m |
| Consumer Lag High | `consumer_lag` | > 10000 | 5m |
| Offline Partitions | `offline_partitions_count` | > 0 | 1m |
| ISR Shrink Rate | `isr_shrink_rate` | > 0 | 5m |

---

## Best Practices

### Monitoring Setup

1. **Monitor all brokers** - Ensure complete visibility
2. **Track consumer lag** - Monitor all consumer groups
3. **Set up alerts** - Proactive issue detection
4. **Use dashboards** - Visualize key metrics

### Metric Collection

1. **Use appropriate intervals** - Balance accuracy vs overhead
2. **Store metrics long-term** - Enable capacity planning
3. **Monitor all components** - Brokers, producers, consumers
4. **Track custom metrics** - Application-specific KPIs

### Alerting Strategy

1. **Set meaningful thresholds** - Avoid alert fatigue
2. **Use severity levels** - Critical vs warning
3. **Include runbooks** - Help with response
4. **Test alerts** - Verify they work

### Performance

1. **Monitor JMX overhead** - Ensure minimal impact
2. **Use efficient exporters** - Minimize resource usage
3. **Aggregate metrics** - Reduce storage requirements
4. **Archive old metrics** - Manage storage costs

### Troubleshooting

1. **Use metric correlation** - Identify root cause
2. **Monitor trends** - Detect gradual degradation
3. **Set up logging** - Complement metrics with logs
4. **Document incidents** - Build knowledge base

---

## Further Reading

- [Kafka Monitoring Documentation](https://kafka.apache.org/documentation/#monitoring)
- [JMX Exporter](https://github.com/prometheus/jmx_exporter)
- [Confluent Control Center](https://docs.confluent.io/platform/current/control-center/)
