# Todo API

A RESTful API for managing todo items built with Flask, demonstrating HTTP methods, JSON handling, and API design.

## Features

- Create, read, update, and delete todos
- Filter todos by status (pending/completed)
- Search todos by title
- Input validation
- Proper HTTP status codes
- JSON request/response format

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/todos | List all todos |
| GET | /api/todos/<id> | Get a specific todo |
| POST | /api/todos | Create a new todo |
| PUT | /api/todos/<id> | Update a todo |
| DELETE | /api/todos/<id> | Delete a todo |
| GET | /api/todos/stats | Get todo statistics |

## Architecture

```
todo-api/
├── app.py       # Flask application setup
├── models.py    # Todo data model
├── routes.py    # API route handlers
├── test_api.py  # API tests
└── README.md    # This file
```

## Learning Objectives

- RESTful API design principles
- Flask routing and request handling
- JSON serialization/deserialization
- HTTP methods and status codes
- API testing with pytest

## How to Run

```bash
# Install dependencies
pip install flask pytest

# Run the server
python app.py

# Run tests
python -m pytest test_api.py -v
```
