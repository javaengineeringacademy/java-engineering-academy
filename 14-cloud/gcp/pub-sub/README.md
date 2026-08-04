# Google Cloud Pub/Sub

## Overview

Pub/Sub is a fully managed, real-time messaging service for event-driven architectures.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                     Pub/Sub                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Topic   │  │Subscrip- │  │ Messages │             │
│  │          │  │  tions   │  │          │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Acknowledge  │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Creating Topics

### gcloud CLI
```bash
# Create topic
gcloud pubsub topics create my-topic

# Create topic with schema
gcloud pubsub topics create my-topic \
  --message-encoding-type=json \
  --schema=my-schema
```

### Terraform
```hcl
resource "google_pubsub_topic" "default" {
  name = "my-topic"

  message_retention_duration = "86400s"
}
```

## Creating Subscriptions

```bash
# Create subscription
gcloud pubsub subscriptions create my-subscription \
  --topic=my-topic \
  --ack-deadline=60 \
  --message-retention-duration=604800s

# Create push subscription
gcloud pubsub subscriptions create my-push-subscription \
  --topic=my-topic \
  --push-endpoint=https://my-endpoint.example.com
```

## Message Ordering

```bash
# Create topic with ordering
gcloud pubsub topics create my-ordered-topic \
  --message-ordering

# Publish with ordering key
gcloud pubsub topics publish my-ordered-topic \
  --message="Hello World" \
  --ordering-key=order-123
```

## Exactly-Once Delivery

```bash
# Create subscription with exactly-once
gcloud pubsub subscriptions create my-exactly-once \
  --topic=my-topic \
  --enable-exactly-once-delivery
```

## Dead Letter Queues

```bash
# Create dead letter topic
gcloud pubsub topics create my-dlq

# Create subscription with DLQ
gcloud pubsub subscriptions create my-subscription \
  --topic=my-topic \
  --dead-letter-topic=projects/my-project/topics/my-dlq \
  --max-delivery-attempts=5
```

## Message Retention

```bash
# Create subscription with retention
gcloud pubsub subscriptions create my-subscription \
  --topic=my-topic \
  --message-retention-duration=604800s
```

## Schema Management

```bash
# Create schema
gcloud pubsub schemas create my-schema \
  --type=avro \
  --definition-file=schema.avsc

# Create topic with schema
gcloud pubsub topics create my-topic \
  --schema=my-schema \
  --message-encoding-type=json
```

## Publishing Messages

### gcloud CLI
```bash
# Publish message
gcloud pubsub topics publish my-topic \
  --message="Hello World"

# Publish with attributes
gcloud pubsub topics publish my-topic \
  --message='{"key": "value"}' \
  --attribute="event_type=order.created"
```

### SDK
```python
from google.cloud import pubsub_v1

publisher = pubsub_v1.PublisherClient()
topic_path = publisher.topic_path("my-project", "my-topic")

data = b"Hello World"
future = publisher.publish(topic_path, data, event_type="order.created")
print(future.result())
```

## Subscribing Messages

### gcloud CLI
```bash
# Pull messages
gcloud pubsub subscriptions pull my-subscription --limit=10
```

### SDK
```python
from google.cloud import pubsub_v1

subscriber = pubsub_v1.SubscriberClient()
subscription_path = subscriber.subscription_path("my-project", "my-subscription")

def callback(message):
    print(f"Received: {message.data}")
    message.ack()

future = subscriber.subscribe(subscription_path, callback=callback)
future.result()
```

## Push Subscriptions

```bash
# Create push subscription
gcloud pubsub subscriptions create my-push-subscription \
  --topic=my-topic \
  --push-endpoint=https://my-endpoint.example.com \
  --push-auth-service-account=my-sa@my-project.iam.gserviceaccount.com
```

## BigQuery Subscriptions

```bash
# Create BigQuery subscription
gcloud pubsub subscriptions create my-bq-subscription \
  --topic=my-topic \
  --bigquery-table=my-project:my_dataset.my_table
```

## Monitoring

```bash
# Get topic metrics
gcloud monitoring metrics list \
  --filter='metric.type="pubsub.googleapis.com/topic/send_message_operation_count"'

# Get subscription metrics
gcloud monitoring metrics list \
  --filter='metric.type="pubsub.googleapis.com/subscription/acknowledge_message_count"'
```

## Cost Optimization

- **Use message batching** for efficiency
- **Implement dead letter queues** for reliability
- **Set appropriate ack deadlines**
- **Monitor subscription backlog**
- **Use ordering keys** for critical messages

## Best Practices

1. **Use exactly-once delivery** for critical messages
2. **Implement dead letter queues**
3. **Set appropriate message retention**
4. **Use message ordering** when needed
5. **Monitor subscription backlog**
6. **Implement proper error handling**
7. **Use service accounts** for authentication
8. **Enable message deduplication**
9. **Use batch publishing** for efficiency
10. **Implement proper monitoring**
