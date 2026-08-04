# Java Advanced Topics

## Table of Contents

1. [Generics](#generics)
2. [Annotations](#annotations)
3. [Reflection](#reflection)
4. [Streams](#streams)
5. [Optional](#optional)
6. [Records](#records)
7. [Sealed Classes](#sealed-classes)
8. [Pattern Matching](#pattern-matching)
9. [Text Blocks](#text-blocks)
10. [var Keyword](#var-keyword)

---

## Generics

### Basic Generics

```java
// Generic class
public class Box<T> {
    private T content;
    
    public Box(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "Box{content=" + content + "}";
    }
}

// Generic method
public class GenericMethods {
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
    
    public static <T extends Comparable<T>> T findMax(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static <T, R> R transform(T input, java.util.function.Function<T, R> transformer) {
        return transformer.apply(input);
    }
    
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        
        System.out.println(stringBox);
        System.out.println(intBox);
        
        Integer[] numbers = {1, 5, 3, 9, 7};
        String[] names = {"Charlie", "Alice", "Bob"};
        
        printArray(numbers);
        printArray(names);
        
        System.out.println("Max number: " + findMax(numbers));
        System.out.println("Max name: " + findMax(names));
        
        String transformed = transform(42, Object::toString);
        System.out.println("Transformed: " + transformed);
    }
}
```

### Bounded Type Parameters

```java
// Upper bounded wildcard
public class UpperBounded {
    public static double sumOfList(java.util.List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }
    
    public static void main(String[] args) {
        java.util.List<Integer> ints = java.util.List.of(1, 2, 3);
        java.util.List<Double> doubles = java.util.List.of(1.1, 2.2, 3.3);
        
        System.out.println("Sum of ints: " + sumOfList(ints));
        System.out.println("Sum of doubles: " + sumOfList(doubles));
    }
}

// Lower bounded wildcard
public class LowerBounded {
    public static void addNumbers(java.util.List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }
    
    public static void main(String[] args) {
        java.util.List<Number> numberList = new java.util.ArrayList<>();
        addNumbers(numberList);
        System.out.println("Numbers: " + numberList);
    }
}

// Multiple bounds
public class MultiBound<T extends Comparable<T> & java.io.Serializable> {
    private T value;
    
    public MultiBound(T value) {
        this.value = value;
    }
    
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}

// Wildcard capture
public class WildcardCapture {
    public static void swap(java.util.List<?> list) {
        swapInternal(list, list);
    }
    
    private static <T> void swapInternal(java.util.List<T> list1, java.util.List<?> list2) {
        // Can't directly use wildcards for modification
        // Need helper method with type capture
    }
    
    public static void main(String[] args) {
        java.util.List<Integer> ints = java.util.ArrayList<>();
        java.util.List<Double> doubles = java.util.ArrayList<>();
        
        // ints = doubles; // Compile error - different types
        
        // But with wildcards
        java.util.List<? extends Number> numbers = ints;
        numbers = doubles; // OK
        
        System.out.println("Numbers: " + numbers);
    }
}
```

### Generic Interfaces and Inheritance

```java
// Generic interface
public interface Repository<T, ID> {
    T findById(ID id);
    java.util.List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
}

// Implementation
public class UserRepository implements Repository<User, Long> {
    private java.util.Map<Long, User> store = new java.util.HashMap<>();
    
    @Override
    public User findById(Long id) {
        return store.get(id);
    }
    
    @Override
    public java.util.List<User> findAll() {
        return new java.util.ArrayList<>(store.values());
    }
    
    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }
    
    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}

// Partially bounded generic
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    protected java.util.Map<ID, T> store = new java.util.HashMap<>();
    
    @Override
    public T findById(ID id) {
        return store.get(id);
    }
    
    @Override
    public java.util.List<T> findAll() {
        return new java.util.ArrayList<>(store.values());
    }
}

// Type erasure
public class TypeErasure {
    // Runtime type information is erased
    public static <T> void checkType(T obj) {
        // Can't do: if (T == String)
        // But can check class at runtime
        System.out.println("Type: " + obj.getClass().getSimpleName());
    }
    
    // Can't create generic arrays
    // T[] array = new T[10]; // Compile error
    
    // Workaround
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, size);
    }
    
    public static void main(String[] args) {
        checkType("Hello");
        checkType(42);
        checkType(3.14);
        
        String[] strings = createArray(String.class, 5);
        System.out.println("Array created: " + strings.getClass().getSimpleName());
    }
}
```

---

## Annotations

### Built-in Annotations

```java
import java.lang.annotation.*;

// @Override - indicates method overrides superclass method
public class Animal {
    public void speak() {
        System.out.println("Animal speaks");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks");
    }
    
    // @Deprecated - marks code as outdated
    @Deprecated
    public void oldMethod() {
        System.out.println("This method is deprecated");
    }
    
    // @SuppressWarnings - suppress compiler warnings
    @SuppressWarnings("deprecation")
    public void useOldMethod() {
        oldMethod();
    }
    
    @SuppressWarnings("unchecked")
    public void uncheckedOperation() {
        java.util.List list = new java.util.ArrayList();
        list.add("Hello");
        java.util.List<String> strings = list; // Unchecked cast
    }
    
    // @FunctionalInterface - marks interface as functional
    @FunctionalInterface
    public interface Transformer<T, R> {
        R transform(T input);
        
        // Can have one abstract method
        // void another(); // Would cause compile error
        
        // Can have multiple default/static methods
        default void doSomething() {
            System.out.println("Doing something");
        }
    }
}
```

### Custom Annotations

```java
import java.lang.annotation.*;

// Basic custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    int duration() default 300; // seconds
    String key() default "";
}

// Annotation with complex types
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
    String name() default "";
    String[] tags() default {};
    boolean singleton() default true;
}

// Annotation with validation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    String message() default "Field cannot be null";
}

// Repeatable annotation (Java 8+)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Schedules.class)
public @interface Schedule {
    String cron();
    String zone() default "UTC";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Schedule[] {
}

// Using annotations
@Service(name = "userService", tags = {"auth", "core"})
public class UserService {
    
    @NotNull(message = "Username is required")
    private String username;
    
    @Cacheable(duration = 600, key = "user_")
    public User findById(Long id) {
        // Method implementation
        return null;
    }
    
    @Schedule(cron = "0 0 * * *")
    @Schedule(cron = "0 12 * * *", zone = "America/New_York")
    public void dailyCleanup() {
        System.out.println("Running daily cleanup");
    }
}

// Processing annotations
import java.lang.reflect.Method;

public class AnnotationProcessor {
    public static void processAnnotations(Class<?> clazz) {
        // Check for @Service annotation
        if (clazz.isAnnotationPresent(Service.class)) {
            Service service = clazz.getAnnotation(Service.class);
            System.out.println("Service name: " + service.name());
            System.out.println("Tags: " + java.util.Arrays.toString(service.tags()));
            System.out.println("Singleton: " + service.singleton());
        }
        
        // Process method annotations
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Cacheable.class)) {
                Cacheable cacheable = method.getAnnotation(Cacheable.class);
                System.out.println("Cacheable method: " + method.getName());
                System.out.println("Duration: " + cacheable.duration());
                System.out.println("Key: " + cacheable.key());
            }
            
            if (method.isAnnotationPresent(Schedule.class)) {
                Schedule[] schedules = method.getAnnotationsByType(Schedule.class);
                for (Schedule schedule : schedules) {
                    System.out.println("Schedule: " + schedule.cron());
                    System.out.println("Zone: " + schedule.zone());
                }
            }
        }
    }
    
    public static void main(String[] args) {
        processAnnotations(UserService.class);
    }
}
```

### Annotation Retention and Targets

```java
import java.lang.annotation.*;

// Retention policies
@Retention(RetentionPolicy.SOURCE)  // Available only in source code
@interface SourceOnly {
}

@Retention(RetentionPolicy.CLASS)   // Available in class file, not runtime
@interface ClassOnly {
}

@Retention(RetentionPolicy.RUNTIME) // Available at runtime
@interface RuntimeOnly {
}

// Target types
@Target(ElementType.TYPE)           // Classes, interfaces, enums
@interface TypeAnnotation {
}

@Target(ElementType.METHOD)         // Methods
@interface MethodAnnotation {
}

@Target(ElementType.FIELD)          // Fields
@interface FieldAnnotation {
}

@Target(ElementType.PARAMETER)      // Method parameters
@interface ParameterAnnotation {
}

@Target(ElementType.CONSTRUCTOR)    // Constructors
@interface ConstructorAnnotation {
}

@Target(ElementType.LOCAL_VARIABLE) // Local variables
@interface LocalVariableAnnotation {
}

@Target(ElementType.ANNOTATION_TYPE) // Other annotations
@interface MetaAnnotation {
}

@Target(ElementType.PACKAGE)        // Packages
@interface PackageAnnotation {
}

// Combination targets
@Target({ElementType.TYPE, ElementType.METHOD})
@interface MultipleTargets {
}

// Inherited annotation
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface InheritedAnnotation {
}

@InheritedAnnotation
class ParentClass {}

class ChildClass extends ParentClass {} // Inherits @InheritedAnnotation

public class AnnotationTargets {
    public static void main(String[] args) {
        // Check if ParentClass has annotation
        System.out.println("Parent has annotation: " + 
            ParentClass.isAnnotationPresent(InheritedAnnotation.class));
        
        // Check if ChildClass inherits annotation
        System.out.println("Child has annotation: " + 
            ChildClass.isAnnotationPresent(InheritedAnnotation.class));
    }
}
```

---

## Reflection

### Class Information

```java
import java.lang.reflect.*;

public class ReflectionBasics {
    public static void main(String[] args) throws Exception {
        // Getting Class object
        Class<?> stringClass = String.class;
        Class<?> integerClass = int.class;
        Class<?> listClass = java.util.List.class;
        
        // From instance
        String str = "Hello";
        Class<?> strClass = str.getClass();
        
        // From fully qualified name
        Class<?> forNameClass = Class.forName("java.lang.String");
        
        // Class information
        System.out.println("Name: " + stringClass.getName());
        System.out.println("Simple Name: " + stringClass.getSimpleName());
        System.out.println("Canonical Name: " + stringClass.getCanonicalName());
        System.out.println("Package: " + stringClass.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(stringClass.getModifiers()));
        System.out.println("Superclass: " + stringClass.getSuperclass().getSimpleName());
        System.out.println("Interfaces: " + java.util.Arrays.toString(stringClass.getInterfaces()));
        
        // Check type
        System.out.println("Is Interface: " + stringClass.isInterface());
        System.out.println("Is Array: " + stringClass.isArray());
        System.out.println("Is Primitive: " + stringClass.isPrimitive());
        System.out.println("Is Annotation: " + stringClass.isAnnotation());
        System.out.println("Is Enum: " + stringClass.isEnum());
    }
}
```

### Accessing Fields

```java
import java.lang.reflect.*;

public class FieldReflection {
    private String name;
    private int age;
    private static final String TYPE = "Person";
    
    public FieldReflection(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public static void main(String[] args) throws Exception {
        FieldReflection obj = new FieldReflection("Alice", 30);
        Class<?> clazz = obj.getClass();
        
        // Get all fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName());
            System.out.println("Type: " + field.getType().getSimpleName());
            System.out.println("Modifiers: " + Modifier.toString(field.getModifiers()));
            System.out.println();
        }
        
        // Access private field
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true); // Bypass private access
        
        String nameValue = (String) nameField.get(obj);
        System.out.println("Name: " + nameValue);
        
        // Modify private field
        nameField.set(obj, "Bob");
        System.out.println("Modified Name: " + obj.name);
        
        // Access static field
        Field typeField = clazz.getDeclaredField("TYPE");
        typeField.setAccessible(true);
        String typeValue = (String) typeField.get(null); // null for static
        System.out.println("Type: " + typeValue);
    }
}
```

### Invoking Methods

```java
import java.lang.reflect.*;

public class MethodReflection {
    private String message = "Hello, World!";
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String addPrefix(String prefix) {
        return prefix + " " + message;
    }
    
    private String privateMethod() {
        return "This is private";
    }
    
    public static void staticMethod() {
        System.out.println("This is static");
    }
    
    public static void main(String[] args) throws Exception {
        MethodReflection obj = new MethodReflection();
        Class<?> clazz = obj.getClass();
        
        // Get all methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
            System.out.println("Return Type: " + method.getReturnType().getSimpleName());
            System.out.println("Parameters: " + java.util.Arrays.toString(method.getParameterTypes()));
            System.out.println("Modifiers: " + Modifier.toString(method.getModifiers()));
            System.out.println();
        }
        
        // Invoke public method
        Method getMessageMethod = clazz.getDeclaredMethod("getMessage");
        String message = (String) getMessageMethod.invoke(obj);
        System.out.println("Message: " + message);
        
        // Invoke method with parameters
        Method addPrefixMethod = clazz.getDeclaredMethod("addPrefix", String.class);
        String prefixed = (String) addPrefixMethod.invoke(obj, "Hello");
        System.out.println("Prefixed: " + prefixed);
        
        // Invoke private method
        Method privateMethod = clazz.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        String privateResult = (String) privateMethod.invoke(obj);
        System.out.println("Private Result: " + privateResult);
        
        // Invoke static method
        Method staticMethod = clazz.getDeclaredMethod("staticMethod");
        staticMethod.invoke(null);
        
        // Invoke setter
        Method setMessageMethod = clazz.getDeclaredMethod("setMessage", String.class);
        setMessageMethod.invoke(obj, "Goodbye, World!");
        System.out.println("After setter: " + obj.getMessage());
    }
}
```

### Creating Instances and Constructors

```java
import java.lang.reflect.*;

public class ConstructorReflection {
    private String name;
    private int age;
    
    public ConstructorReflection() {
        this.name = "Unknown";
        this.age = 0;
    }
    
    public ConstructorReflection(String name) {
        this.name = name;
        this.age = 0;
    }
    
    public ConstructorReflection(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    private ConstructorReflection(String name, int age, String internal) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "ConstructorReflection{name='" + name + "', age=" + age + "}";
    }
    
    public static void main(String[] args) throws Exception {
        Class<?> clazz = ConstructorReflection.class;
        
        // Get all constructors
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("Constructor: " + java.util.Arrays.toString(constructor.getParameterTypes()));
            System.out.println("Modifiers: " + Modifier.toString(constructor.getModifiers()));
            System.out.println();
        }
        
        // Create instance using default constructor
        Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
        Object obj1 = defaultConstructor.newInstance();
        System.out.println("Default: " + obj1);
        
        // Create instance using parameterized constructor
        Constructor<?> stringConstructor = clazz.getDeclaredConstructor(String.class);
        Object obj2 = stringConstructor.newInstance("Alice");
        System.out.println("String: " + obj2);
        
        // Create instance using full constructor
        Constructor<?> fullConstructor = clazz.getDeclaredConstructor(String.class, int.class);
        Object obj3 = fullConstructor.newInstance("Bob", 30);
        System.out.println("Full: " + obj3);
        
        // Create instance using private constructor
        Constructor<?> privateConstructor = clazz.getDeclaredConstructor(String.class, int.class, String.class);
        privateConstructor.setAccessible(true);
        Object obj4 = privateConstructor.newInstance("Charlie", 25, "internal");
        System.out.println("Private: " + obj4);
        
        // Create array of objects
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 3);
        array[0] = obj1;
        array[1] = obj2;
        array[2] = obj3;
        
        for (Object obj : array) {
            System.out.println("Array element: " + obj);
        }
    }
}
```

---

## Streams

### Stream Creation

```java
import java.util.*;
import java.util.stream.*;

public class StreamCreation {
    public static void main(String[] args) {
        // From collection
        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");
        Stream<String> nameStream = names.stream();
        
        // From array
        int[] numbers = {1, 2, 3, 4, 5};
        IntStream numberStream = Arrays.stream(numbers);
        
        // From values
        Stream<Integer> valueStream = Stream.of(1, 2, 3, 4, 5);
        
        // From builder
        Stream<String> builderStream = Stream.<String>builder()
                .add("one")
                .add("two")
                .add("three")
                .build();
        
        // From iterator
        Stream<Integer> iteratorStream = Stream.iterate(0, n -> n + 2).limit(10);
        
        // From generate
        Stream<Double> generateStream = Stream.generate(Math::random).limit(5);
        
        // From string
        IntStream charStream = "Hello".chars();
        
        // From file (java.nio.file)
        // Stream<String> fileLines = Files.lines(Path.of("file.txt"));
        
        // Primitive streams
        IntStream intStream = IntStream.range(1, 11);        // 1 to 10
        IntStream intStream2 = IntStream.rangeClosed(1, 10); // 1 to 10
        LongStream longStream = LongStream.rangeClosed(1, 100);
        DoubleStream doubleStream = DoubleStream.iterate(0.1, n -> n * 2).limit(10);
        
        // Print streams
        System.out.println("Names: " + names);
        System.out.println("Numbers: " + Arrays.toString(numbers));
        System.out.println("Integer range: " + intStream.boxed().collect(Collectors.toList()));
        System.out.println("Double iterate: " + doubleStream.boxed().collect(Collectors.toList()));
    }
}
```

### Intermediate Operations

```java
import java.util.*;
import java.util.stream.*;

public class IntermediateOperations {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // filter - select elements matching predicate
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Even numbers: " + evens);
        
        // map - transform each element
        List<Integer> squared = numbers.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("Squared: " + squared);
        
        // flatMap - flatten nested structures
        List<List<Integer>> nested = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );
        
        List<Integer> flat = nested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("Flat: " + flat);
        
        // distinct - remove duplicates
        List<Integer> withDuplicates = List.of(1, 2, 2, 3, 3, 3);
        List<Integer> unique = withDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Unique: " + unique);
        
        // sorted - sort elements
        List<Integer> unsorted = List.of(5, 3, 8, 1, 9, 2);
        List<Integer> sorted = unsorted.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Sorted: " + sorted);
        
        // sorted with comparator
        List<String> names = List.of("Charlie", "Alice", "Bob", "David");
        List<String> sortedNames = names.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println("Sorted by length: " + sortedNames);
        
        // peek - debug/perform action without modifying stream
        List<Integer> peeked = numbers.stream()
                .filter(n -> n > 5)
                .peek(n -> System.out.println("After filter: " + n))
                .map(n -> n * 2)
                .peek(n -> System.out.println("After map: " + n))
                .collect(Collectors.toList());
        System.out.println("Peeked: " + peeked);
        
        // limit - take first n elements
        List<Integer> limited = numbers.stream()
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("Limited: " + limited);
        
        // skip - skip first n elements
        List<Integer> skipped = numbers.stream()
                .skip(5)
                .collect(Collectors.toList());
        System.out.println("Skipped: " + skipped);
        
        // takeWhile (Java 9+)
        List<Integer> taken = numbers.stream()
                .takeWhile(n -> n < 5)
                .collect(Collectors.toList());
        System.out.println("TakeWhile: " + taken);
        
        // dropWhile (Java 9+)
        List<Integer> dropped = numbers.stream()
                .dropWhile(n -> n < 5)
                .collect(Collectors.toList());
        System.out.println("DropWhile: " + dropped);
    }
}
```

### Terminal Operations

```java
import java.util.*;
import java.util.stream.*;

public class TerminalOperations {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // collect - accumulate into collection
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Evens: " + evens);
        
        // Collectors utilities
        String joined = numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);
        
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Partitioned: " + partitioned);
        
        Map<Integer, List<Integer>> grouped = numbers.stream()
                .collect(Collectors.groupingBy(n -> n % 3));
        System.out.println("Grouped: " + grouped);
        
        // forEach - perform action for each element
        System.out.print("ForEach: ");
        numbers.stream()
                .filter(n -> n % 2 == 0)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();
        
        // reduce - combine elements
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        
        Optional<Integer> max = numbers.stream()
                .reduce(Integer::max);
        System.out.println("Max: " + max.orElse(0));
        
        // count - count elements
        long count = numbers.stream()
                .filter(n -> n > 5)
                .count();
        System.out.println("Count > 5: " + count);
        
        // min/max
        Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);
        System.out.println("Min: " + min.orElse(0));
        
        // anyMatch/allMatch/noneMatch
        boolean anyEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
        System.out.println("Any even: " + anyEven);
        
        boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
        System.out.println("All positive: " + allPositive);
        
        boolean noneNegative = numbers.stream()
                .noneMatch(n -> n < 0);
        System.out.println("None negative: " + noneNegative);
        
        // findFirst/findAny
        Optional<Integer> firstEven = numbers.stream()
                .filter(n -> n % 2 == 0)
                .findFirst();
        System.out.println("First even: " + firstEven.orElse(0));
        
        // toArray
        Integer[] array = numbers.stream()
                .toArray(Integer[]::new);
        System.out.println("Array: " + Arrays.toString(array));
    }
}
```

### Stream Patterns and Best Practices

```java
import java.util.*;
import java.util.stream.*;

public class StreamPatterns {
    
    // Pattern: Builder pattern with streams
    public static class QueryBuilder {
        private final List<String> conditions = new ArrayList<>();
        private String orderBy;
        private int limit;
        
        public QueryBuilder where(String condition) {
            conditions.add(condition);
            return this;
        }
        
        public QueryBuilder orderBy(String column) {
            this.orderBy = column;
            return this;
        }
        
        public QueryBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }
        
        public String build() {
            StringBuilder query = new StringBuilder("SELECT * FROM users");
            
            if (!conditions.isEmpty()) {
                query.append(" WHERE ")
                     .append(conditions.stream()
                         .collect(Collectors.joining(" AND ")));
            }
            
            if (orderBy != null) {
                query.append(" ORDER BY ").append(orderBy);
            }
            
            if (limit > 0) {
                query.append(" LIMIT ").append(limit);
            }
            
            return query.toString();
        }
    }
    
    // Pattern: Pipeline processing
    public static class DataProcessor {
        public static List<String> processNames(List<String> names) {
            return names.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }
        
        public static Map<String, List<String>> groupByFirstLetter(List<String> names) {
            return names.stream()
                    .collect(Collectors.groupingBy(
                        name -> String.valueOf(name.charAt(0)).toUpperCase()
                    ));
        }
    }
    
    // Pattern: Statistics collection
    public static void collectStatistics(List<Integer> numbers) {
        IntSummaryStatistics stats = numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        
        System.out.println("Count: " + stats.getCount());
        System.out.println("Sum: " + stats.getSum());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        System.out.println("Average: " + stats.getAverage());
    }
    
    // Pattern: Parallel processing
    public static long countWords(String text) {
        return Arrays.stream(text.split("\\s+"))
                .parallel()
                .filter(word -> !word.isEmpty())
                .count();
    }
    
    public static void main(String[] args) {
        // Builder pattern
        String query = new QueryBuilder()
                .where("age > 18")
                .where("status = 'ACTIVE'")
                .orderBy("name")
                .limit(10)
                .build();
        System.out.println("Query: " + query);
        
        // Pipeline processing
        List<String> names = List.of(" Alice ", "Bob", null, "  Charlie  ", "", "David");
        List<String> processed = DataProcessor.processNames(names);
        System.out.println("Processed: " + processed);
        
        // Grouping
        Map<String, List<String>> grouped = DataProcessor.groupByFirstLetter(
                List.of("Alice", "Bob", "Charlie", "David", "Eve")
        );
        System.out.println("Grouped: " + grouped);
        
        // Statistics
        collectStatistics(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        
        // Parallel word count
        String text = "Java is a powerful language with streams and concurrency";
        System.out.println("Word count: " + countWords(text));
    }
}
```

---

## Optional

### Basic Optional Usage

```java
import java.util.Optional;

public class OptionalBasics {
    
    public static Optional<String> findName(int id) {
        if (id == 1) return Optional.of("Alice");
        if (id == 2) return Optional.of("Bob");
        return Optional.empty();
    }
    
    public static void main(String[] args) {
        // Creating Optional
        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);
        
        // Checking value
        System.out.println("Is present: " + present.isPresent());
        System.out.println("Is empty: " + empty.isEmpty());
        
        // Getting value
        String value = present.get();
        System.out.println("Value: " + value);
        
        // OrElse
        String orElse = empty.orElse("Default");
        System.out.println("OrElse: " + orElse);
        
        String orElseGet = empty.orElseGet(() -> "Computed Default");
        System.out.println("OrElseGet: " + orElseGet);
        
        // OrElseThrow
        try {
            String orElseThrow = empty.orElseThrow(() -> 
                new RuntimeException("Value not found"));
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        // IfPresent
        present.ifPresent(value2 -> System.out.println("Present: " + value2));
        empty.ifPresent(value2 -> System.out.println("This won't print"));
        
        // IfPresentOrElse (Java 9+)
        present.ifPresentOrElse(
            v -> System.out.println("Found: " + v),
            () -> System.out.println("Not found")
        );
        
        // Filter
        Optional<String> filtered = present.filter(v -> v.startsWith("H"));
        System.out.println("Filtered: " + filtered.isPresent());
        
        Optional<String> notFiltered = present.filter(v -> v.startsWith("X"));
        System.out.println("Not filtered: " + notFiltered.isPresent());
        
        // Map
        Optional<Integer> length = present.map(String::length);
        System.out.println("Length: " + length.orElse(0));
        
        // FlatMap
        Optional<String> flatMapped = findName(1)
                .flatMap(name -> Optional.of(name.toUpperCase()));
        System.out.println("FlatMapped: " + flatMapped.orElse(""));
    }
}
```

### Optional in Practice

```java
import java.util.Optional;

public class OptionalPractice {
    
    // Pattern: Replace null checks
    public static String getUserName(User user) {
        // Without Optional
        // if (user != null) {
        //     if (user.getName() != null) {
        //         return user.getName();
        //     }
        // }
        // return "Unknown";
        
        // With Optional
        return Optional.ofNullable(user)
                .map(User::getName)
                .orElse("Unknown");
    }
    
    // Pattern: Chain of operations
    public static int getUserAge(User user) {
        return Optional.ofNullable(user)
                .map(User::getProfile)
                .map(Profile::getAge)
                .orElse(0);
    }
    
    // Pattern: Validate and transform
    public static Optional<String> validateEmail(String email) {
        return Optional.ofNullable(email)
                .filter(e -> !e.trim().isEmpty())
                .filter(e -> e.contains("@"))
                .filter(e -> e.contains("."))
                .map(String::toLowerCase);
    }
    
    // Pattern: Database lookup simulation
    public static Optional<Order> findOrder(int orderId) {
        // Simulating database lookup
        if (orderId > 0) {
            return Optional.of(new Order(orderId, "Product", 99.99));
        }
        return Optional.empty();
    }
    
    public static void main(String[] args) {
        // Null check replacement
        System.out.println("User name: " + getUserName(null));
        System.out.println("User name: " + getUserName(new User("Alice", null)));
        
        // Chain of operations
        User user = new User("Bob", new Profile(25));
        System.out.println("User age: " + getUserAge(user));
        
        // Email validation
        Optional<String> validEmail = validateEmail("  User@Example.com  ");
        validEmail.ifPresent(e -> System.out.println("Valid email: " + e));
        
        // Database lookup
        findOrder(1)
                .filter(order -> order.getPrice() > 50)
                .ifPresent(order -> System.out.println("Expensive order: " + order));
        
        // Optional with streams
        java.util.List<Optional<String>> optionals = java.util.List.of(
                Optional.of("Hello"),
                Optional.empty(),
                Optional.of("World")
        );
        
        java.util.List<String> values = optionals.stream()
                .flatMap(Optional::stream)
                .collect(java.util.stream.Collectors.toList());
        System.out.println("Values: " + values);
    }
}

// Helper classes
class User {
    private String name;
    private Profile profile;
    
    public User(String name, Profile profile) {
        this.name = name;
        this.profile = profile;
    }
    
    public String getName() { return name; }
    public Profile getProfile() { return profile; }
}

class Profile {
    private int age;
    
    public Profile(int age) {
        this.age = age;
    }
    
    public int getAge() { return age; }
}

class Order {
    private int id;
    private String product;
    private double price;
    
    public Order(int id, String product, double price) {
        this.id = id;
        this.product = product;
        this.price = price;
    }
    
    public int getId() { return id; }
    public String getProduct() { return product; }
    public double getPrice() { return price; }
    
    @Override
    public String toString() {
        return "Order{id=" + id + ", product='" + product + "', price=" + price + "}";
    }
}
```

---

## Records

### Basic Records

```java
// Simple record
public record Point(int x, int y) {
    // Compact constructor for validation
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
    }
    
    // Custom method
    public double distanceTo(Point other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Static factory method
    public static Point origin() {
        return new Point(0, 0);
    }
    
    // Override canonical methods
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}

// Record with multiple components
public record Person(String name, int age, String email) {
    // Validation in compact constructor
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age");
        }
        this.email = email != null ? email.toLowerCase() : null;
    }
    
    // Custom accessor
    public String displayName() {
        return name.toUpperCase();
    }
    
    // Static factory
    public static Person of(String name, int age) {
        return new Person(name, age, null);
    }
}

// Record implementing interface
public interface Printable {
    String toPrettyString();
}

public record Product(String name, double price, int quantity) implements Printable {
    public Product {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
    
    public Product withQuantity(int newQuantity) {
        return new Product(name, price, newQuantity);
    }
    
    public double totalPrice() {
        return price * quantity;
    }
    
    @Override
    public String toPrettyString() {
        return String.format("Product: %s, Price: $%.2f, Qty: %d, Total: $%.2f",
                name, price, quantity, totalPrice());
    }
}

// Record extending class (not allowed) but can implement interfaces
// public record Employee(String name, String department) extends Person {} // Error!

// Usage
public class RecordExample {
    public static void main(String[] args) {
        // Creating records
        Point p1 = new Point(3, 4);
        Point p2 = new Point(6, 8);
        
        System.out.println("P1: " + p1);
        System.out.println("P2: " + p2);
        System.out.println("Distance: " + p1.distanceTo(p2));
        System.out.println("Origin: " + Point.origin());
        
        // Accessor methods
        System.out.println("X: " + p1.x());
        System.out.println("Y: " + p1.y());
        
        // Person
        Person person = new Person("Alice", 30, "alice@example.com");
        System.out.println("Person: " + person);
        System.out.println("Display name: " + person.displayName());
        
        // Product
        Product product = new Product("Laptop", 999.99, 2);
        System.out.println(product.toPrettyString());
        System.out.println("Total: $" + product.totalPrice());
        
        Product updated = product.withQuantity(5);
        System.out.println(updated.toPrettyString());
        
        // Pattern matching with records (Java 21+)
        Object obj = new Point(1, 2);
        if (obj instanceof Point(int x, int y)) {
            System.out.println("Point coordinates: " + x + ", " + y);
        }
    }
}
```

### Records and Collections

```java
import java.util.*;
import java.util.stream.*;

public class RecordsAndCollections {
    
    public record Coordinate(double latitude, double longitude) {
        public Coordinate {
            if (latitude < -90 || latitude > 90) {
                throw new IllegalArgumentException("Invalid latitude");
            }
            if (longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("Invalid longitude");
            }
        }
        
        public double distanceTo(Coordinate other) {
            // Haversine formula approximation
            double R = 6371; // Earth's radius in km
            double dLat = Math.toRadians(other.latitude - latitude);
            double dLon = Math.toRadians(other.longitude - longitude);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c;
        }
    }
    
    public record City(String name, Coordinate coordinate, int population) {
    }
    
    public static void main(String[] args) {
        // List of records
        List<City> cities = List.of(
                new City("New York", new Coordinate(40.7128, -74.0060), 8_336_817),
                new City("London", new Coordinate(51.5074, -0.1278), 8_982_000),
                new City("Tokyo", new Coordinate(35.6762, 139.6503), 13_960_000),
                new City("Paris", new Coordinate(48.8566, 2.3522), 2_161_000)
        );
        
        // Sorting by population
        List<City> sortedByPopulation = cities.stream()
                .sorted(Comparator.comparingInt(City::population).reversed())
                .collect(Collectors.toList());
        
        System.out.println("Cities by population:");
        sortedByPopulation.forEach(city -> 
            System.out.printf("  %s: %,d%n", city.name(), city.population()));
        
        // Finding nearest city
        Coordinate paris = new Coordinate(48.8566, 2.3522);
        Optional<City> nearest = cities.stream()
                .min(Comparator.comparingDouble(city -> 
                    city.coordinate().distanceTo(paris)));
        
        nearest.ifPresent(city -> 
            System.out.println("\nNearest to Paris: " + city.name()));
        
        // Grouping by continent (simplified)
        Map<String, List<City>> byHemisphere = cities.stream()
                .collect(Collectors.partitioningBy(city -> 
                    city.coordinate().latitude() > 0));
        
        System.out.println("\nNorthern hemisphere: " + byHemisphere.get(true).size());
        System.out.println("Southern hemisphere: " + byHemisphere.get(false).size());
    }
}
```

---

## Sealed Classes

### Basic Sealed Classes

```java
// Sealed class
public sealed class Shape 
        permits Circle, Rectangle, Triangle {
    
    private final String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
    
    public abstract double calculateArea();
}

// Permitted subclasses
public final class Circle extends Shape {
    private final double radius;
    
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    public double getRadius() {
        return radius;
    }
}

public final class Rectangle extends Shape {
    private final double width;
    private final double height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
}

public non-sealed class Triangle extends Shape {
    private final double base;
    private final double height;
    
    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

// Permitted class can be abstract
public abstract sealed class Vehicle permits Car, Truck, Motorcycle {
    protected String make;
    protected String model;
    
    public Vehicle(String make, String model) {
        this.make = make;
        this.model = model;
    }
    
    public abstract void start();
}

public final class Car extends Vehicle {
    public Car(String make, String model) {
        super(make, model);
    }
    
    @Override
    public void start() {
        System.out.println("Car started");
    }
}

public final class Truck extends Vehicle {
    public Truck(String make, String model) {
        super(make, model);
    }
    
    @Override
    public void start() {
        System.out.println("Truck started");
    }
}

public final class Motorcycle extends Vehicle {
    public Motorcycle(String make, String model) {
        super(make, model);
    }
    
    @Override
    public void start() {
        System.out.println("Motorcycle started");
    }
}

// Usage
public class SealedClassExample {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5);
        Shape rectangle = new Rectangle("Blue", 4, 6);
        Shape triangle = new Triangle("Green", 3, 8);
        
        System.out.println("Circle area: " + circle.calculateArea());
        System.out.println("Rectangle area: " + rectangle.calculateArea());
        System.out.println("Triangle area: " + triangle.calculateArea());
        
        // Pattern matching with sealed classes (Java 21+)
        Shape shape = new Circle("Yellow", 3);
        String description = switch (shape) {
            case Circle c -> "Circle with radius " + c.getRadius();
            case Rectangle r -> "Rectangle";
            case Triangle t -> "Triangle";
        };
        System.out.println("Shape: " + description);
        
        // Exhaustive switch
        for (Shape s : List.of(circle, rectangle, triangle)) {
            String info = switch (s) {
                case Circle c -> "Circle: " + c.calculateArea();
                case Rectangle r -> "Rectangle: " + r.calculateArea();
                case Triangle t -> "Triangle: " + t.calculateArea();
            };
            System.out.println(info);
        }
    }
}
```

---

## Pattern Matching

### Pattern Matching for instanceof

```java
public class PatternMatchingExample {
    
    // Pre-Java 16 approach
    public static void processOld(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println("String length: " + s.length());
        } else if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            System.out.println("Integer value: " + i);
        } else {
            System.out.println("Unknown type");
        }
    }
    
    // Java 16+ pattern matching
    public static void processModern(Object obj) {
        if (obj instanceof String s) {
            System.out.println("String length: " + s.length());
        } else if (obj instanceof Integer i) {
            System.out.println("Integer value: " + i);
        } else {
            System.out.println("Unknown type");
        }
    }
    
    // Pattern matching with guard (Java 17+)
    public static void processWithGuard(Object obj) {
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("Long string: " + s);
        } else if (obj instanceof Integer i && i > 100) {
            System.out.println("Large integer: " + i);
        } else {
            System.out.println("Doesn't match conditions");
        }
    }
    
    // Pattern matching in switch (Java 21+)
    public static String describe(Object obj) {
        return switch (obj) {
            case Integer i when i > 0 -> "Positive integer: " + i;
            case Integer i when i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            case String s && !s.isEmpty() -> "Non-empty string: " + s;
            case String s -> "Empty string";
            case int[] arr -> "Array of ints with " + arr.length + " elements";
            case null -> "Null value";
            default -> "Other: " + obj.getClass().getSimpleName();
        };
    }
    
    // Nested pattern matching (Java 21+)
    public record Point(int x, int y) {}
    public record Line(Point start, Point end) {}
    
    public static String describeLine(Line line) {
        return switch (line) {
            case Line(Point(0, 0), Point(var x, var y)) -> 
                "Line from origin to (" + x + ", " + y + ")";
            case Line(var start, var end) -> 
                "Line from " + start + " to " + end;
        };
    }
    
    public static void main(String[] args) {
        System.out.println("Old approach:");
        processOld("Hello");
        processOld(42);
        
        System.out.println("\nModern approach:");
        processModern("Hello");
        processModern(42);
        
        System.out.println("\nWith guard:");
        processWithGuard("Hello");
        processWithGuard("Hello, World!");
        
        System.out.println("\nSwitch pattern matching:");
        System.out.println(describe(42));
        System.out.println(describe(-5));
        System.out.println(describe(0));
        System.out.println(describe("Hello"));
        System.out.println(describe(""));
        System.out.println(describe(null));
        
        System.out.println("\nNested patterns:");
        Line line1 = new Line(new Point(0, 0), new Point(5, 5));
        Line line2 = new Line(new Point(1, 2), new Point(3, 4));
        System.out.println(describeLine(line1));
        System.out.println(describeLine(line2));
    }
}
```

### Record Patterns

```java
public class RecordPatterns {
    
    public record Point(int x, int y) {}
    public record Rectangle(Point topLeft, Point bottomRight) {}
    public record Circle(Point center, double radius) {}
    
    // Pattern matching with records
    public static double calculateArea(Object shape) {
        return switch (shape) {
            case Rectangle(Point(var x1, var y1), Point(var x2, var y2)) -> 
                Math.abs(x2 - x1) * Math.abs(y2 - y1);
            case Circle(Point(var x, var y), var radius) -> 
                Math.PI * radius * radius;
            default -> throw new IllegalArgumentException("Unknown shape");
        };
    }
    
    // Nested records
    public record Address(String street, String city, String country) {}
    public record Person(String name, int age, Address address) {}
    
    public static String formatPerson(Person person) {
        return switch (person) {
            case Person(var name, var age, Address(var street, var city, var country)) ->
                String.format("%s, %d, %s, %s, %s", name, age, street, city, country);
        };
    }
    
    // Destructuring in loops
    public static void main(String[] args) {
        Rectangle rect = new Rectangle(new Point(0, 0), new Point(10, 5));
        Circle circle = new Circle(new Point(5, 5), 3);
        
        System.out.println("Rectangle area: " + calculateArea(rect));
        System.out.println("Circle area: " + calculateArea(circle));
        
        // Format person
        Person person = new Person("Alice", 30, 
            new Address("123 Main St", "Springfield", "USA"));
        System.out.println("Person: " + formatPerson(person));
        
        // List of shapes
        var shapes = java.util.List.of(
            new Rectangle(new Point(0, 0), new Point(5, 5)),
            new Circle(new Point(3, 3), 2),
            new Rectangle(new Point(1, 1), new Point(4, 4))
        );
        
        // Calculate total area
        double totalArea = shapes.stream()
            .mapToDouble(RecordPatterns::calculateArea)
            .sum();
        System.out.println("Total area: " + totalArea);
    }
}
```

---

## Text Blocks

### Basic Text Blocks

```java
public class TextBlocks {
    
    public static void main(String[] args) {
        // Basic text block
        String html = """
                <html>
                    <body>
                        <p>Hello, World!</p>
                    </body>
                </html>
                """;
        System.out.println("HTML:\n" + html);
        
        // Multi-line string
        String query = """
                SELECT id, name, email
                FROM users
                WHERE age > 18
                ORDER BY name
                """;
        System.out.println("Query:\n" + query);
        
        // JSON
        String json = """
                {
                    "name": "Alice",
                    "age": 30,
                    "email": "alice@example.com"
                }
                """;
        System.out.println("JSON:\n" + json);
        
        // String formatting with text blocks
        String name = "Bob";
        int age = 25;
        String formatted = """
                Name: %s
                Age: %d
                """.formatted(name, age);
        System.out.println("Formatted:\n" + formatted);
        
        // Escape sequences
        String escaped = """
                Line 1
                Line 2\tTabbed
                Line 3 with \"quotes\"
                Line 4 with \\backslash
                """;
        System.out.println("Escaped:\n" + escaped);
        
        // Incidental indentation
        String withIndent = """
                {
                    "key": "value"
                }
                """;
        System.out.println("With indentation:\n" + withIndent);
        
        // Line continuation
        String lineContinuation = """
                This is a very long \
                line that continues \
                on the next line \
                without breaks.
                """;
        System.out.println("Line continuation: " + lineContinuation);
        
        // Blank line handling
        String withBlankLines = """
                First line
                
                Third line
                """;
        System.out.println("With blank lines:\n" + withBlankLines);
        
        // Trailing spaces
        String trailingSpaces = """
                Hello   
                World   
                """;
        System.out.println("Trailing spaces: [" + trailingSpaces + "]");
    }
}
```

### Text Blocks in Practice

```java
public class TextBlocksPractice {
    
    // SQL query builder
    public static String buildQuery(String table, String condition) {
        return """
                SELECT *
                FROM %s
                WHERE %s
                ORDER BY id
                """.formatted(table, condition);
    }
    
    // HTML generator
    public static String generateHtml(String title, String content) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                </head>
                <body>
                    <h1>%s</h1>
                    <p>%s</p>
                </body>
                </html>
                """.formatted(title, title, content);
    }
    
    // Code template
    public static String generateClass(String packageName, String className) {
        return """
                package %s;
                
                public class %s {
                    
                    public %s() {
                        // Constructor
                    }
                    
                    public void doSomething() {
                        // Implementation
                    }
                }
                """.formatted(packageName, className, className);
    }
    
    // Error message template
    public static String errorMessage(String code, String message, String details) {
        return """
                Error [%s]:
                %s
                
                Details:
                %s
                """.formatted(code, message, details);
    }
    
    public static void main(String[] args) {
        // SQL query
        String query = buildQuery("users", "age > 18");
        System.out.println("SQL:\n" + query);
        
        // HTML
        String html = generateHtml("My Page", "Hello, World!");
        System.out.println("\nHTML:\n" + html);
        
        // Java class
        String javaClass = generateClass("com.example", "UserService");
        System.out.println("\nJava class:\n" + javaClass);
        
        // Error message
        String error = errorMessage("E001", "Invalid input", "Name cannot be empty");
        System.out.println("\nError:\n" + error);
    }
}
```

---

## var Keyword

### Basic var Usage

```java
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VarKeyword {
    
    public static void main(String[] args) {
        // Local variable type inference
        var name = "Alice";           // String
        var age = 30;                 // int
        var price = 99.99;           // double
        var isActive = true;         // boolean
        var numbers = List.of(1, 2, 3); // List<Integer>
        
        // Explicit type still needed for null
        // var x = null; // Compile error - can't infer type
        
        // Explicit type for multiple variables
        // var a = 1, b = 2; // Compile error
        
        // var with method calls
        var length = "Hello".length(); // int
        var doubled = List.of(1, 2, 3).stream().map(x -> x * 2).toList(); // List<Integer>
        
        // var with complex types
        var map = Map.of("key1", "value1", "key2", "value2"); // Map<String, String>
        var set = Set.of(1, 2, 3); // Set<Integer>
        
        // var with arrays
        var array = new int[]{1, 2, 3, 4, 5}; // int[]
        
        // var in loops
        for (var i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        // var in for-each
        for (var number : List.of(1, 2, 3)) {
            System.out.print(number + " ");
        }
        System.out.println();
        
        // var with try-with-resources
        try (var stream = java.nio.file.Files.lines(java.nio.file.Path.of("nonexistent.txt"))) {
            // Would process file
        } catch (Exception e) {
            // File doesn't exist, that's ok
        }
        
        // var in lambdas (not allowed)
        // var x = (String s) -> s.length(); // Compile error
        
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Numbers: " + numbers);
    }
}
```

### var Best Practices

```java
import java.util.*;
import java.util.stream.*;

public class VarBestPractices {
    
    // Good use cases for var
    public void goodUses() {
        // 1. Obvious types
        var list = new ArrayList<String>();
        var map = new HashMap<String, Integer>();
        var set = new HashSet<Integer>();
        
        // 2. Complex generic types
        var entrySet = map.entrySet(); // Set<Map.Entry<String, Integer>>
        
        // 3. Lambda expressions
        var comparator = Comparator.comparing(Person::getAge)
                .thenComparing(Person::getName);
        
        // 4. Method chains
        var result = List.of("Alice", "Bob", "Charlie")
                .stream()
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        
        // 5. Try-with-resources
        try (var reader = new java.io.BufferedReader(new java.io.FileReader("file.txt"))) {
            var line = reader.readLine();
        } catch (Exception e) {
            // Handle exception
        }
    }
    
    // Bad use cases for var
    public void badUses() {
        // 1. Not obvious types
        // var x = computeSomething(); // What type is x?
        
        // 2. Multiple variables
        // var a = 1, b = 2; // Not allowed
        
        // 3. Null values
        // var y = null; // Can't infer type
        
        // 4. Field declarations
        // private var name; // Not allowed
        
        // 5. Method parameters
        // public void method(var x) {} // Not allowed
        
        // 6. Return types
        // public var getValue() {} // Not allowed
    }
    
    // Guidelines
    /*
     * Use var when:
     * - Type is obvious from context
     * - Improves readability with complex generics
     * - Reduces verbosity without losing clarity
     * - Working with lambda expressions
     * - In try-with-resources statements
     * 
     * Avoid var when:
     * - Type is not obvious
     * - Would reduce readability
     * - Working with primitive types where size matters
     * - In public API signatures
     */
    
    record Person(String name, int age) {}
    
    public static void main(String[] args) {
        var practice = new VarBestPractices();
        practice.goodUses();
        
        // var with records
        var person = new Person("Alice", 30);
        System.out.println("Person: " + person);
        
        // var with pattern matching (Java 21+)
        Object obj = "Hello";
        if (obj instanceof String s) {
            var length = s.length(); // var can use pattern variable
            System.out.println("Length: " + length);
        }
    }
}
```

---

## Summary

This guide covers advanced Java topics:

1. **Generics**: Type parameters, bounded types, wildcards, and type erasure
2. **Annotations**: Built-in, custom, and annotation processing
3. **Reflection**: Accessing class information, fields, methods, and constructors
4. **Streams**: Creation, intermediate/terminal operations, and patterns
5. **Optional**: Null handling, chaining, and best practices
6. **Records**: Immutable data carriers, compact constructors, and patterns
7. **Sealed Classes**: Restricting class hierarchies and exhaustive matching
8. **Pattern Matching**: instanceof, switch, and record patterns
9. **Text Blocks**: Multi-line strings and formatting
10. **var Keyword**: Local variable type inference and best practices

These features enable writing more concise, expressive, and maintainable Java code.

---

*Next: [Collections Framework](../collections/README.md)*
