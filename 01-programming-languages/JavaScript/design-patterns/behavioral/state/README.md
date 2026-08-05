# State Pattern (JavaScript)

## Overview

The State pattern allows an object to alter its behavior when its internal state changes.
JavaScript's object literals and functions make state implementations concise and flexible.

## When to Use

- Object behavior depends on its state
- Complex conditional statements based on state
- State transitions are explicit
- Large number of states

## JavaScript Implementation

### Object Literal States

```javascript
class VendingMachine {
  constructor() {
    this.state = this.idleState;
  }

  idleState = {
    insertCoin: () => {
      console.log('Coin inserted');
      this.state = this.hasCoinState;
    },
    dispense: () => console.log('Insert coin first')
  };

  hasCoinState = {
    insertCoin: () => console.log('Coin already inserted'),
    dispense: () => {
      console.log('Dispensing product');
      this.state = this.idleState;
    }
  };

  insertCoin() {
    this.state.insertCoin();
  }

  dispense() {
    this.state.dispense();
  }
}
```

### Functional State

```javascript
function createState(initialState) {
  let state = initialState;

  return {
    getState: () => state,
    transition: (newState) => {
      console.log(`Transitioning from ${state.name} to ${newState.name}`);
      state = newState;
    }
  };
}
```

### State Machine

```javascript
class StateMachine {
  constructor(config) {
    this.states = config.states;
    this.currentState = config.initial;
  }

  transition(event) {
    const transitions = this.states[this.currentState].transitions;
    if (transitions[event]) {
      this.currentState = transitions[event];
      return true;
    }
    return false;
  }

  getState() {
    return this.currentState;
  }
}
```

### State with Actions

```javascript
const states = {
  idle: {
    onEnter: () => console.log('Entering idle'),
    onExit: () => console.log('Exiting idle'),
    transitions: {
      start: 'running'
    }
  },
  running: {
    onEnter: () => console.log('Entering running'),
    onExit: () => console.log('Exiting running'),
    transitions: {
      stop: 'idle',
      pause: 'paused'
    }
  }
};
```

## Best Practices

- Keep state classes small and focused
- Make state transitions explicit
- Document state diagrams
- Handle invalid transitions gracefully
- Consider using state machine libraries

## Interview Questions

1. How does State differ from Strategy?
2. Can states contain behavior?
3. How do you handle invalid state transitions?
4. When should you use State vs conditional logic?
5. How do you implement state entry/exit actions?

## References

- MDN: State Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- XState library documentation
