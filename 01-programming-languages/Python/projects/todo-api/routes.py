"""API route handlers for Todo operations."""

from flask import Blueprint, request, jsonify
from models import Todo
from datetime import datetime
from typing import Dict, List

# Blueprint for todo routes
todo_bp = Blueprint("todos", __name__)

# In-memory storage (would use database in production)
todos: Dict[str, Todo] = {}


def validate_todo_data(data: dict) -> tuple:
    """Validate todo input data."""
    if not data:
        return False, "No data provided"
    
    if "title" not in data or not data["title"].strip():
        return False, "Title is required"
    
    if len(data["title"]) > 200:
        return False, "Title must be 200 characters or less"
    
    if "priority" in data:
        if data["priority"] not in [1, 2, 3]:
            return False, "Priority must be 1, 2, or 3"
    
    return True, None


@todo_bp.route("/todos", methods=["GET"])
def get_todos():
    """Get all todos with optional filtering."""
    status = request.args.get("status")
    search = request.args.get("search")
    
    todo_list = list(todos.values())
    
    # Filter by status
    if status == "completed":
        todo_list = [t for t in todo_list if t.completed]
    elif status == "pending":
        todo_list = [t for t in todo_list if not t.completed]
    
    # Search by title
    if search:
        todo_list = [t for t in todo_list if search.lower() in t.title.lower()]
    
    return jsonify([t.to_dict() for t in todo_list]), 200


@todo_bp.route("/todos/<todo_id>", methods=["GET"])
def get_todo(todo_id: str):
    """Get a specific todo by ID."""
    if todo_id not in todos:
        return jsonify({"error": "Todo not found"}), 404
    
    return jsonify(todos[todo_id].to_dict()), 200


@todo_bp.route("/todos", methods=["POST"])
def create_todo():
    """Create a new todo."""
    data = request.get_json()
    
    valid, error = validate_todo_data(data)
    if not valid:
        return jsonify({"error": error}), 400
    
    todo = Todo.from_dict(data)
    todos[todo.id] = todo
    
    return jsonify(todo.to_dict()), 201


@todo_bp.route("/todos/<todo_id>", methods=["PUT"])
def update_todo(todo_id: str):
    """Update an existing todo."""
    if todo_id not in todos:
        return jsonify({"error": "Todo not found"}), 404
    
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided"}), 400
    
    todo = todos[todo_id]
    todo.update(**data)
    
    return jsonify(todo.to_dict()), 200


@todo_bp.route("/todos/<todo_id>", methods=["DELETE"])
def delete_todo(todo_id: str):
    """Delete a todo."""
    if todo_id not in todos:
        return jsonify({"error": "Todo not found"}), 404
    
    del todos[todo_id]
    return jsonify({"message": "Todo deleted"}), 200


@todo_bp.route("/todos/stats", methods=["GET"])
def get_stats():
    """Get todo statistics."""
    total = len(todos)
    completed = sum(1 for t in todos.values() if t.completed)
    pending = total - completed
    
    high_priority = sum(1 for t in todos.values() if t.priority == 3 and not t.completed)
    
    return jsonify({
        "total": total,
        "completed": completed,
        "pending": pending,
        "high_priority_pending": high_priority
    }), 200
