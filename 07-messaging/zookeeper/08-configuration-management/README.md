# Configuration Management

> Package: `academy.messaging.zookeeper.configmanagement`

## Overview

Zookeeper provides centralized configuration management with real-time updates via watches. Configuration changes propagate instantly to all connected clients.

## Configuration Pattern

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Configuration Management Flow                        │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐     1. Read config      ┌─────────────┐              │
│  │   Admin     │ ────────────────────────►│  Zookeeper  │              │
│  │   Console   │                          │             │              │
│  └─────────────┘                          └─────────────┘              │
│        │                                        ▲                      │
│        │ 2. Update config                      │                      │
│        │                                        │                      │
│        ▼                                        │                      │
│  ┌─────────────┐     3. Watch fires      ┌────┴────────┐              │
│  │   App 1     │ ◄───────────────────────│   App 1     │              │
│  │   App 2     │ ◄───────────────────────│   App 2     │              │
│  │   App 3     │ ◄───────────────────────│   App 3     │              │
│  └─────────────┘                          └─────────────┘              │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## ZNode Structure

```
/config
├── /config/app1
│   ├── /config/app1/db.url = "jdbc:mysql://localhost:3306/mydb"
│   ├── /config/app1/db.user = "admin"
│   ├── /config/app1/db.password = "secret"
│   ├── /config/app1.cache.ttl = "300"
│   └── /config/app1.logging.level = "INFO"
│
├── /config/app2
│   ├── /config/app2/api.url = "https://api.example.com"
│   ├── /config/app2.api.key = "abc123"
│   └── /config/app2.timeout = "30000"
│
└── /config/shared
    ├── /config/shared.feature.flags = {"darkMode":true,"newUI":false}
    └── /config/shared.limits = {"maxRequests":1000,"rateLimit":100}
```

## Implementation

### Configuration Service

```java
package academy.messaging.zookeeper.configmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConfigurationService {
    
    private final CuratorFramework client;
    private final String configPath;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentMap<String, String> configCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, NodeCache> watches = new ConcurrentHashMap<>();
    
    public ConfigurationService(CuratorFramework client, String configPath) {
        this.client = client;
        this.configPath = configPath;
    }
    
    public void startWatching(String appId) throws Exception {
        String appConfigPath = configPath + "/" + appId;
        
        // Watch for config changes
        NodeCache cache = new NodeCache(client, appConfigPath);
        cache.getListenable().addListener(() -> {
            if (cache.getCurrentData() != null) {
                String config = new String(cache.getCurrentData().getData());
                configCache.put(appId, config);
                onConfigChanged(appId, config);
            }
        });
        cache.start();
        watches.put(appId, cache);
        
        // Load initial config
        byte[] data = client.getData().forPath(appConfigPath);
        configCache.put(appId, new String(data));
    }
    
    public String getConfig(String appId) {
        return configCache.get(appId);
    }
    
    public <T> T getConfig(String appId, Class<T> type) throws Exception {
        String config = configCache.get(appId);
        if (config == null) {
            return null;
        }
        return mapper.readValue(config, type);
    }
    
    protected void onConfigChanged(String appId, String newConfig) {
        System.out.println("Config changed for " + appId + ": " + newConfig);
    }
    
    public void updateConfig(String appId, String config) throws Exception {
        String appConfigPath = configPath + "/" + appId;
        
        if (client.checkExists().forPath(appConfigPath) == null) {
            client.create().creatingParentsIfNeeded()
                .forPath(appConfigPath, config.getBytes());
        } else {
            client.setData().forPath(appConfigPath, config.getBytes());
        }
    }
    
    public void stop() {
        watches.values().forEach(NodeCache::close);
        watches.clear();
    }
}
```

### Feature Flags

