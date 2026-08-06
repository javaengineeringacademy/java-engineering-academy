// TypeScript Mapped Types - Mapped types, keyof

// Basic mapped type
type Nullable<T> = {
  [P in keyof T]: T[P] | null;
};

interface User {
  id: number;
  name: string;
  email: string;
}

type NullableUser = Nullable<User>;
// All properties can now be null

// Mapped type with modifiers
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

type Optional<T> = {
  [P in keyof T]?: T[P];
};

// Mapped type with built-in utility types
type Partial<T> = {
  [P in keyof T]?: T[P];
};

type Required<T> = {
  [P in keyof T]-?: T[P];
};

// keyof operator
type UserKeys = keyof User; // "id" | "name" | "email"

// Using keyof for type-safe property access
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

let user: User = { id: 1, name: "Alice", email: "alice@example.com" };
let userName = getProperty(user, "name"); // string

// Mapped types with constraints
type StringOnly<T> = {
  [P in keyof T]: T[P] extends string ? T[P] : never;
};

type UserStrings = StringOnly<User>;
// { id: never; name: string; email: string }

// Extracting property types
type PropertyType<T, K extends keyof T> = T[K];

type UserNameType = PropertyType<User, "name">; // string

// Creating types from unions
type UserPropertyValues = User[keyof User];
// number | string

// Conditional mapped types
type PickByType<T, U> = {
  [K in keyof T as T[K] extends U ? K : never]: T[K];
};

type UserStringsOnly = PickByType<User, string>;
// { name: string; email: string }

// Recursive mapped types
type DeepReadonly<T> = {
  readonly [P in keyof T]: T[P] extends object ? DeepReadonly<T[P]> : T[P];
};

interface Config {
  api: {
    url: string;
    timeout: number;
  };
  db: {
    host: string;
    port: number;
  };
}

type ReadonlyConfig = DeepReadonly<Config>;

// Using mapped types for validation
type Validators<T> = {
  [K in keyof T]: (value: T[K]) => boolean;
};

let userValidators: Validators<User> = {
  id: (value) => typeof value === "number" && value > 0,
  name: (value) => typeof value === "string" && value.length > 0,
  email: (value) => typeof value === "string" && value.includes("@")
};

// Helper function using mapped types
function validate<T>(obj: T, validators: Validators<T>): boolean {
  for (const key in validators) {
    if (!validators[key](obj[key])) {
      return false;
    }
  }
  return true;
}

console.log(validate(user, userValidators));
console.log("Mapped types example running");
