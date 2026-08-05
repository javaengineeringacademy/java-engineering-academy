# Hibernate Fundamentals

## Overview
Hibernate is the most popular JPA implementation. It's a powerful ORM (Object-Relational Mapping) framework that provides additional features beyond the JPA specification.

## Key Concepts

### Hibernate vs JPA
| Aspect | JPA | Hibernate |
|--------|-----|-----------|
| Type | Specification | Implementation |
| Features | Basic ORM | Advanced ORM |
| Caching | Basic | L1, L2, Query cache |
| Validation | Basic | Advanced |
| Performance | Standard | Optimized |

### Hibernate Annotations
| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as persistent entity |
| `@Table` | Specifies database table name |
| `@Id` | Marks primary key field |
| `@GeneratedValue` | Auto-generates primary key |
| `@Column` | Maps field to column |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@ManyToMany` | Many-to-many relationship |
| `@OneToOne` | One-to-one relationship |
| `@Cache` | Second-level cache configuration |
| `@NaturalId` | Natural key identifier |
| `@Formula` | Derived property |

## Topics
- Entity Lifecycle
- Caching (L1, L2, Query Cache)
- Batch Processing
- Inheritance Mapping
- Interceptors
- Events
- Filters
- Schema Generation

## Learning Objectives
- Configure Hibernate with Spring Boot
- Implement caching strategies
- Optimize database operations
- Use Hibernate-specific features

## Prerequisites
- JPA fundamentals
- Spring Boot basics
- SQL fundamentals
