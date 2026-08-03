# Python Fundamentals Example

# Variables
name = "Python"
version = 3.12
print(f"Language: {name}, Version: {version}")

# Lists
numbers = [1, 2, 3, 4, 5]
numbers.append(6)
print(f"Numbers: {numbers}")

# Dictionaries
languages = {
    "python": "Python",
    "java": "Java",
    "go": "Golang",
}
print(f"Languages: {languages}")

# Classes
class Person:
    def __init__(self, name: str, age: int):
        self.name = name
        self.age = age
    
    def __repr__(self):
        return f"Person(name='{self.name}', age={self.age})"

p = Person("Alice", 30)
print(f"Person: {p}")

# List comprehension
squares = [x**2 for x in range(10)]
print(f"Squares: {squares}")

# Function with type hints
def greet(name: str) -> str:
    return f"Hello, {name}!"

print(greet("World"))

# Error handling
try:
    result = 10 / 0
except ZeroDivisionError:
    print("Cannot divide by zero!")