```java
package academy.messaging.zookeeper.configmanagement;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FeatureFlagService {
    
    private final CuratorFramework client;
    private final String featurePath;
    private final ConcurrentMap<String, Boolean> featureFlags = new ConcurrentHashMap<>();
    
    public FeatureFlagService(CuratorFramework client, String featurePath) {
        this.client = client;
        this.featurePath = featurePath;
    }
    
    public void loadFeatures() throws Exception {
        java.util.List<String> features = client.getChildren().forPath(featurePath);
        
        for (String feature : features) {
            byte[] data = client.getData().forPath(featurePath + "/" + feature);
            boolean enabled = Boolean.parseBoolean(new String(data));
            featureFlags.put(feature, enabled);
            
            // Watch for changes
            watchFeature(feature);
        }
    }
    
    private void watchFeature(String feature) throws Exception {
        String featureFilePath = featurePath + "/" + feature;
        
        NodeCache cache = new NodeCache(client, featureFilePath);
        cache.getListenable().addListener(() -> {
            if (cache.getCurrentData() != null) {
                boolean enabled = Boolean.parseBoolean(
                    new String(cache.getCurrentData().getData()));
                featureFlags.put(feature, enabled);
                System.out.println("Feature " + feature + " = " + enabled);
            }
        });
        cache.start();
    }
    
    public boolean isEnabled(String feature) {
        return featureFlags.getOrDefault(feature, false);
    }
    
    public void setEnabled(String feature, boolean enabled) throws Exception {
        String featureFilePath = featurePath + "/" + feature;
        
        if (client.checkExists().forPath(featureFilePath) == null) {
            client.create().creatingParentsIfNeeded()
                .forPath(featureFilePath, Boolean.toString(enabled).getBytes());
        } else {
            client.setData().forPath(featureFilePath, 
                Boolean.toString(enabled).getBytes());
        }
    }
}
```

### Dynamic Properties

```java
package academy.messaging.zookeeper.configmanagement;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import java.util.HashMap;
import java.util.Map;

public class DynamicProperties {
    
    private final CuratorFramework client;
    private final String propertiesPath;
    private final Map<String, String> properties = new HashMap<>();
    
    public DynamicProperties(CuratorFramework client, String propertiesPath) {
        this.client = client;
        this.propertiesPath = propertiesPath;
    }
    
    public void start() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, propertiesPath, true);
        cache.getListenable().addListener((client, event) -> {
            String path = event.getData().getPath();
            String key = path.substring(path.lastIndexOf('/') + 1);
            
            switch (event.getType()) {
                case CHILD_ADDED:
                case CHILD_UPDATED:
                    byte[] data = client.getData().forPath(path);
                    properties.put(key, new String(data));
                    System.out.println("Property updated: " + key + " = " + new String(data));
                    break;
                case CHILD_REMOVED:
                    properties.remove(key);
                    System.out.println("Property removed: " + key);
                    break;
            }
        });
        cache.start();
    }
    
    public String get(String key) {
        return properties.get(key);
    }
    
    public String get(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
    
    public int getInt(String key, int defaultValue) {
        String value = properties.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Use default
            }
        }
        return defaultValue;
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.get(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
}
```

## Configuration Patterns

### Pattern 1: Hierarchical Config

```
/config
├── /config/global
│   ├── /config/global/logging.level = "INFO"
│   └── /config/global.timezone = "UTC"
├── /config/app1
│   ├── /config/app1.db.url = "jdbc:..."
│   └── /config/app1.feature.x = "true"
└── /config/app2
    ├── /config/app2.api.url = "https://..."
    └── /config/app2.feature.y = "false"
```

### Pattern 2: Environment-Specific

```
/config
├── /config/dev
│   ├── /config/dev.db.url = "jdbc:..."
│   └── /config/dev.logging.level = "DEBUG"
├── /config/staging
│   ├── /config/staging.db.url = "jdbc:..."
│   └── /config/staging.logging.level = "INFO"
└── /config/prod
    ├── /config/prod.db.url = "jdbc:..."
    └── /config/prod.logging.level = "WARN"
```

### Pattern 3: Feature Flags

```
/features
├── /features/darkMode = "true"
├── /features/newUI = "false"
├── /features/betaFeature = "true"
└── /features/gradualRollout = "50"
```

## Best Practices

```
✓ Use watches for real-time updates
✓ Cache config locally for fast access
✓ Version your configurations
✓ Validate config before applying
✓ Support graceful reload

✗ Don't store secrets in plain text
✗ Don't poll for changes
✗ Don't ignore config validation
✗ Don't skip rollback strategy
```

## Summary

| Component | Purpose |
|-----------|---------|
| ConfigurationService | Manages app configuration |
| FeatureFlagService | Controls feature rollout |
| DynamicProperties | Runtime property updates |
| NodeCache | Watches for changes |
