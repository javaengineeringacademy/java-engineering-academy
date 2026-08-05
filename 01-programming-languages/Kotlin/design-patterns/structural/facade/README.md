# Facade Pattern (Kotlin)

## Overview

The Facade pattern provides a unified interface to a set of interfaces in a subsystem.
Kotlin's classes and extension functions can wrap complex subsystems to provide simpler
APIs.

## When to Use

- Simplifying complex library usage
- Providing layered architecture
- Decoupling subsystems from clients
- Creating service layers

## Kotlin Implementation

### Basic Facade

```kotlin
class VideoConverter {
    fun convert(filename: String, format: String): Pair<String, String> {
        println("Converting $filename to $format")
        return Pair("$filename.$format", format)
    }
}

class AudioConverter {
    fun extractAudio(filename: String): String {
        println("Extracting audio from $filename")
        return "$filename.mp3"
    }
}

class MediaFacade {
    private val videoConverter = VideoConverter()
    private val audioConverter = AudioConverter()

    fun convertToMP4(filename: String): Pair<String, String> {
        return videoConverter.convert(filename, "mp4")
    }

    fun extractAudio(filename: String): String {
        return audioConverter.extractAudio(filename)
    }
}
```

### Generic Facade

```kotlin
class GenericFacade<T> {
    private val services = mutableMapOf<String, T>()

    fun register(name: String, service: T) {
        services[name] = service
    }

    fun get(name: String): T? {
        return services[name]
    }
}
```

### Service Facade

```kotlin
interface UserService {
    suspend fun getUser(id: String): User
}

interface PostService {
    suspend fun getPosts(userId: String): List<Post>
}

class APIFacade(
    private val userService: UserService,
    private val postService: PostService
) {
    suspend fun getUserWithPosts(id: String): Pair<User, List<Post>> {
        val user = userService.getUser(id)
        val posts = postService.getPosts(id)
        return Pair(user, posts)
    }
}
```

### Extension Function Facade

```kotlin
class Database {
    fun query(sql: String): List<Map<String, Any>> = emptyList()
}

fun Database.findUser(id: String): Map<String, Any>? {
    return query("SELECT * FROM users WHERE id = $id").firstOrNull()
}

fun Database.findPosts(userId: String): List<Map<String, Any>> {
    return query("SELECT * FROM posts WHERE user_id = $userId")
}
```

## Best Practices

- Keep facade focused and minimal
- Don't add business logic to facade
- Use facade as thin layer only
- Document subsystem dependencies
- Consider using dependency injection

## Interview Questions

1. What is the difference between Facade and Adapter?
2. Does Facade add new functionality?
3. When should you use Facade vs direct subsystem access?
4. Can Facade be combined with other patterns?
5. How do you test code using Facade?

## References

- Kotlin documentation: Classes
- "Kotlin in Action" by Svetlana Isakova
- "Clean Architecture" by Robert C. Martin
