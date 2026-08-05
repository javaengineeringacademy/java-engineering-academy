# Competing Consumers Pattern

## Overview

The Competing Consumers pattern enables multiple concurrent instances of a processing component to receive messages from the same source. Each message is processed by only one consumer, providing horizontal scaling of message processing. Load is distributed across consumers based on availability and capacity.

## When to Use

- Processing high volumes of messages concurrently
- Distributing workload across multiple processing instances
- Implementing parallel processing pipelines
- Scaling message-driven architectures horizontally
- Handling variable message arrival rates
- Decoupling producers from processing capacity

## Implementation

### AWS
- SQS with multiple Lambda consumers
- Kinesis with shard-level consumers
- SQS with EC2 consumer fleet (auto-scaled)
- EventBridge with multiple rule targets

### Azure
- Azure Service Bus with message sessions
- Azure Functions with event-driven scaling
- Azure Storage Queue with multiple workers
- Azure Event Hubs with consumer groups

### Google Cloud
- Pub/Sub with subscriber scaling
- Dataflow for parallel stream processing
- Cloud Tasks with multiple workers
- Pub/Sub push subscriptions with load balancing

### Kubernetes
- Kafka consumer groups with partition assignment
- RabbitMQ with prefetch count configuration
- Custom consumer controllers with leader election
- Horizontal Pod Autoscaler based on queue depth

## Best Practices

1. Ensure message ordering requirements are met when needed
2. Implement idempotent message processing
3. Use visibility timeouts to handle failed message processing
4. Monitor consumer lag to detect processing bottlenecks
5. Configure appropriate batch sizes for consumer throughput
6. Implement poison pill handling for malformed messages
7. Scale consumers based on queue depth metrics

## Interview Questions

1. How do you ensure message ordering with competing consumers?
2. What is the visibility timeout and why is it important?
3. How would you handle a poison pill message that always fails?
4. Describe strategies for monitoring consumer health and lag.
5. How do you scale consumers based on message throughput?

## References

- Competing Consumers Pattern - Microsoft Azure Architecture Center
- Amazon SQS Documentation
- Apache Kafka Consumer Groups
- Google Cloud Pub/Sub Documentation
- RabbitMQ Tutorials
- Enterprise Integration Patterns - Gregor Hohpe
