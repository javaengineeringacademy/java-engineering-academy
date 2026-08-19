# DAO (Data Access Object) Pattern

## Intent
Provide an abstract interface to some type of database or other persistence mechanism. Separates data access from business logic.

## Key Components
- **Domain Entity**: Business object
- **DAO Interface**: Defines data access operations
- **DAO Implementation**: Handles actual persistence (JDBC, JPA, etc.)

## DAO vs Repository
- **DAO** is data-centric: mirrors database tables, contains SQL/NoSQL logic
- **Repository** is domain-centric: mirrors aggregate roots, uses domain language

## When to Use
- Direct database access with SQL-level control
- Legacy system integration
- Applications where domain-driven design is not used
- Low-level data access abstractions

## Benefits
- Clean separation of data access logic
- Easier database migration
- Centralized SQL/query management
- Testable with mock implementations

## Example
```java
UserDao dao = new UserDaoImpl();
dao.create(new User("Alice", "alice@example.com"));
List<User> users = dao.findAll();
```
