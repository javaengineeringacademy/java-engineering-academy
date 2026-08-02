package academy.javaengineering.redpanda;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Demonstrates Redpanda/Kafka producer.
 */
@Component
public class RedpandaProducer {

    private final KafkaTemplate<String, RedpandaMessage.StockUpdate> kafkaTemplate;

    public RedpandaProducer(KafkaTemplate<String, RedpandaMessage.StockUpdate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockUpdate(RedpandaMessage.StockUpdate update) {
        kafkaTemplate.send("stock-updates", update.symbol(), update);
        System.out.println("Stock update sent: " + update.symbol());
    }

    public void sendTradeEvent(RedpandaMessage.TradeEvent trade) {
        kafkaTemplate.send("trade-events", trade.tradeId(), trade);
        System.out.println("Trade event sent: " + trade.tradeId());
    }
}
