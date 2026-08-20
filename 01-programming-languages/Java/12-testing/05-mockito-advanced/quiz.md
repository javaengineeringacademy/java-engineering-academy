# Advanced Mockito Quiz

## Question 1
What is the key difference between @Mock and @Spy?

- A) @Mock calls real methods, @Spy does not
- B) @Spy calls real methods by default, @Mock does not
- C) They are identical
- D) @Spy is only for interfaces

**Answer: B**
**Explanation:** @Spy wraps a real object and calls real methods by default; @Mock creates a proxy with no real behavior.

---

## Question 2
How do you stub a void method?

- A) when(mock.voidMethod()).thenReturn(null)
- B) doNothing().when(mock).voidMethod()
- C) stub(mock.voidMethod())
- D) mock.voidMethod().returns(null)

**Answer: B**
**Explanation:** doNothing().when(mock).voidMethod() is the correct syntax for void method stubbing.

---

## Question 3
What does given() import from?

- A) org.mockito.Mockito
- B) org.mockito.BDDMockito
- C) org.junit.jupiter.api
- D) org.mockito.junit

**Answer: B**
**Explanation:** given() is part of BDDMockito for BDD-style stubbing.

---

## Question 4
When should you use doAnswer()?

- A) When you need to return a fixed value
- B) When you need to process arguments dynamically
- C) When you need to throw an exception
- D) When you need to do nothing

**Answer: B**
**Explanation:** doAnswer allows processing arguments and returning dynamic values based on input.

---

## Question 5
What does then().should() verify in BDD style?

- A) Method was called once
- B) Method was called with specific args
- C) Interaction happened after when phase
- D) All of the above

**Answer: D**
**Explanation:** then().should() verifies the interaction happened as expected after the when phase.

---

## Question 6
How do you create a spy of a real object?

- A) @Spy annotation on field
- B) spy(new RealObject())
- C) Mockito.spy(RealObject.class)
- D) Both A and B

**Answer: D**
**Explanation:** @Spy can be used on fields or spy() method can create spies programmatically.

---

## Question 7
What happens when you stub a method on a spy that calls the real method?

- A) Real method executes
- B) Stubbed behavior executes
- C) Both execute
- D) NullPointerException

**Answer: B**
**Explanation:** Stubbed methods use mock behavior; only unstubbed methods call the real implementation.

---

## Question 8
What is the purpose of doReturn() over when().thenReturn() for spies?

- A) Better performance
- B) Avoids calling real method during stubbing
- C) Supports void methods
- D) Both A and B

**Answer: B**
**Explanation:** doReturn() doesn't invoke the real method during stubbing, preventing side effects.

---

## Question 9
How do you stub a method to throw an exception?

- A) when(mock.method()).thenThrow(new Exception())
- B) doThrow(new Exception()).when(mock).method()
- C) Both A and B work
- D) Neither works

**Answer: C**
**Explanation:** Both syntaxes work; doThrow is preferred for void methods.

---

## Question 10
What does verifyNoMoreInteractions() check?

- A) No methods were called
- B) No additional methods were called beyond verified ones
- C) Mock is clean
- D) No stubs exist

**Answer: B**
**Explanation:** verifyNoMoreInteractions ensures no unverified interactions occurred on the mock.
