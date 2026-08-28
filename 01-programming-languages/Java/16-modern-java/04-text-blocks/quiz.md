# Text Blocks Quiz

## Questions

1. What is the syntax for starting a text block?
2. What does `\s` do in a text block?
3. How do you prevent line terminators from being included?
4. What is the closing delimiter called?
5. Can you use escape sequences in text blocks?
6. How is indentation handled in text blocks?
7. Can text blocks be used with `String.format()`?
8. What happens if the closing `"""` is not on its own line?
9. Can you concatenate text blocks with regular strings?
10. What is the type of a text block?

## Answers

1. **Three double quotes followed by an optional newline:** `"""`
2. **Preserves a single space** at the end of the line.
3. **Add `\` at the end of the line** (line terminator escape).
4. **Closing delimiter** or **terminator**.
5. **Yes.** All standard escape sequences work.
6. **Leading whitespace is stripped** based on the closing `"""` position.
7. **Yes,** or use `.formatted()` method.
8. **Compilation error.** The closing `"""` must be on its own line.
9. **Yes,** using `+` operator.
10. **String.** Text blocks are just String literals.
