// C# Methods - params, ref, out, named

using System;

namespace MethodsDemo
{
    class Program
    {
        // Basic method
        static int Add(int a, int b)
        {
            return a + b;
        }
        
        // Method with default parameters
        static string Greet(string name, string greeting = "Hello")
        {
            return $"{greeting}, {name}!";
        }
        
        // Params array
        static int Sum(params int[] numbers)
        {
            int total = 0;
            foreach (int num in numbers)
            {
                total += num;
            }
            return total;
        }
        
        // Ref parameter (passed by reference)
        static void DoubleValue(ref int value)
        {
            value *= 2;
        }
        
        // Out parameter (must be assigned)
        static void Divide(int a, int b, out int quotient, out int remainder)
        {
            quotient = a / b;
            remainder = a % b;
        }
        
        // Multiple return values with tuples
        static (int min, int max) FindMinMax(int[] numbers)
        {
            int min = numbers[0];
            int max = numbers[0];
            
            foreach (int num in numbers)
            {
                if (num < min) min = num;
                if (num > max) max = num;
            }
            
            return (min, max);
        }
        
        // Named method
        static bool IsEven(int number)
        {
            return number % 2 == 0;
        }
        
        // Method overloading
        static double CalculateArea(double radius)
        {
            return Math.PI * radius * radius;
        }
        
        static double CalculateArea(double width, double height)
        {
            return width * height;
        }
        
        // Local functions
        static void ProcessNumbers(int[] numbers)
        {
            // Local function
            bool IsValid(int num) => num > 0;
            
            foreach (int num in numbers)
            {
                if (IsValid(num))
                {
                    Console.WriteLine($"Valid: {num}");
                }
            }
        }
        
        static void Main(string[] args)
        {
            // Basic method call
            Console.WriteLine($"Add: {Add(5, 3)}");
            
            // Named arguments
            Console.WriteLine(Greet(greeting: "Hi", name: "Alice"));
            
            // Params array
            Console.WriteLine($"Sum: {Sum(1, 2, 3, 4, 5)}");
            
            // Ref parameter
            int value = 10;
            Console.WriteLine($"Before: {value}");
            DoubleValue(ref value);
            Console.WriteLine($"After: {value}");
            
            // Out parameter
            int a = 17, b = 5;
            Divide(a, b, out int quotient, out int remainder);
            Console.WriteLine($"{a} / {b} = {quotient} remainder {remainder}");
            
            // Tuple return
            int[] numbers = { 3, 1, 4, 1, 5, 9, 2, 6 };
            var (min, max) = FindMinMax(numbers);
            Console.WriteLine($"Min: {min}, Max: {max}");
            
            // Named method
            Console.WriteLine($"Is 4 even? {IsEven(4)}");
            
            // Method overloading
            Console.WriteLine($"Circle area: {CalculateArea(5.0):F2}");
            Console.WriteLine($"Rectangle area: {CalculateArea(4.0, 6.0):F2}");
            
            // Local functions
            ProcessNumbers(new[] { -2, 5, -1, 8, 3 });
            
            Console.WriteLine("Methods example running");
        }
    }
}