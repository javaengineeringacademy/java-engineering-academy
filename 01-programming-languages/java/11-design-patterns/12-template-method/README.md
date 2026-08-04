# Template Method Pattern

## 1. Introduction

The Template Method Pattern is a behavioral design pattern that defines the skeleton of an algorithm in a method, deferring some steps to subclasses. It lets one redefine certain steps of an algorithm without changing the algorithm's overall structure.

The Template Method pattern is particularly useful when you have an algorithm with invariant steps that remain the same, but variant steps that can be customized by subclasses.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Template Method pattern with hook methods
- Understand invariant vs. variant steps
- Recognize template usage in Java (AbstractList, InputStream)
- Apply the Hollywood Principle (Don't call us, we'll call you)
- Use hooks for optional customization

---

## 3. Prerequisites

- Understanding of inheritance and abstract classes
- Knowledge of method overriding
- Familiarity with the Hollywood Principle
- Understanding of skeleton algorithms

---

## 4. Why This Concept Exists

The Template Method pattern exists because:

- **Code reuse**: Common algorithm steps shared in base class
- **Invariant steps**: Core algorithm remains unchanged
- **Variant steps**: Subclasses customize specific steps
- **Hollywood Principle**: Base class calls subclass, not vice versa
- **Control flow**: Base class controls the algorithm flow

Without Template Method, you would duplicate algorithm structure in every subclass.

---

## 5. Problem Statement

Consider data processing:

```java
public class CsvProcessor {
    public void process(String filename) {
        // 1. Open file (same for all)
        // 2. Parse data (same for all)
        // 3. Validate data (different per type)
        // 4. Transform data (different per type)
        // 5. Save data (same for all)
    }
}

public class JsonProcessor {
    public void process(String filename) {
        // Same steps 1, 2, 5 as CsvProcessor
        // Different steps 3, 4
    }
}
```

**Problems:**
1. Code duplication in steps 1, 2, 5
2. No standardized algorithm flow
3. Hard to add new processor types

---

## 6. Theory

### 6.1 Template Structure

1. **AbstractClass**: Defines template method and abstract steps
2. **ConcreteClass**: Implements abstract steps
3. **Hook methods**: Optional steps with default behavior

### 6.2 Invariant vs. Variant

| Type | Description | Example |
|------|-------------|---------|
| Invariant | Same in all subclasses | File opening |
| Variant | Different per subclass | Data transformation |
| Hook | Optional, can be overridden | Validation |

### 6.3 Hollywood Principle

"Don't call us, we'll call you"
- Base class calls subclass methods
- Subclass never calls base class method (except super)
- Base class controls the flow

---

## 7. Internal Working

```
TemplateMethod()
  |-- step1()  // invariant
  |-- step2()  // invariant
  |-- hook1()  // optional
  |-- step3()  // variant (abstract)
  |-- step4()  // variant (abstract)
  |-- hook2()  // optional
  |-- step5()  // invariant
```

---

## 8. JVM Perspective

- Method dispatch uses virtual table
- Template method is final (typically)
- Abstract methods resolved at compile time
- Hook methods use default implementations

---

## 9. Memory Representation

```
AbstractClass
  |-- templateMethod(): final
  |-- step1(): concrete
  |-- step3(): abstract
  |-- hook1(): concrete (default)

ConcreteClass
  |-- step3(): implementation
  |-- step4(): implementation
```

---

## 10. Syntax

```java
public abstract class AbstractClass {
    public final void templateMethod() {
        step1();
        step2();
        if (hook()) {
            step3();
        }
        step4();
    }

    private void step1() { /* invariant */ }
    private void step2() { /* invariant */ }
    protected abstract void step3();
    protected abstract void step4();
    protected boolean hook() { return true; }
}

public class ConcreteClass extends AbstractClass {
    @Override
    protected void step3() { /* variant */ }

    @Override
    protected void step4() { /* variant */ }
}
```

---

## 11. Easy Example

### Data Miner

```java
public abstract class DataMiner {
    public final void mine() {
        openFile();
        extractData();
        parseData();
        analyzeData();
        closeFile();
    }

    private void openFile() {
        System.out.println("Opening file");
    }

    protected abstract void extractData();

    protected abstract void parseData();

    protected void analyzeData() {
        System.out.println("Analyzing data");
    }

    private void closeFile() {
        System.out.println("Closing file");
    }
}

public class CsvMiner extends DataMiner {
    @Override
    protected void extractData() {
        System.out.println("Extracting CSV data");
    }

    @Override
    protected void parseData() {
        System.out.println("Parsing CSV data");
    }
}

public class JsonMiner extends DataMiner {
    @Override
    protected void extractData() {
        System.out.println("Extracting JSON data");
    }

    @Override
    protected void parseData() {
        System.out.println("Parsing JSON data");
    }
}

// Usage
DataMiner miner = new CsvMiner();
miner.mine();
```

