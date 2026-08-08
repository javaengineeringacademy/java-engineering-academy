# EnumSet Internals

## Bit Vector Representation

```
EnumSet uses a bit vector to represent enum values.

For enum with N values:
  RegularEnumSet:  single long (64 bits max)
  JumboEnumSet:    long[] array (for > 64 values)
```

## How EnumSet Works

```java
enum Color { RED, GREEN, BLUE, YELLOW, PURPLE }

EnumSet<Color> set = EnumSet.of(Color.RED, Color.BLUE);
```

```
Bit positions map to enum ordinals:

  RED   = 0  →  bit 0 = 1
  GREEN = 1  →  bit 1 = 0
  BLUE  = 2  →  bit 2 = 1
  YELLOW= 3  →  bit 3 = 0
  PURPLE= 4  →  bit 4 = 0

  Binary:  0 0 1 0 1
           ↑ ↑ ↑ ↑ ↑
           4 3 2 1 0

  Value:   0b00101 = 5
```

## RegularEnumSet (≤ 64 values)

```java
class RegularEnumSet extends EnumSet {
    private long elements = 0L;
}
```

### add(element)

```
elements |= (1L << element.ordinal())

Example: add(BLUE) where BLUE.ordinal() = 2
  elements |= (1L << 2)
  elements |= 4
  elements = 0b00101
```

### remove(element)

```
elements &= ~(1L << element.ordinal())

Example: remove(RED) where RED.ordinal() = 0
  elements &= ~(1L << 0)
  elements &= ~1
  elements = 0b00100
```

### contains(element)

```
return (elements & (1L << element.ordinal())) != 0

Example: contains(BLUE) where BLUE.ordinal() = 2
  return (0b00101 & (1L << 2)) != 0
  return (0b00101 & 0b00100) != 0
  return 0b00100 != 0
  return true
```

## JumboEnumSet (> 64 values)

```java
class JumboEnumSet extends EnumSet {
    private long[] elements;
}
```

```
For enum with 128 values:
  elements[0]: bits 0-63
  elements[1]: bits 64-127

add(value at index 100):
  elements[1] |= (1L << (100 - 64))
  elements[1] |= (1L << 36)
```

## Memory Layout Diagram

```
EnumSet instance:
┌──────────────────────────────────┐
│  Object header     (12 bytes)    │
│  Class enumClass   (8 bytes)     │
│  long elements      (8 bytes)    │  ← RegularEnumSet
│  (or long[] ref)                  │  ← JumboEnumSet
└──────────────────────────────────┘

Bit vector layout (RegularEnumSet):
┌────┬────┬────┬────┬────┬────┬────┬────┐
│ 63 │ 62 │ 61 │... │  7 │  6 │  5 │  4 │  (high bits)
├────┼────┼────┼────┼────┼────┼────┼────┤
│  3 │  2 │  1 │  0 │    │    │    │    │  (low bits)
└────┴────┴────┴────┴────┴────┴────┴────┘
   ▲    ▲    ▲    ▲
   │    │    │    └── RED (ordinal 0)
   │    │    └────── GREEN (ordinal 1)
   │    └─────────── BLUE (ordinal 2)
   └──────────────── YELLOW (ordinal 3)
```

## Operation Flow

### add(element)

```
1. Check: element belongs to correct enum class
2. Set bit: elements |= (1L << ordinal)
3. Return true if bit was 0 (new), false if already 1

Time: O(1) — single bitwise operation
```

### remove(element)

```
1. Clear bit: elements &= ~(1L << ordinal)
2. Return true if bit was 1 (removed), false if already 0

Time: O(1) — single bitwise operation
```

### contains(element)

```
1. Check bit: (elements & (1L << ordinal)) != 0

Time: O(1) — single bitwise AND
```

### iterator()

```
1. Scan bits from low to high
2. For each set bit, yield corresponding enum value
3. Uses Integer.numberOfTrailingZeros() for speed

Time: O(n) where n = number of enum values
```

## EnumSet.of() Optimization

```java
EnumSet.of(Color.RED, Color.BLUE, Color.YELLOW)
```

```
1. All arguments are constant, so compile-time constant folding:
   elements = (1L << 0) | (1L << 2) | (1L << 3)
            = 1 | 4 | 8
            = 0b1101
            = 13

2. Single assignment, no iteration
```

## Thread Safety

EnumSet is **not** synchronized but is safe for read-only concurrent
access if no thread modifies it. For modifications, use
`Collections.synchronizedSet()`.

However, since EnumSet operations are atomic bitwise operations,
individual adds/removes are typically safe in practice.

## Key Implementation Details

1. **Zero per-element overhead** — No objects created for elements.
   Bits in a long/long[] represent membership.

2. **Enum ordinals are keys** — Element position determined by
   enum ordinal, not hash code.

3. **Type-safe** — Can only contain values of the specified enum type.
   Compile-time and runtime type checking.

4. **EnumSet.of() is fast** — Bitwise OR of precomputed masks.
   No iteration or hashing.

5. **EnumSet.allOf()** — Sets all bits: (1L << enumCount) - 1

6. **Complement operations** — EnumSet.complementOf() flips all bits.
   Very efficient for "all except" operations.

7. **No null elements** — EnumSet does not allow null values.
