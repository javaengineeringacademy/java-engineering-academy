# 11. JVM Security - Quiz

## Questions

### Q1: Security Manager
What does the Java Security Manager control?
- A) Network connections only
- B) What operations code is allowed to perform (file I/O, network, reflection, etc.)
- C) Memory allocation limits
- D) Thread scheduling priority

**Answer: B**
Explanation: The Security Manager provides a policy-based access control mechanism that restricts what operations code can perform, including file access, network connections, reflection, and runtime operations.

### Q2: Bytecode Verification
What does bytecode verification check?
- A) That the code compiles correctly
- B) That the bytecode is valid, type-safe, and doesn't violate JVM constraints
- C) That the code follows naming conventions
- D) That the code has proper comments

**Answer: B**
Explanation: Bytecode verification checks the magic number, version compatibility, constant pool validity, type safety of instructions, and stack integrity. It prevents malicious bytecode from executing.

### Q3: ProtectionDomain
What is a ProtectionDomain?
- A) A network security zone
- B) An association of code source and permissions for loaded classes
- C) A memory protection mechanism
- D) A thread safety mechanism

**Answer: B**
Explanation: A ProtectionDomain associates a CodeSource (origin of the code) with a set of Permissions. The JVM uses it during security checks to determine what operations loaded code can perform.

### Q4: Code Source
What information does a CodeSource contain?
- A) The class name and package
- B) The location (URL) and certificates of the code
- C) The bytecode version
- D) The JIT compilation level

**Answer: B**
Explanation: A CodeSource contains the URL where the code originated and any digital certificates used to sign the code. This information is used to determine what permissions to grant.

### Q5: Policy File
What is a Java security policy file?
- A) A configuration file that defines permissions for code sources
- B) A compiled security class
- C) A JVM startup script
- D) A classpath configuration

**Answer: A**
Explanation: A policy file (e.g., java.policy) defines which permissions are granted to code from specific sources. It uses a grant syntax to map code sources to permission sets.

### Q6: AccessController
What does AccessController.doPrivileged() do?
- A) Grants all permissions to the current thread
- B) Executes a privileged operation with the caller's permissions, ignoring the immediate caller's context
- C) Disables the Security Manager
- D) Creates a new classloader

**Answer: B**
Explanation: doPrivileged() executes a block with the calling code's permissions, not the immediate caller's. This is used when trusted library code needs to perform operations on behalf of less trusted code.

### Q7: Deserialization Security
Why is Java deserialization a security concern?
- A) It uses too much memory
- B) Malformed serialized objects can execute arbitrary code during deserialization
- C) It is too slow
- D) It doesn't support all data types

**Answer: B**
Explanation: Deserialization vulnerabilities allow attackers to craft malicious serialized objects that execute arbitrary code when deserialized. This has been the root cause of many critical vulnerabilities (Log4Shell, WebLogic, etc.).

### Q8: Module System Security
How does the Java Module System (JPMS) improve security?
- A) It encrypts class files
- B) It provides strong encapsulation, hiding internal APIs from untrusted code
- C) It disables reflection
- D) It removes the Security Manager

**Answer: B**
Explanation: JPMS provides strong encapsulation by restricting which packages are accessible. Internal APIs (like sun.misc.Unsafe) are hidden by default, reducing the attack surface.

### Q9: JEP 290
What does JEP 290 (Filter Incoming Serialization Data) provide?
- A) Network traffic filtering
- B) A mechanism to filter deserialized objects before they are fully deserialized
- C) Memory filtering for garbage collection
- D) Class file filtering during loading

**Answer: B**
Explanation: JEP 290 provides a deserialization filter that can reject or limit deserialized objects before they are fully constructed, preventing many deserialization attacks.

### Q10: Security Manager Deprecation
What is the status of the Java Security Manager in modern Java?
- A) It is the recommended security mechanism
- B) It is deprecated for removal since Java 17
- C) It is only available in Oracle JDK
- D) It is required for all applications

**Answer: B**
Explanation: The Security Manager is deprecated for removal since Java 17 (JEP 411). Modern security relies on module encapsulation, deserialization filters, and container-level security instead.

## Score Guide
- **9-10 correct**: Security expert
- **7-8 correct**: Solid understanding, review specific mechanisms
- **5-6 correct**: Good start, study security architecture
- **Below 5**: Review basics before proceeding
