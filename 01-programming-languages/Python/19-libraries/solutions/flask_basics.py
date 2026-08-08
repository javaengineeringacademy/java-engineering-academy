"""
Module 19 - Libraries: Flask Basics Solutions
Complete solutions with explanations
"""

from flask import Flask, request, jsonify, render_template_string
from functools import wraps


# =============================================================================
# Exercise 1: Basic App - SOLUTION
# =============================================================================

def exercise_1_basic_app():
    """
    Create a basic Flask application.
    """
    app = Flask(__name__)
    
    @app.route('/')
    def home():
        return jsonify({'message': 'Hello, World!'})
    
    @app.route('/about')
    def about():
        return jsonify({'about': 'This is a Flask application'})
    
    @app.route('/api/status')
    def status():
        return jsonify({'status': 'healthy', 'version': '1.0.0'})
    
    return app


# =============================================================================
# Exercise 2: Routing - SOLUTION
# =============================================================================

def exercise_2_routing():
    """
    Implement different routing patterns.
    """
    app = Flask(__name__)
    
    @app.route('/user/<int:user_id>')
    def get_user(user_id):
        return jsonify({'user_id': user_id, 'name': f'User {user_id}'})
    
    @app.route('/item/<string:item_name>')
    def get_item(item_name):
        return jsonify({'item': item_name})
    
    @app.route('/data', methods=['GET'])
    def get_data():
        return jsonify({'data': 'GET request'})
    
    @app.route('/data', methods=['POST'])
    def create_data():
        data = request.get_json()
        return jsonify({'received': data}), 201
    
    @app.route('/search')
    def search():
        query = request.args.get('q', '')
        return jsonify({'query': query})
    
    return app


# =============================================================================
# Exercise 3: Templates - SOLUTION
# =============================================================================

def exercise_3_templates():
    """
    Use Jinja2 templates.
    """
    app = Flask(__name__)
    
    base_template = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>{% block title %}My App{% endblock %}</title>
    </head>
    <body>
        {% block content %}{% endblock %}
    </body>
    </html>
    """
    
    hello_template = """
    {% extends base_template %}
    {% block title %}Hello{% endblock %}
    {% block content %}
    <h1>Hello, {{ name }}!</h1>
    <p>Welcome to Flask.</p>
    {% endblock %}
    """
    
    @app.route('/hello/<name>')
    def hello(name):
        return render_template_string(
            hello_template,
            base_template=base_template,
            name=name
        )
    
    @app.route('/items')
    def items():
        items_list = ['Apple', 'Banana', 'Cherry']
        template = """
        <ul>
        {% for item in items_list %}
            <li>{{ item }}</li>
        {% endfor %}
        </ul>
        """
        return render_template_string(template, items_list=items_list)
    
    return app


# =============================================================================
# Exercise 4: Forms and Validation - SOLUTION
# =============================================================================

def exercise_4_forms():
    """
    Handle forms and validation.
    """
    app = Flask(__name__)
    
    def validate_email(email):
        import re
        pattern = r'^[\w\.-]+@[\w\.-]+\.\w+$'
        return bool(re.match(pattern, email))
    
    @app.route('/register', methods=['POST'])
    def register():
        data = request.get_json()
        
        # Validation
        if not data:
            return jsonify({'error': 'No data provided'}), 400
        
        if not data.get('name'):
            return jsonify({'error': 'Name is required'}), 400
        
        if not data.get('email'):
            return jsonify({'error': 'Email is required'}), 400
        
        if not validate_email(data['email']):
            return jsonify({'error': 'Invalid email format'}), 400
        
        if not data.get('password') or len(data['password']) < 6:
            return jsonify({'error': 'Password must be at least 6 characters'}), 400
        
        # Success
        return jsonify({
            'message': 'Registration successful',
            'user': {
                'name': data['name'],
                'email': data['email']
            }
        }), 201
    
    @app.route('/login', methods=['POST'])
    def login():
        data = request.get_json()
        
        if not data or not data.get('email') or not data.get('password'):
            return jsonify({'error': 'Email and password required'}), 400
        
        # Simulate authentication
        if data['email'] == 'user@example.com' and data['password'] == 'password':
            return jsonify({'message': 'Login successful', 'token': 'abc123'})
        
        return jsonify({'error': 'Invalid credentials'}), 401
    
    return app


# =============================================================================
# Exercise 5: Error Handling - SOLUTION
# =============================================================================

def exercise_5_error_handling():
    """
    Implement error handling.
    """
    app = Flask(__name__)
    
    class NotFoundError(Exception):
        pass
    
    class ValidationError(Exception):
        pass
    
    @app.errorhandler(404)
    def not_found(error):
        return jsonify({
            'error': 'Not found',
            'message': str(error)
        }), 404
    
    @app.errorhandler(400)
    def bad_request(error):
        return jsonify({
            'error': 'Bad request',
            'message': str(error)
        }), 400
    
    @app.errorhandler(500)
    def internal_error(error):
        return jsonify({
            'error': 'Internal server error',
            'message': 'An unexpected error occurred'
        }), 500
    
    @app.errorhandler(NotFoundError)
    def handle_not_found(error):
        return jsonify({
            'error': 'Resource not found',
            'message': str(error)
        }), 404
    
    @app.errorhandler(ValidationError)
    def handle_validation(error):
        return jsonify({
            'error': 'Validation failed',
            'message': str(error)
        }), 422
    
    @app.route('/user/<int:user_id>')
    def get_user(user_id):
        # Simulate user lookup
        if user_id == 1:
            return jsonify({'id': 1, 'name': 'John'})
        raise NotFoundError(f'User {user_id} not found')
    
    @app.route('/validate', methods=['POST'])
    def validate():
        data = request.get_json()
        if not data or not data.get('value'):
            raise ValidationError('Value is required')
        return jsonify({'valid': True, 'value': data['value']})
    
    return app


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 19 - Flask Basics Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic App")
    app = exercise_1_basic_app()
    client = app.test_client()
    
    response = client.get('/')
    assert response.status_code == 200
    assert response.get_json()['message'] == 'Hello, World!'
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Routing")
    app = exercise_2_routing()
    client = app.test_client()
    
    response = client.get('/user/123')
    assert response.status_code == 200
    assert response.get_json()['user_id'] == 123
    
    response = client.post('/data', json={'key': 'value'})
    assert response.status_code == 201
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Templates")
    app = exercise_3_templates()
    client = app.test_client()
    
    response = client.get('/hello/Alice')
    assert response.status_code == 200
    assert 'Alice' in response.data.decode()
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Forms and Validation")
    app = exercise_4_forms()
    client = app.test_client()
    
    # Successful registration
    response = client.post('/register', json={
        'name': 'John',
        'email': 'john@example.com',
        'password': 'password123'
    })
    assert response.status_code == 201
    
    # Invalid email
    response = client.post('/register', json={
        'name': 'John',
        'email': 'invalid',
        'password': 'password123'
    })
    assert response.status_code == 400
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Error Handling")
    app = exercise_5_error_handling()
    client = app.test_client()
    
    # Existing user
    response = client.get('/user/1')
    assert response.status_code == 200
    
    # Non-existing user
    response = client.get('/user/999')
    assert response.status_code == 404
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
