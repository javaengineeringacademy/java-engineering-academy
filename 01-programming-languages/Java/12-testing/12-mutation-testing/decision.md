# Decision: Mutation Testing

## When to Use Mutation Testing

**Use mutation testing when:**
- Code coverage is high but bugs still slip through
- Test quality needs improvement
- Evaluating test effectiveness
- Identifying missing assertions

**Skip mutation testing when:**
- Tests are already proven effective
- Code changes are minimal
- Time constraints are severe

## Mutation Threshold Guidelines

| Score | Quality | Action |
|-------|---------|--------|
| 90-100% | Excellent | Maintain |
| 70-89% | Good | Minor improvements |
| 50-69% | Needs work | Add missing tests |
| <50% | Poor | Significant effort needed |

## Common Survived Mutations

1. **Boundary conditions**: Missing edge case tests
2. **Negate conditions**: Missing negative path tests
3. **Return values**: Missing assertion on return value
4. **Math operations**: Missing calculation tests
