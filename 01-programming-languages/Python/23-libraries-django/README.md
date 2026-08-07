# Django — The Web Framework for Perfectionists

> **Batteries included. Convention over configuration. Production-ready from day one.**

## What

Django is a high-level, full-stack web framework that provides everything you need to build robust web applications: ORM, authentication, admin panel, form handling, URL routing, templating, security middleware, and a management CLI. It follows the "Don't Repeat Yourself" (DRY) principle and enforces a structured project layout.

## Why

- **Batteries included:** ORM, admin, auth, forms, middleware — all built-in.
- **Security:** CSRF protection, SQL injection prevention, XSS filtering, clickjacking protection.
- **Scalability:** Powers Instagram, Pinterest, Mozilla, and thousands of high-traffic sites.
- **Admin panel:** Auto-generated admin interface from your models.
- **Ecosystem:** Django REST Framework, Celery, django-allauth, and thousands of packages.

## When

| Scenario | Django Approach | Why |
|----------|----------------|-----|
| Content-heavy site | Models + Templates + Admin | Fast content management |
| REST API | Django REST Framework | Mature, well-documented API toolkit |
| E-commerce | Django + django-oscar | Battle-tested e-commerce framework |
| SaaS application | Django + Celery + Redis | Task queue, caching, multi-tenancy |
| Admin dashboard | Django Admin | Auto-generated from models, no code |
| Social platform | Django + django-allauth | Built-in authentication providers |

## How

### Project Structure

```
myproject/
├── manage.py
├── myproject/
│   ├── __init__.py
│   ├── settings.py
│   ├── urls.py
│   ├── wsgi.py
│   └── asgi.py
├── apps/
│   ├── blog/
│   │   ├── __init__.py
│   │   ├── models.py
│   │   ├── views.py
│   │   ├── urls.py
│   │   ├── admin.py
│   │   ├── forms.py
│   │   └── templates/
│   └── accounts/
└── requirements.txt
```

### Models

```python
from django.db import models
from django.contrib.auth.models import AbstractUser

class User(AbstractUser):
    bio = models.TextField(blank=True)
    avatar = models.ImageField(upload_to='avatars/', blank=True)

class Post(models.Model):
    title = models.CharField(max_length=200)
    slug = models.SlugField(unique=True)
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name='posts')
    content = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    published = models.BooleanField(default=False)

    class Meta:
        ordering = ['-created_at']

    def __str__(self):
        return self.title

    def get_absolute_url(self):
        return f'/blog/{self.slug}/'
```

### Views

```python
from django.shortcuts import render, get_object_or_404, redirect
from django.http import JsonResponse
from django.views.generic import ListView, DetailView, CreateView
from .models import Post
from .forms import PostForm

# Function-based view
def post_list(request):
    posts = Post.objects.filter(published=True)
    return render(request, 'blog/post_list.html', {'posts': posts})

# Class-based view
class PostDetailView(DetailView):
    model = Post
    template_name = 'blog/post_detail.html'

# API view (function-based)
def post_api(request, slug):
    post = get_object_or_404(Post, slug=slug, published=True)
    return JsonResponse({
        'title': post.title,
        'content': post.content,
        'author': post.author.username,
        'created_at': post.created_at.isoformat(),
    })
```

### URL Configuration

```python
# blog/urls.py
from django.urls import path
from . import views

app_name = 'blog'

urlpatterns = [
    path('', views.post_list, name='post_list'),
    path('<slug:slug>/', views.PostDetailView.as_view(), name='post_detail'),
    path('create/', views.PostCreateView.as_view(), name='post_create'),
]

# myproject/urls.py
from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('blog/', include('blog.urls')),
    path('accounts/', include('django.contrib.auth.urls')),
]
```

### Forms

```python
from django import forms
from .models import Post

class PostForm(forms.ModelForm):
    class Meta:
        model = Post
        fields = ['title', 'slug', 'content', 'published']
        widgets = {
            'title': forms.TextInput(attrs={'class': 'form-control'}),
            'content': forms.Textarea(attrs={'rows': 10}),
        }

    def clean_title(self):
        title = self.cleaned_data['title']
        if len(title) < 5:
            raise forms.ValidationError("Title must be at least 5 characters.")
        return title
```

### Templates

