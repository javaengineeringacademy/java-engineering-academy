# Record Patterns Quiz

## Questions

1. What is a record pattern?
2. Can you deconstruct nested records?
3. What is the scope of pattern variables from record patterns?
4. Can record patterns be used with sealed types?
5. Can you use guards with record patterns?
6. What happens if a record component is null?
7. Can record patterns be used in switch expressions?
8. What is the difference between record patterns and accessor methods?
9. Can you partially deconstruct a record?
10. Can record patterns be used with interfaces?

## Answers

1. **A pattern that deconstructs a record** into its components.
2. **Yes.** You can deconstruct nested records.
3. **Follows definite assignment rules** - Same as other pattern variables.
4. **Yes.** Record patterns work well with sealed types for exhaustive matching.
5. **Yes.** You can add `&&` conditions.
6. **The component is null** - You can check for null in the pattern.
7. **Yes.** Record patterns can be used in switch expressions.
8. **Record patterns destructure in one step** - Accessors require explicit calls.
9. **Yes.** You can use `var` for components you don't need.
10. **No.** Record patterns only work with records.
