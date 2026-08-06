# Operators in Go

Go supports standard operators for arithmetic, comparison, logical, and bitwise operations.

## Arithmetic Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `+` | Addition | `5 + 3 = 8` |
| `-` | Subtraction | `5 - 3 = 2` |
| `*` | Multiplication | `5 * 3 = 15` |
| `/` | Division | `5 / 3 = 1` |
| `%` | Modulus | `5 % 3 = 2` |

## Comparison Operators

| Operator | Description |
|----------|-------------|
| `==` | Equal to |
| `!=` | Not equal to |
| `>` | Greater than |
| `<` | Less than |
| `>=` | Greater or equal |
| `<=` | Less or equal |

## Logical Operators

| Operator | Description |
|----------|-------------|
| `&&` | Logical AND |
| `||` | Logical OR |
| `!` | Logical NOT |

## Bitwise Operators

| Operator | Description |
|----------|-------------|
| `&` | AND |
| `\|` | OR |
| `^` | XOR |
| `<<` | Left shift |
| `>>` | Right shift |

## Notes
- No implicit type conversions (even for numeric types)
- Operator overloading not supported
- Short-circuit evaluation for `&&` and `||`
