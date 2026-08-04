# RabbitMQ Management

## Management UI, Monitoring, Policies, and Administration

---

## Table of Contents

- [Overview](#overview)
- [Management UI](#management-ui)
- [Management API](#management-api)
- [Monitoring](#monitoring)
- [Policies](#policies)
- [User Management](#user-management)
- [Best Practices](#best-practices)

---

## Overview

RabbitMQ provides a built-in management UI and API for monitoring, configuring, and administering the broker. This guide covers management tools and best practices.

### Key Features

- **Web UI**: Browser-based management interface
- **REST API**: Programmatic access to management functions
- **Monitoring**: Real-time metrics and alerts
- **Administration**: User, queue, exchange management
- **Policies**: Dynamic configuration management

---

## Management UI

### Enabling Management Plugin

```bash
# Enable management plugin
rabbitmq-plugins enable rabbitmq_management

# Management UI available at:
# http://localhost:15672
# Default credentials: guest/guest
```

### Management UI Features

```
Management UI Sections:
├── Overview
│   ├── Broker status
│   ├── Message rates
│   ├── Object counts
│   └── Health checks
├── Connections
│   ├── Active connections
│   ├── Connection details
│   └── Close connections
├── Channels
│   ├── Active channels
│   ├── Channel details
│   └── Close channels
├── Exchanges
│   ├── List exchanges
│   ├── Create/delete exchanges
│   └── Bindings
├── Queues
│   ├── List queues
│   ├── Create/delete queues
│   ├── Purge queues
│   └── Message details
├── Policies
│   ├── List policies
│   └── Create/delete policies
├── Admin
│   ├── Users
│   ├── Virtual hosts
│   └── Cluster status
```

### Dashboard Metrics

```
Overview Dashboard:
├── Messages
│   ├── Ready
│   ├── Unacked
│   └── Total
├── Message Rates
│   ├── Publish
│   ├── Deliver
│   ├── Acknowledge
│   └── Redeliver
├── Connections
│   ├── Total
│   ├── Channels
│   └── Consumers
└── System
    ├── Memory
    ├── Disk
    ├── File descriptors
    └── Erlang processes
```

---

## Management API

### API Endpoints

```bash
# List queues
curl -u guest:guest http://localhost:15672/api/queues

# List exchanges
curl -u guest:guest http://localhost:15672/api/exchanges

# List connections
curl -u guest:guest http://localhost:15672/api/connections

# List channels
curl -u guest:guest http://localhost:15672/api/channels

# Get cluster status
curl -u guest:guest http://localhost:15672/api/cluster-name

# Get node status
curl -u guest:guest http://localhost:15672/api/nodes
```

### Queue Operations

```bash
# Get queue details
curl -u guest:guest http://localhost:15672/api/queues/%2F/orders

# Purge queue
curl -u guest:guest -X DELETE http://localhost:15672/api/queues/%2F/orders/contents

# Delete queue
curl -u guest:guest -X DELETE http://localhost:15672/api/queues/%2F/orders
```

### Exchange Operations

```bash
# Create exchange
curl -u guest:guest -X PUT http://localhost:15672/api/exchanges/%2F/orders \
  -H "content-type: application/json" \
  -d '{"type":"direct","durable":true}'

# Delete exchange
curl -u guest:guest -X DELETE http://localhost:15672/api/exchanges/%2F/orders
```

### Publishing Messages

```bash
# Publish message to queue
curl -u guest:guest -X POST http://localhost:15672/api/exchanges/%2F/amq.default/publish \
  -H "content-type: application/json" \
  -d '{"routing_key":"orders","payload":"Order data","payload_encoding":"string"}'
```

---

## Monitoring

### Key Metrics

| Metric | Description |
|--------|-------------|
| Messages | Ready, unacked, total |
| Message Rates | Publish, deliver, acknowledge |
| Connections | Active connections |
| Channels | Active channels |
| Consumers | Active consumers |
| Memory | Memory usage |
| Disk | Disk usage |

### Monitoring Commands

```bash
# List queues with message counts
rabbitmqctl list_queues name messages consumers memory

# List connections
rabbitmqctl list_connections name peer_host state

# List channels
rabbitmqctl list_channels connection_name number

# Cluster status
rabbitmqctl cluster_status
```

### Prometheus Integration

```bash
# Enable Prometheus metrics
rabbitmq-plugins enable rabbitmq_prometheus

# Metrics available at:
# http://localhost:15692/metrics
```

### Grafana Dashboard

```json
{
  "panels": [
    {
      "title": "Messages",
      "targets": [
        {
          "expr": "rabbitmq_queue_messages"
        }
      ]
    },
    {
      "title": "Message Rates",
      "targets": [
        {
          "expr": "rabbitmq_queue_messages_published_total"
        }
      ]
    }
  ]
}
```

---

## Policies

### Policy Concepts

```
Policies:
- Dynamic configuration
- Apply to queues/exchanges
- Pattern-based matching
- Priority-based

Example Policy:
{
  "pattern": "^orders\\.",
  "definition": {
    "ha-mode": "all",
    "ha-sync-mode": "automatic"
  },
  "priority": 0
}
```

### Create Policy

```bash
# Create policy via CLI
rabbitmqctl set_policy ha-orders "^orders\." \
  '{"ha-mode":"all","ha-sync-mode":"automatic"}' \
  --priority 0 --apply-to queues

# Create policy via API
curl -u guest:guest -X PUT http://localhost:15672/api/policies/%2F/ha-orders \
  -H "content-type: application/json" \
  -d '{
    "pattern":"^orders\\.",
    "definition":{"ha-mode":"all","ha-sync-mode":"automatic"},
    "priority":0,
    "apply-to":"queues"
  }'
```

### Policy Definition

```json
{
  "pattern": "^orders\\.",
  "definition": {
    "ha-mode": "all",
    "ha-sync-mode": "automatic",
    "queue-mode": "lazy",
    "message-ttl": 86400000
  },
  "priority": 0,
  "apply-to": "queues"
}
```

### Policy Properties

| Property | Description |
|----------|-------------|
| pattern | Regex pattern for queue/exchange names |
| definition | Policy settings |
| priority | Policy priority (higher = more priority) |
| apply-to | "queues", "exchanges", or "all" |

### Common Policies

```bash
# HA policy
rabbitmqctl set_policy ha "^orders\." \
  '{"ha-mode":"all","ha-sync-mode":"automatic"}'

# TTL policy
rabbitmqctl set_policy ttl "^temp\." \
  '{"message-ttl":86400000}'

# Dead letter policy
rabbitmqctl set_policy dlx "^orders\." \
  '{"dead-letter-exchange":"dlx"}'

# Lazy queue policy
rabbitmqctl set_policy lazy "^.*" \
  '{"queue-mode":"lazy"}'
```

---

## User Management

### User Operations

```bash
# Create user
rabbitmqctl add_user myuser mypassword

# Delete user
rabbitmqctl delete_user myuser

# List users
rabbitmqctl list_users

# Set user tags
rabbitmqctl set_user_tags myuser administrator

# Change password
rabbitmqctl change_password myuser newpassword
```

### Virtual Host Operations

```bash
# Create virtual host
rabbitmqctl add_vhost myvhost

# Delete virtual host
rabbitmqctl delete_vhost myvhost

# List virtual hosts
rabbitmqctl list_vhosts
```

### Permissions

```bash
# Set permissions
rabbitmqctl set_permissions -p myvhost myuser ".*" ".*" ".*"

# List permissions
rabbitmqctl list_permissions -p myvhost
```

### Permission Structure

```bash
# Configure, write, read permissions
rabbitmqctl set_permissions -p / user ".*" ".*" ".*"
#   │         │           │      │     │
#   │         │           │      │     └── Read: ".*"
#   │         │           │      └── Write: ".*"
#   │         │           └── Configure: ".*"
#   │         └── Virtual host
#   └── User
```

---

## Best Practices

### Management UI

1. **Secure access** - Use HTTPS, strong passwords
2. **Limit access** - Use virtual hosts, permissions
3. **Monitor regularly** - Check dashboard daily
4. **Set up alerts** - Monitor key metrics

### API Usage

1. **Use authentication** - Always authenticate API calls
2. **Handle errors** - Implement error handling
3. **Rate limit** - Avoid overloading broker
4. **Cache responses** - Reduce API calls

### Monitoring

1. **Track key metrics** - Messages, rates, connections
2. **Set up dashboards** - Use Grafana for visualization
3. **Alert on anomalies** - Set up Prometheus alerts
4. **Monitor trends** - Detect gradual degradation

### Policies

1. **Use meaningful names** - Follow naming conventions
2. **Test policies** - Verify in staging first
3. **Document policies** - Maintain documentation
4. **Monitor policy impact** - Track performance

### Security

1. **Use HTTPS** - Encrypt management access
2. **Strong passwords** - Enforce password policies
3. **Limit admin access** - Use least privilege
4. **Audit changes** - Log management actions

---

## Further Reading

- [RabbitMQ Management](https://www.rabbitmq.com/management.html)
- [Management CLI](https://www.rabbitmq.com/cli.html)
- [Policies](https://www.rabbitmq.com/parameters.html#policies)
