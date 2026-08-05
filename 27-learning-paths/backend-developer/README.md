# Backend Developer Learning Path

Comprehensive roadmap for building robust backend systems.

## Overview

This learning path covers backend development from APIs to databases, caching, and messaging systems.

## Prerequisites

- Programming fundamentals (any language)
- Basic database concepts
- HTTP protocol basics
- Git basics

## Learning Path

### Phase 1: Core Backend Concepts (4-6 weeks)

#### Week 1-2: APIs and Web Services
- [ ] REST API design principles
- [ ] HTTP methods and status codes
- [ ] API versioning
- [ ] Authentication and authorization

**Resources:**
- "Designing Data-Intensive Applications" by Martin Kleppmann
- REST API design guides
- Postman for API testing

**Practice:**
```python
# Flask REST API
from flask import Flask, jsonify, request

app = Flask(__name__)

users = [
    {"id": 1, "name": "Alice", "email": "alice@example.com"},
    {"id": 2, "name": "Bob", "email": "bob@example.com"}
]

@app.route('/api/users', methods=['GET'])
def get_users():
    return jsonify(users)

@app.route('/api/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    user = next((u for u in users if u['id'] == user_id), None)
    if user:
        return jsonify(user)
    return jsonify({"error": "User not found"}), 404

@app.route('/api/users', methods=['POST'])
def create_user():
    data = request.get_json()
    new_user = {
        "id": len(users) + 1,
        "name": data['name'],
        "email": data['email']
    }
    users.append(new_user)
    return jsonify(new_user), 201
```

#### Week 3-4: Database Fundamentals
- [ ] SQL basics (SELECT, INSERT, UPDATE, DELETE)
- [ ] Database design and normalization
- [ ] Indexing strategies
- [ ] Transactions and ACID

**Practice:**
```sql
-- Create tables
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index
CREATE INDEX idx_posts_user_id ON posts(user_id);

-- Query with JOIN
SELECT u.name, p.title, p.created_at
FROM users u
JOIN posts p ON u.id = p.user_id
ORDER BY p.created_at DESC;
```

#### Week 5-6: Authentication and Security
- [ ] JWT authentication
- [ ] OAuth 2.0
- [ ] Password hashing
- [ ] API security best practices

**Practice:**
```python
# JWT authentication
import jwt
from datetime import datetime, timedelta
from functools import wraps

SECRET_KEY = 'your-secret-key'

def create_token(user_id):
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=1),
        'iat': datetime.utcnow()
    }
    return jwt.encode(payload, SECRET_KEY, algorithm='HS256')

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        if not token:
            return jsonify({'error': 'Token is missing'}), 401
        
        try:
            token = token.split(' ')[1]
            data = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
            current_user = data['user_id']
        except jwt.ExpiredSignatureError:
            return jsonify({'error': 'Token has expired'}), 401
        except jwt.InvalidTokenError:
            return jsonify({'error': 'Invalid token'}), 401
        
        return f(current_user, *args, **kwargs)
    return decorated

@app.route('/api/protected', methods=['GET'])
@token_required
def protected_route(current_user):
    return jsonify({'message': f'Hello user {current_user}'})
```

### Phase 2: Database Mastery (4-6 weeks)

#### Week 7-8: Advanced SQL
- [ ] Complex queries and subqueries
- [ ] Window functions
- [ ] Query optimization
- [ ] Database performance tuning

**Practice:**
```sql
-- Window functions
SELECT 
    name,
    department,
    salary,
    RANK() OVER (PARTITION BY department ORDER BY salary DESC) as rank,
    AVG(salary) OVER (PARTITION BY department) as avg_salary
FROM employees;

-- CTEs (Common Table Expressions)
WITH monthly_sales AS (
    SELECT 
        DATE_TRUNC('month', created_at) as month,
        SUM(total) as total_sales
    FROM orders
    GROUP BY DATE_TRUNC('month', created_at)
)
SELECT 
    month,
    total_sales,
    LAG(total_sales) OVER (ORDER BY month) as prev_month_sales,
    (total_sales - LAG(total_sales) OVER (ORDER BY month)) / 
        LAG(total_sales) OVER (ORDER BY month) * 100 as growth_rate
FROM monthly_sales;
```

#### Week 9-10: NoSQL Databases
- [ ] Document databases (MongoDB)
- [ ] Key-value stores (Redis)
- [ ] When to use SQL vs. NoSQL
- [ ] Data modeling for NoSQL

**Practice:**
```javascript
// MongoDB operations
// Insert document
db.users.insertOne({
    name: "Alice",
    email: "alice@example.com",
    preferences: {
        theme: "dark",
        language: "en"
    }
});

// Query with aggregation
db.orders.aggregate([
    { $match: { status: "completed" } },
    { $group: { 
        _id: "$user_id", 
        totalSpent: { $sum: "$total" },
        orderCount: { $sum: 1 }
    }},
    { $sort: { totalSpent: -1 } }
]);

// Redis operations
// Set key-value
redis.set("user:1:name", "Alice");
redis.setex("session:abc123", 3600, "user_data");

// Get value
const name = await redis.get("user:1:name");

// Hash operations
redis.hset("user:1", "name", "Alice", "email", "alice@example.com");
redis.hgetall("user:1");
```

#### Week 11-12: Caching Strategies
- [ ] Cache-aside pattern
- [ ] Write-through cache
- [ ] Cache invalidation
- [ ] Distributed caching

