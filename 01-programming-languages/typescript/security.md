# TypeScript Security

## Type Safety

TypeScript prevents many common security issues at compile time.

### Prevent Injection

```typescript
// SQL injection prevention
async function getUser(id: number): Promise<User> {
  // Parameterized queries
  return db.query('SELECT * FROM users WHERE id = $1', [id]);
}

// XSS prevention
function escapeHtml(input: string): string {
  const div = document.createElement('div');
  div.textContent = input;
  return div.innerHTML;
}
```

### Type-Safe API Calls

```typescript
// Strongly typed responses prevent runtime errors
interface ApiResponse<T> {
  data: T;
  status: number;
}

async function fetchUser(id: string): Promise<ApiResponse<User>> {
  const response = await fetch(`/api/users/${id}`);
  return response.json();
}
```

## Strict Mode

Enable all strict checks for maximum safety.

```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "noUncheckedIndexedAccess": true,
    "exactOptionalPropertyTypes": true
  }
}
```

## Input Validation

```typescript
import { z } from 'zod';

const UserSchema = z.object({
  email: z.string().email(),
  password: z.string().min(8).max(100),
  age: z.number().int().min(0).max(150),
});

type UserInput = z.infer<typeof UserSchema>;

function validateUser(input: unknown): UserInput {
  return UserSchema.parse(input);
}
```

## Type Guards

```typescript
// Runtime type validation
function isString(val: unknown): val is string {
  return typeof val === "string";
}

function isUser(obj: unknown): obj is User {
  return (
    typeof obj === "object" &&
    obj !== null &&
    "id" in obj &&
    "email" in obj
  );
}
```

## Secrets Management

```typescript
// Never hardcode secrets
// Bad
const API_KEY = "sk-1234567890";

// Good - environment variables
const API_KEY = process.env.API_KEY;
if (!API_KEY) throw new Error("API_KEY not configured");
```

## CORS Configuration

```typescript
import cors from 'cors';

app.use(cors({
  origin: process.env.ALLOWED_ORIGINS?.split(',') || [],
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
}));
```

## Rate Limiting

```typescript
import rateLimit from 'express-rate-limit';

const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100,
  standardHeaders: true,
  legacyHeaders: false,
});

app.use('/api/', limiter);
```

## Content Security Policy

```typescript
app.use((req, res, next) => {
  res.setHeader(
    'Content-Security-Policy',
    "default-src 'self'; script-src 'self'"
  );
  next();
});
```
