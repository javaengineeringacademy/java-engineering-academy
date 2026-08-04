# Law of Demeter

## Overview

The Law of Demeter (LoD), also known as the Principle of Least Knowledge, states that an object should only communicate with its immediate friends and not with strangers. Created at Northeastern University in 1987, this principle reduces coupling between components by limiting how objects interact with each other.

## Table of Contents

- [The Law](#the-law)
- [Violation Examples](#violation-examples)
- [Compliance Examples](#compliance-examples)
- [Benefits](#benefits)
- [Common Violations](#common-violations)
- [Refactoring Techniques](#refactoring-techniques)
- [Best Practices](#best-practices)

## The Law

The law states that within a method, an object should only call methods on:

1. **Itself**
2. **Parameters passed to the method**
3. **Any objects it creates**
4. **Direct component objects**

```
┌─────────────────────────────────────────────────────────────┐
│              LAW OF DEMETER RULES                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Object A can call methods on:                              │
│                                                             │
│  ✓ Itself (self)                                           │
│  ✓ Method parameters                                       │
│  ✓ Objects it creates                                      │
│  ✓ Direct components (fields/attributes)                   │
│                                                             │
│  Object A CANNOT call methods on:                          │
│                                                             │
│  ✗ Objects returned by other objects                       │
│  ✗ Objects obtained through method chains                  │
│  ✗ "Strangers" - objects not directly related              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Simple Rule

```python
# VIOLATION - Train Wreck / Method Chaining
customer.get_address().get_city()

# COMPLIANCE - Tell, Don't Ask
customer.get_city()
```

## Violation Examples

### Train Wreck Pattern

```python
# BAD - Violates Law of Demeter
class OrderProcessor:
    def get_customer_city(self, order):
        return order.customer.address.city  # Chain of calls
    
    def calculate_shipping(self, order):
        # Reaching deep into object graph
        weight = order.shipping_info.package.weight
        carrier = order.shipping_info.carrier.name
        return weight * carrier.rate_per_kg

class Order:
    def __init__(self, customer, shipping_info):
        self.customer = customer
        self.shipping_info = shipping_info

class Customer:
    def __init__(self, address):
        self.address = address

class Address:
    def __init__(self, city, country):
        self.city = city
        self.country = country
```

### Bad: Reaching into Collections

```python
# BAD - Reaching into collection elements
class ReportGenerator:
    def generate(self, order):
        city = order.customer.address.city
        
        # Reaching into collection elements
        for item in order.items:
            product_name = item.product.name
            category = item.product.category.name
            print(f"{product_name} - {category}")
        
        # More chain violations
        total = order.billing_info.calculate_total()
        tax = order.billing_info.tax_calculator.compute(total)
```

### Bad: Temporary Object Creation

```python
# BAD - Creating temporary objects to chain
class OrderService:
    def get_region(self, order):
        # Creates temporary Address object
        return Address(order.customer.zip_code).get_region()
```

## Compliance Examples

### Delegate Methods

```python
# GOOD - Complies with Law of Demeter
class Order:
    def __init__(self, customer):
        self._customer = customer
    
    def get_customer_city(self):
        """Delegate to customer"""
        return self._customer.get_city()
    
    def get_customer_name(self):
        """Delegate to customer"""
        return self._customer.get_name()
    
    def get_item_names(self):
        """Delegate to each item"""
        return [item.get_name() for item in self._items]

class Customer:
    def __init__(self, address):
        self._address = address
    
    def get_city(self):
        """Delegate to address"""
        return self._address.get_city()
    
    def get_name(self):
        return self._name

class Address:
    def __init__(self, city):
        self._city = city
    
    def get_city(self):
        return self._city
```

### Tell-Don't-Ask Pattern

```python
# BAD (Asking)
class TaxCalculator:
    def calculate_for_order(self, order):
        city = order.customer.get_city()
        state = order.customer.get_state()
        country = order.customer.get_country()
        return get_tax_rate(city, state, country)

# GOOD (Telling)
class Customer:
    def apply_tax_rules(self, order):
        """Customer knows its tax rules"""
        rate = TaxService.get_rate(self._address)
        return order.subtotal * rate

class Order:
    def calculate_tax(self):
        return self._customer.apply_tax_rules(self)
```

### Facade Pattern

```python
# GOOD - Facade provides simplified interface
class OrderFacade:
    def __init__(self, order_system):
        self._order_system = order_system
    
    def get_order_summary(self, order_id):
        """Facade hides complexity"""
        order = self._order_system.get_order(order_id)
        return {
            'customer_name': order.get_customer_name(),
            'total': order.calculate_total(),
            'status': order.get_status(),
            'items': order.get_item_count()
        }
```

## Benefits

### Reduced Coupling

```python
# HIGH COUPLING (Violation)
class OrderProcessor:
    def process(self, order):
        # Depends on: Order → Customer → Address → City
        city = order.customer.address.city
        rate = TaxService.get_rate(city)
        
        # Depends on: Order → Items → Product → Category
        for item in order.items:
            category = item.product.category.name
            discount = get_discount(category)

# LOW COUPLING (Compliance)
class OrderProcessor:
    def process(self, order):
        # Only depends on Order
        rate = order.calculate_tax()
        
        for item in order.items:
            discount = item.calculate_discount()
```

### Improved Maintainability

```python
# Changes to Address class don't affect OrderProcessor
class Address:
    def __init__(self, city, state, country, zip_code):
        self._city = city
        self._state = state
        self._country = country
        self._zip_code = zip_code
    
    def get_city(self):
        return self._city
    
    def get_state(self):
        return self._state

# OrderProcessor is unaffected by Address changes
class OrderProcessor:
    def process(self, order):
        # This still works even if Address internals change
        rate = order.calculate_tax()
```

### Better Testability

```python
# Easy to mock
class TestOrderProcessor(unittest.TestCase):
    def test_process_order(self):
        # Create mock objects
        mock_customer = Mock()
        mock_customer.get_city.return_value = "New York"
        
        mock_order = Mock()
        mock_order.get_customer_city.return_value = "New York"
        
        processor = OrderProcessor()
        result = processor.process(mock_order)
        
        self.assertEqual(result.tax_rate, 0.08)
```

### Parallel Development

```
Team A: Order domain (Order, OrderItem)
Team B: Customer domain (Customer, Address)
Team C: Product domain (Product, Category)

With LoD:
- Teams can work independently
- Changes in one domain don't affect others
- Clear interfaces between domains

Without LoD:
- Tightly coupled code
- Changes cascade across teams
- Merge conflicts and coordination overhead
```

## Common Violations

### 1. Data Transfer Objects

```python
# BAD - DTO exposes internals
class OrderDTO:
    def __init__(self):
        self.customer = CustomerDTO()
        self.items = [ItemDTO()]

# Usage violation
city = order_dto.customer.address.city

# GOOD - DTO provides accessors
class OrderDTO:
    def __init__(self):
        self._customer = CustomerDTO()
        self._items = [ItemDTO()]
    
    def get_customer_city(self):
        return self._customer.get_city()
```

### 2. ORM Objects

```python
# BAD - Accessing ORM relationships
class OrderService:
    def get_region(self, order_id):
        order = Order.query.get(order_id)
        return order.customer.address.region.name  # Chain

# GOOD - Use repository methods
class OrderRepository:
    def get_order_region(self, order_id):
        order = Order.query.get(order_id)
        return order.get_region()  # Order knows its region
```

### 3. JSON/API Responses

```python
# BAD - Chaining through nested JSON
def get_customer_city(response):
    return response['data']['customer']['address']['city']

# GOOD - Map to domain object first
class Customer:
    @classmethod
    def from_json(cls, data):
        return cls(
            name=data['name'],
            address=Address.from_json(data['address'])
        )
    
    def get_city(self):
        return self._address.get_city()
```

## Refactoring Techniques

### Extract Method

```python
# BEFORE (Violation)
def process_order(order):
    city = order.customer.address.city
    region = RegionService.get_region(city)
    tax_rate = TaxService.get_rate(region)
    return order.subtotal * tax_rate

# AFTER (Compliance)
class Order:
    def calculate_tax(self):
        return self._calculate_tax_rate() * self.subtotal
    
    def _calculate_tax_rate(self):
        return self._customer.get_tax_rate()

class Customer:
    def get_tax_rate(self):
        return self._address.get_tax_rate()

class Address:
    def get_tax_rate(self):
        return TaxService.get_rate(self.get_region())
```

### Introduce Parameter Object

```python
# BEFORE (Violation)
def calculate_shipping(order):
    weight = order.items[0].product.weight
    dimensions = order.items[0].product.dimensions
    destination = order.customer.address

# AFTER (Compliance)
class ShippingInfo:
    def __init__(self, items, destination):
        self.items = items
        self.destination = destination
    
    def calculate_weight(self):
        return sum(item.get_weight() for item in self.items)

def calculate_shipping(order):
    shipping = ShippingInfo(order.items, order.get_destination())
    return shipping.calculate_weight()
```

### Use Delegation

```python
# BEFORE (Violation)
class Order:
    def get_formatted_address(self):
        addr = self.customer.address
        return f"{addr.street}, {addr.city}, {addr.state} {addr.zip}"

# AFTER (Compliance)
class Order:
    def get_formatted_address(self):
        return self._customer.get_formatted_address()

class Customer:
    def get_formatted_address(self):
        return self._address.get_formatted_address()

class Address:
    def get_formatted_address(self):
        return f"{self._street}, {self._city}, {self._state} {self._zip}"
```

## Best Practices

### 1. Apply the "One Dot" Rule

```python
# GOOD - One dot rule
customer.get_city()
order.calculate_total()
item.get_name()

# BAD - Multiple dots
customer.address.city
order.items[0].product.name
item.product.category.name
```

### 2. Create Facade Methods

```python
class Order:
    def get_shipping_cost(self):
        """Facade method for shipping calculation"""
        return ShippingCalculator.calculate(self._get_shipping_info())
    
    def _get_shipping_info(self):
        return {
            'weight': self._calculate_total_weight(),
            'destination': self._customer.get_address()
        }
```

### 3. Use Value Objects

```python
class Money:
    def __init__(self, amount, currency):
        self.amount = amount
        self.currency = currency
    
    def add(self, other):
        if self.currency != other.currency:
            raise CurrencyMismatchError()
        return Money(self.amount + other.amount, self.currency)

# GOOD - Money handles its own operations
total = price.add(tax).add(shipping)
```

### 4. Consider Interface Segregation

```python
# Provide narrow interfaces
class CustomerInfo(ABC):
    @abstractmethod
    def get_name(self): pass
    
    @abstractmethod
    def get_city(self): pass

class CustomerDetails(ABC):
    @abstractmethod
    def get_full_profile(self): pass
    
    @abstractmethod
    def get_order_history(self): pass

# OrderService only needs CustomerInfo
class OrderService:
    def __init__(self, customer_info: CustomerInfo):
        self._customer = customer_info
```

## Further Reading

- [Principle of Least Knowledge](https://en.wikipedia.org/wiki/Principle_of_least_knowledge)
- [Law of Demeter for Python](https://docs.python-guide.org/writing/structure/)
- [Clean Code - Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
