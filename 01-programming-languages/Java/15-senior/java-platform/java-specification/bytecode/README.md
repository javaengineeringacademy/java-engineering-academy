# Java Bytecode

Java bytecode is the instruction set for the Java Virtual Machine (JVM). Understanding bytecode is essential for performance tuning, tool development, and deep Java knowledge.

## Bytecode Format

### Class File Structure

Every `.class` file has this structure:

```
ClassFile {
    u4 magic;                    // 0xCAFEBABE
    u2 minor_version;            // e.g., 0
    u2 major_version;            // e.g., 65 (Java 21)
    u2 constant_pool_count;
    cp_info constant_pool[constant_pool_count-1];
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    u2 interfaces_count;
    u2 interfaces[interfaces_count];
    u2 fields_count;
    field_info fields[fields_count];
    u2 methods_count;
    method_info methods[methods_count];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
```

### Magic Number

All Java class files start with `0xCAFEBABE` (4 bytes).

### Version Numbers

- **Major version**: Indicates Java version
  - 45 = Java 1.1
  - 52 = Java 8
  - 55 = Java 11
  - 61 = Java 17
  - 65 = Java 21

## Common Opcodes

### Constants

| Opcode | Description | Example |
|--------|-------------|---------|
| `iconst_m1` | Push int -1 | `iconst_m1` |
| `iconst_0` | Push int 0 | `iconst_0` |
| `iconst_1` | Push int 1 | `iconst_1` |
| `bipush` | Push byte as int | `bipush 100` |
| `sipush` | Push short as int | `sipush 1000` |
| `ldc` | Push constant from pool | `ldc "Hello"` |
| `ldc_w` | Push constant (wide index) | `ldc_w` |
| `lconst_0` | Push long 0 | `lconst_0` |
| `fconst_0` | Push float 0.0 | `fconst_0` |
| `dconst_0` | Push double 0.0 | `dconst_0` |

### Loads

| Opcode | Description | Example |
|--------|-------------|---------|
| `iload` | Load int from local | `iload 1` |
| `iload_0` | Load int from local 0 | `iload_0` |
| `lload` | Load long from local | `lload 1` |
| `fload` | Load float from local | `fload 1` |
| `dload` | Load double from local | `dload 1` |
| `aload` | Load reference from local | `aload 1` |
| `aload_0` | Load 'this' reference | `aload_0` |
| `iaload` | Load int from array | `iaload` |
| `laload` | Load long from array | `laload` |
| `faload` | Load float from array | `faload` |
| `daload` | Load double from array | `daload` |
| `aaload` | Load reference from array | `aaload` |
| `baload` | Load byte/boolean from array | `baload` |

### Stores

| Opcode | Description | Example |
|--------|-------------|---------|
| `istore` | Store int to local | `istore 1` |
| `istore_0` | Store int to local 0 | `istore_0` |
| `lstore` | Store long to local | `lstore 1` |
| `fstore` | Store float to local | `fstore 1` |
| `dstore` | Store double to local | `dstore 1` |
| `astore` | Store reference to local | `astore 1` |
| `iastore` | Store int to array | `iastore` |
| `lastore` | Store long to array | `lastore` |
| `fastore` | Store float to array | `fastore` |
| `dastore` | Store double to array | `dastore` |
| `aastore` | Store reference to array | `aastore` |
| `bastore` | Store byte/boolean to array | `bastore` |

### Arithmetic

| Opcode | Description | Example |
|--------|-------------|---------|
| `iadd` | int addition | `iadd` |
| `isub` | int subtraction | `isub` |
| `imul` | int multiplication | `imul` |
| `idiv` | int division | `idiv` |
| `irem` | int remainder | `irem` |
| `ineg` | int negate | `ineg` |
| `ishl` | int shift left | `ishl` |
| `ishr` | int shift right | `ishr` |
| `iushr` | int unsigned shift right | `iushr` |
| `iand` | int bitwise AND | `iand` |
| `ior` | int bitwise OR | `ior` |
| `ixor` | int bitwise XOR | `ixor` |
| `ladd` | long addition | `ladd` |
| `fadd` | float addition | `fadd` |
| `dadd` | double addition | `dadd` |

