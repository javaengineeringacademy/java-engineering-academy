# API Pagination

## Comprehensive Guide to Pagination Strategies

Pagination is essential for handling large datasets in APIs. This guide covers offset, cursor, and keyset pagination patterns with implementation examples.

---

## Table of Contents

1. [Pagination Overview](#pagination-overview)
2. [Offset Pagination](#offset-pagination)
3. [Cursor Pagination](#cursor-pagination)
4. [Keyset Pagination](#keyset-pagination)
5. [Implementation Examples](#implementation-examples)
6. [Best Practices](#best-practices)

---

## Pagination Overview

### Why Pagination?

```
Without Pagination:
GET /users
Response: { "users": [...1000000 users...] }  // Slow, memory-intensive

With Pagination:
GET /users?page=1&size=20
Response: { "users": [...20 users...] }  // Fast, efficient
```

### Comparison

| Strategy | Pros | Cons |
|----------|------|------|
| Offset | Simple, familiar | Inconsistent results, slow for large offsets |
| Cursor | Consistent results | Complex implementation |
| Keyset | Fast, consistent | Requires ordered unique column |

---

## Offset Pagination

### Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<Page<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> users = userService.findAll(pageable);

        return ResponseEntity.ok(users);
    }
}
```

### Response Format

```json
{
    "content": [
        { "id": 1, "name": "User 1" },
        { "id": 2, "name": "User 2" }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1000,
    "totalPages": 50,
    "first": true,
    "last": false,
    "empty": false
}
```

### SQL Query

```sql
-- Offset pagination
SELECT * FROM users
ORDER BY id
LIMIT 20 OFFSET 0;

-- Page 2
SELECT * FROM users
ORDER BY id
LIMIT 20 OFFSET 20;

-- Problem: Inconsistent with concurrent inserts
-- Page 1: [1, 2, 3, ..., 20]
-- Insert user 0
-- Page 2: [2, 3, 4, ..., 21]  -- Missing user 1!
```

### Controller with Link Headers

```java
@GetMapping
public ResponseEntity<List<User>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        UriComponentsBuilder uriBuilder) {

    Page<User> users = userService.findAll(PageRequest.of(page, size));

    Link[] links = generatePaginationLinks(users, uriBuilder);

    return ResponseEntity.ok()
        .header("X-Total-Count", String.valueOf(users.getTotalElements()))
        .header("Link", Arrays.toString(links))
        .body(users.getContent());
}

private Link[] generatePaginationLinks(Page<User> users,
                                        UriComponentsBuilder uriBuilder) {
    List<Link> links = new ArrayList<>();

    if (users.hasPrevious()) {
        links.add(Link.of(uriBuilder
            .queryParam("page", users.getNumber() - 1)
            .queryParam("size", users.getSize())
            .toUriString(), "prev"));
    }

    if (users.hasNext()) {
        links.add(Link.of(uriBuilder
            .queryParam("page", users.getNumber() + 1)
            .queryParam("size", users.getSize())
            .toUriString(), "next"));
    }

    return links.toArray(new Link[0]);
}
```

---

## Cursor Pagination

### Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<CursorPage<User>> listUsers(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int size) {

        CursorPage<User> users = userService.findAll(cursor, size);

        return ResponseEntity.ok(users);
    }
}

@Service
public class UserService {

    public CursorPage<User> findAll(String cursor, int size) {
        List<User> users;

        if (cursor == null) {
            users = userRepository.findAllByOrderByIdAsc(
                PageRequest.of(0, size + 1));
        } else {
            users = userRepository.findByIdGreaterThan(
                decodeCursor(cursor),
                PageRequest.of(0, size + 1));
        }

        boolean hasNext = users.size() > size;
        if (hasNext) {
            users = users.subList(0, size);
        }

        String nextCursor = hasNext ?
            encodeCursor(users.get(users.size() - 1).getId()) : null;

        return new CursorPage<>(users, nextCursor, hasNext);
    }
}
```

### Response Format

```json
{
    "data": [
        { "id": 1, "name": "User 1" },
        { "id": 2, "name": "User 2" }
    ],
    "cursor": {
        "next": "eyJpZCI6MjB9"
    },
    "hasMore": true
}
```

### SQL Query

```sql
-- Cursor pagination
SELECT * FROM users
WHERE id > :lastId
ORDER BY id
LIMIT 21;

-- No inconsistency with concurrent inserts!
```

---

## Keyset Pagination

### Implementation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<KeysetPage<User>> listUsers(
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false) Instant lastCreatedAt,
            @RequestParam(defaultValue = "20") int size) {

        KeysetPage<User> users = userService.findAll(
            lastId, lastCreatedAt, size);

        return ResponseEntity.ok(users);
    }
}

@Service
public class UserService {

    public KeysetPage<User> findAll(Long lastId, Instant lastCreatedAt,
                                     int size) {
        List<User> users;

        if (lastId == null || lastCreatedAt == null) {
            users = userRepository.findAllByOrderByCreatedAtDescIdDesc(
                PageRequest.of(0, size + 1));
        } else {
            users = userRepository
                .findByCreatedAtLessThanOr(
                    lastCreatedAt, lastId,
                    PageRequest.of(0, size + 1));
        }

        boolean hasNext = users.size() > size;
        if (hasNext) {
            users = users.subList(0, size);
        }

        return new KeysetPage<>(users, hasNext);
    }
}
```

### SQL Query

```sql
-- Keyset pagination (using composite cursor)
SELECT * FROM users
WHERE (created_at, id) < (:lastCreatedAt, :lastId)
ORDER BY created_at DESC, id DESC
LIMIT 21;
```

### Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC, u.id DESC")
    List<User> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);

    @Query("SELECT u FROM User u " +
           "WHERE (u.createdAt < :lastCreatedAt) OR " +
           "(u.createdAt = :lastCreatedAt AND u.id < :lastId) " +
           "ORDER BY u.createdAt DESC, u.id DESC")
    List<User> findByCreatedAtLessThanOr(
        @Param("lastCreatedAt") Instant lastCreatedAt,
        @Param("lastId") Long lastId,
        Pageable pageable);
}
```

---

## Implementation Examples

### Generic Cursor Page

```java
public class CursorPage<T> {
    private List<T> data;
    private String nextCursor;
    private boolean hasMore;

    public CursorPage(List<T> data, String nextCursor, boolean hasMore) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }

    public static <T> CursorPage<T> of(List<T> data, boolean hasMore) {
        String cursor = hasMore ?
            Base64.getEncoder().encodeToString(
                String.valueOf(((Identifiable) data.get(data.size() - 1)).getId())
                    .getBytes()) : null;
        return new CursorPage<>(data, cursor, hasMore);
    }
}

public interface Identifiable {
    Object getId();
}
```

### Cursor Encoding

```java
@Component
public class CursorEncoder {

    public String encode(Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            return Base64.getUrlEncoder().encodeToString(json.getBytes());
        } catch (JsonProcessingException e) {
            throw new CursorEncodingException("Failed to encode cursor", e);
        }
    }

    public <T> T decode(String cursor, Class<T> type) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(cursor);
            return objectMapper.readValue(bytes, type);
        } catch (Exception e) {
            throw new CursorEncodingException("Failed to decode cursor", e);
        }
    }
}
```

### HATEOAS Links

```java
@GetMapping
public ResponseEntity<CursorPage<User>> listUsers(
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "20") int size) {

    CursorPage<User> users = userService.findAll(cursor, size);

    // Add HATEOAS links
    Map<String, String> links = new HashMap<>();
    links.put("self", buildLink(cursor, size));

    if (users.isHasMore()) {
        links.put("next", buildLink(users.getNextCursor(), size));
    }

    return ResponseEntity.ok()
        .header("X-Total-Has-More", String.valueOf(users.isHasMore()))
        .body(users);
}
```

---

## Best Practices

### 1. Use Consistent Response Format

```json
{
    "data": [],
    "pagination": {
        "hasMore": true,
        "nextCursor": "eyJpZCI6MjB9",
        "total": 1000
    },
    "links": {
        "self": "/api/users?size=20",
        "next": "/api/users?cursor=eyJpZCI6MjB9&size=20"
    }
}
```

### 2. Set Maximum Page Size

```java
@GetMapping
public ResponseEntity<Page<User>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") @Max(100) int size) {
    return ResponseEntity.ok(userService.findAll(page, size));
}
```

### 3. Use Default Values

```java
@GetMapping
public ResponseEntity<Page<User>> listUsers(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "20") int size) {
    return ResponseEntity.ok(userService.findAll(page, size));
}
```

### 4. Include Total Count

```java
@GetMapping
public ResponseEntity<Map<String, Object>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {

    Page<User> users = userService.findAll(page, size);

    Map<String, Object> response = new HashMap<>();
    response.put("data", users.getContent());
    response.put("total", users.getTotalElements());

    return ResponseEntity.ok(response);
}
```

### 5. Document Pagination

```java
@Operation(summary = "List users",
    description = "Returns a paginated list of users. " +
    "Use cursor for large datasets to avoid offset issues.")
@GetMapping
public ResponseEntity<Page<User>> listUsers(
    @Parameter(description = "Page number (0-based)")
    @RequestParam(defaultValue = "0") int page,

    @Parameter(description = "Page size (max 100)")
    @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(userService.findAll(page, size));
}
```

---

## Further Reading

- [Pagination Design Patterns](https://slack.engineering/evolving-api-pagination-backwards-compatible/)
- [Cursor Pagination](https://graphql.org/learn/pagination/)
- [Keyset Pagination](://use-the-index-luke.com/no-offset)
- [Facebook GraphQL Pagination](https://facebook.github.io/relay/docs/connections.htm)
