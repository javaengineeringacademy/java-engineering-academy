package academy.javaengineering.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RedisPubSubExample {

    private final Map<String, List<MessageListener>> subscribers = new ConcurrentHashMap<>();
    private final List<String> messageLog = new CopyOnWriteArrayList<>();

    public interface MessageListener {
        void onMessage(String channel, String message);
    }

    public void subscribe(String channel, MessageListener listener) {
        subscribers.computeIfAbsent(channel, k -> new CopyOnWriteArrayList<>()).add(listener);
        System.out.println("Subscribed to channel: " + channel);
    }

    public void unsubscribe(String channel, MessageListener listener) {
        List<MessageListener> listeners = subscribers.get(channel);
        if (listeners != null) {
            listeners.remove(listener);
        }
        System.out.println("Unsubscribed from channel: " + channel);
    }

    public void publish(String channel, String message) {
        System.out.println("Publishing to " + channel + ": " + message);
        messageLog.add(channel + ":" + message);

        List<MessageListener> listeners = subscribers.get(channel);
        if (listeners != null) {
            for (MessageListener listener : listeners) {
                listener.onMessage(channel, message);
            }
        }
    }

    public List<String> getMessageLog() {
        return new ArrayList<>(messageLog);
    }

    public static void main(String[] args) {
        RedisPubSubExample pubSub = new RedisPubSubExample();

        System.out.println("=== Redis Pub/Sub Demo ===\n");

        pubSub.subscribe("orders", (channel, message) -> {
            System.out.println("Order Service received: " + message);
        });

        pubSub.subscribe("orders", (channel, message) -> {
            System.out.println("Notification Service received: " + message);
        });

        pubSub.subscribe("users", (channel, message) -> {
            System.out.println("User Service received: " + message);
        });

        System.out.println("\n--- Publishing Messages ---");
        pubSub.publish("orders", "Order #123 created");
        pubSub.publish("users", "User John registered");

        System.out.println("\n--- Message Log ---");
        pubSub.getMessageLog().forEach(System.out::println);
    }
}
