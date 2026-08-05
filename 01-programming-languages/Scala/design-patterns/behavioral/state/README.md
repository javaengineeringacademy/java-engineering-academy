# State Pattern in Scala

The State pattern allows an object to alter its behavior when its internal state changes. In Scala, sealed traits and pattern matching provide type-safe state machines.

## When to Use

- Object behavior depends on its state
- State transitions are complex
- Avoiding large conditional statements
- Finite state machines
- Request processing pipelines

## Implementation

### Sealed Trait State

```scala
sealed trait OrderState
case object New extends OrderState
case object Processing extends OrderState
case object Shipped extends OrderState
case object Delivered extends OrderState
case object Cancelled extends OrderState

class Order {
  private var state: OrderState = New

  def process(): Unit = state match {
    case New => state = Processing
    case Processing => state = Shipped
    case _ => state = Cancelled
  }

  def ship(): Unit = state match {
    case Processing => state = Shipped
    case _ => state = Cancelled
  }

  def deliver(): Unit = state match {
    case Shipped => state = Delivered
    case _ => state = Cancelled
  }
}
```

### State with Data

```scala
sealed trait TrafficLight {
  def next: TrafficLight
  def duration: Int
}

case object Red extends TrafficLight {
  def next: TrafficLight = Green
  def duration: Int = 30
}

case object Yellow extends TrafficLight {
  def next: TrafficLight = Red
  def duration: Int = 5
}

case object Green extends TrafficLight {
  def next: TrafficLight = Yellow
  def duration: Int = 25
}
```

### Character State

```scala
sealed trait CharacterState {
  def speed: Double
  def update(): Unit
}

case object Idle extends CharacterState {
  val speed: Double = 0.0
  def update(): Unit = println("Idle")
}

case object Walking extends CharacterState {
  val speed: Double = 5.0
  def update(): Unit = println("Walking")
}

case object Running extends CharacterState {
  val speed: Double = 15.0
  def update(): Unit = println("Running")
}

class Character {
  private var state: CharacterState = Idle

  def update(): Unit = state.update()

  def transition(newState: CharacterState): Unit = {
    state = newState
  }
}
```

### State Machine with Transitions

```scala
sealed trait State
case object Locked extends State
case object Unlocked extends State
case object Broken extends State

case class Transition(from: State, to: State, action: String)

class StateMachine {
  private val transitions = List(
    Transition(Locked, Unlocked, "unlock"),
    Transition(Unlocked, Locked, "lock"),
    Transition(Locked, Broken, "force"),
    Transition(Unlocked, Broken, "force")
  )

  private var currentState: State = Locked

  def transition(action: String): State = {
    transitions.find(t => t.from == currentState && t.action == action) match {
      case Some(t) =>
        currentState = t.to
        currentState
      case None =>
        currentState
    }
  }
}
```

## Best Practices

- Use sealed traits for exhaustive pattern matching
- Document valid state transitions
- Use case objects for stateless states
- Use case classes for states with data
- Consider using typestate pattern for compile-time validation

## Interview Questions

1. What is the difference between the state pattern and a state machine?
2. How does Scala's pattern matching benefit state implementations?
3. When would you use sealed traits vs case classes for states?
4. How do you handle invalid state transitions?
5. What is the typestate pattern and how does it work in Scala?

## References

- [Sealed Traits](https://docs.scala-lang.org/tour/polymorphic-types.html)
- [Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
