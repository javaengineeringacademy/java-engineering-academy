// C# Control Flow - Conditionals

using System;

namespace ConditionalsDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // if-else statement
            int temperature = 25;
            
            if (temperature > 30)
            {
                Console.WriteLine("Hot weather");
            }
            else if (temperature > 20)
            {
                Console.WriteLine("Warm weather");
            }
            else if (temperature > 10)
            {
                Console.WriteLine("Cool weather");
            }
            else
            {
                Console.WriteLine("Cold weather");
            }
            
            // Nested if
            bool hasLicense = true;
            int age = 25;
            
            if (age >= 18)
            {
                if (hasLicense)
                {
                    Console.WriteLine("Can drive");
                }
                else
                {
                    Console.WriteLine("Need license");
                }
            }
            
            // Ternary operator
            string dayType = (DateTime.Now.DayOfWeek == DayOfWeek.Saturday || 
                            DateTime.Now.DayOfWeek == DayOfWeek.Sunday) 
                            ? "Weekend" : "Weekday";
            Console.WriteLine($"Today is: {dayType}");
            
            // switch statement
            string command = "start";
            
            switch (command)
            {
                case "start":
                    Console.WriteLine("Starting process");
                    break;
                case "stop":
                    Console.WriteLine("Stopping process");
                    break;
                case "pause":
                    Console.WriteLine("Pausing process");
                    break;
                default:
                    Console.WriteLine("Unknown command");
                    break;
            }
            
            // Switch with pattern matching
            object obj = 42;
            
            switch (obj)
            {
                case int i when i > 0:
                    Console.WriteLine($"Positive integer: {i}");
                    break;
                case int i:
                    Console.WriteLine($"Non-positive integer: {i}");
                    break;
                case string s:
                    Console.WriteLine($"String: {s}");
                    break;
                case null:
                    Console.WriteLine("Null value");
                    break;
            }
            
            // Switch expression (C# 8.0+)
            string season = DateTime.Now.Month switch
            {
                12 or 1 or 2 => "Winter",
                3 or 4 or 5 => "Spring",
                6 or 7 or 8 => "Summer",
                9 or 10 or 11 => "Fall",
                _ => "Unknown"
            };
            Console.WriteLine($"Current season: {season}");
            
            // Null-coalescing operator
            string? input = null;
            string output = input ?? "default value";
            Console.WriteLine($"Output: {output}");
            
            // Null-conditional operator
            string? name = null;
            int length = name?.Length ?? 0;
            Console.WriteLine($"Name length: {length}");
            
            Console.WriteLine("Conditionals example running");
        }
    }
}