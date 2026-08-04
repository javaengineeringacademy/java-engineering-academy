# Flyweight Pattern

The Flyweight pattern minimizes memory usage by sharing as much data as possible with similar objects. It separates intrinsic state (shared) from extrinsic state (unique).

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Flyweight](#basic-flyweight)
3. [Factory for Flyweights](#factory-for-flyweights)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Flyweight?

Flyweight shares common state (intrinsic) across many objects to save memory. Unique state (extrinsic) is passed in when needed.

```
FlyweightFactory ──▶ Flyweight (shared)
                         │
                    ┌────┴────┐
                    │ intrinsic│ (shared)
                    └────┬────┘
                         │
                    ┌────┴────┐
                    │ extrinsic│ (per object)
                    └─────────┘
```

### When to Use

- Large number of similar objects
- Memory is a constraint
- Most object state can be made extrinsic
- Objects share common state

---

## Basic Flyweight

### Character Rendering

```java
// Flyweight - shared intrinsic state
public class CharacterGlyph {
    private final char character;
    private final Font font;
    private final int size;

    public CharacterGlyph(char character, Font font, int size) {
        this.character = character;
        this.font = font;
        this.size = size;
    }

    public void render(int x, int y) {
        System.out.println("Render '" + character + "' at (" + x + ", " + y + ")");
    }
}

// Context - holds extrinsic state
public class CharacterContext {
    private final CharacterGlyph glyph;
    private final int x;
    private final int y;

    public CharacterContext(CharacterGlyph glyph, int x, int y) {
        this.glyph = glyph;
        this.x = x;
        this.y = y;
    }

    public void render() {
        glyph.render(x, y);
    }
}

// Flyweight factory
public class CharacterFactory {
    private final Map<String, CharacterGlyph> cache = new HashMap<>();

    public CharacterGlyph getGlyph(char character, Font font, int size) {
        String key = character + "_" + font.getName() + "_" + size;
        return cache.computeIfAbsent(key,
            k -> new CharacterGlyph(character, font, size));
    }

    public int getGlyphCount() {
        return cache.size();
    }
}

// Usage
CharacterFactory factory = new CharacterFactory();
Font font = new Font("Arial", 12);

// Create many characters sharing same glyphs
List<CharacterContext> text = new ArrayList<>();
String message = "Hello World";
for (int i = 0; i < message.length(); i++) {
    CharacterGlyph glyph = factory.getGlyph(message.charAt(i), font, 12);
    text.add(new CharacterContext(glyph, i * 10, 0));
}

// Only 8 unique glyphs created (H, e, l, o, ' ', W, r, d)
System.out.println("Glyphs created: " + factory.getGlyphCount());

text.forEach(CharacterContext::render);
```

---

## Factory for Flyweights

### Tree Rendering

```java
// Flyweight
public class TreeType {
    private final String name;
    private final String color;
    private final String texture;

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void render(int x, int y) {
        System.out.println("Tree: " + name + " at (" + x + "," + y + ")");
    }
}

// Context
public class Tree {
    private final int x;
    private final int y;
    private final TreeType type;

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void render() {
        type.render(x, y);
    }
}

// Factory
public class TreeFactory {
    private static final Map<String, TreeType> types = new HashMap<>();

    public static TreeType getType(String name, String color, String texture) {
        String key = name + "_" + color + "_" + texture;
        return types.computeIfAbsent(key,
            k -> new TreeType(name, color, texture));
    }

    public static int getTypeCount() {
        return types.size();
    }
}

// Forest - uses flyweights
public class Forest {
    private final List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = TreeFactory.getType(name, color, texture);
        trees.add(new Tree(x, y, type));
    }

    public void render() {
        trees.forEach(Tree::render);
    }
}

// Usage
Forest forest = new Forest();
// Plant 10000 trees - only create unique types
for (int i = 0; i < 10000; i++) {
    forest.plantTree(
        ThreadLocalRandom.current().nextInt(100),
        ThreadLocalRandom.current().nextInt(100),
        "Oak", "Green", "Rough"
    );
}
forest.render();
System.out.println("Tree types: " + TreeFactory.getTypeCount());  // 1
```

---

## Best Practices

### Do

```java
// 1. Separate intrinsic (shared) from extrinsic (unique)
public class Flyweight {
    private final String intrinsic;  // Shared
}

// 2. Use factory to manage shared instances
public class FlyweightFactory {
    private final Map<String, Flyweight> cache = new HashMap<>();
    public Flyweight get(String key) {
        return cache.computeIfAbsent(key, Flyweight::new);
    }
}

// 3. Make flyweight immutable
public final class Flyweight {
    private final String state;
}
```

### Don't

```java
// 1. Don't use when object count is small
// Flyweight adds complexity - use only when memory matters

// 2. Don't make extrinsic state part of flyweight
// Extrinsic state changes per use

// 3. Don't overuse
// Consider if simpler approach works
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Flyweight** | Share common state to save memory |
| **Intrinsic** | Shared state (immutable) |
| **Extrinsic** | Unique state (per object) |
| **Factory** | Manages shared instances |
| **Cache** | Stores flyweight instances |
| **Memory** | Reduces memory for many similar objects |
| **Use Cases** | Text rendering, game objects, connections |
