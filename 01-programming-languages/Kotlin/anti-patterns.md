# Kotlin Anti-Patterns

## 1. Null Abuse
**Description:** Using nullable types excessively or forcing non-null unwrapping.

**Why it's bad:** Defeats Kotlin's null safety, can cause NPEs.

**Example (bad code):**
```kotlin
var name: String? = null
println(name!!.length) // Force unwrap - NPE risk

fun process(data: String?): String {
    return data!! // Force unwrap
}
```

**Better approach:** Use safe operators:
```kotlin
var name: String? = null
println(name?.length ?: 0)

fun process(data: String?): String {
    return data?.uppercase() ?: "default"
}
```

**Impact:** Null safety preserved, no NPEs.

---

## 2. Scope Function Abuse
**Description:** Overusing let, run, with, apply, also.

**Why it's bad:** Reduces readability, makes code harder to follow.

**Example (bad code):**
```kotlin
person.apply {
    name = "John".also { 
        println(it)
    }.run {
        uppercase()
    }.let {
        process(it)
    }
}
```

**Better approach:** Use scope functions judiciously:
```kotlin
val processedName = person.name?.uppercase() ?: "UNKNOWN"
println(processedName)
```

**Impact:** Clearer code, easier to understand.

---

## 3. Not Using Data Classes
**Description:** Creating regular classes for data holders.

**Why it's bad:** Misses auto-generated equals, hashCode, toString, copy.

**Example (bad code):**
```kotlin
class User(val name: String, val age: Int) {
    override fun equals(other: Any?): Boolean {
        // manual implementation
    }
    override fun hashCode(): Int {
        // manual implementation
    }
}
```

**Better approach:** Use data classes:
```kotlin
data class User(val name: String, val age: Int)
```

**Impact:** Less boilerplate, more functionality.

---

## 4. Ignoring Coroutine Cancellation
**Description:** Not checking for coroutine cancellation.

**Why it's bad:** Wasted resources, delayed shutdown.

**Example (bad code):**
```kotlin
suspend fun longTask() {
    while (true) {
        // No cancellation check
        process()
    }
}
```

**Better approach:** Check for cancellation:
```kotlin
suspend fun longTask() = coroutineScope {
    while (isActive) {
        process()
    }
}
```

**Impact:** Proper cancellation, resource cleanup.

---

## 5. Blocking Main Thread
**Description:** Running blocking operations on main dispatcher.

**Why it's bad:** ANR errors, unresponsive UI.

**Example (bad code):**
```kotlin
GlobalScope.launch(Dispatchers.Main) {
    val data = fetchData() // Blocking call
    updateUI(data)
}
```

**Better approach:** Use proper dispatcher:
```kotlin
lifecycleScope.launch {
    val data = withContext(Dispatchers.IO) {
        fetchData()
    }
    updateUI(data)
}
```

**Impact:** Responsive UI, no ANR.

---

## 6. Using !! Operator Excessively
**Description:** Using force unwrap (!!) instead of safe alternatives.

**Why it's bad:** Can cause NPEs, defeats null safety.

**Example (bad code):**
```kotlin
val user = getUser()
val email = user!!.email!!
println(email!!.length)
```

**Better approach:** Use safe calls:
```kotlin
val email = getUser()?.email ?: return
println(email.length)
```

**Impact:** Null safety preserved.

---

## 7. Not Using Sealed Classes
**Description:** Using enum or when without sealed classes for ADTs.

**Why it's bad:** Misses type safety, no compiler exhaustiveness checks.

**Example (bad code):**
```kotlin
sealed class Result
class Success(val data: String) : Result()
class Error(val message: String) : Result()

when (result) {
    is Success -> // handle
    is Error -> // handle
    // No else needed with sealed class
}
```

**Better approach:** Use sealed classes:
```kotlin
sealed class NetworkResult {
    data class Success(val data: String) : NetworkResult()
    data class Error(val message: String) : NetworkResult()
    object Loading : NetworkResult()
}
```

**Impact:** Type safety, exhaustive when.

---

## 8. Ignoring Coroutine Scope
**Description:** Not managing coroutine scope properly.

**Why it's bad:** Leaked coroutines, memory leaks.

**Example (bad code):**
```kotlin
GlobalScope.launch {
    while (true) {
        // Runs forever, never cancelled
    }
}
```

**Better approach:** Use structured concurrency:
```kotlin
lifecycleScope.launch {
    while (isActive) {
        // Cancelled when scope cancelled
    }
}
```

**Impact:** Proper lifecycle management.

---

## 9. Using var Unnecessarily
**Description:** Using var when val would suffice.

**Why it's bad:** Less predictable, harder to reason about.

**Example (bad code):**
```kotlin
var name = "John"
name = "Jane" // mutation

var list = mutableListOf(1, 2, 3)
list.add(4)
```

**Better approach:** Use val:
```kotlin
val name = "John"
val list = mutableListOf(1, 2, 3)
list.add(4)  // mutation is fine
```

**Impact:** Immutability by default.

---

## 10. Not Using Extension Functions
**description:** Creating utility classes instead of extensions.

**Why it's bad:** Less idiomatic, harder to discover.

**Example (bad code):**
```kotlin
class StringUtils {
    fun capitalize(s: String): String = s.uppercase()
}
```

**Better approach:** Use extensions:
```kotlin
fun String.capitalize(): String = this.uppercase()
```

**Impact:** More idiomatic, discoverable.

---

## 11. Ignoring Sequence for Large Collections
**Description:** Using eager operations on large collections.

**Why it's bad:** Creates intermediate collections, wastes memory.

**Example (bad code):**
```kotlin
val result = largeList
    .filter { it.isValid }
    .map { it.transform() }
    .take(10)
```

**Better approach:** Use sequences:
```kotlin
val result = largeList.asSequence()
    .filter { it.isValid }
    .map { it.transform() }
    .take(10)
    .toList()
```

**Impact:** Lazy evaluation, better performance.

---

## 12. Not Using Const
**Description:** Using val instead of const for compile-time constants.

**Why it's bad:** Runtime initialization instead of compile-time.

**Example (bad code):**
```kotlin
val MAX_SIZE = 100
val API_URL = "https://api.example.com"
```

**Better approach:** Use const:
```kotlin
const val MAX_SIZE = 100
const val API_URL = "https://api.example.com"
```

**Impact:** Compile-time constants, better performance.