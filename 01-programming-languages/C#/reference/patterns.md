# C# Design Patterns

## Overview
Common design patterns implemented in C#.

## 1. Singleton Pattern
```csharp
public sealed class Database
{
    private static Database instance;
    private static readonly object lockObj = new object();
    
    private Database() { }
    
    public static Database Instance
    {
        get
        {
            if (instance == null)
            {
                lock (lockObj)
                {
                    if (instance == null)
                        instance = new Database();
                }
            }
            return instance;
        }
    }
}
```

## 2. Factory Pattern
```csharp
public interface IShape
{
    double Area();
}

public class Circle : IShape
{
    public double Radius { get; set; }
    public double Area() => Math.PI * Radius * Radius;
}

public class Rectangle : IShape
{
    public double Width { get; set; }
    public double Height { get; set; }
    public double Area() => Width * Height;
}

public static class ShapeFactory
{
    public static IShape Create(string type, params double[] dimensions)
    {
        return type switch
        {
            "circle" => new Circle { Radius = dimensions[0] },
            "rectangle" => new Rectangle { Width = dimensions[0], Height = dimensions[1] },
            _ => throw new ArgumentException("Unknown shape")
        };
    }
}
```

## 3. Observer Pattern
```csharp
public interface IObserver<T>
{
    void Update(T data);
}

public class Subject<T>
{
    private List<IObserver<T>> observers = new List<IObserver<T>>();
    
    public void Subscribe(IObserver<T> observer) => observers.Add(observer);
    public void Unsubscribe(IObserver<T> observer) => observers.Remove(observer);
    public void Notify(T data)
    {
        foreach (var observer in observers)
            observer.Update(data);
    }
}
```

## 4. Repository Pattern
```csharp
public interface IRepository<T>
{
    Task<T> GetByIdAsync(int id);
    Task<IEnumerable<T>> GetAllAsync();
    Task AddAsync(T entity);
    Task UpdateAsync(T entity);
    Task DeleteAsync(int id);
}

public class UserRepository : IRepository<User>
{
    public async Task<User> GetByIdAsync(int id)
    {
        // Database query
        return await Task.FromResult<User>(null);
    }
    
    public async Task<IEnumerable<User>> GetAllAsync()
    {
        return await Task.FromResult<IEnumerable<User>>(new List<User>());
    }
    
    public async Task AddAsync(User entity)
    {
        await Task.CompletedTask;
    }
    
    public async Task UpdateAsync(User entity)
    {
        await Task.CompletedTask;
    }
    
    public async Task DeleteAsync(int id)
    {
        await Task.CompletedTask;
    }
}
```

## 5. Builder Pattern
```csharp
public class QueryBuilder
{
    private string table;
    private List<string> conditions = new List<string>();
    private List<string> columns = new List<string>();
    
    public QueryBuilder From(string table)
    {
        this.table = table;
        return this;
    }
    
    public QueryBuilder Select(params string[] cols)
    {
        columns.AddRange(cols);
        return this;
    }
    
    public QueryBuilder Where(string condition)
    {
        conditions.Add(condition);
        return this;
    }
    
    public string Build()
    {
        string cols = columns.Any() ? string.Join(", ", columns) : "*";
        string where = conditions.Any() ? $" WHERE {string.Join(" AND ", conditions)}" : "";
        return $"SELECT {cols} FROM {table}{where}";
    }
}

// Usage
string query = new QueryBuilder()
    .From("Users")
    .Select("Name", "Email")
    .Where("Age > 18")
    .Build();
```

## Key Takeaways
1. Use Singleton for global state
2. Use Factory for object creation
3. Use Observer for event handling
4. Use Repository for data access
5. Use Builder for complex construction