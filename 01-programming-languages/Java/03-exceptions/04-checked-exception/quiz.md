# Quiz: Checked Exceptions

## Questions

### Q1: Which of the following is a checked exception?
**Answer:** `FileNotFoundException` (extends `IOException`, which is checked).

### Q2: What must a method do if it can throw a checked exception?
**Answer:** Either catch it or declare it in a `throws` clause.

### Q3: A method overrides a parent method that declares `throws IOException`. Which of the following is valid for the overriding method?
**Answer:** Declare `throws FileNotFoundException` or `throws RuntimeException`. Narrowing checked exceptions or dropping them is valid; declaring broader checked exceptions is not allowed.

### Q4: True or False: A method that declares `throws Exception` in its signature is considered good API design.
**Answer:** False — It hides the specific failures a caller should handle. Always be specific about which checked exceptions a method can throw.

### Q5: What is the main difference between a checked and unchecked exception?
**Answer:** Checked exceptions must be caught or declared; unchecked exceptions do not.

### Q6: When should you use a checked exception over an unchecked exception?
**Answer:** When the failure is caused by an external system and the caller can recover.

### Q7: Which pattern is recommended when a method catches a low-level checked exception (like `SQLException`) and needs to propagate it to a higher layer?
**Answer:** Wrap it in a domain-specific unchecked exception. Exception translation is the recommended pattern for layer boundaries.

### Q8: What happens if you try to catch a checked exception without declaring it in a `throws` clause?
**Answer:** The compiler will produce a compile-time error. Checked exceptions must be either caught or declared in the method signature.

### Q9: Why does Java enforce checked exceptions at compile time?
**Answer:** To ensure that developers handle expected failure conditions explicitly, making code more robust and forcing callers to deal with potential errors.

### Q10: What is the benefit of using custom checked exceptions?
**Answer:** They provide domain-specific error information, making it clearer to callers what can go wrong and how to recover, while still enforcing compile-time handling.