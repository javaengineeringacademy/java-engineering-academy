"""
Factory Pattern in Python
Demonstrates various factory pattern implementations
"""

from typing import Dict, Any, Type
from abc import ABC, abstractmethod

# ============================================
# Simple Factory
# ============================================

class Animal(ABC):
    """Abstract animal class."""
    
    @abstractmethod
    def speak(self) -> str:
        """Make a sound."""
        pass
    
    @abstractmethod
    def move(self) -> str:
        """Move."""
        pass

class Dog(Animal):
    """Dog implementation."""
    
    def speak(self) -> str:
        return "Woof!"
    
    def move(self) -> str:
        return "Runs on four legs"

class Cat(Animal):
    """Cat implementation."""
    
    def speak(self) -> str:
        return "Meow!"
    
    def move(self) -> str:
        return "Sneaks quietly"

class Bird(Animal):
    """Bird implementation."""
    
    def speak(self) -> str:
        return "Tweet!"
    
    def move(self) -> str:
        return "Flies in the sky"

class AnimalFactory:
    """Simple factory for creating animals."""
    
    @staticmethod
    def create_animal(animal_type: str) -> Animal:
        """Create an animal based on type."""
        animals = {
            "dog": Dog,
            "cat": Cat,
            "bird": Bird
        }
        
        if animal_type.lower() in animals:
            return animals[animal_type.lower()]()
        raise ValueError(f"Unknown animal type: {animal_type}")

# ============================================
# Factory Method Pattern
# ============================================

class Notification(ABC):
    """Abstract notification class."""
    
    @abstractmethod
    def send(self, message: str) -> bool:
        """Send notification."""
        pass

class EmailNotification(Notification):
    """Email notification."""
    
    def __init__(self, email: str) -> None:
        self.email = email
    
    def send(self, message: str) -> bool:
        print(f"  Sending email to {self.email}: {message}")
        return True

class SMSNotification(Notification):
    """SMS notification."""
    
    def __init__(self, phone: str) -> None:
        self.phone = phone
    
    def send(self, message: str) -> bool:
        print(f"  Sending SMS to {self.phone}: {message}")
        return True

class PushNotification(Notification):
    """Push notification."""
    
    def __init__(self, device_id: str) -> None:
        self.device_id = device_id
    
    def send(self, message: str) -> bool:
        print(f"  Sending push to device {self.device_id}: {message}")
        return True

class NotificationFactory(ABC):
    """Abstract notification factory."""
    
    @abstractmethod
    def create_notification(self) -> Notification:
        """Create a notification."""
        pass

class EmailFactory(NotificationFactory):
    """Email notification factory."""
    
    def __init__(self, email: str) -> None:
        self.email = email
    
    def create_notification(self) -> Notification:
        return EmailNotification(self.email)

class SMSFactory(NotificationFactory):
    """SMS notification factory."""
    
    def __init__(self, phone: str) -> None:
        self.phone = phone
    
    def create_notification(self) -> Notification:
        return SMSNotification(self.phone)

class PushFactory(NotificationFactory):
    """Push notification factory."""
    
    def __init__(self, device_id: str) -> None:
        self.device_id = device_id
    
    def create_notification(self) -> Notification:
        return PushNotification(self.device_id)

# ============================================
# Abstract Factory Pattern
# ============================================

class Button(ABC):
    """Abstract button class."""
    
    @abstractmethod
    def render(self) -> str:
        """Render button."""
        pass

class Checkbox(ABC):
    """Abstract checkbox class."""
    
    @abstractmethod
    def render(self) -> str:
        """Render checkbox."""
        pass

class WindowsButton(Button):
    """Windows button."""
    def render(self) -> str:
        return "[Windows Button]"

class WindowsCheckbox(Checkbox):
    """Windows checkbox."""
    def render(self) -> str:
        return "[Windows Checkbox]"

class MacOSButton(Button):
    """MacOS button."""
    def render(self) -> str:
        return "(MacOS Button)"

class MacOSCheckbox(Checkbox):
    """MacOS checkbox."""
    def render(self) -> str:
        return "(MacOS Checkbox)"

class GUIFactory(ABC):
    """Abstract GUI factory."""
    
    @abstractmethod
    def create_button(self) -> Button:
        """Create a button."""
        pass
    
    @abstractmethod
    def create_checkbox(self) -> Checkbox:
        """Create a checkbox."""
        pass

class WindowsFactory(GUIFactory):
    """Windows GUI factory."""
    
    def create_button(self) -> Button:
        return WindowsButton()
    
    def create_checkbox(self) -> Checkbox:
        return WindowsCheckbox()

class MacOSFactory(GUIFactory):
    """MacOS GUI factory."""
    
    def create_button(self) -> Button:
        return MacOSButton()
    
    def create_checkbox(self) -> Checkbox:
        return MacOSCheckbox()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Simple Factory ===")
    for animal_type in ["dog", "cat", "bird"]:
        animal = AnimalFactory.create_animal(animal_type)
        print(f"  {animal_type.capitalize()}: {animal.speak()} - {animal.move()}")
    
    print("\n=== Factory Method ===")
    factories: Dict[str, NotificationFactory] = {
        "email": EmailFactory("user@example.com"),
        "sms": SMSFactory("+1234567890"),
        "push": PushFactory("device-123")
    }
    
    for name, factory in factories.items():
        notification = factory.create_notification()
        notification.send(f"Hello from {name}!")
    
    print("\n=== Abstract Factory ===")
    def create_ui(factory: GUIFactory) -> None:
        """Create UI components using factory."""
        button = factory.create_button()
        checkbox = factory.create_checkbox()
        print(f"  Button: {button.render()}")
        print(f"  Checkbox: {checkbox.render()}")
    
    print("Windows UI:")
    create_ui(WindowsFactory())
    
    print("MacOS UI:")
    create_ui(MacOSFactory())
