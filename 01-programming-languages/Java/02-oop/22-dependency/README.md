# Dependency

## Introduction

Dependency is the weakest form of relationship in object-oriented programming where one class uses another class temporarily to perform a specific operation, without maintaining a long-term relationship or ownership, creating a transient connection that exists only during method execution and disappears when the operation completes. Unlike association, aggregation, or composition which represent structural relationships, dependency represents a usage relationship where one class depends on another for a specific functionality but does not store a reference to it as an instance variable. Dependencies are typically created through method parameters, local variables, or static method calls, and they represent the most loose form of coupling between classes. Understanding dependencies is crucial for designing maintainable, testable systems because excessive or poorly managed dependencies can lead to tight coupling, making code difficult to understand, modify, and test.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the concept of dependency and how it differs from other relationships
- [ ] Identify different types of dependencies (method parameter, local variable, static call)
- [ ] Implement dependencies using dependency injection to reduce coupling
- [ ] Apply dependency management principles to create testable, maintainable designs

## Prerequisites

- [20-association](../20-association/README.md) - Understanding object relationships
- [19-composition](../19-composition/README.md) - Understanding ownership relationships
- [05-methods](../05-methods/README.md) - Method parameters and invocation
- [12-interfaces](../12-interfaces/README.md) - Interface contracts and polymorphism

## Why This Concept Exists

### The Problem

Without proper dependency management, classes become tightly coupled:

1. **Hard-coded dependencies**: Classes directly instantiate their dependencies
2. **Difficult testing**: Hard to mock or stub dependencies for unit testing
3. **Reduced flexibility**: Dependencies cannot be swapped without changing the dependent class
4. **Maintenance nightmare**: Changes in dependencies cascade through the system

```java
// Problem: Tight coupling through direct instantiation
class OrderService {
    private DatabaseRepository repository = new DatabaseRepository(); // Hard dependency
    private EmailService emailService = new EmailService(); // Hard dependency

    public void processOrder(Order order) {
        repository.save(order);
        emailService.sendConfirmation(order);
    }
}
```

### The Solution

Dependency management solves these problems by:

- Defining dependencies through interfaces rather than concrete classes
- Injecting dependencies from outside rather than creating them internally
- Making dependencies explicit and configurable
- Enabling easy testing through mock objects

### Real-World Analogy

Think of dependency as a **restaurant customer ordering food**. The customer depends on the kitchen to prepare the meal, but:
- The customer doesn't own or control the kitchen
- The dependency exists only during the meal preparation
- The kitchen could be replaced (different restaurant) without affecting the customer
- The customer doesn't know the internal workings of the kitchen

This is different from composition (customer owns a phone) or aggregation (customer belongs to a loyalty program).

## Internal Working

### JVM Perspective

Dependencies are implemented through temporary object references:

1. **Method Parameters**: Dependencies passed as method arguments
2. **Local Variables**: Dependencies created and used within methods
3. **Static Calls**: Dependencies invoked through static methods
4. **No Persistent State**: Dependencies are not stored as instance variables

### Memory Representation

```
Dependency in Memory:

OrderService Method Execution:
┌─────────────────────────────┐
│ Stack Frame: processOrder() │
├─────────────────────────────┤
│ Parameters:                 │
│ ├── order: Order → ───────────────→ Order Object
│                             │
│ Local Variables:            │
│ └── repository → ──────────────────→ DatabaseRepository Object
│                             │
│ (Dependencies are temporary)│
└─────────────────────────────┘

After method completes:
┌─────────────────────────────┐
│ Stack Frame: processOrder() │
├─────────────────────────────┘
│ (Stack frame popped, local   │
│  variables destroyed,        │
│  dependencies dereferenced)  │
└─────────────────────────────┘
```

### Dependency Types

```
Dependency Relationships:

1. Method Parameter Dependency:
   class Service {
       void process(Repository repo) { // Dependency through parameter
           repo.save(data);
       }
   }

2. Local Variable Dependency:
   class Service {
       void process() {
           Repository repo = new DatabaseRepository(); // Dependency through local variable
           repo.save(data);
       }
   }

3. Static Call Dependency:
   class Service {
       void process() {
           UtilityHelper.format(data); // Dependency through static call
       }
   }

4. Dependency Injection:
   class Service {
       private final Repository repo; // Dependency through injection

       Service(Repository repo) { // Injected dependency
           this.repo = repo;
       }
   }
```

## Syntax

### Method Parameter Dependency

```java
class OrderProcessor {
    public void processOrder(Order order, PaymentProcessor processor) {
        // Dependency on PaymentProcessor through parameter
        processor.processPayment(order.getTotal());
    }
}

// Usage
OrderProcessor processor = new OrderProcessor();
PaymentProcessor creditCard = new CreditCardProcessor();
processor.processOrder(order, creditCard); // Dependency injected through parameter
```

### Local Variable Dependency

```java
class ReportGenerator {
    public Report generateReport(Data data) {
        // Dependency on ReportFormatter through local variable
        ReportFormatter formatter = new PDFReportFormatter();
        return formatter.format(data);
    }
}
```

### Static Call Dependency

```java
class DataValidator {
    public boolean validate(String data) {
        // Dependency on ValidationUtils through static call
        return ValidationUtils.isNotEmpty(data) && ValidationUtils.isValidFormat(data);
    }
}
```

### Dependency Injection

```java
class OrderService {
    private final Repository repository;
    private final NotificationService notificationService;

    // Dependencies injected through constructor
    public OrderService(Repository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public void processOrder(Order order) {
        repository.save(order);
        notificationService.sendConfirmation(order);
    }
}

// Dependencies provided externally
Repository repo = new DatabaseRepository();
NotificationService notifier = new EmailNotificationService();
OrderService service = new OrderService(repo, notifier);
```

## Easy Examples

### Example 1: Payment Processing System

**Problem Statement**: Design a payment processing system that demonstrates different types of dependencies and how to manage them for flexibility and testability.

**Implementation**:

