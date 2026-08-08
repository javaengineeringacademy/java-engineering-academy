# Testing — C++

## Why It Matters

Testing is not a chore — it's an investment. Every hour spent writing tests saves multiple hours of debugging production incidents. When you deploy a release candidate on Friday evening and customers report broken login Monday morning, a single test case would have caught the one-line change in seconds instead of gambling with every deployment.

## What It Is

Testing in C++ involves writing automated tests to verify code behavior, using frameworks like Google Test and Catch2, with approaches including unit testing, integration testing, mocking, and test-driven development.

## Engineering Decision Framework

| Scenario | Testing Approach | Tool | Priority |
|----------|-----------------|------|----------|
| New function | Unit test with edge cases | Google Test / Catch2 | Critical |
| Bug fix | Regression test for the specific bug | Any framework | Critical |
| Refactor | Before/after comparison tests | Existing test suite | High |
| Integration | Test component interactions | Google Test + mock | High |
| Performance | Benchmark tests | Google Benchmark | Medium |
| Cross-platform | CI matrix testing | GitHub Actions / Jenkins | High |

## Expanded Code Examples

### Unit Testing with Google Test

```cpp
#include <gtest/gtest.h>

// Production code
int factorial(int n) {
    if (n < 0) throw std::invalid_argument("Negative not allowed");
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}

// Test cases — name: TestSuiteName_TestName
TEST(FactorialTest, ZeroReturnsOne) {
    EXPECT_EQ(factorial(0), 1);
}

TEST(FactorialTest, PositiveNumbers) {
    EXPECT_EQ(factorial(1), 1);
    EXPECT_EQ(factorial(5), 120);
    EXPECT_EQ(factorial(10), 3628800);
}

TEST(FactorialTest, NegativeThrows) {
    EXPECT_THROW(factorial(-1), std::invalid_argument);
}

// EXPECT_* macros continue on failure; ASSERT_* macros abort the test
TEST(FactorialTest, DetailedCheck) {
    ASSERT_EQ(factorial(5), 120);  // If this fails, rest of test is skipped
    EXPECT_GT(factorial(5), 100);  // This only runs if ASSERT passes
}
```

### Test Fixtures — Shared Setup/Teardown

```cpp
#include <gtest/gtest.h>
#include <vector>
#include <algorithm>

class SortingTest : public ::testing::Test {
protected:
    std::vector<int> data;

    void SetUp() override {
        data = {5, 3, 8, 1, 9, 2, 7, 4, 6};
    }

    void TearDown() override {
        // Cleanup if needed
    }
};

TEST_F(SortingTest, SortsInAscendingOrder) {
    std::sort(data.begin(), data.end());
    for (size_t i = 1; i < data.size(); ++i) {
        EXPECT_LE(data[i - 1], data[i]);
    }
}

TEST_F(SortingTest, EmptyVectorIsHandled) {
    data.clear();
    std::sort(data.begin(), data.end());
    EXPECT_TRUE(data.empty());
}

TEST_F(SortingTest, SingleElementIsSorted) {
    data = {42};
    std::sort(data.begin(), data.end());
    EXPECT_EQ(data.size(), 1);
    EXPECT_EQ(data[0], 42);
}
```

### Parameterized Tests

```cpp
#include <gtest/gtest.h>

// Test with multiple input/output pairs
class PrimeTest : public ::testing::TestWithParam<int> {};

TEST_P(PrimeTest, IsPrime) {
    int n = GetParam();
    ASSERT_GE(n, 2);
    for (int i = 2; i * i <= n; ++i) {
        EXPECT_NE(n % i, 0) << n << " is divisible by " << i;
    }
}

INSTANTIATE_TEST_SUITE_P(
    PrimeNumbers,
    PrimeTest,
    ::testing::Values(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
);
```

### Mocking with Google Mock

