# AssertJ Quiz

## Question 1
What does assertThat() return?

- A) void
- B) An assertion object
- C) The actual value
- D) A boolean

**Answer: B**
**Explanation:** assertThat() returns an AbstractAssert object that provides fluent assertion methods.

---

## Question 2
How do you assert a collection has a specific size?

- A) assertThat(list.size()).isEqualTo(3)
- B) assertThat(list).hasSize(3)
- C) Both A and B work
- D) Neither works

**Answer: C**
**Explanation:** Both approaches work, but hasSize() is more readable and provides better error messages.

---

## Question 3
What does extracting() do?

- A) Extracts assertion from test
- B) Extracts properties from objects
- C) Extracts exceptions
- D) Extracts collections

**Answer: B**
**Explanation:** extracting() pulls property values from objects for comparison.

---

## Question 4
How do you assert an exception is thrown?

- A) assertThrows()
- B) assertThatThrownBy()
- C) Both A and B
- D) Neither

**Answer: C**
**Explanation:** Both JUnit's assertThrows() and AssertJ's assertThatThrownBy() work.

---

## Question 5
What is the advantage of AssertJ error messages?

- A) They are shorter
- B) They are more descriptive
- C) They are colored
- D) They are formatted as JSON

**Answer: B**
**Explanation:** AssertJ generates detailed error messages showing expected vs actual values with context.

---

## Question 6
How do you assert a string contains a substring?

- A) assertThat(str).hasSubstring()
- B) assertThat(str).contains()
- C) assertThat(str).includes()
- D) assertThat(str).hasText()

**Answer: B**
**Explanation:** contains() checks that the string includes the specified substring.

---

## Question 7
What does isInstanceOf() check?

- A) Object equality
- B) Type compatibility
- C) Null reference
- D) Array contents

**Answer: B**
**Explanation:** isInstanceOf() verifies the actual object is an instance of the specified class.

---

## Question 8
How do you chain assertions?

- A) Using .and()
- B) Using .also()
- C) Just call another assertion method
- D) Using .then()

**Answer: C**
**Explanation:** AssertJ supports fluent chaining by calling assertion methods sequentially.

---

## Question 9
What does describedAs() do?

- A) Describes the test method
- B) Adds custom error message
- C) Adds test description
- D) Both B and C

**Answer: D**
**Explanation:** describedAs() adds a description used in error messages and test documentation.

---

## Question 10
How do you assert a map contains a key-value pair?

- A) assertThat(map).containsEntry(key, value)
- B) assertThat(map).hasEntry(key, value)
- C) Both A and B work
- D) Neither works

**Answer: C**
**Explanation:** Both containsEntry() and hasEntry() work; they are aliases.
