# PostgreSQL Security

## Roles and Users

### Creating Roles

```sql
-- Create role
CREATE ROLE readonly;

-- Create user
CREATE USER app_user WITH PASSWORD 'secure_password';

-- Grant role to user
GRANT readonly TO app_user;
```

### Role Attributes

```sql
-- Create role with attributes
CREATE ROLE admin_role
  WITH LOGIN
  SUPERUSER
  CREATEDB
  CREATEROLE
  PASSWORD 'secure_password';
```

## Privileges

### Database Level

```sql
-- Grant connect
GRANT CONNECT ON DATABASE mydb TO app_user;

-- Grant usage
GRANT USAGE ON SCHEMA public TO app_user;

-- Grant create
GRANT CREATE ON DATABASE mydb TO admin_user;
```

### Table Level

```sql
-- Grant select
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly;

-- Grant all
GRANT ALL PRIVILEGES ON users TO admin_user;

-- Grant specific columns
GRANT SELECT (id, name, email) ON users TO app_user;
```

### Revoking Privileges

```sql
-- Revoke
REVOKE DELETE ON users FROM app_user;

-- Revoke all
REVOKE ALL PRIVILEGES ON users FROM app_user;
```

## Row-Level Security

### Enable RLS

```sql
-- Enable RLS on table
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;

-- Create policy
CREATE POLICY user_orders ON orders
  FOR ALL
  TO app_user
  USING (user_id = current_setting('app.user_id')::int);
```

### Policies

```sql
-- Select policy
CREATE POLICY select_orders ON orders
  FOR SELECT
  TO app_user
  USING (user_id = current_setting('app.user_id')::int);

-- Insert policy
CREATE POLICY insert_orders ON orders
  FOR INSERT
  TO app_user
  WITH CHECK (user_id = current_setting('app.user_id')::int);
```

## SSL Configuration

### Server Configuration

```
ssl = on
ssl_cert_file = 'server.crt'
ssl_key_file = 'server.key'
ssl_ca_file = 'ca.crt'
```

### Client Connection

```bash
# Require SSL
psql "host=localhost dbname=mydb user=app_user sslmode=require"

# Verify certificate
psql "host=localhost dbname=mydb user=app_user sslmode=verify-ca"
```

### pg_hba.conf

```
# Require SSL for remote connections
hostssl mydb app_user 10.0.0.0/8 scram-sha-256
```

## Password Encryption

### SCRAM-SHA-256

```
# In postgresql.conf
password_encryption = scram-sha-256
```

### md5 (Legacy)

```
password_encryption = md5
```

## Auditing

### pgAudit Extension

```sql
-- Install extension
CREATE EXTENSION pgaudit;

-- Configure
ALTER SYSTEM SET pgaudit.log = 'write, ddl';
ALTER SYSTEM SET pgaudit.log_parameter = on;
SELECT pg_reload_conf();
```

### Audit Log

```sql
-- Log all DDL
ALTER SYSTEM SET log_statement = 'ddl';

-- Log all queries
ALTER SYSTEM SET log_statement = 'all';
```

## Network Security

### Firewall Rules

```bash
# Allow only specific IPs
sudo ufw allow from 10.0.0.0/8 to any port 5432

# Deny all others
sudo ufw deny 5432
```

### Connection Limits

```
# In pg_hba.conf
host mydb app_user 10.0.0.0/8 scram-sha-256
```

```sql
-- Per user connection limit
ALTER ROLE app_user CONNECTION LIMIT 10;
```

## Data Encryption

### Transparent Data Encryption

```sql
-- Using pgcrypto
CREATE EXTENSION pgcrypto;

-- Encrypt data
INSERT INTO users (name, email)
VALUES ('Alice', pgp_sym_encrypt('alice@example.com', 'key'));
```

### Column-Level Encryption

```sql
-- Encrypt column
ALTER TABLE users ADD COLUMN encrypted_email BYTEA;

UPDATE users
SET encrypted_email = pgp_sym_encrypt(email, 'key');
```

## Best Practices

1. Use least privilege principle
2. Enable SSL for all connections
3. Use strong password policies
4. Enable row-level security
5. Audit sensitive operations
6. Regular security reviews
