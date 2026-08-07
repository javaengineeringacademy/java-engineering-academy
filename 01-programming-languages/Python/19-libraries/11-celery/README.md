# Celery

## Why Celery Exists

Every Python developer building web applications needs to run background tasks: sending emails, processing images, generating reports. Doing these in the request-response cycle makes users wait. Celery was created to solve this by providing a distributed task queue that offloads work to background workers. It supports multiple message brokers (RabbitMQ, Redis) and result backends, making it the standard for asynchronous task processing in Python.

## What You'll Learn

By the end of this section, you'll be able to:

- Define and execute background tasks with proper error handling
- Configure task queues, routing, and scheduling
- Monitor task execution using Flower and built-in tools

## When to Use Celery

| Use Case | Why Celery | Alternative |
|----------|----------|-------------|
| Email sending | Async delivery, retry logic | Synchronous |
| Image processing | Offload CPU-intensive work | Background thread |
| Report generation | Long-running tasks without blocking | Synchronous |
| Scheduled tasks | Periodic job execution | cron |
| Webhooks | Reliable delivery with retries | Direct HTTP calls |
| Data aggregation | Batch processing | Synchronous |

## How Celery Works Internally

Celery uses a message broker (Redis or RabbitMQ) to communicate between your application and worker processes. When you call `task.delay(args)`, Celery serializes the task and arguments, puts them on a message queue, and returns immediately. A worker process picks up the message, deserializes it, and executes the task.

Task results are stored in a result backend (optional). The worker updates the task status as it progresses: PENDING → STARTED → SUCCESS/FAILURE. You can check results, set timeouts, and handle errors. Celery also supports task chaining, grouping, and routing for complex workflows.

```python
from celery import Celery

app = Celery('tasks', broker='redis://localhost:6379/0')

@app.task
def send_email(to, subject, body):
    # Send email logic here
    return f"Email sent to {to}"

@app.task(bind=True, max_retries=3)
def process_image(self, image_path):
    try:
        # Process image
        return "Processed"
    except Exception as exc:
        self.retry(exc=exc, countdown=60)

# Execute asynchronously
send_email.delay('alice@example.com', 'Welcome', 'Hello!')
```

## Production Checklist

### ✅ Before using Celery in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Celery is only for large applications
**Reality:** Celery works well for any application that needs background tasks. Even small apps benefit from async email sending or image processing.

### ❌ Myth 2: Redis is the best broker for Celery
**Reality:** RabbitMQ is more reliable for production use cases. Redis is simpler to set up but can lose messages on crash. Choose based on your reliability requirements.

### ❌ Myth 3: Celery tasks are always reliable
**Reality:** Tasks can fail due to network issues, worker crashes, or code errors. Always implement retries, dead letter queues, and monitoring.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Distributed task queue |
| Complexity | O(1) for task submission |
| Thread Safe | Yes (per worker) |
| Best Alternative | RQ for simpler use cases |
| When to Use | Background tasks, async processing |
| When to Avoid | Simple scripts without workers |

## Related Topics

- [10-redis](../10-redis/) - Redis as broker/backend
- [05-django](../05-django/) - Django integration
- [04-flask](../04-flask/) - Flask integration
