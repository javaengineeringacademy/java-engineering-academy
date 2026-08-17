# Arrays - Quiz

## Questions

### Q1: How do you declare an integer array of size 10?
- A) `int arr = new int[10];`
- B) `int[] arr = new int[10];`
- C) `int arr[10];`
- D) `int[10] arr;`

### Q2: What is the default value of elements in a new `int[]`?
- A) `null`
- B) `-1`
- C) `0`
- D) Undefined

### Q3: What is the `length` of `new String[5]`?
- A) 4
- B) 5
- C) 6
- D) 0

### Q4: Can arrays be resized after creation?
- A) Yes, with `resize()`
- B) No, arrays are fixed-size
- C) Only with ArrayList
- D) Only by reflection

### Q5: What is `ArrayIndexOutOfBoundsException`?
- A) Compilation error
- B) Runtime exception when accessing invalid index
- C) Logical error
- D) Checked exception

### Q6: How do you copy an array?
- A) `arr2 = arr`
- B) `Arrays.copyOf(arr, arr.length)`
- C) `arr.clone()`
- D) Both B and C

### Q7: What does `Arrays.sort()` do?
- A) Returns a new sorted array
- B) Sorts the array in-place
- C) Reverses the array
- D) Removes duplicates

### Q8: What is a 2D array?
- A) An array of arrays
- B) A matrix
- C) A linked list
- D) A map

### Q9: What is `Arrays.fill()`?
- A) Fills with random values
- B) Sets all elements to a specified value
- C) Resizes the array
- D) Removes elements

### Q10: What is the enhanced for loop limitation?
- A) Cannot iterate arrays
- B) Cannot modify array elements
- C) Too slow
- D) Requires a List

## Answers

1. **B** - `int[] arr = new int[10];` declares and allocates
2. **C** - Numeric arrays default to 0; object arrays default to null
3. **B** - `length` returns the number of elements (5)
4. **B** - Arrays are fixed-size; use ArrayList for dynamic sizing
5. **B** - Thrown at runtime when index is negative or >= length
6. **D** - Both `Arrays.copyOf()` and `.clone()` create copies
7. **B** - `Arrays.sort()` sorts the array in-place (no return)
8. **A** - 2D arrays are arrays of arrays
9. **B** - `Arrays.fill(arr, val)` sets all elements to val
10. **B** - Enhanced for loop gives read-only access; use index for modification
