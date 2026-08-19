# 03. Class Loading Internals Deep Dive

## Class Loading Lifecycle

The complete lifecycle of a class in the JVM:

```
1. LOADING
   ├── Locate .class bytes (filesystem, network, byte array, proxy)
   ├── Create java.lang.Class instance
   ├── Set classloader reference
   └── Mark as "loaded" (findLoadedClass returns non-null)

2. LINKING
   ├── VERIFICATION (structural + type safety)
   │   ├── Format: magic number, version, constant pool
   │   ├── Structural: method descriptors, field types
   │   ├── Type safety: bytecode instruction verification
   │   └── Stack map frames: control flow verification
   │
   ├── PREPARATION (memory allocation)
   │   ├── Allocate space for static fields
   │   ├── Assign default values (0, null, false)
   │   └── Assign constant values (static final primitives)
   │
   └── RESOLUTION (reference replacement)
       ├── Class references -> Class objects
       ├── Field references -> field_info + offset
       ├── Method references -> method_info + entry point
       └── Interface references -> interface method_info

3. INITIALIZATION
   ├── Execute <clinit>() methods
   │   ├── Static variable assignments (in source order)
   │   ├── Static blocks (in source order)
   │   └── Static nested class initialization
   ├── Thread-safe: class initialization is synchronized
   ├── Recursive: parent class initializes first
   └── Exception handling: ExceptionInInitializerError
```

## Class Initialization Rules

The JVM specification defines strict rules for class initialization:

```
Rule 1: Parent class initializes before child
├── Before <clinit> of class C executes
├── Ensure <clinit> of direct superclass has completed
└── Ensure <clinit> of all super interfaces has completed

Rule 2: Interface initialization
├── Interface initialization does NOT require super-interface initialization
├── Only if the interface has static fields that need initialization
└── Constant fields (static final primitive/String) do NOT trigger initialization

Rule 3: Thread safety
├── Class initialization is synchronized (like a lock on the Class object)
├── Only one thread can initialize a class at a time
├── Other threads wait for initialization to complete
└── If initialization throws, the class is marked as erroneous

Rule 4: Recursive initialization prevention
├── If thread T is initializing class C
├── And C's <clinit> recursively triggers C's initialization
├── Return immediately (no deadlock)
└── If another thread tries, it blocks
```

## Class Loading Internals: findClass() vs loadClass()

```
loadClass(String name) -- Template Method
├── Finds if class is already loaded (findLoadedClass)
├── Delegates to parent classloader
├── Calls findClass() if parent fails
└── Calls resolveClass() if resolve=true

findClass(String name) -- Extension Point
├── Called by loadClass() when parent cannot find the class
├── Override this in custom classloaders
├── Must return Class from defineClass()
└── Should throw ClassNotFoundException if not found

defineClass(String name, byte[] b, int off, int len)
├── Converts byte array to Class object
├── Performs verification (if not already verified)
├── Links the class (preparation + resolution)
└── Returns the Class object
```

## Class File Format Internals

The .class file structure that the JVM parses during loading:

```
ClassFile {
    u4  magic;                    // 0xCAFEBABE
    u2  minor_version;            // e.g., 0
    u2  major_version;            // e.g., 65 (Java 21)
    u2  constant_pool_count;      // Number of entries + 1
    cp_info constant_pool[];      // Constant pool entries
    u2  access_flags;             // ACC_PUBLIC, ACC_FINAL, etc.
    u2  this_class;               // Index into constant_pool
    u2  super_class;              // Index into constant_pool
    u2  interfaces_count;
    u2  interfaces[];             // Implemented interfaces
    u2  fields_count;
    field_info fields[];          // Field descriptors
    u2  methods_count;
    method_info methods[];        // Method descriptors
    u2  attributes_count;
    attribute_info attributes[];  // Class attributes
}
```

## Common Class Loading Issues

### Issue 1: ClassNotFoundException
```
Cause: Classloader cannot find the .class file
├── File not on classpath
├── Typo in class name
├── Wrong classloader used
└── Module system hides the class

Fix: Verify classpath, check module exports
```

### Issue 2: NoClassDefFoundError
```
Cause: Class was found at compile time but not at runtime
├── Missing dependency JAR
├── Class initialization failed
├── Class was loaded but then unloaded
└── Transitive dependency not on classpath

Fix: Add missing JARs, check for initialization errors
```

### Issue 3: LinkageError
```
Cause: Linking phase failed
├── ClassFormatError: invalid bytecode
├── VerifyError: bytecode verification failed
├── ClassCircularityError: circular class inheritance
└── ClassFormatError: duplicate class definition

Fix: Recompile classes, check for version mismatches
```
