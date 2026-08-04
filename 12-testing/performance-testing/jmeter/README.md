# JMeter - Performance Testing

## Overview

Apache JMeter is an open-source load testing tool designed for analyzing and measuring the performance of web applications. It supports multiple protocols and provides comprehensive reporting.

## Setup

### Dependencies

```xml
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_core</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_java</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
```

## Basic Test Plan

### Thread Group

```java
// Programmatic JMeter test
class JMeterLoadTest {

    @Test
    void shouldRunLoadTest() throws Exception {
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // Create test plan
        TestPlan testPlan = new TestPlan("API Load Test");
        testPlan.setUserDefinedVariables(new Arguments());

        // Create thread group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(100);
        threadGroup.setRampUp(10);
        threadGroup.setSamplerController(new LoopController());
        ((LoopController) threadGroup.getSamplerController()).setLoops(-1);

        // Create HTTP sampler
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setDomain("localhost");
        httpSampler.setPort(8080);
        httpSampler.setPath("/api/users");
        httpSampler.setMethod("GET");

        // Create listener
        ViewResultsFullVisualizer results = new ViewResultsFullVisualizer();

        // Build test tree
        HashTree testTree = new HashTree();
        testTree.add(testPlan);
        testTree.add(testPlan, threadGroup);
        testTree.add(threadGroup, httpSampler);
        testTree.add(threadGroup, results);

        // Run test
        jmeter.configure(testTree);
        jmeter.run();
    }
}
```

## HTTP Samplers

### GET Request

```java
@Test
void shouldTestGetEndpoint() throws Exception {
    HTTPSampler sampler = new HTTPSampler();
    sampler.setDomain("api.example.com");
    sampler.setPort(443);
    sampler.setPath("/api/users");
    sampler.setProtocol("https");
    sampler.setMethod("GET");

    // Add headers
    HeaderManager headerManager = new HeaderManager();
    headerManager.add(new Header("Authorization", "Bearer token"));
    sampler.setHeaderManager(headerManager);

    // Add assertion
    ResponseAssertion assertion = new ResponseAssertion();
    assertion.setTestField(TestField.RESPONSE_CODE);
    assertion.setTestString("200");
    sampler.addTestElement(assertion);
}
```

### POST Request

```java
@Test
void shouldTestPostEndpoint() throws Exception {
    HTTPSampler sampler = new HTTPSampler();
    sampler.setMethod("POST");
    sampler.setPath("/api/users");
    sampler.setPostBodyRaw(true);

    // Set body
    HTTPArgument argument = new HTTPArgument();
    argument.setValue("{\"name\":\"John\",\"email\":\"john@example.com\"}");
    sampler.setArguments(new Arguments(argument));

    // Set content type
    HeaderManager headerManager = new HeaderManager();
    headerManager.add(new Header("Content-Type", "application/json"));
    sampler.setHeaderManager(headerManager);
}
```

## Assertions

### Response Assertion

```java
@Test
void shouldAssertResponse() {
    ResponseAssertion assertion = new ResponseAssertion();
    assertion.setTestField(TestField.RESPONSE_CODE);
    assertion.setTestString("200");
    assertion.setTestType(TestType.SUBSTRING);
}
```

### JSON Assertion

```java
@Test
void shouldAssertJsonResponse() {
    JSONPathAssertion assertion = new JSONPathAssertion();
    assertion.setJsonPath("$.status");
    assertion.setExpectedValue("success");
    assertion.setExpectNull(false);
}
```

### Duration Assertion

```java
@Test
void shouldAssertResponseTime() {
    DurationAssertion assertion = new DurationAssertion();
    assertion.setMaxDuration(1000); // 1 second
}
```

## Listeners

### Summary Report

```java
SummaryReport report = new SummaryReport();
report.setFilename("summary-report.csv");
```

### Aggregate Report

```java
AggregateReport report = new AggregateReport();
report.setFilename("aggregate-report.csv");
```

### View Results Tree

```java
ViewResultsFullVisualizer tree = new ViewResultsFullVisualizer();
```

## Timers

### Constant Timer

```java
ConstantTimer timer = new ConstantTimer();
timer.setDelay(1000); // 1 second between requests
```

### Gaussian Random Timer

```java
GaussianRandomTimer timer = new GaussianRandomTimer();
timer.setDeviation(200); // 200ms deviation
timer.setOffset(500); // 500ms offset
```

### Uniform Random Timer

```java
UniformRandomTimer timer = new UniformRandomTimer();
timer.setRange(1000); // 0-1000ms random delay
```

## Controllers

### Simple Controller

```java
SimpleController controller = new SimpleController();
controller.setName("User Operations");
```

### Loop Controller

```java
LoopController controller = new LoopController();
controller.setLoops(10); // Loop 10 times
```

### If Controller

```java
IfController controller = new IfController();
controller.setCondition("${__jexl3('${status}' == 'active')}");
```

## Pre-Processors

### User Defined Variables

```java
Arguments variables = new Arguments();
variables.addArgument("BASE_URL", "http://localhost:8080");
variables.addArgument("API_KEY", "test-key");
```

### HTTP Header Manager

```java
HeaderManager headerManager = new HeaderManager();
headerManager.add(new Header("Content-Type", "application/json"));
headerManager.add(new Header("Accept", "application/json"));
```

## Post-Processors

### JSON Extractor

```java
JSONExtractor extractor = new JSONExtractor();
extractor.setVariableNames("userId");
extractor.setJsonPath("$.id");
extractor.setMatchNumber(1);
```

### Regular Expression Extractor

```java
RegexExtractor extractor = new RegexExtractor();
extractor.setVariableName("token");
extractor.setRegex("token=([\\w]+)");
extractor.setTemplate("$1$");
```

## Distributed Testing

### Master-Slave Setup

```properties
# jmeter.properties
remote_hosts=slave1:1099,slave2:1099
```

### Programmatic Distributed Test

```java
@Test
void shouldRunDistributedTest() throws Exception {
    JMeterEngine engine = new JMeterEngine();
    engine.configure(testTree);

    RMIUtils.setProperty(RmiUtils.RMI_PORT, "1099");

    // Run on remote engines
    engine.run();
}
```

## Reporting

### HTML Report

```bash
jmeter -n -t test-plan.jmx -l results.jtl -e -o ./report
```

### Programmatic Report

```java
@Test
void shouldGenerateReport() throws Exception {
    Summariser summariser = new Summariser("test");
    ReportGenerator generator = new ReportGenerator("results.jtl", summariser);
    generator.generate();
}
```

## Best Practices

### Use Thread Groups Properly

```java
// Ramp-up gradually
ThreadGroup threadGroup = new ThreadGroup();
threadGroup.setNumThreads(100);
threadGroup.setRampUp(30); // 30 seconds ramp-up

// Use realistic think time
ConstantTimer timer = new ConstantTimer();
timer.setDelay(3000); // 3 seconds between requests
```

### Clean Up Resources

```java
@AfterEach
void cleanup() {
    // Close connections
    // Clear data
}
```

### Use Parameterization

```java
CSVDataSet csvDataSet = new CSVDataSet();
csvDataSet.setFilename("test-data.csv");
csvDataSet.setVariableNames("username,password");
csvDataSet.setRecycle(true);
```

## Resources

- [JMeter Documentation](https://jmeter.apache.org/)
- [JMeter Plugins](https://jmeter-plugins.org/)
- [JMeter GitHub](https://github.com/apache/jmeter)
- [Performance Testing Guide](https://www BlazeMeter.com/blog/how-create-load-test-plan)
