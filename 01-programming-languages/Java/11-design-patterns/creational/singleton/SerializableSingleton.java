package academy.javaengineering.patterns.creational;

import java.io.Serializable;

/**
 * Singleton that survives Java serialization.
 * Without readResolve, deserialization would create a new instance.
 * The readResolve method replaces the deserialized object with the
 * existing singleton instance.
 */
public class SerializableSingleton implements Serializable {

    private static final long serialVersionUID = 1L;

    private static volatile SerializableSingleton instance;
    private String data;

    private SerializableSingleton() {
        this.data = "Serializable initialized";
    }

    public static SerializableSingleton getInstance() {
        if (instance == null) {
            synchronized (SerializableSingleton.class) {
                if (instance == null) {
                    instance = new SerializableSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Replaces the deserialized instance with the canonical singleton.
     * This is the key hook that preserves singleton invariant across serialization.
     */
    protected Object readResolve() {
        return getInstance();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SerializableSingleton{data='" + data + "'}";
    }
}
