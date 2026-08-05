# Command Pattern (JavaScript)

## Overview

The Command pattern encapsulates a request as an object, letting you parameterize
clients with different requests, queue requests, and support undo operations. JavaScript
functions and closures make command implementations concise.

## When to Use

- Parameterizing objects with operations
- Queueing, logging, or supporting undo
- Decoupling sender from receiver
- Implementing transactional behavior

## JavaScript Implementation

### Basic Command

```javascript
class Command {
  constructor(execute, undo) {
    this.execute = execute;
    this.undo = undo;
  }
}

class Light {
  on() { console.log('Light on'); }
  off() { console.log('Light off'); }
}

const lightOn = new Command(
  () => console.log('Executing: Light on'),
  () => console.log('Undoing: Light off')
);
```

### Functional Command

```javascript
function createAction(execute, undo) {
  return { execute, undo };
}

const actions = {
  increase: (state) => ({ ...state, count: state.count + 1 }),
  decrease: (state) => ({ ...state, count: state.count - 1 })
};
```

### Command History

```javascript
class CommandHistory {
  constructor() {
    this.history = [];
    this.undos = [];
  }

  execute(command) {
    command.execute();
    this.history.push(command);
    this.undos = [];
  }

  undo() {
    const command = this.history.pop();
    if (command) {
      command.undo();
      this.undos.push(command);
    }
  }

  redo() {
    const command = this.undos.pop();
    if (command) {
      command.execute();
      this.history.push(command);
    }
  }
}
```

### Macro Command

```javascript
class MacroCommand {
  constructor(commands = []) {
    this.commands = commands;
  }

  add(command) {
    this.commands.push(command);
    return this;
  }

  execute() {
    this.commands.forEach(cmd => cmd.execute());
  }

  undo() {
    [...this.commands].reverse().forEach(cmd => cmd.undo());
  }
}
```

## Best Practices

- Keep commands focused and single-purpose
- Support undo operations when needed
- Use parameterized commands for variations
- Consider command queueing for async operations
- Document command lifecycle

## Interview Questions

1. How does Command enable undo functionality?
2. What is the difference between Command and Strategy?
3. Can commands be composed into macros?
4. How do you handle command queuing?
5. When should you use Command over direct method calls?

## References

- MDN: Command Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