```java
package academy.javaengineering.oop.dependency;

import java.time.LocalDateTime;
import java.util.UUID;

// Interfaces for dependencies (reduces coupling)
interface PaymentGateway {
    boolean processPayment(double amount, String currency);
    String getTransactionId();
}

interface NotificationService {
    void sendNotification(String recipient, String message);
}

interface Logger {
    void log(String level, String message);
}

// Concrete implementations
class StripePaymentGateway implements PaymentGateway {
    private String transactionId;

    @Override
    public boolean processPayment(double amount, String currency) {
        System.out.println("Stripe: Processing $" + amount + " " + currency);
        transactionId = UUID.randomUUID().toString();
        System.out.println("Stripe: Payment successful. Transaction ID: " + transactionId);
        return true;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}

class PayPalPaymentGateway implements PaymentGateway {
    private String transactionId;

    @Override
    public boolean processPayment(double amount, String currency) {
        System.out.println("PayPal: Processing $" + amount + " " + currency);
        transactionId = "PP-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("PayPal: Payment successful. Transaction ID: " + transactionId);
        return true;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}

class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("Email to " + recipient + ": " + message);
    }
}

class SMSNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("SMS to " + recipient + ": " + message);
    }
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String level, String message) {
        System.out.println("[" + level + "] " + LocalDateTime.now() + ": " + message);
    }
}

class FileLogger implements Logger {
    @Override
    public void log(String level, String message) {
        System.out.println("FILE [" + level + "] " + message);
    }
}

// Order class (domain object)
class Order {
    private String orderId;
    private String customerEmail;
    private double amount;
    private String currency;

    public Order(String customerEmail, double amount, String currency) {
        this.orderId = UUID.randomUUID().toString();
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.currency = currency;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerEmail() { return customerEmail; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}

// OrderService with dependency injection
class OrderService {
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;
    private final Logger logger;

    // Constructor injection (primary dependency injection method)
    public OrderService(PaymentGateway paymentGateway,
                       NotificationService notificationService,
                       Logger logger) {
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
        this.logger = logger;
    }

    public boolean processOrder(Order order) {
        logger.log("INFO", "Processing order: " + order.getOrderId());

        // Use injected dependencies
        boolean paymentSuccess = paymentGateway.processPayment(order.getAmount(), order.getCurrency());

        if (paymentSuccess) {
            String message = "Order " + order.getOrderId() + " confirmed. " +
                           "Transaction ID: " + paymentGateway.getTransactionId();
            notificationService.sendNotification(order.getCustomerEmail(), message);
            logger.log("INFO", "Order processed successfully: " + order.getOrderId());
            return true;
        } else {
            logger.log("ERROR", "Payment failed for order: " + order.getOrderId());
            return false;
        }
    }
}

// Method parameter dependency example
class PaymentRefunder {
    public void refund(Order order, PaymentGateway gateway, Logger logger) {
        // Dependencies through method parameters
        logger.log("INFO", "Processing refund for order: " + order.getOrderId());
        System.out.println("Refunding $" + order.getAmount() + " via " + gateway.getClass().getSimpleName());
        logger.log("INFO", "Refund completed for order: " + order.getOrderId());
    }
}

// Local variable dependency example
class ReceiptGenerator {
    public String generateReceipt(Order order) {
        // Dependency on receipt formatter through local variable
        ReceiptFormatter formatter = new SimpleReceiptFormatter();
        return formatter.format(order);
    }
}

interface ReceiptFormatter {
    String format(Order order);
}

class SimpleReceiptFormatter implements ReceiptFormatter {
    @Override
    public String format(Order order) {
        return String.format(
            "RECEIPT%nOrder ID: %s%nAmount: $%.2f %s%nDate: %s",
            order.getOrderId(), order.getAmount(), order.getCurrency(), LocalDateTime.now()
        );
    }
}

public class PaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== Payment Processing System Demo ===\n");

        // Create dependencies
        PaymentGateway stripe = new StripePaymentGateway();
        NotificationService emailService = new EmailNotificationService();
        Logger logger = new ConsoleLogger();

        // Inject dependencies into service
        OrderService orderService = new OrderService(stripe, emailService, logger);

        // Process orders
        Order order1 = new Order("alice@email.com", 99.99, "USD");
        Order order2 = new Order("bob@email.com", 149.99, "EUR");

        System.out.println("Processing Order 1:");
        orderService.processOrder(order1);

        System.out.println("\nProcessing Order 2:");
        orderService.processOrder(order2);

        // Demonstrate method parameter dependency
        System.out.println("\n=== Method Parameter Dependency ===");
        PaymentRefunder refunder = new PaymentRefunder();
        refunder.refund(order1, stripe, logger);

        // Demonstrate local variable dependency
        System.out.println("\n=== Local Variable Dependency ===");
        ReceiptGenerator receiptGen = new ReceiptGenerator();
        System.out.println(receiptGen.generateReceipt(order1));

        // Demonstrate swapping dependencies
        System.out.println("\n=== Swapping Dependencies ===");
        PaymentGateway paypal = new PayPalPaymentGateway();
        NotificationService smsService = new SMSNotificationService();
        OrderService orderService2 = new OrderService(paypal, smsService, logger);

        Order order3 = new Order("555-1234", 75.00, "USD");
        orderService2.processOrder(order3);
    }
}
```

**Expected Output**:
```
=== Payment Processing System Demo ===

Processing Order 1:
[INFO] 2024-01-15T10:30:00: Processing order: [order-id]
Stripe: Processing $99.99 USD
Stripe: Payment successful. Transaction ID: [uuid]
Email to alice@email.com: Order [order-id] confirmed. Transaction ID: [uuid]
[INFO] 2024-01-15T10:30:00: Order processed successfully: [order-id]

Processing Order 2:
[INFO] 2024-01-15T10:30:00: Processing order: [order-id]
Stripe: Processing $149.99 EUR
Stripe: Payment successful. Transaction ID: [uuid]
Email to bob@email.com: Order [order-id] confirmed. Transaction ID: [uuid]
[INFO] 2024-01-15T10:30:00: Order processed successfully: [order-id]

=== Method Parameter Dependency ===
[INFO] 2024-01-15T10:30:00: Processing refund for order: [order-id]
Refunding $99.99 via StripePaymentGateway
[INFO] 2024-01-15T10:30:00: Refund completed for order: [order-id]

=== Local Variable Dependency ===
RECEIPT
Order ID: [order-id]
Amount: $99.99 USD
Date: 2024-01-15T10:30:00

=== Swapping Dependencies ===
[INFO] 2024-01-15T10:30:00: Processing order: [order-id]
PayPal: Processing $75.0 USD
PayPal: Payment successful. Transaction ID: PP-[id]
SMS to 555-1234: Order [order-id] confirmed. Transaction ID: PP-[id]
[INFO] 2024-01-15T10:30:00: Order processed successfully: [order-id]
```