### Type Conversion

| Opcode | Description | Example |
|--------|-------------|---------|
| `i2l` | int to long | `i2l` |
| `i2f` | int to float | `i2f` |
| `i2d` | int to double | `i2d` |
| `l2i` | long to int | `l2i` |
| `l2f` | long to float | `l2f` |
| `l2d` | long to double | `l2d` |
| `f2i` | float to int | `f2i` |
| `f2l` | float to long | `f2l` |
| `f2d` | float to double | `f2d` |
| `d2i` | double to int | `d2i` |
| `d2l` | double to long | `d2l` |
| `d2f` | double to float | `d2f` |
| `i2b` | int to byte | `i2b` |
| `i2c` | int to char | `i2c` |
| `i2s` | int to short | `i2s` |

### Objects and Arrays

| Opcode | Description | Example |
|--------|-------------|---------|
| `new` | Create new object | `new java/lang/Object` |
| `newarray` | Create new primitive array | `newarray int` |
| `anewarray` | Create new reference array | `anewarray java/lang/String` |
| `arraylength` | Get array length | `arraylength` |
| `getfield` | Get instance field | `getfield owner/name:desc` |
| `putfield` | Set instance field | `putfield owner/name:desc` |
| `getstatic` | Get static field | `getstatic owner/name:desc` |
| `putstatic` | Set static field | `putstatic owner/name:desc` |

### Control Flow

| Opcode | Description | Example |
|--------|-------------|---------|
| `goto` | Unconditional jump | `goto 10` |
| `ifeq` | Jump if int == 0 | `ifeq 10` |
| `ifne` | Jump if int != 0 | `ifne 10` |
| `iflt` | Jump if int < 0 | `iflt 10` |
| `ifge` | Jump if int >= 0 | `ifge 10` |
| `ifgt` | Jump if int > 0 | `ifgt 10` |
| `ifle` | Jump if int <= 0 | `ifle 10` |
| `if_icmpeq` | Jump if int == int | `if_icmpeq 10` |
| `if_icmpne` | Jump if int != int | `if_icmpne 10` |
| `if_acmpeq` | Jump if ref == ref | `if_acmpeq 10` |
| `if_acmpne` | Jump if ref != ref | `if_acmpne 10` |
| `ifnull` | Jump if ref is null | `ifnull 10` |
| `ifnonnull` | Jump if ref is not null | `ifnonnull 10` |
| `tableswitch` | Switch by table | `tableswitch` |
| `lookupswitch` | Switch by lookup | `lookupswitch` |

### Method Invocation

| Opcode | Description | Example |
|--------|-------------|---------|
| `invokevirtual` | Instance method call | `invokevirtual Method` |
| `invokeinterface` | Interface method call | `invokeinterface Method` |
| `invokespecial` | Constructor/super call | `invokespecial Method` |
| `invokestatic` | Static method call | `invokestatic Method` |
| `invokedynamic` | Dynamic method call | `invokedynamic` |
| `return` | Return void | `return` |
| `ireturn` | Return int | `ireturn` |
| `lreturn` | Return long | `lreturn` |
| `freturn` | Return float | `freturn` |
| `dreturn` | Return double | `dreturn` |
| `areturn` | Return reference | `areturn` |

### Stack Operations

| Opcode | Description | Example |
|--------|-------------|---------|
| `pop` | Pop top value | `pop` |
| `pop2` | Pop top two values | `pop2` |
| `dup` | Duplicate top value | `dup` |
| `dup_x1` | Duplicate top under second | `dup_x1` |
| `dup_x2` | Duplicate top under third | `dup_x2` |
| `dup2` | Duplicate top two values | `dup2` |
| `swap` | Swap top two values | `swap` |

### Exceptions

| Opcode | Description | Example |
|--------|-------------|---------|
| `athrow` | Throw exception | `athrow` |
| `monitorenter` | Enter synchronized block | `monitorenter` |
| `monitorexit` | Exit synchronized block | `monitorexit` |

## How to Read Bytecode with javap

### Basic Usage

