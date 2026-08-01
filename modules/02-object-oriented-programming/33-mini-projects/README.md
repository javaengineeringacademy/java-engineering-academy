# Mini Projects

## Overview

Apply all OOP concepts through 14 progressive projects organized by difficulty level. Each project reinforces specific OOP principles and provides hands-on experience with real-world scenarios. Projects are designed to be completed incrementally — start with the simplest version and add complexity as you master each concept.

## Difficulty Levels

### Beginner (1-2 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 1 | [Student Management](#project-1-student-management) | Classes, objects, encapsulation, methods | 1 week |
| 2 | [Library Management](#project-2-library-management) | Inheritance, polymorphism, composition | 1 week |

### Intermediate (2-3 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 3 | [Employee Management](#project-3-employee-management) | Abstract classes, interfaces, SOLID | 2 weeks |
| 4 | [Bank Management](#project-4-bank-management) | All OOP concepts, design patterns | 2 weeks |
| 5 | [Hospital Management](#project-5-hospital-management) | Complex relationships, state management | 3 weeks |

### Advanced (3-4 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 6 | [Inventory Management](#project-6-inventory-management) | Collections, generics, file I/O | 3 weeks |
| 7 | [Parking System](#project-7-parking-system) | Design patterns, concurrency basics | 3 weeks |
| 8 | [Food Delivery](#project-8-food-delivery) | API design, event handling | 4 weeks |
| 9 | [Movie Booking](#project-9-movie-booking) | Concurrent access, booking logic | 3 weeks |
| 10 | [Ride Sharing](#project-10-ride-sharing) | Matching algorithms, geolocation | 4 weeks |

### Enterprise (4-6 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 11 | [Order Processing](#project-11-order-processing) | Full stack, persistence, transactions | 4 weeks |
| 12 | [Payment Gateway](#project-12-payment-gateway) | Security, API design, error handling | 5 weeks |
| 13 | [E-Commerce](#project-13-e-commerce) | Microservices, scalability | 6 weeks |
| 14 | [Trading Platform](#project-14-trading-platform) | Performance, concurrency, reliability | 6 weeks |

---

## Project 1: Student Management

### Description

A console-based application to manage student records — add, update, delete, search, and display students.

### Requirements

**Functional Requirements**:
- Add a new student (ID, name, age, grade, email)
- Update student information by ID
- Delete a student by ID
- Search students by name or grade
- Display all students sorted by name or grade
- Calculate class average grade
- Display honor roll students (GPA >= 3.5)

**Non-Functional Requirements**:
- Data validation (age 5-100, valid email format, grade A-F)
- Persistent storage using file I/O
- Clean console output with formatting
- Graceful error handling

### Architecture

```
┌──────────────────────────────────┐
│         Main (entry point)       │
├──────────────────────────────────┤
│       StudentManager             │
│  - addStudent(Student)           │
│  - updateStudent(id, Student)    │
│  - deleteStudent(id)             │
│  - searchByName(name)            │
│  - searchByGrade(grade)          │
│  - getAllStudents()              │
│  - getClassAverage()             │
│  - getHonorRoll()               │
├──────────────────────────────────┤
│         Student (model)          │
│  - id, name, age, grade, email   │
│  - getGPA()                      │
│  - isHonorRoll()                 │
├──────────────────────────────────┤
│       StudentRepository          │
│  - save(List<Student>)           │
│  - load() → List<Student>        │
└──────────────────────────────────┘
```

### Learning Outcomes

- Understand classes and objects (creating `Student` instances)
- Practice encapsulation (private fields with getters/setters)
- Apply method design (single responsibility methods)
- Implement file I/O for persistence
- Practice input validation and error handling

### Implementation Checklist

- [ ] Create `Student` class with fields, constructor, getters, setters
- [ ] Create `StudentManager` class with CRUD operations
- [ ] Create `StudentRepository` for file persistence
- [ ] Create `Main` class with console menu
- [ ] Implement data validation
- [ ] Add search functionality
- [ ] Add sorting capabilities
- [ ] Add statistics (average, honor roll)
- [ ] Write unit tests
- [ ] Add error handling for file operations

---

## Project 2: Library Management

### Description

A library management system supporting books, members, and borrow/return operations with different member types.

### Requirements

**Functional Requirements**:
- Add/update/remove books (title, author, ISBN, genre, copies)
- Register members (Regular, Premium, Student)
- Borrow books (different borrowing limits per member type)
- Return books with fine calculation for overdue
- Search books by title, author, or genre
- View borrowing history
- Reserve books

**Non-Functional Requirements**:
- Polymorphic behavior for different member types
- Fine calculation based on days overdue
- Book availability tracking
- Member borrowing limits

### Architecture

```
┌─────────────────────────────────────────────┐
│              LibrarySystem                   │
├─────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │   Book   │  │  Member  │  │ Transaction│ │
│  └────┬─────┘  └────┬─────┘  └──────────┘  │
│       │             │                       │
│  ┌────▼─────┐  ┌────▼─────────────────┐    │
│  │  Genre   │  │ AbstractMember        │    │
│  │ (enum)   │  │ ├─ RegularMember      │    │
│  └──────────┘  │ ├─ PremiumMember      │    │
│                │ └─ StudentMember      │    │
│                └─────────────────────────┘  │
├─────────────────────────────────────────────┤
│         BookRepository                      │
│         MemberRepository                    │
│         TransactionRepository               │
└─────────────────────────────────────────────┘
```

### Learning Outcomes

- Apply inheritance and polymorphism (different member types)
- Use abstract classes and interfaces
- Implement the Strategy pattern for fine calculation
- Practice composition (Library HAS-A list of Books)
- Understand method overriding

### Implementation Checklist

- [ ] Create `Book` class with ISBN validation
- [ ] Create abstract `Member` class with `Borrowable` interface
- [ ] Implement `RegularMember`, `PremiumMember`, `StudentMember`
- [ ] Create `Transaction` class (borrow date, due date, return date)
- [ ] Implement `FineCalculator` with Strategy pattern
- [ ] Create `Library` class orchestrating all operations
- [ ] Add book search and filtering
- [ ] Implement reservation system
- [ ] Write unit tests for borrowing rules
- [ ] Add file persistence

---

## Project 3: Employee Management

### Description

An employee management system with different departments, roles, and payroll calculations.

### Requirements

**Functional Requirements**:
- Add employees with different roles (Developer, Manager, Director, Intern)
- Department management (Engineering, HR, Marketing, Finance)
- Payroll calculation (base salary + bonuses - deductions)
- Performance review tracking
- Report generation (department-wise, role-wise)
- Employee hierarchy display

**Non-Functional Requirements**:
- Abstract classes for employee hierarchy
- Interfaces for payroll and review strategies
- SOLID principle compliance
- File-based persistence

### Architecture

```
┌────────────────────────────────────────────────┐
│              EmployeeManagement                 │
├────────────────────────────────────────────────┤
│  ┌──────────────┐      ┌──────────────┐       │
│  │  Department  │      │    Role      │       │
│  │  (enum)      │      │  (enum)      │       │
│  └──────────────┘      └──────────────┘       │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │        AbstractEmployee              │     │
│  │  ├─ Developer                        │     │
│  │  ├─ Manager                          │     │
│  │  ├─ Director                         │     │
│  │  └─ Intern                           │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │     PayrollStrategy (interface)      │     │
│  │  ├─ StandardPayroll                  │     │
│  │  ├─ OvertimePayroll                  │     │
│  │  └─ CommissionPayroll                │     │
│  └──────────────────────────────────────┘     │
├────────────────────────────────────────────────┤
│         EmployeeRepository                     │
│         PayrollService                         │
│         ReportGenerator                        │
└────────────────────────────────────────────────┘
```

### Learning Outcomes

- Design abstract class hierarchies
- Implement interfaces for behavior variation
- Apply SOLID principles
- Use the Strategy pattern for payroll
- Practice the Template Method for reports

---

## Project 4: Bank Management

### Description

A banking system supporting multiple account types, transactions, and financial operations.

### Requirements

**Functional Requirements**:
- Account types: Savings, Current, Fixed Deposit
- Deposit, withdrawal, transfer between accounts
- Interest calculation (different for each account type)
- Transaction history with search
- Account statements (monthly/yearly)
- Overdraft protection for Current accounts

**Non-Functional Requirements**:
- Thread-safe transaction processing
- Transaction integrity (ACID-like)
- Account locking for concurrent access
- Audit trail for all operations

### Architecture

```
┌───────────────────────────────────────────────┐
│              BankingSystem                     │
├───────────────────────────────────────────────┤
│  ┌─────────────────────────────────────┐     │
│  │       AbstractAccount               │     │
│  │  ├─ SavingsAccount                  │     │
│  │  ├─ CurrentAccount                  │     │
│  │  └─ FixedDepositAccount             │     │
│  └─────────────────────────────────────┘     │
│                                                │
│  ┌─────────────────────────────────────┐     │
│  │      Transaction (record)           │     │
│  │  ├─ DepositTransaction              │     │
│  │  ├─ WithdrawalTransaction           │     │
│  │  └─ TransferTransaction             │     │
│  └─────────────────────────────────────┘     │
│                                                │
│  ┌─────────────────────────────────────┐     │
│  │     InterestStrategy (interface)    │     │
│  │  ├─ SimpleInterest                  │     │
│  │  ├─ CompoundInterest                │     │
│  │  └─ FixedDepositInterest            │     │
│  └─────────────────────────────────────┘     │
├───────────────────────────────────────────────┤
│         AccountRepository                     │
│         TransactionService                    │
│         StatementGenerator                    │
└───────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement complex inheritance hierarchies
- Use the Strategy pattern for interest calculation
- Apply the Observer pattern for transaction notifications
- Practice thread safety with synchronized blocks
- Understand the Template Method pattern

---

## Project 5: Hospital Management

### Description

A hospital management system managing patients, doctors, appointments, and medical records.

### Requirements

**Functional Requirements**:
- Patient registration and medical history
- Doctor profiles with specializations
- Appointment scheduling (conflict detection)
- Ward and bed management
- Prescription and medication tracking
- Billing and insurance processing

**Non-Functional Requirements**:
- Complex entity relationships
- State management for appointments (Scheduled, InProgress, Completed, Cancelled)
- Role-based access (Admin, Doctor, Nurse, Receptionist)
- Data validation for medical records

### Architecture

```
┌─────────────────────────────────────────────────┐
│             HospitalManagement                   │
├─────────────────────────────────────────────────┤
│  ┌────────┐  ┌────────┐  ┌──────────────────┐  │
│  │ Patient│  │ Doctor │  │ Appointment      │  │
│  └────┬───┘  └────┬───┘  │ (state machine)  │  │
│       │           │       └──────────────────┘  │
│  ┌────▼───────────▼────┐  ┌──────────────────┐  │
│  │ MedicalRecord       │  │ Ward/Bed         │  │
│  │ (composed of)       │  │ Management       │  │
│  │ - Prescriptions     │  └──────────────────┘  │
│  │ - Lab Results       │                        │
│  │ - Vitals            │  ┌──────────────────┐  │
│  └─────────────────────┘  │ BillingService   │  │
│                            └──────────────────┘  │
├─────────────────────────────────────────────────┤
│     PatientRepository  DoctorRepository         │
│     AppointmentService BillingService           │
└─────────────────────────────────────────────────┘
```

### Learning Outcomes

- Model complex real-world relationships
- Implement state machines for appointment lifecycle
- Use composition for medical records
- Practice role-based design
- Handle complex validation rules

---

## Project 6: Inventory Management

### Description

A warehouse inventory system tracking products, suppliers, orders, and stock levels.

### Requirements

**Functional Requirements**:
- Product catalog (SKU, name, category, price, quantity)
- Supplier management with product associations
- Purchase orders and stock replenishment
- Stock alerts for low inventory
- Inventory reports (stock value, movement history)
- Barcode/QR code generation for products

**Non-Functional Requirements**:
- Efficient searching and filtering using Collections
- Generic repository pattern for type safety
- File I/O for data persistence
- Batch operations for bulk updates

### Architecture

```
┌──────────────────────────────────────────────┐
│          InventoryManagement                  │
├──────────────────────────────────────────────┤
│  ┌────────────────────────────────────┐     │
│  │  GenericRepository<T, ID>          │     │
│  │  ├─ ProductRepository              │     │
│  │  ├─ SupplierRepository             │     │
│  │  └─ OrderRepository                │     │
│  └────────────────────────────────────┘     │
│                                               │
│  ┌─────────┐  ┌──────────┐  ┌───────────┐  │
│  │ Product │  │ Supplier │  │ Order     │  │
│  └─────────┘  └──────────┘  └───────────┘  │
│                                               │
│  ┌────────────────────────────────────┐     │
│  │  InventoryService                  │     │
│  │  - checkStock(sku)                 │     │
│  │  - updateStock(sku, quantity)      │     │
│  │  - getLowStockProducts(threshold)  │     │
│  │  - generateReport()                │     │
│  └────────────────────────────────────┘     │
├──────────────────────────────────────────────┤
│  FilePersistenceService                      │
│  AlertService (Observer pattern)             │
└──────────────────────────────────────────────┘
```

### Learning Outcomes

- Use Java Generics for type-safe repositories
- Implement the Observer pattern for stock alerts
- Practice Collections framework (sorting, filtering, grouping)
- Implement file I/O with serialization
- Design efficient search algorithms

---

## Project 7: Parking System

### Description

A multi-level parking management system with vehicle types, parking slots, and fee calculation.

### Requirements

**Functional Requirements**:
- Multiple parking levels with different slot types
- Vehicle types: Car, Motorcycle, Truck, EV
- Slot assignment algorithm (nearest available)
- Fee calculation (hourly, daily, different rates per vehicle type)
- Entry/exit tracking with timestamps
- Real-time availability display

**Non-Functional Requirements**:
- Thread-safe slot assignment (concurrent parking)
- Design patterns: Factory, Strategy, Observer
- Fee calculation strategies (hourly, flat rate, subscription)
- Capacity management per level

### Architecture

```
┌────────────────────────────────────────────────┐
│              ParkingSystem                      │
├────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────┐     │
│  │  ParkingLot (Singleton)              │     │
│  │  ├─ List<ParkingLevel>               │     │
│  │  ├─ FeeCalculator (Strategy)         │     │
│  │  └─ ParkingObserver (Observer)       │     │
│  └──────────────────────────────────────┘     │
│                                                 │
│  ┌──────────────┐  ┌──────────────┐           │
│  │ ParkingLevel │  │ ParkingSlot  │           │
│  │ (floor, slots)│  │ (type, vehicle)│         │
│  └──────────────┘  └──────────────┘           │
│                                                 │
│  ┌──────────────────────────────────────┐     │
│  │  Vehicle (abstract)                  │     │
│  │  ├─ Car                              │     │
│  │  ├─ Motorcycle                       │     │
│  │  ├─ Truck                            │     │
│  │  └─ ElectricVehicle                  │     │
│  └──────────────────────────────────────┘     │
│                                                 │
│  ┌──────────────────────────────────────┐     │
│  │  FeeStrategy (interface)             │     │
│  │  ├─ HourlyFee                        │     │
│  │  ├─ DailyFee                         │     │
│  │  └─ SubscriptionFee                  │     │
│  └──────────────────────────────────────┘     │
├────────────────────────────────────────────────┤
│  Ticket (entry/exit tracking)                  │
│  RevenueReport                                 │
└────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement thread safety with synchronized blocks
- Use the Factory pattern for vehicle creation
- Apply the Strategy pattern for fee calculation
- Implement the Observer pattern for availability updates
- Practice concurrent data structures

---

## Project 8: Food Delivery

### Description

A food delivery platform managing restaurants, menus, orders, and delivery tracking.

### Requirements

**Functional Requirements**:
- Restaurant profiles with menus and categories
- Order placement with item selection
- Real-time order status tracking
- Delivery partner assignment
- Rating and review system
- Search and filter restaurants

**Non-Functional Requirements**:
- Event-driven architecture for status updates
- Strategy pattern for delivery fee calculation
- State machine for order lifecycle
- Search algorithm for restaurant matching

### Architecture

```
┌──────────────────────────────────────────────────┐
│              FoodDeliveryPlatform                 │
├──────────────────────────────────────────────────┤
│  ┌───────────┐  ┌────────┐  ┌──────────────┐   │
│  │Restaurant │  │ Menu   │  │ MenuItem     │   │
│  └─────┬─────┘  └────────┘  └──────────────┘   │
│        │                                         │
│  ┌─────▼──────────────────────────────────┐     │
│  │  Order (state machine)                  │     │
│  │  PLACED → CONFIRMED → PREPARING →      │     │
│  │  READY → PICKED_UP → DELIVERED         │     │
│  └──────────────────────────────────────────┘     │
│                                                    │
│  ┌──────────────────┐  ┌──────────────────┐     │
│  │ DeliveryPartner  │  │ DeliveryTracker  │     │
│  └──────────────────┘  └──────────────────┘     │
│                                                    │
│  ┌──────────────────────────────────────────┐     │
│  │  EventBus (Observer pattern)             │     │
│  │  - OrderStatusListener                   │     │
│  │  - NotificationListener                  │     │
│  └──────────────────────────────────────────┘     │
├──────────────────────────────────────────────────┤
│  RestaurantRepository    OrderRepository          │
│  DeliveryService         SearchService            │
└──────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement event-driven architecture
- Design state machines for order lifecycle
- Use the Observer pattern for notifications
- Practice search algorithms
- Implement rating systems

---

## Project 9: Movie Booking

### Description

A movie ticket booking system with showtimes, seat selection, and pricing.

### Requirements

**Functional Requirements**:
- Movie listings with showtimes
- Seat selection with real-time availability
- Dynamic pricing (peak hours, weekend surcharges)
- Booking confirmation with ticket generation
- Cancellation and refund processing
- Multiple screens and showtimes

**Non-Functional Requirements**:
- Thread-safe seat booking (prevent double booking)
- Concurrent access handling
- Seat locking mechanism
- Pricing strategy pattern

### Architecture

```
┌────────────────────────────────────────────────┐
│             MovieBookingSystem                  │
├────────────────────────────────────────────────┤
│  ┌───────────┐  ┌──────────┐  ┌────────────┐ │
│  │   Movie   │  │ Showtime │  │   Screen   │ │
│  └───────────┘  └──────────┘  └────────────┘ │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  Seat (with status: AVAILABLE,       │     │
│  │  LOCKED, BOOKED, MAINTENANCE)        │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  Booking (transaction)               │     │
│  │  - seats, showtime, total, status    │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  PricingStrategy (interface)         │     │
│  │  ├─ BasePricing                      │     │
│  │  ├─ PeakHourPricing                  │     │
│  │  └─ DynamicPricing                   │     │
│  └──────────────────────────────────────┘     │
├────────────────────────────────────────────────┤
│  BookingService  SeatLockService               │
│  PaymentService  NotificationService           │
└────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement thread-safe booking with locks
- Use the Strategy pattern for pricing
- Practice concurrent data structures
- Design state machines for booking lifecycle
- Implement seat locking mechanisms

---

## Project 10: Ride Sharing

### Description

A ride-sharing platform matching riders with drivers based on location and availability.

### Requirements

**Functional Requirements**:
- Rider and driver registration
- Ride request with pickup/dropoff locations
- Driver matching algorithm (nearest available)
- Fare calculation based on distance and time
- Trip tracking and history
- Rating system for drivers and riders

**Non-Functional Requirements**:
- Geolocation-based matching
- Real-time availability tracking
- Algorithm efficiency for matching
- Trip state management

### Architecture

```
┌────────────────────────────────────────────────┐
│              RideSharingPlatform                │
├────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐│
│  │  Rider   │  │  Driver  │  │   Vehicle    ││
│  └──────────┘  └──────────┘  └──────────────┘│
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  RideRequest                         │     │
│  │  - pickup, dropoff, timestamp        │     │
│  │  - status: REQUESTED, MATCHED,       │     │
│  │    IN_PROGRESS, COMPLETED, CANCELLED │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  MatchingService                     │     │
│  │  - findNearestDriver(location)       │     │
│  │  - calculateDistance(loc1, loc2)     │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │  FareCalculator (Strategy)           │     │
│  │  ├─ StandardFare                     │     │
│  │  ├─ SurgePricing                     │     │
│  │  └─ PoolFare                         │     │
│  └──────────────────────────────────────┘     │
├────────────────────────────────────────────────┤
│  LocationService   RideService                 │
│  DriverService     PaymentService              │
└────────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement geolocation-based matching
- Design efficient matching algorithms
- Use the Strategy pattern for fare calculation
- Practice state machine design
- Implement real-time tracking

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
- Comprehensive error handling
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
- Practice comprehensive error handling
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
