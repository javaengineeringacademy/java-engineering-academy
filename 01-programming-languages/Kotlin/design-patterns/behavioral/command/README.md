# Command Pattern (Kotlin)

## Overview

The Command pattern encapsulates a request as an object, letting you parameterize
clients with different requests, queue requests, and support undo operations. Kotlin's
lambdas and data classes enable concise command implementations.

## When to Use

- Parameterizing objects with operations
- Queueing, logging, or supporting undo
- Decoupling sender from receiver
- Implementing transactional behavior

## Kotlin Implementation

### Functional Command

```kotlin
data class Command(
    val execute: () -> Unit,
    val undo: () -> Unit
)

class Light {
    fun on() = println("Light on")
    fun off() = println("Light off")
}

fun lightOnCommand(light: Light) = Command(
    execute = { light.on() },
    undo = { light.off() }
)
```

### Sealed Class Command

```kotlin
sealed class Command {
    abstract fun execute()
    abstract fun undo()

    class LightOn(private val light: Light) : Command() {
        override fun execute() = light.on()
        override fun undo() = light.off()
    }

    class LightOff(private val light: Light) : Command() {
        override fun execute() = light.off()
        override fun undo() = light.on()
    }
}
```

### Command History

```kotlin
class CommandHistory {
    private val history = mutableListOf<Command>()
    private val undos = mutableListOf<Command>()

    fun execute(command: Command) {
        command.execute()
        history.add(command)
        undos.clear()
    }

    fun undo() {
        history.removeLastOrNull()?.let { command ->
            command.undo()
            undos.add(command)
        }
    }

    fun redo() {
        undos.removeLastOrNull()?.let { command ->
            command.execute()
            history.add(command)
        }
    }
}
```

### Macro Command

```kotlin
class MacroCommand : Command {
    private val commands = mutableListOf<Command>()

    fun add(command: Command) {
        commands.add(command)
    }

    override fun execute() {
        commands.forEach { it.execute() }
    }

    override fun undo() {
        commands.reversed().forEach { it.undo() }
    }
}
```

## Best Practices

- Use lambdas for simple commands
- Keep commands focused and single-purpose
- Support undo operations when needed
- Use parameterized commands for variations
- Document command lifecycle

## Interview Questions

1. How does Command enable undo functionality?
2. What is the difference between Command and Strategy?
3. Can commands be composed into macros?
4. How do you handle command queuing?
5. When should you use Command over direct method calls?

## References

- Kotlin documentation: Lambdas
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
