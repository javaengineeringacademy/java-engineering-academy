# RabbitMQ Project Structure

> Standard project layout for RabbitMQ-based applications.

## Project Layout

```
my-rabbitmq-app/
├── src/
│   ├── producers/
│   │   ├── __init__.py
│   │   ├── order_producer.py
│   │   └── notification_producer.py
│   ├── consumers/
│   │   ├── __init__.py
│   │   ├── order_consumer.py
│   │   └── notification_consumer.py
│   ├── exchanges/
│   │   ├── __init__.py
│   │   └── exchange_definitions.py
│   ├── queues/
│   │   ├── __init__.py
│   │   └── queue_definitions.py
│   ├── config/
│   │   ├── __init__.py
│   │   ├── settings.py
│   │   └── connection.py
│   └── utils/
│       ├── __init__.py
│       ├── retry.py
│       └── logging.py
├── config/
│   ├── rabbitmq.conf
│   ├── definitions.json
│   └── enabled_plugins
├── docker/
│   ├── Dockerfile
│   └── docker-compose.yml
├── tests/
│   ├── unit/
│   ├── integration/
│   └── fixtures/
├── scripts/
│   ├── setup.sh
│   └── teardown.sh
├── docs/
├── requirements.txt
├── setup.py
└── README.md
```

## Connection Module

```python
# config/connection.py
import pika

def create_connection(host='localhost', port=5672, vhost='/'):
    credentials = pika.PlainCredentials('guest', 'guest')
    parameters = pika.ConnectionParameters(
        host=host,
        port=port,
        virtual_host=vhost,
        credentials=credentials,
        heartbeat=600,
        blocked_connection_timeout=300
    )
    return pika.BlockingConnection(parameters)
```

## Producer Pattern

```python
# producers/order_producer.py
class OrderProducer:
    EXCHANGE = 'orders'
    ROUTING_KEY = 'order.created'

    def __init__(self, connection):
        self.channel = connection.channel()
        self.channel.exchange_declare(
            exchange=self.EXCHANGE,
            exchange_type='topic',
            durable=True
        )

    def publish(self, order_data):
        self.channel.basic_publish(
            exchange=self.EXCHANGE,
            routing_key=self.ROUTING_KEY,
            body=json.dumps(order_data),
            properties=pika.BasicProperties(
                delivery_mode=2,
                content_type='application/json'
            )
        )
```

## Consumer Pattern

```python
# consumers/order_consumer.py
class OrderConsumer:
    QUEUE = 'order-processing'

    def __init__(self, connection):
        self.channel = connection.channel()
        self.channel.queue_declare(queue=self.QUEUE, durable=True)
        self.channel.basic_qos(prefetch_count=10)

    def start(self):
        self.channel.basic_consume(
            queue=self.QUEUE,
            on_message_callback=self.process_order
        )
        self.channel.start_consuming()

    def process_order(self, channel, method, properties, body):
        try:
            order = json.loads(body)
            # Process order
            channel.basic_ack(delivery_tag=method.delivery_tag)
        except Exception as e:
            channel.basic_nack(
                delivery_tag=method.delivery_tag,
                requeue=False
            )
```

## Exchange Definitions

```python
# exchanges/exchange_definitions.py
EXCHANGES = {
    'orders': {
        'type': 'topic',
        'durable': True,
        'arguments': {}
    },
    'notifications': {
        'type': 'fanout',
        'durable': True,
        'arguments': {}
    },
    'logs': {
        'type': 'topic',
        'durable': True,
        'arguments': {}
    }
}
```

## Queue Definitions

```python
# queues/queue_definitions.py
QUEUES = {
    'order-processing': {
        'durable': True,
        'arguments': {
            'x-dead-letter-exchange': 'dlx',
            'x-message-ttl': 86400000
        }
    },
    'notification-email': {
        'durable': True,
        'arguments': {}
    }
}
```

## Configuration

```python
# config/settings.py
import os

RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', 'localhost')
RABBITMQ_PORT = int(os.getenv('RABBITMQ_PORT', 5672))
RABBITMQ_VHOST = os.getenv('RABBITMQ_VHOST', '/')
RABBITMQ_USER = os.getenv('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.getenv('RABBITMQ_PASS', 'guest')
```

## Test Structure

```
tests/
├── unit/
│   ├── test_producer.py
│   └── test_consumer.py
├── integration/
│   ├── test_message_flow.py
│   └── test_exchanges.py
└── fixtures/
    ├── sample_messages.json
    └── mock_rabbitmq.py
```

## References

- [RabbitMQ Tutorials](https://www.rabbitmq.com/tutorials/)
- [Pika Documentation](https://pika.readthedocs.io/)

---
**Prerequisites:** [RabbitMQ core-concepts](core-concepts.md)
**Related:** [RabbitMQ configuration](configuration.md) | [RabbitMQ best-practices](best-practices.md)
**Next:** [RabbitMQ performance](performance.md)