```cpp
#include <gtest/gtest.h>
#include <gmock/gmock.h>

// Interface
class Database {
public:
    virtual ~Database() = default;
    virtual bool connect(const std::string& url) = 0;
    virtual std::string query(const std::string& sql) = 0;
    virtual void disconnect() = 0;
};

// Mock
class MockDatabase : public Database {
public:
    MOCK_METHOD(bool, connect, (const std::string&), (override));
    MOCK_METHOD(std::string, query, (const std::string&), (override));
    MOCK_METHOD(void, disconnect, (), (override));
};

// Production code that depends on Database
class UserService {
    Database& db_;
public:
    explicit UserService(Database& db) : db_(db) {}

    bool initialize(const std::string& url) {
        return db_.connect(url);
    }

    std::string getUser(int id) {
        return db_.query("SELECT * FROM users WHERE id = " + std::to_string(id));
    }
};

// Tests
class UserServiceTest : public ::testing::Test {
protected:
    MockDatabase mock_db;
    UserService service{mock_db};
};

TEST_F(UserServiceTest, InitializeConnectsToDatabase) {
    EXPECT_CALL(mock_db, connect("postgres://localhost/mydb"))
        .WillOnce(testing::Return(true));

    EXPECT_TRUE(service.initialize("postgres://localhost/mydb"));
}

TEST_F(UserServiceTest, GetUserReturnsResult) {
    EXPECT_CALL(mock_db, connect(testing::_))
        .WillOnce(testing::Return(true));

    EXPECT_CALL(mock_db, query(testing::HasSubstr("SELECT")))
        .WillOnce(testing::Return("Alice"));

    std::string user = service.getUser(1);
    EXPECT_EQ(user, "Alice");
}

TEST_F(UserServiceTest, GetUserHandlesDbFailure) {
    EXPECT_CALL(mock_db, connect(testing::_))
        .WillOnce(testing::Return(true));

    EXPECT_CALL(mock_db, query(testing::_))
        .WillOnce(testing::Return(""));

    EXPECT_TRUE(service.getUser(1).empty());
}
```

### Testing Edge Cases — Detailed Examples

```cpp
#include <gtest/gtest.h>

// Production code: a safe integer parser
std::optional<int> safe_parse_int(const std::string& s) {
    if (s.empty()) return std::nullopt;
    try {
        size_t pos;
        int val = std::stoi(s, &pos);
        if (pos != s.size()) return std::nullopt;  // Trailing characters
        return val;
    } catch (...) {
        return std::nullopt;
    }
}

// Detailed edge case tests
class SafeParseIntTest : public ::testing::Test {};

TEST_F(SafeParseIntTest, ValidPositive) {
    auto result = safe_parse_int("42");
    ASSERT_TRUE(result.has_value());
    EXPECT_EQ(*result, 42);
}

TEST_F(SafeParseIntTest, ValidNegative) {
    auto result = safe_parse_int("-7");
    ASSERT_TRUE(result.has_value());
    EXPECT_EQ(*result, -7);
}

TEST_F(SafeParseIntTest, Zero) {
    auto result = safe_parse_int("0");
    ASSERT_TRUE(result.has_value());
    EXPECT_EQ(*result, 0);
}

TEST_F(SafeParseIntTest, EmptyString) {
    EXPECT_FALSE(safe_parse_int("").has_value());
}

TEST_F(SafeParseIntTest, NonNumeric) {
    EXPECT_FALSE(safe_parse_int("abc").has_value());
}

TEST_F(SafeParseIntTest, TrailingCharacters) {
    EXPECT_FALSE(safe_parse_int("42abc").has_value());
}

TEST_F(SafeParseIntTest, Overflow) {
    EXPECT_FALSE(safe_parse_int("99999999999999999999").has_value());
}

TEST_F(SafeParseIntTest, WhitespaceOnly) {
    EXPECT_FALSE(safe_parse_int("  ").has_value());
}
```

### Test Coverage and TDD Workflow

```cpp
// TDD Cycle: Red -> Green -> Refactor
//
// 1. RED: Write a failing test
// 2. GREEN: Write minimal code to pass
// 3. REFACTOR: Improve code while keeping tests green
//
// Example TDD for a Calculator:

// Step 1: Write test (RED)
// TEST(CalculatorTest, Add) { EXPECT_EQ(calc.add(1, 2), 3); }

// Step 2: Minimal implementation (GREEN)
// int add(int a, int b) { return a + b; }

// Step 3: Refactor — extract common patterns, add error handling
// Then write next test...

// Coverage metrics:
// - Line coverage: % of lines executed
// - Branch coverage: % of if/else branches taken
// - Function coverage: % of functions called
// Aim for 80%+ line coverage, 70%+ branch coverage for critical code
```

## Production Incidents

### Incident 1: Missing Test for Edge Case
**Problem**: A payment processing service started rejecting valid transactions for amounts exactly $1000.00.

**Cause**: The payment validator tested amounts like $100, $500, and $2000 but never tested exactly $1000.00 — a boundary condition. The comparison used `>` instead of `>=` for the maximum limit check.

**Impact**: $50K in legitimate transactions were rejected over 4 hours before detection. Customer complaints flooded in. Manual override was needed for each affected transaction.

**Detection**: Customer support reports triggered investigation. Log analysis showed all rejections were exactly $1000.00.

