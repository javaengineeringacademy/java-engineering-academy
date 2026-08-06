# javac — The Java Compiler

javac is the reference Java compiler. It compiles `.java` source files into `.class` bytecode files. javac is written entirely in Java, making it self-hosting — it can compile itself.

## What javac Does

1. Reads `.java` source files
2. Performs lexical analysis and parsing
3. Builds an Abstract Syntax Tree (AST)
4. Runs annotation processors
5. Performs type checking and semantic analysis
6. Generates `.class` files containing JVM bytecode

## Compilation Pipeline

```
Source Code (.java)
    ↓
1. Lexical Analysis (Scanner)
    ↓
2. Parsing (Parser)
    ↓
3. Abstract Syntax Tree (AST)
    ↓
4. Annotation Processing (APT)
    ↓
5. Type Checking & Attribution
    ↓
6. Desugaring
    ↓
7. Bytecode Generation (Code Generator)
    ↓
Bytecode (.class)
```

### Stage 1: Lexical Analysis

The scanner reads source characters and produces tokens:

```
"int x = 42;" → [int] [IDENT:x] [ASSIGN] [INT_LIT:42] [SEMI]
```

Token types include keywords, identifiers, literals, operators, and separators.

### Stage 2: Parsing

The parser organizes tokens into a tree structure based on the Java grammar:

```
ExpressionStatement
├── VariableDeclaration
│   ├── Type: int
│   ├── Name: x
│   └── Initializer: 42
```

javac uses a recursive descent parser.

### Stage 3: Abstract Syntax Tree (AST)

The AST is the central data structure. Each node represents a syntactic construct:

```
JCCompilationUnit
├── PackageDecl
├── ImportList
└── ClassDecl
    ├── Modifiers
    ├── Name: "MyClass"
    └── Body
        └── MethodDecl
            ├── Name: "main"
            ├── ReturnType: void
            └── Body
                └── Exec
                    └── VarDecl
                        ├── Name: "args"
                        └── Type: String[]
```

### Stage 4: Annotation Processing

Before type-checking, javac runs annotation processors:

1. Scan for annotations in the source
2. Discover registered processors (via `META-INF/services`)
3. Run processors that claim relevant annotations
4. Generate new source files or resources
5. Repeat until no new files are generated

### Stage 5: Type Checking & Attribution

The compiler resolves all types, performs overload resolution, and checks semantics:

- Resolve identifiers to their declarations
- Check type compatibility
- Verify method signatures
- Check access control (public, private, etc.)
- Report errors and warnings

### Stage 6: Desugaring

Syntactic sugar is translated to simpler constructs:

- Enhanced for-loops → iterator-based loops
- Autoboxing → explicit `Integer.valueOf()` / `.intValue()`
- Diamond operator → explicit type arguments
- String concatenation → `StringBuilder` (or `invokedynamic` in Java 9+)
- Records → hidden fields, constructors, accessors
- Pattern matching → explicit `instanceof` + cast

### Stage 7: Bytecode Generation

The final AST is translated to bytecode:

```
AST Node → Bytecode Instruction
MethodDecl → Code attribute
Expression → Stack-based instructions
Local variable → Local variable table entry
```

## Using javac

### Basic Usage

```bash
# Compile a single file
javac HelloWorld.java

# Compile multiple files
javac *.java

# Specify output directory
javac -d out/ src/*.java
```

### Classpath

```bash
# Set classpath
javac -cp lib/*.jar src/Main.java

# Add to default classpath
javac -cp $CLASSPATH:lib/mylib.jar src/Main.java
```

### Source and Target Version

```bash
# Compile for Java 17
javac --source 17 --target 17 Main.java

# Enable preview features
javac --enable-preview --source 21 Main.java
```

### Annotation Processing

```bash
# Enable annotation processing
javac -processor com.example.MyProcessor src/*.java

# Disable annotation processing
javac -proc:none src/*.java

# Specify processor path
javac -processorpath lib/processors.jar src/*.java
```

## Compiler Flags

### General

| Flag | Description |
|------|-------------|
| `-d <dir>` | Output directory for `.class` files |
| `-source <version>` | Source version compatibility |
| `-target <version>` | Target version compatibility |
| `-cp <path>` | Classpath |
| `-classpath <path>` | Alias for `-cp` |
| `-verbose` | Output messages about what javac is doing |

### Warnings and Errors

| Flag | Description |
|------|-------------|
| `-Xlint:all` | Enable all warnings |
| `-Xlint:none` | Disable all warnings |
| `-Xlint:deprecation` | Warn about deprecated API usage |
| `-Xlint:unchecked` | Warn about unchecked operations |
| `-Werror` | Treat warnings as errors |
| `-nowarn` | Suppress all warnings |

### Debugging

| Flag | Description |
|------|-------------|
| `-g` | Generate all debugging info |
| `-g:none` | Generate no debugging info |
| `-g:{lines,vars,source}` | Generate specific debug info |
| `-Xlint:path` | Warn about missing classpath entries |

### Advanced

| Flag | Description |
|------|-------------|
| `-implicit:class` | Generate class files for implicitly loaded classes |
| `-proc:none` | Disable annotation processing |
| `-parameters` | Store formal parameter names in class files |
| `-release <version>` | Compile for a specific release version |

## The Java Compiler API

Java provides programmatic access to the compiler via `javax.tools`:

```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects("Main.java");
JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, files);

boolean success = task.call();
```

This API is used by build tools (Maven, Gradle), IDEs, and annotation processors.

## Key Source Files

| File | Purpose |
|------|---------|
| `src/jdk.compiler/share/classes/com/sun/tools/javac/` | Main compiler source |
| `com/sun/tools/javac/main/Main.java` | Entry point |
| `com/sun/tools/javac/parser/Parser.java` | Recursive descent parser |
| `com/sun/tools/javac/tree/Tree.java` | AST node definitions |
| `com/sun/tools/javac/comp/Attr.java` | Type checking |
| `com/sun/tools/javac/code/Type.java` | Type system |
| `com/sun/tools/javac/jvm/Gen.java` | Bytecode generation |
