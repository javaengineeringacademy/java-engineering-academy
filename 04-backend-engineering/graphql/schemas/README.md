# GraphQL Schemas

## Comprehensive Guide to GraphQL Schema Design

A GraphQL schema defines the types, queries, mutations, and subscriptions available in your API. This guide covers schema design, types, interfaces, unions, and enums.

---

## Table of Contents

1. [Schema Design](#schema-design)
2. [Types](#types)
3. [Interfaces](#interfaces)
4. [Unions](#unions)
5. [Enums](#enums)
6. [Directives](#directives)
7. [Best Practices](#best-practices)

---

## Schema Design

### Schema-First Design

```graphql
# schema.graphql

type Query {
    # User queries
    user(id: ID!): User
    users(first: Int, after: String, filter: UserFilterInput): UserConnection!
    userByEmail(email: String!): User
    
    # Post queries
    post(id: ID!): Post
    posts(first: Int, after: String, filter: PostFilterInput): PostConnection!
    postsByAuthor(authorId: ID!): [Post!]!
    
    # Search
    search(query: String!): [SearchResult!]!
}

type Mutation {
    # User mutations
    createUser(input: CreateUserInput!): User!
    updateUser(id: ID!, input: UpdateUserInput!): User!
    deleteUser(id: ID!): Boolean!
    
    # Post mutations
    createPost(input: CreatePostInput!): Post!
    updatePost(id: ID!, input: UpdatePostInput!): Post!
    deletePost(id: ID!): Boolean!
    
    # Comment mutations
    addComment(postId: ID!, input: AddCommentInput!): Comment!
    deleteComment(id: ID!): Boolean!
}

type Subscription {
    postCreated: Post!
    postUpdated(id: ID!): Post!
    commentAdded(postId: ID!): Comment!
}
```

### Schema Composition

```graphql
# types/User.graphql
type User {
    id: ID!
    username: String!
    email: String!
    profile: Profile
    posts: [Post!]!
    comments: [Comment!]!
    createdAt: DateTime!
    updatedAt: DateTime!
}

# types/Post.graphql
type Post {
    id: ID!
    title: String!
    content: String!
    author: User!
    comments: [Comment!]!
    tags: [String!]!
    status: PostStatus!
    createdAt: DateTime!
    updatedAt: DateTime!
}

# types/Comment.graphql
type Comment {
    id: ID!
    text: String!
    author: User!
    post: Post!
    createdAt: DateTime!
}

# inputs/UserInputs.graphql
input CreateUserInput {
    username: String!
    email: String!
    password: String!
    profile: ProfileInput
}

input UpdateUserInput {
    username: String
    email: String
    profile: ProfileInput
}

input UserFilterInput {
    status: UserStatus
    role: UserRole
    createdAfter: DateTime
    createdBefore: DateTime
}
```

---

## Types

### Scalar Types

```graphql
# Built-in scalars
scalar ID
scalar String
scalar Int
scalar Float
scalar Boolean

# Custom scalars
scalar DateTime
scalar JSON
scalar EmailAddress
scalar URL
scalar PositiveInt

# Custom scalar definition
extend scalar DateTime @specifiedBy(url: "https://www.graphql-scalars.com/datetime")
extend scalar EmailAddress @specifiedBy(url: "https://www.graphql-scalars.com/email")
```

### Object Types

```graphql
type User {
    id: ID!
    username: String!
    email: String!
    profile: Profile
    posts: [Post!]!
    comments: [Comment!]!
    role: UserRole!
    status: UserStatus!
    createdAt: DateTime!
    updatedAt: DateTime!
}

type Profile {
    firstName: String!
    lastName: String!
    bio: String
    avatar: String
    socialLinks: [SocialLink!]
}

type SocialLink {
    platform: SocialPlatform!
    url: String!
}

type Address {
    street: String!
    city: String!
    state: String!
    zipCode: String!
    country: String!
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

input UpdateUserInput {
    username: String
    email: String
    profile: ProfileInput
}

input ProfileInput {
    firstName: String!
    lastName: String!
    bio: String
    avatar: String
}

input PostFilterInput {
    status: PostStatus
    authorId: ID
    tags: [String!]
    createdAfter: DateTime
    createdBefore: DateTime
    searchTerm: String
}

input PaginationInput {
    first: Int
    after: String
    last: Int
    before: String
}
```

### Connection Types

```graphql
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

type PostConnection {
    edges: [PostEdge!]!
    pageInfo: PageInfo!
    totalCount: Int
}

type PostEdge {
    node: Post!
    cursor: String!
}
```

---

## Interfaces

### Basic Interfaces

```graphql
interface Node {
    id: ID!
}

interface Timestamped {
    createdAt: DateTime!
    updatedAt: DateTime!
}

interface SoftDeletable {
    deletedAt: DateTime
    isDeleted: Boolean!
}

type User implements Node & Timestamped & SoftDeletable {
    id: ID!
    username: String!
    email: String!
    createdAt: DateTime!
    updatedAt: DateTime!
    deletedAt: DateTime
    isDeleted: Boolean!
}

type Post implements Node & Timestamped & SoftDeletable {
    id: ID!
    title: String!
    content: String!
    createdAt: DateTime!
    updatedAt: DateTime!
    deletedAt: DateTime
    isDeleted: Boolean!
}
```

### Interface with Fields

```graphql
interface HasAuthor {
    author: User!
}

interface HasContent {
    content: String!
}

type Post implements HasAuthor & HasContent {
    id: ID!
    title: String!
    content: String!
    author: User!
    createdAt: DateTime!
}

type Comment implements HasAuthor & HasContent {
    id: ID!
    content: String!
    author: User!
    post: Post!
    createdAt: DateTime!
}
```

### Interface Implementation

```java
// Java implementation
public interface Node {
    String getId();
}

public interface Timestamped {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}

public class User implements Node, Timestamped {
    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // getters and setters
}
```

---

## Unions

### Basic Unions

```graphql
union SearchResult = User | Post | Comment

type Query {
    search(query: String!): [SearchResult!]!
}

# Using union in fragment
query Search($query: String!) {
    search(query: $query) {
        ... on User {
            id
            username
            email
        }
        ... on Post {
            id
            title
            content
        }
        ... on Comment {
            id
            content
            author {
                id
                username
            }
        }
    }
}
```

### Union with Inline Fragments

```graphql
union Media = Image | Video | Audio

type Image {
    id: ID!
    url: String!
    width: Int!
    height: Int!
}

type Video {
    id: ID!
    url: String!
    duration: Int!
    thumbnail: String
}

type Audio {
    id: ID!
    url: String!
    duration: Int!
    artist: String
}

query GetMedia($id: ID!) {
    media(id: $id) {
        ... on Image {
            id
            url
            width
            height
        }
        ... on Video {
            id
            url
            duration
            thumbnail
        }
        ... on Audio {
            id
            url
            duration
            artist
        }
    }
}
```

### Union Implementation

```java
// Java implementation
public interface SearchResult {
    String getId();
    String getType();
}

public class User implements SearchResult {
    private String id;
    private String username;
    private String email;
    
    @Override
    public String getType() {
        return "USER";
    }
}

public class Post implements SearchResult {
    private String id;
    private String title;
    private String content;
    
    @Override
    public String getType() {
        return "POST";
    }
}
```

---

## Enums

### Basic Enums

```graphql
enum UserRole {
    USER
    ADMIN
    MODERATOR
}

enum UserStatus {
    ACTIVE
    INACTIVE
    SUSPENDED
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

type User {
    id: ID!
    username: String!
    role: UserRole!
    status: UserStatus!
}

type Post {
    id: ID!
    title: String!
    status: PostStatus!
    createdAt: DateTime!
}
```

### Enums in Queries

```graphql
query GetUsers($role: UserRole, $status: UserStatus) {
    users(filter: { role: $role, status: $status }) {
        id
        username
        role
        status
    }
}

query GetPosts($status: PostStatus, $sortBy: String, $sortOrder: SortOrder) {
    posts(filter: { status: $status }, sortBy: $sortBy, sortOrder: $sortOrder) {
        id
        title
        status
        createdAt
    }
}
```

### Enum Implementation

```java
// Java implementation
public enum UserRole {
    USER,
    ADMIN,
    MODERATOR
}

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}

public enum PostStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}
```

---

## Directives

### Built-in Directives

```graphql
# @include - Include field if condition is true
query GetUser($id: ID!, $includePosts: Boolean!) {
    user(id: $id) {
        id
        username
        posts @include(if: $includePosts) {
            id
            title
        }
    }
}

# @skip - Skip field if condition is true
query GetUser($id: ID!, $skipEmail: Boolean!) {
    user(id: $id) {
        id
        username
        email @skip(if: $skipEmail)
    }
}
```

### Custom Directives

```graphql
# Directive definition
directive @deprecated(
    reason: String = "No longer supported"
) on FIELD_DEFINITION | ENUM_VALUE

directive @auth(
    requires: Role = USER
) on FIELD_DEFINITION

directive @cacheControl(
    maxAge: Int = 0
) on FIELD_DEFINITION

# Using custom directives
type User {
    id: ID!
    username: String!
    email: String! @auth(requires: ADMIN)
    password: String! @deprecated(reason: "Use auth instead")
}

type Query {
    user(id: ID!): User @cacheControl(maxAge: 300)
}
```

### Directive Implementation

```java
// Java implementation
public class AuthDirective {
    
    private final Role requires;
    
    public AuthDirective(String requires) {
        this.requires = Role.valueOf(requires);
    }
    
    public boolean hasAccess(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + requires));
    }
}
```

---

## Best Practices

### 1. Use Naming Conventions

```graphql
# Good - Consistent naming
type User {
    id: ID!
    username: String!
    email: String!
    createdAt: DateTime!
}

type Post {
    id: ID!
    title: String!
    content: String!
    author: User!
    createdAt: DateTime!
}

# Bad - Inconsistent naming
type user {
    id: ID!
    user_name: String!
    email_address: String!
    created_at: DateTime!
}
```

### 2. Use Descriptive Types

```graphql
# Good - Descriptive types
type User {
    id: ID!
    username: String!
    email: String!
    profile: Profile
    posts: [Post!]!
    createdAt: DateTime!
    updatedAt: DateTime!
}

# Bad - Non-descriptive types
type User {
    id: ID!
    name: String!
    data: Profile
    items: [Post!]!
    time1: DateTime!
    time2: DateTime!
}
```

### 3. Use Input Types for Mutations

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

### 4. Use Pagination for Collections

```graphql
# Good - Using pagination
type Query {
    users(first: Int, after: String): UserConnection!
}

type UserConnection {
    edges: [UserEdge!]!
    pageInfo: PageInfo!
    totalCount: Int
}

# Bad - No pagination
type Query {
    users: [User!]!
}
```

### 5. Use Enums for Fixed Values

```graphql
# Good - Using enums
enum UserRole {
    USER
    ADMIN
    MODERATOR
}

type User {
    role: UserRole!
}

# Bad - Using strings
type User {
    role: String!
}
```

---

## Common Pitfalls

### 1. Not Using Non-Null

```graphql
# Bad - Nullable fields
type User {
    id: ID
    username: String
    email: String
}

# Good - Non-null fields
type User {
    id: ID!
    username: String!
    email: String!
}
```

### 2. Not Using Input Types

```graphql
# Bad - Individual arguments
type Mutation {
    createUser(username: String!, email: String!, password: String!): User!
}

# Good - Input types
input CreateUserInput {
    username: String!
    email: String!
    password: String!
}

type Mutation {
    createUser(input: CreateUserInput!): User!
}
```

### 3. Not Using Pagination

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

- [GraphQL Schema Design](https://graphql.org/learn/schema/)
- [GraphQL Types](https://graphql.org/learn/schema/#type-system)
- [GraphQL Interfaces](https://graphql.org/learn/schema/#interfaces)
- [GraphQL Enums](https://graphql.org/learn/schema/#enumeration-types)
