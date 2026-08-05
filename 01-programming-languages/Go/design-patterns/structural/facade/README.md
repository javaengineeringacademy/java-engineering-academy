# Facade Pattern

## Overview

Facade provides a simplified interface to a complex subsystem.

## When to Use

- Simplifying complex library usage
- Unified API over multiple subsystems
- Reducing dependencies

## Go Implementation

```go
type computerFacade struct {
    cpu    *cpu
    memory *memory
}

func NewComputer() *computerFacade {
    return &computerFacade{&cpu{}, &memory{}}
}

func (f *computerFacade) Start() {
    f.cpu.Freeze()
    f.memory.Load(0, "bootloader")
    f.cpu.Jump(0)
    f.cpu.Execute()
}
```

## Go-Idiomatic Alternative

```go
package hardware

func StartComputer() {
    cpu := &cpu{}
    mem := &memory{}
    cpu.Freeze()
    mem.Load(0, "boot")
    cpu.Jump(0)
    cpu.Execute()
}
```

## Real-World Example

```go
type OrderFacade struct {
    inventory *InventoryService
    payment   *PaymentService
    shipping  *ShippingService
}

func (f *OrderFacade) PlaceOrder(order Order) error {
    if err := f.inventory.Reserve(order.Items); err != nil { return err }
    if err := f.payment.Charge(order.Total); err != nil {
        f.inventory.Release(order.Items)
        return err
    }
    f.shipping.Schedule(order)
    return nil
}
```

## Best Practices

- Keep facade methods high-level
- Do not add business logic
- Use interfaces for testability

## Interview Questions

1. What is the difference between Facade and Mediator?
2. When does a Facade become an anti-pattern?
3. How do you test code using a Facade?
4. Can a Facade compose other Facades?
5. How do you handle Facade API versioning?

## References

- "Design Patterns" - GoF Chapter 4
- Go Dev: Effective Go - Package design