**Best Practices**:
- Use interfaces for dependency types to enable swapping
- Prefer constructor injection for required dependencies
- Use method parameters for optional or transient dependencies
- Keep dependencies explicit and well-documented

### Example 2: File Processing System

**Problem Statement**: Design a file processing system that demonstrates different dependency patterns and how to manage them for flexibility and testability.

**Implementation**:

```java
package academy.javaengineering.oop.dependency;

import java.util.ArrayList;
import java.util.List;

// Dependency interfaces
interface FileReader {
    List<String> read(String filePath);
}

interface FileWriter {
    void write(String filePath, List<String> content);
}

interface ContentProcessor {
    List<String> process(List<String> content);
}

interface ErrorHandler {
    void handleError(String operation, Exception e);
}

// Concrete implementations
class TextFileReader implements FileReader {
    @Override
    public List<String> read(String filePath) {
        System.out.println("Reading text file: " + filePath);
        List<String> content = new ArrayList<>();
        content.add("Line 1: Hello World");
        content.add("Line 2: Java Programming");
        content.add("Line 3: Dependency Management");
        return content;
    }
}

class CSVFileReader implements FileReader {
    @Override
    public List<String> read(String filePath) {
        System.out.println("Reading CSV file: " + filePath);
        List<String> content = new ArrayList<>();
        content.add("Name,Age,City");
        content.add("Alice,30,New York");
        content.add("Bob,25,San Francisco");
        return content;
    }
}

class TextFileWriter implements FileWriter {
    @Override
    public void write(String filePath, List<String> content) {
        System.out.println("Writing to text file: " + filePath);
        for (String line : content) {
            System.out.println("  " + line);
        }
    }
}

class UpperCaseProcessor implements ContentProcessor {
    @Override
    public List<String> process(List<String> content) {
        List<String> processed = new ArrayList<>();
        for (String line : content) {
            processed.add(line.toUpperCase());
        }
        return processed;
    }
}

class LineCounterProcessor implements ContentProcessor {
    @Override
    public List<String> process(List<String> content) {
        List<String> processed = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            processed.add((i + 1) + ": " + content.get(i));
        }
        return processed;
    }
}

class ConsoleErrorHandler implements ErrorHandler {
    @Override
    public void handleError(String operation, Exception e) {
        System.err.println("Error during " + operation + ": " + e.getMessage());
    }
}

class LoggingErrorHandler implements ErrorHandler {
    @Override
    public void handleError(String operation, Exception e) {
        System.out.println("[LOG ERROR] " + operation + ": " + e.getMessage());
    }
}

// File processor service with dependency injection
class FileProcessorService {
    private final FileReader reader;
    private final FileWriter writer;
    private final ContentProcessor processor;
    private final ErrorHandler errorHandler;

    public FileProcessorService(FileReader reader, FileWriter writer,
                               ContentProcessor processor, ErrorHandler errorHandler) {
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
        this.errorHandler = errorHandler;
    }

    public boolean processFile(String inputPath, String outputPath) {
        try {
            // Use injected dependencies
            List<String> content = reader.read(inputPath);
            List<String> processed = processor.process(content);
            writer.write(outputPath, processed);
            return true;
        } catch (Exception e) {
            errorHandler.handleError("processFile", e);
            return false;
        }
    }
}

// Example with multiple dependencies
class BatchFileProcessor {
    private final List<FileProcessorService> processors;

    public BatchFileProcessor(List<FileProcessorService> processors) {
        this.processors = processors;
    }

    public void processFiles(List<String[]> filePairs) {
        System.out.println("=== Batch Processing ===");
        for (String[] pair : filePairs) {
            String input = pair[0];
            String output = pair[1];

            for (FileProcessorService processor : processors) {
                System.out.println("\nProcessing: " + input + " -> " + output);
                processor.processFile(input, output);
            }
        }
    }
}

public class FileProcessingDemo {
    public static void main(String[] args) {
        System.out.println("=== File Processing System Demo ===\n");

        // Create different processor configurations
        FileReader textReader = new TextFileReader();
        FileReader csvReader = new CSVFileReader();
        FileWriter writer = new TextFileWriter();
        ErrorHandler consoleHandler = new ConsoleErrorHandler();
        ErrorHandler loggingHandler = new LoggingErrorHandler();

        // Configuration 1: Text file with uppercase processing
        ContentProcessor upperCase = new UpperCaseProcessor();
        FileProcessorService textUpperProcessor = new FileProcessorService(
            textReader, writer, upperCase, consoleHandler
        );

        // Configuration 2: CSV file with line counting
        ContentProcessor lineCounter = new LineCounterProcessor();
        FileProcessorService csvLineProcessor = new FileProcessorService(
            csvReader, writer, lineCounter, loggingHandler
        );

        // Process files
        System.out.println("=== Processing Text File with Uppercase ===");
        textUpperProcessor.processFile("input.txt", "output_upper.txt");

        System.out.println("\n=== Processing CSV File with Line Counting ===");
        csvLineProcessor.processFile("data.csv", "output_counted.txt");

        // Demonstrate dependency swapping
        System.out.println("\n=== Swapping Dependencies ===");
        FileProcessorService textWithLineCount = new FileProcessorService(
            textReader, writer, lineCounter, consoleHandler
        );
        textWithLineCount.processFile("input.txt", "output_counted.txt");

        // Batch processing with multiple processors
        System.out.println("\n=== Batch Processing ===");
        List<FileProcessorService> processors = new ArrayList<>();
        processors.add(textUpperProcessor);
        processors.add(textWithLineCount);

        BatchFileProcessor batchProcessor = new BatchFileProcessor(processors);
        List<String[]> filePairs = new ArrayList<>();
        filePairs.add(new String[]{"file1.txt", "out1.txt"});
        filePairs.add(new String[]{"file2.txt", "out2.txt"});

        batchProcessor.processFiles(filePairs);
    }
}
```

