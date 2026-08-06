// C# LINQ - Where, Select, OrderBy, GroupBy

using System;
using System.Collections.Generic;
using System.Linq;

namespace LinqDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // Sample data
            List<int> numbers = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            
            List<string> names = new List<string>
            {
                "Alice", "Bob", "Charlie", "David", "Eve", "Frank"
            };
            
            List<Person> people = new List<Person>
            {
                new Person { Name = "Alice", Age = 30, City = "New York" },
                new Person { Name = "Bob", Age = 25, City = "Boston" },
                new Person { Name = "Charlie", Age = 35, City = "New York" },
                new Person { Name = "David", Age = 28, City = "Boston" },
                new Person { Name = "Eve", Age = 32, City = "Chicago" }
            };
            
            // Where - filtering
            Console.WriteLine("=== Where ===");
            var evenNumbers = numbers.Where(n => n % 2 == 0);
            Console.WriteLine($"Even: {string.Join(", ", evenNumbers)}");
            
            var longNames = names.Where(n => n.Length > 4);
            Console.WriteLine($"Long names: {string.Join(", ", longNames)}");
            
            // Select - projection
            Console.WriteLine("\n=== Select ===");
            var doubled = numbers.Select(n => n * 2);
            Console.WriteLine($"Doubled: {string.Join(", ", doubled)}");
            
            var upperNames = names.Select(n => n.ToUpper());
            Console.WriteLine($"Upper: {string.Join(", ", upperNames)}");
            
            // OrderBy - sorting
            Console.WriteLine("\n=== OrderBy ===");
            var sorted = numbers.OrderByDescending(n => n);
            Console.WriteLine($"Descending: {string.Join(", ", sorted)}");
            
            var sortedNames = names.OrderBy(n => n.Length);
            Console.WriteLine($"By length: {string.Join(", ", sortedNames)}");
            
            // GroupBy - grouping
            Console.WriteLine("\n=== GroupBy ===");
            var grouped = people.GroupBy(p => p.City);
            foreach (var group in grouped)
            {
                Console.WriteLine($"{group.Key}: {string.Join(", ", group.Select(p => p.Name))}");
            }
            
            // Chaining operations
            Console.WriteLine("\n=== Chaining ===");
            var result = people
                .Where(p => p.Age > 28)
                .OrderBy(p => p.Name)
                .Select(p => $"{p.Name} ({p.Age})");
            Console.WriteLine($"People over 28: {string.Join(", ", result)}");
            
            // Aggregate operations
            Console.WriteLine("\n=== Aggregates ===");
            Console.WriteLine($"Sum: {numbers.Sum()}");
            Console.WriteLine($"Average: {numbers.Average():F2}");
            Console.WriteLine($"Min: {numbers.Min()}");
            Console.WriteLine($"Max: {numbers.Max()}");
            Console.WriteLine($"Count: {numbers.Count}");
            
            // First, Last, Single
            Console.WriteLine($"\nFirst: {numbers.First()}");
            Console.WriteLine($"Last: {numbers.Last()}");
            Console.WriteLine($"First > 5: {numbers.First(n => n > 5)}");
            
            // Any, All, Contains
            Console.WriteLine($"\nAny > 5: {numbers.Any(n => n > 5)}");
            Console.WriteLine($"All > 0: {numbers.All(n => n > 0)}");
            Console.WriteLine($"Contains 5: {numbers.Contains(5)}");
            
            // Distinct
            List<int> duplicates = new List<int> { 1, 2, 2, 3, 3, 3 };
            Console.WriteLine($"\nDistinct: {string.Join(", ", duplicates.Distinct())}");
            
            // ToDictionary
            var dict = people.ToDictionary(p => p.Name, p => p.Age);
            Console.WriteLine($"\nDictionary: {string.Join(", ", dict.Select(kvp => $"{kvp.Key}={kvp.Value}"))}");
            
            Console.WriteLine("\nLINQ example running");
        }
    }
    
    class Person
    {
        public string Name { get; set; }
        public int Age { get; set; }
        public string City { get; set; }
    }
}