import redis

# Connect to Redis
r = redis.Redis(host='localhost', port=6379, db=0, decode_responses=True)

# String operations
r.set('name', 'Alice')
print(r.get('name'))

# Hash operations
r.hset('user:1', mapping={'name': 'Bob', 'age': '30'})
print(r.hgetall('user:1'))

# List operations
r.lpush('queue', 'task1', 'task2', 'task3')
print(r.lrange('queue', 0, -1))

# Set operations
r.sadd('tags', 'python', 'redis', 'cache')
print(r.smembers('tags'))

# Check if key exists
print(r.exists('name'))