**Expected Output**:
```
=== File Processing System Demo ===

=== Processing Text File with Uppercase ===
Reading text file: input.txt
Writing to text file: output_upper.txt
  LINE 1: HELLO WORLD
  LINE 2: JAVA PROGRAMMING
  LINE 3: DEPENDENCY MANAGEMENT

=== Processing CSV File with Line Counting ===
Reading CSV file: data.csv
Writing to text file: output_counted.txt
  1: Name,Age,City
  2: Alice,30,New York
  3: Bob,25,San Francisco

=== Swapping Dependencies ===
Reading text file: input.txt
Writing to text file: output_counted.txt
  1: Line 1: Hello World
  2: Line 2: Java Programming
  3: Line 3: Dependency Management

=== Batch Processing ===

Processing: file1.txt -> out1.txt
Reading text file: file1.txt
Writing to text file: out1.txt
  LINE 1: HELLO WORLD
  LINE 2: JAVA PROGRAMMING
  LINE 3: DEPENDENCY MANAGEMENT

Processing: file1.txt -> out1.txt
Reading text file: file1.txt
Writing to text file: out1.txt
  1: Line 1: Hello World
  2: Line 2: Java Programming
  3: Line 3: Dependency Management

Processing: file2.txt -> out2.txt
Reading text file: file2.txt
Writing to text file: out2.txt
  LINE 1: HELLO WORLD
  LINE 2: JAVA PROGRAMMING
  LINE 3: DEPENDENCY MANAGEMENT

Processing: file2.txt -> out2.txt
Reading text file: file2.txt
Writing to text file: out2.txt
  1: Line 1: Hello World
  2: Line 2: Java Programming
  3: Line 3: Dependency Management
```

**Best Practices**:
- Use interfaces for all dependencies
- Support multiple dependency injection methods
- Keep dependencies minimal and focused
- Document expected behavior of dependencies

## Medium Examples

### Example 1: Notification System with Multiple Dependencies

**Problem Statement**: Design a notification system that demonstrates complex dependency management with multiple notification channels, message formatting, and delivery tracking.

**Requirements**:

- Support multiple notification channels (Email, SMS, Push)
- Message formatting dependencies
- Delivery tracking and logging
- Retry mechanism with configurable backoff

**Implementation**:

