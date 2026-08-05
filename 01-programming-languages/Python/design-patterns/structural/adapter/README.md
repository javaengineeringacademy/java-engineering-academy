# Adapter Pattern in Python

The Adapter pattern allows objects with incompatible interfaces to collaborate. Python's duck typing makes adapters often unnecessary, but they're useful when integrating third-party code or legacy systems.

## When to Use

- Integrating third-party libraries with different interfaces
- Wrapping legacy code for modern usage
- Making incompatible classes work together
- Creating reusable wrappers for external APIs
- Converting data formats between systems

## Python Implementation

### Object Adapter
```python
class EuropeanSocket:
    def plug_in_type_f(self):
        return "220V AC"

class AmericanSocket:
    def plug_in_type_a(self):
        return "110V AC"

class EuropeanToAmericanAdapter:
    def __init__(self, european_socket: EuropeanSocket):
        self._socket = european_socket
    
    def plug_in_type_a(self):
        voltage = self._socket.plug_in_type_f()
        return f"Converted: {voltage} to 110V AC"

# Usage
euro_socket = EuropeanSocket()
adapter = EuropeanToAmericanAdapter(euro_socket)
print(adapter.plug_in_type_a())
```

### Class Adapter via Multiple Inheritance
```python
class LegacyPrinter:
    def print_old(self, text):
        return f"Legacy: {text}"

class ModernPrinter:
    def print_new(self, text):
        return f"Modern: {text}"

class PrinterAdapter(LegacyPrinter, ModernPrinter):
    def print_document(self, text):
        return self.print_old(text)

# Usage
printer = PrinterAdapter()
print(printer.print_document("Hello"))
```

### Dictionary Adapter
```python
class XMLData:
    def __init__(self):
        self.data = {"name": "John", "age": "30"}
    
    def get_xml(self):
        return "<data>" + "".join(
            f"<{k}>{v}</{k}>" for k, v in self.data.items()
        ) + "</data>"

class JSONAdapter:
    def __init__(self, xml_data: XMLData):
        self._xml = xml_data
    
    def get_json(self):
        import json
        return json.dumps(self._xml.data)

# Usage
xml = XMLData()
adapter = JSONAdapter(xml)
print(adapter.get_json())
```

## Pythonic Alternative

Duck typing often makes adapters unnecessary:
```python
# Instead of adapter, use duck typing
def process(obj):
    # Just call the method you need
    if hasattr(obj, 'get_data'):
        return obj.get_data()
    elif hasattr(obj, 'fetch_data'):
        return obj.fetch_data()
```

## Real-World Example

```python
class ThirdPartyWeather:
    def get_weather_data(self):
        return {"temp": 72, "humidity": 45}

class WeatherAdapter:
    def __init__(self, api_key: str):
        self._api = ThirdPartyWeather()
        self._api_key = api_key
    
    def get_temperature(self):
        data = self._api.get_weather_data()
        return data["temp"]
    
    def get_humidity(self):
        data = self._api.get_weather_data()
        return data["humidity"]
```

## Best Practices

1. Prefer duck typing when possible
2. Keep adapters focused on single responsibility
3. Document the interface contract clearly
4. Use composition over inheritance for object adapters
5. Consider Protocol for structural typing

## Interview Questions

1. What problem does the Adapter pattern solve?
2. How does Python's duck typing affect the need for adapters?
3. What's the difference between object and class adapters?
4. When would you use an adapter versus refactoring the original class?
5. How would you test an adapter?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `typing.Protocol` documentation
- *Fluent Python* - Luciano Ramalho
- PEP 544 - Protocols
