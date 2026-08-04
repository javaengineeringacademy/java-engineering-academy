# MySQL Fundamentals

## Table of Contents

1. [Installation](#installation)
2. [Architecture](#architecture)
3. [SQL Basics](#sql-basics)
4. [Data Types](#data-types)
5. [InnoDB Storage Engine](#innodb-storage-engine)
6. [Indexes](#indexes)
7. [Stored Procedures](#stored-procedures)
8. [Views](#views)
9. [Triggers](#triggers)
10. [Best Practices](#best-practices)

---

## Installation

### Ubuntu/Debian

```bash
# Update package index
sudo apt update

# Install MySQL server
sudo apt install mysql-server

# Secure installation
sudo mysql_secure_installation

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Check status
sudo systemctl status mysql
```

### CentOS/RHEL

```bash
# Add MySQL repository
sudo rpm -i https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm

# Install MySQL
sudo yum install mysql-community-server

# Start MySQL
sudo systemctl start mysqld
sudo systemctl enable mysqld

# Get temporary password
sudo grep 'temporary password' /var/log/mysqld.log

# Secure installation
mysql_secure_installation
```

### Docker

```bash
# Run MySQL container
docker run -d \
  --name mysql-server \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=mydb \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  -v mysql-data:/var/lib/mysql \
  mysql:8.0

# Connect to MySQL
docker exec -it mysql-server mysql -u root -p
```

### macOS (Homebrew)

```bash
# Install MySQL
brew install mysql

# Start MySQL
brew services start mysql

# Secure installation
mysql_secure_installation
```

### Configuration File

```ini
# /etc/mysql/my.cnf
[mysqld]
# Basic Settings
port = 3306
bind-address = 127.0.0.1
max_connections = 200
max_allowed_packet = 64M

# InnoDB Settings
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 1
innodb_file_per_table = ON

# Character Set
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci

# Logging
slow_query_log = ON
slow_query_log_file = /var/log/mysql/slow.log
long_query_time = 2

[client]
default-character-set = utf8mb4
```

---

## Architecture

### Connection Layer

```
┌─────────────────────────────────────────────────────────┐
│                    Client Applications                   │
├─────────────────────────────────────────────────────────┤
│                   Connection Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │  TCP/IP     │  │   Unix      │  │   Shared    │     │
│  │  Socket     │  │   Socket    │  │   Memory    │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│  ┌─────────────┐  ┌─────────────┐                       │
│  │  Thread     │  │  Connection │                       │
│  │  Pool       │  │  Pool       │                       │
│  └─────────────┘  └─────────────┘                       │
├─────────────────────────────────────────────────────────┤
│                    SQL Layer                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   Parser    │  │  Optimizer  │  │  Executor   │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
├─────────────────────────────────────────────────────────┤
│                  Storage Engines                         │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐   │
│  │  InnoDB │  │  MyISAM │  │  Memory │  │  Archive│   │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘   │
└─────────────────────────────────────────────────────────┘
```

### MySQL 8.0 Features

- Window Functions
- Common Table Expressions (CTEs)
- JSON enhancements
- Data dictionary (InnoDB)
- Role-based access control
- Invisible indexes
- Descending indexes
- Instant DDL

---

## SQL Basics

### Database Operations

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS mydb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Use database
USE mydb;

-- Show databases
SHOW DATABASES;

-- Drop database
DROP DATABASE IF EXISTS mydb;

-- Show current database
SELECT DATABASE();
```

### Table Operations

```sql
-- Create table
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_email (email),
  INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Describe table structure
DESCRIBE users;
SHOW CREATE TABLE users;

-- Alter table
ALTER TABLE users
  ADD COLUMN phone VARCHAR(20) AFTER email,
  ADD INDEX idx_phone (phone),
  MODIFY COLUMN username VARCHAR(100) NOT NULL,
  DROP COLUMN phone;

-- Rename table
RENAME TABLE users TO customers;

-- Drop table
DROP TABLE IF EXISTS customers;

-- Truncate table (faster than DELETE for removing all rows)
TRUNCATE TABLE customers;
```

### CRUD Operations

```sql
-- INSERT
INSERT INTO users (username, email, password_hash, first_name, last_name)
VALUES
  ('john_doe', 'john@example.com', SHA2('password123', 256), 'John', 'Doe'),
  ('jane_smith', 'jane@example.com', SHA2('password456', 256), 'Jane', 'Smith');

-- INSERT multiple rows
INSERT INTO users (username, email, password_hash)
VALUES
  ('user1', 'user1@example.com', SHA2('pass1', 256)),
  ('user2', 'user2@example.com', SHA2('pass2', 256));

-- INSERT with subquery
INSERT INTO archived_users (id, username, email)
SELECT id, username, email
FROM users
WHERE is_active = FALSE;

-- SELECT
SELECT * FROM users;

SELECT username, email, first_name
FROM users
WHERE is_active = TRUE
  AND created_at > '2024-01-01'
ORDER BY created_at DESC
LIMIT 10 OFFSET 20;

-- UPDATE
UPDATE users
SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

-- UPDATE with JOIN
UPDATE users u
JOIN orders o ON u.id = o.user_id
SET u.last_order_date = o.created_at
WHERE o.created_at > u.last_order_date;

-- DELETE
DELETE FROM users WHERE id = 1;

-- DELETE with JOIN
DELETE u FROM users u
JOIN inactive_sessions s ON u.id = s.user_id
WHERE s.last_activity < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

### Filtering and Sorting

```sql
-- WHERE clause operators
SELECT * FROM users WHERE
  id IN (1, 2, 3)
  AND email LIKE '%@gmail.com'
  AND username BETWEEN 'a' AND 'm'
  AND phone IS NOT NULL
  AND age IN (25, 30, 35);

-- Pattern matching
SELECT * FROM users WHERE
  username LIKE 'john%'        -- Starts with 'john'
  OR username LIKE '%doe'      -- Ends with 'doe'
  OR email LIKE '%@%._%';      -- Contains @ and .

-- ORDER BY
SELECT * FROM users ORDER BY
  created_at DESC,           -- Newest first
  username ASC;              -- Then alphabetically

-- LIMIT and OFFSET (pagination)
SELECT * FROM users
LIMIT 10 OFFSET 0;           -- Page 1
SELECT * FROM users
LIMIT 10 OFFSET 10;          -- Page 2

-- DISTINCT
SELECT DISTINCT country FROM users;

-- Aliases
SELECT
  first_name AS 'First Name',
  last_name AS 'Last Name',
  CONCAT(first_name, ' ', last_name) AS 'Full Name'
FROM users;
```

---

## Data Types

### Numeric Types

```sql
-- Integer types
TINYINT        -- 1 byte (-128 to 127)
SMALLINT       -- 2 bytes (-32768 to 32767)
MEDIUMINT      -- 3 bytes (-8388608 to 8388607)
INT            -- 4 bytes (-2147483648 to 2147483647)
BIGINT         -- 8 bytes (-9223372036854775808 to 9223372036854775807)

-- Unsigned integers (0 to max value)
INT UNSIGNED   -- 0 to 4294967295

-- Decimal types
DECIMAL(10,2)  -- Exact precision (99999999.99)
FLOAT          -- 4 bytes (approximate)
DOUBLE         -- 8 bytes (approximate)
```

### String Types

```sql
CHAR(50)       -- Fixed length (always stores 50 characters)
VARCHAR(255)   -- Variable length (stores up to 255 characters)
TEXT           -- Up to 65535 characters
MEDIUMTEXT     -- Up to 16777215 characters
LONGTEXT       -- Up to 4294967295 characters
ENUM           -- Enumeration (one value from list)
SET            -- Set (multiple values from list)

-- Example
CREATE TABLE products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  status ENUM('active', 'inactive', 'discontinued') DEFAULT 'active',
  tags SET('sale', 'new', 'featured') DEFAULT NULL
);
```

### Date and Time Types

```sql
DATE           -- '2024-01-15'
TIME           -- '14:30:00'
DATETIME       -- '2024-01-15 14:30:00'
TIMESTAMP      -- Auto-updates, timezone-aware
YEAR           -- 2024

-- Example
CREATE TABLE events (
  id INT AUTO_INCREMENT PRIMARY KEY,
  event_name VARCHAR(100),
  event_date DATE NOT NULL,
  event_time TIME,
  start_datetime DATETIME,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Working with dates
SELECT
  NOW() AS current_datetime,
  CURDATE() AS current_date,
  CURTIME() AS current_time,
  DATE_FORMAT(created_at, '%Y-%m-%d') AS formatted_date,
  DATEDIFF(NOW(), created_at) AS days_since_created,
  DATE_ADD(created_at, INTERVAL 7 DAY) AS next_week,
  DATE_SUB(created_at, INTERVAL 1 MONTH) AS last_month
FROM events;
```

### JSON Type (MySQL 5.7+)

```sql
CREATE TABLE user_profiles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  profile JSON NOT NULL,
  CHECK (JSON_VALID(profile))
);

-- Insert JSON
INSERT INTO user_profiles (profile) VALUES
  ('{"name": "John", "age": 30, "hobbies": ["reading", "gaming"]}');

-- Query JSON
SELECT
  JSON_EXTRACT(profile, '$.name') AS name,
  profile->>'$.name' AS name_unquoted,
  profile->'$.hobbies'[0] AS first_hobby
FROM user_profiles;

-- Modify JSON
UPDATE user_profiles
SET profile = JSON_SET(profile, '$.age', 31)
WHERE id = 1;

UPDATE user_profiles
SET profile = JSON_REMOVE(profile, '$.hobbies[0]')
WHERE id = 1;
```

---

## InnoDB Storage Engine

### Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    InnoDB Architecture                    │
├─────────────────────────────────────────────────────────┤
│  Memory Structures                                      │
│  ┌─────────────────────────────────────────────────┐    │
│  │              Buffer Pool                        │    │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐          │    │
│  │  │  Data   │ │  Index  │ │ Change  │          │    │
│  │  │  Pages  │ │  Pages  │ │ Buffer  │          │    │
│  │  └─────────┘ └─────────┘ └─────────┘          │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Additional Memory Pools                        │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Disk Structures                                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │  System Tablespace (ibdata1)                    │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  File-Per-Table Tablespace (.ibd files)        │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Redo Logs (ib_logfile0, ib_logfile1)          │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Undo Logs                                      │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### Buffer Pool

```sql
-- Check buffer pool size
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';

-- Set buffer pool size (typically 70-80% of available RAM)
SET GLOBAL innodb_buffer_pool_size = 8589934592; -- 8GB

-- Monitor buffer pool usage
SHOW ENGINE INNODB STATUS\G

-- Buffer pool status
SHOW STATUS LIKE 'Innodb_buffer_pool%';

-- Important metrics
-- Innodb_buffer_pool_read_requests: Logical reads
-- Innodb_buffer_pool_reads: Physical reads (from disk)
-- Hit ratio = 1 - (reads / read_requests) -- should be > 99%
```

### Tablespaces

```sql
-- Create table with custom tablespace
CREATE TABLE large_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  data TEXT
) TABLESPACE my_tablespace;

-- Create tablespace
CREATE TABLESPACE my_tablespace
  ADD DATAFILE 'my_tablespace.ibd'
  FILE_BLOCK_SIZE = 16384;

-- Move table to tablespace
ALTER TABLE large_table TABLESPACE my_tablespace;
```

### Transactions

```sql
-- Transaction examples
START TRANSACTION;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

COMMIT; -- or ROLLBACK;

-- Savepoints
START TRANSACTION;
INSERT INTO orders (user_id, total) VALUES (1, 100);
SAVEPOINT order_inserted;
INSERT INTO order_items (order_id, product_id, quantity) VALUES (1, 1, 2);
-- If error: ROLLBACK TO order_inserted;
COMMIT;
```

### MVCC (Multi-Version Concurrency Control)

```sql
-- Check current transaction isolation level
SELECT @@transaction_isolation;

-- Set isolation level
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- MVCC provides:
-- 1. Non-blocking reads
-- 2. Consistent snapshots
-- 3. Read committed data
-- 4. Repeatable reads (default in MySQL)

-- Undo log usage
-- Each row has hidden columns:
-- DB_TRX_ID: Transaction ID
-- DB_ROLL_PTR: Rollback pointer
-- DB_ROW_ID: Row ID (if no primary key)
```

---

## Indexes

### Index Types

```sql
-- B-Tree Index (default)
CREATE INDEX idx_email ON users (email);

-- Unique Index
CREATE UNIQUE INDEX idx_username ON users (username);

-- Composite Index (multi-column)
CREATE INDEX idx_name_email ON users (last_name, first_name, email);

-- Prefix Index (for TEXT/BLOB columns)
CREATE INDEX idx_email_prefix ON users (email(10));

-- Full-Text Index
CREATE FULLTEXT INDEX idx_search ON products (name, description);

-- Spatial Index
CREATE SPATIAL INDEX idx_location ON stores (location);

-- Hash Index (memory engine only)
CREATE INDEX idx_hash ON users (id) USING HASH;
```

### Index Best Practices

```sql
-- GOOD: Selective columns first
CREATE INDEX idx_status_created ON orders (status, created_at);

-- GOOD: Covering index
CREATE INDEX idx_covering ON orders (user_id, status, total);
-- This covers: SELECT status, total FROM orders WHERE user_id = ?

-- BAD: Low cardinality
CREATE INDEX idx_gender ON users (gender); -- Only M/F, not useful

-- BAD: Too many columns
CREATE INDEX idx_bad ON table (col1, col2, col3, col4, col5);

-- Check index usage
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';

-- Monitor unused indexes
SELECT * FROM sys.schema_unused_indexes;

-- Monitor redundant indexes
SELECT * FROM sys.schema_redundant_indexes;
```

### InnoDB Index Structure

```
B+ Tree Structure:
                    ┌─────────────┐
                    │   30 | 60   │  ← Root Node
                    └──────┬──────┘
            ┌───────────────┼───────────────┐
            ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │ 10 | 20     │ │ 40 | 50     │ │ 70 | 80     │  ← Internal Nodes
    └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
        ┌──┴──┐         ┌──┴──┐         ┌──┴──┐
        ▼     ▼         ▼     ▼         ▼     ▼
      ┌───┐ ┌───┐     ┌───┐ ┌───┐     ┌───┐ ┌───┐
      │ 10│ │ 20│     │ 40│ │ 50│     │ 70│ │ 80│  ← Leaf Nodes
      └───┘ └───┘     └───┘ └───┘     └───┘ └───┘
        │    │          │    │          │    │
        ▼    ▼          ▼    ▼          ▼    ▼
      Data  Data      Data  Data      Data  Data
```

### Clustered vs Secondary Indexes

```sql
-- Clustered Index (Primary Key)
-- - One per table
-- - Contains actual row data
-- - Physically orders data on disk

-- Secondary Index
-- - Contains indexed columns + primary key
-- - Requires lookup to get full row (bookmark lookup)

-- Example
CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,  -- Clustered index
  user_id INT,
  total DECIMAL(10,2),
  created_at TIMESTAMP,
  INDEX idx_user_id (user_id),        -- Secondary index (contains id)
  INDEX idx_created (created_at)      -- Secondary index (contains id)
);

-- To avoid lookup, use covering index
CREATE INDEX idx_covering ON orders (user_id, total, created_at);
-- This covers: SELECT total, created_at FROM orders WHERE user_id = ?
```

---

## Stored Procedures

### Basic Syntax

```sql
-- Simple stored procedure
DELIMITER //
CREATE PROCEDURE GetUsers()
BEGIN
  SELECT * FROM users WHERE is_active = TRUE;
END //
DELIMITER ;

-- Call procedure
CALL GetUsers();

-- Procedure with parameters
DELIMITER //
CREATE PROCEDURE GetUserById(IN userId INT)
BEGIN
  SELECT * FROM users WHERE id = userId;
END //
DELIMITER ;

CALL GetUserById(1);

-- Procedure with IN, OUT, INOUT parameters
DELIMITER //
CREATE PROCEDURE GetUserStats(
  IN userId INT,
  OUT totalOrders INT,
  OUT totalSpent DECIMAL(10,2)
)
BEGIN
  SELECT COUNT(*), COALESCE(SUM(total), 0)
  INTO totalOrders, totalSpent
  FROM orders
  WHERE user_id = userId;
END //
DELIMITER ;

-- Call and get output
CALL GetUserStats(1, @orders, @spent);
SELECT @orders AS total_orders, @spent AS total_spent;
```

### Control Structures

```sql
DELIMITER //
CREATE PROCEDURE ProcessOrder(IN orderId INT)
BEGIN
  DECLARE orderStatus VARCHAR(20);
  DECLARE orderTotal DECIMAL(10,2);

  -- Get order info
  SELECT status, total INTO orderStatus, orderTotal
  FROM orders WHERE id = orderId;

  -- IF-ELSEIF-ELSE
  IF orderStatus = 'pending' THEN
    UPDATE orders SET status = 'processing' WHERE id = orderId;
  ELSEIF orderStatus = 'processing' THEN
    UPDATE orders SET status = 'shipped' WHERE id = orderId;
  ELSE
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Invalid order status';
  END IF;

  -- CASE statement
  CASE orderStatus
    WHEN 'pending' THEN
      INSERT INTO order_log (order_id, action) VALUES (orderId, 'started');
    WHEN 'shipped' THEN
      INSERT INTO order_log (order_id, action) VALUES (orderId, 'completed');
  END CASE;

  -- WHILE loop
  DECLARE counter INT DEFAULT 0;
  WHILE counter < orderTotal DO
    INSERT INTO order_items_temp (order_id, item_number)
    VALUES (orderId, counter);
    SET counter = counter + 1;
  END WHILE;

  -- REPEAT loop
  SET counter = 0;
  REPEAT
    SET counter = counter + 1;
  UNTIL counter >= 5
  END REPEAT;

END //
DELIMITER ;
```

### Cursors

```sql
DELIMITER //
CREATE PROCEDURE ProcessAllOrders()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE orderId INT;
  DECLARE orderTotal DECIMAL(10,2);

  -- Declare cursor
  DECLARE order_cursor CURSOR FOR
    SELECT id, total FROM orders WHERE status = 'pending';

  -- Declare handler
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  -- Open cursor
  OPEN order_cursor;

  -- Loop through results
  order_loop: LOOP
    FETCH order_cursor INTO orderId, orderTotal;

    IF done THEN
      LEAVE order_loop;
    END IF;

    -- Process each order
    IF orderTotal > 100 THEN
      UPDATE orders SET status = 'priority' WHERE id = orderId;
    ELSE
      UPDATE orders SET status = 'processing' WHERE id = orderId;
    END IF;

  END LOOP order_loop;

  -- Close cursor
  CLOSE order_cursor;
END //
DELIMITER ;
```

### Error Handling

```sql
DELIMITER //
CREATE PROCEDURE SafeTransfer(
  IN fromAccount INT,
  IN toAccount INT,
  IN amount DECIMAL(10,2)
)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    -- Rollback on any error
    ROLLBACK;
    -- Log error
    INSERT INTO error_log (procedure_name, error_message, error_time)
    VALUES ('SafeTransfer', 'Transfer failed', NOW());
    RESIGNAL;
  END;

  START TRANSACTION;

  -- Check balance
  IF (SELECT balance FROM accounts WHERE id = fromAccount) < amount THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Insufficient funds';
  END IF;

  UPDATE accounts SET balance = balance - amount WHERE id = fromAccount;
  UPDATE accounts SET balance = balance + amount WHERE id = toAccount;

  INSERT INTO transfers (from_account, to_account, amount, transfer_date)
  VALUES (fromAccount, toAccount, amount, NOW());

  COMMIT;
END //
DELIMITER ;
```

---

## Views

```sql
-- Create view
CREATE VIEW active_users AS
SELECT id, username, email, first_name, last_name
FROM users
WHERE is_active = TRUE;

-- Query view
SELECT * FROM active_users;

-- Create complex view
CREATE VIEW user_order_summary AS
SELECT
  u.id,
  u.username,
  u.email,
  COUNT(o.id) AS total_orders,
  COALESCE(SUM(o.total), 0) AS total_spent,
  MAX(o.created_at) AS last_order_date
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username, u.email;

-- Updatable views (under certain conditions)
CREATE VIEW active_user_emails AS
SELECT id, email FROM users WHERE is_active = TRUE;

-- This works
UPDATE active_user_emails SET email = 'new@example.com' WHERE id = 1;

-- Materialized view equivalent (manual refresh)
CREATE TABLE user_stats_cache AS
SELECT user_id, COUNT(*) as order_count, SUM(total) as total_spent
FROM orders GROUP BY user_id;

-- Refresh strategy
TRUNCATE TABLE user_stats_cache;
INSERT INTO user_stats_cache
SELECT user_id, COUNT(*) as order_count, SUM(total) as total_spent
FROM orders GROUP BY user_id;
```

---

## Triggers

```sql
-- Before INSERT trigger
DELIMITER //
CREATE TRIGGER before_user_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  -- Auto-generate username if not provided
  IF NEW.username IS NULL OR NEW.username = '' THEN
    SET NEW.username = CONCAT('user_', NEW.id);
  END IF;

  -- Hash password
  IF NEW.password_hash IS NOT NULL THEN
    SET NEW.password_hash = SHA2(NEW.password_hash, 256);
  END IF;
END //
DELIMITER ;

-- After INSERT trigger
DELIMITER //
CREATE TRIGGER after_order_insert
AFTER INSERT ON orders
FOR EACH ROW
BEGIN
  -- Update user's last order date
  UPDATE users
  SET last_order_date = NEW.created_at
  WHERE id = NEW.user_id;

  -- Log the order
  INSERT INTO order_audit (order_id, action, action_time)
  VALUES (NEW.id, 'created', NOW());
END //
DELIMITER ;

-- BEFORE UPDATE trigger
DELIMITER //
CREATE TRIGGER before_user_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
  -- Track changes
  IF OLD.email != NEW.email THEN
    INSERT INTO user_email_history (user_id, old_email, new_email, changed_at)
    VALUES (OLD.id, OLD.email, NEW.email, NOW());
  END IF;

  -- Update timestamp
  SET NEW.updated_at = NOW();
END //
DELIMITER ;

-- AFTER DELETE trigger
DELIMITER //
CREATE TRIGGER after_user_delete
AFTER DELETE ON users
FOR EACH ROW
BEGIN
  -- Archive user data
  INSERT INTO deleted_users (user_id, username, email, deleted_at)
  VALUES (OLD.id, OLD.username, OLD.email, NOW());

  -- Delete related data
  DELETE FROM user_preferences WHERE user_id = OLD.id;
  DELETE FROM user_sessions WHERE user_id = OLD.id;
END //
DELIMITER ;

-- View triggers
SHOW TRIGGERS;
SELECT * FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = 'mydb';

-- Drop trigger
DROP TRIGGER IF EXISTS before_user_insert;
```

---

## Best Practices

### Naming Conventions

```sql
-- Tables: plural nouns, snake_case
CREATE TABLE user_orders (...);
CREATE TABLE product_categories (...);

-- Columns: snake_case
CREATE TABLE users (
  user_id INT PRIMARY KEY,
  first_name VARCHAR(50),
  created_at TIMESTAMP
);

-- Indexes: idx_table_column
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_orders_user_id ON orders (user_id);

-- Foreign keys: fk_table_referenced
ALTER TABLE orders ADD CONSTRAINT fk_orders_users
  FOREIGN KEY (user_id) REFERENCES users (id);
```

### Security

```sql
-- Create user with limited privileges
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'strong_password';

-- Grant specific privileges
GRANT SELECT, INSERT, UPDATE ON mydb.* TO 'app_user'@'localhost';

-- Revoke privileges
REVOKE DELETE ON mydb.* FROM 'app_user'@'localhost';

-- Use roles (MySQL 8.0+)
CREATE ROLE 'app_read', 'app_write';
GRANT SELECT ON mydb.* TO 'app_read';
GRANT INSERT, UPDATE, DELETE ON mydb.* TO 'app_write';

GRANT 'app_read', 'app_write' TO 'app_user'@'localhost';
SET DEFAULT ROLE ALL TO 'app_user'@'localhost';

-- Enable SSL
ALTER USER 'app_user'@'localhost' REQUIRE SSL;
```

### Performance Tips

```sql
-- Use EXPLAIN for query analysis
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';

-- Avoid SELECT *
SELECT id, username, email FROM users WHERE id = 1;

-- Use LIMIT for large result sets
SELECT * FROM logs ORDER BY created_at DESC LIMIT 100;

-- Batch inserts
INSERT INTO large_table (col1, col2) VALUES
  (1, 'a'), (2, 'b'), (3, 'c'), ...;

-- Use proper data types
-- BAD: VARCHAR for numbers
-- GOOD: INT for numeric values

-- Normalize to 3NF, denormalize selectively

-- Use connection pooling in application
```

### Backup Strategies

```bash
# Logical backup
mysqldump -u root -p --all-databases > full_backup.sql

# Backup specific database
mysqldump -u root -p mydb > mydb_backup.sql

# Backup with routines and triggers
mysqldump -u root -p --routines --triggers mydb > mydb_full.sql

# Restore
mysql -u root -p mydb < mydb_backup.sql

# Physical backup (Percona XtraBackup)
xtrabackup --backup --target-dir=/backup/
xtrabackup --prepare --target-dir=/backup/
xtrabackup --copy-back --target-dir=/backup/
```

---

## Summary

| Feature | Description |
|---------|-------------|
| Storage Engine | InnoDB (ACID, row-level locking) |
| Replication | Async, semi-sync, GTID |
| Partitioning | Range, list, hash, key |
| JSON Support | Native JSON data type |
| Window Functions | ROW_NUMBER, RANK, LAG, LEAD |
| CTEs | WITH clause for complex queries |
| Roles | Role-based access control |
| Encryption | At-rest and in-transit |

## Next Steps

- [MySQL Queries](../queries/) - Advanced querying techniques
- [MySQL Optimization](../optimization/) - Performance tuning
- [MySQL Replication](../replication/) - High availability setup
- [MySQL High Availability](../HA/) - Cluster and group replication
