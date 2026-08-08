"""
Module 17 - Metaclasses: Type Creation Solutions
Complete solutions with explanations
"""


# =============================================================================
# Exercise 1: Basic Type Creation - SOLUTION
# =============================================================================

def exercise_1_basic_type():
    """
    Create a class using type() function.
    """
    # Create a class dynamically using type()
    def init_method(self, x, y):
        self.x = x
        self.y = y
    
    def method_add(self):
        return self.x + self.y
    
    # type(name, bases, namespace)
    DynamicClass = type('DynamicClass', (object,), {
        'attr': 'value',
        '__init__': init_method,
        'add': method_add,
    })
    
    # Create instance
    obj = DynamicClass(10, 20)
    
    return {
        'class_name': DynamicClass.__name__,
        'attr': DynamicClass.attr,
        'add_result': obj.add(),
    }


# =============================================================================
# Exercise 2: Custom Metaclass - SOLUTION
# =============================================================================

class MetaRegistry(type):
    """
    Custom metaclass that registers all created classes.
    """
    _registry = {}
    
    def __new__(mcs, name, bases, namespace):
        # Create the class
        cls = super().__new__(mcs, name, bases, namespace)
        
        # Register it (skip base classes)
        if bases:
            mcs._registry[name] = cls
        
        return cls
    
    @classmethod
    def get_class(mcs, name):
        """Get class by name."""
        return mcs._registry.get(name)


# =============================================================================
# Exercise 3: Auto-Documentation - SOLUTION
# =============================================================================

class DocMeta(type):
    """
    Metaclass that adds docstrings automatically.
    """
    def __new__(mcs, name, bases, namespace):
        # Generate docstring from methods
        doc_lines = [f"{name} class documentation.\n"]
        
        for key, value in namespace.items():
            if callable(value) and not key.startswith('_'):
                sig = f"  - {key}(): "
                if hasattr(value, '__doc__') and value.__doc__:
                    sig += value.__doc__
                else:
                    sig += "No documentation available."
                doc_lines.append(sig)
        
        # Create class with auto-generated docstring
        cls = super().__new__(mcs, name, bases, namespace)
        cls.__doc__ = "\n".join(doc_lines)
        
        return cls


# =============================================================================
# Exercise 4: Attribute Validation - SOLUTION
# =============================================================================

class ValidatedMeta(type):
    """
    Metaclass that validates attribute types.
    """
    def __new__(mcs, name, bases, namespace):
        # Get type annotations
        annotations = namespace.get('__annotations__', {})
        
        # Create validation descriptors
        for attr_name, attr_type in annotations.items():
            if attr_name.startswith('_'):
                continue
            
            def make_validator(attr_name, attr_type):
                def getter(self):
                    return getattr(self, f'_{attr_name}', None)
                
                def setter(self, value):
                    if value is not None and not isinstance(value, attr_type):
                        raise TypeError(
                            f"{attr_name} must be {attr_type.__name__}, "
                            f"got {type(value).__name__}"
                        )
                    setattr(self, f'_{attr_name}', value)
                
                return property(getter, setter)
            
            namespace[attr_name] = make_validator(attr_name, attr_type)
        
        return super().__new__(mcs, name, bases, namespace)


# =============================================================================
# Exercise 5: Singleton Metaclass - SOLUTION
# =============================================================================

class SingletonMeta(type):
    """
    Metaclass that implements Singleton pattern.
    """
    _instances = {}
    
    def __call__(cls, *args, **kwargs):
        # If instance doesn't exist, create it
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        
        return cls._instances[cls]


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Type Creation Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Type Creation")
    result = exercise_1_basic_type()
    assert result['class_name'] == 'DynamicClass'
    assert result['attr'] == 'value'
    assert result['add_result'] == 30
    print(f"  Result: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Custom Metaclass")
    class MyClass(metaclass=MetaRegistry):
        pass
    
    class AnotherClass(metaclass=MetaRegistry):
        pass
    
    result = MetaRegistry.get_class('MyClass')
    assert result is MyClass
    assert 'MyClass' in MetaRegistry._registry
    print(f"  Registered: {list(MetaRegistry._registry.keys())}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Auto-Documentation")
    class MyDoc(metaclass=DocMeta):
        def my_method(self, x):
            """Process x value."""
            return x * 2
        
        def other_method(self):
            pass
    
    assert MyDoc.__doc__ is not None
    assert 'my_method' in MyDoc.__doc__
    print(f"  Docstring:\n{MyDoc.__doc__}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Attribute Validation")
    class MyValidated(metaclass=ValidatedMeta):
        name: str
        age: int
    
    obj = MyValidated()
    obj.name = "John"
    obj.age = 25
    
    assert obj.name == "John"
    assert obj.age == 25
    
    try:
        obj.age = "not an int"
        print("  ✗ Should have raised TypeError")
    except TypeError:
        print("  ✓ Type validation works")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton Metaclass")
    class MySingleton(metaclass=SingletonMeta):
        def __init__(self):
            self.value = 42
    
    s1 = MySingleton()
    s2 = MySingleton()
    
    assert s1 is s2
    assert s1.value == 42
    print(f"  Same instance: {s1 is s2}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
