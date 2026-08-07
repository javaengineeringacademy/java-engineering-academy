"""
Module 11: Design Patterns - Strategy Solutions
Practice implementing the Strategy design pattern.
"""

from abc import ABC, abstractmethod
from typing import Any, List


class Strategy(ABC):
    """Abstract base class for strategies."""

    @abstractmethod
    def execute(self, *args, **kwargs) -> Any:
        """Execute the strategy."""
        pass


class SortStrategy(Strategy):
    """Strategy for sorting algorithms."""

    @abstractmethod
    def sort(self, data: List) -> List:
        """Sort the data."""
        pass

    def execute(self, data: List) -> List:
        return self.sort(data)


class BubbleSortStrategy(SortStrategy):
    """Bubble sort implementation."""

    def sort(self, data: List) -> List:
        result = data.copy()
        n = len(result)
        for i in range(n):
            for j in range(0, n-i-1):
                if result[j] > result[j+1]:
                    result[j], result[j+1] = result[j+1], result[j]
        return result


class QuickSortStrategy(SortStrategy):
    """Quick sort implementation."""

    def sort(self, data: List) -> List:
        if len(data) <= 1:
            return data
        pivot = data[len(data) // 2]
        left = [x for x in data if x < pivot]
        middle = [x for x in data if x == pivot]
        right = [x for x in data if x > pivot]
        return self.sort(left) + middle + self.sort(right)


class MergeSortStrategy(SortStrategy):
    """Merge sort implementation."""

    def sort(self, data: List) -> List:
        if len(data) <= 1:
            return data
        mid = len(data) // 2
        left = self.sort(data[:mid])
        right = self.sort(data[mid:])
        return self._merge(left, right)

    def _merge(self, left: List, right: List) -> List:
        result = []
        i = j = 0
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
        result.extend(left[i:])
        result.extend(right[j:])
        return result


class CompressionStrategy(Strategy):
    """Strategy for compression algorithms."""

    @abstractmethod
    def compress(self, data: bytes) -> bytes:
        """Compress the data."""
        pass

    def execute(self, data: bytes) -> bytes:
        return self.compress(data)


class NoCompressionStrategy(CompressionStrategy):
    """No compression strategy."""

    def compress(self, data: bytes) -> bytes:
        return data


class RLECompressionStrategy(CompressionStrategy):
    """Run-length encoding compression."""

    def compress(self, data: bytes) -> bytes:
        if not data:
            return b""
        result = bytearray()
        count = 1
        for i in range(1, len(data)):
            if data[i] == data[i-1]:
                count += 1
            else:
                result.extend([data[i-1], count])
                count = 1
        result.extend([data[-1], count])
        return bytes(result)


class ValidationStrategy(Strategy):
    """Strategy for validation rules."""

    @abstractmethod
    def validate(self, value: Any) -> bool:
        """Validate the value."""
        pass

    def execute(self, value: Any) -> bool:
        return self.validate(value)


class EmailValidationStrategy(ValidationStrategy):
    """Email validation strategy."""

    def validate(self, value: str) -> bool:
        return "@" in value and "." in value


class LengthValidationStrategy(ValidationStrategy):
    """Length validation strategy."""

    def __init__(self, min_length: int = 0, max_length: int = float('inf')):
        self.min_length = min_length
        self.max_length = max_length

    def validate(self, value: str) -> bool:
        return self.min_length <= len(value) <= self.max_length


class Context:
    """Context that uses a strategy."""

    def __init__(self, strategy: Strategy):
        self._strategy = strategy

    def set_strategy(self, strategy: Strategy) -> None:
        """Change the strategy at runtime."""
        self._strategy = strategy

    def execute_strategy(self, *args, **kwargs) -> Any:
        """Execute the current strategy."""
        return self._strategy.execute(*args, **kwargs)


class SortingContext:
    """Context for sorting with configurable strategy."""

    def __init__(self, strategy: SortStrategy = None):
        self._strategy = strategy or BubbleSortStrategy()

    def set_strategy(self, strategy: SortStrategy) -> None:
        self._strategy = strategy

    def sort(self, data: List) -> List:
        return self._strategy.sort(data)


if __name__ == "__main__":
    print("Testing Strategy Solutions...")

    # Test sorting strategies
    data = [64, 34, 25, 12, 22, 11, 90]

    context = SortingContext(BubbleSortStrategy())
    assert context.sort(data) == [11, 12, 22, 25, 34, 64, 90]

    context.set_strategy(QuickSortStrategy())
    assert context.sort(data) == [11, 12, 22, 25, 34, 64, 90]

    context.set_strategy(MergeSortStrategy())
    assert context.sort(data) == [11, 12, 22, 25, 34, 64, 90]
    print("✓ Sorting strategies work")

    # Test compression strategies
    data = b"AAAABBBCCD"

    context = Context(NoCompressionStrategy())
    assert context.execute_strategy(data) == data

    context = Context(RLECompressionStrategy())
    compressed = context.execute_strategy(data)
    assert compressed == b"A\x04B\x03C\x02D\x01"
    print("✓ Compression strategies work")

    # Test validation strategies
    context = Context(EmailValidationStrategy())
    assert context.execute_strategy("user@example.com") is True
    assert context.execute_strategy("invalid-email") is False

    context = Context(LengthValidationStrategy(3, 10))
    assert context.execute_strategy("hello") is True
    assert context.execute_strategy("hi") is False
    print("✓ Validation strategies work")

    print("All Strategy solutions passed!")
