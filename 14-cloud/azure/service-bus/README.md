# Azure Service Bus

## Overview

Azure Service Bus is a fully managed enterprise message broker.

## Components

| Component     | Description                    |
|---------------|--------------------------------|
| Queues        | Point-to-point messaging       |
| Topics        | Pub/sub messaging              |
| Relay         | Hybrid connectivity            |

## Creating Namespaces

### Azure CLI
```bash
# Create namespace
az servicebus namespace create \
  --resource-group myResourceGroup \
  --name mynamespace \
  --location eastus \
  --sku Standard

# Create queue
az servicebus queue create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name myqueue

# Create topic
az servicebus topic create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name mytopic
```

### ARM Template
```json
{
  "type": "Microsoft.ServiceBus/namespaces",
  "apiVersion": "2022-10-01-preview",
  "name": "mynamespace",
  "location": "eastus",
  "sku": {
    "name": "Standard"
  }
}
```

## Queue Features

```bash
# Create queue with options
az servicebus queue create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name myqueue \
  --max-size 1024 \
  --default-message-time-to-live P1D \
  --lock-duration PT1M \
  --max-delivery-count 10 \
  --dead-lettering-on-message-expiration true
```

## Topic Features

```bash
# Create topic with options
az servicebus topic create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name mytopic \
  --max-size 1024 \
  --default-message-time-to-live P1D

# Create subscription
az servicebus topic subscription create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --topic-name mytopic \
  --name mysubscription
```

## Sessions

```bash
# Create queue with sessions
az servicebus queue create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name myqueue \
  --enable-session true

# Create topic subscription with sessions
az servicebus topic subscription create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --topic-name mytopic \
  --name mysubscription \
  --enable-session true
```

## Dead Letter Queue

```bash
# Enable dead lettering
az servicebus queue create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --name myqueue \
  --enable-dead-lettering-on-message-expiration true \
  --max-delivery-count 10
```

## Filters

```bash
# Create subscription with filter
az servicebus topic subscription create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --topic-name mytopic \
  --name mysubscription \
  --default-message-time-to-live P1D
```

## Security

```bash
# Create authorization rule
az servicebus queue authorization-rule create \
  --resource-group myResourceGroup \
  --namespace-name mynamespace \
  --queue-name myqueue \
  --name mypolicy \
  --rights Listen Send Manage
```

## Monitoring

```bash
# Get namespace metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.ServiceBus/namespaces/mynamespace \
  --metric "ActiveMessageCount"

# Get queue metrics
az monitor metrics list \
  --resource /subscriptions/{sub}/resourceGroups/myResourceGroup/providers/Microsoft.ServiceBus/namespaces/mynamespace \
  --metric "QueueSize"
```

## Cost Optimization

- **Use appropriate SKUs**
- **Implement message batching**
- **Monitor message volumes**
- **Use sessions** for ordered processing
- **Implement proper TTL**

## Best Practices

1. **Use sessions** for ordered processing
2. **Implement dead letter queues**
3. **Use filters** for pub/sub
4. **Implement proper security**
5. **Monitor with Azure Monitor**
6. **Use managed identities**
7. **Implement proper error handling**
8. **Use message batching**
9. **Regular performance reviews**
10. **Monitor costs**
