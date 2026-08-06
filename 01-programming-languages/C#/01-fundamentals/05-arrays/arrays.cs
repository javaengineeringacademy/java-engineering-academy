// C# Arrays - Array, List, Dictionary

using System;
using System.Collections.Generic;

namespace ArraysDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // Arrays - fixed size
            Console.WriteLine("=== Arrays ===");
            int[] numbers = new int[5];
            numbers[0] = 1;
            numbers[1] = 2;
            numbers[2] = 3;
            numbers[3] = 4;
            numbers[4] = 5;
            
            string[] fruits = { "Apple", "Banana", "Cherry" };
            
            // Array methods
            Console.WriteLine($"Length: {fruits.Length}");
            Console.WriteLine($"First: {fruits[0]}");
            Console.WriteLine($"Last: {fruits[^1]}"); // Index from end
            
            // Array traversal
            Console.Write("Fruits: ");
            foreach (string fruit in fruits)
            {
                Console.Write($"{fruit} ");
            }
            Console.WriteLine();
            
            // Array.Sort
            Array.Sort(numbers);
            
            // Array.IndexOf
            int index = Array.IndexOf(fruits, "Banana");
            Console.WriteLine($"Banana index: {index}");
            
            // List<T> - dynamic size
            Console.WriteLine("\n=== List ===");
            List<string> names = new List<string>();
            names.Add("Alice");
            names.Add("Bob");
            names.Add("Charlie");
            
            // List methods
            Console.WriteLine($"Count: {names.Count}");
            Console.WriteLine($"Contains Bob: {names.Contains("Bob")}");
            
            names.Insert(1, "Bobby");
            names.Remove("Charlie");
            
            Console.Write("Names: ");
            foreach (string name in names)
            {
                Console.Write($"{name} ");
            }
            Console.WriteLine();
            
            // List with initial capacity
            List<int> largeList = new List<int>(1000);
            
            // List.Find
            string found = names.Find(name => name.StartsWith("B"));
            Console.WriteLine($"Found: {found}");
            
            // List.RemoveAll
            names.RemoveAll(name => name.Length > 5);
            
            // Dictionary<TKey, TValue> - key-value pairs
            Console.WriteLine("\n=== Dictionary ===");
            Dictionary<string, int> ages = new Dictionary<string, int>();
            ages["Alice"] = 30;
            ages["Bob"] = 25;
            ages["Charlie"] = 35;
            
            // Dictionary methods
            Console.WriteLine($"Alice's age: {ages["Alice"]}");
            Console.WriteLine($"Contains Bob: {ages.ContainsKey("Bob")}");
            Console.WriteLine($"Contains age 30: {ages.ContainsValue(30)}");
            
            // TryGetValue
            if (ages.TryGetValue("David", out int age))
            {
                Console.WriteLine($"David's age: {age}");
            }
            else
            {
                Console.WriteLine("David not found");
            }
            
            // Dictionary traversal
            Console.WriteLine("Ages:");
            foreach (KeyValuePair<string, int> kvp in ages)
            {
                Console.WriteLine($"  {kvp.Key}: {kvp.Value}");
            }
            
            // Remove from dictionary
            ages.Remove("Charlie");
            
            // Tuple
            Console.WriteLine("\n=== Tuples ===");
            var person = (Name: "Alice", Age: 30);
            Console.WriteLine($"{person.Name}: {person.Age}");
            
            // Deconstruction
            var (name, age) = person;
            Console.WriteLine($"Deconstructed: {name}, {age}");
            
            Console.WriteLine("Arrays example running");
        }
    }
}