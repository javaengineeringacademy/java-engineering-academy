# Chain of Responsibility Pattern (Kotlin)

## Overview

The Chain of Responsibility pattern avoids coupling the sender of a request to its
receiver by giving more than one object a chance to handle the request. Kotlin's
lambdas and coroutine support enable clean middleware implementations.

## When to Use

- Multiple objects may handle a request
- Handler should be determined at runtime
- Request should be handled by one of multiple handlers
- Set of handlers should be specified dynamically

## Kotlin Implementation

### Functional Chain

```kotlin
typealias Handler<T> = (T, (T) -> T) -> T

class Pipeline<T> {
    private val handlers = mutableListOf<Handler<T>>()

    fun use(handler: Handler<T>) {
        handlers.add(handler)
    }

    fun execute(request: T): T {
        var index = 0
        val next: (T) -> T = { req ->
            if (index < handlers.size) {
                val handler = handlers[index++]
                handler(req, next)
            } else {
                req
            }
        }
        return next(request)
    }
}
```

### Sealed Class Handler

```kotlin
sealed class Request {
    data class Auth(val token: String) : Request()
    data class Data(val payload: String) : Request()
}

sealed class HandlerResult {
    data class Handled(val response: String) : HandlerResult()
    object NotHandled : HandlerResult()
}

abstract class Handler {
    protected var next: Handler? = null

    fun setNext(handler: Handler): Handler {
        next = handler
        return handler
    }

    open fun handle(request: Request): HandlerResult {
        return next?.handle(request) ?: HandlerResult.NotHandled
    }
}
```

### Coroutine Middleware

```kotlin
typealias SuspendMiddleware<T> = suspend (T, suspend () -> Unit) -> Unit

class SuspendPipeline<T> {
    private val middlewares = mutableListOf<SuspendMiddleware<T>>()

    fun use(middleware: SuspendMiddleware<T>) {
        middlewares.add(middleware)
    }

    suspend fun execute(context: T) {
        var index = 0
        val next: suspend () -> Unit = {
            if (index < middlewares.size) {
                val middleware = middlewares[index++]
                middleware(context, next)
            }
        }
        next()
    }
}
```

### Express-Style Middleware

```kotlin
typealias Middleware = suspend (Request, Response, suspend () -> Unit) -> Unit

class App {
    private val middlewares = mutableListOf<Middleware>()

    fun use(middleware: Middleware) {
        middlewares.add(middleware)
    }

    suspend fun handle(request: Request, response: Response) {
        var index = 0
        val next: suspend () -> Unit = {
            if (index < middlewares.size) {
                val middleware = middlewares[index++]
                middleware(request, response, next)
            }
        }
        next()
    }
}
```

## Best Practices

- Use lambdas for simple handlers
- Keep handlers focused and small
- Define default behavior for unhandled requests
- Document handler ordering
- Consider using coroutines for async chains

## Interview Questions

1. What is the difference between Chain of Responsibility and Middleware?
2. Can multiple handlers process same request?
3. How do you handle unhandled requests?
4. When should you use Chain vs Decorator?
5. How do you implement async chain of responsibility?

## References

- Kotlin documentation: Lambdas
- "Kotlin in Action" by Svetlana Isakova
- Ktor middleware documentation
