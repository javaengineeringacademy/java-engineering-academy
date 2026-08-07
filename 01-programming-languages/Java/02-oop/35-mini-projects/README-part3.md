# Mini Projects — Part 3: Enterprise Projects (11-14)

**[← Part 2: Advanced Projects (6-10)](README-part2.md)**

---

## Project 11: Order Processing

### Description

A complete order processing system with inventory, payment, shipping, and notification.

### Requirements

**Functional Requirements**:
- Product catalog with inventory tracking
- Shopping cart and checkout flow
- Payment processing (multiple methods)
- Order tracking and status updates
- Shipping calculation
- Email/SMS notifications

**Non-Functional Requirements**:
- Transaction integrity for orders
- Inventory atomic operations
- Payment gateway integration (mock)
- Order state management
- SOLID principle compliance

### Architecture

```
┌────────────────────────────────────────────────────┐
│              OrderProcessingSystem                  │
├────────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐│
│  │ Product  │  │   Cart   │  │     Order        ││
│  │ Catalog  │  │          │  │ (state machine)  ││
│  └──────────┘  └──────────┘  └──────────────────┘│
│                                                    │
│  ┌────────────────────────────────────────────┐   │
│  │  OrderService (SRP)                        │   │
│  │  ├─ PaymentService (DIP)                   │   │
│  │  │  ├─ CreditCardPayment                   │   │
│  │  │  ├─ PayPalPayment                       │   │
│  │  │  └─ BankTransferPayment                 │   │
│  │  ├─ ShippingService (DIP)                  │   │
│  │  │  ├─ StandardShipping                    │   │
│  │  │  ├─ ExpressShipping                     │   │
│  │  │  └─ OvernightShipping                   │   │
│  │  ├─ NotificationService (DIP)              │   │
│  │  │  ├─ EmailNotification                   │   │
│  │  │  └─ SMSNotification                     │   │
│  │  └─ InventoryService (SRP)                 │   │
│  └────────────────────────────────────────────┘   │
├────────────────────────────────────────────────────┤
│  OrderRepository  ProductRepository                │
│  EventPublisher   AuditLog                         │
└────────────────────────────────────────────────────┘
```

### Learning Outcomes

- Apply all SOLID principles in a real system
- Implement the Strategy pattern for payment and shipping
- Use the Observer pattern for notifications
- Practice transaction management
- Design clean service layer architecture

---

## Project 12: Payment Gateway

### Description

A payment gateway system processing transactions with fraud detection and reconciliation.

### Requirements

**Functional Requirements**:
- Process payments (credit card, debit card, UPI, wallet)
- Transaction verification and validation
- Fraud detection rules
- Refund processing
- Transaction reconciliation
- Merchant settlement

**Non-Functional Requirements**:
- Security (encryption, tokenization)
- Idempotency for duplicate requests
- Detailed error handling
- Audit logging
- Rate limiting

### Architecture

