# Migration Quiz

## Question 1
What is the Strangler Fig pattern in software migration?
- A) A database migration technique
- B) Gradually replacing parts of a legacy system with new components until the old system is fully replaced
- C) A pattern for scaling databases
- D) A caching strategy

**Answer: B**
**Explanation:** The Strangler Fig pattern incrementally replaces functionality in a legacy system with new implementations. New features are built in the new system, and existing features are migrated one by one until the legacy system can be decommissioned.

## Question 2
What is the most critical step before starting a legacy system migration?
- A) Immediately rewriting all code
- B) Thoroughly understanding the existing system's behavior, dependencies, and business rules
- C) Choosing the newest technology stack
- D) Hiring more developers

**Answer: B**
**Explanation:** Before migrating, you must understand what the existing system does, its business rules, data flows, integrations, and edge cases. Without this understanding, you risk losing critical functionality or introducing bugs.

## Question 3
What is a database migration strategy for moving from a monolith to microservices?
- A) Shared database across all services
- B) Database per service with data synchronization or event-driven patterns
- C) Deleting all data and starting fresh
- D) Using only file-based storage

**Answer: B**
**Explanation:** Each microservice should own its data. Migration strategies include the Database per Service pattern, using events for data synchronization, the Saga pattern for distributed transactions, and careful data partitioning.

## Question 4
What is the "Big Bang" migration approach?
- A) Migrating one feature at a time
- B) Replacing the entire system in a single deployment cutover
- C) Running both systems in parallel indefinitely
- D) Only migrating the database

**Answer: B**
**Explanation:** Big Bang migration replaces the entire legacy system at once with the new system. It's riskier than incremental approaches but can be simpler for smaller systems. It requires extensive testing and a clear rollback plan.

## Question 5
Why is having a rollback plan essential during migration?
- A) It isn't necessary
- B) To quickly revert to the working legacy system if the new system has critical issues in production
- C) To save money on cloud costs
- D) To impress stakeholders

**Answer: B**
**Explanation:** Migrations can fail or reveal unexpected issues. A rollback plan ensures you can quickly restore the previous working state, minimizing downtime and data loss. It should be tested before the migration begins.