# Quiz: String

## Multiple Choice Questions

1. What is the main characteristic of String in Java?
   - A) Mutable
   - B) Immutable
   - C) Thread-unsafe
   - D) Array-based

2. Where are String literals stored?
   - A) Stack
   - B) Heap
   - C) String Pool
   - D) Method Area

3. What does `String.intern()` do?
   - A) Deletes the string
   - B) Returns canonical representation
   - C) Creates new string
   - D) Converts to StringBuilder

4. What is the output of `"hello".length()`?
   - A) 4
   - B) 5
   - C) 6
   - D) 10

5. Which method compares string content?
   - A) `==`
   - B) `compareTo()`
   - C) `equals()`
   - D) Both B and C

## True/False Questions

6. String in Java is mutable.
   - True / False

7. `==` compares string content in Java.
   - True / False

8. String implements CharSequence interface.
   - True / False

## Code Output Questions

9. What will this code print?
```java
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```

10. What will this code print?
```java
String s = "Hello";
System.out.println(s.toLowerCase());
System.out.println(s);
```

## Answers

1. B - String is immutable in Java
2. C - String literals are stored in String Pool
3. B - intern() returns canonical representation
4. B - "hello" has 5 characters
5. D - Both compareTo() and equals() compare content
6. False - String is immutable
7. False - == compares references, equals() compares content
8. True - String implements CharSequence
9. Output:
```
true
false
true
```
10. Output:
```
hello
Hello
```
(Original string unchanged due to immutability)
