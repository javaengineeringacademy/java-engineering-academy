# TypeScript Interfaces

## Overview
Interfaces define the shape of objects and provide type safety.

## Basic Interface
```typescript
interface User {
  name: string;
  age: number;
  email: string;
}
```

## Optional Properties
```typescript
interface Product {
  id: number;
  name: string;
  description?: string; // Optional
}
```

## Extending Interfaces
```typescript
interface Employee extends User {
  employeeId: number;
  department: string;
}

// Multiple inheritance
interface Contact {
  phone: string;
}

interface Person extends User, Contact {
  occupation: string;
}
```

## Readonly Properties
```typescript
interface Config {
  readonly apiUrl: string;
  readonly timeout: number;
}
```

## Function Types
```typescript
interface SearchFunc {
  (source: string, subString: string): boolean;
}
```

## Indexable Types
```typescript
interface StringArray {
  [index: number]: string;
}
```

## Key Takeaways
1. Use interfaces for object shapes
2. Mark non-essential properties as optional
3. Use extends for inheritance
4. Use readonly for immutable properties