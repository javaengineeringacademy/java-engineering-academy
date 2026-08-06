# Food Delivery System

## Project Overview

A Food Delivery System (similar to UberEats or DoorDash) that manages restaurants, menus, orders, delivery tracking, and rider assignment. This advanced project introduces the Chain of Responsibility pattern for order validation, the Observer pattern for real-time tracking, and the Command pattern for order management. Students will design a system that handles the complete food delivery lifecycle.

## Learning Outcomes

- Implement the Chain of Responsibility pattern for order validation
- Use the Observer pattern for real-time order tracking
- Apply the Command pattern for order operations
- Design for real-time location tracking
- Handle complex business rules and validations
- Implement retry mechanisms for failed operations
- Design for scalability and reliability

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Manage restaurants with menus and categories | Must |
| FR02 | Browse restaurants by cuisine, rating, distance | Must |
| FR03 | Add items to cart with customization | Must |
| FR04 | Place orders with address validation | Must |
| FR05 | Real-time order status tracking | Must |
| FR06 | Rider assignment and tracking | Must |
| FR07 | Payment processing with multiple methods | Must |
| FR08 | Order cancellation with refund policy | Should |
| FR09 | Delivery time estimation | Should |
| FR10 | Rating and review system | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Real-time location updates every 5 seconds |
| NFR02 | Order status updates within 2 seconds |
| NFR03 | Support 10,000+ concurrent orders |
| NFR04 | Graceful degradation on service failure |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        MobileAPI[MobileAPI.java]
        WebAPI[WebAPI.java]
    end
    
    subgraph Service Layer
        OrderService[Order Service]
        RestaurantService[Restaurant Service]
        DeliveryService[Delivery Service]
        PaymentService[Payment Service]
        UserService[User Service]
    end
    
    subgraph Core Components
        OrderManager[Order Manager]
        CartManager[Cart Manager]
        RiderAssignment[Rider Assignment]
        LocationTracker[Location Tracker]
    end
    
    subgraph Patterns
        ChainOfResp[Chain of Responsibility]
        Observer[Observer Pattern]
        Command[Command Pattern]
        Strategy[Strategy Pattern]
    end
    
    subgraph Storage
        OrderDB[Order Database]
        RestaurantDB[Restaurant Database]
        LocationDB[Location Database]
        UserDB[User Database]
    end
    
    Main --> OrderService
    MobileAPI --> OrderService
    WebAPI --> OrderService
    OrderService --> OrderManager
    OrderService --> CartManager
    OrderService --> PaymentService
    DeliveryService --> RiderAssignment
    DeliveryService --> LocationTracker
    OrderManager --> ChainOfResp
    OrderManager --> Observer
    DeliveryService --> Observer
    OrderService --> Command
