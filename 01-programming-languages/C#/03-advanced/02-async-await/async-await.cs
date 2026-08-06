// C# Async/Await - async, await, Task, CancellationToken

using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace AsyncAwaitDemo
{
    class Program
    {
        // Async method returning Task
        static async Task<string> FetchDataAsync()
        {
            Console.WriteLine("Fetching data...");
            await Task.Delay(2000); // Simulate async work
            Console.WriteLine("Data fetched!");
            return "Sample data";
        }
        
        // Async method with cancellation
        static async Task ProcessWithCancellationAsync(CancellationToken token)
        {
            for (int i = 0; i < 10; i++)
            {
                token.ThrowIfCancellationRequested();
                Console.WriteLine($"Processing item {i}...");
                await Task.Delay(500, token);
            }
        }
        
        // Async method returning value
        static async Task<int> CalculateAsync(int a, int b)
        {
            await Task.Delay(1000);
            return a + b;
        }
        
        // Multiple concurrent tasks
        static async Task<List<string>> FetchMultipleAsync()
        {
            List<Task<string>> tasks = new List<Task<string>>
            {
                FetchDataAsync(),
                FetchDataAsync(),
                FetchDataAsync()
            };
            
            string[] results = await Task.WhenAll(tasks);
            return new List<string>(results);
        }
        
        // Async void (use sparingly, for event handlers)
        static async void FireAndForget()
        {
            await Task.Delay(1000);
            Console.WriteLine("Fire and forget completed");
        }
        
        // Async with try-catch
        static async Task SafeOperationAsync()
        {
            try
            {
                string data = await FetchDataAsync();
                Console.WriteLine($"Got: {data}");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
        }
        
        // Async iterator (C# 8.0+)
        static async IAsyncEnumerable<int> GenerateNumbersAsync()
        {
            for (int i = 0; i < 5; i++)
            {
                await Task.Delay(500);
                yield return i;
            }
        }
        
        static async Task Main(string[] args)
        {
            // Basic async/await
            Console.WriteLine("=== Basic Async ===");
            string data = await FetchDataAsync();
            Console.WriteLine($"Result: {data}");
            
            // Async with return value
            Console.WriteLine("\n=== Async with Return ===");
            int sum = await CalculateAsync(5, 3);
            Console.WriteLine($"Sum: {sum}");
            
            // Concurrent tasks
            Console.WriteLine("\n=== Concurrent Tasks ===");
            var results = await FetchMultipleAsync();
            Console.WriteLine($"Fetched {results.Count} items");
            
            // Cancellation
            Console.WriteLine("\n=== Cancellation ===");
            var cts = new CancellationTokenSource();
            var task = ProcessWithCancellationAsync(cts.Token);
            
            await Task.Delay(2500);
            cts.Cancel();
            
            try
            {
                await task;
            }
            catch (OperationCanceledException)
            {
                Console.WriteLine("Operation cancelled");
            }
            
            // Safe operation
            Console.WriteLine("\n=== Safe Operation ===");
            await SafeOperationAsync();
            
            // Async foreach (C# 8.0+)
            Console.WriteLine("\n=== Async Iterator ===");
            await foreach (var number in GenerateNumbersAsync())
            {
                Console.WriteLine($"Number: {number}");
            }
            
            Console.WriteLine("\nAsync/Await example running");
        }
    }
}