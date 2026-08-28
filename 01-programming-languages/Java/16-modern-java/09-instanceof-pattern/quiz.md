# instanceof Pattern Matching Quiz

## Questions

1. What is a pattern variable?
2. Can you use `||` with pattern variables?
3. What is the scope of a pattern variable?
4. Can pattern variables be reassigned?
5. What happens if you use a pattern variable outside its scope?
6. Can you use pattern matching with primitive types?
7. Can you nest pattern matches?
8. What is a guarded pattern?
9. Can you use pattern matching with interfaces?
10. What is the difference between `instanceof` and pattern matching?

## Answers

1. **A variable introduced by a pattern** that can be used in the scope where it's definitely assigned.
2. **No.** Only `&&` is allowed with patterns. `||` would make the variable scope ambiguous.
3. **Follows definite assignment rules** - The variable is in scope where it's definitely assigned.
4. **No.** Pattern variables are effectively final.
5. **Compilation error.** You cannot access a variable outside its scope.
6. **No.** Pattern matching only works with reference types.
7. **Yes,** in complex conditions.
8. **A pattern with a boolean condition** that must be true for the case to match.
9. **Yes.** Any reference type can be used.
10. **Pattern matching combines instanceof and cast** into a single expression.
