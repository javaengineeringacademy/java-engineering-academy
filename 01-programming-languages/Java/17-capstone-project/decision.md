# Capstone Project Decision Guide

## When to Start the Capstone Project

| Readiness Indicator | Status | Action Needed |
|---------------------|--------|---------------|
| Completed modules 00-08 | ☐ | Complete foundation modules first |
| Completed modules 09-12 | ☐ | Complete intermediate modules |
| Completed modules 13-16 | ☐ | Complete advanced modules |
| Understand OOP principles | ☐ | Review 02-OOP if needed |
| Can write basic tests | ☐ | Review 12-Testing |
| Understand collections | ☐ | Review 04-Collections |

## Project Selection Criteria

### Choose a Capstone Project That:

| Criterion | Why It Matters | Example |
|-----------|----------------|---------|
| Real-world problem | Demonstrates practical skills | E-commerce, Inventory, Library |
| Moderate complexity | Shows breadth without overwhelming | 5-10 main features |
| Multiple modules | Proves integration ability | Uses at least 10 modules |
| Clear requirements | Enables focus on implementation | Well-defined scope |
| Portfolio worthy | Showcases skills to employers | Impressive for interviews |

### Recommended Capstone Projects

| Project | Complexity | Modules Used | Skills Demonstrated |
|---------|------------|--------------|---------------------|
| E-commerce System | High | 00-16 (all) | Full-stack, complex business logic |
| Library Management | Medium | 00-12, 14-16 | CRUD, reporting, user management |
| Inventory System | Medium | 00-09, 11-16 | Stock management, analytics |
| Chat Application | High | 00-10, 12-16 | Real-time, concurrency |
| Blog Platform | Medium | 00-08, 11-16 | Content management, API design |

## Technology Stack Recommendations

### Option 1: Spring Boot (Recommended)

| Component | Technology | Why |
|-----------|------------|-----|
| Framework | Spring Boot 3.x | Industry standard, full ecosystem |
| Database | H2 (dev), PostgreSQL (prod) | Easy development, production-ready |
| ORM | Spring Data JPA | Simplifies data access |
| Testing | JUnit 5, Mockito | Industry standard |
| Build | Maven | Dependency management |

### Option 2: Plain Java

| Component | Technology | Why |
|-----------|------------|-----|
| Build | Maven | Project management |
| Database | H2 | Embedded, easy setup |
| HTTP | Java HTTP Server | No framework dependencies |
| Testing | JUnit 5 | Standard testing |
| Logging | SLF4J + Logback | Structured logging |

## Time Management

### Recommended Timeline (4 weeks)

| Week | Focus | Deliverables |
|------|-------|--------------|
| 1 | Planning & Setup | Requirements, architecture, project setup |
| 2 | Core Features | Domain model, repository, service layer |
| 3 | Integration | API endpoints, error handling, testing |
| 4 | Polish | Performance, security, documentation |

## Common Pitfalls

| Pitfall | Impact | Prevention |
|---------|--------|------------|
| Over-engineering | Wasted time | Start simple, iterate |
| Skipping tests | Unreliable code | Write tests first (TDD) |
| Ignoring error handling | Poor UX | Handle exceptions properly |
| No documentation | Hard to maintain | Document as you go |
| Wrong technology choice | Project fails | Choose familiar stack |

## Success Criteria

| Criterion | Target | Measurement |
|-----------|--------|-------------|
| Code quality | Clean, maintainable | Code review checklist |
| Test coverage | 80%+ | JaCoCo report |
| Documentation | Complete | README, API docs |
| Performance | Meets requirements | Load testing |
| Security | Basic security | Security checklist |
