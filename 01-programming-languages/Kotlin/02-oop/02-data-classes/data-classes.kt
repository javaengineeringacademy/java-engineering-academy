// Kotlin Data Classes - data class, copy, destructuring

// Basic data class
data class User(val name: String, val age: Int, val email: String)

// Data class with defaults
data class Config(
    val host: String = "localhost",
    val port: Int = 8080,
    val debug: Boolean = false
)

// Nested data class
data class Address(val street: String, val city: String, val country: String)
data class Person(val name: String, val address: Address)

fun main() {
    // Create data class
    val user = User("Alice", 30, "alice@example.com")
    println("User: $user")
    
    // Auto-generated toString
    println("ToString: ${user.toString()}")
    
    // equals
    val user2 = User("Alice", 30, "alice@example.com")
    val user3 = User("Bob", 25, "bob@example.com")
    println("user == user2: ${user == user2}") // true
    println("user == user3: ${user == user3}") // false
    
    // hashCode
    println("user hashCode: ${user.hashCode()}")
    
    // copy function
    val userCopy = user.copy(name = "Bob")
    println("Copy: $userCopy")
    
    // Multiple changes
    val updatedUser = user.copy(
        name = "Charlie",
        age = 35,
        email = "charlie@example.com"
    )
    println("Updated: $updatedUser")
    
    // Destructuring
    val (name, age, email) = user
    println("Name: $name, Age: $age, Email: $email")
    
    // Partial destructuring
    val (userName, _, userEmail) = user
    println("UserName: $userName, UserEmail: $userEmail")
    
    // Destructuring in loop
    val users = listOf(
        User("Alice", 30, "alice@example.com"),
        User("Bob", 25, "bob@example.com"),
        User("Charlie", 35, "charlie@example.com")
    )
    
    println("\nAll users:")
    for ((n, a, e) in users) {
        println("  $n, $a, $e")
    }
    
    // Component functions
    println("\nComponent functions:")
    println("user.component1(): ${user.component1()}") // name
    println("user.component2(): ${user.component2()}") // age
    println("user.component3(): ${user.component3()}") // email
    
    // Data class with defaults
    val config1 = Config()
    val config2 = Config(host = "example.com", port = 9090)
    println("\nConfig1: $config1")
    println("Config2: $config2")
    
    // Nested data class
    val person = Person("Alice", Address("123 Main St", "New York", "USA"))
    println("\nPerson: $person")
    
    // Destructure nested
    val (personName, address) = person
    val (street, city, country) = address
    println("$personName lives in $street, $city, $country")
    
    // Copy with nested change
    val updatedPerson = person.copy(
        address = person.address.copy(city = "Boston")
    )
    println("Updated person: $updatedPerson")
    
    // Data class in collections
    val userSet = setOf(user, user2, user3)
    println("\nUnique users: ${userSet.size}")
    
    // Sorting
    val sortedUsers = users.sortedBy { it.age }
    println("\nSorted by age:")
    for (u in sortedUsers) {
        println("  ${u.name}: ${u.age}")
    }
    
    println("\nData classes example running")
}