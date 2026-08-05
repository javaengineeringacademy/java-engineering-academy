# Arrays

An array is a container that holds a fixed number of values of the same type. Once created, its size cannot change.

---

## Creating Arrays

```java
// Declare and allocate
int[] numbers = new int[5];  // 5 elements, all initialized to 0

// Declare and initialize with values
int[] scores = {90, 85, 78, 92, 88};

// Alternative syntax
String[] names = new String[] {"Alice", "Bob", "Charlie"};
```

---

## Accessing Elements

Elements are accessed by index, starting at 0.

```java
String[] colors = {"Red", "Green", "Blue"};

System.out.println(colors[0]);  // Red
System.out.println(colors[1]);  // Green
System.out.println(colors[2]);  // Blue
// colors[3]  // ArrayIndexOutOfBoundsException!
```

---

## Modifying Elements

```java
int[] values = {10, 20, 30};
values[1] = 25;  // values is now {10, 25, 30}
```

---

## Array Length

Every array has a `length` property.

```java
int[] data = {5, 10, 15, 20};
System.out.println(data.length);  // 4
```

Note: `length` is a property, not a method. No parentheses.

---

## Iterating Over Arrays

### For Loop

```java
int[] numbers = {10, 20, 30, 40, 50};

for (int i = 0; i < numbers.length; i++) {
    System.out.println("Element " + i + ": " + numbers[i]);
}
```

### For-Each Loop

```java
for (int num : numbers) {
    System.out.println(num);
}
```

Use for-each when you don't need the index. It's cleaner and less error-prone.

---

## Common Array Operations

### Find the Maximum

```java
int[] values = {34, 12, 56, 78, 23};
int max = values[0];

for (int i = 1; i < values.length; i++) {
    if (values[i] > max) {
        max = values[i];
    }
}

System.out.println("Maximum: " + max);  // 78
```

### Sum All Elements

```java
int[] values = {10, 20, 30, 40, 50};
int sum = 0;

for (int value : values) {
    sum += value;
}

System.out.println("Sum: " + sum);  // 150
```

### Reverse an Array

```java
int[] original = {1, 2, 3, 4, 5};
int[] reversed = new int[original.length];

for (int i = 0; i < original.length; i++) {
    reversed[i] = original[original.length - 1 - i];
}
// reversed is {5, 4, 3, 2, 1}
```

---

## Multi-Dimensional Arrays

Arrays of arrays.

```java
// 2D array (matrix)
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Access element
System.out.println(matrix[1][2]);  // 6 (row 1, column 2)

// Iterate
for (int row = 0; row < matrix.length; row++) {
    for (int col = 0; col < matrix[row].length; col++) {
        System.out.print(matrix[row][col] + " ");
    }
    System.out.println();
}
```

---

## Arrays Are Fixed-Size

Once created, an array's size cannot change. If you need a resizable collection, use an `ArrayList` (covered in the Collections module).

```java
int[] arr = new int[3];    // size 3
// Can't do: arr[3] = 10;  // ArrayIndexOutOfBoundsException

// To "resize," you create a new array and copy
int[] bigger = new int[5];
System.arraycopy(arr, 0, bigger, 0, arr.length);
arr = bigger;  // arr now points to the bigger array
```

---

## Common Mistakes

**Off-by-one errors:**
```java
int[] arr = {1, 2, 3};
// for (int i = 0; i <= arr.length; i++)  // won't work — i reaches 3
for (int i = 0; i < arr.length; i++)      // correct
```

**Empty array check:**
```java
int[] arr = {};
if (arr.length == 0) {
    System.out.println("Array is empty");
}
```

---

## Practice

1. Find the minimum value in an array
2. Count how many even numbers are in an array
3. Copy elements from one array to another in reverse order
4. Create a 2D array representing a 3x3 tic-tac-toe board

---

**Previous:** [04-Methods](../04-methods/)
**Next:** [06-Strings](../06-strings/)
