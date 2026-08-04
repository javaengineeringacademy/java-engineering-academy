# State Pattern

The State pattern allows an object to alter its behavior when its internal state changes. The object appears to change its class.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic State](#basic-state)
3. [State Machine](#state-machine)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is State?

State encapsulates varying behavior for each state into separate classes.

```
Context ──▶ State ──▶ StateA (behavior)
              │
         StateB (behavior)
         StateC (behavior)
```

### When to Use

- Object behavior depends on its state
- Large conditional statements based on state
- State transitions are complex

---

## Basic State

### Vending Machine

```java
// State interface
public interface VendingState {
    void insertCoin(VendingMachine machine);
    void selectItem(VendingMachine machine);
    void dispense(VendingMachine machine);
}

// Concrete states
public class IdleState implements VendingState {
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin inserted");
        machine.setState(new HasCoinState());
    }

    @Override
    public void selectItem(VendingMachine machine) {
        System.out.println("Insert coin first");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Insert coin first");
    }
}

public class HasCoinState implements VendingState {
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin already inserted");
    }

    @Override
    public void selectItem(VendingMachine machine) {
        System.out.println("Item selected");
        machine.setState(new DispensingState());
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Select item first");
    }
}

public class DispensingState implements VendingState {
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Please wait, dispensing");
    }

    @Override
    public void selectItem(VendingMachine machine) {
        System.out.println("Please wait, dispensing");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Item dispensed");
        machine.setState(new IdleState());
    }
}

// Context
public class VendingMachine {
    private VendingState state = new IdleState();

    public void setState(VendingState state) {
        this.state = state;
    }

    public void insertCoin() { state.insertCoin(this); }
    public void selectItem() { state.selectItem(this); }
    public void dispense() { state.dispense(this); }
}

// Usage
VendingMachine vm = new VendingMachine();
vm.selectItem();    // "Insert coin first"
vm.insertCoin();    // "Coin inserted"
vm.insertCoin();    // "Coin already inserted"
vm.selectItem();    // "Item selected"
vm.dispense();      // "Item dispensed"
```

---

## State Machine

### TCP Connection States

```java
public interface TcpState {
    void open(TcpConnection connection);
    void close(TcpConnection connection);
    void acknowledge(TcpConnection connection);
}

public class ClosedState implements TcpState {
    @Override
    public void open(TcpConnection connection) {
        System.out.println("Sending SYN");
        connection.setState(new ListeningState());
    }

    @Override
    public void close(TcpConnection connection) {
        System.out.println("Already closed");
    }

    @Override
    public void acknowledge(TcpConnection connection) {
        System.out.println("Not connected");
    }
}

public class ListeningState implements TcpState {
    @Override
    public void open(TcpConnection connection) {
        System.out.println("Sending SYN-ACK");
        connection.setState(new EstablishedState());
    }

    @Override
    public void close(TcpConnection connection) {
        System.out.println("Closing connection");
        connection.setState(new ClosedState());
    }

    @Override
    public void acknowledge(TcpConnection connection) {
        System.out.println("Waiting for ACK");
    }
}

public class EstablishedState implements TcpState {
    @Override
    public void open(TcpConnection connection) {
        System.out.println("Already connected");
    }

    @Override
    public void close(TcpConnection connection) {
        System.out.println("Sending FIN");
        connection.setState(new ClosedState());
    }

    @Override
    public void acknowledge(TcpConnection connection) {
        System.out.println("Data acknowledged");
    }
}

public class TcpConnection {
    private TcpState state = new ClosedState();

    public void setState(TcpState state) { this.state = state; }
    public void open() { state.open(this); }
    public void close() { state.close(this); }
    public void acknowledge() { state.acknowledge(this); }
}

// Usage
TcpConnection tcp = new TcpConnection();
tcp.open();        // Sending SYN
tcp.open();        // Sending SYN-ACK
tcp.acknowledge(); // Data acknowledged
tcp.close();       // Sending FIN
```

---

## Best Practices

### Do

```java
// 1. Keep state classes small and focused
public class StateA implements State {
    @Override
    public void handle(Context context) {
        // Specific behavior for state A
        context.setState(new StateB());
    }
}

// 2. Encapsulate transitions
public void transitionTo(State newState) {
    this.state = newState;
}
```

### Don't

```java
// 1. Don't have too many states
// Consider state machine diagram

// 2. Don't let states know about each other
// Use context for transitions
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **State** | Behavior depends on internal state |
| **Context** | Maintains current state |
| **State Classes** | Encapsulate state-specific behavior |
| **Transitions** | Change state based on events |
| **vs Switch** | More flexible than conditional logic |
| **Use Cases** | Games, workflows, protocols |
