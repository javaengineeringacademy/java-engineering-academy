// Kotlin Variables - val, var, types, type inference

fun main() {
    // Immutable variable (val)
    val name = "Alice" // Type inferred as String
    val age: Int = 30  // Explicit type
    val height = 165.5 // Type inferred as Double
    
    println("Name: $name, Age: $age, Height: $height")
    
    // Mutable variable (var)
    var count = 0
    count = 10
    println("Count: $count")
    
    // Basic types
    val integer: Int = 42
    val long: Long = 123456789L
    val float: Float = 3.14f
    val double: Double = 3.14159265358979
    val boolean: Boolean = true
    val char: Char = 'A'
    val string: String = "Hello, Kotlin!"
    
    // Type inference
    val inferredInt = 42        // Int
    val inferredLong = 100L     // Long
    val inferredDouble = 3.14   // Double
    val inferredString = "Hello" // String
    
    // String templates
    val firstName = "John"
    val lastName = "Doe"
    val fullName = "$firstName $lastName"
    val message = "Hello, $firstName! You are ${age + 5} years old."
    
    println("Full name: $fullName")
    println("Message: $message")
    
    // Multi-line strings
    val multiLine = """
        This is a
        multi-line string
        in Kotlin
    """.trimIndent()
    
    println("Multi-line:\n$multiLine")
    
    // Nullable types
    var nullableName: String? = "Alice"
    nullableName = null
    
    // Type conversion
    val intVal = 42
    val longVal = intVal.toLong()
    val doubleVal = intVal.toDouble()
    val stringVal = intVal.toString()
    
    println("Converted: $longVal, $doubleVal, $stringVal")
    
    // Constants (compile-time)
    const val MAX_SIZE = 100
    
    println("Max size: $MAX_SIZE")
    println("Variables example running")
}