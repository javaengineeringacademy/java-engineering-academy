# Quiz: File Operations

## Multiple Choice Questions

1. Which class represents a file?
   - A) File
   - B) Path
   - C) FileSystem
   - D) Directory

2. Which method checks if file exists?
   - A) `exists()`
   - B) `isFile()`
   - C) `check()`
   - D) `find()`

3. What does `File.delete()` return?
   - A) void
   - B) boolean
   - C) File
   - D) String

4. Which class is for NIO file operations?
   - A) Files
   - B) File
   - C) Path
   - D) FileSystem

5. What does `Files.copy()` do?
   - A) Copies file
   - B) Moves file
   - C) Deletes file
   - D) Creates file

## True/False Questions

6. File class is part of NIO.
   - True / False

7. Path is immutable.
   - True / False

8. Files utility class provides convenience methods.
   - True / False

## Code Output Questions

9. What will this code print?
```java
File file = new File("test.txt");
System.out.println(file.exists());
```

10. What will this code print?
```java
Path path = Path.of("/tmp", "test.txt");
System.out.println(path.toString());
```

## Answers

1. A - File class represents a file
2. A - exists() checks if file exists
3. B - delete() returns boolean
4. A - Files utility class for NIO operations
5. A - Files.copy() copies file
6. False - File is part of java.io, not NIO
7. True - Path objects are immutable
8. True - Files provides convenience methods
9. Output:
```
false
```
10. Output:
```
/tmp/test.txt
```
