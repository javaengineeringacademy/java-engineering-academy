# Mini Banking System

A console-based banking application demonstrating object-oriented programming, file I/O, and exception handling in Python.

## Features

- Create bank accounts with unique account numbers
- Deposit and withdraw funds
- Transfer money between accounts
- View account balance and transaction history
- Persistent storage using JSON files
- Input validation and error handling

## Architecture

```
mini-banking/
├── models.py      # Account and Transaction data classes
├── services.py    # Banking business logic
├── storage.py     # JSON file persistence
├── main.py        # CLI interface
└── test_banking.py # Unit tests
```

## Learning Objectives

- Object-oriented design with classes and dataclasses
- File I/O with JSON serialization
- Exception handling and validation
- Unit testing with pytest
- CLI application structure

## How to Run

```bash
# Run the application
python main.py

# Run tests
python -m pytest test_banking.py -v
```
