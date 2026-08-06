# Mini Projects — Part 2: Advanced Projects (6-10)

**[← Part 1: Beginner & Intermediate Projects (1-5)](README.md)** | **[Part 3: Enterprise Projects (11-14) →](README-part3.md)**

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

**[← Part 1: Beginner & Intermediate Projects (1-5)](README.md)** | **[Part 3: Enterprise Projects (11-14) →](README-part3.md)**