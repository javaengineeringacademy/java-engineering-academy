// Kotlin Null Safety - ?, ?:, !!, let

fun main() {
    // Nullable types
    var name: String? = "Alice"
    name = null
    
    // Safe call operator (?.)
    val length: Int? = name?.length
    println("Length: $length")
    
    // Chained safe calls
    data class Address(val city: String?)
    data class User(val address: Address?)
    
    val user: User? = User(Address("New York"))
    val city: String? = user?.address?.city
    println("City: $city")
    
    // Elvis operator (?:)
    val safeLength: Int = name?.length ?: 0
    println("Safe length: $safeLength}")
    
    // Elvis with expressions
    val displayName: String = name ?: "Unknown"
    println("Display name: $displayName}")
    
    // Not-null assertion (!!)
    // val forcedLength = name!!.length // Throws NPE if null
    
    // Safe alternative
    if (name != null) {
        val forcedLength = name.length // Smart cast
        println("Forced length: $forcedLength")
    }
    
    // let function
    name?.let { nonNullName ->
        println("Name is: $nonNullName")
        println("Length: ${nonNullName.length}")
    }
    
    // let with return
    val result: String = name?.let {
        "Name is $it"
    } ?: "Name is null"
    println(result)
    
    // also function
    name?.also { println("Also: $it") }
    
    // run function
    val runResult: Int? = name?.run {
        length
    }
    println("Run result: $runResult")
    
    // apply function
    val sb = StringBuilder().apply {
        append("Hello")
        append(" ")
        append("World")
    }
    println("Apply: $sb")
    
    // with function
    val withResult = with(StringBuilder()) {
        append("Hello")
        append(" ")
        append("World")
        toString()
    }
    println("With: $withResult")
    
    // Safe casts
    val obj: Any = "Hello"
    val safeString: String? = obj as? String
    val safeInt: Int? = obj as? Int
    println("Safe cast string: $safeString")
    println("Safe cast int: $safeInt")
    
    // Nullable collections
    val nullableList: List<Int>? = listOf(1, 2, 3)
    val firstElement: Int? = nullableList?.firstOrNull()
    println("First element: $firstElement")
    
    // Filter nulls
    val mixedList = listOf(1, null, 3, null, 5)
    val nonNullList = mixedList.filterNotNull()
    println("Non-null list: $nonNullList")
    
    // Function returning nullable
    fun findUser(id: Int): String? {
        return if (id == 1) "Alice" else null
    }
    
    val foundUser: String? = findUser(1)
    println("Found user: $foundUser")
    
    println("Null safety example running")
}