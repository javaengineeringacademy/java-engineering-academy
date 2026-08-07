"""Tests for the Todo API."""

import pytest
from app import create_app
from routes import todos


@pytest.fixture
def client():
    """Create test client."""
    app = create_app(testing=True)
    with app.test_client() as client:
        yield client
    todos.clear()


@pytest.fixture
def sample_todo():
    """Create a sample todo for testing."""
    return {"title": "Test Todo", "description": "Test description", "priority": 2}


class TestGetTodos:
    """Tests for GET /api/todos endpoint."""
    
    def test_get_todos_empty(self, client):
        """Test getting todos when none exist."""
        response = client.get("/api/todos")
        assert response.status_code == 200
        assert response.get_json() == []
    
    def test_get_todos_with_data(self, client, sample_todo):
        """Test getting todos with existing data."""
        client.post("/api/todos", json=sample_todo)
        response = client.get("/api/todos")
        assert response.status_code == 200
        assert len(response.get_json()) == 1


class TestCreateTodo:
    """Tests for POST /api/todos endpoint."""
    
    def test_create_todo_success(self, client, sample_todo):
        """Test successful todo creation."""
        response = client.post("/api/todos", json=sample_todo)
        assert response.status_code == 201
        data = response.get_json()
        assert data["title"] == "Test Todo"
        assert "id" in data
    
    def test_create_todo_no_title(self, client):
        """Test todo creation without title fails."""
        response = client.post("/api/todos", json={"description": "No title"})
        assert response.status_code == 400
    
    def test_create_todo_empty_title(self, client):
        """Test todo creation with empty title fails."""
        response = client.post("/api/todos", json={"title": "  "})
        assert response.status_code == 400
    
    def test_create_todo_invalid_priority(self, client):
        """Test todo creation with invalid priority fails."""
        response = client.post("/api/todos", json={"title": "Test", "priority": 5})
        assert response.status_code == 400


class TestGetTodo:
    """Tests for GET /api/todos/<id> endpoint."""
    
    def test_get_todo_success(self, client, sample_todo):
        """Test getting a specific todo."""
        create_response = client.post("/api/todos", json=sample_todo)
        todo_id = create_response.get_json()["id"]
        
        response = client.get(f"/api/todos/{todo_id}")
        assert response.status_code == 200
        assert response.get_json()["title"] == "Test Todo"
    
    def test_get_todo_not_found(self, client):
        """Test getting nonexistent todo returns 404."""
        response = client.get("/api/todos/nonexistent")
        assert response.status_code == 404


class TestUpdateTodo:
    """Tests for PUT /api/todos/<id> endpoint."""
    
    def test_update_todo_success(self, client, sample_todo):
        """Test successful todo update."""
        create_response = client.post("/api/todos", json=sample_todo)
        todo_id = create_response.get_json()["id"]
        
        response = client.put(f"/api/todos/{todo_id}", json={"completed": True})
        assert response.status_code == 200
        assert response.get_json()["completed"] is True
    
    def test_update_todo_not_found(self, client):
        """Test updating nonexistent todo returns 404."""
        response = client.put("/api/todos/nonexistent", json={"title": "Updated"})
        assert response.status_code == 404


class TestDeleteTodo:
    """Tests for DELETE /api/todos/<id> endpoint."""
    
    def test_delete_todo_success(self, client, sample_todo):
        """Test successful todo deletion."""
        create_response = client.post("/api/todos", json=sample_todo)
        todo_id = create_response.get_json()["id"]
        
        response = client.delete(f"/api/todos/{todo_id}")
        assert response.status_code == 200
        
        # Verify deletion
        get_response = client.get(f"/api/todos/{todo_id}")
        assert get_response.status_code == 404
    
    def test_delete_todo_not_found(self, client):
        """Test deleting nonexistent todo returns 404."""
        response = client.delete("/api/todos/nonexistent")
        assert response.status_code == 404


class TestGetStats:
    """Tests for GET /api/todos/stats endpoint."""
    
    def test_get_stats_empty(self, client):
        """Test stats with no todos."""
        response = client.get("/api/todos/stats")
        assert response.status_code == 200
        data = response.get_json()
        assert data["total"] == 0
        assert data["completed"] == 0
    
    def test_get_stats_with_todos(self, client):
        """Test stats with various todos."""
        client.post("/api/todos", json={"title": "Todo 1", "completed": True})
        client.post("/api/todos", json={"title": "Todo 2", "completed": False})
        client.post("/api/todos", json={"title": "Todo 3", "priority": 3})
        
        response = client.get("/api/todos/stats")
        data = response.get_json()
        assert data["total"] == 3
        assert data["completed"] == 1
        assert data["pending"] == 2
        assert data["high_priority_pending"] == 1


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
