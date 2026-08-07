"""
Name Mangling in Python
Demonstrates name mangling and private attributes
"""

# ============================================
# Basic Name Mangling
# ============================================

class Person:
    """Class with name-mangled attributes."""
    
    def __init__(self, name: str, age: int) -> None:
        self.name = name          # Public attribute
        self._age = age           # Protected attribute (convention)
        self.__secret = "hidden"  # Private attribute (mangled)
    
    def get_secret(self) -> str:
        """Access private attribute."""
        return self.__secret
    
    def __private_method(self) -> str:
        """Private method (mangled)."""
        return f"Private method called by {self.name}"

class Employee(Person):
    """Subclass demonstrating name mangling."""
    
    def __init__(self, name: str, age: int, employee_id: int) -> None:
        super().__init__(name, age)
        self.employee_id = employee_id
        self.__department = "Engineering"  # Also mangled
    
    def get_info(self) -> dict:
        """Get employee information."""
        return {
            "name": self.name,
            "age": self._age,
            "id": self.employee_id
        }

# ============================================
# Accessing Mangled Names
# ============================================

def demonstrate_access_patterns() -> None:
    """Show how to access mangled names."""
    person = Person("Alice", 30)
    
    print("=== Normal Access ===")
    print(f"  name: {person.name}")
    print(f"  _age: {person._age}")
    
    print("\n=== Accessing Private Attributes ===")
    # This will fail:
    # print(person.__secret)  # AttributeError
    
    # But we can access via mangled name:
    print(f"  _Person__secret: {person._Person__secret}")
    
    print("\n=== Accessing Private Methods ===")
    # This will fail:
    # print(person.__private_method())  # AttributeError
    
    # But we can access via mangled name:
    print(f"  _Person__private_method(): {person._Person__private_method()}")

# ============================================
# Name Mangling in Inheritance
# ============================================

def demonstrate_inheritance() -> None:
    """Show name mangling with inheritance."""
    employee = Employee("Bob", 25, 12345)
    
    print("\n=== Employee with Inheritance ===")
    print(f"  name: {employee.name}")
    print(f"  _age: {employee._age}")
    print(f"  employee_id: {employee.employee_id}")
    
    # Access mangled attributes
    print(f"  _Person__secret: {employee._Person__secret}")
    print(f"  _Employee__department: {employee._Employee__department}")
    
    print("\n=== Mangled Names in Dir ===")
    mangled = [attr for attr in dir(employee) if '__' in attr and attr.count('__') == 2]
    print(f"  Mangled attributes: {mangled}")

# ============================================
# Name Mangling with Properties
# ============================================

class BankAccount:
    """Bank account with name-mangled balance."""
    
    def __init__(self, owner: str, initial_balance: float = 0) -> None:
        self.owner = owner
        self.__balance = initial_balance
    
    @property
    def balance(self) -> float:
        """Get balance (read-only property)."""
        return self.__balance
    
    def deposit(self, amount: float) -> None:
        """Deposit money."""
        if amount > 0:
            self.__balance += amount
    
    def withdraw(self, amount: float) -> bool:
        """Withdraw money."""
        if 0 < amount <= self.__balance:
            self.__balance -= amount
            return True
        return False

# ============================================
# Name Mangling in Metaclasses
# ============================================

class MetaWithMangling(type):
    """Metaclass that handles name mangling."""
    
    def __new__(mcs, name, bases, namespace):
        # Process name-mangled attributes
        for attr_name, value in list(namespace.items()):
            if attr_name.startswith('__') and not attr_name.endswith('__'):
                # Store original name mapping
                if not hasattr(mcs, '_mangled_names'):
                    mcs._mangled_names = {}
                mcs._mangled_names[name] = mcs._mangled_names.get(name, {})
                mcs._mangled_names[name][attr_name] = f"_{name}{attr_name}"
        
        return super().__new__(mcs, name, bases, namespace)

class MyClass(metaclass=MetaWithMangling):
    """Class using metaclass with name mangling."""
    __internal = "internal value"

# ============================================
# Practical Example: Configuration
# ============================================

class Configuration:
    """Configuration class with protected settings."""
    
    def __init__(self) -> None:
        self.__settings = {}
        self.__defaults = {
            "debug": False,
            "log_level": "INFO",
            "max_retries": 3
        }
    
    def get(self, key: str) -> any:
        """Get configuration value."""
        return self.__settings.get(key, self.__defaults.get(key))
    
    def set(self, key: str, value: any) -> None:
        """Set configuration value."""
        self.__settings[key] = value
    
    def reset(self) -> None:
        """Reset to defaults."""
        self.__settings.clear()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    demonstrate_access_patterns()
    demonstrate_inheritance()
    
    print("\n=== Bank Account Example ===")
    account = BankAccount("Alice", 1000)
    print(f"  Owner: {account.owner}")
    print(f"  Balance: ${account.balance}")
    
    account.deposit(500)
    print(f"  After deposit: ${account.balance}")
    
    account.withdraw(200)
    print(f"  After withdrawal: ${account.balance}")
    
    # Cannot access mangled balance directly:
    # print(account.__balance)  # AttributeError
    
    print("\n=== Configuration Example ===")
    config = Configuration()
    print(f"  Default debug: {config.get('debug')}")
    
    config.set("debug", True)
    print(f"  Custom debug: {config.get('debug')}")
    
    config.reset()
    print(f"  After reset: {config.get('debug')}")
    
    print("\n=== Summary ===")
    print("  - Name mangling: __attr becomes _ClassName__attr")
    print("  - Prevents accidental name conflicts in subclasses")
    print("  - Not true privacy (can still access via mangled name)")
    print("  - Convention: _attr = protected, __attr = name-mangled")
