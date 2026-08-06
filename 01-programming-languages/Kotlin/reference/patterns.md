# Kotlin Design Patterns

## Overview
Common design patterns implemented in Kotlin.

## 1. Singleton Pattern
```kotlin
object Database {
    private val connections = mutableListOf<String>()
    
    fun connect(url: String) {
        connections.add(url)
    }
    
    fun disconnect(url: String) {
        connections.remove(url)
    }
}

Database.connect("jdbc:mysql://localhost/db")
```

## 2. Factory Pattern
```kotlin
interface Shape {
    fun area(): Double
}

class Circle(val radius: Double) : Shape {
    override fun area() = Math.PI * radius * radius
}

class Rectangle(val width: Double, val height: Double) : Shape {
    override fun area() = width * height
}

object ShapeFactory {
    fun create(type: String, vararg dimensions: Double): Shape {
        return when (type) {
            "circle" -> Circle(dimensions[0])
            "rectangle" -> Rectangle(dimensions[0], dimensions[1])
            else -> throw IllegalArgumentException("Unknown shape")
        }
    }
}

val circle = ShapeFactory.create("circle", 5.0)
```

## 3. Builder Pattern
```kotlin
class QueryBuilder private constructor() {
    private var table = ""
    private var conditions = mutableListOf<String>()
    private var columns = mutableListOf<String>()
    
    companion object {
        fun create() = QueryBuilder()
    }
    
    fun from(table: String) = apply { this.table = table }
    fun select(vararg cols: String) = apply { columns.addAll(cols) }
    fun where(condition: String) = apply { conditions.add(condition) }
    
    fun build(): String {
        val cols = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val where = if (conditions.isEmpty()) "" else " WHERE ${conditions.joinToString(" AND ")}"
        return "SELECT $cols FROM $table$where"
    }
}

val query = QueryBuilder.create()
    .from("users")
    .select("name", "email")
    .where("age > 18")
    .build()
```

## 4. Observer Pattern
```kotlin
interface Observer<T> {
    fun update(data: T)
}

class Subject<T> {
    private val observers = mutableListOf<Observer<T>>()
    
    fun subscribe(observer: Observer<T>) = observers.add(observer)
    fun unsubscribe(observer: Observer<T>) = observers.remove(observer)
    fun notify(data: T) = observers.forEach { it.update(data) }
}
```

## 5. Strategy Pattern
```kotlin
interface SortStrategy<T> {
    fun sort(data: List<T>): List<T>
}

class BubbleSort<T : Comparable<T>> : SortStrategy<T> {
    override fun sort(data: List<T>): List<T> = data.sorted()
}

class QuickSort<T : Comparable<T>> : SortStrategy<T> {
    override fun sort(data: List<T>): List<T> = data.sorted()
}

class Sorter<T>(private val strategy: SortStrategy<T>) {
    fun sort(data: List<T>): List<T> = strategy.sort(data)
}
```

## 6. Repository Pattern
```kotlin
interface Repository<T> {
    suspend fun getById(id: Int): T?
    suspend fun getAll(): List<T>
    suspend fun save(entity: T)
    suspend fun delete(id: Int)
}

class UserRepository : Repository<User> {
    private val users = mutableListOf<User>()
    
    override suspend fun getById(id: Int): User? {
        return users.find { it.id == id }
    }
    
    override suspend fun getAll(): List<User> = users.toList()
    
    override suspend fun save(entity: User) {
        users.add(entity)
    }
    
    override suspend fun delete(id: Int) {
        users.removeAll { it.id == id }
    }
}
```

## Key Takeaways
1. Use object for singletons
2. Use companion objects for factories
3. Use apply for builders
4. Use interfaces for strategies
5. Use sealed classes for states