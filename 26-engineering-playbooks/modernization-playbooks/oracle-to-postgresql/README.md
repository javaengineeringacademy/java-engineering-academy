# Oracle to PostgreSQL Migration

## Overview

Oracle Database has been the standard for enterprise applications, but licensing costs, complexity, and the maturation of PostgreSQL have driven organizations to migrate. This playbook covers the migration path from Oracle to PostgreSQL.

## Migration Strategy

### Schema Assessment

Analyze Oracle schema objects including tables, views, sequences, packages, stored procedures, triggers, and indexes. Identify Oracle-specific features that require replacement or modification.

Use migration assessment tools to catalog incompatibilities and estimate effort. Common issues include Oracle data types, PL/SQL code, and proprietary functions.

### Schema Conversion

Convert Oracle schema objects to PostgreSQL equivalents. Handle data type mappings, sequence syntax, index differences, and constraint naming conventions.

Oracle packages are replaced by PostgreSQL schemas, functions, or procedures. PL/SQL code requires conversion to PL/pgSQL, which has similar but not identical syntax.

### Data Migration

Migrate data using appropriate tools based on data volume and downtime tolerance. Options include:

- ETL tools for full data migration
- Logical replication for ongoing synchronization
- Foreign data wrappers for hybrid access during migration

## Implementation Patterns

### Data Type Mapping

Oracle and PostgreSQL data types differ in naming and behavior:

- NUMBER maps to NUMERIC, INTEGER, or SERIAL
- VARCHAR2 maps to VARCHAR or TEXT
- DATE maps to TIMESTAMP (Oracle DATE includes time component)
- CLOB maps to TEXT
- BLOB maps to BYTEA

### PL/SQL to PL/pgSQL

Oracle PL/SQL procedures, functions, and triggers require conversion to PL/pgSQL. Key differences include:

- Package declaration becomes schema functions
- Cursor handling syntax changes
- Exception handling uses different syntax
- Built-in function names differ

### Sequences

Oracle sequences use NEXTVAL and CURRVAL syntax. PostgreSQL uses the same functions but with different sequence creation syntax. Auto-increment columns can use SERIAL or GENERATED ALWAYS AS IDENTITY.

### Stored Procedures

Oracle stored procedures may use packages, which PostgreSQL does not support. Decompose packages into individual functions within a schema. Handle package-level variables through temporary tables or configuration tables.

## Key Differences

### Transaction Isolation

Oracle uses multiversion concurrency control (MVCC) with read consistency at the statement level. PostgreSQL uses MVCC with read consistency at the transaction level. This affects how concurrent reads and writes behave.

### NULL Handling

Oracle and PostgreSQL handle NULL differently in some operations. Test NULL behavior thoroughly, especially in string concatenation, comparisons, and aggregate functions.

### Date/Time Functions

Date and time functions differ significantly between Oracle and PostgreSQL. Map Oracle date functions to PostgreSQL equivalents, paying attention to format strings and timezone handling.

## Lessons Learned

### Test Thoroughly

Oracle and PostgreSQL behave differently in edge cases. Test data migration, stored procedures, and application queries thoroughly before cutting over.

### Migrate Incrementally

Consider migrating schemas and data separately. Migrate the schema first, then synchronize data, and finally cut over application connections.

### Validate Data Integrity

Run data validation checks comparing source and target data. Validate row counts, checksums, and sample data to ensure migration completeness.

### Update Connection Configuration

Update application connection strings, pooling configuration, and driver settings. PostgreSQL JDBC driver has different properties than Oracle's driver.
