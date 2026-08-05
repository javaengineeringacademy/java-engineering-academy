# JavaScript Security

## Content Security Policy (CSP)

```html
<meta http-equiv="Content-Security-Policy" content="
    default-src 'self';
    script-src 'self' https://cdn.example.com;
    style-src 'self' 'unsafe-inline';
    img-src 'self' data: https:;
    font-src 'self' https://fonts.googleapis.com;
">
```

```javascript
// Express.js CSP
const csp = require('helmet');
app.use(csp({
    directives: {
        defaultSrc: ["'self'"],
        scriptSrc: ["'self'", "trusted-cdn.com"],
        styleSrc: ["'self'", "'unsafe-inline'"]
    }
}));
```

## CORS

```javascript
// Express.js CORS
const cors = require('cors');

app.use(cors({
    origin: 'https://example.com',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization'],
    credentials: true,
    maxAge: 86400
}));
```

## XSS Prevention

```javascript
// Input sanitization
function sanitize(input) {
    const div = document.createElement('div');
    div.textContent = input;
    return div.innerHTML;
}

// React prevents XSS by default
// Use dangerouslySetInnerHTML carefully
<div dangerouslySetInnerHTML={{ __html: sanitizedHTML }} />

// DOMPurify
const DOMPurify = require('dompurify');
const clean = DOMPurify.sanitize(dirtyHTML);
```

## Input Validation

```javascript
// Validate email
function validateEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// Validate input length
function validateLength(input, min, max) {
    return input.length >= min && input.length <= max;
}

// Validate data types
function validateNumber(value) {
    return !isNaN(Number(value)) && isFinite(value);
}
```

## helmet.js

```javascript
const helmet = require('helmet');

app.use(helmet());

// Specific protections
app.use(helmet.contentSecurityPolicy());
app.use(helmet.crossOriginEmbedderPolicy());
app.use(helmet.crossOriginOpenerPolicy());
app.use(helmet.crossOriginResourcePolicy());
app.use(helmet.dnsPrefetchControl());
app.use(helmet.frameguard());
app.use(helmet.hidePoweredBy());
app.use(helmet.hsts());
app.use(helmet.ieNoOpen());
app.use(helmet.noSniff());
app.use(helmet.permittedCrossDomainPolicies());
app.use(helmet.referrerPolicy());
app.use(helmet.xssFilter());
```

## Authentication

```javascript
// JWT tokens
const jwt = require('jsonwebtoken');

const token = jwt.sign({ userId: 123 }, secret, {
    expiresIn: '1h',
    algorithm: 'RS256'
});

// Verify token
const decoded = jwt.verify(token, publicKey);

// Password hashing
const bcrypt = require('bcrypt');
const hash = await bcrypt.hash(password, 12);
const match = await bcrypt.compare(password, hash);
```

## Secure Storage

```javascript
// Never store secrets in localStorage
// Use httpOnly cookies for session data
res.cookie('sessionId', id, {
    httpOnly: true,
    secure: true,
    sameSite: 'strict',
    maxAge: 86400000
});

// Use sessionStorage for temporary data
sessionStorage.setItem('token', token);
```
