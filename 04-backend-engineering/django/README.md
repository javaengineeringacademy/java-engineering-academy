# Django Python Web Framework

## Overview
Django is a high-level Python web framework that encourages rapid development and clean, pragmatic design. It follows the "batteries-included" philosophy, providing everything needed to build web applications out of the box.

## Why It Matters
Django powers some of the world's largest websites including Instagram, Pinterest, and Disqus. Its emphasis on security, scalability, and developer productivity makes it an excellent choice for both startups and enterprise applications.

## Key Concepts
- **MVT Pattern**: Model-View-Template architecture separates data, logic, and presentation
- **ORM**: Object-Relational Mapper for database operations without raw SQL
- **Admin Interface**: Automatic admin panel generated from models
- **Middleware**: Components that process requests/responses globally
- **Forms**: Built-in form handling with validation and CSRF protection

## Core Topics
- **Models and Migrations**: Define database schema using Python classes, manage schema changes
- **Views**: Function-based and class-based views for handling HTTP requests
- **Templates**: Template inheritance, filters, tags, and context processors
- **REST Framework**: Django REST framework for building APIs (serializers, viewsets, routers)
- **Signals**: Decoupled application communication (post_save, pre_delete)
- **Authentication**: Built-in user model, permissions, and session management

## Best Practices
- Use Django REST framework for API development
- Implement custom user model from the start
- Use class-based views for reusable patterns
- Leverage Django's security features (CSRF, XSS, SQL injection prevention)
- Write tests for models, views, and APIs
- Use environment variables for sensitive settings

## Hands-on Labs
1. Build a blog application with user authentication and CRUD operations
2. Create a REST API with Django REST framework for a bookstore
3. Implement custom middleware for request logging
4. Build a real-time chat application with Django Channels
5. Create a custom management command for data migration

## Interview Questions
1. What is the MVT pattern and how does it differ from MVC?
2. How does Django's ORM handle database migrations?
3. Explain the difference between function-based and class-based views.
4. What are Django signals and when would you use them?
5. How does Django prevent common security vulnerabilities?
6. Describe Django's caching strategies and when to use each.
7. How do you handle authentication and authorization in Django?
8. What is the purpose of Django middleware and how do you create custom middleware?

## References
- [Django Official Documentation](https://docs.djangoproject.com/)
- [Django REST Framework](https://www.django-rest-framework.org/)
- [Two Scoops of Django](https://www.feldroy.com/books/two-scoops-of-django)
- [Django for Professionals](https://wsvincent.com/django-book/)
