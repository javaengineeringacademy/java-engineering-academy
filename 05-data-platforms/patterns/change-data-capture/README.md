# Change Data Capture Pattern

## Overview

Change Data Capture (CDC) identifies and captures changes made to data in a database, then delivers those changes to downstream systems in real time. CDC extracts insert, update, and delete operations from the database transaction log without modifying source applications.

CDC enables real-time data synchronization, event-driven architectures, and data replication by treating database changes as a stream of events that other systems can consume.

## When to Use

- Real-time data synchronization between systems is needed
- Event-driven architectures require database change events
- Data warehouses need near-real-time updates from operational databases
- Audit logging of data changes is required
- Decoupling downstream consumers from source database schema

## Implementation

### Debezium (Kafka Connect)

```json
{
  "name": "users-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "localhost",
    "database.port": "5432",
    "database.user": "cdc_user",
    "database.dbname": "mydb",
    "table.include.list": "public.users,public.orders",
    "topic.prefix": "dbserver1",
    "plugin.name": "pgoutput",
    "slot.name": "debezium_slot"
  }
}
```

### TypeScript (CDC Consumer)

```typescript
class CdcConsumer {
  constructor(private kafkaConsumer: KafkaConsumer) {}

  async start(): Promise<void> {
    await this.kafkaConsumer.subscribe({ topic: 'dbserver1.public.users' });

    await this.kafkaConsumer.run({
      eachMessage: async ({ message }) => {
        const event = JSON.parse(message.value!.toString());
        await this.handleChangeEvent(event);
      }
    });
  }

  private async handleChangeEvent(event: CdcEvent): Promise<void> {
    switch (event.op) {
      case 'c': // create
        await this.onUserCreated(event.after);
        break;
      case 'u': // update
        await this.onUserUpdated(event.before, event.after);
        break;
      case 'd': // delete
        await this.onUserDeleted(event.before);
        break;
    }
  }
}
```

### Java (Debezium Client)

```java
@Component
public class CdcEventListener {
    @KafkaListener(topics = "dbserver1.public.users")
    public void handleUserChange(ConsumerRecord<String, String> record) {
        JsonNode event = objectMapper.readTree(record.value());
        String operation = event.get("op").asText();

        switch (operation) {
            case "c":
                handleCreate(event.get("after"));
                break;
            case "u":
                handleUpdate(event.get("before"), event.get("after"));
                break;
            case "d":
                handleDelete(event.get("before"));
                break;
        }
    }
}
```

### Python (Database Trigger Approach)

```python
import psycopg2
import json

class CdcListener:
    def __init__(self, dsn: str):
        self.conn = psycopg2.connect(dsn)
        self.conn.set_isolation_level(psycopg2.extensions.ISOLATION_LEVEL_AUTOCOMMIT)

    def listen(self, channel: str):
        cursor = self.conn.cursor()
        cursor.execute(f'LISTEN {channel};')
        while True:
            self.conn.poll()
            while self.conn.notifies:
                notify = self.conn.notifies.pop()
                event = json.loads(notify.payload)
                self.process_event(event)

    def process_event(self, event: dict):
        if event['operation'] == 'INSERT':
            self.on_insert(event['table'], event['new'])
        elif event['operation'] == 'UPDATE':
            self.on_update(event['table'], event['old'], event['new'])
        elif event['operation'] == 'DELETE':
            self.on_delete(event['table'], event['old'])
```

## Best Practices

- Use database log-based CDC for minimal source impact
- Handle schema evolution gracefully with schema registry
- Implement idempotent consumers for at-least-once delivery
- Monitor lag between source changes and downstream consumption
- Use dead letter queues for failed CDC event processing
- Consider tombstone events for deletes in streaming platforms

## Interview Questions

1. What is the difference between CDC and polling-based synchronization?
2. How do you handle schema changes in CDC pipelines?
3. What are the tradeoffs between log-based and query-based CDC?
4. How do you ensure exactly-once processing with CDC events?
5. What monitoring is needed for CDC infrastructure?

## References

- Kleppmann, Martin. *Designing Data-Intensive Applications*, chapter on Derived Data
- Debezium Documentation. *Change Data Capture*
- Thompson, Ben. *Change Data Capture Patterns and Practices*
- Confluent. *CDC with Kafka Connect and Debezium*
