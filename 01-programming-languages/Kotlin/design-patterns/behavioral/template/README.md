# Template Method Pattern (Kotlin)

## Overview

The Template Method pattern defines the skeleton of an algorithm in a base class,
letting subclasses override specific steps. Kotlin's open functions and higher-order
functions enable template implementations.

## When to Use

- Common algorithm structure with varying implementations
- Eliminating code duplication
- Enforcing algorithm structure
- Subclass customization points

## Kotlin Implementation

### Open Function Template

```kotlin
abstract class DataMiner {
    fun mine() {
        openFile()
        extractData()
        parseData()
        analyzeData()
        sendReport()
        closeFile()
    }

    protected open fun openFile() = println("Opening file")
    protected open fun extractData() = println("Extracting data")
    protected open fun parseData() = println("Parsing data")
    protected open fun analyzeData() = println("Analyzing")
    protected open fun sendReport() = println("Sending report")
    protected open fun closeFile() = println("Closing file")
}

class CSVDataMiner : DataMiner() {
    override fun openFile() = println("Opening CSV")
    override fun extractData() = println("Extracting CSV")
    override fun sendReport() = println("Sending CSV report")
    override fun closeFile() = println("Closing CSV")
}
```

### Higher-Order Function Template

```kotlin
fun <T> createPipeline(vararg stages: (T) -> T): (T) -> T {
    return { input -> stages.fold(input) { acc, stage -> stage(acc) } }
}

val process = createPipeline<Int>(
    { it + 1 },
    { it * 2 },
    { it - 3 }
)
```

### Generic Template

```kotlin
abstract class Pipeline<T> {
    fun execute(input: T): Unit {
        val transformed = transform(input)
        process(transformed)
        output(transformed)
    }

    protected abstract fun transform(input: T): T
    protected abstract fun process(input: T)
    protected open fun output(input: T) = println(input)
}
```

### Hook Methods

```kotlin
abstract class WebCrawler {
    suspend fun crawl() {
        if (beforeCrawl()) {
            connect()
            download()
            process()
            afterCrawl()
        }
    }

    protected open fun beforeCrawl(): Boolean = true
    protected open fun afterCrawl() {}
    protected abstract suspend fun connect()
    protected abstract suspend fun download()
    protected abstract suspend fun process()
}
```

## Best Practices

- Use open functions for overridable methods
- Keep template method small
- Use hook methods for optional steps
- Document customization points
- Consider using higher-order functions for simple templates

## Interview Questions

1. How does Template Method differ from Strategy?
2. What are hook methods in Kotlin?
3. Can template methods be suspend functions?
4. How do you handle template method with parameters?
5. When should you use Template Method vs composition?

## References

- Kotlin documentation: Inheritance
- "Kotlin in Action" by Svetlana Isakova
- "Clean Code" by Robert C. Martin
