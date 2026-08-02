package academy.javaengineering.redpanda;

/**
 * Demonstrates Redpanda message types.
 */
public class RedpandaMessage {

    public record StockUpdate(
        String symbol,
        double price,
        int quantity,
        String timestamp
    ) {}

    public record TradeEvent(
        String tradeId,
        String symbol,
        String side,
        double price,
        int quantity
    ) {}
}
