# Adapter Design Pattern

## Overview
Adapter pattern converts the interface of a class into another interface clients expect. It lets classes work together that couldn't otherwise because of incompatible interfaces.

## When to Use
- You want to use an existing class but its interface does not match the one you need
- You need to integrate third-party libraries with incompatible interfaces
- You want to create a reusable class that cooperates with unrelated classes

## Code Example

```java
public interface MediaPlayer {
    void play(String audioType, String fileName);
}

public class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file. Name: " + fileName);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media. " + audioType + " format not supported");
        }
    }
}
```

## Common Mistakes
- Overusing adapters when a simple interface redesign would work
- Creating too many adapter layers that hurt performance
- Not documenting why an adapter exists

## Interview Questions
1. What is the difference between Adapter and Facade patterns?
2. Can you implement Adapter using inheritance instead of composition?
3. How does the Adapter pattern relate to the Open/Closed Principle?

## Performance

Adapter adds minimal overhead — a single method delegation per call (~5-10ns). The cost is negligible compared to I/O or business logic. In performance-critical hot paths, consider inlining the adaptation directly. Multiple adapter layers (adapter of adapter) compound the overhead, so flatten when possible.

## Examples

```java
// Target interface clients expect
interface Charger {
    void charge();
}

// Existing class with incompatible interface
class USBCCharger {
    void plugIntoUSBSocket() {
        System.out.println("Charging via USB-C");
    }
}

// Adapter makes USBCCharger work as a Charger
class USBCToChargerAdapter implements Charger {
    private final USBCCharger usbc;
    
    USBCToChargerAdapter(USBCCharger usbc) {
        this.usbc = usbc;
    }
    
    @Override
    public void charge() {
        usbc.plugIntoUSBSocket();
    }
}

// Client code uses Charger interface
class Phone {
    void chargeWith(Charger charger) {
        charger.charge();
    }
}

// Usage
Phone phone = new Phone();
USBCCharger usbc = new USBCCharger();
Charger adapter = new USBCToChargerAdapter(usbc);
phone.chargeWith(adapter); // Works!
```

## Internal Working

The adapter holds a reference to the adaptee object and implements the target interface. Each method in the target interface delegates to the corresponding adaptee method, potentially transforming parameters or return values. The client calls target methods; the adapter translates these to adaptee calls. No reflection or bytecode manipulation is involved — it is pure delegation.

## Why This Concept Exists

Third-party libraries, legacy systems, and OS APIs expose interfaces you cannot change. When your code expects one interface but gets another, you need an adapter. It decouples your application from external dependencies and lets you integrate incompatible components without modifying either side.

## Pitfalls

1. **Over-engineering**: If you control both sides, redesign the interface instead of adding an adapter
2. **Too many adapters**: Indicates a deeper design problem — consider a facade
3. **Performance in tight loops**: Adapter delegation in hot paths adds measurable overhead
4. **Leaky abstraction**: Adapters that transform data may hide important details from the client
5. **Testing complexity**: Adapters add a layer that must be tested separately

## References

- [Refactoring.Guru - Adapter Pattern](https://refactoring.guru/design-patterns/adapter)
- [Head First Design Patterns - Adapter Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [OpenJDK Collections - adapters in java.util](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html)
