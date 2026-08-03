-- Cassandra CQL Queries

-- Create Keyspace
CREATE KEYSPACE IF NOT EXISTS app
WITH replication = {
    'class': 'NetworkTopologyStrategy',
    'datacenter1': 3
};

-- Create Table
CREATE TABLE IF NOT EXISTS app.users (
    user_id UUID PRIMARY KEY,
    name TEXT,
    email TEXT,
    created_at TIMESTAMP
);

-- Insert
INSERT INTO app.users (user_id, name, email, created_at)
VALUES (uuid(), 'Alice', 'alice@example.com', toTimestamp(now()));

-- Read
SELECT * FROM app.users WHERE user_id = ?;

-- Create Table with Clustering
CREATE TABLE IF NOT EXISTS app.events (
    user_id UUID,
    event_time TIMESTAMP,
    event_type TEXT,
    event_data MAP<TEXT, TEXT>,
    PRIMARY KEY (user_id, event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);

-- Query by Partition
SELECT * FROM app.events WHERE user_id = ? LIMIT 10;

-- Batch
BEGIN BATCH
    INSERT INTO app.users (user_id, name, email) VALUES (uuid(), 'Bob', 'bob@example.com');
    INSERT INTO app.events (user_id, event_time, event_type) VALUES (uuid(), toTimestamp(now()), 'register');
APPLY BATCH;
