"""
Module 19 - Libraries: Flask Basics Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Flask web framework basics
"""


# =============================================================================
# Exercise 1: Basic App (⭐⭐⭐)
# =============================================================================

def exercise_1_basic_app():
    """
    Create a basic Flask application.
    
    TODO:
    1. Create Flask app
    2. Add basic routes
    3. Return JSON responses
    """
    from flask import Flask, jsonify
    
    app = Flask(__name__)
    
    # TODO: Add routes
    # @app.route('/')
    # def home():
    #     return jsonify({'message': 'Hello, World!'})
    
    return app


# =============================================================================
# Exercise 2: Routing (⭐⭐⭐)
# =============================================================================

def exercise_2_routing():
    """
    Implement different routing patterns.
    
    TODO:
    1. Route with parameters
    2. Different HTTP methods
    3. URL converters
    """
    from flask import Flask, request
    
    app = Flask(__name__)
    
    # TODO: Add routes
    # @app.route('/user/<int:user_id>')
    # def get_user(user_id):
    #     return jsonify({'user_id': user_id})
    
    return app


# =============================================================================
# Exercise 3: Templates (⭐⭐⭐⭐)
# =============================================================================

def exercise_3_templates():
    """
    Use Jinja2 templates.
    
    TODO:
    1. Render HTML template
    2. Pass variables to template
    3. Use template inheritance
    """
    from flask import Flask, render_template_string
    
    app = Flask(__name__)
    
    # TODO: Add route with template
    # @app.route('/hello/<name>')
    # def hello(name):
    #     return render_template_string('<h1>Hello, {{ name }}!</h1>', name=name)
    
    return app


# =============================================================================
# Exercise 4: Forms and Validation (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_forms():
    """
    Handle forms and validation.
    
    TODO:
    1. Accept form data
    2. Validate input
    3. Return appropriate response
    """
    from flask import Flask, request, jsonify
    
    app = Flask(__name__)
    
    # TODO: Add form handling route
    # @app.route('/register', methods=['POST'])
    # def register():
    #     data = request.get_json()
    #     if not data.get('email'):
    #         return jsonify({'error': 'Email required'}), 400
    #     return jsonify({'message': 'Registered'}), 201
    
    return app


# =============================================================================
# Exercise 5: Error Handling (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_error_handling():
    """
    Implement error handling.
    
    TODO:
    1. Custom error handlers
    2. Exception handling
    3. Proper HTTP status codes
    """
    from flask import Flask, jsonify
    
    app = Flask(__name__)
    
    # TODO: Add error handlers
    # @app.errorhandler(404)
    # def not_found(error):
    #     return jsonify({'error': 'Not found'}), 404
    
    return app


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 19 - Flask Basics Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic App")
    try:
        app = exercise_1_basic_app()
        assert app is not None
        print(f"  App created: {app}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Routing")
    try:
        app = exercise_2_routing()
        assert app is not None
        print(f"  App created: {app}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Templates")
    try:
        app = exercise_3_templates()
        assert app is not None
        print(f"  App created: {app}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Forms and Validation")
    try:
        app = exercise_4_forms()
        assert app is not None
        print(f"  App created: {app}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Error Handling")
    try:
        app = exercise_5_error_handling()
        assert app is not None
        print(f"  App created: {app}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
