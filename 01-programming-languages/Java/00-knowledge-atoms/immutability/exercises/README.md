# Immutability Exercises

## Exercise 1: Immutable Person

Create an immutable `Person` class with:
- Fields: `name` (String), `age` (int), `email` (String)
- All fields must be final
- No setter methods
- A method `withAge(int newAge)` that returns a new Person with the updated age
- Proper `equals()`, `hashCode()`, and `toString()` methods

## Exercise 2: Immutable Bank Account

Create an immutable `BankAccount` class with:
- Fields: `accountId` (String), `balance` (BigDecimal)
- No setter methods
- A method `deposit(BigDecimal amount)` that returns a new BankAccount
- A method `withdraw(BigDecimal amount)` that returns a new BankAccount (throws if insufficient funds)
- Proper `equals()`, `hashCode()`, and `toString()` methods

## Exercise 3: Defensive Copy for Mutable Collection

Create a class `EventLog` that:
- Stores a list of event strings immutably
- Accepts a mutable list in the constructor but makes a defensive copy
- Returns an unmodifiable view of events via getter
- Includes a method `addEvent(String event)` that returns a new EventLog

## Exercise 4: Record-Based Value Objects

Create three records:
- `Coordinate(double latitude, double longitude)` with validation in compact constructor
- `Color(int red, int green, int blue)` with a method `brighter()` that returns a new Color
- `Range(int min, int max)` with a method `contains(int value)` and `expand(int delta)`

## Exercise 5: Immutable Builder Pattern

Create an immutable `HttpResponse` class using the builder pattern:
- Fields: `statusCode` (int), `headers` (Map<String, String>), `body` (String)
- Use a static `builder()` method
- The builder collects headers into an unmodifiable map when `build()` is called
- The final object must be fully immutable
