// C# Types - Value and Reference Types

using System;

namespace TypesDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // Value Types
            // Boolean
            bool isActive = true;
            
            // Integer types
            byte smallNum = 255;
            short shortNum = 32767;
            int number = 1000000;
            long bigNumber = 9876543210L;
            
            // Floating point
            float floatNum = 3.14f;
            double doubleNum = 3.14159265358979;
            decimal decimalNum = 19.99m;
            
            // Character
            char letter = 'A';
            
            // Struct types
            DateTime now = DateTime.Now;
            TimeSpan duration = TimeSpan.FromHours(2);
            Guid id = Guid.NewGuid();
            
            // Nullable value types
            int? nullableInt = null;
            double? nullableDouble = 5.5;
            
            // Reference Types
            // String
            string name = "Alice";
            string empty = "";
            string nullString = null;
            
            // Object
            object obj = 42;
            object objString = "hello";
            
            // Dynamic
            dynamic dynamicValue = 10;
            dynamicValue = "now a string";
            
            // Arrays
            int[] numbers = { 1, 2, 3, 4, 5 };
            string[] names = new string[3];
            
            // Value type behavior (copy by value)
            int a = 10;
            int b = a;
            b = 20;
            Console.WriteLine($"a: {a}, b: {b}"); // a: 10, b: 20
            
            // Reference type behavior (copy by reference)
            int[] arr1 = { 1, 2, 3 };
            int[] arr2 = arr1;
            arr2[0] = 99;
            Console.WriteLine($"arr1[0]: {arr1[0]}"); // arr1[0]: 99
            
            // Type checking
            Console.WriteLine($"Is int a value type: {typeof(int).IsValueType}");
            Console.WriteLine($"Is string a value type: {typeof(string).IsValueType}");
            
            // Boxing and Unboxing
            int boxed = 42;
            object boxedObj = boxed; // Boxing
            int unboxed = (int)boxedObj; // Unboxing
            
            Console.WriteLine("Types example running");
        }
    }
}