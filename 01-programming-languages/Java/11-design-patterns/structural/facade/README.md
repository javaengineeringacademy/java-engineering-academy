# Facade Design Pattern

## Overview
Facade pattern provides a unified interface to a set of interfaces in a subsystem. It defines a higher-level interface that makes the subsystem easier to use.

## When to Use
- You want to provide a simple interface to a complex subsystem
- There are many dependencies between clients and the implementation classes
- You want to layer your subsystems and define entry points to each level

## Code Example

```java
public class Computer {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;

    public Computer() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void start() {
        System.out.println("Computer: Starting up...");
        cpu.freeze();
        memory.load(0L, "boot");
        cpu.jump(0L);
        cpu.execute();
    }
}
```

## Common Mistakes
- Putting too much logic in the facade itself
- Making the facade a god class that knows everything
- Not allowing direct access to subsystem classes when needed

## Interview Questions
1. What is the difference between Facade and Mediator patterns?
2. Does Facade add new functionality or just simplify existing?
3. Can Facade be combined with other patterns?

## Performance

Facade adds minimal overhead — it delegates to subsystem methods without adding logic. The benefit is architectural: reducing coupling between client and subsystem. For hot paths, the facade is negligible. In some cases, the facade can optimize by batching subsystem calls or caching results.

## Examples

```java
// Home theater facade
class Amplifier {
    void on() { System.out.println("Amp on"); }
    void setVolume(int level) { System.out.println("Volume: " + level); }
}

class DvdPlayer {
    void on() { System.out.println("DVD on"); }
    void play(String movie) { System.out.println("Playing: " + movie); }
}

class Projector {
    void on() { System.out.println("Projector on"); }
    void setWideScreen() { System.out.println("Widescreen mode"); }
}

class HomeTheaterFacade {
    private final Amplifier amp;
    private final DvdPlayer dvd;
    private final Projector projector;
    
    HomeTheaterFacade(Amplifier amp, DvdPlayer dvd, Projector projector) {
        this.amp = amp;
        this.dvd = dvd;
        this.projector = projector;
    }
    
    void watchMovie(String movie) {
        System.out.println("Get ready to watch " + movie);
        projector.on();
        projector.setWideScreen();
        amp.on();
        amp.setVolume(8);
        dvd.on();
        dvd.play(movie);
    }
    
    void endMovie() {
        System.out.println("Shutting movie theater down");
        dvd.on(); // stop
        amp.on(); // off
        projector.on(); // off
    }
}

// Usage
HomeTheaterFacade theater = new HomeTheaterFacade(
    new Amplifier(), new DvdPlayer(), new Projector());
theater.watchMovie("Inception"); // One simple call
```

## Internal Working

The facade class holds references to subsystem components. It provides simplified methods that orchestrate calls to multiple subsystem methods. The client calls facade methods instead of interacting with subsystem classes directly. The facade does not add new behavior — it coordinates existing behavior. Clients can still access subsystem classes directly if they need fine-grained control.

## Why This Concept Exists

Complex subsystems have many classes with complex dependencies. Using them requires understanding the entire subsystem. A facade provides a simple interface that hides complexity. For example, a `JdbcTemplate` is a facade over raw JDBC connections, statements, and result sets. The facade does not add functionality — it makes existing functionality accessible.

## Pitfalls

1. **God object**: Facade that does too much becomes a maintenance burden
2. **Hiding useful features**: Facade may oversimplify and hide important subsystem capabilities
3. **Tight coupling**: Client depends on facade; changes to facade break all clients
4. **Not a replacement**: Facade simplifies but does not replace direct subsystem access when needed
5. **Overuse**: Every subsystem having a facade adds unnecessary indirection

## References

- [Refactoring.Guru - Facade Pattern](https://refactoring.guru/design-patterns/facade)
- [Head First Design Patterns - Facade Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Spring JdbcTemplate](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html)
