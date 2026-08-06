// C# Control Flow - Loops

using System;

namespace LoopsDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            // for loop
            Console.WriteLine("For loop:");
            for (int i = 0; i < 5; i++)
            {
                Console.Write($"{i} ");
            }
            Console.WriteLine();
            
            // Nested for loop
            Console.WriteLine("Multiplication table:");
            for (int i = 1; i <= 3; i++)
            {
                for (int j = 1; j <= 3; j++)
                {
                    Console.Write($"{i * j,4}");
                }
                Console.WriteLine();
            }
            
            // while loop
            Console.WriteLine("While loop:");
            int count = 0;
            while (count < 5)
            {
                Console.Write($"{count} ");
                count++;
            }
            Console.WriteLine();
            
            // do-while loop
            Console.WriteLine("Do-while loop:");
            int num = 0;
            do
            {
                Console.Write($"{num} ");
                num++;
            } while (num < 5);
            Console.WriteLine();
            
            // foreach loop
            Console.WriteLine("Foreach loop:");
            string[] fruits = { "Apple", "Banana", "Cherry" };
            foreach (string fruit in fruits)
            {
                Console.Write($"{fruit} ");
            }
            Console.WriteLine();
            
            // foreach with index
            Console.WriteLine("Foreach with index:");
            for (int i = 0; i < fruits.Length; i++)
            {
                Console.WriteLine($"{i}: {fruits[i]}");
            }
            
            // break statement
            Console.WriteLine("Break example:");
            for (int i = 0; i < 10; i++)
            {
                if (i == 5)
                {
                    break;
                }
                Console.Write($"{i} ");
            }
            Console.WriteLine();
            
            // continue statement
            Console.WriteLine("Continue example:");
            for (int i = 0; i < 10; i++)
            {
                if (i % 2 == 0)
                {
                    continue;
                }
                Console.Write($"{i} ");
            }
            Console.WriteLine();
            
            // goto statement (use sparingly)
            Console.WriteLine("Goto example:");
            for (int i = 0; i < 10; i++)
            {
                if (i == 5)
                {
                    goto Found;
                }
            }
            Console.WriteLine("Not found");
            goto End;
            
            Found:
            Console.WriteLine("Found 5");
            
            End:
            Console.WriteLine("End of program");
            
            // Infinite loop with break
            Console.WriteLine("Infinite loop with break:");
            int counter = 0;
            while (true)
            {
                if (counter >= 5)
                {
                    break;
                }
                Console.Write($"{counter} ");
                counter++;
            }
            Console.WriteLine();
            
            Console.WriteLine("Loops example running");
        }
    }
}