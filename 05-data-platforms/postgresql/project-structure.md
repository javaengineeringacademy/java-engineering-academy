# PostgreSQL Project Structure

## Database Project Layout

```
database-project/
├── migrations/
│   ├── 001_create_users.sql
│   ├── 002_create_orders.sql
│   └── 003_add_indexes.sql
├── seeds/
│   ├── development.sql
│   └── production.sql
├── functions/
│   ├── update_timestamp.sql
│   └── calculate_total.sql
├── views/
│   ├── active_users.sql
│   └── order_summary.sql
├── triggers/
│   └── audit_trigger.sql
├── schemas/
│   └── public.sql
├── config/
│   ├── postgresql.conf
│   └── pg_hba.conf
├── scripts/
│   ├── backup.sh
│   └── restore.sh
├── docker-compose.yml
└── README.md
```

## Migration Tools

### Using Flyway

```bash
# Install Flyway
brew install flyway

# Create migration
flyway create migration V1__create_users

# Run migrations
flyway migrate

# Check status
flyway info
```

### Using Liquibase

```xml
<databaseChangeLog>
  <changeSet id="1" author="dev">
    <createTable tableName="users">
      <column name="id" type="int">
        <constraints primaryKey="true"/>
      </column>
      <column name="name" type="varchar(100)"/>
    </createTable>
  </changeSet>
</databaseChangeLog>
```

### Using golang-migrate

```bash
# Create migration
migrate create -ext sql -dir migrations -seq create_users

# Run migrations
migrate -path migrations -database "postgres://localhost:5432/mydb?sslmode=disable" up
```

## Schema Organization

### Schema-per-Feature

```sql
-- Create schemas
CREATE SCHEMA auth;
CREATE SCHEMA inventory;
CREATE SCHEMA reporting;

-- Use in tables
CREATE TABLE auth.users (...);
CREATE TABLE inventory.products (...);
```

### Function Organization

```sql
-- Create function schema
CREATE SCHEMA functions;

-- Create function
CREATE OR REPLACE functions.update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

## Version Control

### Git Ignore

```
*.log
*.pid
.env
.env.local
data/
```

### Migration Naming Convention

```
001_create_users.sql
002_add_email_index.sql
003_create_orders.sql
```

## Testing

### Test Database Setup

```bash
# Create test database
createdb mydb_test

# Run migrations
psql -d mydb_test -f migrations/*.sql

# Run tests
pgTAP tests/
```

### Sample Test

```sql
BEGIN;
SELECT plan(2);

SELECT has_column('users', 'id', 'Users table has id column');
SELECT has_column('users', 'email', 'Users table has email column');

SELECT * FROM finish();
ROLLBACK;
```

## Docker Setup

### docker-compose.yml

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: secret
    ports:
      - "5432:5432"
    volumes:
      - ./migrations:/docker-entrypoint-initdb.d
      - postgres-data:/var/lib/postgresql/data
volumes:
  postgres-data:
```

## Deployment

### CI/CD Pipeline

```yaml
# .github/workflows/deploy.yml
name: Deploy Database
on:
  push:
    branches: [main]
jobs:
  migrate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run migrations
        run: flyway migrate
```

## Best Practices

1. Use migration tools for schema changes
2. Version control all SQL files
3. Test migrations before production
4. Use schemas for organization
5. Document database structure