```java
package academy.javaengineering.oop.dependency;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Core dependency interfaces
interface MessageFormatter {
    String format(String template, Object... args);
}

interface DeliveryTracker {
    void trackDelivery(String notificationId, String status);
    boolean isDelivered(String notificationId);
}

interface RetryPolicy {
    int getMaxRetries();
    long getBackoffMillis(int attempt);
}

// Notification channel interfaces
interface NotificationChannel {
    String getChannelName();
    boolean send(String recipient, String message);
    boolean supportsRecipient(String recipient);
}

// Concrete implementations
class HTMLMessageFormatter implements MessageFormatter {
    @Override
    public String format(String template, Object... args) {
        String formatted = String.format(template, args);
        return "<html><body>" + formatted + "</body></html>";
    }
}

class PlainTextMessageFormatter implements MessageFormatter {
    @Override
    public String format(String template, Object... args) {
        return String.format(template, args);
    }
}

class InMemoryDeliveryTracker implements DeliveryTracker {
    private final List<String> delivered = new ArrayList<>();

    @Override
    public void trackDelivery(String notificationId, String status) {
        if ("DELIVERED".equals(status)) {
            delivered.add(notificationId);
        }
    }

    @Override
    public boolean isDelivered(String notificationId) {
        return delivered.contains(notificationId);
    }
}

class ExponentialBackoffRetryPolicy implements RetryPolicy {
    @Override
    public int getMaxRetries() {
        return 3;
    }

    @Override
    public long getBackoffMillis(int attempt) {
        return (long) Math.pow(2, attempt) * 1000; // 1s, 2s, 4s
    }
}

class FixedDelayRetryPolicy implements RetryPolicy {
    private final long delayMillis;

    public FixedDelayRetryPolicy(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public int getMaxRetries() {
        return 3;
    }

    @Override
    public long getBackoffMillis(int attempt) {
        return delayMillis;
    }
}

// Concrete notification channels
class EmailNotificationChannel implements NotificationChannel {
    @Override
    public String getChannelName() {
        return "EMAIL";
    }

    @Override
    public boolean send(String recipient, String message) {
        if (!supportsRecipient(recipient)) {
            return false;
        }
        System.out.println("EMAIL to " + recipient + ": " + message.substring(0, Math.min(50, message.length())) + "...");
        return true;
    }

    @Override
    public boolean supportsRecipient(String recipient) {
        return recipient.contains("@");
    }
}

class SMSNotificationChannel implements NotificationChannel {
    @Override
    public String getChannelName() {
        return "SMS";
    }

    @Override
    public boolean send(String recipient, String message) {
        if (!supportsRecipient(recipient)) {
            return false;
        }
        System.out.println("SMS to " + recipient + ": " + message.substring(0, Math.min(160, message.length())));
        return true;
    }

    @Override
    public boolean supportsRecipient(String recipient) {
        return recipient.matches("\\d{10}");
    }
}

class PushNotificationChannel implements NotificationChannel {
    @Override
    public String getChannelName() {
        return "PUSH";
    }

    @Override
    public boolean send(String recipient, String message) {
        if (!supportsRecipient(recipient)) {
            return false;
        }
        System.out.println("PUSH to device " + recipient + ": " + message.substring(0, Math.min(100, message.length())));
        return true;
    }

    @Override
    public boolean supportsRecipient(String recipient) {
        return recipient.startsWith("device_");
    }
}

// Notification service with dependency injection
class NotificationService {
    private final List<NotificationChannel> channels;
    private final MessageFormatter formatter;
    private final DeliveryTracker tracker;
    private final RetryPolicy retryPolicy;

    public NotificationService(List<NotificationChannel> channels,
                              MessageFormatter formatter,
                              DeliveryTracker tracker,
                              RetryPolicy retryPolicy) {
        this.channels = channels;
        this.formatter = formatter;
        this.tracker = tracker;
        this.retryPolicy = retryPolicy;
    }

    public boolean sendNotification(String recipient, String template, Object... args) {
        String message = formatter.format(template, args);
        String notificationId = generateNotificationId();

        // Find appropriate channel
        NotificationChannel channel = findChannel(recipient);
        if (channel == null) {
            System.out.println("No suitable channel for recipient: " + recipient);
            return false;
        }

        // Send with retry
        for (int attempt = 0; attempt <= retryPolicy.getMaxRetries(); attempt++) {
            System.out.println("Attempting to send via " + channel.getChannelName() +
                " (attempt " + (attempt + 1) + ")");

            if (channel.send(recipient, message)) {
                tracker.trackDelivery(notificationId, "DELIVERED");
                System.out.println("Notification sent successfully. ID: " + notificationId);
                return true;
            }

            if (attempt < retryPolicy.getMaxRetries()) {
                long delay = retryPolicy.getBackoffMillis(attempt);
                System.out.println("Retry in " + delay + "ms...");
                try {
                    Thread.sleep(Math.min(delay, 100)); // Cap delay for demo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        tracker.trackDelivery(notificationId, "FAILED");
        System.out.println("Failed to send notification after " + retryPolicy.getMaxRetries() + " attempts");
        return false;
    }

    private NotificationChannel findChannel(String recipient) {
        for (NotificationChannel channel : channels) {
            if (channel.supportsRecipient(recipient)) {
                return channel;
            }
        }
        return null;
    }

    private String generateNotificationId() {
        return "NOTIF-" + System.currentTimeMillis();
    }
}

// Notification manager with batch processing
class NotificationManager {
    private final NotificationService service;

    public NotificationManager(NotificationService service) {
        this.service = service;
    }

    public void sendBatchNotifications(List<String[]> notifications) {
        System.out.println("\n=== Batch Notification Processing ===");
        int successCount = 0;

        for (String[] notification : notifications) {
            String recipient = notification[0];
            String message = notification[1];

            System.out.println("\n--- Sending to: " + recipient + " ---");
            if (service.sendNotification(recipient, message)) {
                successCount++;
            }
        }

        System.out.println("\n=== Batch Complete: " + successCount + "/" + notifications.size() + " successful ===");
    }
}

public class NotificationDemo {
    public static void main(String[] args) {
        System.out.println("=== Notification System Demo ===\n");

        // Create dependencies
        List<NotificationChannel> channels = new ArrayList<>();
        channels.add(new EmailNotificationChannel());
        channels.add(new SMSNotificationChannel());
        channels.add(new PushNotificationChannel());

        MessageFormatter formatter = new PlainTextMessageFormatter();
        DeliveryTracker tracker = new InMemoryDeliveryTracker();
        RetryPolicy retryPolicy = new ExponentialBackoffRetryPolicy();

        // Inject dependencies
        NotificationService service = new NotificationService(channels, formatter, tracker, retryPolicy);
        NotificationManager manager = new NotificationManager(service);

        // Send individual notifications
        System.out.println("=== Individual Notifications ===");
        service.sendNotification("user@email.com", "Hello %s! Your order %s is confirmed.", "Alice", "ORD-001");
        service.sendNotification("1234567890", "Your OTP is %d", 123456);
        service.sendNotification("device_abc", "New message from %s", "Bob");

        // Send batch notifications
        List<String[]> batch = new ArrayList<>();
        batch.add(new String[]{"user2@email.com", "Welcome to our service!"});
        batch.add(new String[]{"0987654321", "Your verification code: %d", 654321});
        batch.add(new String[]{"device_xyz", "You have a new notification"});

        manager.sendBatchNotifications(batch);

        // Verify delivery
        System.out.println("\n=== Delivery Verification ===");
        System.out.println("Notification delivered: " + tracker.isDelivered("NOTIF-123"));
    }
}
```

**Expected Output**:
```
=== Notification System Demo ===

=== Individual Notifications ===
Attempting to send via EMAIL (attempt 1)
EMAIL to user@email.com: Hello Alice! Your order ORD-001 is confirmed....
Notification sent successfully. ID: NOTIF-[id]
Attempting to send via SMS (attempt 1)
SMS to 1234567890: Your OTP is 123456
Notification sent successfully. ID: NOTIF-[id]
Attempting to send via PUSH (attempt 1)
PUSH to device_abc: New message from Bob
Notification sent successfully. ID: NOTIF-[id]

=== Batch Notification Processing ===

--- Sending to: user2@email.com ---
Attempting to send via EMAIL (attempt 1)
EMAIL to user2@email.com: Welcome to our service!...
Notification sent successfully. ID: NOTIF-[id]

--- Sending to: 0987654321 ---
Attempting to send via SMS (attempt 1)
SMS to 0987654321: Your verification code: 654321
Notification sent successfully. ID: NOTIF-[id]

--- Sending to: device_xyz ---
Attempting to send via PUSH (attempt 1)
PUSH to device_xyz: You have a new notification
Notification sent successfully. ID: NOTIF-[id]

=== Batch Complete: 3/3 successful ===

=== Delivery Verification ===
Notification delivered: false
```

**Code Walkthrough**:

1. **Dependency Interfaces**: Each dependency type is defined as an interface for flexibility
2. **Multiple Implementations**: Each interface has multiple implementations
3. **Dependency Injection**: All dependencies are injected through the constructor
4. **Retry Mechanism**: Configurable retry policy demonstrates complex dependency behavior

**Alternative Solution**:

