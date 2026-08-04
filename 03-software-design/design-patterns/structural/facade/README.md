# Facade Pattern

The Facade pattern provides a simplified interface to a complex subsystem. It doesn't add new functionality, just simplifies access to existing functionality.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Facade](#basic-facade)
3. [Simplifying Subsystems](#simplifying-subsystems)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Facade?

Facade provides a unified interface to a set of interfaces in a subsystem. It simplifies usage without changing subsystem functionality.

```
Client ──▶ Facade ──▶ SubsystemA
                    ──▶ SubsystemB
                    ──▶ SubsystemC
```

### When to Use

- Simplify complex library or framework usage
- Provide simple interface to complex subsystem
- Layer subsystems and define entry points

---

## Basic Facade

### Computer Facade

```java
// Complex subsystems
public class CPU {
    public void freeze() { System.out.println("CPU: Freeze"); }
    public void jump(long address) { System.out.println("CPU: Jump to " + address); }
    public void execute() { System.out.println("CPU: Execute"); }
}

public class Memory {
    public void load(long address, String data) {
        System.out.println("Memory: Load " + data + " at " + address);
    }
}

public class HardDrive {
    public String read(long sector, int size) {
        System.out.println("HardDrive: Read sector " + sector);
        return "boot_data";
    }
}

// Facade
public class ComputerFacade {
    private final CPU cpu = new CPU();
    private final Memory memory = new Memory();
    private final HardDrive hardDrive = new HardDrive();

    public void start() {
        System.out.println("Starting computer...");
        cpu.freeze();
        memory.load(0, "BOOT");
        cpu.jump(0);
        cpu.execute();
        System.out.println("Computer started!");
    }

    public void shutdown() {
        System.out.println("Shutting down computer...");
        // Complex shutdown sequence
    }
}

// Usage - simple interface
ComputerFacade computer = new ComputerFacade();
computer.start();  // Complex sequence hidden
```

### Home Theater Facade

```java
public class Amplifier {
    public void on() { System.out.println("Amp on"); }
    public void off() { System.out.println("Amp off"); }
    public void setVolume(int level) { System.out.println("Volume: " + level); }
}

public class DvdPlayer {
    public void on() { System.out.println("DVD on"); }
    public void off() { System.out.println("DVD off"); }
    public void play(String movie) { System.out.println("Playing: " + movie); }
}

public class Projector {
    public void on() { System.out.println("Projector on"); }
    public void off() { System.out.println("Projector off"); }
    public void setWideScreenMode() { System.out.println("Widescreen mode"); }
}

public class Lights {
    public void dim(int level) { System.out.println("Lights dimmed to " + level); }
}

// Facade
public class HomeTheaterFacade {
    private final Amplifier amp;
    private final DvdPlayer dvd;
    private final Projector projector;
    private final Lights lights;

    public HomeTheaterFacade(Amplifier amp, DvdPlayer dvd,
                             Projector projector, Lights lights) {
        this.amp = amp;
        this.dvd = dvd;
        this.projector = projector;
        this.lights = lights;
    }

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch " + movie);
        lights.dim(10);
        projector.on();
        projector.setWideScreenMode();
        amp.on();
        amp.setVolume(5);
        dvd.on();
        dvd.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting movie theater down...");
        dvd.off();
        amp.off();
        projector.off();
        lights.dim(100);
    }
}

// Usage
HomeTheaterFacade theater = new HomeTheaterFacade(
    new Amplifier(), new DvdPlayer(), new Projector(), new Lights());
theater.watchMovie("Inception");
theater.endMovie();
```

---

## Simplifying Subsystems

### Order Processing Facade

```java
// Complex subsystems
public class InventoryService {
    public boolean checkStock(String productId) { return true; }
    public void reserveStock(String productId, int qty) {}
}

public class PaymentService {
    public boolean processPayment(String orderId, double amount) { return true; }
}

public class ShippingService {
    public String createShipment(String orderId, String address) { return "SHIP001"; }
}

public class NotificationService {
    public void sendConfirmation(String orderId, String email) {}
}

// Facade
public class OrderFacade {
    private final InventoryService inventory;
    private final PaymentService payment;
    private final ShippingService shipping;
    private final NotificationService notification;

    public OrderFacade() {
        this.inventory = new InventoryService();
        this.payment = new PaymentService();
        this.shipping = new ShippingService();
        this.notification = new NotificationService();
    }

    public OrderResult placeOrder(Order order) {
        // Simple interface hiding complexity
        if (!inventory.checkStock(order.productId())) {
            return OrderResult.outOfStock();
        }

        inventory.reserveStock(order.productId(), order.quantity());

        if (!payment.processPayment(order.id(), order.total())) {
            return OrderResult.paymentFailed();
        }

        String shipmentId = shipping.createShipment(order.id(), order.address());
        notification.sendConfirmation(order.id(), order.email());

        return OrderResult.success(shipmentId);
    }
}

// Client uses simple interface
OrderFacade facade = new OrderFacade();
OrderResult result = facade.placeOrder(new Order("P001", 2, "123 Main St", "alice@example.com"));
```

---

## Best Practices

### Do

```java
// 1. Keep facade simple
public class Facade {
    public void complexOperation() {
        subsystem1.operation1();
        subsystem2.operation2();
        subsystem3.operation3();
    }
}

// 2. Delegate to subsystem
public class Facade {
    private final Subsystem subsystem;
    public void operation() { subsystem.operation(); }
}
```

### Don't

```java
// 1. Don't add new functionality to facade
// Facade simplifies, doesn't extend

// 2. Don't make facade god class
// Keep it focused on one subsystem

// 3. Don't hide all subsystem methods
// Some advanced users may need direct access
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Facade** | Simplified interface to complex subsystem |
| **Simplification** | Doesn't add functionality |
| **Delegation** | Routes calls to subsystem |
| **Encapsulation** | Hides subsystem complexity |
| **Use Cases** | Libraries, frameworks, complex operations |
| **vs Adapter** | Facade simplifies, Adapter converts interface |