```bash
# Disassemble class file
javap -c MyClass.class

# Show constant pool
javap -verbose MyClass.class

# Show only public members
javap -public MyClass.class

# Show all members (including private)
javap -private MyClass.class
```

### Example Output

```java
public class Example {
    public int add(int a, int b) {
        return a + b;
    }
}

// javap -c output:
public int add(int, int);
  Code:
     0: iload_1
     1: iload_2
     2: iadd
     3: ireturn
```

### Interpreting the Output

- **Line numbers**: Bytecode offset (0, 1, 2, ...)
- **Instructions**: Bytecode operations
- **Stack**: Each instruction describes stack effect
- **Local variables**: Referenced by index (0 = this, 1 = first param, etc.)

## Bytecode Verification

### What Gets Verified

1. **Type checking**: All instructions use correct types
2. **Stack integrity**: Stack overflow/underflow impossible
3. **Access control**: Private methods not accessed incorrectly
4. **Finality**: Final classes/methods not overridden
5. **Code flow**: All paths return or throw

### Stack Map Frames

```java
public void example(boolean condition) {
    if (condition) {
        int x = 1;
    } else {
        int y = 2;
    }
    // Stack map frame required here
}
```

Frames describe stack contents at specific bytecode offsets for verification.

### Verification Process

1. **Class file format verification**: Structure is valid
2. **Semantic verification**: Code is meaningful
3. ** Bytecode verification**: Instructions are type-safe
4. ** Stack map frame verification**: All frames are consistent

## Bytecode Tools

### ASM Library

```java
ClassReader cr = new ClassReader("com.example.MyClass");
ClassVisitor cv = new ClassVisitor(Opcodes.ASM9) {
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, 
                                     String signature, String[] exceptions) {
        return new MethodVisitor(Opcodes.ASM9) {
            @Override
            public void visitCode() {
                // Method bytecode starts
            }
        };
    }
};
cr.accept(cv, 0);
```

### Bytecode Generation

```java
ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
cw.visit(Opcodes.V17, Opcodes.ACC_PUBLIC, "MyClass", null, 
         "java/lang/Object", null);

MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "hello", 
                                  "()V", null, null);
mv.visitCode();
mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", 
                  "Ljava/io/PrintStream;");
mv.visitLdcInsn("Hello, World!");
mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", 
                   "println", "(Ljava/lang/String;)V", false);
mv.visitInsn(Opcodes.RETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();

cw.visitEnd();
byte[] classBytes = cw.toByteArray();
```

## Advanced Bytecode Topics

### Invokedynamic (Java 7+)

```java
// Lambda expressions use invokedynamic
Runnable r = () -> System.out.println("Hello");

// Bytecode uses invokedynamic with bootstrap method
invokedynamic #0:0:Ljava/lang/invoke/LambdaMetafactory;metafactory
    (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;
     Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;
     Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)V
```

### Method Handles (Java 7+)

```java
MethodHandle mh = MethodHandles.lookup()
    .findVirtual(String.class, "length", 
                 MethodType.methodType(int.class));
int length = (int) mh.invokeExact("Hello");
```

### Bytecode Injection

```java
// Instrument classes at load time
ClassLoader cl = new ClassLoader() {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = loadClassBytes(name);
        // Transform bytecode
        byte[] transformed = transform(bytes);
        return defineClass(name, transformed, 0, transformed.length);
    }
};
```

## Performance Implications

### Bytecode Optimization

- **Inlining**: Small methods get inlined
- **Dead code elimination**: Unused code removed
- **Loop optimization**: Bounds checking moved out of loops
- **Escape analysis**: Allocations optimized away

### JIT Compilation

The JVM can compile bytecode to native code:
- **Tier 1-3**: C1 compiler (fast, less optimization)
- **Tier 4**: C2 compiler (slower, more optimization)
- **Graal**: Modern JIT compiler

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Resources

- **Official JVMS**: https://docs.oracle.com/javase/specs/jvms/se21/html/
- **ASM Documentation**: https://asm.ow2.io/
- **Bytecode Engineering Cookbook**: Practical examples
- **OpenJDK Source**: HotSpot JVM implementation

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
