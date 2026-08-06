# Inventory Management System

## Project Overview

An enterprise-grade Inventory Management System that handles products, warehouses, stock tracking, orders, and comprehensive reporting. This advanced project introduces design patterns like Repository, Unit of Work, and Specification pattern. Students will build a scalable system that demonstrates proper layering, abstraction, and extensibility.

## Learning Outcomes

- Implement Repository pattern for data access abstraction
- Use Specification pattern for complex queries
- Apply Unit of Work pattern for transaction management
- Implement the Observer pattern for stock alerts
- Design for scalability and performance
- Use interfaces extensively for loose coupling
- Implement proper audit logging

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Manage products with categories and attributes | Must |
| FR02 | Multi-warehouse support with location tracking | Must |
| FR03 | Stock management with min/max levels | Must |
| FR04 | Purchase order creation and tracking | Must |
| FR05 | Sales order processing with inventory deduction | Must |
| FR06 | Low stock alerts and notifications | Must |
| FR07 | Inventory reports (value, movement, aging) | Should |
| FR08 | Product search with multiple criteria | Should |
| FR09 | Batch/lot tracking | Could |
| FR10 | Barcode/QR code integration | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Handle 100,000+ products efficiently |
| NFR02 | Real-time stock updates |
| NFR03 | Audit trail for all operations |
| NFR04 | Support concurrent operations |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        API[APIController.java]
    end
    
    subgraph Service Layer
        ProductService[Product Service]
        StockService[Stock Service]
        OrderService[Order Service]
        ReportService[Report Service]
    end
    
    subgraph Core Components
        ProductManager[Product Manager]
        StockManager[Stock Manager]
        OrderManager[Order Manager]
    end
    
    subgraph Patterns
        Repository[Repository Pattern]
        Specification[Specification Pattern]
        UnitOfWork[Unit of Work]
        Observer[Observer Pattern]
    end
    
    subgraph Storage
        ProductDB[(Product DB)]
        StockDB[(Stock DB)]
        OrderDB[(Order DB)]
        WarehouseDB[(Warehouse DB)]
    end
    
    Main --> API
    API --> ProductService
    API --> StockService
    API --> OrderService
    ProductService --> ProductManager
    StockService --> StockManager
    OrderService --> OrderManager
    ProductManager --> Repository
    StockService --> Specification
    OrderService --> UnitOfWork
    StockService --> Observer
```

## Package Structure

```
inventory-management/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── inventory/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Product.java
│                       │   ├── Warehouse.java
│                       │   ├── Stock.java
│                       │   ├── PurchaseOrder.java
│                       │   ├── SalesOrder.java
│                       │   ├── OrderItem.java
│                       │   └── enums/
│                       │       ├── ProductStatus.java
│                       │       ├── OrderStatus.java
│                       │       ├── StockStatus.java
│                       │       └── MovementType.java
│                       ├── repository/
│                       │   ├── Repository.java
│                       │   ├── ProductRepository.java
│                       │   ├── StockRepository.java
│                       │   └── OrderRepository.java
│                       ├── specification/
│                       │   ├── Specification.java
│                       │   ├── ByCategorySpec.java
│                       │   ├── PriceRangeSpec.java
│                       │   └── StockLevelSpec.java
│                       ├── pattern/
│                       │   ├── UnitOfWork.java
│                       │   ├── EventBus.java
│                       │   └── EventListener.java
│                       ├── service/
│                       │   ├── ProductService.java
│                       │   ├── StockService.java
│                       │   ├── OrderService.java
│                       │   └── ReportService.java
│                       ├── audit/
│                       │   ├── AuditLogger.java
│                       │   └── AuditEntry.java
│                       └── exception/
│                           ├── ProductNotFoundException.java
│                           ├── InsufficientStockException.java
│                           ├── OrderException.java
│                           └── ValidationException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── inventory/
                        ├── ProductServiceTest.java
                        ├── StockServiceTest.java
                        ├── SpecificationTest.java
                        └── OrderServiceTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Product {
        -String productId
        -String name
        -String description
        -String category
        -BigDecimal price
        -ProductStatus status
        -Map~String,String~ attributes
        +Product(id, name, category, price)
        +getProductId() String
        +getPrice() BigDecimal
        +updatePrice(BigDecimal) void
    }
    
    class Warehouse {
        -String warehouseId
        -String name
        -String location
        -int capacity
        +Warehouse(id, name, location)
        +getWarehouseId() String
        +getLocation() String
    }
    
    class Stock {
        -String stockId
        -Product product
        -Warehouse warehouse
        -int quantity
        -int minimumLevel
        -int maximumLevel
        -LocalDateTime lastUpdated
        +Stock(product, warehouse, quantity)
        +getQuantity() int
        +isLowStock() boolean
        +reduce(int quantity) void
        +add(int quantity) void
    }
    
    class PurchaseOrder {
        -String orderId
        -String supplierId
        -List~OrderItem~ items
        -OrderStatus status
        -BigDecimal totalAmount
        -LocalDateTime orderDate
        +PurchaseOrder(id, supplierId, items)
        +getStatus() OrderStatus
        +updateStatus(OrderStatus) void
    }
    
    class SalesOrder {
        -String orderId
        -String customerId
        -List~OrderItem~ items
        -OrderStatus status
        -BigDecimal totalAmount
        -LocalDateTime orderDate
        +SalesOrder(id, customerId, items)
        +getStatus() OrderStatus
        +process() void
    }
    
    class Repository~T~ {
        <<interface>>
        +save(T) T
        +findById(String) Optional~T~
        +findAll() List~T~
        +delete(String) boolean
        +update(T) T
    }
    
    class Specification~T~ {
        <<interface>>
        +isSatisfiedBy(T) boolean
        +and(Specification) Specification
        +or(Specification) Specification
        +not() Specification
    }
    
    class UnitOfWork {
        -List~Runnable~ newEntities
        -List~Runnable~ modifiedEntities
        -List~Runnable~ deletedEntities
        -EventBus eventBus
        +registerNew(Runnable) void
        +registerModified(Runnable) void
        +registerDeleted(Runnable) void
        +commit() void
        +rollback() void
    }
    
    class EventBus {
        -Map~String,List~EventListener~~ listeners
        +subscribe(String, EventListener) void
        +publish(String, Object) void
    }
    
    Product --> ProductStatus
    Stock --> Product
    Stock --> Warehouse
    PurchaseOrder --> OrderItem
    PurchaseOrder --> OrderStatus
    SalesOrder --> OrderItem
    SalesOrder --> OrderStatus
    Repository <|.. ProductRepository
    Repository <|.. StockRepository
    Specification <|.. ByCategorySpec
    Specification <|.. PriceRangeSpec
    UnitOfWork --> EventBus
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**