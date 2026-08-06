fun main() {
    // Basic class with properties
    val person = Person("Alice", 30)
    println(person)
    println("Name: ${person.name}, Age: ${person.age}")

    // Class with constructor
    val car = Car("Toyota", "Camry", 2023)
    println(car)

    // Class with init block
    val user = User("bob@example.com", "Bob Smith")
    println(user)

    // Companion object
    println("Max age: ${Person.MAX_AGE}")
    println("Created: ${Person.create("Charlie", 25)}")

    // Visibility modifiers
    val account = BankAccount(1000.0)
    account.deposit(500.0)
    account.withdraw(200.0)
    println("Balance: ${account.balance}")

    // Sealed class
    val result = Result.Success("Data loaded")
    when (result) {
        is Result.Loading -> println("Loading...")
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
    }

    // Abstract class
    val circle = Circle(5.0)
    val rectangle = Rectangle(4.0, 6.0)
    println("Circle area: ${circle.area()}")
    println("Rectangle area: ${rectangle.area()}")

    // Enum class
    val direction = Direction.NORTH
    println("Direction: $direction")
    println("Opposite: ${direction.opposite()}")

    // Object declaration (singleton)
    println("Database: ${Database.connection}")
    Database.connect()
}

// Basic class
class Person(val name: String, var age: Int) {
    companion object {
        const val MAX_AGE = 120

        fun create(name: String, age: Int): Person {
            return Person(name, age)
        }
    }

    override fun toString(): String = "Person(name=$name, age=$age)"
}

// Primary constructor
class Car(val make: String, val model: String, val year: Int) {
    override fun toString(): String = "$year $make $model"
}

// Init block
class User(val email: String, var name: String) {
    init {
        require(email.contains("@")) { "Invalid email" }
        println("User created: $name")
    }
}

// Visibility modifiers
class BankAccount(initialBalance: Double) {
    var balance: Double = initialBalance
        private set

    fun deposit(amount: Double) {
        if (amount > 0) balance += amount
    }

    fun withdraw(amount: Double) {
        if (amount in 0.0..balance) balance -= amount
    }
}

// Sealed class
sealed class Result {
    object Loading : Result()
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}

// Abstract class
abstract class Shape {
    abstract fun area(): Double
}

class Circle(private val radius: Double) : Shape() {
    override fun area() = Math.PI * radius * radius
}

class Rectangle(private val width: Double, private val height: Double) : Shape() {
    override fun area() = width * height
}

// Enum class
enum class Direction {
    NORTH, SOUTH, EAST, WEST {
        override fun opposite() = NORTH
    };

    abstract fun opposite(): Direction
}

// Object declaration
object Database {
    val connection: String = "jdbc:mysql://localhost:3306"
    fun connect() = println("Connected to $connection")
}
