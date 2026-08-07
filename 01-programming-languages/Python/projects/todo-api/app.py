"""Flask application setup and configuration."""

from flask import Flask
from routes import todo_bp


def create_app(testing: bool = False) -> Flask:
    """Create and configure the Flask application."""
    app = Flask(__name__)
    app.config["TESTING"] = testing
    
    # Register blueprints
    app.register_blueprint(todo_bp, url_prefix="/api")
    
    # Error handlers
    @app.errorhandler(404)
    def not_found(error):
        return {"error": "Resource not found"}, 404
    
    @app.errorhandler(400)
    def bad_request(error):
        return {"error": "Bad request"}, 400
    
    @app.errorhandler(500)
    def internal_error(error):
        return {"error": "Internal server error"}, 500
    
    return app


if __name__ == "__main__":
    app = create_app()
    print("Starting Todo API server on http://localhost:5000")
    app.run(debug=True, port=5000)