---

## 12. Medium Example

### Web Crawler with Hooks

```java
public abstract class WebCrawler {
    public final void crawl(String url) {
        connect(url);
        if (shouldAuthenticate()) {
            authenticate();
        }
        String content = downloadContent(url);
        if (shouldParse()) {
            content = parseContent(content);
        }
        processContent(content);
        if (shouldSave()) {
            saveContent(content);
        }
        disconnect();
    }

    private void connect(String url) {
        System.out.println("Connecting to " + url);
    }

    protected boolean shouldAuthenticate() {
        return false; // Default: no auth
    }

    protected void authenticate() {
        System.out.println("Authenticating");
    }

    protected String downloadContent(String url) {
        System.out.println("Downloading content");
        return "content from " + url;
    }

    protected boolean shouldParse() {
        return true;
    }

    protected abstract String parseContent(String content);

    protected abstract void processContent(String content);

    protected boolean shouldSave() {
        return true;
    }

    protected void saveContent(String content) {
        System.out.println("Saving content");
    }

    private void disconnect() {
        System.out.println("Disconnecting");
    }
}

public class NewsCrawler extends WebCrawler {
    @Override
    protected String parseContent(String content) {
        return "Parsed news: " + content;
    }

    @Override
    protected void processContent(String content) {
        System.out.println("Processing news article");
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }
}

public class ImageCrawler extends WebCrawler {
    @Override
    protected String parseContent(String content) {
        return "Image URL: " + content;
    }

    @Override
    protected void processContent(String content) {
        System.out.println("Downloading image");
    }

    @Override
    protected boolean shouldParse() {
        return false;
    }
}
```

---

## 13. Hard Example

### Report Generator with Multiple Hooks

```java
public abstract class ReportGenerator {
    public final Report generate(ReportRequest request) {
        validateRequest(request);
        Map<String, Object> data = fetchData(request);
        if (shouldFilterData()) {
            data = filterData(data);
        }
        if (shouldTransformData()) {
            data = transformData(data);
        }
        String content = formatContent(data);
        Report report = buildReport(request, content);
        if (shouldAddSummary()) {
            report.setSummary(generateSummary(data));
        }
        if (shouldExport()) {
            exportReport(report);
        }
        return report;
    }

    protected abstract void validateRequest(ReportRequest request);

    protected abstract Map<String, Object> fetchData(ReportRequest request);

    protected boolean shouldFilterData() { return false; }

    protected Map<String, Object> filterData(Map<String, Object> data) {
        return data;
    }

    protected boolean shouldTransformData() { return true; }

    protected abstract Map<String, Object> transformData(Map<String, Object> data);

    protected abstract String formatContent(Map<String, Object> data);

    protected abstract Report buildReport(ReportRequest request, String content);

    protected boolean shouldAddSummary() { return true; }

    protected String generateSummary(Map<String, Object> data) {
        return "Summary of " + data.size() + " items";
    }

    protected boolean shouldExport() { return false; }

    protected void exportReport(Report report) {
        System.out.println("Exporting report");
    }
}

public class SalesReportGenerator extends ReportGenerator {
    @Override
    protected void validateRequest(ReportRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Date range required");
        }
    }

    @Override
    protected Map<String, Object> fetchData(ReportRequest request) {
        return Map.of("sales", List.of(100, 200, 300));
    }

    @Override
    protected Map<String, Object> transformData(Map<String, Object> data) {
        return Map.of("totalSales", 600);
    }

    @Override
    protected String formatContent(Map<String, Object> data) {
        return "Sales Report: " + data;
    }

    @Override
    protected Report buildReport(ReportRequest request, String content) {
        return new Report("Sales Report", content);
    }

    @Override
    protected boolean shouldExport() {
        return true;
    }
}
```

---

## 14. Enterprise Example

### Payment Processing Template

