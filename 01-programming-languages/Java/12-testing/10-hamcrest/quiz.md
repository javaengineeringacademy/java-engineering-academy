# Hamcrest Quiz

## Question 1
What does assertThat() do?

- A) Creates a matcher
- B) Verifies assertion using matcher
- C) Returns actual value
- D) Throws exception

**Answer: B**
**Explanation:** assertThat() uses a matcher to verify the actual value and throws AssertionError on failure.

---

## Question 2
What does is() matcher do?

- A) Checks equality
- B) Delegates to another matcher
- C) Creates new matcher
- D) Negates matcher

**Answer: B**
**Explanation:** is() is a decorator that wraps another matcher for readability.

---

## Question 3
How do you check a collection has a specific size?

- A) assertThat(list.size(), is(3))
- B) assertThat(list, hasSize(3))
- C) Both work
- D) Neither works

**Answer: C**
**Explanation:** Both approaches work; hasSize() is more readable and provides better error messages.

---

## Question 4
What does allOf() combine matchers with?

- A) OR logic
- B) AND logic
- C) XOR logic
- D) NOT logic

**Answer: B**
**Explanation:** allOf() requires all matchers to match (logical AND).

---

## Question 5
What is a TypeSafeMatcher?

- A) A matcher that never fails
- B) A generic matcher with type checking
- C) A thread-safe matcher
- D) A null-safe matcher

**Answer: B**
**Explanation:** TypeSafeMatcher provides compile-time type safety and automatically handles null/type mismatches.

---

## Question 6
What does containsString() check?

- A) String equals
- B) String contains substring
- C) String starts with
- D) String ends with

**Answer: B**
**Explanation:** containsString() checks that the actual string includes the expected substring.

---

## Question 7
How do you negate a matcher?

- A) not(matcher)
- B) !matcher
- C) negate(matcher)
- D) inverse(matcher)

**Answer: A**
**Explanation:** not() wraps a matcher and inverts its result.

---

## Question 8
What does anyOf() do?

- A) All matchers must match
- B) At least one matcher must match
- C) No matchers must match
- D) Exactly one matcher must match

**Answer: B**
- **Explanation:** anyOf() returns true if at least one matcher matches (logical OR).

---

## Question 9
What is the purpose of describeTo()?

- A) Describe the test
- B) Describe what the matcher expects
- C) Describe the actual value
- D) Describe the error

**Answer: B**
**Explanation:** describeTo() adds a description of the expected value to the error message.

---

## Question 10
How do you check a collection contains specific items?

- A) assertThat(list, hasItem("a"))
- B) assertThat(list, hasItems("a", "b"))
- C) Both work
- D) Neither works

**Answer: C**
**Explanation:** hasItem() checks for a single item; hasItems() checks for multiple items.
