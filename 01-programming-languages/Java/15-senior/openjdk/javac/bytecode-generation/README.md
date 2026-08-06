# javac Bytecode Generation

Bytecode generation is the final phase of the javac compilation pipeline. The compiler translates the attributed and desugared AST into JVM bytecode instructions stored in `.class` files.

## What Bytecode Generation Does

1. Walks the AST
2. Translates each construct to equivalent bytecode instructions
3. Manages the operand stack and local variable table
4. Writes the class file format

## The `.class` File Format

```
ClassFile {
    u4 magic;                    // 0xCAFEBABE
    u2 minor_version;
    u2 major_version;
    u2 constant_pool_count;
    cp_info constant_pool[];
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    u2 interfaces_count;
    u2 interfaces[];
    u2 fields_count;
    field_info fields[];
    u2 methods_count;
    method_info methods[];
    u2 attributes_count;
    attribute_info attributes[];
}
```

## Method Code Generation

### Code Attribute

Each non-abstract, non-native method gets a `Code` attribute:

```
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;       // Maximum operand stack depth
    u2 max_locals;      // Number of local variables
    u4 code_length;
    u1 code[];          // Bytecode instructions
    u2 exception_table_length;
    exception_table_entry exception_table[];
    u2 attributes_count;
    attribute_info attributes[];
}
```

### Bytecode Instruction Set

Java bytecode is stack-based. Key instruction categories:

**Load/Store instructions:**
```
iload, iload_0..3     Load int from local variable
istore, istore_0..3   Store int to local variable
aload, aload_0..3     Load reference from local variable
astore, astore_0..3   Store reference to local variable
```

**Arithmetic:**
```
iadd, isub, imul, idiv    Integer arithmetic
ineg, ishl, ishr, iush unary/shift
ladd, lsub, ...            Long arithmetic
fadd, fsub, ...            Float arithmetic
dadd, dsub, ...            Double arithmetic
```

**Control flow:**
```
goto            Unconditional jump
ifeq, ifne      Compare with zero
if_icmpeq       Compare two ints
tableswitch     Switch statement (dense)
lookupswitch    Switch statement (sparse)
invokevirtual   Virtual method call
invokestatic    Static method call
invokeinterface Interface method call
invokespecial   Constructor, super call, private call
```

**Object operations:**
```
new             Create object
newarray        Create primitive array
anewarray       Create reference array
getfield        Read instance field
putfield        Write instance field
getstatic       Read static field
putstatic       Write static field
arraylength     Get array length
checkcast       Type cast
instanceof      Type check
```

## Code Generation Walkthrough

### Example: Simple Method

```java
public int add(int a, int b) {
    return a + b;
}
```

Bytecode:
```
Method: add(II)I
  max_stack: 2
  max_locals: 3  (this + a + b)
  code:
    iload_1       // push 'a'
    iload_2       // push 'b'
    iadd          // pop two, push sum
    ireturn       // return int
```

### Example: Loop

```java
int sum = 0;
for (int i = 0; i < n; i++) {
    sum += i;
}
```

Bytecode:
```
  iconst_0          // push 0
  istore_2          // sum = 0
  iconst_0          // push 0
  istore_3          // i = 0
Loop:
  iload_3           // push i
  iload_0           // push n
  if_icmpge Done    // if (i >= n) goto Done
  iload_2           // push sum
  iload_3           // push i
  iadd              // sum + i
  istore_2          // sum = sum + i
  iinc 3 1          // i++
  goto Loop
Done:
  ...
```

### Example: Method Invocation

```java
String name = obj.getName();
```

Bytecode:
```
  aload_1           // push 'obj'
  invokevirtual #1  // Method getName:()Ljava/lang/String;
  astore_2          // name = result
```

## Local Variable Table

The compiler assigns local variable slots:

```
this     → slot 0 (for instance methods)
param 1  → slot 1
param 2  → slot 2
local 1  → slot 3
local 2  → slot 4
long/double → takes 2 slots
```

Debuggers use the Local Variable Table attribute to display variable names and types.

## Exception Handling

The compiler generates exception table entries:

```
exception_table:
  from_pc  to_pc  handler_pc  catch_type
  0        10     15          Class java/lang/Exception
```

Each entry maps a bytecode range to an exception handler. The `catch_type` index points to the constant pool entry for the exception class.

## Stack Map Frames

Since Java 7, the class file includes Stack Map Frames for bytecode verification:

```
StackMapTable {
    frames[] {
        frame_type          // offset_delta
        verification_type[] // locals and stack types
    }
}
```

Each frame records the expected types on the operand stack and in local variables at a given bytecode offset. The verifier uses these to ensure type safety without dataflow analysis.

## String Concatenation

### Before Java 9

```java
"Hello " + name + "!"
// Compiled to: new StringBuilder().append("Hello ").append(name).append("!")
```

### Java 9+ (indy string concat)

```java
"Hello " + name + "!"
// Compiled to: invokedynamic (StringConcatFactory)
// More flexible, allows GC to optimize the intermediate string
```

## Key Source Files

| File | Purpose |
|------|---------|
| `com/sun/tools/javac/jvm/Gen.java` | AST to bytecode translation |
| `com/sun/tools/javac/jvm/Code.java` | Bytecode buffer |
| `com/sun/tools/javac/jvm/Items.java` | Stack items |
| `com/sun/tools/javac/jvm/Pool.java` | Constant pool |
| `com/sun/tools/javac/jvm/ClassWriter.java` | Class file output |
| `com/sun/tools/javac/jvm/ByteCodes.java` | Bytecode definitions |
| `com/sun/tools/javac/jvm/Target.java` | Target version specifics |