```java
public abstract class PaymentProcessor {
    public final PaymentResult process(PaymentRequest request) {
        validateRequest(request);
        Payment payment = createPayment(request);
        if (shouldApplyFees()) {
            payment = applyFees(payment);
        }
        if (shouldApplyDiscount(request)) {
            payment = applyDiscount(payment);
        }
        PaymentResult result = executePayment(payment);
        if (result.isSuccess()) {
            sendConfirmation(request, result);
        }
        logTransaction(request, result);
        return result;
    }

    protected abstract void validateRequest(PaymentRequest request);

    protected abstract Payment createPayment(PaymentRequest request);

    protected boolean shouldApplyFees() { return true; }

    protected Payment applyFees(Payment payment) {
        return payment.addFee(BigDecimal.valueOf(2.50));
    }

    protected boolean shouldApplyDiscount(PaymentRequest request) {
        return false;
    }

    protected Payment applyDiscount(Payment payment) {
        return payment;
    }

    protected abstract PaymentResult executePayment(Payment payment);

    protected void sendConfirmation(PaymentRequest request, PaymentResult result) {
        System.out.println("Sending confirmation");
    }

    protected abstract void logTransaction(PaymentRequest request, PaymentResult result);
}

public class CreditCardProcessor extends PaymentProcessor {
    @Override
    protected void validateRequest(PaymentRequest request) {
        if (request.getCardNumber() == null) {
            throw new IllegalArgumentException("Card number required");
        }
    }

    @Override
    protected Payment createPayment(PaymentRequest request) {
        return new Payment(request.getAmount(), "CREDIT_CARD");
    }

    @Override
    protected PaymentResult executePayment(Payment payment) {
        return new PaymentResult(true, "CC-" + System.currentTimeMillis());
    }

    @Override
    protected void logTransaction(PaymentRequest request, PaymentResult result) {
        System.out.println("Logged credit card transaction");
    }

    @Override
    protected boolean shouldApplyDiscount(PaymentRequest request) {
        return request.getAmount().compareTo(BigDecimal.valueOf(100)) > 0;
    }

    @Override
    protected Payment applyDiscount(Payment payment) {
        return payment.applyDiscount(BigDecimal.valueOf(5)); // $5 off
    }
}
```

---

## 15. Performance

| Aspect | Impact | Notes |
|--------|--------|-------|
| Method calls | O(1) | Virtual dispatch |
| Hook checks | O(1) | Boolean checks |
| Template call | O(n) | n = steps |

---

## 16. Best Practices

1. Keep template method final
2. Use hooks for optional steps
3. Document invariant vs. variant steps
4. Limit abstract methods (max 5-7)
5. Use protected for subclass access
6. Follow Hollywood Principle

---

## 17. Common Mistakes

1. Not making template method final
2. Too many abstract methods
3. Calling subclass methods from subclasses
4. Overriding invariant steps
5. Not providing default hook implementations

---

## 18. Pitfalls

- Inheritance can be inflexible
- Difficult to understand algorithm flow
- Subclasses may break invariant steps
- Hard to add new steps to base class

---

## 19. Debugging Tips

1. Add logging to template method
2. Use debugger to trace method calls
3. Document expected call order
4. Test each subclass independently

---

## 20. Comparison Table

| Feature | Template Method | Strategy | Decorator |
|---------|-----------------|----------|-----------|
| Flexibility | Compile-time | Runtime | Runtime |
| Inheritance | Yes | No | No |
| Control | Base class | Client | Client |
| Code reuse | High | Medium | Medium |

---

## 21. Decision Tree

```
Algorithm with invariant structure? -> Template Method
Need runtime algorithm switching? -> Strategy
Need to add behavior dynamically? -> Decorator
Need to encapsulate request? -> Command
```

---

## 22. Interview Questions

### Q1: What is Template Method?
A behavioral pattern that defines algorithm skeleton in base class, deferring specific steps to subclasses.

### Q2: Template Method vs. Strategy?
Template Method uses inheritance, algorithm fixed at compile time. Strategy uses composition, algorithm changeable at runtime.

### Q3: What are hook methods?
Optional methods with default implementations that subclasses can override to customize algorithm behavior.

### Q4: Hollywood Principle?
"Don't call us, we'll call you" - Base class calls subclass methods, not vice versa.

### Q5: Real-world examples?
Java AbstractList, InputStream, JUnit test lifecycle, Spring JdbcTemplate.

---

## 23. Exercises

1. Create a template for file processing (CSV, JSON, XML)
2. Implement a web scraper with template method
3. Build a test framework with setup/teardown hooks

---

## 24. Assignments

1. Implement a report generator with template method
2. Create a data export system with multiple formats
3. Build a validation framework with hooks

---

## 25. Mini Project

### Build System Template
Create a build system with template method that:
- Defines standard build steps
- Allows customization for different project types
- Uses hooks for optional steps
- Supports multiple output formats

---

## 26. Summary

- Template Method defines algorithm skeleton
- Uses inheritance for code reuse
- Hook methods provide optional customization
- Follows Hollywood Principle
- Base class controls algorithm flow
- Variant steps delegated to subclasses

---

## 27. References

1. Gamma, E., et al. (1994). Design Patterns, Chapter 5
2. Bloch, J. (2018). Effective Java
3. Refactoring Guru: https://refactoring.guru/design-patterns/template-method
