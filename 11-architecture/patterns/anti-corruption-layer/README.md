# Anti-Corruption Layer

## Overview

An Anti-Corruption Layer (ACL) translates between the model of a legacy or external system and your own system. It prevents the concepts and data structures of the external system from leaking into your code.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [Components](#components)
- [Implementation](#implementation)
- [Benefits](#benefits)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            ANTI-CORRUPTION LAYER                  |
+--------------------------------------------------+
|                                                  |
|  Your Domain          ACL            External     |
|  +----------+    +----------+    +----------+   |
|  |  Model   |--->| Facade   |--->| Legacy   |   |
|  |          |    | Adapter  |<---| System   |   |
|  |          |    | Translator|    |          |   |
|  +----------+    +----------+    +----------+   |
+--------------------------------------------------+
```

| Component | Responsibility |
|-----------|---------------|
| Facade | Simplifies external system interface |
| Adapter | Converts protocols and interfaces |
| Translator | Converts data models |

## Architecture

### When to Use ACL

- Integrating with legacy systems
- Connecting to third-party APIs
- Migrating between systems
- Working with poorly designed external services

## Components

### Facade

```python
class ExternalSystemFacade:
    def __init__(self, client):
        self._client = client

    def create_order(self, order_data):
        return self._client.post('/api/orders', order_data)

    def get_order(self, order_id):
        return self._client.get(f'/api/orders/{order_id}')

    def update_order(self, order_id, updates):
        return self._client.put(f'/api/orders/{order_id}', updates)
```

### Adapter

```python
class LegacyAPIAdapter:
    def __init__(self, legacy_client):
        self._legacy = legacy_client

    def create(self, data):
        soap_request = self._build_soap_request(data)
        response = self._legacy.call('CreateOrder', soap_request)
        return self._parse_soap_response(response)

    def _build_soap_request(self, data):
        return f'<soap:Body><CreateOrder><Id>{data["id"]}</Id></CreateOrder></soap:Body>'
```

### Translator

```python
class OrderTranslator:
    FIELD_MAPPING = {
        'id': 'ORD_ID',
        'customer_id': 'CUST_NUM',
        'total': 'TOT_AMT',
        'status': 'STATUS_CD'
    }

    def to_external(self, domain_order):
        return {self.FIELD_MAPPING[k]: v for k, v in domain_order.__dict__.items() if k in self.FIELD_MAPPING}

    def to_domain(self, external_data):
        reverse_mapping = {v: k for k, v in self.FIELD_MAPPING.items()}
        return {reverse_mapping[k]: v for k, v in external_data.items() if k in reverse_mapping}
```

## Implementation

### Complete ACL Example

```python
class OrderAntiCorruptionLayer:
    def __init__(self, legacy_client):
        self._facade = LegacyOrderFacade(legacy_client)
        self._translator = OrderTranslator()

    def create_order(self, order):
        external_data = self._translator.to_external(order)
        result = self._facade.create(external_data)
        return self._translator.to_domain(result)

    def get_order(self, order_id):
        external_data = self._facade.get(order_id)
        if external_data:
            return self._translator.to_domain(external_data)
        return None

class LegacyOrderFacade:
    def __init__(self, client):
        self._client = client

    def create(self, data):
        response = self._client.post('/legacy/orders', json=data)
        response.raise_for_status()
        return response.json()

    def get(self, order_id):
        response = self._client.get(f'/legacy/orders/{order_id}')
        if response.status_code == 404:
            return None
        response.raise_for_status()
        return response.json()
```

### Testing ACL

```python
import pytest
from unittest.mock import Mock

class TestOrderACL:
    def setup_method(self):
        self.mock_client = Mock()
        self.acl = OrderAntiCorruptionLayer(self.mock_client)

    def test_create_order_translates_to_external_format(self):
        order = {'id': '123', 'customer_id': 'C001', 'total': 99.99}
        self.mock_client.post.return_value = Mock(
            json=Mock(return_value={'ORD_ID': '123', 'TOT_AMT': 99.99}),
            status_code=201
        )
        result = self.acl.create_order(order)
        self.mock_client.post.assert_called_once()

    def test_get_order_translates_from_external_format(self):
        self.mock_client.get.return_value = Mock(
            json=Mock(return_value={'ORD_ID': '123', 'TOT_AMT': 99.99}),
            status_code=200
        )
        result = self.acl.get_order('123')
        assert result['id'] == '123'
```

## Benefits

1. **Domain Protection**: External system changes do not affect your domain
2. **Clean Architecture**: Keeps domain model pure
3. **Flexibility**: Can change external system without affecting domain
4. **Testability**: ACL can be mocked for testing
5. **Gradual Migration**: Facilitates incremental system replacement

## Best Practices

### 1. Keep ACL Focused

```python
class OrderACL:
    def create_order(self, order): pass
    def get_order(self, order_id): pass
```

### 2. Make Translations Explicit

```python
class OrderTranslator:
    def to_external(self, order):
        return {
            'ORD_ID': order.id,
            'CUST_NUM': order.customer_id,
            'TOT_AMT': float(order.total)
        }
```

### 3. Handle Errors Gracefully

```python
class ACLWithRetry:
    def __init__(self, client, max_retries=3):
        self._client = client
        self._max_retries = max_retries

    def execute_with_retry(self, func, *args):
        for attempt in range(self._max_retries):
            try:
                return func(*args)
            except TransientError:
                if attempt == self._max_retries - 1:
                    raise
                time.sleep(2 ** attempt)
```

### 4. Log Translation Operations

```python
class LoggingACL:
    def create_order(self, order):
        self.logger.info(f'Translating order {order.id}')
        result = self._acl.create_order(order)
        self.logger.info(f'Order {order.id} created')
        return result
```

### 5. Version Your Translations

```python
class VersionedTranslator:
    def __init__(self, version='v1'):
        self.version = version

    def to_external(self, order):
        if self.version == 'v1':
            return self._to_external_v1(order)
        elif self.version == 'v2':
            return self._to_external_v2(order)
```

## Further Reading

- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/ddd/)
- [Patterns of Enterprise Application Architecture - Martin Fowler](https://martinfowler.com/books/eaa.html)
- [Microservices Patterns - Chris Richardson](https://www.manning.com/books/microservices-patterns)