**Practice:**
```python
# Redis caching
import redis
import json
from functools import wraps

redis_client = redis.Redis(host='localhost', port=6379, db=0)

def cache_result(expiration=3600):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            cache_key = f"{func.__name__}:{str(args)}:{str(kwargs)}"
            
            # Try to get from cache
            cached = redis_client.get(cache_key)
            if cached:
                return json.loads(cached)
            
            # Execute function
            result = func(*args, **kwargs)
            
            # Store in cache
            redis_client.setex(cache_key, expiration, json.dumps(result))
            
            return result
        return wrapper
    return decorator

@cache_result(expiration=300)
def get_user(user_id):
    # Database query
    return {"id": user_id, "name": "Alice"}

# Cache invalidation
def invalidate_user_cache(user_id):
    cache_key = f"get_user:({user_id},):{{}}"
    redis_client.delete(cache_key)
```

### Phase 3: Messaging and Async Processing (3-4 weeks)

#### Week 13-14: Message Queues
- [ ] Apache Kafka basics
- [ ] RabbitMQ concepts
- [ ] Event-driven architecture
- [ ] Message patterns (pub/sub, queue)

**Practice:**
```python
# Kafka producer
from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Send message
producer.send('user-events', value={
    'event': 'user_created',
    'user_id': 123,
    'timestamp': '2024-01-15T10:30:00Z'
})

# Kafka consumer
from kafka import KafkaConsumer
import json

consumer = KafkaConsumer(
    'user-events',
    bootstrap_servers=['localhost:9092'],
    group_id='user-service',
    value_deserializer=lambda m: json.loads(m.decode('utf-8'))
)

for message in consumer:
    event = message.value
    print(f"Processing event: {event['event']}")
    # Process event
```

#### Week 15-16: Background Jobs
- [ ] Celery with Redis/RabbitMQ
- [ ] Job scheduling
- [ ] Retry mechanisms
- [ ] Monitoring and logging

**Practice:**
```python
# Celery configuration
from celery import Celery

app = Celery('tasks', broker='redis://localhost:6379/0')

@app.task(bind=True, max_retries=3)
def process_payment(self, order_id):
    try:
        # Process payment logic
        print(f"Processing payment for order {order_id}")
        return {"status": "success", "order_id": order_id}
    except Exception as exc:
        # Retry on failure
        self.retry(exc=exc, countdown=60)

@app.task
def send_email(to, subject, body):
    # Send email logic
    print(f"Sending email to {to}")

# Schedule tasks
from celery.schedules import crontab

app.conf.beat_schedule = {
    'daily-report': {
        'task': 'tasks.generate_report',
        'schedule': crontab(hour=8, minute=0),
    },
}
```

### Phase 4: Advanced Topics (4-6 weeks)

#### Week 17-18: Microservices Architecture
- [ ] Service decomposition
- [ ] API gateways
- [ ] Service discovery
- [ ] Circuit breakers

**Practice:**
```python
# Simple microservice structure
# user-service/app.py
from flask import Flask, jsonify
import requests

app = Flask(__name__)

@app.route('/users/<int:user_id>')
def get_user(user_id):
    # Get user from database
    user = {"id": user_id, "name": "Alice"}
    
    # Call order service
    orders = requests.get(f'http://order-service/orders?user_id={user_id}').json()
    
    user['orders'] = orders
    return jsonify(user)

# Circuit breaker pattern
from circuitbreaker import circuit

@circuit(failure_threshold=5, recovery_timeout=60)
def call_external_service():
    response = requests.get('http://external-service/api/data')
    return response.json()
```

#### Week 19-20: Performance Optimization
- [ ] Profiling and benchmarking
- [ ] Query optimization
- [ ] Connection pooling
- [ ] Load testing

**Practice:**
```python
# Connection pooling
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# Create engine with connection pooling
engine = create_engine(
    'postgresql://user:pass@localhost/db',
    pool_size=20,
    max_overflow=30,
    pool_timeout=30,
    pool_recycle=1800
)

Session = sessionmaker(bind=engine)

# Use context manager for sessions
with Session() as session:
    users = session.query(User).all()
```

### Phase 5: Project Work (4-6 weeks)

#### Project Ideas
1. **Blog Platform**: User auth, posts, comments
2. **E-commerce API**: Products, orders, payments
3. **Chat Application**: Real-time messaging
4. **Task Management System**: Projects, tasks, teams

## Certification Path

### Recommended Certifications
- **AWS Certified Developer**
- **Google Cloud Professional Developer**
- **Azure Developer Associate**

## Career Progression

### Junior Backend Developer (0-2 years)
- Build REST APIs
- Write database queries
- Understand caching
- Implement basic authentication

### Mid-Level Backend Developer (2-5 years)
- Design complex systems
- Implement microservices
- Optimize performance
- Mentor junior developers

### Senior Backend Developer (5+ years)
- Architect systems
- Make technology choices
- Drive technical strategy
- Lead teams

## Resources

### Books
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "Building Microservices" by Sam Newman
- "The Backend Developer Handbook" by Frontend Masters

### Online
- Backend Developer Roadmap
- Microservices.io
- High Scalability blog

### Practice
- Build personal projects
- Contribute to open source
- Solve backend challenges on LeetCode

## Next Steps

After completing this path:
- [19-case-studies](../19-case-studies/) - Learn from real-world examples
- [20-interview-preparation](../20-interview-preparation/) - Prepare for interviews
- [24-certifications](../24-certifications/) - Pursue certifications