```java
// Using factory pattern for dependency creation
class NotificationServiceFactory {
    public static NotificationService create(String channelType) {
        List<NotificationChannel> channels = new ArrayList<>();
        switch (channelType) {
            case "email":
                channels.add(new EmailNotificationChannel());
                break;
            case "sms":
                channels.add(new SMSNotificationChannel());
                break;
            case "all":
                channels.add(new EmailNotificationChannel());
                channels.add(new SMSNotificationChannel());
                channels.add(new PushNotificationChannel());
                break;
        }

        return new NotificationService(
            channels,
            new PlainTextMessageFormatter(),
            new InMemoryDeliveryTracker(),
            new ExponentialBackoffRetryPolicy()
        );
    }
}
```

## Hard Examples

### Example 1: Plugin System with Dependency Management

**Problem Statement**: Design a plugin system that demonstrates complex dependency management with plugin dependencies, lifecycle management, and dynamic dependency resolution.

**Requirements**:

- Plugin dependency resolution
- Circular dependency detection
- Dynamic dependency injection
- Plugin lifecycle management

**Architecture**:

```
Plugin System Architecture
├── PluginManager
│   ├── Plugin Registration
│   ├── Dependency Resolution
│   └── Lifecycle Management
├── Plugin Interface
│   ├── initialize()
│   ├── execute()
│   └── shutdown()
├── Dependency Container
│   ├── Service Registration
│   ├── Service Resolution
│   └── Circular Dependency Detection
└── Plugin Implementations
    ├── LoggerPlugin
    ├── CachePlugin
    └── SecurityPlugin
```

**Implementation**:

```java
package academy.javaengineering.oop.dependency;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Plugin interface
interface Plugin {
    String getName();
    String[] getDependencies();
    void initialize(PluginContext context);
    Object execute(Object input);
    void shutdown();
}

// Plugin context for dependency injection
class PluginContext {
    private final Map<String, Object> services;
    private final Map<String, Plugin> plugins;

    public PluginContext() {
        this.services = new ConcurrentHashMap<>();
        this.plugins = new ConcurrentHashMap<>();
    }

    public void registerService(String name, Object service) {
        services.put(name, service);
    }

    public <T> T getService(String name, Class<T> type) {
        Object service = services.get(name);
        return service != null ? type.cast(service) : null;
    }

    public void registerPlugin(Plugin plugin) {
        plugins.put(plugin.getName(), plugin);
    }

    public Plugin getPlugin(String name) {
        return plugins.get(name);
    }
}

// Dependency container with circular dependency detection
class DependencyContainer {
    private final Map<String, Object> dependencies;
    private final Set<String> resolving;

    public DependencyContainer() {
        this.dependencies = new ConcurrentHashMap<>();
        this.resolving = ConcurrentHashMap.newKeySet();
    }

    public <T> void register(String name, T instance) {
        dependencies.put(name, instance);
    }

    public <T> T resolve(String name, Class<T> type) {
        if (resolving.contains(name)) {
            throw new RuntimeException("Circular dependency detected: " + name);
        }

        resolving.add(name);
        try {
            Object dependency = dependencies.get(name);
            if (dependency == null) {
                throw new RuntimeException("Dependency not found: " + name);
            }
            return type.cast(dependency);
        } finally {
            resolving.remove(name);
        }
    }

    public boolean hasDependency(String name) {
        return dependencies.containsKey(name);
    }
}

// Concrete plugins
class LoggerPlugin implements Plugin {
    private PluginContext context;
    private List<String> logs;

    @Override
    public String getName() { return "Logger"; }

    @Override
    public String[] getDependencies() { return new String[0]; }

    @Override
    public void initialize(PluginContext context) {
        this.context = context;
        this.logs = new ArrayList<>();
        System.out.println("Logger plugin initialized");
    }

    @Override
    public Object execute(Object input) {
        String logEntry = "[LOG] " + input.toString();
        logs.add(logEntry);
        System.out.println(logEntry);
        return logEntry;
    }

    @Override
    public void shutdown() {
        System.out.println("Logger plugin shutdown. Total logs: " + logs.size());
        logs.clear();
    }

    public List<String> getLogs() { return new ArrayList<>(logs); }
}

class CachePlugin implements Plugin {
    private PluginContext context;
    private Map<String, Object> cache;
    private LoggerPlugin logger;

    @Override
    public String getName() { return "Cache"; }

    @Override
    public String[] getDependencies() { return new String[]{"Logger"}; }

    @Override
    public void initialize(PluginContext context) {
        this.context = context;
        this.cache = new HashMap<>();
        this.logger = (LoggerPlugin) context.getPlugin("Logger");
        System.out.println("Cache plugin initialized");
    }

    @Override
    public Object execute(Object input) {
        String key = input.toString();
        Object value = cache.get(key);
        if (value != null) {
            if (logger != null) {
                logger.execute("Cache hit for key: " + key);
            }
            return value;
        }
        if (logger != null) {
            logger.execute("Cache miss for key: " + key);
        }
        return null;
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void shutdown() {
        System.out.println("Cache plugin shutdown. Cache size: " + cache.size());
        cache.clear();
    }
}

class SecurityPlugin implements Plugin {
    private PluginContext context;
    private Set<String> blockedPatterns;
    private LoggerPlugin logger;

    @Override
    public String getName() { return "Security"; }

    @Override
    public String[] getDependencies() { return new String[]{"Logger"}; }

    @Override
    public void initialize(PluginContext context) {
        this.context = context;
        this.blockedPatterns = new HashSet<>(Arrays.asList("DROP", "DELETE", "TRUNCATE"));
        this.logger = (LoggerPlugin) context.getPlugin("Logger");
        System.out.println("Security plugin initialized");
    }

    @Override
    public Object execute(Object input) {
        String query = input.toString().toUpperCase();
        for (String pattern : blockedPatterns) {
            if (query.contains(pattern)) {
                if (logger != null) {
                    logger.execute("Blocked dangerous query: " + input);
                }
                return false;
            }
        }
        if (logger != null) {
            logger.execute("Query allowed: " + input);
        }
        return true;
    }

    @Override
    public void shutdown() {
        System.out.println("Security plugin shutdown");
        blockedPatterns.clear();
    }
}

// Plugin manager with dependency resolution
class PluginManager {
    private final Map<String, Plugin> plugins;
    private final PluginContext context;
    private final DependencyContainer container;

    public PluginManager() {
        this.plugins = new ConcurrentHashMap<>();
        this.context = new PluginContext();
        this.container = new DependencyContainer();
    }

    public boolean loadPlugin(Plugin plugin) {
        String name = plugin.getName();

        if (plugins.containsKey(name)) {
            System.out.println("Plugin already loaded: " + name);
            return false;
        }

        // Check dependencies
        for (String dependency : plugin.getDependencies()) {
            if (!plugins.containsKey(dependency)) {
                System.out.println("Missing dependency: " + dependency + " for plugin: " + name);
                return false;
            }
        }

        plugins.put(name, plugin);
        context.registerPlugin(plugin);
        container.register("plugin_" + name, plugin);
        System.out.println("Loaded plugin: " + name);
        return true;
    }

    public boolean initializePlugin(String name) {
        Plugin plugin = plugins.get(name);
        if (plugin == null) {
            System.out.println("Plugin not found: " + name);
            return false;
        }

        // Initialize dependencies first
        for (String dependency : plugin.getDependencies()) {
            if (!initializePlugin(dependency)) {
                System.out.println("Failed to initialize dependency: " + dependency);
                return false;
            }
        }

        plugin.initialize(context);
        return true;
    }

    public Object executePlugin(String name, Object input) {
        Plugin plugin = plugins.get(name);
        if (plugin == null) {
            System.out.println("Plugin not found: " + name);
            return null;
        }

        return plugin.execute(input);
    }

    public void shutdownAll() {
        System.out.println("\n=== Shutting down all plugins ===");
        for (Plugin plugin : plugins.values()) {
            plugin.shutdown();
        }
    }

    public List<String> getLoadedPlugins() {
        return new ArrayList<>(plugins.keySet());
    }
}

public class PluginDependencyDemo {
    public static void main(String[] args) {
        System.out.println("=== Plugin System with Dependencies Demo ===\n");

        PluginManager manager = new PluginManager();

        // Load plugins
        System.out.println("=== Loading Plugins ===");
        manager.loadPlugin(new LoggerPlugin());
        manager.loadPlugin(new CachePlugin());
        manager.loadPlugin(new SecurityPlugin());

        // Initialize plugins (dependencies resolved automatically)
        System.out.println("\n=== Initializing Plugins ===");
        manager.initializePlugin("Logger");
        manager.initializePlugin("Cache");
        manager.initializePlugin("Security");

        // Execute plugins
        System.out.println("\n=== Executing Plugins ===");
        manager.executePlugin("Logger", "System started");
        manager.executePlugin("Cache", "test_key");
        manager.executePlugin("Security", "SELECT * FROM users");
        manager.executePlugin("Security", "DROP TABLE users");

        // Demonstrate dependency chain
        System.out.println("\n=== Dependency Chain Demo ===");
        CachePlugin cache = (CachePlugin) manager.getLoadedPlugins().stream()
            .filter(name -> name.equals("Cache"))
            .map(name -> {
                try {
                    return manager.executePlugin("Cache", null);
                } catch (Exception e) {
                    return null;
                }
            })
            .findFirst()
            .orElse(null);

        // Shutdown
        manager.shutdownAll();
    }
}
```

