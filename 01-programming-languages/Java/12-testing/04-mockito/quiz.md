# Mockito Quiz

## Question 1
What does @Mock create?

- A) A real object
- B) A test double that returns defaults
- C) A spy
- D) A stub

**Answer: B**
**Explanation:** @Mock creates a test double with default return values and no real behavior.

---

## Question 2
Which method verifies a method was called?

- A) verify()
- B) assert()
- C) check()
- D) validate()

**Answer: A**
**Explanation:** verify(mock).method() checks that the method was called on the mock.

---

## Question 3
What does when().thenReturn() do?

- A) Executes real method
- B) Stubs return value
- C) Verifies call
- D) Resets mock

**Answer: B**
**Explanation:** when(mock.method()).thenReturn(value) configures the mock to return a specific value.

---

## Question 4
What is @InjectMocks?

- A) Injects real dependencies
- B) Creates class under test with mocks injected
- C) Mocks all dependencies
- D) Injects test data

**Answer: B**
**Explanation:** @InjectMocks creates the class under test and injects @Mock and @Spy fields.

---

## Question 5
What does any() matcher match?

- A) Nothing
- B) Any non-null value
- C) Any value including null
- D) Any string

**Answer: C**
**Explanation:** any() matches any argument including null. Use any(Class) for type-safe matching.

---

## Question 6
What does times(3) verify?

- A) Method called at most 3 times
- B) Method called exactly 3 times
- C) Method called at least 3 times
- D) Method called 3 times or more

**Answer: B**
**Explanation:** times(3) verifies the method was called exactly 3 times.

---

## Question 7
What does reset(mock) do?

- A) Deletes the mock
- B) Clears all stubbing and invocation history
- C) Restores default behavior
- D) Both B and C

**Answer: D**
**Explanation:** reset clears stubbing and invocation history, restoring the mock to default state.

---

## Question 8
What does @Captor do?

- A) Captures exceptions
- B) Captures argument for later verification
- C) Captures return values
- D) Captures mock state

**Answer: B**
**Explanation:** @Captor captures arguments passed to mock methods for detailed verification.

---

## Question 9
What is the difference between mock() and @Mock?

- A) No difference
- B) @Mock is annotation-based, mock() is programmatic
- C) mock() is faster
- D) @Mock creates spies

**Answer: B**
**Explanation:** @Mock uses annotation processing (MockitoAnnotations.openMocks), mock() creates inline.

---

## Question 10
What does verifyNoInteractions() check?

- A) Mock was never used
- B) No methods were called on the mock
- C) Mock has no stubs
- D) Mock is properly initialized

**Answer: B**
**Explanation:** verifyNoInteractions verifies no methods were called on the mock since last verify.
