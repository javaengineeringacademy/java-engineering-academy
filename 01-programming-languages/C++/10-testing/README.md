# Testing — C++

## Overview

Testing in C++ is the practice of writing automated programs that verify the correctness, performance, and reliability of production code. The C++ testing ecosystem is mature and production-grade, anchored by Google Test (gtest) and Google Mock (gmock) — the same frameworks used internally at Google, NVIDIA, AMD, and thousands of other companies. C++ testing encompasses unit testing, integration testing, mocking, parameterized data-driven tests, and performance benchmarking. Unlike interpreted languages, C++ testing must account for manual memory management, undefined behavior, template instantiation errors, and platform-specific differences, making a robust test suite not just beneficial but essential.

## Learning Objectives

- Understand why automated testing is critical for C++ projects
- Write unit tests, test fixtures, and parameterized tests using Google Test
- Create mock objects with Google Mock to isolate units under test
- Apply boundary value analysis and edge-case testing techniques
- Design testable code using dependency injection and SOLID principles
- Set up and integrate Google Test into CMake-based build systems
- Identify and eliminate flaky, non-deterministic, and order-dependent tests
- Measure and improve test coverage for production-critical code paths

## Prerequisites

| Prerequisite | Why It's Needed |
|--------------|-----------------|
| [01-fundamentals](../01-fundamentals/) | Understanding of C++ syntax, types, functions, classes, and the compilation model |
| [03-templates](../03-templates/) | Templates are used extensively in Google Test's macro system and in writing generic test utilities |

## History

The history of C++ unit testing mirrors the broader evolution of automated testing. In the early days, developers wrote ad-hoc `main()` functions with `assert()` calls — fragile, non-standard, and hard to maintain. The xUnit paradigm, originating with SUnit (Smalltalk, 1994) and JUnit (Java, 1997), introduced structured test frameworks with assertions, fixtures, and test discovery. C++ followed: CppUnit (1999) brought xUnit to C++, followed by Boost.Test (2001), then Google Test (2005), originally developed internally at Google for testing their massive C++ codebase. Google Mock emerged in 2008 to address C++ mocking needs. By 2010, Google Test became the de facto standard for C++ testing in industry, adopted by Android's native code, Chromium, LLVM, and most major C++ open-source projects. Today, Google Test remains actively maintained and is the framework of choice for production C++ systems worldwide.

## Production Notes

C++ testing has unique constraints compared to other languages. Google Test must be compiled as a static library and linked into test binaries — it is not header-only. Tests execute as native machine code, making them fast but also meaning memory errors in test code can crash the test runner itself. Undefined behavior in test assertions (e.g., signed integer overflow) is undefined — literally. C++ templates can produce cryptic compiler errors in test macros. Test fixtures rely on class inheritance, which couples test structure to the framework's object model. For large codebases, test compilation time becomes a real concern — incremental compilation and parallel test execution are essential. CI pipelines should compile tests with the same compiler flags, sanitizers (AddressSanitizer, UndefinedBehaviorSanitizer), and optimization levels as production to catch real bugs.

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

## Core Concepts

### Test Suites and Test Cases

A **test suite** groups related tests. A **test case** (or test) is a single verification of behavior. In Google Test, `TEST(SuiteName, TestName)` defines both. `TEST_F` uses a fixture class, allowing shared setup across tests in the same suite.

### Assertions

Google Test provides two classes of assertions:

- `EXPECT_*` macros — record failures but continue executing the test. Use these for non-fatal checks.
- `ASSERT_*` macros — abort the test immediately on failure. Use these when subsequent checks depend on a prior condition.

| Assertion | Purpose |
|-----------|---------|
| `EXPECT_EQ(a, b)` | Equality check |
| `EXPECT_NE(a, b)` | Inequality check |
| `EXPECT_LT(a, b)` | Less than |
| `EXPECT_GT(a, b)` | Greater than |
| `EXPECT_TRUE(expr)` | Boolean true |
| `EXPECT_FALSE(expr)` | Boolean false |
| `EXPECT_THROW(stmt, type)` | Exception expected |
| `EXPECT_NO_THROW(stmt)` | No exception expected |
| `EXPECT_NEAR(a, b, tol)` | Floating-point tolerance |

### Test Fixtures

A fixture is a class derived from `::testing::Test` that provides `SetUp()` and `TearDown()` methods. Each `TEST_F` test gets a fresh fixture instance, ensuring test isolation. Fixtures are essential when tests share expensive setup logic (database connections, file I/O, large data structures).

### Parameterized Tests

`TEST_P` and `INSTANTIATE_TEST_SUITE_P` allow running the same test logic with different input values. This is critical for boundary-value testing, data-driven validation, and testing across multiple configurations.

### Mocking

