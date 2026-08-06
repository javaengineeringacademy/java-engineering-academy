fun main() {
    // Nullable types
    var name: String? = "Kotlin"
    println("Name: $name")

    name = null
    println("Name after null: $name")

    // Safe call operator (?.)
    val length: Int? = name?.length
    println("Length: $length")

    // Safe call with let
    val message: String? = "Hello, World!"
    message?.let {
        println("Message length: ${it.length}")
        println("Message: $it")
    } ?: println("Message is null")

    // Elvis operator (?:)
    val nonNullName: String = name ?: "Default Name"
    println("Non-null name: $nonNullName")

    // Not-null assertion (!!)
    // val forcedLength = name!!.length  // Throws NPE if null

    // Safe casting
    val obj: Any = "This is a string"
    val safeString: String? = obj as? String
    val safeInt: Int? = obj as? Int
    println("Safe string: $safeString, Safe int: $safeInt")

    // Let function with null check
    val userInput: String? = "user@example.com"
    userInput?.let { email ->
        println("Processing email: $email")
        val atIndex = email.indexOf('@')
        if (atIndex > 0) {
            println("Domain: ${email.substring(atIndex + 1)}")
        }
    }

    // Also function
    val numbers = mutableListOf(1, 2, 3)
    numbers.also {
        println("Original list: $it")
        it.add(4)
    }.also {
        println("Modified list: $it")
    }

    // Run function
    val result = "Kotlin".run {
        println("Processing: $this")
        this.length
    }
    println("Result length: $result")

    // Apply function
    val sb = StringBuilder().apply {
        append("Hello")
        append(", ")
        append("World!")
    }
    println("Built string: $sb")

    // With function
    data class Person(val name: String, val age: Int)
    val person = Person("Alice", 30)
    val description = with(person) {
        "$name is $age years old"
    }
    println(description)

    // Elvis with throw
    val config: String? = null
    // val value = config ?: throw IllegalArgumentException("Config required")

    // Safe call chains
    data class Address(val street: String, val city: String)
    data class Company(val name: String, val address: Address?)
    data class Employee(val name: String, val company: Company?)

    val employee: Employee? = Employee("Bob", Company("TechCorp", Address("123 Main St", "Springfield")))
    val city = employee?.company?.address?.city ?: "Unknown"
    println("City: $city")

    // Null check with if
    val nullableList: List<Int>? = listOf(1, 2, 3)
    if (nullableList != null && nullableList.isNotEmpty()) {
        println("First element: ${nullableList[0]}")
    }
}
