package academy.javaengineering.patterns.creational;

public enum EnumSingleton {
    INSTANCE;

    private String config;
    private int connectionCount;

    EnumSingleton() {
        this.config = "default-config";
        this.connectionCount = 0;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void incrementConnections() {
        this.connectionCount++;
    }

    public void reset() {
        this.config = "default-config";
        this.connectionCount = 0;
    }
}
