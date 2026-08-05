# PHP Security

## SQL Injection Prevention

Always use prepared statements with parameterized queries.

```php
// Bad - vulnerable to SQL injection
$query = "SELECT * FROM users WHERE id = $id";

// Good - prepared statement
$stmt = $pdo->prepare("SELECT * FROM users WHERE id = :id");
$stmt->execute(['id' => $id]);
$user = $stmt->fetch();

// Good - named parameters
$stmt = $pdo->prepare("SELECT * FROM users WHERE email = :email AND status = :status");
$stmt->execute(['email' => $email, 'status' => $status]);
```

Never concatenate user input directly into SQL queries.

## Cross-Site Scripting (XSS) Prevention

Escape all output sent to HTML.

```php
// Escape for HTML context
echo htmlspecialchars($userInput, ENT_QUOTES, 'UTF-8');

// Escape for JavaScript context
echo json_encode($data);

// Escape for URL context
echo urlencode($param);

// Escape for CSS context
echo escapeshellarg($value);
```

Use Content Security Policy headers to restrict script sources.

## Cross-Site Request Forgery (CSRF) Prevention

Generate and validate CSRF tokens for all state-changing operations.

```php
// Generate token
session_start();
$token = bin2hex(random_bytes(32));
$_SESSION['csrf_token'] = $token;

// Include in form
echo '<input type="hidden" name="csrf_token" value="' . $token . '">';

// Validate on submission
if (!hash_equals($_SESSION['csrf_token'], $_POST['csrf_token'])) {
    die('CSRF token validation failed');
}
```

## Password Hashing

Never store plain text passwords. Use `password_hash` and `password_verify`.

```php
// Hash password
$hash = password_hash($password, PASSWORD_BCRYPT, ['cost' => 12]);

// Verify password
if (password_verify($inputPassword, $storedHash)) {
    echo "Password correct";
}

// Check if hash needs rehashing
if (password_needs_rehash($storedHash, PASSWORD_BCRYPT, ['cost' => 12])) {
    // Rehash and update
}
```

## Input Validation

Validate and sanitize all user input.

```php
// Validate email
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    throw new InvalidArgumentException("Invalid email");
}

// Validate integer
$id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);

// Sanitize string
$name = filter_input(INPUT_POST, 'name', FILTER_SANITIZE_FULL_SPECIAL_CHARS);

// Whitelist validation
$allowedRoles = ['admin', 'editor', 'viewer'];
$role = in_array($inputRole, $allowedRoles) ? $inputRole : 'viewer';
```

## Security Headers

```php
header('X-Content-Type-Options: nosniff');
header('X-Frame-Options: DENY');
header('X-XSS-Protection: 1; mode=block');
header('Strict-Transport-Security: max-age=31536000; includeSubDomains');
header('Content-Security-Policy: default-src \'self\'');
header('Referrer-Policy: strict-origin-when-cross-origin');
```

## Secure Session Configuration

```php
session_start([
    'cookie_httponly' => true,
    'cookie_secure' => true,
    'cookie_samesite' => 'Strict',
    'use_strict_mode' => true,
    'gc_maxlifetime' => 1440,
]);
```
