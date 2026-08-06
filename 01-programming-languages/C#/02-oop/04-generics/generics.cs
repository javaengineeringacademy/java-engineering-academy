// C# Generics

using System;
using System.Collections.Generic;

namespace GenericsDemo
{
    // Generic class
    public class Repository<T>
    {
        private readonly List<T> items = new List<T>();
        
        public void Add(T item)
        {
            items.Add(item);
        }
        
        public void Remove(T item)
        {
            items.Remove(item);
        }
        
        public T GetById(int index)
        {
            return items[index];
        }
        
        public List<T> GetAll()
        {
            return new List<T>(items);
        }
        
        public int Count => items.Count;
    }
    
    // Generic interface
    public interface IRepository<T>
    {
        void Add(T item);
        T GetById(int id);
        IEnumerable<T> GetAll();
    }
    
    // Generic method
    public static class ArrayHelper
    {
        public static T[] Merge<T>(T[] array1, T[] array2)
        {
            T[] result = new T[array1.Length + array2.Length];
            array1.CopyTo(result, 0);
            array2.CopyTo(result, array1.Length);
            return result;
        }
        
        public static void Swap<T>(ref T a, ref T b)
        {
            T temp = a;
            a = b;
            b = temp;
        }
        
        public static T Max<T>(T a, T b) where T : IComparable<T>
        {
            return a.CompareTo(b) > 0 ? a : b;
        }
    }
    
    // Generic constraints
    public class SportsTeam<T> where T : class, IComparable<T>
    {
        private List<T> players = new List<T>();
        
        public void AddPlayer(T player)
        {
            players.Add(player);
        }
        
        public T GetBestPlayer()
        {
            if (players.Count == 0)
                throw new InvalidOperationException("No players");
            
            T best = players[0];
            for (int i = 1; i < players.Count; i++)
            {
                if (players[i].CompareTo(best) > 0)
                    best = players[i];
            }
            return best;
        }
    }
    
    // Generic with multiple constraints
    public class Cache<TKey, TValue> 
        where TKey : notnull
        where TValue : class
    {
        private Dictionary<TKey, TValue> cache = new Dictionary<TKey, TValue>();
        
        public void Add(TKey key, TValue value)
        {
            cache[key] = value;
        }
        
        public TValue Get(TKey key)
        {
            return cache[key];
        }
        
        public bool TryGetValue(TKey key, out TValue value)
        {
            return cache.TryGetValue(key, out value);
        }
    }
    
    // Generic delegate
    public delegate T Func<T>(T input);
    
    // Example class for constraints
    public class Player : IComparable<Player>
    {
        public string Name { get; set; }
        public int Score { get; set; }
        
        public int CompareTo(Player other)
        {
            return Score.CompareTo(other.Score);
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            // Generic class
            Repository<string> stringRepo = new Repository<string>();
            stringRepo.Add("Hello");
            stringRepo.Add("World");
            Console.WriteLine($"String count: {stringRepo.Count}");
            
            Repository<int> intRepo = new Repository<int>();
            intRepo.Add(1);
            intRepo.Add(2);
            Console.WriteLine($"Int count: {intRepo.Count}");
            
            // Generic method
            int[] arr1 = { 1, 2, 3 };
            int[] arr2 = { 4, 5, 6 };
            int[] merged = ArrayHelper.Merge(arr1, arr2);
            Console.WriteLine($"Merged: {string.Join(", ", merged)}");
            
            // Swap
            int a = 5, b = 10;
            Console.WriteLine($"Before: a={a}, b={b}");
            ArrayHelper.Swap(ref a, ref b);
            Console.WriteLine($"After: a={a}, b={b}");
            
            // Max
            Console.WriteLine($"Max(3, 7): {ArrayHelper.Max(3, 7)}");
            
            // Generic constraints
            SportsTeam<Player> team = new SportsTeam<Player>();
            team.AddPlayer(new Player { Name = "Alice", Score = 100 });
            team.AddPlayer(new Player { Name = "Bob", Score = 150 });
            Player best = team.GetBestPlayer();
            Console.WriteLine($"Best player: {best.Name} with score {best.Score}");
            
            // Generic cache
            Cache<string, Player> playerCache = new Cache<string, Player>();
            playerCache.Add("alice", new Player { Name = "Alice", Score = 100 });
            Player cached = playerCache.Get("alice");
            Console.WriteLine($"Cached player: {cached.Name}");
            
            Console.WriteLine("Generics example running");
        }
    }
}