# Operators - Quiz

## Questions

### Q1: What is the result of `5 + 3 * 2`?
- A) 16
- B) 11
- C) 10
- D) 13

### Q2: Which operator is used for bitwise AND?
- A) `&&`
- B) `&`
- C) `|`
- D) `^`

### Q3: What does `x >>> 2` do?
- A) Right shift with sign extension
- B) Left shift
- C) Unsigned right shift (zero-fill)
- D) Divide by 4

### Q4: What is the ternary operator syntax?
- A) `condition ? trueVal : falseVal`
- B) `condition ? trueVal ? falseVal`
- C) `condition : trueVal ? falseVal`
- D) `condition -> trueVal : falseVal`

### Q5: What is the result of `10 % 3`?
- A) 3
- B) 1
- C) 0
- D) 3.33

### Q6: Which has the highest precedence?
- A) `&&`
- B) `|`
- C) `==`
- D) `*`

### Q7: What is the result of `5 == 5.0`?
- A) Compilation error
- B) `true`
- C) `false`
- D) Runtime exception

### Q8: What does `a ^= b` mean?
- A) a = a & b
- B) a = a | b
- C) a = a ^ b
- D) a = a ~ b

### Q9: What is short-circuit evaluation?
- A) All operands are evaluated
- B) Second operand skipped if first determines result
- C) Both operands always evaluated
- D) Operator overloading

### Q10: What is the result of `~0`?
- A) 0
- B) 1
- C) -1
- D) Compilation error

## Answers

1. **B** - Multiplication has higher precedence: `5 + (3 * 2) = 11`
2. **B** - `&` is bitwise AND; `&&` is logical AND
3. **C** - `>>>` performs unsigned right shift (fills with zeros)
4. **A** - Ternary: `boolean ? ifTrue : ifFalse`
5. **B** - Remainder: `10 / 3 = 3 remainder 1`
6. **D** - Multiplication/division have higher precedence than comparison/logical operators
7. **B** - int 5 is promoted to double 5.0; they are equal
8. **C** - XOR assignment: `a = a ^ b`
9. **B** - `&&` and `||` skip second operand when result is determined
10. **C** - `~` inverts all bits; `~0` = all 1s = -1 in two's complement
