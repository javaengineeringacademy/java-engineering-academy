fun main() {
    // Immutable variables (val) - cannot be reassigned
    val name = "Kotlin"
    val version = 1.9
    val isAwesome = true
    println("Language: $name, Version: $version, Awesome: $isAwesome")

    // Mutable variables (var) - can be reassigned
    var counter = 0
    counter = 1
    counter += 10
    println("Counter: $counter")

    // Type inference
    val inferredString = "Hello, World!"
    val inferredInt = 42
    val inferredDouble = 3.14
    val inferredBoolean = false
    println("$inferredString, $inferredInt, $inferredDouble, $inferredBoolean")

    // Explicit types
    val explicitString: String = "Explicitly typed"
    val explicitInt: Int = 100
    val explicitLong: Long = 1_000_000L
    val explicitFloat: Float = 2.5f
    val explicitDouble: Double = 2.5
    val explicitByte: Byte = 127
    val explicitShort: Short = 32767
    val explicitChar: Char = 'A'
    println("Types: $explicitString, $explicitInt, $explicitLong, $explicitFloat, $explicitDouble")

    // String templates
    val language = "Kotlin"
    val year = 2011
    println("$language was released in $year")
    println("Length of language: ${language.length}")
    println("Result: ${if (year > 2010) "Modern" else "Legacy"}")

    // Multi-line strings
    val multiLine = """
        This is a multi-line string.
        It can span multiple lines.
        Trim margin: ${multiLine.trimMargin()}
    """.trimMargin()
    println(multiLine)

    // Constants (compile-time)
    const val MAX_SIZE = 100
    const val APP_NAME = "MyApp"
    println("Max size: $MAX_SIZE, App: $APP_NAME")

    // Arrays and Lists
    val list = listOf(1, 2, 3, 4, 5)
    val mutableList = mutableListOf(1, 2, 3)
    mutableList.add(4)
    println("List: $list, Mutable: $mutableList")

    // Maps
    val map = mapOf("a" to 1, "b" to 2, "c" to 3)
    val mutableMap = mutableMapOf("x" to 10)
    mutableMap["y"] = 20
    println("Map: $map, Mutable: $mutableMap")

    // Destructuring
    val (a, b, c) = listOf(10, 20, 30)
    println("Destructured: $a, $b, $c")
}