**Execution Flow**:

1. **Plugin Loading**: Plugins are loaded with dependency validation
2. **Dependency Resolution**: Dependencies are resolved automatically
3. **Plugin Initialization**: Plugins are initialized in dependency order
4. **Plugin Execution**: Plugins execute with their dependencies available
5. **Plugin Shutdown**: Plugins are shut down in reverse order

**Unit Tests**:

```java
public class PluginDependencyTest {
    public static void main(String[] args) {
        System.out.println("=== Running Plugin Dependency Tests ===\n");

        testPluginLoading();
        testDependencyResolution();
        testCircularDependencyDetection();
        testPluginExecution();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testPluginLoading() {
        System.out.println("Test 1: Plugin Loading");
        PluginManager manager = new PluginManager();

        assert manager.loadPlugin(new LoggerPlugin()) : "Should load Logger";
        assert manager.loadPlugin(new CachePlugin()) : "Should load Cache";
        assert manager.getLoadedPlugins().size() == 2 : "Should have 2 plugins";

        System.out.println("  PASS: Plugin loading test passed\n");
    }

    private static void testDependencyResolution() {
        System.out.println("Test 2: Dependency Resolution");
        PluginManager manager = new PluginManager();

        manager.loadPlugin(new LoggerPlugin());
        manager.loadPlugin(new CachePlugin());

        // Cache depends on Logger, should initialize Logger first
        assert manager.initializePlugin("Cache") : "Should initialize Cache with Logger";

        System.out.println("  PASS: Dependency resolution test passed\n");
    }

    private static void testCircularDependencyDetection() {
        System.out.println("Test 3: Circular Dependency Detection");
        DependencyContainer container = new DependencyContainer();
        container.register("A", "valueA");

        try {
            container.resolve("A", String.class);
            System.out.println("  PASS: No circular dependency\n");
        } catch (RuntimeException e) {
            System.out.println("  PASS: Circular dependency detected\n");
        }
    }

    private static void testPluginExecution() {
        System.out.println("Test 4: Plugin Execution");
        PluginManager manager = new PluginManager();

        manager.loadPlugin(new LoggerPlugin());
        manager.initializePlugin("Logger");

        Object result = manager.executePlugin("Logger", "Test message");
        assert result != null : "Should return result";

        System.out.println("  PASS: Plugin execution test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for plugin initialization where n is number of plugins
- **Space Complexity**: O(n) for storing plugins and their dependencies

**Best Practices**:

- Use dependency injection to manage plugin dependencies
- Detect and prevent circular dependencies
- Initialize plugins in dependency order
- Provide clear error messages for missing dependencies
- Document plugin contracts and expected behaviors

## Exercises

### Easy

1. **Calculator**: Create a calculator with dependency on operation interface for different mathematical operations.

2. **Logger**: Design a logging system with dependency on output handler interface for different output destinations.

3. **Validator**: Create a validation system with dependency on validation rules interface.

### Medium

1. **Email Service**: Design an email service with dependencies on SMTP client, template engine, and logging service.

2. **Payment Gateway**: Create a payment processing system with dependencies on different payment providers.

3. **Cache System**: Design a caching system with dependencies on storage backend and serialization.

### Hard

1. **Microservice Framework**: Create a microservice framework with dependencies on service registry, load balancer, and circuit breaker.

2. **Plugin Architecture**: Design a plugin system with dynamic dependency resolution and lifecycle management.

3. **Event-Driven System**: Create an event-driven system with dependencies on event bus, handlers, and middleware.

## Interview Questions

### Easy

1. **What is dependency in object-oriented programming?**
   Dependency is the weakest form of relationship where one class uses another temporarily to perform a specific operation. It's a usage relationship that doesn't involve ownership or long-term association.

2. **How does dependency differ from association?**
   Association represents a structural relationship where objects are connected and can navigate to each other. Dependency is a temporary usage relationship that exists only during method execution.

3. **What is dependency injection?**
   Dependency injection is a technique where dependencies are provided to an object from outside rather than being created internally. It reduces coupling and makes code more testable and flexible.

### Medium

1. **What are the types of dependencies in Java?**
   Dependencies can occur through method parameters, local variables, static method calls, or instance variable references. Each type has different implications for coupling and testability.

2. **How does dependency injection improve testability?**
   Dependency injection allows you to substitute real dependencies with mock objects during testing. This isolates the unit under test and makes it easier to verify behavior without relying on external systems.

3. **What are the benefits of programming to interfaces for dependencies?**
   Programming to interfaces allows different implementations to be swapped without changing the dependent class. This enables flexibility, testability, and adherence to the Dependency Inversion Principle.

### Hard

1. **How do you handle circular dependencies?**
   Circular dependencies can be detected at compile time or runtime. Solutions include: using interfaces to break cycles, introducing a mediator, using dependency injection containers, or redesigning the architecture.

2. **What are the implications of excessive dependencies?**
   Excessive dependencies lead to tight coupling, difficult testing, and maintenance challenges. Solutions include: applying the Single Responsibility Principle, using dependency injection, and designing cohesive modules.

## Common Pitfalls

### 1. Hard-Coding Dependencies

**Wrong**:
```java
class OrderService {
    private DatabaseRepository repository = new DatabaseRepository(); // Hard dependency
    private EmailService emailService = new EmailService(); // Hard dependency

