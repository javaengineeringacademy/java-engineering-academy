using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CSharpFundamentals
{
    class Program
    {
        static async Task Main(string[] args)
        {
            // Variables
            string name = "C#";
            int version = 12;
            Console.WriteLine($"Language: {name}, Version: {version}");

            // Collections
            var numbers = new List<int> { 1, 2, 3, 4, 5 };
            var doubled = numbers.Select(x => x * 2).ToList();
            Console.WriteLine($"Doubled: {string.Join(", ", doubled)}");

            // Classes
            var person = new Person("Alice", 30);
            Console.WriteLine(person.Greet());

            // Async/Await
            await FetchDataAsync();

            // LINQ
            var names = new[] { "Alice", "Bob", "Charlie" };
            var filtered = names.Where(n => n.Length > 3).OrderBy(n => n);
            Console.WriteLine($"Filtered: {string.Join(", ", filtered)}");
        }

        static async Task FetchDataAsync()
        {
            await Task.Delay(1000);
            Console.WriteLine("Data loaded!");
        }
    }

    record Person(string Name, int Age)
    {
        public string Greet() => $"Hello, I'm {Name}!";
    }
}
