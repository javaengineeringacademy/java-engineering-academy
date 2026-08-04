# GRASP Principles

## Overview

GRASP (General Responsibility Assignment Software Patterns) is a set of nine guidelines for assigning responsibilities to classes and objects in object-oriented design. Created by Craig Larman, GRASP provides a systematic approach to designing classes by answering the fundamental question: "What should this class do and why?"

## Table of Contents

- [The Nine Principles](#the-nine-principles)
- [Information Expert](#information-expert)
- [Creator](#creator)
- [Controller](#controller)
- [Low Coupling](#low-coupling)
- [High Cohesion](#high-cohesion)
- [Polymorphism](#polymorphism)
- [Pure Fabrication](#pure-fabrication)
- [Indirection](#indirection)
- [Protected Variations](#protected-variations)
- [Applying GRASP](#applying-grasp)

## The Nine Principles

```
┌─────────────────────────────────────────────────────────────┐
│                    GRASP PRINCIPLES                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │ Information      │  │ Creator         │                  │
│  │ Expert           │  │                 │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │ Controller       │  │ Low Coupling    │                  │
│  │                 │  │                 │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │ High Cohesion    │  │ Polymorphism    │                  │
│  │                 │  │                 │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │ Pure Fabrication │  │ Indirection     │                  │
│  │                 │  │                 │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐                                       │
│  │ Protected        │                                       │
│  │ Variations       │                                       │
│  └─────────────────┘                                       │
└─────────────────────────────────────────────────────────────┘
```

| Principle | Question Answered |
|-----------|-------------------|
| Information Expert | What is the responsibility? Assign to class with info |
| Creator | Who should create object B? |
| Controller | Who handles system events? |
| Low Coupling | How to minimize dependencies? |
| High Cohesion | How to keep classes focused? |
| Polymorphism | How to handle alternatives? |
| Pure Fabrication | What if Expert gives low cohesion? |
| Indirection | How to decouple components? |
| Protected Variations | How to shield from variations? |

## Information Expert

**Principle**: Assign a responsibility to the class with the information needed to fulfill it.

### Example

```python
# Information Expert: Order knows about its items
class Order:
    def __init__(self, items):
        self.items = items
    
    def calculate_total(self):
        # Order has the information about items
        return sum(item.price * item.quantity for item in self.items)
    
    def get_item_count(self):
        return len(self.items)
    
    def has_high_value_items(self):
        return any(item.price > 100 for item in self.items)

class OrderItem:
    def __init__(self, product, quantity, price):
        self.product = product
        self.quantity = quantity
        self.price = price
    
    def get_subtotal(self):
        # Item has information about its own calculation
        return self.price * self.quantity
```

### Bad Example (Information Expert Violation)

```python
# Bad: Calculator has to know about Order internals
class OrderCalculator:
    def calculate_total(self, order):
        total = 0
        for item in order.items:  # Reaching into Order
            total += item.price * item.quantity
        return total

# Good: Order has the information
class Order:
    def calculate_total(self):
        return sum(item.get_subtotal() for item in self.items)
```

## Creator

**Principle**: Assign class B the responsibility to create instances of class A if B contains, records, uses, or closely uses instances of A.

### Example

```python
# ShoppingCart creates CartItems (it contains them)
class ShoppingCart:
    def __init__(self):
        self.items = []
    
    def add_item(self, product, quantity):
        # ShoppingCart creates CartItems
        item = CartItem(product, quantity)
        self.items.append(item)
        return item
    
    def remove_item(self, product):
        self.items = [i for i in self.items if i.product != product]

class CartItem:
    def __init__(self, product, quantity):
        self.product = product
        self.quantity = quantity
    
    @property
    def subtotal(self):
        return self.product.price * self.quantity
```

### Aggregation Creator

```python
class OrderFactory:
    def create_order(self, customer, items):
        # Factory creates Order
        order = Order(customer)
        for item_data in items:
            order.add_item(item_data['product'], item_data['quantity'])
        return order
    
    def create_order_from_cart(self, cart, customer):
        # Convert cart to order
        order = Order(customer)
        for item in cart.items:
            order.add_item(item.product, item.quantity)
        return order
```

## Controller

**Principle**: Assign responsibility for handling a system event to a class representing a whole system, device, or subsystem.

### System Controller

```python
class OrderController:
    """Handles all order-related system events"""
    
    def __init__(self, order_service, payment_service, inventory_service):
        self.order_service = order_service
        self.payment_service = payment_service
        self.inventory_service = inventory_service
    
    def handle_create_order(self, request):
        """Handle create order event"""
        # Validate input
        self.validate_order_request(request)
        
        # Check inventory
        if not self.inventory_service.check_availability(request.items):
            raise InsufficientInventoryError()
        
        # Create order
        order = self.order_service.create_order(
            customer_id=request.customer_id,
            items=request.items
        )
        
        # Process payment
        payment_result = self.payment_service.process_payment(
            order_id=order.id,
            amount=order.total
        )
        
        if payment_result.success:
            self.order_service.confirm_order(order.id)
            return OrderResponse(success=True, order_id=order.id)
        else:
            self.order_service.cancel_order(order.id)
            return OrderResponse(success=False, error=payment_result.error)
    
    def validate_order_request(self, request):
        if not request.customer_id:
            raise ValidationError("Customer ID required")
        if not request.items:
            raise ValidationError("Order must have items")
```

### Use Case Controller

```python
class PlaceOrderUseCase:
    """Use case controller for placing orders"""
    
    def __init__(self, repository, payment_gateway, notification_service):
        self.repository = repository
        self.payment = payment_gateway
        self.notifications = notification_service
    
    def execute(self, order_data):
        # 1. Create order
        order = Order.create(order_data)
        
        # 2. Validate
        order.validate()
        
        # 3. Process payment
        payment = self.payment.charge(order.total)
        
        # 4. Save
        self.repository.save(order)
        
        # 5. Notify
        self.notifications.send_order_confirmation(order)
        
        return order
```

## Low Coupling

**Principle**: Assign responsibility so that coupling remains low. Minimize dependencies between classes.

### Example

```python
# High Coupling (Bad)
class OrderProcessor:
    def __init__(self):
        self.db = Database()  # Direct dependency
        self.email = EmailService()  # Direct dependency
        self.logger = Logger()  # Direct dependency
    
    def process_order(self, order):
        self.db.save(order)
        self.email.send_confirmation(order)
        self.logger.log("Order processed")

# Low Coupling (Good)
class OrderProcessor:
    def __init__(self, repository, notifier, logger):
        # Dependency injection - depends on abstractions
        self.repository = repository
        self.notifier = notifier
        self.logger = logger
    
    def process_order(self, order):
        self.repository.save(order)
        self.notifier.notify(order)
        self.logger.log("Order processed")
```

### Reducing Coupling

```python
# Reduce coupling via interfaces
from abc import ABC, abstractmethod

class Repository(ABC):
    @abstractmethod
    def save(self, entity): pass
    
    @abstractmethod
    def find(self, id): pass

class OrderRepository(Repository):
    def save(self, order):
        # Database-specific implementation
        pass
    
    def find(self, order_id):
        pass

# OrderProcessor depends on abstraction, not implementation
class OrderProcessor:
    def __init__(self, repository: Repository):
        self.repository = repository
```

## High Cohesion

**Principle**: Assign responsibility so that cohesion remains high. Keep classes focused on a single purpose.

### Example

```python
# Low Cohesion (Bad)
class OrderManager:
    def create_order(self, data): pass
    def save_to_database(self, order): pass
    def send_email(self, order): pass
    def generate_report(self, orders): pass
    def validate_payment(self, payment): pass
    def update_inventory(self, items): pass

# High Cohesion (Good)
class OrderService:
    def create_order(self, data): pass
    def cancel_order(self, order_id): pass

class OrderRepository:
    def save(self, order): pass
    def find(self, id): pass

class NotificationService:
    def send_order_confirmation(self, order): pass
    def send_shipping_notification(self, order): pass

class ReportGenerator:
    def generate_order_report(self, orders): pass

class PaymentValidator:
    def validate(self, payment): pass

class InventoryService:
    def reserve(self, items): pass
    def release(self, items): pass
```

### Calculating Cohesion

```
Cohesion = (related methods / total methods) * 100

OrderService (high cohesion):
- create_order (related)
- cancel_order (related)
- get_order (related)
= 100% cohesion

OrderManager (low cohesion):
- create_order (related)
- send_email (unrelated)
- generate_report (unrelated)
= 33% cohesion
```

## Polymorphism

**Principle**: Use polymorphism to handle alternatives based on type.

### Example

```python
# Without Polymorphism (Bad)
class PaymentProcessor:
    def process(self, payment_type, amount):
        if payment_type == 'credit':
            return self.process_credit(amount)
        elif payment_type == 'debit':
            return self.process_debit(amount)
        elif payment_type == 'paypal':
            return self.process_paypal(amount)
        else:
            raise ValueError(f"Unknown type: {payment_type}")

# With Polymorphism (Good)
from abc import ABC, abstractmethod

class PaymentProcessor(ABC):
    @abstractmethod
    def process(self, amount): pass
    
    @abstractmethod
    def refund(self, transaction_id): pass

class CreditCardProcessor(PaymentProcessor):
    def process(self, amount):
        return self._charge_card(amount)
    
    def refund(self, transaction_id):
        return self._reverse_charge(transaction_id)

class DebitCardProcessor(PaymentProcessor):
    def process(self, amount):
        return self._debit_account(amount)
    
    def refund(self, transaction_id):
        return self._credit_account(transaction_id)

class PayPalProcessor(PaymentProcessor):
    def process(self, amount):
        return self._paypal_charge(amount)
    
    def refund(self, transaction_id):
        return self._paypal_refund(transaction_id)

# Usage - open for extension, closed for modification
def process_payment(processor: PaymentProcessor, amount):
    return processor.process(amount)
```

## Pure Fabrication

**Principle**: When Information Expert leads to low cohesion, create an artificial class to maintain high cohesion.

### Example

```python
# Information Expert solution (low cohesion)
class Order:
    def __init__(self, items):
        self.items = items
    
    def calculate_tax(self):
        # Order shouldn't know about tax calculation rules
        tax = 0
        for item in self.items:
            if item.taxable:
                tax += item.subtotal * 0.08
        return tax
    
    def generate_shipping_label(self):
        # Order shouldn't know about shipping
        address = self.shipping_address
        return f"{address.name}\n{address.street}\n{address.city}"
    
    def calculate_discount(self):
        # Order shouldn't know about discount rules
        if self.customer.is_vip:
            return self.subtotal * 0.1
        return 0

# Pure Fabrication (better)
class TaxCalculator:
    def calculate(self, order):
        return sum(
            item.subtotal * 0.08 
            for item in order.items 
            if item.taxable
        )

class ShippingLabelGenerator:
    def generate(self, order):
        address = order.shipping_address
        return f"{address.name}\n{address.street}\n{address.city}"

class DiscountCalculator:
    def calculate(self, order):
        if order.customer.is_vip:
            return order.subtotal * 0.1
        return 0

class Order:
    def __init__(self, items, customer, shipping_address):
        self.items = items
        self.customer = customer
        self.shipping_address = shipping_address
    
    def calculate_tax(self):
        return TaxCalculator().calculate(self)
    
    def generate_shipping_label(self):
        return ShippingLabelGenerator().generate(self)
    
    def calculate_discount(self):
        return DiscountCalculator().calculate(self)
```

## Indirection

**Principle**: Use an intermediary to decouple components.

### Example

```python
# Direct Coupling (Bad)
class OrderUI:
    def __init__(self):
        self.database = PostgreSQLDatabase()  # Direct dependency
        self.cache = RedisCache()  # Direct dependency
    
    def save_order(self, order):
        self.database.insert(order)
        self.cache.set(f"order:{order.id}", order)

# With Indirection (Good)
class OrderRepository:
    def __init__(self, database, cache):
        self.database = database
        self.cache = cache
    
    def save(self, order):
        self.database.insert(order)
        self.cache.set(f"order:{order.id}", order)
    
    def find(self, order_id):
        cached = self.cache.get(f"order:{order_id}")
        if cached:
            return cached
        return self.database.query(order_id)

class OrderUI:
    def __init__(self, repository):
        self.repository = repository  # Depends on abstraction
    
    def save_order(self, order):
        self.repository.save(order)
```

### Repository Pattern as Indirection

```python
# Indirection between domain and persistence
class OrderService:
    def __init__(self, order_repository):
        self.repository = order_repository
    
    def get_order(self, order_id):
        return self.repository.find(order_id)
    
    def save_order(self, order):
        self.repository.save(order)

# Repository hides persistence details
class OrderRepository:
    def find(self, order_id):
        # Could be database, file, API, etc.
        return self.data_source.get(order_id)
    
    def save(self, order):
        self.data_source.put(order.id, order)
```

## Protected Variations

**Principle**: Protect elements from variations in other elements.

### Example

```python
# Without Protection (Fragile)
class OrderService:
    def process(self, order):
        # Direct dependency on external API
        stripe.charge(order.total)  # Fragile to API changes
        sendgrid.send_email(order.customer.email)  # Fragile

# With Protection (Robust)
class PaymentGateway(ABC):
    @abstractmethod
    def charge(self, amount, payment_method): pass

class NotificationService(ABC):
    @abstractmethod
    def send(self, recipient, message): pass

class StripeGateway(PaymentGateway):
    def charge(self, amount, payment_method):
        return stripe.Charge.create(amount=amount, source=payment_method)

class SendGridNotification(NotificationService):
    def send(self, recipient, message):
        sendgrid.send(to=recipient, body=message)

class OrderService:
    def __init__(self, payment_gateway: PaymentGateway, 
                 notification_service: NotificationService):
        self.payment = payment_gateway
        self.notifications = notification_service
    
    def process(self, order):
        self.payment.charge(order.total, order.payment_method)
        self.notifications.send(order.customer.email, "Order confirmed")
```

### Adapter Pattern as Protection

```python
# Protect from external system changes
class LegacySystemAdapter:
    def __init__(self, legacy_client):
        self.legacy = legacy_client
    
    def get_customer(self, customer_id):
        # Translate between systems
        legacy_data = self.legacy.fetch_customer(customer_id)
        return Customer(
            id=legacy_data['CUST_ID'],
            name=legacy_data['CUST_NAME'],
            email=legacy_data['EMAIL_ADDR']
        )

class OrderService:
    def __init__(self, customer_adapter):
        self.customer_adapter = customer_adapter
    
    def process_order(self, order):
        customer = self.customer_adapter.get_customer(order.customer_id)
        # Process with clean domain model
```

## Applying GRASP

### Design Process

```
1. Identify responsibilities needed
   ↓
2. Apply Information Expert
   ↓
3. Check coupling/cohesion
   ↓
4. If low cohesion → Pure Fabrication
   ↓
5. If high coupling → Indirection
   ↓
6. Apply other patterns as needed
   ↓
7. Validate with Protected Variations
```

### Complete Example

```python
# Using multiple GRASP principles

class Order:
    """Information Expert for order data"""
    def __init__(self, customer_id, items):
        self.id = str(uuid.uuid4())
        self.customer_id = customer_id
        self.items = items
        self.status = 'pending'
    
    def calculate_total(self):
        """Information Expert - has item information"""
        return sum(item.price * item.quantity for item in self.items)
    
    def add_item(self, item):
        """Creator - creates relationship"""
        self.items.append(item)
    
    def can_be_shipped(self):
        """Information Expert - has status information"""
        return self.status == 'confirmed'

class OrderService:
    """Controller - handles order events"""
    def __init__(self, repository, payment_gateway, notification):
        """Low Coupling - depends on abstractions"""
        self.repository = repository
        self.payment = payment_gateway
        self.notification = notification
    
    def place_order(self, order_data):
        """Controller pattern - orchestrates operation"""
        # Creator
        order = Order(order_data['customer_id'], order_data['items'])
        
        # Information Expert
        total = order.calculate_total()
        
        # Protected Variations - payment gateway abstraction
        payment_result = self.payment.charge(total)
        
        if payment_result.success:
            order.status = 'confirmed'
            self.repository.save(order)
            self.notification.send_order_confirmation(order)
            return order
        
        raise PaymentFailedError()

class OrderRepository:
    """Pure Fabrication - persistence concern"""
    def __init__(self, database):
        self.database = database
    
    def save(self, order):
        self.database.insert('orders', order.__dict__)
    
    def find(self, order_id):
        data = self.database.query('orders', order_id)
        return Order(**data) if data else None
```

## Further Reading

- [Applying UML and Patterns - Craig Larman](https://www.amazon.com/Applying-UML-Patterns-Introduction-Object-Oriented/dp/0131489062)
- [GRASP Principles Explained](https://www.cs.sjsu.edu/~pearce/modules/oo/grasp.htm)
- [Object-Oriented Design Heuristics - Arthur Riel](https://www.amazon.com/Object-Oriented-Design-Heuristics-Insights-Improving/dp/020163385X)