```
┌──────────────────────────────────────────────────────┐
│                PaymentGateway                         │
├──────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────┐   │
│  │  PaymentProcessor (Facade)                   │   │
│  │  ├─ PaymentValidator                         │   │
│  │  ├─ FraudDetector                            │   │
│  │  ├─ PaymentRouter (Strategy)                 │   │
│  │  │  ├─ CreditCardProcessor                   │   │
│  │  │  ├─ UPIProcessor                          │   │
│  │  │  └─ WalletProcessor                       │   │
│  │  └─ TransactionLogger                        │   │
│  └──────────────────────────────────────────────┘   │
│                                                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  SecurityService                             │   │
│  │  - encrypt(plaintext) → ciphertext           │   │
│  │  - tokenize(cardNumber) → token              │   │
│  │  - validateSignature(payload, signature)     │   │
│  └──────────────────────────────────────────────┘   │
│                                                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  ReconciliationService                       │   │
│  │  - reconcile(SettlementDate)                 │   │
│  │  - identifyDiscrepancies()                   │   │
│  └──────────────────────────────────────────────┘   │
├──────────────────────────────────────────────────────┤
│  TransactionRepository   MerchantRepository          │
│  FraudRuleEngine         AuditService                │
└──────────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement security patterns (tokenization, encryption)
- Design the Facade pattern for complex subsystems
- Use the Strategy pattern for payment routing
- Practice detailed error handling
- Implement audit logging

---

## Project 13: E-Commerce

### Description

A full e-commerce platform with product catalog, shopping cart, checkout, and order management.

### Requirements

**Functional Requirements**:
- Product catalog with categories and search
- Shopping cart with quantity management
- Multi-step checkout (address, payment, confirmation)
- Order history and tracking
- User reviews and ratings
- Discount and coupon system

**Non-Functional Requirements**:
- Microservice-ready architecture
- Scalable product search
- Cart persistence across sessions
- Order state management
- Performance optimization

### Architecture

```
┌──────────────────────────────────────────────────────────┐
│                   ECommercePlatform                       │
├──────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌──────────┐  ┌────────────────────┐ │
│  │   Product   │  │   Cart   │  │      Order         │ │
│  │   Catalog   │  │ Service  │  │   Service          │ │
│  └──────┬──────┘  └────┬─────┘  └────────┬───────────┘ │
│         │              │                  │              │
│  ┌──────▼──────┐  ┌────▼─────┐  ┌────────▼───────────┐ │
│  │ SearchIndex │  │ CartItem │  │ OrderProcessor     │ │
│  │ (inverted   │  │          │  │ ├─ PaymentService   │ │
│  │  index)     │  │          │  │ ├─ ShippingService  │ │
│  └─────────────┘  └──────────┘  │ └─ InventoryService│ │
│                                  └────────────────────┘ │
│                                                          │
│  ┌──────────────────────────────────────────────┐       │
│  │  CouponService (Strategy)                    │       │
│  │  ├─ PercentageDiscount                       │       │
│  │  ├─ FixedAmountDiscount                      │       │
│  │  └─ BuyOneGetOneFree                         │       │
│  └──────────────────────────────────────────────┘       │
├──────────────────────────────────────────────────────────┤
│  ProductService  CartService  OrderService               │
│  UserService     SearchService  CouponService            │
└──────────────────────────────────────────────────────────┘
```

### Learning Outcomes

- Design microservice-ready architecture
- Implement search indexing algorithms
- Use the Strategy pattern for discounts
- Practice complex state management
- Design scalable service interfaces

---

## Project 14: Trading Platform

### Description

A stock trading platform with real-time price updates, order matching, and portfolio management.

### Requirements

**Functional Requirements**:
- Stock listing with real-time price simulation
- Market and limit order placement
- Order matching engine (price-time priority)
- Portfolio management with P&L calculation
- Trade history and reporting
- Watchlist management

**Non-Functional Requirements**:
- High-performance order matching
- Thread-safe concurrent access
- Event-driven architecture
- Order book data structure
- Performance optimization

### Architecture

```
┌────────────────────────────────────────────────────────────┐
│                  TradingPlatform                            │
├────────────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────────────┐ │
│  │  OrderBook (concurrent data structure)               │ │
│  │  ├─ BidSide (max-heap by price, FIFO by time)       │ │
│  │  └─ AskSide (min-heap by price, FIFO by time)       │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │  MatchingEngine (Observer pattern)                   │ │
│  │  - matchOrders(Order) → List<Trade>                  │ │
│  │  - notifyListeners(Trade)                            │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │  Portfolio                                           │ │
│  │  ├─ positions: Map<Stock, Position>                  │ │
│  │  ├─ calculatePnL()                                   │ │
│  │  └─ getUnrealizedGains()                             │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │  PriceFeed (simulated)                               │ │
│  │  - subscribe(Stock, PriceListener)                   │ │
│  │  - startSimulation()                                 │ │
│  └──────────────────────────────────────────────────────┘ │
├────────────────────────────────────────────────────────────┤
│  OrderRepository  TradeRepository  PortfolioRepository     │
│  RiskService      SettlementService                       │
└────────────────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement high-performance data structures (order book)
- Design thread-safe concurrent systems
- Use the Observer pattern for price feeds
- Practice event-driven architecture
- Implement matching algorithms

---

## Project Template

Each project follows this structure:

```
project-name/
├── README.md              # Requirements, architecture, setup
├── docs/
│   ├── requirements.md    # Functional requirements
│   ├── architecture.md    # System design
│   └── class-diagram.md   # UML diagrams
├── src/
│   └── main/java/         # Implementation
├── test/
│   └── test/java/         # Unit tests
└── solutions/             # Reference implementation
```

## Getting Started

1. Choose a project based on your current skill level
2. Read the requirements thoroughly
3. Design the architecture before coding
4. Implement incrementally
5. Write tests alongside implementation
6. Compare with the reference solution

## Tips

- **Don't look at the solution first** — struggle builds understanding
- **Start with the simplest version** — add complexity incrementally
- **Write tests** — they catch bugs early
- **Refactor regularly** — clean code is maintainable code
- **Document your decisions** — future you will thank present you
- **Apply SOLID principles** — each project is an opportunity to practice
- **Use design patterns** — they provide proven solutions to common problems

## References

- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Clean Architecture by Robert C. Martin](https://www.oreilly.com/library/view/clean-architecture/9780134494166/)
- [Refactoring Guru](https://refactoring.guru/)
- [Baeldung Design Patterns](https://www.baeldung.com/learn-java-design-patterns)