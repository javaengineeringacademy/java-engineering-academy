// Kotlin Inheritance - open, override, sealed

// Open class (can be inherited)
open class Animal(val name: String, val age: Int) {
    open fun speak(): String {
        return "$name makes a sound"
    }
    
    fun describe(): String {
        return "$name, age $age"
    }
}

// Derived class
class Dog(name: String, age: Int, val breed: String) : Animal(name, age) {
    override fun speak(): String {
        return "$name barks"
    }
    
    fun fetch(item: String) {
        println("$name fetches the $item")
    }
}

// Another derived class
class Cat(name: String, age: Int, val isIndoor: Boolean) : Animal(name, age) {
    override fun speak(): String {
        return "$name meows"
    }
    
    fun purr() {
        println("$name purrs...")
    }
}

// Multi-level inheritance
class Puppy(name: String, age: Int, breed: String) : Dog(name, age, breed) {
    override fun speak(): String {
        return "$name yips"
    }
}

// Abstract class
abstract class Shape(val name: String) {
    abstract fun area(): Double
    abstract fun perimeter(): Double
    
    fun describe(): String {
        return "$name: Area = ${"%.2f".format(area())}, Perimeter = ${"%.2f".format(perimeter())}"
    }
}

class Circle(radius: Double) : Shape("Circle") {
    val radius = radius
    
    override fun area(): Double = Math.PI * radius * radius
    override fun perimeter(): Double = 2 * Math.PI * radius
}

class Rectangle(width: Double, height: Double) : Shape("Rectangle") {
    val width = width
    val height = height
    
    override fun area(): Double = width * height
    override fun perimeter(): Double = 2 * (width + height)
}

// Sealed class
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

// Interface
interface Drawable {
    fun draw()
}

// Class implementing interface and inheriting
class DrawableCircle(radius: Double) : Shape("Circle"), Drawable {
    val radius = radius
    
    override fun area(): Double = Math.PI * radius * radius
    override fun perimeter(): Double = 2 * Math.PI * radius
    
    override fun draw() {
        println("Drawing circle with radius $radius")
    }
}

fun main() {
    // Create objects
    val dog = Dog("Rex", 5, "German Shepherd")
    val cat = Cat("Whiskers", 3, true)
    val puppy = Puppy("Buddy", 1, "Labrador")
    
    // Method calls
    println(dog.speak())
    println(cat.speak())
    println(puppy.speak())
    
    // Polymorphism
    val animals = listOf(dog, cat, puppy)
    println("\nAll animals:")
    for (animal in animals) {
        println("  ${animal.speak()}")
    }
    
    // Abstract class
    val circle = Circle(5.0)
    println("\n${circle.describe()}")
    
    val rect = Rectangle(4.0, 6.0)
    println(rect.describe())
    
    // Sealed class
    val result: Result = Result.Success("Data loaded")
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
        is Result.Loading -> println("Loading...")
    }
    
    // Interface
    val drawableCircle = DrawableCircle(3.0)
    drawableCircle.draw()
    
    // Type checking
    if (dog is Dog) {
        dog.fetch("ball")
    }
    
    // Smart cast
    val animal: Animal = dog
    if (animal is Dog) {
        println("Smart cast: ${animal.breed}")
    }
    
    println("\nInheritance example running")
}