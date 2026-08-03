-- PostgreSQL Advanced Queries

-- Window Functions
SELECT 
    name,
    department,
    salary,
    ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rank
FROM employees;

-- CTEs
WITH dept_stats AS (
    SELECT 
        department,
        AVG(salary) as avg_salary,
        COUNT(*) as emp_count
    FROM employees
    GROUP BY department
)
SELECT * FROM dept_stats WHERE avg_salary > 50000;

-- JSON Operations
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    data JSONB NOT NULL
);

INSERT INTO events (data) VALUES 
    ('{"type": "click", "page": "home"}'),
    ('{"type": "view", "page": "product"}');

SELECT data->>'type' as event_type FROM events;

-- Full Text Search
CREATE INDEX idx_search ON articles USING GIN(to_tsvector('english', title || ' ' || content));

SELECT * FROM articles WHERE to_tsvector('english', title || ' ' || content) @@ to_tsquery('english', 'java & programming');

-- Array Operations
CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    name TEXT,
    tags TEXT[]
);

INSERT INTO tags (name, tags) VALUES ('Post 1', ARRAY['java', 'sql', 'database']);

SELECT * WHERE 'java' = ANY(tags);
