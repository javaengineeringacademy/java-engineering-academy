# Trading Platform — Part 3: Tests & Challenges

**[← Part 2: Implementation Guide](README-part2.md)**

---

## Unit Tests

```java
package com.academy.trading;

import com.academy.trading.model.*;
import com.academy.trading.service.TradingService;
import com.academy.trading.orderbook.OrderBook;
import com.academy.trading.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TradingServiceTest {
    private TradingService tradingService;
    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        tradingService = new TradingService();
        orderBook = new OrderBook("AAPL");
    }

    @Test
    void testOrderMatching() {
        Order buyOrder = new Order("user1", createStock("AAPL"), OrderSide.BUY, 
                                  OrderType.LIMIT, 100, new BigDecimal("150.00"));
        Order sellOrder = new Order("user2", createStock("AAPL"), OrderSide.SELL, 
                                   OrderType.LIMIT, 100, new BigDecimal("149.50"));

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Trade> trades = orderBook.matchOrders();
        
        assertEquals(1, trades.size());
        assertEquals(new BigDecimal("149.50"), trades.get(0).getPrice());
    }

    @Test
    void testPortfolioBuy() {
        Portfolio portfolio = new Portfolio("user1", new BigDecimal("100000"));
        Stock stock = createStock("AAPL");
        
        Holding holding = portfolio.buyStock(stock, 10, new BigDecimal("150.00"));
        
        assertEquals(10, holding.getQuantity());
        assertEquals(new BigDecimal("149.99"), portfolio.getCashBalance());
    }

    @Test
    void testPortfolioSell() {
        Portfolio portfolio = new Portfolio("user1", new BigDecimal("100000"));
        Stock stock = createStock("AAPL");
        
        portfolio.buyStock(stock, 10, new BigDecimal("150.00"));
        BigDecimal proceeds = portfolio.sellStock("AAPL", 5, new BigDecimal("155.00"));
        
        assertEquals(new BigDecimal("775.00"), proceeds);
        assertEquals(5, portfolio.getHolding("AAPL").getQuantity());
    }

    @Test
    void testMovingAverageStrategy() {
        TradingStrategy strategy = new MovingAverageStrategy(5, 20);
        Stock stock = createStock("AAPL");
        List<StockQuote> history = createPriceHistory(30);
        
        Signal signal = strategy.analyze(stock, history);
        
        assertNotNull(signal);
    }

    @Test
    void testInsufficientFunds() {
        Portfolio portfolio = new Portfolio("user1", new BigDecimal("1000"));
        Stock stock = createStock("AAPL");
        
        assertThrows(InsufficientFundsException.class, () -> 
            portfolio.buyStock(stock, 100, new BigDecimal("150.00")));
    }

    @Test
    void testOrderCancellation() {
        Order order = new Order("user1", createStock("AAPL"), OrderSide.BUY, 
                               OrderType.LIMIT, 100, new BigDecimal("150.00"));
        orderBook.addOrder(order);
        
        assertTrue(orderBook.cancelOrder(order.getOrderId()));
        assertNull(orderBook.getBidOrders().stream()
            .filter(o -> o.getOrderId().equals(order.getOrderId()))
            .findFirst().orElse(null));
    }
}
```

## Extension Challenges

1. **Options Trading**: Support options contracts and Greeks calculation
2. **Backtesting Engine**: Test trading strategies against historical data
3. **Risk Management**: Implement position limits and stop-loss orders
4. **Market Simulator**: Simulate market conditions for testing
5. **Tax Reporting**: Generate tax reports for capital gains

## Interview Questions

1. **How would you handle concurrent trades on the same stock?**
   - Discuss locking strategies, optimistic locking, order book design

2. **Why use BigDecimal for financial calculations?**
   - Discuss floating-point precision, rounding errors, financial regulations

3. **How would you design for high-frequency trading?**
   - Discuss low-latency design, in-memory processing, network optimization

4. **What are the challenges of implementing a real-time order book?**
   - Discuss data structures, concurrency, price-time priority

5. **How would you ensure compliance with financial regulations?**
   - Discuss audit trails, KYC, suspicious activity monitoring

## References

- [Observer Pattern](https://www.baeldung.com/java-observer-pattern)
- [Concurrent Data Structures](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/concurrent/package-summary.html)
- [BigDecimal for Finance](https://www.baeldung.com/java-bigdecimal)