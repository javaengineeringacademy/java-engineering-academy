# LSP - Liskov Substitution Principle

## Overview

Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

## Violations

### Behavioral Incompatibility
```java
// BAD - Square breaks Rectangle behavior
public class Rectangle {
    protected int width, height;
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int area() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Breaks LSP
    }
    
    @Override
    public void setHeight(int height) {
        this.width = height;
        this.height = height; // Breaks LSP
    }
}

// Client code breaks
void resize(Rectangle r) {
    r.setWidth(5);
    r.setHeight(10);
    assert r.area() == 50; // Fails for Square!
}
```

### Proper Abstraction
```java
// GOOD - Immutable shapes
public interface Shape {
    int area();
}

public class Rectangle implements Shape {
    private final int width, height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public int area() { return width * height; }
}

public class Square implements Shape {
    private final int side;
    
    public Square(int side) {
        this.side = side;
    }
    
    @Override
    public int area() { return side * side; }
}
```

### Exception Handling
```java
// BAD - Subclass throws different exception
public class BaseRepository {
    public Entity findById(Long id) {
        throw new NotFoundException("Not found");
    }
}

public class SpecificRepository extends BaseRepository {
    @Override
    public Entity findById(Long id) {
        throw new DatabaseException("Connection failed"); // Different exception
    }
}

// GOOD - Consistent exception behavior
public interface Repository<T> {
    T findById(Long id) throws RepositoryException;
}

public class BaseRepository<T> implements Repository<T> {
    @Override
    public T findById(Long id) throws RepositoryException {
        throw new RepositoryException("Not found");
    }
}
```

## Rules

1. Preconditions cannot be strengthened in subclasses
2. Postconditions cannot be weakened in subclasses
3. Invariants must be preserved
4. History constraint: History restriction must not be violated

## Best Practices

1. Use interfaces for type contracts
2. Ensure behavioral compatibility
3. Test substitutability
4. Prefer composition over inheritance
5. Design for extensibility
6. Document behavioral contracts
7. Use abstract classes judiciously
8. Verify LSP compliance with tests
