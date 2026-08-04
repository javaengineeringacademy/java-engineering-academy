# ISP - Interface Segregation Principle

## Overview

Clients should not be forced to depend on interfaces they don't use. Create specific interfaces rather than general-purpose ones.

## Violations

### Fat Interface
```java
// BAD - Forced to implement unused methods
public interface Worker {
    void work();
    void eat();
    void sleep();
    void attendMeeting();
    void writeReport();
}

public class Robot implements Worker {
    @Override
    public void work() { /* works */ }
    
    @Override
    public void eat() { throw new UnsupportedOperationException(); }
    
    @Override
    public void sleep() { throw new UnsupportedOperationException(); }
    
    @Override
    public void attendMeeting() { throw new UnsupportedOperationException(); }
    
    @Override
    public void writeReport() { throw new UnsupportedOperationException(); }
}
```

### Segregated Interfaces
```java
// GOOD - Clients choose which interfaces to implement
public interface Workable {
    void work();
}

public interface Feedable {
    void eat();
}

public interface Sleepable {
    void sleep();
}

public interface MeetingAttendee {
    void attendMeeting();
}

public interface ReportWriter {
    void writeReport();
}

public class HumanWorker implements Workable, Feedable, Sleepable, MeetingAttendee, ReportWriter {
    @Override
    public void work() { /* works */ }
    
    @Override
    public void eat() { /* eats */ }
    
    @Override
    public void sleep() { /* sleeps */ }
    
    @Override
    public void attendMeeting() { /* attends */ }
    
    @Override
    public void writeReport() { /* writes */ }
}

public class Robot implements Workable {
    @Override
    public void work() { /* works */ }
}
```

### Spring Example
```java
// BAD - Single service interface
public interface UserService {
    User findById(Long id);
    User create(UserDto dto);
    void update(Long id, UserDto dto);
    void delete(Long id);
    List<User> search(String query);
    void sendWelcomeEmail(User user);
    void generateReport();
}

// GOOD - Segregated interfaces
public interface UserReadService {
    User findById(Long id);
    List<User> search(String query);
}

public interface UserWriteService {
    User create(UserDto dto);
    void update(Long id, UserDto dto);
    void delete(Long id);
}

public interface UserNotificationService {
    void sendWelcomeEmail(User user);
}

public interface UserReportService {
    void generateReport();
}

// Implementation combines needed interfaces
@Service
public class UserServiceImpl implements UserReadService, UserWriteService, UserNotificationService {
    // Implementation
}
```

## Benefits

| Fat Interface | Segregated |
|---------------|------------|
| Tight coupling | Loose coupling |
| Hard to test | Easy to test |
| Changes ripple | Isolated changes |
| Unused dependencies | Minimal dependencies |

## Best Practices

1. Design interfaces for clients
2. Keep interfaces small and focused
3. Separate read and write operations
4. Use role interfaces
5. Consider client needs
6. Split large interfaces
7. Use adapter pattern when needed
8. Document interface purposes
