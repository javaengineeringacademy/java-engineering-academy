# Saga Pattern

## Overview

The Saga pattern manages distributed transactions across multiple services by breaking them into a sequence of local transactions. Each local transaction updates the database and publishes events to trigger the next step. If a step fails, compensating transactions undo the previous work.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Choreography-Based Saga](#choreography-based-saga)
- [Orchestration-Based Saga](#orchestration-based-saga)
- [Compensating Transactions](#compensating-transactions)
- [Benefits](#benefits)
- [Trade-offs](#trade-offs)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            SAGA PATTERN                           |
+--------------------------------------------------+
|                                                  |
|  T1 --> T2 --> T3 --> T4                         |
|  |       |       |       |                       |
|  v       v       v       v                       |
|  C1 <-- C2 <-- C3 <-- C4  (Compensating)        |
|                                                  |
|  T = Transaction    C = Compensating Transaction |
+--------------------------------------------------+
```

| Term | Description |
|------|-------------|
| Transaction | Local database operation |
| Compensating Transaction | Undo operation for a transaction |
| Choreography | Event-driven coordination |
| Orchestration | Central coordinator |

## Choreography-Based Saga

Each service publishes events that trigger the next step.

```python
from dataclasses import dataclass
from uuid import UUID, uuid4
from datetime import datetime

class EventBus:
    def __init__(self):
        self._handlers = {}

    def subscribe(self, event_type, handler):
        if event_type not in self._handlers:
            self._handlers[event_type] = []
        self._handlers[event_type].append(handler)

    def publish(self, event):
        event_type = type(event).__name__
        for handler in self._handlers.get(event_type, []):
            handler(event)

class OrderService:
    def __init__(self, repository, event_bus):
        self._repository = repository
        self._event_bus = event_bus

    def create_order(self, customer_id, items):
        order = Order(id=uuid4(), customer_id=customer_id, items=items, status='created')
        self._repository.save(order)
        self._event_bus.publish(OrderCreated(order_id=order.id))
        return order.id

    def handle_payment_processed(self, event):
        order = self._repository.find(event.order_id)
        order.status = 'paid'
        self._repository.save(order)

    def handle_payment_failed(self, event):
        order = self._repository.find(event.order_id)
        order.status = 'cancelled'
        self._repository.save(order)

class PaymentService:
    def __init__(self, repository, event_bus):
        self._repository = repository
        self._event_bus = event_bus

    def handle_order_created(self, event):
        try:
            payment = self._process_payment(event.total)
            self._event_bus.publish(PaymentProcessed(order_id=event.order_id))
        except PaymentError as e:
            self._event_bus.publish(PaymentFailed(order_id=event.order_id, reason=str(e)))
```

## Orchestration-Based Saga

A central coordinator directs all saga steps.

```python
class SagaOrchestrator:
    def __init__(self, steps):
        self._steps = steps

    def execute(self, context):
        completed = []
        try:
            for step in self._steps:
                result = step.execute(context)
                completed.append((step, result))
                context[step.name] = result
            return {'status': 'completed', 'context': context}
        except Exception as e:
            for step, result in reversed(completed):
                try:
                    step.compensate(result)
                except Exception:
                    pass
            return {'status': 'compensated', 'error': str(e)}

class SagaStep:
    def __init__(self, name, execute, compensate):
        self.name = name
        self.execute = execute
        self.compensate = compensate
```

## Compensating Transactions

Compensating transactions undo completed steps when a later step fails.

```python
class OrderCompensation:
    def cancel_order(self, order_id):
        order = self.repository.find(order_id)
        order.status = 'cancelled'
        self.repository.save(order)

class PaymentCompensation:
    def refund_payment(self, payment_id):
        payment = self.repository.find(payment_id)
        self.gateway.refund(payment.amount)
        payment.status = 'refunded'
        self.repository.save(payment)

class InventoryCompensation:
    def release_reservation(self, reservation_id):
        reservation = self.repository.find(reservation_id)
        for item in reservation.items:
            self.stock.increment(item.product_id, item.quantity)
        reservation.status = 'released'
        self.repository.save(reservation)
```

### Design Principles

1. **Idempotent**: Compensations must be safe to retry
2. **Undoable**: Each transaction must have a reversal
3. **Retriable**: Compensations may need to retry multiple times
4. **Semantic rollback**: Compensations approximate the original intent

## Benefits

1. **No distributed locks**: Each service commits locally
2. **Decentralized**: Choreography avoids single point of failure
3. **Scalable**: Services can scale independently
4. **Flexible**: Easy to add new steps
5. **Eventually consistent**: Data converges to consistency

## Trade-offs

1. **Complexity**: Harder to understand and debug
2. **Eventual consistency**: No immediate consistency guarantee
3. **Compensation logic**: Must design undo operations
4. **Testing**: Integration testing is complex
5. **Monitoring**: Need distributed tracing

## Best Practices

### 1. Design Idempotent Operations

```python
class PaymentService:
    def charge(self, order_id, amount):
        existing = self.repository.find_by_order(order_id)
        if existing:
            return existing
        return self._process_charge(order_id, amount)
```

### 2. Use Correlation IDs

```python
@dataclass
class SagaMessage:
    correlation_id: UUID
    step_name: str
    payload: dict
    timestamp: datetime
```

### 3. Implement Timeout Handling

```python
class SagaStep:
    def execute_with_timeout(self, data, timeout_seconds=30):
        start = datetime.now()
        result = self.execute(data)
        elapsed = (datetime.now() - start).total_seconds()
        if elapsed > timeout_seconds:
            raise SagaTimeoutError(f'{self.name} took too long')
        return result
```

### 4. Log Every Step

```python
class SagaLogger:
    def log_step(self, saga_id, step_name, status, data):
        self.store.save(SagaLogEntry(
            saga_id=saga_id, step=step_name,
            status=status, data=data, timestamp=datetime.now()
        ))
```

### 5. Monitor Saga Progress

```python
class SagaMonitor:
    def get_saga_status(self, saga_id):
        logs = self.store.get_logs(saga_id)
        return {
            'saga_id': saga_id,
            'steps': [{'name': l.step, 'status': l.status} for l in logs],
            'current_step': logs[-1].step if logs else None
        }
```

## Further Reading

- [Saga Pattern - Chris Richardson](https://microservices.io/patterns/data/saga.html)
- [Managing Data Consistency using Sagas](https://www.infoq.com/presentations/data-consistency-microservices/)
- [Original Saga Paper - Garcia-Molina and Salem](https://www.cs.cornell.edu/loeh/cs4120/2019sp/garcia-molina-saga.pdf)
