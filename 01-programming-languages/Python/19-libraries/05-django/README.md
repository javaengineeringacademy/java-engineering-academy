# Django

## Why Django Exists

Every Python developer building a full-stack web application needs an ORM, authentication, admin panel, form handling, and security middleware. Building each from scratch is time-consuming and error-prone. Django was created to provide all these components out of the box, following the "Don't Repeat Yourself" principle. It's the batteries-included framework for developers who want to focus on business logic, not infrastructure.

## What You'll Learn

By the end of this section, you'll be able to:

- Define database models and generate migrations with Django ORM
- Build views, forms, and templates using Django's component architecture
- Configure production settings including security, static files, and background tasks

## When to Use Django

| Use Case | Why Django | Alternative |
|----------|-----------|-------------|
| Content-heavy site | Models + Templates + Admin | Flask + extensions |
| REST API | Django REST Framework | FastAPI |
| E-commerce | Django + django-oscar | Custom solution |
| SaaS application | Django + Celery + Redis | Microservices |
| Admin dashboard | Django Admin (auto-generated) | Custom admin |
| Social platform | Django + django-allauth | Custom auth |

## How Django Works Internally

Django follows the Model-View-Template (MVT) architecture. Models define database tables as Python classes. Views handle HTTP requests and return responses. Templates render HTML with dynamic data. The URL dispatcher routes requests to views based on URL patterns.

Django's ORM translates Python class definitions into SQL schema. When you run `makemigrations`, it compares your current models against the existing schema and generates migration files. These migrations are executed in order to evolve the database without data loss. The ORM uses a "lazy evaluation" pattern — queries are only executed when you evaluate the QuerySet (e.g., by iterating, slicing, or calling `list()`).

```python
from django.db import models

class Post(models.Model):
    title = models.CharField(max_length=200)
    author = models.ForeignKey('User', on_delete=models.CASCADE)
    content = models.TextField()
    published = models.BooleanField(default=False)

    class Meta:
        ordering = ['-created_at']

# QuerySet is lazy — no SQL until evaluation
posts = Post.objects.filter(published=True)  # No query yet
for post in posts:  # SQL executed here
    print(post.title)
```

## Production Checklist

### ✅ Before using Django in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Django is too heavy for APIs
**Reality:** Django REST Framework is the most mature Python API toolkit. For complex APIs with authentication, pagination, and filtering, DRF is faster to build with than Flask.

### ❌ Myth 2: Django ORM is slow compared to raw SQL
**Reality:** The ORM is optimized for most use cases. Raw SQL is only needed for complex queries the ORM can't express. Use `select_related()` and `prefetch_related()` to solve N+1 problems.

### ❌ Myth 3: Django can't do async
**Reality:** Django 3.1+ supports async views. While Python's GIL limits true parallelism, async views help with I/O-bound operations. For heavy async needs, use Django Channels.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Full-stack web framework |
| Complexity | O(n) for ORM queries |
| Thread Safe | Yes (with proper WSGI server) |
| Best Alternative | Flask for microservices |
| When to Use | Full-stack apps, content sites |
| When to Avoid | Simple APIs, microservices |

## Related Topics

- [04-flask](../04-flask/) - Lightweight alternative
- [06-sqlalchemy](../06-sqlalchemy/) - Alternative ORM
- [10-redis](../10-redis/) - Caching and task queue backend

## References
- Django Documentation: https://docs.djangoproject.com/
- Django Source: https://github.com/django/django
- Django REST Framework

## Version Validation
- Verified against: Django 5.0+, Python 3.10+
