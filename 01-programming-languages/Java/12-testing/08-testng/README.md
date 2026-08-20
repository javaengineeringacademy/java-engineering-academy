# 11.8 TestNG Framework

## 1. Introduction

TestNG is a testing framework inspired by JUnit and NUnit but designed to cover a wider range of testing categories: unit, functional, integration, and end-to-end testing. It provides powerful features like parallel execution, data-driven testing, and flexible test configuration.

## 2. Learning Objectives

- Understand TestNG architecture and annotations
- Configure test suites with XML
- Implement data-driven testing with @DataProvider
- Execute tests in parallel
- Use TestNG listeners for custom reporting
- Compare TestNG with JUnit 5

## 3. Prerequisites

- Basic Java knowledge
- Understanding of testing concepts
- Familiarity with annotations

## 4. Why This Concept Exists

TestNG addresses limitations in JUnit:
- Native parallel execution support
- XML-based suite configuration
- Built-in data providers
- Flexible grouping and dependencies
- Advanced reporting capabilities

## 5. Problem Statement

How do we configure and execute complex test suites with dependencies, parallel execution, and data-driven scenarios?

## 6. Theory

### TestNG Annotations

| Annotation | Description |
|------------|-------------|
| @Test | Marks a test method |
| @BeforeSuite | Runs before suite |
| @BeforeTest | Runs before test tag |
| @BeforeClass | Runs before class |
| @BeforeMethod | Runs before each method |
| @AfterMethod | Runs after each method |
| @AfterClass | Runs after class |
| @AfterTest | Runs after test tag |
| @AfterSuite | Runs after suite |
| @DataProvider | Provides test data |
| @Parameters | Reads XML parameters |
| @Factory | Creates dynamic test instances |

### TestNG vs JUnit 5

| Feature | TestNG | JUnit 5 |
|---------|--------|---------|
| Parallel execution | Native XML config | Extension-based |
| Data providers | @DataProvider | @ParameterizedTest |
| Dependencies | @Test(dependsOnMethods) | Not supported |
| Groups | @Test(groups) | @Tag |
| Configuration | XML-based | Annotation-based |

## 7. Internal Working

### Test Execution Flow

1. Parse XML suite configuration
2. Discover test classes and methods
3. Build dependency graph
4. Execute tests respecting dependencies
5. Run data-provider iterations
6. Generate reports (HTML/XML)

### Parallel Execution

```
TestNG Parallel Model:
├── Suite level (tests attribute)
├── Test level (tests attribute)
├── Class level (threadPoolSize)
└── Method level (timeOut)
```

## 8. JVM Perspective

- Tests run in the same JVM
- Parallel threads share JVM heap
- Thread pool managed by TestNG
- Static fields are shared across threads
- Memory overhead for thread management

## 9. Memory Representation

```
TestNG Memory Model:
┌─────────────────────────────────────┐
│           Method Area               │
│  - Test class bytecode              │
│  - TestNG framework classes         │
├─────────────────────────────────────┤
│           Heap Memory               │
│  - Test instances                   │
│  - Data provider results            │
│  - Shared test state                │
├─────────────────────────────────────┤
│      Thread Pool (Parallel)         │
│  - Thread 1: TestClass1             │
│  - Thread 2: TestClass2             │
│  - Thread N: ...                    │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
import org.testng.annotations.*;

public class CalculatorTest {

    @BeforeClass
    public void setUp() {
        System.out.println("Before Class");
    }

    @Test
    public void testAdd() {
        assert 2 + 3 == 5;
    }

    @Test
    public void testMultiply() {
        assert 4 * 5 == 20;
    }

    @AfterClass
    public void tearDown() {
        System.out.println("After Class");
    }
}
```

## 11. Medium Example

```java
import org.testng.annotations.*;

public class DataDrivenTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"admin", "password123", true},
            {"user", "wrong", false},
            {"", "password123", false}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expected) {
        boolean result = authenticate(username, password);
        assert result == expected;
    }

    private boolean authenticate(String username, String password) {
        return "admin".equals(username) && "password123".equals(password);
    }
}
```

## 12. Hard Example

```java
import org.testng.annotations.*;
import java.util.concurrent.*;

public class AdvancedTestNGTest {

    @Test(groups = {"critical", "fast"})
    public void criticalTest() {
        assert true;
    }

    @Test(groups = {"regression"}, dependsOnMethods = {"criticalTest"})
    public void regressionTest() {
        assert true;
    }

    @Test(timeOut = 5000)
    public void testWithTimeout() throws InterruptedException {
        Thread.sleep(1000);
        assert true;
    }

    @Factory(dataProvider = "factoryData")
    public Object[] createTests(int input, int expected) {
        return new Object[] { new DynamicTest(input, expected) };
    }

    @DataProvider(name = "factoryData")
    public Object[][] factoryData() {
        return new Object[][] {{1, 1}, {2, 4}, {3, 9}};
    }

    static class DynamicTest {
        private final int input;
        private final int expected;
        DynamicTest(int input, int expected) {
            this.input = input;
            this.expected = expected;
        }
        @Test
        public void testSquare() {
            assert input * input == expected;
        }
    }
}
```

## Interview Questions

1. **What is TestNG?**
   TestNG is a testing framework designed for Java applications, inspired by JUnit but with additional features like parallel execution, data providers, and XML configuration.

2. **How does TestNG differ from JUnit 5?**
   TestNG has native parallel execution, XML-based configuration, data providers, and test dependencies. JUnit 5 has extensions, parameterized tests, and a more modular architecture.

3. **When would you choose TestNG over JUnit 5?**
   Choose TestNG for large test suites requiring parallel execution, complex test dependencies, or XML-based configuration. Choose JUnit 5 for modern Java projects with extension-based needs.

4. **What is a DataProvider?**
   A DataProvider is a method that returns test data for parameterized tests, enabling data-driven testing.

5. **How do you run tests in parallel with TestNG?**
   Configure parallel execution in the XML suite file using parallel="methods" or parallel="tests" attributes.
