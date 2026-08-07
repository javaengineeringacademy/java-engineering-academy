"""
Module 11: Design Patterns - Strategy Exercises
===============================================
Practice implementing the Strategy design pattern.
"""

from abc import ABC, abstractmethod

# =============================================================================
# Exercise 1: Basic Strategy (★☆☆☆☆)
# =============================================================================
# TODO: Implement strategy pattern for sorting

class SortStrategy(ABC):
    """Base class for sorting strategies."""
    @abstractmethod
    def sort(self, data):
        pass

class BubbleSort(SortStrategy):
    def sort(self, data):
        # TODO: Implement bubble sort
        pass

class QuickSort(SortStrategy):
    def sort(self, data):
        # TODO: Implement quick sort
        pass

# Test Cases
def test_basic_strategy():
    data = [3, 1, 4, 1, 5, 9, 2, 6]
    
    bubble = BubbleSort()
    result1 = bubble.sort(data.copy())
    assert result1 == [1, 1, 2, 3, 4, 5, 6, 9]
    
    quick = QuickSort()
    result2 = quick.sort(data.copy())
    assert result2 == [1, 1, 2, 3, 4, 5, 6, 9]
    
    print("✓ Exercise 1 passed: both strategies produce sorted output")

# =============================================================================
# Exercise 2: Discount Calculator (★★☆☆☆)
# =============================================================================
# TODO: Implement different discount strategies

class DiscountStrategy(ABC):
    @abstractmethod
    def calculate(self, price):
        pass

class NoDiscount(DiscountStrategy):
    def calculate(self, price):
        return price

class PercentageDiscount(DiscountStrategy):
    def __init__(self, percent):
        self.percent = percent
    
    def calculate(self, price):
        # TODO: Apply percentage discount
        pass

class FixedDiscount(DiscountStrategy):
    def __init__(self, amount):
        self.amount = amount
    
    def calculate(self, price):
        # TODO: Apply fixed amount discount
        pass

# Test Cases
def test_discount_calculator():
    assert NoDiscount().calculate(100) == 100
    assert PercentageDiscount(20).calculate(100) == 80
    assert FixedDiscount(15).calculate(100) == 85
    print("✓ Exercise 2 passed: discount strategies working")

# =============================================================================
# Exercise 3: Compression Strategy (★★★☆☆)
# =============================================================================
# TODO: Implement different compression algorithms

class CompressionStrategy(ABC):
    @abstractmethod
    def compress(self, data):
        pass

class ZipCompression(CompressionStrategy):
    def compress(self, data):
        # TODO: Return compressed representation
        pass

class GzipCompression(CompressionStrategy):
    def compress(self, data):
        # TODO: Return compressed representation
        pass

class NoCompression(CompressionStrategy):
    def compress(self, data):
        return data

# Test Cases
def test_compression_strategy():
    data = b"hello world " * 100
    
    zip_c = ZipCompression()
    gzip_c = GzipCompression()
    none_c = NoCompression()
    
    assert len(zip_c.compress(data)) < len(data)
    assert len(gzip_c.compress(data)) < len(data)
    assert len(none_c.compress(data)) == len(data)
    print("✓ Exercise 3 passed: compression strategies reduce size")

# =============================================================================
# Exercise 4: Payment Processor (★★★★☆)
# =============================================================================
# TODO: Implement payment strategy with validation

class PaymentStrategy(ABC):
    @abstractmethod
    def validate(self, details):
        pass
    
    @abstractmethod
    def process(self, amount, details):
        pass

class CreditCardPayment(PaymentStrategy):
    def validate(self, details):
        # TODO: Validate card number, expiry, CVV
        pass
    
    def process(self, amount, details):
        # TODO: Process credit card payment
        pass

class PayPalPayment(PaymentStrategy):
    def validate(self, details):
        # TODO: Validate email
        pass
    
    def process(self, amount, details):
        # TODO: Process PayPal payment
        pass

# Test Cases
def test_payment_strategy():
    cc = CreditCardPayment()
    assert cc.validate({"number": "4111111111111111", "expiry": "12/25", "cvv": "123"})
    assert not cc.validate({"number": "invalid"})
    
    paypal = PayPalPayment()
    assert paypal.validate({"email": "user@example.com"})
    assert not paypal.validate({"email": "invalid"})
    
    print("✓ Exercise 4 passed: payment strategies validate correctly")

# =============================================================================
# Exercise 5: Strategy Context with Dynamic Switching (★★★★★)
# =============================================================================
# TODO: Create context that allows runtime strategy switching

class StrategyContext:
    """Context that allows switching strategies at runtime."""
    # TODO: Implement set_strategy and execute methods
    # TODO: Support strategy history/undo
    pass

# Test Cases
def test_strategy_context():
    data = [5, 3, 8, 1, 9, 2]
    context = StrategyContext(BubbleSort())
    
    result1 = context.execute(data.copy())
    assert result1 == [1, 2, 3, 5, 8, 9]
    
    context.set_strategy(QuickSort())
    result2 = context.execute(data.copy())
    assert result2 == [1, 2, 3, 5, 8, 9]
    
    history = context.get_history()
    assert len(history) == 2
    
    print("✓ Exercise 5 passed: dynamic strategy switching works")

if __name__ == "__main__":
    print("Running Strategy Pattern Exercises...")
    print("=" * 50)
    test_basic_strategy()
    test_discount_calculator()
    test_compression_strategy()
    test_payment_strategy()
    test_strategy_context()
    print("=" * 50)
    print("All tests passed!")
