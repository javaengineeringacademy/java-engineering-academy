# Text Processing Quiz

## Question 1
Why is `StringBuilder` preferred over `String` concatenation in loops?
- A) `StringBuilder` uses less memory and avoids creating intermediate String objects
- B) `StringBuilder` is always faster in all scenarios
- C) `String` concatenation doesn't work in loops
- D) `StringBuilder` is thread-safe while `String` is not

**Answer: A**
**Explanation:** String concatenation in a loop creates a new String object each iteration (O(n²) time). `StringBuilder` modifies a mutable buffer in-place, resulting in O(n) time complexity.

## Question 2
What is the difference between `String.equals()` and `==` for comparing two strings?
- A) They are identical in behavior
- B) `==` compares object references, while `equals()` compares the actual character content
- C) `equals()` compares references, `==` compares content
- D) `==` is faster and should always be used

**Answer: B**
**Explanation:** `==` checks if two references point to the same object in memory. `equals()` compares the actual string content character by character. Always use `equals()` for string content comparison.

## Question 3
What happens when you call `String.intern()` on a string?
- A) It deletes the string from memory
- B) It returns a canonical representation from the string pool, or adds the string to the pool if it doesn't exist
- C) It creates a copy of the string
- D) It converts the string to uppercase

**Answer: B**
**Explanation:** `intern()` checks the string pool for an equal string. If found, it returns the pool reference. If not, it adds the string to the pool and returns that reference. This saves memory for repeated strings.

## Question 4
What is the default capacity of a `StringBuilder` and how does it grow?
- A) Capacity 8, doubles on resize
- B) Capacity 16, grows by doubling current capacity + 2
- C) Capacity 32, grows by adding 16
- D) Capacity is unlimited

**Answer: B**
**Explanation:** `StringBuilder` starts with a capacity of 16 characters. When it needs to grow, the new capacity is calculated as `(oldCapacity + 1) * 2`, ensuring amortized O(1) append operations.

## Question 5
What is the output of `"hello".substring(1, 3)`?
- A) "hel"
- B) "ell"
- C) "el"
- D) "elll"

**Answer: C**
**Explanation:** `substring(1, 3)` returns characters from index 1 (inclusive) to index 3 (exclusive). So it returns the characters at positions 1 and 2, which is "el".