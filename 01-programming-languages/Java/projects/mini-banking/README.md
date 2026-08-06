# Mini Banking System

A detailed Java project demonstrating core Java concepts through a realistic banking domain.

## Topics Covered

| Concept | Where Used |
|---------|-----------|
| OOP (Inheritance, Polymorphism) | `Account`, `Transaction`, `TransactionType` |
| Collections (HashMap, ArrayList) | `InMemoryAccountRepository` |
| Generics | `AccountRepository<T>` |
| Streams | `ReportingService`, `TransactionService` |
| Concurrency | `ConcurrentAccountManager`, `ConcurrentHashMap` |
| Exception Handling | Custom exceptions in `exception/` |
| Logging (SLF4J) | `AuditLogger` with MDC |
| Testing (JUnit 5, Mockito) | `test/` package |

## Project Structure

```
src/main/java/academy/javaengineering/minibanking/
├── Main.java
├── model/
│   ├── Account.java
│   ├── Transaction.java
│   └── TransactionType.java
├── exception/
│   ├── InsufficientFundsException.java
│   ├── AccountNotFoundException.java
│   └── InvalidAmountException.java
├── repository/
│   ├── AccountRepository.java
│   └── InMemoryAccountRepository.java
├── service/
│   ├── AccountService.java
│   ├── TransactionService.java
│   └── ReportingService.java
├── concurrent/
│   └── ConcurrentAccountManager.java
└── logging/
    └── AuditLogger.java

src/test/java/academy/javaengineering/minibanking/
├── AccountServiceTest.java
└── TransactionServiceTest.java
```

## Setup

### Prerequisites
- Java 17+
- Maven 3.8+

### Build & Run
```bash
cd mini-banking
mvn clean compile exec:java -Dexec.mainClass="academy.javaengineering.minibanking.Main"
```

### Run Tests
```bash
mvn test
```

## Design Decisions

1. **Immutable-ish Accounts**: No setters prevents accidental state mutation; balance changes via explicit `deposit()`/`withdraw()` methods enforce business rules.

2. **ConcurrentHashMap**: Thread-safe map for repository eliminates need for external synchronization on reads.

3. **Generic Repository**: `AccountRepository<T>` demonstrates type safety and enables future different entity repositories.

4. **Custom Exceptions**: Domain-specific exceptions (`InsufficientFundsException`, etc.) provide clearer error handling than generic exceptions.

5. **SLF4J + Logback**: Facade pattern allows switching logging implementations without code changes.

6. **Streams for Reporting**: Declarative data processing makes reporting logic concise and readable.
