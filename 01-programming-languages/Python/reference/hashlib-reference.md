# Python hashlib Reference

## What is hashlib?

The hashlib module provides a common interface to many secure hash and message digest algorithms, including SHA256, MD5, and others. It's used for data integrity, password hashing, and digital signatures.

## Why does hashlib matter?

Understanding hashlib helps you:
- Verify data integrity
- Hash passwords securely
- Create digital signatures
- Implement checksums

---

## 1. Basic Hashing

```python
import hashlib

# MD5
md5 = hashlib.md5(b"Hello, World!")
print(md5.hexdigest())  # 65a8e27d8879283831b664bd8b7f0ad4

# SHA256
sha256 = hashlib.sha256(b"Hello, World!")
print(sha256.hexdigest())  # dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f

# SHA512
sha512 = hashlib.sha512(b"Hello, World!")
print(sha512.hexdigest())
```

---

## 2. Streaming Hashing

```python
import hashlib

# Hash large files
sha256 = hashlib.sha256()
with open('large_file.bin', 'rb') as f:
    for chunk in iter(lambda: f.read(4096), b''):
        sha256.update(chunk)
print(sha256.hexdigest())
```

---

## 3. Password Hashing

```python
import hashlib
import os

# Hash password with salt
def hash_password(password):
    salt = os.urandom(32)
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt,
        100000
    )
    return salt + key

# Verify password
def verify_password(password, stored):
    salt = stored[:32]
    stored_key = stored[32:]
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt,
        100000
    )
    return key == stored_key
```

---

## 4. HMAC

```python
import hmac
import hashlib

# Create HMAC
message = b"Hello, World!"
key = b"secret_key"
hmac_obj = hmac.new(key, message, hashlib.sha256)
print(hmac_obj.hexdigest())

# Verify HMAC
def verify_hmac(key, message, expected):
    computed = hmac.new(key, message, hashlib.sha256).hexdigest()
    return hmac.compare_digest(computed, expected)
```

---

## 5. Available Algorithms

```python
import hashlib

# List available algorithms
print(hashlib.algorithms_available)

# List guaranteed algorithms
print(hashlib.algorithms_guaranteed)
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **md5** | MD5 hash | `hashlib.md5(b"data")` |
| **sha1** | SHA1 hash | `hashlib.sha1(b"data")` |
| **sha256** | SHA256 hash | `hashlib.sha256(b"data")` |
| **sha512** | SHA512 hash | `hashlib.sha512(b"data")` |
| **pbkdf2_hmac** | Password hashing | `hashlib.pbkdf2_hmac(...)` |
| **update** | Add data | `hash_obj.update(data)` |
| **hexdigest** | Get hex digest | `hash_obj.hexdigest()` |
| **digest** | Get binary digest | `hash_obj.digest()` |

---

## Common Mistakes

### 1. Using MD5 for Passwords

```python
# WRONG (insecure)
hashlib.md5(password.encode()).hexdigest()

# RIGHT
hashlib.pbkdf2_hmac('sha256', password.encode(), salt, 100000)
```

### 2. Not Using Salt

```python
# WRONG (rainbow table attack)
hashlib.sha256(password.encode()).hexdigest()

# RIGHT
salt = os.urandom(32)
key = hashlib.pbkdf2_hmac('sha256', password.encode(), salt, 100000)
```

### 3. Not Using hmac.compare_digest

```python
# WRONG (timing attack)
if computed_hash == expected_hash:

# RIGHT
if hmac.compare_digest(computed_hash, expected_hash):
```

---

## Production Notes

1. **Use SHA256 or stronger** - MD5 and SHA1 are broken
2. **Use pbkdf2_hmac for passwords** - Or bcrypt/argon2
3. **Always use salt** - Prevent rainbow table attacks
4. **Use hmac.compare_digest** - Prevent timing attacks
5. **Use streaming for large files** - Don't load entire file
6. **Store salt with hash** - Needed for verification
7. **Use enough iterations** - 100,000+ for pbkdf2
8. **Consider using passlib** - Higher-level password hashing
9. **Don't roll your own crypto** - Use proven libraries
10. **Verify hash length** - Ensure it matches expected

---

## Further Reading

- Python documentation on hashlib module
- Python documentation on hmac module
- OWASP password storage guidelines
