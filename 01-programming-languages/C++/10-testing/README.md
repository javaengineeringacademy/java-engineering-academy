# Testing

## What it is
The practice of verifying that code works correctly.

## Why it exists
To catch bugs early, ensure reliability, and enable safe refactoring.

## When to use it
Always! Testing should be part of every development workflow.

## How it works

### Unit Testing with Google Test
```cpp
#include <gtest/gtest.h>

int add(int a, int b) {
    return a + b;
}

TEST(AddTest, PositiveNumbers) {
    EXPECT_EQ(add(2, 3), 5);
}

TEST(AddTest, NegativeNumbers) {
    EXPECT_EQ(add(-1, -1), -2);
}
```

### Test Fixtures
```cpp
class CalculatorTest : public ::testing::Test {
protected:
    Calculator calc;
    
    void SetUp() override {
        calc.clear();
    }
};

TEST_F(CalculatorTest, Addition) {
    calc.add(5);
    EXPECT_EQ(calc.getResult(), 5);
}
```

### Mocking
```cpp
class MockDatabase : public Database {
public:
    MOCK_METHOD(bool, connect, (const std::string&), (override));
    MOCK_METHOD(Data, query, (const std::string&), (override));
};
```

## Production Checklist
- [ ] Write tests for all public APIs
- [ ] Aim for high test coverage
- [ ] Use test fixtures for setup/teardown
- [ ] Mock external dependencies
- [ ] Run tests in CI/CD pipeline
- [ ] Write integration tests

## Maturity Levels
- **Beginner**: Basic unit tests
- **Intermediate**: Test fixtures, mocking
- **Advanced**: Property-based testing, fuzzing

## Common Myths
- ❌ "Testing slows down development"
- ❌ "100% coverage means no bugs"
- ❌ "Manual testing is sufficient"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Unit Test | Test individual functions |
| Integration Test | Test component interaction |
| Mock | Fake implementation |
| Coverage | Percentage of code tested |
| TDD | Test-Driven Development |

## Related Topics
- [Best Practices](../14-best-practices/)
- [Build Systems](../13-build-systems/)
- [Performance](../11-performance/)