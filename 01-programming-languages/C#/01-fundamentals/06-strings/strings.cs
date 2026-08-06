// C# Strings

using System;

namespace StringsDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // String creation
            string s1 = "Hello";
            string s2 = new string('A', 5); // "AAAAA"
            string s3 = string.Concat("Hello", " ", "World");
            
            // String properties
            Console.WriteLine($"Length: {s1.Length}");
            
            // String methods
            Console.WriteLine($"Upper: {s1.ToUpper()}");
            Console.WriteLine($"Lower: {s1.ToLower()}");
            Console.WriteLine($"Trim: {"  hello  ".Trim()}");
            Console.WriteLine($"Substring: {s1.Substring(1, 3)}");
            Console.WriteLine($"Contains: {s1.Contains("ell")}");
            Console.WriteLine($"Starts with: {s1.StartsWith("He")}");
            Console.WriteLine($"Ends with: {s1.EndsWith("lo")}");
            
            // String formatting
            string name = "Alice";
            int age = 30;
            
            // String interpolation
            Console.WriteLine($"Name: {name}, Age: {age}");
            
            // Composite formatting
            Console.WriteLine("Name: {0}, Age: {1}", name, age);
            
            // String.Format
            string formatted = string.Format("Name: {0}, Age: {1}", name, age);
            
            // String concatenation
            string greeting = "Hello, " + name + "!";
            
            // StringBuilder (efficient for many operations)
            System.Text.StringBuilder sb = new System.Text.StringBuilder();
            sb.Append("Hello");
            sb.Append(" ");
            sb.Append("World");
            string result = sb.ToString();
            
            // String split
            string csv = "apple,banana,cherry";
            string[] fruits = csv.Split(',');
            Console.WriteLine($"Fruits: {string.Join(", ", fruits)}");
            
            // String join
            string joined = string.Join(" - ", fruits);
            Console.WriteLine($"Joined: {joined}");
            
            // String replace
            string text = "Hello World";
            string replaced = text.Replace("World", "C#");
            Console.WriteLine($"Replaced: {replaced}");
            
            // String comparison
            string a = "apple";
            string b = "banana";
            Console.WriteLine($"Compare: {string.Compare(a, b)}");
            Console.WriteLine($"Equals: {a.Equals(b)}");
            
            // Verbatim string
            string path = @"C:\Users\Alice\Documents";
            Console.WriteLine($"Path: {path}");
            
            // Raw string literal (C# 11+)
            // string json = """
            // {
            //     "name": "Alice"
            // }
            // """;
            
            // String interpolation in verbatim
            string filePath = $@"C:\Users\{name}\Documents";
            
            // String.Contains with StringComparison
            Console.WriteLine($"Contains: {"Hello World".Contains("hello", StringComparison.OrdinalIgnoreCase)}");
            
            // String.IsNullOrEmpty
            string? nullStr = null;
            Console.WriteLine($"Is null or empty: {string.IsNullOrEmpty(nullStr)}");
            
            // String.IsNullOrWhiteSpace
            string whitespace = "   ";
            Console.WriteLine($"Is null or whitespace: {string.IsNullOrWhiteSpace(whitespace)}");
            
            Console.WriteLine("Strings example running");
        }
    }
}