# Parking Lot Management System

## Project Overview

A Parking Lot Management System that handles vehicle entry/exit, parking spot allocation, ticket management, and payment processing. This advanced project introduces the Strategy pattern for pricing, the State pattern for ticket states, and the Factory pattern for vehicle types. Students will design a system that efficiently manages limited parking resources.

## Learning Outcomes

- Implement the Strategy pattern for flexible pricing models
- Use the State pattern for ticket lifecycle management
- Apply the Factory pattern for vehicle creation
- Design efficient allocation algorithms
- Handle concurrent access to shared resources
- Implement time-based calculations
- Design for different vehicle types

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Vehicle entry with ticket generation | Must |
| FR02 | Parking spot allocation by vehicle type | Must |
| FR03 | Vehicle exit with fee calculation | Must |
| FR04 | Multiple pricing strategies (hourly, daily, flat) | Must |
| FR05 | Display available spots | Must |
| FR06 | Support different vehicle types (car, truck, motorcycle) | Must |
| FR07 | Payment processing | Should |
| FR08 | Spot reservation system | Should |
| FR09 | Membership/subscription pricing | Could |
| FR10 | EV charging station integration | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Thread-safe spot allocation |
| NFR02 | Real-time availability updates |
| NFR03 | Handle 1000+ vehicles |
| NFR04 | Minimal allocation time |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        Terminal[EntryExitTerminal.java]
    end
    
    subgraph Service Layer
        ParkingService[Parking Service]
        PaymentService[Payment Service]
        DisplayService[Display Service]
    end
    
    subgraph Core Components
        ParkingLot[Parking Lot]
        SpotAllocator[Spot Allocator]
        TicketManager[Ticket Manager]
    end
    
    subgraph Patterns
        Strategy[Strategy Pattern]
        State[State Pattern]
        Factory[Factory Pattern]
    end
    
    subgraph Storage
        LotDB[(Parking Lot DB)]
        TicketDB[(Ticket DB)]
        VehicleDB[(Vehicle DB)]
    end
    
    Main --> Terminal
    Terminal --> ParkingService
    ParkingService --> ParkingLot
    ParkingService --> SpotAllocator
    ParkingService --> TicketManager
    ParkingLot --> Strategy
    TicketManager --> State
    SpotAllocator --> Factory
```

## Package Structure

```
parking-lot/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── parking/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Vehicle.java
│                       │   ├── Truck.java
│                       │   ├── Motorcycle.java
│                       │   ├── ElectricVehicle.java
│                       │   ├── ParkingLot.java
│                       │   ├── ParkingFloor.java
│                       │   ├── ParkingSpot.java
│                       │   ├── Ticket.java
│                       │   ├── Payment.java
│                       │   └── enums/
│                       │       ├── VehicleType.java
│                       │       ├── SpotType.java
│                       │       ├── TicketStatus.java
│                       │       └── PaymentStatus.java
│                       ├── strategy/
│                       │   ├── PricingStrategy.java
│                       │   ├── HourlyPricing.java
│                       │   ├── DailyPricing.java
│                       │   └── FlatRatePricing.java
│                       ├── state/
│                       │   ├── TicketState.java
│                       │   ├── ActiveState.java
│                       │   ├── PendingPaymentState.java
│                       │   └── PaidState.java
│                       ├── factory/
│                       │   └── VehicleFactory.java
│                       ├── service/
│                       │   ├── ParkingService.java
│                       │   ├── PaymentService.java
│                       │   └── DisplayService.java
│                       └── exception/
│                           ├── ParkingLotFullException.java
│                           ├── VehicleNotFoundException.java
│                           ├── InvalidTicketException.java
│                           └── PaymentFailedException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── parking/
                        ├── ParkingServiceTest.java
                        ├── PricingStrategyTest.java
                        ├── TicketStateTest.java
                        └── VehicleFactoryTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Vehicle {
        -String licensePlate
        -String ownerName
        -VehicleType type
        +Vehicle(plate, owner, type)
        +getLicensePlate() String
        +getType() VehicleType
    }
    
    class Truck {
        +Truck(plate, owner)
    }
    
    class Motorcycle {
        +Motorcycle(plate, owner)
    }
    
    class ElectricVehicle {
        -boolean isCharging
        +ElectricVehicle(plate, owner)
        +startCharging() void
        +stopCharging() void
    }
    
    class ParkingLot {
        -String lotId
        -String name
        -List~ParkingFloor~ floors
        -Map~String,Ticket~ activeTickets
        +ParkingLot(id, name)
        +findAvailableSpot(VehicleType) ParkingSpot
        +addActiveTicket(Ticket) void
        +removeActiveTicket(String) void
    }
    
    class ParkingFloor {
        -int floorNumber
        -List~ParkingSpot~ spots
        +getAvailableSpots(VehicleType) List~ParkingSpot~
        +getOccupiedSpots() List~ParkingSpot~
    }
    
    class ParkingSpot {
        -String spotId
        -SpotType spotType
        -int floorNumber
        -int spotNumber
        -Vehicle vehicle
        +ParkingSpot(id, type, floor, spot)
        +isAvailable() boolean
        +assignVehicle(Vehicle) void
        +removeVehicle() void
    }
    
    class Ticket {
        -String ticketId
        -Vehicle vehicle
        -ParkingSpot spot
        -TicketState state
        -LocalDateTime entryTime
        -LocalDateTime exitTime
        -BigDecimal amount
        +Ticket(vehicle, spot)
        +enter() void
        +exit() void
        +pay() void
        +getStatus() TicketStatus
    }
    
    class PricingStrategy {
        <<interface>>
        +calculateFee(Ticket) BigDecimal
        +getStrategyName() String
    }
    
    class HourlyPricing {
        -BigDecimal hourlyRate
        -BigDecimal maxDailyCharge
        +calculateFee(Ticket) BigDecimal
    }
    
    class FlatRatePricing {
        -BigDecimal flatRate
        +calculateFee(Ticket) BigDecimal
    }
    
    class TicketState {
        <<interface>>
        +enter(Ticket) void
        +exit(Ticket) void
        +pay(Ticket) void
        +getStatus() TicketStatus
    }
    
    class ActiveState {
        +exit(Ticket) void
        +getStatus() TicketStatus
    }
    
    class PaidState {
        +getStatus() TicketStatus
    }
    
    Vehicle <|-- Truck
    Vehicle <|-- Motorcycle
    Vehicle <|-- ElectricVehicle
    ParkingLot --> ParkingFloor
    ParkingFloor --> ParkingSpot
    ParkingSpot --> Vehicle
    Ticket --> Vehicle
    Ticket --> ParkingSpot
    Ticket --> TicketState
    Ticket --> TicketStatus
    PricingStrategy <|.. HourlyPricing
    PricingStrategy <|.. FlatRatePricing
    TicketState <|.. ActiveState
    TicketState <|.. PaidState
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**