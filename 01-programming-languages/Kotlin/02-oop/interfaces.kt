fun main() {
    // Basic interface
    val circle = DrawableCircle(5.0)
    val rect = DrawableRectangle(4.0, 6.0)
    circle.draw()
    rect.draw()

    // Interface with properties
    val vehicle = Car("Toyota", 120)
    println("Vehicle: ${vehicle.name}, Speed: ${vehicle.speed}")
    vehicle.start()
    vehicle.stop()

    // Multiple interfaces
    val robot = Robot()
    robot.speak()
    robot.move()

    // Interface delegation
    val loggingService = LoggingService(UserRepository())
    loggingService.save("data")
    loggingService.find(1)

    // Interface with default implementation
    val printer = LaserPrinter()
    printer.print("Document")
    printer.scan()
    printer.fax()

    // Functional interface (SAM)
    val clickListener = View.OnClickListener { println("View clicked: $it") }
    clickListener.onClick("Button")

    // Interface inheritance
    val animal = Dog("Rex")
    animal.eat()
    animal.sleep()
    animal.fetch()

    // Property in interface
    val countable = Counter()
    println("Count: ${countable.count}")
    countable.increment()
    println("After increment: ${countable.count}")
}

// Basic interface
interface Drawable {
    fun draw()
    val color: String
        get() = "Default"
}

class DrawableCircle(val radius: Double) : Drawable {
    override fun draw() = println("Drawing circle with radius $radius")
    override val color: String = "Red"
}

class DrawableRectangle(val width: Double, val height: Double) : Drawable {
    override fun draw() = println("Drawing rectangle ${width}x$height")
}

// Interface with properties
interface Vehicle {
    val name: String
    val speed: Int
    fun start()
    fun stop()
}

class Car(override val name: String, override val speed: Int) : Vehicle {
    override fun start() = println("$name starting...")
    override fun stop() = println("$name stopped")
}

// Multiple interfaces
interface Speakable {
    fun speak()
}

interface Movable {
    fun move()
}

class Robot : Speakable, Movable {
    override fun speak() = println("Robot speaking")
    override fun move() = println("Robot moving")
}

// Interface delegation
interface Repository {
    fun save(data: String)
    fun find(id: Int): String
}

class UserRepository : Repository {
    override fun save(data: String) = println("Saving: $data")
    override fun find(id: Int): String = "Data for $id"
}

class LoggingService(repo: Repository) : Repository by repo {
    override fun save(data: String) {
        println("Logging before save")
        super.save(data)
    }
}

// Interface with default implementation
interface Printer {
    fun print(document: String) = println("Printing: $document")
}

interface Scanner {
    fun scan() = println("Scanning...")
}

interface Fax {
    fun fax() = println("Faxing...")
}

class LaserPrinter : Printer, Scanner, Fax

// Functional interface
interface OnClickListener {
    fun onClick(view: String)
}

object View {
    fun interface OnClickListener {
        fun onClick(view: String)
    }
}

// Interface inheritance
interface Animal {
    fun eat()
    fun sleep()
}

interface Pet : Animal {
    fun fetch()
}

class Dog(val name: String) : Pet {
    override fun eat() = println("$name eating")
    override fun sleep() = println("$name sleeping")
    override fun fetch() = println("$name fetching")
}

// Property in interface
interface Countable {
    var count: Int
        get() = 0
        set(value) {}
}

class Counter : Countable {
    override var count: Int = 0
    fun increment() = count++
}
