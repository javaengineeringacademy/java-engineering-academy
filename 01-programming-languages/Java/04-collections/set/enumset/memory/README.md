# EnumSet Memory Usage

## Per-Instance Overhead

```
RegularEnumSet (≤ 64 values):
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Class<? extends Enum> enumClass   8B  │
│  long elements                   8B   │
│  Padding                         4B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 32B  │
└────────────────────────────────────────┘

JumboEnumSet (> 64 values):
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Class<? extends Enum> enumClass   8B  │
│  long[] elements ref              8B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 28B  │
└────────────────────────────────────────┘
```

## Per-Element Cost

```
RegularEnumSet:
  0 bytes per element! (bits in long)

JumboEnumSet:
  0 bytes per element! (bits in long[])
  + long[] array overhead: 16 bytes header
  + 8 bytes per 64 enum values
```

## Scaling: 1000 Enum Values

```
EnumSet<MyEnum> set = EnumSet.allOf(MyEnum.class);

RegularEnumSet instance:      32 bytes
long[] elements array:
  Header:                    16 bytes
  16 longs (128 bytes):    128 bytes
  ─────────────────────────────────
  Array total:             144 bytes

─────────────────────────────────────────
TOTAL:                     176 bytes ≈ 0.2 KB
```

## Comparison: EnumSet vs HashSet (1000 Enum Values)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │   EnumSet    │   HashSet    │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     32 B     │     20 B     │
│ Structure        │    144 B     │   8,208 B    │
│ Nodes            │      None    │  44,000 B    │
│ Elements         │      None    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │ ≈ 176 B     │   ≈ 67 KB   │
│ Per-element      │  0 B         │    44 B      │
│ Memory ratio     │    1x        │   380x       │
└──────────────────┴──────────────┴──────────────┘
```

## Memory Comparison Visual

```
EnumSet (1000 values):
┌────────────────────┐
│ Instance: 32 bytes │  ← Entire EnumSet
│ Array: 144 bytes   │
└────────────────────┘
  Total: 176 bytes

HashSet (1000 values):
┌──────────────────────────────────────────────┐
│ Instance: 20 bytes                           │
│ HashMap: 48 bytes                            │
│ Table: 8,208 bytes                           │
│ Nodes: 44,000 bytes                          │
│ Elements: 16,000 bytes                       │
│                                              │
│ ████ 67 KB ████                              │
└──────────────────────────────────────────────┘
```

## JumboEnumSet Memory

```
For enum with 200 values:

JumboEnumSet instance:       28 bytes
long[] elements:
  Header:                   16 bytes
  4 longs (32 bytes):      32 bytes
  ─────────────────────────────────
  Array total:             48 bytes

─────────────────────────────────────────
TOTAL:                      76 bytes ≈ 0.1 KB
```

## Empty EnumSet

```
EnumSet.noneOf(MyEnum.class)
  Instance:           32 bytes
  elements:           0 (no bits set)
  Total:              32 bytes
```

## Why EnumSet Is Incredibly Efficient

```
1. No objects per element:
   HashSet: Node + Integer = 44 + 16 = 60 bytes per element
   EnumSet: 0 bytes per element (bits in long)

2. No hashing overhead:
   HashSet: hash computation, bucket allocation, collision handling
   EnumSet: direct bit manipulation

3. No null checks:
   HashSet: null key handling, PRESENT dummy object
   EnumSet: ordinal directly maps to bit position

4. Cache-friendly:
   HashSet: scattered Node objects across heap
   EnumSet: single long or small long[] array

5. Bitwise operations are atomic:
   add/remove/contains are single CPU instructions
```

## Memory Optimization

```
1. Use EnumSet for enum types:
   380x more memory efficient than HashSet

2. Use EnumSet.allOf() and EnumSet.complementOf():
   Very fast set operations using bitwise NOT

3. Use EnumSet.range(from, to):
   Precomputed bit masks for range operations

4. Never use HashSet for enums:
   Massive memory waste for no benefit
```
