# GraphQL Fundamentals

## Comprehensive Guide to GraphQL

GraphQL is a query language for APIs that gives clients the power to ask for exactly what they need. This guide covers schema, types, queries, mutations, and subscriptions.

---

## Table of Contents

1. [GraphQL Basics](#graphql-basics)
2. [Schema](#schema)
3. [Types](#types)
4. [Queries](#queries)
5. [Mutations](#mutations)
6. [Subscriptions](#subscriptions)
7. [Best Practices](#best-practices)

---

## GraphQL Basics

### What is GraphQL?

```
GraphQL is:
- A query language for APIs
- A runtime for executing queries
- Type system for describing data
- Not a database language

Key Benefits:
- Clients get exactly what they need
- Single endpoint for all data
- Strongly typed schema
- Introspection capabilities
```

### GraphQL vs REST

```
REST:
- Multiple endpoints
- Over-fetching or under-fetching
- Versioning required
- Less type safety

GraphQL:
- Single endpoint
- Precise data fetching
- No versioning needed
- Strongly typed
```

### Setup

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>

<dependency>
    <groupId>com.graphql-java</groupId>
    <artifactId>graphql-java-tools</artifactId>
</dependency>
```

```yaml
# application.yml
spring:
  graphql:
    graphiql:
      enabled: true
      path: /graphiql
    schema:
      locations: classpath:graphql/
      printer:
        enabled: true
    path: /graphql
```

---

## Schema

### Basic Schema

```graphql
# schema.graphql

type Query {
    user(id: ID!): User
    users: [User!]!
    userByEmail(email: String!): User
}

type Mutation {
    createUser(input: CreateUserInput!): User!
    updateUser(id: ID!, input: UpdateUserInput!): User!
    deleteUser(id: ID!): Boolean!
}

type User {
    id: ID!
    username: String!
    email: String!
    posts: [Post!]!
    createdAt: DateTime!
    updatedAt: DateTime!
}

type Post {
    id: ID!
    title: String!
    content: String!
    author: User!
    comments: [Comment!]!
    createdAt: DateTime!
}

type Comment {
    id: ID!
    text: String!
    author: User!
    post: Post!
    createdAt: DateTime!
}

input CreateUserInput {
    username: String!
    email: String!
    password: String!
}

input UpdateUserInput {
    username: String
    email: String
}

scalar DateTime
```

### Schema with Interfaces

```graphql
interface Node {
    id: ID!
}

interface Timestamped {
    createdAt: DateTime!
    updatedAt: DateTime!
}

type User implements Node & Timestamped {
    id: ID!
    username: String!
    email: String!
    posts: [Post!]!
    createdAt: DateTime!
    updatedAt: DateTime!
}

type Post implements Node & Timestamped {
    id: ID!
    title: String!
    content: String!
    author: User!
    createdAt: DateTime!
    updatedAt: DateTime!
}
```

### Schema with Unions

```graphql
union SearchResult = User | Post | Comment

type Query {
    search(query: String!): [SearchResult!]!
}
```

### Schema with Enums

```graphql
enum PostStatus {
    DRAFT
    PUBLISHED
    ARCHIVED
}

enum SortOrder {
    ASC
    DESC
}

type Post {
    id: ID!
    title: String!
    content: String!
    status: PostStatus!
    createdAt: DateTime!
}

type Query {
    posts(status: PostStatus, sortBy: String, sortOrder: SortOrder): [Post!]!
}
```

---

## Types

### Scalar Types

```graphql
# Built-in scalars
type Query {
    id: ID
    string: String
    int: Int
    float: Float
    boolean: Boolean
}

# Custom scalar
scalar DateTime
scalar JSON
scalar EmailAddress

# Custom scalar definition
extend scalar DateTime @specifiedBy(url: "https://www.graphql-scalars.com/datetime")
```

### Object Types

```graphql
type User {
    id: ID!
    username: String!
    email: String!
    profile: Profile
    posts: [Post!]!
    createdAt: DateTime!
    updatedAt: DateTime!
}

type Profile {
    firstName: String!
    lastName: String!
    bio: String
    avatar: String
}

type Post {
    id: ID!
    title: String!
    content: String!
    author: User!
    tags: [String!]!
    published: Boolean!
    createdAt: DateTime!
}
```

### Input Types

```graphql
input CreateUserInput {
    username: String!
    email: String!
    password: String!
    profile: ProfileInput
}

input ProfileInput {
    firstName: String!
    lastName: String!
    bio: String
}

input PostFilterInput {
    status: PostStatus
    authorId: ID
    tags: [String!]
    createdAfter: DateTime
}
```

### Enum Types

```graphql
enum UserRole {
    USER
    ADMIN
    MODERATOR
}

enum PostStatus {
    DRAFT
    PUBLISHED
    ARCHIVED
}

enum SortOrder {
    ASC
    DESC
}
```

---

## Queries

### Basic Queries

```graphql
# Get single user
query GetUser {
    user(id: "1") {
        id
        username
        email
        createdAt
    }
}

# Get all users
query GetUsers {
    users {
        id
        username
        email
    }
}

# Get user with related data
query GetUserWithPosts {
    user(id: "1") {
        id
        username
        email
        posts {
            id
            title
            createdAt
        }
    }
}
```

### Queries with Arguments

```graphql
# Query with arguments
query GetUserByEmail {
    userByEmail(email: "john@example.com") {
        id
        username
        email
    }
}

# Query with variables
query GetUser($id: ID!) {
    user(id: $id) {
        id
        username
        email
    }
}

# Query with multiple arguments
query GetPosts($status: PostStatus, $sortBy: String, $sortOrder: SortOrder) {
    posts(status: $status, sortBy: $sortBy, sortOrder: $sortOrder) {
        id
        title
        status
        createdAt
    }
}
```

### Queries with Fragments

```graphql
# Fragment definition
fragment UserFields on User {
    id
    username
    email
    createdAt
}

fragment PostFields on Post {
    id
    title
    content
    createdAt
}

# Query using fragments
query GetUserWithPosts {
    user(id: "1") {
        ...UserFields
        posts {
            ...PostFields
        }
    }
}
```

### Queries with Directives

```graphql
# Include directive
query GetUser($id: ID!, $includePosts: Boolean!) {
    user(id: $id) {
        id
        username
        email
        posts @include(if: $includePosts) {
            id
            title
        }
    }
}

# Skip directive
query GetUser($id: ID!, $skipEmail: Boolean!) {
    user(id: $id) {
        id
        username
        email @skip(if: $skipEmail)
    }
}
```

---

## Mutations

### Basic Mutations

```graphql
# Create user
mutation CreateUser($input: CreateUserInput!) {
    createUser(input: $input) {
        id
        username
        email
        createdAt
    }
}

# Update user
mutation UpdateUser($id: ID!, $input: UpdateUserInput!) {
    updateUser(id: $id, input: $input) {
        id
        username
        email
        updatedAt
    }
}

# Delete user
mutation DeleteUser($id: ID!) {
    deleteUser(id: $id)
}
```

### Mutations with Variables

```graphql
# Mutation with variables
mutation CreatePost($title: String!, $content: String!, $authorId: ID!) {
    createPost(input: {
        title: $title
        content: $content
        authorId: $authorId
    }) {
        id
        title
        content
        author {
            id
            username
        }
        createdAt
    }
}
```

### Mutations with Input Objects

```graphql
# Mutation with complex input
mutation CreateUserWithProfile($input: CreateUserInput!) {
    createUser(input: $input) {
        id
        username
        email
        profile {
            firstName
            lastName
            bio
        }
        createdAt
    }
}

# Variables
{
    "input": {
        "username": "johndoe",
        "email": "john@example.com",
        "password": "securepassword",
        "profile": {
            "firstName": "John",
            "lastName": "Doe",
            "bio": "Software developer"
        }
    }
}
```

---

## Subscriptions

### Basic Subscriptions

```graphql
# Subscription for new posts
subscription OnNewPost {
    postCreated {
        id
        title
        content
        author {
            id
            username
        }
        createdAt
    }
}

# Subscription for new comments
subscription OnNewComment($postId: ID!) {
    commentAdded(postId: $postId) {
        id
        text
        author {
            id
            username
        }
        createdAt
    }
}

# Subscription for user updates
subscription OnUserUpdated {
    userUpdated {
        id
        username
        email
        updatedAt
    }
}
```

### Subscriptions with Variables

```graphql
# Subscription with variables
subscription OnPostStatusChanged($authorId: ID!) {
    postStatusChanged(authorId: $authorId) {
        id
        title
        status
        updatedAt
    }
}
```

### Subscription Implementation

```java
@Controller
public class SubscriptionController {
    
    @SubscriptionMapping
    public Flux<Post> postCreated() {
        return postService.getPostFlux();
    }
    
    @SubscriptionMapping
    public Flux<Comment> commentAdded(@Argument Long postId) {
        return commentService.getCommentFlux(postId);
    }
    
    @SubscriptionMapping
    public Flux<User> userUpdated() {
        return userService.getUserFlux();
    }
}
```

---

## Best Practices

### 1. Use Strong Typing

```graphql
# Good - Strong typing
type User {
    id: ID!
    username: String!
    email: String!
    age: Int
    createdAt: DateTime!
}

# Bad - Weak typing
type User {
    id: ID
    username: String
    email: String
    age: Int
    createdAt: String
}
```

### 2. Use Input Types for Mutations

```graphql
# Good - Using input types
input CreateUserInput {
    username: String!
    email: String!
    password: String!
}

type Mutation {
    createUser(input: CreateUserInput!): User!
}

# Bad - Using individual arguments
type Mutation {
    createUser(username: String!, email: String!, password: String!): User!
}
```

### 3. Use Pagination

```graphql
# Good - Using pagination
type Query {
    users(first: Int, after: String, last: Int, before: String): UserConnection!
}

type UserConnection {
    edges: [UserEdge!]!
    pageInfo: PageInfo!
    totalCount: Int
}

type UserEdge {
    node: User!
    cursor: String!
}

type PageInfo {
    hasNextPage: Boolean!
    hasPreviousPage: Boolean!
    startCursor: String
    endCursor: String
}
```

### 4. Use Fragments for Reusability

```graphql
# Good - Using fragments
fragment UserBasicInfo on User {
    id
    username
    email
}

query GetUser {
    user(id: "1") {
        ...UserBasicInfo
        posts {
            id
            title
        }
    }
}
```

### 5. Use Directives for Conditional Fetching

```graphql
# Good - Using directives
query GetUser($id: ID!, $includePosts: Boolean!) {
    user(id: $id) {
        id
        username
        email
        posts @include(if: $includePosts) {
            id
            title
        }
    }
}
```

---

## Common Pitfalls

### 1. Over-fetching in Queries

```graphql
# Bad - Over-fetching
query GetUser {
    user(id: "1") {
        id
        username
        email
        password
        creditCard
        ssn
    }
}

# Good - Only fetch what you need
query GetUser {
    user(id: "1") {
        id
        username
        email
    }
}
```

### 2. N+1 Problem

```java
// Bad - N+1 problem
@QueryMapping
public List<Post> posts(@Argument Long userId) {
    return postRepository.findByUserId(userId); // Called for each user
}

// Good - Using DataLoader
@QueryMapping
public List<Post> posts(@Argument Long userId) {
    return dataLoader.load("posts", userId);
}
```

### 3. Missing Pagination

```graphql
# Bad - No pagination
type Query {
    users: [User!]!
}

# Good - With pagination
type Query {
    users(first: Int, after: String): UserConnection!
}
```

---

## Further Reading

- [GraphQL Official Documentation](https://graphql.org/learn/)
- [GraphQL Specification](https://spec.graphql.org/)
- [Spring for GraphQL](https://spring.io/projects/spring-graphql)
- [GraphQL Java](https://www.graphql-java.com/)
