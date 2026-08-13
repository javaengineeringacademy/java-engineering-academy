# Quiz: Object Lifecycle

## Multiple Choice Questions

1. What are the main stages of an object's lifecycle?
   - A) Creation, usage, destruction
   - B) Declaration, assignment, deletion
   - C) Compilation, execution, termination
   - D) Initialization, modification, garbage collection

2. When is an object eligible for garbage collection?
   - A) When it's created
   - B) When no references point to it
   - C) When its method is called
   - D) When the program ends

3. Which method is called when an object is about to be garbage collected?
   - A) finalize()
   - B) destroy()
   - C) cleanup()
   - D) dispose()

4. What does System.gc() do?
   - A) Immediately garbage collects all objects
   - B) Suggests to the JVM that garbage collection may be helpful
   - C) Disables garbage collection
   - D) Creates more memory

5. What is a strong reference?
   - A) A reference that prevents garbage collection
   - B) A reference that is always null
   - C) A weak reference
   - D) A soft reference

## True/False Questions

6. An object is destroyed as soon as all its references are set to null.
   - True / False

7. The finalize() method is called exactly once before garbage collection.
   - True / False

8. Setting a reference to null makes the object immediately eligible for garbage collection (if no other references exist).
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Resource {
    private String name;
    Resource(String name) { this.name = name; System.out.println("Created: " + name); }
    protected void finalize() { System.out.println("Finalized: " + name); }
}
class Test {
    public static void main(String[] args) {
        Resource r1 = new Resource("First");
        Resource r2 = new Resource("Second");
        r1 = null;
        r2 = null;
        System.gc();
        System.out.println("Done");
    }
}
```

10. What will this code print?
```java
class Node {
    String value;
    Node(String value) { this.value = value; }
    public String toString() { return value; }
}
class Test {
    public static void main(String[] args) {
        Node a = new Node("A");
        Node b = a;
        System.out.println(a == b);
        a = new Node("A'");
        System.out.println(a == b);
        b = null;
        System.out.println(a);
    }
}
```

## Answers

1. A
2. B
3. A - finalize() is called (though deprecated since Java 9)
4. B - It's a hint, not a command
5. A
6. False - The JVM decides when to actually garbage collect
7. True
8. True (assuming no other strong references)
9. Output (order of finalize may vary):
```
Created: First
Created: Second
Done
Finalized: First
Finalized: Second
```
10. Output:
```
true
false
A'
```