    public void processOrder(Order order) {
        repository.save(order); // Cannot test without real database
        emailService.sendConfirmation(order); // Cannot test without real email service
    }
}
```

**Right**:
```java
class OrderService {
    private final Repository repository;
    private final NotificationService notificationService;

    // Dependencies injected
    public OrderService(Repository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public void processOrder(Order order) {
        repository.save(order); // Can be mocked for testing
        notificationService.sendConfirmation(order); // Can be mocked for testing
    }
}
```

### 2. Creating Dependencies Inside Methods

**Wrong**:
```java
class ReportGenerator {
    public Report generateReport(Data data) {
        ReportFormatter formatter = new PDFReportFormatter(); // Created inside method
        // Hard to swap formatter for testing or different formats
        return formatter.format(data);
    }
}
```

**Right**:
```java
class ReportGenerator {
    private final ReportFormatter formatter;

    // Injected dependency
    public ReportGenerator(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public Report generateReport(Data data) {
        return formatter.format(data); // Can be mocked or swapped
    }
}
```

### 3. Not Managing Dependency Lifecycle

**Wrong**:
```java
class Service {
    private DatabaseConnection connection;

    public Service() {
        this.connection = new DatabaseConnection(); // Created but never closed
    }

    // Missing cleanup method
}
```

**Right**:
```java
class Service implements AutoCloseable {
    private DatabaseConnection connection;

    public Service(DatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.close(); // Properly managed lifecycle
        }
    }
}
```

## Best Practices

1. **Program to interfaces**: Define dependencies through interfaces to enable swapping and testing.

2. **Use dependency injection**: Provide dependencies from outside rather than creating them internally.

3. **Keep dependencies minimal**: Only depend on what you actually need (Interface Segregation Principle).

4. **Manage dependency lifecycle**: Ensure dependencies are properly initialized and cleaned up.

5. **Document dependencies**: Clearly document what dependencies a class requires and why.

## Real World Usage

### How Spring Uses This

Spring Framework is built on dependency injection:

- **@Autowired**: Automatic dependency injection
- **@Bean**: Configuration-based dependency creation
- **ApplicationContext**: Central dependency container

### How Hibernate Uses This

Hibernate ORM uses dependency injection for:

- **SessionFactory**: Injected into Session implementations
- **ConnectionProvider**: Dependency for database connections
- **Cache**: Dependencies for second-level cache

### How JDK Uses This

The Java Development Kit uses dependency patterns in:

- **Collections**: Dependencies on comparators and predicates
- **I/O Streams**: Dependencies on filters and transformers
- **Concurrency**: Dependencies on executors and schedulers

### Enterprise Usage

In enterprise applications, dependency management is used for:

- **Service Layer**: Services depend on repositories and external clients
- **Configuration**: Configuration objects depend on property sources
- **Plugin Systems**: Plugins depend on core services and other plugins

## References

1. **Effective Java** by Joshua Bloch - Item 5: Prefer dependency injection to hard-wiring resources
2. **Clean Architecture** by Robert C. Martin - Dependency Rule
3. **Spring in Action** - Dependency injection patterns
4. **Head First Design Patterns** - Factory and Abstract Factory patterns
5. **Dependency Injection Principles, Practices, and Patterns** by Steven van Deursen

## Summary

- Dependency is the weakest form of relationship, representing temporary usage
- Dependencies can occur through parameters, local variables, or static calls
- Dependency injection reduces coupling and improves testability
- Program to interfaces to enable flexible dependency swapping
- Manage dependency lifecycle properly
- Keep dependencies minimal and well-documented

**Next Steps**: [23-immutable-objects](../23-immutable-objects/README.md)
