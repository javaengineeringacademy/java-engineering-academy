-- MySQL Queries

-- Complex Joins
SELECT 
    u.name,
    o.order_date,
    p.product_name,
    oi.quantity
FROM users u
INNER JOIN orders o ON u.id = o.user_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id
WHERE o.order_date >= '2024-01-01';

-- Stored Procedures
DELIMITER //
CREATE PROCEDURE GetUserOrders(IN userId INT)
BEGIN
    SELECT * FROM orders WHERE user_id = userId;
END //
DELIMITER ;

-- Triggers
CREATE TRIGGER before_order_insert
BEFORE INSERT ON orders
FOR EACH ROW
SET NEW.created_at = NOW();

-- Views
CREATE VIEW active_users AS
SELECT id, name, email 
FROM users 
WHERE status = 'active';

-- Transactions
START TRANSACTION;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE FULLTEXT INDEX idx_products_search ON products(name, description);
