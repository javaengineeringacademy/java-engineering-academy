"""
Closures in Python
Demonstrates closures and their practical applications
"""

from typing import Callable, List

# ============================================
# Basic Closure
# ============================================

def outer_function(x: int) -> Callable[[int], int]:
    """Create a closure that adds x to its argument."""
    def inner_function(y: int) -> int:
        return x + y
    return inner_function

add_5 = outer_function(5)
add_10 = outer_function(10)

# ============================================
# Closure as Factory
# ============================================

def create_multiplier(factor: float) -> Callable[[float], float]:
    """Create a multiplier function."""
    def multiplier(x: float) -> float:
        return x * factor
    return multiplier

double = create_multiplier(2)
triple = create_multiplier(3)
half = create_multiplier(0.5)

# ============================================
# Closure with State
# ============================================

def create_counter(start: int = 0) -> Callable[[], int]:
    """Create a counter that maintains state."""
    count = start
    def counter() -> int:
        nonlocal count
        count += 1
        return count
    return counter

counter1 = create_counter(0)
counter2 = create_counter(100)

# ============================================
# Closure for Validation
# ============================================

def create_validator(min_val: int, max_val: int) -> Callable[[int], bool]:
    """Create a range validator."""
    def validate(value: int) -> bool:
        return min_val <= value <= max_val
    return validate

validate_age = create_validator(0, 150)
validate_grade = create_validator(0, 100)

# ============================================
# Closure for Logging
# ============================================

def create_logger(prefix: str) -> Callable[[str], None]:
    """Create a logger with a prefix."""
    def logger(message: str) -> None:
        print(f"[{prefix}] {message}")
    return logger

info_logger = create_logger("INFO")
error_logger = create_logger("ERROR")

# ============================================
# Closure with List Operations
# ============================================

def create_accumulator() -> Callable[[int], List[int]]:
    """Create an accumulator that stores all values."""
    values: List[int] = []
    def accumulator(value: int) -> List[int]:
        values.append(value)
        return values.copy()
    return accumulator

accum = create_accumulator()

# ============================================
# Practical: Discount Calculator
# ============================================

def create_discount_calculator(discount_percent: float) -> Callable[[float], float]:
    """Create a discount calculator."""
    def calculate(price: float) -> float:
        return price * (1 - discount_percent / 100)
    return calculate

ten_percent_off = create_discount_calculator(10)
twenty_percent_off = create_discount_calculator(20)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Closure ===")
    print(f"add_5(3) = {add_5(3)}")    # 8
    print(f"add_5(10) = {add_5(10)}")  # 15
    print(f"add_10(5) = {add_10(5)}")  # 15
    
    print("\n=== Closure as Factory ===")
    print(f"double(5) = {double(5)}")      # 10.0
    print(f"triple(5) = {triple(5)}")      # 15.0
    print(f"half(10) = {half(10)}")        # 5.0
    print(f"double(3.5) = {double(3.5)}")  # 7.0
    
    print("\n=== Closure with State ===")
    print(f"Counter1: {counter1()}")  # 1
    print(f"Counter1: {counter1()}")  # 2
    print(f"Counter2: {counter2()}")  # 101
    print(f"Counter1: {counter1()}")  # 3
    print(f"Counter2: {counter2()}")  # 102
    
    print("\n=== Closure for Validation ===")
    print(f"Age 25 valid: {validate_age(25)}")      # True
    print(f"Age 200 valid: {validate_age(200)}")    # False
    print(f"Grade 85 valid: {validate_grade(85)}")  # True
    
    print("\n=== Closure for Logging ===")
    info_logger("Application started")
    error_logger("Connection failed")
    
    print("\n=== Closure for Accumulation ===")
    print(f"Add 1: {accum(1)}")   # [1]
    print(f"Add 2: {accum(2)}")   # [1, 2]
    print(f"Add 3: {accum(3)}")   # [1, 2, 3]
    
    print("\n=== Practical: Discount Calculator ===")
    price = 100.00
    print(f"Original price: ${price}")
    print(f"After 10% off: ${ten_percent_off(price):.2f}")   # $90.00
    print(f"After 20% off: ${twenty_percent_off(price):.2f}")  # $80.00
