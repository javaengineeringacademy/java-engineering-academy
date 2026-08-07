"""
Strategy Pattern in Python
Demonstrates the Strategy pattern for algorithm selection
"""

from typing import List, Callable, Any
from abc import ABC, abstractmethod

# ============================================
# Strategy Interface
# ============================================

class SortStrategy(ABC):
    """Abstract sorting strategy."""
    
    @abstractmethod
    def sort(self, data: List[int]) -> List[int]:
        """Sort the data."""
        pass

# ============================================
# Concrete Strategies
# ============================================

class BubbleSort(SortStrategy):
    """Bubble sort algorithm."""
    
    def sort(self, data: List[int]) -> List[int]:
        """Perform bubble sort."""
        arr = data.copy()
        n = len(arr)
        for i in range(n):
            for j in range(0, n - i - 1):
                if arr[j] > arr[j + 1]:
                    arr[j], arr[j + 1] = arr[j + 1], arr[j]
        return arr

class QuickSort(SortStrategy):
    """Quick sort algorithm."""
    
    def sort(self, data: List[int]) -> List[int]:
        """Perform quick sort."""
        if len(data) <= 1:
            return data
        pivot = data[len(data) // 2]
        left = [x for x in data if x < pivot]
        middle = [x for x in data if x == pivot]
        right = [x for x in data if x > pivot]
        return self.sort(left) + middle + self.sort(right)

class InsertionSort(SortStrategy):
    """Insertion sort algorithm."""
    
    def sort(self, data: List[int]) -> List[int]:
        """Perform insertion sort."""
        arr = data.copy()
        for i in range(1, len(arr)):
            key = arr[i]
            j = i - 1
            while j >= 0 and key < arr[j]:
                arr[j + 1] = arr[j]
                j -= 1
            arr[j + 1] = key
        return arr

# ============================================
# Context Class
# ============================================

class Sorter:
    """Context class that uses a sorting strategy."""
    
    def __init__(self, strategy: SortStrategy = None) -> None:
        self._strategy = strategy or BubbleSort()
    
    def set_strategy(self, strategy: SortStrategy) -> None:
        """Change the sorting strategy."""
        self._strategy = strategy
    
    def sort(self, data: List[int]) -> List[int]:
        """Sort using current strategy."""
        return self._strategy.sort(data)

# ============================================
# Practical Example: Compression
# ============================================

class CompressionStrategy(ABC):
    """Abstract compression strategy."""
    
    @abstractmethod
    def compress(self, data: str) -> str:
        """Compress the data."""
        pass

class ZipCompression(CompressionStrategy):
    """ZIP compression."""
    
    def compress(self, data: str) -> str:
        """Simulate ZIP compression."""
        return f"ZIP compressed: {data[:10]}... ({len(data)} bytes)"

class GzipCompression(CompressionStrategy):
    """GZIP compression."""
    
    def compress(self, data: str) -> str:
        """Simulate GZIP compression."""
        return f"GZIP compressed: {data[:10]}... ({len(data)} bytes)"

class RLECompression(CompressionStrategy):
    """Run-Length Encoding compression."""
    
    def compress(self, data: str) -> str:
        """Simulate RLE compression."""
        return f"RLE compressed: {data[:10]}... ({len(data)} bytes)"

class FileCompressor:
    """File compressor using strategy pattern."""
    
    def __init__(self, strategy: CompressionStrategy = None) -> None:
        self._strategy = strategy or ZipCompression()
    
    def set_strategy(self, strategy: CompressionStrategy) -> None:
        """Change compression strategy."""
        self._strategy = strategy
    
    def compress(self, filename: str, data: str) -> str:
        """Compress file using current strategy."""
        return f"File: {filename} | {self._strategy.compress(data)}"

# ============================================
# Practical Example: Payment Processing
# ============================================

class PaymentStrategy(ABC):
    """Abstract payment strategy."""
    
    @abstractmethod
    def pay(self, amount: float) -> bool:
        """Process payment."""
        pass

class CreditCardPayment(PaymentStrategy):
    """Credit card payment."""
    
    def __init__(self, card_number: str) -> None:
        self.card_number = card_number
    
    def pay(self, amount: float) -> bool:
        """Process credit card payment."""
        print(f"  Charging ${amount:.2f} to card ending in {self.card_number[-4:]}")
        return True

class PayPalPayment(PaymentStrategy):
    """PayPal payment."""
    
    def __init__(self, email: str) -> None:
        self.email = email
    
    def pay(self, amount: float) -> bool:
        """Process PayPal payment."""
        print(f"  Charging ${amount:.2f} to PayPal: {self.email}")
        return True

class BankTransferPayment(PaymentStrategy):
    """Bank transfer payment."""
    
    def __init__(self, account_number: str) -> None:
        self.account_number = account_number
    
    def pay(self, amount: float) -> bool:
        """Process bank transfer."""
        print(f"  Transferring ${amount:.2f} from account {self.account_number}")
        return True

class ShoppingCart:
    """Shopping cart with payment strategy."""
    
    def __init__(self) -> None:
        self.items: List[Dict[str, Any]] = []
    
    def add_item(self, name: str, price: float) -> None:
        """Add item to cart."""
        self.items.append({"name": name, "price": price})
    
    def get_total(self) -> float:
        """Get total price."""
        return sum(item["price"] for item in self.items)
    
    def checkout(self, payment_strategy: PaymentStrategy) -> bool:
        """Checkout using payment strategy."""
        total = self.get_total()
        print(f"  Total: ${total:.2f}")
        return payment_strategy.pay(total)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Sorting Strategy ===")
    sorter = Sorter()
    data = [64, 34, 25, 12, 22, 11, 90]
    
    print(f"Original: {data}")
    
    sorter.set_strategy(BubbleSort())
    print(f"Bubble sort: {sorter.sort(data)}")
    
    sorter.set_strategy(QuickSort())
    print(f"Quick sort: {sorter.sort(data)}")
    
    sorter.set_strategy(InsertionSort())
    print(f"Insertion sort: {sorter.sort(data)}")
    
    print("\n=== Compression Strategy ===")
    compressor = FileCompressor()
    data = "This is a test file with some content that needs compression"
    
    compressor.set_strategy(ZipCompression())
    print(f"ZIP: {compressor.compress('test.txt', data)}")
    
    compressor.set_strategy(GzipCompression())
    print(f"GZIP: {compressor.compress('test.txt', data)}")
    
    compressor.set_strategy(RLECompression())
    print(f"RLE: {compressor.compress('test.txt', data)}")
    
    print("\n=== Payment Strategy ===")
    cart = ShoppingCart()
    cart.add_item("Laptop", 999.99)
    cart.add_item("Mouse", 29.99)
    
    print("Pay with credit card:")
    cart.checkout(CreditCardPayment("1234567890123456"))
    
    print("\nPay with PayPal:")
    cart.checkout(PayPalPayment("user@example.com"))
    
    print("\nPay with bank transfer:")
    cart.checkout(BankTransferPayment("9876543210"))
