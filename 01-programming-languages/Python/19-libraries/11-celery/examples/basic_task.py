from celery import Celery

app = Celery('tasks', broker='redis://localhost:6379/0', backend='redis://localhost:6379/0')

@app.task
def add(x, y):
    return x + y

@app.task
def process_data(data):
    # Simulate processing
    return f"Processed: {data}"

if __name__ == "__main__":
    result = add.delay(4, 4)
    print(f"Task ID: {result.id}")
    print(f"Result: {result.get(timeout=10)}")
