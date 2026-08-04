# Java Fundamentals

## Table of Contents

1. [Variables](#variables)
2. [Data Types](#data-types)
3. [Operators](#operators)
4. [Control Flow](#control-flow)
5. [Arrays](#arrays)
6. [Methods](#methods)
7. [Object-Oriented Programming](#object-oriented-programming)
8. [String Handling](#string-handling)
9. [Packages and Imports](#packages-and-imports)

---

## Variables

### Variable Declaration and Initialization

```java
// Explicit type declaration
int age = 25;
double salary = 75000.50;
String name = "John Doe";
boolean isActive = true;

// Type inference with var (Java 10+)
var count = 10;          // int
var price = 99.99;       // double
var message = "Hello";   // String

// Multiple declarations of same type
int x = 1, y = 2, z = 3;
```

### Variable Naming Conventions

```java
// camelCase for variables and methods
int studentAge = 20;
String firstName = "John";
boolean isGameOver = false;

// UPPER_SNAKE_CASE for constants
static final int MAX_SIZE = 100;
static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydb";

// PascalCase for classes
class EmployeeRecord { }

// prefix 'i' for interface (convention, not required)
interface Drawable { }
```

### Variable Scope

```java
public class VariableScope {
    // Instance variable - accessible throughout the class
    private int instanceVar = 10;
    
    // Static variable - belongs to the class
    private static int staticVar = 20;
    
    public void method() {
        // Local variable - accessible only within this method
        int localVar = 30;
        
        if (true) {
            // Block variable - accessible only within this block
            int blockVar = 40;
            System.out.println(blockVar); // OK
        }
        
        // System.out.println(blockVar); // Compile error - blockVar not accessible
        System.out.println(localVar); // OK
    }
}
```

### Variable Shadowing

```java
public class Shadowing {
    private int value = 10;
    
    public void demonstrate(int value) {
        // Parameter shadows instance variable
        System.out.println(value); // Prints parameter value
        
        // To access instance variable, use 'this'
        System.out.println(this.value); // Prints instance variable value
        
        {
            int value2 = 20; // Local variable shadows parameter
            System.out.println(value2);
        }
    }
}
```

---

## Data Types

### Primitive Data Types

```java
public class PrimitiveTypes {
    public static void main(String[] args) {
        // Integer types
        byte byteVar = 127;           // 8 bits, -128 to 127
        short shortVar = 32767;       // 16 bits, -32768 to 32767
        int intVar = 2147483647;      // 32 bits, -2^31 to 2^31-1
        long longVar = 9223372036854775807L; // 64 bits, suffix 'L'
        
        // Floating-point types
        float floatVar = 3.14f;       // 32 bits, suffix 'f'
        double doubleVar = 3.14159;   // 64 bits (default for decimals)
        
        // Character type
        char charVar = 'A';          // 16 bits, single quotes
        char unicodeChar = '\u0041';  // Unicode for 'A'
        
        // Boolean type
        boolean boolVar = true;       // 1 bit (conceptually)
        
        System.out.println("Byte: " + byteVar);
        System.out.println("Short: " + shortVar);
        System.out.println("Int: " + intVar);
        System.out.println("Long: " + longVar);
        System.out.println("Float: " + floatVar);
        System.out.println("Double: " + doubleVar);
        System.out.println("Char: " + charVar);
        System.out.println("Boolean: " + boolVar);
    }
}
```

### Type Casting

```java
public class TypeCasting {
    public static void main(String[] args) {
        // Widening casting (automatic) - smaller to larger type
        int intVal = 100;
        long longVal = intVal;        // int to long
        float floatVal = intVal;      // int to float
        double doubleVal = intVal;    // int to double
        
        // Narrowing casting (manual) - larger to smaller type
        double doubleVal2 = 9.78;
        int intVal2 = (int) doubleVal2; // double to int (truncates to 9)
        
        // Overflow example
        byte byteVal = (byte) 200;  // 200 > 127, results in -56
        
        System.out.println("Widened: " + longVal);
        System.out.println("Narrowed: " + intVal2);
        System.out.println("Overflow: " + byteVal);
    }
}
```

### Wrapper Classes

```java
public class WrapperClasses {
    public static void main(String[] args) {
        // Boxing (primitive to wrapper)
        Integer intObj = Integer.valueOf(42);
        Double doubleObj = Double.valueOf(3.14);
        
        // Unboxing (wrapper to primitive)
        int intPrimitive = intObj.intValue();
        double doublePrimitive = doubleObj;
        
        // Auto-boxing and unboxing (Java 5+)
        Integer autoBoxed = 42;        // Auto-boxing
        int autoUnboxed = autoBoxed;   // Auto-unboxing
        
        // Wrapper class methods
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        System.out.println(Integer.compare(10, 20));
        System.out.println(Integer.parseInt("123"));
        System.out.println(Integer.toBinaryString(255));
        System.out.println(Integer.toHexString(255));
        
        // String to int
        String numStr = "456";
        int num = Integer.parseInt(numStr);
        
        // Int to String
        String numStr2 = String.valueOf(num);
        String numStr3 = Integer.toString(num);
        String numStr4 = num + "";
    }
}
```

### Constants

```java
public class Constants {
    // Final variable (constant)
    public static final double PI = 3.141592653589793;
    public static final String APP_NAME = "MyApplication";
    
    // Compile-time constants
    public static final int MAX_RETRY = 3;
    
    // Instance constant (must be initialized in constructor)
    final int instanceConstant;
    
    public Constants(int value) {
        this.instanceConstant = value;
    }
    
    public static void main(String[] args) {
        // PI = 3.14;  // Compile error - cannot reassign final variable
        
        System.out.println("PI: " + PI);
        System.out.println("App Name: " + APP_NAME);
    }
}
```

---

## Operators

### Arithmetic Operators

```java
public class ArithmeticOperators {
    public static void main(String[] args) {
        int a = 10, b = 3;
        
        System.out.println("Addition: " + (a + b));       // 13
        System.out.println("Subtraction: " + (a - b));   // 7
        System.out.println("Multiplication: " + (a * b)); // 30
        System.out.println("Division: " + (a / b));      // 3 (integer division)
        System.out.println("Modulus: " + (a % b));       // 1 (remainder)
        
        // Increment/Decrement
        int x = 5;
        System.out.println("Post-increment: " + x++);  // prints 5, then x becomes 6
        System.out.println("Pre-increment: " + (++x));  // x becomes 7, prints 7
        
        // Compound assignment
        int y = 10;
        y += 5;   // y = y + 5 = 15
        y -= 3;   // y = y - 3 = 12
        y *= 2;   // y = y * 2 = 24
        y /= 4;   // y = y / 4 = 6
        y %= 4;   // y = y % 4 = 2
    }
}
```

### Relational Operators

```java
public class RelationalOperators {
    public static void main(String[] args) {
        int a = 10, b = 20;
        
        System.out.println("Equal: " + (a == b));        // false
        System.out.println("Not Equal: " + (a != b));    // true
        System.out.println("Greater: " + (a > b));       // false
        System.out.println("Less: " + (a < b));          // true
        System.out.println("Greater or Equal: " + (a >= b)); // false
        System.out.println("Less or Equal: " + (a <= b));    // true
        
        // Reference comparison (checks if same object)
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("Reference Equal: " + (s1 == s2)); // false
        System.out.println("Content Equal: " + s1.equals(s2)); // true
    }
}
```

### Logical Operators

```java
public class LogicalOperators {
    public static void main(String[] args) {
        boolean a = true, b = false;
        
        // Logical AND
        System.out.println("AND: " + (a && b));   // false
        
        // Logical OR
        System.out.println("OR: " + (a || b));    // true
        
        // Logical NOT
        System.out.println("NOT: " + (!a));       // false
        
        // Short-circuit evaluation
        int x = 0;
        boolean result = (x != 0) && (10 / x > 1); // Safe due to short-circuit
        System.out.println("Short-circuit: " + result);
        
        // Ternary operator
        int age = 20;
        String status = (age >= 18) ? "Adult" : "Minor";
        System.out.println("Status: " + status);
    }
}
```

### Bitwise Operators

```java
public class BitwiseOperators {
    public static void main(String[] args) {
        int a = 12;  // 1100 in binary
        int b = 10;  // 1010 in binary
        
        System.out.println("AND: " + (a & b));   // 8  (1000)
        System.out.println("OR: " + (a | b));    // 14 (1110)
        System.out.println("XOR: " + (a ^ b));  // 6  (0110)
        System.out.println("NOT: " + (~a));      // -13 (two's complement)
        
        // Shift operators
        System.out.println("Left Shift: " + (a << 2));   // 48 (110000)
        System.out.println("Right Shift: " + (a >> 2));   // 3 (11)
        System.out.println("Unsigned Right Shift: " + (a >>> 2)); // 3
        
        // Bit manipulation examples
        int num = 255;
        System.out.println("Is power of 2: " + ((num & (num - 1)) == 0)); // false
        System.out.println("Set bit 3: " + (num | (1 << 3)));  // 255
        System.out.println("Clear bit 3: " + (num & ~(1 << 3))); // 247
    }
}
```

### instanceof Operator

```java
public class InstanceofExample {
    public static void main(String[] args) {
        Object obj = "Hello, World!";
        
        // Basic instanceof
        if (obj instanceof String) {
            System.out.println("obj is a String");
        }
        
        // Pattern matching instanceof (Java 16+)
        if (obj instanceof String s) {
            System.out.println("String length: " + s.length());
        }
        
        // With logical operator (Java 17+)
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("Long string: " + s);
        }
        
        // With switch (Java 21+)
        Object data = 42;
        switch (data) {
            case Integer i -> System.out.println("Integer: " + i);
            case String s -> System.out.println("String: " + s);
            case Double d -> System.out.println("Double: " + d);
            default -> System.out.println("Unknown type");
        }
    }
}
```

---

## Control Flow

### if-else Statements

```java
public class IfElseExample {
    public static void main(String[] args) {
        int score = 85;
        
        // Simple if
        if (score >= 90) {
            System.out.println("Grade: A");
        }
        
        // if-else
        if (score >= 90) {
            System.out.println("Grade: A");
        } else {
            System.out.println("Grade: B or lower");
        }
        
        // if-else if-else
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade: B");
        } else if (score >= 70) {
            System.out.println("Grade: C");
        } else if (score >= 60) {
            System.out.println("Grade: D");
        } else {
            System.out.println("Grade: F");
        }
        
        // Nested if
        boolean hasTicket = true;
        int age = 25;
        
        if (hasTicket) {
            if (age >= 18) {
                System.out.println("Entry allowed");
            } else {
                System.out.println("Must be accompanied by adult");
            }
        } else {
            System.out.println("Purchase ticket first");
        }
    }
}
```

### switch Statement

```java
public class SwitchExample {
    public static void main(String[] args) {
        // Traditional switch
        int day = 3;
        String dayName;
        
        switch (day) {
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;
            case 7:
                dayName = "Sunday";
                break;
            default:
                dayName = "Invalid day";
                break;
        }
        System.out.println("Day: " + dayName);
        
        // Enhanced switch (Java 14+)
        String type = "ADMIN";
        String accessLevel = switch (type) {
            case "ADMIN" -> "Full access";
            case "USER" -> "Limited access";
            case "GUEST" -> "Read-only";
            default -> "No access";
        };
        System.out.println("Access: " + accessLevel);
        
        // Switch with yield (Java 14+)
        int numLetters = switch (day) {
            case 1 -> "Monday".length();
            case 2 -> "Tuesday".length();
            case 3 -> "Wednesday".length();
            case 4 -> "Thursday".length();
            case 5 -> "Friday".length();
            case 6, 7 -> "Weekend".length();
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println("Number of letters: " + numLetters);
        
        // Pattern matching for switch (Java 21+)
        Object obj = 42;
        String description = switch (obj) {
            case Integer i when i > 0 -> "Positive integer: " + i;
            case Integer i when i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            case String s -> "String: " + s;
            case null -> "Null value";
            default -> "Other type: " + obj.getClass().getSimpleName();
        };
        System.out.println(description);
    }
}
```

### Loops

```java
public class LoopExamples {
    public static void main(String[] args) {
        // for loop
        System.out.println("For loop:");
        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
        }
        
        // Enhanced for loop (for-each)
        System.out.println("\nFor-each loop:");
        int[] numbers = {1, 2, 3, 4, 5};
        for (int num : numbers) {
            System.out.println(num);
        }
        
        // while loop
        System.out.println("\nWhile loop:");
        int count = 0;
        while (count < 5) {
            System.out.println(count);
            count++;
        }
        
        // do-while loop
        System.out.println("\nDo-while loop:");
        int num = 1;
        do {
            System.out.println(num);
            num *= 2;
        } while (num <= 16);
        
        // Nested loops
        System.out.println("\nNested loops:");
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.print(i * j + "\t");
            }
            System.out.println();
        }
        
        // Labeled break and continue
        System.out.println("\nLabeled break:");
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 2 && j == 3) {
                    break outer;
                }
                System.out.print("(" + i + "," + j + ") ");
            }
            System.out.println();
        }
    }
}
```

### Enhanced for Loop with Collections

```java
import java.util.List;
import java.util.ArrayList;

public class EnhancedForExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        
        // Enhanced for loop
        for (String name : names) {
            System.out.println(name);
        }
        
        // Modifying during iteration (not recommended)
        for (String name : names) {
            if (name.equals("Bob")) {
                // names.remove(name); // ConcurrentModificationException
            }
        }
        
        // Use Iterator for safe removal
        var iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name.equals("Bob")) {
                iterator.remove();
            }
        }
    }
}
```

---

## Arrays

### Array Declaration and Initialization

```java
public class ArrayExamples {
    public static void main(String[] args) {
        // Declaration and initialization
        int[] numbers = new int[5];  // Array of 5 integers (default 0)
        String[] names = new String[3]; // Array of 3 strings (default null)
        
        // Initialize with values
        int[] scores = {85, 90, 78, 92, 88};
        String[] fruits = {"Apple", "Banana", "Cherry"};
        
        // Dynamic initialization
        int[] dynamicArray = new int[]{10, 20, 30, 40, 50};
        
        // Multi-dimensional arrays
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        // Jagged arrays
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1, 2};
        jagged[1] = new int[]{3, 4, 5};
        jagged[2] = new int[]{6, 7, 8, 9};
        
        // Accessing elements
        System.out.println("First score: " + scores[0]);
        System.out.println("Matrix[1][2]: " + matrix[1][2]);
        
        // Array length
        System.out.println("Array length: " + scores.length);
        System.out.println("Matrix rows: " + matrix.length);
        System.out.println("Matrix columns: " + matrix[0].length);
    }
}
```

### Array Operations

```java
import java.util.Arrays;

public class ArrayOperations {
    public static void main(String[] args) {
        int[] numbers = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        
        // Sorting
        Arrays.sort(numbers);
        System.out.println("Sorted: " + Arrays.toString(numbers));
        
        // Binary search (array must be sorted)
        int index = Arrays.binarySearch(numbers, 7);
        System.out.println("Index of 7: " + index);
        
        // Fill
        int[] filled = new int[5];
        Arrays.fill(filled, 10);
        System.out.println("Filled: " + Arrays.toString(filled));
        
        // Copy
        int[] copied = Arrays.copyOf(numbers, numbers.length);
        System.out.println("Copied: " + Arrays.toString(copied));
        
        // CopyOfRange
        int[] range = Arrays.copyOfRange(numbers, 2, 6);
        System.out.println("Range: " + Arrays.toString(range));
        
        // Equals
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        System.out.println("Equal: " + Arrays.equals(arr1, arr2));
        
        // DeepEquals (for multi-dimensional arrays)
        int[][] m1 = {{1, 2}, {3, 4}};
        int[][] m2 = {{1, 2}, {3, 4}};
        System.out.println("Deep Equal: " + Arrays.deepEquals(m1, m2));
        
        // Convert to string
        System.out.println("String: " + Arrays.toString(numbers));
        
        // Stream conversion (Java 8+)
        Arrays.stream(numbers)
            .filter(n -> n > 5)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();
    }
}
```

### Array Copying and Comparison

```java
import java.util.Arrays;

public class ArrayCopyingComparison {
    public static void main(String[] args) {
        int[] original = {1, 2, 3, 4, 5};
        
        // Shallow copy (reference copy)
        int[] referenceCopy = original;
        referenceCopy[0] = 99;
        System.out.println("Original after reference copy change: " + original[0]); // 99
        
        // System.arraycopy (efficient for large arrays)
        int[] systemCopy = new int[5];
        System.arraycopy(original, 0, systemCopy, 0, original.length);
        systemCopy[0] = 1;
        System.out.println("System copy: " + Arrays.toString(systemCopy));
        
        // Arrays.copyOf
        int[] copyOfArray = Arrays.copyOf(original, original.length);
        copyOfArray[1] = 2;
        
        // Arrays.copyOfRange
        int[] rangeCopy = Arrays.copyOfRange(original, 1, 4);
        System.out.println("Range copy: " + Arrays.toString(rangeCopy));
        
        // Comparison
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        
        System.out.println("arr1 equals arr2: " + Arrays.equals(arr1, arr2)); // true
        System.out.println("arr1 equals arr3: " + Arrays.equals(arr1, arr3)); // false
        
        // Compare (Java 9+)
        System.out.println("Compare: " + Arrays.compare(arr1, arr3)); // negative
    }
}
```

### Multi-dimensional Arrays

```java
import java.util.Arrays;

public class MultiDimensionalArrays {
    public static void main(String[] args) {
        // 2D Array
        int[][] matrix = new int[3][4];
        
        // Initialize
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = i * 4 + j + 1;
            }
        }
        
        // Print matrix
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        
        // 3D Array
        int[][][] cube = new int[2][3][4];
        cube[0][1][2] = 42;
        System.out.println("\nCube value: " + cube[0][1][2]);
        
        // Jagged array
        int[][] jagged = {
            {1, 2, 3},
            {4, 5},
            {6, 7, 8, 9}
        };
        
        System.out.println("\nJagged array:");
        for (int[] row : jagged) {
            System.out.println(Arrays.toString(row));
        }
        
        // Flattening 2D array
        int[][] twoD = {{1, 2}, {3, 4}, {5, 6}};
        int[] flattened = Arrays.stream(twoD)
            .flatMapToInt(Arrays::stream)
            .toArray();
        System.out.println("Flattened: " + Arrays.toString(flattened));
    }
}
```

---

## Methods

### Method Declaration

```java
public class MethodExamples {
    
    // Void method
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }
    
    // Method with return value
    public static int add(int a, int b) {
        return a + b;
    }
    
    // Method overloading
    public static double add(double a, double b) {
        return a + b;
    }
    
    public static int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Variable arguments (varargs)
    public static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
    
    // Method with named parameters (using objects)
    public static void printUserInfo(String name, int age, String email) {
        System.out.printf("Name: %s, Age: %d, Email: %s%n", name, age, email);
    }
    
    public static void main(String[] args) {
        greet("Alice");
        
        System.out.println("Add (int): " + add(5, 3));
        System.out.println("Add (double): " + add(5.5, 3.3));
        System.out.println("Add (3 params): " + add(1, 2, 3));
        
        System.out.println("Sum: " + sum(1, 2, 3, 4, 5));
        
        printUserInfo("John", 30, "john@example.com");
    }
}
```

### Pass by Value

```java
public class PassByValue {
    
    public static void modifyValue(int x) {
        x = 100; // Only modifies local copy
    }
    
    public static void modifyArray(int[] arr) {
        arr[0] = 100; // Modifies the actual array
    }
    
    public static void modifyObject(StringBuilder sb) {
        sb.append(" World"); // Modifies the object
    }
    
    public static void main(String[] args) {
        // Primitive - pass by value
        int num = 5;
        modifyValue(num);
        System.out.println("After modifyValue: " + num); // Still 5
        
        // Array - reference is passed by value
        int[] numbers = {1, 2, 3};
        modifyArray(numbers);
        System.out.println("After modifyArray: " + numbers[0]); // 100
        
        // Object - reference is passed by value
        StringBuilder str = new StringBuilder("Hello");
        modifyObject(str);
        System.out.println("After modifyObject: " + str); // Hello World
    }
}
```

### Recursion

```java
public class RecursionExamples {
    
    // Factorial
    public static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
    
    // Fibonacci
    public static int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    // Power
    public static double power(double base, int exponent) {
        if (exponent == 0) return 1;
        if (exponent < 0) return 1 / power(base, -exponent);
        return base * power(base, exponent - 1);
    }
    
    // Binary search
    public static int binarySearch(int[] arr, int target, int left, int right) {
        if (left > right) return -1;
        
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) return binarySearch(arr, target, mid + 1, right);
        return binarySearch(arr, target, left, mid - 1);
    }
    
    // Tower of Hanoi
    public static void towerOfHanoi(int n, char from, char to, char aux) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + from + " to " + to);
            return;
        }
        towerOfHanoi(n - 1, from, aux, to);
        System.out.println("Move disk " + n + " from " + from + " to " + to);
        towerOfHanoi(n - 1, aux, to, from);
    }
    
    public static void main(String[] args) {
        System.out.println("Factorial of 5: " + factorial(5));
        System.out.println("Fibonacci of 10: " + fibonacci(10));
        System.out.println("2^10: " + power(2, 10));
        
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("Binary search for 7: " + binarySearch(arr, 7, 0, arr.length - 1));
        
        System.out.println("\nTower of Hanoi with 3 disks:");
        towerOfHanoi(3, 'A', 'C', 'B');
    }
}
```

---

## Object-Oriented Programming

### Classes and Objects

```java
public class Person {
    // Instance variables
    private String name;
    private int age;
    private String email;
    
    // Static variable
    private static int totalPersons = 0;
    
    // Default constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
        this.email = "";
        totalPersons++;
    }
    
    // Parameterized constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        totalPersons++;
    }
    
    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
        this.email = other.email;
        totalPersons++;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age");
        }
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Static method
    public static int getTotalPersons() {
        return totalPersons;
    }
    
    // Instance method
    public boolean isAdult() {
        return age >= 18;
    }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && 
               Objects.equals(name, person.name) && 
               Objects.equals(email, person.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }
    
    public static void main(String[] args) {
        Person person1 = new Person("Alice", 30, "alice@example.com");
        Person person2 = new Person("Bob", 25, "bob@example.com");
        Person person3 = new Person(person1);
        
        System.out.println(person1);
        System.out.println(person2);
        System.out.println(person3);
        System.out.println("Total persons: " + Person.getTotalPersons());
        
        System.out.println(person1.getName() + " is adult: " + person1.isAdult());
        
        person1.setName("Alice Smith");
        System.out.println("Updated: " + person1);
    }
}
```

### Inheritance

```java
// Base class
public class Animal {
    protected String name;
    protected int age;
    
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void eat() {
        System.out.println(name + " is eating");
    }
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
    
    public String getInfo() {
        return "Name: " + name + ", Age: " + age;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name='" + name + "', age=" + age + "}";
    }
}

// Derived class
public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, int age, String breed) {
        super(name, age); // Call parent constructor
        this.breed = breed;
    }
    
    public void bark() {
        System.out.println(name + " is barking");
    }
    
    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
    }
    
    @Override
    public String getInfo() {
        return super.getInfo() + ", Breed: " + breed;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("}", ", breed='" + breed + "'}");
    }
}

// Another derived class
public class Cat extends Animal {
    private boolean isIndoor;
    
    public Cat(String name, int age, boolean isIndoor) {
        super(name, age);
        this.isIndoor = isIndoor;
    }
    
    public void meow() {
        System.out.println(name + " is meowing");
    }
    
    public void purr() {
        System.out.println(name + " is purring");
    }
}

// Usage
public class InheritanceExample {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy", 3, "Golden Retriever");
        Cat cat = new Cat("Whiskers", 2, true);
        
        System.out.println(dog);
        System.out.println(cat);
        
        dog.eat();    // Inherited method
        dog.bark();   // Dog-specific method
        cat.sleep();  // Inherited method
        cat.meow();   // Cat-specific method
        
        // Polymorphism
        Animal animal = new Dog("Rex", 5, "German Shepherd");
        animal.eat();  // Calls Dog's eat if overridden, otherwise Animal's
    }
}
```

### Polymorphism

```java
public class Shape {
    public double calculateArea() {
        return 0;
    }
    
    public void draw() {
        System.out.println("Drawing a generic shape");
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a circle with radius " + radius);
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle " + width + "x" + height);
    }
}

public class Triangle extends Shape {
    private double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a triangle with base " + base + " and height " + height);
    }
}

// Polymorphism in action
public class PolymorphismExample {
    public static void processShape(Shape shape) {
        shape.draw();
        System.out.println("Area: " + shape.calculateArea());
    }
    
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8)
        };
        
        for (Shape shape : shapes) {
            processShape(shape);
            System.out.println();
        }
    }
}
```

### Abstraction

```java
// Abstract class
public abstract class Vehicle {
    protected String make;
    protected String model;
    protected int year;
    
    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }
    
    // Abstract method (must be implemented by subclasses)
    public abstract void start();
    public abstract void stop();
    public abstract double calculateFuelEfficiency();
    
    // Concrete method
    public void displayInfo() {
        System.out.println(year + " " + make + " " + model);
    }
    
    @Override
    public String toString() {
        return year + " " + make + " " + model;
    }
}

// Interface
public interface Electric {
    void charge();
    int getBatteryLevel();
    default void showChargingStatus() {
        System.out.println("Battery level: " + getBatteryLevel() + "%");
    }
}

// Interface with static method
public interface Serializable {
    static boolean isSerializable(Object obj) {
        return obj instanceof Serializable;
    }
}

// Concrete class implementing abstract class and interface
public class Car extends Vehicle implements Electric {
    private int batteryLevel;
    private double fuelEfficiency;
    
    public Car(String make, String model, int year, double fuelEfficiency) {
        super(make, model, year);
        this.batteryLevel = 100;
        this.fuelEfficiency = fuelEfficiency;
    }
    
    @Override
    public void start() {
        System.out.println("Car engine started");
    }
    
    @Override
    public void stop() {
        System.out.println("Car engine stopped");
    }
    
    @Override
    public double calculateFuelEfficiency() {
        return fuelEfficiency;
    }
    
    @Override
    public void charge() {
        System.out.println("Car is charging");
        batteryLevel = 100;
    }
    
    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }
}

// Usage
public class AbstractionExample {
    public static void main(String[] args) {
        Car car = new Car("Tesla", "Model 3", 2024, 120.0);
        
        car.displayInfo();
        car.start();
        car.charge();
        car.showChargingStatus();
        
        // Using interface as type
        Electric electricVehicle = car;
        electricVehicle.charge();
        
        // Using abstract class as type
        Vehicle vehicle = car;
        vehicle.start();
    }
}
```

### Encapsulation

```java
public class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;
    private boolean isActive;
    
    // Private constructor for factory pattern
    private BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.isActive = true;
    }
    
    // Factory method
    public static BankAccount createAccount(String ownerName, double initialBalance) {
        String accountNumber = generateAccountNumber();
        return new BankAccount(accountNumber, ownerName, initialBalance);
    }
    
    private static String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
    
    // Public methods with validation
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        if (!isActive) {
            throw new IllegalStateException("Account is not active");
        }
        balance += amount;
        System.out.println("Deposited: $" + amount);
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (!isActive) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        System.out.println("Withdrawn: $" + amount);
    }
    
    // Getters (no setters for sensitive data)
    public String getAccountNumber() {
        // Mask account number for security
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Method to close account
    public void closeAccount() {
        if (balance != 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance");
        }
        isActive = false;
        System.out.println("Account closed");
    }
    
    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + getAccountNumber() + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                '}';
    }
    
    public static void main(String[] args) {
        BankAccount account = BankAccount.createAccount("John Doe", 1000);
        
        System.out.println(account);
        
        account.deposit(500);
        System.out.println("After deposit: $" + account.getBalance());
        
        account.withdraw(200);
        System.out.println("After withdrawal: $" + account.getBalance());
        
        System.out.println("Account: " + account.getAccountNumber());
    }
}
```

---

## String Handling

### String Basics

```java
public class StringBasics {
    public static void main(String[] args) {
        // String creation
        String s1 = "Hello";                    // String literal
        String s2 = new String("Hello");        // New String object
        String s3 = "Hello";                    // String literal
        
        // Reference comparison
        System.out.println("s1 == s2: " + (s1 == s2));     // false
        System.out.println("s1 == s3: " + (s1 == s3));     // true (same literal)
        
        // Content comparison
        System.out.println("s1.equals(s2): " + s1.equals(s2)); // true
        
        // String length
        System.out.println("Length: " + s1.length());
        
        // Access character
        System.out.println("Char at 0: " + s1.charAt(0));
        
        // Substring
        System.out.println("Substring(1, 3): " + s1.substring(1, 3));
        
        // Index of
        System.out.println("Index of 'l': " + s1.indexOf('l'));
        
        // String concatenation
        String greeting = "Hello" + " " + "World";
        System.out.println("Greeting: " + greeting);
        
        // String with numbers
        String withNumber = "Number: " + 42;
        System.out.println("With number: " + withNumber);
        
        // parseInt
        int num = Integer.parseInt("42");
        System.out.println("Parsed: " + num);
    }
}
```

### String Methods

```java
public class StringMethods {
    public static void main(String[] args) {
        String str = "  Hello, World!  ";
        
        // Case methods
        System.out.println("Upper: " + str.toUpperCase());
        System.out.println("Lower: " + str.toLowerCase());
        
        // Trim and strip
        System.out.println("Trim: '" + str.trim() + "'");
        System.out.println("Strip: '" + str.strip() + "'");
        System.out.println("StripLeading: '" + str.stripLeading() + "'");
        System.out.println("StripTrailing: '" + str.stripTrailing() + "'");
        
        // Replace
        System.out.println("Replace: " + str.replace("World", "Java"));
        System.out.println("ReplaceAll: " + str.replaceAll("[aeiou]", "*"));
        System.out.println("ReplaceFirst: " + str.replaceFirst("l", "L"));
        
        // Split
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        for (String fruit : fruits) {
            System.out.println("Fruit: " + fruit);
        }
        
        // Join
        String joined = String.join(" - ", "A", "B", "C");
        System.out.println("Joined: " + joined);
        
        // Contains
        System.out.println("Contains 'World': " + str.contains("World"));
        
        // Starts/Ends with
        System.out.println("Starts with '  H': " + str.startsWith("  H"));
        System.out.println("Ends with '!  ': " + str.endsWith("!  "));
        
        // Char array conversion
        char[] chars = str.toCharArray();
        String fromChars = new String(chars);
        
        // Value of
        String boolStr = String.valueOf(true);
        String intStr = String.valueOf(123);
        
        // Format
        String formatted = String.format("Name: %s, Age: %d", "John", 30);
        System.out.println("Formatted: " + formatted);
        
        // Indent and transform (Java 12+)
        String multiLine = "Line 1\nLine 2\nLine 3";
        System.out.println("Indented:\n" + multiLine.indent(4));
        
        // Repeat (Java 11+)
        String repeated = "Ha".repeat(3);
        System.out.println("Repeated: " + repeated);
    }
}
```

### StringBuilder and StringBuffer

```java
public class StringBuilderExample {
    public static void main(String[] args) {
        // StringBuilder (not thread-safe, faster)
        StringBuilder sb = new StringBuilder();
        
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Append: " + sb.toString());
        
        sb.insert(5, ",");
        System.out.println("Insert: " + sb.toString());
        
        sb.replace(7, 12, "Java");
        System.out.println("Replace: " + sb.toString());
        
        sb.delete(5, 6);
        System.out.println("Delete: " + sb.toString());
        
        sb.reverse();
        System.out.println("Reverse: " + sb.toString());
        
        System.out.println("Length: " + sb.length());
        System.out.println("Capacity: " + sb.capacity());
        
        // Chaining
        String result = new StringBuilder()
                .append("SELECT * FROM ")
                .append("users WHERE ")
                .append("age > ")
                .append(18)
                .toString();
        System.out.println("Query: " + result);
        
        // StringBuffer (thread-safe)
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Thread-safe ");
        stringBuffer.append("string building");
        System.out.println("StringBuffer: " + stringBuffer.toString());
    }
}
```

### String Immutability

```java
public class StringImmutability {
    public static void main(String[] args) {
        // Strings are immutable
        String original = "Hello";
        String modified = original.concat(" World");
        
        System.out.println("Original: " + original);  // Hello
        System.out.println("Modified: " + modified);   // Hello World
        
        // String pool
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        
        System.out.println("s1 == s2: " + (s1 == s2));       // true (same pool object)
        System.out.println("s1 == s3: " + (s1 == s3));       // false
        System.out.println("s1.equals(s3): " + s1.equals(s3)); // true
        
        // intern() method
        String s4 = s3.intern();
        System.out.println("s1 == s4: " + (s1 == s4));       // true
        
        // Why immutability matters
        // 1. Security - String can't be modified maliciously
        // 2. Thread safety - No synchronization needed
        // 3. Hashing - Hash code can be cached
        // 4. String pool - Strings can be shared
        
        // Performance consideration
        // Bad - creates multiple intermediate strings
        String bad = "";
        for (int i = 0; i < 1000; i++) {
            bad += "a"; // Creates new String each time
        }
        
        // Good - uses StringBuilder
        StringBuilder good = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            good.append("a");
        }
        String result = good.toString();
    }
}
```

---

## Packages and Imports

### Package Declaration

```java
// File: src/com/example/utils/StringHelper.java
package com.example.utils;

public class StringHelper {
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    
    public static boolean isPalindrome(String str) {
        String cleaned = str.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        return cleaned.equals(reverse(cleaned));
    }
}
```

### Import Statements

```java
// Import specific class
import java.util.ArrayList;

// Import all classes from package
import java.util.*;

// Import static members
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

// Import with name conflicts
import java.util.Date;
import java.sql.Date as SqlDate; // Not valid Java, for illustration

public class ImportExamples {
    public static void main(String[] args) {
        // Using imported classes
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        
        // Using static imports
        double radius = 5;
        double area = PI * sqrt(radius);
        
        // Resolving name conflicts
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        // Java.lang is automatically imported
        String str = "Hello"; // No import needed
        int num = 42;         // No import needed
        
        // Default package
        // Classes in default package can't be imported by named packages
    }
}
```

### Package Structure

```
src/
├── com/
│   └── example/
│       ├── app/
│       │   ├── Main.java
│       │   └── Application.java
│       ├── models/
│       │   ├── User.java
│       │   └── Product.java
│       ├── services/
│       │   ├── UserService.java
│       │   └── ProductService.java
│       ├── repositories/
│       │   ├── UserRepository.java
│       │   └── ProductRepository.java
│       └── utils/
│           ├── StringUtils.java
│           └── DateUtils.java
```

### Module System (Java 9+)

```java
// File: module-info.java
module com.example.myapp {
    // Dependencies
    requires java.sql;
    requires java.logging;
    requires com.google.gson;
    
    // Export packages to other modules
    exports com.example.app;
    exports com.example.models;
    
    // Open package for reflection (e.g., for frameworks)
    opens com.example.models to com.fasterxml.jackson.databind;
    
    // Service provider
    uses com.example.spi.MyService;
    provides com.example.spi.MyService with com.example.app.MyServiceImpl;
}

// Using the module
// java --module-path libs --module com.example.myapp/com.example.app.Main
```

### Access Modifiers

```java
public class AccessModifiers {
    public int publicVar = 1;           // Accessible everywhere
    protected int protectedVar = 2;     // Accessible in same package and subclasses
    int defaultVar = 3;                 // Accessible in same package only
    private int privateVar = 4;         // Accessible only in this class
    
    public void publicMethod() {}
    protected void protectedMethod() {}
    void defaultMethod() {}
    private void privateMethod() {}
    
    // Constructor access
    public AccessModifiers() {}
    protected AccessModifiers(int value) {}
    AccessModifiers(String value) {}    // package-private
    private AccessModifiers(double value) {}
    
    // Nested class access
    public class PublicNested {}
    protected class ProtectedNested {}
    class DefaultNested {}
    private class PrivateNested {}
    
    public static void main(String[] args) {
        AccessModifiers obj = new AccessModifiers();
        
        // All accessible within same class
        System.out.println(obj.publicVar);
        System.out.println(obj.protectedVar);
        System.out.println(obj.defaultVar);
        System.out.println(obj.privateVar);
    }
}
```

---

## Summary

This guide covers the fundamental concepts of Java programming:

1. **Variables**: Declaration, initialization, scope, and naming conventions
2. **Data Types**: Primitives, wrapper classes, and type casting
3. **Operators**: Arithmetic, relational, logical, bitwise, and instanceof
4. **Control Flow**: if-else, switch, loops, and enhanced switch
5. **Arrays**: Declaration, initialization, operations, and multi-dimensional arrays
6. **Methods**: Declaration, overloading, recursion, and pass-by-value
7. **OOP**: Classes, objects, inheritance, polymorphism, abstraction, and encapsulation
8. **String Handling**: Immutability, methods, StringBuilder, and StringBuffer
9. **Packages**: Organization, imports, modules, and access modifiers

Understanding these fundamentals is crucial for writing effective Java code and preparing for more advanced topics.

---

*Next: [Advanced Topics](../advanced/README.md)*
