"""
Flask Basic Application
Demonstrates routes, JSON responses, error handling, and blueprints
"""

from flask import Flask, request, jsonify, Blueprint

app = Flask(__name__)

# ============================================
# In-Memory Data Store (for demo)
# ============================================

users = {
    1: {'id': 1, 'name': 'Alice', 'email': 'alice@example.com'},
    2: {'id': 2, 'name': 'Bob', 'email': 'bob@example.com'},
    3: {'id': 3, 'name': 'Charlie', 'email': 'charlie@example.com'},
}
next_id = 4

# ============================================
# Main Routes
# ============================================

@app.route('/')
def home():
    return jsonify({
        'message': 'Welcome to Flask API',
        'version': '1.0.0',
        'endpoints': ['/api/users', '/api/users/<id>']
    })

@app.route('/api/users', methods=['GET'])
def list_users():
    """List all users."""
    return jsonify({
        'users': list(users.values()),
        'count': len(users)
    })

@app.route('/api/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    """Get a specific user."""
    if user_id not in users:
        return jsonify({'error': f'User {user_id} not found'}), 404
    return jsonify(users[user_id])

@app.route('/api/users', methods=['POST'])
def create_user():
    """Create a new user."""
    global next_id

    data = request.get_json()
    if not data or 'name' not in data:
        return jsonify({'error': 'Name is required'}), 400

    user = {
        'id': next_id,
        'name': data['name'],
        'email': data.get('email', '')
    }
    users[next_id] = user
    next_id += 1

    return jsonify(user), 201

@app.route('/api/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    """Delete a user."""
    if user_id not in users:
        return jsonify({'error': f'User {user_id} not found'}), 404

    deleted = users.pop(user_id)
    return jsonify({'deleted': deleted})

# ============================================
# Blueprint Example
# ============================================

health = Blueprint('health', __name__)

@health.route('/health')
def health_check():
    return jsonify({'status': 'healthy'})

@health.route('/health/ready')
def ready():
    return jsonify({'status': 'ready'})

app.register_blueprint(health)

# ============================================
# Error Handlers
# ============================================

@app.errorhandler(404)
def not_found(error):
    return jsonify({'error': 'Resource not found'}), 404

@app.errorhandler(500)
def server_error(error):
    return jsonify({'error': 'Internal server error'}), 500

# ============================================
# Main Execution
# ============================================

if __name__ == '__main__':
    print("Flask API running at http://127.0.0.1:5000")
    print("Endpoints:")
    print("  GET    /                  - Home")
    print("  GET    /api/users         - List users")
    print("  GET    /api/users/<id>    - Get user")
    print("  POST   /api/users         - Create user")
    print("  DELETE /api/users/<id>    - Delete user")
    print("  GET    /health            - Health check")
    print()
    print("Test with: curl http://127.0.0.1:5000/api/users")
    print()

    app.run(debug=True, port=5000)
