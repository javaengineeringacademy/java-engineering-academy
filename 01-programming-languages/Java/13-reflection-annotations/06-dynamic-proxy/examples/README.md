# Examples: Dynamic Proxy

## Example 1: Basic Logging Proxy

```java
package academy.javaengineering.reflection.proxy;

import java.lang.reflect.*;
import java.util.*;

public class LoggingProxyExample {

    interface UserService {
        void saveUser(String name);
        String findUser(int id);
    }

    static class UserServiceImpl implements UserService {
        public void saveUser(String name) { System.out.println("Saving: " + name); }
        public String findUser(int id) { return "User" + id; }
    }

    public static <T> T createLoggingProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                System.out.println("Before: " + method.getName());
                Object result = method.invoke(target, args);
                System.out.println("After: " + method.getName());
                return result;
            }
        );
    }

    public static void main(String[] args) {
        UserService service = createLoggingProxy(new UserServiceImpl());
        service.saveUser("Alice");
        System.out.println(service.findUser(1));
    }
}
```

## Example 2: Caching Proxy

```java
package academy.javaengineering.reflection.proxy;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class CachingProxyExample {

    interface DataProvider {
        String getData(String key);
        int compute(int value);
    }

    static class DataProviderImpl implements DataProvider {
        public String getData(String key) {
            System.out.println("Fetching: " + key);
            return "data-" + key;
        }
        public int compute(int value) {
            System.out.println("Computing: " + value);
            return value * 2;
        }
    }

    public static <T> T createCachingProxy(T target) {
        Map<String, Object> cache = new ConcurrentHashMap<>();
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                String key = method.getName() + ":" + Arrays.toString(args);
                if (cache.containsKey(key)) return cache.get(key);
                Object result = method.invoke(target, args);
                cache.put(key, result);
                return result;
            }
        );
    }

    public static void main(String[] args) {
        DataProvider provider = createCachingProxy(new DataProviderImpl());
        System.out.println(provider.getData("test"));
        System.out.println(provider.getData("test"));
        System.out.println(provider.compute(5));
    }
}
```

## Example 3: Timing Proxy

```java
package academy.javaengineering.reflection.proxy;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class TimingProxyExample {

    interface Service {
        void process();
        String transform(String input);
    }

    static class ServiceImpl implements Service {
        public void process() { try { Thread.sleep(10); } catch (Exception e) {} }
        public String transform(String input) { return input.toUpperCase(); }
    }

    public static <T> Map.Entry<T, Map<String, Long>> createTimingProxy(T target) {
        Map<String, Long> timings = new ConcurrentHashMap<>();
        T proxy = (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (p, method, args) -> {
                long start = System.nanoTime();
                Object result = method.invoke(target, args);
                long elapsed = (System.nanoTime() - start) / 1_000_000;
                timings.merge(method.getName(), elapsed, Long::sum);
                return result;
            }
        );
        return new AbstractMap.SimpleEntry<>(proxy, timings);
    }

    public static void main(String[] args) {
        Map.Entry<Service, Map<String, Long>> entry = createTimingProxy(new ServiceImpl());
        Service service = entry.getKey();
        Map<String, Long> timings = entry.getValue();

        service.process();
        service.transform("hello");
        System.out.println("Timings: " + timings);
    }
}
```
