# Quiz: Object Streams

## Multiple Choice Questions

1. What are object streams for?
   - A) Reading/writing objects
   - B) Reading/writing bytes
   - C) Reading/writing characters
   - D) Reading/writing files

2. Which interface must objects implement?
   - A) Serializable
   - B) Comparable
   - C) Cloneable
   - D) AutoCloseable

3. What does `writeObject()` do?
   - A) Writes object to stream
   - B) Creates object
   - C) Deletes object
   - D) Copies object

4. What does `readObject()` return?
   - A) Object
   - B) Serializable
   - C) String
   - D) byte[]

5. What is serialization?
   - A) Converting object to bytes
   - B) Converting bytes to object
   - C) Creating object
   - D) Deleting object

## True/False Questions

6. All objects can be serialized.
   - True / False

7. transient fields are not serialized.
   - True / False

8. serialVersionUID is optional.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Person implements Serializable {
    String name;
    int age;
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
Person p = new Person("Alice", 25);
ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutputStream oos = new ObjectOutputStream(baos);
oos.writeObject(p);
System.out.println(baos.size());
```

10. What will this code print?
```java
String s = "Hello";
System.out.println(s instanceof Serializable);
```

## Answers

1. A - Object streams read/write objects
2. A - Objects must implement Serializable
3. A - writeObject() writes object to stream
4. A - readObject() returns Object
5. A - Serialization converts object to bytes
6. False - Only Serializable objects can be serialized
7. True - transient fields are not serialized
8. False - serialVersionUID is recommended
9. Output:
```
93
```
10. Output:
```
true
```
