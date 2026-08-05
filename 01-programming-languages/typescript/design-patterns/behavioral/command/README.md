# Command Pattern (TypeScript)

## Overview

The Command pattern encapsulates a request as an object, letting you parameterize
clients with different requests, queue requests, and support undo operations. TypeScript's
interfaces enable type-safe command implementations.

## When to Use

- Parameterizing objects with operations
- Queueing, logging, or supporting undo
- Decoupling sender from receiver
- Implementing transactional behavior

## TypeScript Implementation

### Typed Command

```typescript
interface Command {
  execute(): void;
  undo(): void;
}

class Light {
  on(): void {
    console.log('Light on');
  }
  off(): void {
    console.log('Light off');
  }
}

class LightOnCommand implements Command {
  constructor(private light: Light) {}

  execute(): void {
    this.light.on();
  }

  undo(): void {
    this.light.off();
  }
}
```

### Generic Command

```typescript
interface GenericCommand<T> {
  execute(target: T): void;
  undo(target: T): void;
}
```

### Command History

```typescript
class CommandHistory {
  private history: Command[] = [];
  private undos: Command[] = [];

  execute(command: Command): void {
    command.execute();
    this.history.push(command);
    this.undos = [];
  }

  undo(): void {
    const command = this.history.pop();
    if (command) {
      command.undo();
      this.undos.push(command);
    }
  }

  redo(): void {
    const command = this.undos.pop();
    if (command) {
      command.execute();
      this.history.push(command);
    }
  }
}
```

### Macro Command

```typescript
class MacroCommand implements Command {
  private commands: Command[] = [];

  add(command: Command): this {
    this.commands.push(command);
    return this;
  }

  execute(): void {
    this.commands.forEach(cmd => cmd.execute());
  }

  undo(): void {
    [...this.commands].reverse().forEach(cmd => cmd.undo());
  }
}
```

## Best Practices

- Use interfaces for type safety
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

- TypeScript Handbook: Interfaces
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
