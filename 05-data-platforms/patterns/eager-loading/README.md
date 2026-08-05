# Eager Loading Pattern

## Overview

Eager Loading fetches all related data in a single query or a minimal set of queries upfront, rather than loading relationships on demand. This approach uses JOIN queries or batch fetching to retrieve the complete object graph in one round-trip.

Eager loading trades initial query performance and memory for predictable query behavior and avoidance of N+1 problems. It is the opposite strategy to lazy loading.

## When to Use

- Related data is always needed when the parent is loaded
- N+1 query prevention is a priority
- The total size of the result set is manageable
- Network round-trips are expensive
- Building API responses that require complete object graphs

## Implementation

### TypeScript

```typescript
class UserRepository {
  async findByIdWithPosts(id: string): Promise<UserWithPosts> {
    const result = await this.db.query(`
      SELECT u.*, p.id as post_id, p.title, p.body
      FROM users u
      LEFT JOIN posts p ON p.user_id = u.id
      WHERE u.id = $1
    `, [id]);

    return this.mapToUserWithPosts(result);
  }

  async findAllWithProfiles(): Promise<User[]> {
    return this.db.query(`
      SELECT u.*, up.bio, up.avatar_url
      FROM users u
      INNER JOIN user_profiles up ON up.user_id = u.id
    `);
  }
}
```

### Java (JPA)

```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<Post> posts;
}

// JPQL with JOIN FETCH
@Repository
public class UserRepository {
    @Query("SELECT u FROM User u JOIN FETCH u.posts WHERE u.id = :id")
    User findByIdWithPosts(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.posts JOIN FETCH u.profile")
    List<User> findAllWithPostsAndProfile();
}
```

### Python (SQLAlchemy)

```python
from sqlalchemy.orm import joinedload, subqueryload

class UserRepository:
    def find_by_id_with_posts(self, user_id: int):
        return self.session.query(User).options(
            joinedload(User.posts)  # eager join
        ).filter(User.id == user_id).first()

    def find_all_with_profiles(self):
        return self.session.query(User).options(
            subqueryload(User.profile)  # separate query
        ).all()
```

### C\#

```csharp
public class UserRepository {
    private readonly DbContext _context;

    public async Task<User?> GetByIdWithPostsAsync(int id) {
        return await _context.Users
            .Include(u => u.Posts)
            .FirstOrDefaultAsync(u => u.Id == id);
    }

    public async Task<List<User>> GetAllWithProfilesAsync() {
        return await _context.Users
            .Include(u => u.Profile)
            .ToListAsync();
    }
}
```

## Best Practices

- Use JOINs for one-to-one and many-to-one relationships
- Use subquery loading for one-to-many collections to avoid row duplication
- Limit the depth of eager loading to prevent massive result sets
- Profile query performance before and after adding eager loading
- Consider split queries in EF Core for complex eager loads
- Combine with lazy loading selectively for rarely-needed relationships

## Interview Questions

1. How does eager loading differ from lazy loading in terms of queries?
2. What are the performance tradeoffs of eager loading?
3. How do you prevent Cartesian explosion with multiple eager-loaded collections?
4. When should you use subquery loading versus join loading?
5. How do database indexes affect eager loading performance?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 13
- Hibernate Documentation. *Fetching Strategies*
- SQLAlchemy Documentation. *Eager Loading*
- Entity Framework Core. *Related Data - Eager Loading*
