"""
Module 11 - Design Patterns: Strategy Solutions
Complete solutions with explanations
"""

import heapq
import time
from abc import ABC, abstractmethod


# =============================================================================
# Exercise 1: Basic Strategy - SOLUTION
# =============================================================================

class SortStrategy(ABC):
    """Strategy interface for sorting."""
    
    @abstractmethod
    def sort(self, data):
        pass


class BubbleSortStrategy(SortStrategy):
    """Bubble sort implementation - O(n²)."""
    
    def sort(self, data):
        """Sort using bubble sort algorithm."""
        result = data.copy()
        n = len(result)
        for i in range(n):
            for j in range(0, n - i - 1):
                if result[j] > result[j + 1]:
                    result[j], result[j + 1] = result[j + 1], result[j]
        return result


class QuickSortStrategy(SortStrategy):
    """Quick sort implementation - O(n log n) average."""
    
    def sort(self, data):
        """Sort using quick sort algorithm."""
        if len(data) <= 1:
            return data
        pivot = data[len(data) // 2]
        left = [x for x in data if x < pivot]
        middle = [x for x in data if x == pivot]
        right = [x for x in data if x > pivot]
        return self.sort(left) + middle + self.sort(right)


class Sorter:
    """Context class that uses sorting strategy."""
    
    def __init__(self, strategy=None):
        self._strategy = strategy or BubbleSortStrategy()
    
    @property
    def strategy(self):
        return self._strategy
    
    @strategy.setter
    def strategy(self, strategy):
        """Set the sorting strategy."""
        self._strategy = strategy
    
    def sort(self, data):
        """Use current strategy to sort data."""
        return self._strategy.sort(data)


# =============================================================================
# Exercise 2: Pricing Strategy - SOLUTION
# =============================================================================

class PricingStrategy(ABC):
    """Strategy interface for pricing."""
    
    @abstractmethod
    def calculate_price(self, base_price, quantity):
        pass


class RegularPricing(PricingStrategy):
    """Regular pricing with no discount."""
    
    def calculate_price(self, base_price, quantity):
        return base_price * quantity


class BulkPricing(PricingStrategy):
    """Bulk pricing with quantity discount (10% off for 10+ items)."""
    
    def calculate_price(self, base_price, quantity):
        total = base_price * quantity
        if quantity >= 10:
            total *= 0.90  # 10% discount
        return total


class MemberPricing(PricingStrategy):
    """Member pricing with percentage discount."""
    
    def calculate_price(self, base_price, quantity, member_discount=0.15):
        total = base_price * quantity
        total *= (1 - member_discount)
        return total


class ShoppingCart:
    """Context class that uses pricing strategy."""
    
    def __init__(self, pricing_strategy=None):
        self._pricing_strategy = pricing_strategy or RegularPricing()
        self._items = []
    
    def add_item(self, name, price, quantity=1):
        """Add an item to the cart."""
        self._items.append({'name': name, 'price': price, 'quantity': quantity})
    
    def set_pricing_strategy(self, strategy):
        """Set pricing strategy."""
        self._pricing_strategy = strategy
    
    def calculate_total(self):
        """Calculate total using current strategy."""
        total = 0
        for item in self._items:
            total += self._pricing_strategy.calculate_price(
                item['price'], item['quantity']
            )
        return total


# =============================================================================
# Exercise 3: Validation Strategy - SOLUTION
# =============================================================================

import re

class ValidationStrategy(ABC):
    """Strategy interface for validation."""
    
    @abstractmethod
    def validate(self, value):
        pass


class EmailValidator(ValidationStrategy):
    """Validate email format using regex."""
    
    def validate(self, value):
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, str(value)))


class PhoneValidator(ValidationStrategy):
    """Validate phone number format."""
    
    def validate(self, value):
        # Accept formats: 1234567890, 123-456-7890, (123) 456-7890
        cleaned = re.sub(r'[\s\-\(\)]', '', str(value))
        return cleaned.isdigit() and len(cleaned) == 10


class AgeValidator(ValidationStrategy):
    """Validate age within specified range."""
    
    def validate(self, value, min_age=0, max_age=150):
        try:
            age = int(value)
            return min_age <= age <= max_age
        except (ValueError, TypeError):
            return False


class Validator:
    """Context class that uses validation strategy."""
    
    def __init__(self, strategy=None):
        self._strategy = strategy
    
    def set_strategy(self, strategy):
        """Set validation strategy."""
        self._strategy = strategy
    
    def validate(self, value, **kwargs):
        """Validate using current strategy."""
        if self._strategy is None:
            raise ValueError("No validation strategy set")
        
        # Handle validators with extra parameters
        if isinstance(self._strategy, AgeValidator):
            return self._strategy.validate(value, **kwargs)
        return self._strategy.validate(value)


# =============================================================================
# Exercise 4: Compression Strategy - SOLUTION
# =============================================================================

import gzip
import zipfile
import io

class CompressionStrategy(ABC):
    """Strategy interface for compression."""
    
    @abstractmethod
    def compress(self, data):
        pass


class ZipCompression(CompressionStrategy):
    """ZIP compression implementation."""
    
    def compress(self, data):
        """Compress data using ZIP format."""
        if isinstance(data, str):
            data = data.encode('utf-8')
        
        buffer = io.BytesIO()
        with zipfile.ZipFile(buffer, 'w', zipfile.ZIP_DEFLATED) as zf:
            zf.writestr('data', data)
        return buffer.getvalue()


