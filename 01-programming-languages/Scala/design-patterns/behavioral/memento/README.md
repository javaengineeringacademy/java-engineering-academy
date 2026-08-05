# Memento Pattern in Scala

The Memento pattern captures and externalizes an object's internal state so it can be restored later. In Scala, case classes and immutable data structures provide natural memento implementations.

## When to Use

- Undo/redo functionality
- State restoration
- Checkpointing
- Transaction rollback
- Version control systems

## Implementation

### Case Class Memento

```scala
case class EditorMemento(content: String, cursorPosition: Int)

class Editor {
  private var content = ""
  private var cursorPosition = 0
  private var history: List[EditorMemento] = List()

  def save(): Unit = {
    history = history :+ EditorMemento(content, cursorPosition)
  }

  def typeText(text: String): Unit = {
    content = content.take(cursorPosition) + text + content.drop(cursorPosition)
    cursorPosition += text.length
  }

  def undo(): Unit = history.lastOption.foreach { memento =>
    content = memento.content
    cursorPosition = memento.cursorPosition
    history = history.init
  }
}
```

### Stack-Based Memento

```scala
case class GameState(level: Int, score: Long, health: Double)

class Game {
  private var state = GameState(1, 0, 100.0)
  private var saveStates: List[GameState] = List()

  def save(): Unit = saveStates = saveStates :+ state

  def load(): Unit = saveStates.lastOption.foreach { saved =>
    state = saved
    saveStates = saveStates.init
  }

  def play(): Unit = {
    state = state.copy(
      score = state.score + 100,
      health = state.health - 10.0
    )
    println(s"Playing... Score: ${state.score}, Health: ${state.health}")
  }
}
```

### Memento with Versioning

```scala
case class Config(databaseUrl: String, maxConnections: Int, version: Int)

class ConfigManager {
  private var config = Config("", 10, 0)
  private var history: List[Config] = List()

  def update(url: String, max: Int): Unit = {
    history = history :+ config
    config = Config(url, max, config.version + 1)
  }

  def rollback(): Unit = history.lastOption.foreach { prev =>
    config = prev
    history = history.init
  }

  def getVersion: Int = config.version
}
```

### Immutable Memento

```scala
case class ImmutableEditor(
  content: String,
  cursorPosition: Int,
  history: List[(String, Int)] = List()
) {
  def typeText(text: String): ImmutableEditor = {
    val newContent = content.take(cursorPosition) + text + content.drop(cursorPosition)
    ImmutableEditor(
      newContent,
      cursorPosition + text.length,
      history :+ (content, cursorPosition)
    )
  }

  def undo(): ImmutableEditor = history.lastOption match {
    case Some((prevContent, prevCursor)) =>
      ImmutableEditor(prevContent, prevCursor, history.init)
    case None => this
  }
}
```

## Best Practices

- Use case classes for immutable memento snapshots
- Store mementos in a list for undo/redo
- Use `copy` for creating modified versions
- Document which state is captured in each memento
- Consider using lenses for complex state updates

## Interview Questions

1. What is the difference between memento and command pattern?
2. How do you handle large state snapshots efficiently?
3. How do you implement redo functionality?
4. How do you handle concurrent access to mementos?
5. When should you avoid the memento pattern?

## References

- [Case Classes](https://docs.scala-lang.org/tour/case-classes.html)
- [Immutable Collections](https://docs.scala-lang.org/collections/)
- [Effective Scala](https://twitter.github.io/effectivescala/)
