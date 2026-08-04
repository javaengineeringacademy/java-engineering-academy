# Test Anti-Patterns

## 1. Test Interdependence
**BAD**: Tests depend on execution order via shared state.
**GOOD**: Each test is self-contained with its own setup.

## 2. Excessive Mocking
**BAD**: Mocking 5 dependencies for a simple operation.
**GOOD**: Use real objects where possible, mock only boundaries.

## 3. Testing Implementation Details
**BAD**: `verify(repo).save(any())` - tests implementation.
**GOOD**: `assertEquals(1, repo.findAll().size())` - tests behavior.

## 4. Brittle Tests
**BAD**: `assertTrue(html.contains("<div class=\"product\">"))`.
**GOOD**: Test business outcomes, not HTML structure.

## 5. Slow Tests
**BAD**: `Thread.sleep(1000)` or real HTTP calls.
**GOOD**: Use mocks/fakes for external dependencies.

## 6. God Test
**BAD**: One test checking 15 behaviors.
**GOOD**: Separate tests per behavior.

## 7. Missing Edge Cases
**BAD**: Only testing happy path.
**GOOD**: Test null, empty, boundary, and error cases.

## 8. No Assertions
**BAD**: Tests that catch exceptions but never assert.
**GOOD**: Every test has explicit assertions.

## Best Practices
1. Each test verifies one behavior
2. Tests are independent
3. Test behavior, not implementation
4. Test edge cases and error paths
5. Give tests descriptive names
