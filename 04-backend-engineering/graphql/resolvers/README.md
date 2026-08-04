# GraphQL Resolvers

## Comprehensive Guide to Resolver Patterns in GraphQL

Resolvers are functions that connect your schema fields to backend data sources. This guide covers resolver patterns, the N+1 problem, DataLoaders, and best practices.

---

## Table of Contents

1. [Resolver Basics](#resolver-basics)
2. [Resolver Signatures](#resolver-signatures)
3. [N+1 Problem](#n1-problem)
4. [DataLoaders](#dataloaders)
5. [Resolver Composition](#resolver-composition)
6. [Error Handling](#error-handling)
7. [Best Practices](#best-practices)

---

## Resolver Basics

### Resolver Structure

```graphql
# Schema
type Query {
  user(id: ID!): User
  users(limit: Int, offset: Int): [User!]!
}

type User {
  id: ID!
  name: String!
  email: String!
  posts: [Post!]!
  profile: Profile
}

type Post {
  id: ID!
  title: String!
  author: User!
  comments: [Comment!]!
}
```

### Java Resolver Implementation

```java
@Component
public class UserResolver {

    @QueryMapping
    public User user(@Argument Long id, UserFetcher userFetcher) {
        return userFetcher.fetchById(id);
    }

    @QueryMapping
    public List<User> users(
            @Argument Integer limit,
            @Argument Integer offset,
            UserFetcher userFetcher) {
        return userFetcher.fetchAll(limit, offset);
    }

    @SchemaMapping
    public List<Post> posts(User user, PostFetcher postFetcher) {
        return postFetcher.fetchByUserId(user.getId());
    }

    @SchemaMapping
    public Profile profile(User user, ProfileFetcher profileFetcher) {
        return profileFetcher.fetchByUserId(user.getId());
    }
}
```

### Python Resolver Implementation

```python
import strawberry
from typing import List, Optional

@strawberry.type
class User:
    id: strawberry.ID
    name: str
    email: str

@strawberry.type
class Query:
    @strawberry.field
    def user(self, id: strawberry.ID) -> Optional[User]:
        return user_service.get_by_id(id)

    @strawberry.field
    def users(self, limit: int = 10, offset: int = 0) -> List[User]:
        return user_service.get_all(limit, offset)

schema = strawberry.Schema(query=Query)
```

### JavaScript Resolver Implementation

```javascript
const resolvers = {
  Query: {
    user: (_, { id }, context) => {
      return context.dataSources.userAPI.getUserById(id);
    },
    users: (_, { limit, offset }, context) => {
      return context.dataSources.userAPI.getUsers(limit, offset);
    },
  },
  User: {
    posts: (parent, _, context) => {
      return context.dataSources.postAPI.getPostsByUserId(parent.id);
    },
    profile: (parent, _, context) => {
      return context.dataSources.profileAPI.getProfileByUserId(parent.id);
    },
  },
};
```

---

## Resolver Signatures

### Four Arguments

```java
@Component
public class PostResolver {

    @SchemaMapping
    public User author(
            Post post,           // parent object
            @ContextValue Map<String, Object> context, // shared context
            Environment env      // GraphQL environment
    ) {
        // parent: The Post object being resolved
        // context: Shared data (DataLoaders, user session, etc.)
        // env: Field info, arguments, selection set
        return context.get(DataLoaderRegistry.class)
            .getDataLoader("userLoader")
            .load(post.getAuthorId());
    }
}
```

### Context Object

```java
@Component
public class AuthContextBuilder {

    @Bean
    public GraphQLContext.Builder graphqlContextBuilder() {
        return (builder) -> builder.of(
            "currentUser", null,
            "requestId", UUID.randomUUID().toString(),
            "startTime", System.currentTimeMillis()
        );
    }
}

@Component
public class QueryResolver {

    @QueryMapping
    public User me(@ContextValue GraphQLContext context) {
        User currentUser = context.get("currentUser");
        if (currentUser == null) {
            throw new GraphQLException("Not authenticated");
        }
        return currentUser;
    }
}
```

---

## N+1 Problem

### The Problem

```graphql
# Query
query {
  users {
    name
    posts {
      title
      comments {
        text
      }
    }
  }
}
```

```
N+1 Queries Generated:
1. SELECT * FROM users
2. SELECT * FROM posts WHERE user_id = 1  -- For user 1
3. SELECT * FROM posts WHERE user_id = 2  -- For user 2
4. SELECT * FROM posts WHERE user_id = 3  -- For user 3
...
N+1. SELECT * FROM comments WHERE post_id = 1  -- For each post
```

### Naive Solution (Bypasses N+1 but is worse)

```java
@QueryMapping
public List<UserWithPosts> users() {
    List<User> users = userRepository.findAll();
    for (User user : users) {
        user.setPosts(postRepository.findByUserId(user.getId()));
        for (Post post : user.getPosts()) {
            post.setComments(commentRepository.findByPostId(post.getId()));
        }
    }
    return users;
}
```

### DataLoader Solution

```java
@Component
public class PostDataLoader {

    @Bean
    public BatchLoader<Long, List<Post>> postBatchLoader() {
        return userIds -> {
            Map<Long, List<Post>> postsByUserId = postRepository
                .findByUserIds(new ArrayList<>(userIds))
                .stream()
                .collect(Collectors.groupingBy(Post::getAuthorId));

            return Flux.fromIterable(userIds)
                .map(id -> postsByUserId.getOrDefault(id, Collections.emptyList()));
        };
    }
}

// In resolver
@SchemaMapping
public CompletableFuture<List<Post>> posts(
        User user,
        @ContextValue DataLoaderRegistry registry) {
    return registry.getDataLoader("postLoader").load(user.getId());
}
```

### GraphQL Java Batch Loading

```java
@Component
public class PostResolver {

    @SchemaMapping(batched = true)
    public List<List<Post>> posts(List<User> users,
                                   @ContextValue DataLoaderRegistry registry) {
        DataLoader<Long, List<Post>> loader = registry.getDataLoader("postLoader");

        List<CompletableFuture<List<Post>>> futures = users.stream()
            .map(user -> loader.load(user.getId()))
            .collect(Collectors.toList());

        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }
}
```

---

## DataLoaders

### Basic DataLoader Setup

```java
@Configuration
public class DataLoaderConfig {

    @Bean
    public DataLoaderRegistry dataLoaderRegistry(
            UserLoader userLoader,
            PostLoader postLoader,
            CommentLoader commentLoader) {

        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register("userLoader", userLoader.create());
        registry.register("postLoader", postLoader.create());
        registry.register("commentLoader", commentLoader.create());

        return registry;
    }
}

@Component
public class UserLoader {

    private final UserRepository userRepository;

    public DataLoader<Long, User> create() {
        return DataLoader.newDataLoader(userIds -> {
            Map<Long, User> users = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

            return CompletableFuture.completedFuture(
                userIds.stream()
                    .map(users::get)
                    .collect(Collectors.toList())
            );
        });
    }
}
```

### DataLoader with Caching

```java
@Component
public class CachedPostLoader {

    private final PostRepository postRepository;
    private final Cache<String, Post> cache;

    public CachedPostLoader(PostRepository postRepository,
                             CacheManager cacheManager) {
        this.postRepository = postRepository;
        this.cache = cacheManager.getCache("posts");
    }

    public DataLoader<Long, Post> create() {
        DataLoader<Long, Post> loader = DataLoader.newDataLoader(postIds -> {
            List<Long> uncachedIds = postIds.stream()
                .filter(id -> cache.get("post:" + id) == null)
                .collect(Collectors.toList());

            if (!uncachedIds.isEmpty()) {
                Map<Long, Post> posts = postRepository.findAllById(uncachedIds)
                    .stream()
                    .collect(Collectors.toMap(Post::getId, Function.identity()));

                posts.forEach((id, post) ->
                    cache.put("post:" + id, post));
            }

            List<Post> results = postIds.stream()
                .map(id -> cache.get("post:" + id))
                .collect(Collectors.toList());

            return CompletableFuture.completedFuture(results);
        }, DataLoaderOptions.newOptions()
            .setCachingEnabled(true));

        return loader;
    }
}
```

### DataLoader Context

```java
@Component
public class DataLoaderContextBuilder {

    @Bean
    public GraphQLContext.Builder contextBuilder() {
        return builder -> {
            DataLoaderRegistry registry = new DataLoaderRegistry();
            builder.of(
                "dataLoaderRegistry", registry,
                "batchLoadingContext", new BatchLoadingContext()
            );
        };
    }
}

// Usage in resolvers
@SchemaMapping
public CompletableFuture<User> author(Post post,
        @ContextValue DataLoaderRegistry registry) {
    return registry.getDataLoader("userLoader").load(post.getAuthorId());
}
```

---

## Resolver Composition

### Shared Resolver Logic

```java
@Component
public class BaseEntityResolver {

    @SchemaMapping
    public LocalDateTime createdAt(BaseEntity entity) {
        return entity.getCreatedAt();
    }

    @SchemaMapping
    public LocalDateTime updatedAt(BaseEntity entity) {
        return entity.getUpdatedAt();
    }

    @SchemaMapping
    public Integer version(BaseEntity entity) {
        return entity.getVersion();
    }
}

@Component
public class UserResolver extends BaseEntityResolver {

    @SchemaMapping
    public List<Post> posts(User user,
            @ContextValue DataLoaderRegistry registry) {
        return registry.getDataLoader("postLoader")
            .load(user.getId());
    }
}
```

### Resolver Factory

```java
@Component
public class ResolverFactory {

    private final Map<String, Object> resolvers;

    public ResolverFactory(
            QueryResolver queryResolver,
            UserResolver userResolver,
            PostResolver postResolver,
            MutationResolver mutationResolver) {
        this.resolvers = Map.of(
            "Query", queryResolver,
            "Mutation", mutationResolver,
            "User", userResolver,
            "Post", postResolver
        );
    }

    public Object getResolver(String typeName) {
        return resolvers.get(typeName);
    }
}
```

### Custom Directive Resolvers

```java
@SchemaMapping
public class RateLimitedResolver {

    @DgsDirective(name = "rateLimit")
    public RateLimitingDirectiveHandler rateLimitHandler() {
        return new RateLimitingDirectiveHandler();
    }
}

public class RateLimitingDirectiveHandler {

    private final RateLimiter rateLimiter;

    @DgsDataFetcherInstrumentation
    public Object fetch(DataFetchingEnvironment env, Object result) {
        String key = env.getExecutionStepInfo()
            .getPath()
            .toString();

        if (!rateLimiter.tryAcquire(key)) {
            throw new GraphQLException("Rate limit exceeded");
        }

        return result;
    }
}
```

---

## Error Handling

### Resolver Error Handling

```java
@Component
public class UserResolver {

    @SchemaMapping
    public User user(@Argument Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new GraphQLException(
                "USER_NOT_FOUND",
                Map.of("userId", id)
            ));
    }

    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        try {
            return userService.create(input);
        } catch (DuplicateEmailException e) {
            throw new GraphQLException(
                "DUPLICATE_EMAIL",
                Map.of("email", input.getEmail())
            );
        }
    }
}
```

### Custom Exception

```java
public class GraphQLException extends RuntimeException {

    private final String code;
    private final Map<String, Object> extensions;

    public GraphQLException(String code, Map<String, Object> extensions) {
        super(code);
        this.code = code;
        this.extensions = extensions;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this; // Don't fill stack trace for GraphQL errors
    }
}

// Exception handler
@Component
public class GraphQLErrorHandler {

    @DgsExceptionHandler
    public GraphQLError handle(GraphQLException exception) {
        return GraphqlErrorBuilder.newError()
            .message(exception.getMessage())
            .errorType(ErrorType.DataFetchingException)
            .extensions(Map.of(
                "code", exception.getCode(),
                "details", exception.getExtensions()
            ))
            .build();
    }
}
```

### Partial Error Response

```graphql
# Query with partial success
{
  "data": {
    "users": [
      { "id": "1", "name": "John" },
      null  // This user failed
    ]
  },
  "errors": [
    {
      "message": "User not found",
      "path": ["users", 1],
      "extensions": {
        "code": "USER_NOT_FOUND"
      }
    }
  ]
}
```

---

## Best Practices

### 1. Keep Resolvers Thin

```java
// Good - thin resolver, delegates to service
@SchemaMapping
public List<Post> posts(User user) {
    return postService.getPostsByUserId(user.getId());
}

// Bad - business logic in resolver
@SchemaMapping
public List<Post> posts(User user) {
    List<Post> posts = postRepository.findByUserId(user.getId());
    posts = posts.stream()
        .filter(p -> p.getStatus() == Status.PUBLISHED)
        .sorted(Comparator.comparing(Post::getPublishedAt).reversed())
        .collect(Collectors.toList());
    return posts;
}
```

### 2. Use DataLoaders for Relationship Resolution

```java
// Good - DataLoader
@SchemaMapping
public CompletableFuture<User> author(Post post,
        @ContextValue DataLoaderRegistry registry) {
    return registry.getDataLoader("userLoader").load(post.getAuthorId());
}

// Bad - direct DB call (N+1)
@SchemaMapping
public User author(Post post) {
    return userRepository.findById(post.getAuthorId()).orElse(null);
}
```

### 3. Handle Nullability Explicitly

```graphql
type User {
  id: ID!
  name: String!     # Non-null
  email: String!    # Non-null
  bio: String       # Nullable
  posts: [Post!]!   # Non-null list, non-null items
}
```

### 4. Use Projections for Performance

```java
// Fetch only needed fields
@QueryMapping
public User user(@Argument Long id, ProjectionFactory factory) {
    return userRepository.findById(id, factory.createProjection(UserProjection.class))
        .orElse(null);
}

public interface UserProjection {
    Long getId();
    String getName();
    String getEmail();

    @Value("#{target.posts.size()}")
    int getPostCount();
}
```

### 5. Version Your Resolvers

```java
@Component
@DgsComponent(name = "UserResolver")
public class UserResolverV1 {

    @SchemaMapping(type = "Query", field = "user")
    public User userV1(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

### 6. Cache DataLoader Results

```java
@Bean
public DataLoaderOptions dataLoaderOptions() {
    return DataLoaderOptions.newOptions()
        .setCachingEnabled(true)
        .setBatchLoaderContextProvider(batchLoaderEnv -> {
            Map<String, Object> context = new HashMap<>();
            context.put("requestId", batchLoaderEnv.getKeyContextsList().get(0));
            return context;
        });
}
```

---

## Further Reading

- [GraphQL Java DataLoader](https://www.graphql-java.com/documentation/batching)
- [N+1 Problem in GraphQL](https://blog.apollographql.com/解决-graphql-的-n-1-问题)
- [DataLoader Pattern](https://github.com/graphql/dataloader)
- [Spring for GraphQL Reference](https://docs.spring.io/spring-framework/reference/web/webflux-webgraphql.html)
