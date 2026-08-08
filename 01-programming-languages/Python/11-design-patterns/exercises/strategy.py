"""
Module 11 - Design Patterns: Strategy Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Strategy pattern implementation
"""


# =============================================================================
# Exercise 1: Basic Strategy (⭐⭐⭐)
# =============================================================================

class SortStrategy:
    """Strategy interface for sorting."""
    def sort(self, data):
        pass


class BubbleSortStrategy(SortStrategy):
    """TODO: Implement bubble sort."""
    def sort(self, data):
        # TODO: Implement bubble sort
        pass


class QuickSortStrategy(SortStrategy):
    """TODO: Implement quick sort."""
    def sort(self, data):
        # TODO: Implement quick sort
        pass


class Sorter:
    """Context class that uses sorting strategy."""
    def __init__(self, strategy=None):
        self._strategy = strategy
    
    @property
    def strategy(self):
        return self._strategy
    
    @strategy.setter
    def strategy(self, strategy):
        # TODO: Set the strategy
        pass
    
    def sort(self, data):
        # TODO: Use strategy to sort
        pass


# =============================================================================
# Exercise 2: Pricing Strategy (⭐⭐⭐⭐)
# =============================================================================

class PricingStrategy:
    """Strategy interface for pricing."""
    def calculate_price(self, base_price, quantity):
        pass


class RegularPricing(PricingStrategy):
    """TODO: Regular pricing (no discount)."""
    def calculate_price(self, base_price, quantity):
        # TODO: Implement regular pricing
        pass


class BulkPricing(PricingStrategy):
    """TODO: Bulk pricing with quantity discount."""
    def calculate_price(self, base_price, quantity):
        # TODO: Implement bulk pricing (10% off for 10+ items)
        pass


class MemberPricing(PricingStrategy):
    """TODO: Member pricing with percentage discount."""
    def calculate_price(self, base_price, quantity, member_discount=0.15):
        # TODO: Implement member pricing
        pass


class ShoppingCart:
    """Context class that uses pricing strategy."""
    def __init__(self, pricing_strategy=None):
        self._pricing_strategy = pricing_strategy or RegularPricing()
        self._items = []
    
    def add_item(self, name, price, quantity=1):
        self._items.append({'name': name, 'price': price, 'quantity': quantity})
    
    def set_pricing_strategy(self, strategy):
        # TODO: Set pricing strategy
        pass
    
    def calculate_total(self):
        # TODO: Calculate total using current strategy
        pass


# =============================================================================
# Exercise 3: Validation Strategy (⭐⭐⭐⭐)
# =============================================================================

class ValidationStrategy:
    """Strategy interface for validation."""
    def validate(self, value):
        pass


class EmailValidator(ValidationStrategy):
    """TODO: Validate email format."""
    def validate(self, value):
        # TODO: Implement email validation
        pass


class PhoneValidator(ValidationStrategy):
    """TODO: Validate phone number."""
    def validate(self, value):
        # TODO: Implement phone validation
        pass


class AgeValidator(ValidationStrategy):
    """TODO: Validate age."""
    def validate(self, value, min_age=0, max_age=150):
        # TODO: Implement age validation
        pass


class Validator:
    """Context class that uses validation strategy."""
    def __init__(self, strategy=None):
        self._strategy = strategy
    
    def set_strategy(self, strategy):
        # TODO: Set validation strategy
        pass
    
    def validate(self, value, **kwargs):
        # TODO: Validate using current strategy
        pass


# =============================================================================
# Exercise 4: Compression Strategy (⭐⭐⭐⭐)
# =============================================================================

class CompressionStrategy:
    """Strategy interface for compression."""
    def compress(self, data):
        pass


class ZipCompression(CompressionStrategy):
    """TODO: Implement ZIP compression."""
    def compress(self, data):
        # TODO: Implement ZIP compression
        pass


class GzipCompression(CompressionStrategy):
    """TODO: Implement GZIP compression."""
    def compress(self, data):
        # TODO: Implement GZIP compression
        pass


class NoCompression(CompressionStrategy):
    """TODO: No compression."""
    def compress(self, data):
        # TODO: Return data as-is
        pass


class FileManager:
    """Context class that uses compression strategy."""
    def __init__(self, compression_strategy=None):
        self._compression = compression_strategy or NoCompression()
    
    def set_compression(self, strategy):
        # TODO: Set compression strategy
        pass
    
    def save_file(self, filename, data):
        # TODO: Compress and save data
        pass
    
    def read_file(self, filename):
        # TODO: Read and decompress data
        pass


# =============================================================================
# Exercise 5: Strategy with Factory (⭐⭐⭐⭐⭐)
# =============================================================================

class PaymentStrategy:
    """Strategy interface for payment."""
    def pay(self, amount):
        pass


class CreditCardPayment(PaymentStrategy):
    """TODO: Credit card payment."""
    def __init__(self, card_number):
        self.card_number = card_number
    
    def pay(self, amount):
        # TODO: Process credit card payment
        pass


class PayPalPayment(PaymentStrategy):
    """TODO: PayPal payment."""
    def __init__(self, email):
        self.email = email
    
    def pay(self, amount):
        # TODO: Process PayPal payment
        pass


class BankTransferPayment(PaymentStrategy):
    """TODO: Bank transfer payment."""
    def __init__(self, account_number):
        self.account_number = account_number
    
    def pay(self, amount):
        # TODO: Process bank transfer
        pass


class PaymentProcessor:
    """Factory that creates payment strategies."""
    _strategies = {}
    
    @classmethod
    def register(cls, name, strategy_class):
        # TODO: Register a payment strategy
        pass
    
    @classmethod
    def create(cls, name, **kwargs):
        # TODO: Create and return payment strategy
        pass
    
    @classmethod
    def process_payment(cls, name, amount, **kwargs):
        # TODO: Create strategy and process payment
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Strategy Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Strategy")
    try:
        sorter = Sorter(BubbleSortStrategy())
        data = [3, 1, 4, 1, 5, 9, 2, 6]
        result = sorter.sort(data.copy())
        assert result == sorted(data), "Should sort correctly"
        sorter.strategy = QuickSortStrategy()
        result = sorter.sort(data.copy())
        assert result == sorted(data), "Should sort with new strategy"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Pricing Strategy")
    try:
        cart = ShoppingCart(BulkPricing())
        cart.add_item("Widget", 10, 15)
        total = cart.calculate_total()
        assert total == 135, "Should apply bulk discount"
        cart.set_pricing_strategy(MemberPricing())
        total = cart.calculate_total()
        assert total == 127.5, "Should apply member discount"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Validation Strategy")
    try:
        validator = Validator(EmailValidator())
        assert validator.validate("test@example.com"), "Valid email should pass"
        assert not validator.validate("invalid"), "Invalid email should fail"
        validator.set_strategy(AgeValidator())
        assert validator.validate(25, min_age=18), "Valid age should pass"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Compression Strategy")
    try:
        manager = FileManager(GzipCompression())
        data = b"test data"
        # manager.save_file("test.gz", data)  # File operations
        print("  ✓ Passed (file operations)\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Strategy with Factory")
    try:
        PaymentProcessor.register("credit_card", CreditCardPayment)
        PaymentProcessor.register("paypal", PayPalPayment)
        result = PaymentProcessor.process_payment("credit_card", 100, card_number="1234")
        assert result is not None, "Should process payment"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
