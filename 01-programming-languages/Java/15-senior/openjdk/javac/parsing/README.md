# javac Parsing — Lexical Analysis and Tokenization

Parsing is the first phase of the javac compilation pipeline. It transforms raw source code into a structured representation (AST).

## Lexical Analysis (Scanner)

The scanner reads characters from the source file and groups them into tokens. Each token represents a meaningful unit of the language.

### Token Types

| Category | Examples |
|----------|---------|
| Keywords | `class`, `public`, `static`, `void`, `int`, `if`, `while` |
| Identifiers | `myVariable`, `calculateSum`, `_` |
| Integer literals | `42`, `0xFF`, `0b1010`, `1_000` |
| Floating-point | `3.14`, `2.0f`, `1e10` |
| Character literals | `'a'`, `'\n'`, `'\u0041'` |
| String literals | `"hello"`, `"line1\nline2"` |
| Operators | `+`, `-`, `*`, `/`, `==`, `!=`, `&&`, `||` |
| Separators | `(`, `)`, `{`, `}`, `[`, `]`, `;`, `,`, `.` |
| Comments | `// ...`, `/* ... */`, `/** ... */` |

### Unicode Handling

Java source is Unicode. The scanner handles:

- Unicode escapes: `\u0041` → `A`
- Normalization of line breaks
- Character classification for identifiers (Unicode letters, digits, underscores, `$`)

### Token Structure

Each token carries:

```java
class Token {
    TokenKind kind;     // What type of token
    Name name;          // Interned string for identifiers/literals
    int pos;            // Start position in source
    int endPos;         // End position in source
    int line;           // Line number
}
```

### Keywords

Java keywords are fixed tokens, not identifiers:

```
abstract  assert    boolean   break     byte      case
catch     char      class     const     continue  default
do        double    else      enum      extends   final
finally    float     for       goto      if        implements
import    instanceof int       interface long      native
new       package   private   protected public    return
short     static    strictfp  super     switch    synchronized
this      throw     throws    transient try       void
volatile  while
```

Contextual keywords (not reserved): `var`, `yield`, `record`, `sealed`, `permits`, `non-sealed`.

### Literals

```java
// Integer
42          // decimal
0x2A        // hexadecimal
052         // octal
0b101010    // binary
1_000_000   // underscores (ignored)

// Floating-point
3.14        // double
3.14f       // float
1e10        // scientific notation
0x1.8p1     // hex float

// Character
'a'
'\n'        // escape
'\u0041'    // unicode

// Text Block (Java 13+)
"""
  line 1
  line 2
  """
```

## Parser

The parser consumes tokens and builds the Abstract Syntax Tree. javac uses a **recursive descent parser**.

### Grammar Structure

Java's grammar is divided into:

1. **Compilation unit**: Package declaration, imports, type declarations
2. **Type declarations**: Classes, interfaces, enums, records
3. **Member declarations**: Fields, methods, constructors
4. **Statements**: Blocks, if/else, loops, switches, try/catch
5. **Expressions**: Arithmetic, relational, method calls, assignments

### Parsing Examples

**Variable declaration:**
```
Tokens: [int] [IDENT:x] [ASSIGN] [INT_LIT:42] [SEMI]
AST: JCVariableDecl
  ├── mods: []
  ├── vartype: JCPrimitiveTypeTree (int)
  ├── name: "x"
  └── init: JCLiteral (42)
```

**Method call:**
```
Tokens: [IDENT:obj] [DOT] [IDENT:method] [LPAREN] [RPAREN] [SEMI]
AST: JCExpressionStatement
  └── JCMethodInvocation
      ├── methodselect: JCFieldAccess
      │   ├── selected: JCIdent ("obj")
      │   └── name: "method"
      └── args: []
```

### Operator Precedence

The parser respects Java's operator precedence:

```
1.  () [] .           (postfix)
2.  ++ -- + - ~ !     (unary)
3.  * / %             (multiplicative)
4.  + -               (additive)
5.  << >> >>>         (shift)
6.  < > <= >= instanceof (relational)
7.  == !=             (equality)
8.  &                 (bitwise AND)
9.  ^                 (bitwise XOR)
10. |                 (bitwise OR)
11. &&                (logical AND)
12. ||                (logical OR)
13. ?:                (ternary)
14. = += -= ...       (assignment)
```

### Error Recovery

The parser includes error recovery to continue after syntax errors:

- Semicolon insertion on missing semicolons
- Synchronization at statement boundaries
- Bracket matching to recover from mismatched braces
- Reporting multiple errors per compilation

### Source Positions

The parser tracks source positions for every AST node:

```java
// Every tree node has position info
tree.pos = pos;          // Start position
tree.endpos = endPos;    // End position
```

This enables precise error messages and IDE features (go-to-definition, refactoring).

## Key Source Files

| File | Purpose |
|------|---------|
| `com/sun/tools/javac/parser/Scanner.java` | Lexical analyzer |
| `com/sun/tools/javac/parser/Tokenizer.java` | Token management |
| `com/sun/tools/javac/parser/JavacParser.java` | Recursive descent parser |
| `com/sun/tools/javac/parser/Token.java` | Token definition |
| `com/sun/tools/javac/parser/Tokens.java` | Token kinds enum |
