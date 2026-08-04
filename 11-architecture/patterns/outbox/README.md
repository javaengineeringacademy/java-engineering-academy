# Transactional Outbox Pattern

## Overview

Ensures reliable event publishing by writing events to outbox table in same transaction as business data.

## Implementation

```python
class OrderService:
    def create_order(self, order):
        with transaction:
            # Business data
            self.db.insert('orders', order)
            
            # Outbox event
            self.db.insert('outbox', {
                'aggregate_type': 'Order',
                'aggregate_id': order.id,
                'event_type': 'OrderCreated',
                'payload': json.dumps(order.to_dict())
            })

# Relay process
class OutboxRelay:
    def poll(self):
        events = self.db.query("SELECT * FROM outbox WHERE published = false")
        for event in events:
            self.kafka.send(event['topic'], event['payload'])
            self.db.update('outbox', event['id'], published=True)
```
