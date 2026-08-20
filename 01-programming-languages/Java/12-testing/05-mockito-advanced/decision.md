# Decision: Advanced Mockito

## When to Use Spies

**Use @Spy when:**
- Testing partial mocking scenarios
- Real object behavior is needed for most methods
- Specific methods need stubbing while others remain real

**Use @Mock when:**
- Complete isolation is required
- No real behavior should execute
- Default return values are sufficient

## BDD vs Traditional Style

| Aspect | BDD (given/when/then) | Traditional (when/verify) |
|--------|----------------------|--------------------------|
| Readability | More readable | Less expressive |
| Focus | Behavior | Implementation |
| Naming | Given-When-Then | Arrange-Act-Assert |
| Verification | then().should() | verify() |

## Void Method Strategies

| Strategy | Use Case |
|----------|----------|
| doNothing() | Side-effect-free void methods |
| doAnswer() | Capture arguments, modify state |
| doThrow() | Verify exception handling |
| doCallRealMethod() | Spy partial mocking |

## Custom Answer Guidelines

- Use when return value depends on input arguments
- Keep answer logic simple and focused
- Avoid heavy computation in answers
- Use invocation.getArgument() for parameter access
