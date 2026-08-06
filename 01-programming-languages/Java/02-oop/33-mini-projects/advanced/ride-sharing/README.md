# Ride Sharing System

## Project Overview

A Ride Sharing System (similar to Uber or Lyft) that handles rider requests, driver matching, ride tracking, payment processing, and surge pricing. This advanced project introduces the State pattern for ride lifecycle, the Strategy pattern for matching algorithms, and the Observer pattern for real-time updates. Students will design a system that handles complex real-time operations.

## Learning Outcomes

- Implement the State pattern for ride lifecycle management
- Use the Strategy pattern for driver matching algorithms
- Apply the Observer pattern for real-time ride updates
- Design geospatial indexing for location-based queries
- Handle surge pricing with dynamic algorithms
- Implement rating systems
- Design for high availability and fault tolerance

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Rider registration with profile management | Must |
| FR02 | Driver registration with vehicle and license info | Must |
| FR03 | Request ride with pickup and dropoff locations | Must |
| FR04 | Match riders with available drivers | Must |
| FR05 | Real-time ride tracking on map | Must |
| FR06 | Dynamic fare calculation with surge pricing | Must |
| FR07 | Payment processing with multiple methods | Must |
| FR08 | Driver availability toggle | Should |
| FR09 | Ride history and receipts | Should |
| FR10 | Rating and feedback system | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Driver location update every 5 seconds |
| NFR02 | Match rider within 30 seconds |
| NFR03 | Support 100,000+ concurrent users |
| NFR04 | 99.9% uptime requirement |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        RiderApp[RiderApp.java]
        DriverApp[DriverApp.java]
    end
    
    subgraph Service Layer
        RideService[Ride Service]
        DriverService[Driver Service]
        PaymentService[Payment Service]
        LocationService[Location Service]
        PricingService[Pricing Service]
    end
    
    subgraph Core Components
        RideManager[Ride Manager]
        MatchingEngine[Matching Engine]
        SurgePricing[Surge Pricing]
        GeoIndex[Geo Index]
    end
    
    subgraph Patterns
        State[State Pattern]
        Strategy[Strategy Pattern]
        Observer[Observer Pattern]
    end
    
    subgraph Storage
        RideDB[(Ride DB)]
        DriverDB[(Driver DB)]
        LocationCache[(Location Cache)]
        UserDB[(User DB)]
    end
    
    Main --> RideService
    RiderApp --> RideService
    DriverApp --> DriverService
    RideService --> RideManager
    RideService --> MatchingEngine
    RideService --> SurgePricing
    MatchingEngine --> GeoIndex
    RideManager --> State
    MatchingEngine --> Strategy
    RideService --> Observer
```

## Package Structure

```
ride-sharing/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── rideshare/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Rider.java
│                       │   ├── Driver.java
│                       │   ├── Ride.java
│                       │   ├── RideRequest.java
│                       │   ├── Vehicle.java
│                       │   ├── Location.java
│                       │   ├── Payment.java
│                       │   └── enums/
│                       │       ├── RideStatus.java
│                       │       ├── DriverStatus.java
│                       │       ├── VehicleType.java
│                       │       └── PaymentMethod.java
│                       ├── state/
│                       │   ├── RideState.java
│                       │   ├── RequestedState.java
│                       │   ├── MatchedState.java
│                       │   ├── InProgressState.java
│                       │   ├── CompletedState.java
│                       │   └── CancelledState.java
│                       ├── strategy/
│                       │   ├── MatchingStrategy.java
│                       │   ├── NearestDriverStrategy.java
│                       │   ├── RatingBasedStrategy.java
│                       │   └── SurgeAwareStrategy.java
│                       ├── observer/
│                       │   ├── RideObserver.java
│                       │   ├── RideEventManager.java
│                       │   ├── RiderNotification.java
│                       │   └── DriverNotification.java
│                       ├── pricing/
│                       │   ├── FareCalculator.java
│                       │   ├── SurgePricingEngine.java
│                       │   └── PricingStrategy.java
│                       ├── geo/
│                       │   ├── GeoSpatialIndex.java
│                       │   └── GeoHash.java
│                       ├── service/
│                       │   ├── RideService.java
│                       │   ├── DriverService.java
│                       │   ├── PaymentService.java
│                       │   ├── LocationService.java
│                       │   └── NotificationService.java
│                       └── exception/
│                           ├── NoDriverAvailableException.java
│                           ├── RideNotFoundException.java
│                           ├── InvalidLocationException.java
│                           └── PaymentFailedException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── rideshare/
                        ├── RideServiceTest.java
                        ├── MatchingStrategyTest.java
                        ├── SurgePricingTest.java
                        └── GeoIndexTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Rider {
        -String riderId
        -String name
        -String phone
        -Location currentLocation
        -double rating
        -List~Ride~ rideHistory
        +Rider(id, name, phone)
        +requestRide(RideRequest) Ride
        +cancelRide(String rideId) void
        +rateRide(String rideId, double rating) void
    }
    
    class Driver {
        -String driverId
        -String name
        -String phone
        -Vehicle vehicle
        -Location currentLocation
        -DriverStatus status
        -double rating
        -int totalRides
        +Driver(id, name, phone, vehicle)
        +isAvailable() boolean
        +acceptRide(Ride) void
        +completeRide() void
        +updateLocation(Location) void
    }
    
    class Ride {
        -String rideId
        -Rider rider
        -Driver driver
        -RideRequest request
        -RideStatus status
        -RideState state
        -BigDecimal fare
        -LocalDateTime startTime
        -LocalDateTime endTime
        +Ride(id, rider, request)
        +match(Driver) void
        +start() void
        +complete() void
        +cancel() void
        +getStatus() RideStatus
    }
    
    class RideRequest {
        -String requestId
        -Rider rider
        -Location pickup
        -Location dropoff
        -VehicleType preferredType
        -LocalDateTime requestedAt
        +RideRequest(rider, pickup, dropoff)
        +getPickupLocation() Location
        +getDropoffLocation() Location
    }
    
    class Location {
        -double latitude
        -double longitude
        -String address
        +Location(lat, lng, address)
        +distanceTo(Location) double
        +getLatitude() double
        +getLongitude() double
    }
    
    class RideState {
        <<interface>>
        +match(Ride, Driver) void
        +start(Ride) void
        +complete(Ride) void
        +cancel(Ride) void
        +getStatus() RideStatus
    }
    
    class MatchingStrategy {
        <<interface>>
        +findBestDriver(List~Driver~, RideRequest) Driver
    }
    
    class NearestDriverStrategy {
        +findBestDriver(List~Driver~, RideRequest) Driver
    }
    
    class SurgePricingEngine {
        -Map~String,BigDecimal~ zoneMultipliers
        -GeoSpatialIndex geoIndex
        +calculateSurge(Location, int) BigDecimal
        +getSurgeMultiplier(Location) BigDecimal
    }
    
    class FareCalculator {
        -SurgePricingEngine surgeEngine
        +calculateFare(Ride) BigDecimal
    }
    
    Rider --> RideRequest
    RideRequest --> Location
    Ride --> RideState
    Ride --> RideStatus
    Ride --> Driver
    Ride --> Rider
    Driver --> Vehicle
    Driver --> DriverStatus
    MatchingStrategy <|.. NearestDriverStrategy
    MatchingStrategy <|.. RatingBasedStrategy
    FareCalculator --> SurgePricingEngine
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**