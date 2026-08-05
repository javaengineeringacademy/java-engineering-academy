# Saga Pattern

## Overview

The Saga Pattern manages distributed transactions by breaking them into a sequence of local transactions. Each local transaction updates the database and publishes events to trigger the next step. If a step fails, compensating transactions undo the previous steps.

Sagas provide eventual consistency across multiple services without requiring two-phase commit. Each step is a local transaction, and the overall workflow coordinates success or rollback through compensation logic.

## When to Use

- Business operations span multiple microservices
- Distributed transactions with ACID guarantees are impractical
- Eventual consistency is acceptable for the business process
- Long-running transactions span minutes or hours
- Rollback logic can be expressed as compensating actions

## Implementation

### TypeScript (Choreography-based)

```typescript
interface SagaStep {
  execute(context: SagaContext): Promise<void>;
  compensate(context: SagaContext): Promise<void>;
}

class ReserveInventoryStep implements SagaStep {
  async execute(context: SagaContext): Promise<void> {
    await this.inventoryService.reserve(context.orderId, context.items);
  }

  async compensate(context: SagaContext): Promise<void> {
    await this.inventoryService.release(context.orderId);
  }
}

class ProcessPaymentStep implements SagaStep {
  async execute(context: SagaContext): Promise<void> {
    await this.paymentService.charge(context.customerId, context.amount);
  }

  async compensate(context: SagaContext): Promise<void> {
    await this.paymentService.refund(context.customerId, context.amount);
  }
}

class SagaOrchestrator {
  private steps: SagaStep[] = [];

  addStep(step: SagaStep): void {
    this.steps.push(step);
  }

  async execute(context: SagaContext): Promise<boolean> {
    const completedSteps: SagaStep[] = [];
    try {
      for (const step of this.steps) {
        await step.execute(context);
        completedSteps.push(step);
      }
      return true;
    } catch (error) {
      for (const step of completedSteps.reverse()) {
        await step.compensate(context);
      }
      return false;
    }
  }
}
```

### Java (Orchestration-based)

```java
public interface SagaStep<T> {
    void execute(T context);
    void compensate(T context);
}

@Component
public class OrderSaga {
    private final List<SagaStep<OrderContext>> steps = new ArrayList<>();

    @Autowired
    public OrderSaga(ReserveInventoryStep inventory, ProcessPaymentStep payment,
                     CreateShipmentStep shipment) {
        steps.add(inventory);
        steps.add(payment);
        steps.add(shipment);
    }

    public boolean execute(OrderContext context) {
        List<SagaStep<OrderContext>> completed = new ArrayList<>();
        try {
            for (SagaStep<OrderContext> step : steps) {
                step.execute(context);
                completed.add(step);
            }
            return true;
        } catch (Exception e) {
            Collections.reverse(completed);
            for (SagaStep<OrderContext> step : completed) {
                step.compensate(context);
            }
            return false;
        }
    }
}
```

### Python

```python
from typing import List, Callable
from dataclasses import dataclass

@dataclass
class SagaContext:
    order_id: str
    customer_id: str
    amount: float

class SagaStep:
    def __init__(self, execute_fn: Callable, compensate_fn: Callable):
        self.execute_fn = execute_fn
        self.compensate_fn = compensate_fn

    def execute(self, context: SagaContext):
        self.execute_fn(context)

    def compensate(self, context: SagaContext):
        self.compensate_fn(context)

class SagaOrchestrator:
    def __init__(self):
        self.steps: List[SagaStep] = []

    def add_step(self, step: SagaStep):
        self.steps.append(step)

    def execute(self, context: SagaContext) -> bool:
        completed = []
        try:
            for step in self.steps:
                step.execute(context)
                completed.append(step)
            return True
        except Exception as e:
            for step in reversed(completed):
                step.compensate(context)
            return False
```

### C\#

```csharp
public interface ISagaStep<T> {
    Task ExecuteAsync(T context);
    Task CompensateAsync(T context);
}

public class OrderSaga {
    private readonly List<ISagaStep<OrderContext>> _steps = new();

    public void AddStep(ISagaStep<OrderContext> step) => _steps.Add(step);

    public async Task<bool> ExecuteAsync(OrderContext context) {
        var completed = new List<ISagaStep<OrderContext>>();
        try {
            foreach (var step in _steps) {
                await step.ExecuteAsync(context);
                completed.Add(step);
            }
            return true;
        } catch {
            completed.Reverse();
            foreach (var step in completed) {
                await step.CompensateAsync(context);
            }
            return false;
        }
    }
}
```

## Best Practices

- Design compensating transactions for every forward action
- Make each step idempotent to handle retries safely
- Use saga logs to track execution state and enable recovery
- Prefer choreography for simple flows, orchestration for complex ones
- Define clear timeout and retry policies for each step
- Test compensation paths as thoroughly as the happy path

## Interview Questions

1. What is the difference between choreography and orchestration sagas?
2. How do you handle a failure in a compensating transaction?
3. What are the consistency guarantees of the Saga Pattern?
4. How do you test sagas with their compensation logic?
5. How do sagas handle concurrent execution and race conditions?

## References

- Richardson, Chris. *Microservices Patterns*, Chapter 4
- Garcia-Molina, Hector. *Sagas* (1987)
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Microsoft. *Saga Pattern*
