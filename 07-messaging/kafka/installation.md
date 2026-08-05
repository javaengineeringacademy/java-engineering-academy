# Kafka Installation

> Docker, Docker Compose, binary installation, and Confluent Platform.

## Docker Installation

### Single Broker

```bash
# Run ZooKeeper
docker run -d --name zookeeper \
    -p 2181:2181 \
    confluentinc/cp-zookeeper:7.5.0

# Run Kafka Broker
docker run -d --name kafka \
    -p 9092:9092 \
    -e KAFKA_BROKER_ID=1 \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
    -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
    --link zookeeper \
    confluentinc/cp-kafka:7.5.0

# Verify
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list
```

### KRaft Mode (No ZooKeeper)

```bash
# Generate cluster UUID
KAFKA_CLUSTER_ID=$(docker run --rm confluentinc/cp-kafka:7.5.0 \
    kafka-storage.sh random-uuid)

# Format storage
docker run --rm \
    -v kafka-data:/var/lib/kafka/data \
    -e CLUSTER_ID=$KAFKA_CLUSTER_ID \
    confluentinc/cp-kafka:7.5.0 \
    kafka-storage.sh format -t $KAFKA_CLUSTER_ID \
    -c /etc/kafka/kafka.properties

# Run single node KRaft
docker run -d --name kafka \
    -p 9092:9092 \
    -e KAFKA_NODE_ID=1 \
    -e KAFKA_PROCESS_ROLES=broker,controller \
    -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
    -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
    -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
    -e CLUSTER_ID=$KAFKA_CLUSTER_ID \
    confluentinc/cp-kafka:7.5.0
```

## Docker Compose

### Basic Setup

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
```

### Multi-Broker Setup

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka-1:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-1:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3

  kafka-2:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-2:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3

  kafka-3:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-3:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
```

### KRaft Docker Compose

```yaml
version: '3.8'
services:
  kafka:
    image: confluentinc/cp-kafka:7.5.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@localhost:9093
      CLUSTER_ID: MkU3OEVBNTcwNTJENDM2Qk
```

## Binary Installation

### Linux/macOS

```bash
# Download Kafka
wget https://downloads.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz
tar -xzf kafka_2.13-3.6.0.tgz
cd kafka_2.13-3.6.0

# Start ZooKeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Broker
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create \
    --bootstrap-server localhost:9092 \
    --topic test \
    --partitions 3 \
    --replication-factor 1

# List topics
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Systemd Service

```ini
# /etc/systemd/system/kafka.service
[Unit]
Description=Apache Kafka
After=network.target

[Service]
Type=simple
User=kafka
ExecStart=/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties
ExecStop=/opt/kafka/bin/kafka-server-stop.sh
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

## Confluent Platform

### Confluent CLI

```bash
# Install Confluent CLI
curl -L https://packages.confluent.io/install.sh | sh

# Start local cluster
confluent local services start

# Create topic
confluent topic create orders --partitions 3 --replication-factor 1

# List topics
confluent topic list
```

### Confluent Cloud

```bash
# Create API key
confluent api-key create --cloud aws --environment prod

# Connect to Confluent Cloud
confluent kafka cluster use lkc-xxxxx
confluent kafka topic create orders --partitions 3
```

## Verification

```bash
# Test producer
bin/kafka-console-producer.sh \
    --bootstrap-server localhost:9092 \
    --topic test

# Test consumer
bin/kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic test \
    --from-beginning

# Describe topic
bin/kafka-topics.sh --describe \
    --bootstrap-server localhost:9092 \
    --topic test
```

## References

- [Kafka Quickstart](https://kafka.apache.org/quickstart)
- [Confluent Documentation](https://docs.confluent.io/)
- [Kafka Docker Images](https://hub.docker.com/u/confluentinc)

---
**Prerequisites:** [Kafka architecture](architecture.md)
**Related:** [Kafka configuration](configuration.md) | [Kafka project-structure](project-structure.md)
**Next:** [Kafka project-structure](project-structure.md)