```

## Package Structure

```
food-delivery/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── delivery/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Restaurant.java
│                       │   ├── MenuItem.java
│                       │   ├── Cart.java
│                       │   ├── CartItem.java
│                       │   ├── Order.java
│                       │   ├── Delivery.java
│                       │   ├── Rider.java
│                       │   ├── Location.java
│                       │   ├── Customer.java
│                       │   └── enums/
│                       │       ├── OrderStatus.java
│                       │       ├── DeliveryStatus.java
│                       │       ├── RiderStatus.java
│                       │       └── PaymentMethod.java
│                       ├── chain/
│                       │   ├── OrderValidator.java
│                       │   ├── RestaurantValidator.java
│                       │   ├── CartValidator.java
│                       │   ├── AddressValidator.java
│                       │   └── ValidationResult.java
│                       ├── observer/
│                       │   ├── OrderObserver.java
│                       │   ├── OrderEventManager.java
│                       │   ├── CustomerNotification.java
│                       │   └── RestaurantNotification.java
│                       ├── command/
│                       │   ├── Command.java
│                       │   ├── PlaceOrderCommand.java
│                       │   ├── CancelOrderCommand.java
│                       │   ├── UpdateOrderCommand.java
│                       │   └── CommandHistory.java
│                       ├── strategy/
│                       │   ├── DeliveryStrategy.java
│                       │   ├── NearestRiderStrategy.java
│                       │   ├── FastestRiderStrategy.java
│                       │   └── RatingBasedStrategy.java
│                       ├── service/
│                       │   ├── OrderService.java
│                       │   ├── RestaurantService.java
│                       │   ├── DeliveryService.java
│                       │   ├── PaymentService.java
│                       │   ├── LocationService.java
│                       │   └── RiderService.java
│                       └── exception/
│                           ├── OrderException.java
│                           ├── NoRiderAvailableException.java
│                           ├── RestaurantClosedException.java
│                           └── DeliveryException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── delivery/
                        ├── OrderServiceTest.java
                        ├── DeliveryServiceTest.java
                        ├── ValidationChainTest.java
                        └── ObserverTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Restaurant {
        -String restaurantId
        -String name
        -String cuisine
        -Location location
        -List~MenuItem~ menu
        -double rating
        -boolean isOpen
        +Restaurant(id, name, cuisine)
        +getMenu() List~MenuItem~
        +isCurrentlyOpen() boolean
        +updateRating(double rating) void
    }
    
    class MenuItem {
        -String itemId
        -String name
        -String description
        -BigDecimal price
        -String category
        -boolean isAvailable
        -List~String~ customizations
        +MenuItem(id, name, price)
        +getPrice() BigDecimal
        +isAvailable() boolean
    }
    
    class Order {
        -String orderId
        -String customerId
        -Restaurant restaurant
        -List~OrderItem~ items
        -OrderStatus status
        -BigDecimal totalAmount
        -String deliveryAddress
        -Location deliveryLocation
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +Order(id, customer, restaurant, items)
        +getStatus() OrderStatus
        +updateStatus(OrderStatus) void
        +calculateTotal() BigDecimal
        +canBeCancelled() boolean
    }
    
    class Delivery {
        -String deliveryId
        -Order order
        -Rider rider
        -DeliveryStatus status
        -Location currentLocation
        -LocalDateTime assignedAt
        -LocalDateTime deliveredAt
        +Delivery(Order)
        +assignRider(Rider) void
        +updateLocation(Location) void
        +complete() void
    }
    
    class Rider {
        -String riderId
        -String name
        -String phone
        -Location currentLocation
        -RiderStatus status
        -double rating
        -int totalDeliveries
        +Rider(id, name, phone)
        +isAvailable() boolean
        +completeDelivery(Delivery) void
        +updateLocation(Location) void
    }
    
    class OrderValidator {
        <<abstract>>
        #OrderValidator next
        +setNext(OrderValidator) OrderValidator
        +validate(Order) ValidationResult
        #doValidate(Order) ValidationResult
    }
    
    class RestaurantValidator {
        #doValidate(Order) ValidationResult
    }
    
    class CartValidator {
        #doValidate(Order) ValidationResult
    }
    
    class AddressValidator {
        #doValidate(Order) ValidationResult
    }
    
    class OrderObserver {
        <<interface>>
        +onOrderUpdate(Order, OrderStatus) void
        +onLocationUpdate(String, Location) void
    }
    
    class OrderEventManager {
        -Map~String,List~OrderObserver~~ observers
        +subscribe(String, OrderObserver) void
        +unsubscribe(String, OrderObserver) void
        +notifyOrderUpdate(Order, OrderStatus) void
        +notifyLocationUpdate(String, Location) void
    }
    
    Restaurant --> MenuItem
    Order --> Restaurant
    Order --> OrderStatus
    Delivery --> Order
    Delivery --> Rider
    Delivery --> DeliveryStatus
    Rider --> RiderStatus
    OrderValidator <|-- RestaurantValidator
    OrderValidator <|-- CartValidator
    OrderValidator <|-- AddressValidator
    OrderValidator o-- OrderValidator
    OrderEventManager --> OrderObserver
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**