Google Mock creates fake implementations of interfaces. `MOCK_METHOD` declares mock methods. `EXPECT_CALL` sets expectations: which methods should be called, with what arguments, how many times, and in what order. Mocks decouple tests from external systems (databases, networks, filesystems) and enable deterministic, fast test execution.

### Matchers

Google Mock provides a rich matcher library for flexible argument matching: `testing::_` (any), `testing::Eq(value)`, `testing::HasSubstr(s)`, `testing::Contains(elem)`, `testing::SizeIs(n)`, and many more. Matchers make mock expectations expressive and self-documenting.

## Internal Working

### How Google Test Works

Google Test compiles each `TEST()` and `TEST_F()` macro into a unique function that is registered with a global test registry at static initialization time. When the test binary runs, `main()` (provided by `gtest_main`) iterates the registry and executes matching tests. The `--gtest_filter` command-line flag filters tests by name using glob patterns.

### How Mocking Works

Google Mock uses C++ virtual dispatch. When you declare `MOCK_METHOD` in a mock class, Google Mock generates a virtual method that records calls and replays configured responses. At runtime, `EXPECT_CALL(mock, Method(...))` installs a matcher and action pair. When the production code calls `mock.Method(args)`, Google Mock checks the active expectations in reverse declaration order (last-defined wins), matches arguments against matchers, and either returns the configured action or fails the test. The mock object's destructor verifies that all expectations were satisfied.

### Test Discovery and Execution

Google Test uses static initialization to register tests before `main()` runs. Each `TEST()` macro creates a `::testing::TestInfo` object and adds it to `::testing::UnitTest::GetInstance()`. The test runner filters by `--gtest_filter`, supports `--gtest_repeat` for stress testing, and `--gtest_shuffle` to detect ordering dependencies. Exit code 0 means all tests passed; 1 means at least one failure.

### Build Integration with CMake

Google Test is typically integrated via CMake's `FetchContent` or `add_subdirectory`. A minimal CMake setup:

```cmake
include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/v1.14.0.tar.gz
)
FetchContent_MakeAvailable(googletest)

enable_testing()
add_executable(tests test_file.cpp)
target_link_libraries(tests GTest::gtest_main)
include(GoogleTest)
gtest_discover_tests(tests)
```

`gtest_discover_tests` automatically discovers all test cases and registers them with CTest, enabling `ctest` to run and report results.

## Syntax

### Basic Test

```cpp
#include <gtest/gtest.h>

TEST(TestSuiteName, TestName) {
    EXPECT_EQ(1 + 1, 2);
}
```

### Test with Fixture

```cpp
#include <gtest/gtest.h>

class MyFixture : public ::testing::Test {
protected:
    void SetUp() override { /* per-test setup */ }
    void TearDown() override { /* per-test teardown */ }
    int shared_value = 0;
};

TEST_F(MyFixture, TestName) {
    shared_value = 42;
    EXPECT_EQ(shared_value, 42);
}
```

### Parameterized Test

```cpp
#include <gtest/gtest.h>

class ParamTest : public ::testing::TestWithParam<int> {};

TEST_P(ParamTest, Validates) {
    int val = GetParam();
    EXPECT_GE(val, 0);
}

INSTANTIATE_TEST_SUITE_P(
    NonNegative,
    ParamTest,
    ::testing::Values(0, 1, 5, 100)
);
```

### Mock Declaration

```cpp
#include <gmock/gmock.h>

class Interface {
public:
    virtual ~Interface() = default;
    virtual int compute(int x) = 0;
    virtual void reset() = 0;
};

class MockInterface : public Interface {
public:
    MOCK_METHOD(int, compute, (int), (override));
    MOCK_METHOD(void, reset, (), (override));
};
```

### Expectation Setting

```cpp
MockInterface mock;

// Must be called once with argument 5, returns 10
EXPECT_CALL(mock, compute(5))
    .Times(1)
    .WillOnce(testing::Return(10));

// Called any number of times with any argument
EXPECT_CALL(mock, reset())
    .Times(testing::AnyNumber());
```

## Expanded Code Examples

### Easy — Unit Testing with Google Test

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

### Medium — Test Fixtures: Shared Setup/Teardown

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

### Medium — Parameterized Tests

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

### Hard — Mocking with Google Mock

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

### Hard — Testing Edge Cases

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

### Enterprise — Test Coverage and TDD Workflow

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

## Performance Considerations

