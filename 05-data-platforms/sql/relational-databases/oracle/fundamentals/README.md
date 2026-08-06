# Oracle Database Fundamentals

## Table of Contents

1. [Architecture](#architecture)
2. [Installation](#installation)
3. [SQL Basics](#sql-basics)
4. [Tablespaces](#tablespaces)
5. [PL/SQL Basics](#plsql-basics)
6. [Data Dictionary](#data-dictionary)
7. [Backup and Recovery](#backup-and-recovery)
8. [Best Practices](#best-practices)

---

## Architecture

### Oracle Instance Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Oracle Instance                            │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Memory Structures                      │    │
│  │  ┌─────────────────────────────────────────────┐    │    │
│  │  │           SGA (System Global Area)          │    │    │
│  │  │  ┌──────────┐ ┌──────────┐ ┌──────────┐   │    │    │
│  │  │  │  Buffer  │ │  Shared  │ │   Log    │   │    │    │
│  │  │  │  Cache   │ │   Pool   │ │  Buffer  │   │    │    │
│  │  │  └──────────┘ └──────────┘ └──────────┘   │    │    │
│  │  │  ┌──────────┐ ┌──────────┐                │    │    │
│  │  │  │  Large   │ │  Java    │                │    │    │
│  │  │  │  Pool    │ │  Pool    │                │    │    │
│  │  │  └──────────┘ └──────────┘                │    │    │
│  │  └─────────────────────────────────────────────┘    │    │
│  │  ┌─────────────────────────────────────────────┐    │    │
│  │  │           PGA (Program Global Area)         │    │    │
│  │  │  - Per-session memory                       │    │    │
│  │  │  - Sort area, hash area                     │    │    │
│  │  └─────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Background Processes                    │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐        │    │
│  │  │ PMON│ │ SMON│ │ DBWn│ │ LGWR│ │ CKPT│  ...    │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘        │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Database Files                            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Data Files  │ │ Control     │ │ Redo Logs   │          │
│  │             │ │ Files       │ │             │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Parameter   │ │ Password    │ │ Archive     │          │
│  │ File        │ │ File        │ │ Logs        │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
```

### Background Processes

| Process | Description |
|---------|-------------|
| PMON | Process Monitor - cleans up failed processes |
| SMON | System Monitor - instance recovery, space management |
| DBWn | Database Writer - writes dirty buffers to disk |
| LGWR | Log Writer - writes redo logs to disk |
| CKPT | Checkpoint - updates data file headers |
| ARCn | Archiver - archives redo logs |
| MMON | Manageability Monitor - AWM statistics |

---

## Installation

### Oracle Database Installation

```bash
# Download Oracle Database
# https://www.oracle.com/database/technologies/

# Install dependencies
sudo apt-get install libaio1 libaio-dev

# Create Oracle user
sudo useradd -m -s /bin/bash oracle
sudo passwd oracle

# Set up environment
echo 'export ORACLE_HOME=/opt/oracle/product/21c/dbhome_1' >> ~/.bashrc
echo 'export PATH=$ORACLE_HOME/bin:$PATH' >> ~/.bashrc
echo 'export LD_LIBRARY_PATH=$ORACLE_HOME/lib' >> ~/.bashrc

# Run installer
./runInstaller
```

### Connection

```sql
-- SQL*Plus
sqlplus username/password@hostname:port/service_name

-- Example
sqlplus scott/tiger@localhost:1521/XEPDB1

-- SQL Developer
-- Host: localhost
-- Port: 1521
-- Service: XEPDB1
-- Username: scott
-- Password: tiger
```

---

## SQL Basics

### DDL (Data Definition Language)

```sql
-- Create table
CREATE TABLE employees (
  employee_id NUMBER(6) PRIMARY KEY,
  first_name VARCHAR2(20),
  last_name VARCHAR2(25) NOT NULL,
  email VARCHAR2(25) UNIQUE,
  phone_number VARCHAR2(20),
  hire_date DATE DEFAULT SYSDATE,
  job_id VARCHAR2(10) NOT NULL,
  salary NUMBER(8,2),
  commission_pct NUMBER(2,2),
  manager_id NUMBER(6),
  department_id NUMBER(4)
);

-- Create sequence
CREATE SEQUENCE emp_seq
  START WITH 1000
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- Create index
CREATE INDEX idx_emp_email ON employees (email);
CREATE INDEX idx_emp_dept ON employees (department_id);

-- Create view
CREATE VIEW emp_view AS
SELECT employee_id, first_name, last_name, salary
FROM employees
WHERE salary > 5000;

-- Alter table
ALTER TABLE employees ADD (bonus NUMBER(8,2));
ALTER TABLE employees MODIFY (phone_number VARCHAR2(30));
ALTER TABLE employees DROP COLUMN bonus;

-- Drop table
DROP TABLE employees CASCADE CONSTRAINTS;
```

### DML (Data Manipulation Language)

```sql
-- Insert
INSERT INTO employees (employee_id, first_name, last_name, email, job_id, salary)
VALUES (emp_seq.NEXTVAL, 'John', 'Doe', 'jdoe', 'IT_PROG', 6000);

-- Insert multiple rows
INSERT ALL
  INTO employees (employee_id, first_name, last_name, email, job_id, salary)
    VALUES (emp_seq.NEXTVAL, 'John', 'Doe', 'jdoe1', 'IT_PROG', 6000)
  INTO employees (employee_id, first_name, last_name, email, job_id, salary)
    VALUES (emp_seq.NEXTVAL, 'Jane', 'Smith', 'jsmith', 'IT_PROG', 6500)
SELECT * FROM DUAL;

-- Update
UPDATE employees
SET salary = salary * 1.1
WHERE department_id = 60;

-- Delete
DELETE FROM employees WHERE employee_id = 1000;

-- Merge (UPSERT)
MERGE INTO employees e
USING (SELECT 1000 AS id, 'John' AS name FROM DUAL) src
ON (e.employee_id = src.id)
WHEN MATCHED THEN
  UPDATE SET e.first_name = src.name
WHEN NOT MATCHED THEN
  INSERT (employee_id, first_name, last_name, email, job_id)
  VALUES (src.id, src.name, 'Doe', 'jdoe', 'IT_PROG');
```

### Transaction Control

```sql
-- Start transaction
SET TRANSACTION READ WRITE;

-- Commit
COMMIT;

-- Rollback
ROLLBACK;

-- Savepoint
SAVEPOINT update_sp1;
UPDATE employees SET salary = 7000 WHERE employee_id = 1000;
ROLLBACK TO update_sp1;

-- Auto-commit
SET AUTOCOMMIT ON;
```

---

## Tablespaces

### Creating Tablespaces

```sql
-- Create permanent tablespace
CREATE TABLESPACE my_data
  DATAFILE '/oradata/mydb/mydata01.dbf'
  SIZE 100M
  AUTOEXTEND ON NEXT 10M MAXSIZE 1G
  EXTENT MANAGEMENT LOCAL;

-- Create temporary tablespace
CREATE TEMPORARY TABLESPACE my_temp
  TEMPFILE '/oradata/mydb/mytemp01.dbf'
  SIZE 50M
  AUTOEXTEND ON NEXT 10M MAXSIZE 500M;

-- Create tablespace with multiple datafiles
CREATE TABLESPACE my_ts
  DATAFILE
    '/oradata/mydb/mydata01.dbf' SIZE 100M,
    '/oradata/mydb/mydata02.dbf' SIZE 100M
  AUTOEXTEND ON NEXT 10M MAXSIZE 2G
  EXTENT MANAGEMENT LOCAL;
```

### Managing Tablespaces

```sql
-- Alter tablespace
ALTER TABLESPACE my_data
  ADD DATAFILE '/oradata/mydb/mydata03.dbf'
  SIZE 100M
  AUTOEXTEND ON;

-- Drop tablespace
DROP TABLESPACE my_data INCLUDING CONTENTS AND DATAFILES;

-- Check tablespace usage
SELECT
  tablespace_name,
  ROUND(SUM(bytes) / 1024 / 1024, 2) AS size_mb,
  ROUND(SUM(maxbytes) / 1024 / 1024, 2) AS max_size_mb
FROM dba_data_files
GROUP BY tablespace_name;

-- Check temp tablespace
SELECT
  tablespace_name,
  ROUND(SUM(bytes) / 1024 / 1024, 2) AS size_mb
FROM dba_temp_files
GROUP BY tablespace_name;
```

---

## PL/SQL Basics

### PL/SQL Block Structure

```sql
-- Anonymous block
DECLARE
  v_salary NUMBER(8,2);
  v_name VARCHAR2(50);
BEGIN
  SELECT salary, first_name || ' ' || last_name
  INTO v_salary, v_name
  FROM employees
  WHERE employee_id = 100;

  DBMS_OUTPUT.PUT_LINE('Employee: ' || v_name);
  DBMS_OUTPUT.PUT_LINE('Salary: ' || v_salary);

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Employee not found');
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
```

### Variables and Constants

```sql
DECLARE
  -- Variables
  v_salary NUMBER(8,2);
  v_name VARCHAR2(50) := 'John';
  v_hire_date DATE := SYSDATE;
  v_is_active BOOLEAN := TRUE;

  -- Constants
  c_tax_rate CONSTANT NUMBER(3,2) := 0.15;

  -- %TYPE (column type)
  v_emp_name employees.first_name%TYPE;

  -- %ROWTYPE (row type)
  v_emp_rec employees%ROWTYPE;
BEGIN
  NULL;
END;
/
```

### Control Structures

```sql
-- IF-THEN-ELSIF-ELSE
DECLARE
  v_salary NUMBER(8,2) := 5000;
  v_bonus NUMBER(8,2);
BEGIN
  IF v_salary > 10000 THEN
    v_bonus := v_salary * 0.2;
  ELSIF v_salary > 5000 THEN
    v_bonus := v_salary * 0.15;
  ELSE
    v_bonus := v_salary * 0.1;
  END IF;

  DBMS_OUTPUT.PUT_LINE('Bonus: ' || v_bonus);
END;
/

-- CASE statement
DECLARE
  v_job_id VARCHAR2(10) := 'IT_PROG';
  v_salary NUMBER(8,2);
BEGIN
  CASE v_job_id
    WHEN 'IT_PROG' THEN v_salary := 6000;
    WHEN 'SA_MAN' THEN v_salary := 10000;
    WHEN 'AD_PRES' THEN v_salary := 20000;
    ELSE v_salary := 5000;
  END CASE;

  DBMS_OUTPUT.PUT_LINE('Salary: ' || v_salary);
END;
/

-- LOOP
DECLARE
  v_counter NUMBER := 1;
BEGIN
  LOOP
    DBMS_OUTPUT.PUT_LINE('Counter: ' || v_counter);
    v_counter := v_counter + 1;
    EXIT WHEN v_counter > 10;
  END LOOP;
END;
/

-- FOR loop
BEGIN
  FOR i IN 1..10 LOOP
    DBMS_OUTPUT.PUT_LINE('Counter: ' || i);
  END LOOP;
END;
/

-- WHILE loop
DECLARE
  v_counter NUMBER := 1;
BEGIN
  WHILE v_counter <= 10 LOOP
    DBMS_OUTPUT.PUT_LINE('Counter: ' || v_counter);
    v_counter := v_counter + 1;
  END LOOP;
END;
/
```

---

## Data Dictionary

### Common Views

```sql
-- User tables
SELECT * FROM user_tables;

-- All tables (accessible)
SELECT * FROM all_tables WHERE owner = 'SCOTT';

-- DBA tables
SELECT * FROM dba_tables WHERE owner = 'SCOTT';

-- User objects
SELECT * FROM user_objects;

-- Table columns
SELECT * FROM user_tab_columns WHERE table_name = 'EMPLOYEES';

-- Constraints
SELECT * FROM user_constraints WHERE table_name = 'EMPLOYEES';

-- Indexes
SELECT * FROM user_indexes WHERE table_name = 'EMPLOYEES';

-- Sequences
SELECT * FROM user_sequences;

-- Views
SELECT * FROM user_views;

-- Synonyms
SELECT * FROM user_synonyms;
```

### Performance Views

```sql
-- Session information
SELECT * FROM v$session WHERE username = 'SCOTT';

-- Active sessions
SELECT * FROM v$session WHERE status = 'ACTIVE';

-- SQL statistics
SELECT * FROM v$sql ORDER BY executions DESC;

-- Wait events
SELECT * FROM v$session_wait WHERE sid = 123;

-- Lock information
SELECT * FROM v$lock WHERE block = 1;

-- Tablespace usage
SELECT
  tablespace_name,
  ROUND(SUM(bytes) / 1024 / 1024, 2) AS size_mb
FROM dba_data_files
GROUP BY tablespace_name;
```

---

## Backup and Recovery

### RMAN Backup

```bash
# Connect to RMAN
rman target /

# Full backup
BACKUP DATABASE;

# Backup with archive logs
BACKUP DATABASE PLUS ARCHIVELOG;

# Backup specific tablespace
BACKUP TABLESPACE my_data;

# Backup datafile
BACKUP DATAFILE '/oradata/mydb/system01.dbf';

# Backup controlfile
BACKUP CURRENT CONTROLFILE;
```

### RMAN Restore

```bash
# Restore database
RESTORE DATABASE;
RECOVER DATABASE;

# Restore tablespace
RESTORE TABLESPACE my_data;
RECOVER TABLESPACE my_data;

# Restore datafile
RESTORE DATAFILE '/oradata/mydb/system01.dbf';
RECOVER DATAFILE '/oradata/mydb/system01.dbf';

# Point-in-time recovery
RUN {
  SET UNTIL TIME "TO_DATE('2024-01-15 14:30:00', 'YYYY-MM-DD HH24:MI:SS')";
  RESTORE DATABASE;
  RECOVER DATABASE;
}
ALTER DATABASE OPEN RESETLOGS;
```

### Export/Import (Data Pump)

```bash
# Export schema
expdp scott/tiger DIRECTORY=DATA_PUMP_DIR DUMPFILE=scott.dmp LOGFILE=scott.log SCHEMAS=scott

# Import schema
impdp scott/tiger DIRECTORY=DATA_PUMP_DIR DUMPFILE=scott.dmp LOGFILE=scott_imp.log SCHEMAS=scott

# Export table
expdp scott/tiger DIRECTORY=DATA_PUMP_DIR DUMPFILE=emp.dmp LOGFILE=emp.log TABLES=employees

# Import table
impdp scott/tiger DIRECTORY=DATA_PUMP_DIR DUMPFILE=emp.dmp LOGFILE=emp_imp.log TABLES=employees

# Create directory
CREATE OR REPLACE DIRECTORY DATA_PUMP_DIR AS '/oradata/dump';
GRANT READ, WRITE ON DIRECTORY DATA_PUMP_DIR TO scott;
```

---

## Best Practices

### Naming Conventions

```sql
-- Tables: Plural nouns, UPPERCASE or snake_case
CREATE TABLE EMPLOYEES (...);
CREATE TABLE user_orders (...);

-- Columns: UPPERCASE or snake_case
CREATE TABLE EMPLOYEES (
  EMPLOYEE_ID NUMBER(6) PRIMARY KEY,
  FIRST_NAME VARCHAR2(20),
  HIRE_DATE DATE
);

-- Indexes: IDX_TABLE_COLUMN
CREATE INDEX IDX_EMP_EMAIL ON EMPLOYEES (EMAIL);

-- Primary keys: PK_TABLE
CREATE CONSTRAINT PK_EMPLOYEES PRIMARY KEY (EMPLOYEE_ID);

-- Foreign keys: FK_TABLE_REFERENCED
ALTER TABLE ORDERS ADD CONSTRAINT FK_ORDERS_EMP
  FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID);
```

### Performance Tips

```sql
-- 1. Use bind variables
-- BAD
EXECUTE IMMEDIATE 'SELECT * FROM employees WHERE id = ' || v_id;
-- GOOD
EXECUTE IMMEDIATE 'SELECT * FROM employees WHERE id = :1' INTO v_emp USING v_id;

-- 2. Use EXPLAIN PLAN
EXPLAIN PLAN FOR
SELECT * FROM employees WHERE department_id = 60;
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

-- 3. Use bulk operations
-- BAD
FOR rec IN (SELECT * FROM large_table) LOOP
  -- Process one row at a time
END LOOP;
-- GOOD
FORALL i IN 1..l_array.COUNT
  INSERT INTO target_table VALUES l_array(i);

-- 4. Avoid SELECT *
-- BAD
SELECT * FROM employees;
-- GOOD
SELECT employee_id, first_name, last_name FROM employees;

-- 5. Use appropriate indexes
CREATE INDEX idx_emp_dept ON employees (department_id);
```

---

## Summary

| Feature | Description |
|---------|-------------|
| Architecture | Instance + Database |
| PL/SQL | Procedural language |
| Tablespaces | Logical storage |
| Data Dictionary | Metadata views |
| RMAN | Backup and recovery |
| Data Pump | Export/Import |

## Next Steps

- [Oracle PL/SQL](../plsql/) - Advanced PL/SQL
- Oracle Performance - Performance tuning