class GzipCompression(CompressionStrategy):
    """GZIP compression implementation."""
    
    def compress(self, data):
        """Compress data using GZIP format."""
        if isinstance(data, str):
            data = data.encode('utf-8')
        return gzip.compress(data)


class NoCompression(CompressionStrategy):
    """No compression - returns data as-is."""
    
    def compress(self, data):
        """Return data without compression."""
        return data


class FileManager:
    """Context class that uses compression strategy."""
    
    def __init__(self, compression_strategy=None):
        self._compression = compression_strategy or NoCompression()
    
    def set_compression(self, strategy):
        """Set compression strategy."""
        self._compression = strategy
    
    def compress_data(self, data):
        """Compress data using current strategy."""
        return self._compression.compress(data)


# =============================================================================
# Exercise 5: Strategy with Factory - SOLUTION
# =============================================================================

class PaymentStrategy(ABC):
    """Strategy interface for payment."""
    
    @abstractmethod
    def pay(self, amount):
        pass


class CreditCardPayment(PaymentStrategy):
    """Credit card payment implementation."""
    
    def __init__(self, card_number):
        self.card_number = card_number
    
    def pay(self, amount):
        """Process credit card payment."""
        masked = f"****-****-****-{self.card_number[-4:]}"
        return {
            'method': 'credit_card',
            'card': masked,
            'amount': amount,
            'status': 'success'
        }


class PayPalPayment(PaymentStrategy):
    """PayPal payment implementation."""
    
    def __init__(self, email):
        self.email = email
    
    def pay(self, amount):
        """Process PayPal payment."""
        return {
            'method': 'paypal',
            'email': self.email,
            'amount': amount,
            'status': 'success'
        }


class BankTransferPayment(PaymentStrategy):
    """Bank transfer payment implementation."""
    
    def __init__(self, account_number):
        self.account_number = account_number
    
    def pay(self, amount):
        """Process bank transfer."""
        masked = f"****{self.account_number[-4:]}"
        return {
            'method': 'bank_transfer',
            'account': masked,
            'amount': amount,
            'status': 'pending'
        }


class PaymentProcessor:
    """Factory that creates payment strategies."""
    _strategies = {}
    
    @classmethod
    def register(cls, name, strategy_class):
        """Register a payment strategy class."""
        cls._strategies[name] = strategy_class
    
    @classmethod
    def create(cls, name, **kwargs):
        """Create and return payment strategy instance."""
        if name not in cls._strategies:
            raise ValueError(f"Unknown payment method: {name}")
        return cls._strategies[name](**kwargs)
    
    @classmethod
    def process_payment(cls, name, amount, **kwargs):
        """Create strategy and process payment."""
        strategy = cls.create(name, **kwargs)
        return strategy.pay(amount)


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Strategy Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Strategy")
    sorter = Sorter(BubbleSortStrategy())
    data = [3, 1, 4, 1, 5, 9, 2, 6]
    result = sorter.sort(data.copy())
    assert result == sorted(data), "Bubble sort should work"
    
    sorter.strategy = QuickSortStrategy()
    result = sorter.sort(data.copy())
    assert result == sorted(data), "Quick sort should work"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Pricing Strategy")
    cart = ShoppingCart(BulkPricing())
    cart.add_item("Widget", 10, 15)
    total = cart.calculate_total()
    assert total == 135, f"Should apply bulk discount, got {total}"
    
    cart.set_pricing_strategy(MemberPricing())
    cart._items = []  # Reset
    cart.add_item("Widget", 10, 5)
    total = cart.calculate_total()
    assert total == 42.5, f"Should apply member discount, got {total}"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Validation Strategy")
    validator = Validator(EmailValidator())
    assert validator.validate("test@example.com"), "Valid email should pass"
    assert not validator.validate("invalid"), "Invalid email should fail"
    
    validator.set_strategy(PhoneValidator())
    assert validator.validate("1234567890"), "Valid phone should pass"
    assert validator.validate("(123) 456-7890"), "Formatted phone should pass"
    
    validator.set_strategy(AgeValidator())
    assert validator.validate(25, min_age=18), "Valid age should pass"
    assert not validator.validate(10, min_age=18), "Too young should fail"
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Compression Strategy")
    manager = FileManager(GzipCompression())
    data = "Hello, World! " * 100
    compressed = manager.compress_data(data)
    assert len(compressed) < len(data.encode('utf-8')), "Gzip should compress"
    
    manager.set_compression(NoCompression())
    no_compressed = manager.compress_data(data)
    assert no_compressed == data.encode('utf-8'), "No compression should return raw"
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Strategy with Factory")
    PaymentProcessor.register("credit_card", CreditCardPayment)
    PaymentProcessor.register("paypal", PayPalPayment)
    PaymentProcessor.register("bank_transfer", BankTransferPayment)
    
    result = PaymentProcessor.process_payment(
        "credit_card", 100, card_number="1234567890123456"
    )
    assert result['method'] == 'credit_card'
    assert result['amount'] == 100
    
    result = PaymentProcessor.process_payment(
        "paypal", 50, email="test@example.com"
    )
    assert result['method'] == 'paypal'
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
