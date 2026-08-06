// C# Delegates - Func, Action, events

using System;

namespace DelegatesDemo
{
    // Custom delegate
    public delegate int MathOperation(int a, int b);
    
    // Event delegate
    public delegate void EventHandler(string message);
    
    class Program
    {
        // Event publisher
        public class OrderService
        {
            public event EventHandler OnOrderCreated;
            public event EventHandler OnOrderProcessed;
            
            public void CreateOrder(string orderId)
            {
                Console.WriteLine($"Creating order {orderId}");
                OnOrderCreated?.Invoke($"Order {orderId} created");
            }
            
            public void ProcessOrder(string orderId)
            {
                Console.WriteLine($"Processing order {orderId}");
                OnOrderProcessed?.Invoke($"Order {orderId} processed");
            }
        }
        
        // Event subscriber
        public class OrderLogger
        {
            public void Log(string message)
            {
                Console.WriteLine($"[LOG] {message}");
            }
        }
        
        public class OrderNotifier
        {
            public void Notify(string message)
            {
                Console.WriteLine($"[NOTIFY] {message}");
            }
        }
        
        static void Main(string[] args)
        {
            // Custom delegate
            Console.WriteLine("=== Custom Delegate ===");
            MathOperation add = (a, b) => a + b;
            MathOperation multiply = (a, b) => a * b;
            
            Console.WriteLine($"Add: {add(5, 3)}");
            Console.WriteLine($"Multiply: {multiply(5, 3)}");
            
            // Func delegate (returns value)
            Console.WriteLine("\n=== Func Delegate ===");
            Func<int, int, int> sum = (a, b) => a + b;
            Func<string, int> length = s => s.Length;
            
            Console.WriteLine($"Sum: {sum(5, 3)}");
            Console.WriteLine($"Length: {length("Hello")}");
            
            // Action delegate (no return)
            Console.WriteLine("\n=== Action Delegate ===");
            Action<string> print = message => Console.WriteLine(message);
            Action<int, int> printPair = (a, b) => Console.WriteLine($"{a}, {b}");
            
            print("Hello, World!");
            printPair(5, 3);
            
            // Predicate delegate (returns bool)
            Console.WriteLine("\n=== Predicate Delegate ===");
            Predicate<int> isEven = n => n % 2 == 0;
            Predicate<string> isLong = s => s.Length > 5;
            
            Console.WriteLine($"4 is even: {isEven(4)}");
            Console.WriteLine($"Hello is long: {isLong("Hello")}");
            
            // Delegate as method parameter
            Console.WriteLine("\n=== Delegate as Parameter ===");
            int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            int[] evens = Array.FindAll(numbers, isEven);
            Console.WriteLine($"Evens: {string.Join(", ", evens)}");
            
            // Multicast delegate
            Console.WriteLine("\n=== Multicast Delegate ===");
            Action<string> combined = PrintToConsole;
            combined += PrintToFile;
            combined("Multicast message");
            
            // Events
            Console.WriteLine("\n=== Events ===");
            OrderService orderService = new OrderService();
            OrderLogger logger = new OrderLogger();
            OrderNotifier notifier = new OrderNotifier();
            
            // Subscribe to events
            orderService.OnOrderCreated += logger.Log;
            orderService.OnOrderCreated += notifier.Notify;
            orderService.OnOrderProcessed += logger.Log;
            
            // Trigger events
            orderService.CreateOrder("ORD-001");
            orderService.ProcessOrder("ORD-001");
            
            // Unsubscribe
            orderService.OnOrderCreated -= notifier.Notify;
            
            // Lambda with closure
            Console.WriteLine("\n=== Lambda with Closure ===");
            int multiplier = 3;
            Func<int, int> multiplyBy = n => n * multiplier;
            Console.WriteLine($"5 * {multiplier} = {multiplyBy(5)}");
            
            // Higher-order functions
            Console.WriteLine("\n=== Higher-Order Functions ===");
            var operations = new System.Collections.Generic.Dictionary<string, Func<int, int, int>>
            {
                ["add"] = (a, b) => a + b,
                ["subtract"] = (a, b) => a - b,
                ["multiply"] = (a, b) => a * b
            };
            
            foreach (var op in operations)
            {
                Console.WriteLine($"{op.Key}(5, 3) = {op.Value(5, 3)}");
            }
            
            Console.WriteLine("\nDelegates example running");
        }
        
        static void PrintToConsole(string message)
        {
            Console.WriteLine($"[Console] {message}");
        }
        
        static void PrintToFile(string message)
        {
            // Simulating file write
            Console.WriteLine($"[File] {message}");
        }
    }
}