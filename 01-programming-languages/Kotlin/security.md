# Kotlin Security

## Null Safety

Kotlin's null safety prevents null pointer exceptions at compile time.

```kotlin
// Non-nullable type - cannot be null
val name: String = "Alice"

// Nullable type - must handle null
val nullable: String? = null

// Safe calls
println(nullable?.length)

// Elvis operator
val length = nullable?.length ?: 0

// Force unwrap (use only when null is impossible)
val notNull: String = nullable!!
```

## Type Safety

The type system prevents many common bugs.

```kotlin
// Sealed classes for restricted hierarchies
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}

// Exhaustive when expressions
fun handle(result: Result) = when (result) {
    is Result.Success -> process(result.data)
    is Result.Error -> logError(result.message)
    // No else needed - compiler enforces completeness
}
```

## Input Validation

Validate all external input at boundaries.

```kotlin
data class UserInput(val name: String, val age: Int) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(age in 0..150) { "Age must be between 0 and 150" }
    }
}

// Validation functions
fun validateEmail(email: String): Boolean {
    val pattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return pattern.matches(email)
}
```

## ProGuard and R8

Minify and obfuscate Kotlin code for Android and JVM.

```proguard
# proguard-rules.pro
-keepattributes Signature
-keepattributes *Annotation*

# Keep data classes for serialization
-keep class com.example.models.** { *; }

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Obfuscate package names
-repackageclasses ''
-allowaccessmodification
```

## Serialization Security

```kotlin
// Use kotlinx.serialization with explicit configuration
@Serializable
data class User(val name: String, val age: Int)

// Configure JSON to fail on unknown keys
val json = Json {
    ignoreUnknownKeys = false
    coerceInputValues = true
}

// Deserialize safely
val user = json.decodeFromString<User>(jsonString)
```

## Permission Checks

```kotlin
// Android permission handling
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    == PackageManager.PERMISSION_GRANTED) {
    openCamera()
} else {
    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
}
```

## Secure Storage

```kotlin
// Android Keystore for sensitive data
val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
keyGenerator.init(KeyGenParameterSpec.Builder(
    "alias",
    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
 .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
 .build())
```
