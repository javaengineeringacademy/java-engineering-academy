# Decision: Testing Fundamentals

## When to Use

**Write unit tests when:**
- Business logic needs verification
- Edge cases and error paths must be covered
- Refactoring is planned (tests provide safety net)
- TDD workflow is being followed

**Write integration tests when:**
- Multiple components must work together
- Database queries need validation
- API contracts must be verified
- Configuration correctness matters

**Write E2E tests when:**
- Critical user journeys must be validated
- Acceptance criteria need proof
- Regression prevention is paramount

## Testing Strategy by Layer

| Layer | Test Type | Coverage Target |
|-------|-----------|-----------------|
| Domain Logic | Unit | 80-90% |
| Service Layer | Unit + Integration | 70-80% |
| Repository | Integration | 60-70% |
| Controller | Integration | 50-60% |
| End-to-End | E2E | Critical paths only |

## Test Pyramid Allocation

```
        /\
       /  \    E2E (10%)
      /    \   Few, slow, expensive
     /------\
    /        \ Integration (20%)
   /          \ Moderate, test interactions
  /------------\
 /              \ Unit (70%)
/                \ Many, fast, cheap
```

## Anti-Patterns to Avoid

- 100% coverage obsession (diminishing returns)
- Testing implementation details instead of behavior
- Flaky tests (time-dependent, order-dependent)
- Skipping tests to "save time"
- Writing tests after bugfix instead of before