| Factor | Impact | Mitigation |
|--------|--------|------------|
| Test compilation time | Large projects with thousands of test files compile slowly | Use incremental builds, precompiled headers, and `ccache`; compile tests in parallel |
| Test execution time | Slow tests discourage frequent running | Isolate unit tests from I/O; mock external dependencies; keep unit tests under 100ms each |
| Memory overhead | Each test binary links Google Test statically (~500KB+) | Share a single `gtest_main` across all test targets; avoid unnecessary includes |
| Sanitizer cost | ASan/UBSan slow execution by 2-5x | Run sanitizers in CI, not locally; use them selectively on critical paths |
| Test fixture setup | Expensive `SetUp()` repeated across tests | Use `SetUpTestSuite()` (static) for one-time setup shared across tests in a suite |
| Parameterized test combinatorics | Many parameter combinations explode test count | Limit combinations to meaningful cases; use `::testing::Combine()` sparingly |
| Mock object overhead | Virtual dispatch adds indirection per call | Acceptable for unit tests; avoid in performance-critical test paths |
| Parallel test execution | `--gtest_parallel` or CTest `-j` flag | Speeds up CI; ensure tests are truly independent before parallelizing |

## Best Practices

- **Test behavior, not implementation** — Tests should verify what code does, not how it does it. Refactoring internals shouldn't break tests.
- **One assertion of concept per test** — Each test should verify one logical behavior. Multiple assertions are fine if they validate a single concept.
- **Use descriptive test names** — `TEST_F(PaymentTest, RejectsNegativeAmount)` is better than `TEST_F(PaymentTest, Test1)`.
- **Test boundary values** — Always test min-1, min, min+1, max-1, max, max+1 for numeric ranges.
- **Mock at boundaries, not internals** — Mock external dependencies (databases, APIs, filesystems), not internal helper functions.
- **Keep tests independent** — No test should depend on another test's execution or shared mutable state.
- **Run tests early and often** — Run unit tests locally before committing; run the full suite in CI on every push.
- **Use `ASSERT_*` for preconditions** — If a check is a prerequisite for the rest of the test, use `ASSERT_*` so the test aborts cleanly on failure.
- **Test error paths** — Happy-path tests are insufficient. Test exceptions, error codes, edge cases, and boundary conditions.
- **Delete dead tests** — If production code is removed, delete its tests. Dead tests create confusion and slow down the suite.

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Testing implementation details | Tests break on internal refactors even when behavior is correct | Test public interfaces and observable behavior only |
| Shared mutable state between tests | Non-deterministic failures depending on test execution order | Each test gets its own fixture instance; never use global mutable state in tests |
| Using `ASSERT_*` where `EXPECT_*` suffices | Test aborts on first failure, hiding additional failures | Use `ASSERT_*` only for preconditions; prefer `EXPECT_*` for most checks |
| Mocking everything | Brittle tests tightly coupled to implementation; mocks diverge from real behavior | Mock only external dependencies; use real objects for internal collaborators |
| Ignoring compiler warnings in test code | Tests compile with warnings that mask real issues | Treat test code with the same strictness as production code (`-Wall -Wextra -Werror`) |
| Writing tests after shipping | Bugs discovered in production are harder to reproduce and fix | Write tests as part of development, before or alongside the implementation |
| Hardcoded timeouts in tests | Flaky failures on slow CI machines or under load | Use `testing::UnitTest::Timeout` or polling with backoff instead of fixed sleeps |
| Copy-pasting test code | Duplicated setup logic hides inconsistencies | Extract shared logic into fixtures or helper functions |

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

### Incident 4: Missing Mock Led to Production Outage
**Problem**: A service that sent SMS notifications passed all tests but crashed in production when the third-party SMS provider returned an unexpected HTTP 503 response.

**Cause**: The SMS client was not mocked in tests — tests used a stub that always returned success. The production code had no handling for non-200 HTTP responses. The `std::stoi()` call on the response body threw `std::invalid_argument` when it received an HTML error page instead of a numeric status code.

**Impact**: SMS notifications stopped for 6 hours. 12,000 customers missed critical two-factor authentication codes. Support tickets spiked. Manual failover to a backup provider was required.

**Detection**: Monitoring dashboards showed a 100% drop in SMS sends. Log aggregation revealed uncaught `std::invalid_argument` exceptions at the `std::stoi()` line.

**Solution**: Added Google Mock for the HTTP client interface. Wrote tests for all HTTP error codes (4xx, 5xx, timeout, malformed response). Added `try/catch` around response parsing with a fallback error path. Deployed the fix with a canary release.

**Prevention**: Mock all external service interfaces. Test error paths for every external call. Include HTTP status codes, timeouts, malformed responses, and partial responses in test scenarios. Code review must verify that external dependencies are mocked in unit tests.

### Incident 5: Undefined Behavior in Test Code
**Problem**: A test suite passed on GCC but segfaulted on Clang. The CI matrix showed green for GCC builds while Clang builds were silently skipped due to a configuration error.

**Cause**: A test used `reinterpret_cast` to convert between unrelated types, which is undefined behavior in C++. GCC happened to produce the "correct" result on the target platform, but Clang's optimizer exploited the UB and generated a crash. The CI configuration had a typo that skipped Clang builds.

