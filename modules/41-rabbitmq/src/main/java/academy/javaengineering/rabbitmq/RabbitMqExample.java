package academy.javaengineering.rabbitmq;

import java.util.*;
import java.util.concurrent.*;

public class RabbitMqExample {

    public static class Message {
        private final String id;
        private final String body;
        private final Map<String, Object> headers;
        private final String routingKey;
        private final long timestamp;
        private boolean acknowledged;

        public Message(String body, String routingKey) {
            this.id = UUID.randomUUID().toString();
            this.body = body;
            this.routingKey = routingKey;
            this.headers = new HashMap<>();
            this.timestamp = System.currentTimeMillis();
            this.acknowledged = false;
        }

        public String getId() { return id; }
        public String getBody() { return body; }
        public Map<String, Object> getHeaders() { return headers; }
        public String getRoutingKey() { return routingKey; }
        public long getTimestamp() { return timestamp; }
        public boolean isAcknowledged() { return acknowledged; }
        public void acknowledge() { this.acknowledged = true; }

        public Message addHeader(String key, Object value) {
            headers.put(key, value);
            return this;
        }
    }

    public static class Queue {
        private final String name;
        private final boolean durable;
        private final LinkedList<Message> messages;
        private final List<Message> deadLetters;
        private final int maxLength;
        private final long ttlMillis;

        public Queue(String name, boolean durable) {
            this(name, durable, Integer.MAX_VALUE, Long.MAX_VALUE);
        }

        public Queue(String name, boolean durable, int maxLength, long ttlMillis) {
            this.name = name;
            this.durable = durable;
            this.messages = new LinkedList<>();
            this.deadLetters = new ArrayList<>();
            this.maxLength = maxLength;
            this.ttlMillis = ttlMillis;
        }

        public void enqueue(Message message) {
            if (messages.size() >= maxLength) {
                deadLetters.add(messages.poll());
            }
            messages.addLast(message);
        }

        public Optional<Message> dequeue() {
            if (messages.isEmpty()) return Optional.empty();
            Message msg = messages.pollFirst();
            if (System.currentTimeMillis() - msg.getTimestamp() > ttlMillis) {
                deadLetters.add(msg);
                return dequeue();
            }
            return Optional.of(msg);
        }

        public int size() { return messages.size(); }
        public String getName() { return name; }
        public boolean isDurable() { return durable; }
        public List<Message> getDeadLetters() { return deadLetters; }
    }

    public static class Exchange {
        private final String name;
        private final String type;
        private final Map<String, List<Queue>> bindings;

        public Exchange(String name, String type) {
            this.name = name;
            this.type = type;
            this.bindings = new HashMap<>();
        }

        public void bind(String routingKey, Queue queue) {
            bindings.computeIfAbsent(routingKey, k -> new ArrayList<>()).add(queue);
        }

        public List<Queue> getQueuesForRoutingKey(String routingKey) {
            List<Queue> matchedQueues = new ArrayList<>();

            switch (type) {
                case "direct" -> {
                    if (bindings.containsKey(routingKey)) {
                        matchedQueues.addAll(bindings.get(routingKey));
                    }
                }
                case "fanout" -> {
                    bindings.values().forEach(matchedQueues::addAll);
                }
                case "topic" -> {
                    for (Map.Entry<String, List<Queue>> entry : bindings.entrySet()) {
                        if (matchesTopicPattern(entry.getKey(), routingKey)) {
                            matchedQueues.addAll(entry.getValue());
                        }
                    }
                }
            }
            return matchedQueues;
        }

        private boolean matchesTopicPattern(String pattern, String routingKey) {
            String regex = pattern.replace(".", "\\.")
                .replace("*", "[^.]+")
                .replace("#", ".*");
            return routingKey.matches(regex);
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public Map<String, List<Queue>> getBindings() { return bindings; }
    }

    public static class MessageProducer {
        private final Exchange exchange;

        public MessageProducer(Exchange exchange) {
            this.exchange = exchange;
        }

        public void publish(String body, String routingKey) {
            Message message = new Message(body, routingKey);
            List<Queue> queues = exchange.getQueuesForRoutingKey(routingKey);
            for (Queue queue : queues) {
                queue.enqueue(message);
            }
            System.out.println("Published to " + exchange.getName() + " with key: " + routingKey);
        }
    }

    public static class MessageConsumer {
        private final String consumerId;
        private final List<Message> processedMessages;

        public MessageConsumer(String consumerId) {
            this.consumerId = consumerId;
            this.processedMessages = new ArrayList<>();
        }

        public boolean consume(Queue queue) {
            Optional<Message> messageOpt = queue.dequeue();
            if (messageOpt.isPresent()) {
                Message message = messageOpt.get();
                processMessage(message);
                message.acknowledge();
                processedMessages.add(message);
                return true;
            }
            return false;
        }

        protected void processMessage(Message message) {
            System.out.println(consumerId + " processing: " + message.getBody());
        }

        public String getConsumerId() { return consumerId; }
        public List<Message> getProcessedMessages() { return processedMessages; }
    }

    public static class RabbitMQBroker {
        private final Map<String, Exchange> exchanges;
        private final Map<String, Queue> queues;

        public RabbitMQBroker() {
            this.exchanges = new HashMap<>();
            this.queues = new HashMap<>();
        }

        public void declareExchange(String name, String type) {
            exchanges.put(name, new Exchange(name, type));
        }

        public Queue declareQueue(String name, boolean durable) {
            Queue queue = new Queue(name, durable);
            queues.put(name, queue);
            return queue;
        }

        public void bind(String exchangeName, String routingKey, String queueName) {
            Exchange exchange = exchanges.get(exchangeName);
            Queue queue = queues.get(queueName);
            if (exchange != null && queue != null) {
                exchange.bind(routingKey, queue);
            }
        }

        public Exchange getExchange(String name) { return exchanges.get(name); }
        public Queue getQueue(String name) { return queues.get(name); }
    }

    public static void main(String[] args) {
        RabbitMQBroker broker = new RabbitMQBroker();

        broker.declareExchange("app.exchange", "topic");
        broker.declareQueue("email.queue", true);
        broker.declareQueue("notification.queue", true);
        broker.bind("app.exchange", "email.*", "email.queue");
        broker.bind("app.exchange", "notification.*", "notification.queue");

        MessageProducer producer = new MessageProducer(broker.getExchange("app.exchange"));
        MessageConsumer emailConsumer = new MessageConsumer("EmailService");
        MessageConsumer notifConsumer = new MessageConsumer("NotificationService");

        producer.publish("Welcome email", "email.welcome");
        producer.publish("Order confirmation", "email.order");
        producer.publish("Push notification", "notification.push");

        emailConsumer.consume(broker.getQueue("email.queue"));
        emailConsumer.consume(broker.getQueue("email.queue"));
        notifConsumer.consume(broker.getQueue("notification.queue"));

        Queue dlq = broker.declareQueue("dlq.queue", true);
        Queue processingQueue = broker.declareQueue("processing.queue", true, 100, 5000);

        System.out.println("Email messages processed: " + emailConsumer.getProcessedMessages().size());
        System.out.println("Notification messages processed: " + notifConsumer.getProcessedMessages().size());

        System.out.println("RabbitMQ Example Complete");
    }
}
