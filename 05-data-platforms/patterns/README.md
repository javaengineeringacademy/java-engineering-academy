# Data Access Patterns

## Overview

Data access patterns are proven solutions for common problems encountered when application code interacts with databases and data stores. These patterns abstract persistence concerns, promote separation of concerns, and provide consistent approaches to data manipulation across enterprise applications.

Properly applied patterns reduce coupling between business logic and data storage, improve testability, and enable teams to swap data access technologies without rewriting domain code.

## Core Categories

**Persistence Patterns** handle how objects are stored and retrieved from databases:
- Repository, Data Mapper, Active Record, DAO

**Transaction Patterns** manage data consistency across operations:
- Unit of Work, Two-Phase Commit, Saga

**Query Patterns** optimize how data is fetched and filtered:
- Specification, Lazy Loading, Eager Loading

**Architecture Patterns** structure how data flows through systems:
- CQRS, Event Sourcing, CDC, Outbox, Aggregate Root

**Object Patterns** define how data is represented and transferred:
- DTO, Value Object, Identity Map

## Selection Criteria

| Factor | Consideration |
|--------|--------------|
| Complexity | Simple CRUD vs complex domain logic |
| Team Size | Skill level and pattern familiarity |
| Performance | Read-heavy vs write-heavy workloads |
| Consistency | ACID requirements vs eventual consistency |
| Scale | Single database vs distributed systems |

## Common Anti-Patterns

- **Anemic Domain Model**: Business logic scattered in service layers
- **Smart Database**: Excessive stored procedures and triggers
- **God Repository**: One repository handling all entity types
- **Premature Optimization**: Adding caching layers before measuring
- **N+1 Queries**: Repeated individual fetches in loops

## Implementation Languages

Patterns apply across all languages and frameworks. Implementation specifics vary:
- **Java**: Spring Data, JPA/Hibernate, MyBatis
- **Python**: SQLAlchemy, Django ORM, Peewee
- **TypeScript/JavaScript**: TypeORM, Prisma, Sequelize
- **C#**: Entity Framework, Dapper, NHibernate
- **Go**: GORM, Ent, sqlx

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture* (2002)
- Evans, Eric. *Domain-Driven Design* (2003)
- Vernon, Vaughn. *Implementing Domain-Driven Design* (2013)
- Richardson, Chris. *Microservices Patterns* (2018)
- Fowler. *Catalog of Patterns of Enterprise Application Architecture*
