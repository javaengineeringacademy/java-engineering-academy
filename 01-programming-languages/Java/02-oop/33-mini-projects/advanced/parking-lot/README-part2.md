# Parking Lot Management System — Part 2: Implementation Guide

**[← Part 1: Project Overview & Design](README.md)**

---

## Implementation Guide

### Step 1: Implement Strategy Pattern for Pricing

```java
package com.academy.parking.strategy;

import com.academy.parking.model.Ticket;
import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateFee(Ticket ticket);
    String getStrategyName();
}

package com.academy.parking.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

public class HourlyPricing implements PricingStrategy {
    private final BigDecimal hourlyRate;
    private final BigDecimal maxDailyCharge;

    public HourlyPricing(BigDecimal hourlyRate, BigDecimal maxDaily) {
        this.hourlyRate = hourlyRate;
        this.maxDailyCharge = maxDaily;
    }

    @Override
    public BigDecimal calculateFee(Ticket ticket) {
        long hours = ChronoUnit.HOURS.between(
            ticket.getEntryTime(), 
            ticket.getExitTime()
        );
        
        if (hours == 0) hours = 1;
        
        BigDecimal fee = hourlyRate.multiply(BigDecimal.valueOf(hours));
        return fee.min(maxDailyCharge).setScale(2, RoundingMode.HALF_UP);
    }
}

public class FlatRatePricing implements PricingStrategy {
    private final BigDecimal flatRate;

    public FlatRatePricing(BigDecimal flatRate) {
        this.flatRate = flatRate;
    }

    @Override
    public BigDecimal calculateFee(Ticket ticket) {
        return flatRate;
    }
}
```

### Step 2: Implement State Pattern for Tickets

```java
package com.academy.parking.state;

import com.academy.parking.model.Ticket;
import com.academy.parking.model.enums.TicketStatus;

public interface TicketState {
    void enter(Ticket ticket);
    void exit(Ticket ticket);
    void pay(Ticket ticket);
    TicketStatus getStatus();
}

package com.academy.parking.state;

public class ActiveState implements TicketState {
    @Override
    public void exit(Ticket ticket) {
        ticket.setExitTime(LocalDateTime.now());
        ticket.setState(new PendingPaymentState());
    }

    @Override
    public void pay(Ticket ticket) {
        throw new IllegalStateException("Cannot pay before exiting");
    }

    @Override
    public TicketStatus getStatus() {
        return TicketStatus.ACTIVE;
    }
}

public class PendingPaymentState implements TicketState {
    @Override
    public void pay(Ticket ticket) {
        ticket.setState(new PaidState());
    }

    @Override
    public TicketStatus getStatus() {
        return TicketStatus.PENDING_PAYMENT;
    }
}

public class PaidState implements TicketState {
    @Override
    public TicketStatus getStatus() {
        return TicketStatus.PAID;
    }
}
```

### Step 3: Implement Factory Pattern

```java
package com.academy.parking.factory;

import com.academy.parking.model.Vehicle;
import com.academy.parking.model.enums.VehicleType;

public class VehicleFactory {
    
    public Vehicle createVehicle(String licensePlate, String ownerName, VehicleType type) {
        switch (type) {
            case CAR:
                return new Vehicle(licensePlate, ownerName, VehicleType.CAR);
            case TRUCK:
                return new Truck(licensePlate, ownerName);
            case MOTORCYCLE:
                return new Motorcycle(licensePlate, ownerName);
            case EV:
                return new ElectricVehicle(licensePlate, ownerName);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}
```

### Step 4: Implement Parking Service

```java
package com.academy.parking.service;

import com.academy.parking.model.*;
import com.academy.parking.exception.*;
import java.math.BigDecimal;

public class ParkingService {
    private final ParkingLot parkingLot;
    private PricingStrategy pricingStrategy;
    private final PaymentService paymentService;

    public Ticket enterVehicle(Vehicle vehicle) throws ParkingLotFullException {
        ParkingSpot spot = parkingLot.findAvailableSpot(vehicle.getType());
        if (spot == null) {
            throw new ParkingLotFullException("No available spots for " + vehicle.getType());
        }
        
        spot.assignVehicle(vehicle);
        Ticket ticket = new Ticket(vehicle, spot);
        parkingLot.addActiveTicket(ticket);
        
        return ticket;
    }

    public Payment exitVehicle(String ticketId) throws VehicleNotFoundException {
        Ticket ticket = parkingLot.findTicket(ticketId);
        ticket.exit();
        
        BigDecimal fee = pricingStrategy.calculateFee(ticket);
        ticket.setAmount(fee);
        
        Payment payment = paymentService.processPayment(ticket);
        ticket.pay();
        
        ticket.getSpot().removeVehicle();
        parkingLot.removeActiveTicket(ticketId);
        
        return payment;
    }
}
```

## Unit Tests

