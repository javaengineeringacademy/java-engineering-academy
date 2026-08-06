# Java Logging Quiz

## Question 1
What is the primary purpose of SLF4J in Java logging?
- A) It is a logging implementation that writes logs to files
- B) It is a logging facade that provides a unified API for different logging backends
- C) It replaces all other logging frameworks
- D) It only works with Logback

**Answer: B**
**Explanation:** SLF4J (Simple Logging Facade for Java) is an abstraction layer that provides a common API. You can plug in different implementations like Logback or Log4j2 behind it.

## Question 2
Which log level should be used for messages that indicate a potential problem but the application can continue functioning?
- A) ERROR
- B) INFO
- C) WARN
- D) DEBUG

**Answer: C**
**Explanation:** WARN level is used for potentially harmful situations that don't prevent the application from continuing. ERROR indicates a failure that affects the current operation.

## Question 3
What is the recommended way to pass variable data to a log message in SLF4J?
- A) String concatenation: `logger.info("User: " + userId)`
- B) String formatting: `logger.info(String.format("User: %s", userId))`
- C) Parameterized messages: `logger.info("User: {}", userId)`
- D) System.out.println for dynamic data

**Answer: C**
**Explanation:** Parameterized messages with `{}` placeholders are preferred because they avoid string concatenation overhead when the log level is disabled, and they handle exceptions properly.

## Question 4
What is structured logging?
- A) Logging with consistent indentation
- B) Logging events in a machine-readable format like JSON
- C) Organizing log files into folders
- D) Using only the INFO log level

**Answer: B**
**Explanation:** Structured logging outputs logs in a structured format (e.g., JSON) with key-value pairs, making it easier to parse, search, and analyze logs programmatically.

## Question 5
Why should you avoid logging sensitive information like passwords or credit card numbers?
- A) It slows down the application
- B) Log files may be accessed by unauthorized users and violate compliance regulations
- C) It causes memory leaks
- D) It only works in development mode

**Answer: B**
**Explanation:** Logging sensitive data exposes it in log files, which may be accessed by unauthorized personnel or systems. This violates security best practices and compliance regulations like PCI-DSS and GDPR.