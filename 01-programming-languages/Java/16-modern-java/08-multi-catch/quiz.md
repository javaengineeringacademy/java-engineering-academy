# Multi-catch Quiz

## Questions

1. What symbol separates exception types in a multi-catch?
2. Can you have different variable names for each exception type?
3. Is the exception variable in a multi-catch final?
4. Can you catch related exception types in a multi-catch?
5. Can multi-catch catch both checked and unchecked exceptions?
6. What happens if you try to reassign the exception variable?
7. Can you use multi-catch in a lambda?
8. Can you use multi-catch with finally?
9. What is the benefit of multi-catch over separate catches?
10. Can you have multiple multi-catch blocks?

## Answers

1. **Pipe `|`:** `catch (IOException | SQLException e)`
2. **No.** Only one variable name for all exception types.
3. **Effectively final.** You cannot reassign it.
4. **Technically yes, but not recommended.** Use the parent type instead.
5. **Yes.** You can mix checked and unchecked exceptions.
6. **Compilation error.** The variable is effectively final.
7. **Yes,** in Java 7+.
8. **Yes.** Multi-catch works with try-catch-finally.
9. **Less code duplication** when handling is identical.
10. **Yes.** You can have multiple catch blocks, each with multi-catch.
