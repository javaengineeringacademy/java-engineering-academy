// TypeScript Utility Types - Partial, Required, Pick, Omit, Record

// Original interface
interface User {
  id: number;
  name: string;
  email: string;
  age: number;
  active: boolean;
}

// Partial - All properties optional
function updateUser(id: number, updates: Partial<User>): User {
  // Simulate fetching user
  const user: User = { id, name: "Alice", email: "alice@example.com", age: 30, active: true };
  return { ...user, ...updates };
}

let updated = updateUser(1, { name: "Bob" });

// Required - All properties required
interface Config {
  apiUrl?: string;
  timeout?: number;
  retries?: number;
}

type RequiredConfig = Required<Config>;

// Pick - Select specific properties
type UserBasic = Pick<User, "id" | "name" | "email">;

let basicUser: UserBasic = {
  id: 1,
  name: "Alice",
  email: "alice@example.com"
};

// Omit - Remove specific properties
type UserWithoutEmail = Omit<User, "email">;

let noEmailUser: UserWithoutEmail = {
  id: 1,
  name: "Alice",
  age: 30,
  active: true
};

// Record - Create object type with specific keys
type Roles = "admin" | "user" | "guest";
type UserRoles = Record<Roles, string[]>;

let roles: UserRoles = {
  admin: ["read", "write", "delete"],
  user: ["read", "write"],
  guest: ["read"]
};

// Record with number keys
type NumberDict = Record<number, string>;
let dict: NumberDict = { 1: "one", 2: "two" };

// Readonly - Make all properties readonly
type ReadonlyUser = Readonly<User>;
// ReadonlyUser cannot be modified after creation

// Exclude - Remove types from union
type StringOrNumber = string | number | boolean;
type OnlyStringOrNumber = Exclude<StringOrNumber, boolean>;

// Extract - Extract types from union
type Mixed = string | number | boolean;
type OnlyNumbers = Extract<Mixed, number>;

// NonNullable - Remove null and undefined
type MaybeString = string | null | undefined;
type DefinitelyString = NonNullable<MaybeString>;

// ReturnType - Get function return type
function createUser() {
  return { id: 1, name: "Alice", email: "alice@example.com" };
}
type NewUser = ReturnType<typeof createUser>;

// Parameters - Get function parameter types
function processUser(user: User, verbose: boolean): void {}
type ProcessParams = Parameters<typeof processUser>;

console.log("Utility types example running");
