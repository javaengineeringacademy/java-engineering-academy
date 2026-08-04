# KISS - Keep It Simple, Stupid

## Overview

KISS principle states that most systems work best if they are kept simple rather than made complicated.

## Violations

### Over-Engineering
```java
// BAD - Complex factory for simple object creation
public class UserFactory {
    private final Map<String, UserBuilder> builders = new ConcurrentHashMap<>();
    private final UserValidator validator;
    private final UserAssembler assembler;
    
    public User createUser(UserRequest request) {
        validateRequest(request);
        UserBuilder builder = getBuilder(request.getType());
        User user = builder.build(request);
        assembler.assemble(user);
        validateUser(user);
        return user;
    }
}

// GOOD - Simple approach
public class UserService {
    public User createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }
}
```

### Unnecessary Abstraction
```java
// BAD - Abstract class for single implementation
public interface IProcessor<T> {
    T process(T input);
}

public abstract class AbstractProcessor<T> implements IProcessor<T> {
    @Override
    public T process(T input) {
        return doProcess(input);
    }
    
    protected abstract T doProcess(T input);
}

public class SimpleProcessor extends AbstractProcessor<String> {
    @Override
    protected String doProcess(String input) {
        return input.toUpperCase();
    }
}

// GOOD - Simple class
public class SimpleProcessor {
    public String process(String input) {
        return input.toUpperCase();
    }
}
```

### Clever Code
```java
// BAD - Clever but hard to understand
public int mystery(int n) {
    return (n & (n - 1)) == 0 ? 1 : 0;
}

// GOOD - Clear intent
public boolean isPowerOfTwo(int n) {
    if (n <= 0) return false;
    return (n & (n - 1)) == 0;
}
```

## Guidelines

### Code
- Prefer straightforward solutions
- Avoid premature optimization
- Write self-documenting code
- Use descriptive names
- Keep methods short
- Limit nesting depth

### Architecture
- Choose simple patterns
- Avoid unnecessary layers
- Use proven technologies
- Limit configuration options
- Keep deployment simple

## Best Practices

1. Start simple, add complexity when needed
2. Question every abstraction
3. Prefer readability over cleverness
4. Use the simplest solution that works
5. Refactor when complexity grows
6. Document complex decisions
7. Review code for unnecessary complexity
8. Measure before optimizing