**Impact**: The code was released to production where a small percentage of users (Clang-compiled binaries on certain architectures) experienced crashes. Customer crash reports went unnoticed for two weeks because the crash rate was below the alerting threshold.

**Detection**: A customer reported a reproducible crash on an ARM device. Crash analysis showed a `SIGSEGV` at the `reinterpret_cast` location.

**Solution**: Replaced `reinterpret_cast` with `static_cast` through a proper type conversion. Added UndefinedBehaviorSanitizer (UBSan) to the CI pipeline. Fixed the CI configuration typo that was skipping Clang builds. Enabled ASan and UBSan for all CI matrix entries.

**Prevention**: Run tests with sanitizers (ASan, UBSan, TSan) in CI. Test on all target platforms and compilers in the CI matrix. Never skip CI matrix entries silently. Treat undefined behavior in test code as seriously as in production code.

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
6. **Explain the difference between `EXPECT_*` and `ASSERT_*` macros**: `EXPECT_*` records a failure but continues executing the test, allowing multiple failures to be reported in one run. `ASSERT_*` aborts the test immediately on failure. Use `ASSERT_*` when subsequent code depends on a prior check (e.g., dereferencing a pointer only after asserting it's non-null).
7. **How do test fixtures achieve test isolation?**: Each `TEST_F` test receives a fresh instance of the fixture class. `SetUp()` runs before each test, and `TearDown()` runs after. This means tests never share state — even if they use the same fixture, they operate on independent objects. This prevents order-dependent failures.
8. **What are Google Mock matchers and why are they useful?**: Matchers are flexible argument comparators used in `EXPECT_CALL`. `testing::_` matches any value, `testing::HasSubstr("error")` checks substring membership, `testing::Lt(10)` checks less-than. They make expectations expressive, reduce boilerplate, and produce clear failure messages like "Expected: has substring 'error', actual: 'success'".
9. **How do you test code that depends on the filesystem?**: Extract filesystem operations behind an interface (e.g., `IFileSystem`). In production, use a `RealFileSystem` implementation. In tests, use a `MockFileSystem` or `FakeFileSystem` that operates on an in-memory directory tree. Google Mock's `EXPECT_CALL` lets you verify file operations without touching the real filesystem.
10. **What is the role of `gtest_discover_tests` in CMake?**: `gtest_discover_tests` is a CMake macro that runs the test binary at build time to discover all registered tests, then registers them with CTest. This enables `ctest` to report per-test results, supports `--gtest_filter` via CTest, and allows CI systems to show individual test pass/fail status.
11. **How would you test a multithreaded producer-consumer queue?**: Launch multiple producer threads that enqueue items and multiple consumer threads that dequeue. Use barriers to synchronize thread start times. After all threads finish, verify that every item was dequeued exactly once, the queue is empty, and no data races occurred (run with ThreadSanitizer). Use `std::atomic` counters for thread-safe metrics.
12. **What is mutation testing and how does it differ from code coverage?**: Mutation testing introduces small faults (mutations) into production code — changing `>` to `>=`, deleting a line, inverting a condition — and checks whether the test suite catches them. A test suite with high mutation score (mutations that cause test failure) is more effective than one with high line coverage alone, because coverage measures execution while mutation testing measures detection capability.
13. **How do you test template-heavy code?**: Instantiate templates with specific types in test code. For example, `SortedList<int>` and `SortedList<std::string>`. Test edge cases specific to each type (e.g., empty containers, single elements, large collections). Use `static_assert` in template code to validate invariants at compile time. Parameterized tests can exercise the same template with multiple type instantiations.
14. **What is the Google Test `PrettyUnitTestResultPrinter` and when would you customize it?**: It's the default test output formatter that shows PASS/FAIL for each test with timing. You can customize it by subclassing `::testing::EmptyTestEventListener` and registering it with `UnitTest::GetInstance()->listeners().Append()`. Custom printers are useful for CI integration (JUnit XML output), specialized failure formatting, or adding metadata like test owner or severity.
15. **How do you achieve 80%+ test coverage without writing meaningless tests?**: Focus coverage effort on critical paths (authentication, payment, data processing) rather than chasing a global number. Use coverage tools (gcov, llvm-cov) to identify untested branches, not as a goal in itself. Meaningless tests (e.g., `EXPECT_TRUE(true)`) inflate coverage without adding value. Prioritize tests that verify business logic, boundary conditions, and error handling over boilerplate and trivial getters/setters.

## References

- [Google Test Primer](https://google.github.io/googletest/primer.html)
- [Catch2 Documentation](https://github.com/catchorg/Catch2)
- [C++ Core Guidelines — Testing](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-testing)
- [Effective Unit Testing — Lasse Koskela](https://www.amazon.com/Effective-Unit-Testing-Koskela/dp=1937785480)
