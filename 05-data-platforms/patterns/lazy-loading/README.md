# Lazy Loading Pattern

## Overview

Lazy Loading defers the initialization of related objects or data until the moment it is actually accessed. Instead of fetching all associated data upfront, related objects are loaded on demand, typically through a proxy or placeholder that intercepts access requests.

This pattern reduces initial load time and memory consumption by only retrieving data when needed, but can lead to N+1 query problems if not carefully managed.

## When to Use

- Related data is not always needed by the calling code
- Objects have large or deeply nested relationship graphs
- Memory efficiency is critical for large result sets
- Initial query performance is more important than total query count
- Optional or rarely-used relationships exist

## Implementation

### TypeScript

```typescript
class LazyUser {
  private _posts: Post[] | undefined;

  constructor(
    private id: string,
    private name: string,
    private db: DatabaseClient
  ) {}

  get posts(): Promise<Post[]> {
    if (!this._posts) {
      this._posts = this.db.query('SELECT * FROM posts WHERE user_id = $1', [this.id])
        .then(rows => rows.map(r => new Post(r)));
    }
    return this._posts;
  }
}

class UserRepository {
  async findById(id: string): Promise<LazyUser> {
    const row = await this.db.query('SELECT * FROM users WHERE id = $1', [id]);
    return new LazyUser(row.id, row.name, this.db);
  }
}
```

### Java

```java
@Entity
public class User {
    @Id
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> posts;

    public List<Post> getPosts() {
        if (posts == null) {
            posts = entityManager.createQuery(
                "SELECT p FROM Post p WHERE p.user.id = :userId",
                Post.class)
                .setParameter("userId", id)
                .getResultList();
        }
        return posts;
    }
}

// Usage
User user = userRepository.findById(1L);
// posts not loaded yet
List<Post> posts = user.getPosts(); // SQL query executed here
```

### Python (SQLAlchemy)

```python
from sqlalchemy.orm import relationship

class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    posts = relationship('Post', lazy='select')  # lazy by default

class Post(Base):
    __tablename__ = 'posts'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    title = Column(String)

# Usage
user = session.query(User).get(1)
# posts not loaded yet
print(user.posts)  # SQL query executed here
```

### C\#

```csharp
public class User {
    public int Id { get; set; }
    public string Name { get; set; }

    private ICollection<Post>? _posts;
    public virtual ICollection<Post> Posts {
        get {
            if (_posts == null) {
                _posts = context.Posts
                    .Where(p => p.UserId == Id)
                    .ToList();
            }
            return _posts;
        }
    }
}

// Entity Framework - default is lazy loading with proxies
public class User {
    public int Id { get; set; }
    public string Name { get; set; }
    public virtual ICollection<Post> Posts { get; set; }  // lazy loaded
}
```

## Best Practices

- Use eager loading when you know related data will always be needed
- Monitor query counts to detect N+1 problems early
- Set batch size for collection loading to reduce round-trips
- Be explicit about loading strategy in performance-critical code paths
- Use data profiling tools to identify unexpected lazy loads
- Consider DataLoader pattern in GraphQL to batch lazy loads

## Interview Questions

1. What is the N+1 query problem and how does lazy loading cause it?
2. How do you decide between lazy loading and eager loading?
3. How does lazy loading work with unit of work patterns?
4. What are the memory implications of lazy loading vs eager loading?
5. How do ORMs handle lazy loading across different contexts?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 13
- Hibernate Documentation. *Fetching Strategies*
- SQLAlchemy Documentation. *Relationship Loading Options*
- Martin Fowler. *N plus one queries*
