# State Pattern (TypeScript)

## Overview

The State pattern allows an object to alter its behavior when its internal state changes.
TypeScript's discriminated unions and interfaces enable type-safe state implementations.

## When to Use

- Object behavior depends on its state
- Complex conditional statements based on state
- State transitions are explicit
- Large number of states

## TypeScript Implementation

### Typed State Machine

```typescript
interface State<T> {
  enter(context: T): void;
  exit(context: T): void;
}

class StateMachine<T> {
  private currentState: State<T> | null = null;

  transition(state: State<T>, context: T): void {
    if (this.currentState) {
      this.currentState.exit(context);
    }
    this.currentState = state;
    this.currentState.enter(context);
  }
}
```

### Discriminated Union States

```typescript
type OrderState =
  | { type: 'new' }
  | { type: 'processing'; startTime: Date }
  | { type: 'shipped'; trackingNumber: string }
  | { type: 'delivered'; deliveryDate: Date };

function handleState(state: OrderState): string {
  switch (state.type) {
    case 'new':
      return 'Order is new';
    case 'processing':
      return `Processing since ${state.startTime}`;
    case 'shipped':
      return `Shipped with ${state.trackingNumber}`;
    case 'delivered':
      return `Delivered on ${state.deliveryDate}`;
  }
}
```

### State with Actions

```typescript
class Order {
  private state: OrderState = { type: 'new' };

  process(): void {
    if (this.state.type === 'new') {
      this.state = { type: 'processing', startTime: new Date() };
    }
  }

  ship(trackingNumber: string): void {
    if (this.state.type === 'processing') {
      this.state = { type: 'shipped', trackingNumber };
    }
  }

  deliver(): void {
    if (this.state.type === 'shipped') {
      this.state = { type: 'delivered', deliveryDate: new Date() };
    }
  }
}
```

### Functional State

```typescript
type StateTransition<T> = (context: T) => T;

function createFSM<T>(
  initial: T,
  transitions: Record<string, StateTransition<T>>
) {
  let state = initial;

  return {
    getState: () => state,
    transition: (event: string) => {
      if (transitions[event]) {
        state = transitions[event](state);
      }
    }
  };
}
```

## Best Practices

- Use discriminated unions for type safety
- Keep state classes small and focused
- Make state transitions explicit
- Document state diagrams
- Handle invalid transitions gracefully

## Interview Questions

1. How does State differ from Strategy?
2. Can states contain behavior?
3. How do you handle invalid state transitions?
4. When should you use State vs conditional logic?
5. How do you implement state entry/exit actions?

## References

- TypeScript Handbook: Discriminated Unions
- "TypeScript Design Patterns" by Vaskaran Sarcar
- XState library documentation
