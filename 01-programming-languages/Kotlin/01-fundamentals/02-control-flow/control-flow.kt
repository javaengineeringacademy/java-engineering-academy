// Kotlin Control Flow - if, when, for, while

fun main() {
    // if expression
    val x = 10
    val y = 20
    
    // if as expression
    val max = if (x > y) x else y
    println("Max: $max")
    
    // if with blocks
    val result = if (x > y) {
        println("x is greater")
        x
    } else {
        println("y is greater")
        y
    }
    println("Result: $result")
    
    // when expression (like switch)
    val day = "Monday"
    val dayType = when (day) {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "Weekday"
        "Saturday", "Sunday" -> "Weekend"
        else -> "Unknown"
    }
    println("$day is a $dayType")
    
    // when with conditions
    val temperature = 25
    val weather = when {
        temperature > 30 -> "Hot"
        temperature > 20 -> "Warm"
        temperature > 10 -> "Cool"
        else -> "Cold"
    }
    println("Weather: $weather")
    
    // when with any type
    val obj: Any = 42
    val description = when (obj) {
        is Int -> "Integer: $obj"
        is String -> "String: $obj"
        is Boolean -> "Boolean: $obj"
        else -> "Unknown type"
    }
    println(description)
    
    // for loop
    print("For loop: ")
    for (i in 1..5) {
        print("$i ")
    }
    println()
    
    // for with step
    print("With step: ")
    for (i in 0..10 step 2) {
        print("$i ")
    }
    println()
    
    // for with downTo
    print("Down to: ")
    for (i in 10 downTo 1) {
        print("$i ")
    }
    println()
    
    // for with until
    print("Until: ")
    for (i in 0 until 5) {
        print("$i ")
    }
    println()
    
    // forEach
    print("ForEach: ")
    (1..5).forEach { print("$it ") }
    println()
    
    // while loop
    print("While: ")
    var count = 0
    while (count < 5) {
        print("$count ")
        count++
    }
    println()
    
    // do-while loop
    print("Do-while: ")
    count = 0
    do {
        print("$count ")
        count++
    } while (count < 5)
    println()
    
    // break and continue
    print("Break at 3: ")
    for (i in 1..5) {
        if (i == 3) break
        print("$i ")
    }
    println()
    
    print("Skip even: ")
    for (i in 1..10) {
        if (i % 2 == 0) continue
        print("$i ")
    }
    println()
    
    // Labeled breaks
    print("Labeled break: ")
    outer@ for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) continue@outer
            print("($i,$j) ")
        }
    }
    println()
    
    println("Control flow example running")
}