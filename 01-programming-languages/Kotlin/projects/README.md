# Kotlin Projects

This section provides project ideas to practice and apply Kotlin knowledge.

## Table of Contents

- [Beginner Projects](#beginner-projects)
- [Intermediate Projects](#intermediate-projects)
- [Advanced Projects](#advanced-projects)
- [Project Ideas by Topic](#project-ideas-by-topic)
- [Resources](#resources)

## Beginner Projects

### 1. Todo Application

```kotlin
// Features:
// - Add, edit, delete tasks
// - Mark tasks as complete
// - Filter tasks by status
// - Save/load tasks from file

// Implementation
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

class TodoManager {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1
    
    fun addTask(title: String, description: String): Task {
        val task = Task(nextId++, title, description)
        tasks.add(task)
        return task
    }
    
    fun completeTask(id: Int): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks[index] = tasks[index].copy(isCompleted = true)
            return true
        }
        return false
    }
    
    fun getTasks(): List<Task> = tasks.toList()
}
```

### 2. Calculator Application

```kotlin
// Features:
// - Basic arithmetic operations
// - History of calculations
// - Support for parentheses
// - Unit conversion

// Implementation
class Calculator {
    private val history = mutableListOf<String>()
    
    fun calculate(expression: String): Double {
        // Parse and evaluate expression
        val result = evaluateExpression(expression)
        history.add("$expression = $result")
        return result
    }
    
    private fun evaluateExpression(expression: String): Double {
        // Simple implementation
        return when {
            expression.contains("+") -> {
                val parts = expression.split("+")
                parts[0].toDouble() + parts[1].toDouble()
            }
            expression.contains("-") -> {
                val parts = expression.split("-")
                parts[0].toDouble() - parts[1].toDouble()
            }
            else -> expression.toDouble()
        }
    }
    
    fun getHistory(): List<String> = history.toList()
}
```

### 3. Contact Book

```kotlin
// Features:
// - Add, edit, delete contacts
// - Search contacts by name
// - Group contacts by category
// - Export contacts to CSV

// Implementation
data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val category: String = "General"
)

class ContactBook {
    private val contacts = mutableListOf<Contact>()
    private var nextId = 1
    
    fun addContact(name: String, phone: String, email: String): Contact {
        val contact = Contact(nextId++, name, phone, email)
        contacts.add(contact)
        return contact
    }
    
    fun searchContacts(query: String): List<Contact> {
        return contacts.filter { 
            it.name.contains(query, ignoreCase = true) 
        }
    }
    
    fun getContactsByCategory(category: String): List<Contact> {
        return contacts.filter { it.category == category }
    }
}
```

## Intermediate Projects

### 4. REST API Client

```kotlin
// Features:
// - Make HTTP requests
// - Parse JSON responses
// - Handle authentication
// - Retry logic

// Implementation
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String? = null
)

class ApiClient(private val baseUrl: String) {
    private val json = Json { ignoreUnknownKeys = true }
    
    suspend inline fun <reified T> get(endpoint: String): ApiResponse<T> {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseUrl$endpoint")
            val connection = url.openConnection() as HttpURLConnection
            
            try {
                val response = connection.inputStream.bufferedReader().readText()
                json.decodeFromString<ApiResponse<T>>(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }
    
    suspend inline fun <reified T> post(endpoint: String, body: Any): ApiResponse<T> {
        return withContext(Dispatchers.IO) {
            val url = URL("$baseUrl$endpoint")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            
            try {
                val outputStream = connection.outputStream
                outputStream.write(json.encodeToString(body).toByteArray())
                
                val response = connection.inputStream.bufferedReader().readText()
                json.decodeFromString<ApiResponse<T>>(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }
}
```

### 5. Chat Application

```kotlin
// Features:
// - Real-time messaging
// - User authentication
// - Message history
// - Online status

// Implementation
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

data class Message(
    val sender: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatServer {
    private val channels = mutableMapOf<String, Channel<Message>>()
    private val users = mutableMapOf<String, Boolean>()
    
    suspend fun joinRoom(roomId: String, userId: String) {
        val channel = channels.getOrPut(roomId) { Channel(Channel.BUFFERED) }
        users[userId] = true
        
        launch {
            for (message in channel) {
                // Broadcast to all users in room
                broadcastToRoom(roomId, message)
            }
        }
    }
    
    suspend fun sendMessage(roomId: String, message: Message) {
        channels[roomId]?.send(message)
    }
    
    private suspend fun broadcastToRoom(roomId: String, message: Message) {
        // Implementation for broadcasting
    }
}
```

### 6. File Processor

```kotlin
// Features:
// - Read/write files
// - Process CSV, JSON, XML
// - Data transformation
// - Export results

// Implementation
import java.io.File

class FileProcessor {
    fun readCsv(filePath: String): List<Map<String, String>> {
        val lines = File(filePath).readLines()
        val headers = lines.first().split(",")
        
        return lines.drop(1).map { line ->
            val values = line.split(",")
            headers.zip(values).toMap()
        }
    }
    
    fun processCsv(
        filePath: String,
        processor: (Map<String, String>) -> Map<String, String>
    ): List<Map<String, String>> {
        return readCsv(filePath).map(processor)
    }
    
    fun writeCsv(
        filePath: String,
        data: List<Map<String, String>>
    ) {
        if (data.isEmpty()) return
        
        val headers = data.first().keys.joinToString(",")
        val rows = data.map { row ->
            row.values.joinToString(",")
        }
        
        File(filePath).writeText(headers + "\n" + rows.joinToString("\n"))
    }
}
```

## Advanced Projects

### 7. Web Crawler

```kotlin
// Features:
// - Crawl websites
// - Extract links
// - Respect robots.txt
// - Rate limiting

// Implementation
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import org.jsoup.Jsoup

class WebCrawler(private val maxConcurrent: Int = 10) {
    private val visited = mutableSetOf<String>()
    private val semaphore = Semaphore(maxConcurrent)
    
    suspend fun crawl(url: String, depth: Int = 0) {
        if (depth > 3 || url in visited) return
        
        visited.add(url)
        
        withContext(Dispatchers.IO) {
            semaphore.withPermit {
                try {
                    val document = Jsoup.connect(url).get()
                    val links = document.select("a[href]").map { 
                        it.attr("abs:href") 
                    }
                    
                    links.forEach { link ->
                        launch {
                            crawl(link, depth + 1)
                        }
                    }
                } catch (e: Exception) {
                    println("Error crawling $url: ${e.message}")
                }
            }
        }
    }
}
```

### 8. Image Processing Pipeline

```kotlin
// Features:
// - Load/save images
// - Apply filters
// - Resize/crop
// - Batch processing

// Implementation
import kotlinx.coroutines.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

class ImageProcessor {
    suspend fun processImage(
        inputPath: String,
        outputPath: String,
        operations: List<Operation>
    ) = withContext(Dispatchers.IO) {
        val image = ImageIO.read(File(inputPath))
        val processed = operations.fold(image) { img, op ->
            op.apply(img)
        }
        ImageIO.write(processed, "jpg", File(outputPath))
    }
    
    suspend fun batchProcess(
        inputDir: String,
        outputDir: String,
        operations: List<Operation>
    ) = coroutineScope {
        val files = File(inputDir).listFiles()?.filter { 
            it.extension.lowercase() in listOf("jpg", "png") 
        } ?: emptyArray()
        
        files.map { file ->
            async {
                val outputPath = "$outputDir/${file.name}"
                processImage(file.absolutePath, outputPath, operations)
            }
        }.awaitAll()
    }
}

interface Operation {
    fun apply(image: BufferedImage): BufferedImage
}
```

### 9. Database ORM

```kotlin
// Features:
// - Table definitions
// - CRUD operations
// - Query builder
// - Transaction support

// Implementation
import java.sql.Connection
import java.sql.DriverManager

class Database(private val url: String) {
    private val connection: Connection = DriverManager.getConnection(url)
    
    fun createTable(tableName: String, columns: Map<String, String>) {
        val columnDefinitions = columns.entries.joinToString(", ") {
            "${it.key} ${it.value}"
        }
        val sql = "CREATE TABLE IF NOT EXISTS $tableName ($columnDefinitions)"
        connection.createStatement().execute(sql)
    }
    
    fun insert(tableName: String, data: Map<String, Any>) {
        val columns = data.keys.joinToString(", ")
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = "INSERT INTO $tableName ($columns) VALUES ($placeholders)"
        
        val statement = connection.prepareStatement(sql)
        data.values.forEachIndexed { index, value ->
            statement.setObject(index + 1, value)
        }
        statement.executeUpdate()
    }
    
    fun query(tableName: String, where: String? = null): List<Map<String, Any>> {
        val sql = "SELECT * FROM $tableName${if (where != null) " WHERE $where" else ""}"
        val resultSet = connection.createStatement().executeQuery(sql)
        
        val results = mutableListOf<Map<String, Any>>()
        while (resultSet.next()) {
            val row = mutableMapOf<String, Any>()
            for (i in 1..resultSet.metaData.columnCount) {
                row[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
            }
            results.add(row)
        }
        return results
    }
}
```

### 10. Compiler/Interpreter

```kotlin
// Features:
// - Lexer
// - Parser
// - AST
// - Interpreter

// Implementation
sealed class Token {
    data class Number(val value: Double) : Token()
    data class Identifier(val name: String) : Token()
    object Plus : Token()
    object Minus : Token()
    object Multiply : Token()
    object Divide : Token()
    object LParen : Token()
    object RParen : Token()
}

class Lexer(private val input: String) {
    private var position = 0
    
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        
        while (position < input.length) {
            when (val char = input[position]) {
                ' ' -> position++
                '+' -> { tokens.add(Token.Plus); position++ }
                '-' -> { tokens.add(Token.Minus); position++ }
                '*' -> { tokens.add(Token.Multiply); position++ }
                '/' -> { tokens.add(Token.Divide); position++ }
                '(' -> { tokens.add(Token.LParen); position++ }
                ')' -> { tokens.add(Token.RParen); position++ }
                in '0'..'9' -> {
                    val start = position
                    while (position < input.length && input[position] in '0'..'9') {
                        position++
                    }
                    tokens.add(Token.Number(input.substring(start, position).toDouble()))
                }
                in 'a'..'z', in 'A'..'Z' -> {
                    val start = position
                    while (position < input.length && input[position].isLetterOrDigit()) {
                        position++
                    }
                    tokens.add(Token.Identifier(input.substring(start, position)))
                }
            }
        }
        
        return tokens
    }
}
```

## Project Ideas by Topic

### Coroutines and Concurrency

```kotlin
// 1. Web scraper with rate limiting
// 2. Chat server with WebSocket
// 3. Parallel data processor
// 4. Real-time dashboard
// 5. Video streaming server
```

### Collections and Data Structures

```kotlin
// 1. LRU Cache implementation
// 2. Trie data structure
// 3. Graph algorithms
// 4. Sorting algorithm visualizer
// 5. Data structure playground
```

### Networking

```kotlin
// 1. REST API client
// 2. WebSocket client
// 3. FTP client
// 4. DNS resolver
// 5. Network scanner
```

### File I/O

```kotlin
// 1. File sync tool
// 2. Backup system
// 3. Log analyzer
// 4. CSV processor
// 5. Image organizer
```

### Testing

```kotlin
// 1. Test runner framework
// 2. Mocking library
// 3. Coverage reporter
// 4. Benchmark tool
// 5. Fuzzing tool
```

## Resources

### Learning Resources

```kotlin
// Official Resources
// - Kotlin Documentation: https://kotlinlang.org/docs/home.html
// - Kotlin Koans: https://play.kotlinlang.org/koans/
// - Kotlin Examples: https://play.kotlinlang.org/byExample/overview

// Books
// - "Kotlin in Action" by Dmitry Jemerov
// - "Kotlin Programming" by Brett Slatkin
// - "Kotlin Coroutines" by Marcin Moskala

// Online Courses
// - Coursera: Kotlin for Java Developers
// - Udemy: Kotlin Multiplatform
// - Pluralsight: Kotlin Fundamentals
```

### Project Templates

```kotlin
// Gradle project setup
// build.gradle.kts
plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jsoup:jsoup:1.17.1")
    
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}

tasks.test {
    useJUnit()
}

kotlin {
    jvmToolchain(17)
}
```

## Summary

Project ideas for practicing Kotlin:

- **Beginner**: Todo, Calculator, Contact Book
- **Intermediate**: API Client, Chat App, File Processor
- **Advanced**: Web Crawler, Image Processor, Database ORM, Compiler
- **By Topic**: Coroutines, Collections, Networking, File I/O, Testing

Start with beginner projects and gradually move to more complex ones. Focus on applying the concepts learned in previous sections.
