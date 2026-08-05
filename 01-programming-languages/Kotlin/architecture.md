# Kotlin Architecture

## Kotlin/JVM

Kotlin runs on the Java Virtual Machine, interoperating fully with Java.

- **Compilation**: Kotlin compiles to JVM bytecode (.class files)
- **Runtime**: Uses the JVM runtime, garbage collector, and JIT compiler
- **Interoperability**: Call Java code from Kotlin and vice versa
- **Standard Library**: Provides Kotlin-specific APIs on top of Java standard library

Kotlin adds null safety, coroutines, data classes, and extension functions on top of the JVM platform.

## Kotlin/JS

Kotlin can compile to JavaScript for web development.

- **Compiler Modes**: IR (Intermediate Representation) and legacy backend
- **Kotlin/JS IR**: Modern compiler with tree shaking and better optimization
- **Browser Applications**: Run Kotlin in web browsers
- **Node.js Applications**: Run Kotlin on server-side JavaScript runtime

```kotlin
@JsExport
class Calculator {
    fun add(a: Int, b: Int): Int = a + b
}
```

## Kotlin/Native

Kotlin compiles to native binaries without a virtual machine.

- **Target Platforms**: macOS, Linux, Windows, iOS, Android NDK, WebAssembly
- **Memory Management**: Automatic reference counting with cycle collector
- **C Interop**: Direct interop with C libraries via cinterop
- **No JVM Required**: Standalone executables for each platform

## Kotlin Compiler

The Kotlin compiler transforms source code through multiple stages.

1. **Parsing**: Source code is parsed into an AST
2. **Resolution**: Types and references are resolved
3. **IR Generation**: Intermediate Representation is generated
4. **Backend**: IR is compiled to target (JVM, JS, Native)

Key compiler features:
- Smart casting based on type checks
- Type inference for variables and return types
- Inline function optimization
- Coroutine state machine generation

## Multiplatform

Kotlin Multiplatform shares code across platforms.

```kotlin
// commonMain
expect fun platformName(): String

// jvmMain
actual fun platformName(): String = "JVM"

// jsMain
actual fun platformName(): String = "JS"
```

Source sets: `commonMain`, `commonTest`, `jvmMain`, `jsMain`, `nativeMain`, `iosMain`.

## Gradle Integration

Kotlin uses Gradle with Kotlin DSL for build configuration.

```kotlin
plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

## Ecosystem

- **Ktor**: Framework for building applications and services
- **Exposed**: SQL framework for database access
- **Kotlin Serialization**: JSON and protocol buffer serialization
- **Kotlinx Coroutines**: Asynchronous programming library
- **Compose Multiplatform**: Declarative UI framework
