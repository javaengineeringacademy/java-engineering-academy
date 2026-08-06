fun main() {
    // Basic data class
    val user1 = User("Alice", 30, "alice@example.com")
    val user2 = User("Alice", 30, "alice@example.com")
    println("User1: $user1")
    println("User2: $user2")

    // Equality
    println("Equal: ${user1 == user2}")  // Structural equality
    println("Same: ${user1 === user2}")  // Referential equality

    // Copy
    val user3 = user1.copy(name = "Bob")
    println("Copy: $user3")

    // Destructuring
    val (name, age, email) = user1
    println("Destructured: $name, $age, $email")

    // With defaults
    val product = Product(name = "Laptop")
    println("Product: $product")

    val productWithDetails = product.copy(price = 999.99, quantity = 5)
    println("Updated: $productWithDetails")

    // Component functions
    println("Component1: ${user1.component1()}")
    println("Component2: ${user1.component2()}")
    println("Component3: ${user1.component3()}")

    // Data class with collections
    val student = Student("Charlie", listOf("Math", "Science", "English"))
    println("Student: $student")

    // Nested data classes
    val address = Address("123 Main St", "Springfield", "IL")
    val person = PersonWithAddress("David", 35, address)
    println("Person: $person")

    // Data class in collections
    val users = listOf(
        User("Alice", 30, "alice@example.com"),
        User("Bob", 25, "bob@example.com"),
        User("Charlie", 35, "charlie@example.com")
    )

    val sortedByAge = users.sortedBy { it.age }
    println("Sorted by age: $sortedByAge")

    val groupedByAge = users.groupBy { it.age / 10 * 10 }
    println("Grouped: $groupedByAge")

    // toString customization
    val event = Event("Conference", "2024-01-15", "Tech Event")
    println("Event: $event")

    // Data class with validation
    val validEmail = EmailAddress("test@example.com")
    println("Valid email: $validEmail")

    // Data class with computed property
    val rectangle = Rectangle(5.0, 3.0)
    println("Rectangle: $rectangle, Area: ${rectangle.area}")
}

// Basic data class
data class User(
    val name: String,
    val age: Int,
    val email: String
)

// Data class with defaults
data class Product(
    val name: String,
    val price: Double = 0.0,
    val quantity: Int = 0
)

// Data class with collections
data class Student(
    val name: String,
    val courses: List<String>
)

// Nested data classes
data class Address(
    val street: String,
    val city: String,
    val state: String
)

data class PersonWithAddress(
    val name: String,
    val age: Int,
    val address: Address
)

// Data class with custom toString
data class Event(
    val name: String,
    val date: String,
    val type: String
) {
    override fun toString(): String = "[$type] $name on $date"
}

// Data class with validation
data class EmailAddress(val value: String) {
    init {
        require(value.contains("@")) { "Invalid email format" }
    }
}

// Data class with computed property
data class Rectangle(val width: Double, val height: Double) {
    val area: Double get() = width * height
}
