// Kotlin Interfaces - Interface, default methods

// Basic interface
interface Drivable {
    fun start()
    fun stop()
    
    // Default method
    fun isRunning(): Boolean {
        return false
    }
}

// Interface with properties
interface Identifiable {
    val id: Int
    
    // Default property getter
    val displayName: String
        get() = "Item #$id"
}

// Multiple interfaces
interface Readable {
    fun read(): String
}

interface Writable {
    fun write(data: String)
}

// Interface inheriting from another
interface Cacheable : Identifiable {
    fun cache()
    fun evict()
}

// Implementation
class Car(val brand: String, override val id: Int) : Drivable, Identifiable {
    override fun start() {
        println("$brand car started")
    }
    
    override fun stop() {
        println("$brand car stopped")
    }
    
    override fun isRunning(): Boolean {
        return true // Simplified
    }
}

// Multiple interface implementation
class Document(override val id: Int, var content: String) : Readable, Writable, Cacheable {
    override fun read(): String {
        return content
    }
    
    override fun write(data: String) {
        content = data
        println("Document written")
    }
    
    override fun cache() {
        println("Document $id cached")
    }
    
    override fun evict() {
        println("Document $id evicted")
    }
}

// Interface with companion object
interface Logger {
    fun log(message: String)
    
    companion object {
        fun create(): Logger {
            return ConsoleLogger()
        }
    }
}

class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("[LOG] $message")
    }
}

// Abstract class implementing interface
abstract class BaseRepository : Identifiable {
    abstract fun save()
    
    override val displayName: String
        get() = "Repository #$id"
}

// Interface as function parameter
fun processData(data: Readable) {
    println("Processing: ${data.read()}")
}

fun writeData(data: Writable, content: String) {
    data.write(content)
}

fun main() {
    // Create objects
    val car = Car("Toyota", 1)
    val doc = Document(1, "Hello World")
    
    // Interface as type
    val drivable: Drivable = car
    drivable.start()
    println("Running: ${drivable.isRunning()}")
    drivable.stop()
    
    // Multiple interfaces
    val identifiable: Identifiable = car
    println("ID: ${identifiable.id}")
    println("Display: ${identifiable.displayName}")
    
    // Document using multiple interfaces
    val readable: Readable = doc
    val writable: Writable = doc
    val cacheable: Cacheable = doc
    
    println("Read: ${readable.read()}")
    writable.write("Updated content")
    cacheable.cache()
    
    // Interface as parameter
    processData(doc)
    writeData(doc, "New content")
    
    // Companion object
    val logger: Logger = Logger.create()
    logger.log("Application started")
    
    // Interface in collection
    val documents = listOf(
        Document(1, "Doc 1"),
        Document(2, "Doc 2"),
        Document(3, "Doc 3")
    )
    
    println("\nDocuments:")
    for (d in documents) {
        println("  ${d.displayName}")
    }
    
    // Filter by interface
    val cacheableDocs = documents.filterIsInstance<Cacheable>()
    println("\nCacheable docs: ${cacheableDocs.size}")
    
    println("\nInterfaces example running")
}