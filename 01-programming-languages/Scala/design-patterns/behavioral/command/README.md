# Command Pattern in Scala

The Command pattern encapsulates a request as an object. In Scala, this is implemented using case classes, traits, or function objects.

## When to Use

- Undo/redo functionality
- Task queueing and scheduling
- Transaction systems
- Macro recording
- Decoupling invoker from receiver

## Implementation

### Case Class Command

```scala
sealed trait Command {
  def execute(editor: TextEditor): Unit
  def undo(editor: TextEditor): Unit
}

case class Insert(position: Int, text: String) extends Command {
  def execute(editor: TextEditor): Unit = editor.insert(position, text)
  def undo(editor: TextEditor): Unit = editor.delete(position, text.length)
}

case class Delete(position: Int, length: Int) extends Command {
  def execute(editor: TextEditor): Unit = editor.delete(position, length)
  def undo(editor: TextEditor): Unit = editor.insert(position, "deleted")
}

class TextEditor {
  private var content = ""
  private var history: List[Command] = List()

  def insert(position: Int, text: String): Unit = {
    content = content.take(position) + text + content.drop(position)
  }

  def delete(position: Int, length: Int): Unit = {
    content = content.take(position) + content.drop(position + length)
  }

  def execute(command: Command): Unit = {
    command.execute(this)
    history = history :+ command
  }

  def undo(): Unit = history.lastOption.foreach { command =>
    command.undo(this)
    history = history.init
  }
}
```

### Function Object Command

```scala
case class TaskQueue() {
  private var tasks: List[() => Unit] = List()

  def enqueue(task: () => Unit): Unit = {
    tasks = tasks :+ task
  }

  def executeAll(): Unit = {
    tasks.foreach(_())
    tasks = List()
  }
}

// Usage
val queue = TaskQueue()
queue.enqueue(() => println("Task 1"))
queue.enqueue(() => println("Task 2"))
queue.executeAll()
```

### Macro Command

```scala
case class MacroCommand(commands: List[Command]) extends Command {
  def execute(editor: TextEditor): Unit = commands.foreach(_.execute(editor))
  def undo(editor: TextEditor): Unit = commands.reverse.foreach(_.undo(editor))
}
```

### Undo/Redo

```scala
class UndoRedoEditor {
  private var content = ""
  private var undoStack: List[Command] = List()
  private var redoStack: List[Command] = List()

  def execute(command: Command): Unit = {
    command.execute(this)
    undoStack = undoStack :+ command
    redoStack = List()
  }

  def undo(): Unit = undoStack.lastOption.foreach { command =>
    command.undo(this)
    undoStack = undoStack.init
    redoStack = redoStack :+ command
  }

  def redo(): Unit = redoStack.lastOption.foreach { command =>
    command.execute(this)
    redoStack = redoStack.init
    undoStack = undoStack :+ command
  }
}
```

## Best Practices

- Use sealed traits for command hierarchies
- Implement both `execute` and `undo` for reversible commands
- Use case classes for simple commands with data
- Document command sequencing requirements
- Consider using function objects for simple commands

## Interview Questions

1. How does Scala's immutability model affect command pattern implementation?
2. What is the difference between case class commands and function objects?
3. How do you implement undo/redo with the command pattern?
4. How do you serialize commands for persistence?
5. When should you use sealed traits vs function types for commands?

## References

- [Case Classes](https://docs.scala-lang.org/tour/case-classes.html)
- [Sealed Traits](https://docs.scala-lang.org/tour/polymorphic-types.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
