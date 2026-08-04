# PostgreSQL Extensions

## Table of Contents

1. [Extension Overview](#extension-overview)
2. [PostGIS](#postgis)
3. [pg_trgm](#pg_trgm)
4. [pg_stat_statements](#pg_stat_statements)
5. [pg_partman](#pg_partman)
6. [pgcrypto](#pgcrypto)
7. [Other Extensions](#other-extensions)
8. [Custom Extensions](#custom-extensions)

---

## Extension Overview

### Managing Extensions

```sql
-- List available extensions
SELECT * FROM pg_available_extensions ORDER BY name;

-- List installed extensions
SELECT * FROM pg_extension;

-- Install extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gist";

-- Update extension
ALTER EXTENSION "uuid-ossp" UPDATE;

-- Drop extension
DROP EXTENSION IF EXISTS "uuid-ossp";

-- Check extension version
SELECT
  extname,
  extversion
FROM pg_extension
WHERE extname = 'uuid-ossp';
```

### Extension Categories

| Category | Extensions |
|----------|------------|
| Spatial | PostGIS, pg_raster |
| Text Search | pg_trgm, unaccent, fuzzystrmatch |
| Performance | pg_stat_statements, pg_prewarm |
| Partitioning | pg_partman |
| Security | pgcrypto, pgcrypto |
| Data Types | uuid-ossp, hstore, tablefunc |
| Monitoring | pg_stat_statements, pg_qualstats |

---

## PostGIS

### Installation

```sql
-- Install PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Check PostGIS version
SELECT PostGIS_Version();

-- List PostGIS functions
SELECT routine_name
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_name LIKE 'st_%'
ORDER BY routine_name;
```

### Spatial Data Types

```sql
-- Geometry types
-- POINT, LINESTRING, POLYGON, MULTIPOINT, MULTILINESTRING, MULTIPOLYGON, GEOMETRYCOLLECTION

-- Create spatial table
CREATE TABLE locations (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  geom GEOMETRY(POINT, 4326),  -- SRID 4326 (WGS84)
  description TEXT
);

-- Create index
CREATE INDEX idx_locations_geom ON locations USING GIN (geom);

-- Insert spatial data
INSERT INTO locations (name, geom, description)
VALUES
  ('Office', ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326), 'NYC Office'),
  ('Warehouse', ST_SetSRID(ST_MakePoint(-73.9712, 40.7831), 4326), 'NYC Warehouse');

-- Create polygon
INSERT INTO locations (name, geom, description)
VALUES (
  'Service Area',
  ST_GeomFromText('POLYGON((-74.0 40.7, -73.9 40.7, -73.9 40.8, -74.0 40.8, -74.0 40.7))', 4326),
  'Service boundary'
);
```

### Spatial Functions

```sql
-- Basic geometry functions
SELECT
  name,
  ST_AsText(geom) AS wkt,
  ST_X(geom) AS longitude,
  ST_Y(geom) AS latitude,
  ST_SRID(geom) AS srid
FROM locations;

-- Distance calculation (in meters for geography)
SELECT
  a.name AS location_a,
  b.name AS location_b,
  ST_Distance(
    a.geom::geography,
    b.geom::geography
  ) AS distance_meters
FROM locations a, locations b
WHERE a.id != b.id;

-- Find nearby points
SELECT name
FROM locations
WHERE ST_DWithin(
  geom::geography,
  ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326)::geography,
  1000  -- 1 km radius
);

-- Contains
SELECT name
FROM locations
WHERE ST_Contains(
  geom,
  ST_SetSRID(ST_MakePoint(-73.98, 40.75), 4326)
);

-- Intersection
SELECT ST_Intersection(geom1, geom2) FROM ...;

-- Union
SELECT ST_Union(geom1, geom2) FROM ...;

-- Buffer
SELECT ST_Buffer(geom, 1000) FROM locations;  -- 1 km buffer

-- Area
SELECT ST_Area(geom::geography) FROM locations;

-- Perimeter
SELECT ST_Perimeter(geom::geography) FROM locations;

-- Centroid
SELECT ST_Centroid(geom) FROM locations;

-- Simplify (reduce geometry complexity)
SELECT ST_Simplify(geom, 0.001) FROM locations;
```

### Spatial Queries

```sql
-- Find points within polygon
SELECT l.name
FROM locations l
WHERE ST_Within(l.geom, (SELECT geom FROM locations WHERE name = 'Service Area'));

-- Find nearest neighbor
SELECT name
FROM locations
ORDER BY geom <-> ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326)
LIMIT 1;

-- Find overlapping geometries
SELECT a.name, b.name
FROM locations a, locations b
WHERE ST_Intersects(a.geom, b.geom)
  AND a.id < b.id;

-- Create convex hull
SELECT ST_ConvexHull(ST_Collect(geom)) FROM locations;

-- Create buffer around points
SELECT
  name,
  ST_Buffer(geom::geography, 500)::geometry AS buffer
FROM locations;

-- Spatial join
SELECT
  l.name,
  c.neighborhood
FROM locations l
JOIN neighborhoods c ON ST_Contains(c.geom, l.geom);
```

---

## pg_trgm

### Installation

```sql
-- Install pg_trgm
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Check trigram similarity
SELECT similarity('hello', 'hallo');
-- 0.5

SELECT similarity('hello', 'world');
-- 0.0
```

### Trigram Operations

```sql
-- Create table with trigram index
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  description TEXT
);

-- Create GIN index for trigram similarity
CREATE INDEX idx_products_name_trgm ON products USING GIN (name gin_trgm_ops);

-- Trigram similarity search
SELECT
  name,
  similarity(name, 'laptop') AS sim
FROM products
WHERE similarity(name, 'laptop') > 0.3
ORDER BY sim DESC;

-- Word similarity
SELECT * FROM products WHERE name <<% 'laptop';

-- Similarity operator (%)
SELECT * FROM products WHERE name % 'laptop';

-- Strict word similarity (<<%)
SELECT * FROM products WHERE name <<% 'laptop computer';

-- Fuzzy match
SELECT * FROM products WHERE name % 'lpatop';  -- Typo-tolerant

-- LIKE with trigrams (faster)
CREATE INDEX idx_products_name_gin ON products USING GIN (name gin_trgm_ops);
SELECT * FROM products WHERE name ILIKE '%laptop%';
```

### Similarity Functions

```sql
-- similarity: 0 to 1 (higher = more similar)
SELECT similarity('database', 'database');
-- 1.0

SELECT similarity('database', 'data base');
-- 0.714

-- word_similarity: Word-level similarity
SELECT word_similarity('database system', 'database');
-- 0.5

-- strict_word_similarity: Strict word similarity
SELECT strict_word_similarity('database system', 'database');
-- 0.5

-- Similarity threshold
SET pg_trgm.similarity_threshold = 0.3;
SELECT * FROM products WHERE name % 'laptop';
```

---

## pg_stat_statements

### Installation

```sql
-- Install pg_stat_statements
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

-- Add to postgresql.conf
-- shared_preload_libraries = 'pg_stat_statements'

-- Restart PostgreSQL
```

### Query Statistics

```sql
-- Top queries by total time
SELECT
  queryid,
  LEFT(query, 100) AS query,
  calls,
  total_exec_time AS total_ms,
  mean_exec_time AS avg_ms,
  rows
FROM pg_stat_statements
ORDER BY total_exec_time DESC
LIMIT 10;

-- Top queries by calls
SELECT
  queryid,
  LEFT(query, 100) AS query,
  calls,
  total_exec_time AS total_ms,
  mean_exec_time AS avg_ms
FROM pg_stat_statements
ORDER BY calls DESC
LIMIT 10;

-- Top queries by rows returned
SELECT
  queryid,
  LEFT(query, 100) AS query,
  calls,
  rows,
  ROUND(rows::numeric / calls, 2) AS avg_rows
FROM pg_stat_statements
WHERE calls > 0
ORDER BY rows DESC
LIMIT 10;

-- Queries with high I/O
SELECT
  queryid,
  LEFT(query, 100) AS query,
  shared_blks_read,
  shared_blks_hit,
  ROUND(shared_blks_hit::numeric / NULLIF(shared_blks_hit + shared_blks_read, 0) * 100, 2) AS cache_hit_ratio
FROM pg_stat_statements
ORDER BY shared_blks_read DESC
LIMIT 10;
```

### Reset Statistics

```sql
-- Reset all statistics
SELECT pg_stat_statements_reset();

-- Reset for specific query
SELECT pg_stat_statements_reset(queryid);
```

---

## pg_partman

### Installation

```sql
-- Install pg_partman
CREATE EXTENSION IF NOT EXISTS pg_partman;

-- Add to postgresql.conf
-- shared_preload_libraries = 'pg_partman-bgw'

-- Restart PostgreSQL
```

### Automatic Partition Management

```sql
-- Create parent table
CREATE TABLE orders (
  id BIGSERIAL,
  user_id INTEGER,
  total DECIMAL(10,2),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create partitioned table
SELECT partman.create_parent(
  p_parent_table := 'public.orders',
  p_control := 'created_at',
  p_type := 'native',
  p_interval := 'monthly'
);

-- Check partition configuration
SELECT * FROM partman.part_config;

-- Create partitions manually
SELECT partman.run_maintenance();

-- Drop old partitions
SELECT partman.drop_partition_parent(
  p_parent_table := 'public.orders',
  p_retention := '6 months'
);
```

### Partition Configuration

```sql
-- Configure automatic partition creation
UPDATE partman.part_config
SET
  premake = 3,  -- Create 3 partitions ahead
  retention = '12 months',
  retention_keep_table = false,
  automatic_maintenance = 'on'
WHERE parent_table = 'public.orders';

-- Check partition status
SELECT
  parent_table,
  partition_schemaname,
  partition_tablename,
  partition_range
FROM partman.show_partitions('public.orders');
```

---

## pgcrypto

### Installation

```sql
-- Install pgcrypto
CREATE EXTENSION IF NOT EXISTS pgcrypto;
```

### Cryptographic Functions

```sql
-- Hashing
SELECT encode(digest('password', 'sha256'), 'hex');
SELECT encode(digest('password', 'md5'), 'hex');

-- Password hashing (bcrypt)
SELECT crypt('password', gen_salt('bf'));
SELECT crypt('password', '$2a$10$...') = '$2a$10$...' AS valid;

-- Random data
SELECT encode(gen_random_bytes(16), 'hex');
SELECT gen_random_uuid();

-- HMAC
SELECT encode(hmac('message', 'key', 'sha256'), 'hex');

-- Encryption/Decryption
-- Symmetric encryption
SELECT pgp_sym_encrypt('secret data', 'password');
SELECT pgp_sym_decrypt(encrypted_data, 'password');

-- Asymmetric encryption
SELECT pgp_pub_encrypt('data', dearmor(pubkey));
SELECT pgp_priv_decrypt(encrypted_data, dearmor(privkey), 'password');

-- Gen random UUID
SELECT gen_random_uuid();
-- 550e8400-e29b-41d4-a716-446655440000
```

---

## Other Extensions

### uuid-ossp

```sql
-- Install uuid-ossp
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Generate UUIDs
SELECT uuid_generate_v1();  -- Time-based
SELECT uuid_generate_v4();  -- Random
SELECT uuid_generate_v5(uuid_ns_dns(), 'example.com');  -- Name-based

-- UUID as primary key
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  username VARCHAR(50)
);
```

### hstore

```sql
-- Install hstore
CREATE EXTENSION IF NOT EXISTS hstore;

-- Create table with hstore
CREATE TABLE product_attributes (
  id SERIAL PRIMARY KEY,
  attributes HSTORE
);

-- Insert data
INSERT INTO product_attributes (attributes)
VALUES ('color => blue, size => large, weight => 1.5');

-- Query hstore
SELECT
  attributes->'color' AS color,
  attributes->'size' AS size
FROM product_attributes;

-- Contains
SELECT * FROM product_attributes
WHERE attributes @> 'color => blue';

-- Exists key
SELECT * FROM product_attributes
WHERE attributes ? 'color';

-- Delete key
UPDATE product_attributes
SET attributes = delete(attributes, 'weight');

-- Merge
UPDATE product_attributes
SET attributes = attributes || 'material => cotton'::hstore;
```

### tablefunc

```sql
-- Install tablefunc
CREATE EXTENSION IF NOT EXISTS tablefunc;

-- Cross-tab (pivot)
SELECT * FROM crosstab(
  'SELECT category, month, amount FROM sales ORDER BY 1, 2',
  'SELECT DISTINCT month FROM sales ORDER BY 1'
) AS ct(category TEXT, jan DECIMAL, feb DECIMAL, mar DECIMAL);

-- Normal distribution
SELECT normal_rand(1000, 0, 1);

-- Generate random data
SELECT
  generate_series(1, 10),
  normal_rand(1, 0, 1);
```

### fuzzystrmatch

```sql
-- Install fuzzystrmatch
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;

-- Levenshtein distance
SELECT levenshtein('hello', 'hallo');
-- 1

-- Soundex
SELECT soundex('hello');
-- H400

-- Metaphone
SELECT metaphone('hello', 4);
-- HL

-- Double metaphone
SELECT dmetaphone('hello');

-- Soundex similarity
SELECT soundex_diff('hello', 'hallo');
```

---

## Custom Extensions

### Creating Custom Functions

```sql
-- Create custom function
CREATE OR REPLACE FUNCTION calculate_distance(
  lat1 FLOAT,
  lon1 FLOAT,
  lat2 FLOAT,
  lon2 FLOAT
) RETURNS FLOAT AS $$
BEGIN
  RETURN ST_Distance(
    ST_SetSRID(ST_MakePoint(lon1, lat1), 4326)::geography,
    ST_SetSRID(ST_MakePoint(lon2, lat2), 4326)::geography
  );
END;
$$ LANGUAGE plpgsql;

-- Use function
SELECT calculate_distance(40.7484, -73.9857, 40.7831, -73.9712);
```

### Creating Custom Aggregates

```sql
-- Create custom aggregate
CREATE AGGREGATE jsonb_merge(jsonb) (
  SFUNC = 'jsonb_merge_func',
  STYPE = 'jsonb',
  INITCOND = '{}'
);

-- Create merge function
CREATE OR REPLACE FUNCTION jsonb_merge_func(a jsonb, b jsonb)
RETURNS jsonb AS $$
BEGIN
  RETURN a || b;
END;
$$ LANGUAGE plpgsql;

-- Use aggregate
SELECT jsonb_merge(profile) FROM user_profiles;
```

### Creating Custom Types

```sql
-- Create custom type
CREATE TYPE address AS (
  street VARCHAR(100),
  city VARCHAR(50),
  state VARCHAR(2),
  zip VARCHAR(10)
);

-- Use custom type
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  home_address address
);

-- Access custom type fields
SELECT
  name,
  (home_address).city,
  (home_address).state
FROM users;
```

---

## Best Practices

### Extension Selection

```sql
-- Essential extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";      -- UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";        -- Cryptography
CREATE EXTENSION IF NOT EXISTS "pg_trgm";         -- Fuzzy search

-- Performance extensions
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";  -- Query statistics
CREATE EXTENSION IF NOT EXISTS "pg_prewarm";          -- Buffer cache warming

-- Data type extensions
CREATE EXTENSION IF NOT EXISTS "hstore";          -- Key-value pairs
CREATE EXTENSION IF NOT EXISTS "tablefunc";       -- Cross-tab queries

-- Spatial extensions
CREATE EXTENSION IF NOT EXISTS "postgis";         -- Spatial data
```

### Extension Management

```sql
-- Document all installed extensions
-- Update extensions regularly
-- Test extension updates in development
-- Monitor extension performance impact

-- Check extension dependencies
SELECT
  e.extname,
  e.extversion,
  n.nspname AS schema
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
ORDER BY e.extname;
```

---

## Summary

| Extension | Purpose |
|-----------|---------|
| PostGIS | Spatial data |
| pg_trgm | Fuzzy text search |
| pg_stat_statements | Query statistics |
| pg_partman | Partition management |
| pgcrypto | Cryptography |
| uuid-ossp | UUID generation |
| hstore | Key-value pairs |
| tablefunc | Cross-tab queries |

## Next Steps

- [PostgreSQL Fundamentals](../fundamentals/) - PostgreSQL basics
- [PostgreSQL Queries](../queries/) - Advanced querying
- [PostgreSQL Optimization](../optimization/) - Performance tuning
