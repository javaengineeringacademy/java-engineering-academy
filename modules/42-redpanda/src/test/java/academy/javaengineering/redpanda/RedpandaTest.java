package academy.javaengineering.redpanda;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Redpanda Tests")
class RedpandaTest {

    @Test
    @DisplayName("StockUpdate should be created correctly")
    void testStockUpdate() {
        var update = new RedpandaMessage.StockUpdate("AAPL", 150.25, 1000, "2024-01-01T10:00:00");
        
        assertEquals("AAPL", update.symbol());
        assertEquals(150.25, update.price(), 0.01);
        assertEquals(1000, update.quantity());
    }

    @Test
    @DisplayName("TradeEvent should be created correctly")
    void testTradeEvent() {
        var trade = new RedpandaMessage.TradeEvent(
            "TRD-001", "AAPL", "BUY", 150.25, 100
        );
        
        assertEquals("TRD-001", trade.tradeId());
        assertEquals("AAPL", trade.symbol());
        assertEquals("BUY", trade.side());
        assertEquals(100, trade.quantity());
    }
}
