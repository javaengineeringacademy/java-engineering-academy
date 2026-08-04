# Mediator Pattern

The Mediator pattern defines an object that encapsulates how a set of objects interact. It promotes loose coupling by preventing objects from referring to each other explicitly.

## Table of Contents

1. [Concepts](#concepts)
2. [Chat Room](#chat-room)
3. [Air Traffic Control](#air-traffic-control)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Mediator?

Mediator centralizes complex communications and control logic between objects.

```
Colleague1 ──▶ Mediator ◀── Colleague2
                   │
            Colleague3
```

### When to Use

- Multiple objects communicate in complex ways
- You want to reduce direct dependencies
- Communication logic is distributed

---

## Chat Room

### Basic Chat

```java
// Mediator
public interface ChatRoom {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

// Concrete mediator
public class BasicChatRoom implements ChatRoom {
    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void sendMessage(String message, User sender) {
        users.stream()
            .filter(user -> user != sender)
            .forEach(user -> user.receive(message, sender.getName()));
    }
}

// Colleague
public class User {
    private final String name;
    private final ChatRoom chatRoom;

    public User(String name, ChatRoom chatRoom) {
        this.name = name;
        this.chatRoom = chatRoom;
        chatRoom.addUser(this);
    }

    public void send(String message) {
        chatRoom.sendMessage(message, this);
    }

    public void receive(String message, String senderName) {
        System.out.println(senderName + " to " + name + ": " + message);
    }

    public String getName() { return name; }
}

// Usage
ChatRoom room = new BasicChatRoom();
User alice = new User("Alice", room);
User bob = new User("Bob", room);
User charlie = new User("Charlie", room);

alice.send("Hello everyone!");
// Bob receives: Alice to Bob: Hello everyone!
// Charlie receives: Alice to Charlie: Hello everyone!
```

---

## Air Traffic Control

### ATC Mediator

```java
// Mediator
public interface AirTrafficControl {
    void registerAircraft(Aircraft aircraft);
    void requestLanding(Aircraft aircraft);
    void requestTakeoff(Aircraft aircraft);
    boolean isRunwayAvailable();
}

// Concrete mediator
public class Tower implements AirTrafficControl {
    private final List<Aircraft> aircrafts = new ArrayList<>();
    private boolean runwayAvailable = true;

    @Override
    public void registerAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
    }

    @Override
    public void requestLanding(Aircraft aircraft) {
        if (runwayAvailable) {
            runwayAvailable = false;
            System.out.println("Tower: " + aircraft.getName() + " cleared to land");
            aircraft.notify("Cleared to land");
        } else {
            System.out.println("Tower: " + aircraft.getName() + " hold short");
            aircraft.notify("Hold short - runway busy");
        }
    }

    @Override
    public void requestTakeoff(Aircraft aircraft) {
        System.out.println("Tower: " + aircraft.getName() + " cleared for takeoff");
        aircraft.notify("Cleared for takeoff");
        runwayAvailable = true;
    }

    @Override
    public boolean isRunwayAvailable() { return runwayAvailable; }
}

// Colleague
public class Aircraft {
    private final String name;
    private final AirTrafficControl atc;

    public Aircraft(String name, AirTrafficControl atc) {
        this.name = name;
        this.atc = atc;
        atc.registerAircraft(this);
    }

    public void requestLanding() { atc.requestLanding(this); }
    public void requestTakeoff() { atc.requestTakeoff(this); }
    public void notify(String message) {
        System.out.println(name + " received: " + message);
    }
    public String getName() { return name; }
}

// Usage
AirTrafficControl tower = new Tower();
Aircraft flight1 = new Aircraft("Flight101", tower);
Aircraft flight2 = new Aircraft("Flight202", tower);

flight1.requestLanding();  // Cleared to land
flight2.requestLanding();  // Hold short - runway busy
```

---

## Best Practices

### Do

```java
// 1. Keep mediator focused
public class ChatMediator implements Mediator {
    private final List<Colleague> colleagues = new ArrayList<>();

    @Override
    public void notify(Colleague sender, String event) {
        colleagues.stream()
            .filter(c -> c != sender)
            .forEach(c -> c.handleEvent(event));
    }
}
```

### Don't

```java
// 1. Don't make mediator god class
// Keep it focused on coordination

// 2. Don't let colleagues communicate directly
// Always go through mediator
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Mediator** | Centralizes object communication |
| **Colleague** | Objects that communicate via mediator |
| **Loose Coupling** | Colleagues don't reference each other |
| **Centralization** | Communication logic in one place |
| **Use Cases** | Chat, air traffic, UI coordination |
| **Trade-off** | Mediator can become complex |
