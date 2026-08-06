# Movie Booking System

## Project Overview

A Movie Booking System (similar to BookMyShow or Fandango) that handles movie listings, showtimes, seat selection, booking, and payment processing. This advanced project introduces the Proxy pattern for lazy loading, the Mediator pattern for seat selection coordination, and the Decorator pattern for dynamic pricing. Students will design a system that handles complex booking scenarios.

## Learning Outcomes

- Implement the Proxy pattern for lazy loading and access control
- Use the Mediator pattern for coordinating seat selection
- Apply the Decorator pattern for dynamic pricing
- Handle concurrent seat reservations
- Implement complex business rules
- Design for high availability scenarios
- Use immutability for booking records

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Browse movies with details (title, genre, duration, rating) | Must |
| FR02 | View showtimes by date and theater | Must |
| FR03 | Interactive seat selection with real-time availability | Must |
| FR04 | Create bookings with seat hold timeout | Must |
| FR05 | Process payments with multiple methods | Must |
| FR06 | Support different ticket types (Regular, Premium, VIP) | Must |
| FR07 | Cancel bookings with refund policy | Should |
| FR08 | Apply discount codes and offers | Should |
| FR09 | Seat pricing by location (front, middle, back) | Should |
| FR10 | Food and beverage add-ons | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | Seat hold timeout (5 minutes) |
| NFR02 | Prevent double-booking |
| NFR03 | Handle 1000+ concurrent users |
| NFR04 | Booking confirmation within 30 seconds |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        WebAPI[WebAPI.java]
    end
    
    subgraph Service Layer
        BookingService[Booking Service]
        MovieService[Movie Service]
        ShowService[Show Service]
        PaymentService[Payment Service]
        SeatService[Seat Service]
    end
    
    subgraph Core Components
        SeatSelection[Seat Selection]
        PricingEngine[Pricing Engine]
        BookingManager[Booking Manager]
    end
    
    subgraph Patterns
        Proxy[Proxy Pattern]
        Mediator[Mediator Pattern]
        Decorator[Decorator Pattern]
    end
    
    subgraph Storage
        MovieDB[(Movie DB)]
        ShowDB[(Show DB)]
        BookingDB[(Booking DB)]
        SeatCache[(Seat Cache)]
    end
    
    Main --> BookingService
    WebAPI --> BookingService
    BookingService --> SeatSelection
    BookingService --> PricingEngine
    BookingService --> BookingManager
    SeatSelection --> Mediator
    PricingEngine --> Decorator
    MovieService --> Proxy
```

## Package Structure

```
movie-booking/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── booking/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Movie.java
│                       │   ├── Theater.java
│                       │   ├── Screen.java
│                       │   ├── Show.java
│                       │   ├── Seat.java
│                       │   ├── Booking.java
│                       │   ├── Ticket.java
│                       │   └── enums/
│                       │       ├── BookingStatus.java
│                       │       ├── SeatStatus.java
│                       │       ├── SeatType.java
│                       │       └── TicketType.java
│                       ├── proxy/
│                       │   ├── MovieProxy.java
│                       │   └── LazyLoader.java
│                       ├── mediator/
│                       │   ├── SeatMediator.java
│                       │   ├── SeatSelectionMediator.java
│                       │   └── SeatComponent.java
│                       ├── decorator/
│                       │   ├── PricingComponent.java
│                       │   ├── PricingDecorator.java
│                       │   ├── BasePrice.java
│                       │   ├── PremiumSeatDecorator.java
│                       │   ├── WeekendSurgeDecorator.java
│                       │   └── DiscountDecorator.java
│                       ├── service/
│                       │   ├── BookingService.java
│                       │   ├── MovieService.java
│                       │   ├── ShowService.java
│                       │   ├── PaymentService.java
│                       │   └── SeatService.java
│                       └── exception/
│                           ├── SeatAlreadyBookedException.java
│                           ├── BookingTimeoutException.java
│                           ├── ShowNotFoundException.java
│                           └── InvalidSeatException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── booking/
                        ├── BookingServiceTest.java
                        ├── SeatSelectionTest.java
                        ├── PricingDecoratorTest.java
                        └── ProxyTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Movie {
        -String movieId
        -String title
        -String description
        -List~String~ genres
        -int durationMinutes
        -double rating
        -LocalDate releaseDate
        +Movie(id, title)
        +getMovieId() String
        +getTitle() String
        +getGenres() List~String~
    }
    
    class Theater {
        -String theaterId
        -String name
        -String location
        -List~Screen~ screens
        +Theater(id, name)
        +getScreens() List~Screen~
    }
    
    class Show {
        -String showId
        -Movie movie
        -Screen screen
        -LocalDateTime showTime
        -BigDecimal basePrice
        -Map~String,Seat~ seats
        +Show(id, movie, screen, time)
        +getSeat(String seatId) Seat
        +getAvailableSeats() List~Seat~
    }
    
    class Seat {
        -String seatId
        -String row
        -int number
        -SeatType type
        -SeatStatus status
        -BigDecimal price
        +Seat(id, row, number, type)
        +isAvailable() boolean
        +reserve() void
        +release() void
    }
    
    class Booking {
        -String bookingId
        -String userId
        -Show show
        -List~Seat~ seats
        -BookingStatus status
        -BigDecimal totalAmount
        -LocalDateTime createdAt
        -LocalDateTime expiresAt
        +Booking(id, userId, show, seats)
        +getStatus() BookingStatus
        +confirm() void
        +cancel() void
        +isExpired() boolean
    }
    
    class SeatMediator {
        <<interface>>
        +selectSeat(String userId, String seatId) boolean
        +confirmSelection(String userId) Booking
        +releaseSeat(String userId, String seatId) void
    }
    
    class SeatSelectionMediator {
        -Show show
        -Map~String,List~String~~ userSelections
        -Map~String,ScheduledFuture~~ timers
        +selectSeat(String userId, String seatId) boolean
        +confirmSelection(String userId) Booking
    }
    
    class PricingComponent {
        <<interface>>
        +getPrice() BigDecimal
        +getDescription() String
    }
    
    class PricingDecorator {
        <<abstract>>
        #PricingComponent component
        +PricingDecorator(PricingComponent)
    }
    
    class PremiumSeatDecorator {
        +getPrice() BigDecimal
        +getDescription() String
    }
    
    class WeekendSurgeDecorator {
        +getPrice() BigDecimal
        +getDescription() String
    }
    
    class DiscountDecorator {
        -double discountPercent
        +getPrice() BigDecimal
        +getDescription() String
    }
    
    Theater --> Screen
    Show --> Movie
    Show --> Screen
    Show --> Seat
    Booking --> Show
    Booking --> Seat
    Booking --> BookingStatus
    Seat --> SeatType
    Seat --> SeatStatus
    SeatMediator <|.. SeatSelectionMediator
    PricingComponent <|.. PricingDecorator
    PricingDecorator <|-- PremiumSeatDecorator
    PricingDecorator <|-- WeekendSurgeDecorator
    PricingDecorator <|-- DiscountDecorator
    PricingDecorator o-- PricingDecorator
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**