```html
<!-- base.html -->
<!DOCTYPE html>
<html>
<head><title>{% block title %}My Blog{% endblock %}</title></head>
<body>
  <nav>{% block nav %}{% endblock %}</nav>
  <main>{% block content %}{% endblock %}</main>
</body>
</html>

<!-- post_list.html -->
{% extends "base.html" %}
{% block content %}
  {% for post in posts %}
    <article>
      <h2><a href="{% url 'blog:post_detail' post.slug %}">{{ post.title }}</a></h2>
      <p>{{ post.content|truncatewords:50 }}</p>
      <time>{{ post.created_at|date:"F j, Y" }}</time>
    </article>
  {% endfor %}
{% endblock %}
```

### Admin Configuration

```python
from django.contrib import admin
from .models import Post, User

@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ['title', 'author', 'created_at', 'published']
    list_filter = ['published', 'created_at']
    search_fields = ['title', 'content']
    prepopulated_fields = {'slug': ('title',)}
    date_hierarchy = 'created_at'

@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ['username', 'email', 'first_name', 'last_name']
```

### Settings (Production Highlights)

```python
# settings.py (key production settings)
SECRET_KEY = os.environ.get('DJANGO_SECRET_KEY')
DEBUG = os.environ.get('DJANGO_DEBUG', 'False') == 'True'
ALLOWED_HOSTS = os.environ.get('DJANGO_ALLOWED_HOSTS', '').split(',')

# Database
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql',
        'NAME': os.environ.get('DB_NAME'),
        'USER': os.environ.get('DB_USER'),
        'PASSWORD': os.environ.get('DB_PASSWORD'),
        'HOST': os.environ.get('DB_HOST', 'localhost'),
    }
}

# Security
SECURE_SSL_REDIRECT = True
SESSION_COOKIE_SECURE = True
CSRF_COOKIE_SECURE = True
X_FRAME_OPTIONS = 'DENY'
```

## Production Checklist

- [ ] **Set `DEBUG = False`** — never run debug mode in production
- [ ] **Use `SECRET_KEY` from environment** — never commit secrets
- [ ] **Configure `ALLOWED_HOSTS`** — prevent host header attacks
- [ ] **Enable `SECURE_SSL_REDIRECT`** — force HTTPS
- [ ] **Use PostgreSQL** — SQLite is for development only
- [ ] **Configure `STATIC_ROOT`** — serve static files via Nginx/CDN
- [ ] **Set up Celery** — background tasks with Redis/RabbitMQ
- [ ] **Run `manage.py check --deploy`** — Django's security checklist

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Tutorial** | Follows official tutorial, single app, SQLite |
| 2 | **Basic** | Multiple apps, custom templates, basic forms |
| 3 | **Intermediate** | REST API, custom middleware, signals, caching |
| 4 | **Production** | Docker, CI/CD, monitoring, PostgreSQL, Redis |
| 5 | **Expert** | Custom auth backends, multi-tenancy, performance tuning, deployment orchestration |

## Common Myths

### Myth 1: "Django is too heavy for APIs"
**Reality:** Django REST Framework is the most mature Python API toolkit. For complex APIs with authentication, pagination, and filtering, DRF is faster to build with than Flask. For simple microservices, Flask may be lighter.

### Myth 2: "Django ORM is slow compared to raw SQL"
**Reality:** The ORM is optimized for most use cases. Raw SQL is only needed for complex queries the ORM can't express. Use `select_related()` and `prefetch_related()` to solve N+1 problems before switching to raw SQL.

### Myth 3: "Django can't do async"
**Reality:** Django 3.1+ supports async views. While Python's GIL limits true parallelism, async views help with I/O-bound operations. For heavy async needs, use Django Channels (WebSockets) or offload to Celery.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Model | `class MyModel(models.Model)` | Database table definition |
| Query | `Model.objects.filter(...)` | Database queries (ORM) |
| View | `def view(request):` | Handle HTTP request |
| URL | `path('url/', view)` | Map URL to view |
| Template | `{% extends "base.html" %}` | Template inheritance |
| Form | `class MyForm(forms.ModelForm)` | Data validation |
| Admin | `@admin.register(Model)` | Auto-generated admin |
| Migration | `python manage.py makemigrations` | Schema changes |
| Management | `python manage.py shell` | Interactive Python shell |
| Static | `{% load static %}` | Serve static files |

## Related Topics

- [22-libraries-flask](../22-libraries-flask/) - Lightweight alternative
- [24-libraries-sqlalchemy](../24-libraries-sqlalchemy/) - Alternative ORM
- [25-libraries-pytest](../25-libraries-pytest/) - Testing Django apps
- [21-libraries-requests](../21-libraries-requests/) - API client integration

---

> **Remember:** Django's power is its ecosystem. Don't fight the framework — embrace its conventions, and it will save you months of work.
