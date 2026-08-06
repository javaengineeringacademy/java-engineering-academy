fun main() {
    // If expression (returns a value)
    val x = 10
    val result = if (x > 5) "Greater than 5" else "Less than or equal to 5"
    println(result)

    // If as statement
    val score = 85
    if (score >= 90) {
        println("Grade: A")
    } else if (score >= 80) {
        println("Grade: B")
    } else if (score >= 70) {
        println("Grade: C")
    } else {
        println("Grade: F")
    }

    // When expression (like switch)
    val day = 3
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6, 7 -> "Weekend"
        else -> "Invalid day"
    }
    println("Day: $dayName")

    // When with ranges
    val temperature = 25
    val weather = when (temperature) {
        in Int.MIN_VALUE..0 -> "Freezing"
        in 1..15 -> "Cold"
        in 16..25 -> "Warm"
        in 26..Int.MAX_VALUE -> "Hot"
        else -> "Unknown"
    }
    println("Weather: $weather")

    // When with type checking
    val obj: Any = "Hello"
    when (obj) {
        is String -> println("String of length ${obj.length}")
        is Int -> println("Integer value: $obj")
        is List<*> -> println("List with ${obj.size} elements")
        else -> println("Unknown type")
    }

    // For loop with range
    print("For loop: ")
    for (i in 1..5) print("$i ")
    println()

    // For loop with step
    print("Even numbers: ")
    for (i in 2..10 step 2) print("$i ")
    println()

    // For loop with downTo
    print("Countdown: ")
    for (i in 5 downTo 1) print("$i ")
    println("Go!")

    // For loop with until
    print("Until: ")
    for (i in 0 until 5) print("$i ")
    println()

    // For loop with list
    val fruits = listOf("Apple", "Banana", "Cherry")
    for (fruit in fruits) {
        println("Fruit: $fruit")
    }

    // For loop with index
    for ((index, fruit) in fruits.withIndex()) {
        println("$index: $fruit")
    }

    // While loop
    var count = 0
    while (count < 5) {
        print("$count ")
        count++
    }
    println()

    // Do-while loop
    var num = 10
    do {
        print("$num ")
        num--
    } while (num > 0)
    println()

    // Break and continue
    for (i in 1..10) {
        if (i == 3) continue  // Skip 3
        if (i == 7) break    // Stop at 7
        print("$i ")
    }
    println()
}
