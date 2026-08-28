# Pattern Matching Quiz

## Questions

1. What is the syntax for a type pattern?
2. How do you add a condition to a pattern?
3. Can you match multiple patterns in one case?
4. What happens if no pattern matches?
5. How do you handle null in a pattern switch?
6. Can pattern variables be reassigned?
7. What is the scope of a pattern variable?
8. Can you use pattern matching with regular classes?
9. What is a guarded pattern?
10. Can you nest pattern matches?

## Answers

1. **`case Type variableName`** - e.g., `case String s`
2. **Using `&&`** - e.g., `case String s && s.length() > 5`
3. **Yes,** using comma separation: `case 1, 2, 3 ->`
4. **Compilation error** if not exhaustive. Use `default` or ensure all cases covered.
5. **`case null ->`** or handle in `default`
6. **No.** Pattern variables are effectively final in their scope.
7. **Scoped to the case block** where the pattern is matched.
8. **Yes.** Any type can be used in pattern matching.
9. **A pattern with a boolean condition** that must be true for the case to match.
10. **Yes,** in nested switch expressions or with complex types.
