# Prototype Pattern

## Overview
 Prototype creates new objects by copying an existing instance, avoiding expensive initialization.

## When to Use
- Object creation is expensive
- You need many similar objects
- Avoiding subclassing
- Runtime determines object type

## Code Structure

### Prototype Interface
```java
public interface Shape extends Cloneable {
    Shape clone();
    void draw();
}
```

### Concrete Prototype
```java
public class Circle implements Shape {
    private double radius;
    private String color;

    public Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }
}
```

### Usage
```java
Circle original = new Circle(5.0, "red");
Circle copy = original.clone();
copy.setColor("blue"); // Original unchanged
```

## Common Mistakes
1. Shallow copying mutable fields
2. Not implementing Cloneable properly
3. Forgetting to deep copy collections
4. Creating too many prototype registries

## Interview Questions
1. What is the difference between shallow and deep copy?
2. How does Prototype differ from Factory Method?
3. When would you use Prototype instead of Factory?
4. What are the problems with Java's Cloneable?
5. How do you implement a prototype registry?

## Performance

Cloning is typically faster than constructing from scratch — especially for objects with expensive initialization (database connections, network resources, complex computation). Shallow copy is O(1) for primitive fields; deep copy is O(n) for collections. The `clone()` method avoids constructor overhead (final fields can be set without reflection).

## Examples

```java
// Prototype registry for game characters
class GameCharacter implements Cloneable {
    private String type;
    private int health;
    private int attack;
    private List<String> abilities;
    
    GameCharacter(String type, int health, int attack, List<String> abilities) {
        this.type = type;
        this.health = health;
        this.attack = attack;
        this.abilities = new ArrayList<>(abilities);
    }
    
    // Copy constructor (preferred over clone)
    GameCharacter(GameCharacter other) {
        this.type = other.type;
        this.health = other.health;
        this.attack = other.attack;
        this.abilities = new ArrayList<>(other.abilities); // deep copy
    }
    
    public GameCharacter clone() {
        return new GameCharacter(this);
    }
    
    public void setHealth(int health) { this.health = health; }
    public String getType() { return type; }
    public int getHealth() { return health; }
    public List<String> getAbilities() { return abilities; }
}

class CharacterRegistry {
    private final Map<String, GameCharacter> prototypes = new HashMap<>();
    
    void register(String key, GameCharacter prototype) {
        prototypes.put(key, prototype);
    }
    
    GameCharacter create(String key) {
        GameCharacter prototype = prototypes.get(key);
        return prototype != null ? prototype.clone() : null;
    }
}

// Usage
CharacterRegistry registry = new CharacterRegistry();
registry.register("warrior", new GameCharacter("Warrior", 100, 20, List.of("Slash", "Shield")));
registry.register("mage", new GameCharacter("Mage", 60, 40, List.of("Fireball", "Teleport")));

GameCharacter player1 = registry.create("warrior");
GameCharacter player2 = registry.create("mage");
player1.setHealth(80); // Original unchanged
```

## Internal Working

The prototype pattern uses the clone mechanism (or copy constructor) to create new instances from existing ones. The `clone()` method copies all fields from the source object to the new instance. Shallow copy shares references to mutable objects; deep copy creates independent copies. A prototype registry stores pre-built prototypes keyed by name, enabling runtime creation without knowing concrete classes.

## Why This Concept Exists

Creating objects from scratch can be expensive: database queries, network calls, complex calculations, or reading large files. Prototype avoids this by copying existing instances. It also enables creating objects without knowing their concrete types — you clone a registered prototype. This is useful for document templates, game entities, and configuration objects.

## Pitfalls

1. **Shallow copy bugs**: Mutable fields (lists, maps) are shared between original and clone — use deep copy
2. **Cloneable is broken**: Java's `Cloneable` has known issues (no generics, breaks invariants) — prefer copy constructors
3. **Final fields**: Cannot be set in `clone()` without reflection — design classes with this in mind
4. **Registry management**: Prototypes must be kept in sync with class changes
5. **Over-cloning**: If objects are cheap to create, prototype adds unnecessary complexity

## References

- [Effective Java - Item 13: Override clone judiciously](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Refactoring.Guru - Prototype Pattern](https://refactoring.guru/design-patterns/prototype)
- [Oracle Cloneable Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Cloneable.html)
