# Control Flow - Quiz

## Questions

### Q1: Which keyword is used to exit a loop prematurely?
- A) `return`
- B) `break`
- C) `continue`
- D) `skip`

### Q2: What does `continue` do in a for loop?
- A) Exits the loop entirely
- B) Skips to the next iteration
- C) Restarts the loop from the beginning
- D) Terminates the method

### Q3: Can a `switch` statement use `String` values?
- A) Only since Java 7
- B) Only since Java 12
- C) Never
- D) Since Java 1.0

### Q4: What is the difference between `if-else` and `switch`?
- A) No difference
- B) switch is for discrete values, if-else for ranges
- C) switch is faster always
- D) if-else cannot use booleans

### Q5: What is a labeled break?
- A) A break with a comment
- B) A break that exits a named outer block
- C) A break in a switch
- D) An invalid construct

### Q6: What is pattern matching for switch (Java 21)?
- A) Matching regex patterns
- B) Type checking and binding in case labels
- C) Matching string patterns
- D) Enum matching only

### Q7: How many `case` labels can a single switch have?
- A) One
- B) Up to 255
- C) Unlimited
- D) Depends on the type

### Q8: What happens if no case matches and there's no default?
- A) Compilation error
- B) Runtime exception
- C) Nothing; the switch is skipped
- D) First case executes

### Q9: What is the enhanced for loop used for?
- A) Any loop
- B) Iterating over arrays and Iterable collections
- C) While loops only
- D) Infinite loops

### Q10: What is the result of an infinite `while(true)` loop?
- A) Compilation error
- B) The loop runs forever unless broken
- C) Runtime exception
- D) It never executes

## Answers

1. **B** - `break` exits the innermost loop or switch
2. **B** - `continue` skips the rest of the current iteration and moves to the next
3. **A** - String switch was added in Java 7
4. **B** - switch works with discrete values (enums, ints, strings); if-else handles ranges and complex conditions
5. **B** - `break label` exits the named outer block
6. **B** - `case Type t ->` checks type and binds to a variable
7. **C** - There is no limit on the number of case labels
8. **C** - Without a default, the switch simply does nothing
9. **B** - Enhanced for (`for (T x : collection)`) iterates over arrays or Iterable
10. **B** - `while(true)` runs forever; use `break` to exit
