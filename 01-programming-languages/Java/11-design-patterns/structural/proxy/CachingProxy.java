package academy.javaengineering.patterns.structural.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Caching Proxy that stores results of expensive operations.
 * Subsequent requests for the same data return cached results
 * without delegating to the real object.
 */
public class CachingProxy implements Image {

    private final Map<String, String> cache = new HashMap<>();
    private final String fileName;
    private int delegateCount = 0;

    public CachingProxy(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (cache.containsKey(fileName)) {
            System.out.println("[CachingProxy] Cache hit for: " + fileName);
            System.out.println("[CachingProxy] Cached data: " + cache.get(fileName));
        } else {
            System.out.println("[CachingProxy] Cache miss for: " + fileName);
            RealImage real = new RealImage(fileName);
            cache.put(fileName, "Rendered:" + fileName);
            delegateCount++;
            real.display();
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public int getDelegateCount() {
        return delegateCount;
    }

    public int getCacheSize() {
        return cache.size();
    }

    public void clearCache() {
        cache.clear();
        delegateCount = 0;
    }
}
