// Kotlin Classes - Primary constructor, init block

// Basic class
class Person(val name: String, val age: Int) {
    init {
        println("Person created: $name, $age")
    }
    
    fun greet(): String {
        return "Hello, I'm $name"
    }
}

// Class with secondary constructor
class User(val name: String, val email: String) {
    var age: Int = 0
        private set
    
    constructor(name: String, email: String, age: Int) : this(name, email) {
        this.age = age
    }
    
    override fun toString(): String {
        return "User(name=$name, email=$email, age=$age)"
    }
}

// Data class (auto-generates equals, hashCode, toString, copy)
data class Point(val x: Int, val y: Int)

// Sealed class
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

// Abstract class
abstract class Shape {
    abstract fun area(): Double
    abstract fun perimeter(): Double
    
    fun describe(): String {
        return "Area: ${area()}, Perimeter: ${perimeter()}"
    }
}

class Circle(val radius: Double) : Shape() {
    override fun area(): Double = Math.PI * radius * radius
    override fun perimeter(): Double = 2 * Math.PI * radius
}

class Rectangle(val width: Double, val height: Double) : Shape() {
    override fun area(): Double = width * height
    override fun perimeter(): Double = 2 * (width + height)
}

// Companion object (static members)
class MathUtils {
    companion object {
        const val PI = 3.14159
        
        fun add(a: Int, b: Int): Int = a + b
        fun multiply(a: Int, b: Int): Int = a * b
    }
}

// Object declaration (singleton)
object Database {
    private val connections = mutableListOf<String>()
    
    fun connect(url: String) {
        connections.add(url)
        println("Connected to $url")
    }
    
    fun disconnect(url: String) {
        connections.remove(url)
        println("Disconnected from $url")
    }
    
    fun getConnectionCount(): Int = connections.size
}

fun main() {
    // Basic class
    val person = Person("Alice", 30)
    println(person.greet())
    
    // Secondary constructor
    val user = User("Bob", "bob@example.com", 25)
    println(user)
    
    // Data class
    val point1 = Point(1, 2)
    val point2 = Point(1, 2)
    println("Points equal: ${point1 == point2}")
    println("Point: $point1")
    
    // Copy function
    val point3 = point1.copy(x = 10)
    println("Copied: $point3")
    
    // Destructuring
    val (x, y) = point1
    println("Destructured: x=$x, y=$y")
    
    // Sealed class
    val result: Result = Result.Success("Data loaded")
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
        is Result.Loading -> println("Loading...")
    }
    
    // Abstract class
    val circle = Circle(5.0)
    println(circle.describe())
    
    val rect = Rectangle(4.0, 6.0)
    println(rect.describe())
    
    // Companion object
    println("PI: ${MathUtils.PI}")
    println("Add: ${MathUtils.add(2, 3)}")
    
    // Singleton
    Database.connect("jdbc:mysql://localhost/db")
    Database.connect("jdbc:postgresql://localhost/db")
    println("Connections: ${Database.getConnectionCount()}")
    
    println("Classes example running")
}