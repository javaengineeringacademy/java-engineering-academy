# Reflection & Annotations — Mini Projects

## Project 1: Mini Dependency Injection Container

Build a simplified Spring-like DI container using reflection.

### Requirements

1. Define `@Component` and `@Autowired` annotations
2. Scan a package for classes annotated with `@Component`
3. Create instances of discovered classes via reflection
4. Resolve `@Autowired` dependencies by type
5. Handle circular dependency detection

### Starting Code

```java
// Define these annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {}
```

### Implementation Outline

```java
public class MiniDIContainer {
    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    
    public void scan(String packagePath) throws Exception {
        // 1. Find all classes in the package
        // 2. Check for @Component annotation
        // 3. Create instances
        // 4. Inject @Autowired fields
    }
    
    private Object createInstance(Class<?> clazz) throws Exception {
        // Use Constructor.newInstance()
    }
    
    private void injectDependencies(Object instance) throws Exception {
        // Find @Autowired fields
        // Resolve by type from singletons map
        // field.set(instance, dependency)
    }
    
    public <T> T getBean(Class<T> type) {
        return (T) singletons.get(type);
    }
}
```

### Testing

```java
@Component
public class UserRepository {
    public void save(String data) { System.out.println("Saved: " + data); }
}

@Component
public class UserService {
    @Autowired
    private UserRepository repository;
    
    public void process(String data) {
        repository.save(data);
    }
}

// Test
MiniDIContainer container = new MiniDIContainer();
container.scan("com.example");
UserService service = container.getBean(UserService.class);
service.process("test data");
```

---

## Project 2: Annotation-Based ORM

Build a mini Object-Relational Mapper using annotations and reflection.

### Requirements

1. Define `@Table`, `@Column`, `@Id` annotations
2. Read entity metadata via reflection
3. Generate SQL INSERT statements
4. Map result sets back to objects

### Starting Code

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
    boolean nullable() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {}
```

### Implementation Outline

```java
public class MiniORM {
    
    public String generateInsert(Object entity) {
        Class<?> clazz = entity.getClass();
        Table table = clazz.getAnnotation(Table.class);
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(table.name()).append(" (");
        
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        for (Field field : clazz.getDeclaredFields()) {
            Column col = field.getAnnotation(Column.class);
            if (col == null) continue;
            
            field.setAccessible(true);
            columns.add(col.name());
            values.add(field.get(entity));
        }
        
        sql.append(String.join(", ", columns));
        sql.append(") VALUES (");
        sql.append(String.join(", ", Collections.nCopies(values.size(), "?")));
        sql.append(")");
        
        return sql.toString();
    }
    
    public <T> T mapRow(Class<T> clazz, Map<String, Object> row) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            Column col = field.getAnnotation(Column.class);
            if (col == null) continue;
            field.setAccessible(true);
            field.set(instance, row.get(col.name()));
        }
        return instance;
    }
}
```

---

## Project 3: Method-Level Access Control System

Build an access control system using annotations and dynamic proxy.

### Requirements

1. Define `@RequiresRole` and `@RequiresPermission` annotations
2. Create a proxy that checks roles/permissions before method invocation
3. Throw `SecurityException` if access is denied
4. Support role hierarchy (ADMIN > USER > GUEST)

### Starting Code

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresRole {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresPermission {
    String value();
}
```

### Implementation Outline

```java
public class AccessControlProxy {
    
    private static final Map<String, List<String>> ROLE_HIERARCHY = Map.of(
        "ADMIN", Arrays.asList("USER", "GUEST"),
        "USER", Arrays.asList("GUEST"),
        "GUEST", Collections.emptyList()
    );
    
    public static <T> T createProxy(T target, String userRole) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                // Check @RequiresRole
                RequiresRole roleReq = method.getAnnotation(RequiresRole.class);
                if (roleReq != null && !hasRole(userRole, roleReq.value())) {
                    throw new SecurityException(
                        "Requires role: " + roleReq.value());
                }
                
                // Check @RequiresPermission
                RequiresPermission permReq = method.getAnnotation(RequiresPermission.class);
                if (permReq != null && !hasPermission(userRole, permReq.value())) {
                    throw new SecurityException(
                        "Requires permission: " + permReq.value());
                }
                
                return method.invoke(target, args);
            }
        );
    }
    
    private static boolean hasRole(String userRole, String required) {
        if (userRole.equals(required)) return true;
        List<String> inherited = ROLE_HIERARCHY.getOrDefault(userRole, Collections.emptyList());
        return inherited.contains(required);
    }
}
```