```java
package com.academy.parking;

import com.academy.parking.model.*;
import com.academy.parking.model.enums.*;
import com.academy.parking.service.ParkingService;
import com.academy.parking.strategy.*;
import com.academy.parking.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingServiceTest {
    private ParkingService service;
    private ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        parkingLot = new ParkingLot("PL001", "Main Lot", 3);
        service = new ParkingService(parkingLot, new HourlyPricing(new BigDecimal("5.00"), new BigDecimal("25.00")));
    }

    @Test
    void testEnterVehicle() throws Exception {
        Vehicle car = new Vehicle("ABC123", "John Doe", VehicleType.CAR);
        Ticket ticket = service.enterVehicle(car);
        
        assertNotNull(ticket);
        assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
    }

    @Test
    void testParkingLotFull() throws Exception {
        ParkingLot smallLot = new ParkingLot("PL002", "Small", 1);
        smallLot.addFloor(SpotType.STANDARD, 1);
        
        ParkingService smallService = new ParkingService(smallLot, new FlatRatePricing(new BigDecimal("10.00")));
        
        Vehicle car1 = new Vehicle("ABC123", "John", VehicleType.CAR);
        Vehicle car2 = new Vehicle("DEF456", "Jane", VehicleType.CAR);
        
        smallService.enterVehicle(car1);
        assertThrows(ParkingLotFullException.class, () -> smallService.enterVehicle(car2));
    }

    @Test
    void testHourlyPricing() {
        Ticket ticket = new Ticket(
            new Vehicle("ABC123", "John", VehicleType.CAR),
            new ParkingSpot("S001", SpotType.STANDARD, 1, 1)
        );
        ticket.setEntryTime(LocalDateTime.now().minusHours(3));
        ticket.setExitTime(LocalDateTime.now());
        
        PricingStrategy strategy = new HourlyPricing(new BigDecimal("5.00"), new BigDecimal("25.00"));
        BigDecimal fee = strategy.calculateFee(ticket);
        
        assertEquals(new BigDecimal("15.00"), fee);
    }

    @Test
    void testFlatRatePricing() {
        Ticket ticket = new Ticket(
            new Vehicle("ABC123", "John", VehicleType.CAR),
            new ParkingSpot("S001", SpotType.STANDARD, 1, 1)
        );
        
        PricingStrategy strategy = new FlatRatePricing(new BigDecimal("10.00"));
        BigDecimal fee = strategy.calculateFee(ticket);
        
        assertEquals(new BigDecimal("10.00"), fee);
    }

    @Test
    void testTicketStateTransitions() {
        Ticket ticket = new Ticket(
            new Vehicle("ABC123", "John", VehicleType.CAR),
            new ParkingSpot("S001", SpotType.STANDARD, 1, 1)
        );
        
        assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
        ticket.exit();
        assertEquals(TicketStatus.PENDING_PAYMENT, ticket.getStatus());
        ticket.pay();
        assertEquals(TicketStatus.PAID, ticket.getStatus());
    }

    @Test
    void testSpotAllocationByType() throws Exception {
        parkingLot.addFloor(SpotType.COMPACT, 5);
        parkingLot.addFloor(SpotType.LARGE, 5);
        
        Vehicle car = new Vehicle("ABC123", "John", VehicleType.CAR);
        Vehicle truck = new Vehicle("TRK456", "Jane", VehicleType.TRUCK);
        
        Ticket carTicket = service.enterVehicle(car);
        Ticket truckTicket = service.enterVehicle(truck);
        
        assertEquals(SpotType.COMPACT, carTicket.getSpot().getSpotType());
        assertEquals(SpotType.LARGE, truckTicket.getSpot().getSpotType());
    }
}
```

## Extension Challenges

1. **Reservation System**: Allow advance spot reservations with time windows
2. **Membership Plans**: Implement monthly/annual parking subscriptions
3. **EV Charging**: Track charging status and calculate energy costs
4. **Valet Mode**: Add valet parking with car location tracking
5. **Mobile App Integration**: Design API for mobile check-in/check-out

## Interview Questions

1. **Why use the Strategy pattern for pricing?**
   - Discuss flexibility, easy addition of new pricing models, runtime switching

2. **How would you handle concurrent vehicle entry/exit?**
   - Discuss synchronization, atomic operations, thread-safe data structures

3. **What are the benefits of the State pattern for tickets?**
   - Discuss encapsulation of state-specific behavior, clean state transitions

4. **How would you optimize spot allocation for a large parking garage?**
   - Discuss spatial indexing, proximity algorithms, direction-aware allocation

5. **How would you design for a multi-floor automated parking system?**
   - Discuss robotics integration, sensor systems, real-time tracking

## References

- [Strategy Pattern](https://www.baeldung.com/java-strategy-pattern)
- [State Pattern](https://www.baeldung.com/java-state-pattern)