// C# Operators

using System;

namespace OperatorsDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // Arithmetic Operators
            int a = 10, b = 3;
            Console.WriteLine($"Addition: {a + b}");      // 13
            Console.WriteLine($"Subtraction: {a - b}");   // 7
            Console.WriteLine($"Multiplication: {a * b}"); // 30
            Console.WriteLine($"Division: {a / b}");      // 3 (integer division)
            Console.WriteLine($"Remainder: {a % b}");     // 1
            
            // Floating point division
            double x = 10.0, y = 3.0;
            Console.WriteLine($"Float Division: {x / y}"); // 3.333...
            
            // Increment/Decrement
            int counter = 5;
            counter++;
            Console.WriteLine($"After increment: {counter}"); // 6
            counter--;
            Console.WriteLine($"After decrement: {counter}"); // 5
            
            // Comparison Operators
            Console.WriteLine($"Equal: {a == b}");         // False
            Console.WriteLine($"Not Equal: {a != b}");     // True
            Console.WriteLine($"Greater: {a > b}");        // True
            Console.WriteLine($"Less: {a < b}");           // False
            Console.WriteLine($"Greater/Equal: {a >= b}"); // True
            Console.WriteLine($"Less/Equal: {a <= b}");    // False
            
            // Logical Operators
            bool p = true, q = false;
            Console.WriteLine($"AND: {p && q}");           // False
            Console.WriteLine($"OR: {p || q}");            // True
            Console.WriteLine($"NOT: {!p}");               // False
            
            // Bitwise Operators
            int m = 12, n = 10; // 1100 & 1010
            Console.WriteLine($"AND: {m & n}");            // 8 (1000)
            Console.WriteLine($"OR: {m | n}");             // 14 (1110)
            Console.WriteLine($"XOR: {m ^ n}");            // 6 (0110)
            Console.WriteLine($"Left Shift: {m << 2}");    // 48
            Console.WriteLine($"Right Shift: {m >> 2}");   // 3
            
            // Assignment Operators
            int val = 10;
            val += 5;   // val = val + 5
            val -= 3;   // val = val - 3
            val *= 2;   // val = val * 2
            val /= 4;   // val = val / 4
            
            // Ternary Operator
            int age = 20;
            string status = age >= 18 ? "Adult" : "Minor";
            Console.WriteLine($"Status: {status}");
            
            // Null-coalescing Operator
            string? nullStr = null;
            string result = nullStr ?? "default";
            Console.WriteLine($"Result: {result}");
            
            // Null-conditional Operator
            string? name = null;
            int? length = name?.Length;
            Console.WriteLine($"Length: {length ?? 0}");
            
            // Type checking Operators
            object obj = "hello";
            Console.WriteLine($"Is string: {obj is string}");
            Console.WriteLine($"As string: {(obj as string)?.ToUpper()}");
            
            Console.WriteLine("Operators example running");
        }
    }
}