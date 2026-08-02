package academy.javaengineering.rabbitmq;

import academy.javaengineering.rabbitmq.RabbitMqExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMqTest {

    private RabbitMQBroker broker;

    @BeforeEach
    void setUp() {
        broker = new RabbitMQBroker();
        broker.declareExchange("test.exchange", "topic");
        broker.declareQueue("test.queue", true);
        broker.bind("test.exchange", "test.*", "test.queue");
    }

    @Test
    void testExchangeCreation() {
        assertNotNull(broker.getExchange("test.exchange"));
        assertEquals("topic", broker.getExchange("test.exchange").getType());
    }

    @Test
    void testQueueCreation() {
        Queue queue = broker.declareQueue("my.queue", true);
        assertNotNull(queue);
        assertEquals("my.queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void testBinding() {
        Queue queue = broker.getQueue("test.queue");
        assertNotNull(queue);
        assertEquals(0, queue.size());
    }

    @Test
    void testDirectExchange() {
        broker.declareExchange("direct.exchange", "direct");
        Queue queue = broker.declareQueue("direct.queue", true);
        broker.bind("direct.exchange", "exact.key", "direct.queue");

        MessageProducer producer = new MessageProducer(broker.getExchange("direct.exchange"));
        producer.publish("direct message", "exact.key");

        assertEquals(1, queue.size());
    }

    @Test
    void testDirectExchangeNoMatch() {
        broker.declareExchange("direct.exchange", "direct");
        Queue queue = broker.declareQueue("direct.queue", true);
        broker.bind("direct.exchange", "exact.key", "direct.queue");

        MessageProducer producer = new MessageProducer(broker.getExchange("direct.exchange"));
        producer.publish("no match", "wrong.key");

        assertEquals(0, queue.size());
    }

    @Test
    void testFanoutExchange() {
        broker.declareExchange("fanout.exchange", "fanout");
        Queue q1 = broker.declareQueue("fanout.q1", true);
        Queue q2 = broker.declareQueue("fanout.q2", true);
        broker.bind("fanout.exchange", "", "fanout.q1");
        broker.bind("fanout.exchange", "", "fanout.q2");

        MessageProducer producer = new MessageProducer(broker.getExchange("fanout.exchange"));
        producer.publish("broadcast", "any.key");

        assertEquals(1, q1.size());
        assertEquals(1, q2.size());
    }

    @Test
    void testTopicExchangePattern() {
        broker.declareExchange("topic.exchange", "topic");
        Queue emailQueue = broker.declareQueue("email.q", true);
        Queue allQueue = broker.declareQueue("all.q", true);
        broker.bind("topic.exchange", "email.*", "email.q");
        broker.bind("topic.exchange", "order.#", "all.q");

        MessageProducer producer = new MessageProducer(broker.getExchange("topic.exchange"));
        producer.publish("email msg", "email.sent");
        producer.publish("order msg", "order.created.item1");

        assertEquals(1, emailQueue.size());
        assertEquals(1, allQueue.size());
    }

    @Test
    void testConsumerProcessing() {
        Queue queue = broker.getQueue("test.queue");
        MessageProducer producer = new MessageProducer(broker.getExchange("test.exchange"));
        producer.publish("hello", "test.msg");

        MessageConsumer consumer = new MessageConsumer("TestConsumer");
        boolean consumed = consumer.consume(queue);

        assertTrue(consumed);
        assertEquals(1, consumer.getProcessedMessages().size());
        assertTrue(consumer.getProcessedMessages().get(0).isAcknowledged());
    }

    @Test
    void testConsumerEmptyQueue() {
        Queue queue = broker.getQueue("test.queue");
        MessageConsumer consumer = new MessageConsumer("TestConsumer");
        boolean consumed = consumer.consume(queue);

        assertFalse(consumed);
    }

    @Test
    void testMessageProperties() {
        Message msg = new Message("body", "key");
        msg.addHeader("correlation-id", "123");
        msg.addHeader("priority", 5);

        assertNotNull(msg.getId());
        assertEquals("body", msg.getBody());
        assertEquals("key", msg.getRoutingKey());
        assertEquals("123", msg.getHeaders().get("correlation-id"));
        assertEquals(5, msg.getHeaders().get("priority"));
    }

    @Test
    void testDeadLetterQueue() {
        Queue mainQueue = new Queue("main", true, 2, Long.MAX_VALUE);
        mainQueue.enqueue(new Message("msg1", "key"));
        mainQueue.enqueue(new Message("msg2", "key"));
        mainQueue.enqueue(new Message("msg3", "key"));

        assertEquals(2, mainQueue.size());
        assertEquals(1, mainQueue.getDeadLetters().size());
        assertEquals("msg1", mainQueue.getDeadLetters().get(0).getBody());
    }

    @Test
    void testMultipleConsumers() {
        Queue queue = broker.getQueue("test.queue");
        MessageProducer producer = new MessageProducer(broker.getExchange("test.exchange"));

        for (int i = 0; i < 5; i++) {
            producer.publish("msg" + i, "test.key");
        }

        MessageConsumer consumer1 = new MessageConsumer("Consumer1");
        MessageConsumer consumer2 = new MessageConsumer("Consumer2");

        consumer1.consume(queue);
        consumer1.consume(queue);
        consumer2.consume(queue);
        consumer2.consume(queue);
        consumer1.consume(queue);

        assertEquals(3, consumer1.getProcessedMessages().size());
        assertEquals(2, consumer2.getProcessedMessages().size());
        assertEquals(0, queue.size());
    }

    @Test
    void testTopicPatternMatching() {
        Exchange exchange = new Exchange("topic.test", "topic");
        Queue q1 = new Queue("q1", true);
        exchange.bind("user.*", q1);

        assertFalse(exchange.getQueuesForRoutingKey("user").isEmpty());
        assertFalse(exchange.getQueuesForRoutingKey("user.created").isEmpty());
        assertTrue(exchange.getQueuesForRoutingKey("user.created.detail").isEmpty());
    }
}
