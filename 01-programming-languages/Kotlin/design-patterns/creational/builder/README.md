# Builder Pattern (Kotlin)

## Overview

The Builder pattern separates construction of a complex object from its representation.
Kotlin's DSL capabilities and extension functions make builders particularly elegant.

## When to Use

- Creating objects with many optional parameters
- Avoiding telescoping constructors
- Building different representations of same object
- Complex object initialization logic

## Kotlin Implementation

### DSL Builder

```kotlin
class HttpRequest private constructor(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String?
) {
    class Builder {
        private var url: String = ""
        private var method: String = "GET"
        private var headers: MutableMap<String, String> = mutableMapOf()
        private var body: String? = null

        fun url(url: String) = apply { this.url = url }
        fun method(method: String) = apply { this.method = method }
        fun header(key: String, value: String) = apply { headers[key] = value }
        fun body(body: String) = apply { this.body = body }

        fun build(): HttpRequest {
            require(url.isNotEmpty()) { "URL cannot be empty" }
            return HttpRequest(url, method, headers, body)
        }
    }
}

val request = HttpRequest.Builder()
    .url("https://api.example.com")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("""{"data": "test"}""")
    .build()
```

### Kotlin DSL Builder

```kotlin
class User {
    var name: String = ""
    var email: String = ""
    var age: Int = 0
}

fun user(block: User.() -> Unit): User {
    return User().apply(block)
}

val user = user {
    name = "John"
    email = "john@example.com"
    age = 30
}
```

### Generic Builder

```kotlin
class Builder<T> {
    private val actions = mutableListOf<T.() -> Unit>()

    fun add(action: T.() -> Unit) = apply { actions.add(action) }

    fun build(init: T.() -> Unit): T {
        val obj = T::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()
        return obj.apply {
            actions.forEach { it() }
            init()
        }
    }
}
```

### Builder with Validation

```kotlin
class UserBuilder {
    private var name: String = ""
    private var email: String = ""

    fun name(name: String): UserBuilder {
        require(name.length >= 2) { "Name must be at least 2 characters" }
        this.name = name
        return this
    }

    fun email(email: String): UserBuilder {
        require(email.contains("@")) { "Invalid email" }
        this.email = email
        return this
    }

    fun build(): User {
        require(name.isNotEmpty()) { "Name is required" }
        require(email.isNotEmpty()) { "Email is required" }
        return User(name, email)
    }
}
```

## Best Practices

- Use apply for fluent builders
- Leverage Kotlin DSL capabilities
- Use require for validation
- Consider data classes for immutable objects
- Use extension functions for custom DSLs

## Interview Questions

1. What problem does Builder pattern solve?
2. How does Kotlin DSL differ from traditional builder?
3. Can builder be used with data classes?
4. When should you use Builder vs Factory?
5. How do you handle validation in builders?

## References

- Kotlin documentation: DSLs
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
