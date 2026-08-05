# Mediator Pattern in Scala

The Mediator pattern defines an object that encapsulates how a set of objects interact. In Scala, this is implemented using classes that coordinate communication between components.

## When to Use

- Complex interactions between multiple objects
- UI component coordination
- Chat room implementations
- Air traffic control systems
- Event bus systems

## Implementation

### Basic Mediator

```scala
trait Mediator {
  def notify(sender: String, event: String): Unit
}

class ChatRoom extends Mediator {
  private var users: List[String] = List()

  def addUser(user: String): Unit = users = users :+ user

  def notify(sender: String, event: String): Unit = {
    println(s"ChatRoom: $sender sent '$event' to all users")
  }
}

class User(name: String, mediator: Mediator) {
  def send(message: String): Unit = mediator.notify(name, message)
}
```

### Event Bus

```scala
class EventBus {
  private var handlers = Map[String, List[String => Unit]]()

  def subscribe(event: String, handler: String => Unit): Unit = {
    handlers = handlers.updated(
      event,
      handlers.getOrElse(event, List()) :+ handler
    )
  }

  def publish(event: String, data: String): Unit = {
    handlers.getOrElse(event, List()).foreach(_(data))
  }
}

// Usage
val bus = new EventBus()
bus.subscribe("message", data => println(s"Handler 1: $data"))
bus.subscribe("message", data => println(s"Handler 2: $data"))
bus.publish("message", "Hello")
```

### UI Mediator

```scala
class FormMediator {
  private var buttonEnabled = false
  private var textValid = false

  def textChanged(valid: Boolean): Unit = {
    textValid = valid
    updateButton()
  }

  def checkboxChanged(checked: Boolean): Unit = {
    updateButton()
  }

  private def updateButton(): Unit = {
    buttonEnabled = textValid
    println(s"Submit button enabled: $buttonEnabled")
  }
}
```

### Type-Safe Mediator

```scala
sealed trait Event
case class TextChanged(valid: Boolean) extends Event
case class CheckboxChanged(checked: Boolean) extends Event

class TypeSafeMediator {
  private var handlers: List[Event => Unit] = List()

  def subscribe(handler: Event => Unit): Unit = {
    handlers = handlers :+ handler
  }

  def publish(event: Event): Unit = {
    handlers.foreach(_(event))
  }
}
```

## Best Practices

- Keep mediators focused on coordination, not business logic
- Use immutable data structures for thread safety
- Document the events and their handlers
- Consider using case classes for event types
- Implement cleanup logic when components are removed

## Interview Questions

1. How does the mediator pattern differ from the observer pattern?
2. When should you use a mediator vs direct communication?
3. How do you handle mediator cleanup when components are dropped?
4. How do you test components that depend on a mediator?
5. What are the thread-safety considerations for mediators?

## References

- [Traits](https://docs.scala-lang.org/tour/traits.html)
- [Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
