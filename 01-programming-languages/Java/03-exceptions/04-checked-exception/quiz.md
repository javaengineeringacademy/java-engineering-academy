# Quiz: Checked Exceptions

## Question 1
Which of the following is a checked exception?

- A) `NullPointerException`
- B) `FileNotFoundException`
- C) `ArrayIndexOutOfBoundsException`
- D) `ClassCastException`

**Answer: B**

## Question 2
What must a method do if it can throw a checked exception?

- A) Nothing — the JVM handles it automatically.
- B) Either catch it or declare it in a `throws` clause.
- C) Only log it in the console.
- D) Only wrap it in a RuntimeException.

**Answer: B**

## Question 3
A method overrides a parent method that declares `throws IOException`. Which of the
following is valid for the overriding method?

- A) Declare `throws Exception`
- B) Declare `throws FileNotFoundException`
- C) Declare `throws RuntimeException`
- D) Declare `throws ArithmeticException`

**Answer: B and C** — Narrowing (FileNotFoundException) or dropping checked exceptions
are both valid. Declaring broader checked exceptions (Exception) is not allowed. Declaring
unchecked exceptions is always allowed.

## Question 4
True or False: A method that declares `throws Exception` in its signature is considered
good API design.

**Answer: False** — It hides the specific failures a caller should handle. Always be
specific about which checked exceptions a method can throw.

## Question 5
What is the main difference between a checked and unchecked exception?

- A) Checked exceptions are faster to create.
- B) Checked exceptions must be caught or declared; unchecked exceptions do not.
- C) Checked exceptions can only be thrown in main methods.
- D) Unchecked exceptions extend `Exception`.

**Answer: B**

## Question 6
When should you use a checked exception over an unchecked exception?

- A) When the failure is a programming error.
- B) When the failure is caused by an external system and the caller can recover.
- C) When you want the exception to be silently ignored.
- D) When you want to avoid using try-catch blocks.

**Answer: B**

## Question 7
Which pattern is recommended when a method catches a low-level checked exception
(like `SQLException`) and needs to propagate it to a higher layer?

- A) Declare `throws SQLException` all the way up.
- B) Wrap it in a domain-specific unchecked exception.
- C) Catch it and return null.
- D) Catch it and rethrow it as a checked exception with a different name.

**Answer: B** — Exception translation is the recommended pattern for layer boundaries.
