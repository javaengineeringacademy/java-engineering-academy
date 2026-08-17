# Wrapper Classes - Quiz

## Questions

### Q1: What is autoboxing?
- A) Converting primitive to wrapper manually
- B) Automatic conversion of primitive to wrapper class
- C) Converting wrapper to primitive
- D) Creating wrapper objects

### Q2: What is unboxing?
- A) Converting wrapper to primitive automatically
- B) Converting primitive to wrapper
- C) Removing wrappers from a collection
- D) Creating primitives

### Q3: What is the wrapper class for `int`?
- A) Int
- B) Integer
- C) Number
- D) Digits

### Q4: What is `Integer.parseInt("42")`?
- A) Returns Integer object
- B) Returns primitive int 42
- C) Returns "42"
- D) Compilation error

### Q5: What is `Integer.valueOf("42")`?
- A) Returns primitive int
- B) Returns Integer object (may be cached)
- C) Returns null
- D) Throws exception

### Q6: What is the Integer cache range?
- A) -128 to 127
- B) 0 to 255
- C) -256 to 255
- D) No cache

### Q7: Can wrapper classes be null?
- A) Yes, they are objects
- B) No
- C) Only Integer
- D) Only after Java 8

### Q8: What does `Integer.toBinaryString(10)` return?
- A) "10"
- B) "1010"
- C) "00001010"
- D) "A"

### Q9: What happens with `null` unboxing?
- A) Returns 0
- B) NullPointerException
- C) Returns null
- D) Compilation error

### Q10: What is the wrapper class for `boolean`?
- A) Boolean
- B) Bool
- C) Flag
- D) Bit

## Answers

1. **B** - Autoboxing: automatic primitive → wrapper conversion
2. **A** - Unboxing: automatic wrapper → primitive conversion
3. **B** - `Integer` wraps `int`
4. **B** - `parseInt()` returns primitive int
5. **B** - `valueOf()` returns Integer object (cached for -128 to 127)
6. **A** - Integer caches values from -128 to 127
7. **A** - Wrappers are objects and can be null
8. **B** - `toBinaryString(10)` returns "1010"
9. **B** - Unboxing null throws NullPointerException
10. **A** - `Boolean` wraps `boolean`
