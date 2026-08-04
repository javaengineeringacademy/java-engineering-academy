# Coupling and Cohesion

## Overview

Coupling and cohesion are fundamental design principles that measure the quality of object-oriented designs. They describe how classes relate to each other (coupling) and how focused a class is on its responsibilities (cohesion). Understanding these concepts is essential for creating maintainable, flexible software.

## Table of Contents

- [Coupling](#coupling)
- [Types of Coupling](#types-of-coupling)
- [Cohesion](#cohesion)
- [Types of Cohesion](#types-of-cohesion)
- [Relationship](#relationship)
- [Measurement](#measurement)
- [Improving Design](#improving-design)
- [Best Practices](#best-practices)

## Coupling

Coupling measures the degree of interdependence between modules.

```
┌─────────────────────────────────────────────────────────────┐
│                    COUPLING SPECTRUM                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  TIGHT ◄───────────────────────────────────► LOOSE          │
│                                                             │
│  Content    Common    External    Control   Data    Message  │
│  Coupling   Coupling  Coupling    Coupling  Coupling Coupling│
│                                                             │
│  [Bad] ──────────────────────────────────────► [Good]       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Tight Coupling

```python
# TIGHT COUPLING - Direct dependencies
class OrderProcessor:
    def __init__(self):
        self.db = PostgreSQLDatabase()  # Direct dependency
        self.email = GmailService()  # Direct dependency
        self.cache = RedisCache()  # Direct dependency
    
    def process_order(self, order):
        self.db.save(order)
        self.email.send_confirmation(order)
        self.cache.invalidate(f"order:{order.id}")

class PostgreSQLDatabase:
    def save(self, order):
        # Database-specific implementation
        cursor = self.conn.cursor()
        cursor.execute("INSERT INTO orders VALUES (%s, %s)", 
                      (order.id, order.total))
```

### Loose Coupling

```python
# LOOSE COUPLING - Depends on abstractions
from abc import ABC, abstractmethod

class Repository(ABC):
    @abstractmethod
    def save(self, entity): pass

class NotificationService(ABC):
    @abstractmethod
    def send(self, recipient, message): pass

class CacheService(ABC):
    @abstractmethod
    def invalidate(self, key): pass

class OrderProcessor:
    def __init__(self, repository: Repository, 
                 notifier: NotificationService,
                 cache: CacheService):
        self._repository = repository
        self._notifier = notifier
        self._cache = cache
    
    def process_order(self, order):
        self._repository.save(order)
        self._notifier.send(order.customer_email, "Order confirmed")
        self._cache.invalidate(f"order:{order.id}")

# Dependencies can be swapped
class InMemoryRepository(Repository):
    def save(self, entity):
        self.store[entity.id] = entity

class SQSNotificationService(NotificationService):
    def send(self, recipient, message):
        # AWS SQS implementation
        pass
```

## Types of Coupling

### Content Coupling (Worst)

```python
# Content coupling - accessing internal implementation
class Order:
    def __init__(self):
        self._items = []  # Private
        self._status = None

class OrderManager:
    def process(self, order):
        # Directly accessing private state
        order._items.append(item)  # BAD
        order._status = "processed"  # BAD
```

### Common Coupling

```python
# Common coupling - shared global state
GLOBAL_DATABASE = None

class OrderService:
    def save(self, order):
        GLOBAL_DATABASE.save(order)  # Uses global

class CustomerService:
    def save(self, customer):
        GLOBAL_DATABASE.save(customer)  # Uses same global
```

### External Coupling

```python
# External coupling - shared external interface
class OrderAPI:
    def get_orders(self):
        # Depends on specific external API format
        response = requests.get("https://api.example.com/orders")
        return response.json()['data']['orders']  # Specific format

class CustomerAPI:
    def get_customers(self):
        # Same external dependency
        response = requests.get("https://api.example.com/customers")
        return response.json()['data']['customers']  # Same format
```

### Control Coupling

```python
# Control coupling - passing control information
class OrderProcessor:
    def process(self, order, flag):
        # Flag controls behavior
        if flag == 'EXPRESS':
            self.process_express(order)
        elif flag == 'STANDARD':
            self.process_standard(order)

# Better: Use polymorphism instead
class ProcessingStrategy(ABC):
    @abstractmethod
    def process(self, order): pass

class ExpressProcessing(ProcessingStrategy):
    def process(self, order):
        pass

class StandardProcessing(ProcessingStrategy):
    def process(self, order):
        pass
```

### Data Coupling

```python
# Data coupling - passing data via parameters
class OrderProcessor:
    def process(self, customer_id, order_items, total):
        # Only receives necessary data
        order = Order(customer_id, order_items, total)
        self.save(order)
        self.notify(customer_id)

class EmailService:
    def send_order_confirmation(self, customer_email, order_id, total):
        # Only receives necessary data
        send_email(customer_email, f"Order {order_id} confirmed: ${total}")
```

### Message Coupling (Best)

```python
# Message coupling - asynchronous communication
class OrderService:
    def __init__(self, message_bus):
        self._bus = message_bus
    
    def create_order(self, order):
        self.save(order)
        self._bus.publish('order.created', {
            'order_id': order.id,
            'customer_id': order.customer_id
        })

class InventoryService:
    def __init__(self, message_bus):
        self._bus = message_bus
        self._bus.subscribe('order.created', self.handle_order_created)
    
    def handle_order_created(self, event):
        # Reacts to events, no direct coupling
        self.reserve_inventory(event['order_id'])
```

## Cohesion

Cohesion measures how focused and related the responsibilities of a module are.

```
┌─────────────────────────────────────────────────────────────┐
│                    COHESION SPECTRUM                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  LOW ◄──────────────────────────────────────► HIGH          │
│                                                             │
│  Coincidental  Logical  Temporal  Procedural Functional     │
│  Cohesion      Cohesion Cohesion  Cohesion   Cohesion       │
│                                                             │
│  [Bad] ──────────────────────────────────────► [Good]       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### High Cohesion

```python
# HIGH COHESION - Focused responsibility
class OrderRepository:
    """Only handles order persistence"""
    
    def __init__(self, database):
        self._database = database
    
    def save(self, order):
        self._database.insert('orders', order.to_dict())
    
    def find_by_id(self, order_id):
        data = self._database.query('orders', order_id)
        return Order.from_dict(data) if data else None
    
    def find_by_customer(self, customer_id):
        data = self._database.query('orders', 
                                     {'customer_id': customer_id})
        return [Order.from_dict(d) for d in data]

class OrderValidator:
    """Only handles order validation"""
    
    def validate(self, order):
        errors = []
        if not order.customer_id:
            errors.append("Customer ID required")
        if not order.items:
            errors.append("Order must have items")
        if order.total <= 0:
            errors.append("Total must be positive")
        return errors
```

### Low Cohesion

```python
# LOW COHESION - Unrelated responsibilities
class OrderManager:
    """Handles everything - low cohesion"""
    
    def create_order(self, data):
        # Validation
        if not data.get('customer_id'):
            raise ValueError("Customer ID required")
        
        # Database operations
        db = Database()
        db.connect()
        db.insert('orders', data)
        
        # Email notification
        smtp = smtplib.SMTP('smtp.gmail.com')
        smtp.sendmail(data['email'], "Order created")
        
        # Report generation
        report = self.generate_report(data)
        pdf = self.create_pdf(report)
        
        # Logging
        logging.info(f"Order created: {data}")
```

## Types of Cohesion

### Coincidental (Worst)

```python
# Coincidental cohesion - unrelated functions grouped together
class Utils:
    def calculate_tax(self, amount):
        pass
    
    def send_email(self, to, subject):
        pass
    
    def parse_csv(self, filename):
        pass
    
    def sort_list(self, items):
        pass
```

### Logical Cohesion

```python
# Logical cohesion - logically related but different purposes
class IOOperations:
    def read_file(self, path):
        pass
    
    def write_file(self, path, data):
        pass
    
    def read_database(self, query):
        pass
    
    def write_database(self, table, data):
        pass
```

### Temporal Cohesion

```python
# Temporal cohesion - executed at same time
class StartupTasks:
    def initialize_database(self):
        pass
    
    def load_configuration(self):
        pass
    
    def start_background_workers(self):
        pass
    
    def setup_logging(self):
        pass
```

### Procedural Cohesion

```python
# Procedural cohesion - steps in procedure
class OrderProcessor:
    def validate_order(self, order):
        pass
    
    def calculate_total(self, order):
        pass
    
    def apply_discounts(self, order):
        pass
    
    def save_order(self, order):
        pass
```

### Informational Cohesion (High)

```python
# Informational cohesion - operates on same data
class Order:
    def __init__(self, data):
        self.data = data
    
    def calculate_total(self):
        return sum(item['price'] for item in self.data['items'])
    
    def get_status(self):
        return self.data['status']
    
    def update_status(self, status):
        self.data['status'] = status
```

### Functional Cohesion (Best)

```python
# Functional cohesion - single, well-defined purpose
class TaxCalculator:
    """Single responsibility: calculate taxes"""
    
    def calculate(self, order):
        return order.subtotal * self._get_tax_rate(order)
    
    def _get_tax_rate(self, order):
        if order.is_tax_exempt:
            return 0
        return TaxService.get_rate(order.customer.get_state())

class ShippingCalculator:
    """Single responsibility: calculate shipping"""
    
    def calculate(self, order):
        weight = sum(item.weight for item in order.items)
        distance = self._get_distance(order)
        return weight * distance * self._get_rate()
```

## Relationship

```
┌─────────────────────────────────────────────────────────────┐
│          COUPLING vs COHESION RELATIONSHIP                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  High Cohesion ──────► Low Coupling                        │
│                                                             │
│  When classes have focused responsibilities:                │
│  • They depend on fewer other classes                       │
│  • Dependencies are clearer                                 │
│  • Changes are localized                                    │
│                                                             │
│  Low Cohesion ───────► High Coupling                       │
│                                                             │
│  When classes do too much:                                  │
│  • They depend on many other classes                        │
│  • Dependencies are unclear                                 │
│  • Changes cascade                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Example

```python
# LOW COHESION → HIGH COUPLING
class OrderManager:
    def process_order(self, order):
        # Depends on many unrelated classes
        db = Database()
        email = EmailService()
        cache = CacheService()
        logger = Logger()
        report = ReportGenerator()
        
        db.save(order)
        email.send(order)
        cache.invalidate(order.id)
        logger.log(order)
        report.generate(order)

# HIGH COHESION → LOW COUPLING
class OrderService:
    def __init__(self, repository):
        self._repository = repository  # One dependency
    
    def process_order(self, order):
        self._repository.save(order)

class NotificationService:
    def __init__(self, email_client):
        self._email = email_client  # One dependency
    
    def send_confirmation(self, order):
        self._email.send(order.customer_email, "Confirmed")
```

## Measurement

### Coupling Metrics

```python
# Count dependencies
class OrderProcessor:
    def __init__(self):
        self.repo = OrderRepository()      # +1
        self.cache = CacheService()        # +1
        self.notifier = NotificationService() # +1
    
    def process(self, order):
        self.repo.save(order)       # Uses repo
        self.cache.set(order.id)    # Uses cache
        self.notifier.send(order)   # Uses notifier

# Afferent Coupling (Ca): Classes that depend on this
# Efferent Coupling (Ce): Classes this depends on
# Instability: Ce / (Ca + Ce)
```

### Cohesion Metrics

```python
# Lack of Cohesion of Methods (LCM)
# LCM = 1 - (cohesion score)
# Lower LCM = Better cohesion

class Order:
    def __init__(self):
        self.items = []
        self.total = 0
    
    # These methods use same instance variables → High cohesion
    def add_item(self, item):
        self.items.append(item)
        self.total += item.price
    
    def remove_item(self, item):
        self.items.remove(item)
        self.total -= item.price
```

## Improving Design

### Reduce Coupling

```python
# 1. Dependency Injection
class OrderService:
    def __init__(self, repository):  # Inject dependency
        self._repository = repository

# 2. Interface Segregation
class Readable(ABC):
    @abstractmethod
    def read(self): pass

class Writable(ABC):
    @abstractmethod
    def write(self, data): pass

# 3. Event-Driven
class OrderService:
    def create_order(self, order):
        self.save(order)
        self._event_bus.publish('order.created', order)

# 4. Facade Pattern
class OrderFacade:
    def __init__(self, order_system):
        self._system = order_system
    
    def get_summary(self, order_id):
        return self._system.get_order(order_id).to_summary()
```

### Increase Cohesion

```python
# 1. Extract Class
class OrderProcessor:
    # Before: Mixed responsibilities
    def process_order(self, order): pass
    def send_email(self, order): pass
    def generate_report(self, order): pass

# After: Focused classes
class OrderProcessor:
    def process_order(self, order): pass

class OrderNotifier:
    def send_confirmation(self, order): pass

class OrderReporter:
    def generate_report(self, order): pass

# 2. Move Method
class Order:
    def calculate_tax(self):
        # Tax calculation doesn't belong here
        pass

class TaxCalculator:
    def calculate(self, order):
        # Moved to dedicated class
        pass
```

## Best Practices

### 1. Apply SOLID Principles

```python
# SRP: Single Responsibility → High Cohesion
class OrderValidator:
    def validate(self, order): pass

class OrderProcessor:
    def process(self, order): pass

# DIP: Dependency Inversion → Low Coupling
class OrderService:
    def __init__(self, repository: OrderRepositoryInterface):
        self._repository = repository
```

### 2. Use Interfaces

```python
from abc import ABC, abstractmethod

class Repository(ABC):
    @abstractmethod
    def save(self, entity): pass

class OrderRepository(Repository):
    def save(self, order):
        # Implementation
        pass

# Code depends on interface, not implementation
class OrderService:
    def __init__(self, repository: Repository):
        self._repository = repository
```

### 3. Apply Law of Demeter

```python
# Don't chain calls
customer.get_city()  # Good
customer.address.city  # Bad
```

### 4. Measure and Monitor

```python
# Track coupling/cohesion metrics
metrics = {
    'coupling': calculate_coupling(classes),
    'cohesion': calculate_cohesion(classes),
    'instability': calculate_instability(modules)
}

# Alert on degradation
if metrics['instability'] > 0.5:
    alert("High instability detected")
```

## Further Reading

- [Object-Oriented Software Construction - Bertrand Meyer](https://www.amazon.com/Object-Oriented-Software-Construction-2nd/dp/0136211294)
- [Designing Object-Oriented C++ Software - Robert C. Martin](https://www.amazon.com/Designing-Object-Oriented-Software-Software/dp/0136298427)
- [Clean Architecture - Robert C. Martin](https://www.amazon.com/Clean-Architecture-Craftsmans-Software-Structure/dp/0134494164)
