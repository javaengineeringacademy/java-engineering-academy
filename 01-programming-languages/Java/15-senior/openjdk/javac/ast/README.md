# javac Abstract Syntax Tree (AST)

The AST is the central data structure in javac. After parsing, all subsequent phases (annotation processing, type checking, code generation) operate on the AST.

## Tree Node Hierarchy

```
JCTree
├── JCStatement         (statements)
│   ├── JCBlock         (block statement)
│   ├── JCIf            (if/else)
│   ├── JCFor           (for loop)
│   ├── JCWhile         (while loop)
│   ├── JCDoWhile       (do-while loop)
│   ├── JCSwitch        (switch statement)
│   ├── JCTry           (try-catch-finally)
│   ├── JCThrow         (throw)
│   ├── JCReturn        (return)
│   ├── JCBreak         (break)
│   ├── JCContinue      (continue)
│   ├── JCAssert        (assert)
│   ├── JCSynchronized  (synchronized block)
│   └── JCExpressionStatement (expression as statement)
├── JCExpression        (expressions)
│   ├── JCAssign        (=)
│   ├── JCAssignOp      (+=, -=, etc.)
│   ├── JCUnary         (++, --, !, ~, +, -)
│   ├── JCBinary        (+, -, *, /, ==, etc.)
│   ├── JCTernary       (?: )
│   ├── JCConditional   (?: used differently)
│   ├── JCMethodInvocation (method call)
│   ├── JCNewClass      (new MyClass())
│   ├── JCNewArray      (new int[10])
│   ├── JCArrayAccess   (arr[i])
│   ├── JCFieldAccess   (obj.field)
│   ├── JCIdent         (identifier)
│   ├── JCLiteral       (literal value)
│   ├── JCTypeApply     (generics: List<String>)
│   ├── JCTypeCast      ((int) x)
│   ├── JCInstanceOf    (x instanceof Y)
│   ├── JCAnd           (&&)
│   ├── JCOr            (||)
│   └── JCParens        ((expr))
├── JCDeclaration        (declarations)
│   ├── JCClassDecl      (class/interface/enum/record)
│   ├── JCMethodDecl     (method/constructor)
│   ├── JCVariableDecl   (field/local variable)
│   ├── JCTypeParameter  (type parameter <T>)
│   └── JCModuleDecl     (module declaration)
├── JCCompilationUnit    (top-level)
└── JCModuleDecl         (module)
```

## Example AST

For this code:

```java
package com.example;

import java.util.List;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}
```

The AST looks like:

```
JCCompilationUnit
├── pkgdef: JCIdent ("com.example")
├── imports:
│   ├── JCImport ("java.util.List")
│   └── JCImport (implicit java.lang.*)
└── defs:
    └── JCClassDecl
        ├── mods: JCModifiers [public]
        ├── name: "Hello"
        ├── extending: null
        ├── implementing: []
        └── defs:
            └── JCMethodDecl
                ├── mods: [public, static]
                ├── name: "main"
                ├── restype: JCPrimitiveTypeTree (void)
                ├── params:
                │   └── JCVariableDecl
                │       ├── vartype: JCArrayTypeTree (String[])
                │       └── name: "args"
                └── body: JCBlock
                    └── JCExpressionStatement
                        └── JCMethodInvocation
                            ├── methodselect: JCFieldAccess
                            │   ├── selected: JCFieldAccess
                            │   │   ├── selected: JCIdent (System)
                            │   │   └── name: "out"
                            │   └── name: "println"
                            └── args:
                                └── JCLiteral ("Hello, world!")
```

## Tree Tags

Each tree node has a tag identifying its kind:

```java
enum Tag {
    TOPLEVEL,     // Compilation unit
    PACKAGEDEF,   // package declaration
    IMPORT,       // import declaration
    CLASSDEF,     // class/interface declaration
    METHODDEF,    // method declaration
    VARDEF,       // variable declaration
    BLOCK,        // block statement
    DOLOOP,       // do-while
    WHILELOOP,    // while
    FORLOOP,      // for
    SWITCH,       // switch
    CASE,         // case in switch
    EXEC,         // expression statement
    IF,           // if
    BREAK,        // break
    CONTINUE,     // continue
    RETURN,       // return
    THROW,        // throw
    TRY,          // try-catch-finally
    ASSIGN,       // =
    PREINCPOST,   // ++x, x++
    POSTINCPOST,  // x++, x--
    CALL,         // method invocation
    NEWCLASS,     // new C()
    NEWARRAY,     // new T[]
    PARENS,       // (expr)
    IDENT,        // identifier
    LITERAL,      // literal value
    TYPEAPPLY,    // generic type (List<String>)
    TYPETREE,     // type reference
    ANNOTATION,   // annotation
    ERRONEOUS,    // error node
    // ...
}
```

## AST Visitors

The AST is traversed using the visitor pattern:

```java
// TreeScanner visits all nodes
class MyVisitor extends TreeScanner<Void, Context> {
    @Override
    public Void visitClassDef(JCClassDecl node, Context ctx) {
        System.out.println("Found class: " + node.name);
        return super.visitClassDef(node, ctx);
    }

    @Override
    public Void visitMethodDef(JCMethodDecl node, Context ctx) {
        System.out.println("Found method: " + node.name);
        return super.visitMethodDef(node, ctx);
    }
}
```

Built-in visitors:
- `TreeScanner` — Walk all nodes
- `TreeTranslator` — Transform AST
- `Pretty` — Source code printer

## Source Position Tracking

Every AST node records its position in the source:

```java
class JCTree {
    int pos;      // Start position (char offset)
    int endpos;   // End position
    SymTable symtab;  // Type info
}
```

This enables precise error messages and IDE features.

## Key Source Files

| File | Purpose |
|------|---------|
| `com/sun/tools/javac/tree/Tree.java` | All AST node definitions |
| `com/sun/tools/javac/tree/JCTree.java` | JC* node classes |
| `com/sun/tools/javac/tree/TreeScanner.java` | Visitor base class |
| `com/sun/tools/javac/tree/TreeTranslator.java` | AST transformation base |
| `com/sun/tools/javac/tree/Pretty.java` | Source code printer |
| `com/sun/tools/javac/tree/TreeMaker.java` | Factory for creating AST nodes |
