# Bank Management System

## Overview

A comprehensive banking system demonstrating all OOP concepts learned in Sprint 2.

## OOP Concepts Demonstrated

| Concept | Implementation |
|---------|----------------|
| Encapsulation | Private fields, getters/setters, validation |
| Inheritance | Account hierarchy (Savings, Checking, Business) |
| Polymorphism | Different account behaviors, method overriding |
| Abstraction | Abstract Account class, InterestBearing interface |
| Interfaces | InterestBearing, TransactionProcessor |
| Composition | Bank HAS Accounts, Account HAS Transactions |
| Records | Transaction record |
| Enums | TransactionType enum |
| SOLID | Single Responsibility, Open/Closed, Liskov Substitution |

## Project Structure

```
project/
├── src/main/java/academy/javaengineering/oop/bank/project/
│   ├── BankApplication.java      # Main entry point
│   ├── Bank.java                 # Bank management
│   ├── Account.java              # Abstract base class
│   ├── SavingsAccount.java       # Savings with interest
│   ├── CheckingAccount.java      # Checking with overdraft
│   ├── BusinessAccount.java      # Business with fees
│   ├── Customer.java             # Customer entity
│   ├── Transaction.java          # Transaction record
│   ├── TransactionType.java      # Transaction type enum
│   ├── TransactionLogger.java    # Logger utility
│   └── InterestBearing.java      # Interest interface
├── src/test/java/academy/javaengineering/oop/bank/project/
│   ├── BankTest.java             # Bank tests
│   ├── AccountTest.java          # Account tests
│   └── CustomerTest.java         # Customer tests
└── pom.xml
```

## How to Run

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Run application
mvn exec:java -Dexec.mainClass="academy.javaengineering.oop.bank.project.BankApplication"
```

## Design Patterns Used

- **Template Method**: Account defines algorithm, subclasses override steps
- **Strategy**: Different interest/fee calculation strategies
- **Factory Method**: Bank creates different account types
- **Observer**: TransactionLogger observes all transactions

## Future Enhancements

- [ ] Add database persistence
- [ ] Implement REST API
- [ ] Add user authentication
- [ ] Create web frontend
- [ ] Add more account types (Investment, Loan)
- [ ] Implement transfer limits
- [ ] Add statement generation
