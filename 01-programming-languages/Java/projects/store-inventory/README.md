# Store Inventory Management System

## Overview
A complete inventory management system for retail stores. Manages products, stock levels, movements, and generates reports. This project demonstrates OOP, design patterns, and data persistence.

## Features
- Product management (CRUD)
- Category management
- Stock tracking
- Stock movements (in/out/transfer)
- Low stock alerts
- Inventory reports
- File-based persistence

## Architecture
- InventoryManager: Business logic orchestrator
- Product: Product entity with attributes
- Category: Product categorization
- StockMovement: Tracks stock changes
- InventoryReport: Report generation
- InventoryRepository: Data access interface
- FileInventoryRepository: File-based implementation

## Learning Objectives
- OOP design (encapsulation, inheritance, polymorphism)
- Repository pattern
- SOLID principles
- File I/O for persistence
- Business logic implementation

## How to Run
```bash
javac src/*.java src/persistence/*.java
java -cp src InventoryManager
```

## Production Notes
- In production, use a database (JPA/Hibernate)
- Add transaction management
- Implement audit logging