"""
Django Basic Application
Demonstrates models, views, URLs, and forms for a simple blog app.

NOTE: This is a demonstration of Django concepts. To run this,
you need to set up a Django project with:
    django-admin startproject myproject
    cd myproject
    python manage.py startapp blog

Then copy the relevant code into your project files.
"""

# ============================================
# blog/models.py
# ============================================

MODELS_CODE = '''
from django.db import models
from django.contrib.auth.models import User


class Post(models.Model):
    title = models.CharField(max_length=200)
    slug = models.SlugField(unique=True)
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name="posts")
    content = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    published = models.BooleanField(default=False)

    class Meta:
        ordering = ["-created_at"]

    def __str__(self):
        return self.title
'''

# ============================================
# blog/views.py
# ============================================

VIEWS_CODE = '''
from django.shortcuts import render, get_object_or_404
from django.http import JsonResponse
from .models import Post


def post_list(request):
    """List all published posts."""
    posts = Post.objects.filter(published=True)
    context = {"posts": posts}
    return render(request, "blog/post_list.html", context)


def post_detail(request, slug):
    """Show a single post."""
    post = get_object_or_404(Post, slug=slug, published=True)
    context = {"post": post}
    return render(request, "blog/post_detail.html", context)


def post_api(request, slug):
    """API endpoint returning post as JSON."""
    post = get_object_or_404(Post, slug=slug, published=True)
    return JsonResponse({
        "title": post.title,
        "content": post.content,
        "author": post.author.username,
        "created_at": post.created_at.isoformat(),
    })
'''

# ============================================
# blog/urls.py
# ============================================

URLS_CODE = '''
from django.urls import path
from . import views

app_name = "blog"

urlpatterns = [
    path("", views.post_list, name="post_list"),
    path("<slug:slug>/", views.post_detail, name="post_detail"),
    path("api/<slug:slug>/", views.post_api, name="post_api"),
]
'''

# ============================================
# blog/forms.py
# ============================================

FORMS_CODE = '''
from django import forms
from .models import Post


class PostForm(forms.ModelForm):
    class Meta:
        model = Post
        fields = ["title", "slug", "content", "published"]
        widgets = {
            "title": forms.TextInput(attrs={"class": "form-control"}),
            "slug": forms.TextInput(attrs={"class": "form-control"}),
            "content": forms.Textarea(attrs={"class": "form-control", "rows": 10}),
        }

    def clean_title(self):
        title = self.cleaned_data["title"]
        if len(title) < 5:
            raise forms.ValidationError("Title must be at least 5 characters.")
        return title
'''

# ============================================
# blog/admin.py
# ============================================

ADMIN_CODE = '''
from django.contrib import admin
from .models import Post


@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ["title", "author", "created_at", "published"]
    list_filter = ["published", "created_at"]
    search_fields = ["title", "content"]
    prepopulated_fields = {"slug": ("title",)}
    date_hierarchy = "created_at"
'''

# ============================================
# blog/templates/blog/base.html
# ============================================

BASE_TEMPLATE = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>{% block title %}My Blog{% endblock %}</title>
    <style>
        body { font-family: sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }
        nav { margin-bottom: 20px; }
        article { border-bottom: 1px solid #eee; padding: 10px 0; }
    </style>
</head>
<body>
    <nav>
        <a href="{% url 'blog:post_list' %}">Home</a> |
        <a href="/admin/">Admin</a>
    </nav>
    <main>{% block content %}{% endblock %}</main>
</body>
</html>
'''

POST_LIST_TEMPLATE = '''
{% extends "blog/base.html" %}
{% block title %}All Posts{% endblock %}
{% block content %}
    <h1>Blog Posts</h1>
    {% for post in posts %}
    <article>
        <h2><a href="{% url 'blog:post_detail' post.slug %}">{{ post.title }}</a></h2>
        <p>{{ post.content|truncatewords:50 }}</p>
        <time>{{ post.created_at|date:"F j, Y" }}</time>
        <small>by {{ post.author.username }}</small>
    </article>
    {% empty %}
    <p>No posts yet.</p>
    {% endfor %}
{% endblock %}
'''

# ============================================
# main() - Print all code files
# ============================================

if __name__ == "__main__":
    print("=" * 60)
    print("DJANGO BLOG APP - Code Structure")
    print("=" * 60)

    sections = [
        ("blog/models.py", MODELS_CODE),
        ("blog/views.py", VIEWS_CODE),
        ("blog/urls.py", URLS_CODE),
        ("blog/forms.py", FORMS_CODE),
        ("blog/admin.py", ADMIN_CODE),
        ("blog/templates/blog/base.html", BASE_TEMPLATE),
        ("blog/templates/blog/post_list.html", POST_LIST_TEMPLATE),
    ]

    for filename, code in sections:
        print(f"\n{'=' * 60}")
        print(f"FILE: {filename}")
        print("=" * 60)
        print(code.strip())

    print("\n" + "=" * 60)
    print("To run this Django app:")
    print("  1. django-admin startproject myproject")
    print("  2. cd myproject")
    print("  3. python manage.py startapp blog")
    print("  4. Copy the code above into the respective files")
    print("  5. Add 'blog' to INSTALLED_APPS in settings.py")
    print("  6. Add path('blog/', include('blog.urls')) to myproject/urls.py")
    print("  7. python manage.py migrate")
    print("  8. python manage.py createsuperuser")
    print("  9. python manage.py runserver")
    print("=" * 60)