**Solution**: Fixed the comparison operator. Added boundary value tests: `EXPECT_TRUE(validator.isValid(999.99))`, `EXPECT_TRUE(validator.isValid(1000.00))`, `EXPECT_FALSE(validator.isValid(1000.01))`.

**Prevention**: Always test boundary values (min, min-1, min, min+1, max-1, max, max+1). Use parameterized tests for boundary conditions. Code review checklist must include "boundary values tested."

### Incident 2: Flaky Test Causing CI Distrust
**Problem**: A CI pipeline had 15% of builds failing with a `Connection refused` error in the integration test suite. Developers started ignoring CI failures, assuming they were "just flaky."

**Cause**: The integration tests connected to a real PostgreSQL instance. On CI, the database container sometimes took 10 seconds to start, but the tests only waited 3 seconds. Race condition between container startup and test execution.

**Impact**: 15% of CI runs failed. Developers stopped checking CI. A real regression slipped through when everyone assumed a failure was "flaky." The regression caused data corruption in production.

**Detection**: Root cause analysis after the production incident revealed the flaky test pattern.

**Solution**: Added a retry mechanism with exponential backoff for database connections. Increased timeout to 30 seconds. Added a health check that waits for the database to be ready before running tests. Marked previously-flaky tests with `[[reliable]]` attribute.

**Prevention**: Never use fixed timeouts for external services. Always use health checks + retry. Track flaky test rates and fix them — never ignore them. Add a "flaky test" dashboard to CI.

### Incident 3: Test Environment Contamination
**Problem**: Unit tests passed locally but failed in CI. The CI test runner executed tests in a different order than local, causing state leakage.

**Cause**: Tests shared a global configuration object. Test A set a value, and Test B (which ran after A in CI but before A locally) expected a different value. The tests weren't isolated.

**Impact**: 30% of CI runs failed non-deterministically. Developers spent hours reproducing "flaky" failures. Trust in CI eroded.

**Detection**: Running tests in CI's order locally reproduced the failure. `--gtest_repeat` with different permutations revealed the ordering dependency.

**Solution**: Each test now creates its own configuration instance in SetUp(). Removed all global mutable state from tests. Added a `TEST_P` permutation test to verify independence.

**Prevention**: Tests must be independent and order-agnostic. Never share mutable state between tests. Use SetUp()/TearDown() to reset state. Run tests in random order in CI to detect dependencies.

## Production Checklist

- [ ] Write tests for all public APIs
- [ ] Test boundary values and edge cases
- [ ] Use test fixtures for shared setup/teardown
- [ ] Mock external dependencies (databases, network, filesystem)
- [ ] Run tests in CI/CD pipeline on every commit
- [ ] Achieve 80%+ line coverage for critical paths
- [ ] Write regression tests for every bug fix
- [ ] Use parameterized tests for data-driven scenarios
- [ ] Keep tests fast (< 10 minutes for unit tests)
- [ ] Never ignore flaky tests — fix or delete them

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | Basic unit tests, manual test running |
| **Intermediate** | Test fixtures, mocking, CI integration, coverage tracking |
| **Advanced** | Property-based testing, fuzzing, mutation testing, test-driven design |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Testing slows down development" | Testing speeds up development by catching bugs early and enabling safe refactoring. The ROI is ~10x. |
| "100% coverage means no bugs" | Coverage measures execution, not correctness. You can have 100% coverage with wrong assertions. |
| "Manual testing is sufficient" | Manual testing doesn't scale, isn't reproducible, and misses regression bugs. |
| "We'll add tests later" | Later never comes. Code without tests accumulates bugs faster than code with tests. |
| "Tests are just for QA" | Developers write tests. Testing is a development practice, not a QA activity. |

## One-Minute Revision Table

| Concept | Description | When to Use |
|---------|-------------|-------------|
| Unit Test | Test individual functions in isolation | Always — for every function |
| Integration Test | Test component interactions | When components must work together |
| Mock | Fake implementation for dependencies | When dependencies are slow, external, or non-deterministic |
| Fixture | Shared setup/teardown for tests | When tests need common state or resources |
| TDD | Write test first, then implement | When design clarity matters |
| Parameterized Test | Run same test with different data | When testing multiple input/output pairs |
| Regression Test | Test that catches previously found bugs | After every bug fix |
| Coverage | Percentage of code executed by tests | To identify untested code paths |

## Cross-Linked Related Topics

- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Testability is a best practice
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — CMake integration with test targets
- **Performance** → [Module 11: Performance](../11-performance/) — Benchmark tests for performance regression
- **Design Patterns** → [Module 09: Design Patterns](../09-design-patterns/) — Patterns should be testable; Mock uses Adapter
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `constexpr` tests, `std::optional` in test assertions
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Thread-safety tests, race condition detection

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Flaky test failing non-deterministically | `--gtest_repeat` with random ordering | Run `--gtest_repeat=100 --gtest_shuffle` to detect ordering dependencies; isolate shared state |
| Test environment contamination between tests | `SetUp()`/`TearDown()` audit + global state review | Remove all global mutable state; each test creates its own configuration instance |
| Mock not matching expected calls | Google Mock error messages + `testing::_` wildcards | Use `testing::_` for matchers; check exact argument types and call counts |
| Boundary condition not tested (e.g., $1000.00) | Parameterized tests for boundary values | Use `INSTANTIATE_TEST_SUITE_P` with min-1, min, min+1, max-1, max, max+1 |
| Integration test connecting to real database slowly | Health check + retry with exponential backoff | Wait for database readiness before running tests; increase timeout to 30s |

## Code Review Checklist

- [ ] Tests written for all public APIs
- [ ] Boundary values and edge cases tested (min, max, min-1, min+1)
- [ ] External dependencies mocked (databases, network, filesystem)
- [ ] Tests are independent — no shared mutable state between tests
- [ ] Regression test exists for every bug fix
- [ ] Test names clearly describe the scenario being tested
- [ ] Flaky tests tracked and fixed — never ignored

## Architecture Considerations

Testing is the architectural safety net that enables confident refactoring and deployment. Unit tests verify individual components in isolation. Integration tests verify component interactions. Mocks replace external dependencies for deterministic testing. Test-driven development (TDD) shapes architecture toward testability — small, focused, loosely coupled components. A strong test suite enables continuous deployment with confidence.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Test fixture for shared setup | Multiple tests with common state | Reduces duplication vs. hidden test dependencies |
| Parameterized tests for boundary conditions | Data-driven testing of input/output pairs | Concise test definitions vs. harder to debug individual failures |
| Mock objects for external dependencies | Testing without real databases/network | Deterministic tests vs. mocks may not perfectly simulate real behavior |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Tests connecting to production database | Data corruption, security breach | Use separate test databases; mock external services in unit tests |
| Flaky tests hiding real regressions | Security vulnerabilities slipping through | Fix or delete flaky tests; never ignore CI failures |
| Missing tests for security-critical code paths | Exploitable vulnerabilities undetected | Write tests for authentication, authorization, input validation, and boundary conditions |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `constexpr` functions enable compile-time test assertions | Use `static_assert` for compile-time validation of invariants |
| C++17 | `std::optional` simplifies test assertions for nullable returns | Replace `bool + output` with `std::optional` in test expectations |
| C++20 | Concepts enable clearer test fixture type requirements | Constrain test fixture template parameters with `requires` clauses |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `static_assert` for compile-time tests | C++11 | Widely supported |
| `std::optional` for nullable test assertions | C++17 | Widely supported |
| `constexpr` functions in test code | C++11 | Widely supported |
| `std::ranges` for test data pipelines | C++20 | Supported in GCC 10+, Clang 12+, MSVC 19.22+ |

## Interview Questions

1. **What is the difference between unit, integration, and system tests?**: Unit tests verify individual functions/classes in isolation. Integration tests verify component interactions (e.g., service + database). System tests verify the entire application end-to-end. Unit tests are fast and numerous; integration tests are slower and fewer.
2. **How do you write testable code?**: Follow SOLID principles — small single-responsibility functions, dependency injection for external resources, interfaces for mocking, and avoiding global state. Code that's easy to test is usually well-designed.
3. **What is the purpose of mocking?**: Mocks replace external dependencies (databases, APIs, file systems) with controlled fakes. They enable deterministic, fast tests that don't depend on external services. Mocks verify interactions (was this method called with these arguments?).
4. **How do you handle flaky tests?**: Never ignore them. Track flaky test rates. Fix root causes (timing issues, shared state, external dependencies). Use retries with exponential backoff for network-dependent tests. Delete tests that can't be made reliable.
5. **What is TDD and what are its benefits?**: Test-Driven Development: write a failing test, write minimal code to pass, refactor. Benefits: tests drive design toward testability, immediate feedback, safe refactoring, living documentation of expected behavior.

## References

- [Google Test Primer](https://google.github.io/googletest/primer.html)
- [Catch2 Documentation](https://github.com/catchorg/Catch2)
- [C++ Core Guidelines — Testing](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-testing)
- [Effective Unit Testing — Lasse Koskela](https://www.amazon.com/Effective-Unit-Testing-Koskela/dp=1937785480)
