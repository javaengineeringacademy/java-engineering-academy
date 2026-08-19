# 11. JVM Security Internals Deep Dive

## JVM Security Architecture

### Security Layers

```
Layer 1: Bytecode Verification
├── Format verification (magic number, version)
├── Structural verification (method signatures, field types)
├── Type safety verification (bytecode instruction verification)
├── Stack integrity verification (stack map frames)
└── Prevents: Invalid bytecode execution

Layer 2: Class Loading Security
├── Parent delegation model (prevents core class replacement)
├── Namespace isolation (different classloaders = different types)
├── Code source protection (origin tracking)
├── ProtectionDomain (permissions per classloader)
└── Prevents: Untrusted code replacing core classes

Layer 3: Security Manager (Deprecated)
├── Access control (permission checking)
├── Policy enforcement (file, network, runtime permissions)
├── Stack-based permissions (caller's permissions)
└── Prevents: Unauthorized resource access

Layer 4: Runtime Security
├── Memory protection (no buffer overflows)
├── Thread safety (synchronization guarantees)
├── Exception handling (prevent information leakage)
├── Deserialization filtering (JEP 290)
└── Prevents: Memory corruption, code injection
```

### Bytecode Verification Internals

```
Verification Phases:
1. Format Verification
   ├── Magic number: 0xCAFEBABE
   ├── Version: major/minor version compatibility
   ├── Constant pool: valid entries and indices
   └── Access flags: valid combinations

2. Structural Verification
   ├── Method descriptors: valid parameter/return types
   ├── Field descriptors: valid types
   ├── Instruction sequences: valid bytecode instructions
   └── Exception table: valid handler ranges

3. Type Safety Verification
   ├── Stack map frames: consistent types at each branch
   ├── Type compatibility: assignment compatibility checks
   ├── Method invocations: valid signatures
   └── Field accesses: valid types

4. Stack Verification
   ├── Stack depth: within limits
   ├── Operand types: correct types on stack
   ├── Local variables: valid indices and types
   └── Consistent types: same type at merge points
```

### Security Manager Internals

```
Permission Checking Process:
1. Code requests operation (e.g., file read)
2. Security Manager checks:
   a. Get caller's ProtectionDomain
   b. Get code source and certificates
   c. Check policy file for matching grants
   d. Check stack for privileged frames
3. Decision:
   a. If permission granted: operation proceeds
   b. If permission denied: SecurityException thrown

Permission Types:
├── java.io.FilePermission: File access
├── java.net.SocketPermission: Network access
├── java.lang.RuntimePermission: Runtime operations
├── java.lang.reflect.ReflectPermission: Reflection
├── java.security.AllPermission: All permissions (dangerous!)
└── java.security.SecurityPermission: Security config
```

### Deserialization Security (JEP 290)

```
Deserialization Filter Process:
1. Serialized data arrives
2. Filter evaluates:
   ├── Class name pattern matching
   ├── Array size limits
   ├── Depth limits
   ├── Object count limits
   └── Custom filter logic
3. Decision:
   ├── ALLOW: Proceed with deserialization
   ├── REJECT: Throw InvalidClassException
   └── RETURN: Return default value (for primitives)

Filter Configuration:
├── System property: jdk.serialFilter
├── Per-stream: ObjectInputStream.setObjectInputFilter()
├── Global: java.security.properties file
└── Custom: implement ObjectInputFilter
```
