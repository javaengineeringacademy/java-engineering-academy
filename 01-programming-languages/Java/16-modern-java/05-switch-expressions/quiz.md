# Switch Expressions Quiz

## Questions

1. What is the difference between `->` and `:` in switch?
2. Do switch expressions require a `default` case?
3. What is the `yield` keyword used for?
4. Can switch expressions have side effects?
5. Can you combine multiple values in one case?
6. What happens if a case doesn't return a value?
7. Can switch expressions be used with strings?
8. What is the type of a switch expression?
9. Can switch expressions throw exceptions?
10. Can you nest switch expressions?

## Answers

1. **`->` is arrow syntax** (no fall-through, no break). **`:` is colon syntax** (with fall-through, requires break/yield).
2. **Yes,** unless all possible values are covered (e.g., enum with all values).
3. **Returns a value** from a colon-syntax case block.
4. **Yes,** but arrow cases are limited to single expressions or throw.
5. **Yes,** using comma separation: `case 1, 2, 3 ->`
6. **Compilation error.** All cases must return a value or throw.
7. **Yes.** String is a valid switch type.
8. **The common type** of all case values.
9. **Yes.** Cases can throw exceptions.
10. **Yes,** as long as they're used as expressions.
