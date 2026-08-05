# Observer Pattern in Scala

The Observer pattern defines a one-to-many dependency between objects. In Scala, this is implemented using function types, actors, or reactive streams.

## When to Use

- Event-driven architectures
- UI notification systems
- Model-view separation
- Distributed systems communication
- Reactive programming

## Implementation

### Function-Based Observer

```scala
class EventEmitter[T] {
  private var listeners: List[T => Unit] = List()

  def on(listener: T => Unit): Unit = {
    listeners = listeners :+ listener
  }

  def emit(event: T): Unit = {
    listeners.foreach(_(event))
  }
}

// Usage
val emitter = new EventEmitter[String]
emitter.on(event => println(s"Listener 1: $event"))
emitter.on(event => println(s"Listener 2: $event"))
emitter.emit("Hello")
```

### Property Observer

```scala
class Observable[T](private var _value: T) {
  private var observers: List[T => Unit] = List()

  def value: T = _value

  def value_=(newValue: T): Unit = {
    _value = newValue
    notifyObservers(newValue)
  }

  def subscribe(observer: T => Unit): Unit = {
    observers = observers :+ observer
  }

  private def notifyObservers(value: T): Unit = {
    observers.foreach(_(value))
  }
}

// Usage
val observable = new Observable(0)
observable.subscribe(v => println(s"Changed to: $v"))
observable.value = 5
```

### Actor-Based Observer

```scala
import akka.actor._

sealed trait Message
case class Notify(data: String) extends Message
case class Subscribe(observer: ActorRef) extends Message

class EventActor extends Actor {
  private var observers: List[ActorRef] = List()

  def receive: Receive = {
    case Subscribe(observer) =>
      observers = observers :+ observer
    case Notify(data) =>
      observers.foreach(_ ! Notify(data))
  }
}

// Usage
val system = ActorSystem("ObserverSystem")
val eventActor = system.actorOf(Props[EventActor], "eventActor")
```

### Observable Collection

```scala
import scala.collection.mutable.ListBuffer

class ObservableList[T] {
  private val list = ListBuffer[T]()
  private var onChange: List[T => Unit] = List()

  def add(item: T): Unit = {
    list += item
    onChange.foreach(_(list.toList))
  }

  def subscribe(observer: List[T] => Unit): Unit = {
    onChange = onChange :+ observer
  }
}
```

## Best Practices

- Use function types for simple in-process observers
- Use actors for cross-thread observer notifications
- Consider using reactive libraries (Akka Streams, ZIO) for complex cases
- Implement cleanup logic for observer unsubscription
- Document thread-safety guarantees of observer implementations

## Interview Questions

1. How does Scala's immutability model affect the observer pattern?
2. What is the difference between function-based and actor-based observers?
3. How do you handle observer cleanup when subscribers are dropped?
4. How do you implement thread-safe observers in Scala?
5. When should you use reactive streams vs simple observers?

## References

- [Function Types](https://docs.scala-lang.org/tour/higher-order-functions.html)
- [Akka Actors](https://doc.akka.io/docs/akka/current/)
- [Effective Scala](https://twitter.github.io/effectivescala/)
