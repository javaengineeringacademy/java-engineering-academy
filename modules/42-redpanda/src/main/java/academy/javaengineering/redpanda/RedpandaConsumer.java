package academy.javaengineering.redpanda;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Demonstrates Redpanda/Kafka consumer.
 */
@Component
public class RedpandaConsumer {

    @KafkaListener(topics = "stock-updates", groupId = "stock-consumer")
    public void handleStockUpdate(RedpandaMessage.StockUpdate update) {
        System.out.println("Processing stock update: " + update.symbol() + 
            " at $" + update.price());
    }

    @KafkaListener(topics = "trade-events", groupId = "trade-consumer")
    public void handleTradeEvent(RedpandaMessage.TradeEvent trade) {
        System.out.println("Processing trade: " + trade.side() + 
            " " + trade.quantity() + " of " + trade.symbol());
    }
}
