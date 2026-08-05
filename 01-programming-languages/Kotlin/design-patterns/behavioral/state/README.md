# State Pattern (Kotlin)

## Overview

The State pattern allows an object to alter its behavior when its internal state changes.
Kotlin's sealed classes and when expressions enable type-safe state implementations.

## When to Use

- Object behavior depends on its state
- Complex conditional statements based on state
- State transitions are explicit
- Large number of states

## Kotlin Implementation

### Sealed Class State

```kotlin
sealed class State {
    object Idle : State() {
        override fun handle(vendingMachine: VendingMachine) {
            println("Inserting coin...")
            vendingMachine.state = HasCoin
        }
    }

    object HasCoin : State() {
        override fun handle(vendingMachine: VendingMachine) {
            println("Dispensing product...")
            vendingMachine.state = Idle
        }
    }

    abstract fun handle(vendingMachine: VendingMachine)
}

class VendingMachine {
    var state: State = State.Idle

    fun request() {
        state.handle(this)
    }
}
```

### State with Actions

```kotlin
sealed class OrderState {
    data class New(val createdAt: Long = System.currentTimeMillis()) : OrderState()
    data class Processing(val startTime: Long) : OrderState()
    data class Shipped(val trackingNumber: String) : OrderState()
    data class Delivered(val deliveryDate: Long) : OrderState()
}

class Order {
    private var state: OrderState = OrderState.New()

    fun process() {
        state = when (state) {
            is OrderState.New -> OrderState.Processing(System.currentTimeMillis())
            else -> throw IllegalStateException("Cannot process from $state")
        }
    }

    fun ship(trackingNumber: String) {
        state = when (state) {
            is OrderState.Processing -> OrderState.Shipped(trackingNumber)
            else -> throw IllegalStateException("Cannot ship from $state")
        }
    }
}
```

### State Machine

```kotlin
class StateMachine<S, E>(
    private val initialState: S,
    private val transitions: Map<Pair<S, E>, S>
) {
    private var currentState: S = initialState

    fun transition(event: E): S {
        val newState = transitions[Pair(currentState, event)]
            ?: throw IllegalStateException("No transition from $currentState on $event")
        currentState = newState
        return currentState
    }

    fun getState(): S = currentState
}
```

### Functional State

```kotlin
fun <T> createStateMachine(
    initial: T,
    transitions: Map<String, (T) -> T>
): (String) -> T {
    var state = initial
    return { event ->
        state = transitions[event]?.invoke(state) ?: state
        state
    }
}
```

## Best Practices

- Use sealed classes for type safety
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

- Kotlin documentation: Sealed classes
- "Kotlin in Action" by Svetlana Isakova
- XState library documentation
