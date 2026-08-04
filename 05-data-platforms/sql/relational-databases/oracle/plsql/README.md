# PL/SQL Programming

## Comprehensive Guide to Oracle PL/SQL

PL/SQL is Oracle's procedural extension to SQL. This guide covers cursors, triggers, stored procedures, and packages.

---

## Table of Contents

1. [PL/SQL Basics](#plsql-basics)
2. [Cursors](#cursors)
3. [Triggers](#triggers)
4. [Stored Procedures](#stored-procedures)
5. [Functions](#functions)
6. [Packages](#packages)
7. [Exception Handling](#exception-handling)

---

## PL/SQL Basics

### Block Structure

```sql
DECLARE
    v_name VARCHAR2(100);
    v_count NUMBER;
BEGIN
    -- Executable code
    SELECT COUNT(*) INTO v_count FROM employees;

    DBMS_OUTPUT.PUT_LINE('Employee count: ' || v_count);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
```

### Variables and Types

```sql
DECLARE
    -- Scalar types
    v_name VARCHAR2(100) := 'John';
    v_salary NUMBER(10,2) := 5000.00;
    v_hire_date DATE := SYSDATE;
    v_active BOOLEAN := TRUE;

    -- Record type
    TYPE emp_record IS RECORD (
        emp_id NUMBER,
        emp_name VARCHAR2(100),
        salary NUMBER
    );
    v_emp emp_record;

    -- Collection types
    TYPE name_table IS TABLE OF VARCHAR2(100);
    v_names name_table;

    TYPE emp_table IS TABLE OF employees%ROWTYPE;
    v_employees emp_table;
BEGIN
    NULL;
END;
/
```

---

## Cursors

### Implicit Cursors

```sql
BEGIN
    UPDATE employees SET salary = salary * 1.1 WHERE department_id = 10;

    IF SQL%FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Updated ' || SQL%ROWCOUNT || ' rows');
    END IF;
END;
/
```

### Explicit Cursors

```sql
DECLARE
    CURSOR emp_cursor IS
        SELECT employee_id, first_name, salary
        FROM employees
        WHERE department_id = 10;

    v_emp emp_cursor%ROWTYPE;
BEGIN
    OPEN emp_cursor;
    LOOP
        FETCH emp_cursor INTO v_emp;
        EXIT WHEN emp_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(v_emp.first_name || ': ' || v_emp.salary);
    END LOOP;
    CLOSE emp_cursor;
END;
/
```

### Cursor FOR Loop

```sql
DECLARE
    CURSOR emp_cursor IS
        SELECT * FROM employees WHERE department_id = 10;
BEGIN
    FOR emp IN emp_cursor LOOP
        DBMS_OUTPUT.PUT_LINE(emp.first_name || ': ' || emp.salary);
    END LOOP;
END;
/
```

### Cursor with Parameters

```sql
DECLARE
    CURSOR emp_cursor(p_dept_id NUMBER) IS
        SELECT * FROM employees WHERE department_id = p_dept_id;
BEGIN
    FOR emp IN emp_cursor(10) LOOP
        DBMS_OUTPUT.PUT_LINE(emp.first_name);
    END LOOP;

    FOR emp IN emp_cursor(20) LOOP
        DBMS_OUTPUT.PUT_LINE(emp.first_name);
    END LOOP;
END;
/
```

---

## Triggers

### Row-Level Trigger

```sql
CREATE OR REPLACE TRIGGER emp_salary_audit
BEFORE UPDATE OF salary ON employees
FOR EACH ROW
BEGIN
    INSERT INTO salary_audit (
        employee_id, old_salary, new_salary, change_date
    ) VALUES (
        :OLD.employee_id, :OLD.salary, :NEW.salary, SYSDATE
    );
END;
/
```

### Statement-Level Trigger

```sql
CREATE OR REPLACE TRIGGER emp_dept_audit
AFTER INSERT OR UPDATE OR DELETE ON employees
BEGIN
    INSERT INTO department_audit (
        action, action_date, user_name
    ) VALUES (
        ora_dict_obj_name, SYSDATE, USER
    );
END;
/
```

### Compound Trigger

```sql
CREATE OR REPLACE TRIGGER emp_compound
FOR INSERT OR UPDATE OR DELETE ON employees
COMPOUND TRIGGER

    v_count NUMBER := 0;

    BEFORE STATEMENT IS
    BEGIN
        v_count := 0;
    END BEFORE STATEMENT;

    AFTER EACH ROW IS
    BEGIN
        v_count := v_count + 1;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('Rows affected: ' || v_count);
    END AFTER STATEMENT;
END emp_compound;
/
```

---

## Stored Procedures

### Basic Procedure

```sql
CREATE OR REPLACE PROCEDURE update_salary (
    p_emp_id IN NUMBER,
    p_percentage IN NUMBER
) AS
    v_old_salary NUMBER;
    v_new_salary NUMBER;
BEGIN
    SELECT salary INTO v_old_salary
    FROM employees WHERE employee_id = p_emp_id;

    v_new_salary := v_old_salary * (1 + p_percentage / 100);

    UPDATE employees
    SET salary = v_new_salary
    WHERE employee_id = p_emp_id;

    INSERT INTO salary_history (
        employee_id, old_salary, new_salary, change_date
    ) VALUES (
        p_emp_id, v_old_salary, v_new_salary, SYSDATE
    );

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Salary updated from ' || v_old_salary ||
                         ' to ' || v_new_salary);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Employee not found');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END update_salary;
/
```

### Procedure with OUT Parameters

```sql
CREATE OR REPLACE PROCEDURE get_employee_info (
    p_emp_id IN NUMBER,
    p_name OUT VARCHAR2,
    p_salary OUT NUMBER,
    p_dept_name OUT VARCHAR2
) AS
BEGIN
    SELECT e.first_name || ' ' || e.last_name,
           e.salary,
           d.department_name
    INTO p_name, p_salary, p_dept_name
    FROM employees e
    JOIN departments d ON e.department_id = d.department_id
    WHERE e.employee_id = p_emp_id;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_name := NULL;
        p_salary := NULL;
        p_dept_name := NULL;
END get_employee_info;
/
```

---

## Functions

### Function with RETURN

```sql
CREATE OR REPLACE FUNCTION calculate_bonus (
    p_emp_id IN NUMBER
) RETURN NUMBER AS
    v_salary NUMBER;
    v_years NUMBER;
    v_bonus NUMBER;
BEGIN
    SELECT salary,
           MONTHS_BETWEEN(SYSDATE, hire_date) / 12
    INTO v_salary, v_years
    FROM employees
    WHERE employee_id = p_emp_id;

    v_bonus := v_salary * LEAST(v_years * 0.05, 0.5);

    RETURN ROUND(v_bonus, 2);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
END calculate_bonus;
/
```

### Pipelined Function

```sql
CREATE OR REPLACE TYPE emp_type AS OBJECT (
    emp_id NUMBER,
    emp_name VARCHAR2(100),
    salary NUMBER
);
/

CREATE OR REPLACE TYPE emp_table AS TABLE OF emp_type;
/

CREATE OR REPLACE FUNCTION get_high_salary_emps
RETURN emp_table PIPELINED AS
BEGIN
    FOR rec IN (SELECT employee_id, first_name, salary
                FROM employees
                WHERE salary > 10000) LOOP
        PIPE ROW(emp_type(rec.employee_id, rec.first_name, rec.salary));
    END LOOP;
    RETURN;
END;
/

-- Usage
SELECT * FROM TABLE(get_high_salary_emps());
```

---

## Packages

### Package Specification

```sql
CREATE OR REPLACE PACKAGE employee_pkg AS
    -- Type declarations
    TYPE emp_record IS RECORD (
        emp_id NUMBER,
        emp_name VARCHAR2(100),
        salary NUMBER
    );

    -- Procedure declarations
    PROCEDURE update_salary(
        p_emp_id IN NUMBER,
        p_percentage IN NUMBER
    );

    FUNCTION get_salary(p_emp_id IN NUMBER) RETURN NUMBER;

    -- Constants
    g_max_salary CONSTANT NUMBER := 100000;
END employee_pkg;
/
```

### Package Body

```sql
CREATE OR REPLACE PACKAGE BODY employee_pkg AS

    -- Private variable
    v_audit_enabled BOOLEAN := TRUE;

    -- Procedure implementation
    PROCEDURE update_salary(
        p_emp_id IN NUMBER,
        p_percentage IN NUMBER
    ) AS
        v_old_salary NUMBER;
    BEGIN
        SELECT salary INTO v_old_salary
        FROM employees WHERE employee_id = p_emp_id;

        UPDATE employees
        SET salary = salary * (1 + p_percentage / 100)
        WHERE employee_id = p_emp_id;

        IF v_audit_enabled THEN
            INSERT INTO salary_audit VALUES (
                p_emp_id, v_old_salary, SYSDATE
            );
        END IF;
    END update_salary;

    -- Function implementation
    FUNCTION get_salary(p_emp_id IN NUMBER) RETURN NUMBER AS
        v_salary NUMBER;
    BEGIN
        SELECT salary INTO v_salary
        FROM employees WHERE employee_id = p_emp_id;
        RETURN v_salary;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END get_salary;

END employee_pkg;
/
```

---

## Exception Handling

### Predefined Exceptions

```sql
DECLARE
    v_name VARCHAR2(100);
BEGIN
    SELECT first_name INTO v_name
    FROM employees WHERE employee_id = 999;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Employee not found');
    WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE('Multiple employees found');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLCODE || ' - ' || SQLERRM);
END;
/
```

### User-Defined Exceptions

```sql
DECLARE
    e_insufficient_funds EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_insufficient_funds, -20001);

    v_balance NUMBER;
BEGIN
    SELECT balance INTO v_balance FROM accounts WHERE id = 1;

    IF v_balance < 100 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient funds');
    END IF;

EXCEPTION
    WHEN e_insufficient_funds THEN
        DBMS_OUTPUT.PUT_LINE('Not enough money');
END;
/
```

---

## Further Reading

- [Oracle PL/SQL Documentation](https://docs.oracle.com/en/database/oracle/oracle-database/19/lnpls/)
- [PL/SQL User's Guide](https://docs.oracle.com/en/database/oracle/oracle-database/19/lnpls/plsql-control-statements.html)
