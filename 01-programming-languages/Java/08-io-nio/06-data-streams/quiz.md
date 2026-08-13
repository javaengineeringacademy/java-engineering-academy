# Quiz: Data Streams

## Multiple Choice Questions

1. What are data streams for?
   - A) Reading/writing primitive types
   - B) Reading/writing characters
   - C) Reading/writing bytes
   - D) Reading/writing strings

2. Which class writes primitive types?
   - A) DataOutputStream
   - B) DataInputStream
   - C) ObjectOutputStream
   - D) FileOutputStream

3. What does `writeInt()` write?
   - A) 2 bytes
   - B) 4 bytes
   - C) 8 bytes
   - D) Variable bytes

4. What does `readDouble()` read?
   - A) 4 bytes
   - B) 8 bytes
   - C) 16 bytes
   - D) Variable bytes

5. What is the order of reading?
   - A) Same as writing
   - B) Reverse of writing
   - C) Random order
   - D) Any order

## True/False Questions

6. Data streams are for text data.
   - True / False

7. Data streams use binary format.
   - True / False

8. You can read in any order.
   - True / False

## Code Output Questions

9. What will this code print?
```java
ByteArrayOutputStream baos = new ByteArrayOutputStream();
DataOutputStream dos = new DataOutputStream(baos);
dos.writeInt(12345);
dos.close();
System.out.println(baos.size());
```

10. What will this code print?
```java
double d = 3.14;
System.out.println(Double.doubleToLongBits(d));
```

## Answers

1. A - Data streams read/write primitive types
2. A - DataOutputStream writes primitive types
3. B - writeInt() writes 4 bytes
4. B - readDouble() reads 8 bytes
5. A - Must read in same order as writing
6. False - Data streams are for primitive types
7. True - Data streams use binary format
8. False - Must read in same order as writing
9. Output:
```
4
```
10. Output:
```
4614256656552042342
```
