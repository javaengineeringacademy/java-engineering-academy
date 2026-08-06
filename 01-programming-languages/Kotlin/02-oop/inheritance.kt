fun main() {
    // Single inheritance
    val animal = Animal("Generic")
    val dog = Dog("Rex", "Labrador")
    val cat = Cat("Whiskers", true)

    animal.eat()
    dog.eat()
    dog.fetch()
    cat.eat()
    cat.purr()

    // Type checking with is
    val creatures: List<Animal> = listOf(dog, cat, Animal("Generic"))
    for (creature in creatures) {
        when (creature) {
            is Dog -> creature.fetch()
            is Cat -> creature.purr()
            else -> println("Unknown creature")
        }
    }

    // Polymorphism
    val shapes: List<Shape> = listOf(Circle(5.0), Rectangle(4.0, 6.0), Triangle(3.0, 4.0, 5.0))
    for (shape in shapes) {
        println("${shape::class.simpleName}: area=${shape.area()}, perimeter=${shape.perimeter()}")
    }

    // Abstract class inheritance
    val vehicle = ElectricCar("Tesla", 250)
    vehicle.start()
    vehicle.charge()
    vehicle.stop()

    // Open class
    val base = BaseClass()
    base.openMethod()
    val derived = DerivedClass()
    derived.openMethod()
    derived.overriddenMethod()

    // Super call
    val child = Child()
    child.childMethod()

    // Interface inheritance
    val smartDevice = Smartphone("iPhone", 15)
    smartDevice.turnOn()
    smartDevice.makeCall()
    smartDevice.takePhoto()
}

// Basic inheritance
open class Animal(val name: String) {
    open fun eat() = println("$name is eating")
    fun sleep() = println("$name is sleeping")
}

class Dog(name: String, val breed: String) : Animal(name) {
    override fun eat() {
        super.eat()
        println("Dog food only")
    }
    fun fetch() = println("$name is fetching")
}

class Cat(name: String, val isIndoor: Boolean) : Animal(name) {
    fun purr() = println("Purrrr...")
}

// Abstract class
abstract class Shape {
    abstract fun area(): Double
    abstract fun perimeter(): Double
}

class Circle(val radius: Double) : Shape() {
    override fun area() = Math.PI * radius * radius
    override fun perimeter() = 2 * Math.PI * radius
}

class Rectangle(val width: Double, val height: Double) : Shape() {
    override fun area() = width * height
    override fun perimeter() = 2 * (width + height)
}

class Triangle(val a: Double, val b: Double, val c: Double) : Shape() {
    val s = (a + b + c) / 2
    override fun area() = Math.sqrt(s * (s-a) * (s-b) * (s-c))
    override fun perimeter() = a + b + c
}

// Abstract vehicle
abstract class Vehicle(val make: String) {
    abstract fun start()
    abstract fun stop()
}

class ElectricCar(make: String, val batteryRange: Int) : Vehicle(make) {
    override fun start() = println("$make started")
    override fun stop() = println("$make stopped")
    fun charge() = println("Charging... Range: $batteryRange miles")
}

// Open class
open class BaseClass {
    open fun openMethod() = println("Base open method")
    fun finalMethod() = println("Base final method")
}

open class DerivedClass : BaseClass() {
    override fun openMethod() = println("Derived open method")
    open fun overriddenMethod() = println("Derived overridden")
}

// Super call
open class Parent {
    open fun parentMethod() = println("Parent method")
}

class Child : Parent() {
    override fun parentMethod() {
        super.parentMethod()
        println("Child method")
    }
    fun childMethod() = parentMethod()
}

// Interface inheritance
interface Turnable {
    fun turnOn()
    fun turnOff()
}

interface Callable {
    fun makeCall()
}

interface Camera {
    fun takePhoto()
}

class Smartphone(val model: String, val version: Int) : Turnable, Callable, Camera {
    override fun turnOn() = println("$model turning on")
    override fun turnOff() = println("$model turning off")
    override fun makeCall() = println("Making call from $model")
    override fun takePhoto() = println("Taking photo with $model")
}
